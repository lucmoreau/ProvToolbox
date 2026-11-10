package org.openprovenance.prov.template.compiler.privacy;

import org.openprovenance.prov.template.descriptors.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * T31: the data-protection annotations of a catalogue's templates, as the application decides them.
 * <ul>
 *     <li>R1, R7: the library's annotations state intent; the application's policy file (the catalogue's {@code privacy}
 *     member) decides: honour, ignore, replace, or add, per scope; the most specific scope wins.</li>
 *     <li>R4: methods are data (built-ins in {@code privacy/methods.json}, the policy's {@code methods}), or a
 *     {@link PrivacyMethod} class; a definition under a built-in's name replaces it.</li>
 *     <li>Ruling 1: every method is resolved once, here, when the code is generated; its digest identifies what it
 *     meant.</li>
 *     <li>R11, Rulings 2 and 4, R16: what is refused, each refusal naming template, variable and method.</li>
 *     <li>R12: the register, one row per annotated variable.</li>
 * </ul>
 * The policy is applied where a bindings schema is read ({@code CompilerUtil.getBindingsSchema}), through
 * {@link #current()}, so every generator sees the effective annotations.
 */
public class PrivacyPolicy {

    public static final String PT = "pt:";
    public static final String DEFAULT_TRANSIENT = "pt:drop";
    static final String BUILT_INS = "/privacy/methods.json";
    static final Set<String> LANGUAGES = Set.of("java", "python", "javascript", "rust");
    static final String CUSTOM_PAST_NOTE = "client check defined in application code";
    static final String NOT_IN_RUST_NOTE = "not checked by the generated Rust client (Ruling 7)";

    private static volatile PrivacyPolicy current;

    private final ObjectMapper om = JsonMapper.builderWithJackson2Defaults().build();
    private final ObjectMapper canonical = JsonMapper.builderWithJackson2Defaults()
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true).build();

    /** Resolved definitions by qualified name, and where each came from. */
    private final Map<String, MethodDefinition> methods = new LinkedHashMap<>();
    private final Map<String, String> definedBy = new HashMap<>();
    private final Set<String> builtInNames;
    private final Map<String, Decision> overrides = new LinkedHashMap<>();
    private final Set<String> languages;
    private final boolean hasPolicyFile;

    private final Map<String, String> templateByBindingsPath = new HashMap<>();
    private final Map<String, TemplateBindingsSchema> appliedTemplates = new LinkedHashMap<>();
    private final Map<String, RegisterEntry> register = new LinkedHashMap<>();
    private final List<String> warnings = new ArrayList<>();

    /**
     * @param policyFile the application's policy file, or null when the catalogue has none
     * @param languages  the languages the catalogue generates ({@code java}, {@code python}, {@code javascript}, {@code rust})
     */
    public PrivacyPolicy(File policyFile, Set<String> languages) {
        this.languages = languages;
        this.hasPolicyFile = policyFile != null;
        try (InputStream in = PrivacyPolicy.class.getResourceAsStream(BUILT_INS)) {
            Map<String, MethodDefinition> builtIns = om.readValue(in, new TypeReference<LinkedHashMap<String, MethodDefinition>>() {});
            builtIns.forEach((name, def) -> define(name, def, "built-in"));
            builtInNames = Set.copyOf(builtIns.keySet());
        } catch (java.io.IOException e) {
            throw new IllegalStateException("T31: cannot read the built-in methods " + BUILT_INS, e);
        }
        for (PrivacyMethod m : ServiceLoader.load(PrivacyMethod.class, PrivacyMethod.class.getClassLoader())) {
            defineChecked(qualify(m.name()), m.definition(), "class:" + m.getClass().getName());
        }
        if (policyFile != null) {
            PolicyFile policy;
            try {
                policy = om.readValue(policyFile, PolicyFile.class);
            } catch (Exception e) {
                throw new PrivacyException("cannot read the privacy policy " + policyFile + ": " + e.getMessage());
            }
            policy.methods.forEach((name, def) -> {
                String q = qualify(name);
                if (def.className != null) {
                    defineChecked(q, instantiate(q, def.className).definition(), "class:" + def.className);
                } else {
                    defineChecked(q, def, "policy");
                }
            });
            policy.overrides.forEach((scope, node) -> overrides.put(checkScope(scope), Decision.parse(scope, node)));
        }
        methods.forEach(this::validate);
    }

    /** The policy of the generation in progress, applied wherever a bindings schema is read; null outside one. */
    public static PrivacyPolicy current() {
        return current;
    }

    public static void setCurrent(PrivacyPolicy policy) {
        current = policy;
    }

    /** The languages a catalogue generates, as the compiler decides them. */
    public static Set<String> languages(boolean python, boolean javascript, boolean rust) {
        Set<String> res = new TreeSet<>();
        res.add("java");
        if (python) res.add("python");
        if (javascript) res.add("javascript");
        if (rust) res.add("rust");
        return res;
    }

    /** Declares that a bindings file describes a template, so that template and variable scopes can apply to it. */
    public void registerBindings(String bindingsPath, String templateFullyQualifiedName) {
        templateByBindingsPath.putIfAbsent(canonical(bindingsPath), templateFullyQualifiedName);
    }

    /** Applies the policy to a bindings schema just read from a file; a file not registered is left as it is. */
    public void applyToFile(String bindingsPath, TemplateBindingsSchema schema) {
        String template = templateByBindingsPath.get(canonical(bindingsPath));
        if (template != null) apply(template, schema);
    }

    /** Resolves every variable's effective annotation; sets it on the descriptors; records the register rows. */
    public void apply(String template, TemplateBindingsSchema schema) {
        appliedTemplates.put(template, schema);
        Map<String, List<Descriptor>> vars = schema.getVar();
        for (String key : vars.keySet()) {
            if (vars.get(key) == null || vars.get(key).isEmpty()) continue;
            Descriptor descriptor = vars.get(key).get(0);
            Annotated d = Annotated.of(descriptor);
            String where = "template " + template + ", variable " + key;

            List<Ann> declared = new ArrayList<>();
            Object t = d.getTransient();
            if (t instanceof Boolean) {
                if ((Boolean) t) declared.add(new Ann(EffectivePrivacy.Kind.TRANSIENT, DEFAULT_TRANSIENT));
            } else if (t instanceof String) {
                declared.add(new Ann(EffectivePrivacy.Kind.TRANSIENT, qualify((String) t)));
            } else if (t != null) {
                throw new PrivacyException(where + ": @transient must be true, false or a method name, not " + t);
            }
            if (d.getPseudonym() != null) declared.add(new Ann(EffectivePrivacy.Kind.PSEUDONYM, qualify(d.getPseudonym())));

            // R7: the most specific scope decides; variable and template scopes decide for the variable as a whole
            List<Ann> effective = new ArrayList<>();
            String decidedBy = "library";
            String reason = null;
            String variableScope = "variable:" + template + "#" + key;
            String templateScope = "template:" + template;
            String wholeScope = overrides.containsKey(variableScope) ? variableScope
                    : overrides.containsKey(templateScope) ? templateScope : null;
            if (wholeScope != null) {
                Decision dec = overrides.get(wholeScope);
                decidedBy = wholeScope;
                reason = dec.reason;
                switch (dec.action) {
                    case HONOUR: effective.addAll(declared); break;
                    case IGNORE: break;
                    case REPLACE:
                        // a replacement may add an annotation the library didn't declare only at variable scope
                        if (!declared.isEmpty() || wholeScope.equals(variableScope)) effective.add(dec.replacement);
                        break;
                }
            } else {
                for (Ann a : declared) {
                    String scope = overrides.containsKey("method:" + a.method) ? "method:" + a.method
                            : overrides.containsKey("*") ? "*" : null;
                    if (scope == null) {
                        effective.add(a);
                        continue;
                    }
                    Decision dec = overrides.get(scope);
                    decidedBy = scope;
                    reason = dec.reason;
                    switch (dec.action) {
                        case HONOUR: effective.add(a); break;
                        case IGNORE: break;
                        case REPLACE: effective.add(dec.replacement); break;
                    }
                }
            }
            if (declared.isEmpty() && effective.isEmpty()) {
                d.setEffectivePrivacy(null);
                continue;
            }

            RegisterEntry row = new RegisterEntry();
            row.template = template;
            row.variable = key;
            row.declared = declared.isEmpty() ? null : declared.stream().map(Ann::toString).collect(Collectors.joining(", "));
            row.decidedBy = decidedBy;
            row.reason = reason;
            register.put(template + "#" + key, row);

            if (effective.size() > 1) {
                throw new PrivacyException(where + ": both @transient and @pseudonym (" + row.declared + ")");
            }
            if (effective.isEmpty()) {
                row.effective = "ignored";
                d.setEffectivePrivacy(null);
                continue;
            }
            Ann a = effective.get(0);
            String what = where + ", method " + a.method;
            MethodDefinition def = methods.get(a.method);
            if (def == null) throw new PrivacyException(what + ": unknown method");
            if (!def.kind.equals(a.kind == EffectivePrivacy.Kind.TRANSIENT ? "transient" : "pseudonym")) {
                throw new PrivacyException(what + ": a " + def.kind + " method can't be used with " + a.annotationName());
            }
            checkVariable(what, a, key, d, schema);

            EffectivePrivacy ep = effective(a, def);
            d.setEffectivePrivacy(ep);
            row.effective = a.toString();
            row.definedBy = definedBy.get(a.method);
            row.digest = ep.digest;
            if (ep.isTransient()) {
                row.keeps = ep.keeps.stream().map(p -> p.name().toLowerCase()).sorted().collect(Collectors.toList());
            } else {
                row.pattern = ep.pattern;
                if (ep.past != null) row.note = CUSTOM_PAST_NOTE;
                if (languages.contains("rust") && !checkedInRust(a.method)) {
                    row.note = (row.note == null) ? NOT_IN_RUST_NOTE : row.note + "; " + NOT_IN_RUST_NOTE;
                }
            }
            row.resolver = ep.resolver;
        }
    }

    /** R11: a policy scope must name a template of the catalogue, and a variable of that template. */
    public void checkScopes(Collection<String> catalogueTemplates) {
        for (String scope : overrides.keySet()) {
            if (scope.startsWith("template:")) {
                String t = scope.substring("template:".length());
                if (!catalogueTemplates.contains(t)) throw new PrivacyException("policy scope " + scope + ": unknown template");
            } else if (scope.startsWith("variable:")) {
                String rest = scope.substring("variable:".length());
                int i = rest.lastIndexOf('#');
                String t = rest.substring(0, i);
                String v = rest.substring(i + 1);
                if (!catalogueTemplates.contains(t)) throw new PrivacyException("policy scope " + scope + ": unknown template");
                TemplateBindingsSchema schema = appliedTemplates.get(t);
                if (schema != null && !schema.getVar().containsKey(v)) {
                    throw new PrivacyException("policy scope " + scope + ": unknown variable " + v);
                }
            }
        }
    }

    /**
     * R11: a composite's elements are tied together by the value of each variable it shares, so a shared variable
     * can't be transient. R8: otherwise a composite uses its component's bindings, and so its effective annotations.
     */
    public void checkComposite(String composite, String component, Collection<String> sharing) {
        if (sharing == null) return;
        TemplateBindingsSchema schema = appliedTemplates.get(component);
        if (schema == null) return;
        for (String key : sharing) {
            EffectivePrivacy ep = new DescriptorUtils().getPrivacy(key, schema).orElse(null);
            if (ep != null && ep.kind == EffectivePrivacy.Kind.TRANSIENT) {
                throw new PrivacyException("template " + component + ", variable " + key + ", method " + ep.method
                        + ": @transient on a variable shared by the composite " + composite
                        + ", whose elements are tied together by its value");
            }
        }
    }

    /**
     * Whether the generated Rust client checks a pseudonym method's values. Never, until the Rust registry maps a
     * static call (R16): neither the built-in {@code Pseudonyms.check} nor an application's own PAST method, even one
     * listing {@code rust} among its targets, reaches the Rust code. Ruling 7: the Rust client leaves the check out.
     */
    public boolean checkedInRust(String method) {
        return false;
    }

    /** The warnings of this generation, also printed on standard error as they arise. */
    public List<String> warnings() {
        return Collections.unmodifiableList(warnings);
    }

    private void warn(String message) {
        String m = "T31: " + message;
        if (warnings.contains(m)) return;
        warnings.add(m);
        System.err.println("[privacy] WARNING: " + m);
    }

    /** R12: the register's rows, in catalogue order. Empty when no variable of the catalogue is annotated. */
    public List<RegisterEntry> register() {
        return new ArrayList<>(register.values());
    }

    public boolean hasPolicyFile() {
        return hasPolicyFile;
    }

    public MethodDefinition method(String name) {
        return methods.get(qualify(name));
    }

    public static String qualify(String name) {
        return name.contains(":") ? name : PT + name;
    }

    // ---------------------------------------------------------------------------------------------------------------

    private void checkVariable(String what, Ann a, String key, Annotated d, TemplateBindingsSchema schema) {
        // Ruling 2: inputs only, whether declared by the library or added by a policy
        if (!InputFieldValue.isInput(d.getInput()) || OutputFieldValue.isOutput(d.getOutput())) {
            throw new PrivacyException(what + ": " + a.annotationName()
                    + " is only for an input (@input compulsory or optional, not an @output)");
        }
        if (a.kind == EffectivePrivacy.Kind.TRANSIENT) {
            if (d.isName()) {
                throw new PrivacyException(what + ": @transient on a name variable, which is in an identifier position: unbound, it would suppress its node");
            }
            if (d.isUnique()) throw new PrivacyException(what + ": @transient with @unique: a transient value has no column");
            for (Map.Entry<String, List<Descriptor>> e : schema.getVar().entrySet()) {
                if (e.getValue() == null || e.getValue().isEmpty()) continue;
                Descriptor other = e.getValue().get(0);
                if (other instanceof NameDescriptor) {
                    NameDescriptor nd = (NameDescriptor) other;
                    if ((nd.getNewInputs() != null && nd.getNewInputs().containsValue(key))
                            || (nd.getAlsoOutputs() != null && nd.getAlsoOutputs().containsValue(key))) {
                        throw new PrivacyException(what + ": @transient on a variable used by @sql.new.inputs / @sql.also.outputs of " + e.getKey());
                    }
                }
            }
        } else {
            MethodDefinition def = methods.get(a.method);
            if (languages.contains("rust") && !checkedInRust(a.method)) {
                // Ruling 7: the Rust client leaves the check out, with a warning; the other clients and the database keep it
                warn(what + ": the generated Rust client doesn't check this pseudonym (the Rust registry doesn't map"
                        + " the call); the other clients and the database constraint still do");
            }
            if (def.past != null) {
                for (String lang : languages) {
                    if (lang.equals("rust")) continue;   // Ruling 7: left out of the Rust client, with the warning above
                    if (!def.targets.contains(lang)) {
                        throw new PrivacyException(what + ": the catalogue generates " + lang
                                + ", for which the method's PAST method is not provided (targets " + def.targets + ")");
                    }
                }
            }
        }
    }

    private EffectivePrivacy effective(Ann a, MethodDefinition def) {
        Set<EffectivePrivacy.Place> keeps = EnumSet.noneOf(EffectivePrivacy.Place.class);
        if (def.keeps != null) def.keeps.forEach(p -> keeps.add(EffectivePrivacy.Place.valueOf(p.toUpperCase())));
        return new EffectivePrivacy(a.kind, a.method, keeps, def.pattern, def.sql,
                def.past == null ? null : Collections.unmodifiableMap(new TreeMap<>(def.past)), def.resolver, digest(def));
    }

    /** Ruling 1: the SHA-256 of the canonical JSON of what a method means (not its name, nor its documentation). */
    String digest(MethodDefinition def) {
        Map<String, Object> meaning = new TreeMap<>();
        meaning.put("kind", def.kind);
        if ("transient".equals(def.kind)) {
            meaning.put("keeps", def.keeps == null ? List.of() : def.keeps.stream().sorted().collect(Collectors.toList()));
        } else {
            meaning.put("pattern", def.pattern);
            if (def.sql != null) meaning.put("sql", def.sql);
            if (def.past != null) meaning.put("past", new TreeMap<>(def.past));
        }
        try {
            byte[] bytes = canonical.writeValueAsString(meaning).getBytes(StandardCharsets.UTF_8);
            byte[] h = MessageDigest.getInstance("SHA-256").digest(bytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : h) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private void define(String name, MethodDefinition def, String source) {
        methods.put(name, def);
        definedBy.put(name, source);
    }

    /** R3: an application may redefine a built-in, not invent a method under {@code pt:}. */
    private void defineChecked(String name, MethodDefinition def, String source) {
        if (name.startsWith(PT) && !builtInNames.contains(name)) {
            throw new PrivacyException("method " + name + ": pt: is reserved for ProvToolbox; an application may redefine a built-in, not add a pt: method");
        }
        define(name, def, source);
    }

    private PrivacyMethod instantiate(String name, String className) {
        try {
            return (PrivacyMethod) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException | ClassCastException e) {
            throw new PrivacyException("method " + name + ": cannot instantiate PrivacyMethod class " + className + ": " + e);
        }
    }

    /** R4, R11: a definition must be complete, and its pattern within the subset all targets read alike. */
    private void validate(String name, MethodDefinition def) {
        String m = "method " + name;
        if (def == null || def.kind == null) throw new PrivacyException(m + ": no kind (transient or pseudonym)");
        switch (def.kind) {
            case "transient":
                if (def.keeps == null) throw new PrivacyException(m + ": a transient method needs keeps");
                for (String p : def.keeps) {
                    if (!Set.of("store", "log", "expansion").contains(p)) {
                        throw new PrivacyException(m + ": keeps names an unknown place " + p + " (store, log, expansion)");
                    }
                }
                break;
            case "pseudonym":
                if (def.pattern == null) throw new PrivacyException(m + ": a pseudonym method needs a pattern");
                checkPattern(m, def.pattern);
                if (def.past != null) {
                    for (String k : List.of("package", "class", "method")) {
                        if (def.past.get(k) == null) throw new PrivacyException(m + ": past needs " + k);
                    }
                    if (def.targets == null || def.targets.isEmpty()) throw new PrivacyException(m + ": past needs targets");
                    for (String l : def.targets) {
                        if (!LANGUAGES.contains(l)) throw new PrivacyException(m + ": unknown target " + l + " " + LANGUAGES);
                    }
                }
                break;
            default:
                throw new PrivacyException(m + ": unknown kind " + def.kind);
        }
    }

    /** No escapes for character classes ({@code \d}, {@code \w}, ...), no {@code (?...)} groups: they differ between engines. */
    static void checkPattern(String m, String pattern) {
        // anchored, so that Java matches(), Python fullmatch, JavaScript test, Rust is_match and PostgreSQL ~ agree
        if (!pattern.startsWith("^") || !pattern.endsWith("$")) {
            throw new PrivacyException(m + ": pattern " + pattern + " must be anchored, ^...$, so that every target matches the whole value");
        }
        if (pattern.contains("(?") || Pattern.compile("\\\\[a-zA-Z]").matcher(pattern).find()) {
            throw new PrivacyException(m + ": pattern " + pattern + " is outside the subset all targets read alike (no \\d, \\w, \\s, \\b, no (?...))");
        }
        try {
            Pattern.compile(pattern);
        } catch (PatternSyntaxException e) {
            throw new PrivacyException(m + ": invalid pattern " + pattern + ": " + e.getDescription());
        }
    }

    private static String checkScope(String scope) {
        if (scope.equals("*") || scope.startsWith("method:") || scope.startsWith("template:")
                || (scope.startsWith("variable:") && scope.indexOf('#') > "variable:".length())) {
            return scope;
        }
        throw new PrivacyException("policy scope " + scope + ": not one of *, method:<m>, template:<fqn>, variable:<fqn>#<variable>");
    }

    private static String canonical(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (java.io.IOException e) {
            return new File(path).getAbsolutePath();
        }
    }

    // ---------------------------------------------------------------------------------------------------------------

    static final class Ann {
        final EffectivePrivacy.Kind kind;
        final String method;

        Ann(EffectivePrivacy.Kind kind, String method) {
            this.kind = kind;
            this.method = method;
        }

        String annotationName() {
            return kind == EffectivePrivacy.Kind.TRANSIENT ? "@transient" : "@pseudonym";
        }

        @Override
        public String toString() {
            return annotationName() + "(" + method + ")";
        }
    }

    enum Action { HONOUR, IGNORE, REPLACE }

    static final class Decision {
        final Action action;
        final Ann replacement;
        final String reason;

        Decision(Action action, Ann replacement, String reason) {
            this.action = action;
            this.replacement = replacement;
            this.reason = reason;
        }

        /** {@code "honour"}, {@code {"decision": "ignore", "reason": ...}}, {@code {"@pseudonym": m, "reason": ...}}. */
        static Decision parse(String scope, JsonNode node) {
            String where = "policy scope " + scope;
            if (node.isTextual()) {
                if (node.asText().equals("honour")) return new Decision(Action.HONOUR, null, null);
                throw new PrivacyException(where + ": a bare decision can only be \"honour\"; ignore and replacements are objects with a reason");
            }
            if (!node.isObject()) throw new PrivacyException(where + ": a decision is \"honour\" or an object");
            Set<String> keys = new HashSet<>();
            node.propertyNames().forEach(keys::add);
            keys.remove("reason");
            if (keys.size() != 1) {
                throw new PrivacyException(where + ": a decision has exactly one of decision, @transient, @pseudonym (and a reason)");
            }
            String k = keys.iterator().next();
            String reason = node.has("reason") ? node.get("reason").asText() : null;
            Decision d;
            switch (k) {
                case "decision": {
                    String v = node.get(k).asText();
                    if (v.equals("honour")) d = new Decision(Action.HONOUR, null, reason);
                    else if (v.equals("ignore")) d = new Decision(Action.IGNORE, null, reason);
                    else throw new PrivacyException(where + ": decision " + v + " is not honour or ignore");
                    break;
                }
                case "@transient": {
                    JsonNode v = node.get(k);
                    String m = v.isBoolean() && v.asBoolean() ? DEFAULT_TRANSIENT : v.isTextual() ? qualify(v.asText()) : null;
                    if (m == null) throw new PrivacyException(where + ": @transient must be true or a method name");
                    d = new Decision(Action.REPLACE, new Ann(EffectivePrivacy.Kind.TRANSIENT, m), reason);
                    break;
                }
                case "@pseudonym": {
                    JsonNode v = node.get(k);
                    if (!v.isTextual()) throw new PrivacyException(where + ": @pseudonym must be a method name");
                    d = new Decision(Action.REPLACE, new Ann(EffectivePrivacy.Kind.PSEUDONYM, qualify(v.asText())), reason);
                    break;
                }
                default:
                    throw new PrivacyException(where + ": unknown member " + k);
            }
            if (d.action != Action.HONOUR && (reason == null || reason.isBlank())) {
                throw new PrivacyException(where + ": a decision other than honour needs a reason, kept in the register");
            }
            return d;
        }
    }

    /** The members of a name or attribute descriptor the policy reads and writes. */
    interface Annotated {
        Object getTransient();
        String getPseudonym();
        InputFieldValue getInput();
        OutputFieldValue getOutput();
        boolean isUnique();
        boolean isName();
        void setEffectivePrivacy(EffectivePrivacy p);

        static Annotated of(Descriptor descriptor) {
            if (descriptor instanceof NameDescriptor) {
                NameDescriptor nd = (NameDescriptor) descriptor;
                return new Annotated() {
                    public Object getTransient() { return nd.getTransient(); }
                    public String getPseudonym() { return nd.getPseudonym(); }
                    public InputFieldValue getInput() { return nd.getInput(); }
                    public OutputFieldValue getOutput() { return nd.getOutput(); }
                    public boolean isUnique() { return nd.isUnique(); }
                    public boolean isName() { return true; }
                    public void setEffectivePrivacy(EffectivePrivacy p) { nd.setEffectivePrivacy(p); }
                };
            }
            AttributeDescriptor ad = ((AttributeDescriptorList) descriptor).getItems().get(0);
            return new Annotated() {
                public Object getTransient() { return ad.getTransient(); }
                public String getPseudonym() { return ad.getPseudonym(); }
                public InputFieldValue getInput() { return ad.getInput(); }
                public OutputFieldValue getOutput() { return ad.getOutput(); }
                public boolean isUnique() { return ad.isUnique(); }
                public boolean isName() { return false; }
                public void setEffectivePrivacy(EffectivePrivacy p) { ad.setEffectivePrivacy(p); }
            };
        }
    }
}
