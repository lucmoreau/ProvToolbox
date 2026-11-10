package org.openprovenance.prov.template.compiler.privacy;

/** T31 (R11): a data-protection annotation or policy the compiler refuses. Generation stops: it fails closed. */
public class PrivacyException extends RuntimeException {
    public PrivacyException(String message) {
        super("T31: " + message);
    }
}
