package org.openprovenance.prov.xml;

import java.util.Collection;

import javax.xml.namespace.QName;

/* Not usefully named class, to be removed ultimately hopefully */

public class Helper  {
    
    public static QName provQName(String s) {
	return new QName(NamespacePrefixMapper.PROV_NS, s, NamespacePrefixMapper.PROV_PREFIX);
    }
    
    public static final QName PROV_TYPE_QNAME=provQName("type"); 
    public static final QName PROV_LABEL_QNAME=provQName("label"); 
    public static final QName PROV_ROLE_QNAME=provQName("role");
    public static final QName PROV_LOCATION_QNAME=provQName("location");
    public static final QName PROV_VALUE_QNAME=provQName("value");
    
    /** Method replicated from ProvFactory. */
    static public String qnameToString(QName qname) {
	return ((qname.getPrefix().equals("")) ? "" : (qname.getPrefix() + ":"))
		+ qname.getLocalPart();
    }

    //TODO: move this code to ValueConverter
    public static String valueToNotationString(Object val, QName xsdType) {
 	if (val instanceof InternationalizedString) {
 	    InternationalizedString istring = (InternationalizedString) val;
 	    return "\"" + istring.getValue() + 
 		    ((istring.getLang()==null) ? "\"" : "\"@" + istring.getLang())
 		    + " %% " + qnameToString(xsdType);
 	} else if (val instanceof QName) {
 	    QName qn = (QName) val;	    
 	    return "'" + qnameToString(qn) + "'";
 	} else if (val instanceof String) {
	    String s=(String)val;
	    if (s.contains("\n")) {
		// return "\"\"\"" + val + "\"\"\" %% " + qnameToString(xsdType);
		return "\"\"\"" + val + "\"\"\"" ;
	    } else {
		return "\"" + val + "\" %% " + qnameToString(xsdType);
	    }
 	} else {
	    // We should never be here!
 	    return "\"" + val + "\" %% " + qnameToString(xsdType);
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

   
}
