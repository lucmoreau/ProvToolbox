package org.openprovenance.prov.core.json.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.vanilla.QualifiedName;

import java.io.IOException;


public class CustomQualifiedNameSerializerAsField extends StdSerializer<QualifiedName> {

    protected CustomQualifiedNameSerializerAsField() {
        super(QualifiedName.class);
    }

    protected CustomQualifiedNameSerializerAsField(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s=q.getPrefix() + ":" + q.getLocalPart();
        jsonGenerator.writeFieldName(s);
    }
}