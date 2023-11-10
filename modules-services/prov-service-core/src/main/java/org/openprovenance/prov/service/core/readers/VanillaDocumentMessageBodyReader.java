package org.openprovenance.prov.service.core.readers;

import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.vanilla.Document;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter.trimCharSet;

@Provider
public class VanillaDocumentMessageBodyReader implements MessageBodyReader<Document> {
	final ProvFactory pf;
    final InteropFramework intF;

	public VanillaDocumentMessageBodyReader(ProvFactory pf) {
		this.pf=pf;
		this.intF=new InteropFramework(pf);
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
							  Annotation[] annotations, MediaType mediaType) {
		ProvFormat format=intF.mimeTypeRevMap.get(trimCharSet(mediaType));
		return format!=null;
	}

	@Override
	public Document readFrom(Class<Document> type, Type genericType,
                             Annotation[] annotations, MediaType mediaType,
                             MultivaluedMap<String, String> httpHeaders,
                             InputStream is) throws IOException, WebApplicationException {
		ProvFormat format=intF.mimeTypeRevMap.get(trimCharSet(mediaType));
        return (Document)intF.readDocument(is,format);
	}
}
