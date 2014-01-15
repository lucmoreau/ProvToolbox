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
 * <p>Java class for Derivation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Derivation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="generatedEntity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="usedEntity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
 *         &lt;element name="generation" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
 *         &lt;element name="usage" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
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
@XmlType(name = "Derivation", propOrder = {
    "generatedEntity",
    "usedEntity",
    "activity",
    "generation",
    "usage",
    "label",
//    "type",
//    "any"
    "all"
})
@XmlSeeAlso({
    Revision.class,
    PrimarySource.class,
    Quotation.class
})
public class WasDerivedFrom implements Equals, HashCode, ToString, org.openprovenance.prov.model.WasDerivedFrom, HasAllAttributes
{

    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(required = true, type = org.openprovenance.prov.xml.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName generatedEntity;

    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(required = true, type = org.openprovenance.prov.xml.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName usedEntity;

    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(type = org.openprovenance.prov.xml.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName activity;

    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(type = org.openprovenance.prov.xml.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName generation;

    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(type = org.openprovenance.prov.xml.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName usage;

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
     * Gets the value of the generatedEntity property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getGeneratedEntity() {
        return generatedEntity;
    }

    /**
     * Sets the value of the generatedEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public void setGeneratedEntity(org.openprovenance.prov.model.QualifiedName value) {
        this.generatedEntity = value;
    }

    /**
     * Gets the value of the usedEntity property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getUsedEntity() {
        return usedEntity;
    }

    /**
     * Sets the value of the usedEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public void setUsedEntity(org.openprovenance.prov.model.QualifiedName value) {
        this.usedEntity = value;
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
     * Gets the value of the generation property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getGeneration() {
        return generation;
    }

    /**
     * Sets the value of the generation property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public void setGeneration(org.openprovenance.prov.model.QualifiedName value) {
        this.generation = value;
    }

    /**
     * Gets the value of the usage property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getUsage() {
        return usage;
    }

    /**
     * Sets the value of the usage property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public void setUsage(org.openprovenance.prov.model.QualifiedName value) {
        this.usage = value;
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
     *     {@link QualifiedName }
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
     *     {@link QualifiedName }
     *     
     */
    public void setId(org.openprovenance.prov.model.QualifiedName value) {
        this.id = value;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WasDerivedFrom)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WasDerivedFrom that = ((WasDerivedFrom) object);
        equalsBuilder.append(this.getGeneratedEntity(), that.getGeneratedEntity());
        equalsBuilder.append(this.getUsedEntity(), that.getUsedEntity());
        equalsBuilder.append(this.getActivity(), that.getActivity());
        equalsBuilder.append(this.getGeneration(), that.getGeneration());
        equalsBuilder.append(this.getUsage(), that.getUsage());
        equalsBuilder.append(this.getLabel(), that.getLabel());
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getOther(), that.getOther());
        equalsBuilder.append(this.getId(), that.getId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WasDerivedFrom)) {
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
        hashCodeBuilder.append(this.getGeneratedEntity());
        hashCodeBuilder.append(this.getUsedEntity());
        hashCodeBuilder.append(this.getActivity());
        hashCodeBuilder.append(this.getGeneration());
        hashCodeBuilder.append(this.getUsage());
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
            org.openprovenance.prov.model.QualifiedName theGeneratedEntity;
            theGeneratedEntity = this.getGeneratedEntity();
            toStringBuilder.append("generatedEntity", theGeneratedEntity);
        }
        {
            org.openprovenance.prov.model.QualifiedName theUsedEntity;
            theUsedEntity = this.getUsedEntity();
            toStringBuilder.append("usedEntity", theUsedEntity);
        }
        {
            org.openprovenance.prov.model.QualifiedName theActivity;
            theActivity = this.getActivity();
            toStringBuilder.append("activity", theActivity);
        }
        {
            org.openprovenance.prov.model.QualifiedName theGeneration;
            theGeneration = this.getGeneration();
            toStringBuilder.append("generation", theGeneration);
        }
        {
            org.openprovenance.prov.model.QualifiedName theUsage;
            theUsage = this.getUsage();
            toStringBuilder.append("usage", theUsage);
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
        { //TODO: only now, for debugging.
            toStringBuilder.append("all", getAllAttributes());
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }
    
    public Kind getKind() {
   	return org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DERIVATION;
    }

}
