package org.openprovenance.prov.template.compiler.past;

import java.util.List;

public class Iterator extends Statement {
    public Parameter parameter;
    public Expression collection;
    public List<Statement> body=new java.util.ArrayList<>();

    public Iterator(Parameter parameter, Expression collection, List<Statement> body) {
        this.parameter = parameter;
        this.collection = collection;
        if (body!=null) this.body.addAll(body);
        this.statementKind= Statement.StatementKind.ITERATOR;
    }

    public Iterator(Parameter parameter, Expression collection) {
        this.parameter = parameter;
        this.collection = collection;
        this.statementKind= Statement.StatementKind.ITERATOR;
    }

    public static Iterator ITERATOR(Parameter parameter, Expression collection) {
        return new Iterator(parameter, collection);
    }

    public Iterator BODY(Statement... statements) {
        for (Statement statement: statements) {
            if (statement==null) {
                throw new IllegalArgumentException("Statement cannot be null");
            }
            this.body.add(statement);
        }
        return this;
    }

}
