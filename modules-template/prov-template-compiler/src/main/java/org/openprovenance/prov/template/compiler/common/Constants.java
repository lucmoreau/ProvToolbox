package org.openprovenance.prov.template.compiler.common;

public interface Constants {
    String IS_A = "isA";
    String PREFIX_LOG_VAR = "___";
    String GET_NODES_METHOD = "getNodes";
    String BUILDER_INTERFACE = "Builder";
    String INIT = "Init";
    String BUILDERS = "builders";
    String TYPEMANAGERS = "typeManagers";
    String PF = "pf";
    String GET_SUCCESSOR_METHOD = "getSuccessors";
    String GET_TYPED_SUCCESSOR_METHOD = "getTypedSuccessors";
    String GET_NAME = "getName";
    String LOGGER_INTERFACE = "LoggerInterface";
    String PROCESSOR_ARGS_INTERFACE = "ProcessorArgsInterface";
    String TESTER_FILE = "ExampleTest";
    String GET_BUILDERS_METHOD = "getBuilders";
    String CLIENT_PACKAGE = "org.openprovenance.prov.client";
    String PROPERTY_ORDER = "propertyOrder";
    String OUTPUTS = "outputs";
    String COMPULSORY_INPUTS = "compulsoryInputs";
    String INPUTS = "inputs";
    String PROPERTY_ORDER_METHOD  = "getPropertyOrder";
    String OUTPUTS_METHOD  = "getOutputs";
    String COMPULSORY_INPUTS_METHOD = "getCompulsoryInputs";
    String INPUTS_METHOD  = "getInputs";
    String RECORD_CSV_PROCESSOR_METHOD  = "record2csv";
    String ARGS_CSV_CONVERSION_METHOD   = "args2csv";
    String BEAN_SQL_CONVERSION_METHOD   = "bean2sql";
    String PROCESSOR_CONVERTER          = "processorConverter";
    String ARGS2RECORD_CONVERTER        = "aArgs2RecordConverter";
    String A_BEAN_SQL_CONVERTER         = "aBean2SqlConverter";
    String A_ARGS_BEAN_CONVERTER        = "aArgs2BeanConverter";
    String A_ARGS_CSV_CONVERTER         = "aArgs2CsVConverter";
    String A_RECORD_BEAN_CONVERTER      = "aRecord2BeanConverter";
    String A_RECORD_CSV_CONVERTER       = "aRecord2CsvConverter";
    String A_RECORD_SQL_CONVERTER       = "aRecord2SqlConverter";
    String BUILDER = "Builder";
    String SQL_CONFIGURATOR = "SqlConfigurator";
    String SQL_INSERT_CONFIGURATOR = "SqlInsertConfigurator";
    String CSV_CONFIGURATOR = "CsvConfigurator";
    String CONVERTER_CONFIGURATOR = "ConverterConfigurator";
    String ENACTOR_CONFIGURATOR = "EnactorConfigurator";
    String BEAN_COMPLETER = "BeanCompleter";
    String BEAN_ENACTOR = "BeanEnactor";
    String QUERY_INVOKER = "QueryInvoker";
    String BEAN_CHECKER = "BeanChecker";
    String PROPERTY_ORDER_CONFIGURATOR = "PropertyOrderConfigurator";
    String PROCESS_METHOD_NAME = "process";
    String PROCESSOR_PROCESS_METHOD_NAME = "process";
    String GETTER = "Getter";
    String ENACTOR_IMPLEMENTATION = "EnactorImplementation";
    String INSERT_PREFIX = "insert_";
    String NOT_NULL_METHOD = "notNull";
    String INPUT_PREFIX = "input_";
    String SHARED_PREFIX = "shared_";
    String NULLABLE_TEXT = "nullableTEXT";
    String TIMESTAMPTZ = "timestamptz";
    String RECORDS_VAR = "records";
    String ARECORD_VAR = "arecord";
    String INPUT_TABLE = "input_table";
    String JAVADOC_NO_DOCUMENTATION = "-- no @documentation";
    String JAVADOC_NO_DOCUMENTATION_DEFAULT_TYPE = "xsd:string";
    String GENERATED_VAR_PREFIX = "__";
    boolean IN_INTEGRATOR = true;
    String REALISER = "realiser";
}
