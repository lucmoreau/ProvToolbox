package org.openprovenance.prov.core.xml.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.vanilla.QualifiedName;

import java.io.IOException;


public class CustomMapSerializer extends StdSerializer<QualifiedName> {

    public static final String CONTEXT_KEY_FOR_MAP = "CONTEXT_KEY_FOR_MAP";

    protected CustomMapSerializer() {
        super(QualifiedName.class);
    }

    protected CustomMapSerializer(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        serializerProvider.setAttribute(CONTEXT_KEY_FOR_MAP,q);

    }
}