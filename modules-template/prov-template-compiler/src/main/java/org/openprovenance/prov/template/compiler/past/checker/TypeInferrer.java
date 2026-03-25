package org.openprovenance.prov.template.compiler.past.checker;

import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ArrayType;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import java.util.List;

public class TypeInferrer {
    // For inferring lambda types, we use a special internal "inferred function" type with type args for parameters and return type.
    static final ClassName INFERRED_FUNCTION = ClassName.get( "Function", "past.inferred");
    static final ClassName INFERRED_NULL = ClassName.get( "Null", "past.inferred");

    /**
     * Callback interface for delegating full statement checking (Definition, Assignment,
     * Return, IfStatement, etc.) back to TypeChecker from within lambda body walks.
     */
    public interface StatementChecker {
        void checkStatement(Statement stmt, TypeEnvironment env, TypeName expectedReturn,
                            String className, String methodName);
    }

    private final TypeRegistry registry;
    private final List<TypeDiagnostic> diagnostics;
    private StatementChecker statementChecker;

    public TypeInferrer(TypeRegistry registry, List<TypeDiagnostic> diagnostics) {
        this.registry = registry;
        this.diagnostics = diagnostics;
    }

    public void setStatementChecker(StatementChecker statementChecker) {
        this.statementChecker = statementChecker;
    }

    // --- Main entry point ---

    public TypeName infer(Expression expr, TypeEnvironment env, String className, String methodName) {
        if (expr == null) return null;

        TypeName result;
        switch (expr.expressionKind) {
            case CONSTANT:
                result = inferConstant((Constant) expr);
                break;
            case VARIABLE:
                result = inferVariable((Variable) expr, env, className, methodName);
                break;
            case CAST:
                result = inferCast((CastExpression) expr, env, className, methodName);
                break;
            case ARRAY_INITIALISER:
                result = inferArrayInitialiser((ArrayInitialiser) expr);
                break;
            case ARRAY_ALLOCATOR:
                result = inferArrayAllocator((ArrayAllocator) expr);
                break;
            case ARRAY_ACCESSOR:
                result = inferArrayAccessor((ArrayAccessor) expr, env, className, methodName);
                break;
            case POST_INCREMENT:
                result = inferPostIncrement((PostIncrement) expr, env, className, methodName);
                break;
            case BINARY_OP:
                result = inferBinaryOp((BinaryOp) expr, env, className, methodName);
                break;
            case IF_EXPRESSION:
                result = inferIfExpression((IfExpression) expr, env, className, methodName);
                break;
            case LAMBDA_EXPRESSION:
                result = inferLambdaType((LambdaExpression) expr, env, null, className, methodName);
                break;
            case METHOD_CALL:
                result = inferMethodCall((MethodCall) expr, env, className, methodName);
                break;
            case INSTANCEOF:
                result = inferInstanceOf((InstanceOf)expr, env, className, methodName);
                break;
            default:
                result = ClassName.OBJECT;
                break;
        }

        expr.inferredType = result;
        return result;
    }

    private TypeName inferInstanceOf(InstanceOf expr, TypeEnvironment env, String className, String methodName) {
        infer(expr.expression, env, className, methodName);
        return ClassName.BOOLEAN;
    }

    // --- Constant ---

    private TypeName inferConstant(Constant c) {
        if (c.constantType == null) return ClassName.OBJECT;
        switch (c.constantType) {
            case STRING:  return ClassName.STRING;
            case INTEGER: return ClassName.INTEGER;
            case LONG:    return ClassName.LONG;
            case FLOAT:   return ClassName.FLOAT;
            case DOUBLE:  return ClassName.DOUBLE;
            case BOOL:
            case BOOLEAN: return ClassName.BOOLEAN;
            case NULL:    return INFERRED_NULL;
            default:      return ClassName.OBJECT;
        }
    }

    // --- Variable ---

