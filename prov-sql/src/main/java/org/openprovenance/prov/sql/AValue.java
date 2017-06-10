package org.openprovenance.prov.sql;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.xml.builder.JAXBEqualsBuilder;
import org.openprovenance.prov.xml.builder.JAXBToStringBuilder;
import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.model.ProvUtilities;


/**
 * <p>Java class for AValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AValue">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="int" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="string" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="long" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="short" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="double" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="float" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="decimal" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="boolean" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="byte" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="anyURI" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="qname" type="{http://www.w3.org/2001/XMLSchema}QName"/>
 *         &lt;element name="unsignedInt" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="unsignedLong" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *         &lt;element name="dateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="gYear" type="{http://www.w3.org/2001/XMLSchema}gYear"/>
 *         &lt;element name="integer" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AValue", propOrder = {
    "_int",
    "string",
    "_long",
    "_short",
    "_double",
    "_float",
    "decimal",
    "_boolean",
    "_byte",
    "anyURI",
    "qualifiedName",
    "unsignedInt",
    "unsignedLong",
    "dateTime",
    "gYear",
    "integer"
})
@Embeddable
public class AValue
    implements Equals, HashCode, ToStringBuilder
{

    @XmlElement(name = "int")
    protected Integer _int;
    protected String string;
    @XmlElement(name = "long")
    protected Long _long;
    @XmlElement(name = "short")
    protected Short _short;
    @XmlElement(name = "double")
    protected Double _double;
    @XmlElement(name = "float")
    protected Float _float;
    protected BigDecimal decimal;
    @XmlElement(name = "boolean")
    protected Boolean _boolean;
    @XmlElement(name = "byte")
    protected Byte _byte;
    @XmlSchemaType(name = "anyURI")
    protected String anyURI;
    protected QualifiedName qualifiedName;
    @XmlSchemaType(name = "unsignedInt")
    protected Long unsignedInt;
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger unsignedLong;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateTime;
    @XmlSchemaType(name = "gYear")
    protected XMLGregorianCalendar gYear;
    protected BigInteger integer;

    
    /**
     * Gets the value of the string property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    //    @Column(name = "STRING")
    //    @Lob
//   @Column(name = "STRING", length = 255)
   @Column(name = "STRING", columnDefinition="TEXT")
    public String getString() {
        return string;
    }

    /**
     * Sets the value of the string property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setString(String value) {
        this.string = value;
    }

    /**
     * Gets the value of the long property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    @Basic
    @Column(name = "LONG_", precision = 20, scale = 0)
    public Long getLong() {
        return _long;
    }

    /**
     * Sets the value of the long property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLong(Long value) {
        this._long = value;
    }


    /**
     * Gets the value of the double property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    @Basic
    @Column(name = "DOUBLE_", precision = 20, scale = 10)
    public Double getDouble() {
        return _double;
    }

    /**
     * Sets the value of the double property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDouble(Double value) {
        this._double = value;
    }

    /**
     * Gets the value of the float property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    @Basic
    @Column(name = "FLOAT_", precision = 20, scale = 10)
    public Float getFloat() {
        return _float;
    }

    /**
     * Sets the value of the float property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setFloat(Float value) {
        this._float = value;
    }


    
    /**
     * Gets the value of the qualifiedName property.
     * 
     * @return
     *     possible object is
     *     {@link QualifiedName }
     *     
     */
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "QN")
    public QualifiedName getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Sets the value of the qualifiedName property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualifiedName }
     *     
     */
    public void setQualifiedName(QualifiedName value) {
        this.qualifiedName = value;
    }

   
    /**
     * Gets the value of the dateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Transient
    public XMLGregorianCalendar getDateTime() {
        return dateTime;
    }

    /**
     * Sets the value of the dateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateTime(XMLGregorianCalendar value) {
        this.dateTime = value;
    }

    /**
     * Gets the value of the gYear property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Transient
    public XMLGregorianCalendar getGYear() {
        return gYear;
    }

    /**
     * Sets the value of the gYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGYear(XMLGregorianCalendar value) {
        this.gYear = value;
    }

    @Basic
    @Column(name = "DATETIMEITEM")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateTimeItem() {
        return ProvUtilities.toDate(this.getDateTime());

    }

    public void setDateTimeItem(Date target) {
        setDateTime(ProvUtilities.toXMLGregorianCalendar(target));
    }

    /*
    public Date getDateTimeItem() {
        return XmlAdapterUtils.unmarshall(XMLGregorianCalendarAsDateTime.class, this.getDateTime());
    }

    public void setDateTimeItem(Date target) {
        setDateTime(XmlAdapterUtils.marshall(XMLGregorianCalendarAsDateTime.class, target));
    }*/

    @Basic
    @Column(name = "GYEARITEM")
    @Temporal(TemporalType.DATE)
    public Date getGYearItem() {
        //return XmlAdapterUtils.unmarshall(XMLGregorianCalendarAsGYear.class, this.getGYear());
        return ProvUtilities.toDate(this.getGYear());

    }

    public void setGYearItem(Date target) {
        //setGYear(XmlAdapterUtils.marshall(XMLGregorianCalendarAsGYear.class, target));
        setGYear(ProvUtilities.toXMLGregorianCalendar(target));

    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof AValue)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final AValue that = ((AValue) object);
        equalsBuilder.append(this.getString(), that.getString());
        equalsBuilder.append(this.getLong(), that.getLong());
        equalsBuilder.append(this.getDouble(), that.getDouble());
        equalsBuilder.append(this.getFloat(), that.getFloat());
        equalsBuilder.append(this.getQualifiedName(), that.getQualifiedName());
        equalsBuilder.append(this.getDateTime(), that.getDateTime());
        equalsBuilder.append(this.getGYear(), that.getGYear());
    }

    public boolean equals(Object object) {
        if (!(object instanceof AValue)) {
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
        hashCodeBuilder.append(this.getString());
        hashCodeBuilder.append(this.getLong());
        hashCodeBuilder.append(this.getDouble());
        hashCodeBuilder.append(this.getFloat());
        hashCodeBuilder.append(this.getQualifiedName());
        hashCodeBuilder.append(this.getDateTime());
        hashCodeBuilder.append(this.getGYear());
    }

    public void toString(ToStringBuilder toStringBuilder) {
        toStringBuilder.append("string", this.getString());
        toStringBuilder.append("long", this.getLong());
        toStringBuilder.append("double", this.getDouble());
        toStringBuilder.append("float", this.getFloat());
        toStringBuilder.append("qualifiedName", this.getQualifiedName());
        toStringBuilder.append("dateTime", this.getDateTime());
        toStringBuilder.append("gyear", this.getGYear());
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }


}
