package nlg.wrapper.stax;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;


import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.Serializable;

public class StaxStreamWriterUtil implements Serializable {
    public static void setPrefix(JsonGenerator jsonGenerator, String prefix, String provNs) throws IOException {
        ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;
        try {
            xmlGenerator.getStaxWriter().setPrefix(prefix, provNs);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    public static void writeNamespace(JsonGenerator jsonGenerator, String prefix, String provNs) throws IOException {
        try {
            ToXmlGenerator xmlGenerator=(ToXmlGenerator)jsonGenerator;
            xmlGenerator.getStaxWriter().writeNamespace(prefix, provNs);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new IOException(e);
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