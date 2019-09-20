/**
 * 
 */
package org.openprovenance.prov.model.exception;

import org.openprovenance.prov.model.QualifiedName;

/**
 * An exception thrown when {@link QualifiedName} processing is problematic.
 * @author lavm
 *
 */
public class QualifiedNameException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -2683382479059721612L;

    /**
     * 
     */
    public QualifiedNameException() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message a message descriping the exception situation
     */
    public QualifiedNameException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param cause the cause
     */
    public QualifiedNameException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message a messae
     * @param cause the cause
     */
    public QualifiedNameException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    
}
