package org.openprovenance.prov.model;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

public class DOMProcessing {
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


}
