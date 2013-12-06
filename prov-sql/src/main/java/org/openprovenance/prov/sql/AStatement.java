package org.openprovenance.prov.sql;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.openprovenance.prov.model.StatementOrBundle;


/**
 * <p>Java class for AStatement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AStatement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AStatement")
@XmlSeeAlso({
    WasAssociatedWith.class,
    WasInfluencedBy.class,
    Used.class,
    ActedOnBehalfOf.class,
    WasInformedBy.class,
    WasEndedBy.class,
    HadMember.class,
    AlternateOf.class,
    Activity.class,
    SpecializationOf.class,
    WasAttributedTo.class,
    MentionOf.class,
    WasInvalidatedBy.class,
    Agent.class,
    DerivedByInsertionFrom.class,
    DerivedByRemovalFrom.class,
    org.openprovenance.prov.sql.Entity.class,
    WasDerivedFrom.class,
    WasGeneratedBy.class,
    WasStartedBy.class,
    NamedBundle.class
})
@javax.persistence.Entity(name = "AStatement")
@Table(name = "ASTATEMENT")
@Inheritance(strategy = InheritanceType.JOINED)
public class AStatement
    implements Equals, HashCode, StatementOrBundle
{

    @XmlAttribute(name = "pk")
    protected Long hjid;

    /**
     * Gets the value of the hjid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    @Id
    @Column(name = "HJID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getHjid() {
        return hjid;
    }

    /**
     * Sets the value of the hjid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHjid(Long value) {
        this.hjid = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof AStatement)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }
    
    @Transient
    public Kind getKind() {
        throw new UnsupportedOperationException();
    }

    

}
