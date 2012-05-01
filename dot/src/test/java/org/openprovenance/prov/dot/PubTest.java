package org.openprovenance.prov.dot;
import javax.xml.bind.JAXBException;
import org.openprovenance.prov.xml.Bundle;

/**
 * 
 */
public class PubTest extends org.openprovenance.prov.notation.PubTest {

    public PubTest( String testName ) {
         super(testName);
    }
    
    static Bundle co1;
    static Bundle co2;
    static Bundle co3;

    @Override
    public void testReadASNSaveXML() {
        // ignore
    }

    public void testReadASNSaveXML1() throws java.io.IOException, java.lang.Throwable {
        String file="../asn/src/test/resources/prov/w3c-publication1.prov-asn";
        testReadASNSaveXML(file,"target/w3c-publication1.prov-xml");
        co1=graph1;
    }

    public void testReadASNSaveXML2() throws java.io.IOException, java.lang.Throwable {
        String file="../asn/src/test/resources/prov/w3c-publication2.prov-asn";
        testReadASNSaveXML(file,"target/w3c-publication2.prov-xml");
        co2=graph1;
    }

    public void testReadASNSaveXML3() throws java.io.IOException, java.lang.Throwable {
        String file="../asn/src/test/resources/prov/w3c-publication3.prov-asn";
        testReadASNSaveXML(file,"target/w3c-publication3.prov-xml");
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
