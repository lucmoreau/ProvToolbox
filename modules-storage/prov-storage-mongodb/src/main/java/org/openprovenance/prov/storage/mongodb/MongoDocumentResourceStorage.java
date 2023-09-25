package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.mongojack.DBUpdate;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.interop.Formats;
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
        this("localhost", dbname);
    }

    public MongoDocumentResourceStorage(String host, String dbname) {
        //System.out.println("Creating a client");
        MongoClient mongoClient = new MongoClient(host, 27017);
        DB db = mongoClient.getDB(dbname);
        this.db=db;

        DBCollection collection=db.getCollection(COLLECTION_DOCUMENTS);

        this.mapper=new ObjectMapper();
        mapper.registerModule(org.mongojack.internal.MongoJackModule.INSTANCE);

        this.serialiser=new ProvSerialiser(mapper, false);
        serialiser.customize(mapper);

        this.deserialiser=new ProvDeserialiser(mapper);


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
    public Document readDocument(String id, boolean known, DateTimeOption dateTimeOption, TimeZone timeZone) throws IOException {
        logger.warn("No support fro dateTimeOption and timeZone");
        System.out.println("No support for dateTimeOption and timeZone");
        logger.warn("No support for dateTimeOption and timeZone");
        return readDocument(id, known);
    }

    @Override
    public Document readDocument(String id) throws IOException {
        // HACK, global variable, prevents concurrren tuse
        getAttributes().get().remove(JSONLD_CONTEXT_KEY_NAMESPACE);
        DocumentWrapper wrapper=documentCollection.findOneById(id);
        return wrapper.document;
    }

    @Override
    public void writeDocument(String id, Formats.ProvFormat format, Document doc) throws IOException {
        logger.debug("writeDocument " + id + " " + doc);
        documentCollection.updateById(id, DBUpdate.set("document", doc));
    }

    @Override
    public boolean delete(String storageId) {
        WriteResult<DocumentWrapper, String> result=documentCollection.removeById(storageId);
        return result.getN()==1;
    }
}
