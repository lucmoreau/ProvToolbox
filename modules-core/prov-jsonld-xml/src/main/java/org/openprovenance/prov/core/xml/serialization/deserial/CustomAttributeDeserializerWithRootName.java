package org.openprovenance.prov.core.xml.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.vanilla.LangString;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.core.xml.serialization.Constants;
import org.openprovenance.prov.core.xml.serialization.ProvDeserialiser;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.Map;

import static org.openprovenance.prov.core.xml.serialization.deserial.DeserializerUtil.CONTEXT_KEY_NAMESPACE;

public class CustomAttributeDeserializerWithRootName extends StdDeserializer<Attribute> implements Constants {


    private static final ProvFactory pf= ProvDeserialiser.pf;

    public CustomAttributeDeserializerWithRootName() {
        this(Attribute.class);
    }


    public CustomAttributeDeserializerWithRootName(Class<?> vc) {
        super(vc);
    }

    @Override
    public Attribute deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

        return deserialize(node, deserializationContext);

    }

    public Attribute deserialize(JsonNode node, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns = DeserializerUtil.getNamespace(deserializationContext);
        Map.Entry<String, JsonNode> pair=node.fields().next();

        QualifiedName elementName=ns.stringToQualifiedName(pair.getKey(),pf);
        elementName=DeserializerUtil.unescapeQualifiedName(elementName);


        JsonNode vObj=pair.getValue();

        return deserialize(elementName,vObj,deserializationContext);

    }


    public Attribute deserialize(QualifiedName elementName, JsonNode vObj, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns = DeserializerUtil.getNamespace(deserializationContext);


        JsonNode typeRaw = vObj.get(PROPERTY_AT_TYPE);
        String type = (typeRaw == null) ? null : typeRaw.textValue();

        JsonNode value = vObj.get(PROPERTY_AT_VALUE);


        Object valueObject=value.textValue(); //TODO: should not be checking qname but uri
        if ((type.equals("xsd:string") || type.equals("prov:InternationalizedString")) && value.isObject()) {
            JsonNode theValue=value.get(Constants.PROPERTY_STRING_VALUE);
            JsonNode theLang=value.get(Constants.PROPERTY_STRING_LANG);
            valueObject=new LangString(theValue.textValue(),(theLang==null)?null:theLang.textValue());
        } else if (type.equals("xsd:QName")) {
            valueObject=ns.stringToQualifiedName(value.textValue(),pf);
        }

        QualifiedName typeQN=ns.stringToQualifiedName(type,pf);
        return pf.newAttribute(elementName,valueObject, typeQN);
    }



    public Attribute deserialize(QualifiedName elementName, String type, String lang, String body, DeserializationContext deserializationContext) {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CONTEXT_KEY_NAMESPACE);

        QualifiedName unescaped=DeserializerUtil.unescapeQualifiedName(elementName);
        Object valueObject=body;
        if (type==null || type.equals("xsd:string") || type.equals("prov:InternationalizedString")) {
            valueObject=new LangString(body,lang);
            if (type==null) {
                type="xsd:string";
            }
        } else if (type.equals("xsd:QName")) {
            valueObject=ns.stringToQualifiedName(body,pf);
            type="prov:QUALIFIED_NAME";
        }
        Attribute attr= pf.newAttribute(unescaped,valueObject, ns.stringToQualifiedName(type,pf));

        return attr;

    }
}
