package org.openprovenance.prov.template.descriptors;

/** T36 part D: an {@code @performative} declaration the compiler refuses. Generation stops: it fails closed. */
public class PerformativeException extends InJsonException {
    public PerformativeException(String message) {
        super(message);
    }
}
