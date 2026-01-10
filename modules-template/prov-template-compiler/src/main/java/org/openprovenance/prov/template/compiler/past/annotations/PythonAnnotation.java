package org.openprovenance.prov.template.compiler.past.annotations;

abstract public class PythonAnnotation implements PastAnnotation {
    private final String name;

    public PythonAnnotation(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLanguage() {
        return "Python";
    }


}
