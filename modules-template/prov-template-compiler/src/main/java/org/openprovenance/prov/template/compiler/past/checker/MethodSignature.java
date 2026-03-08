package org.openprovenance.prov.template.compiler.past.checker;

import org.openprovenance.prov.template.compiler.past.annotations.PastAnnotation;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MethodSignature {
    public final String name;
    public final List<TypeName> parameterTypes;
    public final TypeName returnType;
    public final List<TypeName> typeVariables;
    public final boolean isStatic;
    public final Set<Modifier> modifiers;
    public final List<PastAnnotation> annotations=new ArrayList<>();

    public MethodSignature(String name, List<TypeName> parameterTypes, TypeName returnType,
                           List<TypeName> typeVariables, boolean isStatic, Set<Modifier> modifiers) {
        this.name = name;
        this.parameterTypes = Collections.unmodifiableList(parameterTypes);
        this.returnType = returnType;
        this.typeVariables = (typeVariables != null) ? Collections.unmodifiableList(typeVariables) : Collections.emptyList();
        this.isStatic = isStatic;
        this.modifiers = (modifiers != null) ? Collections.unmodifiableSet(modifiers) : Collections.emptySet();
    }

    public MethodSignature(String name, List<TypeName> parameterTypes, TypeName returnType,
                           List<TypeName> typeVariables, boolean isStatic) {
        this(name, parameterTypes, returnType, typeVariables, isStatic, Collections.emptySet());
    }

    public List<PastAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return "MethodSignature{" +
                "name='" + name + '\'' +
                ", parameterTypes=" + parameterTypes +
                ", returnType=" + returnType +
                ", typeVariables=" + typeVariables +
                ", isStatic=" + isStatic +
                ", modifiers=" + modifiers +
                ", annotations=" + annotations +
                '}';
    }
}
