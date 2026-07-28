package org.openprovenance.prov.template.compiler.past.emitter.registry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Immutable metadata describing how a specific Java method call should be emitted in Rust.
 *
 * <p>Instances are loaded from {@code rust-registry.json} and indexed in
 * {@link RustMethodRegistry} by {@code (RustTypeCategory, javaName)} for category-specific
 * methods, or by {@code javaName} alone for global methods.</p>
 *
 * <b>Arg treatment coverage</b>
 * <p>The {@link #argTreatments} list may be shorter than the actual argument count.
 * {@link #argTreatment(int)} repeats the last entry for any excess positions, giving
 * varargs-style coverage.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)   // allows JSON comments via "_comment" fields
public final class RustMethodSpec {

    /** Java method name as it appears in the PAST node (e.g. {@code "containsKey"}). */
    @JsonProperty("javaName")
    public final String javaName;

    /** Rust method name to emit (e.g. {@code "contains_key"}). */
    @JsonProperty("rustName")
    public final String rustName;

    /**
     * {@code true} when calling this method mutates the receiver.
     * Used by the emitter to decide whether the enclosing method needs {@code &mut self}.
     */
    @JsonProperty("mutatesReceiver")
    public final boolean mutatesReceiver;

    /** Transformation to apply to the receiver expression before the {@code .method(} fragment. */
    @JsonProperty("receiverTransform")
    public final ReceiverTransform receiverTransform;

    /**
     * Behaviour when the receiver is itself a chained {@code .get(…)} call on a HashMap.
     * {@link ChainedGetBehavior#NONE} for methods that never appear in this pattern.
     */
    @JsonProperty("chainedGetBehavior")
    public final ChainedGetBehavior chainedGetBehavior;

    /**
     * Per-argument-position treatment.  The last entry is repeated for any excess
     * positions.  Empty list means every argument receives {@link ArgTreatment#PASS_BY_VALUE}.
     */
    @JsonProperty("argTreatments")
    public final List<ArgTreatment> argTreatments;

    /** Suffix appended after the closing {@code )} of the emitted call. */
    @JsonProperty("resultTransform")
    public final ResultTransform resultTransform;

    /** Jackson / programmatic constructor. */
    @JsonCreator
    public RustMethodSpec(
            @JsonProperty("javaName")           String javaName,
            @JsonProperty("rustName")           String rustName,
            @JsonProperty("mutatesReceiver")    boolean mutatesReceiver,
            @JsonProperty("receiverTransform")  ReceiverTransform receiverTransform,
            @JsonProperty("chainedGetBehavior") ChainedGetBehavior chainedGetBehavior,
            @JsonProperty("argTreatments")      List<ArgTreatment> argTreatments,
            @JsonProperty("resultTransform")    ResultTransform resultTransform) {
        this.javaName           = javaName;
        this.rustName           = rustName;
        this.mutatesReceiver    = mutatesReceiver;
        this.receiverTransform  = receiverTransform  != null ? receiverTransform  : ReceiverTransform.NONE;
        this.chainedGetBehavior = chainedGetBehavior != null ? chainedGetBehavior : ChainedGetBehavior.NONE;
        this.argTreatments      = argTreatments      != null ? List.copyOf(argTreatments) : List.of();
        this.resultTransform    = resultTransform    != null ? resultTransform    : ResultTransform.NONE;
    }

    /**
     * Return the treatment for the argument at position {@code index}.
     * Repeats the last entry for positions beyond the list size (varargs semantics).
     * Returns {@link ArgTreatment#PASS_BY_VALUE} when the list is empty.
     */
    public ArgTreatment argTreatment(int index) {
        if (argTreatments.isEmpty()) return ArgTreatment.PASS_BY_VALUE;
        return argTreatments.get(Math.min(index, argTreatments.size() - 1));
    }

    @Override
    public String toString() {
        return "RustMethodSpec{javaName='" + javaName + "', rustName='" + rustName +
               "', mutates=" + mutatesReceiver + ", argTreatments=" + argTreatments + "}";
    }
}
