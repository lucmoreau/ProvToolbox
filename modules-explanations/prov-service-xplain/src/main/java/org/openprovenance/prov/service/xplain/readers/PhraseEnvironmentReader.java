package org.openprovenance.prov.service.xplain.readers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.ArrayType;
import org.openprovenance.prov.vanilla.Document;
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

    /* TEMPORARY BRIDGE (JACKSON3_MIGRATION.md, step C2): the PROV document is read with the
     * Jackson 3 PROV deserialiser, the rest of the phrase environment (Scala types of
     * nlg-spec-transformer) with SpecLoader's Jackson 2 mapper. Before, the PROV deserialisers were
     * added to SpecLoader's (shared) mapper, which cannot work while prov-jsonld is on Jackson 3 and
     * nlg-spec-transformer on Jackson 2. Remove when nlg-spec-transformer is on Jackson 3 (step C7). */
    ProvDeserialiser deserialiser;
    private final ObjectMapper objectMapper;

    public PhraseEnvironmentReader() {


        this.objectMapper = SpecLoader.mapper();
        this.deserialiser=new CustomDeserialiser(JsonMapper.builderWithJackson2Defaults());


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
            // see the TEMPORARY BRIDGE note above
            JsonNode tree = objectMapper.readTree(inputStream);
            JsonNode document = tree instanceof ObjectNode ? ((ObjectNode) tree).remove("document") : null;
            PhraseEnvironment environment = objectMapper.treeToValue(tree, PhraseEnvironment.class);
            if (document != null && !document.isNull()) {
                byte[] json = objectMapper.writeValueAsBytes(document);
                environment.setDocument((Document) deserialiser.deserialiseDocument(new java.io.ByteArrayInputStream(json)));
            }
            return environment;
        } catch (com.fasterxml.jackson.databind.JsonMappingException re) {
            logger.throwing(re);
            throw re;
        }
    }

}
