package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.openprovenance.prov.core.xml.serialization.deserial.*;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.vanilla.Bundle;
import org.openprovenance.prov.vanilla.Document;
import org.openprovenance.prov.vanilla.ProvFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.util.*;


import static org.openprovenance.prov.core.xml.serialization.deserial.DeserializerUtil.newNamespace;
import static org.openprovenance.prov.core.xml.serialization.deserial.DeserializerUtil.removeNamespace;

public class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {

    public static final ProvFactory pf=new ProvFactory();

    private final ProvMixin provMixin = new ProvMixin();

    private final DateTimeOption dateTimeOption;
    private final TimeZone optionalTimeZone;


    public ProvDeserialiser() {
        this.dateTimeOption = DateTimeOption.PRESERVE;
        this.optionalTimeZone = null;
    }
    public ProvDeserialiser(DateTimeOption dateTimeOption) {
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = null;
    }
    public ProvDeserialiser(DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = optionalTimeZone;
    }

    public org.openprovenance.prov.model.Document deserialiseDocument (InputStream in) throws IOException {
        removeNamespace();
        Namespace docNs = newNamespace();
        docNs.addKnownNamespaces();
        XmlMapper mapper = getMapper();
        Document document = mapper.readValue(in, Document.class);
        document.setNamespace(docNs);
        return document;
    }


    public XmlMapper getMapper() {
        XmlMapper mapper = new XmlMapper();

        SimpleModule module =
                new StatementsHandler("CustomKindDeserializer", new Version(1, 0, 0, null, null, null));

        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType setType = typeFactory.constructCollectionType(Set.class, org.openprovenance.prov.model.Attribute.class);
        module.addDeserializer(Set.class,new CustomAttributeSetDeserializer(setType));
        module.addDeserializer(XMLGregorianCalendar.class, new CustomXMLGregorianCalendarDeserializer(dateTimeOption,optionalTimeZone));
        module.addDeserializer(Bundle.class, new CustomBundleDeserializer());
        module.addKeyDeserializer(QualifiedName.class, new CustomKeyDeserializer());

        mapper.registerModule(module);
        provMixin.addProvMixin(mapper);
        return mapper;
    }

}
