package org.openprovenance.prov.model.exception;

/**
 * An exception thrown when an invalid case occurs.
 *
 * @author lavm
 *
 */
public class InvalidIndexException extends RuntimeException {


    /**
     *
     */
    private static final long serialVersionUID = -2443740480287709699L;

    /**
     *
     */
    public InvalidIndexException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message a message
     */
    public InvalidIndexException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause the exception
     */
    public InvalidIndexException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message a message
     * @param cause the exception
     */
    public InvalidIndexException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }


}
