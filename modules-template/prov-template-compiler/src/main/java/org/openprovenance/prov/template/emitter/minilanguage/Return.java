package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.LinkedList;
import java.util.List;

import static org.openprovenance.prov.template.emitter.minilanguage.Expression.makeExpression;

public class Return extends Statement {
    private final Expression value;

    public Return(List<Element> value, List<Element> elements) {
        super(elements);
        this.value=makeExpression(value);
    }



    @Override
    public String toString() {
        return "Return{" +
                "value=" + value +
                '}';
    }

    static int lambdaCount=0;

    public void emit(Python emitter, List<String> classVariables, List<String> instanceVariables) {
        // as Python does not accept multiline lambdas, name it first, then return it.
        if (value instanceof Lambda) {
            int suffix= (lambdaCount++);
            Lambda lambda=(Lambda) value;
            emitter.emitBeginLine("def lambda" + suffix + " (") ;
            boolean first=true;
            for (Parameter p: lambda.parameters) {
                if (!first) {
                    emitter.emitContinueLine(",");
                }
                first=false;
                emitter.emitContinueLine(p.name);
            }
            emitter.emitContinueLine("):");
            emitter.emitNewline();
            emitter.indent();
            for (Statement s: lambda.body) {
                s.emit(emitter, classVariables, instanceVariables);
            }
            emitter.unindent();
            emitter.emitNewline();
            emitter.emitLine("return lambda"+suffix);
            emitter.emitNewline();

        } else {
            emitter.emitBeginLine("return ");
            value.emit(emitter, true, classVariables, instanceVariables);
            emitter.emitNewline();
        }
    }

}
