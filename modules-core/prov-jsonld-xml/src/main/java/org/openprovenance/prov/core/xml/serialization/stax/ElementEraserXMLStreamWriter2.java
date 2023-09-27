package org.openprovenance.prov.core.xml.serialization.stax;

import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;
import java.util.Stack;

import static org.openprovenance.prov.core.xml.serialization.serial.CustomDocumentSerializer.TO_DELETE;

public class ElementEraserXMLStreamWriter2 extends NamespaceXMLStreamWriter2 {

    public ElementEraserXMLStreamWriter2(XMLStreamWriter2 delegate) {
        super(delegate);
    }

    int count = 0;
    Stack<Integer> ignore = new Stack<>();


    @Override
    public void writeEndElement() throws XMLStreamException {
        //      System.out.println(" count " + count + " ignore " + ignore);
        int valueToIgnore=(ignore.empty())?-5:ignore.peek();
        if (count != valueToIgnore) {
            super.writeEndElement();
        } else {
            ignore.pop();
        }
        count--;
    }

    @Override
    public void writeStartElement(String localName) throws XMLStreamException {
        count++;
        if (elementToBeDeleted(localName)) {
            //    System.out.println(" * writeStartElement " + localName + " " + count);
            ignore.push(count);
        } else {

            super.writeStartElement(localName);
        }
    }

    private boolean elementToBeDeleted(String localName) {
        return localName.equals("statements") || localName.equals(TO_DELETE);
    }

    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        count++;
        if (elementToBeDeleted(localName)) {
            //      System.out.println(" * writeStartElement " + localName + " " + namespaceURI + " " + count);
            ignore.push(count);
        } else {
           // System.out.println("localnaem " + localName);
            super.writeStartElement(namespaceURI, localName);
        }
    }

    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        count++;
        if (elementToBeDeleted(localName)) {
            //       System.out.println(" * writeStartElement " + localName + " " + namespaceURI + " (pre " + prefix + " " + count);
            ignore.push(count);
        } else {
            super.writeStartElement(prefix, localName, namespaceURI);
        }
    }

    @Override
    public String toString() {
        return "<<" + super.toString() + ">>";
    }
}
