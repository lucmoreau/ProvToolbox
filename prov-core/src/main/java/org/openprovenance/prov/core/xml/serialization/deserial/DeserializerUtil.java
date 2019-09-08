package org.openprovenance.prov.core.xml.serialization.deserial;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.openprovenance.prov.core.xml.serialization.deserial.attic.CustomNamespaceDeserializer;
import org.openprovenance.prov.model.Namespace;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;

public class DeserializerUtil {
    public static Namespace getNamespace(DeserializationContext deserializationContext) {
        Namespace ns = (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        if (ns == null) {
            ns = new Namespace();
            ns.addKnownNamespaces();
            deserializationContext.setAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE, ns);
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

}