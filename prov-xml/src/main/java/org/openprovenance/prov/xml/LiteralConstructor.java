package org.openprovenance.prov.xml;

public interface LiteralConstructor {

    Object newQName(String value);

    Object newGYear(String value);

    Object newISOTime(String value);

}
