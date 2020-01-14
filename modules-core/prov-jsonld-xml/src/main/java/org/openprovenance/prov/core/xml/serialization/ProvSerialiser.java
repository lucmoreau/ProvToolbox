package org.openprovenance.prov.core.xml.serialization;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.vanilla.TypedValue;
import org.openprovenance.prov.core.xml.serialization.serial.CustomDateSerializer;
import org.openprovenance.prov.core.xml.serialization.serial.CustomQualifiedNameSerializer;
import org.openprovenance.prov.core.xml.serialization.serial.CustomTypedValueSerializer;
import org.openprovenance.prov.core.xml.serialization.stax.ElementEraserXMLStreamWriter2;
import org.openprovenance.prov.core.xml.serialization.stax.NamespaceXMLStreamWriter2;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.exception.UncheckedException;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {

    final static private Collection<String> myMedia= Set.of(InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML, InteropMediaType.MEDIA_APPLICATION_XML);

    @Override
    public Collection<String> mediaTypes() {
        return myMedia;
    }


    static final public ProvFactory pf = ProvFactory.getFactory();

    static final public org.openprovenance.prov.model.QualifiedName QUALIFIED_NAME_XSD_STRING = pf.getName().XSD_STRING;



    public ProvSerialiser() {
        this.WRAP_ERASE=false;
    }

    public ProvSerialiser(boolean WRAP_ERASE) {
        this.WRAP_ERASE=WRAP_ERASE;
    }

    private final ProvMixin provMixin = new ProvMixin();

    final public boolean WRAP_ERASE;


    @Override
    public void serialiseDocument(OutputStream out, Document document, String mediaType, boolean formatted) {
        serialiseDocument(out, document, formatted);
    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        serialiseDocument(out,document,formatted,false);
    }

    public void serialiseDocument(OutputStream out, Object document, boolean formatted, boolean ignore) {
        XmlMapper mapper = getMapper(formatted);

        try {
            mapper.writeValue(out,document);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }

    public XmlMapper getMapper(boolean formatted) {
        XmlMapper mapper = getXmlMapper();

        if (formatted) mapper.enable(SerializationFeature.INDENT_OUTPUT);

        SimpleModule module = makeModule();
        mapper.registerModule(module);

        SimpleFilterProvider filterProvider = makeFilter();
        mapper.setFilterProvider(filterProvider);

        provMixin.addProvMixin(mapper);
        return mapper;
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

        // module.addSerializer(StatementOrBundle.Kind.class, new CustomKindSerializer());
        module.addSerializer(QualifiedName.class, new CustomQualifiedNameSerializer());
        module.addSerializer(XMLGregorianCalendar.class, new CustomDateSerializer());
        module.addSerializer(TypedValue.class, new CustomTypedValueSerializer());

        //mapper.setDefaultUseWrapper(false); //NO, use annotation instead

        //module.addSerializer(Attribute.class, new CustomAttributeSerializer());
        return module;
    }

    public XmlMapper getXmlMapper() {
        XMLInputFactory2 inputFactory2 =  new WstxInputFactory();
        XMLOutputFactory2 outputFactory2 = new WstxOutputFactory() {

            @Override
            public XMLStreamWriter createXMLStreamWriter(OutputStream w, String enco) throws XMLStreamException {
                //mConfig.enableAutomaticNamespaces(true);
                //  mConfig.setProperty(WstxInputProperties.P_RETURN_NULL_FOR_DEFAULT_NAMESPACE,  true);
                XMLStreamWriter2 result = (XMLStreamWriter2) super.createXMLStreamWriter(w, enco);

                result.setPrefix("prov", "http://www.w3.org/ns/prov#");
                result.setPrefix("ex", "http://example.org/");
                // result.setPrefix("", "http://www.w3.org/ns/prov#");
                result.setDefaultNamespace("http://www.w3.org/ns/prov#");

                if (WRAP_ERASE) {
                    return new ElementEraserXMLStreamWriter2(result);
                } else {
                    return new NamespaceXMLStreamWriter2(result);
                }
            }
        };

        outputFactory2.setProperty(XMLOutputFactory2.P_AUTOMATIC_NS_PREFIX,Boolean.TRUE);
        outputFactory2.configureForRobustness();
        XmlFactory xmlFactory = new XmlFactory(inputFactory2,outputFactory2);


        return new XmlMapper(xmlFactory);
    }


}
