package org.openprovenance.prov.service.core.memory;


import java.util.LinkedHashMap;
import java.util.Map;


public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {
    private final int limit;
    public LRUHashMap(int limit) {
        super(16, 0.75f, true);
        this.limit = limit;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > limit;
    }
}
