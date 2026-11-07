package org.openprovenance.prov.scala.jsonld11.serialization.deserial;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.scala.immutable.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.Map;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.ID_COERCED_ATTRIBUTE_TERMS;

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
    public Attribute deserialize(JsonParser jp, DeserializationContext deserializationContext) {
        JsonNode node = deserializationContext.readTree(jp) /* jackson 3: the ongoing context, see catalogue #21 */;

        return deserialize(node, deserializationContext);


    }

    public Attribute deserialize(JsonNode node, DeserializationContext deserializationContext) {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        Map.Entry<String, JsonNode> pair=node.properties().iterator().next();

        QualifiedName elementName=ns.stringToQualifiedName(pair.getKey(),pf);
        deserializationContext.setAttribute(CustomKeyDeserializer.ID_COERCED_CONTEXT_KEY, ID_COERCED_ATTRIBUTE_TERMS.contains(pair.getKey()));
        JsonNode vObj=pair.getValue();

        return deserialize(elementName,vObj,deserializationContext);

    }

    /**
     * A bare string is what a JSON-LD processor makes of it: an IRI, so a qualified name, under a key the context
     * coerces to {@code @id}; a string under any other key (https://github.com/openprov/prov-jsonld/issues/7).
     */
    public Attribute deserialize(QualifiedName elementName,  String astring, DeserializationContext deserializationContext) {
        if (Boolean.TRUE.equals(deserializationContext.getAttribute(CustomKeyDeserializer.ID_COERCED_CONTEXT_KEY))) {
            return qualifiedName(elementName, astring, deserializationContext);
        }
        return pf.newAttribute(elementName, pf.newInternationalizedString(astring, null), XSD_STRING);
    }

    private Attribute qualifiedName(QualifiedName elementName, String astring, DeserializationContext deserializationContext) {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        return pf.newAttribute(elementName, ns.stringToQualifiedName(astring,pf), PROV_QUALIFIED_NAME);
    }





    public Attribute deserialize(QualifiedName elementName, JsonNode vObj, DeserializationContext deserializationContext) {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);

        if (vObj.isString()) {
            return deserialize(elementName, vObj.stringValue(null), deserializationContext);

        } else if (!vObj.isObject()) {
            throw new UnsupportedOperationException("vObj can only be object");

        } else if (vObj.has(PROPERTY_AT_ID)) {
            // a node reference {"@id": "ex:shed"}: the IRI, so a qualified name, under any key
            if (vObj.size() != 1 || !vObj.get(PROPERTY_AT_ID).isString()) {
                throw tools.jackson.databind.DatabindException.from(deserializationContext, "a node reference takes no @value, @type or @language: " + elementName); // jackson 3: unchecked
            }
            return qualifiedName(elementName, vObj.get(PROPERTY_AT_ID).stringValue(null), deserializationContext);

        } else {

            JsonNode typeRaw = vObj.get(PROPERTY_AT_TYPE);
            String type = (typeRaw == null) ? null : typeRaw.stringValue(null);

            JsonNode value = vObj.get(PROPERTY_AT_VALUE);

            Object valueObject;

            QualifiedName typeQN;
            if (type == null || type.equals("xsd:string") || type.equals("prov:InternationalizedString")) {
                //System.out.println(" This is an object " + value);
                // JsonNode theValue=value.get(Constants.PROPERTY_STRING_VALUE);
                JsonNode theLang = vObj.get(Constants.PROPERTY_STRING_LANG);
                valueObject = pf.newInternationalizedString(value.stringValue(null), (theLang == null) ? null : theLang.stringValue(null));

                typeQN = (theLang == null) ? XSD_STRING : PROV_INTERNATIONALIZED_STRING;
            } else if (type.equals("prov:QUALIFIED_NAME")) {
                valueObject = ns.stringToQualifiedName(value.stringValue(null), pf);
                typeQN = ns.stringToQualifiedName(type, pf);
            } else {
                valueObject = value.stringValue(null);
                ; //TODO: should not be checking qname but uri
                typeQN = ns.stringToQualifiedName(type, pf);
            }


            return pf.newAttribute(elementName, valueObject, typeQN);

        }

    }
}
