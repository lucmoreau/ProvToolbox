package org.openprovenance.prov.xml;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.datatype.DatatypeFactory;
import org.openprovenance.prov.model.Attribute.AttributeKind;
import org.openprovenance.prov.model.QualifiedName;

/** A stateless factory for PROV objects. */

//TODO: move the QNameExport capability outside the factory, and make it purely stateless, without namespace. 

public class ProvFactory extends org.openprovenance.prov.model.ProvFactory {

    private static String fileName = "toolbox.properties";

    private final static ProvFactory oFactory = new ProvFactory();

    //public static final String packageList = "org.openprovenance.prov.xml:org.openprovenance.prov.xml.validation";
    public static final String packageList = "org.openprovenance.prov.xml";
    private static final String toolboxVersion = getPropertiesFromClasspath(fileName).getProperty("toolbox.version");

    public static ProvFactory getFactory() {
	return oFactory;
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


    public String getVersion() {
        return toolboxVersion;
    }

    @Override
    public org.openprovenance.prov.model.Attribute newAttribute(AttributeKind kind,
								Object value,
								org.openprovenance.prov.model.QualifiedName type) {
	switch (kind) {
	case PROV_LOCATION: return newLocation(value, type);
	case OTHER: throw new UnsupportedOperationException();
	case PROV_LABEL: return newLabel(value,type); 
	case PROV_ROLE: return newRole(value, type);
	case PROV_TYPE: return newType(value, type);
	case PROV_VALUE: return newValue(value, type);
	case PROV_KEY: throw new UnsupportedOperationException();
	}
	return null;
    }

    public org.openprovenance.prov.model.Attribute newAttribute(QualifiedName elementName, Object value, QualifiedName type) {

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
	if (elementName.equals(getName().PROV_KEY)) {
	    return newKey(value,type);
	}
	return newOther(elementName, value, type);
    }
    
   
    
    public Key newKey(Object value, QualifiedName type) {
        Key key=new Key();
        key.type=type;
        key.setValueFromObject(value);
        return key;
    }
    public Label newLabel(Object value, QualifiedName type) {
        Label res=new Label();
        res.type=type;
        res.setValueFromObject(value);
        return res;
    }
    public Location newLocation(Object value, QualifiedName type) {
        Location loc=new Location();
        loc.type=type;
        loc.setValueFromObject(value);
        return loc;
    }
    public Other newOther(QualifiedName elementName, Object value, QualifiedName type) {
        Other res=new Other();
        res.type=type;
        res.setValueFromObject(value);
        res.setElementName(elementName);
        return res;
    }
    @Override
    public org.openprovenance.prov.model.QualifiedName newQualifiedName(String namespace,
									String local,
									String prefix) {
	return new org.openprovenance.prov.xml.QualifiedName(namespace, local, prefix);
    }
    

}
