/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for strings with language attribute.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType name="InternationalizedString"&gt;
     * &lt;simpleContent&gt;
     * &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
     * &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/&gt;
     * &lt;/extension&gt;
     * &lt;/simpleContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     * @class
     */
    export interface LangString {
        /**
         * Gets the value of the value property.
         * 
         * @return
         * possible object is
         * {@link String}
         * 
         * @return {string}
         */
        getValue(): string;

        /**
         * Sets the value of the value property.
         * 
         * @param {string} value
         * allowed object is
         * {@link String}
         * 
         */
        setValue(value: string);

        /**
         * Gets the value of the lang property.
         * 
         * @return
         * possible object is
         * {@link String}
         * 
         * @return {string}
         */
        getLang(): string;

        /**
         * Sets the value of the lang property.
         * 
         * @param {string} value
         * allowed object is
         * {@link String}
         * 
         */
        setLang(value: string);
    }
}

