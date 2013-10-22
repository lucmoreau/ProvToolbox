package org.openprovenance.prov.model;

import javax.xml.namespace.QName;

public interface OtherAttribute extends TypedValue {
    public QName getElementName();
    
    public void setElementName(QName elementName) ;

}
