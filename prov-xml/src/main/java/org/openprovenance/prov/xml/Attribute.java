package org.openprovenance.prov.xml;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

@XmlJavaTypeAdapter(AnyAdapter.class)

public class Attribute {

    final private Object val;
    final private QName elementName;
    final private String xsdType;

    public Attribute(QName elementName, Object val, String xsdType) {
      this.val=val;
      this.elementName=elementName;
      this.xsdType=xsdType;
    }

    public Object getValue() {
        return val;
    }
    public QName getElementName() {
	return elementName;
    }
    
    public String getXsdType() {
	return xsdType;
    }
    public boolean equals(Object o) {
	if (o instanceof Attribute) {
	    Attribute other=(Attribute)o;
	    return elementName.getLocalPart().equals(other.elementName.getLocalPart())
		    && elementName.getNamespaceURI().equals(other.elementName.getNamespaceURI())
		    && val.equals(other.val)
		    && xsdType.equals(other.xsdType);
		    
	}
	return false;
    }

    public String toString() {
	return "[" + elementName.getPrefix() + ":" + elementName.getLocalPart() + " = \"" + val + "\" %% " + xsdType + "]";
    }

}
