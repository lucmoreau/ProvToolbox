package org.openprovenance.prov.core;

import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
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

        System.out.println("reading (rdf) from " + file);

        Ontology onto=new Ontology(pFactory);

        org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(
                pFactory, onto);
        return rdfU.parseRDF(new FileInputStream(file + ".ttl" ), RDFFormat.NTRIPLES, "file://" + file);


    }


    public void addTypes(HasType ht) {

        ht.getType().add(pFactory.newType(pFactory.newQualifiedName(EX_NS, "abc", EX_PREFIX),
                name.PROV_QUALIFIED_NAME));

    }


}
