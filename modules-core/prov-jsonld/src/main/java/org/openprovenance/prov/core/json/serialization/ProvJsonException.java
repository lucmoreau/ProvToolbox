package org.openprovenance.prov.core.json.serialization;

/** A PROV-JSON document that cannot be read, or a PROV construct that cannot be written, and why. */
public class ProvJsonException extends RuntimeException {
    public ProvJsonException(String message) {
        super(message);
    }

    public ProvJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
