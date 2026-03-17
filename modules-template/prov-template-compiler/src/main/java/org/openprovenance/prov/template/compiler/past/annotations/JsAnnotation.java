package org.openprovenance.prov.template.compiler.past.annotations;

abstract public class JsAnnotation implements PastAnnotation {
    private final String name;

    public JsAnnotation(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLanguage() {
        return "JS";
    }


}
