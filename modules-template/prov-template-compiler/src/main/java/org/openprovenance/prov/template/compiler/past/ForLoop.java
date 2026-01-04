package org.openprovenance.prov.template.compiler.past;

import java.util.List;

public class ForLoop extends Statement {
    public Assignment initialization;
    public Expression condition;
    public Assignment update;
    public List<Statement> body=new java.util.ArrayList<>();

    public ForLoop(Assignment initialization, Expression condition, Assignment update, List<Statement> body) {
        this.initialization = initialization;
        this.condition = condition;
        this.update = update;
        if (body!=null) this.body.addAll(body);
        this.statementKind= Statement.StatementKind.FOR_LOOP;
    }

    @Override
    public String toString() {
        return "ForLoop{" +
                "initialization=" + initialization +
                ", condition=" + condition +
                ", update=" + update +
                ", body=" + body +
                ", statementKind=" + statementKind +
                '}';
    }

    public static ForLoop FOR(Assignment initialization, Expression condition, Assignment update) {
        return new ForLoop(initialization, condition, update, null);
    }

    public ForLoop BODY(Statement... statements) {
        for (Statement statement: statements) {
            if (statement==null) {
                throw new IllegalArgumentException("Statement cannot be null");
            }
            this.body.add(statement);
        }
        return this;
    }

}
