package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.LangString;
import org.openprovenance.prov.core.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.Iterator;
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

        return pf.newAttribute(elementName, new LangString(astring,null), CustomTypedValueSerializer.QUALIFIED_NAME_XSD_STRING);
    }





    public Attribute deserialize(QualifiedName elementName, JsonNode vObj, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);



        Iterator<Map.Entry<String, JsonNode>> pairs=vObj.fields();
        Map.Entry<String, JsonNode> pair1=pairs.next();
        Map.Entry<String, JsonNode> pair2=pairs.next();

        String key1=pair1.getKey();
        String key2=pair2.getKey();

        String type=null;
        JsonNode value=null;
        if (PROPERTY_AT_TYPE.equals(key1)   ) type=pair1.getValue().textValue();
        if (PROPERTY_AT_TYPE.equals(key2)   ) type=pair2.getValue().textValue();

        if (PROPERTY_AT_VALUE.equals(key1)   ) value=pair1.getValue();
        if (PROPERTY_AT_VALUE.equals(key2)   ) value=pair2.getValue();


        /*

        System.out.println("-Found key1 " + key1);
        System.out.println("-Found key2 " + key2);
        System.out.println("-Found @value " + value);
        System.out.println("-Found @type " + type);

         */
        Object valueObject=value.textValue(); //TODO: should not be checking qname but uri
        if ((type.equals("xsd:string") || type.equals("prov:InternationalizedString")) && value.isObject()) {
            //System.out.println(" This is an object " + value);
            JsonNode theValue=value.get(Constants.PROPERTY_STRING_VALUE);
            JsonNode theLang=value.get(Constants.PROPERTY_STRING_LANG);
            valueObject=new LangString(theValue.textValue(),(theLang==null)?null:theLang.textValue());
        } else if (type.equals("prov:QUALIFIED_NAME")) {
            valueObject=ns.stringToQualifiedName(value.textValue(),pf);
        }

        QualifiedName typeQN=ns.stringToQualifiedName(type,pf);
        return pf.newAttribute(elementName,valueObject, ns.stringToQualifiedName(type,pf));
    }
}
