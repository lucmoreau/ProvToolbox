package org.openprovenance.prov.model;

import java.io.OutputStream;
import java.util.Collection;

/** Interface for serialization of PROV documents.
 */
public interface ProvDocumentWriter {


    void serialiseDocument (OutputStream out, Document document, String mediaType, boolean formatted);


    Collection<String> mediaTypes();
    
}
