package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

public class InstanceOf extends Expression {
    public final Expression expression;
    public final ClassName type;

    public InstanceOf(Expression expression, ClassName type) {
        this.expression = expression;
        if (type==null) {
            throw new NullPointerException("Null type in instanceof expression");
        }
        if (expression==null) {
            throw new NullPointerException("Null expression in instanceof expression");
        }
        this.type = type;
        this.expressionKind=ExpressionKind.INSTANCEOF;
    }

    @Override
    public String toString() {
        return "InstanceOf{" +
                "expression=" + expression +
                ", type='" + type + '\'' +
                ", expressionKind=" + expressionKind +
                ", inferredType=" + inferredType +
                ", statementKind=" + statementKind +
                '}';
    }

    public static InstanceOf INSTANCE_OF(Expression expression, ClassName type) {
        return new InstanceOf(expression, type);
    }
}
