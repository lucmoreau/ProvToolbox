package org.openprovenance.prov.template.descriptors;

/** T36 part D: an {@code @performative} declaration the compiler refuses. Generation stops: it fails closed. */
/** <b>Experimental:</b> in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality: their declarations, the classes generated from them and this class may change without notice. */
public class PerformativeException extends InJsonException {
    public PerformativeException(String message) {
        super(message);
    }
}
