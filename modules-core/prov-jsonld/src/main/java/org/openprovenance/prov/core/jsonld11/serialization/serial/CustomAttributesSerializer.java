package org.openprovenance.prov.core.jsonld11.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.vanilla.LangString;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.vanilla.TypedValue;

import java.io.IOException;
import java.util.Set;


public class CustomAttributesSerializer extends StdSerializer<Object> implements Constants {
    static final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;


    protected CustomAttributesSerializer() {
        super(Object.class);

    }

    protected CustomAttributesSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String newKey=(String)serializerProvider.getAttribute(CustomMapSerializer.CONTEXT_KEY_FOR_MAP);

        Set<TypedValue> set=(Set<TypedValue>) o;
        if (!(set.isEmpty())) {
            jsonGenerator.writeFieldName(newKey);
            jsonGenerator.writeStartArray();
            for (TypedValue a : set) {
                serialize_TypedValue(a, jsonGenerator, serializerProvider);
            }
            jsonGenerator.writeEndArray();
        }
    }

    public void serialize_TypedValue(TypedValue attr, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        final Object value = attr.getValue();

        if ((value instanceof LangString) &&
                ((LangString) value).getLang()==null &&
                (QUALIFIED_NAME_XSD_STRING.equals(attr.getType()))) {

            LangString me=(LangString) value;
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField(Constants.PROPERTY_STRING_VALUE, me.getValue());
            jsonGenerator.writeEndObject();

        } else if ((value instanceof LangString) &&
                ((LangString) value).getLang()!=null ) {

            LangString me=(LangString) value;
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField(Constants.PROPERTY_STRING_VALUE, me.getValue());
            jsonGenerator.writeStringField(Constants.PROPERTY_STRING_LANG,  me.getLang());
            jsonGenerator.writeEndObject();

        } else if (value instanceof QualifiedName)   {

            jsonGenerator.writeObject(prnt((QualifiedName) value));

        } else if ((value instanceof String) &&
                   (QUALIFIED_NAME_XSD_STRING.equals(attr.getType()))) {

            throw new UnsupportedOperationException("should never be here");

        } else {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField(PROPERTY_AT_TYPE, prnt(attr.getType()));
            serializeValue(PROPERTY_AT_VALUE, value, jsonGenerator, serializerProvider);
            jsonGenerator.writeEndObject();
        }

    }


    final private void serializeValue(String fieldName, Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
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

    final private String prnt(QualifiedName qn) {
        return qn.getPrefix() + ":" + qn.getLocalPart();
    }

}