    private TypeName inferVariable(Variable v, TypeEnvironment env, String className, String methodName) {
        TypeName type = env.lookup(v.name);
        if (type != null) return type;

        // Fall back to inherited field lookup via superclass chain
        TypeName envThis = env.lookup("this");
        ClassName currentClass = resolveClassName(envThis);
        if (currentClass != null) {
            ClassSignature sig = registry.lookup(currentClass);
            if (sig != null && sig.superclass != null) {
                ClassName superCn = resolveClassName(sig.superclass);
                if (superCn != null) {
                    TypeName inherited = registry.lookupField(superCn, v.name);
                    if (inherited != null) return inherited;
                }
            }
        }

        diagnostics.add(TypeDiagnostic.warning(
                "Undefined variable '" + v.name + "'",
                className, methodName, v.toString() + " IN ENV " + env.getVariables()));
        return ClassName.OBJECT;
    }

    // --- Cast ---

    private TypeName inferCast(CastExpression c, TypeEnvironment env, String className, String methodName) {
        infer(c.expression, env, className, methodName);
        return c.targetType;
    }

    // --- Array ---

    private TypeName inferArrayInitialiser(ArrayInitialiser ai) {
        return ArrayType.of(ai.elementType);
    }

    private TypeName inferArrayAllocator(ArrayAllocator aa) {
        return ArrayType.of(aa.elementType);
    }

    private TypeName inferArrayAccessor(ArrayAccessor aa, TypeEnvironment env, String className, String methodName) {
        TypeName arrayType = infer(aa.arrayExpression, env, className, methodName);
        infer(aa.indexExpression, env, className, methodName);
        if (arrayType instanceof ArrayType) {
            return ((ArrayType) arrayType).elementType;
        }
        return ClassName.OBJECT;
    }

    // --- PostIncrement ---

    private TypeName inferPostIncrement(PostIncrement pi, TypeEnvironment env, String className, String methodName) {
        TypeName innerType = infer(pi.expression, env, className, methodName);
        if (!TypeCompatibility.isNumericType(innerType)) {
            diagnostics.add(TypeDiagnostic.warning(
                    "Post-increment on non-numeric type " + innerType,
                    className, methodName, pi.toString()));
        }
        return innerType;
    }

    // --- BinaryOp ---

    private TypeName inferBinaryOp(BinaryOp bo, TypeEnvironment env, String className, String methodName) {
        TypeName leftType = infer(bo.left, env, className, methodName);
        TypeName rightType = infer(bo.right, env, className, methodName);

        switch (bo.op) {
            case "==":
            case "!=":
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "&&":
            case "||":
                return ClassName.BOOLEAN;
            case "+":
                if (isString(leftType) || isString(rightType)) {
                    return ClassName.STRING;
                }
                return TypeCompatibility.numericPromotion(leftType, rightType);
            case "-":
            case "*":
            case "/":
            case "%":
                return TypeCompatibility.numericPromotion(leftType, rightType);
            case "Objects.equals":
                return ClassName.BOOLEAN;
            case "instanceof":
                throw new UnsupportedOperationException("instanceof should be handled as a separate expression kind");
            default:
                return ClassName.OBJECT;
        }
    }

    // --- IfExpression (ternary) ---

    private TypeName inferIfExpression(IfExpression ie, TypeEnvironment env, String className, String methodName) {
        infer(ie.condition, env, className, methodName);
        TypeName thenType = (ie.thenExpression != null) ? infer(ie.thenExpression, env, className, methodName) : null;
        TypeName elseType = (ie.elseExpression != null) ? infer(ie.elseExpression, env, className, methodName) : null;
        return TypeCompatibility.commonSupertype(thenType, elseType, registry);
    }

    // --- Lambda ---

