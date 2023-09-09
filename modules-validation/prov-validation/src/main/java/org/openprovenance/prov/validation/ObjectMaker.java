package org.openprovenance.prov.validation;

import org.openprovenance.prov.model.QualifiedName;

public interface ObjectMaker {
    VarQName makeVarQName(String namespaceURI, String localPart, String prefix);

    VarQName makeVarQName(QualifiedName stringToQualifiedName);

    Unknown makeUnknown(String this_VAL_URI, String valPrefix);

}
