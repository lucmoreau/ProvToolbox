package org.openprovenance.prov.template.types;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.template.log2prov.DocumentProcessor;

public class NullDocumentProcessor implements DocumentProcessor {
    @Override
    public void process(Document make) {

    }

    @Override
    public Document getDocument() {
        return null;
    }

}
