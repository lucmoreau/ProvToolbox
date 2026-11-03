package org.openprovenance.prov.core.xml.serialization.stax;

import tools.jackson.core.JsonGenerator;
import tools.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.codehaus.stax2.typed.TypedXMLStreamWriter;
import org.openprovenance.prov.core.xml.serialization.serial.CustomTypedValueSerializer;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.Serializable;

public class StaxStreamWriterUtil implements Serializable {
    public static void setPrefix(JsonGenerator jsonGenerator, String prefix, String provNs) {
        ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;
        try {
            xmlGenerator.getStaxWriter().setPrefix(prefix, provNs);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw tools.jackson.core.exc.JacksonIOException.construct(new IOException(e)); // jackson 3: unchecked
        }
    }

    public static void  writeNamespace(JsonGenerator jsonGenerator, String prefix, String provNs) {
        try {
            ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;
            xmlGenerator.getStaxWriter().writeNamespace(prefix, provNs);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw tools.jackson.core.exc.JacksonIOException.construct(new IOException(e)); // jackson 3: unchecked
        }
    }
    public static void  writeDefaultNamespace(JsonGenerator jsonGenerator, String provNs) {
        try {
            ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;
            xmlGenerator.getStaxWriter().setDefaultNamespace(provNs);
            xmlGenerator.getStaxWriter().writeDefaultNamespace(provNs);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw tools.jackson.core.exc.JacksonIOException.construct(new IOException(e)); // jackson 3: unchecked
        }
    }

    public static String  getDefaultNamespace(JsonGenerator jsonGenerator) {
        ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;
        return xmlGenerator.getStaxWriter().getNamespaceContext().getNamespaceURI("");
    }

    public static void writeAttribute(JsonGenerator jsonGenerator, String prefix, String namespace, String property, QualifiedName qn) {
        ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;

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

    public static void writeAttributeValue(JsonGenerator jsonGenerator, String prefix, String namespace, String property, QualifiedName qn) {
        ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;

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

    public static void writeAttribute(JsonGenerator jsonGenerator, String prefix, String namespace, String property, String str) {
        ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;

        try {
            //xmlGenerator.getStaxWriter().
            xmlGenerator.getStaxWriter().writeAttribute(prefix, namespace, property, str);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }

    public static void writeAttribute(JsonGenerator jsonGenerator, String property, String str) {
        ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;

        try {
            //xmlGenerator.getStaxWriter().
            xmlGenerator.getStaxWriter().writeAttribute(property, str);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }
}