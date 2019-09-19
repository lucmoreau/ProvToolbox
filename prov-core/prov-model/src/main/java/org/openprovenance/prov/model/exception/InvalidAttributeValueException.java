package org.openprovenance.prov.model.exception;

/**An Exception when an attribute is given an invalid value. For instance, when a prov:label is given a value that is not a string.
 * @author lavm
 *
 */
public class InvalidAttributeValueException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InvalidAttributeValueException() {
	// TODO Auto-generated constructor stub
    }

    public InvalidAttributeValueException(String arg0) {
	super(arg0);
	// TODO Auto-generated constructor stub
    }

    public InvalidAttributeValueException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    public InvalidAttributeValueException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    public InvalidAttributeValueException(String message, Throwable cause,
	    boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	// TODO Auto-generated constructor stub
    }

}
