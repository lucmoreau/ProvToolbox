package org.openprovenance.prov.template.compiler.past.checker;

import org.openprovenance.prov.template.compiler.past.type.ArrayType;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;

import static org.openprovenance.prov.template.compiler.past.checker.TypeInferrer.INFERRED_FUNCTION;
import static org.openprovenance.prov.template.compiler.past.checker.TypeInferrer.INFERRED_NULL;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.FUNCTION;

public class TypeCompatibility {


    // Widening order: int < Integer < Long < Float < Double
    private static final List<ClassName> NUMERIC_WIDENING = List.of(
            ClassName._int, ClassName.INTEGER, ClassName.LONG, ClassName.FLOAT, ClassName.DOUBLE
    );

    // --- Core: is `source` assignable to `target`? ---

    public static boolean isAssignable(TypeName target, TypeName source, TypeRegistry registry) {
        if (target == null || source == null) return true; // unknown types — be lenient

        // Same reference
        if (target == source) return true;

        // OBJECT accepts anything
        if (isObject(target)) return true;

        if (source instanceof ClassName&& isInferredNullType((ClassName) source)) return true;

        // TypeVariable on either side — treat as OBJECT bound (simplified)
        if (target instanceof TypeVariable || source instanceof TypeVariable) return true;

        // Same ClassName
        if (target instanceof ClassName && source instanceof ClassName) {
            ClassName tc = (ClassName) target;
            ClassName sc = (ClassName) source;
            if (sameClassName(tc, sc)) return true;


            // Numeric widening or unboxing
            if (isNumericType(target) && isNumericType(source)) {
                return numericRank(tc) >= numericRank(sc)
                        || unboxing(tc, sc);
            }

            // Boolean compatibility: bool and Boolean are interchangeable
            if (isBooleanType(tc) && isBooleanType(sc)) return true;

            // Subclass/interface check via registry
            return isSubtype(sc, tc, registry);
        }

        if (isFunctionType(source) && isFunctionalInterface(target, registry)) {
            MethodSignature methodSignature = theFunctionalInterface(target, registry);
            ParameterizedType functionType = (ParameterizedType) source;
            //System.out.println("***** Checking function type " + source + " is compatible with functional interface " + methodSignature);

            // Check parameter count
            assert methodSignature != null;
            // note functiontype, includes all parameters + return type, while method signature only includes parameters
            if (functionType.getTypeArguments().length != methodSignature.parameterTypes.size()+1) {
                return false;
            }
            // Check parameter types contravariantly
            for (int i = 0; i < methodSignature.parameterTypes.size(); i++) {
                if (!isAssignable(methodSignature.parameterTypes.get(i), functionType.getTypeArguments()[i], registry)) {
                    return false;
                }
            }
            // Check return type covariantly
            if (!isAssignable(functionType.getTypeArguments()[functionType.getTypeArguments().length - 1], methodSignature.returnType, registry)) {
                return false;
            }
            //System.out.println("***** Checking function type succeeded");
            return true;
        }

        // ParameterizedType assignability
        if (target instanceof ParameterizedType && source instanceof ParameterizedType) {
            ParameterizedType tpt = (ParameterizedType) target;
            ParameterizedType spt = (ParameterizedType) source;
            // Raw types must be compatible
            if (!isAssignable(tpt.getRawType(), spt.getRawType(), registry)) return false;
            // Diamond/raw: if source has no type args, accept
            if (spt.getTypeArguments().length == 0) return true;
            if (tpt.getTypeArguments().length == 0) return true;
            // Check type arguments covariantly (simplified)
            if (tpt.getTypeArguments().length != spt.getTypeArguments().length) return false;
            for (int i = 0; i < tpt.getTypeArguments().length; i++) {
                if (!isAssignable(tpt.getTypeArguments()[i], spt.getTypeArguments()[i], registry)) {
                    return false;
                }
            }
            return true;
        }

        // ParameterizedType target, ClassName source (raw type compatibility)
        if (target instanceof ParameterizedType && source instanceof ClassName) {
            return isAssignable(((ParameterizedType) target).getRawType(), source, registry);
        }

        // ClassName target, ParameterizedType source
        if (target instanceof ClassName && source instanceof ParameterizedType) {
            return isAssignable(target, ((ParameterizedType) source).getRawType(), registry);
        }

        // Array assignability: covariant on element type
        if (target instanceof ArrayType && source instanceof ArrayType) {
            return isAssignable(((ArrayType) target).elementType, ((ArrayType) source).elementType, registry);
        }


        return false;
    }

