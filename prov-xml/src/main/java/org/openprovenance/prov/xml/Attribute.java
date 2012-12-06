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
        if (val instanceof InternationalizedString) {
            InternationalizedString istring=(InternationalizedString) val;
            return elementName.getPrefix() + ":" + elementName.getLocalPart() + " = \"" + istring.getValue() + "\"@" + istring.getLang() + " %% " + xsdType;
        } else if (val instanceof QName) {
                QName qn=(QName) val;
                String qnAsString;
                if ((qn.getPrefix()==null) || (qn.getPrefix().equals(""))) {
                    qnAsString=qn.getLocalPart();
                } else {
                    qnAsString=qn.getPrefix()+ ":" + qn.getLocalPart();
                }
                return elementName.getPrefix() + ":" + elementName.getLocalPart() + " = '" + qnAsString + "'";
	} else {
	    return elementName.getPrefix() + ":" + elementName.getLocalPart() + " = \"" + val + "\" %% " + xsdType;
	}
    }

	@Override
	public int hashCode() {
		int hash = 0;
		if (val != null) hash ^= val.hashCode();
		if (elementName != null) hash ^= elementName.toString().hashCode();
		if (xsdType != null) hash ^= xsdType.hashCode();
		return hash;
	}

}
