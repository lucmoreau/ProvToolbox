package org.openprovenance.prov.service.xplain.readers;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.type.ArrayType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.scala.nlgspec_transformer.SpecLoader;


import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import org.openprovenance.prov.service.xplain.PhraseEnvironment;


import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class PhraseEnvironmentReader implements MessageBodyReader<PhraseEnvironment> {

    static final Logger logger = LogManager.getLogger(PhraseEnvironmentReader.class);

    ProvDeserialiser deserialiser;
    private final ObjectMapper objectMapper;

    public PhraseEnvironmentReader() {
        /* Jackson 3: mappers are immutable. With Jackson 2, the PROV deserialisers were added to
         * SpecLoader's shared mapper; now they are added to a copy of it (rebuild()), whose mapper
         * reads the whole phrase environment, the PROV document included. */
        this.deserialiser=new CustomDeserialiser(SpecLoader.mapper().rebuild());
        this.objectMapper = deserialiser.getMapper();
    }
    
    static class CustomDeserialiser extends ProvDeserialiser {
        CustomDeserialiser(MapperBuilder<?, ?> builder) {
            super(builder);
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
        } catch (JacksonException re) {
            logger.throwing(re);
            throw re;
        }
    }

}
