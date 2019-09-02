package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.codehaus.stax2.typed.TypedXMLStreamWriter;
import org.openprovenance.prov.core.vanilla.QualifiedName;
import org.openprovenance.prov.core.xml.QualifiedNameRef;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;


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