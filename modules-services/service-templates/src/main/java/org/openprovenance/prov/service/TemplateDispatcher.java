package org.openprovenance.prov.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.client.ProcessorArgsInterface;
import org.openprovenance.prov.client.RecordsProcessorInterface;
import org.openprovenance.prov.template.library.plead.client.configurator.*;
import org.openprovenance.prov.template.library.plead.client.logger.TemplateBuilders;
import org.openprovenance.prov.template.library.plead.sql.access_control.SqlCompositeEnactorConfigurator4;
import org.openprovenance.prov.template.library.plead.sql.access_control.SqlEnactorConfigurator4;
import org.openprovenance.prov.template.library.plead.sql.integration.SqlCompositeEnactorConfigurator3;
import org.openprovenance.prov.template.library.plead.sql.integration.SqlEnactorConfigurator3;


import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.library.plead.client.logger.Logger.initializeBeanTable;
import static org.openprovenance.prov.template.library.plead.client.logger.Logger.initializeCompositeBeanTable;

public class TemplateDispatcher {
    static Logger logger = LogManager.getLogger(TemplateDispatcher.class);

    final TemplateBuilders allBuilders=new TemplateBuilders();


    private final Map<String,String[]> propertyOrder;
    private final Map<String, ProcessorArgsInterface<String>> sqlConverter;
    private final Map<String, String> sqlInsert;
    private final Map<String, ProcessorArgsInterface<String>> csvConverter;
    private final Map<String, ProcessorArgsInterface<?>> beanConverter;
    private final Map<String, ProcessorArgsInterface<?>> enactorConverter;
    private final Map<String, RecordsProcessorInterface<?>> compositeEnactorConverter;
    private final Map<String, Map<String, List<String>>> successors;
    private final Map<String, Map<String, List<String>>> predecessors;
    private final Map<String, String[]> inputs;
    private final Map<String, String[]> outputs;


    public TemplateDispatcher(Storage storage, Connection conn, BiFunction<Integer, String, Object> postProcessing) {
        propertyOrder=initializeBeanTable(new PropertyOrderConfigurator());
        inputs=initializeBeanTable(new InputsConfigurator());
        outputs = initializeBeanTable(new OutputsConfigurator());
        sqlConverter=initializeBeanTable(new SqlConfigurator());
        csvConverter=initializeBeanTable(new CsvConfigurator());
        sqlInsert=initializeBeanTable(new SqlInsertConfigurator());
        beanConverter=initializeBeanTable(new ConverterConfigurator());
        //enactorConverter=initializeBeanTable(new SqlEnactorConfigurator(storage,conn));
        enactorConverter=initializeBeanTable(new SqlEnactorConfigurator4(storage.getQuerier(conn), postProcessing));
        //compositeEnactorConverter=initializeCompositeBeanTable(new SqlCompositeEnactorConfigurator(storage,conn));
        compositeEnactorConverter=initializeCompositeBeanTable(new SqlCompositeEnactorConfigurator4(storage.getQuerier(conn), postProcessing));
        successors = initializeBeanTable(new SuccessorConfigurator());
        predecessors = successors
                .keySet()
                .stream()
                .collect(Collectors.toMap(k -> k,
                        k -> getNonNullStringListMap(k)
                                .entrySet()
                                .stream()
                                .flatMap(entry ->
                                        entry
                                        .getValue()
                                        .stream().map(v -> Map.entry(entry.getKey(),v)))
                                .collect(Collectors.groupingBy(Map.Entry::getValue,
                                                               Collectors.mapping(Map.Entry::getKey, Collectors.toList())))));
    }

    private Map<String, List<String>> getNonNullStringListMap(String k) {
        Map<String, List<String>> stringListMap = successors.get(k);
        return stringListMap==null?Map.of():stringListMap;
    }


    public Map<String, String[]> getPropertyOrder() {
        return propertyOrder;
    }

    public Map<String, ProcessorArgsInterface<String>> getSqlConverter() {
        return sqlConverter;
    }

    public Map<String, ProcessorArgsInterface<String>> getCsvConverter() {
        return csvConverter;
    }

    public Map<String, ProcessorArgsInterface<?>> getBeanConverter() {
        return beanConverter;
    }

    public Map<String, String> getSqlInsert() {
        return sqlInsert;
    }

    public Map<String, ProcessorArgsInterface<?>> getEnactorConverter() {
        return enactorConverter;
    }

    public Map<String, RecordsProcessorInterface<?>> getCompositeEnactorConverter() {
        return compositeEnactorConverter;
    }

    public Map<String, Map<String, List<String>>> getSuccessors() {
        return successors;
    }
    public Map<String, Map<String, List<String>>> getPredecessors() {
        return predecessors;
    }
    public Map<String, String[]> getInputs() {
        return inputs;
    }
    public Map<String, String[]> getOutputs() {
        return outputs;
    }
}
