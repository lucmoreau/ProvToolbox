package org.openprovenance.prov.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

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

final public class DOMProcessing {
    private static final String XML_LITERAL = "XMLLiteral";
    private static final String RDF_PREFIX = NamespacePrefixMapper.RDF_PREFIX;
    private static final String RDF_NAMESPACE = NamespacePrefixMapper.RDF_NS;
    private static final String RDF_LITERAL = RDF_PREFIX + ":" + XML_LITERAL;
    private final ProvFactory pFactory;
    
    public DOMProcessing(ProvFactory pFactory) {
	this.pFactory=pFactory;
    }
    
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
    
    
    static public String qualifiedNameToString(QualifiedName name) {
	Namespace ns=Namespace.getThreadNamespace();
	return ns.qualifiedNameToString(name);
    }
    

    

    /**
     * Converts a string to a QualifiedName, extracting namespace from the DOM.
     * 
     * @param str
     *            string to convert to QualifiedName
     * @param el
     *            current Element in which this string was found (as attribute
     *            or attribute value)
     * @return a qualified name {@link QualifiedName}
     */
    final public QualifiedName stringToQualifiedName(String str, org.w3c.dom.Element el) {
        if (str == null)
            return null;
        int index = str.indexOf(':');
        if (index == -1) {
            QualifiedName qn = pFactory.newQualifiedName(el.lookupNamespaceURI(null), // find default namespace
                                                         // namespace
                                                         str,
                                                         null);
            return qn;
        }
        String prefix = str.substring(0, index);
        String local = str.substring(index + 1, str.length());
        QualifiedName qn = pFactory.newQualifiedName(el.lookupNamespaceURI(prefix), local, prefix);
        return qn;
    }

    
    
    
    final static public Element newElement(QualifiedName elementName, QualifiedName value) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(elementName.getNamespaceURI(),
					 qualifiedNameToString(elementName));
	
	// 1. we add an xsi:type="xsd:QName" attribute
	//   making sure xsi and xsd prefixes are appropriately declared.
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", "xsd:QName");
	el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd", NamespacePrefixMapper.XSD_NS);

	// 2. We add the QualifiedName's string representation as child of the element
	//    This representation depends on the extant prefix-namespace mapping
	String valueAsString=qualifiedNameToString(value);
	el.appendChild(doc.createTextNode(valueAsString));
	
	// 3. We make sure that the QualifiedName's prefix is given the right namespace, or the default namespace is declared if there is no prefix
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

    final static public Element newElement(QualifiedName name, String value, QualifiedName type) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(name.getNamespaceURI(),
	                                 qualifiedNameToString(name));
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", qualifiedNameToString(type));
	el.appendChild(doc.createTextNode(value));
	doc.appendChild(el);
	return el;
    }


    final public static Element newElement(QualifiedName name, 
                                           String value, 
                                           QualifiedName type,
                                           String lang) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(name.getNamespaceURI(),
	                                 qualifiedNameToString(name));				 
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", qualifiedNameToString(type));
	el.setAttributeNS(NamespacePrefixMapper.XML_NS, "xml:lang", lang);
        el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xml", NamespacePrefixMapper.XML_NS);

	el.appendChild(doc.createTextNode(value));
	doc.appendChild(el);
	return el;
    }


    final static public Element newElement(QualifiedName name, Element value) {
        org.w3c.dom.Document doc = builder.newDocument();
        Element el = doc.createElementNS(name.getNamespaceURI(),
                                         qualifiedNameToString(name)); 
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
    
    
    public static void trimNode(Node node) {
    	List<org.w3c.dom.Text> nodes=new LinkedList<org.w3c.dom.Text>();
    	trimNode(node,nodes);
    	for (org.w3c.dom.Text n: nodes){
    		if (n.getTextContent().equals(""))
    		n.getParentNode().removeChild(n);
    	}
    }	

    static void trimNode(Node node, List<org.w3c.dom.Text> nodes) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            node.normalize();
            org.w3c.dom.Text txt = (org.w3c.dom.Text) node;
            txt.setTextContent(txt.getTextContent().trim());
            nodes.add(txt);
        } else {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                trimNode(nl.item(i), nodes);
            }
        }
    }
    
    final static
    public org.w3c.dom.Element marshalAttribute(org.openprovenance.prov.model.Attribute attribute) {
	return marshalTypedValue(attribute, attribute.getElementName());
    }
    
    final static
    public org.w3c.dom.Element marshalTypedValue(org.openprovenance.prov.model.TypedValue attribute,
                                                 QualifiedName elementName) {	
	Object value = attribute.getValue();
	QualifiedName type = attribute.getType();
	
	if (value instanceof LangString) {
	    LangString istring = ((LangString) value);
	    return newElement(elementName, 
	                      istring.getValue(),
			      attribute.getType(), 
			      istring.getLang());
	} else if (value instanceof QualifiedName) {
	    return newElement(elementName, 
	                      (QualifiedName) value);

	} else if (type.getNamespaceURI().equals(RDF_NAMESPACE)
		&& type.getLocalPart().equals(XML_LITERAL)) {
	    return newElement(elementName,
			      (org.w3c.dom.Element) attribute.getConvertedValue());

	} else {
	    return newElement(elementName, 
	                      value.toString(),
			      attribute.getType());
	}
    }
    
    final
    public org.openprovenance.prov.model.Attribute unmarshallAttribute(org.w3c.dom.Element el,
                                                                              ProvFactory pFactory,
                                                                              ValueConverter vconv) {
        String prefix = el.getPrefix();
        String namespace = el.getNamespaceURI();
        String local = el.getLocalName();
        String child = el.getTextContent();
        String typeAsString = el.getAttributeNS(NamespacePrefixMapper.XSI_NS,
                                                "type");

        String lang = el.getAttributeNS(NamespacePrefixMapper.XML_NS, "lang");
        QualifiedName type = ((typeAsString == null) || (typeAsString.equals(""))) ? null
                : stringToQualifiedName(typeAsString, el);
        Name name=pFactory.getName();
        if (type == null)
            type = name.XSD_STRING;
        if (type.equals(name.XSD_QNAME)) {
            QualifiedName qn = stringToQualifiedName(child, el);
            Attribute x= pFactory.newAttribute(namespace, local, prefix, qn, type);
            return x;
        } else if (type.equals(name.RDF_LITERAL)) {
            NodeList nodes=el.getChildNodes();
            org.w3c.dom.Element content=null;
            for (int i=0; i<nodes.getLength(); i++) {
                Node node=nodes.item(i);
                if (node instanceof org.w3c.dom.Element) {
                    content=(org.w3c.dom.Element)node;
                    break;
                }
            }
            return pFactory
                    .newAttribute(namespace, local, prefix,
                                  content, type);
        } else if ((lang == null) || (lang.equals(""))) {
            return pFactory
                    .newAttribute(namespace, local, prefix,
                                  vconv.convertToJava(type, child), type);
        } else {
            return pFactory.newAttribute(namespace, local, prefix, pFactory
                    .newInternationalizedString(child, lang), type);
        }
    }


    

}
