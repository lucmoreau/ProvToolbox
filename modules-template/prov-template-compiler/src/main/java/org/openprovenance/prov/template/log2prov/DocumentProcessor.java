package org.openprovenance.prov.template.log2prov;

import org.openprovenance.prov.model.Document;

/** A streaming interface for a constructor of PROV Documents, which accumulates process statements by means of its process method. */

public interface DocumentProcessor {

    void process(Document make);
    
    Document getDocument();

    default void end() {};
}
