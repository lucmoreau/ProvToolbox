package org.openprovenance.prov.core.json.serialization;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.openprovenance.prov.core.json.serialization.deserial.CustomAttributeSetDeserializer;
import org.openprovenance.prov.core.json.serialization.deserial.CustomBundleDeserializer;
import org.openprovenance.prov.core.json.serialization.deserial.CustomKindDeserializer;
import org.openprovenance.prov.core.json.serialization.deserial.CustomNamespaceDeserializer;
import org.openprovenance.prov.core.vanilla.Bundle;
import org.openprovenance.prov.core.vanilla.ProvFactory;
import org.openprovenance.prov.model.Namespace;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class ProvDeserialiser {

    ProvFactory pf=new ProvFactory();

    private final ProvMixin provMixin = new ProvMixin();


    public org.openprovenance.prov.model.Document deserialiseDocument (File serialised) throws IOException {
        return deserialiseDocument(new FileInputStream(serialised));
    }
    public org.openprovenance.prov.model.Document deserialiseDocument (InputStream in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module =
                new SimpleModule("CustomKindSerializer", new Version(1, 0, 0, null, null, null));

        module.addDeserializer(org.openprovenance.prov.model.StatementOrBundle.Kind.class, new CustomKindDeserializer());


        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType setType = typeFactory.constructCollectionType(Set.class, org.openprovenance.prov.model.Attribute.class);
        JavaType qnType = mapper.getTypeFactory().constructType(org.openprovenance.prov.model.QualifiedName.class);
        MapType mapType = typeFactory.constructMapType(HashMap.class, qnType, setType);
       // module.addDeserializer(Map.class,new CustomAttributeMapDeserializer(mapType));




        MapType mapType2 = typeFactory.constructMapType(HashMap.class, String.class, String.class);
        module.addDeserializer(Namespace.class, new CustomNamespaceDeserializer(mapType2));


        module.addDeserializer(Bundle.class, new CustomBundleDeserializer());



        module.addDeserializer(Set.class,new CustomAttributeSetDeserializer(setType));

        provMixin.addProvMixin(mapper);


        mapper.registerModule(module);

        SortedDocument doc= mapper.readValue(in, SortedDocument.class);

        return doc.toDocument(pf);

    }

}
