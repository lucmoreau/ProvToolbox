package org.openprovenance.prov.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XMLGregorianCalendarAsDateTime;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XmlAdapterUtils;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.xml.AttributeList;
import org.openprovenance.prov.xml.HasAllAttributes;
import org.openprovenance.prov.xml.SortedAttributeList;


/**
 * <p>Java class for Usage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Usage">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement">
 *       &lt;sequence>
 *         &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}role" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}others" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "Usage", propOrder = {
    "activity",
    "entity",
    "time",
    "label",
    //"location",
    //"role",
    //"type",
    //"others",
    //"any"
    "all"
})
@Entity(name = "Used")
@Table(name = "USED")
public class Used
    extends AStatement
    implements Equals, HashCode, org.openprovenance.prov.model.Used, HasAllAttributes
{
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName activity;
    
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName entity;
    
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar time;
    @XmlElement(type = org.openprovenance.prov.sql.InternationalizedString.class)
    protected List<org.openprovenance.prov.model.LangString> label;

    transient protected List<org.openprovenance.prov.model.Location> location;
    transient protected List<org.openprovenance.prov.model.Role> role;
    transient protected List<org.openprovenance.prov.model.Type> type;
    transient protected List<Other> other;
    
    @XmlAnyElement
    protected List<Attribute> all;
    @XmlAttribute(name = "id", namespace = "http://www.w3.org/ns/prov#")
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(QNameAdapter.class)
    protected QualifiedName id;
    
   

    
    /**
     * Gets the value of the activity property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ACTIVITY")
    public org.openprovenance.prov.model.QualifiedName getActivity() {
        return activity;
    }

    /**
     * Sets the value of the activity property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setActivity(org.openprovenance.prov.model.QualifiedName value) {
        this.activity = value;
    }

    /**
     * Gets the value of the entity property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ENTITY")
    public org.openprovenance.prov.model.QualifiedName getEntity() {
        return entity;
    }

    
  
    
    /**
     * Sets the value of the entity property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setEntity(org.openprovenance.prov.model.QualifiedName value) {
        this.entity = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Transient
    public XMLGregorianCalendar getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTime(XMLGregorianCalendar value) {
        this.time = value;
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
    @OneToMany(targetEntity = org.openprovenance.prov.sql.InternationalizedString.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "LABEL_USED_PK")
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
     * {@link org.openprovenance.prov.sql.Location }
     * 
     * 
     */
    @OneToMany(targetEntity = org.openprovenance.prov.sql.Location.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "USAGE")
    public List<org.openprovenance.prov.model.Location> getLocation() {
        if (location == null) {
            location=AttributeList.populateKnownAttributes(this,all, org.openprovenance.prov.model.Location.class);
        }
        return this.location;
    }

    /**
     * 
     * 
     */
    public void setLocation(List<org.openprovenance.prov.model.Location> location) {
        this.location = location;
    }

    /**
     * Gets the value of the role property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the role property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openprovenance.prov.sql.Role }
     * 
     * 
     */
    @OneToMany(targetEntity = org.openprovenance.prov.sql.Role.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ROLE__USED_PK")
    public List<org.openprovenance.prov.model.Role> getRole() {
        if (role == null) {
            role=AttributeList.populateKnownAttributes(this,all, org.openprovenance.prov.model.Role.class);
        }
        return this.role;
    }

    /**
     * 
     * 
     */
    public void setRole(List<org.openprovenance.prov.model.Role> role) {
        this.role = role;
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
    @OneToMany(targetEntity = org.openprovenance.prov.sql.Type.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "TYPE__USED_PK")
    public List<org.openprovenance.prov.model.Type> getType() {
        if (type == null) {
            type=AttributeList.populateKnownAttributes(this,all, org.openprovenance.prov.model.Type.class);
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
     * Gets the value of the others property.
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
    @OneToMany(targetEntity = org.openprovenance.prov.sql.Other.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "OTHERS_USED_PK")
    public List<Other> getOther() {
        if (other == null) {
            other=AttributeList.populateKnownAttributes(this,all, org.openprovenance.prov.model.Other.class);
        }
        return this.other;
    }

    /**
     * 
     * 
     */
    public void setOther(List<Other> others) {
        this.other = others;
    }


    /** Gets the List of all attributes
     * @see org.openprovenance.prov.xml.HasAllAttributes#getAll()
     */
    @Transient
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
     *     {@link QName }
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
     *     {@link QName }
     *     
     */
    public void setId(QualifiedName value) {
        this.id = value;
    }

    @Basic
    @Column(name = "TIMEITEM")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimeItem() {
        return XmlAdapterUtils.unmarshall(XMLGregorianCalendarAsDateTime.class, this.getTime());
    }

    public void setTimeItem(Date target) {
        setTime(XmlAdapterUtils.marshall(XMLGregorianCalendarAsDateTime.class, target));
    }

    

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Used)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final Used that = ((Used) object);
        {
            org.openprovenance.prov.model.QualifiedName lhsActivity;
            lhsActivity = this.getActivity();
            org.openprovenance.prov.model.QualifiedName rhsActivity;
            rhsActivity = that.getActivity();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "activity", lhsActivity), LocatorUtils.property(thatLocator, "activity", rhsActivity), lhsActivity, rhsActivity)) {
                return false;
            }
        }
        {
            org.openprovenance.prov.model.QualifiedName lhsEntity;
            lhsEntity = this.getEntity();
            org.openprovenance.prov.model.QualifiedName rhsEntity;
            rhsEntity = that.getEntity();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "entity", lhsEntity), LocatorUtils.property(thatLocator, "entity", rhsEntity), lhsEntity, rhsEntity)) {
                return false;
            }
        }
        {
            XMLGregorianCalendar lhsTime;
            lhsTime = this.getTime();
            XMLGregorianCalendar rhsTime;
            rhsTime = that.getTime();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "time", lhsTime), LocatorUtils.property(thatLocator, "time", rhsTime), lhsTime, rhsTime)) {
                return false;
            }
        }
        {
            List<org.openprovenance.prov.model.LangString> lhsLabel;
            lhsLabel = (((this.label!= null)&&(!this.label.isEmpty()))?this.getLabel():null);
            List<org.openprovenance.prov.model.LangString> rhsLabel;
            rhsLabel = (((that.label!= null)&&(!that.label.isEmpty()))?that.getLabel():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "label", lhsLabel), LocatorUtils.property(thatLocator, "label", rhsLabel), lhsLabel, rhsLabel)) {
                return false;
            }
        }
        {
            List<org.openprovenance.prov.model.Location> lhsLocation;
            lhsLocation = (((this.location!= null)&&(!this.location.isEmpty()))?this.getLocation():null);
            List<org.openprovenance.prov.model.Location> rhsLocation;
            rhsLocation = (((that.location!= null)&&(!that.location.isEmpty()))?that.getLocation():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "location", lhsLocation), LocatorUtils.property(thatLocator, "location", rhsLocation), lhsLocation, rhsLocation)) {
                return false;
            }
        }
        {
            List<org.openprovenance.prov.model.Role> lhsRole;
            lhsRole = (((this.role!= null)&&(!this.role.isEmpty()))?this.getRole():null);
            List<org.openprovenance.prov.model.Role> rhsRole;
            rhsRole = (((that.role!= null)&&(!that.role.isEmpty()))?that.getRole():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "role", lhsRole), LocatorUtils.property(thatLocator, "role", rhsRole), lhsRole, rhsRole)) {
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
            List<Other> lhsOthers;
            lhsOthers = (((this.other!= null)&&(!this.other.isEmpty()))?this.getOther():null);
            List<Other> rhsOthers;
            rhsOthers = (((that.other!= null)&&(!that.other.isEmpty()))?that.getOther():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "others", lhsOthers), LocatorUtils.property(thatLocator, "others", rhsOthers), lhsOthers, rhsOthers)) {
                return false;
            }
        }
        {
            QualifiedName lhsId;
            lhsId = this.getId();
            QualifiedName rhsId;
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
            org.openprovenance.prov.model.QualifiedName theActivity;
            theActivity = this.getActivity();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "activity", theActivity), currentHashCode, theActivity);
        }
        {
            org.openprovenance.prov.model.QualifiedName theEntity;
            theEntity = this.getEntity();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "entity", theEntity), currentHashCode, theEntity);
        }
        {
            XMLGregorianCalendar theTime;
            theTime = this.getTime();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "time", theTime), currentHashCode, theTime);
        }
        {
            List<org.openprovenance.prov.model.LangString> theLabel;
            theLabel = (((this.label!= null)&&(!this.label.isEmpty()))?this.getLabel():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "label", theLabel), currentHashCode, theLabel);
        }
        {
            List<org.openprovenance.prov.model.Location> theLocation;
            theLocation = (((this.location!= null)&&(!this.location.isEmpty()))?this.getLocation():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "location", theLocation), currentHashCode, theLocation);
        }
        {
            List<org.openprovenance.prov.model.Role> theRole;
            theRole = (((this.role!= null)&&(!this.role.isEmpty()))?this.getRole():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "role", theRole), currentHashCode, theRole);
        }
        {
            List<org.openprovenance.prov.model.Type> theType;
            theType = (((this.type!= null)&&(!this.type.isEmpty()))?this.getType():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "type", theType), currentHashCode, theType);
        }
        {
            List<Other> theOthers;
            theOthers = (((this.other!= null)&&(!this.other.isEmpty()))?this.getOther():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "others", theOthers), currentHashCode, theOthers);
        }
        {
            QualifiedName theId;
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
        return StatementOrBundle.Kind.PROV_USAGE;
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            org.openprovenance.prov.model.QualifiedName theActivity;
            theActivity = this.getActivity();
            toStringBuilder.append("activity", theActivity);
        }
        {
            org.openprovenance.prov.model.QualifiedName theEntity;
            theEntity = this.getEntity();
            toStringBuilder.append("entity", theEntity);
        }
        {
            XMLGregorianCalendar theTime;
            theTime = this.getTime();
            toStringBuilder.append("time", theTime);
        }
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
            List<org.openprovenance.prov.model.Role> theRole;
            theRole = this.getRole();
            toStringBuilder.append("role", theRole);
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
            QualifiedName theId;
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

    
    

}
