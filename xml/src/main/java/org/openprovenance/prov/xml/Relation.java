package org.openprovenance.prov.xml;

public interface Relation extends HasAttributes  {
    Ref getCause();
    Ref getEffect();
} 