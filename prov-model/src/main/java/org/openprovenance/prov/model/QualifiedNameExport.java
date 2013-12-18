package org.openprovenance.prov.model;

/** An interface for an export of QualifiedName to String. */

public interface QualifiedNameExport {

    /** A method to create a string representation of a QualifiedName,
     * selecting the appropriate prefix, chose by the current exporter.
     * @param name the QualifiedName to convert to string
     * @return a string representation of the QualifiedName
     */
    public String qualifiedNameToString(QualifiedName qn);

}
