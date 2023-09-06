package org.openprovenance.prov.template.expander.exception;

import java.util.List;

import org.openprovenance.prov.model.QualifiedName;

public class BundleVariableHasMultipleValues extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1270038830746760223L;

    public BundleVariableHasMultipleValues(QualifiedName id,
	    List<QualifiedName> vals) {
    }

}
