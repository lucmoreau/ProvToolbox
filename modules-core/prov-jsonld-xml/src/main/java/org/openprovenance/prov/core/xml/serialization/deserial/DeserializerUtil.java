package org.openprovenance.prov.core.xml.serialization.deserial;

import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.core.xml.serialization.ProvDeserialiser;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.QualifiedNameUtils;

import static org.openprovenance.prov.core.xml.serialization.deserial.CustomThreadConfig.getAttributes;
import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;

public class DeserializerUtil {
    private static final String PROVX_CONTEXT_KEY_NAMESPACE = "PROVX_CONTEXT_KEY_NAMESPACE";

    private final static ProvFactory pf= ProvDeserialiser.pf;
    private final static QualifiedNameUtils qnU=new QualifiedNameUtils();

    public static Namespace getNamespace() {
        Namespace ns= getAttributes().get().get(PROVX_CONTEXT_KEY_NAMESPACE);
        if (ns == null) {
            System.out.println("=============> ns is null");
        }
        return ns;
    }
    public static Namespace newNamespace() {
        Namespace ns= new Namespace();
        getAttributes().get().put(PROVX_CONTEXT_KEY_NAMESPACE,ns);
        return ns;
    }

    public static Namespace removeNamespace() {
        return getAttributes().get().remove(PROVX_CONTEXT_KEY_NAMESPACE);
    }
    public static void setNamespace(Namespace namespace) {
        getAttributes().get().put(PROVX_CONTEXT_KEY_NAMESPACE, namespace);
    }

    public static String getAttributeValue(Namespace ns, FromXmlParser xmlParser, String name) {
        String attributeValue=xmlParser.getStaxReader().getAttributeValue(PROV_NS, name);
        if (attributeValue.contains(":")) {
            String prefix=attributeValue.substring(0,attributeValue.indexOf(":"));
            String ans=xmlParser.getStaxReader().getNamespaceURI(prefix);
            ns.register(prefix,ans);
        } else {
            String ans=xmlParser.getStaxReader().getNamespaceURI();
            ns.registerDefault(ans);
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