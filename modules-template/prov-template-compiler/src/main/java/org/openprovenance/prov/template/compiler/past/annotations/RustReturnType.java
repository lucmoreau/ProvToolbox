package org.openprovenance.prov.template.compiler.past.annotations;

/**
 * The type a method returns in Rust when its PAST return type is not what Rust should return (see {@link RustFieldType}, the same for a field).
 * Used by T46 for a rule of a flow that returns the open keys of a carrier: a {@code Map<String,Object>} in Java, the runtime's {@code Object} in Rust.
 * <p><b>Experimental:</b> in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality: their declarations, the classes generated from them and this class may change without notice.
 */
public class RustReturnType extends RustAnnotation {

    public static final String NAME = "rust:@RustReturnType";

    public final String rustType;
    public final java.util.List<String> imports;

    public RustReturnType(String rustType, String... imports) {
        super(NAME);
        this.rustType = rustType;
        this.imports = java.util.List.of(imports);
    }
}
