package org.openprovenance.prov.sql;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
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
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.xml.AttributeList;
import org.openprovenance.prov.xml.HasAllAttributes;
import org.openprovenance.prov.xml.SortedAttributeList;


/**
 * <p>Java class for Attribution complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Attribution">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement">
 *       &lt;sequence>
 *         &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="agent" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "Attribution", propOrder = {
    "entity",
    "agent",
    "label",
    //"type",
    //"others",
    //"any"
    "all"
})
@Entity(name = "WasAttributedTo")
@Table(name = "WASATTRIBUTEDTO")
public class WasAttributedTo
    extends AStatement
    implements Equals, HashCode, org.openprovenance.prov.model.WasAttributedTo, HasAllAttributes
{

    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName entity;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName agent;
    @XmlElement(type = org.openprovenance.prov.sql.InternationalizedString.class)
    protected List<org.openprovenance.prov.model.LangString> label;
    
    transient protected List<org.openprovenance.prov.model.Type> type;
    transient protected List<Other> other;
    
    @XmlAnyElement
    protected List<Attribute> all;
    @XmlAttribute(name = "id", namespace = "http://www.w3.org/ns/prov#")
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(QNameAdapter.class)
    protected QualifiedName id;
    

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
     * Gets the value of the agent property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "AGENT")
    public org.openprovenance.prov.model.QualifiedName getAgent() {
        return agent;
    }

    /**
     * Sets the value of the agent property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setAgent(org.openprovenance.prov.model.QualifiedName value) {
        this.agent = value;
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
    @JoinColumn(name = "LABEL_WASATTRIBUTEDTO_PK")
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
    @OneToMany(targetEntity = org.openprovenance.prov.sql.Type.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "TYPE__WASATTRIBUTEDTO_PK")
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
    @JoinColumn(name = "OTHERS_WASATTRIBUTEDTO_PK")
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
    
    
    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WasAttributedTo)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final WasAttributedTo that = ((WasAttributedTo) object);
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
            org.openprovenance.prov.model.QualifiedName lhsAgent;
            lhsAgent = this.getAgent();
            org.openprovenance.prov.model.QualifiedName rhsAgent;
            rhsAgent = that.getAgent();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "agent", lhsAgent), LocatorUtils.property(thatLocator, "agent", rhsAgent), lhsAgent, rhsAgent)) {
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
            org.openprovenance.prov.model.QualifiedName theEntity;
            theEntity = this.getEntity();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "entity", theEntity), currentHashCode, theEntity);
        }
        {
            org.openprovenance.prov.model.QualifiedName theAgent;
            theAgent = this.getAgent();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "agent", theAgent), currentHashCode, theAgent);
        }
        {
            List<org.openprovenance.prov.model.LangString> theLabel;
            theLabel = (((this.label!= null)&&(!this.label.isEmpty()))?this.getLabel():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "label", theLabel), currentHashCode, theLabel);
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
        return StatementOrBundle.Kind.PROV_ATTRIBUTION;
    }
 
}
