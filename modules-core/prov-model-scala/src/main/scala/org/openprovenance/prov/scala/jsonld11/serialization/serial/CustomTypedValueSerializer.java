package org.openprovenance.prov.scala.jsonld11.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.scala.immutable.LangString;
import org.openprovenance.prov.scala.immutable.ProvFactory;
import org.openprovenance.prov.scala.immutable.TypedValue;
import tools.jackson.databind.SerializationContext;
import org.openprovenance.prov.model.QualifiedName;


public class CustomTypedValueSerializer extends StdSerializer<TypedValue> implements Constants {

    static final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;

    protected CustomTypedValueSerializer() {
        super(TypedValue.class);
    }

    protected CustomTypedValueSerializer(Class<TypedValue> t) {
        super(t);
    }

    @Override
    public void serialize(TypedValue attr, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        if ((attr.getValue() instanceof LangString) &&
                ((LangString)attr.getValue()).getLang()==null &&
                (QUALIFIED_NAME_XSD_STRING.equals(attr.getType()))) {
            jsonGenerator.writePOJO(attr.getValue());
        } else if ((attr.getValue() instanceof String) &&
                (QUALIFIED_NAME_XSD_STRING.equals(attr.getType()))) {
            jsonGenerator.writeString((String)attr.getValue());
        } else if ((attr.getValue() instanceof LangString) &&
                ((LangString)attr.getValue()).getLang()!=null ) {
            jsonGenerator.writePOJO(attr.getValue());
        } else if ((attr.getValue() instanceof QualifiedName)  ) {
            // a bare string under a key coerced to @id, a node reference under any other (https://github.com/openprov/prov-jsonld/issues/7)
            if (Boolean.TRUE.equals(serializerProvider.getAttribute(CustomMapSerializer2.ID_COERCED_KEY))) {
                jsonGenerator.writeString(prnt((QualifiedName)attr.getValue()));
            } else {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringProperty(PROPERTY_AT_ID, prnt((QualifiedName)attr.getValue()));
                jsonGenerator.writeEndObject();
            }
        } else {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringProperty(PROPERTY_AT_TYPE, prnt(attr.getType()));
            serializeValue(PROPERTY_AT_VALUE, attr.getValue(), jsonGenerator, serializerProvider);
            jsonGenerator.writeEndObject();
        }


    }

    private void serializeValue(String fieldName, Object value, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        if (value instanceof String) {
            jsonGenerator.writeStringProperty (fieldName, (String)value);
            return;
        }
        if (value instanceof QualifiedName) {
            jsonGenerator.writeStringProperty (fieldName, prnt((QualifiedName)value));
            return;
        }
        if (value instanceof LangString) {
            jsonGenerator.writeName(fieldName);
            jsonGenerator.writePOJO(value);
            return;
        }
        throw new UnsupportedOperationException("unknown value type " + value);
    }

    private String prnt(QualifiedName qn) {
        return qn.getPrefix() + ":" + qn.getLocalPart();
    }
}