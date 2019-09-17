package org.openprovenance.prov.scala.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.scala.immutable.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.Map;

public class CustomAttributeDeserializerWithRootName extends StdDeserializer<Attribute> implements Constants {


    static final ProvFactory pf=ProvFactory.pf();

    static final QualifiedName XSD_STRING=pf.getName().XSD_STRING;

    static final QualifiedName PROV_INTERNATIONALIZED_STRING =pf.getName().PROV_LANG_STRING;

    static final QualifiedName PROV_QUALIFIED_NAME =pf.getName().PROV_QUALIFIED_NAME;

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
        return pf.newAttribute(elementName, ns.stringToQualifiedName(astring,pf), PROV_QUALIFIED_NAME);
    }





    public Attribute deserialize(QualifiedName elementName, JsonNode vObj, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
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
                //System.out.println(" This is an object " + value);
                // JsonNode theValue=value.get(Constants.PROPERTY_STRING_VALUE);
                JsonNode theLang = vObj.get(Constants.PROPERTY_STRING_LANG);
                valueObject = pf.newInternationalizedString(value.textValue(), (theLang == null) ? null : theLang.textValue());

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
