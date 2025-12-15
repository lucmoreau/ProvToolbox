package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;
import org.openprovenance.apache.commons.lang.StringEscapeUtils;
import org.openprovenance.prov.storage.api.NonDocumentGenericResourceStorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class MongoCSVResourceStorage implements NonDocumentGenericResourceStorage<Collection<CSVRecord>>, Constants {
    private static final Logger logger = LogManager.getLogger(MongoCSVResourceStorage.class);


    public static final String CSV_ENTRY = "csv";
    private final MongoDatabase db;


    private final JacksonMongoCollection<CSVWrapper> csvCollection;

    private final ObjectMapper mapper;

    public MongoCSVResourceStorage(String dbname, ObjectMapper mapper) {
        this("localhost",dbname,mapper);
    }
    public MongoCSVResourceStorage(String host, String dbname, ObjectMapper mapper) {

        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://" + host + ":27017"));
        MongoDatabase db = mongoClient.getDatabase(dbname);
        mapper.registerModule(org.mongojack.internal.MongoJackModule.DEFAULT_MODULE_INSTANCE);
        MongoCollection<CSVWrapper> generic=db.getCollection(COLLECTION_CSV,CSVWrapper.class);
        this.csvCollection = JacksonMongoCollection.builder().withObjectMapper(mapper).build(generic,CSVWrapper.class, UuidRepresentation.STANDARD);
        this.mapper=mapper;
        this.db=db;
    }

    @Override
    public String newStore(String _suggestedExtension, String _mimeType) throws IOException {
        CSVWrapper cw=new CSVWrapper();
        csvCollection.insertOne(cw);
        String id = cw.getId();
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
    public void serializeObjectToStore(Collection<CSVRecord> coll, String id) throws IOException {
        List<List<String>> result=new ArrayList<>();
        for (CSVRecord rec: coll) {
            List<String> ll = toList(rec);
            result.add(ll);
        }
        List<?>[] array=result.toArray(new List<?> [0]);

        csvCollection.updateById(id, Updates.set(CSV_ENTRY, array));
    }

    public void serializeObjectToStore(Iterator<CSVRecord> iterator, String id) throws IOException {
        List<List<String>> result=new ArrayList<>();
        for (Iterator<CSVRecord> it = iterator; it.hasNext(); ) {
            CSVRecord rec = it.next();
            List<String> ll = toList(rec);
            result.add(ll);
        }
        List<?>[] array=result.toArray(new List<?> [0]);

        csvCollection.updateById(id, Updates.set(CSV_ENTRY, array));
    }

    public static List<String> toList(CSVRecord rec) {
        List<String> ll=new ArrayList<>();
        for (Iterator<String> it = rec.iterator(); it.hasNext(); ) {
            String entry = it.next();
            ll.add(entry);
        }
        return ll;
    }

    @Override
    public void copyStoreToOutputStream(String id, OutputStream outputStream) throws IOException {
        throw new UnsupportedOperationException("yet");
    }

    public Collection<String> [] deserializeObjectFromStoreToArray(String id) throws IOException {
        CSVWrapper wrapper= csvCollection.findOneById(id);
        return wrapper.csv;
    }

    @Override
    public Collection<CSVRecord> deserializeObjectFromStore(String id) throws IOException {
        CSVWrapper wrapper= csvCollection.findOneById(id);
        if (wrapper==null) return null;
        List<String>[] array=wrapper.csv;
        List result=new ArrayList();
        for (List<String> ll: array) {
            StringBuffer sb=new StringBuffer();
            boolean first=true;
            for (String s: ll) {
                if (first) {
                    first=false;
                } else {
                    sb.append(",");
                }
                sb.append(StringEscapeUtils.escapeCsv(s));
            }
            final String string = sb.toString();
            //System.out.println(string);
            // TODO: Isn't there a way to build a csvrecord without constructing a string to b eparsed?
            CSVRecord record=CSVParser.parse(string, CSVFormat.DEFAULT).getRecords().get(0);
            result.add(record);
        }
        return result;
    }



    @Override
    public boolean delete(String storageId) {
        DeleteResult result=csvCollection.removeById(storageId);
        return result.getDeletedCount()==1;
    }
}
