package org.openprovenance.prov.service.core;

public interface Instantiable<T> {
    T newResource(DocumentResource dr);
    T newResource();
}
