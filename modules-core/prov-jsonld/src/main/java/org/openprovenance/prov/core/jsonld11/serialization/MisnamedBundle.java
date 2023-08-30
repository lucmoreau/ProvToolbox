package org.openprovenance.prov.core.jsonld11.serialization;

import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import java.util.Map;

public class MisnamedBundle extends org.openprovenance.prov.vanilla.Bundle {
    public org.openprovenance.prov.model.Bundle toBundle(ProvFactory pf) {
        QualifiedName oldId = getId();
        //System.out.println("MisnamedBundle.toBundle " + oldId);
        Namespace namespace = getNamespace();
        String prefix = oldId.getPrefix();
        Map<String, String> prefixes = namespace.getPrefixes();
        if (prefixes.containsKey(prefix)) {
            //System.out.println("MisnamedBundle.toBundle " + oldId + " has a prefix " + prefix + " in local namespace" + prefixes);
            QualifiedName newId = pf.newQualifiedName(prefixes.get(prefix), oldId.getLocalPart(), prefix);
            //System.out.println("MisnamedBundle.toBundle " + oldId + " is a prefix, newId=" + newId);
            return pf.newNamedBundle(newId, namespace, getStatement());
        } else {
            return pf.newNamedBundle(oldId, namespace, getStatement());
        }
    }
}
