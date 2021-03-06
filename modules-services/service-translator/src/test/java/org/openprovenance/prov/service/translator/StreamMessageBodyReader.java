package org.openprovenance.prov.service.translator;

import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.interop.InteropFramework;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class StreamMessageBodyReader implements MessageBodyReader<InputStream> {

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
