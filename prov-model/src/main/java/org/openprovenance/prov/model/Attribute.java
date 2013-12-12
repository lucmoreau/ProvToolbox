package org.openprovenance.prov.model;


/** 
 * <p> Interface for a PROV attribute-value pair.
 * <p> Attribute-value pairs are meant to provide further descriptions to (most) {@link Statement}.
 * 
 * @author lavm
 *
 */
public interface Attribute extends TypedValue {

    public enum AttributeKind {
	PROV_TYPE,
	PROV_LABEL,
	PROV_ROLE,
	PROV_LOCATION,
	PROV_VALUE,
	PROV_KEY,
	OTHER	
    }

    public abstract QualifiedName getQualifiedName(AttributeKind kind);

    public abstract AttributeKind getAttributeKind(QualifiedName q);

    public abstract QualifiedName getElementName();

    public abstract AttributeKind getKind();

    
    /** Get the type of an Attribute 
     * @return  possible object of {@link String}, {@link QualifiedName}, {@link LangString}
     */
    
    public abstract Object getValue();

    
    /** Get the type of an Attribute 
     * @return  possible instance of  {@link QualifiedName}
     */
    
    public abstract QualifiedName getType();

    /** A method to generate the prov-n representation of an attribute  ex:attr="value" %% xsd:type */

    public abstract String toNotationString();
    
    /** Returns the value of an Attribute as a Java Object. */
    
    public Object getConvertedValue();

}
