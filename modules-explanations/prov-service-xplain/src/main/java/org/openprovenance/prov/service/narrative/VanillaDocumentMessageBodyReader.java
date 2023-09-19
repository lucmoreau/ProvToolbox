package org.openprovenance.prov.service.narrative;

import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.vanilla.Document;

import jakarta.ws.rs.Consumes;
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
@Consumes({ InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION,
		InteropMediaType.MEDIA_APPLICATION_JSON,
		InteropMediaType.MEDIA_APPLICATION_JSONLD})
public class VanillaDocumentMessageBodyReader implements MessageBodyReader<Document> {


	private final InteropFramework intF;

	public VanillaDocumentMessageBodyReader(InteropFramework interopFramework) {
		this.intF=interopFramework;
	}

	public String trimCharSet(MediaType mediaType) {
		String med=mediaType.toString();
		int ind=med.indexOf(";");
		if (ind>0) med=med.substring(0,ind);
		return med;
	}


	@Override
	public boolean isReadable(Class<?> type, Type genericType,
							  Annotation[] annotations, MediaType mediaType) {

		ProvFormat format=intF.mimeTypeRevMap.get(trimCharSet(mediaType));
		//System.out.println("*** Supported format " + format + " for type " + type + " given media type " + mediaType.toString()  + " " + intF.mimeTypeRevMap);
		return format!=null;
	}

	@Override
	public Document readFrom(Class<Document> type, Type genericType,
                             Annotation[] annotations, MediaType mediaType,
                             MultivaluedMap<String, String> httpHeaders,
                             InputStream is) throws IOException,
			WebApplicationException {
		ProvFormat format=intF.mimeTypeRevMap.get(trimCharSet(mediaType));
		//System.out.println("--->>>> Found " + format);
		Document doc=(Document)intF.deserialiseDocument(is,format);
		return doc;
	}





}
