package org.openprovenance.prov.xml;

import java.util.Collection;

import javax.xml.namespace.QName;

/* Not usefully named class, to be removed ultimately hopefully */

public class Helper  {
    
 
    
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
