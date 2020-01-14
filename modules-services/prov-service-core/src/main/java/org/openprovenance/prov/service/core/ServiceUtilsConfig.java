package org.openprovenance.prov.service.core;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.filesystem.DocumentResourceStorageFileSystem;
import org.openprovenance.prov.service.core.filesystem.NonDocumentResourceStorageFileSystem;
import org.openprovenance.prov.service.core.memory.NonDocumentResourceIndexInMemory;

import java.util.HashMap;
import java.util.Map;

public class ServiceUtilsConfig {
    public Map<String,ResourceIndex<?>> extensionMap =new HashMap<>();
    public NonDocumentResourceIndex<NonDocumentResource> nonDocumentResourceIndex=new NonDocumentResourceIndexInMemory( 100);
    public NonDocumentResourceStorage nonDocumentResourceStorage=new NonDocumentResourceStorageFileSystem();
    public Map<String,NonDocumentGenericResourceStorage<?>> genericResourceStorageMap=new HashMap<>();
    public ResourceStorage storageManager;
    public int documentCacheSize;
    public ProvFactory pFactory;

}
