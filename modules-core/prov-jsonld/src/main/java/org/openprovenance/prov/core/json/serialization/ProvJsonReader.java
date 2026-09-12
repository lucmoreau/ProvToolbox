package org.openprovenance.prov.core.json.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.DocumentedUnsupportedCaseException;
import org.openprovenance.prov.vanilla.ProvFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

/**
 * Reads a PROV-JSON tree into a {@link Document}, accepting everything the W3C member submission
 * <a href="https://www.w3.org/submissions/prov-json/">PROV-JSON</a> allows:
 * <ul>
 * <li>an attribute value as a JSON string, number or boolean, a {@code {"$", "type"}} or {@code {"$", "lang"}}
 *     object, or an array of those; a qualified name typed {@code xsd:QName} as the submission has it, or
 *     {@code prov:QUALIFIED_NAME} as PROV-DM names the type;</li>
 * <li>{@code prov} and {@code xsd} without declaration;</li>
 * <li>a blank identifier {@code _:x} on a relation, which is a relation without identifier;</li>
 * <li>a specialization, alternate or membership with an identifier or attributes, which becomes the qualified
 *     extension of that relation.</li>
 * </ul>
 * A section or property the submission does not define is an error, not silently dropped.
 */
public class ProvJsonReader implements ProvJsonVocabulary {

    private final ProvFactory pf;
    private final Name name;
    private final DateTimeOption dateTimeOption;
    private final TimeZone timeZone;

    public ProvJsonReader(ProvFactory pf, DateTimeOption dateTimeOption, TimeZone timeZone) {
        this.pf = pf;
        this.name = pf.getName();
        this.dateTimeOption = dateTimeOption == null ? DateTimeOption.PRESERVE : dateTimeOption;
        this.timeZone = timeZone;
    }

    public Document read(JsonNode root) {
        if (!root.isObject()) throw new ProvJsonException("a PROV-JSON document is a JSON object, not " + root.getNodeType());
        Namespace ns = namespace(root.get(PREFIX), null);
        List<Statement> statements = new ArrayList<>();
        List<Bundle> bundles = new ArrayList<>();
        readScope(root, ns, statements, bundles, true);
        Document doc = pf.newDocument(ns, statements, bundles);
        for (Bundle b : bundles) b.getNamespace().setParent(ns);
        return doc;
    }

    private void readScope(JsonNode scope, Namespace ns, List<Statement> statements, List<Bundle> bundles, boolean bundlesAllowed) {
        Iterator<Map.Entry<String, JsonNode>> sections = scope.fields();
        while (sections.hasNext()) {
            Map.Entry<String, JsonNode> section = sections.next();
            String key = section.getKey();
            if (PREFIX.equals(key)) continue;
            if (BUNDLE.equals(key)) {
                if (!bundlesAllowed) throw new ProvJsonException("a bundle must not contain a bundle");
                Iterator<Map.Entry<String, JsonNode>> entries = object(section.getValue(), key).fields();
                while (entries.hasNext()) {
                    Map.Entry<String, JsonNode> e = entries.next();
                    bundles.add(readBundle(id(e.getKey(), ns), object(e.getValue(), e.getKey()), ns));
                }
                continue;
            }
            if (!SECTIONS.contains(key)) throw new ProvJsonException("unknown PROV-JSON section: " + key);
            Iterator<Map.Entry<String, JsonNode>> records = object(section.getValue(), key).fields();
            while (records.hasNext()) {
                Map.Entry<String, JsonNode> r = records.next();
                statements.add(readStatement(key, r.getKey(), object(r.getValue(), r.getKey()), ns));
            }
        }
    }

    private Bundle readBundle(QualifiedName id, JsonNode content, Namespace docNs) {
        Namespace ns = namespace(content.get(PREFIX), docNs);
        List<Statement> statements = new ArrayList<>();
        readScope(content, ns, statements, null, false);
        return pf.newNamedBundle(id, ns, statements);
    }

    /** {@code prov} and {@code xsd} are implicitly declared; {@code default} names the default namespace. */
    private Namespace namespace(JsonNode prefixes, Namespace parent) {
        Namespace ns = new Namespace();
        ns.addKnownNamespaces();
        if (prefixes != null) {
            Iterator<Map.Entry<String, JsonNode>> it = object(prefixes, PREFIX).fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> e = it.next();
                String uri = e.getValue().asText();
                if (DEFAULT.equals(e.getKey())) {
                    ns.setDefaultNamespace(uri);
                } else if (!BLANK_PREFIX.equals(e.getKey())) {
                    ns.register(e.getKey(), uri);
                }
            }
        }
        ns.setParent(parent);
        return ns;
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              STATEMENTS
    ///
    //////////////////////////////////////////////////////////////////////

