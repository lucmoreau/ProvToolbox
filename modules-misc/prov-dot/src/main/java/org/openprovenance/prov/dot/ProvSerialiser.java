package org.openprovenance.prov.dot;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.OutputStream;

public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {
    private final ProvFactory pFactory;
    private final String extension;
    private final Integer maxStringLength;
    private final boolean displayQualifiedRelation;

    public ProvSerialiser(ProvFactory pFactory, String extension, Integer maxStringLength) {
        this.pFactory=pFactory;
        this.extension=extension;
        this.maxStringLength=maxStringLength;
        this.displayQualifiedRelation=false;
    }
    public ProvSerialiser(ProvFactory pFactory, String extension, Integer maxStringLength, boolean displayQualifiedRelation) {
        this.pFactory=pFactory;
        this.extension=extension;
        this.maxStringLength=maxStringLength;
        this.displayQualifiedRelation=displayQualifiedRelation;
    }

    public ProvSerialiser(ProvFactory pFactory, String extension) {
        this.pFactory=pFactory;
        this.extension=extension;
        this.maxStringLength=null;
        this.displayQualifiedRelation=false;
    }
    public ProvSerialiser(ProvFactory pFactory, String extension, boolean displayQualifiedRelation) {
        this.pFactory=pFactory;
        this.extension=extension;
        this.maxStringLength=null;
        this.displayQualifiedRelation=displayQualifiedRelation;
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


        ProvToDot provToDot = (displayQualifiedRelation)? ProvToDot.newProvToDot(pFactory,true): new ProvToDot(pFactory);
        provToDot.setMaxStringLength(maxStringLength);
        provToDot.convert(document, out, extension, "title");

    }
}
