package org.openprovenance.prov.core.xml.serialization;

import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import java.util.Map;

public class MisnamedBundle extends org.openprovenance.prov.vanilla.Bundle {
    public Bundle toBundle(ProvFactory pf) {
        QualifiedName oldId = getId();
        Namespace namespace = getNamespace();
        String prefix = oldId.getPrefix();
        String namespaceURI=namespace.lookupPrefix(prefix);
        QualifiedName newId = pf.newQualifiedName(namespaceURI, oldId.getLocalPart(), prefix);
        return pf.newNamedBundle(newId, namespace, getStatement());
    }
}
