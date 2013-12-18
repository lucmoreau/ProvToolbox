package org.openprovenance.prov.model;

/** 
 * Interface for a PROV Statement ({@link Statement}) (a unit of provenance description) or a Bundle ({@link NamedBundle}) (a named set of provenance statements)
 * @author lavm
 *
 */
public interface StatementOrBundle {

    /** Gets the type of a provenance statement
     * @return {@link Kind}
     */
    public Kind getKind();

    /** Enumerated type for each type of provenance statement or bundle. */
    public enum Kind {
	
	PROV_ENTITY,
	PROV_ACTIVITY,
	PROV_AGENT,
	PROV_USAGE,
	PROV_GENERATION,
	PROV_INVALIDATION,
	PROV_START,
	PROV_END,
	PROV_COMMUNICATION,
	PROV_DERIVATION,
	PROV_ASSOCIATION,
	PROV_ATTRIBUTION,
	PROV_DELEGATION,
	PROV_INFLUENCE,
	PROV_ALTERNATE,
	PROV_SPECIALIZATION,
	PROV_MENTION,
	PROV_MEMBERSHIP,
	PROV_BUNDLE,
	PROV_DICTIONARY_INSERTION,
	PROV_DICTIONARY_REMOVAL,
	PROV_DICTIONARY_MEMBERSHIP
    }

}
