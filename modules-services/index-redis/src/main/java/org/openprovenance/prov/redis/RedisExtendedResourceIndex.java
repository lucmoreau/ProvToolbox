package org.openprovenance.prov.redis;

import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.ExtendedDocumentResourceIndexFactory;
import org.openprovenance.prov.service.core.Instantiable;
import org.openprovenance.prov.service.core.ResourceIndex;

public class RedisExtendedResourceIndex<EXTENDED_RESOURCE extends DocumentResource> implements ResourceIndex<EXTENDED_RESOURCE> {

    private final RedisDocumentResourceIndex rdri;

    public RedisExtendedResourceIndex(RedisDocumentResourceIndex rdri) {
        this.rdri=rdri;
    }

    @Override
    public EXTENDED_RESOURCE get(String key) {
        return null;
    }

    @Override
    public EXTENDED_RESOURCE newResource() {
        return null;
    }

    @Override
    public EXTENDED_RESOURCE newResource(DocumentResource dr) {
        return null;
    }

    @Override
    public void put(String key, EXTENDED_RESOURCE dr) {

    }

    @Override
    public <EXTENDED_RESOURCE2 extends EXTENDED_RESOURCE> ExtendedDocumentResourceIndexFactory<EXTENDED_RESOURCE2> getExtender(Instantiable<EXTENDED_RESOURCE2> factory) {
        return null;
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public String newId() {
        return null;
    }

    @Override
    public StorageKind kind() {
        return null;
    }

    @Override
    public ResourceIndex<DocumentResource> getAncestor() {
        return null;
    }
}
