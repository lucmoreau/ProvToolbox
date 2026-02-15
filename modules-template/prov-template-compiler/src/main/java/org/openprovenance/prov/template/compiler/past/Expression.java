package org.openprovenance.prov.template.compiler.past;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

@JsonPropertyOrder({"expressionKind"})
public abstract class Expression extends Statement {
    public enum ExpressionKind {VALUE, VARIABLE, METHOD_CALL,CLASS_INSTANTIATION, PARAMETER_REFERENCE, CONSTANT, CAST, ARRAY_INITIALISER, LAMBDA_EXPRESSION, ARRAY_ACCESSOR, POST_INCREMENT, IF_EXPRESSION, ARRAY_ALLOCATOR, BINARY_OP    }
    public ExpressionKind expressionKind;
    public TypeName inferredType;  // set by TypeInferrer during type checking

    public Expression() {
        this.statementKind=StatementKind.EXPRESSION_STATEMENT;
    }

}
