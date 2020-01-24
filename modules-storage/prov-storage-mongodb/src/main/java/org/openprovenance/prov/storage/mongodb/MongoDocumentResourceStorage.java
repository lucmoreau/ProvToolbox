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
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.storage.api.ResourceStorage;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class MongoDocumentResourceStorage implements ResourceStorage, Constants {
    private static Logger logger = Logger.getLogger(MongoDocumentResourceStorage.class);

    private final DB db;
    private final JacksonDBCollection<DocumentWrapper, String> documentCollection;
    private final ProvSerialiser serialiser;
    private final ProvDeserialiser deserialiser;

    public ObjectMapper getMapper() {
        return mapper;
    }

    private final ObjectMapper mapper;
    final ProvFactory factory = ProvFactory.getFactory();

    public MongoDocumentResourceStorage(String dbname) {

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

    InteropFramework interop= new InteropFramework(factory);


    @Override
    public void copyInputStreamToStore(InputStream inputStream, Formats.ProvFormat format, String id) throws IOException {
        logger.debug("copyStrcopyInputStreamToStore  " + id);
        Document doc=interop.readDocument(inputStream, format, "");  //TODO: can we improve?
        if (!(doc instanceof org.openprovenance.prov.vanilla.Document)) {
            // if it was constructed with a different factory, convert to vanilla
            BeanTraversal bc=new BeanTraversal(factory, factory);
            doc=bc.doAction(doc);
        }
        writeDocument(id, Formats.ProvFormat.PROVN,doc);
    }

    @Override
    public void copyStringToStore(CharSequence str, Formats.ProvFormat format, String id) throws IOException {
        logger.debug("copyStringToStore " + id);
        InputStream stream = IOUtils.toInputStream(str, Charset.defaultCharset());
        copyInputStreamToStore(stream,format, id);
    }

    @Override
    public Document readDocument(String id, boolean known) throws IOException {
        DocumentWrapper wrapper=documentCollection.findOneById(id);
        return wrapper.document;
    }

    @Override
    public Document readDocument(String id) throws IOException {
        DocumentWrapper wrapper=documentCollection.findOneById(id);
        return wrapper.document;
    }

    @Override
    public void writeDocument(String id, Formats.ProvFormat format, Document doc) throws IOException {
        logger.debug("writeDocument " + id);
        documentCollection.updateById(id, DBUpdate.set("document", doc));
    }

    @Override
    public boolean delete(String storageId) {
        WriteResult<DocumentWrapper, String> result=documentCollection.removeById(storageId);
        return result.getN()==1;
    }
}
