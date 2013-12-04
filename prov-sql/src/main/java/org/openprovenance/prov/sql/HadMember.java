package org.openprovenance.prov.sql;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 * <p>Java class for Membership complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Membership">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement">
 *       &lt;sequence>
 *         &lt;element name="collection" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Membership", propOrder = {
    "collection",
    "entity"
})
@Entity(name = "HadMember")
@Table(name = "HADMEMBER")
public class HadMember
    extends AStatement
    implements Equals, HashCode, org.openprovenance.prov.model.HadMember
{

    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName collection;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected List<org.openprovenance.prov.model.QualifiedName> entity;

    /**
     * Gets the value of the collection property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "COLLECTION")
    public org.openprovenance.prov.model.QualifiedName getCollection() {
        return collection;
    }

    /**
     * Sets the value of the collection property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setCollection(org.openprovenance.prov.model.QualifiedName value) {
        this.collection = value;
    }

    /**
     * Gets the value of the entity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openprovenance.prov.sql.IDRef }
     * 
     * 
     */
    @ManyToMany(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinTable(name = "HADMEMBER_ELEMENTS", joinColumns = {
        @JoinColumn(name = "COLLECTION")
    }, inverseJoinColumns = {
        @JoinColumn(name = "ENTITY")
    })
    public List<org.openprovenance.prov.model.QualifiedName> getEntity() {
        if (entity == null) {
            entity = new ArrayList<org.openprovenance.prov.model.QualifiedName>();
        }
        return this.entity;
    }

    /**
     * 
     * 
     */
    public void setEntity(List<org.openprovenance.prov.model.QualifiedName> entity) {
        this.entity = entity;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof HadMember)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final HadMember that = ((HadMember) object);
        {
            org.openprovenance.prov.model.QualifiedName lhsCollection;
            lhsCollection = this.getCollection();
            org.openprovenance.prov.model.QualifiedName rhsCollection;
            rhsCollection = that.getCollection();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "collection", lhsCollection), LocatorUtils.property(thatLocator, "collection", rhsCollection), lhsCollection, rhsCollection)) {
                return false;
            }
        }
        {
            List<org.openprovenance.prov.model.QualifiedName> lhsEntity;
            lhsEntity = (((this.entity!= null)&&(!this.entity.isEmpty()))?this.getEntity():null);
            List<org.openprovenance.prov.model.QualifiedName> rhsEntity;
            rhsEntity = (((that.entity!= null)&&(!that.entity.isEmpty()))?that.getEntity():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "entity", lhsEntity), LocatorUtils.property(thatLocator, "entity", rhsEntity), lhsEntity, rhsEntity)) {
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
            org.openprovenance.prov.model.QualifiedName theCollection;
            theCollection = this.getCollection();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "collection", theCollection), currentHashCode, theCollection);
        }
        {
            List<org.openprovenance.prov.model.QualifiedName> theEntity;
            theEntity = (((this.entity!= null)&&(!this.entity.isEmpty()))?this.getEntity():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "entity", theEntity), currentHashCode, theEntity);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }
    
    @Transient
    public Kind getKind() {
	return StatementOrBundle.Kind.PROV_MEMBERSHIP;
    }


}
