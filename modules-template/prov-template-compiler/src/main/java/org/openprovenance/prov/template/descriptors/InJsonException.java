package org.openprovenance.prov.template.descriptors;

/** T36: an {@code @inJson} declaration the compiler refuses. Generation stops: it fails closed. */
public class InJsonException extends RuntimeException {
    public InJsonException(String message) {
        super("T36: " + message);
    }
}
