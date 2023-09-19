package org.openprovenance.prov.service.narrative;

import org.openprovenance.prov.scala.wrapper.IO;
import org.openprovenance.prov.scala.wrapper.defs;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class PhraseReader implements MessageBodyReader<defs.Phrase> {

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public defs.Phrase readFrom(Class<defs.Phrase> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        return IO.phraseImport(inputStream);
    }
}
