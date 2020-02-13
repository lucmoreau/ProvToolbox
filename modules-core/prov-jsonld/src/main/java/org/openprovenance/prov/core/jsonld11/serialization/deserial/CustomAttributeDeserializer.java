package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.vanilla.LangString;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;

public class CustomAttributeDeserializer extends StdDeserializer<Attribute> implements Constants {


    static final ProvFactory pf=new ProvFactory();

    static final QualifiedName XSD_STRING=pf.getName().XSD_STRING;

    static final QualifiedName PROV_INTERNATIONALIZED_STRING =pf.getName().PROV_LANG_STRING;

    static final QualifiedName PROV_QUALIFIED_NAME =pf.getName().PROV_QUALIFIED_NAME;

    public CustomAttributeDeserializer() {
        this(Attribute.class);
    }


    public CustomAttributeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Attribute deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
      //  System.out.println("CustomAttributeDeserializerWithRootName " + deserializationContext);

        if (jp.isExpectedStartObjectToken()) {
            JsonNode node = jp.getCodec().readTree(jp);
       //     System.out.println("CustomAttributeDeserializerWithRootName " + node);

            return deserialize(node, deserializationContext);
        } else {
            String s=jp.getText();
       //     System.out.println("CustomAttributeDeserializerWithRootName: String " + s);
            return deserialize(s, deserializationContext);
        }

    }

    /*
    private Attribute deserializeNode(JsonNode node, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        if (ns==null) {
            ns=Namespace.getThreadNamespace();
        }

        System.out.println(node);
        System.out.println(node.fields());
        System.out.println("next? " + node.fields().hasNext());
        Map.Entry<String, JsonNode> pair=node.fields().next();
        System.out.println("next: " + pair);
        System.out.println("ns: " + ns);

        QualifiedName elementName=ns.stringToQualifiedName(pair.getKey(),pf);
        JsonNode vObj=pair.getValue();

        return deserialize_SIMPLIFY2(elementName,vObj,deserializationContext);

    }


     */


    public Attribute deserialize(String astring, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        QualifiedName elementName = (QualifiedName) deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);
        return pf.newAttribute(elementName, ns.stringToQualifiedName(astring,pf), PROV_QUALIFIED_NAME);
    }

    public Attribute deserialize(JsonNode vObj, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        QualifiedName elementName = (QualifiedName) deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);
     //   System.out.println("---> " + elementName);
        return deserialize_AttributeValueAndType(elementName, vObj, deserializationContext);
    }

    public Attribute deserialize_AttributeValueAndType(QualifiedName elementName, JsonNode vObj, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);

        if (!vObj.isObject()) {
            throw new UnsupportedOperationException("vObj can only be object");

        } else {

            JsonNode typeRaw = vObj.get(PROPERTY_AT_TYPE);
            String type = (typeRaw == null) ? null : typeRaw.textValue();

            JsonNode value = vObj.get(PROPERTY_AT_VALUE);

            Object valueObject;

            QualifiedName typeQN;
            if (type == null || type.equals("xsd:string") || type.equals("prov:InternationalizedString")) {
                JsonNode theLang = vObj.get(Constants.PROPERTY_STRING_LANG);
                valueObject = new LangString(value.textValue(), (theLang == null) ? null : theLang.textValue());
                typeQN = (theLang == null) ? XSD_STRING : PROV_INTERNATIONALIZED_STRING;
            } else if (type.equals("prov:QUALIFIED_NAME")) {
                valueObject = ns.stringToQualifiedName(value.textValue(), pf);
                typeQN = ns.stringToQualifiedName(type, pf);
            } else {
                valueObject = value.textValue();
                ; //TODO: should not be checking qname but uri
                typeQN = ns.stringToQualifiedName(type, pf);
            }


            return pf.newAttribute(elementName, valueObject, typeQN);

        }

    }
}
