package org.openprovenance.prov.viz;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.OutputStream;

/** Serialises a document as mermaid text ({@code mmd}), or as what {@code mmdc} renders from it (svg, png, pdf). */
public class MermaidSerialiser implements org.openprovenance.prov.model.ProvSerialiser {
    private final ProvFactory pFactory;
    private final String extension;
    private final Integer maxStringLength;
    private final boolean displayQualifiedRelation;

    public MermaidSerialiser(ProvFactory pFactory, String extension) {
        this(pFactory, extension, null, false);
    }

    public MermaidSerialiser(ProvFactory pFactory, String extension, boolean displayQualifiedRelation) {
        this(pFactory, extension, null, displayQualifiedRelation);
    }

    public MermaidSerialiser(ProvFactory pFactory, String extension, Integer maxStringLength) {
        this(pFactory, extension, maxStringLength, false);
    }

    public MermaidSerialiser(ProvFactory pFactory, String extension, Integer maxStringLength, boolean displayQualifiedRelation) {
        this.pFactory = pFactory;
        this.extension = extension;
        this.maxStringLength = maxStringLength;
        this.displayQualifiedRelation = displayQualifiedRelation;
    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        ProvToMermaid toMermaid = displayQualifiedRelation ? ProvToMermaid.newProvToMermaid(pFactory, true) : new ProvToMermaid(pFactory);
        toMermaid.setMaxStringLength(maxStringLength);
        toMermaid.convert(document, out, extension, "title");
    }
}
