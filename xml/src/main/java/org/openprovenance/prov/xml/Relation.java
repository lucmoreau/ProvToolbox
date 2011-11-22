package org.openprovenance.prov.xml;

public interface Relation extends Identifiable, HasExtensibility  {
    Ref getCause();
    Ref getEffect();
} 