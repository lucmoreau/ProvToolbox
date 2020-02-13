package org.openprovenance.prov.core.jsonld11.serialization;


import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomAttributeDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomNamespaceDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.vanilla.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

//import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomAttributeMapDeserializer;

public class ProvDeserialiser extends org.openprovenance.prov.core.json.serialization.ProvDeserialiser {

    public ProvDeserialiser() {
        customize(mapper);
    }

    public ProvMixin provMixin() {
        return new ProvMixin();
    }

    final ObjectMapper mapper = new ObjectMapper();

    public org.openprovenance.prov.model.Document deserialiseDocument (InputStream in)  {
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
        module.addDeserializer(Namespace.class, new CustomNamespaceDeserializer(arrayType));



        module.addDeserializer(Attribute.class,new CustomAttributeDeserializer());

        provMixin().addProvMixin(mapper);


        mapper.registerModule(module);
    }

}
