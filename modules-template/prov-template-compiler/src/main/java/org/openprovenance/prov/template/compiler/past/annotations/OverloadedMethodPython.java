package org.openprovenance.prov.template.compiler.past.annotations;

public class OverloadedMethodPython extends PythonAnnotation {

    public static final String NAME = "python:@OverloadedMethod";
    private final String altName;

    public OverloadedMethodPython(String altName) {
        super(NAME);
        this.altName = altName;
    }

    public String getAltName() {
        return altName;
    }
}
