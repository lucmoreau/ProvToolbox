package org.openprovenance.prov.model;

import javax.xml.namespace.QName;

public interface Other extends TypedValue {
    public QName getElementName();
    
    public void setElementName(QName elementName) ;

}
