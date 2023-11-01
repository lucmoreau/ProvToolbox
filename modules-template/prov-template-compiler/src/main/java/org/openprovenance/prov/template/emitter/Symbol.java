package org.openprovenance.prov.template.emitter;

import java.util.List;

public class Symbol extends Expression {
    private final String arg;

    public Symbol(String arg, List<Element> elements) {
        super(elements);
        this.arg=arg;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "arg='" + arg + '\'' +
                '}';
    }
}
