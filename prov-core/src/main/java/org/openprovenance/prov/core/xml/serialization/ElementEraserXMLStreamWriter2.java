package org.openprovenance.prov.core.xml.serialization;

import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;

public class ElementEraserXMLStreamWriter2 extends NamespaceXMLStreamWriter2 {

    public ElementEraserXMLStreamWriter2(XMLStreamWriter2 delegate) {
        super(delegate);
    }

    int count = 0;
    int ignore = -1;

    @Override
    public void writeEndElement() throws XMLStreamException {
        count--;
        //      System.out.println(" count " + count + " ignore " + ignore);
        if (count != ignore)
            super.writeEndElement();
    }

    @Override
    public void writeStartElement(String localName) throws XMLStreamException {
        count++;
        if (localName.equals("statements")) {
            //    System.out.println(" * writeStartElement " + localName + " " + count);
            ignore = count;
        } else {

            super.writeStartElement(localName);
        }
    }

    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        count++;
        if (localName.equals("statements")) {
            //      System.out.println(" * writeStartElement " + localName + " " + namespaceURI + " " + count);
            ignore = count;
        } else {
            super.writeStartElement(namespaceURI, localName);
        }
    }

    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        count++;
        if (localName.equals("statements")) {
            //       System.out.println(" * writeStartElement " + localName + " " + namespaceURI + " (pre " + prefix + " " + count);
            ignore = count;
        } else {
            super.writeStartElement(prefix, localName, namespaceURI);
        }
    }
}
