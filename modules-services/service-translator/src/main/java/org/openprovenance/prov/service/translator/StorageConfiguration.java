package org.openprovenance.prov.service.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.xml.serialization.ProvSerialiser;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.redis.RedisDocumentResourceIndex;
import org.openprovenance.prov.redis.RedisTemplateResourceIndex;
import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.ResourceIndex;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.service.core.filesystem.DocumentResourceStorageFileSystem;
import org.openprovenance.prov.service.core.filesystem.NonDocumentGenericResourceStorageFileSystem;
import org.openprovenance.prov.service.core.memory.DocumentResourceIndexInMemory;
import org.openprovenance.prov.service.translation.ActionExpand;
import org.openprovenance.prov.service.translation.TemplateResource;
import org.openprovenance.prov.service.translation.filesystem.BindingsResourceStorageFileSystem;
import org.openprovenance.prov.service.translation.memory.TemplateResourceIndexInMemory;

import java.io.OutputStream;
import java.util.Map;
import java.util.function.Consumer;

public class StorageConfiguration {



    public ServiceUtilsConfig makeFSConfig(ProvFactory factory) {
        ServiceUtilsConfig config=new ServiceUtilsConfig();
        //initRedis(config);
        initInMemory(config);
        final String DBNAME = "prov";
        config.documentCacheSize=200;
        config.storageManager=new DocumentResourceStorageFileSystem(factory);

        config.pFactory= factory;
        ProvSerialiser serial = new ProvSerialiser();
        config.serialiser=(OutputStream out, Object document, String mediaType, boolean formatted) -> serial.serialiseDocument(out,document,false, false);

        config.genericResourceStorageMap.put(ActionExpand.BINDINGS_KEY,new BindingsResourceStorageFileSystem(new ObjectMapper()));

        return config;
    }


    public void initInMemory(ServiceUtilsConfig config) {

        Consumer<Map<String, ResourceIndex<?>>> inMemoryInit = extensionMap -> {
            DocumentResourceIndexInMemory di=new DocumentResourceIndexInMemory();
            extensionMap.put(DocumentResource.getResourceKind(), di);
            extensionMap.put(TemplateResource.getResourceKind(), 	new TemplateResourceIndexInMemory(di, TemplateResourceIndexInMemory.factory));
        };
        inMemoryInit.accept(config.extensionMap);
    }


    public void initRedis(ServiceUtilsConfig config) {
        Consumer<Map<String, ResourceIndex<?>>> redisInit = extensionMap -> {
            RedisDocumentResourceIndex di=new RedisDocumentResourceIndex();
            extensionMap.put(DocumentResource.getResourceKind(), di);
            extensionMap.put(TemplateResource.getResourceKind(),   new RedisTemplateResourceIndex(di, RedisTemplateResourceIndex.factory));
        };
        redisInit.accept(config.extensionMap);
    }

}
