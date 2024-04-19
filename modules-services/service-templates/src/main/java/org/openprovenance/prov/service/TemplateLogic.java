package org.openprovenance.prov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.StreamingOutput;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.client.ProcessorArgsInterface;
import org.openprovenance.prov.client.RecordsProcessorInterface;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.service.iobean.composite.SqlCompositeBeanEnactor3;
import org.openprovenance.prov.template.compiler.sql.QueryBuilder;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.sql.QueryBuilder.*;

public class TemplateLogic {

    public static final String INPUT_VALUE = "__input_id";
    public static final String INPUT_PROPERTY = "__input_property";
    public static final String INPUT_TEMPLATE = "__input_template";
    static Logger logger = LogManager.getLogger(TemplateLogic.class);
    private final ProvFactory pf;
    private final TemplateDispatcher templateDispatcher;
    private final Map<String, FileBuilder> documentBuilderDispatcher;
    private final ServiceUtils utils;
    private final ObjectMapper om;
    private final SqlCompositeBeanEnactor3 sqlCompositeBeanEnactor3;

    private final EnactCsvRecords<Object> enactCsvRecords= new EnactCsvRecords<>();

    final Map<String,Map<String, Map<String, String>>> ioMap;

    public TemplateLogic(ProvFactory pf, Object o, TemplateDispatcher templateDispatcher, Object o1, Map<String, FileBuilder> documentBuilderDispatcher, ServiceUtils utils, ObjectMapper om, SqlCompositeBeanEnactor3 sqlCompositeBeanEnactor3) {
        this.pf = pf;
        this.templateDispatcher = templateDispatcher;
        this.documentBuilderDispatcher = documentBuilderDispatcher;
        this.utils = utils;
        this.om = om;
        om.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        this.sqlCompositeBeanEnactor3 = sqlCompositeBeanEnactor3;

        this.ioMap = setIoMap();

        System.out.println(generateBackwardTemplateTraversal(ioMap));

    }

    public List<Object> processIncomingJson(List<Map<String, Object>> entries) {
        //logger.info("Processing incoming JSON " + entries);

        // assumption: all entries use the same template
        final String isA=(String) entries.get(0).get(IS_A);
        List<Object> recordsResult=new LinkedList<>();
        Map<String, String[]> properties= templateDispatcher.getPropertyOrder();
        Map<String, ProcessorArgsInterface<?>> enactorConverters= templateDispatcher.getEnactorConverter();
        Map<String, RecordsProcessorInterface<?>> compositeEnactorConverters= templateDispatcher.getCompositeEnactorConverter();



        String[] props=properties.get(isA);
        ProcessorArgsInterface<?> enactor=enactorConverters.get(isA);


        if (enactor!=null) {
            //debugDisplay("entries ", entries) ;
            for (Map<String, Object> entry : entries) {
                Object[] array = convertToArray(entry, props);
                //debugDisplay("array ", array) ;
                recordsResult.add(enactor.process(array));
            }
        } else {
            for (Map<String, Object> composite : entries) {
                List<Object[]> objects = new LinkedList<>();
                Object[] array = convertToArray(composite, props);
                objects.add(array);

                for (Map<String, Object> composee: (List<Map<String, Object>>) composite.get(ELEMENTS)) {

                    String is2A=(String) composee.get(IS_A);
                    String[] props2=properties.get(is2A);
                    Object[] array2 = convertToArray(composee, props2);
                    objects.add(array2);
                }


                //debugDisplay("founds objects " , objects);


                System.out.println("need composite converter for " + isA);
                System.out.println("need composite converter for " + compositeEnactorConverters);
                RecordsProcessorInterface<?> compositeEnactor = compositeEnactorConverters.get(isA);
                System.out.println("found composite converter for " + compositeEnactor);
                Object res=compositeEnactor.process(objects);
                debugDisplay("found result " , res);
                recordsResult.add(res);
            }
        }
        return recordsResult;
    }

    public Object[] convertToArray(Map<String, Object> entry, String[] props) {
        Object[] array = new Object[props.length];
        for (int i = 0; i < props.length; i++) {
            String prop = props[i];
            array[i] = entry.getOrDefault(prop, null);
        }

        return array;
    }

