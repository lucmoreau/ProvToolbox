package org.openprovenance.prov.dot.test;
import junit.framework.TestCase;
import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.notation.ProvSerialiser;

import java.io.FileOutputStream;
import java.io.IOException;

import org.openprovenance.prov.notation.Utility;

public class SculptureTest extends TestCase {

    public void sculptureToDot(String asnFile, String xmlFile, String dotFile, String pdfFile, String title) throws IOException {
        Utility u=new Utility(DateTimeOption.PRESERVE,null);
        ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();


        Document o= (Document) u.convertSyntaxTreeToJavaBean(asnFile,pFactory);

        Namespace.withThreadNamespace(o.getNamespace());

        ProvSerialiser serial=new ProvSerialiser(pFactory);
        
        serial.serialiseDocument(new FileOutputStream(xmlFile),o,true);

        ProvToDot toDot=new ProvToDot(pFactory);
        
        toDot.convert(o,dotFile,pdfFile,title);
    }

    public void testSculptureToDot1() throws   java.io.IOException{
        sculptureToDot("src/test/resources/org/openprovenance/prov/dot/test/sculpture.provn",
                       "target/sculpture.prov-xml",
                       "target/sculpture.dot",
                       "target/sculpture.pdf",
                       "sculpture");
    }
}
