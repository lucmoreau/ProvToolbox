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

public class ProvDeserialiser {

    public static final ProvFactory pf=new ProvFactory();

    private final ProvMixin provMixin = new ProvMixin();


    public org.openprovenance.prov.model.Document deserialiseDocument (File serialised) throws IOException, FileNotFoundException {
        return deserialiseDocument(new FileInputStream(serialised));
    }


    public org.openprovenance.prov.model.Document deserialiseDocument (InputStream in) throws IOException {
        XmlMapper mapper = new XmlMapper();

        SimpleModule module =
                new StatementsHandler("CustomKindSerializer", new Version(1, 0, 0, null, null, null));

     //   module.addDeserializer(org.openprovenance.prov.model.QualifiedName.class, new CustomQualifiedNameDeserializer());
     //   module.addDeserializer(org.openprovenance.prov.model.StatementOrBundle.Kind.class, new CustomKindDeserializer());
       // module.addDeserializer(Type.class, new CustomTypeDeserializer());
       // module.addDeserializer(Location.class, new CustomLocationDeserializer());
       // module.addDeserializer(Other.class, new CustomOtherDeserializer());


        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType setType = typeFactory.constructCollectionType(Set.class, org.openprovenance.prov.model.Attribute.class);
   //    JavaType qnType = mapper.getTypeFactory().constructType(org.openprovenance.prov.model.QualifiedName.class);
   //     MapType mapType = typeFactory.constructMapType(Map.class, qnType, setType);
  //      module.addDeserializer(Map.class,new CustomAttributeMapDeserializer(mapType));


       // CollectionType listType = typeFactory.constructCollectionType(List.class, qnType);


        MapType mapType2 = typeFactory.constructMapType(HashMap.class, String.class, String.class);
        //module.addDeserializer(Namespace.class, new CustomNamespaceDeserializer(mapType2));


        //CollectionType setType2 = typeFactory.constructCollectionType(Set.class, org.openprovenance.prov.core.vanilla.TypedValue.class);

        module.addDeserializer(Set.class,new CustomAttributeSetDeserializer(setType));



        provMixin.addProvMixin(mapper);


        mapper.registerModule(module);



        return mapper.readValue(in, Document.class);

    }

}
