package org.openprovenance.prov.model.exception;

/**
 * A TimeZone exception.
 *
 */
public class TimeZoneException extends RuntimeException {

    public TimeZoneException(String message) {
        super(message);
    }
    public TimeZoneException(Exception e) {
        super("TimeZone Exception", e);
    }
    public TimeZoneException(String reason, Exception e) {
        super(reason, e);
    }


}
