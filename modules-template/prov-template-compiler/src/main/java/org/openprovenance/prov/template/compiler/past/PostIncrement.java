package org.openprovenance.prov.template.compiler.past;

public class PostIncrement extends Expression {
    public final Expression expression;
    public final int increment;

    public PostIncrement(Expression expression, int increment) {
        this.expression = expression;
        this.increment=increment;
        this.expressionKind= Expression.ExpressionKind.POST_INCREMENT;
    }

    public static PostIncrement POST_INCREMENT(Expression expression, int increment) {
        return new PostIncrement(expression, increment);
    }

    @Override
    public String toString() {
        return "PostIncrement{" +
                "expression=" + expression +
                ", increment=" + increment +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }
}
