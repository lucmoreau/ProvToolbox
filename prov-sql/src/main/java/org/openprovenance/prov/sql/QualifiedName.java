package org.openprovenance.prov.sql;

import javax.persistence.Basic;
import javax.persistence.NamedQuery;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.QNameAsString;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XmlAdapterUtils;


/**
 * 
 * 
 */
@Entity(name = "QualifiedName")
@NamedQuery(name="QualifiedName.Find", query="SELECT e FROM QualifiedName e WHERE e.uri LIKE :uri")


@javax.persistence.Cacheable 
@Table(name = "QUALIFIEDNAME", uniqueConstraints=@javax.persistence.UniqueConstraint(columnNames={"URI"}))
@Inheritance(strategy = InheritanceType.JOINED)
public class QualifiedName
 implements org.openprovenance.prov.model.QualifiedName
    
{

    public QualifiedName() {} // for the purpose of persistence
	
    public QualifiedName(String namespaceURI, String localPart, String prefix) {
        //super(namespaceURI, localPart, prefix);
        this.namespace=namespaceURI;
        this.local=localPart;
        this.prefix=prefix;
    }

    @XmlAttribute(name = "pk")
    protected Long pk;

    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#toQName()
     */
    @Override
    public javax.xml.namespace.QName toQName () {
    	if (prefix==null) {
    		return new javax.xml.namespace.QName(namespace,local);		
    	} else {
    		return new javax.xml.namespace.QName(namespace,local,prefix);
    	}
    }

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
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#getUri()
     */
    @Override
    @Basic
    @Column(name = "URI")
    public String getUri() {
	return this.getNamespaceURI()
		+ this.getLocalPart();
    } 
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#setUri(java.lang.String)
     */
    @Override
    public void setUri(String uri) {} 
    
    @Basic@Column(name = "REFITEM")
    public String getRefItem() {
        return XmlAdapterUtils.unmarshall(QNameAsString.class, this.toQName());
	//return Helper2.QNameToString(this.toQName());
    }

    public void setRefItem(String target) {
	javax.xml.namespace.QName qname=XmlAdapterUtils.marshall(QNameAsString.class, target);
	//javax.xml.namespace.QName qname=Helper2.stringToQName(target);
        setNamespaceURI(qname.getNamespaceURI());
        setLocalPart(qname.getLocalPart());
        setPrefix(qname.getPrefix());
        //System.out.println("      initialized to " + this);
    }

    transient String local;
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#getLocalPart()
     */
    @Override
    @Transient
    public String getLocalPart() {
        return local;
    }
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#setLocalPart(java.lang.String)
     */
    @Override
    public void setLocalPart(String  local) {
        this.local=local;
    }
 
    transient String namespace;
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#getNamespaceURI()
     */
    @Override
    @Transient
    public String getNamespaceURI() {
        return namespace;
    }
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#setNamespaceURI(java.lang.String)
     */
    @Override
    public void setNamespaceURI(String namespace) {
        this.namespace=namespace;
    }
    
    transient String prefix;
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#getPrefix()
     */
    @Override
    @Transient
    public String getPrefix() {
        return prefix;
    }
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#setPrefix(java.lang.String)
     */
    @Override
    public void setPrefix(String prefix) {
        this.prefix=prefix;
    }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(Object objectToTest) {
    	             // Is this the same object?
    	             if (objectToTest == this) {
    	                 return true;
    	             }
    	             // Is this a QName?
    	             if (objectToTest instanceof QualifiedName) {
    	                 QualifiedName qName = (QualifiedName) objectToTest;
    	                 return local.equals(qName.local) && namespace.equals(qName.namespace);
    	             }
    	  
           return false;
    	  
       }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#hashCode()
     */
    @Override
    public final int hashCode() {
        return namespace.hashCode() ^ local.hashCode();
    }

    public String toString() {
	return "'" + prefix + "::{" + namespace + "}" + local + "'";
    }

    	     
}
