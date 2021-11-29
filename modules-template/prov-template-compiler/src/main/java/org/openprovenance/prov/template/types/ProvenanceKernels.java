package org.openprovenance.prov.template.types;


import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.log2prov.DocumentProcessor;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.RecordProcessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProvenanceKernels {
    final static boolean debug=true;

    public static void main (String [] args) throws IOException {
        System.out.println("args.length: " + args.length);
        String in=args[1];
        String out=args[2];
        if (debug) System.out.println("Start kernel main " + in);

        InputStream is= ("-".equals(in))? System.in: new FileInputStream(in);
        DocumentProcessor dp=new NullDocumentProcessor ();
        RecordProcessor rp=null;
        TypesRecordProcessor trp=new TypesRecordProcessor();

        Map<QualifiedName, Set<String>> knownTypeMap=new HashMap<>();
        Map<QualifiedName, Set<String>> unknownTypeMap=new HashMap<>();


        FileBuilder.reader(is,dp,rp,trp);

        if (debug) System.out.println("End main");
    }



}
