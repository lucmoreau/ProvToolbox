package org.openprovenance.prov.service.validation.memory;

import org.openprovenance.prov.service.core.ExtendedDocumentResourceIndexFactory;
import org.openprovenance.prov.service.validation.ValidationResource;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.Instantiable;
import org.openprovenance.prov.storage.api.ResourceIndex;

public class ValidationResourceIndexInMemory extends ExtendedDocumentResourceIndexFactory<ValidationResource> implements ResourceIndex<ValidationResource> {
    public ValidationResourceIndexInMemory(ResourceIndex<DocumentResource> dri, Instantiable<ValidationResource> factory) {
        super(dri, factory);
    }



    public static Instantiable<ValidationResource> factory =new Instantiable<ValidationResource>() {
        @Override
        public ValidationResource newResource(DocumentResource dr) {
            return new ValidationResourceInMemory(dr);
        }

        @Override
        public ValidationResource newResource() {
            return new ValidationResourceInMemory();
        }
    };

    public static ValidationResourceIndexInMemory make (ResourceIndex<DocumentResource> ri) {
        return new ValidationResourceIndexInMemory(ri, factory);
    }

}
