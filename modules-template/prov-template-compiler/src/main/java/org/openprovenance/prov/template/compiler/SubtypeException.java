package org.openprovenance.prov.template.compiler;

/** T36: a {@code subtype} entry the compiler refuses. Generation stops: it fails closed. */
public class SubtypeException extends RuntimeException {
    public SubtypeException(String message) {
        super("T36: " + message);
    }
}
