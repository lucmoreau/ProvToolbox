package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.sun.security.auth.UnixNumericGroupPrincipal;
import org.openprovenance.prov.core.vanilla.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CustomAttributeSetDeserializer extends StdDeserializer<Set> {


    public CustomAttributeSetDeserializer(JavaType vc) {
        super(vc);
    }

    @Override
    public Set deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

        QualifiedName context=(QualifiedName)deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);



        JsonNode aType=node.get(Constants.PROPERTY_AT_TYPE);
        String type = (aType==null)?null:aType.textValue();
        JsonNode aLang=node.get(Constants.PROPERTY_STRING_LANG);
        String lang = (aLang==null)?null:aLang.textValue();
        JsonNode aBody=node.get("");
        String body = (aBody==null)?node.textValue():aBody.textValue();

        Iterator<JsonNode> elements=node.elements();

        Set<Attribute> set=new HashSet<>();
        Attribute attr;
        attr = new CustomAttributeDeserializerWithRootName().deserializeX(context,type, lang, body, deserializationContext);
        set.add(attr);


        return set;
    }

}
