package org.openprovenance.prov.template.compiler.past.annotations;

/**
 * Says what a field is in Rust when its PAST type is not what Rust should hold: the other languages use the field's type, Rust uses
 * {@link #rustType}. Used by T46 for the map of the other keys of a JSON text carrier, a {@code Map<String,Object>} in Java and
 * {@code Object} of the Rust runtime ({@code json_text.rs}) in Rust, which, unlike {@code HashMap<String, Box<dyn Any>>}, can be
 * cloned, compared and serialised. The struct keeps its derives, and the field gets {@code #[serde(with = serdeWith)]} when given.
 * <p><b>Experimental:</b> in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality: their declarations, the classes generated from them and this class may change without notice.
 */
public class RustFieldType extends RustAnnotation {

    public static final String NAME = "rust:@RustFieldType";

    /** The Rust type, as written in the struct. */
    public final String rustType;
    /** The path of the serde module to use for the field, or null. */
    public final String serdeWith;
    /** The paths to import (use declarations) for the type. */
    public final java.util.List<String> imports;

    public RustFieldType(String rustType, String serdeWith, String... imports) {
        super(NAME);
        this.rustType = rustType;
        this.serdeWith = serdeWith;
        this.imports = java.util.List.of(imports);
    }
}
