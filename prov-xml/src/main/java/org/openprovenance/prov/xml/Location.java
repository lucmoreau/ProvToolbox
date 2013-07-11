package org.openprovenance.prov.xml;


import java.util.Collection;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


@XmlJavaTypeAdapter(LocationAdapter.class)
public class Location {
    
    private Object val;
    private QName xsdType;

    
    public Location() {
    }
    
    public Location(Object val, QName xsdType) {
 	this.val = val;
 	this.xsdType = xsdType;
     }

   
    public boolean equals(Object o) {
	if (o instanceof Location) {
	    Location other = (Location) o;
	    return val.equals(other.val) && xsdType.equals(other.xsdType);

	}
	return false;
    }


    public QName getXsdType() {
        return xsdType;
    }



    public void setXsdType(QName xsdType) {
        this.xsdType=xsdType;
    }


    public Object getValue() {
        return val;
    }
    
    public void setValue(Object val) {
        this.val=val;
    }
    

    @Override
    public int hashCode() {
	int hash = 0;
	if (val != null)
	    hash ^= val.hashCode();
	if (xsdType != null)
	    hash ^= xsdType.hashCode();
	return hash;
    }


    public String toStringDebug() { 
	return "[loc " + val + " " + xsdType + "]";
    }

    public String toString() {
        return toStringDebug();
    }
    

}
