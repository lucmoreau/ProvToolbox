package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

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

    public void emit(Python emitter) {
        emitter.emitBeginLine("def ");
        emitter.emitContinueLine(name);
        emitter.emitContinueLine("(");
        boolean first=true;
        // in python, add self as first parameter
        emitter.emitContinueLine("self");
        for (Parameter p: parameters) {
            emitter.emitContinueLine(",");
            emitter.emitContinueLine(p.name);
        }
        emitter.emitContinueLine("):");
        emitter.emitNewline();
        emitter.indent();
        for (Statement s: body) {
            s.emit(emitter);
        }
        emitter.unindent();
    }
}
