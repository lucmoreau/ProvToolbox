package org.openprovenance.prov.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** A low-level interface for  serialization of documents.
 */
public interface ProvDeserialiser {

    /**
     * Deerializes a document from a stream
     * @param in an {@link InputStream}
     * @return org.openprovenance.prov.model.Document
     * @throws IOException if the stream cannot be read
     */

    org.openprovenance.prov.model.Document deserialiseDocument (InputStream in) throws IOException;

}
