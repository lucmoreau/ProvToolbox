package org.openprovenance.prov.template.emitter;

import java.util.List;

import static org.openprovenance.prov.template.emitter.Expression.makeExpression;

public class Return extends Statement {
    private final Expression value;

    public Return(List<Element> value, List<Element> elements) {
        super(elements);
        System.out.println("+++Return: " + value);
        this.value=makeExpression(value);
    }



    @Override
    public String toString() {
        return "Return{" +
                "value=" + value +
                '}';
    }
}
