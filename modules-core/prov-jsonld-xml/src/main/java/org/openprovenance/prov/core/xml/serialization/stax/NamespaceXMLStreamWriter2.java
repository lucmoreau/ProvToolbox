package org.openprovenance.prov.core.xml.serialization.stax;

import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;

public class NamespaceXMLStreamWriter2 extends WrappedXMLStreamWriter2 {
    public NamespaceXMLStreamWriter2(XMLStreamWriter2 delegate) {
        super(delegate);
    }

boolean first=true;
    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        super.writeStartElement(namespaceURI, localName);
        if (first) {
            first = false;

            writeNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
            writeNamespace("prov", "http://www.w3.org/ns/prov#");
            writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

           // setPrefix("xsd", "http://www.w3.org/2001/XMLSchema");
        }
    }
}
