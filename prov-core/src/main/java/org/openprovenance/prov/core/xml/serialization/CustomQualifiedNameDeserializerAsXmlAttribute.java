package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.openprovenance.prov.core.vanilla.ProvFactory;
import org.openprovenance.prov.core.xml.QualifiedNameRef;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.namespace.QName;
import java.io.IOException;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;

public class CustomQualifiedNameDeserializerAsXmlAttribute extends JsonDeserializer<QualifiedName> { //StdDeserializer<QualifiedName> {


    static final ProvFactory pf=new ProvFactory();
    static final QualifiedName PROV_TYPE=pf.getName().PROV_TYPE;

    public CustomQualifiedNameDeserializerAsXmlAttribute() {
        super();

    }


    @Override
    public QualifiedName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        if (ns==null) {
            ns=new Namespace();
            ns.addKnownNamespaces();
        }
        deserializationContext.setAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE,ns);

        FromXmlParser xmlParser=(FromXmlParser)jsonParser;

        String ref=xmlParser.getStaxReader().getAttributeValue(PROV_NS,"ref");




        if (ref.contains(":")) {
            String prefix=ref.substring(0,ref.indexOf(":"));
            String ans=xmlParser.getStaxReader().getNamespaceURI(prefix);

            ns.register(prefix,ans);

        }
        jsonParser.nextValue();

        jsonParser.readValueAsTree();
        jsonParser.readValueAsTree();

        return ns.stringToQualifiedName(ref, pf, false);

    }


}
