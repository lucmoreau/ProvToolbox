package org.openprovenance.prov.service.core.writers;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openprovenance.prov.interop.InteropMediaType;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces({ InteropMediaType.MEDIA_APPLICATION_JSON })
public class NodeMessageBodyWriter implements MessageBodyWriter<ObjectNode> {
    
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
    		Annotation[] annotations, MediaType mediaType) {
    	return true;
    }

    @Override
    public long getSize(ObjectNode t, Class<?> type, Type genericType,
    		Annotation[] annotations, MediaType mediaType) {
    	return -1;
    }

    @Override
    public void writeTo(ObjectNode node,
                        Class<?> arg1,
                        Type arg2,
                        Annotation[] arg3,
                        MediaType arg4,
                        MultivaluedMap<String, Object> arg5,
                        OutputStream out) throws IOException, WebApplicationException {

        writer.writeValue(out,node);
    }

}
