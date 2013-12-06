package org.openprovenance.prov.sql;

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
 * <p>Java class for Mention complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Mention">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement">
 *       &lt;sequence>
 *         &lt;element name="specificEntity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="generalEntity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="bundle" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Mention", propOrder = {
    "specificEntity",
    "generalEntity",
    "bundle"
})
@Entity(name = "MentionOf")
@Table(name = "MENTIONOF")
public class MentionOf
    extends AStatement
    implements Equals, HashCode, org.openprovenance.prov.model.MentionOf
{

    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName specificEntity;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName generalEntity;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName bundle;

    /**
     * Gets the value of the specificEntity property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "SPECIFICENTITY_MENTIONOF_PK")
    public org.openprovenance.prov.model.QualifiedName getSpecificEntity() {
        return specificEntity;
    }

    /**
     * Sets the value of the specificEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setSpecificEntity(org.openprovenance.prov.model.QualifiedName value) {
        this.specificEntity = value;
    }

    /**
     * Gets the value of the generalEntity property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "GENERALENTITY_MENTIONOF_PK")
    public org.openprovenance.prov.model.QualifiedName getGeneralEntity() {
        return generalEntity;
    }

    /**
     * Sets the value of the generalEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setGeneralEntity(org.openprovenance.prov.model.QualifiedName value) {
        this.generalEntity = value;
    }

    /**
     * Gets the value of the bundle property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "BUNDLE")
    public org.openprovenance.prov.model.QualifiedName getBundle() {
        return bundle;
    }

    /**
     * Sets the value of the bundle property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setBundle(org.openprovenance.prov.model.QualifiedName value) {
        this.bundle = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof MentionOf)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final MentionOf that = ((MentionOf) object);
        {
            org.openprovenance.prov.model.QualifiedName lhsSpecificEntity;
            lhsSpecificEntity = this.getSpecificEntity();
            org.openprovenance.prov.model.QualifiedName rhsSpecificEntity;
            rhsSpecificEntity = that.getSpecificEntity();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "specificEntity", lhsSpecificEntity), LocatorUtils.property(thatLocator, "specificEntity", rhsSpecificEntity), lhsSpecificEntity, rhsSpecificEntity)) {
                return false;
            }
        }
        {
            org.openprovenance.prov.model.QualifiedName lhsGeneralEntity;
            lhsGeneralEntity = this.getGeneralEntity();
            org.openprovenance.prov.model.QualifiedName rhsGeneralEntity;
            rhsGeneralEntity = that.getGeneralEntity();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "generalEntity", lhsGeneralEntity), LocatorUtils.property(thatLocator, "generalEntity", rhsGeneralEntity), lhsGeneralEntity, rhsGeneralEntity)) {
                return false;
            }
        }
        {
            org.openprovenance.prov.model.QualifiedName lhsBundle;
            lhsBundle = this.getBundle();
            org.openprovenance.prov.model.QualifiedName rhsBundle;
            rhsBundle = that.getBundle();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "bundle", lhsBundle), LocatorUtils.property(thatLocator, "bundle", rhsBundle), lhsBundle, rhsBundle)) {
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
            org.openprovenance.prov.model.QualifiedName theSpecificEntity;
            theSpecificEntity = this.getSpecificEntity();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "specificEntity", theSpecificEntity), currentHashCode, theSpecificEntity);
        }
        {
            org.openprovenance.prov.model.QualifiedName theGeneralEntity;
            theGeneralEntity = this.getGeneralEntity();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "generalEntity", theGeneralEntity), currentHashCode, theGeneralEntity);
        }
        {
            org.openprovenance.prov.model.QualifiedName theBundle;
            theBundle = this.getBundle();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "bundle", theBundle), currentHashCode, theBundle);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }
    
    @Transient
    public Kind getKind() {
        return StatementOrBundle.Kind.PROV_MENTION;
    }
}
