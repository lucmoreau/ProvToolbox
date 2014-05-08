package org.openprovenance.prov.template;

import java.util.List;

import org.openprovenance.prov.model.QualifiedName;

public class BundleVariableHasMultipleValues extends RuntimeException {

    public BundleVariableHasMultipleValues(QualifiedName id,
	    List<QualifiedName> vals) {
    }

}
