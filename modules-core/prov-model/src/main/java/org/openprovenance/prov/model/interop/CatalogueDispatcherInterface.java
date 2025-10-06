package org.openprovenance.prov.model.interop;


import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface CatalogueDispatcherInterface<T> {


    Map<String, String[]> getOutputs();

    Map<String, String[]> getPropertyOrder();

    Map<String, String[]> getInputs();

    Map<String, Function<Object[], String>> getSqlConverter();

    Map<String, Map<String, List<String>>> getSuccessors();

    Map<String, Function<Object[], String>> getCsvConverter();

    Map<String, String[]> getForeignTables();

    void initEnactorConverter(Function<String, ResultSet> querier,
                              BiFunction<Integer, String, Object> postProcessing);

    Map<String, Function<Object[], ?>> getEnactorConverter();

    Map<String, String> getSqlInsert();

    Map<String, Function<Object[], ?>> getBeanConverter();

    void initCompositeEnactorConverter(Function<String, ResultSet> querier,
                                       BiFunction<Integer, String, Object> postProcessing);

    Map<String, Function<List<Object[]>, ?>> getCompositeEnactorConverter();

    Map<String, Map<String, Map<String, int[]>>> getRelation0();

    Map<String, T> getDocumentBuilderDispatcher();

    Map<String, Map<String, Set<String>>> getTypeAssignment();

    Map<String, Function<Object[], Object[]>> getRecordMaker() ;

}
