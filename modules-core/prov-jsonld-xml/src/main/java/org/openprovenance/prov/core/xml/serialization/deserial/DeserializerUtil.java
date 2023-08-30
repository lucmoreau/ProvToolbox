package org.openprovenance.prov.core.xml.serialization.deserial;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.core.xml.serialization.ProvDeserialiser;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.QualifiedNameUtils;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;

public class DeserializerUtil {
    public static final String CONTEXT_KEY_NAMESPACE = "CONTEXT_KEY_NAMESPACE";

    private final static ProvFactory pf= ProvDeserialiser.pf;
    private final static QualifiedNameUtils qnU=new QualifiedNameUtils();

    public static Namespace getNamespace(DeserializationContext deserializationContext) {
        Namespace ns = (Namespace) deserializationContext.getAttribute(CONTEXT_KEY_NAMESPACE);
        if (ns == null) {
            ns = new Namespace();
            ns.addKnownNamespaces();
            deserializationContext.setAttribute(CONTEXT_KEY_NAMESPACE, ns);
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