    private Statement readStatement(String section, String key, JsonNode r, Namespace ns) {
        QualifiedName id = isBlank(key) ? null : id(key, ns);
        Collection<Attribute> attrs = attributes(r, ns);
        switch (section) {
            case ENTITY:
                return pf.newEntity(id(key, ns), attrs);
            case AGENT:
                return pf.newAgent(id(key, ns), attrs);
            case ACTIVITY:
                return pf.newActivity(id(key, ns), time(r, PROV_START_TIME), time(r, PROV_END_TIME), attrs);
            case USED:
                return pf.newUsed(id, ref(r, PROV_ACTIVITY, ns), ref(r, PROV_ENTITY, ns), time(r, PROV_TIME), attrs);
            case WAS_GENERATED_BY:
                return pf.newWasGeneratedBy(id, ref(r, PROV_ENTITY, ns), ref(r, PROV_ACTIVITY, ns), time(r, PROV_TIME), attrs);
            case WAS_INVALIDATED_BY:
                return pf.newWasInvalidatedBy(id, ref(r, PROV_ENTITY, ns), ref(r, PROV_ACTIVITY, ns), time(r, PROV_TIME), attrs);
            case WAS_STARTED_BY:
                return pf.newWasStartedBy(id, ref(r, PROV_ACTIVITY, ns), ref(r, PROV_TRIGGER, ns), ref(r, PROV_STARTER, ns), time(r, PROV_TIME), attrs);
            case WAS_ENDED_BY:
                return pf.newWasEndedBy(id, ref(r, PROV_ACTIVITY, ns), ref(r, PROV_TRIGGER, ns), ref(r, PROV_ENDER, ns), time(r, PROV_TIME), attrs);
            case WAS_INFORMED_BY:
                return pf.newWasInformedBy(id, ref(r, PROV_INFORMED, ns), ref(r, PROV_INFORMANT, ns), attrs);
            case WAS_DERIVED_FROM:
                return pf.newWasDerivedFrom(id, ref(r, PROV_GENERATED_ENTITY, ns), ref(r, PROV_USED_ENTITY, ns),
                        ref(r, PROV_ACTIVITY, ns), ref(r, PROV_GENERATION, ns), ref(r, PROV_USAGE, ns), attrs);
            case WAS_ATTRIBUTED_TO:
                return pf.newWasAttributedTo(id, ref(r, PROV_ENTITY, ns), ref(r, PROV_AGENT, ns), attrs);
            case WAS_ASSOCIATED_WITH:
                return pf.newWasAssociatedWith(id, ref(r, PROV_ACTIVITY, ns), ref(r, PROV_AGENT, ns), ref(r, PROV_PLAN, ns), attrs);
            case ACTED_ON_BEHALF_OF:
                return pf.newActedOnBehalfOf(id, ref(r, PROV_DELEGATE, ns), ref(r, PROV_RESPONSIBLE, ns), ref(r, PROV_ACTIVITY, ns), attrs);
            case WAS_INFLUENCED_BY:
                return pf.newWasInfluencedBy(id, ref(r, PROV_INFLUENCEE, ns), ref(r, PROV_INFLUENCER, ns), attrs);
            case SPECIALIZATION_OF: {
                QualifiedName specific = ref(r, PROV_SPECIFIC_ENTITY, ns);
                QualifiedName general = ref(r, PROV_GENERAL_ENTITY, ns);
                return (id == null && attrs.isEmpty()) ? pf.newSpecializationOf(specific, general)
                        : pf.newQualifiedSpecializationOf(id, specific, general, attrs);
            }
            case ALTERNATE_OF: {
                QualifiedName alt1 = ref(r, PROV_ALTERNATE1, ns);
                QualifiedName alt2 = ref(r, PROV_ALTERNATE2, ns);
                return (id == null && attrs.isEmpty()) ? pf.newAlternateOf(alt1, alt2)
                        : pf.newQualifiedAlternateOf(id, alt1, alt2, attrs);
            }
            case HAD_MEMBER: {
                QualifiedName collection = ref(r, PROV_COLLECTION, ns);
                List<QualifiedName> members = refs(r, PROV_ENTITY, ns);
                return (id == null && attrs.isEmpty()) ? pf.newHadMember(collection, members)
                        : pf.newQualifiedHadMember(id, collection, members, attrs);
            }
            case MENTION_OF:
                try {
                    return pf.newMentionOf(ref(r, PROV_SPECIFIC_ENTITY, ns), ref(r, PROV_GENERAL_ENTITY, ns), ref(r, PROV_BUNDLE, ns));
                } catch (UnsupportedOperationException e) {
                    throw unsupported(MENTION_OF);
                }
            default:
                throw new ProvJsonException("unknown PROV-JSON section: " + section);
        }
    }

