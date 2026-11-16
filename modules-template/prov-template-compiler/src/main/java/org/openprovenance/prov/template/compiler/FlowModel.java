package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.template.compiler.configuration.FlowTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.SubtypeTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.descriptors.*;

import java.util.*;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;

/**
 * T36 part E (design §13): a {@code flow} entry, read and checked, as the generator and the descriptions share it. {@link #resolve}
 * refuses a declaration that does not account for every input exactly once (§13.2, §13.4); what it returns is the list of
 * {@link Item}s, one per assignment, each with its kind and its origin, which {@link CompilerFlow} writes as PAST, the documentation
 * writes as a table and the Turtle writes as terms: the table says what the code does because both read the same list.
 */
public class FlowModel {

    /** The six kinds of assignment (§13.1), and a value left unset on purpose. */
    public enum Kind {
        AS_ASKED("AS ASKED", "asked"), CHAINED("CHAINED", "chained"), CARRIED("CARRIED OVER", "carried over"), CONSTANT("CONSTANT", "constant"),
        SUPPLIED("SUPPLIED", "supplied"), AS_MADE("AS MADE", "as made"), ABSENT("ABSENT", "absent");

        public final String label;
        public final String word;

        Kind(String label, String word) {
            this.label = label;
            this.word = word;
        }
    }

    /** A field of a bean: its name, what it was declared as, and its PAST type. */
    public static final class Field {
        public final String name;
        /** {@code "node"} for a node, else the declared type ({@code xsd:string}...); for the map of other keys, {@code "other keys"}. */
        public final String declared;
        public final TypeName past;
        public final boolean compulsory;
        public final String performative;
        public final String carrier;
        public final String documentation;
        /** For a node, the type of the value the node's field holds ({@code @type} of its declaration, else {@code xsd:string}); else the declared type. */
        public String valueType;

        Field(String name, String declared, TypeName past, boolean compulsory, String performative, String carrier, String documentation) {
            this.name = name;
            this.valueType = declared;
            this.declared = declared;
            this.past = past;
            this.compulsory = compulsory;
            this.performative = performative;
            this.carrier = carrier;
            this.documentation = documentation;
        }

        public boolean isNode() {
            return "node".equals(declared);
        }

        public boolean isOtherKeys() {
            return "other keys".equals(declared);
        }
    }

    /** A thing that stood before: the record that made it, and how this record refers to it. */
    public static final class Stood {
        public final String name;
        public String node;
        public String madeBy;
        public String addressed;
        public String identityUser;
        public String identityBackend;
        /** The subtype of the previous record: {@code to}, or the subtype {@code of} names. */
        public SubtypeTemplateCompilerConfig record;
        public Map<String, Field> fields;

        Stood(String name) {
            this.name = name;
        }
    }

    /** One assignment: which step it is in, what it fills, its kind and where the value comes from. */
    public static final class Item {
        public final String step;
        public final String target;
        public final Field field;
        public final Kind kind;
        /** For chained and carried over: the stood thing. */
        public String thing;
        /** The name read: a field of the user bean, of a stood thing's bean, of the recorded bean, or an output of the store. */
        public String source;
        /** {@code "text"} or null. */
        public String as;
        public Object constant;
        public String rule;
        public boolean argument;
        /** {@code when}/{@code is}/{@code otherwise}: the output whose existence decides, and the two constants. */
        public String whenMade;
        public Object is;
        public Object otherwise;
        /** For chained: the output of the previous record that made the node. */
        public String madeBy;
        /** Where the source is: {@code "user"} (the user bean), {@code "recorded"}, {@code "made"}, {@code "stood"}, {@code "request"}. */
        public String from = "";

        Item(String step, String target, Field field, Kind kind) {
            this.step = step;
            this.target = target;
            this.field = field;
            this.kind = kind;
        }

        /** The origin in words, for the comment beside the assignment, the table in the documentation and the Turtle. */
        public String describe() {
            switch (kind) {
                case AS_ASKED:
                    return ("request".equals(from) ? "the request's " : "the user bean's ") + source + (as == null ? "" : ", as " + as);
                case CHAINED:
                    return "the " + thing + " as it stood: " + madeBy + " of the record that made it";
                case CARRIED:
                    return "the " + thing + "'s " + source + ", unchanged";
                case CONSTANT:
                    return "the constant " + constant;
                case SUPPLIED:
                    return argument ? "an argument of the method" : "the application's rule " + rule;
                case AS_MADE:
                    if (whenMade != null) return (is + " when the store made " + whenMade + ", otherwise " + otherwise);
                    return "made".equals(from) ? "what the store made: " + source : "the backend's " + source + " as recorded" + (as == null ? "" : ", as " + as);
                default:
                    return "left unset";
            }
        }
    }

