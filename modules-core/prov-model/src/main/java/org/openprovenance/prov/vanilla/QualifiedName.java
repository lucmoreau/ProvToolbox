package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.prov.model.QualifiedNameUtils;
import org.openprovenance.prov.model.exception.QualifiedNameException;

public class QualifiedName implements org.openprovenance.prov.model.QualifiedName {


    static final QualifiedNameUtils qnU = new QualifiedNameUtils();
    private String namespace;
    private String local;
    private String prefix;
    private int hash; // cached hashCode; 0 means not yet computed
    private String uri; // cached getUri(); null means not yet computed

    public QualifiedName(String namespaceURI, String localPart, String prefix) {
        this.namespace = namespaceURI;
        this.local = localPart;
        this.prefix = prefix;
    }

    private QualifiedName() {

    }


    protected org.openprovenance.prov.model.QualifiedName ref;


    @Override
    public javax.xml.namespace.QName toQName() {
        String escapedLocal = qnU.escapeToXsdLocalName(getUnescapedLocalPart());
        if (qnU.is_NC_Name(escapedLocal)) {
            if (prefix == null) {
                return new javax.xml.namespace.QName(namespace, escapedLocal);
            } else {
                return new javax.xml.namespace.QName(namespace, escapedLocal, prefix);
            }
        } else {
            throw new QualifiedNameException("PROV-XML QName: local not valid " + escapedLocal);

        }
    }


    private String getUnescapedLocalPart() {
        return qnU.unescapeProvLocalName(local);
    }

    @Override
    public String getUri() {
        String u = uri;
        if (u == null) {
            u = this.getNamespaceURI() + this.getUnescapedLocalPart();
            uri = u;
        }
        return u;
    }


    @Override
    public void setUri(String uri) {

    }

    @Override
    public String getLocalPart() {
        return local;
    }

    @Override
    public void setLocalPart(String local) {

    }

    @Override
    public String getNamespaceURI() {
        return namespace;
    }

    @Override
    public void setNamespaceURI(String namespaceURI) {

    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(String prefix) {

    }

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


    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.local);
        hashCodeBuilder.append(this.namespace);
    }
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedName#hashCode()
     */
    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0) {
            // Inlined equivalent of `new HashCodeBuilder().append(local).append(namespace).toHashCode()`
            // (initial=17, multiplier=37). Preserves the exact value previously produced.
            h = 17 * 37 + (local == null ? 0 : local.hashCode());
            h = h * 37 + (namespace == null ? 0 : namespace.hashCode());
            hash = h;
        }
        return h;
    }


    public String toString() {
        return "'" + prefix + ":{{" + namespace + "}}" + local + "'";
    }

}


