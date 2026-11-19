package org.openprovenance.prov.template.descriptors;

/** T36: an {@code @inJson} declaration the compiler refuses. Generation stops: it fails closed. */
/** <b>Experimental:</b> in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality: their declarations, the classes generated from them and this class may change without notice. */
public class InJsonException extends RuntimeException {
    public InJsonException(String message) {
        super("T36: " + message);
    }
}