    /** A rule the application supplies. */
    public static final class Rule {
        public final String name;
        public final String step;
        public final String target;
        public final TypeName returns;

        Rule(String name, String step, String target, TypeName returns) {
            this.name = name;
            this.step = step;
            this.target = target;
            this.returns = returns;
        }
    }

    /** The simple name of the generated class: the last segment of the flow's fully qualified name. */
    public String className() {
        String fqn = flow.fullyQualifiedName;
        return fqn.substring(fqn.lastIndexOf('.') + 1);
    }

    public final FlowTemplateCompilerConfig flow;
    public SubtypeTemplateCompilerConfig from, to;
    public SimpleTemplateCompilerConfig userTemplate, backendTemplate;
    public TemplateBindingsSchema userSchema, backendSchema;
    public Map<String, Field> userFields = new LinkedHashMap<>();
    public Map<String, Field> backendFields = new LinkedHashMap<>();
    /** The outputs of the backend template's outputs bean. */
    public Map<String, Field> outputs = new LinkedHashMap<>();
    public final List<String> requestValues = new ArrayList<>(), replyValues = new ArrayList<>(), ownValues = new ArrayList<>();
    public final Map<String, Stood> stood = new LinkedHashMap<>();
    /** The assignments of each step: {@code ask}, {@code record}, and the reply blocks {@code answer}, {@code refused}, {@code failed}. */
    public final Map<String, List<Item>> steps = new LinkedHashMap<>();
    public final Map<String, Rule> rules = new LinkedHashMap<>();

    private FlowModel(FlowTemplateCompilerConfig flow) {
        this.flow = flow;
    }

    // ---- reading -------------------------------------------------------------------------------------------------------------

    public static List<FlowTemplateCompilerConfig> flows(TemplatesProjectConfiguration configs) {
        List<FlowTemplateCompilerConfig> res = new ArrayList<>();
        if (configs.templates == null) return res;
        for (TemplateCompilerConfig c : configs.templates) if (c instanceof FlowTemplateCompilerConfig) res.add((FlowTemplateCompilerConfig) c);
        return res;
    }

    /** Reads and checks the flow; throws {@link FlowException} naming the flow and what is wrong. */
    public static FlowModel resolve(TemplatesProjectConfiguration configs, FlowTemplateCompilerConfig flow, CompilerUtil compilerUtil, CompilerSubtype compilerSubtype,
                                    String cbindingsBaseDir) {
        FlowModel m = new FlowModel(flow);
        m.read(configs, compilerUtil, compilerSubtype, cbindingsBaseDir);
        return m;
    }

    private FlowException refusal(String why) {
        return new FlowException("flow " + flow.fullyQualifiedName + ": " + why);
    }

    private SubtypeTemplateCompilerConfig subtype(TemplatesProjectConfiguration configs, String fqn, String member) {
        if (fqn == null) throw refusal(member + " is required: the fully qualified name of a subtype of the catalogue");
        for (TemplateCompilerConfig c : configs.templates) {
            if (c instanceof SubtypeTemplateCompilerConfig && Objects.equals(c.fullyQualifiedName, fqn)) return (SubtypeTemplateCompilerConfig) c;
        }
        throw refusal(member + " names " + fqn + ", which is no subtype of the catalogue");
    }

