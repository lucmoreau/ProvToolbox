package org.openprovenance.prov.core.json.serialization;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.openprovenance.prov.core.json.serialization.deserial.CustomAttributeSetDeserializer;
import org.openprovenance.prov.core.json.serialization.deserial.CustomBundleDeserializer;
import org.openprovenance.prov.core.json.serialization.deserial.CustomKindDeserializer;
import org.openprovenance.prov.core.json.serialization.deserial.CustomXMLGregorianCalendarDeserializer;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.vanilla.Bundle;
import org.openprovenance.prov.vanilla.ProvFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;

import static org.openprovenance.prov.core.json.serialization.deserial.CustomThreadConfig.JSON_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.json.serialization.deserial.CustomThreadConfig.getAttributes;

public class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser{
    ProvFactory pf=new ProvFactory();
    private final ProvMixin provMixin = new ProvMixin();
    final ObjectMapper mapper;
    private final DateTimeOption dateTimeOption;
    private final TimeZone optionalTimeZone;


    public ProvDeserialiser() {
        this(new ObjectMapper());
    }

    public ProvDeserialiser(ObjectMapper mapper) {
        this.mapper = mapper;
        this.dateTimeOption = DateTimeOption.PRESERVE;
        this.optionalTimeZone = null;
        customize(mapper);
    }
    public ProvDeserialiser(ObjectMapper mapper, DateTimeOption dateTimeOption) {
        this.mapper = mapper;
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = null;
        customize(mapper);
    }
    public ProvDeserialiser(ObjectMapper mapper, DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        this.mapper = mapper;
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = optionalTimeZone;
        customize(mapper);
    }


    public org.openprovenance.prov.model.Document deserialiseDocument (File serialised) throws IOException {
        return deserialiseDocument(new FileInputStream(serialised));
    }
    public org.openprovenance.prov.model.Document deserialiseDocument (InputStream in)  {
        getAttributes().get().remove(JSON_CONTEXT_KEY_NAMESPACE);
        SortedDocument doc= null;
        try {
            doc = mapper.readValue(in, SortedDocument.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }

        return doc.toDocument(pf);

    }

    public void customize(ObjectMapper mapper) {
        SimpleModule module =
                new SimpleModule("CustomKindSerializer", new Version(1, 0, 0, null, null, null));

        module.addDeserializer(org.openprovenance.prov.model.StatementOrBundle.Kind.class, new CustomKindDeserializer());


        TypeFactory typeFactory = mapper.getTypeFactory();


        //MapType mapType2 = typeFactory.constructMapType(HashMap.class, String.class, String.class);
       // module.addDeserializer(Namespace.class, new CustomNamespaceDeserializer(mapType2));


        module.addDeserializer(Bundle.class, new CustomBundleDeserializer());

        CollectionType setType = typeFactory.constructCollectionType(Set.class, org.openprovenance.prov.model.Attribute.class);
        module.addDeserializer(Set.class,new CustomAttributeSetDeserializer(setType));
        module.addDeserializer(XMLGregorianCalendar.class, new CustomXMLGregorianCalendarDeserializer(dateTimeOption,optionalTimeZone));


        provMixin.addProvMixin(mapper);

        mapper.registerModule(module);
    }

}