    public TypeName inferLambdaType(LambdaExpression lambda, TypeEnvironment env,
                                     TypeName targetType, String className, String methodName) {
        TypeEnvironment lambdaEnv = env.pushScope();

        List<TypeName> parameterTypes = new java.util.ArrayList<>();
        // Register lambda parameters
        for (int i = 0; i < lambda.parameters.size(); i++) {
            Parameter param = lambda.parameters.get(i);
            if (param.type != null) {
                lambdaEnv.define(param.name, param.type);
                parameterTypes.add(param.type);
            } else if (targetType instanceof ParameterizedType) {
                TypeName inferredParamType = extractParamTypeFromFunctionalInterface(
                        (ParameterizedType) targetType, i);
                if (inferredParamType != null) {
                    lambdaEnv.define(param.name, inferredParamType);
                    parameterTypes.add(inferredParamType);
                } else {
                    lambdaEnv.define(param.name, ClassName.OBJECT);
                    parameterTypes.add(ClassName.OBJECT);
                }
            } else {
                lambdaEnv.define(param.name, ClassName.OBJECT);
                parameterTypes.add(ClassName.OBJECT);
            }
        }

        // Determine expected return type
        TypeName expectedReturn = lambda.returnType;
        if (expectedReturn == null && targetType instanceof ParameterizedType) {
            expectedReturn = extractReturnTypeFromFunctionalInterface((ParameterizedType) targetType);
        }


        // Walk lambda body — delegate to full statement checker if available
        String lambdaMethodName = methodName + "$lambda";
        for (Statement stmt : lambda.body) {
            if (statementChecker != null) {
                statementChecker.checkStatement(stmt, lambdaEnv, expectedReturn, className, lambdaMethodName);
            } else if (stmt instanceof Expression) {
                infer((Expression) stmt, lambdaEnv, className, lambdaMethodName);
            }
        }

        // Infer lambda's own type
        TypeName result;
        if (targetType != null) {
            result = targetType;
        } else if (expectedReturn!=null) {
            result = expectedReturn;
        } else {
            result = ClassName.OBJECT;
        }
        parameterTypes.add(result);
        lambda.inferredType = ParameterizedType.get(INFERRED_FUNCTION, parameterTypes.toArray(new TypeName[0]));
       // System.out.println("---> Inferring lambda with target type: " + targetType + ", inferredType: " + lambda.inferredType + " lambda expression  : " + lambda);

        return lambda.inferredType;
    }

    // --- MethodCall ---

    private TypeName inferMethodCall(MethodCall mc, TypeEnvironment env, String className, String methodName) {
        switch (mc.operatorKind) {
            case CONSTRUCTOR_CALL:
                return inferConstructorCall(mc, env, className, methodName);
            case STATIC_METHOD_CALL:
                return inferStaticMethodCall(mc, env, className, methodName);
            case OBJECT_METHOD_CALL:
                return inferObjectMethodCall(mc, env, className, methodName);
            case OPERATOR_VARIABLE:
                return inferObjectMethodCall(mc, env, className, methodName);
            case FUNCTIONAL_INTERFACE_CALL:
                return inferObjectMethodCall(mc, env, className, methodName);
            case OBJECT_ACCESSOR:
                return inferObjectAccessor(mc, env, className, methodName);
            case SUPER_METHOD_CALL:
                return inferSuperMethodCall(mc, env, className, methodName);
            case NO_OPERATOR:
                return inferNoOperatorCall(mc, env, className, methodName);
            default:
                return ClassName.OBJECT;
        }
    }

    private TypeName inferConstructorCall(MethodCall mc, TypeEnvironment env, String className, String methodName) {
        // Anonymous class: inferred type is the first interface
        if (mc.clazz != null) {
            Class anonClass = mc.clazz;
            if (anonClass.classKind == Class.ClassKind.ANONYMOUS && !anonClass.interfaces.isEmpty()) {
                inferArguments(mc, env, className, methodName, null);
                return anonClass.interfaces.get(0);
            }
            // Named inner class passed as clazz — use its name if available
            if (anonClass.name != null) {
                inferArguments(mc, env, className, methodName, null);
                return ClassName.get(anonClass.name, "");
            }
        }

        TypeName constructedType = mc.className;
        if (constructedType == null) return ClassName.OBJECT;

        ClassName rawType = resolveClassName(constructedType);
        int argCount = (mc.arguments != null) ? mc.arguments.size() : 0;

        // Lookup constructor signature for argument checking
        MethodSignature ctorSig = null;
        if (rawType != null) {
            ctorSig = registry.lookupConstructor(rawType, argCount);
        }
        inferArguments(mc, env, className, methodName, ctorSig);

        // The type of "new Foo<T>(...)" is Foo<T> (the full parameterized type if given)
        return constructedType;
    }

