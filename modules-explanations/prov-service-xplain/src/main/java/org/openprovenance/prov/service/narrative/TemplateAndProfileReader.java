package org.openprovenance.prov.service.narrative;

import org.openprovenance.prov.scala.query.JsonSupport;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class TemplateAndProfileReader implements MessageBodyReader<TemplateAndProfile> {

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public TemplateAndProfile readFrom(Class<TemplateAndProfile> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        return JsonSupport.om().readValue(inputStream,TemplateAndProfile.class);
    }
}
