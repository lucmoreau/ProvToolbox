package org.openprovenance.prov.core.xml.serialization.stax;

import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;

public class NamespaceXMLStreamWriter2 extends WrappedXMLStreamWriter2 {
    public NamespaceXMLStreamWriter2(XMLStreamWriter2 delegate) {
        super(delegate);
    }


    @Override
    public void writeStartDocument() throws XMLStreamException {
        super.writeStartDocument();
        delegate.writeNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        delegate.setPrefix("xsd", "http://www.w3.org/2001/XMLSchema");
    }

    @Override
    public void writeStartDocument(String version) throws XMLStreamException {
        super.writeStartDocument(version);
        delegate.writeNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        delegate.setPrefix("xsd", "http://www.w3.org/2001/XMLSchema");
    }

    @Override
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        super.writeStartDocument(encoding, version);
        delegate.writeNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        delegate.setPrefix("xsd", "http://www.w3.org/2001/XMLSchema");
    }

}
