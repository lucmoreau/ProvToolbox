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

import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.xml.builder.JAXBEqualsBuilder;
import org.openprovenance.prov.xml.builder.JAXBHashCodeBuilder;
import org.openprovenance.prov.xml.builder.JAXBToStringBuilder;
import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.model.StatementOrBundle;


/**
 * <p>Java class for Specialization complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Specialization"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="specificEntity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *         &lt;element name="generalEntity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Specialization", propOrder = {
    "specificEntity",
    "generalEntity"
})
@Entity(name = "SpecializationOf")
@Table(name = "SPECIALIZATIONOF")
public class SpecializationOf
    extends AStatement
    implements Equals, HashCode, ToString, org.openprovenance.prov.model.SpecializationOf
{

    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName specificEntity;
    @XmlElement(required = true, type = org.openprovenance.prov.sql.IDRef.class)
    protected org.openprovenance.prov.model.QualifiedName generalEntity;

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
    @JoinColumn(name = "SPECIFIC")
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
    @JoinColumn(name = "GENERAL")
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SpecializationOf)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final SpecializationOf that = ((SpecializationOf) object);
        equalsBuilder.append(this.getSpecificEntity(), that.getSpecificEntity());
        equalsBuilder.append(this.getGeneralEntity(), that.getGeneralEntity());
    }

    public boolean equals(Object object) {
        if (!(object instanceof SpecializationOf)) {
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
        hashCodeBuilder.append(this.getSpecificEntity());
        hashCodeBuilder.append(this.getGeneralEntity());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            org.openprovenance.prov.model.QualifiedName theSpecificEntity;
            theSpecificEntity = this.getSpecificEntity();
            toStringBuilder.append("specificEntity", theSpecificEntity);
        }
        {
            org.openprovenance.prov.model.QualifiedName theGeneralEntity;
            theGeneralEntity = this.getGeneralEntity();
            toStringBuilder.append("generalEntity", theGeneralEntity);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }
    

    @Transient
    public Kind getKind() {
        return StatementOrBundle.Kind.PROV_SPECIALIZATION;
    }


}
