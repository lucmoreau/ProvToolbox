package org.openprovenance.prov.template.compiler.past;


import org.openprovenance.prov.template.compiler.past.annotations.PastAnnotation;

import java.util.List;

import static org.openprovenance.prov.template.compiler.past.annotations.AnnotationConverter.toAnnotation;

public class Variable extends Expression {
    public final String name;
    public VariableKind field= VariableKind.LOCAL_VARIABLE;
    public List<PastAnnotation> annotation=new java.util.ArrayList<>();

    public enum VariableKind { LOCAL_VARIABLE, FIELD_VARIABLE, STATIC_FIELD_VARIABLE }

    public Variable(String name) {
        if (name==null) {
            throw new IllegalArgumentException("Variable name cannot be null");
        }
        this.name = name;
        this.expressionKind=ExpressionKind.VARIABLE;
    }

    public Variable(String name, VariableKind field) {
        if (name==null) {
            throw new IllegalArgumentException("Variable name cannot be null");
        }
        this.name = name;
        this.expressionKind=ExpressionKind.VARIABLE;
        this.field=field;
    }


    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", field=" + field +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }

    public static Variable VARIABLE(String name) {
        return new Variable(name);
    }
    public static Variable VARIABLE(String name, VariableKind kind) {
        return new Variable(name, kind);
    }

    public Variable ANNOTATION(PastAnnotation... annot) {
        for (PastAnnotation a: annot) {
            if (a==null) {
                throw new IllegalArgumentException("Annotation cannot be null");
            }
            this.annotation.add(a);
        }
        return this;
    }

    public Variable ANNOTATION(String... annot) {
        for (String a: annot) {
            if (a==null) {
                throw new IllegalArgumentException("Annotation cannot be null");
            }
            this.annotation.add(toAnnotation(a));
        }
        return this;
    }


}
