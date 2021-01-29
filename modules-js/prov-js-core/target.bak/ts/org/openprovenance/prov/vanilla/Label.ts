/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class Label extends org.openprovenance.prov.vanilla.TypedValue implements org.openprovenance.prov.model.Label, org.openprovenance.prov.model.Attribute {
        static PROV_LABEL_KIND: Attribute.AttributeKind; public static PROV_LABEL_KIND_$LI$(): Attribute.AttributeKind { if (Label.PROV_LABEL_KIND == null) { Label.PROV_LABEL_KIND = Attribute.AttributeKind.PROV_LABEL; }  return Label.PROV_LABEL_KIND; }

        static PROV_LABEL_QualifiedName: org.openprovenance.prov.model.QualifiedName; public static PROV_LABEL_QualifiedName_$LI$(): org.openprovenance.prov.model.QualifiedName { if (Label.PROV_LABEL_QualifiedName == null) { Label.PROV_LABEL_QualifiedName = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LABEL; }  return Label.PROV_LABEL_QualifiedName; }

        /**
         * 
         * @return {*}
         */
        public getElementName(): org.openprovenance.prov.model.QualifiedName {
            return Label.PROV_LABEL_QualifiedName_$LI$();
        }

        /*private*/ setElementName(q: org.openprovenance.prov.model.QualifiedName) {
        }

        /**
         * 
         * @return {org.openprovenance.prov.model.Attribute.AttributeKind}
         */
        public getKind(): Attribute.AttributeKind {
            return Label.PROV_LABEL_KIND_$LI$();
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
    Label["__class"] = "org.openprovenance.prov.vanilla.Label";
    Label["__interfaces"] = ["org.openprovenance.prov.model.Label","org.openprovenance.prov.model.TypedValue","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Attribute","org.openprovenance.apache.commons.lang.builder.ToString"];


}

