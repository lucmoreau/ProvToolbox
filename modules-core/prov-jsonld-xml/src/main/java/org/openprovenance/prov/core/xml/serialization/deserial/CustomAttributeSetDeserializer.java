package org.openprovenance.prov.core.xml.serialization.deserial;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.xml.serialization.Constants;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import java.util.HashSet;
import java.util.Set;

public class CustomAttributeSetDeserializer extends StdDeserializer<Set> {


    public CustomAttributeSetDeserializer(JavaType vc) {
        super(vc);
    }

    @Override
    public Set deserialize(JsonParser jp, DeserializationContext deserializationContext) {
        JsonNode node = jp.objectReadContext().readTree(jp);

        QualifiedName context=(QualifiedName)deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);



        JsonNode aType=node.get(Constants.PROPERTY_AT_TYPE);
        String type = (aType==null)?null:aType.stringValue();
        JsonNode aLang=node.get(Constants.PROPERTY_STRING_LANG);
        String lang = (aLang==null)?null:aLang.stringValue();
        JsonNode aBody=node.get("");
        String body = (aBody==null)?node.stringValue():aBody.stringValue();


        Set<Attribute> set=new HashSet<>();
        Attribute attr = new CustomAttributeDeserializerWithRootName().deserialize(context,type, lang, body, deserializationContext);
        set.add(attr);


        return set;
    }

}
