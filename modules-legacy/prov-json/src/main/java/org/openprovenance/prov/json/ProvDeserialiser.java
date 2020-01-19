package org.openprovenance.prov.json;

import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

public class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {
    private final ProvFactory pFactory;

    public ProvDeserialiser(ProvFactory pFactory) {
        this.pFactory=pFactory;
    }

    /**
     * Deerializes a document from a stream
     *
     * @param in an {@link InputStream}
     * @return org.openprovenance.prov.model.Document
     */
    @Override
    public Document deserialiseDocument(InputStream in) throws IOException {
        return new Converter(pFactory).readDocument(in);
    }
}
