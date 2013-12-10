package org.openprovenance.prov.model;


public interface TypedValue {

    /**
     * Converts the value associated with the <tt>value</tt> property into a type.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public abstract Object convertValueToObject(ValueConverter vconv);

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link QualifiedName }
     *     
     */
    public abstract QualifiedName getType();
    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public abstract Object getValue();
    
    //FIXME: why do we need this?
    public abstract Object getValueAsObject();

    /**
     * Sets the value of the <tt>type</tt> property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualifiedName }
     *     
     */
    public abstract void setType(QualifiedName value);

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalizedString }
     *     
     */
    public abstract void setValue(InternationalizedString value);

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualifiedName }
     *     
     */
    public abstract void setValue(QualifiedName value);

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public abstract void setValue(String value);

    /**
     * Sets the value of the <tt>value</tt> property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public abstract void setValueFromObject(Object value);

}
