package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.type.TypeName;

public class Parameter {
    public String name;
    public TypeName type;

    public Parameter(String name, TypeName type) {
        this.name=name;
        this.type=type;
    }

    public static Parameter PARAMETER(String name, TypeName type) {
        return new Parameter(name,type);
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
