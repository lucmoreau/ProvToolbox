package org.openprovenance.prov.service.readers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.service.TableKeyList;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
public class TableKeyListMessageBodyReader implements MessageBodyReader<TableKeyList> {

    public TableKeyListMessageBodyReader() {
        System.out.println("TableKeyListMessageBodyReader ....");
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
    public TableKeyList readFrom(Class<TableKeyList> aClass,
                                 Type type,
                                 Annotation[] annotations,
                                 MediaType mediaType,
                                 MultivaluedMap<String, String> multivaluedMap,
                                 InputStream inputStream) throws IOException, WebApplicationException {

        TableKeyList tkl=om.readValue(inputStream, TableKeyList.class);

        return tkl;
    }
}
