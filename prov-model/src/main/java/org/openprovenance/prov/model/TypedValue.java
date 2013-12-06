package org.openprovenance.prov.model;


public interface TypedValue {

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public abstract Object getValue();

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public abstract void setValue(Object value);

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
    public abstract Object getValueAsObject(ValueConverter vconv);

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public abstract void setValueAsObject(Object valueAsJava);

    /*
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualifiedName }
     *     
     */
    public abstract void setType(QualifiedName value);

    public abstract Object getValueAsObject();

}
