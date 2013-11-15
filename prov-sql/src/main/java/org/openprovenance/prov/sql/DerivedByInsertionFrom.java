package org.openprovenance.prov.sql;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
import org.openprovenance.prov.model.Attribute;
import org.w3c.dom.Element;
import org.openprovenance.prov.model.StatementOrBundle;


/**
 * <p>Java class for Insertion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Insertion">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement">
 *       &lt;sequence>
 *         &lt;element name="newDictionary" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="oldDictionary" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="keyEntityPair" type="{http://www.w3.org/ns/prov#}KeyEntityPair" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
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
    implements Equals, HashCode, org.openprovenance.prov.model.DerivedByInsertionFrom
{

    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.IDRef newDictionary;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.IDRef oldDictionary;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.Entry.class)
    protected List<org.openprovenance.prov.model.Entry> keyEntityPair;
    @XmlElement(type = org.openprovenance.prov.sql.InternationalizedString.class)
    protected List<org.openprovenance.prov.model.InternationalizedString> label;
    @XmlElement(type = org.openprovenance.prov.sql.Type.class)
    protected List<org.openprovenance.prov.model.Type> type;
    @XmlElement(type = org.openprovenance.prov.sql.Other.class)
    protected List<org.openprovenance.prov.model.Other> other;
    @XmlAnyElement(lax = true)
    protected List<Attribute> any;
    @XmlAttribute(name = "id", namespace = "http://www.w3.org/ns/prov#")
    protected QName id;

    /**
     * Gets the value of the newDictionary property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public org.openprovenance.prov.model.IDRef getNewDictionary() {
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
    public void setNewDictionary(org.openprovenance.prov.model.IDRef value) {
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
    public org.openprovenance.prov.model.IDRef getOldDictionary() {
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
    public void setOldDictionary(org.openprovenance.prov.model.IDRef value) {
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
    public List<org.openprovenance.prov.model.InternationalizedString> getLabel() {
        if (label == null) {
            label = new ArrayList<org.openprovenance.prov.model.InternationalizedString>();
        }
        return this.label;
    }

    /**
     * 
     * 
     */
    public void setLabel(List<org.openprovenance.prov.model.InternationalizedString> label) {
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
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     * 
     * 
     */
    public List<Attribute> getAny() {
        if (any == null) {
            any = new ArrayList<Attribute>();
        }
        return this.any;
    }

    /**
     * 
     * 
     */
    public void setAny(List<Attribute> any) {
        this.any = any;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setId(QName value) {
        this.id = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof DerivedByInsertionFrom)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final DerivedByInsertionFrom that = ((DerivedByInsertionFrom) object);
        {
            org.openprovenance.prov.model.IDRef lhsNewDictionary;
            lhsNewDictionary = this.getNewDictionary();
            org.openprovenance.prov.model.IDRef rhsNewDictionary;
            rhsNewDictionary = that.getNewDictionary();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "newDictionary", lhsNewDictionary), LocatorUtils.property(thatLocator, "newDictionary", rhsNewDictionary), lhsNewDictionary, rhsNewDictionary)) {
                return false;
            }
        }
        {
            org.openprovenance.prov.model.IDRef lhsOldDictionary;
            lhsOldDictionary = this.getOldDictionary();
            org.openprovenance.prov.model.IDRef rhsOldDictionary;
            rhsOldDictionary = that.getOldDictionary();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "oldDictionary", lhsOldDictionary), LocatorUtils.property(thatLocator, "oldDictionary", rhsOldDictionary), lhsOldDictionary, rhsOldDictionary)) {
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
        {
            List<org.openprovenance.prov.model.InternationalizedString> lhsLabel;
            lhsLabel = (((this.label!= null)&&(!this.label.isEmpty()))?this.getLabel():null);
            List<org.openprovenance.prov.model.InternationalizedString> rhsLabel;
            rhsLabel = (((that.label!= null)&&(!that.label.isEmpty()))?that.getLabel():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "label", lhsLabel), LocatorUtils.property(thatLocator, "label", rhsLabel), lhsLabel, rhsLabel)) {
                return false;
            }
        }
        {
            List<org.openprovenance.prov.model.Type> lhsType;
            lhsType = (((this.type!= null)&&(!this.type.isEmpty()))?this.getType():null);
            List<org.openprovenance.prov.model.Type> rhsType;
            rhsType = (((that.type!= null)&&(!that.type.isEmpty()))?that.getType():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "type", lhsType), LocatorUtils.property(thatLocator, "type", rhsType), lhsType, rhsType)) {
                return false;
            }
        }
        {
            List<org.openprovenance.prov.model.Other> lhsOthers;
            lhsOthers = (((this.other!= null)&&(!this.other.isEmpty()))?this.getOther():null);
            List<org.openprovenance.prov.model.Other> rhsOthers;
            rhsOthers = (((that.other!= null)&&(!that.other.isEmpty()))?that.getOther():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "others", lhsOthers), LocatorUtils.property(thatLocator, "others", rhsOthers), lhsOthers, rhsOthers)) {
                return false;
            }
        }
        {
            List<Attribute> lhsAny;
            lhsAny = (((this.any!= null)&&(!this.any.isEmpty()))?this.getAny():null);
            List<Attribute> rhsAny;
            rhsAny = (((that.any!= null)&&(!that.any.isEmpty()))?that.getAny():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "any", lhsAny), LocatorUtils.property(thatLocator, "any", rhsAny), lhsAny, rhsAny)) {
                return false;
            }
        }
        {
            QName lhsId;
            lhsId = this.getId();
            QName rhsId;
            rhsId = that.getId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "id", lhsId), LocatorUtils.property(thatLocator, "id", rhsId), lhsId, rhsId)) {
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
        int currentHashCode = super.hashCode(locator, strategy);
        {
            org.openprovenance.prov.model.IDRef theNewDictionary;
            theNewDictionary = this.getNewDictionary();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "newDictionary", theNewDictionary), currentHashCode, theNewDictionary);
        }
        {
            org.openprovenance.prov.model.IDRef theOldDictionary;
            theOldDictionary = this.getOldDictionary();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "oldDictionary", theOldDictionary), currentHashCode, theOldDictionary);
        }
        {
            List<org.openprovenance.prov.model.Entry> theKeyEntityPair;
            theKeyEntityPair = (((this.keyEntityPair!= null)&&(!this.keyEntityPair.isEmpty()))?this.getKeyEntityPair():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "keyEntityPair", theKeyEntityPair), currentHashCode, theKeyEntityPair);
        }
        {
            List<org.openprovenance.prov.model.InternationalizedString> theLabel;
            theLabel = (((this.label!= null)&&(!this.label.isEmpty()))?this.getLabel():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "label", theLabel), currentHashCode, theLabel);
        }
        {
            List<org.openprovenance.prov.model.Type> theType;
            theType = (((this.type!= null)&&(!this.type.isEmpty()))?this.getType():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "type", theType), currentHashCode, theType);
        }
        {
            List<org.openprovenance.prov.model.Other> theOthers;
            theOthers = (((this.other!= null)&&(!this.other.isEmpty()))?this.getOther():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "others", theOthers), currentHashCode, theOthers);
        }
        {
            List<Attribute> theAny;
            theAny = (((this.any!= null)&&(!this.any.isEmpty()))?this.getAny():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "any", theAny), currentHashCode, theAny);
        }
        {
            QName theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }
    @Transient
    public Kind getKind() {
        return StatementOrBundle.Kind.PROV_DICTIONARY_INSERTION;
    }
}
