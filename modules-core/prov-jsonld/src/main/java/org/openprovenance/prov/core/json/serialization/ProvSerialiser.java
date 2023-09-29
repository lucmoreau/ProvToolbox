package org.openprovenance.prov.core.json.serialization;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.openprovenance.prov.core.json.serialization.serial.CustomBundleSerializer;
import org.openprovenance.prov.core.json.serialization.serial.CustomDateSerializer;
import org.openprovenance.prov.core.json.serialization.serial.CustomKindSerializer;
import org.openprovenance.prov.core.json.serialization.serial.CustomQualifiedNameSerializer;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.vanilla.Bundle;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.exception.UncheckedException;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {

    final static private Collection<String> myMedia= Set.of(InteropMediaType.MEDIA_APPLICATION_JSON);

    final ObjectMapper mapper = new ObjectMapper();
    final ObjectMapper mapperWithFormat = new ObjectMapper();

    public ProvSerialiser() {
        mapperWithFormat.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        customize(mapper);
        customize(mapperWithFormat);
    }

    public void customize(ObjectMapper mapper) {
        registerModule(mapper);
        registerFilter(mapper);
        provMixin.addProvMixin(mapper);
    }

    private final ProvMixin provMixin = new ProvMixin();

    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        try {
            if (formatted) {
                mapperWithFormat.writeValue(out, new SortedDocument(document));
            } else {
                mapper.writeValue(out, new SortedDocument(document));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }

    public void serialiseObject(OutputStream out, Object document, boolean formatted) {
        try {
            if (formatted) {
                mapperWithFormat.writeValue(out, document);
            } else {
                mapper.writeValue(out, document);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }

    public void registerFilter(ObjectMapper mapper) {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("nsFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("prefixes"));
        mapper.setFilterProvider(filterProvider);
    }

    public void registerModule(ObjectMapper mapper) {
        SimpleModule module =
                new SimpleModule("CustomKindSerializer",
                        new Version(1, 0, 0, null, null, null));

        module.addSerializer(StatementOrBundle.Kind.class, new CustomKindSerializer());
        module.addSerializer(QualifiedName.class, new CustomQualifiedNameSerializer());
        module.addSerializer(XMLGregorianCalendar.class, new CustomDateSerializer());

        module.addSerializer(Bundle.class, new CustomBundleSerializer());
        mapper.registerModule(module);
    }


}
