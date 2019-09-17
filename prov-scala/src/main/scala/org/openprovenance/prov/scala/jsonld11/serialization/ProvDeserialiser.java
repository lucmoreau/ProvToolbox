package org.openprovenance.prov.scala.jsonld11.serialization;


import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.scala.immutable.ProvFactory;
import org.openprovenance.prov.scala.jsonld11.serialization.deserial.CustomAttributeMapDeserializer;
import org.openprovenance.prov.scala.jsonld11.serialization.deserial.CustomAttributeSetDeserializer;
import org.openprovenance.prov.scala.jsonld11.serialization.deserial.CustomKindDeserializer;
import org.openprovenance.prov.scala.jsonld11.serialization.deserial.CustomNamespaceDeserializer;
import org.openprovenance.prov.core.vanilla.Document;
import org.openprovenance.prov.model.Namespace;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProvDeserialiser extends org.openprovenance.prov.core.jsonld.serialization.ProvDeserialiser {


    public ProvMixin2 provMixin() {
        return new ProvMixin2();
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
        module.addDeserializer(Map.class,new CustomAttributeMapDeserializer(mapType));




        MapType mapType2 = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        ArrayType arrayType=typeFactory.constructArrayType(mapType2);
        module.addDeserializer(Namespace.class, new CustomNamespaceDeserializer(arrayType));



        module.addDeserializer(Set.class,new CustomAttributeSetDeserializer(setType));

        provMixin().addProvMixin(mapper);


        mapper.registerModule(module);

        Document doc= mapper.readValue(in, Document.class);

        org.openprovenance.prov.model.Document doc4=pf.newDocument(doc.getNamespace(),pu.getStatement(doc), pu.getBundle(doc));

        return doc4;
    }

    static ProvUtilities pu=new ProvUtilities();
    static ProvFactory pf=ProvFactory.pf();

}
