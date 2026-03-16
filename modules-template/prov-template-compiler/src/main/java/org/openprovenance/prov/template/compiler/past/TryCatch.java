package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.type.TypeName;

import java.util.ArrayList;
import java.util.List;

public class TryCatch extends Statement {
    public final List<Statement> tryBlock = new ArrayList<>();
    public final TypeName exceptionType;
    public final String exceptionName;
    public final List<Statement> catchBlock = new ArrayList<>();

    public TryCatch(TypeName exceptionType, String exceptionName) {
        if (exceptionType == null) {
            throw new IllegalArgumentException("Exception type cannot be null");
        }
        if (exceptionName == null) {
            throw new IllegalArgumentException("Exception name cannot be null");
        }
        this.exceptionType = exceptionType;
        this.exceptionName = exceptionName;
        this.statementKind = StatementKind.TRY_CATCH;
    }

    public TryCatch TRY(Statement... statements) {
        for (Statement statement : statements) {
            if (statement == null) {
                throw new IllegalArgumentException("Statement cannot be null");
            }
            this.tryBlock.add(statement);
        }
        return this;
    }

    public TryCatch CATCH(Statement... statements) {
        for (Statement statement : statements) {
            if (statement == null) {
                throw new IllegalArgumentException("Statement cannot be null");
            }
            this.catchBlock.add(statement);
        }
        return this;
    }

    public static TryCatch TRY_CATCH(TypeName exceptionType, String exceptionName) {
        return new TryCatch(exceptionType, exceptionName);
    }

    @Override
    public String toString() {
        return "TryCatch{" +
                "tryBlock=" + tryBlock +
                ", exceptionType=" + exceptionType +
                ", exceptionName='" + exceptionName + '\'' +
                ", catchBlock=" + catchBlock +
                ", statementKind=" + statementKind +
                '}';
    }
}