    private void read(TemplatesProjectConfiguration configs, CompilerUtil compilerUtil, CompilerSubtype compilerSubtype, String cbindingsBaseDir) {
        if (flow.name == null || flow.fullyQualifiedName == null || flow.package_ == null) {
            throw new FlowException("flow " + flow.fullyQualifiedName + ": name, fullyQualifiedName and package are all required");
        }
        from = subtype(configs, flow.from, "from");
        to = subtype(configs, flow.to, "to");
        userTemplate = CompilerSubtype.refined(configs, from);
        backendTemplate = CompilerSubtype.refined(configs, to);
        userSchema = compilerUtil.getBindingsSchema(userTemplate, cbindingsBaseDir);
        backendSchema = compilerUtil.getBindingsSchema(backendTemplate, cbindingsBaseDir);

        userFields = fieldsOf(userSchema, from, compilerUtil, compilerSubtype);
        backendFields = fieldsOf(backendSchema, to, compilerUtil, compilerSubtype);
        for (Field f : userFields.values()) {
            if (f.isOtherKeys()) continue;
            if ("request".equals(f.performative)) requestValues.add(f.name);
            else if ("reply".equals(f.performative)) replyValues.add(f.name);
            else ownValues.add(f.name);
        }
        if (requestValues.isEmpty()) throw refusal("the user subtype " + from.fullyQualifiedName + " has no request value: there is nothing to ask (declare @performative \"request\")");
        for (String key : descriptorUtils.fieldNames(backendSchema)) {
            if (!descriptorUtils.isOutput(key, backendSchema)) continue;
            Descriptor d = backendSchema.getVar().get(key).get(0);
            boolean node = !descriptorUtils.getAttributeVariable(key, d).isPresent();
            String type = node ? "node" : descriptorUtils.getFromDescriptor(d, AttributeDescriptor::getType, nd -> null);
            Field o = new Field(key, type, compilerUtil.getPastTypeForDeclaredType(backendSchema.getVar(), key), false, null, null, null);
            if (node) o.valueType = nodeType(d);
            outputs.put(key, o);
        }

        readStood(configs, compilerUtil, compilerSubtype, cbindingsBaseDir);
        readAsk();
        readRecord();
        readReply();
    }

    /** The fields of a subtype's typed bean (design §5.2), in order: the ordinary inputs it keeps, the carried values, a map of other keys per carrier. */
    static Map<String, Field> fieldsOf(TemplateBindingsSchema schema, SubtypeTemplateCompilerConfig st, CompilerUtil compilerUtil, CompilerSubtype compilerSubtype) {
        Map<String, Field> res = new LinkedHashMap<>();
        Map<String, List<Descriptor>> var = schema.getVar();
        for (String key : CompilerSubtype.ordinaryInputs(schema, st)) {
            Descriptor d = var.get(key).get(0);
            boolean node = !descriptorUtils.getAttributeVariable(key, d).isPresent();
            String type = node ? "node" : descriptorUtils.getFromDescriptor(d, AttributeDescriptor::getType, nd -> null);
            String doc = descriptorUtils.getFromDescriptor(d, AttributeDescriptor::getDocumentation, NameDescriptor::getDocumentation);
            String performative = descriptorUtils.getFromDescriptor(d, AttributeDescriptor::getPerformative, NameDescriptor::getPerformative);
            Field f = new Field(key, type, compilerUtil.getPastTypeForDeclaredType(var, key), descriptorUtils.isCompulsoryInput(key, schema), performative, null, doc);
            if (node) f.valueType = nodeType(d);
            res.put(key, f);
        }
        for (Map.Entry<String, List<InJsonValue>> e : CompilerSubtype.carried(schema, st).entrySet()) {
            for (InJsonValue v : e.getValue()) {
                res.put(v.getName(), new Field(v.getName(), v.getType(), compilerUtil.getPastClassForType(v.getType()),
                        SubtypeTemplateCompilerConfig.REQUIRED.equals(st.carries.get(v.getName())), v.getPerformative(), e.getKey(), v.getDocumentation()));
            }
            String other = CompilerSubtype.otherKeysField(e.getKey());
            res.put(other, new Field(other, "other keys", ClassName.MAP_STRING_OBJECT, false, null, e.getKey(), null));
        }
        return res;
    }

    // ---- stood ---------------------------------------------------------------------------------------------------------------

    private static final Set<String> STOOD_KEYS = Set.of("node", "madeBy", "addressed", "identity", "of");

