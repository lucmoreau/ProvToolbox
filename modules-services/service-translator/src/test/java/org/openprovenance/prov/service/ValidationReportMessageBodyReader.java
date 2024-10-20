package org.openprovenance.prov.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.validation.report.ValidationReport;
import org.openprovenance.prov.validation.report.json.ValidationReportInterface;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class ValidationReportMessageBodyReader implements MessageBodyReader<ValidationReport> {
    static Logger logger = LogManager.getLogger(ValidationReportMessageBodyReader.class);

    public String trimCharSet(MediaType mediaType) {
        String med=mediaType.toString();
        int ind=med.indexOf(";");
        if (ind>0) med=med.substring(0,ind);
        return med;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
			      Annotation[] annotations, MediaType mediaType) {
        return trimCharSet(mediaType).equals(MediaType.APPLICATION_JSON_TYPE.toString());
    }

    @Override
    public ValidationReport readFrom(Class<ValidationReport> type, Type genericType,
			                         Annotation[] annotations, MediaType mediaType,
			                         MultivaluedMap<String, String> httpHeaders,
			                         InputStream is) throws IOException, WebApplicationException {
    	ValidationReport report=null;

        logger.info("readFrom() returning ValidationReport");
        ObjectMapper mapper= new ProvDeserialiser().getMapper();
        //mapper.addMixIn(ValidationReport.class,  IsDocumentWithFieldsToIgnore.class);
        mapper.addMixIn(ValidationReport.class,  ValidationReportInterface.class);

        logger.info("readFrom() mixin set");

        //ObjectMapper mapper= Mapper.getValidationReportMapper();


        report=mapper.readValue(is,ValidationReport.class);
        logger.info("readFrom() done");

        new ProvSerialiser().getMapper().writeValue(System.out, report);
        System.out.println();


    	return report;	       

    }

}
