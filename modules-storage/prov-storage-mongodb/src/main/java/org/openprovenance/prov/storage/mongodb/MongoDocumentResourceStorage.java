package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
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
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.model.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.storage.api.ResourceStorage;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.TimeZone;

import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.JSONLD_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.getAttributes;

public class MongoDocumentResourceStorage implements ResourceStorage, Constants {
    private static final Logger logger = LogManager.getLogger(MongoDocumentResourceStorage.class);

    private final MongoDatabase db;
    private final JacksonMongoCollection<DocumentWrapper> documentCollection;
    private final ProvSerialiser serialiser;
    private final ProvDeserialiser deserialiser;

    public ObjectMapper getMapper() {
        return mapper;
    }

    private final ObjectMapper mapper;
    final ProvFactory factory = ProvFactory.getFactory();

    public MongoDocumentResourceStorage(String dbname) {
        this("localhost", dbname);
    }

    public MongoDocumentResourceStorage(String host, String dbname) {
        //System.out.println("Creating a client");

        com.mongodb.client.MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://" + host + ":27017"));
        MongoDatabase db = mongoClient.getDatabase(dbname);
        this.db=db;

        this.mapper=new ObjectMapper();
        mapper.registerModule(org.mongojack.internal.MongoJackModule.DEFAULT_MODULE_INSTANCE);

        this.serialiser=new ProvSerialiser(mapper, false);
        serialiser.customize(mapper);

        this.deserialiser=new ProvDeserialiser(mapper);

        MongoCollection<DocumentWrapper> collection=db.getCollection(COLLECTION_DOCUMENTS,DocumentWrapper.class);
        this.documentCollection = JacksonMongoCollection.builder().withObjectMapper(mapper).build(collection,DocumentWrapper.class, UuidRepresentation.STANDARD);

    }

    JacksonMongoCollection<DocumentWrapper> newDocumentCollection(DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(org.mongojack.internal.MongoJackModule.DEFAULT_MODULE_INSTANCE);

        // this call updates the mapper with custom deserialisers
        ProvDeserialiser deserialiser=new ProvDeserialiser(mapper, dateTimeOption, optionalTimeZone);

        MongoCollection<DocumentWrapper> collection=db.getCollection(COLLECTION_DOCUMENTS,DocumentWrapper.class);
        return JacksonMongoCollection.builder().withObjectMapper(mapper).build(collection,DocumentWrapper.class,UuidRepresentation.STANDARD);
    }


    @Override
    public String newStore(Formats.ProvFormat format) throws IOException {
        DocumentWrapper dw=new DocumentWrapper();
        documentCollection.insert(dw);
        String id = dw.getId();
        return id;
    }

    InteropFramework interop= new InteropFramework(factory);


    @Override
    public void copyInputStreamToStore(InputStream inputStream, Formats.ProvFormat format, String id) throws IOException {
        logger.debug("copyStrcopyInputStreamToStore  " + id);
        Document doc=interop.readDocument(inputStream, format);  //TODO: can we improve?
        if (!(doc instanceof org.openprovenance.prov.vanilla.Document)) {
            // if it was constructed with a different factory, convert to vanilla
            BeanTraversal bc=new BeanTraversal(factory, factory);
            doc=bc.doAction(doc);
        }
        writeDocument(id, doc, Formats.ProvFormat.PROVN);
    }

    @Override
    public void copyStringToStore(CharSequence str, Formats.ProvFormat format, String id) throws IOException {
        logger.debug("copyStringToStore " + id);
        InputStream stream = IOUtils.toInputStream(str, Charset.defaultCharset());
        copyInputStreamToStore(stream,format, id);
    }

    @Override
    public Document readDocument(String id, boolean known) {
        // HACK
        getAttributes().get().remove(JSONLD_CONTEXT_KEY_NAMESPACE);
        DocumentWrapper wrapper=documentCollection.findOneById(id);
        if (wrapper==null) {
            return null;
        }
        return wrapper.document;
    }

    @Override
    public Document readDocument(String id, boolean known, DateTimeOption dateTimeOption, TimeZone timeZone) {
        logger.warn("Check new support for dateTimeOption and timeZon " + dateTimeOption + " " + timeZone);
        System.out.println("Check new support for dateTimeOption and timeZone " + dateTimeOption + " " + timeZone);
        // HACK, global variable, prevents concurrren tuse
        getAttributes().get().remove(JSONLD_CONTEXT_KEY_NAMESPACE);
        return newDocumentCollection(dateTimeOption, timeZone).findOneById(id).document;
        //return readDocument(id, known);
    }

    @Override
    public Document readDocument(String id) {
        // HACK, global variable, prevents concurrren tuse
        getAttributes().get().remove(JSONLD_CONTEXT_KEY_NAMESPACE);
        DocumentWrapper wrapper=documentCollection.findOneById(id);
        return wrapper.document;
    }

    @Override
    public void writeDocument(String id, Document doc, Formats.ProvFormat format) {
        logger.debug("writeDocument " + id + " " + doc);
        documentCollection.updateById(id, Updates.set("document", doc));
    }

    @Override
    public boolean delete(String storageId) {
        DeleteResult result=documentCollection.removeById(storageId);
        return result.getDeletedCount()==1;
    }
}
