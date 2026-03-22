package org.openprovenance.prov.template.compiler.test;

import org.junit.Test;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;  // explicit: shadows java.lang.Class
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
}
