package org.openprovenance.prov.template.compiler.past.annotations;

public class OverloadedMethod extends PythonAnnotation {

    public static final String NAME = "python:@OverloadedMethod";
    private final String altName;

    public OverloadedMethod(String altName) {
        super(NAME);
        this.altName = altName;
    }

    public String getAltName() {
        return altName;
    }
}
