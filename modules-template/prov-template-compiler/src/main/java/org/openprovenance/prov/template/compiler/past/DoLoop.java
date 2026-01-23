package org.openprovenance.prov.template.compiler.past;

import java.util.List;

public class DoLoop extends Statement {
    public List<Statement> body=new java.util.ArrayList<>();
    public Expression condition;

    public DoLoop(List<Statement> body,  Expression condition ) {
        this.condition = condition;
        if (body!=null) this.body.addAll(body);
        this.statementKind= StatementKind.DO_LOOP;
    }

    public DoLoop() {
        this.statementKind= StatementKind.DO_LOOP;
    }

    @Override
    public String toString() {
        return "DoLoop{" +
                "body=" + body +
                ", condition=" + condition +
                ", statementKind=" + statementKind +
                '}';
    }

    public static DoLoop DO() {
        return new DoLoop();
    }

    public DoLoop BODY(Statement... statements) {
        for (Statement statement: statements) {
            if (statement==null) {
                throw new IllegalArgumentException("Statement cannot be null");
            }
            this.body.add(statement);
        }
        return this;
    }
    public DoLoop WHILE(Expression condition) {
        this.condition=condition;
        return this;
    }

}
