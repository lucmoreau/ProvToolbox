package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.vanilla.LangString;
import org.openprovenance.prov.core.vanilla.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.QualifiedNameUtils;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static org.openprovenance.prov.core.xml.serialization.CustomAttributeDeserializerWithRootName.unescapeQualifiedName;

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
        elementName=unescapeQualifiedName(elementName);

        System.out.println("[[[[[ " + elementName);

        JsonNode vObj=pair.getValue();

        return deserialize(elementName,vObj,deserializationContext);

    }

    public Attribute deserialize(QualifiedName elementName,  String astring, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);

        System.out.println("++++++ " + astring + " " + elementName);
        return pf.newAttribute(elementName, new LangString(astring,null), CustomTypedValueSerializer.QUALIFIED_NAME_XSD_STRING);
    }





    public Attribute deserialize(QualifiedName elementName, JsonNode vObj, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);


        JsonNode typeRaw = vObj.get(PROPERTY_AT_TYPE);
        String type = (typeRaw == null) ? null : typeRaw.textValue();

        JsonNode value = vObj.get(PROPERTY_AT_VALUE);


        System.out.println("+ type " + type);
        System.out.println("+ value " + value);



        Object valueObject=value.textValue(); //TODO: should not be checking qname but uri
        if ((type.equals("xsd:string") || type.equals("prov:InternationalizedString")) && value.isObject()) {
            //System.out.println(" This is an object " + value);
            JsonNode theValue=value.get(Constants.PROPERTY_STRING_VALUE);
            JsonNode theLang=value.get(Constants.PROPERTY_STRING_LANG);
            valueObject=new LangString(theValue.textValue(),(theLang==null)?null:theLang.textValue());
        } else if (type.equals("xsd:QName")) {
            valueObject=ns.stringToQualifiedName(value.textValue(),pf);
        }

        QualifiedName typeQN=ns.stringToQualifiedName(type,pf);
        return pf.newAttribute(elementName,valueObject, typeQN);
    }

    final static QualifiedNameUtils qnU=new QualifiedNameUtils();

    static public QualifiedName unescapeQualifiedName(QualifiedName id) {

        String namespace=id.getNamespaceURI();
        String local=qnU.escapeProvLocalName(qnU.unescapeFromXsdLocalName(id.getLocalPart()));
        String prefix=id.getPrefix();
        return pf.newQualifiedName(namespace,local,prefix);
    }

    public Attribute deserializeX(QualifiedName elementName, String type, String lang, String body, DeserializationContext deserializationContext) {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);

        elementName=unescapeQualifiedName(elementName);
System.out.println("[[[[[ " + elementName);
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
        return pf.newAttribute(elementName,valueObject, ns.stringToQualifiedName(type,pf));

    }
}
