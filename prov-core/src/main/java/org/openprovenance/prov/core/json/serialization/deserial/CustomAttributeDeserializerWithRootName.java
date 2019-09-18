package org.openprovenance.prov.core.json.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.json.serialization.Constants;
import org.openprovenance.prov.core.json.serialization.serial.CustomTypedValueSerializer;
import org.openprovenance.prov.core.vanilla.LangString;
import org.openprovenance.prov.core.vanilla.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.Map;

public class CustomAttributeDeserializerWithRootName extends StdDeserializer<Attribute> implements Constants {


    static final ProvFactory pf=new ProvFactory();

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
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        Map.Entry<String, JsonNode> pair=node.fields().next();

        QualifiedName elementName=ns.stringToQualifiedName(pair.getKey(),pf);
        JsonNode vObj=pair.getValue();

        return deserialize(elementName,vObj,deserializationContext);

    }

    public Attribute deserialize(QualifiedName elementName,  String astring, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        return pf.newAttribute(elementName, astring, CustomTypedValueSerializer.QUALIFIED_NAME_XSD_STRING);
    }





    public Attribute deserialize(QualifiedName elementName, JsonNode vObj, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);


        JsonNode typeRaw = vObj.get(PROPERTY_AT_TYPE);
        String type = (typeRaw == null) ? null : typeRaw.textValue();

        JsonNode value = vObj.get(PROPERTY_AT_VALUE);



        /*

        System.out.println("-Found key1 " + key1);
        System.out.println("-Found key2 " + key2);
        System.out.println("-Found @value " + value);
        System.out.println("-Found @type " + type);

         */
        Object valueObject=value.textValue(); //TODO: should not be checking qname but uri
        if (type==null) {
            JsonNode theLang=vObj.get(Constants.PROPERTY_STRING_LANG);
            valueObject=new LangString(value.textValue(),(theLang==null)?null:theLang.textValue());
            if (theLang!=null) {
                type="prov:InternationalizedString";
            } else {
                type="xsd:string";
            }
        }
        else if (( type.equals("xsd:string") || type.equals("prov:InternationalizedString")) && value.isObject()) {
            //System.out.println(" This is an object " + value);
            JsonNode theValue=value.get(Constants.PROPERTY_STRING_VALUE);
            JsonNode theLang=value.get(Constants.PROPERTY_STRING_LANG);
            valueObject=new LangString(theValue.textValue(),(theLang==null)?null:theLang.textValue());
        } else if (type.equals("prov:QUALIFIED_NAME")) {
            valueObject=ns.stringToQualifiedName(value.textValue(),pf);
        }

        QualifiedName typeQN=ns.stringToQualifiedName(type,pf);
        return pf.newAttribute(elementName,valueObject, typeQN);
    }
}
