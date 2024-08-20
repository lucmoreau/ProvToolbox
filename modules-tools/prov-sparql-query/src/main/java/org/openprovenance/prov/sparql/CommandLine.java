package org.openprovenance.prov.sparql;

import java.io.IOException;

public class CommandLine {

    // write a main method that parse its argument , as follows:
    // -infile <filename> : input file
    // -outfile <filename> : output file
    // -informat <format> : input format
    // -outformat <format> : output format
    // -query <filename> : query file
    // -ontology <filename> : ontology file


    public static void main(String[] args) throws IOException {
        String infile=null;
        String outfile=null;
        String informat=null;
        String outformat=null;
        String query=null;
        String ontology=null;

        for (int i=0; i<args.length; i++) {
            if (args[i].equals("-infile")) {
                infile=args[i+1];
            }
            if (args[i].equals("-outfile")) {
                outfile=args[i+1];
            }
            if (args[i].equals("-informat")) {
                informat=args[i+1];
            }
            if (args[i].equals("-outformat")) {
                outformat=args[i+1];
            }
            if (args[i].equals("-query")) {
                query=args[i+1];
            }
            if (args[i].equals("-ontology")) {
                ontology=args[i+1];
            }
        }

        if (ontology==null) {
            ontology="src/main/resources/prov-o.ttl";
        }

        System.out.println("infile=" + infile);
        System.out.println("outfile=" + outfile);
        System.out.println("informat=" + informat);
        System.out.println("outformat=" + outformat);
        System.out.println("query=" + query);
        System.out.println("ontology=" + ontology);

        Config config=new Config();
        config.setInfile(infile);
        config.setOutfile(outfile);
        config.setInformat(informat);
        config.setOutformat(outformat);
        config.setQuery(query);
        config.setOntology(ontology);

        Reasoning.execute(config);
    }





}
