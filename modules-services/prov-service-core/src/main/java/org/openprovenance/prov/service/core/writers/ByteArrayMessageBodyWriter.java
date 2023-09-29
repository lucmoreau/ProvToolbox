package org.openprovenance.prov.service.core.writers;

import jakarta.ws.rs.Produces;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import org.openprovenance.prov.interop.InteropMediaType;

@Provider
@Produces({ InteropMediaType.MEDIA_TEXT_XML})
public class ByteArrayMessageBodyWriter implements MessageBodyWriter<ByteArrayOutputStream> {
    
    public String trimCharSet(MediaType mediaType) {
        String med=mediaType.toString();
        int ind=med.indexOf(";");
        if (ind>0) med=med.substring(0,ind);
        return med;
    }

    public ByteArrayMessageBodyWriter () {
        System.out.println("*********** ByteArrayMessageBodyWriter  ************");
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        if (type==ByteArrayOutputStream.class) return true;
        return false;
    }

    @Override
    public long getSize(ByteArrayOutputStream t, Class<?> type, Type genericType,
    		Annotation[] annotations, MediaType mediaType) {
    	return -1;
    }

    @Override
    public void writeTo(ByteArrayOutputStream doc, Class<?> type, Type genericType,
    		            Annotation[] annotations, MediaType mediaType,
    		            MultivaluedMap<String, Object> httpHeaders,
    		            OutputStream out) throws IOException, WebApplicationException {

        System.out.println(" ---- ByteArray writeTo doc " + " " + mediaType);
        

        doc.writeTo(out);

    }

}
