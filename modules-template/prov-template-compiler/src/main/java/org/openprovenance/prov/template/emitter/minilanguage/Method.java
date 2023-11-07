package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import javax.lang.model.element.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Method {
    public String name;
    public String returnType;
    public List<Parameter> parameters;
    public List<Statement> body;
    public Set<Modifier> modifiers;


    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", parameters=" + parameters +
                ", body=" + body +
                '}';
    }

    public boolean isStatic() {
        return modifiers.contains(Modifier.STATIC);
    }

    public void emit(Python emitter, List<String> classVariables, List<String> instanceVariables) {
        if (isStatic()) {
            emitter.emitBeginLine("@classmethod");
            emitter.emitNewline();
        }
        emitter.emitBeginLine("def ");
        emitter.emitContinueLine(name);
        emitter.emitContinueLine("(");

        if (isStatic()) {
            emitter.emitContinueLine("cls");
        } else {
            emitter.emitContinueLine("self");
        }
        // in python, add self as first parameter
        for (Parameter p: parameters) {
            emitter.emitContinueLine(",");
            emitter.emitContinueLine(p.name);
        }
        emitter.emitContinueLine("):");
        emitter.emitNewline();
        emitter.indent();
        for (Statement s: body) {
            s.emit(emitter, classVariables, instanceVariables);
        }
        emitter.unindent();
        emitter.emitNewline();
    }
}
