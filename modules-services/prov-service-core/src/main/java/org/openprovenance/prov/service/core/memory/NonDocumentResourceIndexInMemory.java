package org.openprovenance.prov.service.core.memory;

import org.openprovenance.prov.service.core.*;

import java.util.HashMap;
import java.util.Map;

public class NonDocumentResourceIndexInMemory implements NonDocumentResourceIndex<NonDocumentResource> {
    public final Map<String, NonDocumentResource> table;

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

    public NonDocumentResourceIndexInMemory(Map<String,NonDocumentResource> table) {
        this.table=table;
    }

    public NonDocumentResourceIndexInMemory(int limit) {
        this.table=new LRUHashMap<>(limit);
    }

    public NonDocumentResourceIndexInMemory() {
        this.table = new HashMap<>();
    }

    @Override
    public NonDocumentResource get(String key) {
        return table.get(key);
    }

    @Override
    public void put(String key, NonDocumentResource dr) {
        table.put(key,dr);
    }

    @Override
    public void remove(String key) {
        System.out.println("NonDocumentResourceIndexInMemory.remove() " + key);
        table.remove(key);
    }

    @Override
    synchronized public String newId() {
        return "m" + DocumentResourceIndexInMemory.count++;
    }

    @Override
    public NonDocumentResource newResource() {
        String id=newId();
        NonDocumentResource dr=new NonDocumentResourceInMemory();
        dr.setVisibleId(id);
        put(id, dr); // MAYBE, I should not 'put' it by default
        return dr;
    }

    @Override
    public StorageKind kind() {
        return StorageKind.ME;
    }


    public static void register(Map<String,Instantiable<?>> m) {
        m.put(DocumentResource.getResourceKind(), NonDocumentResourceIndexInMemory.factory);
    }


}
