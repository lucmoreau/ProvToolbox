package org.openprovenance.prov.model.exception;

/**
 * An exception thrown when an invalid case occurs.
 * 
 * @author lavm
 *
 */
public class InvalidCaseException extends RuntimeException {


    /**
     * 
     */
    private static final long serialVersionUID = -2443740480287709699L;

    /**
     * 
     */
    public InvalidCaseException() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message a message
     */
    public InvalidCaseException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param cause the exception
     */
    public InvalidCaseException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message a message
     * @param cause the exception
     */
    public InvalidCaseException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    
}
