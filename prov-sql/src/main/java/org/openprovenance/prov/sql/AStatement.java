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
//@Inheritance(strategy = InheritanceType.JOINED)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)

abstract public class AStatement
    implements  StatementOrBundle
{

    @XmlAttribute(name = "pk")
    protected Long pk;

    /**
     * Gets the value of the pk property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    @Id
    @Column(name = "PK")
    //    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.TABLE)
    // see http://stackoverflow.com/questions/916169/cannot-use-identity-column-key-generation-with-union-subclass-table-per-clas
    public Long getPk() {
        return pk;
    }

    /**
     * Sets the value of the pk property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPk(Long value) {
        this.pk = value;
    }


    @Transient
    public Kind getKind() {
        throw new UnsupportedOperationException();
    }

    

}
