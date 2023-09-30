package org.openprovenance.prov.service.xplain.writers;

import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.scala.nlg.Narrative;
import org.openprovenance.prov.scala.nlgspec_transformer.SpecLoader;

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
@Produces({InteropMediaType.MEDIA_APPLICATION_JSON })
public class NarrativeWriter implements MessageBodyWriter<Narrative> {

	public String trimCharSet(MediaType mediaType) {
		String med=mediaType.toString();
		int ind=med.indexOf(";");
		if (ind>0) med=med.substring(0,ind);
		return med;
	}

	public NarrativeWriter() {
		System.out.println("*********** Narrative Writer   ************");
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
							   Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public long getSize(Narrative t, Class<?> type, Type genericType,
						Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Narrative narrative, Class<?> type, Type genericType,
						Annotation[] annotations, MediaType mediaType,
						MultivaluedMap<String, Object> httpHeaders,
						OutputStream entityStream) throws IOException {

		System.out.println(" ---- writeTo Narrative " + mediaType);

		SpecLoader.mapper().writeValue(entityStream,narrative);

	}

}
