package org.openprovenance.prov.core;

import org.openprovenance.prov.core.xml.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.xml.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.RoundTripFromJavaTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class RoundTripFromJavaXMLTest extends RoundTripFromJavaTest {


    public Document readDocumentFromFile(String file) throws IOException {
        System.out.println(" reading from " + file);
        ProvDeserialiser deserial=new ProvDeserialiser();
        return deserial.deserialiseDocument(new FileInputStream(file));
    }



    public void writeDocumentToFile(Document doc, String file) throws IOException {
        System.out.println("writing to " + file);
        ProvSerialiser serial=new ProvSerialiser(wrapper_erase());
        serial.serialiseDocument(new FileOutputStream(file), doc, true);
    }

    public boolean wrapper_erase() {
        return true;
    }
    public String extension() {
        return ".xml";
    }
    @Override
    public void testEntity101() {
        System.out.println("* testEntity101() not supported in XML");
    }
}
