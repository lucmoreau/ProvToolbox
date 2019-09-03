package org.openprovenance.prov.core;

import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RoundTripFromJavaJSONLD11Test extends RoundTripFromJavaTest {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RoundTripFromJavaJSONLD11Test(String testName) {
        super(testName);
    }

    public Document readXMLDocument(String file)
            throws IOException {

        System.out.println("reading from " + file);

        ProvDeserialiser deserial=new ProvDeserialiser();
        return deserial.deserialiseDocument(new File(file));

    }



    public void writeXMLDocument(Document doc, String file)
            throws IOException {


        System.out.println("writing to " + file);


        ProvSerialiser serial=new ProvSerialiser();
        serial.serialiseDocument(new FileOutputStream(file), doc, true);



    }

    public String extension() {
        return "_11.jsonld";
    }

}
