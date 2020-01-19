package org.openprovenance.prov.rdf;

import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.IOException;
import java.io.InputStream;

public class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {

    final Utility u;
    private final ProvFactory pFactory;
    private final Formats.ProvFormat format;

    public ProvDeserialiser(ProvFactory pFactory, Formats.ProvFormat format) {
        this.pFactory=pFactory;
        this.u=new Utility(pFactory);
        this.format=format;
    }

    /**
     * Deerializes a document from a stream
     *
     * @param in an {@link InputStream}
     * @return org.openprovenance.prov.model.Document
     */
    @Override
    public Document deserialiseDocument(InputStream in) throws IOException {
        return u.parseRDF(in,format,"baseuri");
    }
}
