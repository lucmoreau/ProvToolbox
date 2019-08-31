package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.openprovenance.prov.core.vanilla.TypedValue;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Set;

import static org.openprovenance.prov.core.xml.serialization.CustomTypedValueSerializer.writeAttribute;


public class CustomAttributesSerializer extends StdSerializer<Object> {


    protected CustomAttributesSerializer() {
        super(Object.class);

    }

    protected CustomAttributesSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        QualifiedName newKey=(QualifiedName) serializerProvider.getAttribute(CustomMapSerializer2.CONTEXT_KEY_FOR_MAP);

        Set<TypedValue> set=(Set<TypedValue>) o;
        

        if (!(set.isEmpty())) {
            ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;

            setPrefix(xmlGenerator,newKey.getPrefix(),newKey.getNamespaceURI());

            xmlGenerator.setNextName(new QName(newKey.getNamespaceURI(),newKey.getLocalPart()));

            jsonGenerator.writeFieldName(newKey.getLocalPart());

            jsonGenerator.writeStartArray();

            for (TypedValue a : set) {

                new CustomTypedValueSerializer().serialize(a, jsonGenerator, serializerProvider);
            }

            jsonGenerator.writeEndArray();
        }
    }

    static void setDefaultNamespace(ToXmlGenerator xmlGenerator, String provNs) throws IOException {
        try {
            xmlGenerator.getStaxWriter().writeDefaultNamespace(provNs);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }
    public void writeEmptyElement(ToXmlGenerator xmlGenerator, String name, String prefix, String provNs) throws IOException {
        try {
            xmlGenerator.getStaxWriter().writeEmptyElement(prefix,name,provNs);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }
    public void setPrefix(ToXmlGenerator xmlGenerator, String prefix, String provNs) throws IOException {
        try {
            xmlGenerator.getStaxWriter().setPrefix(prefix,provNs);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }


}