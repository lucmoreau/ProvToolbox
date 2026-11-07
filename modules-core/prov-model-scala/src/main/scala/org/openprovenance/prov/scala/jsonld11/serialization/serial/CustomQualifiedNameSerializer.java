package org.openprovenance.prov.scala.jsonld11.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.scala.immutable.QualifiedName;
import tools.jackson.databind.SerializationContext;


public class CustomQualifiedNameSerializer extends StdSerializer<QualifiedName> {

    public CustomQualifiedNameSerializer() {
        super(QualifiedName.class);
    }

    protected CustomQualifiedNameSerializer(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        String s=q.getPrefix() + ":" + q.getLocalPart();
        jsonGenerator.writeString(s);
    }
}