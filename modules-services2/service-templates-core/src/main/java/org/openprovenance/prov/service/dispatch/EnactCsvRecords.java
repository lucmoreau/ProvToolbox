package org.openprovenance.prov.service.dispatch;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnactCsvRecords<T> {
    static Logger logger = LogManager.getLogger(EnactCsvRecords.class);



    public List<T> process(CSVParser parser, Map<String, Function<Object[],T>> enactors, Map<String,  Function<List<Object[]>,T>> enactors_N) throws IOException {
        return process(parser.getRecords(),enactors,enactors_N);
    }

    public List<T> process(Collection<CSVRecord> records, Map<String, Function<Object[],T>> enactors, Map<String,  Function<List<Object[]>,T>> enactors_N) {

        List<T> populatedRecords=new LinkedList<>();

        CSVRecord record0=records.iterator().next();
        int size0=record0.size();
        Object[] args0=new Object[size0];
        String method = populateRecordAndExtractMethod(record0, size0, args0);
        Function<Object[],T> processor_1=enactors.get(method);

        // NOTE
        // distinguish the processor for a single record (enactors.get(method)) from the processor for N (enactors_N.get(method)).
        // Assumption: we receive a single record, or we receive multiple records of the same type
        if (processor_1!=null) {
            for (CSVRecord record : records) {
                int size = record.size();
                Object[] args = new Object[size];
                populateRecordAndExtractMethod(record, size, args);
                populatedRecords.add(processor_1.apply(args));
            }
        } else {
            Function<List<Object[]>,T> processor_N=enactors_N.get(method);
            if (processor_N!=null) {
                List<Object[]> ll = records.stream().map(record -> {
                    int size = record.size();
                    Object[] args = new Object[size];
                    populateRecordAndExtractMethod(record, size, args);
                    return args;
                }).collect(Collectors.toList());
                T populatedRecords0 = processor_N.apply(ll);
                populatedRecords.add(populatedRecords0);
            } else {
                throw new EnactorException("Unknown method " + method, method);
            }
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