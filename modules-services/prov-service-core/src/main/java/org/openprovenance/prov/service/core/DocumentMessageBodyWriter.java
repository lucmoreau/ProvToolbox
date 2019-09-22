package org.openprovenance.prov.service.core;

import javax.ws.rs.Produces;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.ProvSerialiser;
import org.openprovenance.prov.vanilla.Document;
import org.openprovenance.prov.vanilla.ProvFactory;

@Provider
@Produces({ InteropMediaType.MEDIA_TEXT_TURTLE, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION,
	        InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML, InteropMediaType.MEDIA_APPLICATION_TRIG,
	        InteropMediaType.MEDIA_APPLICATION_RDF_XML, InteropMediaType.MEDIA_APPLICATION_JSON,
	        InteropMediaType.MEDIA_IMAGE_SVG_XML, InteropMediaType.MEDIA_APPLICATION_PDF })
public class DocumentMessageBodyWriter implements MessageBodyWriter<Document> {

	private final ProvSerialiser serializer;

	public String trimCharSet(MediaType mediaType) {
		String med=mediaType.toString();
		int ind=med.indexOf(";");
		if (ind>0) med=med.substring(0,ind);
		return med;
	}

	public DocumentMessageBodyWriter (ProvSerialiser serializer) {
		System.out.println("*********** DocumentMessageBodyWriter  ************");
		this.serializer=serializer;
	}

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
    		Annotation[] annotations, MediaType mediaType) {
		return serializer.mediaTypes().contains(trimCharSet(mediaType));
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

		System.out.println(" ---- writeTo doc " + mediaType);

		serializer.serialiseDocument(entityStream, doc, true);
	}

}
