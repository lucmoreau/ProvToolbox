package org.openprovenance.prov.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.client.ProcessorArgsInterface;
import org.openprovenance.prov.client.RecordsProcessorInterface;
import org.openprovenance.prov.service.iobean.composite.SqlCompositeEnactorConfigurator3;
import org.openprovenance.prov.service.iobean.simple.SqlEnactorConfigurator3;
import org.openprovenance.prov.template.library.plead.client.configurator.*;
import org.openprovenance.prov.template.library.plead.logger.TemplateBuilders;


import java.sql.Connection;
import java.util.Map;

import static org.openprovenance.prov.template.library.plead.logger.Logger.initializeBeanTable;
import static org.openprovenance.prov.template.library.plead.logger.Logger.initializeCompositeBeanTable;

public class TemplatesDispatcher {
    static Logger logger = LogManager.getLogger(TemplatesDispatcher.class);

    final TemplateBuilders allBuilders=new TemplateBuilders();


    private final Map<String,String[]> propertyOrder;
    private final Map<String, ProcessorArgsInterface<String>> sqlConverter;
    private final Map<String, String> sqlInsert;
    private final Map<String, ProcessorArgsInterface<String>> csvConverter;
    private final Map<String, ProcessorArgsInterface<?>> beanConverter;
    private final Map<String, ProcessorArgsInterface<?>> enactorConverter;
    private final Map<String, RecordsProcessorInterface<?>> compositeEnactorConverter;

    public TemplatesDispatcher(Storage storage, Connection conn) {
        propertyOrder=initializeBeanTable(new PropertyOrderConfigurator());
        sqlConverter=initializeBeanTable(new SqlConfigurator());
        csvConverter=initializeBeanTable(new CsvConfigurator());
        sqlInsert=initializeBeanTable(new SqlInsertConfigurator());
        beanConverter=initializeBeanTable(new ConverterConfigurator());
        //enactorConverter=initializeBeanTable(new SqlEnactorConfigurator(storage,conn));
        enactorConverter=initializeBeanTable(new SqlEnactorConfigurator3(storage,conn));
        //compositeEnactorConverter=initializeCompositeBeanTable(new SqlCompositeEnactorConfigurator(storage,conn));
        compositeEnactorConverter=initializeCompositeBeanTable(new SqlCompositeEnactorConfigurator3(storage,conn));
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
}
