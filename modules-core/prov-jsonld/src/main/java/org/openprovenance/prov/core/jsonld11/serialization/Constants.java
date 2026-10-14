package org.openprovenance.prov.core.jsonld11.serialization;

import java.util.Set;

public interface Constants {
    String PROPERTY_AT_TYPE = "@type";
    String PROPERTY_AT_VALUE = "@value";
    String PROPERTY_AT_ID = "@id";

    /**
     * The attribute terms the PROV-JSONLD context coerces to {@code @id}: under them a bare string is an IRI, so a
     * qualified name. Under any other key a bare string is a string, and a qualified name is a node reference
     * {@code {"@id": ...}} (https://github.com/openprov/prov-jsonld/issues/7). The openprov context's scoped terms are
     * coerced too; the scoped key (de)serialisers say so.
     */
    Set<String> ID_COERCED_ATTRIBUTE_TERMS = Set.of("type", "location", "role");

    String PROPERTY_STRING_VALUE = "@value";
    String PROPERTY_STRING_LANG = "@language";

    String PROPERTY_BLOCK_TYPE = "@type";

    String PROPERTY_PROV_TYPE = "prov:type";


    String PROPERTY_PROV_ACTIVITY = "Activity";
    String PROPERTY_PROV_AGENT = "Agent";
    String PROPERTY_PROV_ENTITY = "Entity";
    String PROPERTY_PROV_USED = "Usage";
    String PROPERTY_PROV_GENERATION = "Generation";
    String PROPERTY_PROV_ASSOCIATION = "Association";
    String PROPERTY_PROV_ATTRIBUTION = "Attribution";
    String PROPERTY_PROV_SPECIALIZATION = "Specialization";
    String PROPERTY_PROV_ALTERNATE = "Alternate";
    String PROPERTY_PROV_DERIVATION = "Derivation";
    String PROPERTY_PROV_COMMUNICATION = "Communication";
    String PROPERTY_PROV_INFLUENCE = "Influence";
    String PROPERTY_PROV_MEMBERSHIP = "Membership";
    String PROPERTY_PROV_INVALIDATION = "Invalidation";
    String PROPERTY_PROV_START = "Start";
    String PROPERTY_PROV_END = "End";
    String PROPERTY_PROV_DELEGATION = "Delegation";
    String PROPERTY_PROV_BUNDLE = "Bundle";

    String JSONLD_CONTEXT_URL = "https://openprovenance.org/prov-jsonld/context.jsonld";
    /** The openprov context, which extends the PROV-JSONLD one: cited by a document that uses openprov terms. */
    String OPENPROV_CONTEXT_URL = "https://openprovenance.org/ns/openprov.jsonld";
    String OPENPROV_CONTEXT_RESOURCE = "openprov-context/openprov.jsonld";
    /** The PROV-JSONLD schema extended, by reference, with the openprov context's terms; a document citing the openprov context validates against it. */
    String OPENPROV_SCHEMA_URL = "https://openprovenance.org/ns/openprov-schema.json";
    /** What the extension references; the module's copy at JSONLDSCHEMA_2026_10_14 stands in for it. */
    String JSONLD_SCHEMA_URL = "https://openprovenance.org/prov-jsonld/schema.json";
    String OPENPROV_SCHEMA_RESOURCE = "openprov-context/openprov-schema.json";
    /** The schema of the W3C member submission, https://www.w3.org/submissions/2024/SUBM-prov-jsonld-20240825/schema.json. */
    String JSONLDSCHEMA_2024_08_25 = "2024-08-25/jsonldschema.json";
    /**
     * The 2024-08-25 schema admitting a node reference {@code {"@id": ...}} as an attribute value, the form of a
     * qualified name under a key the context does not coerce to {@code @id} (https://github.com/openprov/prov-jsonld/issues/7).
     */
    String JSONLDSCHEMA_2026_10_14 = "2026-10-14/jsonldschema.json";
    String JSONLDCONTEXT_2024_08_25 = "2024-08-25/jsonldcontext.jsonld";

    String OLD_JSONLD_CONTEXT_URL = "http://openprovenance.org/prov.jsonld";

    String JSONLDSCHEMA_JSON_2024_06_11 = "src/main/resources/2024-06-11/jsonldschema.json";
    String JSONLDSCHEMA_JSON_2020_03_23 = "src/main/resources/2020-03-23/jsonldschema.json";

}
