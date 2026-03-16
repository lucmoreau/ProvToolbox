package org.openprovenance.prov.template.compiler.past.annotations;

abstract public class JavaAnnotation implements PastAnnotation {
    private final String name;

    public JavaAnnotation(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLanguage() {
        return "Java";
    }


}
