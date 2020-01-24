package org.openprovenance.prov.storage.api;

public interface TemplateResourceIndex extends ResourceIndex<DocumentResource> {
    TemplateResource newResource(DocumentResource dr);
}
