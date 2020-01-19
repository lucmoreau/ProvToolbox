package org.openprovenance.prov.notation;

import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.IOException;
import java.io.InputStream;

public class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {

    private final ProvFactory pFactory;
    private final Utility u;

    public ProvDeserialiser(ProvFactory pFactory) {
        this.pFactory=pFactory;
        this.u=new Utility();
    }

    /**
     * Deerializes a document from a stream
     *
     * @param in an {@link InputStream}
     * @return org.openprovenance.prov.model.Document
     */
    @Override
    public Document deserialiseDocument(InputStream in){
        return u.readDocument(in,pFactory) ;
    }
}
