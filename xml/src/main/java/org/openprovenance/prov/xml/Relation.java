package org.openprovenance.prov.xml;

public interface Relation extends Annotable, HasAccounts {
    Ref getCause();
    Ref getEffect();
} 