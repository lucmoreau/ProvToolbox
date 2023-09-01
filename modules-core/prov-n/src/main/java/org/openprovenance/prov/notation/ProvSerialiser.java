package org.openprovenance.prov.notation;

import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {

    private final ProvFactory pFactory;

    private final Utility u=new Utility();

    public ProvSerialiser (ProvFactory pFactory) {
        this.pFactory=pFactory;
    }
    /**
     * Serializes a document to a stream
     *
     * @param out       an {@link OutputStream}
     * @param document  a {@link Document}
     * @param formatted a boolean indicating whether the output should be pretty-printed
     */
    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted)  {
        u.writeDocument(document,out,pFactory);
    }

    final static private Collection<String> myMedia= Set.of(InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION);

    @Override
    public Collection<String> mediaTypes() {
        return myMedia;
    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, String mediaType, boolean formatted) {
        serialiseDocument(out, document, formatted);
    }
}
