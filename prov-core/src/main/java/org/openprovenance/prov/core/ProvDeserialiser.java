package org.openprovenance.prov.core;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.*;

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

        mapper.registerModule(module);

        return mapper.readValue(in,Document.class);

    }

}
