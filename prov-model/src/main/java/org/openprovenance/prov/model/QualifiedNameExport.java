package org.openprovenance.prov.model;

public interface QualifiedNameExport {
    /**A method to create a string representation of a QualifiedName,
     * selecting the appropriate prefix, chose for the current export.
     */
    public String qualifiedNameToString(QualifiedName qn);

}
