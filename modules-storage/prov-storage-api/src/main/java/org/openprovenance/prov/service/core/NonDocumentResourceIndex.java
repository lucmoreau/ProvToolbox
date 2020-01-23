package org.openprovenance.prov.service.core;

public interface NonDocumentResourceIndex<T extends NonDocumentResource> {
    T get(String key);
    T newResource();
    void put(String key, T dr);
    void remove(String key);
    String newId();
    StorageKind kind();
    enum StorageKind { ME, RE; }
}
