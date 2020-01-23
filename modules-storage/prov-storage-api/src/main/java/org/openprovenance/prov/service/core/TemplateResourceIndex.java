package org.openprovenance.prov.service.core;

public interface TemplateResourceIndex extends ResourceIndex<DocumentResource> {
    TemplateResource newResource(DocumentResource dr);
}
