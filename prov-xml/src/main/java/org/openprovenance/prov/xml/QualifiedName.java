package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.QualifiedNameUtils;
import org.openprovenance.prov.model.exception.QualifiedNameException;


/**
 * <p>Java class for QualifiedName complex type.
 * 
 * 
 */


public class QualifiedName
 implements org.openprovenance.prov.model.QualifiedName
    
{
    
    final QualifiedNameUtils qnU=new QualifiedNameUtils();

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

    @XmlAttribute(name = "ref", namespace = "http://www.w3.org/ns/prov#", required = true)
    protected org.openprovenance.prov.model.QualifiedName ref;

    /* (non-Javadoc)
     * @see org.opeprovenance.prov.model.QualifiedName#toQName()
     */
    @Override
    public javax.xml.namespace.QName toQName () {
	String escapedLocal=qnU.escapeToXsdLocalName(getUnescapedLocalPart());
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

    public String getUnescapedLocalPart() {
	return qnU.unescapeProvLocalName(local);
    }
    

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#getUri()
     */
    @Override
    public String getUri() {
	return this.getNamespaceURI()
		+ this.getUnescapedLocalPart();
    } 
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#setUri(java.lang.String)
     */
    @Override
    public void setUri(String uri) {} 
    
    

  

    transient String local;
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#getLocalPart()
     */
    @Override
    public String getLocalPart() {
        return local;
    }
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#setLocalPart(java.lang.String)
     */
    @Override
    public void setLocalPart(String  local) {
        this.local=local;
    }
 
    transient String namespace;
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#getNamespaceURI()
     */
    @Override
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
	return "'" + prefix + ":{" + namespace + "}" + local + "'";
    }

    	     
}

