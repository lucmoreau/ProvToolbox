package org.openprovenance.prov.template.emitter;

import java.util.List;

public class Method {
    public String name;
    public String returnType;
    public List<Parameter> parameters;
    public List<Statement> body;


    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", parameters=" + parameters +
                ", body=" + body +
                '}';
    }
}
