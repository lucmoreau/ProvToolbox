package org.openprovenance.prov.service.core.writers;

import jakarta.ws.rs.Produces;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import org.openprovenance.prov.model.interop.InteropMediaType;
import org.openprovenance.prov.model.ProvDocumentWriter;
import org.openprovenance.prov.vanilla.Document;

import static org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter.trimCharSet;

@Provider
@Produces({
		InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION, InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML,
		InteropMediaType.MEDIA_APPLICATION_JSON, InteropMediaType.MEDIA_APPLICATION_JSONLD,
		InteropMediaType.MEDIA_IMAGE_SVG_XML, InteropMediaType.MEDIA_APPLICATION_PDF,
		InteropMediaType.MEDIA_IMAGE_JPEG, InteropMediaType.MEDIA_IMAGE_PNG })
public class DocumentMessageBodyWriter implements MessageBodyWriter<Document> {

	private final ProvDocumentWriter documentWriter;

	public DocumentMessageBodyWriter (ProvDocumentWriter documentWriter) {
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
		documentWriter.writeDocument(entityStream, doc, media, true);
	}

}
