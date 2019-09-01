package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.openprovenance.prov.core.vanilla.ProvFactory;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.namespace.QName;
import java.io.IOException;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;

public class CustomQualifiedNameDeserializer extends JsonDeserializer<QualifiedName> { //StdDeserializer<QualifiedName> {


    static final ProvFactory pf=new ProvFactory();
    static final QualifiedName PROV_TYPE=pf.getName().PROV_TYPE;

    public CustomQualifiedNameDeserializer() {
        //this(QualifiedName.class);
    }

/*
    public CustomQualifiedNameDeserializer(Class<?> vc) {
        super(vc);


    }

 */

    @Override
    public QualifiedName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        if (ns==null) {
            ns=new Namespace();
            ns.addKnownNamespaces();
        }
        deserializationContext.setAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE,ns);

        FromXmlParser xmlParser=(FromXmlParser)jsonParser;

        String av=xmlParser.getStaxReader().getAttributeValue(PROV_NS,"id");
        System.out.println( " -> " + av);
        if (av.contains(":")) {
            String prefix=av.substring(0,av.indexOf(":"));
            String ans=xmlParser.getStaxReader().getNamespaceURI(prefix);

            System.out.println( "  --> " + ans + " " + prefix);
            ns.register(prefix,ans);

        }

        String text = jsonParser.getText();

        if (Constants.PROPERTY_AT_TYPE.equals(text)) return PROV_TYPE;
        return ns.stringToQualifiedName(text, pf, false);
    }


    public QualifiedName deserialize(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);

        JsonParser jsonParser=deserializationContext.getParser();
        FromXmlParser xmlParser=(FromXmlParser)jsonParser;

        QName qName=xmlParser.getStaxReader().getName();
        ns.register(qName.getPrefix(),qName.getNamespaceURI());
        System.out.println(ns);
        if (Constants.PROPERTY_AT_TYPE.equals(s)) return PROV_TYPE;
        return pf.newQualifiedName(qName);
    }
}
