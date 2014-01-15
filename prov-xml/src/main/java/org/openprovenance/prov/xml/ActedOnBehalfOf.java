package org.openprovenance.prov.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.xml.builder.JAXBEqualsBuilder;
import org.openprovenance.prov.xml.builder.JAXBHashCodeBuilder;
import org.openprovenance.prov.xml.builder.JAXBToStringBuilder;
import org.openprovenance.prov.model.Attribute;


/**
 * <p>Java class for Delegation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Delegation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="delegate" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="responsible" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Delegation", propOrder = {
    "delegate",
    "responsible",
    "activity",
    "label",
    //"type",
    //"any"
    "all"
})
public class ActedOnBehalfOf implements Equals, HashCode, ToString, org.openprovenance.prov.model.ActedOnBehalfOf, HasAllAttributes
{

    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(required = true, type = org.openprovenance.prov.xml.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName delegate;
    
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(required = true, type = org.openprovenance.prov.xml.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName responsible;
    
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(required = true, type = org.openprovenance.prov.xml.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName activity;
    
    @XmlElement(type = org.openprovenance.prov.xml.InternationalizedString.class)
    protected List<org.openprovenance.prov.model.LangString> label;

    transient protected List<org.openprovenance.prov.model.Type> type;
    transient protected List<org.openprovenance.prov.model.Other> others;
    
    @XmlAnyElement
    protected List<Attribute> all;
    

    @XmlAttribute(name = "id", namespace = "http://www.w3.org/ns/prov#")
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(QualifiedNameAdapter.class)
    protected org.openprovenance.prov.model.QualifiedName id;

    /**
     * Gets the value of the delegate property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getDelegate() {
        return delegate;
    }

    /**
     * Sets the value of the delegate property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public void setDelegate(org.openprovenance.prov.model.QualifiedName value) {
        this.delegate = value;
    }

    /**
     * Gets the value of the responsible property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getResponsible() {
        return responsible;
    }

    /**
     * Sets the value of the responsible property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public void setResponsible(org.openprovenance.prov.model.QualifiedName value) {
        this.responsible = value;
    }

    /**
     * Gets the value of the activity property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getActivity() {
        return activity;
    }

    /**
     * Sets the value of the activity property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public void setActivity(org.openprovenance.prov.model.QualifiedName value) {
        this.activity = value;
    }

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

    
    public List<org.openprovenance.prov.model.Other> getOther() {
	if (others == null) {
	    others=AttributeList.populateKnownAttributes(this,all, org.openprovenance.prov.model.Other.class);
	} 
	return this.others;
    }
     
    
    /** Gets the List of all attributes
     * @see org.openprovenance.prov.xml.HasAllAttributes#getAll()
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
        if (!(object instanceof ActedOnBehalfOf)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final ActedOnBehalfOf that = ((ActedOnBehalfOf) object);
        equalsBuilder.append(this.getDelegate(), that.getDelegate());
        equalsBuilder.append(this.getResponsible(), that.getResponsible());
        equalsBuilder.append(this.getActivity(), that.getActivity());
        equalsBuilder.append(this.getLabel(), that.getLabel());
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getOther(), that.getOther());
        equalsBuilder.append(this.getId(), that.getId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof ActedOnBehalfOf)) {
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
        hashCodeBuilder.append(this.getDelegate());
        hashCodeBuilder.append(this.getResponsible());
        hashCodeBuilder.append(this.getActivity());
        hashCodeBuilder.append(this.getLabel());
        hashCodeBuilder.append(this.getType());
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
            org.openprovenance.prov.model.QualifiedName theDelegate;
            theDelegate = this.getDelegate();
            toStringBuilder.append("delegate", theDelegate);
        }
        {
            org.openprovenance.prov.model.QualifiedName theResponsible;
            theResponsible = this.getResponsible();
            toStringBuilder.append("responsible", theResponsible);
        }
        {
            org.openprovenance.prov.model.QualifiedName theActivity;
            theActivity = this.getActivity();
            toStringBuilder.append("activity", theActivity);
        }
        {
            List<org.openprovenance.prov.model.LangString> theLabel;
            theLabel = this.getLabel();
            toStringBuilder.append("label", theLabel);
        }
        {
            List<org.openprovenance.prov.model.Type> theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
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
 	return org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DELEGATION;
     }


}
