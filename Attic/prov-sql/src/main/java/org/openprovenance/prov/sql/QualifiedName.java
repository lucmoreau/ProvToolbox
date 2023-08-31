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
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.QualifiedNameUtils;
import org.openprovenance.prov.model.exception.QualifiedNameException;



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
	
	static final QualifiedNameUtils qnU=new QualifiedNameUtils();

    public QualifiedName() {} // for the purpose of persistence
	
    public QualifiedName(String namespaceURI, String localPart, String prefix) {
        this.namespace=namespaceURI;
        this.local=localPart;
        this.prefix=prefix;
    }
    
    public QualifiedName(QName id) {
    	this.namespace=id.getNamespaceURI();
    	this.local=qnU.escapeProvLocalName(qnU.unescapeFromXsdLocalName(id.getLocalPart()));
    	this.prefix=id.getPrefix();
    }


    @XmlAttribute(name = "pk")
    protected Long pk;

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#toQName()
     */
    @Override
    public javax.xml.namespace.QName toQName () {
    	String escapedLocal=qnU.escapeToXsdLocalName(qnU.unescapeProvLocalName(local));
	if (qnU.is_NC_Name(escapedLocal)) {
	    if (prefix==null) {
		return new javax.xml.namespace.QName(namespace,escapedLocal);
	    } else {
		return new javax.xml.namespace.QName(namespace,escapedLocal,prefix);
	    }
	} else {
	    throw new QualifiedNameException("PROV-XML QName: local not valid " + local);

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
     * @see org.openprovenance.prov.model.QualifiedName#getUri()
     */
    @Override
    @Basic
    @Column(name = "URI", columnDefinition="TEXT")
    public String getUri() {
	return this.getNamespaceURI()
		+ qnU.unescapeProvLocalName(getLocalPart());
    } 
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#setUri(java.lang.String)
     */
    @Override
    public void setUri(String uri) {} 
    
    @Basic@Column(name = "REFITEM")
    public String getRefItem() {
    	javax.xml.namespace.QName qname=this.toQName();
    	if (qname==null) return null;
    	return qname.toString();
    }

    public void setRefItem(String target) {
    	if (target!=null) {
    		javax.xml.namespace.QName qname=javax.xml.namespace.QName.valueOf(target);
    		setNamespaceURI(qname.getNamespaceURI());
    		setLocalPart(qnU.escapeProvLocalName(qnU.unescapeFromXsdLocalName(qname.getLocalPart())));
    		setPrefix(qname.getPrefix());
    	}
    }

    transient String local;
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#getLocalPart()
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
     * @see org.openprovenance.prov.model.QualifiedName#setNamespaceURI(java.lang.String)
     */
    @Override
    public void setNamespaceURI(String namespace) {
        this.namespace=namespace;
    }
    
    transient String prefix;
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#getPrefix()
     */
    @Override
    @Transient
    public String getPrefix() {
        return prefix;
    }
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#setPrefix(java.lang.String)
     */
    @Override
    public void setPrefix(String prefix) {
        this.prefix=prefix;
    }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(Object objectToTest) {
	// Is this the same object?
	if (objectToTest == this) {
	    return true;
	}
	// Is this a QualifiedName?
	if (objectToTest instanceof QualifiedName) {
	    QualifiedName qualifiedName = (QualifiedName) objectToTest;
	    return local.equals(qualifiedName.local) && namespace.equals(qualifiedName.namespace);
	}
    	
	return false;
    	
    }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#hashCode()
     */
    @Override
    public final int hashCode() {
        return namespace.hashCode() ^ local.hashCode();
    }

    public String toString() {
	return "'" + prefix + "::{" + namespace + "}" + local + "'";
    }

    	     
}
