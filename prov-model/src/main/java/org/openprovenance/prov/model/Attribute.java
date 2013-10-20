package org.openprovenance.prov.model;

import javax.xml.namespace.QName;

public interface Attribute {

    public enum AttributeKind {
	PROV_TYPE,
	PROV_LABEL,
	PROV_ROLE,
	PROV_LOCATION,
	PROV_VALUE,
	OTHER	
    }

    public abstract QName getQName(AttributeKind kind);

    public abstract AttributeKind getAttributeKind(QName q);

    public abstract QName getElementName();

    public abstract AttributeKind getKind();

    
    /** Get the type of an Attribute 
     * @return  possible object of {@link String}, {@link QName}, {@link InternationalizedString}
     */
    
    public abstract Object getValue();

    
    /** Get the type of an Attribute 
     * @return  possible instance of  {@link QName}
     */
    
    public abstract QName getType();

    /** A method to generate the prov-n representation of an attribute  ex:attr="value" %% xsd:type */

    public abstract String toNotationString();
    
    /** Returns the value of an Attribute as a Java Object. */
    public Object getValueAsObject();

}
