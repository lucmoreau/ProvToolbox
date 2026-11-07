package org.openprovenance.prov.core.xml.serialization.deserial;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import org.openprovenance.prov.vanilla.ProvFactory;
import tools.jackson.databind.ValueDeserializer;
import org.openprovenance.prov.core.xml.serialization.ProvDeserialiser;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.namespace.QName;


public class CustomQualifiedNameDeserializer extends ValueDeserializer<QualifiedName> { //StdDeserializer<QualifiedName> {

    private static final ProvFactory pf= ProvDeserialiser.pf;


    @Override
    public QualifiedName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        Namespace ns = DeserializerUtil.getNamespace();
        FromXmlParser xmlParser=(FromXmlParser)jsonParser;
        // has the side effect of registering the prefix
        String av = DeserializerUtil.getAttributeValue(ns, xmlParser, "id");
        String text = jsonParser.getString();
        return DeserializerUtil.unescapeQualifiedName(ns.stringToQualifiedName(text, pf));
    }


    public QualifiedName deserialize(String s, DeserializationContext deserializationContext) {
        Namespace ns = DeserializerUtil.getNamespace();

        JsonParser jsonParser=deserializationContext.getParser();
        FromXmlParser xmlParser=(FromXmlParser)jsonParser;

        QName qName=xmlParser.getStaxReader().getName();
        ns.register(qName.getPrefix(),qName.getNamespaceURI());

        return DeserializerUtil.unescapeQualifiedName(pf.newQualifiedName(qName));
    }
}
