package org.openprovenance.prov.core.json.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.QualifiedNameException;

import java.util.*;

/**
 * Writes a {@link Document} as a PROV-JSON tree, following the W3C member submission
 * <a href="https://www.w3.org/submissions/prov-json/">PROV-JSON</a>:
 * <ul>
 * <li>a {@code prefix} map, the default namespace under the reserved prefix {@code default};</li>
 * <li>one object per kind of statement ({@code entity}, {@code used}, ...) mapping identifiers to records, in
 *     the order the kinds first occur, a relation without identifier getting a blank {@code _:nN};</li>
 * <li>a record's core properties ({@code prov:entity}, {@code prov:time}, ...) beside its attributes;</li>
 * <li>an attribute value as a JSON string when it is an {@code xsd:string}, a {@code {"$", "lang"}} object when
 *     it carries a language, a {@code {"$", "type"}} object otherwise, and an array when the attribute has several
 *     values;</li>
 * <li>bundles under {@code bundle}, each a document without bundles.</li>
 * </ul>
 * The qualified extensions of specialization, alternate and membership are written as the standard relation,
 * with their identifier and attributes, which PROV-JSON allows on any relation.
 */
public class ProvJsonWriter implements ProvJsonVocabulary {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ProvFactory factory;
    private final Name name;
    private int blankCounter = 0;

    public ProvJsonWriter(ProvFactory pf) {
        this.factory = pf;
        this.name = pf.getName();
    }

    public JsonNode write(Document doc) {
        blankCounter = 0;
        // a copy: a name whose prefix the document never declared gets declared here, not in the document
        Namespace ns = doc.getNamespace() == null ? new Namespace() : new Namespace(doc.getNamespace());
        ObjectNode root = mapper.createObjectNode();
        writeScope(root, ns, doc.getStatementOrBundle());
        return root;
    }

    /** A document or a bundle: its prefixes, then its statements grouped by kind. */
    private void writeScope(ObjectNode scope, Namespace ns, Collection<? extends StatementOrBundle> statements) {
        ObjectNode prefixes = mapper.createObjectNode();
        Map<String, ObjectNode> sections = new LinkedHashMap<>();
        for (StatementOrBundle s : statements) {
            if (s instanceof Bundle) {
                Bundle bun = (Bundle) s;
                ObjectNode bundles = sections.computeIfAbsent(BUNDLE, k -> mapper.createObjectNode());
                ObjectNode content = mapper.createObjectNode();
                Namespace bunNs = bun.getNamespace() == null ? new Namespace() : new Namespace(bun.getNamespace());
                bunNs.setParent(ns);
                writeScope(content, bunNs, bun.getStatement());
                bundles.set(qn(bun.getId(), ns), content);
            } else {
                writeStatement(sections, (Statement) s, ns);
            }
        }
        writePrefixes(prefixes, ns);
        if (!prefixes.isEmpty()) scope.set(PREFIX, prefixes);
        // sections in the order their first statement came: a document read back keeps the order it was written in
        // whenever its statements were grouped by kind
        for (Map.Entry<String, ObjectNode> section : sections.entrySet()) {
            scope.set(section.getKey(), section.getValue());
        }
    }

    private void writePrefixes(ObjectNode prefixes, Namespace ns) {
        if (ns.getDefaultNamespace() != null) prefixes.put(DEFAULT, ns.getDefaultNamespace());
        for (Map.Entry<String, String> e : ns.getPrefixes().entrySet()) {
            if (BLANK_PREFIX.equals(e.getKey())) continue;
            prefixes.put(e.getKey(), e.getValue());
        }
    }

