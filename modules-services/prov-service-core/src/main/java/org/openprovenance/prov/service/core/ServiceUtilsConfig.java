package org.openprovenance.prov.service.core;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.config.StorageConfiguration;
import org.openprovenance.prov.storage.filesystem.NonDocumentResourceStorageFileSystem;
import org.openprovenance.prov.service.core.memory.NonDocumentResourceIndexInMemory;
import org.openprovenance.prov.storage.api.*;

import java.io.File;
import java.util.HashMap;


public class ServiceUtilsConfig extends StorageConfig {
    public final StorageConfiguration configuration;

    public ServiceUtilsConfig(StorageConfiguration configuration) {
        extensionMap = new HashMap<>();
        nonDocumentResourceIndex=new NonDocumentResourceIndexInMemory( 100);
        nonDocumentResourceStorage=new NonDocumentResourceStorageFileSystem(new File(configuration.uploaded_filepath));
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
