package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.vanilla.LangString;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;
import java.util.Map;

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
        Object o=jp.readValueAs(Object.class);

        if (o instanceof String) {
            return deserialize((String)o, deserializationContext);
        } else if (o instanceof Map) {
            Map<String,Object> map=(Map<String,Object>) o;
            QualifiedName elementName = (QualifiedName) deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);
            return deserialize_AttributeValueAndType(elementName,map,deserializationContext);
        } else {
            throw new UnsupportedOperationException("unknown object " + o);
        }

    }


    private final Attribute deserialize(String astring, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        QualifiedName elementName = (QualifiedName) deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);
        return pf.newAttribute(elementName, ns.stringToQualifiedName(astring,pf), PROV_QUALIFIED_NAME);
    }


    private final Attribute deserialize_AttributeValueAndType(QualifiedName elementName, Map<String, Object> map, DeserializationContext deserializationContext) {

        final String type = (String) map.get(PROPERTY_AT_TYPE);

        final String textValue = (String) map.get(PROPERTY_AT_VALUE);

        Object valueObject;
        QualifiedName typeQN;
        if (type == null || type.equals("xsd:string") || type.equals("prov:InternationalizedString")) {
            final String theLang = (String) map.get(Constants.PROPERTY_STRING_LANG);
            valueObject = new LangString(textValue, (theLang == null) ? null : theLang);
            typeQN = (theLang == null) ? XSD_STRING : PROV_INTERNATIONALIZED_STRING;
        } else if (type.equals("prov:QUALIFIED_NAME")) {
            final Namespace ns = (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
            valueObject = ns.stringToQualifiedName(textValue, pf);
            typeQN = ns.stringToQualifiedName(type, pf);
        } else {
            final Namespace ns = (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
            valueObject = textValue;
            typeQN = ns.stringToQualifiedName(type, pf);
        }


        return pf.newAttribute(elementName, valueObject, typeQN);



    }
}
