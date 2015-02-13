package org.openprovenance.prov.rdf;

import java.io.IOException;

import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;

public class RdfConverterException extends RuntimeException {

    private static final long serialVersionUID = 4521714384291852841L;

    public RdfConverterException(String string, IOException e) {
	super(string, e);
    }

    public RdfConverterException(String string, RepositoryException e) {
	super(string, e);
    }

    public RdfConverterException(String string, RDFHandlerException e) {
	super(string, e);
    }

}
