package org.openprovenance.prov.template.compiler.past.annotations;

/**
 * Base class for Rust-specific annotations that control code generation.
 */
abstract public class RustAnnotation implements PastAnnotation {
    private final String name;

    public RustAnnotation(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLanguage() {
        return "Rust";
    }
}
