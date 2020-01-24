package org.openprovenance.prov.service.core.memory;

import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;

import java.util.Date;
import java.util.Map;

public class ExtendedDocumentResourceInMemory implements DocumentResource, Cloneable {

    private final DocumentResource d;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public ExtendedDocumentResourceInMemory(DocumentResource d) {
        this.d=d;
    }


    public ExtendedDocumentResourceInMemory() {
        throw new UnsupportedOperationException("null constructor to Extended resource in memory");
    }

    @Override
    public String getVisibleId() {
        return d.getVisibleId();
    }

    @Override
    public void setVisibleId(String visibleId) {
        d.setVisibleId(visibleId);
    }

    @Override
    public String getStorageId() {
        return d.getStorageId();
    }

    @Override
    public void setStorageId(String storageId) {
        d.setStorageId(storageId);
    }

    @Override
    public Date getExpires() {
        return d.getExpires();
    }

    @Override
    public void setExpires(Date expires) {
        d.setExpires(expires);
    }

    @Override
    public ResourceIndex.StorageKind getKind() {
        return d.getKind();
    }

    @Override
    public void setKind(ResourceIndex.StorageKind kind) {
        d.setKind(kind);
    }

    @Override
    public Throwable getThrown() {
        return d.getThrown();
    }

    @Override
    public void setThrown(Throwable thrown) {
        d.setThrown(thrown);
    }

    @Override
    public Map<String, Object> getExtension() {
        return d.getExtension();
    }

    @Override
    public void setExtension(Map<String, Object> extension) {
        d.setExtension(extension);
    }


}
