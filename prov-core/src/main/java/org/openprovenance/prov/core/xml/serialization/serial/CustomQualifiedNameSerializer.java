package org.openprovenance.prov.core.xml.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.core.xml.serialization.stax.StaxStreamWriterUtil;

import java.io.IOException;


public class CustomQualifiedNameSerializer extends StdSerializer<QualifiedName> {

    public CustomQualifiedNameSerializer() {
        super(QualifiedName.class);
    }

    protected CustomQualifiedNameSerializer(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        String prefix = q.getPrefix();

        String s= prefix + ":" + q.getLocalPart();

        StaxStreamWriterUtil.writeNamespace(jsonGenerator, prefix,q.getNamespaceURI());

        jsonGenerator.writeString(s);



    }
}