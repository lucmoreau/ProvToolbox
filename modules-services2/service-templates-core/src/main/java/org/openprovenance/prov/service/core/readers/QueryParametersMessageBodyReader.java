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

/**
 * JAX-RS {@link MessageBodyReader} that deserialises an {@code application/json}
 * request body into a {@link QueryParameters} instance.
 *
 * <p>Registered as a singleton in
 * {@link org.openprovenance.prov.service.core.ProvapiApplication}.</p>
 */
@Provider
@Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
public class QueryParametersMessageBodyReader implements MessageBodyReader<QueryParameters> {

    private final ObjectMapper om = new ObjectMapper();

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
                              Annotation[] annotations, MediaType mediaType) {
        return QueryParameters.class.isAssignableFrom(type)
                && mediaType.toString().startsWith(InteropMediaType.MEDIA_APPLICATION_JSON);
    }

    @Override
    public QueryParameters readFrom(Class<QueryParameters> type,
                                    Type genericType,
                                    Annotation[] annotations,
                                    MediaType mediaType,
                                    MultivaluedMap<String, String> httpHeaders,
                                    InputStream entityStream)
            throws IOException, WebApplicationException {
        return om.readValue(entityStream, QueryParameters.class);
    }
}
