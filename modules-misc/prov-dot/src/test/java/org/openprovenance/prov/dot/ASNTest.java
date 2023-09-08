package org.openprovenance.prov.dot;
import junit.framework.TestCase;



import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.Utility;

public class ASNTest extends TestCase {

    public void asnToDot(String asnFile, String xmlFile, String dotFile, String pdfFile, String title)
        throws  java.io.IOException {
        Utility u=new Utility();

        ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();

        Document o= (Document) u.convertSyntaxTreeToJavaBean(asnFile, pFactory);

        ProvToDot toDot=new ProvToDot(pFactory);
        
        toDot.convert(o,dotFile,pdfFile, title);
    }

    public void testAsnToDot1() throws java.io.IOException {
        asnToDot("src/test/resources/prov/file-example2.provn",
                 "target/file-example2.prov-xml",
                 "target/file-example2.dot",
                 "target/file-example2.pdf",
                 "file-example2");
    }
}
