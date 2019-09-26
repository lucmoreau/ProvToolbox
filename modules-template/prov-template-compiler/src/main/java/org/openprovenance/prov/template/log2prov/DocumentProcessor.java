package org.openprovenance.prov.template.log2prov;

import org.openprovenance.prov.model.Document;

public interface DocumentProcessor {

    void process(Document make);
    
    Document getDocument();
}
