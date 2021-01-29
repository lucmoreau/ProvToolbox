/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * 
     * <p> Interface for a PROV attribute-value pair.
     * <p> Attribute-value pairs are meant to provide further descriptions to (most) {@link Statement}.
     * 
     * @author lavm
     * @class
     */
    export interface Attribute extends org.openprovenance.prov.model.TypedValue {
        getQualifiedName(kind: Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName;

        getAttributeKind(q: org.openprovenance.prov.model.QualifiedName): Attribute.AttributeKind;

        getElementName(): org.openprovenance.prov.model.QualifiedName;

        getKind(): Attribute.AttributeKind;

        /**
         * Get the type of an Attribute
         * @return  {*} possible object of {@link String}, {@link QualifiedName}, {@link LangString}
         */
        getValue(): any;

        /**
         * Get the type of an Attribute
         * @return  {*} possible instance of  {@link QualifiedName}
         */
        getType(): org.openprovenance.prov.model.QualifiedName;

        /**
         * A method to generate the prov-n representation of an attribute  ex:attr="value" %% xsd:type
         * @return {string}
         */
        toNotationString(): string;

        /**
         * Returns the value of an Attribute as a Java Object.
         * @return {*}
         */
        getConvertedValue(): any;
    }

    export namespace Attribute {

        /**
         * Enumerated type for all types of attributes. Some are predefined PROV attributes.
         * All the others are classed as "OTHER".
         * 
         * @author lavm
         * @enum
         * @property {org.openprovenance.prov.model.Attribute.AttributeKind} PROV_TYPE
         * @property {org.openprovenance.prov.model.Attribute.AttributeKind} PROV_LABEL
         * @property {org.openprovenance.prov.model.Attribute.AttributeKind} PROV_ROLE
         * @property {org.openprovenance.prov.model.Attribute.AttributeKind} PROV_LOCATION
         * @property {org.openprovenance.prov.model.Attribute.AttributeKind} PROV_VALUE
         * @property {org.openprovenance.prov.model.Attribute.AttributeKind} PROV_KEY
         * @property {org.openprovenance.prov.model.Attribute.AttributeKind} OTHER
         * @class
         */
        export enum AttributeKind {
            PROV_TYPE, PROV_LABEL, PROV_ROLE, PROV_LOCATION, PROV_VALUE, PROV_KEY, OTHER
        }
    }

}

