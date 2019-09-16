package org.openprovenance.prov.scala.model.immutable;

import org.openprovenance.prov.core.RoundTripFromJavaTest;
import org.openprovenance.prov.core.vanilla.ProvFactory;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.scala.jsonld11.serialization.ProvSerialiser;

import java.io.*;

/**
 * Unit test for PROV roundtrip conversion between Java represenations
 */
public class RoundTripJavaToScalaToJsonAndBackTest extends RoundTripFromJavaTest {

    public RoundTripJavaToScalaToJsonAndBackTest(String testName) {
        super(testName);
        test=false;
        pFactory=new ProvFactory();
    }



    ProvFactory pFactoryS=new org.openprovenance.prov.scala.immutable.ProvFactory();

    @Override
    public void compareDocAndFile(Document doc, String file, boolean check) {
        BeanTraversal bc=new BeanTraversal(pFactoryS, pFactoryS);
        Document doc2=bc.doAction(doc);

        file = file + extension();
        writeDocument(doc2, file);
        if (check)
            conditionalCheckSchema(file);
        Document doc3 = readDocument(file);



        compareDocuments(doc, doc3, check && checkTest(file));
    }


    @Override
    public String extension() {
        return ".jsonld11";
    }

    public boolean checkSchema(String name) {
        return false;
    }

    @Override
    public void writeXMLDocument(Document doc, String file)
            throws IOException {


        System.out.println("writing (scala) to " + file);


        org.openprovenance.prov.model.ProvSerialiser serial=new ProvSerialiser();
        serial.serialiseDocument(new FileOutputStream(file), doc, true);



    }

    public Document readXMLDocument(String file)
            throws IOException {

        System.out.println("reading from " + file);

        org.openprovenance.prov.model.ProvDeserialiser deserial=new  org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser();
        return deserial.deserialiseDocument(new BufferedInputStream(new FileInputStream(file)));

    }



}
