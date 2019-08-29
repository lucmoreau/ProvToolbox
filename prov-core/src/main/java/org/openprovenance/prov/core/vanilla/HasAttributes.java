package org.openprovenance.prov.core.vanilla;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface HasAttributes {
    Collection<Attribute> getAttributes () ;
    Map<QualifiedName, Set<Attribute>> getIndexedAttributes () ;
}
