package org.openprovenance.prov.core;

import org.jetbrains.annotations.NotNull;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.HasType;
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



    public Document readXMLDocument(String file)
            throws IOException {

        String ttl = turtleFile(file);
        System.out.println("reading from " + ttl);

        Ontology onto=new Ontology(pFactory);

        org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(
                pFactory, onto);
        Document doc= rdfU.parseRDF(new FileInputStream(ttl), RDFFormat.NTRIPLES, "file://" + file);

        BeanTraversal bc=new BeanTraversal(pFactory, pFactory);
        org.openprovenance.prov.model.Document doc2=bc.doAction(doc);
        return doc2;

    }

    @NotNull
    public String turtleFile(String file) {
        return file.replace(".jsonld11",".ttl" );
    }


    public void writeXMLDocument(Document doc, String file)
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

        ht.getType().add(pFactory.newType(pFactory.newQualifiedName(EX_NS, "abc", EX_PREFIX),
                name.PROV_QUALIFIED_NAME));

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


}
