package org.openprovenance.prov.template.compiler.past;


import java.util.Arrays;

public class Comment extends Statement {
    public String format="";
    public Object [] objects;

    public Comment(String format, Object... objects) {
        this.format = format;
        this.objects = objects;
        this.statementKind= StatementKind.COMMENT;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "format='" + format + '\'' +
                ", objects=" + Arrays.toString(objects) +
                ", statementKind=" + statementKind +
                '}';
    }

}
