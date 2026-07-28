package org.openprovenance.prov.template.compiler.past.annotations;

public class OverloadedMethodRust extends RustAnnotation {

    public static final String NAME = "rust:@OverloadedMethod";
    private final String altName;

    public OverloadedMethodRust(String altName) {
        super(NAME);
        this.altName = altName;
    }

    public String getAltName() {
        return altName;
    }
}
