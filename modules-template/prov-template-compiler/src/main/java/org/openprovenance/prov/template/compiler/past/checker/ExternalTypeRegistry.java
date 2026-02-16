package org.openprovenance.prov.template.compiler.past.checker;

import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.BOOLEAN;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.BUILDER_INTERFACE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.DOUBLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.FLOAT;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.INTEGER;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.LONG;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.OBJECT;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.PROV_FACTORY;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.PROV_FILE_BUILDER;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.PROV_QUALIFIED_NAME;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.STRING;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.STRING_ARRAY;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.STRING_BUILDER;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.VOID;
import static org.openprovenance.prov.template.compiler.past.type.ClassName._bool;

public class ExternalTypeRegistry {
    private final Map<String, List<MethodSignature>> methods = new HashMap<>();
    private final Map<String, Map<String, TypeName>> fields = new HashMap<>();
    private final Map<String, List<MethodSignature>> constructors = new HashMap<>();
    private final Map<String, List<TypeVariable>> typeParams = new HashMap<>();

    private static String key(ClassName owner) {
        return owner.packge + "." + owner.simpleName;
    }

    public void registerMethod(ClassName owner, String methodName,
                               List<TypeName> paramTypes, TypeName returnType, boolean isStatic) {
        String k = key(owner);
        methods.computeIfAbsent(k, x -> new ArrayList<>())
                .add(new MethodSignature(methodName, paramTypes, returnType, Collections.emptyList(), isStatic));
    }

    public void registerField(ClassName owner, String fieldName, TypeName type) {
        String k = key(owner);
        fields.computeIfAbsent(k, x -> new LinkedHashMap<>())
                .put(fieldName, type);
    }

    public void registerConstructor(ClassName owner, List<TypeName> paramTypes) {
        String k = key(owner);
        ClassName returnType = owner;
        constructors.computeIfAbsent(k, x -> new ArrayList<>())
                .add(new MethodSignature("<init>", paramTypes, returnType, Collections.emptyList(), false));
    }

    public ExternalTypeBuilder forClass(ClassName className) {
        return new ExternalTypeBuilder(this, className);
    }
    public ExternalTypeBuilder forClass(ClassName className, TypeVariable... typeParams) {
        return new ExternalTypeBuilder(this, className, typeParams);
    }


    public MethodSignature lookupMethod(ClassName owner, String methodName, int argCount) {
        String k = key(owner);
        List<MethodSignature> sigs = methods.get(k);
        if (sigs == null) return null;
        for (MethodSignature sig : sigs) {
            if (sig.name.equals(methodName) && sig.parameterTypes.size() == argCount) {
                return sig;
            }
        }
        return null;
    }

    /**
     * Lookup a method by name and argument types, resolving overloads.
     * Returns the most specific matching signature, or null if none match.
     */
    public MethodSignature lookupMethod(ClassName owner, String methodName,
                                         List<TypeName> argTypes, TypeRegistry registry) {
        String k = key(owner);
        List<MethodSignature> sigs = methods.get(k);
        if (sigs == null) return null;

        MethodSignature bestMatch = null;
        for (MethodSignature sig : sigs) {
            if (!sig.name.equals(methodName)) continue;
            if (sig.parameterTypes.size() != argTypes.size()) continue;
            if (matchesArgTypes(sig, argTypes, registry)) {
                if (bestMatch == null || isMoreSpecific(sig, bestMatch, registry)) {
                    bestMatch = sig;
                }
            }
        }
        return bestMatch;
    }