    private static boolean unboxing(ClassName tc, ClassName sc) {
        return (sameClassName(tc, ClassName._int) && sameClassName(sc, ClassName.INTEGER));
    }

    private static boolean isFunctionalInterface(TypeName typeName, TypeRegistry registry) {

        ClassName cn = resolveClassName(typeName);
        ClassSignature sig = registry.lookup(cn);
        if (sig == null) return false;

        // A functional interface has exactly one abstract method (ignoring Object methods)
        long abstractMethodCount = sig.methods.stream()
                .filter(m -> !m.modifiers.contains(javax.lang.model.element.Modifier.DEFAULT)
                        && !m.modifiers.contains(javax.lang.model.element.Modifier.STATIC))
                .filter(m -> !isObjectMethod(m.name))
                .count();
        return abstractMethodCount == 1;
    }
    private static MethodSignature theFunctionalInterface(TypeName typeName, TypeRegistry registry) {

        ClassName cn = resolveClassName(typeName);
        ClassSignature sig = registry.lookup(cn);
        if (sig == null) return null;

        // A functional interface has exactly one abstract method (ignoring Object methods)
        Optional<MethodSignature> abstractMethod = sig.methods.stream()
                .filter(m -> !m.modifiers.contains(Modifier.DEFAULT)
                        && !m.modifiers.contains(Modifier.STATIC))
                .filter(m -> !isObjectMethod(m.name))
                .findFirst();
        return abstractMethod.get();
    }


    private static boolean isObjectMethod(String name) {
        return "toString".equals(name) || "equals".equals(name) || "hashCode".equals(name);

    }

    // --- Numeric type utilities ---

    public static boolean isNumericType(TypeName type) {
        if (!(type instanceof ClassName)) return false;
        return numericRank((ClassName) type) >= 0;
    }

    public static boolean isPrimitiveType(TypeName type) {
        if (!(type instanceof ClassName)) return false;
        ClassName cn = (ClassName) type;
        return sameClassName(cn, ClassName._int)
                || sameClassName(cn, (ClassName) ClassName._bool);
    }

    public static TypeName numericPromotion(TypeName left, TypeName right) {
        if (!(left instanceof ClassName) || !(right instanceof ClassName)) {
            return ClassName.OBJECT;
        }
        ClassName cl = (ClassName) left;
        ClassName cr = (ClassName) right;
        int lr = numericRank(cl);
        int rr = numericRank(cr);
        if (lr < 0 || rr < 0) return ClassName.OBJECT;
        return (lr >= rr) ? left : right;
    }

    // --- Common supertype (for if-expression, ternary) ---

    public static TypeName commonSupertype(TypeName a, TypeName b, TypeRegistry registry) {
        if (a == null) return b;
        if (b == null) return a;
        if (isAssignable(a, b, registry)) return a;
        if (isAssignable(b, a, registry)) return b;
        // Both numeric → promote
        if (isNumericType(a) && isNumericType(b)) return numericPromotion(a, b);
        // Fallback
        return ClassName.OBJECT;
    }

    // --- Subtype check via registry (superclass/interface chain) ---

    private static boolean isSubtype(ClassName source, ClassName target, TypeRegistry registry) {
        // Known collection hierarchy: ArrayList/LinkedList → List → Collection
        // HashMap → Map, HashSet → Set → Collection
        if (isCollectionSubtype(source, target)) return true;

        if (isFunctionSubtype(source, target)) return true;

        // Exception hierarchy
        if (isExceptionSubtype(source, target)) return true;

        if (isInferredNullType(source)) return true;

        // Registry-based: walk superclass and interface chain
        ClassSignature sig = registry.lookup(source);
        if (sig == null) return false;

        if (sig.superclass != null) {
            ClassName superCn = resolveClassName(sig.superclass);
            if (superCn != null) {
                if (sameClassName(superCn, target)) return true;
                if (isSubtype(superCn, target, registry)) return true;
                if (isFunctionSubtype(superCn, target)) return true;
            }
        }

        for (TypeName iface : sig.interfaces) {
           // System.out.println("***** Checking interface " + iface + " of " + source + " against target " + target);
            ClassName ifaceCn = resolveClassName(iface);
            if (ifaceCn != null) {
                if (sameClassName(ifaceCn, target)) return true;
                if (isSubtype(ifaceCn, target, registry)) return true;
                if (isFunctionSubtype(ifaceCn, target)) return true;

            }
        }

        return false;
    }

