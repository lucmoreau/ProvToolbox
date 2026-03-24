package org.openprovenance.prov.template.compiler.test;

import org.junit.Test;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;  // explicit: shadows java.lang.Class
import org.openprovenance.prov.template.compiler.past.annotations.MutableFirstParam;
import org.openprovenance.prov.template.compiler.past.annotations.MutableReceiver;
import org.openprovenance.prov.template.compiler.past.annotations.OverrideAnnotation;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.compiler.past.emitter.Rust;

import javax.lang.model.element.Modifier;
import java.util.List;

import static org.junit.Assert.*;
import static org.openprovenance.prov.template.compiler.past.Variable.VariableKind.*;

/**
 * Unit tests for Rust code generation from PAST trees.
 *
 * <p>Each test constructs a minimal PAST {@link Class} tree, feeds it to the {@link Rust}
 * emitter, and asserts that key fragments appear (or do not appear) in the generated source.
 *
 * <p>Test groups:
 * <ol>
 *   <li>Struct layout — field optionality, primitive types, field visibility.</li>
 *   <li>Method name translation — add→push, size→len, isEmpty→is_empty, etc.</li>
 *   <li>HashMap argument treatment — key borrows, owned strings, value unwrap.</li>
 *   <li>Chained-get dispatch — unwrap + SWITCH_TO_GET_MUT / UNWRAP_AND_CHAIN.</li>
 *   <li>Mutation detection — methods containing mutating calls emit {@code &mut self}.</li>
 *   <li>Control flow — if/else, return (implicit last-expression vs. explicit).</li>
 *   <li>Static method calls and constructor calls.</li>
 * </ol>
 */
public class RustEmitterTest {

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /** Emit a class and return the generated Rust source string. */
    private String emit(Class clazz) {
        Rust rust = new Rust();
        rust.discoverClass(clazz);
        return rust.emit(clazz).toString();
    }

    /** Build a public method with the given name, void return, and body statements. */
    private Method publicMethod(String name, Statement... body) {
        Method m = new Method(name).MODIFIERS(Modifier.PUBLIC).RETURNS(ClassName.VOID);
        for (Statement s : body) m.addStatement(s);
        return m;
    }

    /** Build a public method with a return type and body. */
    private Method publicMethod(String name, TypeName returnType, Statement... body) {
        Method m = new Method(name).MODIFIERS(Modifier.PUBLIC).RETURNS(returnType);
        for (Statement s : body) m.addStatement(s);
        return m;
    }

    /** Convenience: local Variable. */
    private Variable local(String name) {
        return new Variable(name, LOCAL_VARIABLE);
    }

    /** Convenience: local Variable with an inferred type. */
    private Variable local(String name, TypeName type) {
        Variable v = new Variable(name, LOCAL_VARIABLE);
        v.inferredType = type;
        return v;
    }

    /** Convenience: field Variable (emits as self.<name>). */
    private Variable field(String name) {
        return new Variable(name, FIELD_VARIABLE);
    }

    // =========================================================================
    // Group 1 — Struct layout
    // =========================================================================

    @Test
    public void simpleClass_emitsStructKeyword() {
        Class clazz = new Class("MyStruct").MODIFIERS(Modifier.PUBLIC);
        String out = emit(clazz);
        assertTrue("Expected 'pub struct MyStruct'", out.contains("pub struct MyStruct"));
    }

    @Test
    public void fieldWithoutInitializer_emittedAsOptionString() {
        // A field with no initialiser must be Option<String> in Rust
        Field f = new Field("myName", ClassName.STRING);   // no initialiser
        Class clazz = new Class("MyStruct").MODIFIERS(Modifier.PUBLIC).FIELDS(f);
        String out = emit(clazz);
        assertTrue("Expected Option<String> for uninitialised field", out.contains("Option<String>"));
        assertFalse("Should not emit bare 'String' when field is Option", out.contains(": String,"));
    }

    @Test
    public void fieldWithInitializer_emittedAsPlainString() {
        // A field with an initialiser is plain String, not Option<String>
        Field f = new Field("myName", ClassName.STRING);
        f.initialiser = new Constant("default");
        Class clazz = new Class("MyStruct").MODIFIERS(Modifier.PUBLIC).FIELDS(f);
        String out = emit(clazz);
        assertTrue("Expected bare String type for initialised field", out.contains(": String,"));
        assertFalse("Should not wrap initialised field in Option", out.contains("Option<String>"));
    }

    @Test
    public void intField_emittedAsI32() {
        Field f = new Field("count", ClassName._int);
        f.initialiser = new Constant(0);   // initialised → no Option wrapping
        Class clazz = new Class("Counter").MODIFIERS(Modifier.PUBLIC).FIELDS(f);
        String out = emit(clazz);
        assertTrue("int should map to i32", out.contains(": i32,"));
    }

