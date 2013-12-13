package org.openprovenance.prov.model.exception;

public class UncheckedException extends RuntimeException {
    
    public UncheckedException(Exception e) {
	super("Unchecked Exception", e);
    }
    public UncheckedException(String reason, Exception e) {
	super(reason, e);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1569547113186462619L;

}
