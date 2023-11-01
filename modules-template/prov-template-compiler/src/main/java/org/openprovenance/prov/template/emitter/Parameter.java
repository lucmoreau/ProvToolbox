package org.openprovenance.prov.template.emitter;

public class Parameter {
    public String name;
    public String type;

    public Parameter(String name, String type) {
        this.name=name;
        this.type=type;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
