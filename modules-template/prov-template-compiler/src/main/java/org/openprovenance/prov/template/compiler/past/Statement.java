package org.openprovenance.prov.template.compiler.past;


public abstract class Statement {
    public enum StatementKind {RETURN, ASSIGNMENT, DECLARATION, COMMENT, IF_STATEMENT, FOR_LOOP, ITERATOR, EXPRESSION_STATEMENT}
    public StatementKind statementKind;

}