    private boolean matchesArgTypes(MethodSignature sig, List<TypeName> argTypes, TypeRegistry registry) {
        for (int i = 0; i < sig.parameterTypes.size(); i++) {
            TypeName paramType = sig.parameterTypes.get(i);
            TypeName argType = argTypes.get(i);
            // TypeVariable parameters accept anything
            if (paramType instanceof TypeVariable) continue;
            if (!TypeCompatibility.isAssignable(paramType, argType, registry)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if candidate is more specific than current, i.e. candidate's
     * parameter types are subtypes of current's parameter types.
     */
    private boolean isMoreSpecific(MethodSignature candidate, MethodSignature current, TypeRegistry registry) {
        int score = 0;
        for (int i = 0; i < candidate.parameterTypes.size(); i++) {
            TypeName candParam = candidate.parameterTypes.get(i);
            TypeName currParam = current.parameterTypes.get(i);
            // A non-TypeVariable param is more specific than a TypeVariable param
            if (!(candParam instanceof TypeVariable) && currParam instanceof TypeVariable) {
                score++;
            } else if (candParam instanceof TypeVariable && !(currParam instanceof TypeVariable)) {
                score--;
            } else if (!(candParam instanceof TypeVariable) && !(currParam instanceof TypeVariable)) {
                // Both concrete: candidate is more specific if its param is a subtype of current's param
                if (TypeCompatibility.isAssignable(currParam, candParam, registry)
                        && !TypeCompatibility.isAssignable(candParam, currParam, registry)) {
                    score++;
                } else if (TypeCompatibility.isAssignable(candParam, currParam, registry)
                        && !TypeCompatibility.isAssignable(currParam, candParam, registry)) {
                    score--;
                }
            }
        }
        return score > 0;
    }

    public TypeName lookupField(ClassName owner, String fieldName) {
        String k = key(owner);
        Map<String, TypeName> fieldMap = fields.get(k);
        if (fieldMap == null) return null;
        return fieldMap.get(fieldName);
    }

    public MethodSignature lookupConstructor(ClassName owner, int argCount) {
        String k = key(owner);
        List<MethodSignature> sigs = constructors.get(k);
        if (sigs == null) return null;
        for (MethodSignature sig : sigs) {
            if (sig.parameterTypes.size() == argCount) {
                return sig;
            }
        }
        return null;
    }

    public void registerTypeParams(ClassName owner, List<TypeVariable> params) {
        if (params != null && !params.isEmpty()) {
            typeParams.put(key(owner), new ArrayList<>(params));
        }
    }

    public List<TypeVariable> lookupTypeParams(ClassName owner) {
        List<TypeVariable> result = typeParams.get(key(owner));
        return (result != null) ? result : Collections.emptyList();
    }

    public boolean isKnown(ClassName owner) {
        String k = key(owner);
        return methods.containsKey(k) || fields.containsKey(k) || constructors.containsKey(k);
    }

    public static ExternalTypeRegistry initializeExternalRegistry(ExternalTypeRegistry externalRegistry) {
        externalRegistry.forClass(PROV_FACTORY)
                .method("newQualifiedName", ClassName.PROV_QUALIFIED_NAME, STRING, STRING, STRING)
                .method("newDocument", PROV_DOCUMENT)
                .method("newNamedBundle", ClassName.PROV_BUNDLE, PROV_QUALIFIED_NAME, PROV_NAMESPACE, PROV_STATEMENT) // notes, should be Collection<Statement> but we want to allow any for now
                .constructor()
                .register();

        externalRegistry.forClass(PROV_BUNDLE)
                .method("addStatement", VOID, PROV_STATEMENT)
                .register();

        externalRegistry.forClass(ClassName.PROV_QUALIFIED_NAME)
                .field("namespaceURI", STRING)
                .field("localPart", STRING)
                .field("prefix", STRING)
                .method("getUri", STRING)
                .register();

        externalRegistry.forClass(PROV_NAMESPACE)
                .method("stringToQualifiedName", PROV_QUALIFIED_NAME, STRING, PROV_FACTORY)
                .method("register", VOID, STRING, STRING)
                .constructor()
                .register();

        externalRegistry.forClass(PROV_FILE_BUILDER)
                .method("toInt", INTEGER, OBJECT)
                .method("toDouble", DOUBLE, OBJECT)
                .method("toFloat", FLOAT, OBJECT)
                .method("toLong", LONG, OBJECT)
                .method("toBoolean", BOOLEAN, OBJECT)
                .method("registerBuilders", _bool, STRING_ARRAY, PROV_FACTORY)
                .register();

        externalRegistry.forClass(ClassName.MAP, TypeVariable.get("alpha"), TypeVariable.get("beta"))
                .method("get", TypeVariable.get("beta"), TypeVariable.get("alpha")) // get(alpha) -> beta
                .method("put", TypeVariable.get("beta"), TypeVariable.get("alpha"), TypeVariable.get("beta")) // put(alpha, beta) -> beta
                .method("containsKey", BOOLEAN, TypeVariable.get("alpha"))
                .register();

        externalRegistry.forClass(BUILDER_INTERFACE)
                .register();

        externalRegistry.forClass(ClassName.PAIR, TypeVariable.get("alpha"), TypeVariable.get("beta"))
                .method("getLeft", TypeVariable.get("alpha"))
                .method("getRight", TypeVariable.get("beta"))
                .register();

        externalRegistry.forClass(ClassName.LIST, TypeVariable.get("alpha"))
                .method("size", INTEGER)
                .method("get", TypeVariable.get("alpha"), INTEGER)
                .register();
        externalRegistry.forClass(ClassName.LINKED_LIST, TypeVariable.get("alpha"))
                .method("size", INTEGER)
                .method("get", TypeVariable.get("alpha"), INTEGER)
                .constructor()
                .register();


        externalRegistry.forClass(ClassName.SET, TypeVariable.get("alpha"))
                .method("size", INTEGER)
                .method("contains", BOOLEAN, OBJECT)
                .method("add", BOOLEAN, TypeVariable.get("alpha"))
                .register();

        externalRegistry.forClass(ClassName.HASH_SET, TypeVariable.get("alpha"))
                .method("size", INTEGER)
                .method("contains", BOOLEAN, OBJECT)
                .method("add", BOOLEAN, TypeVariable.get("alpha"))
                .constructor()
                .register();




        externalRegistry.forClass(ClassName.SYSTEM)
                .field("out", ClassName.PRINT_STREAM)
                .register();

        externalRegistry.forClass(ClassName.PRINT_STREAM)
                .method("println", VOID, OBJECT)
                .register();

        externalRegistry.forClass(ClassName.OBJECT)
                .register();


        externalRegistry.forClass(STRING)
                .method("length", INTEGER)
                .method("substring", STRING, INTEGER, INTEGER)
                .method("substring", STRING, INTEGER)
                .method("indexOf", INTEGER, STRING)
                .method("replace", STRING, STRING, STRING)
                .staticMethod("concat", STRING, STRING, STRING)
                .staticMethod("concat", STRING, STRING, STRING, STRING)
                .register();

        externalRegistry.forClass(STRING_BUILDER)
                .method("append", STRING_BUILDER, STRING)
                .method("append", STRING_BUILDER, OBJECT)
                .method("toString", STRING)
                .constructor()
                .register();

        externalRegistry.forClass(HASHMAP, TypeVariable.get("alpha"), TypeVariable.get("beta"))
                .method("put", TypeVariable.get("beta"), TypeVariable.get("alpha"), TypeVariable.get("beta")) // put(alpha, beta) -> beta
                .method("get", TypeVariable.get("beta"), TypeVariable.get("alpha")) // get(alpha) -> beta
                .method("containsKey", BOOLEAN, TypeVariable.get("alpha"))
                .constructor()
                .register();

        externalRegistry.forClass(TRIFUNCTION, TypeVariable.get("alpha"), TypeVariable.get("beta"), TypeVariable.get("gamma"), TypeVariable.get("delta"))
                .method("apply", TypeVariable.get("delta"), TypeVariable.get("alpha"), TypeVariable.get("beta"), TypeVariable.get("gamma"))
                .register();
        externalRegistry.forClass(BIFUNCTION, TypeVariable.get("alpha"), TypeVariable.get("beta"), TypeVariable.get("gamma"))
                .method("apply", TypeVariable.get("gamma"), TypeVariable.get("alpha"), TypeVariable.get("beta"))
                .register();
        externalRegistry.forClass(FUNCTION, TypeVariable.get("alpha"), TypeVariable.get("beta"))
                .method("apply", TypeVariable.get("beta"), TypeVariable.get("alpha"))
                .register();

        externalRegistry.forClass(PROV_FRAMEWORK)
                .method("getFactory", PROV_FACTORY)
                .staticMethod("dynamicLoad", PROV_FRAMEWORK)
                .register();

        externalRegistry.forClass(INTEGER)
                .staticMethod("valueOf", INTEGER, STRING)
                .field("class", ClassName.CLASS)
                .register();

        externalRegistry.forClass(DOUBLE)
                .staticMethod("valueOf", DOUBLE, STRING)
                .register();
        externalRegistry.forClass(FLOAT)
                .staticMethod("valueOf", FLOAT, STRING)
                .register();
        externalRegistry.forClass(LONG)
                .staticMethod("valueOf", LONG, STRING)
                .register();
        externalRegistry.forClass(BOOLEAN)
                .staticMethod("valueOf", BOOLEAN, STRING)
                .register();
        externalRegistry.forClass(PROV_INSTANTIATE_ACTION)
                .staticMethod("getUUIDQualifiedName2", PROV_QUALIFIED_NAME, PROV_FACTORY)
                .register();

        externalRegistry.forClass(RESULT_SET)
                .method("next", BOOLEAN)
                .method("close", VOID)
                .method("getInt", INTEGER, _int)
                .method("getString", STRING, _int)
                .method("getRow", _int)
                .method("getMetaData", RESULT_SET_META_DATA)
                .register();
        externalRegistry.forClass(RESULT_SET_META_DATA)
                .method("getColumnCount", INTEGER)
                .method("getColumnName", STRING, _int)
                .register();


        externalRegistry.forClass(ClassName.get("Plead_transformingBean","org.openprovenance.prov.template.library.plead.client.common"))
                .field("class", ParameterizedType.get(CLASS))
                .register();
        return externalRegistry;
    }
    public static final ClassName CLASSNAME = ClassName.get( "ClassName", "past.lang");

}
