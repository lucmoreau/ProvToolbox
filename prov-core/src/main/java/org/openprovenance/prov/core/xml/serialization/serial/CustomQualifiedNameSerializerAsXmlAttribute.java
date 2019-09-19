package org.openprovenance.prov.core.xml.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.core.xml.QualifiedNameRef;

import java.io.IOException;


public class CustomQualifiedNameSerializerAsXmlAttribute extends StdSerializer<QualifiedName> {

    protected CustomQualifiedNameSerializerAsXmlAttribute() {
        super(QualifiedName.class);
    }

    protected CustomQualifiedNameSerializerAsXmlAttribute(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeObject(new QualifiedNameRef(q));

    }
}