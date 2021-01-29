/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * 
     * <p>Interface for a PROV value.
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-value">PROV-DM Definition for Value</a>:
     * A value is a constant such as a string, number, time, qualified name, IRI, and encoded binary data,
     * whose interpretation is outside the scope of PROV. Values can occur in attribute-value pairs.
     * 
     * <p>In the Java implementation, a TypedValue can be seen as an object with two properties: a {@code value} and a {@code type}.
     * 
     * <p>The {@code type} is expressed as a QualifiedName, whereas the {@code value} is normally expected to be a string.
     * (Two notable exceptions are qualified names whose external representation is dependent on the choice of
     * prefix, and langString which also have a language property.
     * 
     * <p><span class="strong">Relevant Factory Methods:</span>
     * <ul>
     * <li> {@link ProvFactory#newKey(Object, QualifiedName)}
     * <li> {@link ProvFactory#newLocation(Object, QualifiedName)}
     * <li> {@link ProvFactory#newOther(QualifiedName, Object, QualifiedName)}
     * <li> {@link ProvFactory#newRole(Object, QualifiedName)}
     * <li> {@link ProvFactory#newType(Object, QualifiedName)}
     * <li> {@link ProvFactory#newValue(Object, QualifiedName)}
     * </ul>
     * 
     * <p><span class="strong">Schema Definition:</span>
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType name="Location"&gt;
     * &lt;simpleContent&gt;
     * &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;anySimpleType"&gt;
     * &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}QName" /&gt;
     * &lt;/extension&gt;
     * &lt;/simpleContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * <p>
     * @see <a href="http://www.w3.org/TR/prov-dm/#term-value">PROV-DM Value</a>
     * @see <a href="http://www.w3.org/TR/prov-n/#expression-literal">PROV-N Literal</a>
     * @see <a href="http://www.w3.org/TR/prov-xml/#type-value">PROV-XML Value</a>
     * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#data-typing">PROV-JSON Data Typing</a>
     * @class
     */
    export interface TypedValue {
        /**
         * Converts the value associated with the {@code value} property into a Java object.
         * 
         * @return
         * possible object is
         * {@link Object}
         * 
         * @param {org.openprovenance.prov.model.ValueConverter} vconv
         * @return {*}
         */
        convertValueToObject(vconv: org.openprovenance.prov.model.ValueConverter): any;

        /**
         * Gets the type, expressed as a {@link QualifiedName}
         * 
         * @return
         * possible object is
         * {@link QualifiedName}
         * 
         * @return {*}
         */
        getType(): org.openprovenance.prov.model.QualifiedName;

        /**
         * Gets the value of the value property.
         * @return  {*} possible object of {@link String}, {@link QualifiedName}, {@link LangString}
         * 
         */
        getValue(): any;

        /**
         * Returns the cached converted value for the {@code value} property .
         * 
         * @return
         * possible object is
         * {@link Object}
         * 
         * @return {*}
         */
        getConvertedValue(): any;

        /**
         * Sets the value of the {@code type} property.
         * 
         * @param {*} value
         * allowed object is
         * {@link QualifiedName}
         * 
         */
        setType(value: org.openprovenance.prov.model.QualifiedName);

        /**
         * Sets the {@code value} property.
         * 
         * @param {*} value
         * allowed object is
         * {@link LangString}
         * 
         */
        setValue(value?: any);

        /**
         * Sets the value of the {@code value} property.
         * 
         * @param {*} value
         * allowed object is
         * {@link Object}
         * 
         */
        setValueFromObject(value: any);
    }
}

