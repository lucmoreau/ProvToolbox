package org.openprovenance.prov.core;

import org.openprovenance.prov.model.RoundTripFromJavaTest;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

public class RoundTripFromJavaXMLViaLegacyTest extends RoundTripFromJavaXMLTest {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RoundTripFromJavaXMLViaLegacyTest(String testName) {
        super(testName);
    }

    ProvFactory pf=new ProvFactory();
    public Document readDocumentFromFile(String file)
            throws IOException {

        System.out.println("reading (xml) from " + file);



        Document doc= null;
        try {
            doc = org.openprovenance.prov.xml.ProvDeserialiser.getThreadProvDeserialiser().deserialiseDocument(new File(file));
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        BeanTraversal bc=new BeanTraversal(RoundTripFromJavaTest.pFactory, RoundTripFromJavaTest.pFactory);
        org.openprovenance.prov.model.Document doc2=bc.doAction(doc);
        return doc2;
    }

    public String extension() {
        return "_b.xml";
    }



    public String get0tagWithDigit() {
        return "0tagWithDigit";
    }


    @Override
    public boolean wrapper_erase() {
        return true;
    }


    public void testMembership1() {

    }
    public void testMembership2() {

    }
    public void testMembership3() {

    }


    // TODO: need to be in namespace provext
    public void testQualifiedSpecializationOf1 () {}
    public void testQualifiedSpecializationOf2 () {}
    public void testQualifiedHadMember1 () {}
    public void testQualifiedHadMember2 () {}
    public void testQualifiedAlternateOf1 () {}
    public void testQualifiedAlternateOf2 () {}


}
