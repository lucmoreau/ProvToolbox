package org.openprovenance.prov.service.core;

import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.ProvSerialiser;
import org.openprovenance.prov.vanilla.Document;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces({ InteropMediaType.MEDIA_TEXT_TURTLE, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION,
	        InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML, InteropMediaType.MEDIA_APPLICATION_TRIG,
	        InteropMediaType.MEDIA_APPLICATION_RDF_XML, InteropMediaType.MEDIA_APPLICATION_JSON,
	        InteropMediaType.MEDIA_IMAGE_SVG_XML, InteropMediaType.MEDIA_APPLICATION_PDF,
		    InteropMediaType.MEDIA_IMAGE_JPEG, InteropMediaType.MEDIA_IMAGE_PNG,
		    InteropMediaType.MEDIA_APPLICATION_JSONLD})
public class VanillaDocumentMessageBodyWriter implements MessageBodyWriter<Document> {

	private final ProvSerialiser serializer;

	public String trimCharSet(MediaType mediaType) {
		String med=mediaType.toString();
		int ind=med.indexOf(";");
		if (ind>0) med=med.substring(0,ind);
		return med;
	}

	public VanillaDocumentMessageBodyWriter(ProvSerialiser serializer) {
		System.out.println("*********** VanillaDocumentMessageBodyWriter  ************");
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

		String media=trimCharSet(mediaType);

		System.out.println(" ---- writeTo doc " + media);

	//	if (InteropMediaType.MEDIA_APPLICATION_JSONLD.equals(media)) {
	//		new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser().serialiseDocument(entityStream,doc,false);
	//	} else {
			serializer.serialiseDocument(entityStream, doc, media, true);
	//	}
	}

}
