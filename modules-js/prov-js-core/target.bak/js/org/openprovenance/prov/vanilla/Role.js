/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class Role extends org.openprovenance.prov.vanilla.TypedValue {
                    constructor(type, value) {
                        if (((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && ((value != null) || value === null)) {
                            let __args = arguments;
                            super(type, vanilla.TypedValue.castToStringOrLangStringOrQualifiedName(value, type));
                        }
                        else if (type === undefined && value === undefined) {
                            let __args = arguments;
                            super();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static PROV_ROLE_KIND_$LI$() { if (Role.PROV_ROLE_KIND == null) {
                        Role.PROV_ROLE_KIND = Attribute.AttributeKind.PROV_ROLE;
                    } return Role.PROV_ROLE_KIND; }
                    static PROV_ROLE_QualifiedName_$LI$() { if (Role.PROV_ROLE_QualifiedName == null) {
                        Role.PROV_ROLE_QualifiedName = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_ROLE;
                    } return Role.PROV_ROLE_QualifiedName; }
                    /**
                     *
                     * @return {*}
                     */
                    getElementName() {
                        return Role.PROV_ROLE_QualifiedName_$LI$();
                    }
                    /*private*/ setElementName(q) {
                    }
                    /**
                     *
                     * @return {org.openprovenance.prov.model.Attribute.AttributeKind}
                     */
                    getKind() {
                        return Role.PROV_ROLE_KIND_$LI$();
                    }
                    /**
                     *
                     * @return {string}
                     */
                    toNotationString() {
                        return org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(this.getElementName()) + " = " + org.openprovenance.prov.model.ProvUtilities.valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.getValue(), this.getType());
                    }
                }
                vanilla.Role = Role;
                Role["__class"] = "org.openprovenance.prov.vanilla.Role";
                Role["__interfaces"] = ["org.openprovenance.prov.model.Role", "org.openprovenance.prov.model.TypedValue", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Attribute", "org.openprovenance.apache.commons.lang.builder.ToString"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
