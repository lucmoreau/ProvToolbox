package org.openprovenance.prov.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import org.openprovenance.prov.interop.InteropMediaType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
public class GenericJsonMessageBodyReader<T> implements MessageBodyReader<T> {

    private final Class<T> clazz;

    public GenericJsonMessageBodyReader(Class<T> clazz) {
        this.clazz=clazz;
    }

    ObjectMapper om=new ObjectMapper();
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return (mediaType.toString().startsWith(InteropMediaType.MEDIA_APPLICATION_JSON));
    }

    @Override
    public T readFrom(Class<T> aClass,
                      Type type,
                      Annotation[] annotations,
                      MediaType mediaType,
                      MultivaluedMap<String, String> multivaluedMap,
                      InputStream inputStream) throws IOException, WebApplicationException {
        return om.readValue(inputStream, clazz);
    }
}
