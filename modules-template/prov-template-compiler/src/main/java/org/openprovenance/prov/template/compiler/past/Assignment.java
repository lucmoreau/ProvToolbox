package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.type.TypeName;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Assignment extends Statement {

    public final TypeName type;
    public final Expression leftHandExpression;
    public final Expression value;
    public List<Modifier> modifiers = new ArrayList<>();
    public List<String> annotation=new java.util.ArrayList<>();

    public Assignment(TypeName type, Expression leftHandExpression, Expression value) {
        if (leftHandExpression ==null) {
            throw new IllegalArgumentException("Left hand expression cannot be null");
        }
        if (value==null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        this.type = type;
        this.leftHandExpression = leftHandExpression;
        this.value = value;
        this.statementKind=StatementKind.ASSIGNMENT;

    }

    static public Assignment get(TypeName type, Variable variable, Expression value) {
        return new Assignment(type, variable, value);
    }

    public Assignment addModifier(Modifier modifier) {
        if (modifier==null) {
            throw new IllegalArgumentException("Modifier cannot be null");
        }
        this.modifiers.add(modifier);
        return this;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "type=" + type +
                ", leftHandExpression=" + leftHandExpression +
                ", value=" + value +
                ", modifiers=" + modifiers +
                ", statementKind=" + statementKind +
                '}';
    }

    public static Assignment ASSIGNMENT(TypeName type, Expression leftHandExpression, Expression value) {
        return new Assignment(type, leftHandExpression, value);
    }
    public Assignment ANNOTATION(String annot) {
        this.annotation.add(annot);
        return this;
    }
}
