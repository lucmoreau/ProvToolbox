package org.openprovenance.prov.template.emitter;

import java.util.List;

public class Constructor extends Expression {
    private final String type;

    public Constructor(String type, List<Element> elements) {
        super(elements);
        this.type = type;
    }

    @Override
    public String toString() {
        return "Constructor{" +
                "type='" + type + '\'' +
                '}';
    }
}
