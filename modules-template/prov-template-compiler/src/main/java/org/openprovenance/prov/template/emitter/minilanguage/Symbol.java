package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.List;

public class Symbol extends Expression {
    private final String symbol;

    public Symbol(String symbol, List<Element> elements) {
        super(elements);
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "symbol='" + symbol + '\'' +
                '}';
    }

    public void emit(Python emitter, boolean continueLine, List<String> locals) {
        emitter.emitLine(localized(symbol,locals),continueLine);
    }
}
