package org.openprovenance.prov.storage.mongodb;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class mongoDb2Test extends TestCase {

    public void testMongoStorage() throws IOException {
      //  System.out.println("Creating a NDS");

        MongoNonDocumentResourceStorage nds=new MongoNonDocumentResourceStorage();
      //  System.out.println("Creating a new storeID");

        String id=nds.newStore("ttl","rdf");

     //   System.out.println("object is " + id);

        assertNotNull(id);

        final String contents = "hello, this is some contents!";
        nds.copyStringToStore(contents, id);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        nds.copyStoreToOutputStream(id,baos);
        String retrieved=baos.toString();

     //   System.out.println(retrieved);
        assertEquals(contents,retrieved);

        assertTrue(nds.delete(id));

    }


}
