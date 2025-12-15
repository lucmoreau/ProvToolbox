package org.openprovenance.prov.service.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.StreamingOutput;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.model.interop.PrincipalManager;
import org.openprovenance.prov.scala.iface.Explainer;
import org.openprovenance.prov.scala.iface.XFactory;
import org.openprovenance.prov.service.core.dispatch.EnactCsvRecords;
import org.openprovenance.prov.service.core.readers.TemplatesVizConfig;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openprovenance.prov.service.core.TemplateService.*;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class TemplateLogic {


    public static final String HTTP_HEADER_CONTENT_PROV_HASH = "PROV-Hash";
    public static final String HTTP_HEADER_LOCATION = "Location";
    public static final String HTTP_HEADER_ACCEPT_PROV_HASH = "Accept-PROV-hash";
    public static final String HTTP_HEADER_ACCEPT_PROV_EXPLANATION = "Accept-PROV-explanation";
    static Logger logger = LogManager.getLogger(TemplateLogic.class);
    private final ProvFactory pf;
    private final CatalogueDispatcherInterface<FileBuilder> templateDispatcher;
    private final Map<String, FileBuilder> documentBuilderDispatcher;
    private final ServiceUtils utils;
    private final ObjectMapper om;

    private final EnactCsvRecords<Object> enactCsvRecords= new EnactCsvRecords<>();
    private final TemplateQuery templateQuery;
    private final Map<String, Map<String, Set<String>>> typeAssignment;
    private final Map<String, Map<String, List<String>>> successors;
    private final PrincipalManager principalManager;
    private final Map<String, String> shortNames;


    public TemplateLogic(ProvFactory pf, TemplateQuery templateQuery, Map<String, String> shortNames, CatalogueDispatcherInterface<FileBuilder> templateDispatcher, PrincipalManager principalManager, ServiceUtils utils, ObjectMapper om) {
        this.pf = pf;
        this.templateDispatcher = templateDispatcher;
        this.documentBuilderDispatcher = templateDispatcher.getDocumentBuilderDispatcher();
        this.utils = utils;
        this.om = om;
        om.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        this.templateQuery = templateQuery;
        this.typeAssignment = templateDispatcher.getTypeAssignment();
        this.successors = templateDispatcher.getSuccessors();
        this.principalManager = principalManager;
        this.shortNames=shortNames;
    }

    public List<Object> processIncomingJson(List<Map<String, Object>> entries) {
        //logger.info("Processing incoming JSON " + entries);

        // assumption: all entries use the same template
        final String isA=(String) entries.get(0).get(IS_A);
        List<Object> recordsResult=new LinkedList<>();
        Map<String, String[]> properties= templateDispatcher.getPropertyOrder();
        Map<String, Function<Object[],?>> enactorConverters= templateDispatcher.getEnactorConverter();
        Map<String, Function<List<Object[]>,?>> compositeEnactorConverters= templateDispatcher.getCompositeEnactorConverter();



        String[] props=properties.get(isA);
        Function<Object[],?> enactor=enactorConverters.get(isA);


        if (enactor!=null) {
            //debugDisplay("entries ", entries) ;
            for (Map<String, Object> entry : entries) {
                Object[] array = convertToArray(entry, props);
                //debugDisplay("array ", array) ;
                recordsResult.add(enactor.apply(array));
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


                //System.out.println("need composite converter for " + isA);
                //System.out.println("need composite converter for " + compositeEnactorConverters);
                Function<List<Object[]>,?> compositeEnactor = compositeEnactorConverters.get(isA);
                //System.out.println("found composite converter for " + compositeEnactor);
                Object res=compositeEnactor.apply(objects);
                //debugDisplay("found result " , res);
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
        Map<String, Function<Object[],?>> enactors = templateDispatcher.getEnactorConverter();
        Map<String, Function<Object[],Object>> enactors2= enactors.entrySet().stream().filter(e->e.getValue()!=null).collect(Collectors.toMap(Map.Entry::getKey, e -> (Function<Object[],Object>) e.getValue()));

        Map<String, Function<List<Object[]>,?>> compositeEnactors= templateDispatcher.getCompositeEnactorConverter();
        Map<String, Function<List<Object[]>,Object>> compositeEnactors2= compositeEnactors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (Function<List<Object[]>,Object>) e.getValue()));
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

        //logger.info("typeAssignment " + typeAssignment);

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

        //logger.info("baseTypes " + baseTypes);
        templateQuery.generateViz(config.id, config.template, config.property, config.style, baseTypes, principal, out);
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
        if (value==null) return "none";
        return value.stream().max(Comparator.comparingInt(this::colorValue)).orElse("none");
    }


    public List<TemplateQuery.RecordEntry2> generateLiveNode(String relation, Integer id, String principal) {
        List<TemplateQuery.RecordEntry2> records=templateQuery.queryTemplatesRecordsById(relation, id, 30, principal);
        //System.out.println("records " + records);
        return records;
    }

    public Object submitPostProcessing(int id, String templateFullyQualifiedName) {
        logger.info("postProcessing " + id + " " + templateFullyQualifiedName);

        String principal=principalManager.getPrincipal();
        List<Object[]> records = templateQuery.query(templateFullyQualifiedName, id, false, principal);

        logger.debug("PROV_API " + provAPI);
        logger.info("records " + records.size() + " " + records.stream().map(Arrays::toString).collect(Collectors.joining(",")));
        if (!records.isEmpty()) {
            if (records.size()>1) {
                Map<String, String> hash = templateQuery.computeHash(templateFullyQualifiedName, id, records);
                templateQuery.updateHash(shortNames.get(templateFullyQualifiedName), id, hash, principal);
                logger.info("update hash for " + id + " " + templateFullyQualifiedName + ": " + hash);
                headerInfo.get().put(HTTP_HEADER_CONTENT_PROV_HASH, getContentProvHash(hash));
            } else {
                Object[] record = records.get(0);
                Map<String, String> hash = templateQuery.computeHash(templateFullyQualifiedName, id, record);
                templateQuery.getRelationMapping().mapGraphToRelations(templateFullyQualifiedName,id,record);
                templateQuery.updateHash(shortNames.get(templateFullyQualifiedName), id, hash, principal);
                logger.info("update hash for " + id + " " + templateFullyQualifiedName + ": " + hash);
                headerInfo.get().put(HTTP_HEADER_CONTENT_PROV_HASH,getContentProvHash(hash));
            }
            headerInfo.get().put(HTTP_HEADER_LOCATION,provAPI + "/template/" + templateFullyQualifiedName + "/" + id);

        }
        return null;
    }
    Base64.Encoder encoder = Base64.getEncoder();

    private String getContentProvHash(Map<String, String> hash) {
        return encoder.encodeToString(templateQuery.makeHashRecord(hash).getBytes());
    }

    final ProvFactory pFactoryS = org.openprovenance.prov.scala.immutable.ProvFactory.pf();
    final XFactory factory = new XFactory();
    final Explainer explainer=factory.makeExplainer();
    final org.openprovenance.prov.scala.iface.Narrator narrator=factory.makeNarrator();

    public Map<String, String> generateExplanation(String template, Integer id, String headerAcceptExplanation, Document doc) {

        org.openprovenance.prov.scala.immutable.Document sdoc = (org.openprovenance.prov.scala.immutable.Document) new BeanTraversal(pFactoryS, pFactoryS).doAction(doc);

        XplainerConfig config = new XplainerConfig();
        //scala.collection.immutable.Map<String, Narrative> foo=explainer.explain(sdoc, config);
        //System.out.println("foo " + foo);
        scala.collection.immutable.Map<String, scala.collection.immutable.List<String>> result = narrator.getTextOnly(explainer.explain(sdoc, config));

        // convert result to java map
        Map<String, String> javaResult = new HashMap<>();
        for (Map.Entry<String, scala.collection.immutable.List<String>> entry : scala.jdk.CollectionConverters.MapHasAsJava(result).asJava().entrySet()) {
            javaResult.put(entry.getKey(), entry.getValue().mkString("\n"));
        }

        //result.put("default", "No explanation available for " + template + " " + id);
        return javaResult;
    }
}