    private TypeName inferStaticMethodCall(MethodCall mc, TypeEnvironment env, String className, String methodName) {
        ClassName owner = resolveClassName(mc.className);
        MethodSignature sig = null;
        if (owner != null) {
            List<TypeName> argTypes = preInferArgTypes(mc, env, className, methodName);
            sig = registry.lookupMethod(owner, mc.methodName, argTypes);
            if (sig == null) {
                // fallback to count-based lookup
                int argCount = (mc.arguments != null) ? mc.arguments.size() : 0;
                sig = registry.lookupMethod(owner, mc.methodName, argCount);
            }
        }
        inferArguments(mc, env, className, methodName, sig);
        if (sig != null) return sig.returnType;
        return ClassName.OBJECT;
    }

    private TypeName inferObjectMethodCall(MethodCall mc, TypeEnvironment env, String className, String methodName) {
        TypeName receiverType = null;
        if (mc.object != null) {
            receiverType = infer(mc.object, env, className, methodName);
        }
        ClassName receiverClass = resolveClassName(receiverType);
        MethodSignature sig = null;
        if (receiverClass != null) {
            List<TypeName> argTypes = preInferArgTypes(mc, env, className, methodName);
            sig = registry.lookupMethod(receiverClass, mc.methodName, argTypes);
            if (sig == null) {
                // fallback to count-based lookup
                int argCount = (mc.arguments != null) ? mc.arguments.size() : 0;
                sig = registry.lookupMethod(receiverClass, mc.methodName, argCount);
            }
        }
        inferArguments(mc, env, className, methodName, sig);

        if (sig != null) {
            return resolveReturnType(sig.returnType, receiverType);
        }
        // When the receiver is a type variable, the return type cannot be determined statically
        // without knowing the concrete type argument. Return the TypeVariable so that
        // isAssignable() treats it as compatible rather than falling back to Object.
        if (receiverType instanceof TypeVariable) {
            return receiverType;
        }
        return ClassName.OBJECT;
    }

    private TypeName inferFunctionalInterfaceCall(MethodCall mc, TypeEnvironment env, String className, String methodName) {
        TypeName receiverType = null;
        if (mc.object != null) {
            receiverType = infer(mc.object, env, className, methodName);
        }
        inferArguments(mc, env, className, methodName, null);

        // Extract return type from functional interface type args
        if (receiverType instanceof ParameterizedType) {
            TypeName extracted = extractReturnTypeFromFunctionalInterface((ParameterizedType) receiverType);
            if (extracted != null) return extracted;
        }
        return ClassName.OBJECT;
    }

    private TypeName inferObjectAccessor(MethodCall mc, TypeEnvironment env, String className, String methodName) {
        // Accessor on a ClassName (static field access)
        if (mc.className != null) {
            ClassName owner = resolveClassName(mc.className);
            if (owner != null) {
                TypeName fieldType = registry.lookupField(owner, mc.methodName);
                if (fieldType != null) return fieldType;
            }
            return ClassName.OBJECT;
        }
        // Accessor on an expression (instance field access)
        if (mc.object != null) {
            TypeName receiverType = infer(mc.object, env, className, methodName);
            ClassName receiverClass = resolveClassName(receiverType);
            if (receiverClass != null) {
                TypeName fieldType = registry.lookupField(receiverClass, mc.methodName);
                if (fieldType != null) return fieldType;
            }
        }
        return ClassName.OBJECT;
    }

