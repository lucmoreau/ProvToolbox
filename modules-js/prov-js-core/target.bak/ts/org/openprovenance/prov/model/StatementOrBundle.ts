/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * 
     * Interface for a PROV Statement ({@link Statement}) (a unit of provenance description) or a Bundle ({@link Bundle}) (a named set of provenance statements)
     * @author lavm
     * @class
     */
    export interface StatementOrBundle {
        /**
         * Gets the type of a provenance statement
         * @return {org.openprovenance.prov.model.StatementOrBundle.Kind} {@link Kind}
         */
        getKind(): StatementOrBundle.Kind;
    }

    export namespace StatementOrBundle {

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
        export enum Kind {
            PROV_ENTITY, PROV_ACTIVITY, PROV_AGENT, PROV_USAGE, PROV_GENERATION, PROV_INVALIDATION, PROV_START, PROV_END, PROV_COMMUNICATION, PROV_DERIVATION, PROV_ASSOCIATION, PROV_ATTRIBUTION, PROV_DELEGATION, PROV_INFLUENCE, PROV_ALTERNATE, PROV_SPECIALIZATION, PROV_MENTION, PROV_MEMBERSHIP, PROV_BUNDLE, PROV_DICTIONARY_INSERTION, PROV_DICTIONARY_REMOVAL, PROV_DICTIONARY_MEMBERSHIP
        }
    }

}

