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
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.xml.AttributeList;
import org.openprovenance.prov.xml.HasAllAttributes;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.xml.SortedAttributeList;


/**
 * <p>Java class for End complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="End">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement">
 *       &lt;sequence>
 *         &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="trigger" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
 *         &lt;element name="ender" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
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
@XmlType(name = "End", propOrder = {
    "activity",
    "trigger",
    "ender",
    "time",
    "label",
  //  "location",
  //  "role",
   // "type",
   // "others",
   // "any"
    "all"
})
@Entity(name = "WasEndedBy")
@Table(name = "WASENDEDBY")
public class WasEndedBy
    extends AStatement
    implements Equals, HashCode, org.openprovenance.prov.model.WasEndedBy, HasAllAttributes
{

    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName activity;
    @XmlElement(type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName trigger;
    @XmlElement(type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName ender;
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
     * Gets the value of the trigger property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "TRIGGER_")
    public org.openprovenance.prov.model.QualifiedName getTrigger() {
        return trigger;
    }

    /**
     * Sets the value of the trigger property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setTrigger(org.openprovenance.prov.model.QualifiedName value) {
        this.trigger = value;
    }

    /**
     * Gets the value of the ender property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ENDER")
    public org.openprovenance.prov.model.QualifiedName getEnder() {
        return ender;
    }

    /**
     * Sets the value of the ender property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setEnder(org.openprovenance.prov.model.QualifiedName value) {
        this.ender = value;
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
    @JoinColumn(name = "LABEL_WASENDEDBY_PK")
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
    @JoinColumn(name = "END_")
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
    @JoinColumn(name = "ROLE__WASENDEDBY_PK")
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
    @JoinColumn(name = "TYPE__WASENDEDBY_PK")
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
    @OneToMany(targetEntity =  org.openprovenance.prov.sql.Other.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "OTHERS_WASENDEDBY_PK")
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
     * @see org.openprovenance.prov.xml.HasAllAttributes#getAllAttributes()
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

    @Basic
    @Column(name = "TIMEITEM")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimeItem() {
        return ProvUtilities.toDate(this.getTime());

    }

    public void setTimeItem(Date target) {
        setTime(ProvUtilities.toXMLGregorianCalendar(target));
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WasEndedBy)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WasEndedBy that = ((WasEndedBy) object);
        equalsBuilder.append(this.getActivity(), that.getActivity());
        equalsBuilder.append(this.getTrigger(), that.getTrigger());
        equalsBuilder.append(this.getEnder(), that.getEnder());
        equalsBuilder.append(this.getTime(), that.getTime());
        equalsBuilder.append(this.getLabel(), that.getLabel());
        equalsBuilder.append(this.getLocation(), that.getLocation());
        equalsBuilder.append(this.getRole(), that.getRole());
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getOther(), that.getOther());
        equalsBuilder.append(this.getId(), that.getId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WasEndedBy)) {
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
        hashCodeBuilder.append(this.getActivity());
        hashCodeBuilder.append(this.getTrigger());
        hashCodeBuilder.append(this.getEnder());
        hashCodeBuilder.append(this.getTime());
        hashCodeBuilder.append(this.getLabel());
        hashCodeBuilder.append(this.getLocation());
        hashCodeBuilder.append(this.getRole());
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
            org.openprovenance.prov.model.QualifiedName theActivity;
            theActivity = this.getActivity();
            toStringBuilder.append("activity", theActivity);
        }
        {
            org.openprovenance.prov.model.QualifiedName theTrigger;
            theTrigger = this.getTrigger();
            toStringBuilder.append("trigger", theTrigger);
        }
        {
            org.openprovenance.prov.model.QualifiedName theEnder;
            theEnder = this.getEnder();
            toStringBuilder.append("ender", theEnder);
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
        return StatementOrBundle.Kind.PROV_END;
    }
  

}
