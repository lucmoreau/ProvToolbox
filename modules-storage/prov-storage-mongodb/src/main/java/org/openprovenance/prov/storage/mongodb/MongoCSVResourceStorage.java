package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.mongojack.DBUpdate;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.openprovenance.apache.commons.lang.StringEscapeUtils;
import org.openprovenance.prov.storage.api.NonDocumentGenericResourceStorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class MongoCSVResourceStorage implements NonDocumentGenericResourceStorage<Collection<CSVRecord>>, Constants {


    public static final String CSV_ENTRY = "csv";
    private final DB db;

    private static Logger logger = Logger.getLogger(MongoCSVResourceStorage.class);

    private final JacksonDBCollection<CSVWrapper, String> csvCollection;

    private final ObjectMapper mapper;

    public MongoCSVResourceStorage(String dbname, ObjectMapper mapper) {
        this("localhost",dbname,mapper);
    }
    public MongoCSVResourceStorage(String host, String dbname, ObjectMapper mapper) {

        MongoClient mongoClient = new MongoClient(host, 27017);
        DB db = mongoClient.getDB(dbname);
        this.db=db;

        DBCollection collection=db.getCollection(COLLECTION_CSV);
        mapper.registerModule(org.mongojack.internal.MongoJackModule.INSTANCE);
        JacksonDBCollection<CSVWrapper, String> bindingsCollection = JacksonDBCollection.wrap(collection, CSVWrapper.class, String.class, mapper);
        this.csvCollection =bindingsCollection;
        this.mapper=mapper;
    }

    @Override
    public String newStore(String _suggestedExtension, String _mimeType) throws IOException {
        CSVWrapper cw=new CSVWrapper();
        WriteResult<CSVWrapper, String> result= csvCollection.insert(cw);
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
    public void serializeObjectToStore(Collection<CSVRecord> coll, String id) throws IOException {
        List<List<String>> result=new ArrayList<>();
        for (CSVRecord rec: coll) {
            List<String> ll = toList(rec);
            result.add(ll);
        }
        List<?>[] array=result.toArray(new List<?> [0]);

        csvCollection.updateById(id, DBUpdate.set(CSV_ENTRY, array));
    }

    public void serializeObjectToStore(Iterator<CSVRecord> iterator, String id) throws IOException {
        List<List<String>> result=new ArrayList<>();
        for (Iterator<CSVRecord> it = iterator; it.hasNext(); ) {
            CSVRecord rec = it.next();
            List<String> ll = toList(rec);
            result.add(ll);
        }
        List<?>[] array=result.toArray(new List<?> [0]);

        csvCollection.updateById(id, DBUpdate.set("csv", array));
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
        WriteResult<CSVWrapper, String> result=csvCollection.removeById(storageId);
        return result.getN()==1;
    }
}
