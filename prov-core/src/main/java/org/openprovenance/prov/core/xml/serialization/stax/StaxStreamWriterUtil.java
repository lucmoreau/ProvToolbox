package org.openprovenance.prov.core.xml.serialization.stax;

import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.codehaus.stax2.typed.TypedXMLStreamWriter;
import org.openprovenance.prov.core.xml.serialization.CustomTypedValueSerializer;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.Serializable;

public class StaxStreamWriterUtil implements Serializable {
    public static void setPrefix(ToXmlGenerator xmlGenerator, String prefix, String provNs) throws IOException {
        try {
            xmlGenerator.getStaxWriter().setPrefix(prefix, provNs);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    public static void writeNamespace(ToXmlGenerator xmlGenerator, String prefix, String provNs) throws IOException {
        try {
            xmlGenerator.getStaxWriter().writeNamespace(prefix, provNs);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    public static void writeAttribute(ToXmlGenerator xmlGenerator, String prefix, String namespace, String property, QualifiedName qn) {
        try {
            //xmlGenerator.getStaxWriter().
            QName qn2 = qn.toQName();
            if (NamespacePrefixMapper.XSD_NS.equals(qn.getNamespaceURI())) {
                qn2 = new QName(CustomTypedValueSerializer.XSD_NS_NO_HASH, qn2.getLocalPart(), qn2.getPrefix());
            }
            if (NamespacePrefixMapper.PROV_NS.equals(qn.getNamespaceURI())
                    && ("QUALIFIED_NAME".equals(qn.getLocalPart()))) {
                qn2 = new QName(CustomTypedValueSerializer.XSD_NS_NO_HASH, "QName", "xsd");
            }

            ((TypedXMLStreamWriter) xmlGenerator.getStaxWriter()).writeQNameAttribute(prefix, namespace, property, qn2);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }

    public static void writeAttributeValue(ToXmlGenerator xmlGenerator, String prefix, String namespace, String property, QualifiedName qn) {
        try {
            //xmlGenerator.getStaxWriter().
            QName qn2 = qn.toQName();
            if (NamespacePrefixMapper.XSD_NS.equals(qn.getNamespaceURI())) {
                qn2 = new QName(CustomTypedValueSerializer.XSD_NS_NO_HASH, qn2.getLocalPart(), qn2.getPrefix());
            }
            ((TypedXMLStreamWriter) xmlGenerator.getStaxWriter()).writeQNameAttribute(prefix, namespace, property, qn2);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }

    public static void writeAttribute(ToXmlGenerator xmlGenerator, String prefix, String namespace, String property, String str) {
        try {
            //xmlGenerator.getStaxWriter().
            xmlGenerator.getStaxWriter().writeAttribute(prefix, namespace, property, str);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }

    public static void writeAttribute(ToXmlGenerator xmlGenerator, String property, String str) {
        try {
            //xmlGenerator.getStaxWriter().
            xmlGenerator.getStaxWriter().writeAttribute(property, str);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }
}