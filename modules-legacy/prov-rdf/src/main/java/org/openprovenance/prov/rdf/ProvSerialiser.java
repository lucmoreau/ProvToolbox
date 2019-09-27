package org.openprovenance.prov.rdf;

import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openrdf.rio.RDFFormat;

import java.io.OutputStream;
import java.util.Collection;

public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser{


    private final ProvFactory pFactory;
    private final Ontology onto;
    private final Formats.ProvFormat format;

    public ProvSerialiser(ProvFactory pFactory, Formats.ProvFormat format) {
        this.pFactory=pFactory;
        this.onto=new Ontology(pFactory);
        this.format=format;
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
        new Utility(pFactory, onto)
                .dumpRDF(document, format, out);
    }

    @Override
    public Collection<String> mediaTypes() {
        return null;
    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, String mediaType, boolean formatted) {
        serialiseDocument(out, document, formatted);
    }
}
