/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var model;
            (function (model) {
                let StatementOrBundle;
                (function (StatementOrBundle) {
                    /**
                     * Enumerated type for each type of provenance statement or bundle.
                     * @enum
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_ENTITY
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_ACTIVITY
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_AGENT
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_USAGE
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_GENERATION
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_INVALIDATION
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_START
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_END
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_COMMUNICATION
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_DERIVATION
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_ASSOCIATION
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_ATTRIBUTION
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_DELEGATION
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_INFLUENCE
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_ALTERNATE
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_SPECIALIZATION
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_MENTION
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_MEMBERSHIP
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_BUNDLE
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_DICTIONARY_INSERTION
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_DICTIONARY_REMOVAL
                     * @property {org.openprovenance.prov.model.StatementOrBundle.Kind} PROV_DICTIONARY_MEMBERSHIP
                     * @class
                     */
                    let Kind;
                    (function (Kind) {
                        Kind[Kind["PROV_ENTITY"] = 0] = "PROV_ENTITY";
                        Kind[Kind["PROV_ACTIVITY"] = 1] = "PROV_ACTIVITY";
                        Kind[Kind["PROV_AGENT"] = 2] = "PROV_AGENT";
                        Kind[Kind["PROV_USAGE"] = 3] = "PROV_USAGE";
                        Kind[Kind["PROV_GENERATION"] = 4] = "PROV_GENERATION";
                        Kind[Kind["PROV_INVALIDATION"] = 5] = "PROV_INVALIDATION";
                        Kind[Kind["PROV_START"] = 6] = "PROV_START";
                        Kind[Kind["PROV_END"] = 7] = "PROV_END";
                        Kind[Kind["PROV_COMMUNICATION"] = 8] = "PROV_COMMUNICATION";
                        Kind[Kind["PROV_DERIVATION"] = 9] = "PROV_DERIVATION";
                        Kind[Kind["PROV_ASSOCIATION"] = 10] = "PROV_ASSOCIATION";
                        Kind[Kind["PROV_ATTRIBUTION"] = 11] = "PROV_ATTRIBUTION";
                        Kind[Kind["PROV_DELEGATION"] = 12] = "PROV_DELEGATION";
                        Kind[Kind["PROV_INFLUENCE"] = 13] = "PROV_INFLUENCE";
                        Kind[Kind["PROV_ALTERNATE"] = 14] = "PROV_ALTERNATE";
                        Kind[Kind["PROV_SPECIALIZATION"] = 15] = "PROV_SPECIALIZATION";
                        Kind[Kind["PROV_MENTION"] = 16] = "PROV_MENTION";
                        Kind[Kind["PROV_MEMBERSHIP"] = 17] = "PROV_MEMBERSHIP";
                        Kind[Kind["PROV_BUNDLE"] = 18] = "PROV_BUNDLE";
                        Kind[Kind["PROV_DICTIONARY_INSERTION"] = 19] = "PROV_DICTIONARY_INSERTION";
                        Kind[Kind["PROV_DICTIONARY_REMOVAL"] = 20] = "PROV_DICTIONARY_REMOVAL";
                        Kind[Kind["PROV_DICTIONARY_MEMBERSHIP"] = 21] = "PROV_DICTIONARY_MEMBERSHIP";
                    })(Kind = StatementOrBundle.Kind || (StatementOrBundle.Kind = {}));
                })(StatementOrBundle = model.StatementOrBundle || (model.StatementOrBundle = {}));
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
