package org.openprovenance.prov.service.core.memory;

import org.openprovenance.prov.service.core.*;

import java.util.Map;

public class DocumentResourceIndexInMemory implements ResourceIndex<DocumentResource> {
    static int count=100000;
    private final Map<String, DocumentResource> table;

    public DocumentResourceIndexInMemory(Map<String,DocumentResource> table) {
        this.table=table;
    }
    @Override
    public DocumentResource get(String key) {
        return table.get(key);
    }

    @Override
    public void put(String key, DocumentResource dr) {
        table.put(key,dr);
    }

    @Override
    public void remove(String key) {
        table.remove(key);
    }

    @Override
    synchronized public String newId() {
        return "m" + count++;
    }

    @Override
    public DocumentResource newResource() {
        String id=newId();
        DocumentResource dr=new DocumentResourceInMemory();
        dr.setVisibleId(id);
        put(id, dr);
        return dr;
    }

    @Override
    public DocumentResource newResource(DocumentResource dr) {
        return dr;
    }

    @Override
    public StorageKind kind() {
        return StorageKind.ME;
    }


    @Override
    public <EXTENDED_RESOURCE extends DocumentResource> ExtendedDocumentResourceIndexFactory<EXTENDED_RESOURCE> getExtender(Instantiable<EXTENDED_RESOURCE> factory) {
        return new ExtendedDocumentResourceIndexFactory(this,factory);
    }


}
