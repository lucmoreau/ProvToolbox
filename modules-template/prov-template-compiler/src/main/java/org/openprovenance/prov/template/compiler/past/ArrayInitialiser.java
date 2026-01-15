package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.annotations.PastAnnotation;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

import java.util.Arrays;
import java.util.List;

import static org.openprovenance.prov.template.compiler.past.annotations.AnnotationConverter.toAnnotation;

public class ArrayInitialiser extends Expression {


    public final TypeName elementType;
    public final List<Expression> values=new java.util.LinkedList<>();
    public List<PastAnnotation> annotation=new java.util.ArrayList<>();

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

    public ArrayInitialiser ANNOTATION(PastAnnotation... annot) {
        for (PastAnnotation a: annot) {
            if (a==null) {
                throw new IllegalArgumentException("Annotation cannot be null");
            }
            this.annotation.add(a);
        }
        return this;
    }

    public ArrayInitialiser ANNOTATION(String... annot) {
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
        return "ArrayInitialiser{" +
                "elementType=" + elementType +
                ", values=" + values +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }
}
