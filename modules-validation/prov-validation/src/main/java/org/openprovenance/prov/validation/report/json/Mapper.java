package org.openprovenance.prov.validation.report.json;

import tools.jackson.core.Version;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.validation.report.*;

import javax.xml.datatype.XMLGregorianCalendar;

public class Mapper {

    static public ObjectMapper getValidationReportMapper() {
        // Jackson 3: immutable mappers, so everything is configured on one builder, then built.
        // Jackson 2 defaults keep the output unchanged (docs/JACKSON3_MIGRATION.md, D2).
        JsonMapper.Builder mapper = JsonMapper.builderWithJackson2Defaults();

        new ProvSerialiser().customize(mapper);
        new ProvDeserialiser().customize(mapper);

        SimpleModule module = new SimpleModule("ValidationDeserializer", new Version(1, 0, 0, (String)null, (String)null, (String)null));
        module.addDeserializer(XMLGregorianCalendar.class, new CustomXMLGregorianCalendarDeserializer());

        mapper.addModule(module);

        mapper.addMixIn(ValidationReport.class,     ValidationReportInterface.class);
        mapper.addMixIn(Dependencies.class,         DependenciesInterface.class);
        mapper.addMixIn(MergeReport.class,          MergeReportInterface.class);
        mapper.addMixIn(SpecializationReport.class, SpecializationReportInterface.class);
        mapper.addMixIn(MalformedStatements.class,  DependenciesInterface.class);

        return mapper.build();
    }

}
