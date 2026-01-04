package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.type.TypeName;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LambdaExpression extends Expression {
    public final List<Parameter> parameters=new LinkedList<>();
    public List<Statement> body=new java.util.ArrayList<>();
    private TypeName returnType;

    public LambdaExpression(List<Parameter> parameters) {
        this.parameters.addAll(parameters);
        this.expressionKind= Expression.ExpressionKind.LAMBDA_EXPRESSION;
    }

    public LambdaExpression(Parameter... parameters) {
        this.parameters.addAll(Arrays.asList(parameters));
        this.expressionKind= Expression.ExpressionKind.LAMBDA_EXPRESSION;
    }

    public LambdaExpression returns(TypeName typeT) {
        this.returnType=typeT;
        return this;
    }


    public LambdaExpression addStatement(Statement statement) {
        this.body.add(statement);
        return this;
    }

    public LambdaExpression BODY(Statement... statements) {
        for (Statement statement: statements) {
            if (statement==null) {
                throw new IllegalArgumentException("Statement cannot be null");
            }
            this.body.add(statement);
        }
        return this;
    }

    public static LambdaExpression LAMBDA(Parameter... parameters) {
        return new LambdaExpression(parameters);
    }
    public static LambdaExpression LAMBDA(List<Parameter> parameters) {
        return new LambdaExpression(parameters);
    }

    @Override
    public String toString() {
        return "LambdaExpression{" +
                "parameters=" + parameters +
                ", body=" + body +
                ", returnType=" + returnType +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }
}
