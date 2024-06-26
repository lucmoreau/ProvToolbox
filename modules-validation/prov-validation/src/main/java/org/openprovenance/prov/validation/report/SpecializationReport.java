//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.10 at 05:05:05 PM GMT 
//


package org.openprovenance.prov.validation.report;

import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.apache.commons.lang.builder.Equals;
import org.openprovenance.apache.commons.lang.builder.HashCode;
import org.openprovenance.apache.commons.lang.builder.ToString;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for SpecializationReport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpecializationReport"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}specializationOf" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpecializationReport", propOrder = {
    "specializationOf"
})
public class SpecializationReport
    implements Equals, HashCode, ToString
{

    @XmlElement(namespace = "http://www.w3.org/ns/prov#")
    protected List<SpecializationOf> specializationOf;

    /**
     * Gets the value of the specializationOf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specializationOf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecializationOf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SpecializationOf }
     *
     * @return a list of specializations
     * 
     */
    public List<SpecializationOf> getSpecializationOf() {
        if (specializationOf == null) {
            specializationOf = new ArrayList<SpecializationOf>();
        }
        return this.specializationOf;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SpecializationReport)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final SpecializationReport that = ((SpecializationReport) object);
        equalsBuilder.append(this.getSpecializationOf(), that.getSpecializationOf());
    }

    public boolean equals(Object object) {
        if (!(object instanceof SpecializationReport)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new EqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getSpecializationOf());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<SpecializationOf> theSpecializationOf;
            theSpecializationOf = this.getSpecializationOf();
            toStringBuilder.append("specializationOf", theSpecializationOf);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

}
