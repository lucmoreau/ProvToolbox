package org.openprovenance.prov.model.builder;

public class Prefix {
    private final String prefix;
    private final Builder builder;

    public Prefix(Builder builder, String pref) {
        this.prefix=pref;
        this.builder=builder;
    }

    public String get() {
        return prefix;
    }
}
