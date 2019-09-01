package org.openprovenance.prov.core.xml.serialization;

import com.ctc.wstx.api.WstxInputProperties;
import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.codehaus.stax2.*;
import org.codehaus.stax2.typed.Base64Variant;
import org.codehaus.stax2.validation.ValidationProblemHandler;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidator;
import org.openprovenance.prov.core.vanilla.QualifiedName;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.exception.UncheckedException;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

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

        System.out.println("+++++++ here");

        XMLInputFactory2 inputFactory2 =  new WstxInputFactory();
        XMLOutputFactory2 outputFactory2 = new WstxOutputFactory() {

            @Override
            public XMLStreamWriter createXMLStreamWriter(OutputStream w, String enco) throws XMLStreamException {
                //mConfig.enableAutomaticNamespaces(true);
                //  mConfig.setProperty(WstxInputProperties.P_RETURN_NULL_FOR_DEFAULT_NAMESPACE,  true);
                XMLStreamWriter2 result = (XMLStreamWriter2) super.createXMLStreamWriter(w, enco);
                System.out.println("Serialzietr" + result.getNamespaceContext());
                result.setPrefix("prov", "http://www.w3.org/ns/prov#");
                result.setPrefix("ex", "http://example.org/");
                // result.setPrefix("", "http://www.w3.org/ns/prov#");
                result.setDefaultNamespace("http://www.w3.org/ns/prov#");

                XMLStreamWriter xsw=new XMLStreamWriter2() {
                    @Override
                    public XMLValidator validateAgainst(XMLValidationSchema xmlValidationSchema) throws XMLStreamException {
                        return result.validateAgainst(xmlValidationSchema);
                    }

                    @Override
                    public XMLValidator stopValidatingAgainst(XMLValidationSchema xmlValidationSchema) throws XMLStreamException {
                        return result.stopValidatingAgainst(xmlValidationSchema);
                    }

                    @Override
                    public XMLValidator stopValidatingAgainst(XMLValidator xmlValidator) throws XMLStreamException {
                        return result.stopValidatingAgainst(xmlValidator);
                    }

                    @Override
                    public ValidationProblemHandler setValidationProblemHandler(ValidationProblemHandler validationProblemHandler) {
                        return result.setValidationProblemHandler(validationProblemHandler);
                    }

                    @Override
                    public void writeBoolean(boolean b) throws XMLStreamException {
                        result.writeBoolean(b);
                    }

                    @Override
                    public void writeInt(int i) throws XMLStreamException {
                        result.writeInt(i);
                    }

                    @Override
                    public void writeLong(long l) throws XMLStreamException {
                        result.writeLong(l);
                    }

                    @Override
                    public void writeFloat(float v) throws XMLStreamException {
                        result.writeFloat(v);
                    }

                    @Override
                    public void writeDouble(double v) throws XMLStreamException {
                        result.writeDouble(v);
                    }

                    @Override
                    public void writeInteger(BigInteger bigInteger) throws XMLStreamException {
                        result.writeInteger(bigInteger);
                    }

                    @Override
                    public void writeDecimal(BigDecimal bigDecimal) throws XMLStreamException {
                        result.writeDecimal(bigDecimal);
                    }

                    @Override
                    public void writeQName(QName qName) throws XMLStreamException {
                        result.writeQName(qName);
                    }

                    @Override
                    public void writeBinary(byte[] bytes, int i, int i1) throws XMLStreamException {
                        result.writeBinary(bytes, i, i1);
                    }

                    @Override
                    public void writeBinary(Base64Variant base64Variant, byte[] bytes, int i, int i1) throws XMLStreamException {
                        result.writeBinary(base64Variant, bytes, i, i1);
                    }

                    @Override
                    public void writeIntArray(int[] ints, int i, int i1) throws XMLStreamException {
                        result.writeIntArray(ints, i, i1);
                    }

                    @Override
                    public void writeLongArray(long[] longs, int i, int i1) throws XMLStreamException {
                        result.writeLongArray(longs, i, i1);
                    }

                    @Override
                    public void writeFloatArray(float[] floats, int i, int i1) throws XMLStreamException {
                        result.writeFloatArray(floats, i, i1);
                    }

                    @Override
                    public void writeDoubleArray(double[] doubles, int i, int i1) throws XMLStreamException {
                        result.writeDoubleArray(doubles, i, i1);
                    }

                    @Override
                    public void writeBooleanAttribute(String s, String s1, String s2, boolean b) throws XMLStreamException {
                        result.writeBooleanAttribute(s, s1, s2, b);
                    }

                    @Override
                    public void writeIntAttribute(String s, String s1, String s2, int i) throws XMLStreamException {
                        result.writeIntAttribute(s, s1, s2, i);
                    }

                    @Override
                    public void writeLongAttribute(String s, String s1, String s2, long l) throws XMLStreamException {
                        result.writeLongAttribute(s, s1, s2, l);
                    }

                    @Override
                    public void writeFloatAttribute(String s, String s1, String s2, float v) throws XMLStreamException {
                        result.writeFloatAttribute(s, s1, s2, v);
                    }

                    @Override
                    public void writeDoubleAttribute(String s, String s1, String s2, double v) throws XMLStreamException {
                        result.writeDoubleAttribute(s, s1, s2, v);
                    }

                    @Override
                    public void writeIntegerAttribute(String s, String s1, String s2, BigInteger bigInteger) throws XMLStreamException {
                        result.writeIntegerAttribute(s, s1, s2, bigInteger);
                    }

                    @Override
                    public void writeDecimalAttribute(String s, String s1, String s2, BigDecimal bigDecimal) throws XMLStreamException {
                        result.writeDecimalAttribute(s, s1, s2, bigDecimal);
                    }

                    @Override
                    public void writeQNameAttribute(String s, String s1, String s2, QName qName) throws XMLStreamException {
                        result.writeQNameAttribute(s, s1, s2, qName);
                    }

                    @Override
                    public void writeBinaryAttribute(String s, String s1, String s2, byte[] bytes) throws XMLStreamException {
                        result.writeBinaryAttribute(s, s1, s2, bytes);
                    }

                    @Override
                    public void writeBinaryAttribute(Base64Variant base64Variant, String s, String s1, String s2, byte[] bytes) throws XMLStreamException {
                        result.writeBinaryAttribute(base64Variant, s, s1, s2, bytes);
                    }

                    @Override
                    public void writeIntArrayAttribute(String s, String s1, String s2, int[] ints) throws XMLStreamException {
                        result.writeIntArrayAttribute(s, s1, s2, ints);
                    }

                    @Override
                    public void writeLongArrayAttribute(String s, String s1, String s2, long[] longs) throws XMLStreamException {
                        result.writeLongArrayAttribute(s, s1, s2, longs);
                    }

                    @Override
                    public void writeFloatArrayAttribute(String s, String s1, String s2, float[] floats) throws XMLStreamException {
                        result.writeFloatArrayAttribute(s, s1, s2, floats);
                    }

                    @Override
                    public void writeDoubleArrayAttribute(String s, String s1, String s2, double[] doubles) throws XMLStreamException {
                        result.writeDoubleArrayAttribute(s, s1, s2, doubles);
                    }

                    @Override
                    public boolean isPropertySupported(String s) {
                        return result.isPropertySupported(s);
                    }

                    @Override
                    public boolean setProperty(String s, Object o) {
                        return result.setProperty(s, o);
                    }

                    @Override
                    public XMLStreamLocation2 getLocation() {
                        return result.getLocation();
                    }

                    @Override
                    public String getEncoding() {
                        return result.getEncoding();
                    }

                    @Override
                    public void writeCData(char[] chars, int i, int i1) throws XMLStreamException {
                        result.writeCData(chars, i, i1);
                    }

                    @Override
                    public void writeDTD(String s, String s1, String s2, String s3) throws XMLStreamException {
                        result.writeDTD(s, s1, s2, s3);
                    }

                    @Override
                    public void writeFullEndElement() throws XMLStreamException {
                        result.writeFullEndElement();
                    }

                    @Override
                    public void writeStartDocument(String s, String s1, boolean b) throws XMLStreamException {
                        result.writeStartDocument(s, s1, b);
                    }

                    @Override
                    public void writeSpace(String s) throws XMLStreamException {
                        result.writeSpace(s);
                    }

                    @Override
                    public void writeSpace(char[] chars, int i, int i1) throws XMLStreamException {
                        result.writeSpace(chars, i, i1);
                    }

                    @Override
                    public void writeRaw(String s) throws XMLStreamException {
                        result.writeRaw(s);
                    }

                    @Override
                    public void writeRaw(String s, int i, int i1) throws XMLStreamException {
                        result.writeRaw(s, i, i1);
                    }

                    @Override
                    public void writeRaw(char[] chars, int i, int i1) throws XMLStreamException {
                        result.writeRaw(chars, i, i1);
                    }

                    @Override
                    public void copyEventFromReader(XMLStreamReader2 xmlStreamReader2, boolean b) throws XMLStreamException {
                        result.copyEventFromReader(xmlStreamReader2, b);
                    }

                    @Override
                    public void closeCompletely() throws XMLStreamException {
                        result.closeCompletely();
                    }

                    int count=0;
                    int ignore=-1;

                    @Override
                    public void writeStartElement(String localName) throws XMLStreamException {
                        count++;
                        if (localName.equals("statements")) {
                            System.out.println(" * writeStartElement " + localName + " " + count);
                            ignore=count;
                        } else {

                            result.writeStartElement(localName);
                        }
                    }

                    @Override
                    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
                        count++;
                        if (localName.equals("statements")) {
                            System.out.println(" * writeStartElement " + localName + " " + namespaceURI + " " + count);
                            ignore=count;
                        } else {
                            result.writeStartElement(namespaceURI, localName);
                        }
                    }

                    @Override
                    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
                        count++;
                        if (localName.equals("statements")) {
                            System.out.println(" * writeStartElement " + localName + " " + namespaceURI + " (pre " + prefix + " " + count);
                            ignore = count;
                        } else {
                            result.writeStartElement(prefix, localName, namespaceURI);
                        }
                    }

                    @Override
                    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
                        result.writeEmptyElement(namespaceURI,localName);
                    }

                    @Override
                    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
                        result.writeEmptyElement(prefix,localName,namespaceURI);

                    }

                    @Override
                    public void writeEmptyElement(String localName) throws XMLStreamException {
                        result.writeEmptyElement(localName);
                    }

                    @Override
                    public void writeEndElement() throws XMLStreamException {
                        count--;
                        System.out.println(" count " + count + " ignore " + ignore);
                        if (count!=ignore)
                        result.writeEndElement();
                    }

                    @Override
                    public void writeEndDocument() throws XMLStreamException {
                        result.writeEndDocument();
                    }

                    @Override
                    public void close() throws XMLStreamException {
                        result.close();
                    }

                    @Override
                    public void flush() throws XMLStreamException {
                        result.flush();
                    }

                    @Override
                    public void writeAttribute(String localName, String value) throws XMLStreamException {
                        result.writeAttribute(localName,value);
                    }

                    @Override
                    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
                        result.writeAttribute(prefix, namespaceURI, localName, value);
                    }

                    @Override
                    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
                        result.writeAttribute(namespaceURI, localName, value);
                    }

                    @Override
                    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
                        result.writeNamespace(prefix, namespaceURI);
                    }

                    @Override
                    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
                        result.writeDefaultNamespace(namespaceURI);
                    }

                    @Override
                    public void writeComment(String data) throws XMLStreamException {
                        result.writeComment(data);
                    }

                    @Override
                    public void writeProcessingInstruction(String target) throws XMLStreamException {
                        result.writeProcessingInstruction(target);
                    }

                    @Override
                    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
                        result.writeProcessingInstruction(target, data);
                    }

                    @Override
                    public void writeCData(String data) throws XMLStreamException {
                        result.writeCData(data);
                    }

                    @Override
                    public void writeDTD(String dtd) throws XMLStreamException {
                        result.writeDTD(dtd);
                    }

                    @Override
                    public void writeEntityRef(String name) throws XMLStreamException {
                        result.writeEntityRef(name);
                    }

                    @Override
                    public void writeStartDocument() throws XMLStreamException {
                        result.writeStartDocument();
                        result.writeNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
                        result.setPrefix("xsd", "http://www.w3.org/2001/XMLSchema");
                    }

                    @Override
                    public void writeStartDocument(String version) throws XMLStreamException {
                        result.writeStartDocument(version);
                        result.writeNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
                        result.setPrefix("xsd", "http://www.w3.org/2001/XMLSchema");
                    }

                    @Override
                    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
                        result.writeStartDocument(encoding, version);
                        result.writeNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
                        result.setPrefix("xsd", "http://www.w3.org/2001/XMLSchema");
                    }

                    @Override
                    public void writeCharacters(String text) throws XMLStreamException {
                        result.writeCharacters(text);
                    }

                    @Override
                    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
                        result.writeCharacters(text, start, len);
                    }

                    @Override
                    public String getPrefix(String uri) throws XMLStreamException {
                        return result.getPrefix(uri);
                    }

                    @Override
                    public void setPrefix(String prefix, String uri) throws XMLStreamException {
                        result.setPrefix(prefix, uri);
                    }

                    @Override
                    public void setDefaultNamespace(String uri) throws XMLStreamException {
                        result.setDefaultNamespace(uri);
                    }

                    @Override
                    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
                        result.setNamespaceContext(context);
                    }

                    @Override
                    public NamespaceContext getNamespaceContext() {
                        return result.getNamespaceContext();
                    }

                    @Override
                    public Object getProperty(String name) throws IllegalArgumentException {
                        return result.getProperty(name);
                    }
                };

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
