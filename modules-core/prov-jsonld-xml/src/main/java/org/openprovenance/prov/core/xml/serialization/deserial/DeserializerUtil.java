package org.openprovenance.prov.core.xml.serialization.deserial;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.core.xml.serialization.ProvDeserialiser;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.QualifiedNameUtils;

import static org.openprovenance.prov.core.xml.serialization.deserial.CustomThreadConfig.PROVX_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.xml.serialization.deserial.CustomThreadConfig.getAttributes;
import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;

public class DeserializerUtil {

    private final static ProvFactory pf= ProvDeserialiser.pf;
    private final static QualifiedNameUtils qnU=new QualifiedNameUtils();

    public static Namespace getNamespace(DeserializationContext deserializationContext) {
        Namespace ns= getAttributes().get().get(PROVX_CONTEXT_KEY_NAMESPACE);
        if (ns == null) {
            ns = new Namespace();
            ns.addKnownNamespaces();
            getAttributes().get().put(PROVX_CONTEXT_KEY_NAMESPACE,ns);
        }
        return ns;
    }

    public static String getAttributeValue(Namespace ns, FromXmlParser xmlParser, String name) {
        String attributeValue=xmlParser.getStaxReader().getAttributeValue(PROV_NS, name);
        if (attributeValue.contains(":")) {
            String prefix=attributeValue.substring(0,attributeValue.indexOf(":"));
            String ans=xmlParser.getStaxReader().getNamespaceURI(prefix);
            ns.register(prefix,ans);
        }
        return attributeValue;
    }


    static public QualifiedName unescapeQualifiedName(QualifiedName id) {
        String namespace=id.getNamespaceURI();
        String local=qnU.escapeProvLocalName(qnU.unescapeFromXsdLocalName(id.getLocalPart()));
        String prefix=id.getPrefix();
        return pf.newQualifiedName(namespace,local,prefix);
    }

}