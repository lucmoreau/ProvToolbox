package org.openprovenance.prov.template.emitter;

import java.util.List;

public class Lambda extends Expression {
    private final List<Statement> body;

    public Lambda(List<Statement> body, List<Element> elements1) {
        super(elements1);
        this.body = body;
    }

    @Override
    public String toString() {
        return "Lambda{" +
                "elements=" + body +
                '}';
    }
}
