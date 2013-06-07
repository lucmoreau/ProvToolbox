package org.openprovenance.prov.dot;
import junit.framework.TestCase;
import org.openprovenance.prov.xml.Document;

import javax.xml.bind.JAXBException;
import org.openprovenance.prov.notation.Utility;

public class ASNTest extends TestCase {

    public void asnToDot(String asnFile, String xmlFile, String dotFile, String pdfFile, String title)
        throws java.io.FileNotFoundException,  java.io.IOException, JAXBException, Throwable {
        Utility u=new Utility();


        Document o= (Document) u.convertASNToJavaBean(asnFile);

        //serial.serialiseDocument(new File(xmlFile),o,true);

        ProvToDot toDot=new ProvToDot("src/main/resources/defaultConfigWithRoleNoLabel.xml"); 
        
        toDot.convert(o,dotFile,pdfFile, title);
    }

    public void testAsnToDot1() throws java.io.FileNotFoundException,  java.io.IOException, JAXBException, Throwable {
        asnToDot("../prov-n/src/test/resources/prov/file-example2.asn",
                 "target/file-example2.prov-xml",
                 "target/file-example2.dot",
                 "target/file-example2.pdf",
                 "file-example2");
    }
}