    private static boolean isInferredNullType(ClassName source) {
        return sameClassName(source, INFERRED_NULL);
    }

    private static boolean isFunctionSubtype(ClassName source, ClassName target) {
        //System.out.println("***** Checking function subtype: " + source + " <: " + target);
        if (sameClassName(source, INFERRED_FUNCTION)) {
            return sameClassName(target, FUNCTION) || sameClassName(target,INFERRED_FUNCTION);
        }
        if (sameClassName(source, FUNCTION)) {
            return sameClassName(target, FUNCTION);
        }
        return false;
    }

    // Built-in collection subtyping (these types aren't PAST Classes, so the registry won't have them)
    private static boolean isCollectionSubtype(ClassName source, ClassName target) {
        String sn = source.simpleName;
        String tn = target.simpleName;

        // List subtypes
        if ("List".equals(tn) || "Collection".equals(tn) || "Iterable".equals(tn)) {
            if ("ArrayList".equals(sn) || "LinkedList".equals(sn) || "List".equals(sn)) return true;
        }
        if ("Collection".equals(tn) || "Iterable".equals(tn)) {
            if ("Set".equals(sn) || "HashSet".equals(sn) || "Collection".equals(sn)) return true;
            if ("List".equals(sn)) return true;
        }

        // Set subtypes
        if ("Set".equals(tn)) {
            if ("HashSet".equals(sn)) return true;
        }

        // Map subtypes
        if ("Map".equals(tn)) {
            if ("HashMap".equals(sn)) return true;
        }

        return false;
    }

    // Built-in exception subtyping
    private static boolean isExceptionSubtype(ClassName source, ClassName target) {
        String sn = source.simpleName;
        String tn = target.simpleName;

        if ("Exception".equals(tn) || "Throwable".equals(tn)) {
            return "RuntimeException".equals(sn)
                    || "IllegalArgumentException".equals(sn)
                    || "IllegalStateException".equals(sn)
                    || "UnsupportedOperationException".equals(sn)
                    || "UncheckedException".equals(sn)
                    || "SQLException".equals(sn);
        }

        if ("RuntimeException".equals(tn)) {
            return "IllegalArgumentException".equals(sn)
                    || "IllegalStateException".equals(sn)
                    || "UnsupportedOperationException".equals(sn);
        }

        return false;
    }

    // --- Helpers ---

    static boolean sameClassName(ClassName a, ClassName b) {
        return a.simpleName.equals(b.simpleName) && a.packge.equals(b.packge);
    }

    static boolean isFunctionType(TypeName type) {
        ClassName cl = resolveClassName(type);
        if (cl == null) return false;
        return sameClassName(cl, INFERRED_FUNCTION);
    }

    private static boolean isBooleanType(ClassName cn) {
        return sameClassName(cn, ClassName.BOOLEAN)
                || sameClassName(cn, (ClassName) ClassName._bool);
    }

    static boolean isObject(TypeName type) {
        if (!(type instanceof ClassName)) return false;
        return sameClassName((ClassName) type, ClassName.OBJECT);
    }

    private static int numericRank(ClassName cn) {
        for (int i = 0; i < NUMERIC_WIDENING.size(); i++) {
            if (sameClassName(cn, NUMERIC_WIDENING.get(i))) return i;
        }
        return -1;
    }

    private static ClassName resolveClassName(TypeName type) {
        if (type instanceof ClassName) return (ClassName) type;
        if (type instanceof ParameterizedType) return ((ParameterizedType) type).getRawType();
        return null;
    }
}
