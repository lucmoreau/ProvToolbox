package org.openprovenance.prov.service.core;

public interface ResourceIndex<T extends DocumentResource> {
    T get(String key);
    T newResource();
    T newResource(DocumentResource dr);
    void put(String key, T dr);
    <EXTENDED_RESOURCE extends T> ResourceIndex<EXTENDED_RESOURCE> getExtender(Instantiable<EXTENDED_RESOURCE> factory);
    void remove(String key);
    String newId();
    StorageKind kind();
    enum StorageKind { ME, RE; }
    ResourceIndex<DocumentResource> getAncestor();

}
