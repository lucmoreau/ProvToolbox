package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.jsonld11.AttributeValueAndType;
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
    public Attribute deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        if (jp.isExpectedStartObjectToken()) {
            final AttributeValueAndType valueAndType=jp.readValueAs(AttributeValueAndType.class);
            final QualifiedName elementName = (QualifiedName) deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);
            return deserialize_AttributeValueAndType(elementName,valueAndType,deserializationContext);
        } else {
            return deserialize(jp.getText(), deserializationContext);
        }
    }

    private final Attribute deserialize(String astring, DeserializationContext deserializationContext) {
        final Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        final QualifiedName elementName = (QualifiedName) deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);
        return pf.newAttribute(elementName, ns.stringToQualifiedName(astring,pf), PROV_QUALIFIED_NAME);
    }


    private final Attribute deserialize_AttributeValueAndType(QualifiedName elementName, AttributeValueAndType valueAndType, DeserializationContext deserializationContext) {

        final String type = valueAndType.typex;

        final String textValue = valueAndType.value;

        Object valueObject;
        QualifiedName typeQN;
        if (type == null || type.equals("xsd:string") || type.equals("prov:InternationalizedString")) {
            final String theLang = valueAndType.language;
            if (theLang==null) {
                valueObject = new LangString(textValue);
                typeQN =  XSD_STRING;
            } else {
                valueObject = new LangString(textValue, theLang);
                typeQN = PROV_INTERNATIONALIZED_STRING;
            }
        } else {
            final Namespace ns = (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
            typeQN = ns.stringToQualifiedName(type, pf);
            if (type.equals("prov:QUALIFIED_NAME")) {
                valueObject = ns.stringToQualifiedName(textValue, pf);
            } else {
                valueObject = textValue;
            }
        }


        return pf.newAttribute(elementName, valueObject, typeQN);



    }
}
