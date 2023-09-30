package org.openprovenance.prov.service.summary.writers;

import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.scala.summary.Level0Mapper;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces({
		InteropMediaType.MEDIA_APPLICATION_JSON })
public class Level0MessageBodyWriter implements MessageBodyWriter<Level0Mapper> {

	public String trimCharSet(MediaType mediaType) {
		String med=mediaType.toString();
		int ind=med.indexOf(";");
		if (ind>0) med=med.substring(0,ind);
		return med;
	}

	public Level0MessageBodyWriter() {
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
							   Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public long getSize(Level0Mapper t, Class<?> type, Type genericType,
						Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Level0Mapper doc, Class<?> type, Type genericType,
						Annotation[] annotations, MediaType mediaType,
						MultivaluedMap<String, Object> httpHeaders,
						OutputStream entityStream) throws IOException {

		System.out.println(" ---- writeTo level0mapper " + mediaType);

		doc.exportToJSon(entityStream);
	}

}
