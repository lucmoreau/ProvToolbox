package org.openprovenance.prov.model;


/** 
 * <p> Interface for a PROV attribute-value pair.
 * <p> Attribute-value pairs are meant to provide further descriptions to (most) {@link Statement}.
 * 
 * @author lavm
 *
 */
public interface Attribute extends TypedValue {

    /**
     * Enumerated type for all types of attributes. Some are predefined PROV attributes. 
     * All the others are classed as "OTHER".
     * 
     * @author lavm
     *
     */
    enum AttributeKind {
        PROV_TYPE,
        PROV_LABEL,
        PROV_ROLE,
        PROV_LOCATION,
        PROV_VALUE,
        PROV_KEY,
        OTHER
    }

    QualifiedName getQualifiedName(AttributeKind kind);

    AttributeKind getAttributeKind(QualifiedName q);

    QualifiedName getElementName();

    AttributeKind getKind();

    
    /** Get the type of an Attribute 
     * @return  possible object of {@link String}, {@link QualifiedName}, {@link LangString}
     */
    
    Object getValue();

    
    /** Get the type of an Attribute 
     * @return  possible instance of  {@link QualifiedName}
     */
    
    QualifiedName getType();

    /** A method to generate the prov-n representation of an attribute  ex:attr="value" %% xsd:type
     *
     * @return a string representation of the attribute
     */

    String toNotationString();
    
    /** Returns the value of an Attribute as a Java Object.
     *
     * @return a Java Object
     * */

    Object getConvertedValue();

}