    private TypeName inferSuperMethodCall(MethodCall mc, TypeEnvironment env, String className, String methodName) {
        // super.method(args) — resolve method on the superclass of the current class
        TypeName envThis = env.lookup("this");
        ClassName currentClass = resolveClassName(envThis);
        ClassName superClass = null;
        if (currentClass != null) {
            ClassSignature sig = registry.lookup(currentClass);
            if (sig != null && sig.superclass != null) {
                superClass = resolveClassName(sig.superclass);
            }
        }
        MethodSignature methodSig = null;
        if (superClass != null) {
            List<TypeName> argTypes = preInferArgTypes(mc, env, className, methodName);
            methodSig = registry.lookupMethod(superClass, mc.methodName, argTypes);
            if (methodSig == null) {
                int argCount = (mc.arguments != null) ? mc.arguments.size() : 0;
                methodSig = registry.lookupMethod(superClass, mc.methodName, argCount);
            }
        }
        inferArguments(mc, env, className, methodName, methodSig);
        if (methodSig != null) return methodSig.returnType;
        return ClassName.OBJECT;
    }

    private TypeName inferNoOperatorCall(MethodCall mc, TypeEnvironment env, String className, String methodName) {
        // Try current class first — className from the checker context
        TypeName envThis = env.lookup("this");
        ClassName currentClass = resolveClassName(envThis);
        MethodSignature sig = null;
        if (currentClass != null) {
            List<TypeName> argTypes = preInferArgTypes(mc, env, className, methodName);
            sig = registry.lookupMethod(currentClass, mc.methodName, argTypes);
            if (sig == null) {
                // fallback to count-based lookup
                int argCount = (mc.arguments != null) ? mc.arguments.size() : 0;
                sig = registry.lookupMethod(currentClass, mc.methodName, argCount);
            }
        }
        inferArguments(mc, env, className, methodName, sig);
        if (sig != null) return sig.returnType;
        return ClassName.OBJECT;
    }

    // --- Pre-infer argument types for overload resolution ---

    private List<TypeName> preInferArgTypes(MethodCall mc, TypeEnvironment env,
                                             String className, String methodName) {
        java.util.ArrayList<TypeName> argTypes = new java.util.ArrayList<>();
        if (mc.arguments == null) return argTypes;
        for (Expression arg : mc.arguments) {
            if (arg instanceof LambdaExpression) {
                // Lambda type depends on the method signature, use OBJECT as placeholder
                argTypes.add(ClassName.OBJECT);
            } else {
                // Infer the argument type (this also sets arg.inferredType)
                TypeName inferred = infer(arg, env, className, methodName);
                argTypes.add(inferred != null ? inferred : ClassName.OBJECT);
            }
        }
        return argTypes;
    }

    // --- Argument inference with lambda context propagation ---

    private void inferArguments(MethodCall mc, TypeEnvironment env,
                                String className, String methodName, MethodSignature sig) {
        if (mc.arguments == null) return;
        for (int i = 0; i < mc.arguments.size(); i++) {
            Expression arg = mc.arguments.get(i);
            if (arg instanceof LambdaExpression) {
                TypeName expectedParamType = null;
                if (sig != null && i < sig.parameterTypes.size()) {
                    expectedParamType = sig.parameterTypes.get(i);
                }
                inferLambdaType((LambdaExpression) arg, env, expectedParamType, className, methodName);
            } else if (arg.inferredType == null) {
                // Only infer if not already inferred by preInferArgTypes
                infer(arg, env, className, methodName);
            }
        }
    }

    // --- Functional interface type extraction ---

