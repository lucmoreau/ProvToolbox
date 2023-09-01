package org.openprovenance.prov.dot;
import junit.framework.TestCase;



import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.ProvSerialiser;
import org.openprovenance.prov.notation.Utility;

public class FileExampleTest extends TestCase {

    public void fileToDot(String asnFile, String xmlFile, String dotFile, String pdfFile, String title)
            throws java.io.FileNotFoundException,  java.io.IOException, JAXBException, Throwable {
        Utility u=new Utility();
        ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();

        Document o= (Document) u.convertSyntaxTreeToJavaBean(asnFile,pFactory);

        Namespace.withThreadNamespace(o.getNamespace());
        System.out.println("ns is " + o.getNamespace());
        ProvSerialiser serial=new  ProvSerialiser(pFactory);

        serial.serialiseDocument(new FileOutputStream(xmlFile),o,true);

        ProvToDot toDot=new ProvToDot(pFactory,"src/main/resources/defaultConfigWithRoleNoLabel.xml");

        toDot.convert(o,dotFile,pdfFile,title);
    }

    public void testFileExampleToDot1() throws java.io.FileNotFoundException,  java.io.IOException, JAXBException, Throwable {
        fileToDot("src/test/resources/prov/prov-dm-example1.provn",
                  "target/file.prov-xml",
                  "target/file.dot",
                  "target/file.pdf",
                  "prov-dm-example1");
    }
}
