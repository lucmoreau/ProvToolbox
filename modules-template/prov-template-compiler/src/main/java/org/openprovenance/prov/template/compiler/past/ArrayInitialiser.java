package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.type.TypeName;

import java.util.Arrays;
import java.util.List;

public class ArrayInitialiser extends Expression {


    public final TypeName elementType;
    public final List<Expression> values=new java.util.LinkedList<>();

    public ArrayInitialiser(TypeName elementType, List<Expression> expressions) {
        this.elementType = elementType;
        this.values.addAll(expressions);
        this.expressionKind= Expression.ExpressionKind.ARRAY_INITIALISER;
    }
    public ArrayInitialiser(TypeName elementType, Expression... expressions) {
        this.elementType = elementType;
        this.values.addAll(Arrays.asList(expressions));
        this.expressionKind= Expression.ExpressionKind.ARRAY_INITIALISER;
    }

    public static ArrayInitialiser ARRAY_INITIALISER(TypeName elementType, Expression... expressions) {
        return new ArrayInitialiser(elementType, expressions);
    }
    public static ArrayInitialiser ARRAY_INITIALISER(TypeName elementType, List<Expression> expressions) {
        return new ArrayInitialiser(elementType, expressions);
    }

    @Override
    public String toString() {
        return "ArrayInitialiser{" +
                "elementType=" + elementType +
                ", values=" + values +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }
}
