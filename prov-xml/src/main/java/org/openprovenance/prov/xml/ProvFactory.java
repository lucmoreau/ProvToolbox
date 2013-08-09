package org.openprovenance.prov.xml;

import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;
import java.util.Date;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.xml.bind.JAXBElement;
import java.util.GregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;

import org.openprovenance.prov.model.Attribute.AttributeKind;
import org.openprovenance.prov.model.KeyQNamePair;
import org.openprovenance.prov.model.LiteralConstructor;
import org.openprovenance.prov.model.URIWrapper;
import org.openprovenance.prov.xml.DictionaryMembership;
import org.openprovenance.prov.xml.DerivedByInsertionFrom;
import org.openprovenance.prov.xml.DerivedByRemovalFrom;
import org.openprovenance.prov.xml.DictionaryMembership;
import org.openprovenance.prov.xml.Entry;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** A stateless factory for PROV objects. */

//TODO: move the QNameExport capability outside the factory, and make it purely stateless, without namespace. 

public class ProvFactory extends org.openprovenance.prov.model.ProvFactory {

    static public DocumentBuilder builder;

    public static final String DEFAULT_NS = "_";

    private final static ProvFactory oFactory = new ProvFactory();

    public static final String packageList = "org.openprovenance.prov.xml:org.openprovenance.prov.xml.validation";

    static {
	initBuilder();
    }

    private static String fileName = "toolbox.properties";
    private static final String toolboxVersion = getPropertiesFromClasspath(fileName).getProperty("toolbox.version");

    public String getVersion() {
        return toolboxVersion;
    }

    private static Properties getPropertiesFromClasspath(String propFileName) {
        Properties props = new Properties();
        InputStream inputStream = ProvFactory.class.getClassLoader().getResourceAsStream(propFileName);
        if (inputStream == null) {
            return null;
        }
        try {
            props.load(inputStream);
        } catch (IOException ee) {
            return null;
        }
        return props;   
    }

    public static ProvFactory getFactory() {
	return oFactory;
    }

    static void initBuilder() {
	try {
	    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	    docBuilderFactory.setNamespaceAware(true);
	    builder = docBuilderFactory.newDocumentBuilder();
	} catch (ParserConfigurationException ex) {
	    throw new RuntimeException(ex);
	}
    }

    public static String printURI(java.net.URI u) {
	return u.toString();
    }


    protected DatatypeFactory dataFactory;
    /** Note, this method now makes it stateful :-( */
    private Hashtable<String, String> namespaces = null;

    final protected org.openprovenance.prov.xml.validation.ObjectFactory vof;

    public ProvFactory() {
	super(new ObjectFactory2());
	vof = new org.openprovenance.prov.xml.validation.ObjectFactory();
	init();
	setNamespaces(new Hashtable<String, String>());
    }

    public ProvFactory(Hashtable<String, String> namespaces) {
	super(new ObjectFactory2(), namespaces);
	vof = new org.openprovenance.prov.xml.validation.ObjectFactory();
	this.namespaces = namespaces;
	init();
    }

    public ProvFactory(ObjectFactory2 of) {
	super(of);
	vof = new org.openprovenance.prov.xml.validation.ObjectFactory();
	init();
	setNamespaces(new Hashtable<String, String>());
    }

  






    public Attribute newAttribute(QName qname, Object value, ValueConverter vconv) {
  	Attribute res = new Attribute(qname, value, vconv.getXsdType(value));
  	return res;
      }

    public Attribute newAttribute(QName qname, Object value, QName type) {
  	Attribute res = new Attribute(qname, value, type);
  	return res;
      }

    public Attribute newAttribute(Attribute.AttributeKind kind, Object value, ValueConverter vconv) {
  	Attribute res = new Attribute(kind, value, vconv.getXsdType(value));
  	return res;
      }
    public Attribute newAttribute(Attribute.AttributeKind kind, Object value, QName type) {
  	Attribute res = new Attribute(kind, value, type);
  	return res;
      }

    public Attribute newAttribute(String namespace, String localName,
				  String prefix, Object value, ValueConverter vconv) {
	Attribute res = new Attribute(new QName(namespace, localName, prefix),
				      value, vconv.getXsdType(value));
	return res;
    }

    public Attribute newAttribute(String namespace, String localName,
				  String prefix, Object value, QName type) {
	Attribute res = new Attribute(new QName(namespace, localName, prefix),
				      value, type);
	return res;
    }

 


    @Override
    public org.openprovenance.prov.model.Attribute createAttribute(QName qname,
							    Object value,
							    QName type) {
	return new Attribute(qname,value,type);
    }

    @Override
    public org.openprovenance.prov.model.Attribute createAttribute(AttributeKind kind,
							    Object value,
							    QName type) {
	return new Attribute(kind,value,type);

    }

    @Override
    public org.openprovenance.prov.model.IDRef createIDRef() {
	
	return new IDRef();
    }

    public org.openprovenance.prov.xml.validation.ObjectFactory getValidationObjectFactory() {
        return vof;
    }

}
