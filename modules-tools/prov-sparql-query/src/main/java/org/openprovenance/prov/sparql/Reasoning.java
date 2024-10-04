package org.openprovenance.prov.sparql;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.*;
import org.apache.jena.ontology.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.Lang;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Reasoning {

    public static final String JSON = "JSON";
    public static final String XML = "XML";
    public static final String TEXT = "TEXT";

    public static void execute(Config config) throws IOException {
        // File paths
        String ontologyFile = config.getOntology();  // The OWL ontology
        String rdfFile = config.getInfile();   // The RDF file with data
        // rdfFile = "src/test/resources/pc1-full.jsonld";

        String outFile = config.getOutfile();  // The output file
        String outFormat = config.getOutformat();  // The output format
        if (outFormat == null) {
            if (outFile != null) {
                if (outFile.endsWith(".json")) {
                    outFormat = JSON;
                } else if (outFile.endsWith(".xml")) {
                    outFormat = XML;
                } else if (outFile.endsWith(".txt")) {
                    outFormat = TEXT;
                }
            } else {
                outFormat = TEXT;
            }
        }
        if (outFile != null) {
            System.setOut(new java.io.PrintStream(new java.io.FileOutputStream(outFile)));
        }


        String sparqlQueryFile=config.getQuery();  // The SPARQL query

        // Step 0: Load the query
        String sparqlQuery = new String(Files.readAllBytes(Paths.get(sparqlQueryFile)));

        // Step 1: Load the ontology model
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        RDFDataMgr.read(ontModel, ontologyFile, Lang.TURTLE);

        // Step 2: Load the RDF file into the model
        Model dataModel = ModelFactory.createDefaultModel();
        RDFDataMgr.read(dataModel, rdfFile, Lang.JSONLD);

        // Step 3: Combine the ontology and the data model
        ontModel.addSubModel(dataModel);

        // Step 4: Apply OWL2 reasoning
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, ontModel);

        // display tuples in inferred model



        // Step 5: Execute the SPARQL query on the inferred model
        Query query = QueryFactory.create(sparqlQuery);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, infModel)) {
            ResultSet results = qexec.execSelect();

            // Step 6: Output results
            //ResultSetFormatter.outputAsJSON(System.out,results);
            if (outFormat.equals(JSON)) {
                ResultSetFormatter.outputAsJSON(System.out, results);
            } else if (outFormat.equals(XML)) {
                ResultSetFormatter.outputAsXML(System.out, results);
            } else {
                ResultSetFormatter.out(System.out, results, query);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        CommandLine.main(new String[] {"-infile", "src/test/resources/pc1-full.jsonld", "-query", "src/test/resources/q1.rq", "-outformat", "TEXT"});
    }
}
