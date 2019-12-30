package org.openprovenance.prov.service.translation;

import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.ResourceIndex;

public interface TemplateResourceIndex extends ResourceIndex<DocumentResource> {
    TemplateResource newResource(DocumentResource dr);
}
