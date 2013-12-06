package org.openprovenance.prov.model;


public interface QualifiedName {

    public javax.xml.namespace.QName toQName();


    public String getUri();

    public void setUri(String uri);

    public String getLocalPart();

    public void setLocalPart(String local);

    public String getNamespaceURI();

    public void setNamespaceURI(String namespace);

    
    public String getPrefix();

    public void setPrefix(String prefix);

    public boolean equals(Object objectToTest);

    /**
     * <p>Generate the hash code for this <code>QualifiedName</code>.</p>
     *
     * <p>The hash code is calculated using both the Namespace URI and
     * the local part of the <code>QualifiedName</code>.  The prefix is
     * <strong><em>NOT</em></strong> used to calculate the hash
     * code.</p>
     *
     * <p>This method satisfies the general contract of {@link
     * java.lang.Object#hashCode() Object.hashCode()}.</p>
     *
     * @return hash code for this <code>QualifiedName</code> <code>Object</code>
     */
    public abstract int hashCode();

}