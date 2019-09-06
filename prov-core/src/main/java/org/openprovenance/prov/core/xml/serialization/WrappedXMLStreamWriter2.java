package org.openprovenance.prov.core.xml.serialization;

import org.codehaus.stax2.XMLStreamLocation2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.codehaus.stax2.typed.Base64Variant;
import org.codehaus.stax2.validation.ValidationProblemHandler;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class WrappedXMLStreamWriter2 implements XMLStreamWriter2 {
    protected final XMLStreamWriter2 delegate;

    public WrappedXMLStreamWriter2(XMLStreamWriter2 delegate) {
        this.delegate = delegate;
    }


    @Override
    public XMLValidator validateAgainst(XMLValidationSchema xmlValidationSchema) throws
            XMLStreamException {
        return delegate.validateAgainst(xmlValidationSchema);
    }

    @Override
    public XMLValidator stopValidatingAgainst(XMLValidationSchema xmlValidationSchema) throws XMLStreamException {
        return delegate.stopValidatingAgainst(xmlValidationSchema);
    }

    @Override
    public XMLValidator stopValidatingAgainst(XMLValidator xmlValidator) throws XMLStreamException {
        return delegate.stopValidatingAgainst(xmlValidator);
    }

    @Override
    public ValidationProblemHandler setValidationProblemHandler(ValidationProblemHandler validationProblemHandler) {
        return delegate.setValidationProblemHandler(validationProblemHandler);
    }

    @Override
    public void writeBoolean(boolean b) throws XMLStreamException {
        delegate.writeBoolean(b);
    }

    @Override
    public void writeInt(int i) throws XMLStreamException {
        delegate.writeInt(i);
    }

    @Override
    public void writeLong(long l) throws XMLStreamException {
        delegate.writeLong(l);
    }

    @Override
    public void writeFloat(float v) throws XMLStreamException {
        delegate.writeFloat(v);
    }

    @Override
    public void writeDouble(double v) throws XMLStreamException {
        delegate.writeDouble(v);
    }

    @Override
    public void writeInteger(BigInteger bigInteger) throws XMLStreamException {
        delegate.writeInteger(bigInteger);
    }

    @Override
    public void writeDecimal(BigDecimal bigDecimal) throws XMLStreamException {
        delegate.writeDecimal(bigDecimal);
    }

    @Override
    public void writeQName(QName qName) throws XMLStreamException {
        delegate.writeQName(qName);
    }

    @Override
    public void writeBinary(byte[] bytes, int i, int i1) throws XMLStreamException {
        delegate.writeBinary(bytes, i, i1);
    }

    @Override
    public void writeBinary(Base64Variant base64Variant, byte[] bytes, int i, int i1) throws XMLStreamException {
        delegate.writeBinary(base64Variant, bytes, i, i1);
    }

    @Override
    public void writeIntArray(int[] ints, int i, int i1) throws XMLStreamException {
        delegate.writeIntArray(ints, i, i1);
    }

    @Override
    public void writeLongArray(long[] longs, int i, int i1) throws XMLStreamException {
        delegate.writeLongArray(longs, i, i1);
    }

    @Override
    public void writeFloatArray(float[] floats, int i, int i1) throws XMLStreamException {
        delegate.writeFloatArray(floats, i, i1);
    }

    @Override
    public void writeDoubleArray(double[] doubles, int i, int i1) throws XMLStreamException {
        delegate.writeDoubleArray(doubles, i, i1);
    }

    @Override
    public void writeBooleanAttribute(String s, String s1, String s2, boolean b) throws XMLStreamException {
        delegate.writeBooleanAttribute(s, s1, s2, b);
    }

    @Override
    public void writeIntAttribute(String s, String s1, String s2, int i) throws XMLStreamException {
        delegate.writeIntAttribute(s, s1, s2, i);
    }

    @Override
    public void writeLongAttribute(String s, String s1, String s2, long l) throws XMLStreamException {
        delegate.writeLongAttribute(s, s1, s2, l);
    }

    @Override
    public void writeFloatAttribute(String s, String s1, String s2, float v) throws XMLStreamException {
        delegate.writeFloatAttribute(s, s1, s2, v);
    }

    @Override
    public void writeDoubleAttribute(String s, String s1, String s2, double v) throws XMLStreamException {
        delegate.writeDoubleAttribute(s, s1, s2, v);
    }

    @Override
    public void writeIntegerAttribute(String s, String s1, String s2, BigInteger bigInteger) throws XMLStreamException {
        delegate.writeIntegerAttribute(s, s1, s2, bigInteger);
    }

    @Override
    public void writeDecimalAttribute(String s, String s1, String s2, BigDecimal bigDecimal) throws XMLStreamException {
        delegate.writeDecimalAttribute(s, s1, s2, bigDecimal);
    }

    @Override
    public void writeQNameAttribute(String s, String s1, String s2, QName qName) throws XMLStreamException {
        delegate.writeQNameAttribute(s, s1, s2, qName);
    }

    @Override
    public void writeBinaryAttribute(String s, String s1, String s2, byte[] bytes) throws XMLStreamException {
        delegate.writeBinaryAttribute(s, s1, s2, bytes);
    }

    @Override
    public void writeBinaryAttribute(Base64Variant base64Variant, String s, String s1, String s2, byte[] bytes) throws XMLStreamException {
        delegate.writeBinaryAttribute(base64Variant, s, s1, s2, bytes);
    }

    @Override
    public void writeIntArrayAttribute(String s, String s1, String s2, int[] ints) throws XMLStreamException {
        delegate.writeIntArrayAttribute(s, s1, s2, ints);
    }

    @Override
    public void writeLongArrayAttribute(String s, String s1, String s2, long[] longs) throws XMLStreamException {
        delegate.writeLongArrayAttribute(s, s1, s2, longs);
    }

    @Override
    public void writeFloatArrayAttribute(String s, String s1, String s2, float[] floats) throws XMLStreamException {
        delegate.writeFloatArrayAttribute(s, s1, s2, floats);
    }

    @Override
    public void writeDoubleArrayAttribute(String s, String s1, String s2, double[] doubles) throws XMLStreamException {
        delegate.writeDoubleArrayAttribute(s, s1, s2, doubles);
    }

    @Override
    public boolean isPropertySupported(String s) {
        return delegate.isPropertySupported(s);
    }

    @Override
    public boolean setProperty(String s, Object o) {
        return delegate.setProperty(s, o);
    }

    @Override
    public XMLStreamLocation2 getLocation() {
        return delegate.getLocation();
    }

    @Override
    public String getEncoding() {
        return delegate.getEncoding();
    }

    @Override
    public void writeCData(char[] chars, int i, int i1) throws XMLStreamException {
        delegate.writeCData(chars, i, i1);
    }

    @Override
    public void writeDTD(String s, String s1, String s2, String s3) throws XMLStreamException {
        delegate.writeDTD(s, s1, s2, s3);
    }

    @Override
    public void writeFullEndElement() throws XMLStreamException {
        delegate.writeFullEndElement();
    }

    @Override
    public void writeStartDocument(String s, String s1, boolean b) throws XMLStreamException {
        delegate.writeStartDocument(s, s1, b);
    }

    @Override
    public void writeSpace(String s) throws XMLStreamException {
        delegate.writeSpace(s);
    }

    @Override
    public void writeSpace(char[] chars, int i, int i1) throws XMLStreamException {
        delegate.writeSpace(chars, i, i1);
    }

    @Override
    public void writeRaw(String s) throws XMLStreamException {
        delegate.writeRaw(s);
    }

    @Override
    public void writeRaw(String s, int i, int i1) throws XMLStreamException {
        delegate.writeRaw(s, i, i1);
    }

    @Override
    public void writeRaw(char[] chars, int i, int i1) throws XMLStreamException {
        delegate.writeRaw(chars, i, i1);
    }

    @Override
    public void copyEventFromReader(XMLStreamReader2 xmlStreamReader2, boolean b) throws XMLStreamException {
        delegate.copyEventFromReader(xmlStreamReader2, b);
    }

    @Override
    public void closeCompletely() throws XMLStreamException {
        delegate.closeCompletely();
    }

    @Override
    public void writeStartElement(String localName) throws XMLStreamException {
        delegate.writeStartElement(localName);
    }

    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        delegate.writeStartElement(namespaceURI, localName);
    }

    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        delegate.writeStartElement(prefix, localName, namespaceURI);
    }

    @Override
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        delegate.writeEmptyElement(namespaceURI, localName);
    }

    @Override
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        delegate.writeEmptyElement(prefix, localName, namespaceURI);

    }

    @Override
    public void writeEmptyElement(String localName) throws XMLStreamException {
        delegate.writeEmptyElement(localName);
    }

    @Override
    public void writeEndElement() throws XMLStreamException {
            delegate.writeEndElement();
    }

    @Override
    public void writeEndDocument() throws XMLStreamException {
        delegate.writeEndDocument();
    }

    @Override
    public void close() throws XMLStreamException {
        delegate.close();
    }

    @Override
    public void flush() throws XMLStreamException {
        delegate.flush();
    }

    @Override
    public void writeAttribute(String localName, String value) throws XMLStreamException {
        delegate.writeAttribute(localName, value);
    }

    @Override
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
        delegate.writeAttribute(prefix, namespaceURI, localName, value);
    }

    @Override
    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
        delegate.writeAttribute(namespaceURI, localName, value);
    }

    @Override
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        delegate.writeNamespace(prefix, namespaceURI);
    }

    @Override
    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        delegate.writeDefaultNamespace(namespaceURI);
    }

    @Override
    public void writeComment(String data) throws XMLStreamException {
        delegate.writeComment(data);
    }

    @Override
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        delegate.writeProcessingInstruction(target);
    }

    @Override
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        delegate.writeProcessingInstruction(target, data);
    }

    @Override
    public void writeCData(String data) throws XMLStreamException {
        delegate.writeCData(data);
    }

    @Override
    public void writeDTD(String dtd) throws XMLStreamException {
        delegate.writeDTD(dtd);
    }

    @Override
    public void writeEntityRef(String name) throws XMLStreamException {
        delegate.writeEntityRef(name);
    }

    @Override
    public void writeStartDocument() throws XMLStreamException {
        delegate.writeStartDocument();
    }

    @Override
    public void writeStartDocument(String version) throws XMLStreamException {
        delegate.writeStartDocument(version);
    }

    @Override
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        delegate.writeStartDocument(encoding, version);
    }

    @Override
    public void writeCharacters(String text) throws XMLStreamException {
        delegate.writeCharacters(text);
    }

    @Override
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        delegate.writeCharacters(text, start, len);
    }

    @Override
    public String getPrefix(String uri) throws XMLStreamException {
        return delegate.getPrefix(uri);
    }

    @Override
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        delegate.setPrefix(prefix, uri);
    }

    @Override
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        delegate.setDefaultNamespace(uri);
    }

    @Override
    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
        delegate.setNamespaceContext(context);
    }

    @Override
    public NamespaceContext getNamespaceContext() {
        return delegate.getNamespaceContext();
    }

    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        return delegate.getProperty(name);
    }
}