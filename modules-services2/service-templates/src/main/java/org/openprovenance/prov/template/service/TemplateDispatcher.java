package org.openprovenance.prov.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.service.core.dispatch.ForeignTableConfigurator;
import org.openprovenance.prov.service.core.dispatch.RelationConfigurator;
import org.openprovenance.prov.service.core.dispatch.SuccessorConfigurator;
import org.openprovenance.prov.template.library.plead.client.configurator.*;
import org.openprovenance.prov.template.library.plead.client.logger.TemplateBuilders;
import org.openprovenance.prov.template.library.plead.sql.access_control.SqlCompositeEnactorConfigurator4;
import org.openprovenance.prov.template.library.plead.sql.access_control.SqlEnactorConfigurator4;


import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.library.plead.client.logger.Logger.initializeBeanTable;
import static org.openprovenance.prov.template.library.plead.client.logger.Logger.initializeCompositeBeanTable;

public class TemplateDispatcher {
    static Logger logger = LogManager.getLogger(TemplateDispatcher.class);

    final TemplateBuilders allBuilders=new TemplateBuilders();


    private final Map<String,String[]> propertyOrder;
    private final Map<String, Function<Object[], String>> sqlConverter;
    private final Map<String, String> sqlInsert;
    private final Map<String, Function<Object[], String>> csvConverter;
    private final Map<String, Function<Object[], ?>> beanConverter;
    private final Map<String, Function<Object[], ?>> enactorConverter;
    private final Map<String, Function<List<Object[]>,?>> compositeEnactorConverter;
    private final Map<String, Map<String, List<String>>> successors;
    private final Map<String, Map<String, List<String>>> predecessors;
    private final Map<String, String[]> inputs;
    private final Map<String, String[]> outputs;
    private final Map<String, String[]> foreignTables;
    private final Map<String, Map<String, Map<String, List<String>>>> relations;
    private final Map<String, Map<String, Map<String, int[]>>> relations0;


    public TemplateDispatcher(Function<String, ResultSet> querier, BiFunction<Integer, String, Object> postProcessing) {
        propertyOrder=initializeBeanTable(new PropertyOrderConfigurator());
        inputs=initializeBeanTable(new InputsConfigurator());
        outputs = initializeBeanTable(new OutputsConfigurator());
        sqlConverter=initializeBeanTable(new SqlConfigurator());
        foreignTables=initializeBeanTable(new ForeignTableConfigurator());
        csvConverter=initializeBeanTable(new CsvConfigurator());
        sqlInsert=initializeBeanTable(new SqlInsertConfigurator());
        beanConverter=initializeBeanTable(new ConverterConfigurator());
        successors = initializeBeanTable(new SuccessorConfigurator());
        relations = initializeBeanTable(new RelationConfigurator());
        relations0 = initializeBeanTable(new Relation0Configurator());
        enactorConverter=initializeBeanTable(new SqlEnactorConfigurator4(querier, postProcessing));
        compositeEnactorConverter=initializeCompositeBeanTable(new SqlCompositeEnactorConfigurator4(querier, postProcessing));

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

    public Map<String, Function<Object[], String>> getSqlConverter() {
        return sqlConverter;
    }

    public Map<String, Function<Object[], String>> getCsvConverter() {
        return csvConverter;
    }

    public Map<String, Function<Object[], ?>> getBeanConverter() {
        return beanConverter;
    }

    public Map<String, String> getSqlInsert() {
        return sqlInsert;
    }

    public Map<String, Function<Object[], ?>> getEnactorConverter() {
        return enactorConverter;
    }

    public Map<String, Function<List<Object[]>,?>> getCompositeEnactorConverter() {
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
    public Map<String, String[]> getForeignTables() {
        return foreignTables;
    }
    public Map<String, Map<String, Map<String, List<String>>> > getRelations() {
        return relations;
    }
    public Map<String, Map<String, Map<String, int[]>>> getRelations0() {
        return relations0;
    }
}
