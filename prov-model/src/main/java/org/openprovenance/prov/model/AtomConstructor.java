package org.openprovenance.prov.model;

import org.openprovenance.prov.model.QualifiedName;

public interface AtomConstructor {
    public org.openprovenance.prov.model.Role newRole(Object value, org.openprovenance.prov.model.QualifiedName type);

    public org.openprovenance.prov.model.Location newLocation(Object value, org.openprovenance.prov.model.QualifiedName type);

    public org.openprovenance.prov.model.Type newType(Object value, org.openprovenance.prov.model.QualifiedName type);

    public org.openprovenance.prov.model.Label newLabel(Object value, org.openprovenance.prov.model.QualifiedName type);

    public org.openprovenance.prov.model.LangString newInternationalizedString(String s);

    public org.openprovenance.prov.model.LangString newInternationalizedString(String s, String lang);

    public org.openprovenance.prov.model.Value newValue(Object value, org.openprovenance.prov.model.QualifiedName type);

    public org.openprovenance.prov.model.Other newOther(QualifiedName elementName, Object value, QualifiedName type);


}
