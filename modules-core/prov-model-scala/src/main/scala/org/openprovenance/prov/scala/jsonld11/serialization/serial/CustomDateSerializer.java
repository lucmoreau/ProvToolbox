package org.openprovenance.prov.scala.jsonld11.serialization.serial;

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
        String s=d.toString();
        jsonGenerator.writeString(s);
    }
}