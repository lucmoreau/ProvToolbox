package org.openprovenance.prov.validation.report.json;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.validation.VarTime;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class CustomXMLGregorianCalendarDeserializer extends StdDeserializer<XMLGregorianCalendar> {

    static DatatypeFactory dataFactory;

    static {
        try {
            dataFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
    }

    protected CustomXMLGregorianCalendarDeserializer(Class<?> vc) {
        super(vc);
    }

    public CustomXMLGregorianCalendarDeserializer() {
        this(XMLGregorianCalendar.class);
    }


    @Override
    public XMLGregorianCalendar deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        String text = jsonParser.getString();
        if (text.startsWith("http")) {
            return new VarTime(text);
        } else {
            return  dataFactory.newXMLGregorianCalendar(text);
        }
    }
}
