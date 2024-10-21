package org.openprovenance.prov.validation.report.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.validation.report.ValidationReport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.openprovenance.prov.validation.Gensym.VAL_URI;
import static org.openprovenance.prov.validation.report.json.Mapper.getValidationReportMapper;

public class Serialization {

    public static void registerMissingNamespace(ValidationReport report) {
        report.getNamespace().register("val", VAL_URI + "xyz/");
    }

    public static void serializeValidationReport(Object report, OutputStream out) throws IOException {
        ObjectMapper mapper=getValidationReportMapper();
        //ObjectMapper mapper= new ProvSerialiser().getMapper();
        //mapper.addMixIn(ValidationReport.class,  ValidationReportInterface.class);
        mapper.writeValue(out,report);
    }
    public static String serializeValidationReportAsString(ValidationReport report) throws JsonProcessingException {
        ObjectMapper mapper=getValidationReportMapper();
        //ObjectMapper mapper= new ProvSerialiser().getMapper();
        //mapper.addMixIn(ValidationReport.class,  ValidationReportInterface.class);
        String s = mapper.writeValueAsString(report);
        System.out.println("SERIALIZED REPORT: " + s);
        return s;
    }

    public static ValidationReport deserializeValidationReport(InputStream in) throws IOException {
        ObjectMapper mapper=getValidationReportMapper();
        //ObjectMapper mapper= new ProvDeserialiser().getMapper();
        //mapper.addMixIn(ValidationReport.class,  ValidationReportInterface.class);
        return mapper.readValue(in, ValidationReport.class);
    }
    public static ValidationReport deserializeValidationReport(String in) throws IOException {
        ObjectMapper mapper=getValidationReportMapper();
        //new ProvDeserialiser().getMapper();
        //mapper.addMixIn(ValidationReport.class,  ValidationReportInterface.class);

        return mapper.readValue(in, ValidationReport.class);
    }


}
