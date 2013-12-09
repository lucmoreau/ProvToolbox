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
 * <p>Java class for Alternate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Alternate">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement">
 *       &lt;sequence>
 *         &lt;element name="alternate1" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="alternate2" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Alternate", propOrder = {
    "alternate1",
    "alternate2"
})
@Entity(name = "AlternateOf")
@Table(name = "ALTERNATEOF")
public class AlternateOf
    extends AStatement
    implements Equals, HashCode, org.openprovenance.prov.model.AlternateOf
{

    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName alternate1;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName alternate2;

    /**
     * Gets the value of the alternate1 property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ALTERNATE1")
    public org.openprovenance.prov.model.QualifiedName getAlternate1() {
        return alternate1;
    }

    /**
     * Sets the value of the alternate1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setAlternate1(org.openprovenance.prov.model.QualifiedName value) {
        this.alternate1 = value;
    }

    /**
     * Gets the value of the alternate2 property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ALTERNATE2")
    public org.openprovenance.prov.model.QualifiedName getAlternate2() {
        return alternate2;
    }

    /**
     * Sets the value of the alternate2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.sql.IDRef }
     *     
     */
    public void setAlternate2(org.openprovenance.prov.model.QualifiedName value) {
        this.alternate2 = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof AlternateOf)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final AlternateOf that = ((AlternateOf) object);
        {
            org.openprovenance.prov.model.QualifiedName lhsAlternate1;
            lhsAlternate1 = this.getAlternate1();
            org.openprovenance.prov.model.QualifiedName rhsAlternate1;
            rhsAlternate1 = that.getAlternate1();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "alternate1", lhsAlternate1), LocatorUtils.property(thatLocator, "alternate1", rhsAlternate1), lhsAlternate1, rhsAlternate1)) {
                return false;
            }
        }
        {
            org.openprovenance.prov.model.QualifiedName lhsAlternate2;
            lhsAlternate2 = this.getAlternate2();
            org.openprovenance.prov.model.QualifiedName rhsAlternate2;
            rhsAlternate2 = that.getAlternate2();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "alternate2", lhsAlternate2), LocatorUtils.property(thatLocator, "alternate2", rhsAlternate2), lhsAlternate2, rhsAlternate2)) {
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
            org.openprovenance.prov.model.QualifiedName theAlternate1;
            theAlternate1 = this.getAlternate1();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "alternate1", theAlternate1), currentHashCode, theAlternate1);
        }
        {
            org.openprovenance.prov.model.QualifiedName theAlternate2;
            theAlternate2 = this.getAlternate2();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "alternate2", theAlternate2), currentHashCode, theAlternate2);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }
    
    @Transient
    public Kind getKind() {
        return StatementOrBundle.Kind.PROV_ALTERNATE;
    }


}
