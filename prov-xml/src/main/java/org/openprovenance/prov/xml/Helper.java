package org.openprovenance.prov.xml;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.openprovenance.prov.model.Namespace;

/* Not usefully named class, to be removed ultimately hopefully */

public class Helper  {
    /*
    public static QName provQName(String s) {
	return new QName(NamespacePrefixMapper.PROV_NS, s, NamespacePrefixMapper.PROV_PREFIX);
    }
    
    public static final QName PROV_TYPE_QNAME=provQName("type"); 
    public static final QName PROV_LABEL_QNAME=provQName("label"); 
    public static final QName PROV_ROLE_QNAME=provQName("role");
    public static final QName PROV_LOCATION_QNAME=provQName("location");
    public static final QName PROV_VALUE_QNAME=provQName("value");
    public static final QName PROV_KEY_QNAME=provQName("key");
    */
    
    /*
    static public String qnameToStringDELETE(QName qname) {
	Namespace ns=Namespace.getThreadNamespace();
	return ns.qnameToString(qname);
    }
    
    */
    
    public static String valueToNotationString(org.openprovenance.prov.model.Key key) {
	return valueToNotationString(key.getValue(), key.getType());
    }

    
    public static String escape (String s) {
  	return s.replace("\"", "\\\"");
      }
    
    public static String unescape (String s) {
  	return s.replace("\\\"","\"");
      }
    
    
    //TODO: move this code to ValueConverter
    //TODO: what else should be escaped?
    public static String valueToNotationString(Object val, org.openprovenance.prov.model.QualifiedName xsdType) {
 	if (val instanceof InternationalizedString) {
 	    InternationalizedString istring = (InternationalizedString) val;
 	    return "\"" + istring.getValue() + 
 		    ((istring.getLang()==null) ? "\"" : "\"@" + istring.getLang())
 		    + " %% " + Namespace.qualifiedNameToStringWithNamespace(xsdType);
 	} else if (val instanceof QualifiedName) {
 	    QualifiedName qn = (QualifiedName) val;	    
 	    return "'" + Namespace.qualifiedNameToStringWithNamespace(qn) + "'";
 	} else if (val instanceof String) {
	    String s=(String)val;
	    if (s.contains("\n")) {
		// return "\"\"\"" + val + "\"\"\" %% " + qnameToString(xsdType);
		return "\"\"\"" + escape(s) + "\"\"\"" ;
	    } else {
		return "\"" + escape(s) + "\" %% " + Namespace.qualifiedNameToStringWithNamespace(xsdType);
	    }
 	} else {
	    // We should never be here!
 	    return "\"" + val + "\" %% " + Namespace.qualifiedNameToStringWithNamespace(xsdType);
	}
     }

    
    static public boolean hasType(QName type, Collection<org.openprovenance.prov.model.Attribute> attributes) {
    	for (org.openprovenance.prov.model.Attribute attribute: attributes) {
    		switch (attribute.getKind()) {
    			case PROV_TYPE :
    				if (attribute.getValue().equals(type)) {
    					return true;
    				}
					break;			
				default :
					break;
    			
    		}
    	}
    	return false;
    		
    }

    static public boolean hasType(org.openprovenance.prov.model.QualifiedName type, Collection<org.openprovenance.prov.model.Attribute> attributes) {
    	for (org.openprovenance.prov.model.Attribute attribute: attributes) {
    		switch (attribute.getKind()) {
    			case PROV_TYPE :
    				if (attribute.getValue().equals(type)) {
    					return true;
    				}
					break;			
				default :
					break;
    			
    		}
    	}
    	return false;
    		
    }

   
}
