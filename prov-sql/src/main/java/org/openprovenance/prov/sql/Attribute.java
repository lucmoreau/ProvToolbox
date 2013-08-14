package org.openprovenance.prov.sql;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.openprovenance.prov.sql.AValue;
import org.openprovenance.prov.sql.InternationalizedString;
import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ValueConverter;

import java.util.Collection;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

@XmlJavaTypeAdapter(org.openprovenance.prov.sql.AnyAdapter.class)
@javax.persistence.Entity(name = "Attribute")
@Table(name = "ATTRIBUTE")
@Inheritance(strategy = InheritanceType.JOINED)
public class Attribute implements org.openprovenance.prov.model.Attribute {
    
    public static QName provQName(String s) {
	return new QName(NamespacePrefixMapper.PROV_NS, s, NamespacePrefixMapper.PROV_PREFIX);
    }
    
    public static QName PROV_TYPE_QNAME=provQName("type"); 
    public static QName PROV_LABEL_QNAME=provQName("label"); 
    public static QName PROV_ROLE_QNAME=provQName("role");
    public static QName PROV_LOCATION_QNAME=provQName("location");
    public static QName PROV_VALUE_QNAME=provQName("value");

    private QName elementName;
    private Object val;
    private QName xsdType;
    private AttributeKind kind;
    
    public Attribute() {
    }
    
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
    

    public AttributeKind getAttributeKind(QName q) {
	if (q.equals(PROV_TYPE_QNAME)) return AttributeKind.PROV_TYPE;
	if (q.equals(PROV_LABEL_QNAME)) return AttributeKind.PROV_LABEL;
	if (q.equals(PROV_VALUE_QNAME)) return AttributeKind.PROV_VALUE;
	if (q.equals(PROV_LOCATION_QNAME)) return AttributeKind.PROV_LOCATION;
	if (q.equals(PROV_ROLE_QNAME)) return AttributeKind.PROV_ROLE;
	return AttributeKind.OTHER;
    }

    @Transient
    public AttributeKind getKind() {
	return kind;
    }

    @Basic
    @Column(name = "KIND")
    public String getAttributeKindItem () {
	return "" + kind;
    }
    public void setAttributeKindItem (String k) {
	//System.out.println("---> setAttributeKindItem() reading " + k);
	kind=AttributeKind.OTHER;
    }
    



    public boolean equals(Object o) {
	//System.out.println("calling equal on: me: " + this + " and other: " + o);
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

    @Basic
    @Column(name = "ELEMENTNAME") 
    public String getElementNameItem() {
	if (elementName==null) return null;
	return QNameToString(elementName);
    }

    public void setElementNameItem(String name) {
	//System.out.println("---> setElementNameItem() reading " + name);
	elementName=stringToQName(name);
	//System.out.println(" ---> setElementNameItem() got " + elementName);
    }

    public static String QNameToString(QName element) {
	if (element==null) return null;
	return element.getPrefix()
	    + " "
	    + element.getNamespaceURI()
	    + " "
	    + element.getLocalPart();
    }
    public static QName stringToQName(String s) {
	String []parts=s.split(" ");
	return new QName( parts[1], // namespaceURI
			       parts[2], // localPart
			       parts[0]  // prfix
			       );
    }
    

    @Transient
    public QName getXsdType() {
	return xsdType;
    }

    //    @Basic
    //    @Column(name = "XSDTYPE")
    public String getXsdTypeItem() {    
	if (xsdType==null) return null;
	return QNameToString(xsdType);
    }

    public void setXsdTypeItem(String name) {
	//System.out.println("---> setXsdTypeIterm() reading " + name);
	xsdType=stringToQName(name);
	//System.out.println(" ---> setXsdTypeIterm() got " + xsdType);
    }

    

    @Transient
    public Object getValue() {
	return val;
    }
    
    ValueConverter vc=new ValueConverter(ProvFactory.getFactory());
    

    /*
    //
    @Basic
    //    @Column(name = "VALUE")
    public String getValueItem() {
	return "" + this.getValue();
    }
    public void setValueItem(String value) {
	System.out.println("---> setValueString() reading " + value);
	if (getXsdType()==null) {
	    val=value;
	    return;
	}
	val=vc.convertToJava(getXsdType(), value);  //LUC: have we deserialized this value yet?
    }

    */
	
    AValue avalue;
    
    /**
     * Gets the value of the test property.
     * 
     * @return
     *     possible object is
     *     {@link AValue }
     *     
     */
    //@ManyToOne(targetEntity = AValue.class, cascade = CascadeType.ALL)
    //@JoinColumn(name = "VALUE_ATTRIBUTE_HJID")
    @Embedded
    public AValue getValueItem() {
        //System.out.println("---> getValueItem() reading " + val);
        if (avalue==null) avalue=javaToValue(val);
        return avalue;
    }

    public static AValue javaToValue(Object val2) {
        //System.out.println("---> setValueItem() for " + val2);

        AValue res=new AValue();
        if (val2 instanceof String) {
            res.setString((String) val2);
        }
        if (val2 instanceof Integer) {
            res.setInt((Integer) val2);
        }
        return res;
    }

    /**
     * Sets the value of the test property.
     * 
     * @param value
     *     allowed object is
     *     {@link AValue }
     *     
     */
    public void setValueItem(AValue value) {
        this.avalue=value;
        this.val = valueToJava(value);
    }


    public Object valueToJava(AValue value) {
        Object tmp;
        tmp=value.getString();
        if (tmp!=null) return tmp;
        tmp=value.getInt();
        if (tmp!=null) return tmp;
        return null;
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
