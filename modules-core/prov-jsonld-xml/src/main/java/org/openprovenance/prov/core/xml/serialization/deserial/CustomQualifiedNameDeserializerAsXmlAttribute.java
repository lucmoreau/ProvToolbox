package org.openprovenance.prov.core.xml.serialization.deserial;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import org.openprovenance.prov.vanilla.ProvFactory;
import tools.jackson.databind.ValueDeserializer;
import org.openprovenance.prov.core.xml.serialization.ProvDeserialiser;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

public class CustomQualifiedNameDeserializerAsXmlAttribute extends ValueDeserializer<QualifiedName> { //StdDeserializer<QualifiedName> {

    private static final ProvFactory pf= ProvDeserialiser.pf;


    public CustomQualifiedNameDeserializerAsXmlAttribute() {
        super();

    }


    @Override
    public QualifiedName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        Namespace ns = DeserializerUtil.getNamespace();

        FromXmlParser xmlParser=(FromXmlParser)jsonParser;

        String ref = DeserializerUtil.getAttributeValue(ns, xmlParser, "ref");
        // Jackson 3: consume the rest of the element (<x prov:ref="..."/>), ending on its END_OBJECT.
        // Was nextValue() + 2 x readValueAsTree(), which relied on Jackson 2 clearing the current token
        // after readValueAsTree(); Jackson 3 does not, which left the parser inside the element.
        jsonParser.skipChildren();

        return ns.stringToQualifiedName(ref, pf, false);

    }



}
