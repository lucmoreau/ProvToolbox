package org.openprovenance.prov.service.summary;

import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.scala.immutable.Document;
import org.openprovenance.prov.scala.summary.SummaryAPI;
import org.openprovenance.prov.scala.summary.SummaryConfig;import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;


import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces({
		InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION,
		InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML,
		InteropMediaType.MEDIA_APPLICATION_JSON,
		InteropMediaType.MEDIA_IMAGE_SVG_XML, InteropMediaType.MEDIA_APPLICATION_PDF })
public class ScalaDocumentMessageBodyWriter implements MessageBodyWriter<Document> {

	public String trimCharSet(MediaType mediaType) {
		String med=mediaType.toString();
		int ind=med.indexOf(";");
		if (ind>0) med=med.substring(0,ind);
		return med;
	}

	public ScalaDocumentMessageBodyWriter() {
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
							   Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public long getSize(Document t, Class<?> type, Type genericType,
						Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Document doc, Class<?> type, Type genericType,
						Annotation[] annotations, MediaType mediaType,
						MultivaluedMap<String, Object> httpHeaders,
						OutputStream entityStream) {
		SummaryConfig config=new SummaryConfig(entityStream,trimCharSet(mediaType));
		SummaryAPI.outputer(doc,config);
	}

}
