package org.openprovenance.prov.core.xml.serialization;

import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import java.util.Map;

public class MisnamedBundle extends org.openprovenance.prov.vanilla.Bundle {
    public Bundle toBundle(ProvFactory pf) {
        QualifiedName oldId = getId();
        //System.out.println("MisnamedBundle.toBundle " + oldId);
        Namespace namespace = getNamespace();
        String prefix = oldId.getPrefix();

        String namespaceURI=null;

        Map<String, String> prefixes = namespace.getPrefixes();
        namespaceURI = prefixes.get(prefix);
        if (namespaceURI==null) {
            namespaceURI = namespace.getParent().getPrefixes().get(prefix);
        }

        QualifiedName newId = pf.newQualifiedName(namespaceURI, oldId.getLocalPart(), prefix);

        Bundle bundle = pf.newNamedBundle(newId, namespace, getStatement());

       // System.out.println("MisnamedBundle.toBundle " + oldId + " -> " + newId);

        return bundle;


    }
}
