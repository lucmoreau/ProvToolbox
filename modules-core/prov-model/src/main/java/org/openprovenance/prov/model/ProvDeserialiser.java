package org.openprovenance.prov.model;

import java.io.IOException;
import java.io.InputStream;

/** A low-level interface for  serialization of documents.
 */
public interface ProvDeserialiser {

    /**
     * Deserializes a document from a stream
     * @param in an {@link InputStream}
     * @return Document
     * @throws IOException if the stream cannot be read
     */

    Document deserialiseDocument (InputStream in)
            throws IOException;

}
