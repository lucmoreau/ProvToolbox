package org.openprovenance.prov.template.compiler;

/** T36 part E: a {@code flow} entry the compiler refuses. Generation stops: it fails closed. */
public class FlowException extends RuntimeException {
    public FlowException(String message) {
        super("T36: " + message);
    }
}
