package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.openprovenance.prov.core.vanilla.TypedValue;

import javax.xml.stream.XMLStreamException;
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
        String newKey=(String)serializerProvider.getAttribute(CustomMapSerializer2.CONTEXT_KEY_FOR_MAP);

        Set<TypedValue> set=(Set<TypedValue>) o;
        if (!(set.isEmpty())) {
            ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;

            try {
                xmlGenerator.getStaxWriter().setDefaultNamespace("http://ff");
            } catch (XMLStreamException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
            if (newKey.contains(":")) {
                String s=newKey.substring(newKey.indexOf(":")+1);
                if (s.startsWith("0")) s="A"+s;
                jsonGenerator.writeFieldName(s);
            } else {
                jsonGenerator.writeFieldName(newKey);
            }

            jsonGenerator.writeStartArray();
            for (TypedValue a : set) {
                new CustomTypedValueSerializer().serialize(a, jsonGenerator, serializerProvider);
            }
            jsonGenerator.writeEndArray();
        }
    }


}