package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.jsonld11.AttributeTypedValue;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.vanilla.LangString;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;

import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.JSONLD_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.getAttributes;

public class CustomAttributeDeserializer extends StdDeserializer<Attribute> implements Constants {

    static final ProvFactory pf=ProvFactory.getFactory();

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
            final AttributeTypedValue valueAndType=jp.readValueAs(AttributeTypedValue.class);
            final QualifiedName elementName = (QualifiedName) deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);
            return deserialize_TypedValue(elementName,valueAndType,deserializationContext);
        } else {
            return deserialize(jp.getText(), deserializationContext);
        }
    }

    /**
     * A bare string is what a JSON-LD processor makes of it: an IRI, so a qualified name, under a key the context
     * coerces to {@code @id}; a string under any other key (https://github.com/openprov/prov-jsonld/issues/7).
     */
    private final Attribute deserialize(String astring, DeserializationContext deserializationContext) {
        final QualifiedName elementName = (QualifiedName) deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);
        if (Boolean.TRUE.equals(deserializationContext.getAttribute(CustomKeyDeserializer.ID_COERCED_CONTEXT_KEY))) {
            return qualifiedName(elementName, astring);
        }
        return pf.newAttribute(elementName, new LangString(astring), XSD_STRING);
    }

    private final Attribute qualifiedName(QualifiedName elementName, String astring) {
        //final Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        final Namespace ns = getAttributes().get().get(JSONLD_CONTEXT_KEY_NAMESPACE);
        return pf.newAttribute(elementName, ns.stringToQualifiedName(astring,pf), PROV_QUALIFIED_NAME);
    }


    private final Attribute deserialize_TypedValue(QualifiedName elementName, AttributeTypedValue valueAndType, DeserializationContext deserializationContext) throws IOException {

        if (valueAndType.id != null) {
            // a node reference {"@id": "ex:shed"}: the IRI, so a qualified name, under any key
            if (valueAndType.value != null || valueAndType.typex != null || valueAndType.language != null) {
                throw new IOException("a node reference takes no @value, @type or @language: " + elementName);
            }
            return qualifiedName(elementName, valueAndType.id);
        }

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
            //final Namespace ns = (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
            final Namespace ns = getAttributes().get().get(JSONLD_CONTEXT_KEY_NAMESPACE);
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
