package org.openprovenance.prov.core.jsonld11.serialization;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.openprovenance.prov.model.NamespacePrefixMapper.OPENPROV_NS;
import static org.openprovenance.prov.model.NamespacePrefixMapper.OPENPROV_PREFIX;

/**
 * The terms the openprov context, https://openprovenance.org/ns/openprov.jsonld, scopes to three relations.
 * Inside an Attribution, a Membership or a Specialization, a bare key such as {@code association} names an
 * {@code openprov:had...} property, as {@code generation} names {@code prov:hadGeneration} inside a Derivation.
 * The (de)serialisers are no JSON-LD processors: this table is what they know of that context, and the test
 * suite holds it to the published file.
 */
public class OpenprovTerms {

    static final ProvFactory pf = ProvFactory.getFactory();

    private static final Map<Kind, Map<String, QualifiedName>> TERM_TO_PROPERTY = new LinkedHashMap<>();
    private static final Map<Kind, Map<QualifiedName, String>> PROPERTY_TO_TERM = new LinkedHashMap<>();

    static {
        scope(Kind.PROV_ATTRIBUTION,
                "activity", "hadActivity",
                "association", "hadAssociation",
                "generation", "hadGeneration",
                "invalidation", "hadInvalidation",
                "entityDerivation", "hadEntityDerivation",
                "agentDerivation", "hadAgentDerivation");
        scope(Kind.PROV_MEMBERSHIP,
                "activity", "hadActivity",
                "generation", "hadGeneration",
                "usage", "hadUsage",
                "collectionGeneration", "hadCollectionGeneration",
                "itemGeneration", "hadItemGeneration");
        scope(Kind.PROV_SPECIALIZATION,
                "entity", "hadEntity",
                "derivation", "hadDerivation",
                "specialization", "hadSpecialization");
    }

    private static void scope(Kind kind, String... termAndLocal) {
        Map<String, QualifiedName> forward = new LinkedHashMap<>();
        Map<QualifiedName, String> backward = new LinkedHashMap<>();
        for (int i = 0; i < termAndLocal.length; i += 2) {
            QualifiedName property = pf.newQualifiedName(OPENPROV_NS, termAndLocal[i + 1], OPENPROV_PREFIX);
            forward.put(termAndLocal[i], property);
            backward.put(property, termAndLocal[i]);
        }
        TERM_TO_PROPERTY.put(kind, Collections.unmodifiableMap(forward));
        PROPERTY_TO_TERM.put(kind, Collections.unmodifiableMap(backward));
    }

    /** The property a bare term names inside a relation of this kind, or null when the term is not scoped there. */
    public static QualifiedName property(Kind kind, String term) {
        Map<String, QualifiedName> scope = TERM_TO_PROPERTY.get(kind);
        return scope == null ? null : scope.get(term);
    }

    /** The bare term that names a property inside a relation of this kind, or null when the property is not scoped there. */
    public static String term(Kind kind, QualifiedName property) {
        Map<QualifiedName, String> scope = PROPERTY_TO_TERM.get(kind);
        return scope == null ? null : scope.get(property);
    }

    /** The scoped terms of a kind, term to property, in declaration order; empty for a kind with none. */
    public static Map<String, QualifiedName> scope(Kind kind) {
        return TERM_TO_PROPERTY.getOrDefault(kind, Collections.emptyMap());
    }
}
