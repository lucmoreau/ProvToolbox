package org.openprovenance.prov.template.compiler.past.annotations;

public class NoSerialization extends RustAnnotation {

    public static final String NAME = "rust:@noserialize";

    public NoSerialization() {
        super(NAME);
    }
}
