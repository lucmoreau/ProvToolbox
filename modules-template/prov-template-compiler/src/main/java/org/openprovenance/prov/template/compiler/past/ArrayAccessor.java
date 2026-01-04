package org.openprovenance.prov.template.compiler.past;

public class ArrayAccessor extends Expression {
    public final Expression arrayExpression;
    public final Expression indexExpression;

    public ArrayAccessor(Expression arrayExpression, Expression indexExpression) {
        this.arrayExpression = arrayExpression;
        this.indexExpression = indexExpression;
        this.expressionKind= Expression.ExpressionKind.ARRAY_ACCESSOR;
    }

    public static ArrayAccessor ARRAY_ACCESSOR(Expression arrayExpression, Expression indexExpression) {
        return new ArrayAccessor(arrayExpression, indexExpression);
    }
    @Override
    public String toString() {
        return "ArrayAccessor{" +
                "arrayExpression=" + arrayExpression +
                ", indexExpression=" + indexExpression +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }
}
