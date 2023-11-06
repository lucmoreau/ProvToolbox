package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.List;

public class Conditional extends Statement{
    public Expression predicate;
    public List<Statement> consequents;
    public List<Statement> alternates;

    public Conditional(List<Element> elements) {
        super(elements);
    }

    @Override
    public void emit(Python emitter, List<String> classVariables) {
        emitter.emitBeginLine("if ");
        predicate.emit(emitter, true, classVariables);
        emitter.emitContinueLine(":");
        emitter.emitNewline();
        emitter.indent();
        if (consequents!=null) {

            for (Statement consequent : consequents) {
                consequent.emit(emitter, classVariables);
            }
            emitter.emitLine("pass");
        }
        emitter.unindent();
        if (alternates!=null && !alternates.isEmpty()) {
            emitter.emitBeginLine("else:");
            emitter.emitNewline();
            emitter.indent();
            for (Statement alternate: alternates) {
                alternate.emit(emitter, classVariables);
            }
            emitter.unindent();
        }
    }

    @Override
    public String toString() {
        return "Conditional{" +
                "predicate=" + predicate +
                ", consequents=" + consequents +
                ", alternates=" + alternates +
                '}';
    }
}
