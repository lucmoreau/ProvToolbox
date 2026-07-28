package org.openprovenance.prov.template.compiler.past.emitter.registry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central registry mapping PAST/Java type names and method names to their Rust
 * emission metadata.
 *
 * <b>Loading</b>
 * <ul>
 *   <li>{@link #loadFromClasspath()} — loads the bundled {@code rust-registry.json}
 *       resource from the same package directory.  Preferred for production use.</li>
 *   <li>{@link #builder()} — constructs a registry programmatically.  Useful for
 *       unit tests that need a minimal or custom registry without file I/O.</li>
 * </ul>
 *
 * <b>Primary query methods</b>
 * <ul>
 *   <li>{@link #isCategory(TypeName, RustTypeCategory)} — replaces the scattered
 *       {@code isMap()}, {@code isList()}, {@code isPrimitiveType()} … helpers.</li>
 *   <li>{@link #rustName(TypeName)} / {@link #rustName(String)} — replaces
 *       {@code convertCommonType(String)}.</li>
 *   <li>{@link #resolveMethod(RustTypeCategory, String)} — look up method emission
 *       metadata by (category, javaName).</li>
 *   <li>{@link #resolveMethodGlobal(String)} — for mutation detection and simple
 *       name translation when the receiver type is unknown.</li>
 * </ul>
 *
 * <b>Thread safety</b>
 * <p>Instances are immutable after construction — safe to share as a static field.</p>
 */
public class RustMethodRegistry {

    // ---- Jackson-populated raw fields -------------------------------------------------------

    @JsonProperty("types")
    private List<RustTypeSpec> types = new ArrayList<>();

    /**
     * Key: {@link RustTypeCategory#name()} string → list of method specs for that category.
     * Jackson uses String keys in JSON; we convert to enum keys in {@link #buildIndexes()}.
     */
    @JsonProperty("methodsByCategory")
    private Map<String, List<RustMethodSpec>> methodsByCategory = new HashMap<>();

    /** Methods that apply regardless of receiver type (e.g. {@code toString → to_string}). */
    @JsonProperty("globalMethods")
    private List<RustMethodSpec> globalMethods = new ArrayList<>();

    // ---- Computed indexes (not serialised) --------------------------------------------------

    @JsonIgnore private Map<String, RustTypeSpec>                         typesByJavaName;
    @JsonIgnore private Map<RustTypeCategory, Map<String, RustMethodSpec>> methodIndex;
    @JsonIgnore private Map<String, RustMethodSpec>                        globalMethodIndex;

    // ---- Constructors -----------------------------------------------------------------------

    /** No-arg constructor required by Jackson. */
    public RustMethodRegistry() {}

    // ---- Factory methods --------------------------------------------------------------------

    /**
     * Load the registry from the bundled {@code rust-registry.json} classpath resource
     * located in the same package directory as this class.
     *
     * @throws IllegalStateException if the resource is missing or cannot be parsed
     */
    public static RustMethodRegistry loadFromClasspath() {
        try (InputStream is = RustMethodRegistry.class.getResourceAsStream("rust-registry.json")) {
            if (is == null) {
                throw new IllegalStateException(
                        "rust-registry.json not found on classpath next to RustMethodRegistry. " +
                        "Expected at: org/openprovenance/prov/template/compiler/past/emitter/registry/rust-registry.json");
            }
            ObjectMapper mapper = new ObjectMapper();
            RustMethodRegistry registry = mapper.readValue(is, RustMethodRegistry.class);
            registry.buildIndexes();
            return registry;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load or parse rust-registry.json", e);
        }
    }

    // ---- Programmatic builder (for tests) ---------------------------------------------------

    /** @return a new {@link Builder} for constructing a registry without JSON I/O. */
    public static Builder builder() { return new Builder(); }

    /** Fluent builder for constructing {@link RustMethodRegistry} instances programmatically. */
    public static final class Builder {

        private final List<RustTypeSpec>                         types             = new ArrayList<>();
        private final Map<String, List<RustMethodSpec>>          methodsByCategory = new HashMap<>();
        private final List<RustMethodSpec>                       globalMethods     = new ArrayList<>();

        /** Register a type spec. */
        public Builder type(RustTypeSpec spec) {
            types.add(spec);
            return this;
        }

        /** Register a method spec for a specific receiver category. */
        public Builder method(RustTypeCategory category, RustMethodSpec spec) {
            methodsByCategory.computeIfAbsent(category.name(), k -> new ArrayList<>()).add(spec);
            return this;
        }

        /** Register a method spec that applies regardless of receiver type. */
        public Builder globalMethod(RustMethodSpec spec) {
            globalMethods.add(spec);
            return this;
        }

        /** Build and index the registry. */
        public RustMethodRegistry build() {
            RustMethodRegistry r = new RustMethodRegistry();
            r.types             = new ArrayList<>(types);
            r.methodsByCategory = new HashMap<>(methodsByCategory);
            r.globalMethods     = new ArrayList<>(globalMethods);
            r.buildIndexes();
            return r;
        }
    }

    // ---- Index construction -----------------------------------------------------------------

    /**
     * Build look-up indexes from the raw lists populated by Jackson or the Builder.
     * Must be called once before any query method is used.
     */
    private void buildIndexes() {
        // Type index: every javaName in the spec maps to the spec
        typesByJavaName = new HashMap<>();
        for (RustTypeSpec spec : types) {
            for (String name : spec.javaNames) {
                typesByJavaName.put(name, spec);
            }
        }

        // Method index by category
        methodIndex = new HashMap<>();
        for (Map.Entry<String, List<RustMethodSpec>> entry : methodsByCategory.entrySet()) {
            RustTypeCategory cat;
            try {
                cat = RustTypeCategory.valueOf(entry.getKey());
            } catch (IllegalArgumentException e) {
                // Unknown category string in JSON — skip silently
                continue;
            }
            Map<String, RustMethodSpec> byJavaName = new HashMap<>();
            for (RustMethodSpec spec : entry.getValue()) {
                byJavaName.put(spec.javaName, spec);
            }
            methodIndex.put(cat, byJavaName);
        }

        // Global method index
        globalMethodIndex = new HashMap<>();
        for (RustMethodSpec spec : globalMethods) {
            globalMethodIndex.put(spec.javaName, spec);
        }
    }

    // ---- Query API --------------------------------------------------------------------------

    /**
     * Resolve the {@link RustTypeSpec} for a given PAST {@link TypeName}.
     *
     * <p>Handles both raw {@code CLASS} TypeNames (looked up by fully-qualified name first,
     * then simple name) and {@code PARAMETERIZED} types by delegating to their raw type.</p>
     *
     * @param tn the PAST TypeName to resolve (may be {@code null})
     * @return the matching spec, or {@code null} if not registered
     */
    public RustTypeSpec resolveType(TypeName tn) {
        if (tn == null) return null;
        switch (tn.typeKind) {
            case CLASS: {
                ClassName cn = (ClassName) tn;
                String fullName = (cn.packge != null && !cn.packge.isEmpty())
                        ? cn.packge + "." + cn.simpleName
                        : cn.simpleName;
                RustTypeSpec spec = typesByJavaName.get(fullName);
                if (spec == null) spec = typesByJavaName.get(cn.simpleName); // simple-name fallback
                return spec;
            }
            case PARAMETERIZED:
                return resolveType(((ParameterizedType) tn).rawType);
            default:
                return null;
        }
    }

    /**
     * Test whether a PAST TypeName belongs to the given {@link RustTypeCategory}.
     *
     * <p>This is the primary replacement for the hardcoded boolean helpers in
     * {@code Rust.java}:</p>
     * <ul>
     *   <li>{@code isMap(tn)}          → {@code isCategory(tn, MAP)}</li>
     *   <li>{@code isList(tn)}         → {@code isCategory(tn, LIST)}</li>
     *   <li>{@code isStringType(tn)}   → {@code isCategory(tn, STRING)}</li>
     *   <li>{@code isPrimitiveType(tn)}→ {@code isCategory(tn, PRIMITIVE)}</li>
     *   <li>{@code isFunctionType(tn)} → {@code isCategory(tn, FUNCTION)}</li>
     *   <li>{@code isClassType(tn)}    → {@code isCategory(tn, CLASS_REF)}</li>
     * </ul>
     *
     * @param tn  TypeName to test (may be {@code null} — returns {@code false})
     * @param cat desired category
     */
    public boolean isCategory(TypeName tn, RustTypeCategory cat) {
        RustTypeSpec spec = resolveType(tn);
        return spec != null && spec.category == cat;
    }

    /**
     * Return the Rust base type name for a given PAST TypeName.
     * Replaces the {@code convertCommonType(String)} switch in {@code Rust.java}.
     *
     * @param tn TypeName to look up (may be {@code null})
     * @return Rust type name string (e.g. {@code "i32"}, {@code "String"}),
     *         or {@code null} if not registered
     */
    public String rustName(TypeName tn) {
        RustTypeSpec spec = resolveType(tn);
        return spec != null ? spec.rustName : null;
    }

    /**
     * Return the Rust base type name for a plain Java simple-name string.
     * Convenience overload for call sites that already hold a {@code String} rather than a
     * {@link TypeName} (e.g. inside {@code convertCommonType}).
     *
     * @param javaSimpleName simple Java type name (e.g. {@code "int"}, {@code "String"})
     * @return Rust type name, or {@code null} if not registered
     */
    public String rustName(String javaSimpleName) {
        RustTypeSpec spec = typesByJavaName.get(javaSimpleName);
        return spec != null ? spec.rustName : null;
    }

    /**
     * Whether this type has Rust's {@code Copy} semantics (no {@code .clone()} needed).
     *
     * @param tn TypeName to test (may be {@code null} — returns {@code false})
     */
    public boolean isCopy(TypeName tn) {
        RustTypeSpec spec = resolveType(tn);
        return spec != null && spec.copy;
    }

    /**
     * Resolve the emission spec for a method given its receiver category and Java name.
     *
     * @param category       the resolved category of the receiver type
     * @param javaMethodName the Java method name from the PAST node (e.g. {@code "containsKey"})
     * @return the spec, or {@code null} when the combination is not registered
     *         (caller should fall back to generic emit logic)
     */
    public RustMethodSpec resolveMethod(RustTypeCategory category, String javaMethodName) {
        if (category == null) return null;
        Map<String, RustMethodSpec> byName = methodIndex.get(category);
        return byName != null ? byName.get(javaMethodName) : null;
    }

    /**
     * Convenience overload: resolve method spec given a receiver TypeName.
     *
     * @param receiverType   PAST TypeName of the receiver (may be {@code null})
     * @param javaMethodName Java method name from the PAST node
     * @return the spec, or {@code null} when unknown
     */
    public RustMethodSpec resolveMethod(TypeName receiverType, String javaMethodName) {
        RustTypeSpec typeSpec = resolveType(receiverType);
        return typeSpec != null ? resolveMethod(typeSpec.category, javaMethodName) : null;
    }

    /**
     * Look up a method by Java name regardless of receiver type.
     *
     * <p>Used for mutation detection and simple name-to-Rust-name translation when the
     * receiver type is not available at the call site.  Checks global methods first, then
     * searches all category-specific entries.</p>
     *
     * @param javaMethodName Java method name (e.g. {@code "add"}, {@code "forEach"})
     * @return the spec, or {@code null} if not found in any category
     */
    public RustMethodSpec resolveMethodGlobal(String javaMethodName) {
        // Global methods take precedence (they are registered explicitly as global)
        RustMethodSpec spec = globalMethodIndex.get(javaMethodName);
        if (spec != null) return spec;
        // Fall through to category-specific entries
        for (Map<String, RustMethodSpec> byName : methodIndex.values()) {
            spec = byName.get(javaMethodName);
            if (spec != null) return spec;
        }
        return null;
    }

    // ---- Accessors for testing / inspection -------------------------------------------------

    /** @return unmodifiable view of the raw type specs as loaded from JSON. */
    public List<RustTypeSpec> getTypes() {
        return Collections.unmodifiableList(types);
    }

    /** @return unmodifiable view of the global method specs. */
    public List<RustMethodSpec> getGlobalMethods() {
        return Collections.unmodifiableList(globalMethods);
    }

    /** @return unmodifiable view of the raw category → methods map (String keys, as in JSON). */
    public Map<String, List<RustMethodSpec>> getMethodsByCategory() {
        return Collections.unmodifiableMap(methodsByCategory);
    }
}
