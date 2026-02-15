package org.openprovenance.prov.template.compiler.past.checker;

import org.openprovenance.prov.template.compiler.past.Assignment;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Comment;
import org.openprovenance.prov.template.compiler.past.Constructor;
import org.openprovenance.prov.template.compiler.past.Definition;
import org.openprovenance.prov.template.compiler.past.DoLoop;
import org.openprovenance.prov.template.compiler.past.Expression;
import org.openprovenance.prov.template.compiler.past.Field;
import org.openprovenance.prov.template.compiler.past.ForLoop;
import org.openprovenance.prov.template.compiler.past.IfStatement;
import org.openprovenance.prov.template.compiler.past.Iterator;
import org.openprovenance.prov.template.compiler.past.LambdaExpression;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.Parameter;
import org.openprovenance.prov.template.compiler.past.Return;
import org.openprovenance.prov.template.compiler.past.Statement;
import org.openprovenance.prov.template.compiler.past.SuperConstructorCall;
import org.openprovenance.prov.template.compiler.past.ThrowStatement;
import org.openprovenance.prov.template.compiler.past.TryCatch;
import org.openprovenance.prov.template.compiler.past.Variable;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeChecker {
    private final TypeRegistry registry;
    private final List<TypeDiagnostic> diagnostics = new ArrayList<>();
    private final TypeInferrer inferrer;

    // Stored for Pass 2: class → packageName
    private final Map<String, String> registeredPackages = new HashMap<>();
    private final List<String> registrationOrder = new ArrayList<>();

    public TypeChecker() {
        this.registry = new TypeRegistry();
        this.inferrer = new TypeInferrer(registry, diagnostics);
        this.inferrer.setStatementChecker(this::checkStatement);
    }

    public TypeChecker(ExternalTypeRegistry externalRegistry) {
        this.registry = new TypeRegistry(externalRegistry);
        this.inferrer = new TypeInferrer(registry, diagnostics);
        this.inferrer.setStatementChecker(this::checkStatement);
    }

    // --- Pass 1: Registration ---

    public void registerClass(Class pastClass, String packageName) {
        registry.registerClass(pastClass, packageName);
        String key = packageName + "." + pastClass.name;
        registeredPackages.put(key, packageName);
        registrationOrder.add(key);
    }

    // --- Pass 2: Check All ---

    public List<TypeDiagnostic> checkAll() {
        diagnostics.clear();
        for (String key : registrationOrder) {
            String packageName = registeredPackages.get(key);
            String simpleName = key.substring(packageName.length() + 1);
            Class pastClass = registry.getPastClass(simpleName, packageName);
            if (pastClass != null) {
                checkClass(pastClass, packageName);
            }
        }
        return new ArrayList<>(diagnostics);
    }

    public TypeRegistry getRegistry() {
        return registry;
    }

    // --- Class-level checks ---

    private void checkClass(Class pastClass, String packageName) {
        // Check fields
        for (Field field : pastClass.fields) {
            checkField(field, pastClass, packageName);
        }

        // Check methods
        for (Method method : pastClass.methods) {
            checkMethod(method, pastClass, packageName);
        }

        // Check constructors
        for (Constructor ctor : pastClass.constructors) {
            checkConstructor(ctor, pastClass, packageName);
        }

        // Modifier checks (section 15d): abstract method checks
        checkAbstractMethods(pastClass, packageName);

        // Check interface implementation
        checkInterfaceImplementation(pastClass, packageName);
    }

    // --- Field checking ---

    private void checkField(Field field, Class pastClass, String packageName) {
        if (field.initialiser != null) {
            TypeEnvironment env = createClassEnvironment(pastClass, packageName, false);
            TypeName valueType = inferrer.infer(field.initialiser, env, pastClass.name, "<field>");
            if (field.type != null && valueType != null) {
               // System.out.println("Checking field '" + field.name + "': declared type " + field.type + ", initializer type " + valueType);
                if (!TypeCompatibility.isAssignable(field.type, valueType, registry)) {
                    diagnostics.add(TypeDiagnostic.error(
                            "Incompatible types: cannot assign " + valueType + " to field '" + field.name + "' of type " + field.type,
                            pastClass.name, "<field>", field.toString()));
                }
            }
        }
    }

    // --- Method checking (section 11) ---

    private void checkMethod(Method method, Class pastClass, String packageName) {
        TypeEnvironment env = createClassEnvironment(pastClass, packageName, method.isStatic());

        // Set static context
        if (method.isStatic()) {
            env.setInStaticContext(true);
        }

        // Register method parameters
        for (Parameter param : method.parameters) {
            if (param.type == null) {
                diagnostics.add(TypeDiagnostic.error(
                        "Parameter '" + param.name + "' has no type declaration",
                        pastClass.name, method.name, null));
                env.define(param.name, ClassName.OBJECT);
            } else {
                env.define(param.name, param.type);
            }
        }

        // Register type variables as OBJECT bounds
        for (TypeName tv : method.typeVariables) {
            if (tv instanceof TypeVariable) {
                env.define(((TypeVariable) tv).name, ClassName.OBJECT);
            }
        }

        // Determine expected return type
        TypeName expectedReturn = method.returnType;
        if (expectedReturn == null) {
            expectedReturn = ClassName.VOID;
        }

        // Walk method body
        for (Statement stmt : method.body) {
            checkStatement(stmt, env, expectedReturn, pastClass.name, method.name);
        }
    }

    // --- Constructor checking (section 13) ---

    private void checkConstructor(Constructor ctor, Class pastClass, String packageName) {
        TypeEnvironment env = createClassEnvironment(pastClass, packageName, false);

        // Register constructor parameters
        for (Parameter param : ctor.parameters) {
            if (param.type != null) {
                env.define(param.name, param.type);
            } else {
                env.define(param.name, ClassName.OBJECT);
            }
        }

        // Walk constructor body — expectedReturn is VOID
        for (Statement stmt : ctor.body) {
            checkStatement(stmt, env, ClassName.VOID, pastClass.name, "<init>");
        }
    }

    // --- Statement checking (section 10) ---

    private void checkStatement(Statement stmt, TypeEnvironment env, TypeName expectedReturn,
                                 String className, String methodName) {
        if (stmt == null) return;

        switch (stmt.statementKind) {
            case DEFINITION:
                checkDefinition((Definition) stmt, env, className, methodName);
                break;
            case ASSIGNMENT:
                checkAssignment((Assignment) stmt, env, className, methodName);
                break;
            case RETURN:
                checkReturn((Return) stmt, env, expectedReturn, className, methodName);
                break;
            case THROW:
                checkThrow((ThrowStatement) stmt, env, className, methodName);
                break;
            case IF_STATEMENT:
                checkIfStatement((IfStatement) stmt, env, expectedReturn, className, methodName);
                break;
            case FOR_LOOP:
                checkForLoop((ForLoop) stmt, env, expectedReturn, className, methodName);
                break;
            case DO_LOOP:
                checkDoLoop((DoLoop) stmt, env, expectedReturn, className, methodName);
                break;
            case ITERATOR:
                checkIterator((Iterator) stmt, env, expectedReturn, className, methodName);
                break;
            case TRY_CATCH:
                checkTryCatch((TryCatch) stmt, env, expectedReturn, className, methodName);
                break;
            case EXPRESSION_STATEMENT:
                if (stmt instanceof Expression) {
                    inferrer.infer((Expression) stmt, env, className, methodName);
                }
                break;
            case COMMENT:
                // Skip
                break;
            case SUPER_CONSTRUCTOR_CALL:
                checkSuperConstructorCall((SuperConstructorCall) stmt, env, className, methodName);
                break;
            default:
                break;
        }
    }

    // --- Definition ---

    private void checkDefinition(Definition def, TypeEnvironment env, String className, String methodName) {
        TypeName valueType = inferrer.infer(def.value, env, className, methodName);

        if (def.type != null && valueType != null) {
            if (!TypeCompatibility.isAssignable(def.type, valueType, registry)) {
                diagnostics.add(TypeDiagnostic.error(
                        "Incompatible types: cannot assign " + valueType + " to variable of type " + def.type,
                        className, methodName, def.toString()));
            }
        }

        // Register variable in environment
        String varName = extractVariableName(def.leftHandExpression);
        if (varName != null && def.type != null) {
            Set<Modifier> mods = def.modifiers.isEmpty()
                    ? Set.of()
                    : EnumSet.copyOf(def.modifiers);
            env.define(varName, def.type, mods);
        }

        if ("sb".equals(varName)) {
            System.out.println("Registered variable 'sb' of type " + def.type + " in " + className + "." + methodName + " with ENV " + env.getVariables());
        }
    }

    // --- Assignment ---

    private void checkAssignment(Assignment assign, TypeEnvironment env, String className, String methodName) {
        TypeName valueType = inferrer.infer(assign.value, env, className, methodName);

        // Check final variable reassignment (section 15a)
        String targetName = extractVariableName(assign.leftHandExpression);
        if (targetName != null && env.isFinal(targetName)) {
            diagnostics.add(TypeDiagnostic.error(
                    "Cannot assign to final variable '" + targetName + "'",
                    className, methodName, null));
        }

        // Type-check: infer LHS type and check assignability
        TypeName lhsType = inferrer.infer(assign.leftHandExpression, env, className, methodName);
        if (lhsType != null && valueType != null) {
            if (!TypeCompatibility.isAssignable(lhsType, valueType, registry)) {
                diagnostics.add(TypeDiagnostic.warning(
                        "Possible incompatible assignment: " + valueType + " to " + lhsType,
                        className, methodName, assign.toString()));
            }
        }
    }

    // --- Return ---

    private void checkReturn(Return ret, TypeEnvironment env, TypeName expectedReturn,
                              String className, String methodName) {
        if (ret.expression == null) return;

        TypeName exprType = inferrer.infer(ret.expression, env, className, methodName);

        if (expectedReturn != null && !TypeCompatibility.isObject(expectedReturn)
                && !(expectedReturn instanceof TypeVariable)) {
            if (!TypeCompatibility.sameClassName(asClassName(expectedReturn), ClassName.VOID)) {
                if (exprType != null && !TypeCompatibility.isAssignable(expectedReturn, exprType, registry)) {
                    diagnostics.add(TypeDiagnostic.error(
                            "Incompatible return type: expected " + expectedReturn + " but found " + exprType,
                            className, methodName, ret.toString()));
                }
            }
        }
    }

    // --- Throw ---

    private void checkThrow(ThrowStatement thr, TypeEnvironment env, String className, String methodName) {
        inferrer.infer(thr.expression, env, className, methodName);
    }

    // --- IfStatement ---

    private void checkIfStatement(IfStatement ifs, TypeEnvironment env, TypeName expectedReturn,
                                   String className, String methodName) {
        TypeName condType = inferrer.infer(ifs.condition, env, className, methodName);
        if (condType != null && !isBooleanCompatible(condType)) {
            diagnostics.add(TypeDiagnostic.warning(
                    "If condition should be boolean, found " + condType,
                    className, methodName, null));
        }

        TypeEnvironment thenEnv = env.pushScope();
        for (Statement s : ifs.thenBlock) {
            checkStatement(s, thenEnv, expectedReturn, className, methodName);
        }

        TypeEnvironment elseEnv = env.pushScope();
        for (Statement s : ifs.elseBlock) {
            checkStatement(s, elseEnv, expectedReturn, className, methodName);
        }
    }

    // --- ForLoop ---

    private void checkForLoop(ForLoop fl, TypeEnvironment env, TypeName expectedReturn,
                               String className, String methodName) {
        TypeEnvironment loopEnv = env.pushScope();

        if (fl.initialization != null) {
            checkDefinition(fl.initialization, loopEnv, className, methodName);
        }
        if (fl.condition != null) {
            TypeName condType = inferrer.infer(fl.condition, loopEnv, className, methodName);
            if (condType != null && !isBooleanCompatible(condType)) {
                diagnostics.add(TypeDiagnostic.warning(
                        "For-loop condition should be boolean, found " + condType,
                        className, methodName, null));
            }
        }
        if (fl.update != null) {
            checkAssignment(fl.update, loopEnv, className, methodName);
        }
        for (Statement s : fl.body) {
            checkStatement(s, loopEnv, expectedReturn, className, methodName);
        }
    }

    // --- DoLoop ---

    private void checkDoLoop(DoLoop dl, TypeEnvironment env, TypeName expectedReturn,
                              String className, String methodName) {
        TypeEnvironment loopEnv = env.pushScope();
        for (Statement s : dl.body) {
            checkStatement(s, loopEnv, expectedReturn, className, methodName);
        }
        if (dl.condition != null) {
            TypeName condType = inferrer.infer(dl.condition, loopEnv, className, methodName);
            if (condType != null && !isBooleanCompatible(condType)) {
                diagnostics.add(TypeDiagnostic.warning(
                        "Do-loop condition should be boolean, found " + condType,
                        className, methodName, null));
            }
        }
    }

    // --- Iterator (enhanced for) ---

    private void checkIterator(Iterator iter, TypeEnvironment env, TypeName expectedReturn,
                                String className, String methodName) {
        TypeName collectionType = inferrer.infer(iter.collection, env, className, methodName);

        TypeEnvironment iterEnv = env.pushScope();

        // Register the iteration variable
        if (iter.parameter != null && iter.parameter.type != null) {
            iterEnv.define(iter.parameter.name, iter.parameter.type);

            // If collection is parameterized, extract element type and check
            if (collectionType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) collectionType;
                String rawName = pt.getRawType().simpleName;
                if (("Collection".equals(rawName) || "List".equals(rawName)
                        || "Set".equals(rawName) || "ArrayList".equals(rawName)
                        || "LinkedList".equals(rawName) || "HashSet".equals(rawName))
                        && pt.getTypeArguments().length >= 1) {
                    TypeName elementType = pt.getTypeArguments()[0];
                    if (!TypeCompatibility.isAssignable(iter.parameter.type, elementType, registry)) {
                        diagnostics.add(TypeDiagnostic.warning(
                                "Iterator parameter type " + iter.parameter.type
                                        + " may be incompatible with collection element type " + elementType,
                                className, methodName, null));
                    }
                }
            }
        }

        for (Statement s : iter.body) {
            checkStatement(s, iterEnv, expectedReturn, className, methodName);
        }
    }

    // --- TryCatch ---

    private void checkTryCatch(TryCatch tc, TypeEnvironment env, TypeName expectedReturn,
                                String className, String methodName) {
        TypeEnvironment tryEnv = env.pushScope();
        for (Statement s : tc.tryBlock) {
            checkStatement(s, tryEnv, expectedReturn, className, methodName);
        }

        TypeEnvironment catchEnv = env.pushScope();
        if (tc.exceptionName != null && tc.exceptionType != null) {
            catchEnv.define(tc.exceptionName, tc.exceptionType);
        }
        for (Statement s : tc.catchBlock) {
            checkStatement(s, catchEnv, expectedReturn, className, methodName);
        }
    }

    // --- SuperConstructorCall ---

    private void checkSuperConstructorCall(SuperConstructorCall sc, TypeEnvironment env,
                                            String className, String methodName) {
        if (sc.arguments != null) {
            for (Expression arg : sc.arguments) {
                inferrer.infer(arg, env, className, methodName);
            }
        }
    }

    // --- Abstract method checks (section 15d) ---

    private void checkAbstractMethods(Class pastClass, String packageName) {
        for (Method method : pastClass.methods) {
            boolean isAbstract = method.modifiers.contains(Modifier.ABSTRACT);
            if (isAbstract && !method.body.isEmpty()) {
                diagnostics.add(TypeDiagnostic.warning(
                        "Abstract method '" + method.name + "' should not have a body",
                        pastClass.name, method.name, null));
            }
        }

        // Non-abstract, non-interface class with abstract methods
        if (!pastClass.isInterface && !pastClass.modifiers.contains(Modifier.ABSTRACT)) {
            for (Method method : pastClass.methods) {
                if (method.modifiers.contains(Modifier.ABSTRACT)) {
                    diagnostics.add(TypeDiagnostic.warning(
                            "Non-abstract class '" + pastClass.name + "' has unimplemented abstract method '" + method.name + "'",
                            pastClass.name, method.name, null));
                }
            }
        }
    }

    // --- Interface implementation checks (section 15d) ---

    private void checkInterfaceImplementation(Class pastClass, String packageName) {
        if (pastClass.isInterface) return;
        if (pastClass.modifiers.contains(Modifier.ABSTRACT)) return;

        for (TypeName ifaceType : pastClass.interfaces) {
            ClassName ifaceCn = resolveClassName(ifaceType);
            if (ifaceCn == null) continue;
            ClassSignature ifaceSig = registry.lookup(ifaceCn);
            if (ifaceSig == null) continue;

            for (MethodSignature ifaceMethod : ifaceSig.methods) {
                boolean found = false;
                for (Method m : pastClass.methods) {
                    if (m.name.equals(ifaceMethod.name)
                            && m.parameters.size() == ifaceMethod.parameterTypes.size()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    diagnostics.add(TypeDiagnostic.warning(
                            "Class '" + pastClass.name + "' does not implement '" + ifaceMethod.name
                                    + "()' from interface '" + ifaceSig.name + "'",
                            pastClass.name, ifaceMethod.name, null));
                }
            }
        }
    }

    // --- Environment creation ---

    private TypeEnvironment createClassEnvironment(Class pastClass, String packageName, boolean isStatic) {
        TypeEnvironment env = new TypeEnvironment();

        // Register class fields
        for (Field field : pastClass.fields) {
            if (field.type != null) {
                Set<Modifier> mods = field.modifiers.isEmpty()
                        ? Set.of()
                        : EnumSet.copyOf(field.modifiers);
                env.define(field.name, field.type, mods);
            }
        }

        // Register "this" for non-static contexts
        if (!isStatic) {
            env.define("this", ClassName.get(pastClass.name, packageName));
        }

        if (isStatic) {
            env.setInStaticContext(true);
        }

        return env;
    }

    // --- Helpers ---

    private static String extractVariableName(Expression expr) {
        if (expr instanceof Variable) {
            return ((Variable) expr).name;
        }
        return null;
    }

    private static boolean isBooleanCompatible(TypeName type) {
        if (type instanceof ClassName) {
            ClassName cn = (ClassName) type;
            return TypeCompatibility.sameClassName(cn, ClassName.BOOLEAN)
                    || TypeCompatibility.sameClassName(cn, (ClassName) ClassName._bool);
        }
        return false;
    }

    private static ClassName asClassName(TypeName type) {
        if (type instanceof ClassName) return (ClassName) type;
        if (type instanceof ParameterizedType) return ((ParameterizedType) type).getRawType();
        return ClassName.OBJECT;
    }

    private static ClassName resolveClassName(TypeName type) {
        if (type instanceof ClassName) return (ClassName) type;
        if (type instanceof ParameterizedType) return ((ParameterizedType) type).getRawType();
        return null;
    }
}
