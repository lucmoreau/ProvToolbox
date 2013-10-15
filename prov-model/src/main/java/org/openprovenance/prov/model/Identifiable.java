package org.openprovenance.prov.model;
import javax.xml.namespace.QName;

public interface Identifiable {
    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public abstract QName getId();

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public abstract void setId(QName value);
} 
