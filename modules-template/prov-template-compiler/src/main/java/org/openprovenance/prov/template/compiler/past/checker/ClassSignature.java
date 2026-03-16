package org.openprovenance.prov.template.compiler.past.checker;

import org.openprovenance.prov.template.compiler.past.type.TypeName;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassSignature {
    public final String name;
    public final String packageName;
    public final boolean isInterface;
    public final TypeName superclass;
    public final List<TypeName> interfaces;
    public final Map<String, FieldInfo> fields;
    public final List<MethodSignature> methods;
    public final List<MethodSignature> constructors;
    public final List<TypeName> typeVariables;

    public ClassSignature(String name, String packageName, boolean isInterface, TypeName superclass,
                          List<TypeName> interfaces, Map<String, FieldInfo> fields,
                          List<MethodSignature> methods, List<MethodSignature> constructors,
                          List<TypeName> typeVariables) {
        this.name = name;
        this.packageName = packageName;
        this.isInterface = isInterface;
        this.superclass = superclass;
        this.interfaces = Collections.unmodifiableList(interfaces);
        this.fields = Collections.unmodifiableMap(fields);
        this.methods = Collections.unmodifiableList(methods);
        this.constructors = Collections.unmodifiableList(constructors);
        this.typeVariables = Collections.unmodifiableList(typeVariables);
    }

    public String qualifiedName() {
        return packageName + "." + name;
    }

    public static class FieldInfo {
        public final TypeName type;
        public final Set<Modifier> modifiers;

        public FieldInfo(TypeName type, Set<Modifier> modifiers) {
            this.type = type;
            this.modifiers = (modifiers != null) ? Collections.unmodifiableSet(modifiers) : Collections.emptySet();
        }

        @Override
        public String toString() {
            return "FieldInfo{type=" + type + ", modifiers=" + modifiers + '}';
        }
    }

    @Override
    public String toString() {
        return "ClassSignature{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", isInterface=" + isInterface +
                ", methods=" + methods.size() +
                ", fields=" + fields.size() +
                '}';
    }
}