    private void readStood(TemplatesProjectConfiguration configs, CompilerUtil compilerUtil, CompilerSubtype compilerSubtype, String cbindingsBaseDir) {
        if (flow.stood.isEmpty()) throw refusal("stood is empty: a flow starts from at least one previous record");
        for (Map.Entry<String, Map<String, Object>> e : flow.stood.entrySet()) {
            String name = e.getKey();
            Map<String, Object> d = e.getValue();
            for (String k : d.keySet()) if (!STOOD_KEYS.contains(k)) throw refusal("stood." + name + ": unknown member " + k + " (" + STOOD_KEYS + ")");
            Stood s = new Stood(name);
            s.node = string(d.get("node"), "stood." + name + ".node");
            s.madeBy = string(d.get("madeBy"), "stood." + name + ".madeBy");
            if (s.node == null || s.madeBy == null) throw refusal("stood." + name + ": node and madeBy are required");
            s.addressed = string(d.get("addressed"), "stood." + name + ".addressed");
            if (d.get("identity") != null) {
                if (!(d.get("identity") instanceof Map)) throw refusal("stood." + name + ".identity is {\"user\": value, \"backend\": value}");
                Map<?, ?> id = (Map<?, ?>) d.get("identity");
                s.identityUser = string(id.get("user"), "stood." + name + ".identity.user");
                s.identityBackend = string(id.get("backend"), "stood." + name + ".identity.backend");
                if (s.identityUser == null || s.identityBackend == null) throw refusal("stood." + name + ".identity needs user and backend");
            }
            String of = string(d.get("of"), "stood." + name + ".of");
            s.record = to;
            if (of != null) {
                s.record = subtype(configs, of, "stood." + name + ".of");
                if (!Objects.equals(s.record.refines, to.refines)) {
                    throw refusal("stood." + name + ".of is " + of + ", a subtype of " + s.record.refines + ", not of " + to.refines + " (the outputs bean of the record must be the backend template's)");
                }
            }
            s.fields = s.record == to ? backendFields
                    : fieldsOf(compilerUtil.getBindingsSchema(CompilerSubtype.refined(configs, s.record), cbindingsBaseDir), s.record, compilerUtil, compilerSubtype);

            // node: an input node of the backend's bean; madeBy: an output node of the store
            Field node = backendFields.get(s.node);
            if (node == null || !node.isNode()) throw refusal("stood." + name + ".node is " + s.node + ", which is no node input of the backend subtype's bean");
            Field made = outputs.get(s.madeBy);
            if (made == null || !made.isNode()) throw refusal("stood." + name + ".madeBy is " + s.madeBy + ", which is no output node of " + backendTemplate.fullyQualifiedName);
            if (s.addressed != null) {
                Field a = userFields.get(s.addressed);
                if (a == null || !ownValues.contains(s.addressed) || !a.isNode()) {
                    throw refusal("stood." + name + ".addressed is " + s.addressed + ", which is no node among the user bean's own values (" + ownValues + ")");
                }
            }
            if (s.identityUser != null) {
                Field u = userFields.get(s.identityUser);
                if (u == null || !requestValues.contains(s.identityUser)) {
                    throw refusal("stood." + name + ".identity.user is " + s.identityUser + ", which is no request value of the user subtype (" + requestValues + ")");
                }
                Field b = s.fields.get(s.identityBackend);
                if (b == null || b.isOtherKeys() || b.isNode()) {
                    throw refusal("stood." + name + ".identity.backend is " + s.identityBackend + ", which is no value of the previous record's bean");
                }
                if (!convertible(u, b, null) && !convertible(u, b, "text")) {
                    throw refusal("stood." + name + ".identity: the user value " + s.identityUser + " (" + u.declared + ") cannot be compared with " + s.identityBackend + " (" + b.declared + ")");
                }
            }
            stood.put(name, s);
        }
        Set<String> nodes = new HashSet<>();
        for (Stood s : stood.values()) {
            if (!nodes.add(s.node)) throw refusal("the node " + s.node + " is named by two stood things");
        }
    }

    // ---- ask -----------------------------------------------------------------------------------------------------------------

    private final Map<String, String> userAccounted = new LinkedHashMap<>();

