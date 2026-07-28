package org.openprovenance.prov.template.compiler.past.emitter.registry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/**
 * Immutable metadata describing how a PAST/Java type maps to Rust.
 *
 * <p>Instances are loaded from {@code rust-registry.json} via Jackson (see
 * {@link RustMethodRegistry#loadFromClasspath()}) and indexed by every Java name in
 * {@link #javaNames}.  They can also be constructed programmatically via the
 * public all-args constructor — useful in unit tests.</p>
 *
 * <b>Relationship to old Rust.java helpers</b>
 * <ul>
 *   <li>{@code isMap(tn)}          → {@code registry.isCategory(tn, MAP)}</li>
 *   <li>{@code isList(tn)}         → {@code registry.isCategory(tn, LIST)}</li>
 *   <li>{@code isStringType(tn)}   → {@code registry.isCategory(tn, STRING)}</li>
 *   <li>{@code isPrimitiveType(tn)}→ {@code registry.isCategory(tn, PRIMITIVE)}</li>
 *   <li>{@code isFunctionType(tn)} → {@code registry.isCategory(tn, FUNCTION)}</li>
 *   <li>{@code isClassType(tn)}    → {@code registry.isCategory(tn, CLASS_REF)}</li>
 *   <li>{@code convertCommonType(s)}→ {@code registry.rustName(s)}</li>
 * </ul>
 */
public final class RustTypeSpec {

    /**
     * All Java names (simple and fully-qualified) that map to this spec.
     * Example: {@code {"past.util.Map", "past.util.HashMap", "HashMap"}}.
     */
    @JsonProperty("javaNames")
    public final Set<String> javaNames;

    /** The broad emitter category for this type. */
    @JsonProperty("category")
    public final RustTypeCategory category;

    /**
     * The base Rust type name to emit for this type when used directly
     * (e.g. {@code "i32"}, {@code "String"}, {@code "HashMap"}, {@code "()"}).
     * For parameterized types (Map, Vec) this is only the raw name; the emitter
     * constructs the full generic form from the PAST type arguments.
     */
    @JsonProperty("rustName")
    public final String rustName;

    /**
     * {@code true} when the Rust type implements {@code Copy} — i.e. values are
     * bitwise-copyable and do not require {@code .clone()} when ownership is transferred.
     * All primitive numeric and boolean types are Copy; structs, Strings, and Vecs are not.
     */
    @JsonProperty("copy")
    public final boolean copy;

    /** Jackson / programmatic constructor. */
    @JsonCreator
    public RustTypeSpec(
            @JsonProperty("javaNames") Set<String> javaNames,
            @JsonProperty("category")  RustTypeCategory category,
            @JsonProperty("rustName")  String rustName,
            @JsonProperty("copy")      boolean copy) {
        this.javaNames = javaNames;
        this.category  = category;
        this.rustName  = rustName;
        this.copy      = copy;
    }

    @Override
    public String toString() {
        return "RustTypeSpec{category=" + category + ", rustName='" + rustName + "', javaNames=" + javaNames + "}";
    }
}