    @Test
    public void uninitializedIntField_emittedAsOptionI32() {
        Field f = new Field("count", ClassName._int);  // no initialiser
        Class clazz = new Class("Counter").MODIFIERS(Modifier.PUBLIC).FIELDS(f);
        String out = emit(clazz);
        assertTrue("Uninitialised int field should be Option<i32>", out.contains("Option<i32>"));
    }

    @Test
    public void fieldNameIsSnakedCased() {
        Field f = new Field("myFieldName", ClassName.STRING);
        f.initialiser = new Constant("x");
        Class clazz = new Class("MyStruct").MODIFIERS(Modifier.PUBLIC).FIELDS(f);
        String out = emit(clazz);
        assertTrue("Field name should be snake_cased", out.contains("my_field_name:"));
    }

    @Test
    public void classNameIsPascalCased() {
        Class clazz = new Class("myStruct").MODIFIERS(Modifier.PUBLIC);
        String out = emit(clazz);
        assertTrue("struct name should be PascalCase", out.contains("struct MyStruct"));
    }

    // =========================================================================
    // Group 2 — Method name translation (via OPERATOR_VARIABLE)
    // =========================================================================

    @Test
    public void methodCall_add_translatedToPush() {
        // list.add(item) on a local variable → list.push(item.clone())
        Variable listVar = local("myList");
        Variable itemVar = local("item");
        MethodCall addCall = new MethodCall(listVar, "add", List.of(itemVar));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("doAdd", addCall));
        String out = emit(clazz);
        assertTrue("add should translate to push", out.contains("my_list.push("));
    }

    @Test
    public void methodCall_add_appendsClone() {
        // When the arg has no inferred type → .clone() is appended
        Variable listVar = local("myList");
        Variable itemVar = local("item");  // no inferredType → CLONE will add .clone()
        MethodCall addCall = new MethodCall(listVar, "add", List.of(itemVar));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("doAdd", addCall));
        String out = emit(clazz);
        assertTrue("Non-Copy arg to push should have .clone()", out.contains("item.clone()"));
    }

    @Test
    public void methodCall_add_copyType_noClone() {
        // When the arg is a Copy type (e.g. i32) → no .clone()
        Variable listVar = local("myList");
        Variable intVar  = local("n", ClassName._int);  // i32 is Copy
        MethodCall addCall = new MethodCall(listVar, "add", List.of(intVar));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("doAdd", addCall));
        String out = emit(clazz);
        assertTrue("push should be emitted", out.contains("my_list.push("));
        assertFalse("Copy-type arg to push should NOT have .clone()", out.contains("n.clone()"));
    }

    @Test
    public void methodCall_size_translatedToLen() {
        Variable listVar = local("myList");
        MethodCall sizeCall = new MethodCall(listVar, "size", List.of());
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("getSize", ClassName._int,
                        new Return(sizeCall)));
        String out = emit(clazz);
        assertTrue("size should translate to len", out.contains("my_list.len()"));
    }

    @Test
    public void methodCall_isEmpty_translatedToIsEmpty() {
        Variable listVar = local("myList");
        MethodCall isEmptyCall = new MethodCall(listVar, "isEmpty", List.of());
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("check", ClassName._bool,
                        new Return(isEmptyCall)));
        String out = emit(clazz);
        assertTrue("isEmpty should translate to is_empty", out.contains("my_list.is_empty()"));
    }

    @Test
    public void methodCall_toString_translatedToToString() {
        Variable objVar = local("myObj");
        MethodCall tsCall = new MethodCall(objVar, "toString", List.of());
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("getStr", ClassName.STRING,
                        new Return(tsCall)));
        String out = emit(clazz);
        assertTrue("toString should translate to to_string", out.contains("my_obj.to_string()"));
    }

    // =========================================================================
    // Group 3 — HashMap argument treatment (OPERATOR_VARIABLE)
    // =========================================================================

    @Test
    public void hashMap_containsKey_keyIsBorrowed() {
        // map.containsKey(key) → my_map.contains_key(&key)
        Variable mapVar = local("myMap");
        Variable keyVar = local("key");
        MethodCall call = new MethodCall(mapVar, "containsKey", List.of(keyVar));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("check", ClassName.BOOLEAN,
                        new Return(call)));
        String out = emit(clazz);
        assertTrue("containsKey should be contains_key", out.contains("contains_key("));
        assertTrue("key arg should be borrowed", out.contains("contains_key(&key)"));
    }

    @Test
    public void hashMap_put_firstArgOwnedString() {
        // map.put("myKey", val) → my_map.insert("myKey".to_string(), val)
        Variable mapVar = local("myMap");
        Variable valVar = local("val");
        MethodCall call = new MethodCall(mapVar, "put", List.of(new Constant("myKey"), valVar));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("doInsert", call));
        String out = emit(clazz);
        assertTrue("put should become insert", out.contains("insert("));
        assertTrue("string key should become owned String", out.contains("\"myKey\".to_string()"));
    }

    @Test
    public void hashMap_put_secondArgPassedThrough() {
        // map.put("k", localVar) — local var (not a field access) passed through as-is
        Variable mapVar = local("myMap");
        Variable valVar = local("val");
        MethodCall call = new MethodCall(mapVar, "put", List.of(new Constant("k"), valVar));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("doInsert", call));
        String out = emit(clazz);
        // val is not a field access → no .unwrap()
        assertTrue("value arg should appear as-is", out.contains(", val)"));
    }

    // =========================================================================
    // Group 4 — Chained-get dispatch (OBJECT_METHOD_CALL)
    // =========================================================================

    @Test
    public void chainedGet_get_unwrapsAndAddsCopied() {
        // map.get(k1).get(k2) → my_map.get(&k1).unwrap().get(&k2).copied()
        Variable mapVar = local("myMap");
        Variable k1Var  = local("k1");
        Variable k2Var  = local("k2");
        // Inner: OPERATOR_VARIABLE  map.get(k1)
        MethodCall innerGet = new MethodCall(mapVar, "get", List.of(k1Var));
        // Outer: OBJECT_METHOD_CALL  <innerGet>.get(k2)
        MethodCall outerGet = new MethodCall(innerGet, "get", List.of(k2Var));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("lookup", ClassName.INTEGER,
                        new Return(outerGet)));
        String out = emit(clazz);
        assertTrue("inner .get() should have .unwrap()", out.contains(".unwrap()"));
        assertTrue("outer .get() should add .copied()", out.contains(".copied()"));
        assertTrue("outer key should be borrowed", out.contains("get(&k2)"));
    }

    @Test
    public void chainedGet_put_switchesToGetMut() {
        // map.get(k1).put("k2", val) → my_map.get_mut(&k1).unwrap().insert(…)
        Variable mapVar = local("myMap");
        Variable k1Var  = local("k1");
        Variable valVar = local("val");
        MethodCall innerGet = new MethodCall(mapVar, "get", List.of(k1Var));
        MethodCall chainedPut = new MethodCall(innerGet, "put",
                List.of(new Constant("k2"), valVar));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("doChainedPut", chainedPut));
        String out = emit(clazz);
        assertTrue("get should be rewritten to get_mut", out.contains("get_mut("));
        assertFalse("plain .get( should not remain", out.contains(".get(&k1)"));
        assertTrue("unwrap after get_mut", out.contains(".unwrap()"));
        assertTrue("put should become insert", out.contains("insert("));
    }

    // =========================================================================
    // Group 5 — Mutation detection → &mut self
    // =========================================================================

    @Test
    public void methodWithFieldListAdd_emitsMutSelf() {
        // self.items.add(x) as OPERATOR_VARIABLE on a FIELD_VARIABLE → &mut self
        Variable fieldVar = field("items");
        Variable argVar   = local("x");
        MethodCall addCall = new MethodCall(fieldVar, "add", List.of(argVar));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("appendItem", addCall));
        String out = emit(clazz);
        assertTrue("mutating method should emit &mut self", out.contains("&mut self"));
    }

    @Test
    public void methodWithoutMutation_emitsSharedSelf() {
        // self.items.size() — read-only call → &self (not &mut)
        Variable fieldVar = field("items");
        MethodCall sizeCall = new MethodCall(fieldVar, "size", List.of());
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("getSize", ClassName._int,
                        new Return(sizeCall)));
        String out = emit(clazz);
        assertFalse("non-mutating method should not have &mut self", out.contains("&mut self"));
        assertTrue("non-mutating method should have &self", out.contains("&self"));
    }

    @Test
    public void methodWithLocalListAdd_doesNotEmitMutSelf() {
        // local (not field) variable.add(x) — not a mutation of self's fields → &self
        Variable localList = local("items");  // LOCAL_VARIABLE, not FIELD_VARIABLE
        Variable argVar    = local("x");
        MethodCall addCall = new MethodCall(localList, "add", List.of(argVar));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("appendItem", addCall));
        String out = emit(clazz);
        assertFalse("add on local var should not mark method as &mut self",
                out.contains("&mut self"));
    }

    // =========================================================================
    // Group 6 — Control flow
    // =========================================================================

    @Test
    public void ifStatement_emitsIfBlock() {
        Variable condVar = local("flag");
        Variable xVar    = local("x");
        MethodCall addCall = new MethodCall(field("items"), "add", List.of(xVar));
        IfStatement ifStmt = IfStatement.IF(condVar).THEN(addCall);
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("maybeAdd", ifStmt));
        String out = emit(clazz);
        assertTrue("Should emit 'if flag {'", out.contains("if flag {"));
        assertTrue("Should emit push inside if body", out.contains("push("));
    }

    @Test
    public void ifStatement_withElse_emitsElseBlock() {
        Variable condVar = local("flag");
        IfStatement ifStmt = IfStatement.IF(condVar)
                .THEN(new Return(new Constant(1)))
                .ELSE(new Return(new Constant(0)));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("choose", ClassName._int, ifStmt));
        String out = emit(clazz);
        assertTrue("Should emit 'if flag {'", out.contains("if flag {"));
        assertTrue("Should emit '} else {'", out.contains("} else {"));
    }

    @Test
    public void lastReturnStatement_emittedAsImplicitReturn() {
        // In Rust, the last expression has no semicolon → no 'return' keyword
        Variable v = local("result");
        Method m = new Method("compute")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(ClassName._int)
                .BODY(new Return(v));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC).METHODS(m);
        String out = emit(clazz);
        // Last statement → no 'return' keyword, no semicolon for the expression itself
        assertFalse("Last return should not emit 'return' keyword", out.contains("return result"));
        assertTrue("Should contain the expression", out.contains("result"));
    }

    @Test
    public void nonLastReturnStatement_emittedWithReturnKeyword() {
        // When return is NOT the last statement, emit explicit 'return'
        Variable flag = local("flag");
        Variable result = local("result");
        IfStatement earlyReturn = IfStatement.IF(flag).THEN(new Return(new Constant(0)));
        Method m = new Method("compute")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(ClassName._int)
                .BODY(earlyReturn, new Return(result));   // 2 statements; early return is non-last
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC).METHODS(m);
        String out = emit(clazz);
        // The return inside the if body is non-last → explicit 'return'
        assertTrue("Early return should emit 'return' keyword", out.contains("return 0"));
    }

    // =========================================================================
    // Group 7 — Static calls and constructors
    // =========================================================================

    @Test
    public void staticMethodCall_emitsDoubleColon() {
        // HashMap::new() from a STATIC_METHOD_CALL node
        MethodCall staticNew = new MethodCall(ClassName.HASHMAP, "new", List.of());
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("makeMap", ParameterizedType.get(ClassName.HASHMAP, ClassName.STRING, ClassName.STRING),
                        new Return(staticNew)));
        String out = emit(clazz);
        assertTrue("Static call should use :: syntax", out.contains("HashMap::new()"));
    }

    @Test
    public void constructorCall_emitsColonColonNew() {
        // new MyClass(arg) → MyClass::new(arg)
        TypeName myType = ClassName.get("Payload", "com.example");
        Variable argVar = local("data");
        MethodCall ctor = MethodCall.CONSTRUCTOR_CALL(myType, List.of(argVar));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("build", myType,
                        new Return(ctor)));
        String out = emit(clazz);
        assertTrue("Constructor call should emit ::new()", out.contains("Payload::new("));
    }

    // =========================================================================
    // Group 8 — Variable conversion
    // =========================================================================

    @Test
    public void fieldVariable_emitsWithSelfPrefix() {
        // A FIELD_VARIABLE named "myField" should appear as self.my_field
        Variable fieldVar = field("myField");
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("get", ClassName.STRING,
                        new Return(fieldVar)));
        String out = emit(clazz);
        assertTrue("Field variable should emit as self.my_field", out.contains("self.my_field"));
    }

    @Test
    public void localVariable_emitsWithoutSelfPrefix() {
        Variable localVar = local("myLocal");
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("get", ClassName.STRING,
                        new Return(localVar)));
        String out = emit(clazz);
        assertTrue("Local variable should appear as my_local", out.contains("my_local"));
        assertFalse("Local variable should not have self. prefix", out.contains("self.my_local"));
    }

    // =========================================================================
    // Group 9 — Iterator (for-each)
    // =========================================================================

    @Test
    public void iterator_emitsForLoop() {
        // for (Item item : items) { ... } → for item in items { ... }
        Parameter param = new Parameter("item", ClassName.get("Item", "com.example"));
        Variable collection = local("items");
        Iterator iter = new Iterator(param, collection)
                .BODY(new Comment("// body"));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("process", iter));
        String out = emit(clazz);
        assertTrue("Should emit 'for item in '", out.contains("for item in "));
    }

    @Test
    public void iterator_overLocalFieldAccess_addsIterCloned() {
        // for-each over bean.myItems where bean is a local variable → .iter().cloned()
        // This pattern arises when iterating over a field of a borrowed local struct.
        Parameter param = new Parameter("item", ClassName.STRING);
        // OBJECT_ACCESSOR: new MethodCall(Variable, accessorName) → local.field form
        Variable localBean = local("bean");
        MethodCall fieldAccess = new MethodCall(localBean, "myItems");  // OBJECT_ACCESSOR
        Iterator iter = new Iterator(param, fieldAccess)
                .BODY(new Comment("// body"));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("process", iter));
        String out = emit(clazz);
        assertTrue("Local field access iterator should use .iter().cloned()",
                out.contains(".iter().cloned()"));
    }

    @Test
    public void iterator_overSelfField_doesNotAddIterCloned() {
        // for-each over self.myItems (FIELD_VARIABLE) — the emitter does NOT add .iter().cloned()
        // because it uses the FIELD_VARIABLE convert path, not the OBJECT_ACCESSOR path.
        Parameter param = new Parameter("item", ClassName.STRING);
        Variable fieldCollection = field("myItems");  // FIELD_VARIABLE → self.my_items
        Iterator iter = new Iterator(param, fieldCollection)
                .BODY(new Comment("// body"));
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("process", iter));
        String out = emit(clazz);
        assertTrue("for self-field iterator should iterate over self.my_items",
                out.contains("for item in self.my_items"));
    }

    // =========================================================================
    // Group 10 — OBJECT_METHOD_CALL: camelCase method names are snake_cased
    // =========================================================================

    @Test
    public void objectMethodCall_camelCaseName_snakeCased() {
        // someExpr.addElements(arg) via OBJECT_METHOD_CALL → .add_elements(arg)
        // Regression for: addElements was emitted verbatim (E0599) because toSnakeCase
        // was applied only in the OPERATOR_VARIABLE path, not OBJECT_METHOD_CALL.
        Variable receiver = local("myObj");
        MethodCall innerCall = new MethodCall(receiver, "getItems", List.of());  // OPERATOR_VARIABLE
        MethodCall outerCall = new MethodCall(innerCall, "addElements", List.of(local("item")));  // OBJECT_METHOD_CALL
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("doAdd", outerCall));
        String out = emit(clazz);
        assertTrue("camelCase OBJECT_METHOD_CALL name should be snake_cased",
                out.contains("add_elements("));
        assertFalse("camelCase should not remain in output", out.contains("addElements("));
    }

    @Test
    public void objectMethodCall_chainedGetEarlyReturn_methodNameSnakeCased() {
        // map.get(k).addItem(v) via chained-get early-return path → .add_item(v)
        Variable mapVar = local("myMap");
        Variable keyVar = local("k");
        Variable valVar = local("v");
        MethodCall innerGet = new MethodCall(mapVar, "get", List.of(keyVar));    // OPERATOR_VARIABLE
        MethodCall chainedCall = new MethodCall(innerGet, "addItem", List.of(valVar));  // OBJECT_METHOD_CALL chained-get path
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("doChain", chainedCall));
        String out = emit(clazz);
        assertTrue("chained-get method name should be snake_cased", out.contains("add_item("));
        assertFalse("camelCase should not remain", out.contains("addItem("));
    }

    // =========================================================================
    // Group 11 — Vec index access: __elements.get(i) → [i as usize].clone()
    // =========================================================================

    @Test
    public void vecGet_onElements_emitsIndexAccess() {
        // self.__elements.get(count) → self.elements[count as usize].clone()
        // Regression for: generated Vec::get() which returns Option<&T>, causing type mismatch (E0308).
        Variable thisVar = new Variable("this", LOCAL_VARIABLE);
        MethodCall elementsAccess = new MethodCall(thisVar, "__elements");  // OBJECT_ACCESSOR, name = "__elements"
        MethodCall getCall = new MethodCall(elementsAccess, "get", List.of(local("count")));  // OBJECT_METHOD_CALL
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("fetch", new Return(getCall)));
        String out = emit(clazz);
        assertTrue("Vec::get should become index access",
                out.contains("elements[count as usize].clone()"));
        assertFalse("Vec::get should not remain as .get(count)",
                out.contains(".get(count)"));
    }

    @Test
    public void vecGet_onElements_noSpuriousUnwrap_onFieldAccess() {
        // Accessing a field on the Vec index result must NOT add .unwrap():
        // self.__elements.get(idx).myField → elements[idx as usize].clone().my_field
        // Regression for: OBJECT_ACCESSOR case added .unwrap() for any mc2.methodName == "get",
        // but Vec index returns T (not Option<T>) so no .unwrap() is needed.
        Variable thisVar = new Variable("this", LOCAL_VARIABLE);
        MethodCall elementsAccess = new MethodCall(thisVar, "__elements");
        MethodCall getCall = new MethodCall(elementsAccess, "get", List.of(local("idx")));
        // Field access on the Vec index result
        MethodCall fieldOnGet = new MethodCall((Expression) getCall, "myField");  // OBJECT_ACCESSOR
        Class clazz = new Class("Foo").MODIFIERS(Modifier.PUBLIC)
                .METHODS(publicMethod("getField", new Return(fieldOnGet)));
        String out = emit(clazz);
        assertTrue("Field access on Vec index should use .clone().my_field",
                out.contains("elements[idx as usize].clone().my_field"));
        assertFalse("No .unwrap() should appear for Vec index result",
                out.contains(".clone().unwrap()"));
    }

    // =========================================================================
    // Group 12 — Self Option field passed as by-value argument
    // =========================================================================

    @Test
    public void selfOptionField_passedByValue_cloneUnwrap() {
        // this.myBean (Option<MyBean>) as a PASS_BY_VALUE argument → self.my_bean.clone().unwrap()
        // Regression for: add_elements(self.bean__x) failed with E0308 because
        // Option<T> was passed where T was expected.
        Field optField = new Field("myBean", ClassName.get("MyBean", "com.example"));  // no initialiser → Option<T>
        Variable thisVar = new Variable("this", LOCAL_VARIABLE);
        MethodCall fieldAccess = new MethodCall(thisVar, "myBean");  // OBJECT_ACCESSOR for Option field
        Variable receiver = local("someList");
        MethodCall call = new MethodCall(receiver, "addItem", List.of(fieldAccess));  // OPERATOR_VARIABLE
        Class clazz = new Class("Merger").MODIFIERS(Modifier.PUBLIC)
                .FIELDS(optField)
                .METHODS(publicMethod("doAdd", call));
        String out = emit(clazz);
        assertTrue("Self Option field passed by value should be .clone().unwrap()",
                out.contains("my_bean.clone().unwrap()"));
    }

    @Test
    public void selfInitialisedField_passedByValue_noUnwrap() {
        // A field WITH an initialiser is plain T (not Option<T>) — no .clone().unwrap() needed.
        Field initField = new Field("myBean", ClassName.get("MyBean", "com.example"));
        initField.initialiser = new Constant("default");  // has initialiser → plain T
        Variable thisVar = new Variable("this", LOCAL_VARIABLE);
        MethodCall fieldAccess = new MethodCall(thisVar, "myBean");
        Variable receiver = local("someList");
        MethodCall call = new MethodCall(receiver, "addItem", List.of(fieldAccess));
        Class clazz = new Class("Merger").MODIFIERS(Modifier.PUBLIC)
                .FIELDS(initField)
                .METHODS(publicMethod("doAdd", call));
        String out = emit(clazz);
        assertFalse("Initialised field passed by value should not get .clone().unwrap()",
                out.contains("my_bean.clone().unwrap()"));
    }

    // =========================================================================
    // Group 13 — MutableReceiver annotation
    // =========================================================================

    @Test
    public void mutableReceiver_emitsMutSelf() {
        // A method annotated with MutableReceiver should emit &mut self.
        TypeName beanType = ClassName.get("MyBean", "com.example");
        Method m = new Method("processBean")
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(MutableReceiver.NAME)
                .RETURNS(beanType);
        Class clazz = new Class("Merger").MODIFIERS(Modifier.PUBLIC).METHODS(m);
        String out = emit(clazz);
        assertTrue("MutableReceiver annotation should emit &mut self", out.contains("&mut self"));
    }

    @Test
    public void mutableReceiver_returnSelf_appendsClone() {
        // return this/self in a MutableReceiver method → self.clone() (not just self)
        // Regression for: &mut self methods return Self by value — returning a reference
        // would give &BeanMerger instead of BeanMerger (E0308).
        TypeName mergerType = ClassName.get("Merger", "com.example");
        Variable thisVar = new Variable("this", LOCAL_VARIABLE);
        Method m = new Method("merge")
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(MutableReceiver.NAME)
                .RETURNS(mergerType)
                .BODY(new Return(thisVar));
        Class clazz = new Class("Merger").MODIFIERS(Modifier.PUBLIC).METHODS(m);
        String out = emit(clazz);
        assertTrue("Returning self in MutableReceiver method should emit self.clone()",
                out.contains("self.clone()"));
    }

    @Test
    public void mutableReceiver_returnOptionField_appendsCloneUnwrap() {
        // return this.myBean (Option<T>) in a MutableReceiver method → self.my_bean.clone().unwrap()
        // Regression for: returning a raw Option field gave Option<T> when T was expected.
        TypeName beanType = ClassName.get("MyBean", "com.example");
        Field optField = new Field("myBean", beanType);  // no initialiser → Option<MyBean>
        Variable thisVar = new Variable("this", LOCAL_VARIABLE);
        MethodCall fieldReturn = new MethodCall(thisVar, "myBean");  // OBJECT_ACCESSOR
        Method m = new Method("getBean")
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(MutableReceiver.NAME)
                .RETURNS(beanType)
                .BODY(new Return(fieldReturn));
        Class clazz = new Class("Merger").MODIFIERS(Modifier.PUBLIC)
                .FIELDS(optField)
                .METHODS(m);
        String out = emit(clazz);
        assertTrue("MutableReceiver return of Option field should emit .clone().unwrap()",
                out.contains("my_bean.clone().unwrap()"));
    }

    @Test
    public void mutableReceiver_assignToOptionSubField_asMutUnwrap() {
        // LHS: this.myBean.time = value  →  self.my_bean.as_mut().unwrap().time = ...
        // Regression for: self.bean__x.time = ... failed (E0609) because intermediate
        // field is Option<T> and field access on Option<T> is not allowed directly.
        TypeName beanType = ClassName.get("MyBean", "com.example");
        Field optField = new Field("myBean", beanType);  // no initialiser → Option<MyBean>
        Variable thisVar = new Variable("this", LOCAL_VARIABLE);
        MethodCall fieldAccess = new MethodCall(thisVar, "myBean");   // OBJECT_ACCESSOR: this.myBean
        MethodCall subField    = new MethodCall(fieldAccess, "time"); // OBJECT_ACCESSOR: .time
        Variable inputTime = local("inputTime");
        Assignment assignment = new Assignment(subField, inputTime);
        Class clazz = new Class("Merger").MODIFIERS(Modifier.PUBLIC)
                .FIELDS(optField)
                .METHODS(publicMethod("setTime", assignment));
        String out = emit(clazz);
        assertTrue("LHS 2-level Option chain should insert .as_mut().unwrap()",
                out.contains("my_bean.as_mut().unwrap().time"));
    }

    // =========================================================================
    // Group 14 — MutableFirstParam annotation
    // =========================================================================
    //
    // These tests cover the &'a mut T pattern introduced for the BeanMerger:
    //   - trait definition signatures get <'a>, &'a mut T for the first param and return
    //   - trait impl signatures agree with the trait (same lifetime / mutability)
    //   - field reads from &T parameters (params 1..n) get .clone() appended
    //   - call sites on self add &mut to arg 0 and & to args 1+
    //
    // The "two code-path" structure (emitTraitMethod for trait defs, emitMethod(m, true)
    // for trait impls) means both paths must be exercised independently.
    //
    // Setup shared between most tests in this group:
    //   beanType  = FileInitBean   (first param / return type)
    //   inputType = FileInitInputs (second param — a read-only borrow)
    //   method    = processBean(bean, inputBean) annotated with MutableFirstParam
    // -------------------------------------------------------------------------

    /** Helper: build a minimal interface (trait) class with one MutableFirstParam method. */
    private Class buildMfpTrait() {
        TypeName beanType  = ClassName.get("FileInitBean",   "com.example");
        TypeName inputType = ClassName.get("FileInitInputs", "com.example");
        Method m = new Method("processBean")
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .ANNOTATIONS(MutableFirstParam.NAME)
                .PARAMETER(beanType,  "bean")
                .PARAMETER(inputType, "inputBean")
                .RETURNS(beanType);
        Class traitClass = new Class("BeanMergerInterface", true)
                .MODIFIERS(Modifier.PUBLIC)
                .METHODS(m);
        return traitClass;
    }

    /** Helper: build a minimal impl class with one MutableFirstParam method (inTrait path). */
    private Class buildMfpImpl() {
        TypeName beanType  = ClassName.get("FileInitBean",   "com.example");
        TypeName inputType = ClassName.get("FileInitInputs", "com.example");
        Method m = new Method("processBean")
                .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                .ANNOTATIONS(OverrideAnnotation.NAME, MutableFirstParam.NAME)
                .PARAMETER(beanType,  "bean")
                .PARAMETER(inputType, "inputBean")
                .RETURNS(beanType);
        return new Class("BeanMerger").MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(ClassName.get("BeanMergerInterface", "com.example"))
                .METHODS(m);
    }

    // ---- trait definition (emitTraitMethod path) ----------------------------

    @Test
    public void mutableFirstParam_traitDef_hasLifetimeParam() {
        // MutableFirstParam on a trait method → <'a> lifetime must appear on the signature.
        // Without the lifetime, &'a mut T in the param and -> &'a mut T in the return
        // would be an undeclared lifetime (E0261).
        String out = emit(buildMfpTrait());
        assertTrue("Trait method with MutableFirstParam should declare <'a>",
                out.contains("<'a>"));
    }

    @Test
    public void mutableFirstParam_traitDef_firstParamIsMutableBorrow() {
        // The first non-self parameter must be &'a mut T.
        // Without this, writing bean.field = ... fails with E0594.
        String out = emit(buildMfpTrait());
        assertTrue("First param in trait def should be &'a mut FileInitBean",
                out.contains("bean: &'a mut FileInitBean"));
    }

    @Test
    public void mutableFirstParam_traitDef_subsequentParamIsSharedBorrow() {
        // Params 1..n must remain &T (read-only borrow) — only the first param is mutable.
        String out = emit(buildMfpTrait());
        assertTrue("Subsequent param in trait def should be &FileInitInputs",
                out.contains("input_bean: &FileInitInputs"));
        assertFalse("Subsequent param must NOT be &'a mut",
                out.contains("input_bean: &'a mut"));
    }

    @Test
    public void mutableFirstParam_traitDef_returnTypeIsMutableBorrow() {
        // Return type must be &'a mut T to tie the returned reference to the input
        // bean's lifetime (no clone needed, E0308 is avoided).
        String out = emit(buildMfpTrait());
        assertTrue("Return type in trait def should be &'a mut FileInitBean",
                out.contains("-> &'a mut FileInitBean"));
    }

    // ---- trait implementation (emitMethod(m, inTrait=true) path) ------------

    @Test
    public void mutableFirstParam_impl_hasLifetimeParam() {
        // The impl-side signature must also carry <'a> so it matches the trait declaration
        // (E0053 if the two signatures differ).
        String out = emit(buildMfpImpl());
        assertTrue("Impl method with MutableFirstParam should declare <'a>",
                out.contains("<'a>"));
    }

    @Test
    public void mutableFirstParam_impl_firstParamIsMutableBorrow() {
        // The impl-side first param must be &'a mut T to match the trait signature.
        String out = emit(buildMfpImpl());
        assertTrue("First param in impl should be &'a mut FileInitBean",
                out.contains("bean: &'a mut FileInitBean"));
    }

    @Test
    public void mutableFirstParam_impl_subsequentParamIsSharedBorrow() {
        // Params 1..n are emitted via convertTypeToRustTraitParam → &T in impl too.
        String out = emit(buildMfpImpl());
        assertTrue("Subsequent param in impl should be &FileInitInputs",
                out.contains("input_bean: &FileInitInputs"));
        assertFalse("Subsequent param in impl must NOT be &'a mut",
                out.contains("input_bean: &'a mut"));
    }

    @Test
    public void mutableFirstParam_impl_returnTypeIsMutableBorrow() {
        // Impl-side return type must agree with the trait: &'a mut T.
        String out = emit(buildMfpImpl());
        assertTrue("Return type in impl should be &'a mut FileInitBean",
                out.contains("-> &'a mut FileInitBean"));
    }

    // ---- field reads from &T reference params get .clone() ------------------

    @Test
    public void mutableFirstParam_fieldReadFromRefParam_appendsClone() {
        // Within a MutableFirstParam impl method, params 1..n are &T references.
        // Reading a field through a &T reference for a non-Copy type requires .clone()
        // to avoid E0507 ("cannot move out of a shared reference").
        //
        // body:  myBean.time = inputRef.time   →   my_bean.time = input_ref.time.clone()
        //
        // Note: variable names are chosen so "my_bean.time.clone()" is NOT a substring
        // of "input_ref.time.clone()" (avoids false assertion failure from prefix overlap).
        TypeName beanType  = ClassName.get("FileInitBean",   "com.example");
        TypeName inputType = ClassName.get("FileInitInputs", "com.example");

        // First param (mutable, &'a mut T): "myBean"  → snake_case: "my_bean"
        // Second param (read-only, &T):    "inputRef" → snake_case: "input_ref"
        Variable myBeanVar   = local("myBean");
        Variable inputRefVar = local("inputRef");
        MethodCall myBeanTime  = new MethodCall(myBeanVar,   "time");  // OBJECT_ACCESSOR (LHS — &mut T field write)
        MethodCall inputRefTime = new MethodCall(inputRefVar, "time"); // OBJECT_ACCESSOR (RHS — &T field read)
        Assignment assign = new Assignment(myBeanTime, inputRefTime);
        Return ret = new Return(myBeanVar);

        Method m = new Method("processBean")
                .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                .ANNOTATIONS(OverrideAnnotation.NAME, MutableFirstParam.NAME)
                .PARAMETER(beanType,  "myBean")
                .PARAMETER(inputType, "inputRef")
                .RETURNS(beanType)
                .BODY(assign, ret);

        Class implClass = new Class("BeanMerger").MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(ClassName.get("BeanMergerInterface", "com.example"))
                .METHODS(m);

        String out = emit(implClass);
        assertTrue("Field read from &T ref param should have .clone()",
                out.contains("input_ref.time.clone()"));
        assertFalse("First param field (my_bean.time) must NOT get .clone() when read via &mut T",
                out.contains("my_bean.time.clone()"));
    }

    // ---- call-site &mut / & argument injection ------------------------------

    @Test
    public void mutableFirstParam_callSite_firstArgGetsMutRef_secondArgGetsRef() {
        // When calling a MutableFirstParam method on self, the emitter must add:
        //   &mut  to argument 0  (the bean being updated)
        //   &     to arguments 1+ (the read-only input/output)
        //
        // Regression for: E0308 "expected FileTransformingInputs1, found &FileTransformingInputs1"
        // when the composite-loop body called self.process_*(bean, composee) without the
        // correct reference decorators.
        TypeName beanType  = ClassName.get("FileInitBean",   "com.example");
        TypeName inputType = ClassName.get("FileInitInputs", "com.example");

        // The MutableFirstParam method that will be called.
        Method processMethod = new Method("processBean")
                .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                .ANNOTATIONS(OverrideAnnotation.NAME, MutableFirstParam.NAME)
                .PARAMETER(beanType,  "bean")
                .PARAMETER(inputType, "inputBean")
                .RETURNS(beanType)
                .BODY(new Return(local("bean")));

        // A regular outer method that calls this.processBean(beanArg, inputArg).
        Variable thisVar   = new Variable("this",     LOCAL_VARIABLE);
        Variable beanArg   = local("beanArg");
        Variable inputArg  = local("inputArg");
        MethodCall callSite = new MethodCall(thisVar, "processBean", List.of(beanArg, inputArg));
        Method outerMethod = publicMethod("callProcess", callSite);

        Class implClass = new Class("BeanMerger").MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(ClassName.get("BeanMergerInterface", "com.example"))
                .METHODS(processMethod, outerMethod);

        String out = emit(implClass);
        assertTrue("First arg to MutableFirstParam method should be &mut",
                out.contains("&mut bean_arg"));
        assertTrue("Second arg to MutableFirstParam method should be &",
                out.contains("&input_arg"));
    }
}
