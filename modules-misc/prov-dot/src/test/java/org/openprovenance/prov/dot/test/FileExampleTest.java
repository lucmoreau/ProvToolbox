package org.openprovenance.prov.dot.test;
import junit.framework.TestCase;


import java.io.FileOutputStream;
import java.io.IOException;

import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.ProvSerialiser;
import org.openprovenance.prov.notation.Utility;

public class FileExampleTest extends TestCase {

    public void fileToDot(String asnFile, String xmlFile, String dotFile, String pdfFile, String title) throws IOException {
        Utility u=new Utility(DateTimeOption.PRESERVE,null);
        ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();

        Document o= (Document) u.convertSyntaxTreeToJavaBean(asnFile,pFactory);

        Namespace.withThreadNamespace(o.getNamespace());
        System.out.println("ns is " + o.getNamespace());
        ProvSerialiser serial=new  ProvSerialiser(pFactory);

        serial.serialiseDocument(new FileOutputStream(xmlFile),o,true);

        ProvToDot toDot=new ProvToDot(pFactory);

        toDot.convert(o,dotFile,pdfFile,title);
    }

    public void fileToDotType(String asnFile, String dotFile, String pdfFile, String type, String title) throws IOException {
        Utility u=new Utility(DateTimeOption.PRESERVE,null);
        ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();

        Document o= (Document) u.convertSyntaxTreeToJavaBean(asnFile,pFactory);

        Namespace.withThreadNamespace(o.getNamespace());
        System.out.println("ns is " + o.getNamespace());
        ProvSerialiser serial=new  ProvSerialiser(pFactory);


        ProvToDot toDot=new ProvToDot(pFactory);

        toDot.convert(o,dotFile,pdfFile, type, title);
    }

    public void testFileExampleToDot1() throws IOException {
        fileToDot("src/test/resources/org/openprovenance/prov/dot/test/prov-dm-example1.provn",
                  "target/file.prov-xml",
                  "target/file.dot",
                  "target/file.pdf",
                  "prov-dm-example1");
    }
    public void testFileExampleToDot2() throws IOException {
        fileToDotType("src/test/resources/org/openprovenance/prov/dot/test/label-example.provn",
                "target/label-example.dot",
                "target/label-example.svg",
                "svg",
                "label-example");
    }
}
