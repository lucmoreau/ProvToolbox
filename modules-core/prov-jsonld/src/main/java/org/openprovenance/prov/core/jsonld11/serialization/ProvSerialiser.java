package org.openprovenance.prov.core.jsonld11.serialization;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import org.openprovenance.prov.core.jsonld11.serialization.serial.CustomDateSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.serial.CustomNamespaceSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.serial.CustomQualifiedNameSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.serial.CustomOverridingAttributeSerializer;
import org.openprovenance.prov.model.interop.InteropMediaType;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.vanilla.TypedValue;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

public class  ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser  {

    final static private Collection<String> myMedia= Set.of(InteropMediaType.MEDIA_APPLICATION_JSONLD);

    protected final boolean embedContext;
    // create two independent mappers, with formatting or not
    final ObjectMapper mapper;
    final ObjectMapper mapperWithFormat = new ObjectMapper();
    final ObjectWriter writer;

    public ProvSerialiser () {
        this(new ObjectMapper(), false);
    }

    public ProvSerialiser (ObjectMapper mapper, boolean embedContext) {
        this.embedContext=embedContext;
        this.mapper=mapper;
        customize(mapper);
        customize(mapperWithFormat);
        mapperWithFormat.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        writer=mapper.writer().withDefaultPrettyPrinter();

    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public ProvMixin provMixin() {
        return new ProvMixin();
    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        try {
            // a document with openprov attributes cites the openprov context, which extends the PROV-JSONLD one
            boolean usesOpenprov = usesOpenprov(document);
            if (formatted) {
                mapperWithFormat.writer().withAttribute(CustomNamespaceSerializer.USES_OPENPROV, usesOpenprov).writeValue(out, document);
            } else {
                writer.withAttribute(CustomNamespaceSerializer.USES_OPENPROV, usesOpenprov).writeValue(out, document);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }

    /** Whether any statement, in the document or its bundles, carries an attribute of the openprov vocabulary. */
    public static boolean usesOpenprov(Document document) {
        for (StatementOrBundle s : document.getStatementOrBundle()) {
            if (s instanceof Bundle) {
                for (Statement inner : ((Bundle) s).getStatement()) if (hasOpenprovAttribute(inner)) return true;
            } else if (hasOpenprovAttribute(s)) {
                return true;
            }
        }
        return false;
    }

    static boolean hasOpenprovAttribute(StatementOrBundle s) {
        if (!(s instanceof HasOther)) return false;
        for (Other o : ((HasOther) s).getOther()) {
            if (NamespacePrefixMapper.OPENPROV_NS.equals(o.getElementName().getNamespaceURI())) return true;
        }
        return false;
    }

    public ObjectMapper customize(ObjectMapper mapper) {
        mapper.getFactory().setCodec(mapper);

        SimpleModule module =
                new SimpleModule("CustomKindSerializer",
                        new Version(1, 0, 0, null, null, null));

        module.addSerializer(QualifiedName.class, new CustomQualifiedNameSerializer());
        module.addSerializer(XMLGregorianCalendar.class, new CustomDateSerializer());
        module.addSerializer(Namespace.class, new CustomNamespaceSerializer(embedContext,mapper));
        mapper.registerModule(module);

        // See https://www.baeldung.com/jackson-serialize-field-custom-criteria#2-custom-serializer

        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
                    @Override
                    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription desc, JsonSerializer<?> serializer) {
                        if (TypedValue.class.isAssignableFrom(desc.getBeanClass())) {
                            return new CustomOverridingAttributeSerializer((JsonSerializer<TypedValue>) serializer);
                        }
                        return serializer;
                    }
                });
            }
        });

        /*
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("nsFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("prefixes", "defaultNamespace"));
     //    mapper.setFilterProvider(filterProvider);
         */
        provMixin().addProvMixin(mapper);
        return mapper;
    }



}
