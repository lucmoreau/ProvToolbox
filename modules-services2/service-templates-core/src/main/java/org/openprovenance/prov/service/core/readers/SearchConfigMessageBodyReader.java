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
public class SearchConfigMessageBodyReader implements MessageBodyReader<SearchConfig> {

    public SearchConfigMessageBodyReader() {
        //System.out.println("SearchConfigMessageBodyReader ....");
    }

    public String trimCharSet(MediaType mediaType) {
        String med=mediaType.toString();
        int ind=med.indexOf(";");
        if (ind>0) med=med.substring(0,ind);
        return med;
    }



    ObjectMapper om=new ObjectMapper();
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return mediaType.toString().startsWith(InteropMediaType.MEDIA_APPLICATION_JSON);
    }

    @Override
    public SearchConfig readFrom(Class<SearchConfig> aClass,
                                                       Type type,
                                                       Annotation[] annotations,
                                                       MediaType mediaType,
                                                       MultivaluedMap<String, String> multivaluedMap,
                                                       InputStream inputStream) throws IOException, WebApplicationException {

        SearchConfig tkl=om.readValue(inputStream, SearchConfig.class);

        return tkl;
    }
}
