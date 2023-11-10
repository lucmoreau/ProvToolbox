package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.List;

public class ConstantInteger extends Expression {
    private final Integer integer;

    public ConstantInteger(Integer integer, List<Element> elements) {
        super(elements);
        this.integer = integer;
    }

    @Override
    public String toString() {
        return "ConstantInteger{" +
                "integer='" + integer + '\'' +
                '}';
    }

    public void emit(Python emitter, List<String> classVariables, List<String> instanceVariables) {
        emit(emitter, false,  classVariables, instanceVariables);
    }

    public void emit(Python emitter, boolean continueLine, List<String> classVariables, List<String> instanceVariables) {
        emitter.emitLine( ""+integer ,continueLine);
    }
}
