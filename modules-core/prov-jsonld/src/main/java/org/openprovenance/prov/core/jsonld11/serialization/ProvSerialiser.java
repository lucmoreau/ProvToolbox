package org.openprovenance.prov.core.jsonld11.serialization;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import org.openprovenance.prov.core.jsonld11.serialization.serial.CustomDateSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.serial.CustomNamespaceSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.serial.CustomQualifiedNameSerializer;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.exception.UncheckedException;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

public class ProvSerialiser extends org.openprovenance.prov.core.json.serialization.ProvSerialiser {

    final static private Collection<String> myMedia= Set.of(InteropMediaType.MEDIA_APPLICATION_JSONLD);

    @Override
    public Collection<String> mediaTypes() {
        return myMedia;
    }

    protected final boolean embedContext;
    // create two independent mappers, with formatting or not
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectMapper mapperWithFormat = new ObjectMapper();

    public ProvSerialiser () {
        this(true);
    }

    public ProvSerialiser (boolean embedContext) {
        this.embedContext=embedContext;
        customize(mapper);
        customize(mapperWithFormat);
        mapperWithFormat.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
    }

    public ProvMixin provMixin() {
        return new ProvMixin();
    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        try {
            if (formatted) {
                mapperWithFormat.writeValue(out,document);
            } else {
                mapper.writeValue(out,document);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }

    public ObjectMapper customize(ObjectMapper mapper) {

        SimpleModule module =
                new SimpleModule("CustomKindSerializer",
                        new Version(1, 0, 0, null, null, null));

        module.addSerializer(QualifiedName.class, new CustomQualifiedNameSerializer());
        module.addSerializer(XMLGregorianCalendar.class, new CustomDateSerializer());
        module.addSerializer(Namespace.class, new CustomNamespaceSerializer(embedContext));
        mapper.registerModule(module);

        /*
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("nsFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("prefixes", "defaultNamespace"));
     //    mapper.setFilterProvider(filterProvider);
         */
        provMixin().addProvMixin(mapper);
        return mapper;
    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, String mediaType, boolean formatted) {
        serialiseDocument(out, document, formatted);
    }



}
