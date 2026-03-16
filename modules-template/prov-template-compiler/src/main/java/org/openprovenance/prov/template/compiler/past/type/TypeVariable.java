package org.openprovenance.prov.template.compiler.past.type;

public class TypeVariable extends TypeName {
    public final String name;

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
                '}';
    }
}
