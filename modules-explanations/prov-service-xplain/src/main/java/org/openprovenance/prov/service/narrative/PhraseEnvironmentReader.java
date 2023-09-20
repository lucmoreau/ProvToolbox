package org.openprovenance.prov.service.narrative;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.scala.nlgspec_transformer.SpecLoader;


import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;


import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class PhraseEnvironmentReader implements MessageBodyReader<PhraseEnvironment> {

    static final Logger logger = LogManager.getLogger(PhraseEnvironmentReader.class);

    ProvDeserialiser deserialiser;
    private final ObjectMapper objectMapper;

    public PhraseEnvironmentReader() {


        this.objectMapper = SpecLoader.mapper();
        this.deserialiser=new CustomDeserialiser(this.objectMapper);


    }
    
    static class CustomDeserialiser extends ProvDeserialiser {
        CustomDeserialiser(ObjectMapper om) {
            super(om);
        }

        @Override
        public org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomNamespaceDeserializer newCustomNamespaceDeserializer(ArrayType arrayType) {
            return new CustomNamespaceDeserializer(arrayType);
        }
    }

    static class CustomNamespaceDeserializer extends org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomNamespaceDeserializer {
        public CustomNamespaceDeserializer(JavaType tr) {
            super(tr);
        }

        @Override
        public void processContextObject(Namespace ns, Object o) {
            super.processContextObject(ns, o);
            if (o instanceof scala.collection.immutable.Map) {
                scala.collection.immutable.Map<?,?> m = (scala.collection.immutable.Map<?,?>) o;
                super.processContextObject(ns, scala.jdk.CollectionConverters.MapHasAsJava(m).asJava());
            }
        }
    }

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public PhraseEnvironment readFrom(Class<PhraseEnvironment> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {

        try {
            return objectMapper.readValue(inputStream, PhraseEnvironment.class);
        } catch (com.fasterxml.jackson.databind.JsonMappingException re) {
            logger.throwing(re);
            throw re;
        }
    }

}
