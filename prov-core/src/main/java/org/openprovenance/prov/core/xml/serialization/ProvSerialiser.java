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
import org.codehaus.stax2.*;
import org.openprovenance.prov.core.vanilla.QualifiedName;
import org.openprovenance.prov.core.xml.serialization.stax.ElementEraserXMLStreamWriter2;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.exception.UncheckedException;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;

public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {

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

                XMLStreamWriter xsw = new ElementEraserXMLStreamWriter2(result);
                if (WRAP_ERASE) {
                    return xsw;
                } else {
                    return result;
                }
            }
        };

        outputFactory2.setProperty(XMLOutputFactory2.P_AUTOMATIC_NS_PREFIX,Boolean.TRUE);
        outputFactory2.configureForRobustness();
        XmlFactory xmlFactory = new XmlFactory(inputFactory2,outputFactory2);


        XmlMapper mapper = new XmlMapper(xmlFactory);



        if (formatted) mapper.enable(SerializationFeature.INDENT_OUTPUT);

        SimpleModule module =
                new SimpleModule("CustomKindSerializer",
                        new Version(1, 0, 0, null, null, null));

        module.addSerializer(StatementOrBundle.Kind.class, new CustomKindSerializer());
        module.addSerializer(QualifiedName.class, new CustomQualifiedNameSerializer());
        module.addSerializer(XMLGregorianCalendar.class, new CustomDateSerializer());

        //mapper.setDefaultUseWrapper(false); NO, use annotation instead

        //module.addSerializer(Attribute.class, new CustomAttributeSerializer());
        mapper.registerModule(module);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("nsFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("prefixes", "defaultNamespace"));
        mapper.setFilterProvider(filterProvider);

        provMixin.addProvMixin(mapper);


        try {
            mapper.writeValue(out,document);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }


}
