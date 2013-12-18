package org.openprovenance.prov.sql;


import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.datatype.DatatypeFactory;

import org.openprovenance.prov.sql.ObjectFactory2;
import org.openprovenance.prov.model.Attribute.AttributeKind;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.LiteralConstructor;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** A stateless factory for PROV objects. */

//TODO: move the QNameExport capability outside the factory, and make it purely stateless, without namespace. 

public class ProvFactory extends org.openprovenance.prov.model.ProvFactory implements LiteralConstructor { //implements ModelConstructor, QNameExport {

    static public DocumentBuilder builder;

    public static final String DEFAULT_NS = "_";

    private final static ProvFactory oFactory = new ProvFactory();

    public static final String packageList = "org.openprovenance.prov.sql";

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
    protected ObjectFactory of;

    

    public ProvFactory() {
	super(new ObjectFactory2());
	init();
    }


    public ProvFactory(ObjectFactory2 of) {
	super(of);
	init();
    }

    /*
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
	// TODO Auto-generated method stub
	return null;
    }
    */

   



    
    public Location newLocation(Object value, QualifiedName type) {
        Location loc=new Location();
        loc.type=type;
        loc.setValueFromObject(value);
        return loc;
    }
    public Type newType(Object value, QualifiedName type) {
        Type typ=new Type();
        typ.type=type;
        typ.setValueFromObject(value);
        return typ;
    }
    public Value newValue(Object value, QualifiedName type) {
        Value res=new Value();
        res.type=type;
        res.setValueFromObject(value);
        return res;
    }
    public Role newRole(Object value, QualifiedName type) {
        Role res=new Role();
        res.type=type;
        res.setValueFromObject(value);
        return res;
    }
    public Label newLabel(Object value, QualifiedName type) {
        Label res=new Label();
        res.type=type;
        res.setValueFromObject(value);
        return res;
    }
    public Other newOther(QualifiedName elementName, Object value, QualifiedName type) {
        Other res=new Other();
        res.type=type;
        res.setValueFromObject(value);
        res.setElementName(elementName);
        return res;
    }
    
    @Override
    public Attribute newAttribute(QualifiedName elementName, Object value, QualifiedName type) {
	// TODO: use TypedValue.getAttributeKind and switch on a kind
	if (elementName.equals(getName().PROV_LOCATION)) {
	    return newLocation(value,type);
	}
	if (elementName.equals(getName().PROV_TYPE)) {
	    return newType(value,type);
	}
	if (elementName.equals(getName().PROV_VALUE)) {
	    return newValue(value,type);
	}
	if (elementName.equals(getName().PROV_ROLE)) {
	    return newRole(value,type);
	}
	if (elementName.equals(getName().PROV_LABEL)) {
	    return newLabel(value,type);
	}
	return newOther(elementName, value, type);
    }
 

    @Override
    public org.openprovenance.prov.model.Attribute newAttribute(AttributeKind kind,
								Object value,
								QualifiedName type) {

	switch (kind) {
	case PROV_LOCATION:
	    return newLocation(value, type);
	case OTHER:
	    throw new UnsupportedOperationException();
	case PROV_LABEL:
	    return newLabel(value, type);
	case PROV_ROLE:
	    return newRole(value, type);
	case PROV_TYPE:
	    return newType(value, type);
	case PROV_VALUE:
	    return newValue(value, type);
	case PROV_KEY:
	    throw new UnsupportedOperationException();
	}
	return null;
    }

	@Override
	public QualifiedName newQualifiedName(String namespace, String local,
			String prefix) {
		return new org.openprovenance.prov.sql.QualifiedName(namespace, local, prefix);
	}


}
