package org.openprovenance.prov.core.jsonld11.serialization;

import tools.jackson.core.Version;
import tools.jackson.databind.*;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.ser.ValueSerializerModifier;

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
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ValueSerializer;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

public class  ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser  {

    final static private Collection<String> myMedia= Set.of(InteropMediaType.MEDIA_APPLICATION_JSONLD);

    protected final boolean embedContext;
    // create two independent mappers, with formatting or not
    final ObjectMapper mapper;
    final ObjectMapper mapperWithFormat;
    final ObjectWriter writer;

    public ProvSerialiser () {
        // Jackson 2 defaults keep the serialised PROV byte-identical (JACKSON3_MIGRATION.md, D2)
        this(JsonMapper.builderWithJackson2Defaults(), false);
    }

    /** Jackson 3: mappers are immutable, so the serialiser configures the given builder
     * (instead of mutating a given mapper, as with Jackson 2) and builds its two mappers from it. */
    public ProvSerialiser (MapperBuilder<?, ?> builder, boolean embedContext) {
        this.embedContext=embedContext;
        customize(builder);
        this.mapper=builder.disable(SerializationFeature.INDENT_OUTPUT).build();
        this.mapperWithFormat=mapper.rebuild().enable(SerializationFeature.INDENT_OUTPUT).build();
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
        } catch (JacksonException e) {
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

    public MapperBuilder<?, ?> customize(MapperBuilder<?, ?> mapper) {

        SimpleModule module =
                new SimpleModule("CustomKindSerializer",
                        new Version(1, 0, 0, null, null, null));

        module.addSerializer(QualifiedName.class, new CustomQualifiedNameSerializer());
        module.addSerializer(XMLGregorianCalendar.class, new CustomDateSerializer());
        module.addSerializer(Namespace.class, new CustomNamespaceSerializer(embedContext));
        mapper.addModule(module);

        // See https://www.baeldung.com/jackson-serialize-field-custom-criteria#2-custom-serializer

        mapper.addModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addSerializerModifier(new ValueSerializerModifier() {
                    @Override
                    public ValueSerializer<?> modifySerializer(SerializationConfig config, BeanDescription.Supplier desc, ValueSerializer<?> serializer) {
                        if (TypedValue.class.isAssignableFrom(desc.getBeanClass())) {
                            return new CustomOverridingAttributeSerializer((ValueSerializer<TypedValue>) serializer);
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
