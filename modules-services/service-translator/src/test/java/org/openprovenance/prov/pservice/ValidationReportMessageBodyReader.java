package org.openprovenance.prov.pservice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBException;

import org.openprovenance.prov.xml.validation.ProvDeserialiser;
import org.openprovenance.prov.xml.validation.ValidationReport;

@Provider
public class ValidationReportMessageBodyReader implements MessageBodyReader<ValidationReport> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
			      Annotation[] annotations, MediaType mediaType) {
        return mediaType.equals(MediaType.APPLICATION_XML_TYPE);
    }

    @Override
    public ValidationReport readFrom(Class<ValidationReport> type, Type genericType,
			                         Annotation[] annotations, MediaType mediaType,
			                         MultivaluedMap<String, String> httpHeaders,
			                         InputStream is) throws IOException, WebApplicationException {
    	ValidationReport report=null;
    	ProvDeserialiser deserialiser=ProvDeserialiser.getThreadProvDeserialiser();
    	try {
    		report=deserialiser.deserialiseValidationReport(is);
    	} catch (JAXBException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	return report;	       

    }

}
