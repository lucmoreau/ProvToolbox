package org.openprovenance.prov.template.compiler.past.checker;

public class TypeDiagnostic {
    public enum Severity { WARNING, ERROR }

    public final Severity severity;
    public final String message;
    public final String className;
    public final String methodName;
    public final String detail;

    public TypeDiagnostic(Severity severity, String message, String className, String methodName, String detail) {
        this.severity = severity;
        this.message = message;
        this.className = className;
        this.methodName = methodName;
        this.detail = detail;
    }

    public static TypeDiagnostic error(String message, String className, String methodName, String detail) {
        return new TypeDiagnostic(Severity.ERROR, message, className, methodName, detail);
    }

    public static TypeDiagnostic warning(String message, String className, String methodName, String detail) {
        return new TypeDiagnostic(Severity.WARNING, message, className, methodName, detail);
    }

    @Override
    public String toString() {
        return severity + ": " + className + "." + methodName + " - " + message +
                (detail != null ? " [" + detail + "]" : "");
    }
}
