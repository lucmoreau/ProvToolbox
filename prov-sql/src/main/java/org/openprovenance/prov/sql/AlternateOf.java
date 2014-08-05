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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.xml.builder.JAXBEqualsBuilder;
import org.openprovenance.prov.xml.builder.JAXBHashCodeBuilder;
import org.openprovenance.prov.xml.builder.JAXBToStringBuilder;
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
    implements Equals, HashCode, ToString, org.openprovenance.prov.model.AlternateOf
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof AlternateOf)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final AlternateOf that = ((AlternateOf) object);
        equalsBuilder.append(this.getAlternate1(), that.getAlternate1());
        equalsBuilder.append(this.getAlternate2(), that.getAlternate2());
    }

    public boolean equals(Object object) {
        if (!(object instanceof AlternateOf)) {
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
        hashCodeBuilder.append(this.getAlternate1());
        hashCodeBuilder.append(this.getAlternate2());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            org.openprovenance.prov.model.QualifiedName theAlternate1;
            theAlternate1 = this.getAlternate1();
            toStringBuilder.append("alternate1", theAlternate1);
        }
        {
            org.openprovenance.prov.model.QualifiedName theAlternate2;
            theAlternate2 = this.getAlternate2();
            toStringBuilder.append("alternate2", theAlternate2);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    @Transient
    public Kind getKind() {
        return StatementOrBundle.Kind.PROV_ALTERNATE;
    }


}
