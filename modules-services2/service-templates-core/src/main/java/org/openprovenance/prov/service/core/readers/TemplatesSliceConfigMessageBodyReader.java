package org.openprovenance.prov.service.core.readers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import org.openprovenance.prov.model.interop.InteropMediaType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
public class TemplatesSliceConfigMessageBodyReader implements MessageBodyReader<TemplatesSliceConfig> {

    ObjectMapper om=new ObjectMapper();

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return TemplatesSliceConfig.class.isAssignableFrom(aClass)
                && mediaType.toString().startsWith(InteropMediaType.MEDIA_APPLICATION_JSON);
    }

    @Override
    public TemplatesSliceConfig readFrom(Class<TemplatesSliceConfig> aClass,
                                         Type type,
                                         Annotation[] annotations,
                                         MediaType mediaType,
                                         MultivaluedMap<String, String> multivaluedMap,
                                         InputStream inputStream) throws IOException, WebApplicationException {

        return om.readValue(inputStream, TemplatesSliceConfig.class);
    }
}
