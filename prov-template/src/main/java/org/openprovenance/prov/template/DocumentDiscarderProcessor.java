package org.openprovenance.prov.template;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvUtilities;

public final class DocumentDiscarderProcessor implements DocumentProcessor {
    

    private Document doc;

    public DocumentDiscarderProcessor(Document doc) {
        this.doc=doc;
    }

    @Override
    public void process(Document ndoc) {
    }

    @Override
    public Document getDocument() {
        new ProvUtilities().updateNamespaces(doc);
        return doc;
    }
}