    private void readAsk() {
        List<Item> items = new ArrayList<>();
        for (String v : requestValues) {
            Item i = new Item("ask", v, userFields.get(v), Kind.AS_ASKED);
            i.source = v;
            i.from = "request";
            items.add(i);
        }
        for (Stood s : stood.values()) {
            if (s.addressed == null) continue;
            claim(userAccounted, s.addressed, "stood." + s.name + ".addressed", "user bean's own value");
            Item i = new Item("ask", s.addressed, userFields.get(s.addressed), Kind.CHAINED);
            i.thing = s.name;
            i.madeBy = s.madeBy;
            items.add(i);
        }
        for (Map.Entry<String, Object> e : flow.ask.entrySet()) {
            String target = e.getKey();
            Field f = userFields.get(target);
            if (f == null || !ownValues.contains(target)) {
                throw refusal("ask." + target + " is no own value of the user bean: the request's values are given by the request, the reply's by reply (own values: " + ownValues + ")");
            }
            claim(userAccounted, target, "ask." + target, "user bean's own value");
            if ("absent".equals(e.getValue())) {
                items.add(absentItem("ask", target, f, "ask." + target));
                continue;
            }
            Map<String, Object> d = map(e.getValue(), "ask." + target);
            Item i;
            if (d.containsKey("supplied") && d.size() == 1) {
                i = new Item("ask", target, f, Kind.SUPPLIED);
                i.rule = rule(string(d.get("supplied"), "ask." + target + ".supplied"), "ask", f, "ask." + target);
            } else if (d.containsKey("constant") && d.size() == 1) {
                i = new Item("ask", target, f, Kind.CONSTANT);
                i.constant = constant(f, d.get("constant"), "ask." + target);
            } else {
                throw refusal("ask." + target + " is \"absent\", {\"supplied\": rule} or {\"constant\": value}");
            }
            items.add(i);
        }
        List<String> missing = new ArrayList<>();
        for (String v : ownValues) if (!userAccounted.containsKey(v)) missing.add(v);
        if (!missing.isEmpty()) {
            throw refusal("the user bean's own values " + missing + " are not accounted for: say each in ask (supplied or constant), or as the addressed node of a stood thing");
        }
        steps.put("ask", items);
    }

    // ---- record --------------------------------------------------------------------------------------------------------------

    private final Map<String, String> backendAccounted = new LinkedHashMap<>();

    private void readRecord() {
        List<Item> items = new ArrayList<>();
        for (Stood s : stood.values()) {
            claim(backendAccounted, s.node, "stood." + s.name + ".node", "backend value");
            Item i = new Item("record", s.node, backendFields.get(s.node), Kind.CHAINED);
            i.thing = s.name;
            i.madeBy = s.madeBy;
            items.add(i);
        }
        for (Map.Entry<String, Object> e : flow.record.entrySet()) {
            String target = e.getKey();
            Field f = backendFields.get(target);
            if (f == null) throw refusal("record." + target + " is no input of the backend subtype " + to.fullyQualifiedName + " (inputs: " + backendFields.keySet() + ")");
            claim(backendAccounted, target, "record." + target, "backend value");
            items.add(recordItem(target, f, e.getValue()));
        }
        // carried over, by name
        String restThing = null;
        for (Map.Entry<String, Object> e : flow.carry.entrySet()) {
            Stood s = stood.get(e.getKey());
            if (s == null) throw refusal("carry." + e.getKey() + " names no stood thing (" + stood.keySet() + ")");
            if ("rest".equals(e.getValue())) {
                if (restThing != null) throw refusal("carry: \"rest\" is used for two stood things (" + restThing + " and " + s.name + "): it is for at most one");
                restThing = s.name;
            } else if (e.getValue() instanceof List) {
                for (Object o : (List<?>) e.getValue()) {
                    String target = string(o, "carry." + s.name);
                    Field f = backendFields.get(target);
                    if (f == null) throw refusal("carry." + s.name + " names " + target + ", which is no input of the backend subtype");
                    claim(backendAccounted, target, "carry." + s.name, "backend value");
                    items.add(carriedItem(s, f, target));
                }
            } else {
                throw refusal("carry." + s.name + " is a list of names or \"rest\"");
            }
        }
        // the other keys of a carrier
        for (Map.Entry<String, String> e : flow.carryOpen.entrySet()) {
            Stood s = stood.get(e.getValue());
            if (s == null) throw refusal("carryOpen." + e.getKey() + " names no stood thing (" + stood.keySet() + ")");
            String target = CompilerSubtype.otherKeysField(e.getKey());
            Field f = backendFields.get(target);
            if (f == null) throw refusal("carryOpen names the carrier " + e.getKey() + ", of which the backend subtype carries no value (" + carriersOf(backendFields) + ")");
            claim(backendAccounted, target, "carryOpen." + e.getKey(), "backend value");
            items.add(carriedItem(s, f, target));
        }
        // the rest
        List<String> left = new ArrayList<>();
        for (Field f : backendFields.values()) if (!backendAccounted.containsKey(f.name)) left.add(f.name);
        if (restThing != null) {
            Stood s = stood.get(restThing);
            for (String target : new ArrayList<>(left)) {
                Field f = backendFields.get(target);
                if (f.isOtherKeys()) continue;   // the open keys are said by carryOpen, or absent
                claim(backendAccounted, target, "carry." + restThing + " (rest)", "backend value");
                items.add(carriedItem(s, f, target));
                left.remove(target);
            }
        }
        if (!left.isEmpty()) {
            throw refusal("the backend subtype's inputs " + left + " are not accounted for: say each in record (asked, carried, constant, supplied or absent), in carry"
                    + ", or in carryOpen for a map of other keys");
        }
        List<Field> order = new ArrayList<>(backendFields.values());
        items.sort(Comparator.comparingInt((Item i) -> i.kind.ordinal()).thenComparingInt(i -> order.indexOf(i.field)));
        steps.put("record", items);
    }

