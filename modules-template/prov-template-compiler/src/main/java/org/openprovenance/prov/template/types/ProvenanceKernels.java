package org.openprovenance.prov.template.types;


import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.template.log2prov.DocumentProcessor;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.RecordProcessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ProvenanceKernels {
    final static boolean debug=true;

    public static void main (String [] args) throws IOException {
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

        TypesRecordProcessor trp=new TypesRecordProcessor(knownTypes,knownTypesSets,records);

       
        FileBuilder.reader(is,dp,rp,trp);

    }



}
