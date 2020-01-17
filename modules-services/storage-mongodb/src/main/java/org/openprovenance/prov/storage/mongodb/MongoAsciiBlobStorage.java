package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.mongojack.DBUpdate;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.openprovenance.prov.service.core.NonDocumentGenericResourceStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;


public class MongoAsciiBlobStorage implements NonDocumentGenericResourceStorage<String>, Constants {


    private final DB db;

    private static Logger logger = Logger.getLogger(MongoDocumentResourceStorage.class);

    private final JacksonDBCollection<AsciiWrapper, String> genericCollection;

    private final String collectionName;

    public MongoAsciiBlobStorage(String dbname, String collectionName) {

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB(dbname);
        this.db=db;
        this.collectionName=collectionName;

        DBCollection generic=db.getCollection(collectionName);
        this.genericCollection = JacksonDBCollection.wrap(generic, AsciiWrapper.class, String.class);
    }

    @Override
    public String newStore(String _suggestedExtension, String _mimeType) {
        WriteResult<AsciiWrapper, String> result= genericCollection.insert(new AsciiWrapper());
        String id = result.getSavedId();
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
        logger.info("serializeObjectToStore " + id);
        logger.info("serializeObjectToStore " + o);
        genericCollection.updateById(id, DBUpdate.set(TypeWrapper.VALUE, o));
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
        return false;
    }
}
