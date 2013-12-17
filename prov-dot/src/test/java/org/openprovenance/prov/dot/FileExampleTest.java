package org.openprovenance.prov.dot;
import junit.framework.TestCase;

import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.Document;

import javax.xml.bind.JAXBException;
import java.io.File;

import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.notation.Utility;

public class FileExampleTest extends TestCase {

    public void fileToDot(String asnFile, String xmlFile, String dotFile, String pdfFile, String title)
        throws java.io.FileNotFoundException,  java.io.IOException, JAXBException, Throwable {
        Utility u=new Utility();
        ProvFactory pFactory=ProvFactory.getFactory();

        Document o= (Document) u.convertASNToJavaBean(asnFile,pFactory);

	Namespace.withThreadNamespace(o.getNamespace());
	System.out.println("ns is " + o.getNamespace());
        ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        
        serial.serialiseDocument(new File(xmlFile),o,true);

        ProvToDot toDot=new ProvToDot("src/main/resources/defaultConfigWithRoleNoLabel.xml"); 
        
        toDot.convert(o,dotFile,pdfFile,title);
    }

    public void testFileExampleToDot1() throws java.io.FileNotFoundException,  java.io.IOException, JAXBException, Throwable {
        fileToDot("../prov-n/src/test/resources/prov/prov-dm-example1.prov-asn",
                  "target/file.prov-xml",
                  "target/file.dot",
                  "target/file.pdf",
                  "prov-dm-example1");
    }
}
