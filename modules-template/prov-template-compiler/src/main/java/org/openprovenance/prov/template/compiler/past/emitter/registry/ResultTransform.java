package org.openprovenance.prov.template.compiler.past.emitter.registry;

/**
 * Post-processing suffix appended after the closing {@code )} of an emitted method call.
 */
public enum ResultTransform {

    /** No post-processing — emit the call result as-is. */
    NONE,

    /**
     * Append {@code .copied()} after the call.
     *
     * <p>Used after {@code HashMap::get()}, which returns {@code Option<&V>} for
     * {@code Copy} value types.  {@code .copied()} converts this to {@code Option<V>},
     * matching the {@code Option<T>} struct fields directly and avoiding double-wrapping
     * with {@code Some(…)} at the assignment site.</p>
     */
    COPIED
}
