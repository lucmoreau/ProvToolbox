package org.openprovenance.prov.interop;

public class InteropException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 341614640704209138L;

    public InteropException(Exception e) {
	super(e);
    }

    public InteropException(Throwable e) {
        super (e);
    }

    public InteropException(String string) {
        super(string);
    }
}
