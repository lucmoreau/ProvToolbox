package org.openprovenance.prov.template.compiler.past;

public class IfExpression extends Expression {
    public Expression condition;
    public Expression thenExpression;
    public Expression elseExpression;

    public IfExpression(Expression condition, Expression thenExpression, Expression elseExpression) {
        this.condition = condition;
        this.thenExpression=thenExpression;
        this.elseExpression=elseExpression;
        this.statementKind=StatementKind.EXPRESSION_STATEMENT;
        this.expressionKind= ExpressionKind.IF_EXPRESSION;
    }

    public IfExpression(Expression condition) {
        this.condition = condition;
        this.statementKind=StatementKind.IF_STATEMENT;
        this.expressionKind= ExpressionKind.IF_EXPRESSION;
    }

    static public IfExpression IFEXPRESSION(Expression condition, Expression  thenExpression, Expression elseExpression) {
        return new IfExpression(condition, thenExpression, elseExpression);
    }

    static public IfExpression IF_(Expression condition) {
        return new IfExpression(condition);
    }

    public IfExpression THEN(Expression expression) {
        if (expression==null) {
            throw new NullPointerException("Null statement in thenBlock");
        }
        thenExpression=expression;
        return this;
    }

    public IfExpression ELSE(Expression expression) {
        if (expression==null) {
            throw new NullPointerException("Null statement in elseBlock");
        }
        this.elseExpression=expression;
        return this;
    }

    @Override
    public String toString() {
        return "IfExpression{" +
                "condition=" + condition +
                ", thenExpression=" + thenExpression +
                ", elseExpression=" + elseExpression +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }
}
