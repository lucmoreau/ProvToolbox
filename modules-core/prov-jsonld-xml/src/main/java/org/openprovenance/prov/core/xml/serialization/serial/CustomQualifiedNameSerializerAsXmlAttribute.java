package org.openprovenance.prov.core.xml.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.vanilla.QualifiedName;
import tools.jackson.databind.SerializationContext;
import org.openprovenance.prov.core.xml.QualifiedNameRef;


public class CustomQualifiedNameSerializerAsXmlAttribute extends StdSerializer<QualifiedName> {

    protected CustomQualifiedNameSerializerAsXmlAttribute() {
        super(QualifiedName.class);
    }

    protected CustomQualifiedNameSerializerAsXmlAttribute(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {

        jsonGenerator.writePOJO(new QualifiedNameRef(q));

    }
}