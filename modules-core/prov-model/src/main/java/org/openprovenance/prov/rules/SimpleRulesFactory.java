package org.openprovenance.prov.rules;

public class SimpleRulesFactory implements RulesFactory {
    public SimpleRulesFactory() {
    }

    public Rules newRules() {
        return new Rules();
    }
}