package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.exception.TimeZoneException;
import org.openprovenance.prov.vanilla.ProvFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.util.TimeZone;

public class CustomXMLGregorianCalendarDeserializer extends JsonDeserializer<XMLGregorianCalendar> {
    private static final Logger logger = LogManager.getLogger(CustomXMLGregorianCalendarDeserializer.class);
    static final ProvFactory pf=ProvFactory.getFactory();
    private final DateTimeOption dateTimeOption;
    private final TimeZone optionalTimeZone;

    public CustomXMLGregorianCalendarDeserializer(DateTimeOption dateTimeOption) {
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = null;
        if (dateTimeOption==null) throw new TimeZoneException("Null dateTimeOption");
        if (dateTimeOption==DateTimeOption.TIMEZONE) throw new TimeZoneException("no TimeZone provided");
    }
    public CustomXMLGregorianCalendarDeserializer(DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = optionalTimeZone;
        if (dateTimeOption==null) throw new TimeZoneException("Null dateTimeOption");
    }

    @Override
    public XMLGregorianCalendar deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        //System.out.println("********* Deserializing XMLGregorianCalendar " + dateTimeOption + " " + optionalTimeZone);
        String text = jsonParser.getText();
        XMLGregorianCalendar xmlGregorianCalendar = pf.newISOTime(text, dateTimeOption, optionalTimeZone);
        logger.debug("Deserialized XMLGregorianCalendar: {} {}", text, xmlGregorianCalendar);
        return xmlGregorianCalendar;
    }
}
