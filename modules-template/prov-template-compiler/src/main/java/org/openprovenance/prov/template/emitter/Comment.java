package org.openprovenance.prov.template.emitter;

public class Comment implements Element {
    private final String str;

    public Comment(String str) {
        this.str=str;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "str='" + str.substring(0,Math.min(5,str.length())) + '\'' +
                '}';
    }
}
