package org.openprovenance.prov.template.compiler.past;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"expressionKind", "constantType", "string"})
public class Constant extends Expression {

    public enum ConstantType {
        STRING,
        INTEGER,
        FLOAT,
        BOOL,
        BOOLEAN,
        NULL
    }
    public Object value;
    public ConstantType constantType;

    public Constant(String value) {
        this.value = value;
        this.constantType=ConstantType.STRING;
        this.expressionKind=ExpressionKind.CONSTANT;
    }
    public Constant(boolean value) {
        this.value = value;
        this.constantType=ConstantType.BOOL;
        this.expressionKind=ExpressionKind.CONSTANT;
    }

    public Constant(Integer value) {
        this.value = value;
        this.constantType=ConstantType.INTEGER;
        this.expressionKind=ExpressionKind.CONSTANT;
    }

    public Constant(Float value) {
        this.value = value;
        this.constantType=ConstantType.FLOAT;
        this.expressionKind=ExpressionKind.CONSTANT;
    }

    static public Constant getNull() {
        Constant c=new Constant((String)null);
        c.constantType=ConstantType.NULL;
        return c;
    }

    static public Constant CONSTANT(String value) {
        Constant constant = new Constant(value);
        if (value==null) {
            constant.constantType=ConstantType.NULL;
        }
        return constant;
    }
    static public Constant CONSTANT(Integer value) {
        return new Constant(value);
    }
    static public Constant CONSTANT(boolean value) {
        return new Constant(value);
    }

    @Override
    public String toString() {
        return "Constant{" +
                "value=" + value +
                ", constantType=" + constantType +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }


}
