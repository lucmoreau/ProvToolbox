package org.openprovenance.prov.service.translation.memory;

import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.core.TemplateResource;

import java.util.Map;


public class TemplateResourceIndexInMemory extends ExtendedDocumentResourceIndexFactory<TemplateResource> implements ResourceIndex<TemplateResource> {
    public TemplateResourceIndexInMemory(ResourceIndex<DocumentResource> dri, Instantiable<TemplateResource> factory) {
        super(dri, factory);
    }

    public static Instantiable<TemplateResource> factory =new Instantiable<TemplateResource>() {
        @Override
        public TemplateResource newResource(DocumentResource dr) {
            return new TemplateResourceInMemory(dr);
        }

        @Override
        public TemplateResource newResource() {
            return new TemplateResourceInMemory();
        }
    };

    public static TemplateResourceIndexInMemory make (ResourceIndex<DocumentResource> ri) {
        return new TemplateResourceIndexInMemory(ri, factory);
    }
/*
    @Override
    public <EXTENDED_RESOURCE extends TemplateResource> ExtendedDocumentResourceIndexFactory getExtender(Instantiable<EXTENDED_RESOURCE> factory) {
        return new ExtendedDocumentResourceIndexFactory(this,factory);
    }


 */
    @Override
    public ResourceIndex<DocumentResource> getAncestor() {
        return dri;
    }


    public static void register(Map<String,Instantiable<?>> m) {
        m.put(TemplateResource.getResourceKind(), TemplateResourceIndexInMemory.factory);
    }


}