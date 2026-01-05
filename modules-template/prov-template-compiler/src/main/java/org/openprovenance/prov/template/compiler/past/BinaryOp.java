package org.openprovenance.prov.template.compiler.past;



public class BinaryOp extends Expression {
    public static final String EQ = "==";
    public static final String LT = "<";
    public static final String INSTANCEOF= "instanceof";
    public final Expression left;
    public final String op;
    public final Expression right;

    public BinaryOp(Expression left, String op, Expression right) {
        this.left=left;
        this.op=op;
        this.right=right;
        this.expressionKind=ExpressionKind.BINARY_OP;
    }

    @Override
    public String toString() {
        return "BinaryOp{" +
                "left=" + left +
                ", op='" + op + '\'' +
                ", right=" + right +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }

    public static BinaryOp BINARY_OP(Expression left, String op, Expression right) {
        return new BinaryOp(left, op, right);
    }
}
