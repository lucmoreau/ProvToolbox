package org.openprovenance.prov.core.jsonld11.serialization;


import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomAttributeDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomBundleDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomNamespaceDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.vanilla.Bundle;
import org.openprovenance.prov.vanilla.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.JSONLD_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.getAttributes;


public class ProvDeserialiser extends org.openprovenance.prov.core.json.serialization.ProvDeserialiser {
    final ObjectMapper mapper ;

    public ProvDeserialiser() {
        this(new ObjectMapper());
    }

    public ProvDeserialiser(ObjectMapper mapper) {
        this.mapper= mapper;
        customize(mapper);
    }

    public ProvMixin provMixin() {
        return new ProvMixin();
    }


    public ObjectMapper getMapper() {
        return mapper;
    }

    public org.openprovenance.prov.model.Document deserialiseDocument (InputStream in)  {
        getAttributes().get().remove(JSONLD_CONTEXT_KEY_NAMESPACE);
        try {
            return mapper.readValue(in, Document.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }

    public org.openprovenance.prov.model.Document deserialiseDocument (BufferedReader in)  {
        getAttributes().get().remove(JSONLD_CONTEXT_KEY_NAMESPACE);
        try {
            return mapper.readValue(in, Document.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }


    public void customize(ObjectMapper mapper) {

        SimpleModule module =
                new SimpleModule("CustomKindDeserializer", new Version(1, 0, 0, null, null, null));



        TypeFactory typeFactory = mapper.getTypeFactory();



        // DESERIALISER
        MapType mapType2 = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        ArrayType arrayType=typeFactory.constructArrayType(mapType2);

        module.addDeserializer(Namespace.class, newCustomNamespaceDeserializer(arrayType));
        module.addDeserializer(Bundle.class, new CustomBundleDeserializer());
        module.addDeserializer(Attribute.class, new CustomAttributeDeserializer());

        provMixin().addProvMixin(mapper);


        mapper.registerModule(module);
    }

    public CustomNamespaceDeserializer newCustomNamespaceDeserializer(ArrayType arrayType) {
        return new CustomNamespaceDeserializer(arrayType);
    }

}
