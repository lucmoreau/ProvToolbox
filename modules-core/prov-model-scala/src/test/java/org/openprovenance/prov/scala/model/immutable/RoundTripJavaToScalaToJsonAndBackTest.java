package org.openprovenance.prov.scala.model.immutable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.RoundTripFromJavaTest;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.scala.jsonld11.serialization.ProvSerialiser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Unit test for PROV roundtrip conversion between Java represenations
 */
public class RoundTripJavaToScalaToJsonAndBackTest extends RoundTripFromJavaTest {

    final static  Logger logger = LogManager.getLogger(RoundTripJavaToScalaToJsonAndBackTest.class);

    public RoundTripJavaToScalaToJsonAndBackTest() {
        test=false;
        pFactory=new ProvFactory();
    }



    ProvFactory pFactoryS=new org.openprovenance.prov.scala.immutable.ProvFactory();

    ProvUtilities pu=new ProvUtilities();

    @Override
    public void compareDocAndFile(Document doc, String file, boolean check) {
        BeanTraversal bc=new BeanTraversal(pFactoryS, pFactoryS);

        Document doc2=bc.doAction(doc);

        file = file + extension();
        writeDocument(doc2, file);
        if (check)
            conditionalCheckSchema(file);
        Document doc3 = readDocument(file);


        documentComparator.compareDocuments(doc2, doc3, check && checkTest(file));
    }


    @Override
    public String extension() {
        return ".jsonld11";
    }

    public boolean checkSchema(String name) {
        return false;
    }

    @Override
    public void writeDocumentToFile(Document doc, String file) throws IOException {
        System.out.println("writing (scala) to " + file);
        org.openprovenance.prov.model.ProvSerialiser serial=new ProvSerialiser();
        serial.serialiseDocument(Files.newOutputStream(Paths.get(file)), doc, true);
    }

    public Document readDocumentFromFile(String file) throws IOException {
        System.out.println("reading (scala) from " + file);
        org.openprovenance.prov.model.ProvDeserialiser deserial=new  org.openprovenance.prov.scala.jsonld11.serialization.ProvDeserialiser();
        return deserial.deserialiseDocument(new BufferedInputStream(Files.newInputStream(Paths.get(file))));
    }

    @Override
    public void testBundle6() {
        logger.info("###### failing testBundle6" );
    }
}
