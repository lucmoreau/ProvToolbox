package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.type.TypeName;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Definition extends Statement {

    public final TypeName type;
    public final Expression leftHandExpression;
    public final Expression value;
    public List<Modifier> modifiers = new ArrayList<>();
    public List<String> annotation=new ArrayList<>();

    public Definition(TypeName type, Expression leftHandExpression, Expression value) {
        if (leftHandExpression ==null) {
            throw new IllegalArgumentException("Left hand expression cannot be null");
        }
        if (value==null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (type==null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
        this.leftHandExpression = leftHandExpression;
        this.value = value;
        this.statementKind=StatementKind.DEFINITION;

    }

    static public Definition get(TypeName type, Variable variable, Expression value) {
        return new Definition(type, variable, value);
    }

    public Definition addModifier(Modifier modifier) {
        if (modifier==null) {
            throw new IllegalArgumentException("Modifier cannot be null");
        }
        this.modifiers.add(modifier);
        return this;
    }

    @Override
    public String toString() {
        return "Definition{" +
                "type=" + type +
                ", leftHandExpression=" + leftHandExpression +
                ", value=" + value +
                ", modifiers=" + modifiers +
                ", annotation=" + annotation +
                ", statementKind=" + statementKind +
                '}';
    }

    public static Definition DEFINITION(TypeName type, Expression leftHandExpression, Expression value) {
        return new Definition(type, leftHandExpression, value);
    }
    public Definition ANNOTATION(String annot) {
        this.annotation.add(annot);
        return this;
    }
}
