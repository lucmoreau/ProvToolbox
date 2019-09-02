package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.codehaus.stax2.typed.TypedXMLStreamWriter;
import org.openprovenance.prov.core.vanilla.LangString;
import org.openprovenance.prov.core.vanilla.ProvFactory;
import org.openprovenance.prov.core.vanilla.TypedValue;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.openprovenance.prov.core.xml.serialization.CustomAttributesSerializer.*;


public class CustomTypedValueSerializer extends StdSerializer<TypedValue> implements Constants {

    static final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;
    public static final String NAMESPACE_XSI = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String NAMESPACE_XML = "http://www.w3.org/XML/1998/namespace";
    public static final String PREFIX_XSI = "xsi";
    public static final String PREFIX_XML = "xml";

    protected CustomTypedValueSerializer() {
        super(TypedValue.class);
    }

    protected CustomTypedValueSerializer(Class<TypedValue> t) {
        super(t);
    }
    @Override
    public void serialize(TypedValue attr, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s=null;
        ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;



        if ((attr.getValue() instanceof LangString) &&
                ((LangString)attr.getValue()).getLang()==null &&
                (QUALIFIED_NAME_XSD_STRING.equals(attr.getType()))) {
            String strValue = ((LangString) attr.getValue()).getValue();
            jsonGenerator.writeString(strValue);

        } else if ((attr.getValue() instanceof String) &&
                (QUALIFIED_NAME_XSD_STRING.equals(attr.getType()))) {
            throw new UnsupportedOperationException("should never be here");
        } else {
            xmlGenerator.setNextIsAttribute(true);
            jsonGenerator.writeStartObject();


            writeAttribute(xmlGenerator,PREFIX_XSI, NAMESPACE_XSI,PROPERTY_AT_TYPE,attr.getType());
            Object obj=attr.getValue();
            if (obj instanceof org.openprovenance.prov.model.LangString) {
                String theLang = ((org.openprovenance.prov.model.LangString) obj).getLang();

                writeAttribute(xmlGenerator, PREFIX_XML, NamespacePrefixMapper.XML_NS, PROPERTY_STRING_LANG, theLang);

                xmlGenerator.setNextIsAttribute(false);

                xmlGenerator.setNextIsUnwrapped(true);
                serializeValue(PROPERTY_AT_VALUE, ((org.openprovenance.prov.model.LangString) obj).getValue(), jsonGenerator, serializerProvider);
            } else if (obj instanceof QualifiedName) {
                QualifiedName qn=(QualifiedName) obj;
                QName q=qn.toQName();
                //setPrefix(xmlGenerator,qn.getPrefix(),qn.getNamespaceURI());
                writeNamespace(xmlGenerator, qn.getPrefix(), qn.getNamespaceURI());

System.out.println("Adding prefix for " + qn);

                xmlGenerator.setNextIsAttribute(false);
                xmlGenerator.setNextIsUnwrapped(true);

                serializeValue(PROPERTY_AT_VALUE, obj, jsonGenerator, serializerProvider);

            } else {
                xmlGenerator.setNextIsAttribute(false);
                xmlGenerator.setNextIsUnwrapped(true);
                serializeValue(PROPERTY_AT_VALUE, obj, jsonGenerator, serializerProvider);
            }
            jsonGenerator.writeEndObject();
        }
    }



    static final public String XSD_NS_NO_HASH = "http://www.w3.org/2001/XMLSchema";

    static void writeAttribute(ToXmlGenerator xmlGenerator, String prefix, String namespace, String property, QualifiedName qn) {
        try {
            //xmlGenerator.getStaxWriter().
            QName qn2=qn.toQName();
            if (NamespacePrefixMapper.XSD_NS.equals(qn.getNamespaceURI())) {
                qn2=new QName(XSD_NS_NO_HASH,qn2.getLocalPart(),qn2.getPrefix());
            }
            if (NamespacePrefixMapper.PROV_NS.equals(qn.getNamespaceURI())
                    && ("QUALIFIED_NAME".equals(qn.getLocalPart()))) {
                qn2=new QName(XSD_NS_NO_HASH,"QName","xsd");
            }

            ((TypedXMLStreamWriter)xmlGenerator.getStaxWriter()).writeQNameAttribute(prefix, namespace,property, qn2);
            System.out.println("%%%%% qn " + qn);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }
    static void writeAttributeValue(ToXmlGenerator xmlGenerator, String prefix, String namespace, String property, QualifiedName qn) {
        try {
            //xmlGenerator.getStaxWriter().
            QName qn2=qn.toQName();
            if (NamespacePrefixMapper.XSD_NS.equals(qn.getNamespaceURI())) {
                qn2=new QName(XSD_NS_NO_HASH,qn2.getLocalPart(),qn2.getPrefix());
            }
            ((TypedXMLStreamWriter)xmlGenerator.getStaxWriter()).writeQNameAttribute(prefix,namespace,property,qn2);
            System.out.println("%%%%% qn " + qn);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }
    static void writeAttribute(ToXmlGenerator xmlGenerator, String prefix, String namespace, String property, String str) {
        try {
            //xmlGenerator.getStaxWriter().
            xmlGenerator.getStaxWriter().writeAttribute(prefix, namespace,property, str);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }

    static void writeAttribute(ToXmlGenerator xmlGenerator, String property, String str) {
        try {
            //xmlGenerator.getStaxWriter().
            xmlGenerator.getStaxWriter().writeAttribute(property, str);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }

    private void serializeValue(String fieldName, Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value instanceof String) {
            jsonGenerator.writeStringField (fieldName, (String)value);
            return;
        }
        if (value instanceof QualifiedName) {
            jsonGenerator.writeStringField (fieldName, prnt((QualifiedName)value));
            return;
        }
        if (value instanceof LangString) {
            jsonGenerator.writeFieldName(fieldName);
            jsonGenerator.writeObject(value);
            return;
        }
        throw new UnsupportedOperationException("unknown value type " + value);
    }

    private String prnt(QualifiedName qn) {
        return qn.getPrefix() + ":" + qn.getLocalPart();
    }
}