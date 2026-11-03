package org.openprovenance.prov.core.jsonld11.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.vanilla.QualifiedName;
import tools.jackson.databind.SerializationContext;


final public class CustomQualifiedNameSerializer extends StdSerializer<QualifiedName> {

    public CustomQualifiedNameSerializer() {
        super(QualifiedName.class);
    }

    protected CustomQualifiedNameSerializer(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        if (q.getPrefix()==null) {
            jsonGenerator.writeString(q.getLocalPart());
        } else {
            String s = q.getPrefix() + ":" + q.getLocalPart();
            jsonGenerator.writeString(s);
        }
    }
}