package org.openprovenance.prov.scala.jsonld11.serialization.serial;

import tools.jackson.core.JsonGenerator;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.model.QualifiedName;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

abstract public class CustomMapSerializer extends ValueSerializer<QualifiedName> {
    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        String s=q.getPrefix() + ":" + q.getLocalPart();
        if (s.equals("prov:type")) {
            s= Constants.PROPERTY_AT_TYPE;
        }
        //deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);

        jsonGenerator.writeName(s);
    }
}
