package org.openprovenance.prov.core.xml.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.core.xml.serialization.ProvDeserialiser;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.namespace.QName;
import java.io.IOException;


public class CustomQualifiedNameDeserializer extends JsonDeserializer<QualifiedName> { //StdDeserializer<QualifiedName> {

    private static final ProvFactory pf= ProvDeserialiser.pf;


    @Override
    public QualifiedName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns = DeserializerUtil.getNamespace(deserializationContext);
        FromXmlParser xmlParser=(FromXmlParser)jsonParser;
        // has the side effect of registering the prefix
        String av = DeserializerUtil.getAttributeValue(ns, xmlParser, "id");
        String text = jsonParser.getText();
        return DeserializerUtil.unescapeQualifiedName(ns.stringToQualifiedName(text, pf));
    }


    public QualifiedName deserialize(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns = DeserializerUtil.getNamespace(deserializationContext);

        JsonParser jsonParser=deserializationContext.getParser();
        FromXmlParser xmlParser=(FromXmlParser)jsonParser;

        QName qName=xmlParser.getStaxReader().getName();
        ns.register(qName.getPrefix(),qName.getNamespaceURI());

        return DeserializerUtil.unescapeQualifiedName(pf.newQualifiedName(qName));
    }
}
