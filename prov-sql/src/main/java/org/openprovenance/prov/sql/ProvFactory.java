package org.openprovenance.prov.sql;


import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.sql.ObjectFactory2;
import org.openprovenance.prov.xml.ProvUtilities;
import org.openprovenance.prov.model.Attribute.AttributeKind;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.LiteralConstructor;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvSerialiser;
import org.openprovenance.prov.model.ProvUtilities.BuildFlag;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.QualifiedNameUtils;
import org.openprovenance.prov.model.exception.QualifiedNameException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** A stateless factory for PROV objects. */

public class ProvFactory extends org.openprovenance.prov.model.ProvFactory implements LiteralConstructor { 
    
    final private QualifiedNameUtils qnU=new QualifiedNameUtils();

    static public DocumentBuilder builder;

    public static final String DEFAULT_NS = "_";

    private final static ProvFactory oFactory = new ProvFactory();

    public static final String packageList = "org.openprovenance.prov.sql";

    static {
	initBuilder();
	initializeTables();
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
    public org.openprovenance.prov.model.QualifiedName newQualifiedName(String namespace,
									String local,
									String prefix) {
	return newQualifiedName(namespace,local,prefix, BuildFlag.STRICT);
    }
    @Override
    public org.openprovenance.prov.model.QualifiedName newQualifiedName(String namespace,
									String local,
									String prefix,
									BuildFlag flag) {
	if (BuildFlag.NOCHEK.equals(flag) || qnU.patternExactMatch(local)) {
	    return new org.openprovenance.prov.sql.QualifiedName(namespace, local, prefix);
	} else {
	    switch(flag){
	    case STRICT:
		throw new QualifiedNameException("PROV-N QualifiedName: local name not valid " + local);
	    case WARN:
		System.out.println("ProvToolbox Warning: PROV-N QualifiedName: local name not valid " + local);
	    default:
		return new org.openprovenance.prov.sql.QualifiedName(namespace, local, prefix);
	    
	    }
	}
    }	

    public Namespace newNamespace(Namespace ns) {
    	return new org.openprovenance.prov.sql.Namespace(ns);
    }
    
    public Namespace newNamespace() {
    	return new org.openprovenance.prov.sql.Namespace();
    }
    



    static public void initializeTables () {
          ProvUtilities.putFields(Activity.class, new String[] { "Id", "StartTime", "EndTime", "Other" });
          ProvUtilities.putFields(Entity.class, new String[] { "Id", "Other" });
          ProvUtilities.putFields(Agent.class, new String[] { "Id", "Other" });

          ProvUtilities.putFields(Used.class, new String[] { "Id", "Activity", "Entity",
  					      "Time", "Other" });
          ProvUtilities.putFields(WasGeneratedBy.class, new String[] { "Id", "Entity",
  							"Activity", "Time",
  							"Other" });
          ProvUtilities.putFields(WasInvalidatedBy.class, new String[] { "Id", "Entity",
  							  "Activity", "Time",
  							  "Other" });
          ProvUtilities.putFields(WasStartedBy.class, new String[] { "Id", "Activity",
  						      "Trigger", "Starter",
  						      "Time", "Other" });
          // 0 , 1 , 2 , 3 , 4 , 5
          // length=6
          // firstTimeIndex=4
          // last index=5
          ProvUtilities.putFields(WasEndedBy.class, new String[] { "Id", "Activity",
  						    "Trigger", "Ender", "Time",
  						    "Other" });
          ProvUtilities.putFields(WasInformedBy.class, new String[] { "Id", "Informed", "Informant",
  						       "Other" });
          ProvUtilities.putFields(WasDerivedFrom.class, new String[] { "Id",
  							"GeneratedEntity",
  							"UsedEntity",
  							"Activity",
  							"Generation", "Usage",
  							"Other" });
          ProvUtilities.putFields(WasInfluencedBy.class, new String[] { "Id", "Influencee",
  							 "Influencer", "Others" });
          ProvUtilities.putFields(WasAttributedTo.class, new String[] { "Id", "Entity",
  							 "Agent", "Other" });
          ProvUtilities.putFields(WasAssociatedWith.class, new String[] { "Id", "Activity",
  							   "Agent", "Plan",
  							   "Other" });
          ProvUtilities.putFields(ActedOnBehalfOf.class, new String[] { "Id", "Delegate",
  							 "Responsible",
  							 "Activity", "Others" });
          ProvUtilities.putFields(SpecializationOf.class, new String[] { "SpecificEntity",
  							  "GeneralEntity" });
          ProvUtilities.putFields(AlternateOf.class, new String[] { "Alternate2",
  						     "Alternate2" });
          ProvUtilities.putFields(HadMember.class, new String[] { "Collection",
  						   "Entity" });

  	// never use the accessor id for Mention, since it is not defined.
  	// However, this allows iterations over this data structure to be performed
  	//  like others.

          ProvUtilities.putFields(MentionOf.class, new String[] { "Id", 
  						   "SpecificEntity",
  						   "GeneralEntity",
  						   "Bundle" });
          

          ProvUtilities.putTypes(Entity.class, new Class[] { QualifiedName.class, 
  					      Object.class });
          ProvUtilities.putTypes(Agent.class, new Class[] { QualifiedName.class, 
  					     Object.class });
          ProvUtilities.putTypes(Activity.class, new Class[] { QualifiedName.class, 
  						XMLGregorianCalendar.class,
  						XMLGregorianCalendar.class,
  						Object.class });
          ProvUtilities.putTypes(Used.class, new Class[] { QualifiedName.class, QualifiedName.class,
  					    QualifiedName.class,
  					    XMLGregorianCalendar.class,
  					    Object.class });
          ProvUtilities.putTypes(WasGeneratedBy.class,
                    new Class[] { QualifiedName.class, QualifiedName.class,
  				QualifiedName.class, XMLGregorianCalendar.class,
  				Object.class });
          ProvUtilities.putTypes(WasInvalidatedBy.class,
                    new Class[] { QualifiedName.class, QualifiedName.class,
  				QualifiedName.class, XMLGregorianCalendar.class,
  				Object.class });
          ProvUtilities.putTypes(WasStartedBy.class, new Class[] { QualifiedName.class,
  						    QualifiedName.class,
  						    QualifiedName.class,
  						    QualifiedName.class,
  						    XMLGregorianCalendar.class,
  						    Object.class });
          ProvUtilities.putTypes(WasEndedBy.class, new Class[] { QualifiedName.class,
  						  QualifiedName.class,
  						  QualifiedName.class,
  						  QualifiedName.class,
  						  XMLGregorianCalendar.class,
  						  Object.class });
          ProvUtilities.putTypes(WasInformedBy.class, new Class[] { QualifiedName.class,
  						     QualifiedName.class,
  						     QualifiedName.class,
  						     Object.class });
          ProvUtilities.putTypes(WasDerivedFrom.class, new Class[] { QualifiedName.class,
  						      QualifiedName.class,
  						      QualifiedName.class,
  						      QualifiedName.class,
  						      QualifiedName.class,
  						      QualifiedName.class,
  						      Object.class });
          ProvUtilities.putTypes(WasInfluencedBy.class, new Class[] { QualifiedName.class,
  						       QualifiedName.class,
  						       QualifiedName.class,
  						       Object.class });
          ProvUtilities.putTypes(WasAttributedTo.class, new Class[] { QualifiedName.class,
  						       QualifiedName.class,
  						       QualifiedName.class,
  						       Object.class });
          ProvUtilities.putTypes(WasAssociatedWith.class, new Class[] { QualifiedName.class,
  							 QualifiedName.class,
  							 QualifiedName.class,
  							 QualifiedName.class,
  							 Object.class });
          ProvUtilities.putTypes(ActedOnBehalfOf.class, new Class[] { QualifiedName.class,
  						       QualifiedName.class,
  						       QualifiedName.class,
  						       QualifiedName.class,
  						       Object.class });
          ProvUtilities.putTypes(SpecializationOf.class, new Class[] { QualifiedName.class,
  							QualifiedName.class });
          ProvUtilities.putTypes(MentionOf.class, new Class[] { QualifiedName.class,
  						 QualifiedName.class,
  						 QualifiedName.class,
  						 QualifiedName.class });
          ProvUtilities.putTypes(AlternateOf.class, new Class[] { QualifiedName.class,
  						   QualifiedName.class });
          
          ProvUtilities.putTypes(HadMember.class, new Class[] { QualifiedName.class,
  						 Object.class });
      }

	@Override
	public ProvSerialiser getSerializer() throws JAXBException {
		return new org.openprovenance.prov.sql.ProvSerialiser();
	}
    


}
