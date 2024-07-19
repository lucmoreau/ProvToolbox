package org.openprovenance.prov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.StreamingOutput;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.client.ProcessorArgsInterface;
import org.openprovenance.prov.client.RecordsProcessorInterface;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.service.readers.TemplatesVizConfig;
import org.openprovenance.prov.template.library.plead.Plead_trainingBuilder;
import org.openprovenance.prov.template.library.plead.sql.access_control.SqlCompositeBeanEnactor4;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.service.TemplateService.*;
import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.library.plead.sql.access_control.BeanEnactor4.getPrincipal;

public class TemplateLogic {


    public static final String HTTP_HEADER_CONTENT_PROV_HASH = "PROV-Hash";
    public static final String HTTP_HEADER_LOCATION = "Location";
    public static final String HTTP_HEADER_ACCEPT_PROV_HASH = "Accept-PROV-hash";
    static Logger logger = LogManager.getLogger(TemplateLogic.class);
    private final ProvFactory pf;
    private final TemplateDispatcher templateDispatcher;
    private final Map<String, FileBuilder> documentBuilderDispatcher;
    private final ServiceUtils utils;
    private final ObjectMapper om;
    private final SqlCompositeBeanEnactor4 sqlCompositeBeanEnactor;

    private final EnactCsvRecords<Object> enactCsvRecords= new EnactCsvRecords<>();
    private final TemplateQuery templateQuery;
    private final Map<String, Map<String, Set<String>>> typeAssignment;


    public TemplateLogic(ProvFactory pf, TemplateQuery templateQuery, TemplateDispatcher templateDispatcher, Object o1, Map<String, FileBuilder> documentBuilderDispatcher, ServiceUtils utils, ObjectMapper om, SqlCompositeBeanEnactor4 sqlCompositeBeanEnactor, Map<String, Map<String, Set<String>>> typeAssignment) {
        this.pf = pf;
        this.templateDispatcher = templateDispatcher;
        this.documentBuilderDispatcher = documentBuilderDispatcher;
        this.utils = utils;
        this.om = om;
        om.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        this.sqlCompositeBeanEnactor = sqlCompositeBeanEnactor;
        this.templateQuery = templateQuery;
        this.typeAssignment = typeAssignment;
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


    public void generateViz(TemplatesVizConfig config, String principal, OutputStream out) {

        typeAssignment.entrySet().removeIf(entry -> entry.getValue() ==null || entry.getValue().isEmpty());

        Map<String,Map<String,String>> baseTypes
                = typeAssignment
                .keySet()
                .stream()
                .collect(Collectors
                        .toMap(tpl -> tpl,
                                tpl ->
                                        typeAssignment
                                                .get(tpl)
                                                .keySet()
                                                .stream()
                                                .collect(Collectors
                                                        .toMap(var->var,
                                                                var -> preferredType(typeAssignment
                                                                        .get(tpl)
                                                                        .get(var))))));

        logger.info("baseTypes " + baseTypes);
        templateQuery.generateViz(config.id, config.template, config.property, baseTypes, principal, out);
    }


    private int colorValue(String s) {
        if (s==null) return 0;
        switch (s) {
            case "http://www.w3.org/ns/prov#Entity":
                return 1;
            case "http://www.w3.org/ns/prov#Activity":
                return 2;
            case "http://www.w3.org/ns/prov#Agent":
                return 3;
            default:
                return 0;
        }
    }

    private String preferredType(Set<String> value) {
        return value.stream().max(Comparator.comparingInt(this::colorValue)).orElse("none");
    }

    private Map<String, Set<String>> plead_trainingLevel0() {
        String [] propertyOrder=templateDispatcher.getPropertyOrder().get("plead_training");
        Plead_trainingBuilder plead_trainingBuilder = (Plead_trainingBuilder) documentBuilderDispatcher.get("plead_training");
        Object[] record2=plead_trainingBuilder.make(plead_trainingBuilder.getTypedRecord()); //new Plead_trainingBuilderTypedRecord());
        Map<QualifiedName, Set<String>> knownTypeMap=new HashMap<>();

        plead_trainingBuilder.make(plead_trainingBuilder.getTypeManager(knownTypeMap, new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>()));
        Map<String,Set<String>> propertyMap=new HashMap<>();
        for (int i=0; i<record2.length; i++) {
            String property=propertyOrder[i];
            Object value=record2[i];
            if (value instanceof QualifiedName) {
                propertyMap.put(property, knownTypeMap.get((QualifiedName) value));
            }
        }


        Map<String, Set<String>> propertyMap2=typeAssignment.get("plead_training");

        System.out.println("propertyMap " + propertyMap);
        System.out.println("propertyMap2 " + propertyMap2);


        return propertyMap;
    }

    public List<TemplateQuery.RecordEntry2> generateLiveNode(String relation, Integer id, String principal) {
        List<TemplateQuery.RecordEntry2> records=templateQuery.queryTemplatesRecordsById(relation, id, 30, principal);
        //System.out.println("records " + records);
        return records;
    }

    public Object submitPostProcessing(int id, String template) {
        logger.info("postProcessing " + id + " " + template);

        String principal=getPrincipal();
        List<Object[]> records = templateQuery.query(template, id, false, principal);

        logger.info("PROV_API " + provAPI);
        logger.info("records " + records.size() + " " + records.stream().map(Arrays::toString).collect(Collectors.joining(",")));
        if (!records.isEmpty()) {
            if (records.size()>1) {
                Map<String, String> hash = templateQuery.computeHash(template, id, records);
                templateQuery.updateHash(template, id, hash, principal);
                logger.info("update hash for " + id + " " + template + ": " + hash);
                headerInfo.get().put(HTTP_HEADER_CONTENT_PROV_HASH, getContentProvHash(hash));
                headerInfo.get().put(HTTP_HEADER_LOCATION,provAPI + "/template/" + template + "/" + id);
            } else {
                Object[] record = records.get(0);
                Map<String, String> hash = templateQuery.computeHash(template, id, record);
                templateQuery.updateHash(template, id, hash, principal);
                logger.info("update hash for " + id + " " + template + ": " + hash);
                headerInfo.get().put(HTTP_HEADER_CONTENT_PROV_HASH,getContentProvHash(hash));
                headerInfo.get().put(HTTP_HEADER_LOCATION,provAPI + "/template/" + template + "/" + id);


            }
        }
        return null;
    }
    Base64.Encoder encoder = Base64.getEncoder();

    private String getContentProvHash(Map<String, String> hash) {
        return encoder.encodeToString(templateQuery.makeHashRecord(hash).getBytes());
    }
}
