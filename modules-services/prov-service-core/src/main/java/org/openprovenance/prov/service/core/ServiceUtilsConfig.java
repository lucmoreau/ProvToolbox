package org.openprovenance.prov.service.core;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.storage.filesystem.NonDocumentResourceStorageFileSystem;
import org.openprovenance.prov.service.core.memory.NonDocumentResourceIndexInMemory;
import org.openprovenance.prov.storage.api.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.openprovenance.prov.service.core.ServiceUtils.UPLOADED_FILE_PATH;

public class ServiceUtilsConfig extends StorageConfig {
    public final Map<String, String> configuration;

    public ServiceUtilsConfig(Map<String, String> configuration) {
        extensionMap = new HashMap<>();
        nonDocumentResourceIndex=new NonDocumentResourceIndexInMemory( 100);
        nonDocumentResourceStorage=new NonDocumentResourceStorageFileSystem(new File(UPLOADED_FILE_PATH));
        genericResourceStorageMap=new HashMap<>();
        this.configuration=configuration;
    }
    public ResourceStorage storageManager;
    public int documentCacheSize;
    public ProvFactory pFactory;
    public ObjectSerialiser serialiser;
    public boolean autoDelete=true;
    public int deletePeriod=600;
}
