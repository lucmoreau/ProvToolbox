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
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Document;

@Provider
public class DocumentMessageBodyReader implements MessageBodyReader<Document> {

    public String trimCharSet(MediaType mediaType) {
	String med=mediaType.toString();
	int ind=med.indexOf(";");
	if (ind>0) med=med.substring(0,ind);
	return med;
    }


    @Override
    public boolean isReadable(Class<?> type, Type genericType,
			      Annotation[] annotations, MediaType mediaType) {
	InteropFramework intF=new InteropFramework();
	
	ProvFormat format=intF.mimeTypeRevMap.get(trimCharSet(mediaType));
	System.out.println("*** Supported format " + format + " for type " + type + " given media type " + mediaType.toString()  + " " + intF.mimeTypeRevMap);
	return format!=null;
    }

    @Override
    public Document readFrom(Class<Document> type, Type genericType,
			     Annotation[] annotations, MediaType mediaType,
			     MultivaluedMap<String, String> httpHeaders,
			     InputStream is) throws IOException,
						      WebApplicationException {
	InteropFramework intF=new InteropFramework();
	ProvFormat format=intF.mimeTypeRevMap.get(trimCharSet(mediaType));
	System.out.println("--->>>> Found " + format);
	Document doc=intF.readDocument(is, format,"file://stdin/message/");
	return doc;
    }

}
