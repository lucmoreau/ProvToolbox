package org.openprovenance.prov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.client.ProcessorArgsInterface;
import org.openprovenance.prov.client.RecordsProcessorInterface;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.service.iobean.composite.SqlCompositeBeanEnactor3;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.common.Constants.ELEMENTS;
import static org.openprovenance.prov.template.compiler.common.Constants.IS_A;

public class TemplateLogic {

    static Logger logger = LogManager.getLogger(TemplateLogic.class);
    private final ProvFactory pf;
    private final TemplateDispatcher templateDispatcher;
    private final Map<String, FileBuilder> documentBuilderDispatcher;
    private final ServiceUtils utils;
    private final ObjectMapper om;
    private final SqlCompositeBeanEnactor3 sqlCompositeBeanEnactor3;

    private final EnactCsvRecords<Object> enactCsvRecords= new EnactCsvRecords<>();

    public TemplateLogic(ProvFactory pf, Object o, TemplateDispatcher templateDispatcher, Object o1, Map<String, FileBuilder> documentBuilderDispatcher, ServiceUtils utils, ObjectMapper om, SqlCompositeBeanEnactor3 sqlCompositeBeanEnactor3) {
        this.pf = pf;
        this.templateDispatcher = templateDispatcher;
        this.documentBuilderDispatcher = documentBuilderDispatcher;
        this.utils = utils;
        this.om = om;
        this.sqlCompositeBeanEnactor3 = sqlCompositeBeanEnactor3;

    }

    public Response processIncomingJson(List<Map<String, Object>> entries) {
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
        StreamingOutput promise= out -> om.writeValue(out,recordsResult);
        return ServiceUtils.composeResponseOK(promise).build();
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

    public Response processIncomingCsv(CSVParser csv) {
        Collection<CSVRecord> collection=csv.getRecords();
        Map<String, ProcessorArgsInterface<?>> enactors = templateDispatcher.getEnactorConverter();
        Map<String, ProcessorArgsInterface<Object>> enactors2= enactors.entrySet().stream().filter(e->e.getValue()!=null).collect(Collectors.toMap(Map.Entry::getKey, e -> (ProcessorArgsInterface<Object>) e.getValue()));

        Map<String, RecordsProcessorInterface<?>> compositeEnactors= templateDispatcher.getCompositeEnactorConverter();
        Map<String, RecordsProcessorInterface<Object>> compositeEnactors2= compositeEnactors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (RecordsProcessorInterface<Object>) e.getValue()));
        Map<String, RecordsProcessor<Object>> enactorsN = null;
        List<Object> newRecords=enactCsvRecords.process(collection, enactors2, null);

        StreamingOutput promise= out -> om.writeValue(out,newRecords);
        return ServiceUtils.composeResponseOK(promise).build();
    }
}
