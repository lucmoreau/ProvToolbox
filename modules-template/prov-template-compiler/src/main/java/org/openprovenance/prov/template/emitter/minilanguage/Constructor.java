package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

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

    public void emit(Python emitter, boolean continueLine, List<String> locals) {
        if (type.equals("java.lang.StringBuffer")) {
            emitter.emitLine( "[]",continueLine);
        } else {
            emitter.emitLine(type + "()", continueLine);
        }
    }
}
