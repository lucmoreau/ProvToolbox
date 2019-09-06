package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.openprovenance.prov.core.vanilla.QualifiedName;
import org.openprovenance.prov.core.xml.QualifiedNameRef;
import org.openprovenance.prov.model.NamespacePrefixMapper;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;


public class CustomQualifiedNameSerializer extends StdSerializer<QualifiedName> {

    protected CustomQualifiedNameSerializer() {
        super(QualifiedName.class);
    }

    protected CustomQualifiedNameSerializer(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {


        ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;

        String s=q.getPrefix() + ":" + q.getLocalPart();

        jsonGenerator.writeString(s);


        try {
            xmlGenerator.getStaxWriter().writeNamespace(q.getPrefix(),q.getNamespaceURI());
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }




    }
}