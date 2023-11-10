package org.openprovenance.prov.service.translation.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.core.xml.serialization.ProvSerialiser;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
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
import java.util.HashMap;
import java.util.Map;

import static org.openprovenance.prov.service.core.ServiceUtils.*;

public class StorageConfiguration implements EnvironmentVariables{

    static Logger logger = LogManager.getLogger(StorageConfiguration.class);

    public Map<String,String> defaultConfiguration=theDefaultConfiguration();

    public Map<String,String> theDefaultConfiguration() {
        Map<String,String> config=new HashMap<>();
        config.put(PSERVICE_DBNAME,     "prov");
        config.put(PSERVICE_INDEX,      "redis");
        config.put(PSERVICE_STORAGE,    "mongodb");
        config.put(PSERVICE_CACHE,      "200");
        config.put(PSERVICE_AUTODELETE, "true");
        config.put(PSERVICE_DEL_PERIOD, "1200");

        config.put(PSERVICE_REDIS_HOST, notNullDefault(System.getProperty(REDISSERVER_HOST),"localhost"));
        config.put(PSERVICE_REDIS_PORT, notNullDefault(System.getProperty(REDISSERVER_PORT), "6379"));
        config.put(PSERVICE_MONGO_HOST, notNullDefault(System.getProperty(MONGOSERVER_HOST),"localhost"));

        return config;
    }


    static String notNullDefault(String value, String default_value) {
        System.out.println("value: " + value + ", default: " + default_value);
        return (value==null)? default_value : value;
    }


    public ServiceUtilsConfig makeConfig(ProvFactory factory) {
        Map<String,String> config=new HashMap<>();
        config.putAll(defaultConfiguration);
        config.putAll(loadConfigFromSystem(defaultConfiguration));
        config.putAll(loadConfigFromEnvironment(defaultConfiguration));

        return makeConfig(factory,config);
    }



    public ServiceUtilsConfig makeConfig(ProvFactory factory, Map<String,String> configuration) {
        ServiceUtilsConfig utilsConfig = new ServiceUtilsConfig(configuration);
        utilsConfig.pFactory=factory;
        logger.info("Configuration --- " + configuration);
        switch (configuration.get(PSERVICE_INDEX)) {
            case "redis":
                initRedis(utilsConfig, configuration);
                break;
            case "memory":
                initInMemory(utilsConfig, configuration);
                break;
            default: // do nothing
        }
        utilsConfig.documentCacheSize = Integer.parseInt(configuration.get(PSERVICE_CACHE));
        utilsConfig.autoDelete= Boolean.parseBoolean(configuration.get(PSERVICE_AUTODELETE));
        utilsConfig.deletePeriod= Integer.parseInt(configuration.get(PSERVICE_DEL_PERIOD));
        switch (configuration.get(PSERVICE_STORAGE)) {
            case "mongodb":
                withMongoDb(utilsConfig,factory,configuration);
                break;
            case "fs":
                withFileSystem(utilsConfig,factory,configuration);
                break;
            default: // do nothing
        }

        return utilsConfig;
    }

    public ServiceUtilsConfig withMongoDb(ServiceUtilsConfig utilsConfig, ProvFactory factory, Map<String,String> configuration) {

        final String mongoHost = configuration.get(PSERVICE_MONGO_HOST);
        final String mongoDbName = configuration.get(PSERVICE_DBNAME);
        utilsConfig.storageManager=new MongoDocumentResourceStorage(mongoHost, mongoDbName);
        utilsConfig.nonDocumentResourceStorage=new MongoNonDocumentResourceStorage(mongoHost, mongoDbName);
        utilsConfig.genericResourceStorageMap.put(ActionExpand.BINDINGS_KEY,new MongoBindingsResourceStorage(mongoHost, mongoDbName, new ObjectMapper()));



        ProvSerialiser serial=new ProvSerialiser();
        utilsConfig.serialiser=(OutputStream out, Object document, String mediaType, boolean formatted) -> serial.serialiseOtherObject(out, document,false, false);

        return utilsConfig;
    }

    protected File path=new File(UPLOADED_FILE_PATH); //FIXME, integrate with configuration?


    public ServiceUtilsConfig withFileSystem(ServiceUtilsConfig utilsConfig, ProvFactory factory, Map<String,String> configuration) {
        utilsConfig.storageManager=new DocumentResourceStorageFileSystem(factory, path);

        ProvSerialiser serial = new ProvSerialiser();
        utilsConfig.serialiser=(OutputStream out, Object document, String mediaType, boolean formatted) -> serial.serialiseOtherObject(out, document,false, false);

        utilsConfig.genericResourceStorageMap.put(ActionExpand.BINDINGS_KEY,new BindingsResourceStorageFileSystem(new ObjectMapper()));

        return utilsConfig;
    }


    public Map<String, ResourceIndex<?>> initInMemory(ServiceUtilsConfig config, Map<String, String> configuration) {
        Map<String, ResourceIndex<?>> extensionMap = config.extensionMap;
        DocumentResourceIndexInMemory di=new DocumentResourceIndexInMemory();
        extensionMap.put(DocumentResource.getResourceKind(), di);
        extensionMap.put(TemplateResource.getResourceKind(), 	new TemplateResourceIndexInMemory(di,TemplateResourceIndexInMemory.factory));
        return extensionMap;
    }


    public Map<String, ResourceIndex<?>> initRedis(ServiceUtilsConfig config, Map<String, String> configuration) {
        Map<String, ResourceIndex<?>> extensionMap = config.extensionMap;

        RedisDocumentResourceIndex di=new RedisDocumentResourceIndex(configuration.get(PSERVICE_REDIS_HOST), Integer.parseInt(configuration.get(PSERVICE_REDIS_PORT)));
        extensionMap.put(DocumentResource.getResourceKind(), di);
        extensionMap.put(TemplateResource.getResourceKind(), new RedisTemplateResourceIndex(di,RedisTemplateResourceIndex.factory));
        return extensionMap;

    }

}
