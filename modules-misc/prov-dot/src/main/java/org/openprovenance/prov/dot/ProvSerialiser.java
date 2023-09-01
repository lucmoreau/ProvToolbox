package org.openprovenance.prov.dot;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.OutputStream;
import java.util.Collection;

public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {
    private final ProvFactory pFactory;
    private final String extension;
    private final Integer maxStringLength;

    public ProvSerialiser(ProvFactory pFactory, String extension, Integer maxStringLength) {
        this.pFactory=pFactory;
        this.extension=extension;
        this.maxStringLength=maxStringLength;
    }
    public ProvSerialiser(ProvFactory pFactory, String extension) {
        this.pFactory=pFactory;
        this.extension=extension;
        this.maxStringLength=null;
    }

    /**
     * Serializes a document to a stream
     *
     * @param out       an {@link OutputStream}
     * @param document  a {@link Document}
     * @param formatted a boolean indicating whether the output should be pretty-printed
     */
    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {

        ProvToDot provToDot = new ProvToDot(pFactory);
        provToDot.setMaxStringLength(maxStringLength);
        provToDot.convert(document, out, extension, "title");

    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, String mediaType, boolean formatted) {
        serialiseDocument(out, document, formatted);
    }

    @Override
    public Collection<String> mediaTypes() {
        return null;
    }
}
