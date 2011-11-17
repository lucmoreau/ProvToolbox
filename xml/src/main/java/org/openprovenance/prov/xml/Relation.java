package org.openprovenance.prov.xml;

public interface Relation extends HasExtensibility  {
    Ref getCause();
    Ref getEffect();
} 