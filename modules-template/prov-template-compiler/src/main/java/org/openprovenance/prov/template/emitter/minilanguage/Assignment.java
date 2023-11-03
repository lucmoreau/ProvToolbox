package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.List;

public class Assignment extends Statement {
    private final Object type;
    private final String variable;
    private final Expression value;

    public Assignment(Object type, String variable, Expression value, List<Element> elementList) {
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

    public void emit(Python emitter) {
        if (variable.equals("self")) {
            return;
        }
        emitter.emitBeginLine(variable);
        emitter.emitContinueLine("=");
        value.emit(emitter, true);
        emitter.emitNewline();
    }
}
