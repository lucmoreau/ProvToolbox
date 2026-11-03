package org.openprovenance.prov.scala.jsonld11.serialization;

import tools.jackson.core.Version;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.module.SimpleModule;
import org.openprovenance.prov.scala.jsonld11.serialization.serial.CustomDateSerializer;
import org.openprovenance.prov.scala.jsonld11.serialization.serial.CustomKindSerializer;
import org.openprovenance.prov.scala.jsonld11.serialization.serial.CustomNamespaceSerializer;
import org.openprovenance.prov.scala.jsonld11.serialization.serial.CustomQualifiedNameSerializer;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ser.std.SimpleBeanPropertyFilter;
import tools.jackson.databind.ser.std.SimpleFilterProvider;
import org.openprovenance.prov.scala.immutable.QualifiedName;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.exception.UncheckedException;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.OutputStream;

public class ProvSerialiser extends org.openprovenance.prov.core.json.serialization.ProvSerialiser {


    private final boolean embedContext;

    public ObjectMapper getMapper() {
        return mapper;
    }

    /** Jackson 3: an immutable mapper, configured (customize2) when the serialiser is created. */
    final ObjectMapper mapper ;

    public ProvSerialiser () {
        this(true);
    }

    public ProvSerialiser (boolean embedContext) {
        // Jackson 2 defaults keep the serialised output unchanged (docs/JACKSON3_MIGRATION.md, D2)
        this(JsonMapper.builderWithJackson2Defaults(), embedContext);
    }

    public ProvSerialiser (MapperBuilder<?, ?> builder, boolean embedContext) {
        this.embedContext=embedContext;
        this.mapper=customize2(builder).build();
    }

    public ProvMixin provMixin() {
        return new ProvMixin();
    }



    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        // Jackson 3: no mutation of the shared mapper (with Jackson 2, INDENT_OUTPUT stayed enabled after a
        // formatted call, and the module was registered again at each call)
        ObjectMapper mapper = formatted ? this.mapper.rebuild().enable(SerializationFeature.INDENT_OUTPUT).build() : this.mapper;
        try {
            mapper.writeValue(out,document);
        } catch (JacksonException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }

    public <B extends MapperBuilder<?, ?>> B customize2(B mapper) {
        SimpleModule module =
                new SimpleModule("CustomKindSerializer",
                        new Version(1, 0, 0, null, null, null));

        module.addSerializer(StatementOrBundle.Kind.class, new CustomKindSerializer());
        module.addSerializer(QualifiedName.class, new CustomQualifiedNameSerializer());
        module.addSerializer(XMLGregorianCalendar.class, new CustomDateSerializer());
        module.addSerializer(Namespace.class, new CustomNamespaceSerializer(embedContext));
        mapper.addModule(module);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("nsFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("prefixes", "defaultNamespace"));
        mapper.filterProvider(filterProvider);

        provMixin().addProvMixin(mapper);
        return mapper;
    }

}
