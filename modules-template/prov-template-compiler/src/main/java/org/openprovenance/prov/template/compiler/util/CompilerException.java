package org.openprovenance.prov.template.compiler.util;

public class CompilerException extends RuntimeException{
    public CompilerException(String message) {
        super(message);
    }

    public CompilerException(String message, Throwable cause) {
        super(message, cause);
    }
}
