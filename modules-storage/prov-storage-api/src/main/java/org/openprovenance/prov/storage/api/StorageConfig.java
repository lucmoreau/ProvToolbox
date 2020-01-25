package org.openprovenance.prov.storage.api;

import java.util.HashMap;
import java.util.Map;

public class StorageConfig {
    public Map<String, ResourceIndex<?>> extensionMap =new HashMap<>();
    public NonDocumentResourceIndex<NonDocumentResource> nonDocumentResourceIndex;
    public NonDocumentResourceStorage nonDocumentResourceStorage;
    public Map<String, NonDocumentGenericResourceStorage<?>> genericResourceStorageMap=new HashMap<>();
    public ResourceStorage storageManager;

}

