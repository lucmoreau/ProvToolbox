package org.openprovenance.prov.model;

import javax.xml.namespace.QName;

public interface QNameExport {
    /**A method to create a string representation of a QName,
     * selecting the appropriate prefix, chose for the current export.
     */
    public String qnameToString(QName qname);

}
