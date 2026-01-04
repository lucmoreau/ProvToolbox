package org.openprovenance.prov.template.compiler.past;


public class Variable extends Expression {
    public final String name;
    public boolean field=false;

    public Variable(String name) {
        if (name==null) {
            throw new IllegalArgumentException("Variable name cannot be null");
        }
        this.name = name;
        this.expressionKind=ExpressionKind.VARIABLE;
    }

    public Variable(String name, boolean field) {
        if (name==null) {
            throw new IllegalArgumentException("Variable name cannot be null");
        }
        this.name = name;
        this.expressionKind=ExpressionKind.VARIABLE;
        this.field=field;
    }


    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", field=" + field +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }

    public static Variable VARIABLE(String name) {
        return new Variable(name);
    }
    public static Variable VARIABLE(String name, boolean field) {
        return new Variable(name, field);
    }

}
