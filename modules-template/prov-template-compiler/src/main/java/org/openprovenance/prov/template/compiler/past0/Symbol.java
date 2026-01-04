package org.openprovenance.prov.template.compiler.past0;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.emitters.Python;

import java.util.List;

import static org.openprovenance.prov.template.compiler.past0.MethodCall.convertName;

public class Symbol extends Expression {
    public final String symbol;

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

    public void emit(Python emitter, boolean continueLine, List<String> classVariables, List<String> instanceVariables) {
        emitter.emitLine(convertName(symbol,classVariables, instanceVariables),continueLine);
    }
}
