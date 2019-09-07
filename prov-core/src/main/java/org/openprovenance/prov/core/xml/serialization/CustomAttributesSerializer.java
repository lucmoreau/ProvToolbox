package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.openprovenance.prov.core.vanilla.TypedValue;
import org.openprovenance.prov.core.xml.serialization.stax.StaxStreamWriterUtil;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Set;


public class CustomAttributesSerializer extends StdSerializer<Object> {


    protected CustomAttributesSerializer() {
        super(Object.class);

    }

    protected CustomAttributesSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        QualifiedName newKey=(QualifiedName) serializerProvider.getAttribute(CustomMapSerializer.CONTEXT_KEY_FOR_MAP);

        Set<TypedValue> set=(Set<TypedValue>) o;


        if (!(set.isEmpty())) {
            ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;

            StaxStreamWriterUtil.setPrefix(xmlGenerator, newKey.getPrefix(), newKey.getNamespaceURI());


            QName qn=newKey.toQName();
            xmlGenerator.setNextName(qn);


            jsonGenerator.writeFieldName(qn.getLocalPart());

            jsonGenerator.writeStartArray();

            for (TypedValue a : set) {

                new CustomTypedValueSerializer().serialize(a, jsonGenerator, serializerProvider);
            }

            jsonGenerator.writeEndArray();
        }
    }


}