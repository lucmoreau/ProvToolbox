package org.openprovenance.prov.core.xml.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;


public class CustomDateSerializer extends StdSerializer<XMLGregorianCalendar> {

    public CustomDateSerializer() {
        super(XMLGregorianCalendar.class);
    }

    protected CustomDateSerializer(Class<XMLGregorianCalendar> t) {
        super(t);
    }

    @Override
    public void serialize(XMLGregorianCalendar d, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(d.toString());
    }
}