    private static Set<String> carriersOf(Map<String, Field> fields) {
        Set<String> res = new TreeSet<>();
        for (Field f : fields.values()) if (f.carrier != null) res.add(f.carrier);
        return res;
    }

    private Item recordItem(String target, Field f, Object spec) {
        String place = "record." + target;
        if ("asked".equals(spec)) return askedItem(target, f, target, null, place);
        if ("absent".equals(spec)) return absentItem("record", target, f, place);
        Map<String, Object> d = map(spec, place);
        Set<String> keys = new TreeSet<>(d.keySet());
        if (keys.equals(Set.of("asked")) || keys.equals(Set.of("asked", "as"))) {
            return askedItem(target, f, string(d.get("asked"), place + ".asked"), string(d.get("as"), place + ".as"), place);
        }
        if (keys.equals(Set.of("carried"))) {
            String c = string(d.get("carried"), place + ".carried");
            int dot = c == null ? -1 : c.indexOf('.');
            if (dot < 1 || dot == c.length() - 1) throw refusal(place + ".carried is \"thing.value\" (not " + c + ")");
            Stood s = stood.get(c.substring(0, dot));
            if (s == null) throw refusal(place + ".carried names no stood thing: " + c.substring(0, dot) + " (" + stood.keySet() + ")");
            return carriedItem(s, f, c.substring(dot + 1));
        }
        if (keys.equals(Set.of("constant"))) {
            Item i = new Item("record", target, f, Kind.CONSTANT);
            i.constant = constant(f, d.get("constant"), place);
            return i;
        }
        if (keys.equals(Set.of("supplied"))) {
            Item i = new Item("record", target, f, Kind.SUPPLIED);
            i.rule = rule(string(d.get("supplied"), place + ".supplied"), "record", f, place);
            return i;
        }
        throw refusal(place + " is \"asked\", \"absent\", or one of {\"asked\": value, \"as\": \"text\"}, {\"carried\": \"thing.value\"}, {\"constant\": value}, {\"supplied\": rule}");
    }

    private Item askedItem(String target, Field f, String user, String as, String place) {
        Field u = userFields.get(user);
        if (u == null || u.isOtherKeys() || "reply".equals(u.performative)) {
            throw refusal(place + ": asked names " + user + ", which is no request value or own value of the user bean (" + new ArrayList<>(requestValues) + ownValues + ")");
        }
        if (!convertible(u, f, as)) throw refusal(place + ": the user value " + user + " (" + u.declared + ") cannot be given to " + f.name + " (" + f.declared + ")"
                + (as == null ? "; say \"as\": \"text\" for a number or a boolean" : ""));
        Item i = new Item("record", target, f, Kind.AS_ASKED);
        i.source = user;
        i.as = as;
        i.from = "user";
        return i;
    }

    private Item carriedItem(Stood s, Field target, String name) {
        Field src = s.fields.get(name);
        String place = "the " + s.name + "'s " + name;
        if (src == null) throw refusal(place + " does not exist: the previous record's bean (" + s.record.fullyQualifiedName + ") has no " + name + " to carry to " + target.name);
        if (!convertible(src, target, null)) throw refusal(place + " (" + src.declared + ") cannot be carried to " + target.name + " (" + target.declared + "): the types differ");
        Item i = new Item("record", target.name, target, Kind.CARRIED);
        i.thing = s.name;
        i.source = name;
        return i;
    }

