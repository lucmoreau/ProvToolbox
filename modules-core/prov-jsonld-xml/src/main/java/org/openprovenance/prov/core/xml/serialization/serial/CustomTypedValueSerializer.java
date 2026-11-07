package org.openprovenance.prov.core.xml.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.openprovenance.prov.vanilla.TypedValue;
import tools.jackson.databind.SerializationContext;
import org.openprovenance.prov.core.xml.serialization.Constants;
import org.openprovenance.prov.core.xml.serialization.ProvSerialiser;
import org.openprovenance.prov.core.xml.serialization.stax.StaxStreamWriterUtil;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;


public class CustomTypedValueSerializer extends StdSerializer<TypedValue> implements Constants {

    public static final String NAMESPACE_XSI = NamespacePrefixMapper.XSI_NS;
    public static final String NAMESPACE_XML = NamespacePrefixMapper.XML_NS;
    public static final String PREFIX_XSI = "xsi";
    public static final String PREFIX_XML = "xml";
    static final public String XSD_NS_NO_HASH = "http://www.w3.org/2001/XMLSchema";

    public CustomTypedValueSerializer() {
        super(TypedValue.class);
    }

    protected CustomTypedValueSerializer(Class<TypedValue> t) {
        super(t);
    }

    @Override
    public void serialize(TypedValue attr, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;


        xmlGenerator.setNextIsAttribute(true);
        jsonGenerator.writeStartObject();

        Object value = attr.getValue();
        QualifiedName type = attr.getType();


        if (value instanceof org.openprovenance.prov.model.LangString) {
            org.openprovenance.prov.model.LangString langString = (org.openprovenance.prov.model.LangString) value;
            String lang = langString.getLang();
            if (lang != null)
                StaxStreamWriterUtil.writeAttribute(xmlGenerator, PREFIX_XML, NAMESPACE_XML, PROPERTY_STRING_LANG, lang);
            value = langString.getValue();
        }



        if (type != null)
            StaxStreamWriterUtil.writeAttribute(xmlGenerator, PREFIX_XSI, NAMESPACE_XSI, PROPERTY_AT_TYPE, type);

        xmlGenerator.setNextIsAttribute(false);
        xmlGenerator.setNextIsUnwrapped(true);

        if ((!(value instanceof String)) && !(value instanceof QualifiedName)) {
           // throw new UnsupportedOperationException("unknown value type " + value);
            // no need to throw an exception, serialization may just be awkward
        }

        jsonGenerator.writePOJOProperty(PROPERTY_AT_VALUE, value);

        jsonGenerator.writeEndObject();

    }


}