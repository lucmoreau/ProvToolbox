package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openprovenance.prov.template.expander.BindingsJson;
import org.openprovenance.prov.template.expander.BindingsJson.BindingsBean;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.openprovenance.prov.storage.mongodb.MongoCSVResourceStorage.toList;

public class mongoCSVDbTest extends TestCase {

    public static final String EX_NS = "http://example.org";
    public static final String EX_PREFIX = "ex";

    ObjectMapper mapper=new ObjectMapper();

    public void testMongoStorage() throws IOException {

        MongoCSVResourceStorage ds=new MongoCSVResourceStorage("provtest",mapper);

        String id=ds.newStore(null,null);


        assertNotNull(id);


        CSVParser parser = CSVParser.parse(new File("src/test/resources/test1.csv"), StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        List<CSVRecord> records=parser.getRecords();


        ds.serializeObjectToStore(records,id);


        Collection<CSVRecord> records2=ds.deserializeObjectFromStore(id);
        assertNotNull(records2);

        List<CSVRecord> ll2=new ArrayList<>(records2);
        for (int i=0; i < records.size(); i++) {
            System.out.println(toList(records.get(i)));
            assertEquals(toList(records.get(i)), toList(ll2.get(i)) );
        }

    }


}
