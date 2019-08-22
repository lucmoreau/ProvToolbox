package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.LangString;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;

import java.io.IOException;


public class CustomAttributeSerializer extends StdSerializer<Attribute> {

    protected CustomAttributeSerializer() {
        super(Attribute.class);
    }

    protected CustomAttributeSerializer(Class<Attribute> t) {
        super(t);
    }
    @Override
    public void serialize(Attribute attr, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s=null;
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldStart(prnt(attr.getElementName()));
        jsonGenerator.writeStringField("type", prnt(attr.getType()));
        jsonGenerator.writeStringField("value",  prntValue(attr.getValue()));
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
    }

    private String prntValue(Object value) {
        if (value instanceof String) return (String) value;
        if (value instanceof QualifiedName) return prnt((QualifiedName)value);
        if (value instanceof LangString) return value.toString();
        throw new UnsupportedOperationException("unknown value type " + value);
    }

    private String prnt(QualifiedName qn) {
        return qn.getPrefix() + ":" + qn.getLocalPart();
    }
}