    private TypeName extractParamTypeFromFunctionalInterface(ParameterizedType pt, int paramIndex) {
        String name = pt.getRawType().simpleName;
        TypeName[] args = pt.getTypeArguments();

        switch (name) {
            case "Function":      // Function<T, R> → param [T]
                if (paramIndex == 0 && args.length >= 1) return args[0];
                break;
            case "BiFunction":    // BiFunction<T, U, R> → params [T, U]
                if (paramIndex < 2 && args.length >= 2) return args[paramIndex];
                break;
            case "Consumer":      // Consumer<T> → param [T]
                if (paramIndex == 0 && args.length >= 1) return args[0];
                break;
            case "BiConsumer":    // BiConsumer<T, U> → params [T, U]
                if (paramIndex < 2 && args.length >= 2) return args[paramIndex];
                break;
            case "Supplier":      // Supplier<T> → no params
                break;
            case "TriFunction":   // TriFunction<T, U, V, R> → params [T, U, V]
                if (paramIndex < 3 && args.length >= 3) return args[paramIndex];
                break;
        }
        return null;
    }

    private TypeName extractReturnTypeFromFunctionalInterface(ParameterizedType pt) {
        String name = pt.getRawType().simpleName;
        TypeName[] args = pt.getTypeArguments();

        switch (name) {
            case "Function":      // Function<T, R> → return R
                if (args.length >= 2) return args[1];
                break;
            case "BiFunction":    // BiFunction<T, U, R> → return R
                if (args.length >= 3) return args[2];
                break;
            case "Consumer":      // Consumer<T> → return VOID
            case "BiConsumer":    // BiConsumer<T, U> → return VOID
                return ClassName.VOID;
            case "Supplier":      // Supplier<T> → return T
                if (args.length >= 1) return args[0];
                break;
            case "TriFunction":   // TriFunction<T, U, V, R> → return R
                if (args.length >= 4) return args[3];
                break;
        }
        return null;
    }

    // --- Return type resolution (substitute type variables from receiver's type args) ---

    private TypeName resolveReturnType(TypeName returnType, TypeName receiverType) {
        if (returnType == null) return ClassName.VOID;
        // If return type is a type variable and receiver is parameterized, try to resolve
        if (returnType instanceof TypeVariable && receiverType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) receiverType;
            String varName = ((TypeVariable) returnType).name;
            // Try PAST ClassSignature first
            ClassSignature classSig = registry.lookup(pt.getRawType());
            if (classSig != null && !classSig.typeVariables.isEmpty()) {
                for (int i = 0; i < classSig.typeVariables.size(); i++) {
                    TypeName tv = classSig.typeVariables.get(i);
                    if (tv instanceof TypeVariable && ((TypeVariable) tv).name.equals(varName)) {
                        if (i < pt.getTypeArguments().length) {
                            return pt.getTypeArguments()[i];
                        }
                    }
                }
            }
            // Try external type params
            List<TypeVariable> extParams = registry.getExternalRegistry().lookupTypeParams(pt.getRawType());
            if (!extParams.isEmpty()) {
                for (int i = 0; i < extParams.size(); i++) {
                    if (extParams.get(i).name.equals(varName)) {
                        if (i < pt.getTypeArguments().length) {
                            return pt.getTypeArguments()[i];
                        }
                    }
                }
            }
            // Fallback: positional for well-known generic types
            TypeName[] args = pt.getTypeArguments();
            if ("E".equals(varName) || "T".equals(varName)) {
                if (args.length >= 1) return args[0];
            }
            if ("V".equals(varName)) {
                if (args.length >= 2) return args[1];
            }
            if ("K".equals(varName)) {
                if (args.length >= 1) return args[0];
            }
        }
        return returnType;
    }

    // --- Helpers ---

    private static boolean isString(TypeName type) {
        if (!(type instanceof ClassName)) return false;
        return TypeCompatibility.sameClassName((ClassName) type, ClassName.STRING);
    }

    private static ClassName resolveClassName(TypeName type) {
        if (type instanceof ClassName) return (ClassName) type;
        if (type instanceof ParameterizedType) return ((ParameterizedType) type).getRawType();
        if (type instanceof TypeVariable) {
            TypeVariable tv = (TypeVariable) type;
            if (!tv.bounds.isEmpty()) {
                return resolveClassName(tv.bounds.get(0));
            }
        }
        return null;
    }
}
