package org.openprovenance.prov.template.compiler.past;

import java.util.List;

public class SuperConstructorCall extends Statement {
    public final List<Expression> arguments;

    public SuperConstructorCall(List<Expression> arguments) {
        this.arguments = arguments;
        this.statementKind = StatementKind.SUPER_CONSTRUCTOR_CALL;
    }

    public static SuperConstructorCall SUPER_CALL(List<Expression> arguments) {
        return new SuperConstructorCall(arguments);
    }

    public static SuperConstructorCall SUPER_CALL(Expression... arguments) {
        return new SuperConstructorCall(List.of(arguments));
    }

    @Override
    public String toString() {
        return "SuperConstructorCall{" +
                "arguments=" + arguments +
                ", statementKind=" + statementKind +
                '}';
    }
}
