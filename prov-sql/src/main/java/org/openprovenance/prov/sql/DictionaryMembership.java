package org.openprovenance.prov.sql;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
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
    implements Equals, HashCode, org.openprovenance.prov.model.DictionaryMembership
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

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof DictionaryMembership)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final DictionaryMembership that = ((DictionaryMembership) object);
        {
            org.openprovenance.prov.model.QualifiedName lhsDictionary;
            lhsDictionary = this.getDictionary();
            org.openprovenance.prov.model.QualifiedName rhsDictionary;
            rhsDictionary = that.getDictionary();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "dictionary", lhsDictionary), LocatorUtils.property(thatLocator, "dictionary", rhsDictionary), lhsDictionary, rhsDictionary)) {
                return false;
            }
        }
        {
            List<org.openprovenance.prov.model.Entry> lhsKeyEntityPair;
            lhsKeyEntityPair = (((this.keyEntityPair!= null)&&(!this.keyEntityPair.isEmpty()))?this.getKeyEntityPair():null);
            List<org.openprovenance.prov.model.Entry> rhsKeyEntityPair;
            rhsKeyEntityPair = (((that.keyEntityPair!= null)&&(!that.keyEntityPair.isEmpty()))?that.getKeyEntityPair():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "keyEntityPair", lhsKeyEntityPair), LocatorUtils.property(thatLocator, "keyEntityPair", rhsKeyEntityPair), lhsKeyEntityPair, rhsKeyEntityPair)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            org.openprovenance.prov.model.QualifiedName theDictionary;
            theDictionary = this.getDictionary();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "dictionary", theDictionary), currentHashCode, theDictionary);
        }
        {
            List<org.openprovenance.prov.model.Entry> theKeyEntityPair;
            theKeyEntityPair = (((this.keyEntityPair!= null)&&(!this.keyEntityPair.isEmpty()))?this.getKeyEntityPair():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "keyEntityPair", theKeyEntityPair), currentHashCode, theKeyEntityPair);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }
    @Transient 
    public Kind getKind() {
        return StatementOrBundle.Kind.PROV_DICTIONARY_MEMBERSHIP;
    }


}
