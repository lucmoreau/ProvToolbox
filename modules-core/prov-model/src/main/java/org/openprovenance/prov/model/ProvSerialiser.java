package org.openprovenance.prov.model;

import java.io.OutputStream;

/** A low-level interface for serialization of documents by a given serializer. No selection of media type permitted here.
 * For a more generic interface, @see {@link ProvDocumentWriter}.
 */
public interface ProvSerialiser {

    /**
     * Serializes a document to a stream
     * @param out an {@link OutputStream}
     * @param document a {@link Document}
     * @param formatted  a boolean indicating whether the output should be pretty-printed
     */
    void serialiseDocument (OutputStream out, Document document, boolean formatted);

    
}
