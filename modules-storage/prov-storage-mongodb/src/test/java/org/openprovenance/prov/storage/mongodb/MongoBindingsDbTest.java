package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;

import org.openprovenance.prov.template.core.Bindings;

import java.io.FileInputStream;
import java.io.IOException;

public class MongoBindingsDbTest extends TestCase {

    public static final String EX_NS = "http://example.org";
    public static final String EX_PREFIX = "ex";

    ObjectMapper mapper=new ObjectMapper();

    public void testMongoStorage() throws IOException {

        MongoBindingsResourceStorage ds=new MongoBindingsResourceStorage("provtest",mapper);

        String id=ds.newStore(null,null);


        assertNotNull(id);


        Bindings bindings1= Bindings.fromStream(mapper, new FileInputStream("src/test/resources/bindings1.json"));

        ds.serializeObjectToStore(bindings1,id);


        Bindings bindings2=ds.deserializeObjectFromStore(id);

        assertNotNull(bindings2);

        assertEquals(bindings1.var,bindings2.var);
        assertEquals(bindings1.vargen,bindings2.vargen);
        assertEquals(bindings1.template,bindings2.template);
        assertEquals(bindings1.context,bindings2.context);

    }


}
