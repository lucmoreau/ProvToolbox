package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.List;

public class Constant extends Expression {
    private final String string;

    public Constant(String string, List<Element> elements) {
        super(elements);
        this.string=string;
    }

    @Override
    public String toString() {
        return "Constant{" +
                "string='" + string + '\'' +
                '}';
    }

    public void emit(Python emitter, List<String> classVariables, List<String> instanceVariables) {
        emit(emitter, false,  classVariables, instanceVariables);
    }

    public void emit(Python emitter, boolean continueLine, List<String> classVariables, List<String> instanceVariables) {
        emitter.emitLine("'" + string + "'",continueLine);
    }
}
