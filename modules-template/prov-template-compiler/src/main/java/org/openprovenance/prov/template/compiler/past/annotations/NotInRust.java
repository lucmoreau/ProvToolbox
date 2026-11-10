package org.openprovenance.prov.template.compiler.past.annotations;

/**
 * Marks a statement the Rust emitter leaves out, writing a comment with the reason in its place. Used by T31 for a
 * client check whose call the Rust registry doesn't map: the other languages keep the statement.
 */
public class NotInRust extends RustAnnotation {

    public static final String NAME = "rust:@NotInRust";

    public final String reason;

    public NotInRust(String reason) {
        super(NAME);
        this.reason = reason;
    }
}
