package org.openprovenance.prov.core.roundtrip;

import org.openprovenance.prov.core.RoundTripFromJavaXMLTest;
import org.openprovenance.prov.model.RoundTripFromJavaTest;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;

import java.io.File;
import java.io.IOException;

public class RoundTripFromJavaXMLViaLegacyTest extends RoundTripFromJavaXMLTest {

    public Document readDocumentFromFile(String file) throws IOException {
        System.out.println(" reading (xml) from " + file);
        Document doc= new org.openprovenance.prov.core.xml.serialization.ProvDeserialiser().deserialiseDocument(new File(file));
        BeanTraversal bc=new BeanTraversal(RoundTripFromJavaTest.pFactory, RoundTripFromJavaTest.pFactory);
        org.openprovenance.prov.model.Document doc2=bc.doAction(doc);
        return doc2;
    }

    public String extension() {
        return "_b.xml";
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
