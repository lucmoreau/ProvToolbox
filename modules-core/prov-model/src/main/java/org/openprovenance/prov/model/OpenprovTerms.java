package org.openprovenance.prov.model;

import org.openprovenance.prov.model.StatementOrBundle.Kind;

import java.util.*;

/**
 * The attributes of the openprov vocabulary, https://openprovenance.org/ns/openprov, that a relation may carry to
 * point at the statements it arose from, and the names they go by in documents.
 * <p>
 * The ontology names them as PROV-O names the statements a qualified derivation arose from: {@code hadActivity},
 * {@code hadAssociation}, ... In a document they are written without {@code had}, as PROV-N writes its reserved
 * attributes ({@code prov:type}, {@code prov:role}, ...) and as the openprov JSON-LD context scopes them: inside
 * an attribution, {@code openprov:association} is the property {@code openprov:hadAssociation}; inside a
 * specialization, {@code openprov:previousEntity} is {@code openprov:hadPreviousEntity}; on a communication, {@code openprov:entity} is {@code openprov:hadEntity}; on a start or an end, {@code openprov:generation} is {@code openprov:hadGeneration}. Elsewhere, an openprov attribute is an
 * attribute like any other. Every reader turns the written name into the property when it builds one of the three
 * relations, and every writer turns it back; the model holds the property.
 */
public final class OpenprovTerms {

    private OpenprovTerms() {}

    private static final Map<Kind, Map<String, String>> TERM_TO_LOCAL = new EnumMap<>(Kind.class);
    private static final Map<Kind, Map<String, String>> LOCAL_TO_TERM = new EnumMap<>(Kind.class);

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
                "previousEntity", "hadPreviousEntity",
                "derivation", "hadDerivation",
                "specialization", "hadSpecialization");
        scope(Kind.PROV_COMMUNICATION,
                "entity", "hadEntity",
                "generation", "hadGeneration",
                "usage", "hadUsage");
        scope(Kind.PROV_START,
                "generation", "hadGeneration");
        scope(Kind.PROV_END,
                "generation", "hadGeneration");
    }

    /** The roles of the vocabulary: the parties to a change of collection, and the item an activity handles. */
    public static final String AS_COLLECTION = "asCollection";
    public static final String AS_MEMBER = "asMember";
    public static final String AS_ITEM = "asItem";
    public static final String AS_ENTITY = "asEntity";

    /** The types of the vocabulary: changes of collection, on the activity and the derivations it accounts for. */
    public static final String INSERTING_ITEM_INTO_COLLECTION = "InsertingItemIntoCollection";
    public static final String INSERTING_INTO_COLLECTION = "InsertingInto_Collection";
    public static final String INSERTING_INTO_ITEM = "InsertingInto_Item";
    public static final String INSERTING_ELEMENT = "InsertingElement";
    public static final String REMOVING_ELEMENT_FROM_COLLECTION = "RemovingElementFromCollection";
    public static final String REMOVING_ELEMENT = "RemovingElement";

    private static void scope(Kind kind, String... termAndLocal) {
        Map<String, String> forward = new LinkedHashMap<>();
        Map<String, String> backward = new LinkedHashMap<>();
        for (int i = 0; i < termAndLocal.length; i += 2) {
            forward.put(termAndLocal[i], termAndLocal[i + 1]);
            backward.put(termAndLocal[i + 1], termAndLocal[i]);
        }
        TERM_TO_LOCAL.put(kind, Collections.unmodifiableMap(forward));
        LOCAL_TO_TERM.put(kind, Collections.unmodifiableMap(backward));
    }

    /** The kinds of relation that carry openprov attributes. */
    public static Set<Kind> kinds() {
        return TERM_TO_LOCAL.keySet();
    }

    /** The terms a kind scopes, term to the local name of the property; empty for a kind that scopes none. */
    public static Map<String, String> scope(Kind kind) {
        return TERM_TO_LOCAL.getOrDefault(kind, Collections.emptyMap());
    }

    /** The local name of the property a term names inside a relation of this kind, or null. */
    public static String propertyLocalName(Kind kind, String term) {
        return scope(kind).get(term);
    }

    /** The term that names a property inside a relation of this kind, or null. */
    public static String term(Kind kind, String propertyLocalName) {
        return LOCAL_TO_TERM.getOrDefault(kind, Collections.emptyMap()).get(propertyLocalName);
    }

    /**
     * Writing: the term under which an openprov local name is shown inside a relation of this kind, or null when
     * the kind scopes no such name. The property is shown under its term; a name that already is a term, as code
     * that names attributes the way a document does leaves it (a template expansion, say), is shown as it is: the
     * serialisers are the counterpart of the deserialisers, which read the term into the property.
     */
    public static String shownAs(Kind kind, String localName) {
        String term = term(kind, localName);
        if (term != null) return term;
        return scope(kind).containsKey(localName) ? localName : null;
    }

    public static boolean isOpenprov(QualifiedName name) {
        return name != null && NamespacePrefixMapper.OPENPROV_NS.equals(name.getNamespaceURI());
    }

    /** The property a term names inside a relation of this kind, or null. */
    public static QualifiedName property(Kind kind, String term, ProvFactory pf) {
        String local = propertyLocalName(kind, term);
        return local == null ? null : pf.newQualifiedName(NamespacePrefixMapper.OPENPROV_NS, local, NamespacePrefixMapper.OPENPROV_PREFIX);
    }

    /** Reading: the attribute as the model holds it — an openprov term of this kind becomes its property, any other attribute is itself. */
    public static Attribute canonical(Kind kind, Attribute attribute, ProvFactory pf) {
        QualifiedName name = attribute.getElementName();
        if (!isOpenprov(name)) return attribute;
        QualifiedName property = property(kind, name.getLocalPart(), pf);
        return property == null ? attribute : pf.newAttribute(property, attribute.getValue(), attribute.getType());
    }

    /** Writing: the attribute as a document shows it — an openprov property of this kind under its term, any other attribute as itself. */
    public static Attribute surface(Kind kind, Attribute attribute, ProvFactory pf) {
        QualifiedName name = attribute.getElementName();
        if (!isOpenprov(name)) return attribute;
        String term = shownAs(kind, name.getLocalPart());
        return term == null || term.equals(name.getLocalPart()) ? attribute : pf.newAttribute(pf.newQualifiedName(NamespacePrefixMapper.OPENPROV_NS, term, NamespacePrefixMapper.OPENPROV_PREFIX), attribute.getValue(), attribute.getType());
    }

    public static List<Attribute> canonical(Kind kind, Collection<Attribute> attributes, ProvFactory pf) {
        if (attributes == null || TERM_TO_LOCAL.get(kind) == null) return attributes == null ? null : new ArrayList<>(attributes);
        List<Attribute> result = new ArrayList<>(attributes.size());
        for (Attribute a : attributes) result.add(canonical(kind, a, pf));
        return result;
    }

    public static List<Attribute> surface(Kind kind, Collection<Attribute> attributes, ProvFactory pf) {
        if (attributes == null || TERM_TO_LOCAL.get(kind) == null) return attributes == null ? null : new ArrayList<>(attributes);
        List<Attribute> result = new ArrayList<>(attributes.size());
        for (Attribute a : attributes) result.add(surface(kind, a, pf));
        return result;
    }
}
