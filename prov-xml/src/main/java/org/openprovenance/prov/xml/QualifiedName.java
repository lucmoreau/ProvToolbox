package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for QualifiedName complex type.
 * 
 * 
 */


public class QualifiedName
 implements org.openprovenance.prov.model.QualifiedName
    
{

    public QualifiedName(String namespaceURI, String localPart, String prefix) {
        //super(namespaceURI, localPart, prefix);
        this.namespace=namespaceURI;
        this.local=localPart;
        this.prefix=prefix;
    }

    public QualifiedName(QName id) {
	this.namespace=id.getNamespaceURI();
	this.local=id.getLocalPart();
	this.prefix=id.getPrefix();
    }

    @XmlAttribute(name = "ref", namespace = "http://www.w3.org/ns/prov#", required = true)
    protected org.openprovenance.prov.model.QualifiedName ref;

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
    

    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#getUri()
     */
    @Override
    public String getUri() {
	return this.getNamespaceURI()
		+ this.getLocalPart();
    } 
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#setUri(java.lang.String)
     */
    @Override
    public void setUri(String uri) {} 
    
    

  

    transient String local;
    /* (non-Javadoc)
     * @see org.openprovenance.prov.sql.QualifiedName#getLocalPart()
     */
    @Override
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
	return "'" + prefix + ":{" + namespace + "}" + local + "'";
    }

    	     
}

