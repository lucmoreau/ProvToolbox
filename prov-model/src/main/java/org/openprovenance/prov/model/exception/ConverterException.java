package org.openprovenance.prov.model.exception;

/**
 * An exception occurring during value conversion.
 * @author lavm
 *
 */
public class ConverterException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -2683382479059721612L;

    /**
     * 
     */
    public ConverterException() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message a message
     */
    public ConverterException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param cause a cause
     */
    public ConverterException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message a message
     * @param cause a cause
     */
    public ConverterException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    
}
