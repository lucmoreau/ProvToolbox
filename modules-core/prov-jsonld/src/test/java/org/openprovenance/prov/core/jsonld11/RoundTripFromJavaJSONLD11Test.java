package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.RoundTripFromJavaTest;

import java.io.*;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.JSONLDSCHEMA_JSON_2024_06_11;

public class RoundTripFromJavaJSONLD11Test extends RoundTripFromJavaTest {


    public Document readDocumentFromFile(String file) throws IOException {
        readingMessage(file);
        ProvDeserialiser deserial=new ProvDeserialiser();
        return deserial.deserialiseDocument(new FileInputStream(file));

    }
    public void readingMessage(String file) {
        System.out.println(" reading from " + file);
    }

    public void writingMessage(String file) {
        System.out.println("writing to " + file);
    }
    final ObjectMapper mapper=new ObjectMapper();



    public void writeDocumentToFile(Document doc, String file) throws IOException {
        writingMessage(file);

        ProvSerialiser serial=new ProvSerialiser(mapper, false);
        serial.serialiseDocument(new FileOutputStream(file), doc, true);
    }


    public void executeAndWait (String command) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        java.lang.Process proc = runtime.exec(command);
        try {
            final int exitValue = proc.waitFor();
            if (exitValue == 0) {
                return ;
            } else {
                try (final BufferedReader b = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                    String line;
                    if ((line = b.readLine()) != null)
                        System.out.println(escapeRed(line));
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                throw new IOException("exit value " + exitValue);
            }
            //System.err.println("exit value " + proc.exitValue());
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    public String extension() {
        return ".jsonld";
    }



    public boolean checkTest(String name) {
        if (name.contains("DictionaryMembership")) {
            System.out.println(escapeRed("########## Skipping testing for " + name + " in " + extension()));
            return false;
        }
        // call ajv executable on the command line to validate the JSON-LD file
        try {
            executeAndWait("ajv -s " + jsonSchemaLocation() + " -d " + name);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String jsonSchemaLocation() {
        return JSONLDSCHEMA_JSON_2024_06_11;
    }

    @Override
    public void testDictionaryMembership1() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership1 (jsonld)"));
    }
    @Override
    public void testDictionaryMembership2() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership2 (jsonld)"));
    }
    @Override
    public void testDictionaryMembership3() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership3 (jsonld)"));
    }
    @Override
    public void testDictionaryMembership4() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership4 (jsonld)"));
    }


}
