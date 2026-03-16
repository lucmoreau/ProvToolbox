package org.openprovenance.prov.template.compiler.past.annotations;

public class MethodRename extends PythonAnnotation {

    public static final String NAME = "python:@methodrename";

    String newName;

    public MethodRename(String newName) {
        super(NAME);
        this.newName = newName;
    }
}
