package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.mongojack.DBUpdate;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.openprovenance.prov.service.core.NonDocumentGenericResourceStorage;
import org.openprovenance.prov.template.expander.BindingsJson.BindingsBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MongoBindingsResourceStorage implements NonDocumentGenericResourceStorage<BindingsBean>, Constants {


    private final DB db;

    private static Logger logger = Logger.getLogger(MongoDocumentResourceStorage.class);

    private final JacksonDBCollection<BindingsWrapper, String> bindingsCollection;

    private final ObjectMapper mapper;

    MongoBindingsResourceStorage(String dbname, ObjectMapper mapper) {

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB(dbname);
        this.db=db;

        DBCollection collection=db.getCollection(COLLECTION_BINDINGS);
        mapper.registerModule(org.mongojack.internal.MongoJackModule.INSTANCE);
        JacksonDBCollection<BindingsWrapper, String> bindingsCollection = JacksonDBCollection.wrap(collection, BindingsWrapper.class, String.class, mapper);
        this.bindingsCollection =bindingsCollection;
        this.mapper=mapper;
    }

    @Override
    public String newStore(String _suggestedExtension, String _mimeType) throws IOException {
        BindingsWrapper bw=new BindingsWrapper();
        WriteResult<BindingsWrapper, String> result= bindingsCollection.insert(bw);
        String id = result.getSavedId();
        return id;
    }

    @Override
    public void copyInputStreamToStore(InputStream inputStream, String id) throws IOException {
        throw new UnsupportedOperationException("yet");
    }

    @Override
    public void copyStringToStore(CharSequence str, String id) throws IOException {
        throw new UnsupportedOperationException("yet");
    }

    @Override
    public void serializeObjectToStore(BindingsBean o, String id) throws IOException {
        bindingsCollection.updateById(id, DBUpdate.set("bindings", o));
    }

    @Override
    public void copyStoreToOutputStream(String id, OutputStream outputStream) throws IOException {
        throw new UnsupportedOperationException("yet");
    }

    @Override
    public BindingsBean deserializeObjectFromStore(String id) throws IOException {
        BindingsWrapper wrapper= bindingsCollection.findOneById(id);
        return wrapper.bindings;
    }


    @Override
    public boolean delete(String storageId) {
        return false;
    }
}
