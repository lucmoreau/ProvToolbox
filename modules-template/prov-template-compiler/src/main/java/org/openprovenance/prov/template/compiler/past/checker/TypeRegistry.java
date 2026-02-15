package org.openprovenance.prov.template.compiler.past.checker;

import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Constructor;
import org.openprovenance.prov.template.compiler.past.Field;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.Parameter;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TypeRegistry {
    private final Map<String, ClassSignature> registry = new HashMap<>();
    private final Map<String, Class> pastClasses = new HashMap<>();
    private final Map<String, String> packageNames = new HashMap<>();
    private final ExternalTypeRegistry externalRegistry;

    public TypeRegistry() {
        this.externalRegistry = new ExternalTypeRegistry();
    }

    public TypeRegistry(ExternalTypeRegistry externalRegistry) {
        this.externalRegistry = (externalRegistry != null) ? externalRegistry : new ExternalTypeRegistry();
    }

    private static String key(String simpleName, String packageName) {
        return packageName + "." + simpleName;
    }

    private static String key(ClassName className) {
        return className.packge + "." + className.simpleName;
    }

    // --- Pass 1: register a PAST Class ---

    public void registerClass(Class pastClass, String packageName) {
        String k = key(pastClass.name, packageName);

        // Extract field signatures
        Map<String, ClassSignature.FieldInfo> fieldMap = new LinkedHashMap<>();
        for (Field f : pastClass.fields) {
            Set<Modifier> mods = f.modifiers.isEmpty()
                    ? Set.of()
                    : EnumSet.copyOf(f.modifiers);
            fieldMap.put(f.name, new ClassSignature.FieldInfo(f.type, mods));
        }

        // Extract method signatures
        List<MethodSignature> methodSigs = new ArrayList<>();
        for (Method m : pastClass.methods) {
            List<TypeName> paramTypes = m.parameters.stream()
                    .map(p -> p.type)
                    .collect(Collectors.toList());
            Set<Modifier> mods = m.modifiers.isEmpty()
                    ? Set.of()
                    : EnumSet.copyOf(m.modifiers);
            methodSigs.add(new MethodSignature(
                    m.name, paramTypes, m.returnType,
                    m.typeVariables, m.isStatic(), mods));
        }

        // Extract constructor signatures
        List<MethodSignature> ctorSigs = new ArrayList<>();
        ClassName selfType = ClassName.get(pastClass.name, packageName);
        for (Constructor c : pastClass.constructors) {
            List<TypeName> paramTypes = c.parameters.stream()
                    .map(p -> p.type)
                    .collect(Collectors.toList());
            Set<Modifier> mods = c.modifiers.isEmpty()
                    ? Set.of()
                    : EnumSet.copyOf(c.modifiers);
            ctorSigs.add(new MethodSignature(
                    "<init>", paramTypes, selfType,
                    List.of(), false, mods));
        }

        // Extract type variables
        List<TypeName> typeVars = new ArrayList<>(pastClass.typeVariables);

        ClassSignature sig = new ClassSignature(
                pastClass.name, packageName, pastClass.isInterface,
                pastClass.superclass, new ArrayList<>(pastClass.interfaces),
                fieldMap, methodSigs, ctorSigs, typeVars);

        registry.put(k, sig);
        pastClasses.put(k, pastClass);
        packageNames.put(k, packageName);
    }

    // --- Lookup ---

    public ClassSignature lookup(ClassName className) {
        return registry.get(key(className));
    }

    public ClassSignature lookup(String simpleName, String packageName) {
        return registry.get(key(simpleName, packageName));
    }

    public MethodSignature lookupMethod(ClassName className, String methodName, int argCount) {
        // Check PAST registry first
        ClassSignature sig = lookup(className);
        if (sig != null) {
            for (MethodSignature ms : sig.methods) {
                if (ms.name.equals(methodName) && ms.parameterTypes.size() == argCount) {
                    return ms;
                }
            }
            // Walk superclass chain
            if (sig.superclass instanceof ClassName) {
                ClassName superCn = (ClassName) sig.superclass;
                MethodSignature inherited = lookupMethod(superCn, methodName, argCount);
                if (inherited != null) return inherited;
            }
            // Walk interfaces
            for (TypeName iface : sig.interfaces) {
                ClassName ifaceCn = resolveClassName(iface);
                if (ifaceCn != null) {
                    MethodSignature fromIface = lookupMethod(ifaceCn, methodName, argCount);
                    if (fromIface != null) return fromIface;
                }
            }
            return null;
        }
        // Fall back to external registry
        return externalRegistry.lookupMethod(className, methodName, argCount);
    }

    /**
     * Lookup a method by name and argument types, resolving overloads by picking
     * the most specific matching signature.
     */
    public MethodSignature lookupMethod(ClassName className, String methodName, List<TypeName> argTypes) {
        // Check PAST registry first
        ClassSignature sig = lookup(className);
        if (sig != null) {
            MethodSignature best = findBestMatch(sig.methods, methodName, argTypes);
            if (best != null) return best;
            // Walk superclass chain
            if (sig.superclass instanceof ClassName) {
                ClassName superCn = (ClassName) sig.superclass;
                MethodSignature inherited = lookupMethod(superCn, methodName, argTypes);
                if (inherited != null) return inherited;
            }
            // Walk interfaces
            for (TypeName iface : sig.interfaces) {
                ClassName ifaceCn = resolveClassName(iface);
                if (ifaceCn != null) {
                    MethodSignature fromIface = lookupMethod(ifaceCn, methodName, argTypes);
                    if (fromIface != null) return fromIface;
                }
            }
            return null;
        }
        // Fall back to external registry
        return externalRegistry.lookupMethod(className, methodName, argTypes, this);
    }

    private MethodSignature findBestMatch(List<MethodSignature> methods, String methodName, List<TypeName> argTypes) {
        MethodSignature bestMatch = null;
        for (MethodSignature ms : methods) {
            if (!ms.name.equals(methodName)) continue;
            if (ms.parameterTypes.size() != argTypes.size()) continue;
            if (matchesArgTypes(ms, argTypes)) {
                if (bestMatch == null || isMoreSpecific(ms, bestMatch)) {
                    bestMatch = ms;
                }
            }
        }
        return bestMatch;
    }

    private boolean matchesArgTypes(MethodSignature sig, List<TypeName> argTypes) {
        for (int i = 0; i < sig.parameterTypes.size(); i++) {
            TypeName paramType = sig.parameterTypes.get(i);
            TypeName argType = argTypes.get(i);
            // TypeVariable parameters accept anything
            if (paramType instanceof TypeVariable) continue;
            if (!TypeCompatibility.isAssignable(paramType, argType, this)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if candidate is more specific than current.
     */
    private boolean isMoreSpecific(MethodSignature candidate, MethodSignature current) {
        int score = 0;
        for (int i = 0; i < candidate.parameterTypes.size(); i++) {
            TypeName candParam = candidate.parameterTypes.get(i);
            TypeName currParam = current.parameterTypes.get(i);
            if (!(candParam instanceof TypeVariable) && currParam instanceof TypeVariable) {
                score++;
            } else if (candParam instanceof TypeVariable && !(currParam instanceof TypeVariable)) {
                score--;
            } else if (!(candParam instanceof TypeVariable) && !(currParam instanceof TypeVariable)) {
                if (TypeCompatibility.isAssignable(currParam, candParam, this)
                        && !TypeCompatibility.isAssignable(candParam, currParam, this)) {
                    score++;
                } else if (TypeCompatibility.isAssignable(candParam, currParam, this)
                        && !TypeCompatibility.isAssignable(currParam, candParam, this)) {
                    score--;
                }
            }
        }
        return score > 0;
    }

    public TypeName lookupField(ClassName className, String fieldName) {
        ClassSignature sig = lookup(className);
        if (sig != null) {
            ClassSignature.FieldInfo fi = sig.fields.get(fieldName);
            if (fi != null) return fi.type;
            // Walk superclass chain
            if (sig.superclass instanceof ClassName) {
                TypeName inherited = lookupField((ClassName) sig.superclass, fieldName);
                if (inherited != null) return inherited;
            }
            return null;
        }
        return externalRegistry.lookupField(className, fieldName);
    }

    public ClassSignature.FieldInfo lookupFieldInfo(ClassName className, String fieldName) {
        ClassSignature sig = lookup(className);
        if (sig != null) {
            ClassSignature.FieldInfo fi = sig.fields.get(fieldName);
            if (fi != null) return fi;
            if (sig.superclass instanceof ClassName) {
                return lookupFieldInfo((ClassName) sig.superclass, fieldName);
            }
        }
        return null;
    }

    public MethodSignature lookupConstructor(ClassName className, int argCount) {
        ClassSignature sig = lookup(className);
        if (sig != null) {
            for (MethodSignature cs : sig.constructors) {
                if (cs.parameterTypes.size() == argCount) {
                    return cs;
                }
            }
            return null;
        }
        return externalRegistry.lookupConstructor(className, argCount);
    }

    public boolean isKnown(ClassName className) {
        return registry.containsKey(key(className)) || externalRegistry.isKnown(className);
    }

    public ExternalTypeRegistry getExternalRegistry() {
        return externalRegistry;
    }

    public Class getPastClass(String simpleName, String packageName) {
        return pastClasses.get(key(simpleName, packageName));
    }

    public Map<String, ClassSignature> getAllSignatures() {
        return Map.copyOf(registry);
    }

    // --- Helpers ---

    private static ClassName resolveClassName(TypeName type) {
        if (type instanceof ClassName) return (ClassName) type;
        if (type instanceof ParameterizedType) return ((ParameterizedType) type).getRawType();
        return null;
    }
}
