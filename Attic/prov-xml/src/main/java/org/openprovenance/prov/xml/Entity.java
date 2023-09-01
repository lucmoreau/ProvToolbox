package org.openprovenance.prov.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.xml.builder.JAXBEqualsBuilder;
import org.openprovenance.prov.xml.builder.JAXBHashCodeBuilder;
import org.openprovenance.prov.xml.builder.JAXBToStringBuilder;
import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.StatementOrBundle;


/**
 * <p>Java class for Entity complex type.
 * 
 * <p>An entity is a physical, digital, conceptual, or other kind of thing with some fixed aspects; entities may be real or imaginary. 
 * (<a href="http://www.w3.org/TR/prov-dm/#concept-entity">PROV-DM Definition for Entity</a>).
 *  
 * 
 * <p>The following schema fragment specifies the expected content contained within this class. 
 * <pre>
 * &lt;complexType name="Entity"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}location" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}value" minOccurs="0"/&gt;
 *         &lt;any processContents='skip' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * <p>Due to JAXB restrictions, it was not possible to parse attributes that had a QName as value. 
 * To avoid this problem, all attributes (except labels) are processed as &lt;any/&gt;
 * The Entity bean still offers the convenience accessors getLocation, getType, getValue, getOthers,
 * while automatically maintaining a sorted list of attributes to be compatible with prov-xml schema.
 *
 *  <pre>
 * &lt;complexType name="Entity"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;any processContents='skip' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-entity">PROV-DM Entity</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Entity">PROV-O Entity</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-Entity">PROV-N Entity</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Entity">PROV-XML Entity</a>
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Entity", propOrder = {
    "label",
   // "location",
    //"type",
    //"value",
   // "any"
    "all"
})
@XmlSeeAlso({
    BundleEntity.class,
    Collection.class,
    Plan.class
})
public class Entity implements Equals, HashCode, ToString, org.openprovenance.prov.model.Entity, HasAllAttributes
{

    @XmlElement(type = org.openprovenance.prov.xml.InternationalizedString.class)
    protected List<org.openprovenance.prov.model.LangString> label;

    transient protected List<org.openprovenance.prov.model.Location> location;
    transient protected List<org.openprovenance.prov.model.Type> type;
    transient protected org.openprovenance.prov.model.Value value;
    transient protected List<org.openprovenance.prov.model.Other> others; 

    @XmlAnyElement
    protected List<Attribute> all;

    @XmlAttribute(name = "id", namespace = "http://www.w3.org/ns/prov#")
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(QualifiedNameAdapter.class)
    protected org.openprovenance.prov.model.QualifiedName id;


    /**
     * Gets the value of the label property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the label property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLabel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openprovenance.prov.xml.InternationalizedString }
     * 
     * 
     */
    public List<org.openprovenance.prov.model.LangString> getLabel() {
        if (label == null) {
            label = new ArrayList<org.openprovenance.prov.model.LangString>();
        }
        return this.label;
    }

    /**
     * Gets the value of the location property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the location property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openprovenance.prov.xml.Location }
     * 
     * 
     */

    public List<org.openprovenance.prov.model.Location> getLocation() {
        if (location == null) {
            location=AttributeList.populateKnownAttributes(this,all, org.openprovenance.prov.model.Location.class);
        } 
        return this.location;
    }



    
    
    /**
     * Gets the value of the type property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the type property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openprovenance.prov.xml.Type }
     * 
     * 
     */


    public List<org.openprovenance.prov.model.Type> getType() {
        if (type == null) {
            type=AttributeList.populateKnownAttributes(this,all, org.openprovenance.prov.model.Type.class);
        } 
        return this.type;
    }


 
    
    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.xml.Value }
     *     
     */
    public org.openprovenance.prov.model.Value getValue() {
	if (value==null) {
	    if (all!=null) { // TODO: inefficient, I search this list every time getValue is called, though there may be no value
        	for (Attribute attr: all) {
        	    if (attr instanceof org.openprovenance.prov.model.Value) {
        	        value=(Value)attr;
        	    }
        	}
            }
	}
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.xml.Value }
     *     
     */
    public void setValue(org.openprovenance.prov.model.Value value) {
	if (value!=null) {
	    this.value = value;
	    getAllAttributes().add((org.openprovenance.prov.model.Attribute)value);	//FIXME: should replace previous value!
	}
    }

    public List<org.openprovenance.prov.model.Other> getOther() {
        if (others == null) {
            others=AttributeList.populateKnownAttributes(this,all, org.openprovenance.prov.model.Other.class);
        } 
        return this.others;
    }
    
    
    /** Gets the List of all attributes
     * @see org.openprovenance.prov.xml.HasAllAttributes#getAllAttributes()
     */
    @Override
    public List<Attribute> getAllAttributes() {
        if (all == null) {
            all = new SortedAttributeList<Attribute>();
        }
        return this.all;
    }
 
    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.model.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.model.QualifiedName }
     *     
     */
    public void setId(org.openprovenance.prov.model.QualifiedName value) {
        this.id = value;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Entity)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Entity that = ((Entity) object);
        equalsBuilder.append(this.getLabel(), that.getLabel());
        equalsBuilder.append(this.getLocation(), that.getLocation());
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getValue(), that.getValue());
        equalsBuilder.append(this.getOther(), that.getOther());
        equalsBuilder.append(this.getId(), that.getId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Entity)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getLabel());
        hashCodeBuilder.append(this.getLocation());
        hashCodeBuilder.append(this.getType());
        hashCodeBuilder.append(this.getValue());
        hashCodeBuilder.append(this.getOther());
        hashCodeBuilder.append(this.getId());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<org.openprovenance.prov.model.LangString> theLabel;
            theLabel = this.getLabel();
            toStringBuilder.append("label", theLabel);
        }
        {
            List<org.openprovenance.prov.model.Location> theLocation;
            theLocation = this.getLocation();
            toStringBuilder.append("location", theLocation);
        }
        {
            List<org.openprovenance.prov.model.Type> theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
        }
        {
            org.openprovenance.prov.model.Value theValue;
            theValue = this.getValue();
            toStringBuilder.append("value", theValue);
        }
  
        {
            List<org.openprovenance.prov.model.Other> theOthers;
            theOthers = this.getOther();
            toStringBuilder.append("others", theOthers);
        }
        {
            org.openprovenance.prov.model.QualifiedName theId;
            theId = this.getId();
            toStringBuilder.append("id", theId);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }
    
    public Kind getKind() {
	return StatementOrBundle.Kind.PROV_ENTITY;
    }

}