    /** The attributes of a record: every property that is not one of the relation's own. */
    private Collection<Attribute> attributes(JsonNode r, Namespace ns) {
        List<Attribute> attrs = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> fields = r.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> f = fields.next();
            if (CORE_PROPERTIES.contains(f.getKey())) continue;
            QualifiedName elementName = id(f.getKey(), ns);
            JsonNode v = f.getValue();
            if (v.isArray()) {
                for (JsonNode e : v) attrs.add(attribute(elementName, e, ns));
            } else {
                attrs.add(attribute(elementName, v, ns));
            }
        }
        return attrs;
    }

    /**
     * One literal: a string is an {@code xsd:string}, a boolean an {@code xsd:boolean}, a number an {@code xsd:int}
     * when integral (or a wider integer type when it does not fit) and an {@code xsd:decimal} otherwise, as the
     * submission has it; an object carries its value under {@code $} with a {@code type} or a {@code lang}.
     */
    public Attribute attribute(QualifiedName elementName, JsonNode v, Namespace ns) {
        if (v.isTextual()) return pf.newAttribute(elementName, v.textValue(), name.XSD_STRING);
        if (v.isBoolean()) return pf.newAttribute(elementName, v.asText(), name.XSD_BOOLEAN);
        if (v.isNumber()) {
            QualifiedName type;
            if (v.isInt()) type = name.XSD_INT;
            else if (v.isLong()) type = name.XSD_LONG;
            else if (v.isBigInteger()) type = name.XSD_INTEGER;
            else type = name.XSD_DECIMAL;
            return pf.newAttribute(elementName, v.asText(), type);
        }
        if (!v.isObject()) throw new ProvJsonException("not a PROV-JSON literal: " + v);
        JsonNode dollar = v.get(DOLLAR);
        if (dollar == null) throw new ProvJsonException("a literal object needs a \"$\": " + v);
        String value = dollar.asText();
        JsonNode lang = v.get(LANG);
        JsonNode type = v.get(TYPE);
        if (lang != null) {
            return pf.newAttribute(elementName, pf.newInternationalizedString(value, lang.asText()), name.PROV_LANG_STRING);
        }
        if (type == null) return pf.newAttribute(elementName, value, name.XSD_STRING);
        String typeName = type.asText();
        if (XSD_QNAME.equals(typeName) || PROV_QUALIFIED_NAME.equals(typeName)) {
            return pf.newAttribute(elementName, id(value, ns), name.PROV_QUALIFIED_NAME);
        }
        return pf.newAttribute(elementName, value, id(typeName, ns));
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              NAMES AND TIMES
    ///
    //////////////////////////////////////////////////////////////////////

    static boolean isBlank(String id) {
        return id.startsWith(BLANK_PREFIX + ":");
    }

    /** A qualified name from {@code prefix:local}, or {@code local} in the default namespace; a blank {@code _:local}
     *  is a name in the blank namespace, which an element keeps as its identifier and a relation drops. */
    public QualifiedName id(String s, Namespace ns) {
        if (isBlank(s)) return pf.newQualifiedName(BLANK_NS, s.substring(BLANK_PREFIX.length() + 1), BLANK_PREFIX);
        try {
            return ns.stringToQualifiedName(s, pf, true);
        } catch (RuntimeException e) {
            throw new ProvJsonException("cannot resolve " + s + ": " + e.getMessage(), e);
        }
    }

    private QualifiedName ref(JsonNode r, String property, Namespace ns) {
        JsonNode v = r.get(property);
        if (v == null || v.isNull()) return null;
        if (!v.isTextual()) throw new ProvJsonException(property + " must be an identifier, not " + v);
        return id(v.textValue(), ns);
    }

    /** One identifier or an array of them: a membership may list several members. */
    private List<QualifiedName> refs(JsonNode r, String property, Namespace ns) {
        JsonNode v = r.get(property);
        List<QualifiedName> result = new ArrayList<>();
        if (v == null || v.isNull()) return result;
        if (v.isArray()) {
            for (JsonNode e : v) result.add(id(e.asText(), ns));
        } else {
            result.add(id(v.asText(), ns));
        }
        return result;
    }

    private XMLGregorianCalendar time(JsonNode r, String property) {
        JsonNode v = r.get(property);
        if (v == null || v.isNull()) return null;
        try {
            return pf.newISOTime(v.asText(), dateTimeOption, timeZone);
        } catch (RuntimeException e) {
            throw new ProvJsonException(property + " is not an xsd:dateTime: " + v, e);
        }
    }

    private static JsonNode object(JsonNode n, String what) {
        if (!n.isObject()) throw new ProvJsonException(what + " must be a JSON object, not " + n.getNodeType());
        return n;
    }

    /** What the model cannot build: mentions, and the dictionary relations, have no PROV-JSON reading here. */
    static DocumentedUnsupportedCaseException unsupported(String what) {
        return new DocumentedUnsupportedCaseException("PROV-JSON: " + what + " not supported");
    }
}
