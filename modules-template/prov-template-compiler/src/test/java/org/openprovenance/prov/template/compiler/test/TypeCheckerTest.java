package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.checker.*;
import org.openprovenance.prov.template.compiler.past.type.ArrayType;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import javax.lang.model.element.Modifier;
import java.util.List;

import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class TypeCheckerTest extends TestCase {

    // ===== TypeCompatibility tests =====

    public void testSameTypeAssignable() {
        TypeRegistry registry = new TypeRegistry();
        assertTrue(TypeCompatibility.isAssignable(STRING, STRING, registry));
        assertTrue(TypeCompatibility.isAssignable(INTEGER, INTEGER, registry));
    }

    public void testObjectAcceptsAnything() {
        TypeRegistry registry = new TypeRegistry();
        assertTrue(TypeCompatibility.isAssignable(ClassName.OBJECT, STRING, registry));
        assertTrue(TypeCompatibility.isAssignable(ClassName.OBJECT, INTEGER, registry));
        assertTrue(TypeCompatibility.isAssignable(ClassName.OBJECT, ClassName.BOOLEAN, registry));
    }

    public void testNumericWidening() {
        TypeRegistry registry = new TypeRegistry();
        // int → Integer → Long → Float → Double
        assertTrue(TypeCompatibility.isAssignable(INTEGER, ClassName._int, registry));
        assertTrue(TypeCompatibility.isAssignable(ClassName.LONG, INTEGER, registry));
        assertTrue(TypeCompatibility.isAssignable(ClassName.DOUBLE, ClassName.FLOAT, registry));
        // No narrowing
        assertFalse(TypeCompatibility.isAssignable(ClassName._int, ClassName.DOUBLE, registry));
    }

    public void testCollectionSubtyping() {
        TypeRegistry registry = new TypeRegistry();
        assertTrue(TypeCompatibility.isAssignable(ClassName.LIST, ClassName.ARRAY_LIST, registry));
        assertTrue(TypeCompatibility.isAssignable(ClassName.LIST, ClassName.LINKED_LIST, registry));
        assertTrue(TypeCompatibility.isAssignable(ClassName.MAP, ClassName.HASHMAP, registry));
        assertTrue(TypeCompatibility.isAssignable(ClassName.SET, ClassName.HASH_SET, registry));
        assertTrue(TypeCompatibility.isAssignable(ClassName.COLLECTION, ClassName.LIST, registry));
        // Not assignable the other way
        assertFalse(TypeCompatibility.isAssignable(ClassName.ARRAY_LIST, ClassName.LIST, registry));
    }

    public void testParameterizedTypeAssignability() {
        TypeRegistry registry = new TypeRegistry();
        ParameterizedType listString = ParameterizedType.get(ClassName.LIST, STRING);
        ParameterizedType arrayListString = ParameterizedType.get(ClassName.ARRAY_LIST, STRING);
        assertTrue(TypeCompatibility.isAssignable(listString, arrayListString, registry));

        ParameterizedType listInteger = ParameterizedType.get(ClassName.LIST, INTEGER);
        assertFalse(TypeCompatibility.isAssignable(listString, listInteger, registry));
    }

    public void testArrayAssignability() {
        TypeRegistry registry = new TypeRegistry();
        ArrayType stringArray = ArrayType.of(STRING);
        ArrayType stringArray2 = ArrayType.of(STRING);
        assertTrue(TypeCompatibility.isAssignable(stringArray, stringArray2, registry));

        ArrayType objectArray = ArrayType.of(ClassName.OBJECT);
        assertTrue(TypeCompatibility.isAssignable(objectArray, stringArray, registry));
    }

    public void testTypeVariableAssignable() {
        TypeRegistry registry = new TypeRegistry();
        TypeVariable t = TypeVariable.get("T");
        assertTrue(TypeCompatibility.isAssignable(t, STRING, registry));
        assertTrue(TypeCompatibility.isAssignable(STRING, t, registry));
    }

    public void testNumericPromotion() {
        TypeName promoted = TypeCompatibility.numericPromotion(ClassName._int, ClassName.DOUBLE);
        assertTrue(promoted instanceof ClassName);
        assertEquals("Double", ((ClassName) promoted).simpleName);
    }

    public void testCommonSupertype() {
        TypeRegistry registry = new TypeRegistry();
        TypeName common = TypeCompatibility.commonSupertype(STRING, STRING, registry);
        assertTrue(common instanceof ClassName);
        assertEquals("String", ((ClassName) common).simpleName);

        TypeName numCommon = TypeCompatibility.commonSupertype(ClassName._int, ClassName.DOUBLE, registry);
        assertTrue(numCommon instanceof ClassName);
        assertEquals("Double", ((ClassName) numCommon).simpleName);
    }

    // ===== TypeEnvironment tests =====

    public void testEnvironmentDefineAndLookup() {
        TypeEnvironment env = new TypeEnvironment();
        env.define("x", STRING);
        assertEquals(STRING, env.lookup("x"));
        assertNull(env.lookup("y"));
    }

    public void testEnvironmentScoping() {
        TypeEnvironment parent = new TypeEnvironment();
        parent.define("x", STRING);

        TypeEnvironment child = parent.pushScope();
        child.define("y", INTEGER);

        // Child sees both
        assertEquals(STRING, child.lookup("x"));
        assertEquals(INTEGER, child.lookup("y"));

        // Parent doesn't see child's variable
        assertNull(parent.lookup("y"));
    }

    public void testEnvironmentFinalTracking() {
        TypeEnvironment env = new TypeEnvironment();
        env.define("x", STRING, java.util.EnumSet.of(Modifier.FINAL));
        assertTrue(env.isFinal("x"));
        assertFalse(env.isFinal("y"));

        env.define("y", INTEGER);
        assertFalse(env.isFinal("y"));
    }

    public void testEnvironmentStaticContext() {
        TypeEnvironment env = new TypeEnvironment();
        assertFalse(env.isInStaticContext());

        env.setInStaticContext(true);
        assertTrue(env.isInStaticContext());

        TypeEnvironment child = env.pushScope();
        assertTrue(child.isInStaticContext());
    }

    // ===== TypeRegistry tests =====

    public void testRegisterAndLookupClass() {
        TypeRegistry registry = new TypeRegistry();
        Class pastClass = new Class("MyClass");
        pastClass.fields.add(FIELD("name", STRING));
        pastClass.methods.add(METHOD("getName").RETURNS(STRING));

        registry.registerClass(pastClass, "com.example");

        ClassSignature sig = registry.lookup(ClassName.get("MyClass", "com.example"));
        assertNotNull(sig);
        assertEquals("MyClass", sig.name);
        assertEquals("com.example", sig.packageName);
        assertEquals(1, sig.fields.size());
        assertEquals(1, sig.methods.size());
    }

    public void testLookupMethod() {
        TypeRegistry registry = new TypeRegistry();
        Class pastClass = new Class("MyClass");
        pastClass.methods.add(METHOD("greet").RETURNS(STRING).PARAMETERS(PARAMETER("name", STRING)));
        registry.registerClass(pastClass, "com.example");

        MethodSignature sig = registry.lookupMethod(ClassName.get("MyClass", "com.example"), "greet", 1);
        assertNotNull(sig);
        assertEquals("greet", sig.name);
        assertEquals(STRING, sig.returnType);
    }

    public void testExternalTypeRegistration() {
        ExternalTypeRegistry external = new ExternalTypeRegistry();
        external.forClass(ClassName.PROV_FACTORY)
                .method("newQualifiedName", ClassName.PROV_QUALIFIED_NAME,
                        STRING, STRING, STRING)
                .constructor()
                .register();

        assertNotNull(external.lookupMethod(ClassName.PROV_FACTORY, "newQualifiedName", 3));
        assertNotNull(external.lookupConstructor(ClassName.PROV_FACTORY, 0));
        assertTrue(external.isKnown(ClassName.PROV_FACTORY));
    }

    // ===== TypeChecker / TypeInferrer integration tests =====

    public void testReturnTypeMismatch() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("Foo");
        pastClass.methods.add(
                METHOD("getNumber").RETURNS(INTEGER)
                        .BODY(RETURN(CONSTANT("hello")))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        boolean foundReturnError = false;
        for (TypeDiagnostic d : diagnostics) {
            if (d.message.contains("Incompatible return type")) {
                foundReturnError = true;
                break;
            }
        }
        assertTrue("Expected return type mismatch diagnostic", foundReturnError);
    }

    public void testDefinitionTypeMismatch() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("Foo");
        pastClass.methods.add(
                METHOD("doSomething").RETURNS(ClassName.VOID)
                        .BODY(DEFINITION(INTEGER, VARIABLE("x"), CONSTANT("hello")))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        boolean foundDefError = false;
        for (TypeDiagnostic d : diagnostics) {
            if (d.message.contains("Incompatible types") && d.message.contains("variable")) {
                foundDefError = true;
                break;
            }
        }
        assertTrue("Expected definition type mismatch diagnostic", foundDefError);
    }

    public void testUndefinedVariableWarning() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("Foo");
        pastClass.methods.add(
                METHOD("doSomething").RETURNS(ClassName.VOID)
                        .BODY(RETURN(VARIABLE("undeclared")))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        boolean foundUndefined = false;
        for (TypeDiagnostic d : diagnostics) {
            if (d.message.contains("Undefined variable") && d.message.contains("undeclared")) {
                foundUndefined = true;
                break;
            }
        }
        assertTrue("Expected undefined variable warning", foundUndefined);
    }

    public void testWellTypedClassNoDiagnostics() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("Greeter");
        pastClass.fields.add(FIELD("greeting", STRING));
        pastClass.methods.add(
                METHOD("greet").RETURNS(STRING)
                        .PARAMETERS(PARAMETER("name", STRING))
                        .BODY(RETURN(VARIABLE("greeting")))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        // Filter only ERROR-level diagnostics
        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
            }
        }
        assertEquals("Well-typed class should have no errors", 0, errors);
    }

    public void testBinaryOpInference() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("Math");
        pastClass.methods.add(
                METHOD("add").RETURNS(INTEGER)
                        .PARAMETERS(PARAMETER("a", INTEGER), PARAMETER("b", INTEGER))
                        .BODY(RETURN(BINARY_OP(VARIABLE("a"), "+", VARIABLE("b"))))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
            }
        }
        assertEquals("Numeric addition should type-check cleanly", 0, errors);
    }

    public void testStringConcatenation() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("Concat");
        pastClass.methods.add(
                METHOD("concat").RETURNS(STRING)
                        .PARAMETERS(PARAMETER("a", STRING), PARAMETER("b", STRING))
                        .BODY(RETURN(BINARY_OP(VARIABLE("a"), "+", VARIABLE("b"))))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
            }
        }
        assertEquals("String concatenation should type-check cleanly", 0, errors);
    }

    public void testMethodCallInference() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("Caller");
        pastClass.methods.add(
                METHOD("getName").RETURNS(STRING)
                        .BODY(RETURN(CONSTANT("hello")))
        );
        pastClass.methods.add(
                METHOD("useName").RETURNS(STRING)
                        .BODY(RETURN(METHOD_CALL("getName", List.of())))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
            }
        }
        assertEquals("Intra-class method call should type-check cleanly", 0, errors);
    }

    public void testConstructorCallInference() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("Factory");
        pastClass.methods.add(
                METHOD("create").RETURNS(ClassName.ARRAY_LIST)
                        .BODY(RETURN(CONSTRUCTOR_CALL(
                                ParameterizedType.get(ClassName.ARRAY_LIST),
                                List.of())))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
            }
        }
        assertEquals("Constructor call should type-check cleanly", 0, errors);
    }

    public void testFinalVariableReassignment() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("Foo");
        pastClass.methods.add(
                METHOD("doSomething").RETURNS(ClassName.VOID)
                        .BODY(
                                new Definition(STRING, VARIABLE("x"), CONSTANT("hello")).addModifier(Modifier.FINAL),
                                ASSIGNMENT(VARIABLE("x"), CONSTANT("world"))
                        )
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        boolean foundFinalError = false;
        for (TypeDiagnostic d : diagnostics) {
            if (d.message.contains("Cannot assign to final variable")) {
                foundFinalError = true;
                break;
            }
        }
        assertTrue("Expected final variable reassignment error", foundFinalError);
    }

    public void testCrossClassMethodLookup() {
        TypeChecker checker = new TypeChecker();

        Class helper = new Class("Helper");
        helper.methods.add(
                METHOD("help").RETURNS(STRING).MODIFIERS(Modifier.PUBLIC)
        );

        Class caller = new Class("Caller");
        caller.fields.add(FIELD("helper", ClassName.get("Helper", "com.example")));
        caller.methods.add(
                METHOD("doWork").RETURNS(STRING)
                        .BODY(RETURN(METHOD_CALL(VARIABLE("helper"), "help", List.of())))
        );

        checker.registerClass(helper, "com.example");
        checker.registerClass(caller, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
            }
        }
        assertEquals("Cross-class method call should type-check cleanly", 0, errors);
    }

    public void testLambdaTypeInference1() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("LambdaTest");
        pastClass.fields.add(
                FIELD("lambda",ParameterizedType.get(ClassName.FUNCTION, INTEGER, STRING))
                        .INITIALIZER(
                                LAMBDA(PARAMETER("i", INTEGER)).returns(STRING)
                                        .BODY(CONSTANT("hello"))));

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            }

        }
        assertEquals("Lambda expression 1 should type-check cleanly", 0, errors);
    }
    public void testLambdaTypeInference2() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("LambdaTest");
        pastClass.methods.add(
                METHOD("test").RETURNS(ParameterizedType.get(ClassName.FUNCTION, INTEGER, STRING))
                        .BODY(
                                RETURN(LAMBDA(PARAMETER("i", INTEGER)).returns(STRING)
                                        .BODY(CONSTANT("hello")))));

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            }

        }
        assertEquals("Lambda expression 2 should type-check cleanly", 0, errors);
    }

    public void testGenericTypeInference1() {
        ExternalTypeRegistry extReg = new ExternalTypeRegistry();
        extReg.forClass(ClassName.MAP, TypeVariable.get("alpha"), TypeVariable.get("beta"))
                .method("get", TypeVariable.get("beta"), TypeVariable.get("alpha"))
                .method("put", TypeVariable.get("beta"), TypeVariable.get("alpha"), TypeVariable.get("beta"))
                .register();
        TypeChecker checker = new TypeChecker(extReg);

        Class pastClass = new Class("LambdaTest");
        pastClass.methods.add(
                METHOD("test")
                        .PARAMETER(ParameterizedType.get(ClassName.MAP, INTEGER, STRING), "m")
                        .RETURNS(STRING)
                        .BODY(
                                RETURN(METHOD_CALL(VARIABLE("m"), "get", List.of(CONSTANT(42))))));

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            }

        }
        assertEquals("get of Map should type-check cleanly", 0, errors);
    }

    public void testOverloadedMethodLookup() {
        // Register a class with overloaded methods: process(String) -> String, process(Integer) -> Integer
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("Overloaded");
        pastClass.methods.add(
                METHOD("process").RETURNS(STRING)
                        .PARAMETERS(PARAMETER("s", STRING))
                        .BODY(RETURN(VARIABLE("s")))
        );
        pastClass.methods.add(
                METHOD("process").RETURNS(INTEGER)
                        .PARAMETERS(PARAMETER("n", INTEGER))
                        .BODY(RETURN(VARIABLE("n")))
        );
        // Caller that passes a String — should resolve to process(String) -> String
        pastClass.methods.add(
                METHOD("callWithString").RETURNS(STRING)
                        .BODY(RETURN(METHOD_CALL("process", List.of(CONSTANT("hello")))))
        );
        // Caller that passes an Integer — should resolve to process(Integer) -> Integer
        pastClass.methods.add(
                METHOD("callWithInt").RETURNS(INTEGER)
                        .BODY(RETURN(METHOD_CALL("process", List.of(CONSTANT(42)))))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            }
        }
        assertEquals("Overloaded method calls should resolve correctly", 0, errors);
    }

    public void testOverloadedExternalMethodLookup() {
        // StringBuilder has append(String) -> StringBuilder and append(Object) -> StringBuilder
        // Calling with a String should prefer append(String) over append(Object)
        ExternalTypeRegistry extReg = new ExternalTypeRegistry();
        extReg.forClass(ClassName.STRING_BUILDER)
                .method("append", ClassName.STRING_BUILDER, ClassName.STRING)
                .method("append", ClassName.STRING_BUILDER, ClassName.OBJECT)
                .method("toString", ClassName.STRING)
                .constructor()
                .register();
        TypeRegistry registry = new TypeRegistry(extReg);

        // Lookup with String arg should select append(String), not append(Object)
        MethodSignature sig = registry.lookupMethod(
                ClassName.STRING_BUILDER, "append", List.of(STRING));
        assertNotNull("Should find append for String arg", sig);
        assertEquals("Should select append(String)", 1, sig.parameterTypes.size());
        assertEquals("Parameter should be String, not Object",
                "String", ((ClassName) sig.parameterTypes.get(0)).simpleName);

        // Lookup with Integer arg should select append(Object), since no append(Integer) exists
        MethodSignature sigInt = registry.lookupMethod(
                ClassName.STRING_BUILDER, "append", List.of(INTEGER));
        assertNotNull("Should find append for Integer arg", sigInt);
        assertEquals("Should select append(Object) for Integer",
                "Object", ((ClassName) sigInt.parameterTypes.get(0)).simpleName);

        // Also verify the full type-check pipeline works
        TypeChecker checker = new TypeChecker(extReg);

        Class pastClass = new Class("BuilderTest");
        pastClass.methods.add(
                METHOD("build").RETURNS(ClassName.STRING_BUILDER)
                        .PARAMETERS(PARAMETER("sb", ClassName.STRING_BUILDER))
                        .BODY(RETURN(METHOD_CALL(VARIABLE("sb"), "append", List.of(CONSTANT("hello")))))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            }
        }
        assertEquals("Overloaded external method should resolve correctly", 0, errors);
    }

    public void testNullTypeInference1() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("NullTest");
        pastClass.fields.add(FIELD("f", STRING).INITIALIZER(Constant.getNull()));


        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            }

        }
        assertEquals("Null type-check cleanly", 0, errors);
    }

    public void testNullTypeInference2() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("NullTest");
        pastClass.methods.add(
                METHOD("test").RETURNS(INTEGER)
                        .BODY(RETURN(Constant.getNull()))
        );
        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            }

        }
        assertEquals("Null type-check cleanly", 0, errors);
    }

    public void testGenericTypes() {
        ExternalTypeRegistry extReg = ExternalTypeRegistry.initializeExternalRegistry(new ExternalTypeRegistry());
        TypeChecker checker = new TypeChecker(extReg);

        Class pastClass = new Class("GenericTypeTest");
        pastClass.methods.add(
                METHOD("test").PARAMETER(LIST_OF_STRING,"ll").RETURNS(STRING)
                        .BODY(RETURN(METHOD_CALL(VARIABLE("ll"), "get", List.of(CONSTANT(0)))))
        );
        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            }

        }
        assertEquals("generitc types type cleanly", 0, errors);
    }

    public void testConverter() {
        ExternalTypeRegistry extReg = ExternalTypeRegistry.initializeExternalRegistry(new ExternalTypeRegistry());
        TypeChecker checker = new TypeChecker(extReg);

        Class pastClass = new Class("ConverterTypeTest");
        pastClass.methods.add(
                METHOD("test").RETURNS(INTEGER)
                        .BODY(RETURN(METHOD_CALL(INTEGER, "valueOf", List.of(CONSTANT("213")))))
        );
        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            }

        }
        assertEquals("converter types cleanly", 0, errors);
    }

    public void testLambdaWithDefinitionInBody() {
        TypeChecker checker = new TypeChecker();

        Class pastClass = new Class("LambdaDefTest");
        pastClass.methods.add(
                METHOD("test").RETURNS(ParameterizedType.get(FUNCTION, STRING, INTEGER))
                        .BODY(RETURN(
                                LAMBDA(PARAMETER("s", STRING)).returns(INTEGER)
                                        .BODY(
                                                DEFINITION(INTEGER, VARIABLE("x"), CONSTANT(1)),
                                                RETURN(VARIABLE("x"))
                                        )
                        ))
        );

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        long warnings = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            } else {
                warnings++;
                System.out.println("Warning: " + d);
            }
        }
        assertEquals("Lambda with definition and return should have no errors", 0, errors);
        assertEquals("Lambda with definition and return should have no warnings", 0, warnings);
    }

    public void testGenericTypesWithTypeVariables() {
        ExternalTypeRegistry extReg = new ExternalTypeRegistry();
        extReg.forClass(ClassName.OPTIONAL, TypeVariable.get("T"))
                .method("of", ParameterizedType.get(ClassName.OPTIONAL, TypeVariable.get("T")), TypeVariable.get("T"))
                .method("get", TypeVariable.get("T"))
                .register();

        TypeChecker checker = new TypeChecker(extReg);

        Class pastClass = new Class("GenericTypeVariableTest");
        pastClass.methods.add(
                METHOD("test").PARAMETER(ParameterizedType.get(ClassName.OPTIONAL, STRING), "input").RETURNS(STRING)
                        .BODY(RETURN(METHOD_CALL(VARIABLE("input"), "get", List.of()))));

        checker.registerClass(pastClass, "com.example");
        List<TypeDiagnostic> diagnostics = checker.checkAll();

        long errors = 0;
        for (TypeDiagnostic d : diagnostics) {
            if (d.severity == TypeDiagnostic.Severity.ERROR) {
                errors++;
                System.out.println("Error: " + d);
            }

        }
        assertEquals("generic types with types variables type cleanly", 0, errors);
    }


}