    private Item absentItem(String step, String target, Field f, String place) {
        if (f.compulsory) throw refusal(place + " is absent, but " + target + " is compulsory: it cannot be left unset");
        return new Item(step, target, f, Kind.ABSENT);
    }

    // ---- reply ---------------------------------------------------------------------------------------------------------------

    private static final Set<String> BLOCKS = new LinkedHashSet<>(List.of("answer", "refused", "failed"));

    private void readReply() {
        for (String block : flow.reply.keySet()) if (!BLOCKS.contains(block)) throw refusal("reply." + block + " is not one of " + BLOCKS);
        if (replyValues.isEmpty()) {
            if (!flow.reply.isEmpty()) throw refusal("reply is declared, but the user subtype has no reply value (declare @performative \"reply\")");
            return;
        }
        if (!flow.reply.containsKey("answer")) throw refusal("reply.answer is required: how the reply is given from what the store made");
        for (Map.Entry<String, Map<String, Object>> b : flow.reply.entrySet()) {
            String block = b.getKey();
            List<Item> items = new ArrayList<>();
            Set<String> done = new HashSet<>();
            for (Map.Entry<String, Object> e : b.getValue().entrySet()) {
                String target = e.getKey();
                String place = "reply." + block + "." + target;
                Field f = userFields.get(target);
                if (f == null || !replyValues.contains(target)) throw refusal(place + " is no reply value of the user bean (" + replyValues + ")");
                if (!done.add(target)) throw refusal(place + " is given twice");
                items.add(replyItem(block, target, f, e.getValue(), place));
            }
            List<String> missing = new ArrayList<>();
            for (String v : replyValues) if (!done.contains(v)) missing.add(v);
            if (!missing.isEmpty()) throw refusal("reply." + block + " does not account for the reply values " + missing + " (give each, or say \"absent\")");
            items.sort(Comparator.comparingInt(i -> replyValues.indexOf(i.target)));
            steps.put(block, items);
        }
    }

    private Item replyItem(String block, String target, Field f, Object spec, String place) {
        Item i = replyItem0(block, target, f, spec, place);
        if (i.kind == Kind.AS_MADE && !"answer".equals(block)) {
            throw refusal(place + ": nothing was recorded or made in a " + block + " reply: only answer can say what the store made (use constant, supplied or argument)");
        }
        return i;
    }

    private Item replyItem0(String block, String target, Field f, Object spec, String place) {
        if ("absent".equals(spec)) return absentItem(block, target, f, place);
        Map<String, Object> d = map(spec, place);
        Set<String> keys = new TreeSet<>(d.keySet());
        if (keys.equals(Set.of("constant"))) {
            Item i = new Item(block, target, f, Kind.CONSTANT);
            i.constant = constant(f, d.get("constant"), place);
            return i;
        }
        if (keys.equals(Set.of("recorded")) || keys.equals(Set.of("recorded", "as"))) {
            String name = string(d.get("recorded"), place + ".recorded");
            Field b = backendFields.get(name);
            String as = string(d.get("as"), place + ".as");
            if (b == null || b.isOtherKeys()) throw refusal(place + ".recorded is " + name + ", which is no value of the backend's bean");
            if (!convertible(b, f, as)) throw refusal(place + ": the backend value " + name + " (" + b.declared + ") cannot be given to " + target + " (" + f.declared + ")");
            Item i = new Item(block, target, f, Kind.AS_MADE);
            i.source = name;
            i.as = as;
            i.from = "recorded";
            return i;
        }
        if (keys.equals(Set.of("made"))) {
            String name = string(d.get("made"), place + ".made");
            Field o = outputs.get(name);
            if (o == null) throw refusal(place + ".made is " + name + ", which is no output of " + backendTemplate.fullyQualifiedName + " (outputs: " + outputs.keySet() + ")");
            if (!convertible(o, f, null)) throw refusal(place + ": the output " + name + " (" + o.declared + ") cannot be given to " + target + " (" + f.declared + ")");
            Item i = new Item(block, target, f, Kind.AS_MADE);
            i.source = name;
            i.from = "made";
            return i;
        }
        if (keys.equals(Set.of("is", "otherwise", "when"))) {
            Map<String, Object> w = map(d.get("when"), place + ".when");
            String name = w.size() == 1 ? string(w.get("made"), place + ".when.made") : null;
            if (name == null || !outputs.containsKey(name)) throw refusal(place + ".when is {\"made\": output} with an output of " + backendTemplate.fullyQualifiedName + " (" + outputs.keySet() + ")");
            Item i = new Item(block, target, f, Kind.AS_MADE);
            i.whenMade = name;
            i.is = constant(f, d.get("is"), place + ".is");
            i.otherwise = constant(f, d.get("otherwise"), place + ".otherwise");
            i.from = "made";
            return i;
        }
        if (keys.equals(Set.of("supplied"))) {
            Item i = new Item(block, target, f, Kind.SUPPLIED);
            i.rule = rule(string(d.get("supplied"), place + ".supplied"), block, f, place);
            return i;
        }
        if (keys.equals(Set.of("argument"))) {
            if (!Boolean.TRUE.equals(d.get("argument"))) throw refusal(place + ".argument is true");
            Item i = new Item(block, target, f, Kind.SUPPLIED);
            i.argument = true;
            return i;
        }
        throw refusal(place + " is \"absent\", or one of {\"constant\": v}, {\"recorded\": value, \"as\": \"text\"}, {\"made\": output}, {\"when\": {\"made\": output}, \"is\": v, \"otherwise\": v}, {\"supplied\": rule}, {\"argument\": true}");
    }

