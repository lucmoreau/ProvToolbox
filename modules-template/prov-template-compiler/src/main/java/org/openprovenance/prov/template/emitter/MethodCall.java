package org.openprovenance.prov.template.emitter;

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
}
