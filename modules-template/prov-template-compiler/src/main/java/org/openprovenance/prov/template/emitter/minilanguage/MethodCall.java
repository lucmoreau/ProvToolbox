package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.List;

public class MethodCall extends Expression {
    private final Object object;
    private final String methodName;
    private final List<?> arguments;

    public MethodCall(Object object, String methodName, List<?> arguments, List<Element> elements) {
        super(elements);
        this.object=object;
        this.methodName=methodName;
        this.arguments=arguments;
    }

    @Override
    public String toString() {
        return "MethodCall{" +
                "object=" + object +
                ", methodName=" + methodName +
                ", arguments=" + arguments +
                '}';
    }

    public void emit(Python emitter, boolean continueLine) {
        emitter.emitLine(object.toString(),continueLine);
        emitter.emitContinueLine(".");
        emitter.emitContinueLine(methodName);
        emitter.emitContinueLine("(");
        // concat all arguments, with a comma separator
        boolean first=true;
        for (Object argument : arguments) {
            if (first) {
                first=false;
            } else {
                emitter.emitContinueLine(",");
            }
            if (argument instanceof Expression) {
                ((Expression) argument).emit(emitter);
            } else {
                emitter.emitContinueLine(argument.toString());
            }
        }
        emitter.emitContinueLine(")");
        if (!continueLine) {
            emitter.emitNewline();
        }
    }
}
