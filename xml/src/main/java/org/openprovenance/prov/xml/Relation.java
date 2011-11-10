package org.openprovenance.prov.xml;

public interface Relation extends HasAttributes, HasAccounts {
    Ref getCause();
    Ref getEffect();
} 