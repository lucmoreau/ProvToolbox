package org.openprovenance.prov.service.core.memory;

import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.Instantiable;
import org.openprovenance.prov.storage.api.ResourceIndex;

import java.util.HashMap;
import java.util.Map;

public class DocumentResourceIndexInMemory implements ResourceIndex<DocumentResource> {
    static int count=100000;
    public final Map<String, DocumentResource> table;

    public static Instantiable<DocumentResource> factory =new Instantiable<DocumentResource>() {
        @Override
        public DocumentResource newResource(DocumentResource dr) {
            return dr;
        }

        @Override
        public DocumentResource newResource() {
            return new DocumentResourceInMemory();
        }
    };

    public DocumentResourceIndexInMemory(Map<String,DocumentResource> table) {
        this.table=table;
    }

    public DocumentResourceIndexInMemory(int limit) {
        this.table=new LRUHashMap<>(limit);
    }

    public DocumentResourceIndexInMemory () {
        this.table = new HashMap<>();
    }

    @Override
    public DocumentResource get(String key) {
        return table.get(key);
    }

    @Override
    public void put(String key, DocumentResource dr) {
        //System.out.println("DocumentResourceIndexInMemory.put() " + key + " " + dr);
        table.put(key,dr);
    }

    @Override
    public void remove(String key) {
        //System.out.println("DocumentResourceIndexInMemory.remove() " + key);
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
        put(id, dr); // MAYBE, I should not 'put' it by default
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
    public ResourceIndex<DocumentResource> getAncestor() {
        throw new UnsupportedOperationException("No ancestor to root Index");
    }

/*
    @Override
    public <EXTENDED_RESOURCE extends DocumentResource> ExtendedDocumentResourceIndexFactory<EXTENDED_RESOURCE> getExtender(Instantiable<EXTENDED_RESOURCE> factory) {
        return new ExtendedDocumentResourceIndexFactory(this,factory);
    }


 */
    public static void register(Map<String,Instantiable<?>> m) {
        m.put(DocumentResource.getResourceKind(),DocumentResourceIndexInMemory.factory);
    }


}
