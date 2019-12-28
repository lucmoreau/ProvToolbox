package org.openprovenance.prov.service.core;

import java.util.Map;

public class ResourceIndexAsMap implements  ResourceIndex {
    static int count=100000;
    private final Map<String, DocumentResource> table;

    public ResourceIndexAsMap(Map<String,DocumentResource> table) {
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
    public StorageKind kind() {
        return StorageKind.ME;
    }


}