    // ---- pieces --------------------------------------------------------------------------------------------------------------

    private void claim(Map<String, String> accounted, String target, String place, String what) {
        String before = accounted.put(target, place);
        if (before != null) throw refusal("the " + what + " " + target + " is accounted for twice: in " + before + " and in " + place);
    }

    private String rule(String name, String step, Field target, String place) {
        if (name == null || !name.matches("[A-Za-z_][A-Za-z0-9_]*")) throw refusal(place + ": the rule's name is an identifier (not " + name + ")");
        Rule before = rules.get(name);
        if (before != null) throw refusal(place + ": the rule " + name + " is already the rule of " + before.target + " in " + before.step + ": a supplied value has a rule of its own");
        rules.put(name, new Rule(name, step, target.name, target.past));
        return name;
    }

    /** Whether a value can be given to a field: the same kind and PAST type, or a number or a boolean as text. */
    private static boolean convertible(Field from, Field to, String as) {
        if (from.isOtherKeys() || to.isOtherKeys()) return from.isOtherKeys() && to.isOtherKeys() && as == null;
        if (as == null) return from.isNode() == to.isNode() && String.valueOf(from.past).equals(String.valueOf(to.past));
        if (!"text".equals(as)) return false;
        return !to.isNode() && "xsd:string".equals(to.declared) && !from.isNode() && Set.of("xsd:int", "xsd:long", "xsd:float", "xsd:double", "xsd:boolean").contains(from.declared);
    }

    /** The type of the value a node's field holds: its {@code @type}, else a text. */
    static String nodeType(Descriptor d) {
        String t = descriptorUtils.getFromDescriptor(d, ad -> null, NameDescriptor::getType);
        return t == null ? "xsd:string" : t;
    }

    private Object constant(Field f, Object c, String place) {
        if (f.isNode()) {
            if ("xsd:string".equals(f.valueType)) {
                if (!(c instanceof String) || ((String) c).isEmpty() || ((String) c).contains(":")) {
                    throw refusal(place + ": the constant of a node is its local part, a text without prefix (not " + c + ")");
                }
                return c;
            }
            String why = SubtypeChecker.misfit(f.valueType, c);
            if (why != null) throw refusal(place + ": the constant " + c + " (for the node " + f.name + ", " + f.valueType + ") " + why);
            return c;
        }
        if (f.isOtherKeys()) throw refusal(place + ": a map of other keys has no constant");
        String why = SubtypeChecker.misfit(f.declared, c);
        if (why != null) throw refusal(place + ": the constant " + c + " (for " + f.name + ", " + f.declared + ") " + why);
        return c;
    }

    private String string(Object o, String place) {
        if (o == null) return null;
        if (!(o instanceof String)) throw refusal(place + " is a text (not " + o + ")");
        return (String) o;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> map(Object o, String place) {
        if (!(o instanceof Map)) throw refusal(place + " is an object (not " + o + ")");
        return (Map<String, Object>) o;
    }
}
