package org.openprovenance.prov.template;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvUtilities;

public final class DocumentConcatenatorProcessor implements DocumentProcessor {
    

    private Document doc;

    public DocumentConcatenatorProcessor(Document doc) {
        this.doc=doc;
    }

    @Override
    public void process(Document ndoc) {
        this.doc.getStatementOrBundle().addAll(ndoc.getStatementOrBundle());   
    }

    @Override
    public Document getDocument() {
        new ProvUtilities().updateNamespaces(doc);
        return doc;
    }
}