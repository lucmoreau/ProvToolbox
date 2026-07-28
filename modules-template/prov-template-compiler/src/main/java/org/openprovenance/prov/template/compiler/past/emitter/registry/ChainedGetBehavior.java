package org.openprovenance.prov.template.compiler.past.emitter.registry;

/**
 * Describes what the emitter must do when this method's receiver is itself a
 * {@code .get(…)} call on an outer HashMap (the "chained get" pattern).
 *
 * <b>Pattern</b>
 * <pre>{@code
 * // PAST:  map.get(outerKey).insert(innerKey, value)
 * // Rust:  map.get_mut("outer").unwrap().insert(inner, value)
 * }</pre>
 * <p>Here the outer method is {@code insert} and its receiver is
 * {@code map.get(outerKey)}, which returns {@code Option<&mut HashMap<K,V>>}.
 * The emitter must both switch {@code .get(} → {@code .get_mut(} (for mutable access)
 * and unwrap the resulting Option.</p>
 */
public enum ChainedGetBehavior {

    /** This method never appears as the outer call in a chained get — no special handling. */
    NONE,

    /**
     * Mutable operation (e.g. {@code insert}): replace the last {@code .get(} occurrence
     * in the receiver string with {@code .get_mut(} so the inner HashMap is obtained as a
     * mutable reference, then append {@code .unwrap()} to unwrap the Option.
     */
    SWITCH_TO_GET_MUT,

    /**
     * Read-only operation (e.g. {@code get}, {@code contains_key}): just append
     * {@code .unwrap()} to the receiver to unwrap the {@code Option} returned by the inner
     * {@code .get()} call.  No mutability switch is needed.
     */
    UNWRAP_AND_CHAIN
}
