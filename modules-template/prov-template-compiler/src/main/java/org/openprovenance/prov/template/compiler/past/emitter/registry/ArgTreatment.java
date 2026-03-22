package org.openprovenance.prov.template.compiler.past.emitter.registry;

/**
 * Specifies how a particular argument position should be converted when emitting
 * a method call in Rust.
 *
 * <p>The treatment is applied on top of the basic {@code convert(arg)} result and
 * may depend on the argument's runtime structure — for example, whether it is a
 * local-variable field access whose Rust field type is {@code Option<T>}.</p>
 *
 * <p>The last entry in {@link RustMethodSpec#argTreatments} is repeated for any
 * argument positions beyond the list's length (varargs-style coverage).</p>
 */
public enum ArgTreatment {

    /** Emit the argument expression as-is. */
    PASS_BY_VALUE,

    /** Prepend {@code &} — pass a shared borrow rather than transferring ownership. */
    PASS_BY_REF,

    /**
     * HashMap key: prepend {@code &} and, when the argument is a local-variable field
     * access (emitted as {@code Option<T>}), also append {@code .unwrap()}.
     * Corresponds to the {@code convertHashMapKeyArg(arg)} helper in {@code Rust.java}.
     */
    KEY_BORROW,

    /**
     * HashMap value or non-key slot: when the argument is a local-variable field access
     * ({@code Option<T>}), append {@code .unwrap()} to extract the inner value.
     * Corresponds to the {@code convertOptionArg(arg)} helper in {@code Rust.java}.
     */
    VALUE_UNWRAP,

    /**
     * Convert a {@code &str} literal or borrow to an owned {@code String} via
     * {@code .to_string()}.  Used for HashMap string keys that require an owned key type.
     */
    OWNED_STRING,

    /**
     * Append {@code .clone()} — used when the method takes ownership (e.g. {@code push /
     * add}) but the caller must keep the value live after the call.
     */
    CLONE,

    /**
     * Wrap in {@code Box::new(arg.clone())} — used when pushing a struct value into a
     * {@code Vec<Box<dyn Any>>} self-field.
     */
    BOX_CLONE
}
