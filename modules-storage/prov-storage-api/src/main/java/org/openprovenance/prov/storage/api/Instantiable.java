package org.openprovenance.prov.storage.api;

public interface Instantiable<T> {
    T newResource(DocumentResource dr);
    T newResource();
}
