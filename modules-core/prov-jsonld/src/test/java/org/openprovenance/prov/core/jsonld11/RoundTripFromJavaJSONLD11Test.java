package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.RoundTripFromJavaTest;

import java.io.*;

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
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String s_error=errorReader.readLine();
            if (s_error!=null && !s_error.equals("")) {
                System.out.println("Error:  " + s_error);
            }
            proc.waitFor();
            //System.err.println("exit value " + proc.exitValue());
        } catch (InterruptedException e){};
    }

    public String extension() {
        return ".jsonld";
    }

    public boolean checkTest(String name) {
        if (name.endsWith("entity101" + extension())) {
            System.out.println(escapeRed("########## Skipping testing for entity101 in JSONLD"));
            return false;
        }
        return true;
    }


}
