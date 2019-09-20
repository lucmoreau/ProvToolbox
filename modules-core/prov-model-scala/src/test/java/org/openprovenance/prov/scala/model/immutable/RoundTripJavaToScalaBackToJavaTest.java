package org.openprovenance.prov.scala.model.immutable;

import org.openprovenance.prov.model.RoundTripFromJavaTest;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;


/**
 * Unit test for PROV roundtrip conversion between Java represenations
 */
public class RoundTripJavaToScalaBackToJavaTest extends RoundTripFromJavaTest {

    public static org.openprovenance.prov.vanilla.ProvFactory pFactory=new org.openprovenance.prov.scala.immutable.ProvFactory();


    public RoundTripJavaToScalaBackToJavaTest(String testName) {
        super(testName);
        test=false;
        pFactory=new ProvFactory();
    }



    ProvFactory pFactoryS=new org.openprovenance.prov.scala.immutable.ProvFactory();

    @Override
    public void compareDocAndFile(Document doc, String file, boolean check) {
        BeanTraversal bc=new BeanTraversal(pFactoryS, pFactoryS);
        Document doc2=bc.doAction(doc);

        BeanTraversal bc2=new BeanTraversal(pFactory, pFactory);
        Document doc3=bc2.doAction(doc2);


        compareDocuments(doc, doc3, check && checkTest(file));
    }

    public boolean checkSchema(String name) {
        return false;
    }



}
