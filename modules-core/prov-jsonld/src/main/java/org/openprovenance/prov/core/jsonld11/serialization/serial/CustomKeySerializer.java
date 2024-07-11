package org.openprovenance.prov.core.jsonld11.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.vanilla.QualifiedName;

import java.io.IOException;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;


final public class CustomKeySerializer extends StdSerializer<QualifiedName> {


    protected CustomKeySerializer() {
        super(QualifiedName.class);
    }

    protected CustomKeySerializer(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s;
        if (q.getPrefix()==null || PROV_NS.equals(q.getNamespaceURI())) {
            s = q.getLocalPart();
        } else {
            s = q.getPrefix() + ":" + q.getLocalPart();
        }
        jsonGenerator.writeFieldName(s);
    }
}