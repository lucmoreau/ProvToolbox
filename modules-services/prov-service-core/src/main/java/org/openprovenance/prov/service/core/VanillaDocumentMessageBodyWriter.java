package org.openprovenance.prov.service.core;

import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.ProvDocumentWriter;
import org.openprovenance.prov.vanilla.Document;

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
@Produces({
		InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION,
		InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML,
		InteropMediaType.MEDIA_APPLICATION_JSONLD,
		InteropMediaType.MEDIA_APPLICATION_JSON,
		InteropMediaType.MEDIA_IMAGE_SVG_XML,
		InteropMediaType.MEDIA_APPLICATION_PDF,
		InteropMediaType.MEDIA_IMAGE_JPEG,
		InteropMediaType.MEDIA_IMAGE_PNG
})
public class VanillaDocumentMessageBodyWriter implements MessageBodyWriter<Document> {

	private final ProvDocumentWriter documentWriter;

	public String trimCharSet(MediaType mediaType) {
		String med=mediaType.toString();
		int ind=med.indexOf(";");
		if (ind>0) med=med.substring(0,ind);
		return med;
	}

	public VanillaDocumentMessageBodyWriter(ProvDocumentWriter documentWriter) {
		//System.out.println("*********** VanillaDocumentMessageBodyWriter  ************");
		this.documentWriter = documentWriter;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
							   Annotation[] annotations, MediaType mediaType) {
		return documentWriter.mediaTypes().contains(trimCharSet(mediaType));
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
						OutputStream entityStream) throws IOException, WebApplicationException {

		String media=trimCharSet(mediaType);
		documentWriter.serialiseDocument(entityStream, doc, media, true);
	}

}
