package org.openprovenance.prov.template.types;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.template.log2prov.DocumentProcessor;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.RecordProcessor;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ProvenanceKernels {
    final static boolean debug=true;

    public final static TypeReference<Collection<String>> collRef = new TypeReference<>() {};
    public final static TypeReference<Map<String,Integer>> mapRef = new TypeReference<>() {};
    public final static TypeReference<Map<String,String>> mapStringRef = new TypeReference<>() {};
    public final static TypeReference<Map<String,Map<String,List<String>>>> propertyConverterRef = new TypeReference<>() {};


    public static void main(String [] args) throws IOException {
        Map<String, Object> result=new ProvenanceKernels().do_main(args);
        global_result=result;
    }

    public Map<String, Object> do_main(String [] args) throws IOException {

        String [] args0=Arrays.copyOfRange(args,1,args.length);
        PK_CommandLineArguments cliArgs=PK_CommandLineArguments.parse("<Executable> " + args[0], args0);
        ObjectMapper om=new ObjectMapper();
        if (cliArgs!=null) {
            String debug= cliArgs.debug;
            String infile = cliArgs.infile;
            String outfile = cliArgs.outfile;
            String knowntypesFile = cliArgs.knowntypes;
            String knownrelationsFile = cliArgs.knownrelations;
            String rejectedTypesFile = cliArgs.rejectedTypes;
            int setOffset= cliArgs.setOffset;
            int relationOffset= cliArgs.relationOffset;
            int levelOffset= cliArgs.levelOffset;
            String translationFile = cliArgs.translation;
            int levelNumber = cliArgs.levelNumber;
            boolean addLevel0ToAllLevels=cliArgs.addLevel0ToAllLevels;
            String propertyConverters=cliArgs.propertyConverters;
            String idataConverters=cliArgs.idataConverters;


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


            Map<String, String> translation=((translationFile==null)||(translationFile.equals("")))?Map.of():om.readValue(new File(translationFile),mapStringRef);

            Map<Set<Integer>,Integer> knownTypesSets=knownTypes.keySet().stream().collect(Collectors.toMap(k->Set.of(knownTypes.get(k)), knownTypes::get));

            List<Pair<String,Object[]>> records=new LinkedList<>();

            final Map<String,Integer> knownRelations=(knownrelationsFile==null)?  new HashMap<>(): om.readValue(new File(knownrelationsFile),mapRef) ;

            final Map<String,Integer> knownRelations2 = knownRelations.keySet().stream().collect(Collectors.toMap(r -> r, r -> knownRelations.get(r) + relationOffset));

            Map<String,Map<String,List<String>>> propertyConvertersMap= loadPropertyConvertersMap(propertyConverters, om);
            Map<String,Map<String,List<String>>> idataConvertersMap= loadPropertyConvertersMap(idataConverters, om);

            Collection<String> rejectedTypes=new LinkedList<>();

            if (rejectedTypesFile!=null) {
                rejectedTypes = om.readValue(new File(rejectedTypesFile), collRef);
            }

            TypesRecordProcessor trp = newTypesRecordProcessor(relationOffset, levelOffset, levelNumber, addLevel0ToAllLevels, knownTypes, translation, knownTypesSets, records, knownRelations2, propertyConvertersMap, idataConvertersMap, rejectedTypes);

            Map<String, Object> result=FileBuilder.reader(is,dp,rp,trp);

            if (outfile!=null) {
                om.writerWithDefaultPrettyPrinter().writeValue(new FileOutputStream(outfile), result);
            }

            System.out.println("completed Provenance Kernels");

            return result;


        }
        return null;
    }

    static public Map<String, Map<String, List<String>>> loadPropertyConvertersMap(String propertyConverters, ObjectMapper om) throws IOException {
        return (propertyConverters == null) ? Map.of() : om.readValue(new File(propertyConverters), propertyConverterRef);
    }

    public TypesRecordProcessor newTypesRecordProcessor(int relationOffset, int levelOffset, int levelNumber, boolean addLevel0ToAllLevels, Map<String, Integer> knownTypes, Map<String, String> translation, Map<Set<Integer>, Integer> knownTypesSets, List<Pair<String, Object[]>> records, Map<String, Integer> knownRelations2, Map<String, Map<String, List<String>>> propertyConvertersMap, Map<String, Map<String, List<String>>> idataConvertersMap, Collection<String> rejectedTypes) {
        TypesRecordProcessor trp=new TypesRecordProcessor(knownTypes, knownTypesSets, knownRelations2, relationOffset, levelOffset, translation, levelNumber, addLevel0ToAllLevels, propertyConvertersMap, idataConvertersMap, rejectedTypes, records);
        return trp;
    }

    public static Map<String, Object> global_result=null;

}
