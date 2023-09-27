package org.openprovenance.prov.service;

import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.interop.InteropFramework;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class



StreamMessageBodyReader implements MessageBodyReader<InputStream> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
							  Annotation[] annotations, MediaType mediaType) {
		InteropFramework intF=new InteropFramework();

		ProvFormat format=intF.mimeTypeRevMap.get(mediaType.toString());
		//System.out.println("*** Supported format " + format + " for type " + type + " given media type " + mediaType.toString()  + " " + intF.mimeTypeRevMap);
		return format!=null;
	}

	@Override
	public InputStream readFrom(Class<InputStream> type, Type genericType,
								Annotation[] annotations, MediaType mediaType,
								MultivaluedMap<String, String> httpHeaders,
								InputStream is) throws IOException,
			WebApplicationException {
		return is;
	}

}
