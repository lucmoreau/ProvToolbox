package org.openprovenance.prov.core.jsonld11.serialization;

import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import java.util.Map;

public class MisnamedBundle extends org.openprovenance.prov.vanilla.Bundle {
    public org.openprovenance.prov.model.Bundle toBundle(ProvFactory pf) {
        QualifiedName oldId = getId();
        //System.out.println("MisnamedBundle.toBundle " + oldId);
        Namespace ns = getNamespace();
        String prefix = oldId.getPrefix();

        Map<String, String> prefixes = ns.getPrefixes();
        String namespaceURI = prefixes.get(prefix);
        if (namespaceURI == null) {
            namespaceURI=ns.getParent().getPrefixes().get(prefix);
            if (namespaceURI==null) {
                throw new RuntimeException("Cannot find namespace for prefix " + prefix + " in " + prefixes);
            }
        }
        QualifiedName newId = pf.newQualifiedName(namespaceURI, oldId.getLocalPart(), prefix);

        Bundle bundle = pf.newNamedBundle(newId, ns, getStatement());

       //  System.out.println("----> MisnamedBundle.toBundle " + oldId + " -> " + newId);

        return bundle;


    }
}
