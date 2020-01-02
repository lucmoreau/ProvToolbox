package org.openprovenance.prov.service.translation.memory;

import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.memory.ExtendedDocumentResourceInMemory;
import org.openprovenance.prov.service.translation.TemplateResource;

public class TemplateResourceInMemory extends ExtendedDocumentResourceInMemory implements TemplateResource, Cloneable {

    private String templateStorageId;

    private String bindingsStorageId;


    public TemplateResourceInMemory(DocumentResource d) {
        super(d);
    }


    public TemplateResourceInMemory() {
        throw new UnsupportedOperationException("null constructor to template resource in memory");
    }

    @Override
    public String getTemplateStorageId() {
        return templateStorageId;
    }

    @Override
    public void setTemplateStorageId(String templateStorageId) {
        this.templateStorageId = templateStorageId;
    }

    @Override
    public String getBindingsStorageId() {
        return bindingsStorageId;
    }

    @Override
    public void setBindingsStorageId(String bindingsStorageId) {
        this.bindingsStorageId = bindingsStorageId;
    }

}
