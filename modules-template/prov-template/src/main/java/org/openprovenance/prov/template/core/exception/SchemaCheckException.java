package org.openprovenance.prov.template.core.exception;

/**
 * A generated jsonld output does not conform to the version of the PROV-JSONLD schema its TTF's schemaCheck names.
 * Unlike other task errors, which the batch executor logs and passes over, it fails the execution.
 */
public class SchemaCheckException extends RuntimeException {

    private static final long serialVersionUID = 4127836160425183301L;

    public SchemaCheckException(String message) {
        super(message);
    }
}
