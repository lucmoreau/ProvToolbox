package org.openprovenance.prov.notation;

import java.util.List;

import org.openprovenance.prov.model.exception.ParserException;

/**
 * Thrown by {@link ProvNReader} when the PROV-N given is not syntactically valid. It reports all the
 * syntax errors found, each with its line and position.
 */
public class ProvNSyntaxException extends ParserException {

    private static final long serialVersionUID = 6411734372367196524L;

    private final List<String> errors;

    public ProvNSyntaxException(String source, List<String> errors) {
        super("Invalid PROV-N" + (source == null ? "" : " in " + source) + ": " + String.join("; ", errors));
        this.errors = List.copyOf(errors);
    }

    /** The syntax errors, each as "line l:c message", in the order they were found. */
    public List<String> getErrors() {
        return errors;
    }
}
