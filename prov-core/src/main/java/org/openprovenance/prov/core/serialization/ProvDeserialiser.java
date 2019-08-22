package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.openprovenance.prov.core.Document;
import org.openprovenance.prov.core.Location;
import org.openprovenance.prov.core.Other;
import org.openprovenance.prov.core.Type;
import org.openprovenance.prov.core.serialization.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProvDeserialiser {

    public org.openprovenance.prov.model.Document deserialiseDocument (File serialised) throws IOException, FileNotFoundException {
        return deserialiseDocument(new FileInputStream(serialised));
    }
    public org.openprovenance.prov.model.Document deserialiseDocument (InputStream in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module =
                new SimpleModule("CustomKindSerializer", new Version(1, 0, 0, null, null, null));

     //   module.addDeserializer(org.openprovenance.prov.model.QualifiedName.class, new CustomQualifiedNameDeserializer());
        module.addDeserializer(org.openprovenance.prov.model.StatementOrBundle.Kind.class, new CustomKindDeserializer());
        module.addDeserializer(Type.class, new CustomTypeDeserializer());
        module.addDeserializer(Location.class, new CustomLocationDeserializer());
        module.addDeserializer(Other.class, new CustomOtherDeserializer());

        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType setType = typeFactory.constructCollectionType(Set.class, org.openprovenance.prov.model.Attribute.class);
        JavaType qnType = mapper.getTypeFactory().constructType(org.openprovenance.prov.model.QualifiedName.class);
        MapType mapType = typeFactory.constructMapType(HashMap.class, qnType, setType);
        module.addDeserializer(Map.class,new CustomAttributeMapDeserializer(mapType));

        //CollectionType setType2 = typeFactory.constructCollectionType(Set.class, org.openprovenance.prov.core.TypedValue.class);

        module.addDeserializer(Set.class,new CustomAttributeSetDeserializer(setType));


        mapper.registerModule(module);

        return mapper.readValue(in, Document.class);

    }

}
