/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class Location extends org.openprovenance.prov.vanilla.TypedValue {
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
                    static PROV_LOCATION_KIND_$LI$() { if (Location.PROV_LOCATION_KIND == null) {
                        Location.PROV_LOCATION_KIND = Attribute.AttributeKind.PROV_LOCATION;
                    } return Location.PROV_LOCATION_KIND; }
                    static PROV_LOCATION_QualifiedName_$LI$() { if (Location.PROV_LOCATION_QualifiedName == null) {
                        Location.PROV_LOCATION_QualifiedName = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LOCATION;
                    } return Location.PROV_LOCATION_QualifiedName; }
                    /**
                     *
                     * @return {*}
                     */
                    getElementName() {
                        return Location.PROV_LOCATION_QualifiedName_$LI$();
                    }
                    /*private*/ setElementName(q) {
                    }
                    /**
                     *
                     * @return {org.openprovenance.prov.model.Attribute.AttributeKind}
                     */
                    getKind() {
                        return Location.PROV_LOCATION_KIND_$LI$();
                    }
                    /**
                     *
                     * @return {string}
                     */
                    toNotationString() {
                        return org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(this.getElementName()) + " = " + org.openprovenance.prov.model.ProvUtilities.valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.getValue(), this.getType());
                    }
                }
                vanilla.Location = Location;
                Location["__class"] = "org.openprovenance.prov.vanilla.Location";
                Location["__interfaces"] = ["org.openprovenance.prov.model.Location", "org.openprovenance.prov.model.TypedValue", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Attribute", "org.openprovenance.apache.commons.lang.builder.ToString"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
