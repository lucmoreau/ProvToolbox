package org.openprovenance.prov.service.validation.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongojack.internal.MongoJackModule;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.service.core.config.StorageConfiguration;
import org.openprovenance.prov.service.core.memory.DocumentResourceIndexInMemory;
import org.openprovenance.prov.service.validation.ActionValidate;
import org.openprovenance.prov.service.validation.ValidationResource;
import org.openprovenance.prov.service.validation.memory.ValidationResourceIndexInMemory;
import org.openprovenance.prov.service.validation.redis.RedisValidationResourceIndex;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.filesystem.NonDocumentGenericResourceStorageFileSystem;
import org.openprovenance.prov.storage.mongodb.*;
import org.openprovenance.prov.storage.redis.RedisDocumentResourceIndex;
import org.openprovenance.prov.validation.report.ValidationReport;
import org.openprovenance.prov.validation.report.json.Mapper;

import java.io.File;
import java.util.Map;

public class StorageSetup extends org.openprovenance.prov.service.translation.storage.StorageSetup {

    private final static Logger logger = LogManager.getLogger(StorageSetup.class);

    @Override
    protected ServiceUtilsConfig withMongoDb(ServiceUtilsConfig utilsConfig2, ProvFactory factory, StorageConfiguration configuration) {
        ServiceUtilsConfig utilsConfig=super.withMongoDb(utilsConfig2, factory,configuration);

        // extends configuration with this service storage requirements

        ObjectMapper mapper= Mapper.getValidationReportMapper();
        mapper.registerModule(MongoJackModule.DEFAULT_MODULE_INSTANCE);

        final String mongoHost = configuration.mongo_host;
        final String mongoDbName = configuration.dbname;

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

        return utilsConfig;
    }



    @Override
    protected ServiceUtilsConfig withFileSystem(ServiceUtilsConfig utilsConfig2, ProvFactory factory, StorageConfiguration configuration) {
        ServiceUtilsConfig utilsConfig=super.withFileSystem(utilsConfig2, factory, configuration);

        utilsConfig.genericResourceStorageMap.put(ActionValidate.REPORT_KEY,new NonDocumentGenericResourceStorageFileSystem<>(Mapper.getValidationReportMapper(), ValidationReport.class, new File(configuration.uploaded_filepath)));
        utilsConfig.genericResourceStorageMap.put(ActionValidate.MATRIX_KEY,new NonDocumentGenericResourceStorageFileSystem<>(new ObjectMapper(), Object.class, new File(configuration.uploaded_filepath)));

        return utilsConfig;
    }


    @Override
    protected Map<String, ResourceIndex<?>> initInMemory(ServiceUtilsConfig config, StorageConfiguration configuration) {
        Map<String, ResourceIndex<?>> extensionMap = super.initInMemory(config, configuration);
        ResourceIndex<?> di=extensionMap.get(DocumentResource.getResourceKind());
        extensionMap.put(ValidationResource.getResourceKind(),  new ValidationResourceIndexInMemory((DocumentResourceIndexInMemory)di, ValidationResourceIndexInMemory.factory));
        return extensionMap;
    }


    @Override
    protected Map<String, ResourceIndex<?>> initRedis(ServiceUtilsConfig config, StorageConfiguration configuration) {
        Map<String, ResourceIndex<?>> extensionMap = super.initRedis(config, configuration);
        ResourceIndex<?> di=extensionMap.get(DocumentResource.getResourceKind());
        extensionMap.put(ValidationResource.getResourceKind(), new RedisValidationResourceIndex((RedisDocumentResourceIndex) di,RedisValidationResourceIndex.factory));
        return extensionMap;
    }

}
