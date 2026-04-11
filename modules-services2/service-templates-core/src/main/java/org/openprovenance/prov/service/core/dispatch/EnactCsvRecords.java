package org.openprovenance.prov.service.core.dispatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.apache.commons.lang.StringEscapeUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnactCsvRecords<T> {
    static Logger logger = LogManager.getLogger(EnactCsvRecords.class);





    @SuppressWarnings("unchecked")
    public List<T> process(Collection<CSVRecord> records, Map<String, Function<Object[],T>> enactors, Map<String,  Function<List<Object[]>,T>> enactors_N) {
        return (List<T>) processInternal(records, enactors, enactors_N, null, null);
    }

    public List<Object> process(Collection<CSVRecord> records, Map<String, Function<Object[],T>> enactors, Map<String,  Function<List<Object[]>,T>> enactors_N, Map<String, Function<Object,String>> csvConverter4Outputs, Map<String, Function<Object, String>> csvConverter4OutputsComposite) {
        return processInternal(records, enactors, enactors_N, csvConverter4Outputs,csvConverter4OutputsComposite);
    }

    private List<Object> processInternal(Collection<CSVRecord> records, Map<String, Function<Object[],T>> enactors, Map<String, Function<List<Object[]>,T>> enactors_N, Map<String, Function<Object,String>> csvConverter4Outputs,Map<String, Function<Object, String>> csvConverter4OutputsComposite) {

        List<Object> populatedRecords=new LinkedList<>();

        CSVRecord record0=records.iterator().next();
        int size0=record0.size();
        Object[] args0=new Object[size0];
        String method = populateRecordAndExtractMethod(record0, size0, args0);
        Function<Object[],T> processor_1=enactors.get(method);
        Function<List<Object[]>,T> processor_N=enactors_N.get(method);

        // NOTE
        // distinguish the processor for a single record (enactors.get(method)) from the processor for N (enactors_N.get(method)).
        // Assumption: we receive a single record, or we receive multiple records of the same type
        if (processor_N==null) {
            if (processor_1!=null) {
                Function<Object,Object> outputTransformer = (csvConverter4Outputs != null) ? csvConverter4Outputs.get(method)::apply : t -> t;
                for (CSVRecord record : records) {
                    int size = record.size();
                    Object[] args = new Object[size];
                    populateRecordAndExtractMethod(record, size, args);
                    populatedRecords.add(outputTransformer.apply(processor_1.apply(args)));
                }
            } else {
                throw new EnactorException("Unknown method " + method, method);
            }
        } else {
            Function<Object,Object> outputTransformer = (csvConverter4OutputsComposite != null) ? csvConverter4OutputsComposite.get(method)::apply : t -> t;
            List<Object[]> ll = records.stream().map(record -> {
                int size = record.size();
                Object[] args = new Object[size];
                populateRecordAndExtractMethod(record, size, args);
                return args;
            }).collect(Collectors.toList());
            try {
                System.out.println(new ObjectMapper().writeValueAsString(ll));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            populatedRecords.add(outputTransformer.apply(processor_N.apply(ll)));
        }

        return populatedRecords;
    }

    private String populateRecordAndExtractMethod(CSVRecord record, int size, Object[] args) {
        String method=null;
        for (int i = 0; i< size; i++ ) {
            String s = destringify(record.get(i));
            //logger.info("i " + i + " s \"" + s + "\"");
            if (i==0) {
                method=s;
            }
            if (s==null || s.isEmpty()) {
                args[i] = null;
            } else {
                args[i] = s;
            }
        }
        return method;
    }


    public String destringify(String s) {
        final String s1 = StringEscapeUtils.unescapeCsv(s);
        //logger.debug("<<" + s1 + ">>");
        return s1;
    }



}