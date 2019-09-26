package org.openprovenance.prov.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/** A low-level interface for JAXB-compatible serialization of documents. 
 */
public interface ProvSerialiser {

    /**
     * Serializes a document to a stream
     * @param out an {@link OutputStream}
     * @param document a {@link Document}
     * @param formatted  a boolean indicating whether the output should be pretty-printed
     */
    public void serialiseDocument (OutputStream out, Document document, boolean formatted);

    public void serialiseDocument (OutputStream out, Document document, String mediaType, boolean formatted);


    public Collection<String> mediaTypes();
    
}
