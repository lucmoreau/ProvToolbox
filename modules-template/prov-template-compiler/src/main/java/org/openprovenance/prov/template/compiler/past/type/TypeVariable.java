package org.openprovenance.prov.template.compiler.past.type;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TypeVariable extends TypeName {
    public final String name;
    public final List<TypeName> bounds=new LinkedList<>();

    public TypeVariable(String name) {
        this.name = name;
        this.typeKind=TypeKind.VARIABLE;
    }

    static public TypeVariable get(String name) {
        if (name==null) {
            throw new IllegalArgumentException("Type variable name cannot be null");
        }
        return new TypeVariable(name);
    }

    static public TypeVariable T() {
        return get("T");
    }

    @Override
    public String toString() {
        return "TypeVariable{" +
                "name='" + name + '\'' +
                ", bounds=" + bounds +
                ", typeKind=" + typeKind +
                '}';
    }

    public TypeVariable withBounds(TypeName... bounds) {
        return this.withBounds(Arrays.asList(bounds));
    }

    public TypeVariable withBounds(List<? extends TypeName> bounds) {
        this.bounds.addAll(bounds);
        return this;
    }
}
