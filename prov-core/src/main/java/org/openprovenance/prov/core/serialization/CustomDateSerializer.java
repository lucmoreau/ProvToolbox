package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;


public class CustomDateSerializer extends StdSerializer<XMLGregorianCalendar> {

    protected CustomDateSerializer() {
        super(XMLGregorianCalendar.class);
    }

    protected CustomDateSerializer(Class<XMLGregorianCalendar> t) {
        super(t);
    }

    @Override
    public void serialize(XMLGregorianCalendar d, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s=d.toString();
        jsonGenerator.writeString(s);
    }
}