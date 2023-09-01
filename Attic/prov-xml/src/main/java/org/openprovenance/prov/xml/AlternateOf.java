package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.xml.builder.JAXBEqualsBuilder;
import org.openprovenance.prov.xml.builder.JAXBHashCodeBuilder;
import org.openprovenance.prov.xml.builder.JAXBToStringBuilder;


/**
 * <p>Java class for Alternate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Alternate"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="alternate1" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *         &lt;element name="alternate2" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Alternate", propOrder = {
    "alternate1",
    "alternate2"
})
public class AlternateOf implements Equals, HashCode, ToString, org.openprovenance.prov.model.AlternateOf
{

    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(required = true, type = org.openprovenance.prov.xml.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName alternate1;

    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
    @XmlElement(required = true, type = org.openprovenance.prov.xml.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName alternate2;

    /**
     * Gets the value of the alternate1 property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getAlternate1() {
        return alternate1;
    }

    /**
     * Sets the value of the alternate1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
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
     *     {@link org.openprovenance.prov.xml.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getAlternate2() {
        return alternate2;
    }

    /**
     * Sets the value of the alternate2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.xml.QualifiedName }
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
    

    public Kind getKind() {
   	return org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ALTERNATE;
    }


}
