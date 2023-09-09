package org.openprovenance.prov.validation.report.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.validation.report.*;

import javax.xml.datatype.XMLGregorianCalendar;

public class Mapper {

    static public ObjectMapper getValidationReportMapper() {
        ObjectMapper mapper = new ObjectMapper();

        ProvSerialiser serial=new ProvSerialiser(mapper, false);
        ProvDeserialiser deserial=new ProvDeserialiser();
        serial.customize(mapper);
        deserial.customize(mapper);

        SimpleModule module = new SimpleModule("ValidationDeserializer", new Version(1, 0, 0, (String)null, (String)null, (String)null));
        module.addDeserializer(XMLGregorianCalendar.class, new CustomXMLGregorianCalendarDeserializer());

        mapper.registerModule(module);

        mapper.addMixIn(ValidationReport.class,     ValidationReportInterface.class);
        mapper.addMixIn(Dependencies.class,         DependenciesInterface.class);
        mapper.addMixIn(MergeReport.class,          MergeReportInterface.class);
        mapper.addMixIn(SpecializationReport.class, SpecializationReportInterface.class);
        mapper.addMixIn(MalformedStatements.class,  DependenciesInterface.class);

        return mapper;
    }

}
