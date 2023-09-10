package org.openprovenance.prov.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.core.xml.serialization.ProvSerialiser;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.redis.validation.RedisValidationResourceIndex;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.service.core.memory.DocumentResourceIndexInMemory;
import org.openprovenance.prov.service.translation.ActionExpand;
import org.openprovenance.prov.service.translation.filesystem.BindingsResourceStorageFileSystem;
import org.openprovenance.prov.service.translation.memory.TemplateResourceIndexInMemory;
import org.openprovenance.prov.service.validation.ActionValidate;
import org.openprovenance.prov.service.validation.ValidationResource;
import org.openprovenance.prov.service.validation.memory.ValidationResourceIndexInMemory;
import org.openprovenance.prov.service.store.ValidationReportWrapper;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.api.TemplateResource;
import org.openprovenance.prov.storage.filesystem.DocumentResourceStorageFileSystem;
import org.openprovenance.prov.storage.filesystem.NonDocumentGenericResourceStorageFileSystem;
import org.openprovenance.prov.storage.mongodb.*;
import org.openprovenance.prov.storage.redis.RedisDocumentResourceIndex;
import org.openprovenance.prov.storage.redis.RedisTemplateResourceIndex;
import org.openprovenance.prov.validation.report.ValidationReport;
import org.openprovenance.prov.validation.report.json.Mapper;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.openprovenance.prov.service.core.ServiceUtils.UPLOADED_FILE_PATH;

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
        config.put(PSERVICE_DEL_PERIOD, "60");
        config.put(PSERVICE_REDIS_HOST, "localhost");
        config.put(PSERVICE_REDIS_PORT, "6379");
        config.put(PSERVICE_MONGO_HOST, "mongo");
        return config;
    }

    public Map<String,String> loadConfigFromEnvironment(Map<String,String> defaultConfiguration) {
        Map<String,String> config=new HashMap<>();
        for (String variable: defaultConfiguration.keySet()) {
            String value=System.getProperty(variable,null);
            if (value!=null) {
                config.put(variable,value);
                logger.info("Configuration: system properties --- " + variable + " " + value);
            }
        }
        return config;
    }

    public ServiceUtilsConfig makeConfig(ProvFactory factory) {
        Map<String,String> config=new HashMap<>();
        config.putAll(defaultConfiguration);
        config.putAll(loadConfigFromEnvironment(defaultConfiguration));

        return makeConfig(factory,config);
    }



    public ServiceUtilsConfig makeConfig(ProvFactory factory, Map<String,String> configuration) {
        ServiceUtilsConfig utilsConfig = new ServiceUtilsConfig();
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

        ObjectMapper mapper= Mapper.getValidationReportMapper();
        mapper.registerModule(org.mongojack.internal.MongoJackModule.INSTANCE);

        MongoGenericResourceStorage<ValidationReport> reportStorage= new MongoGenericResourceStorage<>(
                mongoHost,
                mongoDbName,
                ActionValidate.REPORT_KEY,
                mapper,
                ValidationReport.class,
                ValidationReportWrapper::new);

        utilsConfig.genericResourceStorageMap.put(ActionValidate.REPORT_KEY, reportStorage);

        MongoAsciiBlobStorage matrixStorage= new MongoAsciiBlobStorage(mongoHost, mongoDbName, ActionValidate.MATRIX_KEY);
        utilsConfig.genericResourceStorageMap.put(ActionValidate.MATRIX_KEY, matrixStorage);


        ProvSerialiser serial=new ProvSerialiser();
        utilsConfig.serialiser=(OutputStream out, Object document, String mediaType, boolean formatted) -> serial.serialiseDocument(out,document,false, false);

        return utilsConfig;
    }

    File path=new File(UPLOADED_FILE_PATH); //FIXME, integrate with configuration?


    public ServiceUtilsConfig withFileSystem(ServiceUtilsConfig utilsConfig, ProvFactory factory, Map<String,String> configuration) {
        utilsConfig.storageManager=new DocumentResourceStorageFileSystem(factory, path);

        ProvSerialiser serial = new ProvSerialiser();
        utilsConfig.serialiser=(OutputStream out, Object document, String mediaType, boolean formatted) -> serial.serialiseDocument(out,document,false, false);

        utilsConfig.genericResourceStorageMap.put(ActionExpand.BINDINGS_KEY,new BindingsResourceStorageFileSystem(new ObjectMapper()));
        utilsConfig.genericResourceStorageMap.put(ActionValidate.REPORT_KEY,new NonDocumentGenericResourceStorageFileSystem<>(Mapper.getValidationReportMapper(), ValidationReport.class, path));
        utilsConfig.genericResourceStorageMap.put(ActionValidate.MATRIX_KEY,new NonDocumentGenericResourceStorageFileSystem<>(new ObjectMapper(), Object.class, path));

        return utilsConfig;
    }


    public void initInMemory(ServiceUtilsConfig config, Map<String, String> configuration) {

        Consumer<Map<String, ResourceIndex<?>>> inMemoryInit = extensionMap -> {
            DocumentResourceIndexInMemory di=new DocumentResourceIndexInMemory();
            extensionMap.put(DocumentResource.getResourceKind(), di);
            extensionMap.put(TemplateResource.getResourceKind(), 	new TemplateResourceIndexInMemory(di,TemplateResourceIndexInMemory.factory));
            extensionMap.put(ValidationResource.getResourceKind(),  new ValidationResourceIndexInMemory(di, ValidationResourceIndexInMemory.factory));
        };
        inMemoryInit.accept(config.extensionMap);
    }


    public void initRedis(ServiceUtilsConfig config, Map<String, String> configuration) {
        Consumer<Map<String, ResourceIndex<?>>> redisInit = extensionMap -> {
            RedisDocumentResourceIndex di=new RedisDocumentResourceIndex(configuration.get(PSERVICE_REDIS_HOST), Integer.valueOf(configuration.get(PSERVICE_REDIS_PORT)));
            extensionMap.put(DocumentResource.getResourceKind(), di);
            extensionMap.put(TemplateResource.getResourceKind(),   new RedisTemplateResourceIndex(di,RedisTemplateResourceIndex.factory));
            extensionMap.put(ValidationResource.getResourceKind(), new RedisValidationResourceIndex(di,RedisValidationResourceIndex.factory));
        };
        redisInit.accept(config.extensionMap);
    }

}
