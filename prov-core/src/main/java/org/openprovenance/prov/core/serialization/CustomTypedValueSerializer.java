package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.LangString;
import org.openprovenance.prov.core.TypedValue;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;


public class CustomTypedValueSerializer extends StdSerializer<TypedValue> implements Constants {

    protected CustomTypedValueSerializer() {
        super(TypedValue.class);
    }

    protected CustomTypedValueSerializer(Class<TypedValue> t) {
        super(t);
    }
    @Override
    public void serialize(TypedValue attr, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s=null;
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(PROPERTY_AT_TYPE, prnt(attr.getType()));
        serializeValue(PROPERTY_AT_VALUE,  attr.getValue(), jsonGenerator, serializerProvider);
        jsonGenerator.writeEndObject();
    }

    private void serializeValue(String fieldName, Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value instanceof String) {
            jsonGenerator.writeStringField (fieldName, (String)value);
            return;
        }
        if (value instanceof QualifiedName) {
            jsonGenerator.writeStringField (fieldName, prnt((QualifiedName)value));
            return;
        }
        if (value instanceof LangString) {
            jsonGenerator.writeFieldName(fieldName);
            jsonGenerator.writeObject(value);
            return;
        }
        throw new UnsupportedOperationException("unknown value type " + value);
    }

    private String prnt(QualifiedName qn) {
        return qn.getPrefix() + ":" + qn.getLocalPart();
    }
}