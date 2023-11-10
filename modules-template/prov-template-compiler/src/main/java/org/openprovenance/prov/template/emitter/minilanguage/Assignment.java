package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.LinkedList;
import java.util.List;

public class Assignment extends Statement {
    private final Object type;
    private final Expression variable;
    private final Expression value;

    public Assignment(Object type, Expression variable, Expression value, List<Element> elementList) {
        super(elementList);
        this.type = type;
        this.variable = variable;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "type=" + type +
                ", variable=" + variable +
                ", value=" + value +
                '}';
    }

    public void emit(Python emitter, List<String> classVariables, List<String> instanceVariables) {
        if (variable instanceof Symbol && ((Symbol) variable).symbol.equals("self")) {
            return;
        }
        emitter.emitBeginLine("");
        variable.emit(emitter, true, classVariables, instanceVariables);
        emitter.emitContinueLine("=");
        value.emit(emitter, true, classVariables, instanceVariables);
        emitter.emitNewline();
    }
}
