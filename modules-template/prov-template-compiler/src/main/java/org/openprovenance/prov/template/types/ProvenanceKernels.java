package org.openprovenance.prov.template.types;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.template.log2prov.DocumentProcessor;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.RecordProcessor;

import java.io.*;
import java.util.*;

public class ProvenanceKernels {
    final static boolean debug=true;

    static TypeReference<Map<String,Integer>> mapRef = new TypeReference<>() {};


    public static void main(String [] args) throws IOException {
        String [] args0=Arrays.copyOfRange(args,1,args.length);
        PK_CommandLineArguments cliArgs=PK_CommandLineArguments.parse("<Executable> " + args[0], args0);
        ObjectMapper om=new ObjectMapper();
        if (cliArgs!=null) {
            String debug= cliArgs.debug;
            String infile = cliArgs.infile;
            String outfile = cliArgs.outfile;
            String knowntypesFile = cliArgs.knowntypes;


            InputStream is= ("-".equals(infile))? System.in: new FileInputStream(infile);
            DocumentProcessor dp=new NullDocumentProcessor ();
            RecordProcessor rp=null;

            Map<String,Integer> knownTypes;
            if (knowntypesFile!=null) {
                knownTypes=om.readValue(new File(knowntypesFile),mapRef);
            } else {
                knownTypes = Map.of(
                        "http://www.w3.org/ns/prov#Entity", 1,
                        "http://www.w3.org/ns/prov#Activity", 2,
                        "http://www.w3.org/ns/prov#Agent", 3);
            }


            int setOffset=100000;

            Map<Set<Integer>,Integer> knownTypesSets=Map.of(
                    Set.of(1) ,1+  setOffset,
                    Set.of(2), 2+  setOffset,
                    Set.of(3), 3+  setOffset);

            List<Pair<String,Object[]>> records=new LinkedList<>();

            int relationOffset=2000;

            Map<String,Integer> knownRelations=Map.of(
                    "template_block.produced.wdf.consumed1", relationOffset+0,
                    "template_block.produced.wdf.consumed2", relationOffset+1       );
            

            TypesRecordProcessor trp=new TypesRecordProcessor(knownTypes,knownTypesSets,knownRelations, records);


            Map<String, Object> result=FileBuilder.reader(is,dp,rp,trp);
            if (outfile!=null) {
                om.writerWithDefaultPrettyPrinter().writeValue(new FileOutputStream(outfile), result);
            }

        }
    }

    /*
    public static void mainOld (String [] args) throws IOException {
        String in=args[1];
        String out=args[2];
        if (debug) System.out.println("Start kernel main " + in);





        InputStream is= ("-".equals(in))? System.in: new FileInputStream(in);
        DocumentProcessor dp=new NullDocumentProcessor ();
        RecordProcessor rp=null;

        Map<String,Integer> knownTypes=Map.of(
                "http://www.w3.org/ns/prov#Entity" ,1,
                "http://www.w3.org/ns/prov#Activity", 2,
                "http://www.w3.org/ns/prov#Agent", 3);


        int setOffset=100000;

        Map<Set<Integer>,Integer> knownTypesSets=Map.of(
                Set.of(1) ,1+  setOffset,
                Set.of(2), 2+  setOffset,
                Set.of(3), 3+  setOffset);

        List<Pair<String,Object[]>> records=new LinkedList<>();

        int relationOffset=2000;

        Map<String,Integer> knownRelations=Map.of(
                "template_block.produced.wdf.consumed1", relationOffset+0,
                "template_block.produced.wdf.consumed2", relationOffset+1       );



        TypesRecordProcessor trp=new TypesRecordProcessor(knownTypes,knownTypesSets,knownRelations, records);

       
        Map<String, Object> result=FileBuilder.reader(is,dp,rp,trp);
        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new FileOutputStream(out),result);

    }



     */
}
