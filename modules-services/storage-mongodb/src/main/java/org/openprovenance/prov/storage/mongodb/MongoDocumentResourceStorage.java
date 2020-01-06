package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.mongojack.DBUpdate;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.service.core.ResourceStorage;

import java.io.IOException;
import java.io.InputStream;

public class MongoDocumentResourceStorage implements ResourceStorage, Constants {
    private static Logger logger = Logger.getLogger(MongoDocumentResourceStorage.class);

    private final DB db;
    private final JacksonDBCollection<DocumentWrapper, String> documentCollection;
    private final ProvSerialiser serialiser;
    private final ProvDeserialiser deserialiser;
    private final ObjectMapper mapper;

    MongoDocumentResourceStorage(String dbname) {

        //System.out.println("Creating a client");
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB(dbname);
        this.db=db;

        //mongoClient.getDatabaseNames().forEach(System.out::println);
        DBCollection collection=db.getCollection(COLLECTION_DOCUMENTS);
        //db.getCollectionNames().forEach(System.out::println);


        ProvSerialiser serialiser=new ProvSerialiser(false);
        this.mapper=new ObjectMapper();
        this.serialiser =serialiser;
        ProvDeserialiser deserialiser=new ProvDeserialiser();
        this.deserialiser=deserialiser;

        deserialiser.customize(mapper);
        serialiser.customize(mapper,false);

        mapper.registerModule(org.mongojack.internal.MongoJackModule.INSTANCE);

        JacksonDBCollection<DocumentWrapper, String> documentCollection = JacksonDBCollection.wrap(collection, DocumentWrapper.class, String.class, mapper);
        this.documentCollection=documentCollection;


    }
    @Override
    public String newStore(Formats.ProvFormat format) throws IOException {
        DocumentWrapper dw=new DocumentWrapper();
        WriteResult<DocumentWrapper, String> result=documentCollection.insert(dw);
        String id = result.getSavedId();
        return id;
    }

    @Override
    public void copyInputStreamToStore(InputStream inputStream, String id) throws IOException {

    }

    @Override
    public void copyStringToStore(CharSequence str, String id) throws IOException {

    }

    @Override
    public Document readDocument(String id, boolean known) throws IOException {
        return null;
    }

    @Override
    public Document readDocument(String id) throws IOException {
        DocumentWrapper wrapper=documentCollection.findOneById(id);
        return wrapper.document;
    }

    @Override
    public void writeDocument(String id, Formats.ProvFormat format, Document doc) throws IOException {
        documentCollection.updateById(id, DBUpdate.set("document", doc));
    }

    @Override
    public boolean delete(String storageId) {
        return false;
    }
}
