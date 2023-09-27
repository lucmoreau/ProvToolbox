package org.openprovenance.prov.core.xml.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.xml.serialization.deserial.DeserializerUtil;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.core.xml.serialization.stax.StaxStreamWriterUtil;

import java.io.IOException;
import java.util.Objects;

import static org.openprovenance.prov.core.xml.serialization.stax.StaxStreamWriterUtil.writeNamespace;


public class CustomQualifiedNameSerializer extends StdSerializer<QualifiedName> {

    public CustomQualifiedNameSerializer() {
        super(QualifiedName.class);
    }

    protected CustomQualifiedNameSerializer(Class<QualifiedName> t) {
        super(t);
    }

    int count=0;

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        String prefix = q.getPrefix();
        String str =  q.getLocalPart();

        if (prefix==null) {

            // if there is a default, it will have been set by CustomDocumentSerializer??
            String actualDefault=StaxStreamWriterUtil.getDefaultNamespace(jsonGenerator);

            Namespace namespace = DeserializerUtil.getNamespace();
            StaxStreamWriterUtil.writeDefaultNamespace(jsonGenerator, q.getNamespaceURI());


            if (!Objects.equals(actualDefault, namespace.getDefaultNamespace())) {
                // in this case, the serializer has chose a different default,
                // so let's look for the prefix associated with the default namespaceURI, and use it.
                // _pre[x] is generated automatically
                String actualNS = namespace.getNamespaces().get(q.getNamespaceURI());
                String newPrefix="_pre" + count++;
                writeNamespace(jsonGenerator, newPrefix, actualNS);
                str = actualNS + ":" + str;
            }
        } else {
            str = prefix + ":" + str;
            writeNamespace(jsonGenerator, prefix, q.getNamespaceURI());
        }
        jsonGenerator.writeString(str);
    }
}