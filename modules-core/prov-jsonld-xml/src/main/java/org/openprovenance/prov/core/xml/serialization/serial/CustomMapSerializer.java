package org.openprovenance.prov.core.xml.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.vanilla.QualifiedName;
import tools.jackson.databind.SerializationContext;


public class CustomMapSerializer extends StdSerializer<QualifiedName> {

    public static final String CONTEXT_KEY_FOR_MAP = "CONTEXT_KEY_FOR_MAP";

    protected CustomMapSerializer() {
        super(QualifiedName.class);
    }

    protected CustomMapSerializer(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {

        serializerProvider.setAttribute(CONTEXT_KEY_FOR_MAP,q);

    }
}