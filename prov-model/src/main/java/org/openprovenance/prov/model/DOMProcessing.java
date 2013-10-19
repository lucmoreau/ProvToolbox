package org.openprovenance.prov.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMProcessing {
    private static final QName QNAME_RDF_LITERAL = ValueConverter.QNAME_RDF_LITERAL;
    private static final String RDF_PREFIX = QNAME_RDF_LITERAL.getPrefix();
    private static final String RDF_NAMESPACE = QNAME_RDF_LITERAL.getNamespaceURI();
    private static final String RDF_LITERAL = qnameToString(QNAME_RDF_LITERAL);
    
    static public DocumentBuilder builder;
 
    static void initBuilder() {
  	try {
  	    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
  	    docBuilderFactory.setNamespaceAware(true);
  	    builder = docBuilderFactory.newDocumentBuilder();
  	} catch (ParserConfigurationException ex) {
  	    throw new RuntimeException(ex);
  	}
      }

    static {
	initBuilder();
    }
    
    
    ///TODO: should use the prefix definition of nss, as opposed to the one in qname

    static public String qnameToString(QName qname) {
	return ((qname.getPrefix().equals("")) ? "" : (qname.getPrefix() + ":"))
		+ qname.getLocalPart();
    }


    /** Create a new element, with given elementName, and QName as content. One cannot assume that 
     * the prefixes have been declared, hence all namespaces need to be declared.
     * @param elementName
     * @param value
     * @return
     */
    
    public Element newElement(QName elementName, QName value) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(elementName.getNamespaceURI(),
					 qnameToString(elementName));
	
	// 1. we add an xsi:type="xsd:QName" attribute
	//   making sure xsi and xsd prefixes are appropriately declared.
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", "xsd:QName");
	el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd", NamespacePrefixMapper.XSD_NS);

	// 2. We add the QName's string representation as child of the element
	String valueAsString=qnameToString(value);
	el.appendChild(doc.createTextNode(valueAsString));
	
	// 3. We make sure that the QName's prefix is given the right namespace, or the default namespace is declared if there is no prefix
	int index=valueAsString.indexOf(":");
	if (index!=-1) {
	    String prefix = valueAsString.substring(0, index);
	    el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:"+prefix, value.getNamespaceURI());
	} else {
	    el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", value.getNamespaceURI());
	}
	
	doc.appendChild(el);
	return el;
    }

    public Element newElement(QName qname, String value, QName type) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(qname.getNamespaceURI(),
	                                 qnameToString(qname));
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", qnameToString(type));
	el.appendChild(doc.createTextNode(value));
	doc.appendChild(el);
	return el;
    }

    public Element newElement(QName qname, String value, QName type,
			      String lang) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(qname.getNamespaceURI(),
	                                 qnameToString(qname));				 
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", qnameToString(type));
	el.setAttributeNS(NamespacePrefixMapper.XML_NS, "xml:lang", lang);
	el.appendChild(doc.createTextNode(value));
	doc.appendChild(el);
	return el;
    }


    public Object newElement(QName qname, Element value) {
        org.w3c.dom.Document doc = builder.newDocument();
        Element el = doc.createElementNS(qname.getNamespaceURI(),
                                         qnameToString(qname)); 
        el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", RDF_LITERAL);
        el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:"+RDF_PREFIX, RDF_NAMESPACE);
        el.appendChild(doc.importNode(value, true));
        return el;
    }
    
    static public void writeDOMToPrinter(Node document, StreamResult result,
                                         boolean formatted)
            throws TransformerConfigurationException, TransformerException {
        
        // Use a Transformer for output
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        DOMSource source = new DOMSource(document);
        // transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
        // transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");
        transformer.setOutputProperty(OutputKeys.METHOD,"xml");

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        if (formatted) {
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        } else {
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
        }

        transformer.transform(source, result);

    }                                                                                                                                            
    static public void writeDOMToPrinter(Node document, Writer out, boolean formatted)                                                           
            throws TransformerConfigurationException, TransformerException  {                                                                        
                                                                                                                                                     
            StreamResult result = new StreamResult(out);                                                                                             
            writeDOMToPrinter(document,result,formatted);                                                                                            
        }                                                                                                                                            
                 
    static public String writeToString (Node toWrite)                                                                                            
            throws TransformerConfigurationException, TransformerException {                                                                         
                                                                                                                                                     
            StringWriter sw=new StringWriter();                                                                                                      
            writeDOMToPrinter(toWrite,new PrintWriter(sw),false);                                                                                    
            return sw.toString();                                                                                                                    
        }     
    
    
    public static void trimNode(org.w3c.dom.Node node) {
    	List<org.w3c.dom.Text> nodes=new LinkedList<org.w3c.dom.Text>();
    	trimNode(node,nodes);
    	for (org.w3c.dom.Text n: nodes){
    		if (n.getTextContent().equals(""))
    		n.getParentNode().removeChild(n);
    	}
    }	
	
	static void trimNode(org.w3c.dom.Node node, List<org.w3c.dom.Text> nodes) {
		if (node.getNodeType() == node.TEXT_NODE) {
			node.normalize();
			org.w3c.dom.Text txt = (org.w3c.dom.Text) node;
			txt.setTextContent(txt.getTextContent().trim());
			System.out.println("<" + node.getTextContent() + ">");
			nodes.add(txt);
		} else {
			NodeList nl = node.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				trimNode(nl.item(i), nodes);
			}
		}
	}


}
