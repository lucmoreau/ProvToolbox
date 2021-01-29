/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class Location extends org.openprovenance.prov.vanilla.TypedValue implements org.openprovenance.prov.model.Location, org.openprovenance.prov.model.Attribute {
        static PROV_LOCATION_KIND: Attribute.AttributeKind; public static PROV_LOCATION_KIND_$LI$(): Attribute.AttributeKind { if (Location.PROV_LOCATION_KIND == null) { Location.PROV_LOCATION_KIND = Attribute.AttributeKind.PROV_LOCATION; }  return Location.PROV_LOCATION_KIND; }

        static PROV_LOCATION_QualifiedName: org.openprovenance.prov.model.QualifiedName; public static PROV_LOCATION_QualifiedName_$LI$(): org.openprovenance.prov.model.QualifiedName { if (Location.PROV_LOCATION_QualifiedName == null) { Location.PROV_LOCATION_QualifiedName = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LOCATION; }  return Location.PROV_LOCATION_QualifiedName; }

        /**
         * 
         * @return {*}
         */
        public getElementName(): org.openprovenance.prov.model.QualifiedName {
            return Location.PROV_LOCATION_QualifiedName_$LI$();
        }

        /*private*/ setElementName(q: org.openprovenance.prov.model.QualifiedName) {
        }

        /**
         * 
         * @return {org.openprovenance.prov.model.Attribute.AttributeKind}
         */
        public getKind(): Attribute.AttributeKind {
            return Location.PROV_LOCATION_KIND_$LI$();
        }

        /**
         * 
         * @return {string}
         */
        public toNotationString(): string {
            return org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(this.getElementName()) + " = " + org.openprovenance.prov.model.ProvUtilities.valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.getValue(), this.getType());
        }

        public constructor(type?: any, value?: any) {
            if (((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && ((value != null) || value === null)) {
                let __args = arguments;
                super(type, TypedValue.castToStringOrLangStringOrQualifiedName(value, type));
            } else if (type === undefined && value === undefined) {
                let __args = arguments;
                super();
            } else throw new Error('invalid overload');
        }
    }
    Location["__class"] = "org.openprovenance.prov.vanilla.Location";
    Location["__interfaces"] = ["org.openprovenance.prov.model.Location","org.openprovenance.prov.model.TypedValue","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Attribute","org.openprovenance.apache.commons.lang.builder.ToString"];


}

