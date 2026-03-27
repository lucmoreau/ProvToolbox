package org.openprovenance.prov.template.compiler.past.annotations;

public class OverloadedMethodJavascript extends RustAnnotation {

    public static final String NAME = "js:@OverloadedMethod";
    private final String altName;

    public OverloadedMethodJavascript(String altName) {
        super(NAME);
        this.altName = altName;
    }

    public String getAltName() {
        return altName;
    }
}
