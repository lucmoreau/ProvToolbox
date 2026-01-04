package org.openprovenance.prov.template.compiler.past;

import java.util.List;

public class IfStatement extends Statement {
    public Expression condition;
    public List<Statement> thenBlock=new java.util.LinkedList<>();
    public List<Statement>  elseBlock=new java.util.LinkedList<>();

    public IfStatement(Expression condition, List<Statement>  thenBlock, List<Statement>  elseBlock) {
        this.condition = condition;
        this.thenBlock.addAll(thenBlock);
        this.elseBlock.addAll(elseBlock);
        this.statementKind=StatementKind.IF_STATEMENT;
    }

    public IfStatement(Expression condition) {
        this.condition = condition;
        this.statementKind=StatementKind.IF_STATEMENT;
    }

    static public IfStatement IF(Expression condition, List<Statement>  thenBlock, List<Statement>  elseBlock) {
        return new IfStatement(condition, thenBlock, elseBlock);
    }

    static public IfStatement IF(Expression condition) {
        return new IfStatement(condition);
    }

    public IfStatement THEN(Statement ...  statements) {
        for (Statement s: statements) {;
            if (s==null) {
                throw new NullPointerException("Null statement in thenBlock");
            }
            thenBlock.add(s);
        }
        return this;
    }

    public IfStatement ELSE(Statement ...  statements) {
        for (Statement s: statements) {;
            if (s==null) {
                throw new NullPointerException("Null statement in elseBlock");
            }
            this.elseBlock.add(s);
        }
        return this;
    }

    @Override
    public String toString() {
        return "IfStatement{" +
                "condition=" + condition +
                ", thenBlock=" + thenBlock +
                ", elseBlock=" + elseBlock +
                ", statementKind=" + statementKind +
                '}';
    }
}
