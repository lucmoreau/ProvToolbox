package org.openprovenance.prov.template.log2prov;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;

public final class DocumentMergerProcessor implements DocumentProcessor {
    

    private IndexedDocument iDoc;

    public DocumentMergerProcessor(IndexedDocument iDoc) {
        this.iDoc=iDoc;
    }

    @Override
    public void process(Document doc) {
        iDoc.merge(doc);   
    }

    @Override
    public Document getDocument() {
        return iDoc.toDocument();
    }
}