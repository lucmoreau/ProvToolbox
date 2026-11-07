package org.openprovenance.prov.core.xml.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.databind.SerializationContext;

import javax.xml.datatype.XMLGregorianCalendar;


public class CustomDateSerializer extends StdSerializer<XMLGregorianCalendar> {

    public CustomDateSerializer() {
        super(XMLGregorianCalendar.class);
    }

    protected CustomDateSerializer(Class<XMLGregorianCalendar> t) {
        super(t);
    }

    @Override
    public void serialize(XMLGregorianCalendar d, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        jsonGenerator.writeString(d.toString());
    }
}