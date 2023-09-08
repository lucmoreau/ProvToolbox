package org.openprovenance.prov.dot;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.Document;

/**
 * 
 */
public class PubTest extends org.openprovenance.prov.notation.PubTest {

    ProvFactory pf= org.openprovenance.prov.vanilla.ProvFactory.getFactory();

    public PubTest( String testName ) {
         super(testName);
    }
    
   
    @Override
    public void auxTestReadASNSaveXML() {
        // ignore
    }

    public void testReadASNSaveXML1() throws java.io.IOException {
        String file="src/test/resources/prov/w3c-publication1.provn";
        auxTestReadASNSaveXML(file,"target/w3c-publication1.prov-xml");
        Document co1=graph1;
        ProvToDot toDot=new ProvToDot(pf);
        toDot.convert(co1,"target/w3c-publication1.dot", "target/w3c-publication1.pdf", "w3c-publication1");

    }

    public void testReadASNSaveXML2() throws java.io.IOException {
        String file="src/test/resources/prov/w3c-publication2.provn";
        auxTestReadASNSaveXML(file,"target/w3c-publication2.prov-xml");
        Document co2=graph1;
        ProvToDot toDot=new ProvToDot(pf);
        toDot.convert(co2,"target/w3c-publication2.dot", "target/w3c-publication2.pdf", "w3c-publication2");


    }

    public void testReadASNSaveXML3() throws java.io.IOException{
        String file="src/test/resources/prov/w3c-publication3.provn";
        auxTestReadASNSaveXML(file,"target/w3c-publication3.prov-xml");
        Document co3=graph1;
        ProvToDot toDot=new ProvToDot(pf);
        toDot.convert(co3,"target/w3c-publication3.dot", "target/w3c-publication3.pdf", "w3c-publication3");
    }



    //FIXME: BUNDLE
    public void NOtestBundles2() throws java.io.IOException {
        String file="src/test/resources/prov/bundles2.provn";
        auxTestReadASNSaveXML(file,"target/bundles2.provx");
        Document co3=graph1;
        ProvToDot toDot=new ProvToDot(pf);
        toDot.convert(co3,"target/bundles2.dot", "target/bundles2.pdf", "bundles2");
    }



}
