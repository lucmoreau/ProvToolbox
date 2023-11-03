package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.List;

public class Lambda extends Expression {
    final List<Parameter> parameters;
    final List<Statement> body;

    public Lambda(List<Parameter> parameters, List<Statement> body, List<Element> elements1) {
        super(elements1);
        this.body = body;
        this.parameters=parameters;
    }

    @Override
    public String toString() {
        return "Lambda{" +
                "parameters=" + parameters +
                ", body=" + body +
                '}';
    }

    public void emit(Python emitter, boolean continueLine) {
        // ahahah ... lambdas cannot be multiline
        emitter.emitLine("lambda ", continueLine);
        boolean first=true;
        for (Parameter p: parameters) {
            if (!first) {
                emitter.emitContinueLine(",");
            }
            first=false;
            emitter.emitContinueLine(p.name);
        }
        emitter.emitContinueLine(": ");
        emitter.emitNewline();
        emitter.indent();
        for (Statement s: body) {
            s.emit(emitter);
        }
        emitter.unindent();
        emitter.emitNewline();
    }
}
