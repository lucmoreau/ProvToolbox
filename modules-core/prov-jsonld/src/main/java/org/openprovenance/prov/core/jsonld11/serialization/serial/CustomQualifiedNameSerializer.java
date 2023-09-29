package org.openprovenance.prov.core.jsonld11.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.vanilla.QualifiedName;

import java.io.IOException;


final public class CustomQualifiedNameSerializer extends StdSerializer<QualifiedName> {

    public CustomQualifiedNameSerializer() {
        super(QualifiedName.class);
    }

    protected CustomQualifiedNameSerializer(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (q.getPrefix()==null) {
            jsonGenerator.writeString(q.getLocalPart());
        } else {
            String s = q.getPrefix() + ":" + q.getLocalPart();
            jsonGenerator.writeString(s);
        }
    }
}