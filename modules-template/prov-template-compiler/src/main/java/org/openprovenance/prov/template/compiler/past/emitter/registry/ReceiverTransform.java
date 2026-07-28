package org.openprovenance.prov.template.compiler.past.emitter.registry;

/**
 * Specifies how to transform the receiver expression before emitting the
 * {@code receiver.methodName(…)} fragment of a method call.
 */
public enum ReceiverTransform {

    /** No transformation — use the receiver expression exactly as produced by {@code convert()}. */
    NONE,

    /**
     * The receiver is an {@code Option<Vec<…>>} self-field (e.g. {@code self.inputs}).
     * Append {@code .as_mut().unwrap()} to unwrap the Option and obtain a mutable
     * reference to the underlying Vec before calling {@code push}.
     */
    AS_MUT_UNWRAP
}
