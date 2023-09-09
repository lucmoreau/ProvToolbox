package org.openprovenance.prov.validation.report.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.validation.VarTime;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;

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
    public XMLGregorianCalendar deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText();
        if (text.startsWith("http")) {
            return new VarTime(text);
        } else {
            return  dataFactory.newXMLGregorianCalendar(text);
        }
    }
}
