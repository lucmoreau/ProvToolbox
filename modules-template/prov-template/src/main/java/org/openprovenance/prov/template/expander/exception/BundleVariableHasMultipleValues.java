package org.openprovenance.prov.template.expander.exception;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.json.Descriptors;

public class BundleVariableHasMultipleValues extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1270038830746760223L;

    public BundleVariableHasMultipleValues(QualifiedName id,
                                           Descriptors vals) {
    }

}
