package org.openprovenance.prov.core.xml.serialization;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import tools.jackson.core.Version;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.dataformat.xml.XmlFactory;
import tools.jackson.dataformat.xml.XmlMapper;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.openprovenance.prov.core.xml.serialization.serial.*;
import org.openprovenance.prov.model.interop.InteropMediaType;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.vanilla.TypedValue;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ser.std.SimpleBeanPropertyFilter;
import tools.jackson.databind.ser.std.SimpleFilterProvider;
import org.openprovenance.prov.core.xml.serialization.stax.ElementEraserXMLStreamWriter2;
import org.openprovenance.prov.core.xml.serialization.stax.NamespaceXMLStreamWriter2;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.exception.UncheckedException;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

import static org.openprovenance.prov.core.xml.serialization.deserial.DeserializerUtil.setNamespace;

public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {

    final static private Collection<String> myMedia= Set.of(InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML, InteropMediaType.MEDIA_APPLICATION_XML);

    static final public ProvFactory pf = ProvFactory.getFactory();



    public ProvSerialiser() {
        this.WRAP_ERASE=false;
    }

    public ProvSerialiser(boolean WRAP_ERASE) {
        this.WRAP_ERASE=WRAP_ERASE;
    }

    private final ProvMixin provMixin = new ProvMixin();

    final public boolean WRAP_ERASE;



    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        serialiseDocument(out,document,formatted,false);
    }

    public void serialiseDocument(OutputStream out, Document document, boolean formatted, boolean ignore) {
        XmlMapper mapper = getMapper(formatted);

        setNamespace(document.getNamespace());
        try {
            mapper.writeValue(out,document);
        } catch (JacksonException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }
    /* this method allows the serialization configuration to be used to save other objects (potentially containing prov objects) */
    public void serialiseOtherObject(OutputStream out, Object document, boolean formatted, boolean ignore) {
        XmlMapper mapper = getMapper(formatted);
        setNamespace(new Namespace());
        try {
            mapper.writeValue(out,document);
        } catch (JacksonException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }

    public XmlMapper getMapper(boolean formatted) {
        // Jackson 3: mappers are immutable, so everything is configured on the builder, then built
        XmlMapper.Builder builder = getXmlMapperBuilder();
        if (formatted) builder.enable(SerializationFeature.INDENT_OUTPUT);
        builder.addModule(makeModule());
        builder.filterProvider(makeFilter());
        provMixin.addProvMixin(builder);
        return builder.build();
    }

    public SimpleFilterProvider makeFilter() {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("nsFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("prefixes", "defaultNamespace"));
        return filterProvider;
    }

    public SimpleModule makeModule() {
        SimpleModule module =
                new SimpleModule("CustomKindSerializer",
                        new Version(1, 0, 0, null, null, null));

        module.addSerializer(QualifiedName.class, new CustomQualifiedNameSerializer());
        module.addSerializer(XMLGregorianCalendar.class, new CustomDateSerializer());
        module.addSerializer(TypedValue.class, new CustomTypedValueSerializer());
        module.addSerializer(org.openprovenance.prov.vanilla.Document.class, new CustomDocumentSerializer());
        //module.addSerializer(org.openprovenance.prov.vanilla.Bundle.class, new CustomBundleSerializer());

        return module;
    }

    public XmlMapper.Builder getXmlMapperBuilder() {
        XMLInputFactory2 inputFactory2 =  new WstxInputFactory();
        XMLOutputFactory2 outputFactory2 = new WstxOutputFactory() {
            @Override
            public XMLStreamWriter createXMLStreamWriter(OutputStream w, String enco) throws XMLStreamException {
                XMLStreamWriter2 result = (XMLStreamWriter2) super.createXMLStreamWriter(w, enco);
                result.setPrefix("prov", "http://www.w3.org/ns/prov#");
                //result.setDefaultNamespace("http://www.w3.org/ns/prov#");
                if (WRAP_ERASE) {
                    return new ElementEraserXMLStreamWriter2(result);
                } else {
                    return new NamespaceXMLStreamWriter2(result);
                }
            }
        };

        outputFactory2.setProperty(XMLOutputFactory2.P_AUTOMATIC_NS_PREFIX,Boolean.TRUE);
        outputFactory2.configureForRobustness();
        // Jackson 2 defaults for the factory and the mapper keep the output unchanged (docs/JACKSON3_MIGRATION.md, D2)
        XmlFactory xmlFactory = XmlFactory.builderWithJackson2Defaults()
                .xmlInputFactory(inputFactory2)
                .xmlOutputFactory(outputFactory2)
                .build();
        return XmlMapper.builder(xmlFactory).configureForJackson2();
    }


}
