package nlg.wrapper.stax;

import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;
import java.util.Stack;
public class ElementEraserXMLStreamWriter2 extends NamespaceXMLStreamWriter2 {
    private final String key;

    public ElementEraserXMLStreamWriter2(XMLStreamWriter2 delegate, String key, int count) {
        super(delegate);
        this.count = count;
        this.key=key;
    }

    //public static final String key = "statements";

    public ElementEraserXMLStreamWriter2(XMLStreamWriter2 delegate, String key) {
        super(delegate);
        this.key=key;
    }

    int count = 0;
    Stack<Integer> ignore = new Stack<Integer>();


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
        if (localName.equals(key)) {
            //    System.out.println(" * writeStartElement " + localName + " " + count);
            ignore.push(count);
        } else {

            super.writeStartElement(localName);
        }
    }

    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        count++;
        if (localName.equals(key)) {
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
        if (localName.equals(key)) {
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
