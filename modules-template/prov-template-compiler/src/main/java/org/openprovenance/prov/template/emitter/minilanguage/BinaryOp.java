package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.List;

public class BinaryOp extends Expression {
    private final Object left;
    private final String op;
    private final Object right;

    public BinaryOp(Object left, String op, Object right, List<Element> elements) {
        super(elements);
        this.left=left;
        this.op=op;
        this.right=right;
    }

    public void emit(Python emitter, boolean continueLine, List<String> classVariables,  List<String> instanceVariables) {
        emitter.emitLine("(" + left.toString(),continueLine);
        if (right==null) {
            emitter.emitContinueLine(" is ");
            emitter.emitContinueLine("None");
        } else {
            emitter.emitContinueLine(" "+op+" ");
            emitter.emitContinueLine(localized(right.toString(),classVariables, instanceVariables));
        }
        emitter.emitContinueLine(")");
    }
}
