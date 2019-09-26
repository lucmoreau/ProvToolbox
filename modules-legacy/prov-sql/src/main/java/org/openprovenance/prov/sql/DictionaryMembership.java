package org.openprovenance.prov.sql;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
import org.openprovenance.prov.model.StatementOrBundle;


/**
 * <p>Java class for DictionaryMembership complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DictionaryMembership">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dictionary" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="keyEntityPair" type="{http://www.w3.org/ns/prov#}KeyEntityPair" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DictionaryMembership", propOrder = {
    "dictionary",
    "keyEntityPair"
})
@Entity(name = "DictionaryMembership")
@Table(name = "DICTIONARYMEMBERSHIP")
//@Inheritance(strategy = InheritanceType.JOINED)
public class DictionaryMembership extends AStatement
    implements Equals, HashCode, ToString, org.openprovenance.prov.model.DictionaryMembership
{

    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName dictionary;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.Entry.class)
    protected List<org.openprovenance.prov.model.Entry> keyEntityPair;

    /**
     * Gets the value of the dictionary property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "DICTIONARY__DICTIONARYMEMBER_0")
    public org.openprovenance.prov.model.QualifiedName getDictionary() {
        return dictionary;
    }

    /**
     * Sets the value of the dictionary property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setDictionary(org.openprovenance.prov.model.QualifiedName value) {
        this.dictionary = value;
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
    @Transient
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof DictionaryMembership)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final DictionaryMembership that = ((DictionaryMembership) object);
        equalsBuilder.append(this.getDictionary(), that.getDictionary());
        equalsBuilder.append(this.getKeyEntityPair(), that.getKeyEntityPair());
    }

    public boolean equals(Object object) {
        if (!(object instanceof DictionaryMembership)) {
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
        hashCodeBuilder.append(this.getDictionary());
        hashCodeBuilder.append(this.getKeyEntityPair());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            org.openprovenance.prov.model.QualifiedName theDictionary;
            theDictionary = this.getDictionary();
            toStringBuilder.append("dictionary", theDictionary);
        }
        {
            List<org.openprovenance.prov.model.Entry> theKeyEntityPair;
            theKeyEntityPair = this.getKeyEntityPair();
            toStringBuilder.append("keyEntityPair", theKeyEntityPair);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    @Transient 
    public Kind getKind() {
        return StatementOrBundle.Kind.PROV_DICTIONARY_MEMBERSHIP;
    }


}
