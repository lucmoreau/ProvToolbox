package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class CustomMapSerializer extends JsonSerializer<QualifiedName> {
    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s=q.getPrefix() + ":" + q.getLocalPart();
        jsonGenerator.writeFieldName(s);
    }
}
