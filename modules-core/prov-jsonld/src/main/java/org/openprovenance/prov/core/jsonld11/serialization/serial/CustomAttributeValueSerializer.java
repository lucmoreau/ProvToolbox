package org.openprovenance.prov.core.jsonld11.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.jsonld11.AttributeTypedValue;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.vanilla.LangString;
import org.openprovenance.prov.vanilla.TypedValue;
import tools.jackson.databind.SerializationContext;

final public class CustomAttributeValueSerializer extends StdSerializer<Object> {
    protected CustomAttributeValueSerializer(Class<Object> t) {
        super(t);
    }
    protected CustomAttributeValueSerializer() {
        super(Object.class);
    }

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        final boolean isLangString    = value instanceof LangString;
        final boolean isQualifiedName = value instanceof QualifiedName;
        final String output    = isLangString ? ((LangString) value).getValue() : (isQualifiedName? prnt((QualifiedName) value): value.toString());
        jsonGenerator.writeString(output);
    }

    static String prnt(QualifiedName qn) {
        if (qn.getPrefix()==null) {
            return qn.getLocalPart();
        }
        return qn.getPrefix() + ":" + qn.getLocalPart();
    }

}
