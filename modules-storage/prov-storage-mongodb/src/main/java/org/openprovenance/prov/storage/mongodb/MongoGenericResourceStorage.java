package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.mongojack.DBUpdate;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.openprovenance.prov.storage.api.NonDocumentGenericResourceStorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;


public class MongoGenericResourceStorage<TYPE> implements NonDocumentGenericResourceStorage<TYPE>, Constants {


    private final DB db;

    private static Logger logger = Logger.getLogger(MongoGenericResourceStorage.class);

    private final JacksonDBCollection<TypeWrapper<TYPE>, String> genericCollection;

    private final ObjectMapper mapper;
    private final Class<TYPE> cl;
    private final Supplier<TypeWrapper<TYPE>> wmake;
    private final String collectionName;

    public MongoGenericResourceStorage(String dbname, String collectionName, ObjectMapper mapper, Class<TYPE> cl, Supplier<TypeWrapper<TYPE>> wmake) {
        this("localhost",dbname,collectionName,mapper,cl,wmake);
    }

    public MongoGenericResourceStorage(String host, String dbname, String collectionName, ObjectMapper mapper, Class<TYPE> cl, Supplier<TypeWrapper<TYPE>> wmake) {

        MongoClient mongoClient = new MongoClient(host, 27017);
        DB db = mongoClient.getDB(dbname);
        this.db=db;

        this.cl=cl;
        this.wmake=wmake;
        this.collectionName=collectionName;
        TypeWrapper<TYPE> instance=wmake.get();
        Class<TypeWrapper<TYPE>> wrapperClass = (Class<TypeWrapper<TYPE>>) instance.getClass();

        DBCollection generic=db.getCollection(collectionName);
        mapper.registerModule(org.mongojack.internal.MongoJackModule.INSTANCE);
        this.genericCollection = JacksonDBCollection.wrap(generic, wrapperClass, String.class, mapper);
        this.mapper=mapper;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public String newStore(String _suggestedExtension, String _mimeType) {
        TypeWrapper<TYPE> gw= wmake.get();
        WriteResult<TypeWrapper<TYPE>, String> result= genericCollection.insert(gw);
        String id = result.getSavedId();
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
        genericCollection.updateById(id, DBUpdate.set(TypeWrapper.VALUE, o));
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
        WriteResult<TypeWrapper<TYPE>, String> result=genericCollection.removeById(storageId);
        return result.getN()==1;
    }
}
