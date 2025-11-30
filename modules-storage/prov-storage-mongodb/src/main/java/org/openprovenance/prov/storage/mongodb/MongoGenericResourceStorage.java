package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;
import org.openprovenance.prov.storage.api.NonDocumentGenericResourceStorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;


public class MongoGenericResourceStorage<TYPE> implements NonDocumentGenericResourceStorage<TYPE>, Constants {


    private final MongoDatabase db;

    private static Logger logger = LogManager.getLogger(MongoGenericResourceStorage.class);

    private final JacksonMongoCollection<TypeWrapper<TYPE>> genericCollection;

    private final ObjectMapper mapper;
    private final Class<TYPE> cl;
    private final Supplier<TypeWrapper<TYPE>> wmake;
    private final String collectionName;

    public MongoGenericResourceStorage(String dbname, String collectionName, ObjectMapper mapper, Class<TYPE> cl, Supplier<TypeWrapper<TYPE>> wmake) {
        this("localhost",dbname,collectionName,mapper,cl,wmake);
    }

    public MongoGenericResourceStorage(String host, String dbname, String collectionName, ObjectMapper mapper, Class<TYPE> cl, Supplier<TypeWrapper<TYPE>> wmake) {


        com.mongodb.client.MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://" + host + ":27017"));
        MongoDatabase db = mongoClient.getDatabase(dbname);
        this.db=db;

        this.cl=cl;
        this.wmake=wmake;
        this.collectionName=collectionName;
        TypeWrapper<TYPE> instance=wmake.get();
        Class<TypeWrapper<TYPE>> wrapperClass = (Class<TypeWrapper<TYPE>>) instance.getClass();

        mapper.registerModule(org.mongojack.internal.MongoJackModule.DEFAULT_MODULE_INSTANCE);

        MongoCollection<TypeWrapper<TYPE>> generic=db.getCollection(collectionName,wrapperClass);
        this.genericCollection = JacksonMongoCollection.builder().withObjectMapper(mapper).build(generic,wrapperClass, UuidRepresentation.STANDARD);
        this.mapper=mapper;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public String newStore(String _suggestedExtension, String _mimeType) {
        TypeWrapper<TYPE> gw= wmake.get();
        genericCollection.insert(gw);
        String id = gw.getId();
        return id;
    }

    @Override
    public void copyInputStreamToStore(InputStream inputStream, String id) throws IOException {
        TYPE object=mapper.readValue(inputStream,cl);
        serializeObjectToStore(object,id);
    }

    @Override
    public void copyStringToStore(CharSequence str, String id) throws IOException {
        TYPE object=mapper.readValue(str.toString(),cl);
        serializeObjectToStore(object,id);
    }

    @Override
    public void serializeObjectToStore(TYPE o, String id) {
        logger.debug("serializeObjectToStore " + id);
        genericCollection.updateById(id, Updates.set(TypeWrapper.VALUE, o));
    }

    @Override
    public void copyStoreToOutputStream(String id, OutputStream outputStream) throws IOException {
        logger.debug("copyStoreToOutputStream: deserializeObjectFromStore " + id);
        TYPE object=deserializeObjectFromStore(id);
        logger.debug("copyStoreToOutputStream: writeValue  " + id);
        mapper.writeValue(outputStream,object);
    }

    @Override
    public TYPE deserializeObjectFromStore(String id) {
        TypeWrapper<TYPE> wrapper= genericCollection.findOneById(id);
        if (wrapper==null) return null;
        return wrapper.value;
    }

    @Override
    public boolean delete(String storageId) {
        DeleteResult result=genericCollection.removeById(storageId);
        return result.getDeletedCount()==1;
    }
}
