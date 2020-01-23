package org.openprovenance.prov.service.core;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.filesystem.NonDocumentResourceStorageFileSystem;
import org.openprovenance.prov.service.core.memory.NonDocumentResourceIndexInMemory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.openprovenance.prov.service.core.ServiceUtils.UPLOADED_FILE_PATH;

public class ServiceUtilsConfig {
    public Map<String,ResourceIndex<?>> extensionMap =new HashMap<>();
    public NonDocumentResourceIndex<NonDocumentResource> nonDocumentResourceIndex=new NonDocumentResourceIndexInMemory( 100);
    public NonDocumentResourceStorage nonDocumentResourceStorage=new NonDocumentResourceStorageFileSystem(new File(UPLOADED_FILE_PATH));
    public Map<String,NonDocumentGenericResourceStorage<?>> genericResourceStorageMap=new HashMap<>();
    public ResourceStorage storageManager;
    public int documentCacheSize;
    public ProvFactory pFactory;
    public ObjectSerialiser serialiser;
    public boolean autoDelete=true;
    public int deletePeriod=600;
}