    private void writeStatement(Map<String, ObjectNode> sections, Statement s, Namespace ns) {
        ObjectNode record = mapper.createObjectNode();
        String section;
        switch (s.getKind()) {
            case PROV_ENTITY -> section = ENTITY;
            case PROV_AGENT -> section = AGENT;
            case PROV_ACTIVITY -> {
                Activity a = (Activity) s;
                section = ACTIVITY;
                time(record, PROV_START_TIME, a.getStartTime());
                time(record, PROV_END_TIME, a.getEndTime());
            }
            case PROV_USAGE -> {
                Used u = (Used) s;
                section = USED;
                ref(record, PROV_ACTIVITY, u.getActivity(), ns);
                ref(record, PROV_ENTITY, u.getEntity(), ns);
                time(record, PROV_TIME, u.getTime());
            }
            case PROV_GENERATION -> {
                WasGeneratedBy g = (WasGeneratedBy) s;
                section = WAS_GENERATED_BY;
                ref(record, PROV_ENTITY, g.getEntity(), ns);
                ref(record, PROV_ACTIVITY, g.getActivity(), ns);
                time(record, PROV_TIME, g.getTime());
            }
            case PROV_INVALIDATION -> {
                WasInvalidatedBy i = (WasInvalidatedBy) s;
                section = WAS_INVALIDATED_BY;
                ref(record, PROV_ENTITY, i.getEntity(), ns);
                ref(record, PROV_ACTIVITY, i.getActivity(), ns);
                time(record, PROV_TIME, i.getTime());
            }
            case PROV_START -> {
                WasStartedBy w = (WasStartedBy) s;
                section = WAS_STARTED_BY;
                ref(record, PROV_ACTIVITY, w.getActivity(), ns);
                ref(record, PROV_TRIGGER, w.getTrigger(), ns);
                ref(record, PROV_STARTER, w.getStarter(), ns);
                time(record, PROV_TIME, w.getTime());
            }
            case PROV_END -> {
                WasEndedBy w = (WasEndedBy) s;
                section = WAS_ENDED_BY;
                ref(record, PROV_ACTIVITY, w.getActivity(), ns);
                ref(record, PROV_TRIGGER, w.getTrigger(), ns);
                ref(record, PROV_ENDER, w.getEnder(), ns);
                time(record, PROV_TIME, w.getTime());
            }
            case PROV_COMMUNICATION -> {
                WasInformedBy w = (WasInformedBy) s;
                section = WAS_INFORMED_BY;
                ref(record, PROV_INFORMED, w.getInformed(), ns);
                ref(record, PROV_INFORMANT, w.getInformant(), ns);
            }
            case PROV_DERIVATION -> {
                WasDerivedFrom d = (WasDerivedFrom) s;
                section = WAS_DERIVED_FROM;
                ref(record, PROV_GENERATED_ENTITY, d.getGeneratedEntity(), ns);
                ref(record, PROV_USED_ENTITY, d.getUsedEntity(), ns);
                ref(record, PROV_ACTIVITY, d.getActivity(), ns);
                ref(record, PROV_GENERATION, d.getGeneration(), ns);
                ref(record, PROV_USAGE, d.getUsage(), ns);
            }
            case PROV_ATTRIBUTION -> {
                WasAttributedTo w = (WasAttributedTo) s;
                section = WAS_ATTRIBUTED_TO;
                ref(record, PROV_ENTITY, w.getEntity(), ns);
                ref(record, PROV_AGENT, w.getAgent(), ns);
            }
            case PROV_ASSOCIATION -> {
                WasAssociatedWith w = (WasAssociatedWith) s;
                section = WAS_ASSOCIATED_WITH;
                ref(record, PROV_ACTIVITY, w.getActivity(), ns);
                ref(record, PROV_AGENT, w.getAgent(), ns);
                ref(record, PROV_PLAN, w.getPlan(), ns);
            }
            case PROV_DELEGATION -> {
                ActedOnBehalfOf a = (ActedOnBehalfOf) s;
                section = ACTED_ON_BEHALF_OF;
                ref(record, PROV_DELEGATE, a.getDelegate(), ns);
                ref(record, PROV_RESPONSIBLE, a.getResponsible(), ns);
                ref(record, PROV_ACTIVITY, a.getActivity(), ns);
            }
            case PROV_INFLUENCE -> {
                WasInfluencedBy w = (WasInfluencedBy) s;
                section = WAS_INFLUENCED_BY;
                ref(record, PROV_INFLUENCEE, w.getInfluencee(), ns);
                ref(record, PROV_INFLUENCER, w.getInfluencer(), ns);
            }
            case PROV_SPECIALIZATION -> {
                SpecializationOf sp = (SpecializationOf) s;
                section = SPECIALIZATION_OF;
                ref(record, PROV_SPECIFIC_ENTITY, sp.getSpecificEntity(), ns);
                ref(record, PROV_GENERAL_ENTITY, sp.getGeneralEntity(), ns);
            }
            case PROV_ALTERNATE -> {
                AlternateOf alt = (AlternateOf) s;
                section = ALTERNATE_OF;
                ref(record, PROV_ALTERNATE1, alt.getAlternate1(), ns);
                ref(record, PROV_ALTERNATE2, alt.getAlternate2(), ns);
            }
            case PROV_MEMBERSHIP -> {
                HadMember m = (HadMember) s;
                section = HAD_MEMBER;
                ref(record, PROV_COLLECTION, m.getCollection(), ns);
                List<QualifiedName> members = m.getEntity();
                if (members.size() == 1) {
                    ref(record, PROV_ENTITY, members.get(0), ns);
                } else {
                    ArrayNode array = record.putArray(PROV_ENTITY);
                    for (QualifiedName member : members) array.add(qn(member, ns));
                }
            }
            case PROV_MENTION -> {
                MentionOf m = (MentionOf) s;
                section = MENTION_OF;
                ref(record, PROV_SPECIFIC_ENTITY, m.getSpecificEntity(), ns);
                ref(record, PROV_GENERAL_ENTITY, m.getGeneralEntity(), ns);
                ref(record, PROV_BUNDLE, m.getBundle(), ns);
            }
            case PROV_BUNDLE -> throw new IllegalStateException("a bundle is not a statement");
            default -> throw new ProvJsonException("PROV-JSON has no representation for " + s.getKind());
        }
        writeAttributes(record, s, ns);
        ObjectNode records = sections.computeIfAbsent(section, k -> mapper.createObjectNode());
        String id = (s instanceof Identifiable && ((Identifiable) s).getId() != null) ? qn(((Identifiable) s).getId(), ns) : blank();
        JsonNode previous = records.get(id);
        if (previous == null) {
            records.set(id, record);
        } else {
            // two statements with one identifier make one record: PROV-JSON has no room for both
            merge((ObjectNode) previous, record, id);
        }
    }

