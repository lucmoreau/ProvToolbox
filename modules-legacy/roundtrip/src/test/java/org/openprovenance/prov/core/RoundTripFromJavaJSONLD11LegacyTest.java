package org.openprovenance.prov.core;

import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.HasType;
import org.openprovenance.prov.model.RoundTripFromJavaTest;
import org.openprovenance.prov.rdf.Ontology;
import org.openrdf.rio.RDFFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class RoundTripFromJavaJSONLD11LegacyTest extends RoundTripFromJavaJSONLD11Test {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RoundTripFromJavaJSONLD11LegacyTest(String testName) {
        super(testName);
    }



    public Document readDocumentFromFile(String file)
            throws IOException {

        String ttl = turtleFile(file);
        System.out.println("reading from " + ttl);

        Ontology onto=new Ontology(RoundTripFromJavaTest.pFactory);

        org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(
                RoundTripFromJavaTest.pFactory, onto);
        Document doc= rdfU.parseRDF(new FileInputStream(ttl), RDFFormat.NTRIPLES, "file://" + file);

        BeanTraversal bc=new BeanTraversal(RoundTripFromJavaTest.pFactory, RoundTripFromJavaTest.pFactory);
        org.openprovenance.prov.model.Document doc2=bc.doAction(doc);
        return doc2;

    }

    public String turtleFile(String file) {
        return file.replace(".jsonld11",".ttl" );
    }


    public void writeDocumentToFile(Document doc, String file)
            throws IOException {


        System.out.println("writing to " + file);


        ProvSerialiser serial=new ProvSerialiser();
        serial.serialiseDocument(new FileOutputStream(file), doc, true);

        String command = "jsonld --format=ntriples -o " + turtleFile(file) + " " + file;
        executeAndWait(command);



    }

    String theExtension = "_b.jsonld11";

    public String extension() {
        return theExtension;
    }


    public void addTypes(HasType ht) {

        ht.getType().add(RoundTripFromJavaTest.pFactory.newType(RoundTripFromJavaTest.pFactory.newQualifiedName(RoundTripFromJavaTest.EX_NS, "abc", RoundTripFromJavaTest.EX_PREFIX),
                RoundTripFromJavaTest.name.PROV_QUALIFIED_NAME));

    }


    public void testDerivation11() {
    }
    public void testDerivation12() {

    }
    public void testDerivation13() {

    }

    public void testMembership1() {

    }
    public void testMembership2() {

    }
    public void testMembership3() {

    }


    public void testSpecialization1() {

    }

    public void testAlternate1() {

    }


    public void testEntity3() {

    }

    public void testBundle1() {}
    public void testBundle2() {}
    public void testBundle3() {}
    public void testBundle4() {}

    public void testQualifiedSpecializationOf1 () {}
    public void testQualifiedSpecializationOf2 () {}
    public void testQualifiedHadMember1 () {}
    public void testQualifiedHadMember2 () {}
    public void testQualifiedAlternateOf1 () {}
    public void testQualifiedAlternateOf2 () {}




}
