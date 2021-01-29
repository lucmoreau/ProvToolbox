/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var model;
            (function (model) {
                let Attribute;
                (function (Attribute) {
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
                    let AttributeKind;
                    (function (AttributeKind) {
                        AttributeKind[AttributeKind["PROV_TYPE"] = 0] = "PROV_TYPE";
                        AttributeKind[AttributeKind["PROV_LABEL"] = 1] = "PROV_LABEL";
                        AttributeKind[AttributeKind["PROV_ROLE"] = 2] = "PROV_ROLE";
                        AttributeKind[AttributeKind["PROV_LOCATION"] = 3] = "PROV_LOCATION";
                        AttributeKind[AttributeKind["PROV_VALUE"] = 4] = "PROV_VALUE";
                        AttributeKind[AttributeKind["PROV_KEY"] = 5] = "PROV_KEY";
                        AttributeKind[AttributeKind["OTHER"] = 6] = "OTHER";
                    })(AttributeKind = Attribute.AttributeKind || (Attribute.AttributeKind = {}));
                })(Attribute = model.Attribute || (model.Attribute = {}));
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
