package org.openprovenance.prov.dot;
import javax.xml.bind.JAXBException;
import org.openprovenance.prov.xml.Container;

/**
 * 
 */
public class PubTest extends org.openprovenance.prov.asn.PubTest {

    public PubTest( String testName ) {
         super(testName);
    }
    
    static Container co1;
    static Container co2;
    static Container co3;

    @Override
    public void testReadASNSaveXML() {
        // ignore
    }

    public void testReadASNSaveXML1() throws java.io.IOException, java.lang.Throwable {
        String file="../asn/src/test/resources/prov/w3c-publication.prov-asn";
        testReadASNSaveXML(file);
        co1=graph1;
    }

    public void testReadASNSaveXML2() throws java.io.IOException, java.lang.Throwable {
        String file="../asn/src/test/resources/prov/w3c-publication2.prov-asn";
        testReadASNSaveXML(file);
        co2=graph1;
    }

    public void testReadASNSaveXML3() throws java.io.IOException, java.lang.Throwable {
        String file="../asn/src/test/resources/prov/w3c-publication3.prov-asn";
        testReadASNSaveXML(file);
        co3=graph1;
    }

    /** Produces a dot representation
     * of created graph. */
    public void testPubToDot() throws java.io.FileNotFoundException,  java.io.IOException   {
        ProvToDot toDot=new ProvToDot("src/main/resources/defaultConfigWithRoleNoLabel.xml"); 
        
        toDot.convert(co1,"target/w3c-publication1.dot", "target/w3c-publication1.pdf");
        toDot.convert(co2,"target/w3c-publication2.dot", "target/w3c-publication2.pdf");
        toDot.convert(co3,"target/w3c-publication3.dot", "target/w3c-publication3.pdf");
    }


}