    /** The core properties must agree; the attribute values of the second join those of the first. */
    private void merge(ObjectNode into, ObjectNode record, String id) {
        Iterator<Map.Entry<String, JsonNode>> fields = record.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            JsonNode existing = into.get(field.getKey());
            if (existing == null) {
                into.set(field.getKey(), field.getValue());
            } else if (!existing.equals(field.getValue())) {
                if (CORE_PROPERTIES.contains(field.getKey())) {
                    throw new ProvJsonException("two statements identified by " + id + " disagree on " + field.getKey()
                            + ": " + existing + " and " + field.getValue());
                }
                ArrayNode values = asArray(existing);
                for (JsonNode v : asArray(field.getValue())) {
                    if (!contains(values, v)) values.add(v);
                }
                into.set(field.getKey(), values.size() == 1 ? values.get(0) : values);
            }
        }
    }

    private ArrayNode asArray(JsonNode n) {
        if (n.isArray()) return ((ArrayNode) n).deepCopy();
        ArrayNode a = mapper.createArrayNode();
        a.add(n);
        return a;
    }

    private static boolean contains(ArrayNode array, JsonNode v) {
        for (JsonNode e : array) if (e.equals(v)) return true;
        return false;
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              ATTRIBUTES
    ///
    //////////////////////////////////////////////////////////////////////

    /** Labels, types, locations, roles, value and other attributes, in that order, grouped by name. */
    private void writeAttributes(ObjectNode record, Statement s, Namespace ns) {
        Map<String, List<JsonNode>> values = new LinkedHashMap<>();
        if (s instanceof HasLabel) {
            for (LangString label : ((HasLabel) s).getLabel()) {
                values.computeIfAbsent(PROV_LABEL, k -> new ArrayList<>()).add(langString(label));
            }
        }
        if (s instanceof HasType) {
            for (Type type : ((HasType) s).getType()) addValue(values, PROV_TYPE, type, ns);
        }
        if (s instanceof HasLocation) {
            for (Location location : ((HasLocation) s).getLocation()) addValue(values, PROV_LOCATION, location, ns);
        }
        if (s instanceof HasRole) {
            for (Role role : ((HasRole) s).getRole()) addValue(values, PROV_ROLE, role, ns);
        }
        if (s instanceof HasValue) {
            Value value = ((HasValue) s).getValue();
            if (value != null) addValue(values, PROV_VALUE, value, ns);
        }
        if (s instanceof HasOther) {
            for (Other other : ((HasOther) s).getOther()) {
                // an openprov property of an attribution, membership or specialization is written under its term, without had
                Attribute shown = OpenprovTerms.surface(s.getKind(), other, factory);
                addValue(values, qn(shown.getElementName(), ns), (TypedValue) shown, ns);
            }
        }
        for (Map.Entry<String, List<JsonNode>> e : values.entrySet()) {
            if (e.getValue().size() == 1) {
                record.set(e.getKey(), e.getValue().get(0));
            } else {
                ArrayNode array = record.putArray(e.getKey());
                e.getValue().forEach(array::add);
            }
        }
    }

    private void addValue(Map<String, List<JsonNode>> values, String key, TypedValue attribute, Namespace ns) {
        values.computeIfAbsent(key, k -> new ArrayList<>()).add(value(attribute, ns));
    }

    /** A JSON string for an {@code xsd:string}, {@code {"$","lang"}} for a language-tagged string, {@code {"$","type"}} otherwise. */
    public JsonNode value(TypedValue attribute, Namespace ns) {
        Object v = attribute.getValue();
        QualifiedName type = attribute.getType();
        if (v instanceof LangString) {
            LangString ls = (LangString) v;
            if (ls.getLang() != null || type == null || name.XSD_STRING.equals(type) || name.PROV_LANG_STRING.equals(type)) {
                return langString(ls);
            }
            return typed(ls.getValue(), qn(type, ns));
        }
        if (v instanceof QualifiedName) {
            return typed(qn((QualifiedName) v, ns), qn(type == null ? name.PROV_QUALIFIED_NAME : type, ns));
        }
        String text = String.valueOf(v);
        if (type == null || name.XSD_STRING.equals(type)) return mapper.getNodeFactory().textNode(text);
        return typed(text, qn(type, ns));
    }

    private JsonNode langString(LangString ls) {
        if (ls.getLang() == null) return mapper.getNodeFactory().textNode(ls.getValue());
        ObjectNode o = mapper.createObjectNode();
        o.put(DOLLAR, ls.getValue());
        o.put(LANG, ls.getLang());
        return o;
    }

    private ObjectNode typed(String value, String type) {
        ObjectNode o = mapper.createObjectNode();
        o.put(DOLLAR, value);
        o.put(TYPE, type);
        return o;
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              NAMES AND TIMES
    ///
    //////////////////////////////////////////////////////////////////////

    private void ref(ObjectNode record, String property, QualifiedName id, Namespace ns) {
        if (id != null) record.put(property, qn(id, ns));
    }

    private void time(ObjectNode record, String property, javax.xml.datatype.XMLGregorianCalendar t) {
        if (t != null) record.put(property, t.toXMLFormat());
    }

    /** {@code prefix:local} as the namespace declares it, {@code local} alone in the default namespace, {@code _:local} for a blank. */
    public String qn(QualifiedName q, Namespace ns) {
        if (BLANK_NS.equals(q.getNamespaceURI())) return BLANK_PREFIX + ":" + q.getLocalPart();
        try {
            return ns.qualifiedNameToString(q);
        } catch (QualifiedNameException e) {
            if (q.getPrefix() != null && !q.getPrefix().isEmpty()) {
                // undeclared, but the name carries its prefix: declare it so the reader can resolve it
                ns.register(q.getPrefix(), q.getNamespaceURI());
                return q.getPrefix() + ":" + q.getLocalPart();
            }
            throw new ProvJsonException("no prefix for " + q + " in " + ns, e);
        }
    }

    private String blank() {
        return BLANK_PREFIX + ":n" + (blankCounter++);
    }
}