    private void debugDisplay(String msg, Object object) {
        try {
            System.out.println(msg + om.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> processIncomingCsv(CSVParser csv) {
        Collection<CSVRecord> collection=csv.getRecords();
        Map<String, ProcessorArgsInterface<?>> enactors = templateDispatcher.getEnactorConverter();
        Map<String, ProcessorArgsInterface<Object>> enactors2= enactors.entrySet().stream().filter(e->e.getValue()!=null).collect(Collectors.toMap(Map.Entry::getKey, e -> (ProcessorArgsInterface<Object>) e.getValue()));

        Map<String, RecordsProcessorInterface<?>> compositeEnactors= templateDispatcher.getCompositeEnactorConverter();
        Map<String, RecordsProcessorInterface<Object>> compositeEnactors2= compositeEnactors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (RecordsProcessorInterface<Object>) e.getValue()));
        List<Object> newRecords=enactCsvRecords.process(collection, enactors2, compositeEnactors2);

        return newRecords;
    }

    @NotNull
    public StreamingOutput streamOutRecordsToCSV(List<Object> newRecords) {
        return out -> newRecords.forEach(record -> {
            try {
                out.write(((String)record).getBytes(StandardCharsets.UTF_8));
                out.write('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    String ioMapString="{\"output\":{\"plead_validating\":{\"score\":\"score\",\"validating\":\"activity\"},\"plead_approving\":{\"approving\":\"activity\",\"approved_pipeline\":\"file\"},\"plead_filtering\":{\"filtered_file\":\"file\",\"filtering\":\"activity\"},\"plead_splitting\":{\"splitting\":\"activity\",\"split_file2\":\"file\",\"split_file1\":\"file\"},\"plead_training\":{\"pipeline\":\"file\",\"training\":\"activity\"},\"plead_transforming\":{\"transformed_file\":\"file\",\"transforming\":\"activity\"}},\"input\":{\"plead_validating\":{\"testing_dataset\":\"file\"},\"plead_approving\":{\"pipeline\":\"file\",\"score\":\"score\"},\"plead_filtering\":{\"file\":\"file\",\"method\":\"method\"},\"plead_splitting\":{\"file\":\"file\"},\"plead_training\":{\"training_dataset\":\"file\"},\"plead_transforming\":{\"file\":\"file\",\"method\":\"method\"}}}";


    TypeReference<Map<String,Map<String, Map<String, String>>>> typeRef = new TypeReference<>() {};

    public Map<String, Map<String, Map<String, String>>> setIoMap() {
        try {
            return om.readValue(ioMapString, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateBackwardTemplateTraversal(Map<String,Map<String, Map<String, String>>> ioMap) {
        String backwardTraversalFunctionName="backwardTraversal";

        Map<String,?> funParams=new LinkedHashMap<>() {{
            put(INPUT_TEMPLATE, unquote("text"));
            put(INPUT_PROPERTY, unquote("text"));
            put(INPUT_VALUE, unquote("INT"));

        }};
        Map<String,Object> functionReturns= new LinkedHashMap<>() {{
            put("src_id",                 unquote("INT"));
            put("src_template",    unquote("text"));
            put("src_property",    unquote("text"));
            put("dst_id",                 unquote("INT"));
            put("dst_template",                 unquote("text"));
            put("dst_property",                 unquote("text"));

        }};


        QueryBuilder fun=
                new QueryBuilder()
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLSearchRecordFunction")
                        .next(createFunction(backwardTraversalFunctionName))
                        .params(funParams)
                        .returns("table", functionReturns)
                        .bodyStart("");
        Set<String> allTables=ioMap.get("output").values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toSet());
        allTables.addAll(ioMap.get("input").values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toSet()));

        System.out.println("allTables = " + allTables);

        System.out.println("* successors: " + templateDispatcher.getSuccessors());
        System.out.println("* predecessors: " + templateDispatcher.getPredecessors());

        System.out.println(ioMap.get("input"));
        System.out.println(ioMap.get("output"));
        Map<String, Map<String, String>> input = ioMap.get("input");
        Map<String, Map<String, String>> output = ioMap.get("output");

        boolean before=false;

        for (String table: allTables) {

            System.out.println("table = " + table);



            Map<String, Map<String, String>> input_table=
                    filterMapAccordingToTable(table, input);
            Map<String, Map<String, String>> output_table=
                    filterMapAccordingToTable(table, output);

            /*
            try {
                System.out.println("  - input_table = " + om.writeValueAsString(input_table));
                System.out.println("  - output_table = " + om.writeValueAsString(output_table));

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

             */



            for (String dst_template: input_table.keySet()) {
                if (!input_table.get(dst_template).keySet().isEmpty()) {
                    fun.comment(" + dst_template = " + dst_template);
                    for (String dst_property : input_table.get(dst_template).keySet()) {
                        for (String src_template : output_table.keySet()) {
                            if (!output_table.get(src_template).keySet().isEmpty()) {
                                for (String src_property : output_table.get(src_template).keySet()) {
                                    String dst_templatex=dst_template;
                                    String src_templatex=src_template;

                                    if(dst_template.equals(src_template)) {
                                        dst_templatex = dst_template + "_dst";
                                        src_templatex = src_template + "_src";
                                    }

                                    String [] args={src_templatex + ".id as src_id", "'" + src_template + "' as src_template", "'" + src_property + "' as src_property",
                                                    INPUT_VALUE   + "    as dst_id", "'" + dst_template + "' as dst_template", "'" + dst_property + "' as dst_property"};
                                    if (before) {
                                        fun.newline().union(pp -> select(args).apply(pp));
                                    } else {
                                        fun.selectExp(args);
                                        before = true;
                                    }
                                    fun.from(dst_template);
                                    if (!dst_template.equals(dst_templatex)) {
                                        fun.alias(dst_templatex);
                                    }
                                    fun.join(src_template);
                                    if (!src_template.equals(src_templatex)) {
                                        fun.alias(src_templatex);
                                    }
                                    fun.on (src_templatex + "." +  src_property + " = " + dst_templatex  + "." +  dst_property )
                                            .and(           unquote(INPUT_PROPERTY) + " = '" + dst_property + "'")
                                            .and( dst_templatex + ".id=" + INPUT_VALUE);
                                }
                            }
                        }
                    }
                }
            }

        }

        return fun.bodyEnd("").getSQL();

    }

    private Map<String, Map<String, String>> filterMapAccordingToTable(String table, Map<String, Map<String, String>> input) {
        return input.keySet().stream().collect(Collectors.toMap(k -> k, k -> input.get(k).keySet().stream().filter(k2 -> input.get(k).get(k2).equals(table)).collect(Collectors.toMap(k2 -> k2, k2 -> input.get(k).get(k2)))));
    }

}
