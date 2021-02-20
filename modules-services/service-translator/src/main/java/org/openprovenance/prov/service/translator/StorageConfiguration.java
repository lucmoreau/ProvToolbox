package org.openprovenance.prov.service.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.core.xml.serialization.ProvSerialiser;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.service.core.memory.DocumentResourceIndexInMemory;
import org.openprovenance.prov.service.translation.ActionExpand;
import org.openprovenance.prov.service.translation.filesystem.BindingsResourceStorageFileSystem;
import org.openprovenance.prov.service.translation.memory.TemplateResourceIndexInMemory;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.api.TemplateResource;
import org.openprovenance.prov.storage.filesystem.DocumentResourceStorageFileSystem;
import org.openprovenance.prov.storage.redis.RedisDocumentResourceIndex;
import org.openprovenance.prov.storage.redis.RedisTemplateResourceIndex;

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
        config.put(PSERVICE_STORAGE,    "fs");
        config.put(PSERVICE_CACHE,      "200");
        config.put(PSERVICE_AUTODELETE, "true");
        config.put(PSERVICE_DEL_PERIOD, "600");
        config.put(PSERVICE_REDIS_HOST, "localhost");
        config.put(PSERVICE_REDIS_PORT, "6379");
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
                //Db(utilsConfig,factory,configuration);
                throw new UnsupportedOperationException();
               // break;
            case "fs":
                withFileSystem(utilsConfig,factory,configuration);
                break;
            default: // do nothing
        }

        return utilsConfig;
    }

    /*
    public ServiceUtilsConfig withMongoDb(ServiceUtilsConfig utilsConfig, ProvFactory factory, Map<String,String> configuration) {

        utilsConfig.storageManager=new MongoDocumentResourceStorage(configuration.get(PSERVICE_DBNAME));
        utilsConfig.nonDocumentResourceStorage=new MongoNonDocumentResourceStorage(configuration.get(PSERVICE_DBNAME));
        utilsConfig.genericResourceStorageMap.put(ActionExpand.BINDINGS_KEY,new MongoBindingsResourceStorage(configuration.get(PSERVICE_DBNAME), new ObjectMapper()));

        ObjectMapper mapper= Mapper.getValidationReportMapper();
        mapper.registerModule(org.mongojack.internal.MongoJackModule.INSTANCE);

        MongoGenericResourceStorage<ValidationReport> reportStorage= new MongoGenericResourceStorage<>(
                configuration.get(PSERVICE_DBNAME),
                ActionValidate.REPORT_KEY,
                mapper,
                ValidationReport.class,
                ValidationReportWrapper::new);

        utilsConfig.genericResourceStorageMap.put(ActionValidate.REPORT_KEY, reportStorage);

        MongoAsciiBlobStorage matrixStorage= new MongoAsciiBlobStorage(configuration.get(PSERVICE_DBNAME), ActionValidate.MATRIX_KEY);
        utilsConfig.genericResourceStorageMap.put(ActionValidate.MATRIX_KEY, matrixStorage);


        ProvSerialiser serial=new ProvSerialiser();
        utilsConfig.serialiser=(OutputStream out, Object document, String mediaType, boolean formatted) -> serial.serialiseDocument(out,document,false, false);

        return utilsConfig;
    }

     */

    File path=new File(UPLOADED_FILE_PATH); //FIXME, integrate with configuration?


    public ServiceUtilsConfig withFileSystem(ServiceUtilsConfig utilsConfig, ProvFactory factory, Map<String,String> configuration) {


        utilsConfig.storageManager=new DocumentResourceStorageFileSystem(factory, path);

        ProvSerialiser serial = new ProvSerialiser();
        utilsConfig.serialiser=(OutputStream out, Object document, String mediaType, boolean formatted) -> serial.serialiseDocument(out,document,false, false);

        utilsConfig.genericResourceStorageMap.put(ActionExpand.BINDINGS_KEY,new BindingsResourceStorageFileSystem(new ObjectMapper()));

        return utilsConfig;
    }


    public void initInMemory(ServiceUtilsConfig config, Map<String, String> configuration) {

        Consumer<Map<String, ResourceIndex<?>>> inMemoryInit = extensionMap -> {
            DocumentResourceIndexInMemory di=new DocumentResourceIndexInMemory();
            extensionMap.put(DocumentResource.getResourceKind(), di);
            extensionMap.put(TemplateResource.getResourceKind(), 	new TemplateResourceIndexInMemory(di, TemplateResourceIndexInMemory.factory));
        };
        inMemoryInit.accept(config.extensionMap);
    }


    public void initRedis(ServiceUtilsConfig config, Map<String, String> configuration) {
        Consumer<Map<String, ResourceIndex<?>>> redisInit = extensionMap -> {
            RedisDocumentResourceIndex di=new RedisDocumentResourceIndex(configuration.get(PSERVICE_REDIS_HOST), Integer.valueOf(configuration.get(PSERVICE_REDIS_PORT)));
            extensionMap.put(DocumentResource.getResourceKind(), di);
            extensionMap.put(TemplateResource.getResourceKind(),   new RedisTemplateResourceIndex(di, RedisTemplateResourceIndex.factory));
        };
        redisInit.accept(config.extensionMap);
    }

}
