package org.openprovenance.prov.dot;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.Container;

import javax.xml.bind.JAXBException;
import java.io.File;

import org.antlr.runtime.tree.CommonTree;
import org.openprovenance.prov.asn.Utility;

public class SculptureTest extends TestCase {

    public void sculptureToDot(String asnFile, String xmlFile, String dotFile, String pdfFile)
        throws java.io.FileNotFoundException,  java.io.IOException, JAXBException, Throwable {
        Utility u=new Utility();

        CommonTree tree = u.convertASNToTree(asnFile);

        Container o= (Container) u.convertTreeToJavaBean(tree);

        ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        
        serial.serialiseContainer(new File(xmlFile),o,true);

        ProvToDot toDot=new ProvToDot("src/main/resources/defaultConfigWithRoleNoLabel.xml"); 
        
        toDot.convert(o,dotFile,pdfFile);
    }

    public void testSculptureToDot1() throws java.io.FileNotFoundException,  java.io.IOException, JAXBException, Throwable {
        sculptureToDot("../asn/src/test/resources/prov/sculpture.prov-asn",
                       "target/sculpture.prov-xml",
                       "target/sculpture.dot",
                       "target/sculpture.pdf");
    }
}
