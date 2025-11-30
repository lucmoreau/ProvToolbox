package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.bson.types.ObjectId;
import org.openprovenance.prov.storage.api.NonDocumentResourceStorage;

import java.io.*;
/* Documentation
https://howtodoinjava.com/mongodb/java-mongodb-getsave-image-using-gridfs-apis/
*/


public class MongoNonDocumentResourceStorage implements NonDocumentResourceStorage, Constants {
    private static final Logger logger = LogManager.getLogger(MongoNonDocumentResourceStorage.class);

    private final MongoDatabase db;
    private final MongoCollection<BasicDBObject> collection;

    public MongoNonDocumentResourceStorage(String dbname) {
        this("localhost", dbname);
    }

    public MongoNonDocumentResourceStorage(String host, String dbname) {


        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://" + host + ":27017"));
        MongoDatabase db = mongoClient.getDatabase(dbname);

        this.db=db;
        MongoCollection<BasicDBObject> collection=db.getCollection(COLLECTION_FILES,BasicDBObject.class);

        this.collection=collection;
    }

    public MongoNonDocumentResourceStorage(MongoDatabase db) {
        this.db=db;
        MongoCollection<BasicDBObject> collection=db.getCollection(COLLECTION_FILES,BasicDBObject.class);
        this.collection=collection;
    }
    
    @Override
    public String newStore(String suggestedExtension, String mimeType) throws IOException {
        BasicDBObject document = new BasicDBObject();
        document.put(KEY_EXTENSION,suggestedExtension);
        document.put(KEY_MIME_TYPE,mimeType);
        collection.insertOne(document);
        ObjectId id = (ObjectId)document.get( "_id" );
        return id.toHexString();
    }

    @Override
    public void copyInputStreamToStore(InputStream inputStream, String id) throws IOException {
        logger.debug("copyInputStreamToStore: " + id);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        IOUtils.copy(inputStream,baos);
        copyStringToStore(baos.toString(),id);
    }

    public BasicDBObject findDocumentById(MongoCollection<BasicDBObject> collection, String id) {
        logger.debug("findDocumentById: " + id);

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        FindIterable<BasicDBObject> iter = collection.find(query);
        return iter.first();
    }

    @Override
    public void copyStringToStore(CharSequence str, String id) throws IOException {
        logger.debug("copyStringToStore: " + id);

        BasicDBObject query = new BasicDBObject();
        query.put("_id",new ObjectId(id));

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put(KEY_CONTENTS,str);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument);

        collection.updateOne(query, updateObject);
    }

    @Override
    public void serializeObjectToStore(ObjectMapper om, Object o, String id) throws IOException {
        throw new UnsupportedOperationException("yet!");
    }

    @Override
    public void copyStoreToOutputStream(String id, OutputStream outputStream) throws IOException {

        BasicDBObject query = new BasicDBObject();
        query.put("_id",new ObjectId(id));

        FindIterable<BasicDBObject> result=collection.find(query);
        String contents=(String) result.first().get(KEY_CONTENTS);

        new PrintStream(outputStream).print(contents);
    }

    @Override
    public <T> T deserializeObjectFromStore(ObjectMapper om, String id, Class<T> clazz) throws IOException {
        throw new UnsupportedOperationException("yet!");
    }

    @Override
    public boolean delete(String storageId) {

        BasicDBObject query = new BasicDBObject();
        query.put("_id",new ObjectId(storageId));

        DeleteResult res=collection.deleteOne(query);
        return res.getDeletedCount()==1;
    }
}
