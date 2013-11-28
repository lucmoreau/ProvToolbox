package org.openprovenance.prov.xml;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.namespace.QName;
import javax.xml.datatype.DatatypeFactory;
import org.openprovenance.prov.model.Attribute.AttributeKind;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** A stateless factory for PROV objects. */

//TODO: move the QNameExport capability outside the factory, and make it purely stateless, without namespace. 

public class ProvFactory extends org.openprovenance.prov.model.ProvFactory {

    private final static ProvFactory oFactory = new ProvFactory();

    //public static final String packageList = "org.openprovenance.prov.xml:org.openprovenance.prov.xml.validation";
    public static final String packageList = "org.openprovenance.prov.xml";

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

    public static String printURI(java.net.URI u) {
	return u.toString();
    }

    protected DatatypeFactory dataFactory;
  
    public ProvFactory() {
	super(new ObjectFactory2());
	init();
    }


    public ProvFactory(ObjectFactory2 of) {
	super(of);
	init();
    }

  
    public org.openprovenance.prov.model.Attribute newAttribute(QName elementName, Object value, QName type) {

	// TODO: use TypedValue.getAttributeKind and switch on a kind
	if (elementName.equals(Helper.PROV_LOCATION_QNAME)) {
	    return newLocation(value,type);
	}
	if (elementName.equals(Helper.PROV_TYPE_QNAME)) {
	    return newType(value,type);
	}
	if (elementName.equals(Helper.PROV_VALUE_QNAME)) {
	    return newValue(value,type);
	}
	if (elementName.equals(Helper.PROV_ROLE_QNAME)) {
	    return newRole(value,type);
	}
	if (elementName.equals(Helper.PROV_LABEL_QNAME)) {
	    return newLabel(value,type);
	}	
	if (elementName.equals(Helper.PROV_KEY_QNAME)) {
	    return newKey(value,type);
	}
	return newOther(elementName, value, type);
    }
    
   
    
    public Location newLocation(Object value, QName type) {
        Location loc=new Location();
        loc.type=type;
        loc.setValueAsObject(value);
        return loc;
    }
    public Type newType(Object value, QName type) {
        Type typ=new Type();
        typ.type=type;
        typ.setValueAsObject(value);
        return typ;
    }
    public Value newValue(Object value, QName type) {
        Value res=new Value();
        res.type=type;
        res.setValueAsObject(value);
        return res;
    }
    public Role newRole(Object value, QName type) {
        Role res=new Role();
        res.type=type;
        res.setValueAsObject(value);
        return res;
    }
    public Label newLabel(Object value, QName type) {
        Label res=new Label();
        res.type=type;
        res.setValueAsObject(value);
        return res;
    }
    public Other newOther(QName elementName, Object value, QName type) {
        Other res=new Other();
        res.type=type;
        res.setValueAsObject(value);
        res.setElementName(elementName);
        return res;
    }
    public Key newKey(Object value, QName type) {
        Key typ=new Key();
        typ.type=type;
        typ.setValueAsObject(value);
        return typ;
    }
    

    


    @Override
    public org.openprovenance.prov.model.Attribute newAttribute(AttributeKind kind,
								Object value,
								QName type) {
	switch (kind) {
	case PROV_LOCATION: return newLocation(value, type);
	case OTHER: throw new UnsupportedOperationException();
	case PROV_LABEL: return newLabel(value,type); 
	case PROV_ROLE: return newRole(value, type);
	case PROV_TYPE: return newType(value, type);
	case PROV_VALUE: return newValue(value, type);
	case PROV_KEY: return newValue(value, type);
	}
	return null;
    }

}
