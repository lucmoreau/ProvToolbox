package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.type.TypeName;

public class CastExpression extends Expression {
    public final TypeName targetType;
    public final Expression expression;

    public CastExpression(TypeName targetType, Expression expression) {
        if (targetType==null) {
            throw new IllegalArgumentException("Target type cannot be null");
        }
        if (expression==null) {
            throw new IllegalArgumentException("Expression to cast cannot be null");
        }
        this.targetType = targetType;
        this.expression = expression;
        this.expressionKind=ExpressionKind.CAST;
    }

    public static CastExpression CAST(TypeName targetType, Expression expression) {
        return new CastExpression(targetType, expression);
    }

    @Override
    public String toString() {
        return "CastExpression{" +
                "targetType=" + targetType +
                ", expression=" + expression +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }
}
