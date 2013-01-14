package org.openprovenance.prov.xml;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.openprovenance.prov.sql.InternationalizedString;
import java.util.Collection;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

@XmlJavaTypeAdapter(AnyAdapter.class)
@javax.persistence.Entity(name = "Attribute")
@Table(name = "ATTRIBUTE")
@Inheritance(strategy = InheritanceType.JOINED)
public class Attribute {
    
    public static QName provQName(String s) {
	return new QName(NamespacePrefixMapper.PROV_NS, s, NamespacePrefixMapper.PROV_PREFIX);
    }
    
    public static QName PROV_TYPE_QNAME=provQName("type"); 
    public static QName PROV_LABEL_QNAME=provQName("label"); 
    public static QName PROV_ROLE_QNAME=provQName("role");
    public static QName PROV_LOCATION_QNAME=provQName("location");
    public static QName PROV_VALUE_QNAME=provQName("value");
    
    enum AttributeKind {
	PROV_TYPE,
	PROV_LABEL,
	PROV_ROLE,
	PROV_LOCATION,
	PROV_VALUE,
	OTHER
    }

    final private QName elementName;
    final private Object val;
    final private QName xsdType;
    private AttributeKind kind;
    
    
    public Attribute(QName elementName, Object val, QName xsdType) {
 	if (elementName==null) throw new IllegalArgumentException("Attribute elementName is null ");
 	this.val = val;
 	this.elementName = elementName;
 	this.xsdType = xsdType;
 	this.kind=getAttributeKind(elementName);
     }

   
    /** Short cut, for PROV attribute, raises exception otherwise. */
    
    public Attribute(AttributeKind kind, Object val, QName xsdType) {
 	this.val = val;
 	this.elementName = getQName(kind);
 	if (this.elementName==null) throw new IllegalArgumentException("Attribute kind is not PROV " + kind);
 	this.xsdType = xsdType;
 	this.kind=kind;
     }

    @Basic
    @Column(name = "QNAME")
    public QName getQName(AttributeKind kind) {
	switch (kind) {
	case  PROV_TYPE: return PROV_TYPE_QNAME;
	case  PROV_LABEL: return PROV_LABEL_QNAME;
	case  PROV_VALUE: return PROV_VALUE_QNAME;
	case  PROV_LOCATION: return PROV_LOCATION_QNAME;
	case  PROV_ROLE: return PROV_ROLE_QNAME;
	case OTHER:
        default: 
		return null;
	}
    }
    
    @Transient
    public AttributeKind getAttributeKind(QName q) {
	if (q.equals(PROV_TYPE_QNAME)) return AttributeKind.PROV_TYPE;
	if (q.equals(PROV_LABEL_QNAME)) return AttributeKind.PROV_LABEL;
	if (q.equals(PROV_VALUE_QNAME)) return AttributeKind.PROV_VALUE;
	if (q.equals(PROV_LOCATION_QNAME)) return AttributeKind.PROV_LOCATION;
	if (q.equals(PROV_ROLE_QNAME)) return AttributeKind.PROV_ROLE;
	return AttributeKind.OTHER;
    }


    public boolean equals(Object o) {
	if (o instanceof Attribute) {
	    Attribute other = (Attribute) o;
	    return elementName.getLocalPart()
			      .equals(other.elementName.getLocalPart())
		    && elementName.getNamespaceURI()
				  .equals(other.elementName.getNamespaceURI())
		    && val.equals(other.val) && xsdType.equals(other.xsdType);

	}
	return false;
    }

    @Transient
    public QName getElementName() {
	return elementName;
    }
    
    @Transient
    public AttributeKind getKind() {
	return kind;
    }

    @Transient
    public Object getValue() {
	return val;
    }

    @Transient
    public QName getXsdType() {
	return xsdType;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	if (val != null)
	    hash ^= val.hashCode();
	if (elementName != null)
	    hash ^= elementName.toString().hashCode();
	if (xsdType != null)
	    hash ^= xsdType.hashCode();
	return hash;
    }

    public String toString() { 
	return toNotationString();
	//return toStringDebug();
    }

    public String toStringDebug() { 
	return "[" + elementName + " " + val + " " + xsdType + "]";
    }

    /** Method replicated from ProvFactory. */
    public String qnameToString(QName qname) {
	return ((qname.getPrefix().equals("")) ? "" : (qname.getPrefix() + ":"))
		+ qname.getLocalPart();
    }
    
    /** A method to generate the prov-n representation of an attribute  ex:attr="value" %% xsd:type */
    
    public String toNotationString() {
	if (val instanceof InternationalizedString) {
	    InternationalizedString istring = (InternationalizedString) val;
	    return qnameToString(elementName)
		    + " = \"" + istring.getValue() + 
		    ((istring.getLang()==null) ? "\"" : "\"@" + istring.getLang())
		    + " %% " + qnameToString(xsdType);
	} else if (val instanceof QName) {
	    QName qn = (QName) val;	    
	    return qnameToString(elementName)
		    + " = '" + qnameToString(qn) + "'";
	} else {
	    return qnameToString(elementName)
		   + " = \"" + val + "\" %% " + qnameToString(xsdType);
	}
    }
    
    static public boolean hasType(QName type, Collection<Attribute> attributes) {
    	for (Attribute attribute: attributes) {
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

    Long hjid;

    @Id
    @Column(name = "HJID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getHjid() {
        return hjid;
    }

    public void setHjid(Long value) {
        this.hjid = value;
    }

}
