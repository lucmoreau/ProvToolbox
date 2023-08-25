package org.openprovenance.prov.template.compiler.common;

public interface Constants {
    String IS_A = "isA";
    String ELEMENTS = "__elements";
    String ADD_ELEMENTS = "__addElements";
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
    String INPUT_OUTPUT_PROCESSOR = "InputOutputProcessor";
    String INPUT_PROCESSOR = "InputProcessor";
    String TEMPLATE_INVOKER = "TemplateInvoker";
    String PROCESSOR_ARGS_INTERFACE = "ProcessorArgsInterface";
    String RECORDS_PROCESSOR_INTERFACE = "RecordsProcessorInterface";
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
    String A_RECORD_INPUTS_CONVERTER      = "aRecord2InputsConverter";
    String A_RECORD_CSV_CONVERTER       = "aRecord2CsvConverter";
    String A_RECORD_SQL_CONVERTER       = "aRecord2SqlConverter";
    String BUILDER = "Builder";
    String SQL_CONFIGURATOR = "SqlConfigurator";
    String SQL_INSERT_CONFIGURATOR = "SqlInsertConfigurator";
    String CSV_CONFIGURATOR = "CsvConfigurator";
    String BUILDER_CONFIGURATOR = "BuilderConfigurator";
    String CONVERTER_CONFIGURATOR = "ConverterConfigurator";
    String ENACTOR_CONFIGURATOR = "EnactorConfigurator";
    String COMPOSITE_ENACTOR_CONFIGURATOR = "CompositeEnactorConfigurator";
    String COMPOSITE_ENACTOR_CONFIGURATOR2 = "CompositeEnactorConfigurator2";
    String ENACTOR_CONFIGURATOR2 = "EnactorConfigurator2";
    String BEAN_COMPLETER = "BeanCompleter";
    String BEAN_COMPLETER2 = "BeanCompleter2";
    String COMPOSITE_BEAN_COMPLETER2 = "CompositeBeanCompleter2";
    String TYPE_CONVERTER = "TypeConverter";
    String BEAN_ENACTOR = "BeanEnactor";
    String BEAN_ENACTOR2 = "BeanEnactor2";
    String QUERY_INVOKER = "QueryInvoker";
    String QUERY_INVOKER2 = "QueryInvoker2";
    String BEAN_CHECKER = "BeanChecker";
    String BEAN_CHECKER2 = "BeanChecker2";
    String DELEGATOR = "Delegator";
    String PROPERTY_ORDER_CONFIGURATOR = "PropertyOrderConfigurator";
    String PROCESS_METHOD_NAME = "process";
    String PROCESSOR_PROCESS_METHOD_NAME = "process";
    String GETTER = "Getter";
    String ENACTOR_IMPLEMENTATION = "EnactorImplementation";
    String INSERT_PREFIX = "insert_";
    String NOT_NULL_METHOD = "notNull";
    String INPUT_PREFIX = "input_";
    String SHARED_PREFIX = "composite_";
    String NULLABLE_TEXT = "nullableTEXT";
    String JSON_TEXT = "json";
    String TIMESTAMPTZ = "timestamptz";
    String SQL_DATE = "date";
    String RECORDS_VAR = "_records";
    String ARECORD_VAR = "_arecord";
    String INPUT_TABLE = "_input_table";
    String JAVADOC_NO_DOCUMENTATION = "-- no @documentation";
    String JAVADOC_NO_DOCUMENTATION_DEFAULT_TYPE = "xsd:string";
    String GENERATED_VAR_PREFIX = "__";
    boolean IN_INTEGRATOR = true;
    String REALISER = "realiser";
    String OPENPROVENANCE_COMPOSITE_BEAN_JSON = "openprovenance:composite-bean.json";
    String RESOURCE_COMPOSITE_BEAN_JSON = "/composite/composite-bean.json";
    String __NODES_FIELD = "__nodes";
    String COMPOSITE = "Composite";
    String INITIALIZE_BEAN_TABLE = "initializeBeanTable";
    String INSERT_ARRAY_SUFFIX = "_array";
    String VARIABLE_BEAN = "bean";
    String GENERIC_POST_AND_RETURN = "generic_post_and_return";
    String M_VAR = "m";
    String LL_VAR = "ll";

    String BEAN_VAR = "bean";
    String DELEGATOR_VAR = "delegator";
    String TO_INPUTS = "toInputs";
    String DOT_JAVA_EXTENSION = ".java";
    String SQL_INTERFACE = "SQL";
}
