package org.openprovenance.prov.sql;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;


/**
 * <p>Java class for Insertion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Insertion"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="newDictionary" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *         &lt;element name="oldDictionary" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *         &lt;element name="keyEntityPair" type="{http://www.w3.org/ns/prov#}KeyEntityPair" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Insertion", propOrder = {
    "newDictionary",
    "oldDictionary",
    "keyEntityPair",
    "label",
    "type",
    "other",
    "any"
})
public class DerivedByInsertionFrom
    extends AStatement
    implements Equals, HashCode, ToString, org.openprovenance.prov.model.DerivedByInsertionFrom
{

    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName newDictionary;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName oldDictionary;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.Entry.class)
    protected List<org.openprovenance.prov.model.Entry> keyEntityPair;
    @XmlElement(type = org.openprovenance.prov.sql.InternationalizedString.class)
    protected List<org.openprovenance.prov.model.LangString> label;
    @XmlElement(type = org.openprovenance.prov.sql.Type.class)
    protected List<org.openprovenance.prov.model.Type> type;
    @XmlElement(type = org.openprovenance.prov.sql.Other.class)
    protected List<org.openprovenance.prov.model.Other> other;
    @XmlAnyElement(lax = true)
    protected List<Attribute> any;
    @XmlAttribute(name = "id", namespace = "http://www.w3.org/ns/prov#")
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(QualifiedNameAdapter.class)
    protected QualifiedName id;

    /**
     * Gets the value of the newDictionary property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getNewDictionary() {
        return newDictionary;
    }

    /**
     * Sets the value of the newDictionary property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setNewDictionary(org.openprovenance.prov.model.QualifiedName value) {
        this.newDictionary = value;
    }

    /**
     * Gets the value of the oldDictionary property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getOldDictionary() {
        return oldDictionary;
    }

    /**
     * Sets the value of the oldDictionary property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setOldDictionary(org.openprovenance.prov.model.QualifiedName value) {
        this.oldDictionary = value;
    }

    /**
     * Gets the value of the keyEntityPair property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keyEntityPair property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeyEntityPair().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openprovenance.prov.sql.Entry }
     * 
     * 
     */
    public List<org.openprovenance.prov.model.Entry> getKeyEntityPair() {
        if (keyEntityPair == null) {
            keyEntityPair = new ArrayList<org.openprovenance.prov.model.Entry>();
        }
        return this.keyEntityPair;
    }

    /**
     * 
     * 
     */
    public void setKeyEntityPair(List<org.openprovenance.prov.model.Entry> keyEntityPair) {
        this.keyEntityPair = keyEntityPair;
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
     * {@link org.openprovenance.prov.sql.InternationalizedString }
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
     * 
     * 
     */
    public void setLabel(List<org.openprovenance.prov.model.LangString> label) {
        this.label = label;
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
     * {@link org.openprovenance.prov.sql.Type }
     * 
     * 
     */
    public List<org.openprovenance.prov.model.Type> getType() {
        if (type == null) {
            type = new ArrayList<org.openprovenance.prov.model.Type>();
        }
        return this.type;
    }

    /**
     * 
     * 
     */
    public void setType(List<org.openprovenance.prov.model.Type> type) {
        this.type = type;
    }

    /**
     * Gets the value of the other property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the others property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOthers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Other }
     * 
     * 
     */
    public List<org.openprovenance.prov.model.Other> getOther() {
        if (other == null) {
            other = new ArrayList<org.openprovenance.prov.model.Other>();
        }
        return this.other;
    }

    /**
     * 
     * 
     */
    public void setOther(List<org.openprovenance.prov.model.Other> other) {
        this.other = other;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link QualifiedName }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ID")
    public QualifiedName getId() {
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
    public void setId(QualifiedName value) {
        this.id = value;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof DerivedByInsertionFrom)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final DerivedByInsertionFrom that = ((DerivedByInsertionFrom) object);
        equalsBuilder.append(this.getNewDictionary(), that.getNewDictionary());
        equalsBuilder.append(this.getOldDictionary(), that.getOldDictionary());
        equalsBuilder.append(this.getKeyEntityPair(), that.getKeyEntityPair());
        equalsBuilder.append(this.getLabel(), that.getLabel());
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getOther(), that.getOther());
        equalsBuilder.append(this.getId(), that.getId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof DerivedByInsertionFrom)) {
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
        hashCodeBuilder.append(this.getNewDictionary());
        hashCodeBuilder.append(this.getOldDictionary());
        hashCodeBuilder.append(this.getKeyEntityPair());
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
            org.openprovenance.prov.model.QualifiedName theNewDictionary;
            theNewDictionary = this.getNewDictionary();
            toStringBuilder.append("newDictionary", theNewDictionary);
        }
        {
            org.openprovenance.prov.model.QualifiedName theOldDictionary;
            theOldDictionary = this.getOldDictionary();
            toStringBuilder.append("oldDictionary", theOldDictionary);
        }
        {
            List<org.openprovenance.prov.model.Entry> theKeyEntityPair;
            theKeyEntityPair = this.getKeyEntityPair();
            toStringBuilder.append("keyEntityPair", theKeyEntityPair);
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
    
    @Transient
    public Kind getKind() {
        return StatementOrBundle.Kind.PROV_DICTIONARY_INSERTION;
    }
}
