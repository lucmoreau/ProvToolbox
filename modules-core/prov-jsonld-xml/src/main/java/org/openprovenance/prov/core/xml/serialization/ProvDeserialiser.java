package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.openprovenance.prov.core.xml.serialization.deserial.CustomAttributeSetDeserializer;
import org.openprovenance.prov.core.xml.serialization.deserial.StatementsHandler;
import org.openprovenance.prov.vanilla.Document;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.*;
import java.util.*;

public class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {

    public static final ProvFactory pf=new ProvFactory();

    private final ProvMixin provMixin = new ProvMixin();


    public org.openprovenance.prov.model.Document deserialiseDocument (File serialised) throws IOException, FileNotFoundException {
        return deserialiseDocument(new FileInputStream(serialised));
    }


    public org.openprovenance.prov.model.Document deserialiseDocument (InputStream in) throws IOException {
        XmlMapper mapper = getMapper();


        return mapper.readValue(in, Document.class);

    }

    public XmlMapper getMapper() {
        XmlMapper mapper = new XmlMapper();

        SimpleModule module =
                new StatementsHandler("CustomKindDeserializer", new Version(1, 0, 0, null, null, null));

        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType setType = typeFactory.constructCollectionType(Set.class, org.openprovenance.prov.model.Attribute.class);

        //MapType mapType2 = typeFactory.constructMapType(HashMap.class, String.class, String.class);

        module.addDeserializer(Set.class,new CustomAttributeSetDeserializer(setType));
        mapper.registerModule(module);
        provMixin.addProvMixin(mapper);
        return mapper;
    }

}
