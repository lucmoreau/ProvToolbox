package org.openprovenance.prov.service.translation.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.core.xml.serialization.ProvSerialiser;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.service.core.config.StorageConfiguration;
import org.openprovenance.prov.service.core.memory.DocumentResourceIndexInMemory;
import org.openprovenance.prov.service.translation.actions.ActionExpand;
import org.openprovenance.prov.service.translation.filesystem.BindingsResourceStorageFileSystem;
import org.openprovenance.prov.service.translation.memory.TemplateResourceIndexInMemory;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.api.TemplateResource;
import org.openprovenance.prov.storage.filesystem.DocumentResourceStorageFileSystem;
import org.openprovenance.prov.storage.mongodb.MongoBindingsResourceStorage;
import org.openprovenance.prov.storage.mongodb.MongoDocumentResourceStorage;
import org.openprovenance.prov.storage.mongodb.MongoNonDocumentResourceStorage;
import org.openprovenance.prov.storage.redis.RedisDocumentResourceIndex;
import org.openprovenance.prov.storage.redis.RedisTemplateResourceIndex;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

public class StorageSetup {

    private static final Logger logger = LogManager.getLogger(StorageSetup.class);

    public ServiceUtilsConfig makeConfig(ProvFactory factory, StorageConfiguration configuration) {
        ServiceUtilsConfig utilsConfig = new ServiceUtilsConfig(configuration);
        utilsConfig.pFactory=factory;
        logger.info("Configuration --- " + configuration);
        switch (configuration.index) {
            case "redis":
                logger.info("Using Redis " + configuration.index);
                initRedis(utilsConfig, configuration);
                break;
            case "memory":
                initInMemory(utilsConfig, configuration);
                break;
            case "none":
                break;
            default:
                throw new IllegalArgumentException("Unknown index type: " + configuration.index);
        }
        utilsConfig.documentCacheSize = configuration.cache;
        utilsConfig.autoDelete= configuration.autodelete;
        utilsConfig.deletePeriod= configuration.delete_period;
        switch (configuration.storage) {
            case "mongodb":
                withMongoDb(utilsConfig,factory,configuration);
                break;
            case "fs":
                withFileSystem(utilsConfig,factory,configuration);
                break;
            case "none":
                break;
            default:
                throw new IllegalArgumentException("Unknown storage type: " + configuration.storage);
        }
        return utilsConfig;
    }

    protected ServiceUtilsConfig withMongoDb(ServiceUtilsConfig utilsConfig, ProvFactory factory, StorageConfiguration configuration) {

        final String mongoHost = configuration.mongo_host;
        final String mongoDbName = configuration.dbname;
        utilsConfig.storageManager=new MongoDocumentResourceStorage(mongoHost, mongoDbName);
        utilsConfig.nonDocumentResourceStorage=new MongoNonDocumentResourceStorage(mongoHost, mongoDbName);
        utilsConfig.genericResourceStorageMap.put(ActionExpand.BINDINGS_KEY,new MongoBindingsResourceStorage(mongoHost, mongoDbName, new ObjectMapper()));
        ProvSerialiser serial=new ProvSerialiser();
        utilsConfig.serialiser=(OutputStream out, Object document, String mediaType, boolean formatted) -> serial.serialiseOtherObject(out, document,false, false);
        return utilsConfig;
    }


    protected ServiceUtilsConfig withFileSystem(ServiceUtilsConfig utilsConfig, ProvFactory factory, StorageConfiguration configuration) {
        File uploadLocation = new File(configuration.uploaded_filepath);
        utilsConfig.storageManager=new DocumentResourceStorageFileSystem(factory, uploadLocation);
        ProvSerialiser serial = new ProvSerialiser();
        utilsConfig.serialiser=(OutputStream out, Object document, String mediaType, boolean formatted) -> serial.serialiseOtherObject(out, document,false, false);
        utilsConfig.genericResourceStorageMap.put(ActionExpand.BINDINGS_KEY,new BindingsResourceStorageFileSystem(new ObjectMapper(), uploadLocation));
        return utilsConfig;
    }


    protected Map<String, ResourceIndex<?>> initInMemory(ServiceUtilsConfig config, StorageConfiguration configuration) {
        Map<String, ResourceIndex<?>> extensionMap = config.extensionMap;
        DocumentResourceIndexInMemory di=new DocumentResourceIndexInMemory();
        extensionMap.put(DocumentResource.getResourceKind(), di);
        extensionMap.put(TemplateResource.getResourceKind(), 	new TemplateResourceIndexInMemory(di,TemplateResourceIndexInMemory.factory));
        return extensionMap;
    }


    protected Map<String, ResourceIndex<?>> initRedis(ServiceUtilsConfig config, StorageConfiguration configuration) {
        Map<String, ResourceIndex<?>> extensionMap = config.extensionMap;
        RedisDocumentResourceIndex di=new RedisDocumentResourceIndex(configuration.redis_host, configuration.redis_port);
        extensionMap.put(DocumentResource.getResourceKind(), di);
        extensionMap.put(TemplateResource.getResourceKind(), new RedisTemplateResourceIndex(di,RedisTemplateResourceIndex.factory));
        return extensionMap;

    }

}
