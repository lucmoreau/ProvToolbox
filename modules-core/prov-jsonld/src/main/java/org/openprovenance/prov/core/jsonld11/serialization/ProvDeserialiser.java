package org.openprovenance.prov.core.jsonld11.serialization;


import tools.jackson.core.Version;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.type.ArrayType;
import tools.jackson.databind.type.MapType;
import tools.jackson.databind.type.TypeFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.*;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.vanilla.Bundle;
import org.openprovenance.prov.vanilla.Document;
import tools.jackson.core.JacksonException;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.TimeZone;

import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.JSONLD_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.getAttributes;


public class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {
    private static final Logger logger = LogManager.getLogger(ProvDeserialiser.class);
    final ObjectMapper mapper ;
    private final DateTimeOption dateTimeOption;
    private final TimeZone optionalTimeZone;

    public ProvDeserialiser() {
        // Jackson 2 defaults keep the behaviour of the Jackson 2 version (docs/JACKSON3_MIGRATION.md, D2)
        this(JsonMapper.builderWithJackson2Defaults());
    }

    /* Jackson 3: mappers are immutable, so the deserialiser configures the given builder
     * (instead of mutating a given mapper, as with Jackson 2) and builds its mapper from it. */
    public ProvDeserialiser(MapperBuilder<?, ?> builder) {
        this(builder, DateTimeOption.PRESERVE, null);
    }
    public ProvDeserialiser(MapperBuilder<?, ?> builder, DateTimeOption dateTimeOption) {
        this(builder, dateTimeOption, null);
    }
    public ProvDeserialiser(MapperBuilder<?, ?> builder, DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = optionalTimeZone;
        customize(builder);
        this.mapper = builder.build();
    }

    public ProvMixin provMixin() {
        return new ProvMixin();
    }


    public ObjectMapper getMapper() {
        return mapper;
    }

    public org.openprovenance.prov.model.Document deserialiseDocument (InputStream in)  {
        getAttributes().get().remove(JSONLD_CONTEXT_KEY_NAMESPACE);
        try {
            return mapper.readValue(in, Document.class);
        } catch (JacksonException e) {
            logger.throwing(e);
            throw new UncheckedException(e);
        }
    }

    public org.openprovenance.prov.model.Document deserialiseDocument (BufferedReader in)  {
        getAttributes().get().remove(JSONLD_CONTEXT_KEY_NAMESPACE);
        try {
            return mapper.readValue(in, Document.class);
        } catch (JacksonException e) {
            logger.throwing(e);
            throw new UncheckedException(e);
        }
    }


    public void customize(MapperBuilder<?, ?> mapper) {

        SimpleModule module =
                new SimpleModule("CustomKindDeserializer", new Version(1, 0, 0, null, null, null));
        TypeFactory typeFactory = mapper.typeFactory();

        // DESERIALISER
        MapType mapType2 = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        ArrayType arrayType=typeFactory.constructArrayType(mapType2);

        module.addKeyDeserializer(QualifiedName.class, new CustomKeyDeserializer());
        module.addDeserializer(Namespace.class, newCustomNamespaceDeserializer(arrayType));
        module.addDeserializer(Bundle.class, new CustomBundleDeserializer());
        module.addDeserializer(Attribute.class, new CustomAttributeDeserializer());
        module.addDeserializer(XMLGregorianCalendar.class, new CustomXMLGregorianCalendarDeserializer(dateTimeOption,optionalTimeZone));

        provMixin().addProvMixin(mapper);

        mapper.addModule(module);
        // Jackson 3: let PROV attribute keys reach the any-setter again (see AnySetterAttributesModule)
        mapper.addModule(new AnySetterAttributesModule());
    }

    public CustomNamespaceDeserializer newCustomNamespaceDeserializer(ArrayType arrayType) {
        return new CustomNamespaceDeserializer(arrayType);
    }

}
