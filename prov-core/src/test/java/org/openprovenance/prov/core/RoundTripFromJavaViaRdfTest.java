package org.openprovenance.prov.core;

import org.openprovenance.prov.core.xml.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.xml.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.HasType;
import org.openprovenance.prov.model.Location;
import org.openprovenance.prov.rdf.Ontology;
import org.openrdf.rio.RDFFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class RoundTripFromJavaViaRdfTest extends RoundTripFromJavaTest {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RoundTripFromJavaViaRdfTest(String testName) {
        super(testName);
    }

    public Document readXMLDocument(String file)
            throws IOException {

        System.out.println("reading (rdf) from " + file);

        Ontology onto=new Ontology(pFactory);

        org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(
                pFactory, onto);
        return rdfU.parseRDF(new FileInputStream(file), RDFFormat.JSONLD, "file://" + file);

      //  ProvDeserialiser deserial=new ProvDeserialiser();
       // return deserial.deserialiseDocument(new File(file));

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
