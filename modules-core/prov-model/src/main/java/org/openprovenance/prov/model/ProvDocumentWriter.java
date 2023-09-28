package org.openprovenance.prov.model;

import java.io.OutputStream;
import java.util.Collection;

/** Interface for serialization of PROV documents.
 */
public interface ProvDocumentWriter {


    /**
     * Serializes a document to a stream
     * @param out an {@link OutputStream}
     * @param document a {@link Document} to serialize
     * @param mediaType a media type
     * @param formatted  a boolean indicating whether the output should be pretty-printed
     */
    void writeDocument(OutputStream out, Document document, String mediaType, boolean formatted);


    /*
     * Returns the collection of media types supported by this writer.
     * @return a collection of media types
     */
    Collection<String> mediaTypes();
    
}
