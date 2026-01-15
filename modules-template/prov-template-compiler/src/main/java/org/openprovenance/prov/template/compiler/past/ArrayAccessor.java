package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.annotations.PastAnnotation;

import java.util.List;

import static org.openprovenance.prov.template.compiler.past.annotations.AnnotationConverter.toAnnotation;

public class ArrayAccessor extends Expression {
    public final Expression arrayExpression;
    public final Expression indexExpression;
    public List<PastAnnotation> annotation = new java.util.ArrayList<>();

    public ArrayAccessor(Expression arrayExpression, Expression indexExpression) {
        this.arrayExpression = arrayExpression;
        this.indexExpression = indexExpression;
        this.expressionKind= Expression.ExpressionKind.ARRAY_ACCESSOR;
    }

    public static ArrayAccessor ARRAY_ACCESSOR(Expression arrayExpression, Expression indexExpression) {
        return new ArrayAccessor(arrayExpression, indexExpression);
    }

    public ArrayAccessor ANNOTATION(PastAnnotation... annot) {
        for (PastAnnotation a: annot) {
            if (a==null) {
                throw new IllegalArgumentException("Annotation cannot be null");
            }
            this.annotation.add(a);
        }
        return this;
    }

    public ArrayAccessor ANNOTATION(String... annot) {
        for (String a: annot) {
            if (a==null) {
                throw new IllegalArgumentException("Annotation cannot be null");
            }
            this.annotation.add(toAnnotation(a));
        }
        return this;
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
