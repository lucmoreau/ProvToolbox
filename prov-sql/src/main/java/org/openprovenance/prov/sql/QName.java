package org.openprovenance.prov.sql;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.xml.bind.annotation.XmlType;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.QNameAsString;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XmlAdapterUtils;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for IDRef complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IDRef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}ref use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QName")
@Entity(name = "QName")
@javax.persistence.Cacheable @Table(name = "QName", uniqueConstraints=@javax.persistence.UniqueConstraint(columnNames={"URI"}))
@Inheritance(strategy = InheritanceType.JOINED)
public class QName extends javax.xml.namespace.QName
    
{

    public QName(String namespaceURI, String localPart, String prefix) {
        super(namespaceURI, localPart, prefix);
        this.namespace=namespaceURI;
        this.local=localPart;
        this.prefix=prefix;
    }

    @XmlAttribute(name = "ref", namespace = "http://www.w3.org/ns/prov#", required = true)
    protected QName ref;
    @XmlAttribute(name = "Hjid")
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

    @Basic
    @Column(name = "URI")
    public String getUri() {
	return this.getNamespaceURI()
		+ this.getLocalPart();
    } 
    
    public void setUri(String uri) {} 
    
    @Basic@Column(name = "REFITEM")
    public String getRefItem() {
        return XmlAdapterUtils.unmarshall(QNameAsString.class, this);
    }

    public void setRefItem(String target) {
        javax.xml.namespace.QName qname=XmlAdapterUtils.marshall(QNameAsString.class, target);
        System.out.println("QName.setRefIterm " + target);
        setNamespaceURI(qname.getNamespaceURI());
        setLocalPart(qname.getLocalPart());
        setPrefix(qname.getPrefix());
        System.out.println("      initialized to " + this);
    }

    transient String local;
    @Transient
    public String getLocalPart() {
        return local;
    }
    public void setLocalPart(String  local) {
        this.local=local;
    }
 
    transient String namespace;
    @Transient
    public String getNamespaceURI() {
        return namespace;
    }
    public void setNamespaceURI(String namespace) {
        this.namespace=namespace;
    }
    
    transient String prefix;
    @Transient
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix=prefix;
    }
}
