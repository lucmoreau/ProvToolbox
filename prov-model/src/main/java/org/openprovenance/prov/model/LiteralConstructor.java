package org.openprovenance.prov.model;

public interface LiteralConstructor {

    Object newQName(String value);

    Object newGYear(String value);

    Object newISOTime(String value);

}
