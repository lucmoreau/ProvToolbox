package org.openprovenance.prov.service.translation.memory;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.ResourceIndex;
import org.openprovenance.prov.service.translation.TemplateResource;
import org.openprovenance.prov.template.expander.Bindings;

import java.util.Date;
import java.util.Map;

public class TemplateResourceInMemory implements TemplateResource, Cloneable {

    private final DocumentResource d;

    private String templateStorageId;

    public Bindings getBindings() {
        return bindings;
    }

    public void setBindings(Bindings bindings) {
        this.bindings = bindings;
    }


    private Bindings bindings;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public TemplateResourceInMemory(DocumentResource d) {
        this.d=d;
    }


    public TemplateResourceInMemory() {
        throw new UnsupportedOperationException("null constructor to template resource in memory");
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
    public Document document() {
        return d.document();
    }

    @Override
    public void setDocument(Document doc) {
        d.setDocument(doc);
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

    @Override
    public String getTemplateStorageId() {
        return templateStorageId;
    }
    @Override
    public void setTemplateStorageId(String templateStorageId) {
        this.templateStorageId = templateStorageId;
    }
}
