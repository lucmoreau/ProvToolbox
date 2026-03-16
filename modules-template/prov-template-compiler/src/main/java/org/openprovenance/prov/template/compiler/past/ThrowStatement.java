package org.openprovenance.prov.template.compiler.past;

public class ThrowStatement extends Statement {
    public final Expression expression;

    public ThrowStatement(Expression expression) {
        if (expression == null) {
            throw new IllegalArgumentException("Expression to throw cannot be null");
        }
        this.expression = expression;
        this.statementKind = StatementKind.THROW;
    }

    public static ThrowStatement THROW(Expression expression) {
        return new ThrowStatement(expression);
    }

    @Override
    public String toString() {
        return "ThrowStatement{" +
                "expression=" + expression +
                ", statementKind=" + statementKind +
                '}';
    }
}
