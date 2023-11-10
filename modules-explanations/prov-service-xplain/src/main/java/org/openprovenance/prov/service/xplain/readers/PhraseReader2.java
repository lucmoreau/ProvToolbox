package org.openprovenance.prov.service.xplain.readers;

import org.openprovenance.prov.scala.nlgspec_transformer.SpecLoader;
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class PhraseReader2 implements MessageBodyReader<specTypes.Phrase> {

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public specTypes.Phrase readFrom(Class<specTypes.Phrase> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        return SpecLoader.phraseImport(inputStream);
    }
}
