package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.prov.model.QualifiedNameUtils;
import org.openprovenance.prov.model.exception.QualifiedNameException;

public class QualifiedName implements org.openprovenance.prov.model.QualifiedName {


    static final QualifiedNameUtils qnU = new QualifiedNameUtils();
    private String namespace;
    private String local;
    private String prefix;

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
        return this.getNamespaceURI()
                + this.getUnescapedLocalPart();
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
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }


    public String toString() {
        return "'" + prefix + ":{{" + namespace + "}}" + local + "'";
    }

}


