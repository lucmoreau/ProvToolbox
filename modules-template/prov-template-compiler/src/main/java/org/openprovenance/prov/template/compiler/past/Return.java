package org.openprovenance.prov.template.compiler.past;

public class Return extends Statement {
    public Expression expression;

    public Return(Expression expression) {
        this.expression = expression;
        this.statementKind=StatementKind.RETURN;
    }

    static public Return RETURN(Expression expression) {
        if (expression==null) {
            throw new IllegalArgumentException("Return expression cannot be null");
        }
        return new Return(expression);
    }

    @Override
    public String toString() {
        return "Return{" +
                "expression=" + expression +
                ", statementKind=" + statementKind +
                '}';
    }
}
