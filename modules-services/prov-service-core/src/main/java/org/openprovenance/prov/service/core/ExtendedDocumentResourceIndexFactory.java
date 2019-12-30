package org.openprovenance.prov.service.core;

public class ExtendedDocumentResourceIndexFactory<EXTENDED_RESOURCE extends DocumentResource>  implements ResourceIndex<EXTENDED_RESOURCE> {

    private final ResourceIndex<EXTENDED_RESOURCE> eri;
    private final Instantiable<EXTENDED_RESOURCE> factory;
    private final ResourceIndex<DocumentResource> dri;

    public ExtendedDocumentResourceIndexFactory(ResourceIndex<DocumentResource> dri, Instantiable<EXTENDED_RESOURCE> factory) {
        this.dri=dri;
        this.eri =(ResourceIndex<EXTENDED_RESOURCE>)dri;  //TODO: is this right?
        this.factory=factory;
    }

    public EXTENDED_RESOURCE newResource(DocumentResource dr) {
        final EXTENDED_RESOURCE extended_resource = factory.newResource(dr);
        return extended_resource;
    }

    @Override
    public EXTENDED_RESOURCE get(String key) {
        return  eri.get(key);
    }

    @Override
    public void put(String key, EXTENDED_RESOURCE dr) {
        eri.put(key,dr);
    }

    @Override
    public void remove(String key) {
        eri.remove(key);
    }

    @Override
    public String newId() {
        return eri.newId();
    }

    @Override
    public EXTENDED_RESOURCE newResource() {
        return factory.newResource();
    }


    @Override
    public ResourceIndex.StorageKind kind() {
        return eri.kind();
    }


    @Override
    public <R2 extends EXTENDED_RESOURCE> ExtendedDocumentResourceIndexFactory<R2> getExtender(Instantiable<R2> factory2) {
        return new ExtendedDocumentResourceIndexFactory(dri,factory2);
    }

}
