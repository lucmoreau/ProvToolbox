package org.openprovenance.prov.storage.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;
import org.openprovenance.prov.storage.api.NonDocumentGenericResourceStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MongoAsciiBlobStorage implements NonDocumentGenericResourceStorage<String>, Constants {

    private static final Logger logger = LogManager.getLogger(MongoAsciiBlobStorage.class);

    private final MongoDatabase db;

    private final JacksonMongoCollection<AsciiWrapper> genericCollection;

    private final String collectionName;

    public MongoAsciiBlobStorage(String dbname, String collectionName) {
        this("localhost",dbname,collectionName);
    }

    public MongoAsciiBlobStorage(String host, String dbname, String collectionName) {

        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://" + host + ":27017"));
        MongoDatabase db = mongoClient.getDatabase(dbname);
        this.db=db;
        this.collectionName=collectionName;

        MongoCollection<AsciiWrapper> generic=db.getCollection(collectionName,AsciiWrapper.class);
        this.genericCollection = JacksonMongoCollection.builder().build(generic,AsciiWrapper.class, UuidRepresentation.STANDARD);
    }

    @Override
    public String newStore(String _suggestedExtension, String _mimeType) {
        AsciiWrapper asciiWrapper = new AsciiWrapper();
        genericCollection.insert(asciiWrapper);
        String id = asciiWrapper.getId();
        return id;
    }

    @Override
    public void copyInputStreamToStore(InputStream inputStream, String id) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        IOUtils.copy(inputStream,baos);
        serializeObjectToStore(baos.toString(),id);
    }

    @Override
    public void copyStringToStore(CharSequence str, String id) throws IOException {
        serializeObjectToStore(str.toString(),id);
    }

    @Override
    public void serializeObjectToStore(String o, String id) {
        logger.debug("serializeObjectToStore " + id);
        logger.debug("serializeObjectToStore " + o);
        genericCollection.updateById(id, Updates.set(TypeWrapper.VALUE, o));
    }

    @Override
    public void copyStoreToOutputStream(String id, OutputStream outputStream) throws IOException {
        String contents=deserializeObjectFromStore(id);
        outputStream.write(contents.getBytes());
    }

    @Override
    public String deserializeObjectFromStore(String id) {
        AsciiWrapper wrapper= genericCollection.findOneById(id);
        return wrapper.value;
    }

    @Override
    public boolean delete(String storageId) {
        DeleteResult result=genericCollection.removeById(storageId);
        return result.getDeletedCount()==1;
    }
}
