package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.objectMapper;
import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.ArrayAccessor.ARRAY_ACCESSOR;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Iterator.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.ThrowStatement.THROW;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;


public class CompilerCatalogueDispatcher {


    public static final String POST_PROCESSING_VAR = "postProcessing";

       public static final ParameterizedTypeName poetSupplierOfString = ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(java.util.function.Supplier.class), com.squareup.javapoet.ClassName.get(String.class));

    // PAST types for field type declarations

    public static Map<String,String> dataConfiguratorMap=new java.util.HashMap<>() {{
        put(PROPERTY_ORDER, PROPERTY_ORDER_CONFIGURATOR);
        put("inputs", INPUTS_CONFIGURATOR);
        put("outputs", OUTPUTS_CONFIGURATOR);
        put("sqlConverter", SQL_CONFIGURATOR);
        put("csvConverter", CSV_CONFIGURATOR);
        put("csvConverter4Beans", CSV_CONFIGURATOR_4_BEANS);
        put("csvConverter4Outputs", CSV_CONFIGURATOR_4_OUTPUTS);
        put("csvConverter4OutputsComposite", CSV_CONFIGURATOR_4_OUTPUTS_COMPOSITE);
        put("sqlInsert", SQL_INSERT_CONFIGURATOR);
        put("beanConverter", CONVERTER_CONFIGURATOR);
        put("relation0", RELATION0_CONFIGURATOR);
        put("foreignTables", BUILDER_PROCESSOR_CONFIGURATOR);
        put("successors", BUILDER_PROCESSOR_CONFIGURATOR);
        put("enactorConverter", SQL_ENACTOR_CONFIGURATOR4);
        put("compositeEnactorConverter", SQL_ENACTOR_COMPOSITE_CONFIGURATOR_4);
        put("documentBuilderDispatcher", TABLE_CONFIGURATOR + WITH_MAP);
        put("typeAssignment", TABLE_CONFIGURATOR + "ForTypes" + WITH_MAP);
        put("recordMaker", OBJECT_RECORD_MAKER_CONFIGURATOR);
    }};

    public static Map<String, org.openprovenance.prov.template.compiler.past.type.TypeName> pastDataTypeMap =new java.util.HashMap<>() {{
        put(PROPERTY_ORDER,              ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.STRING_ARRAY));
        put("inputs",                    ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.STRING_ARRAY));
        put("outputs",                   ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.STRING_ARRAY));
        put("sqlConverter",              ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.FUNCTION_OBJARRAY_TO_STRING));
        put("csvConverter",              ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.FUNCTION_OBJARRAY_TO_STRING));
        put("csvConverter4Beans",        ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.FUNCTION_BEAN_TO_STRING));
        put("csvConverter4Outputs",      ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.FUNCTION_BEAN_TO_STRING));
        put("csvConverter4OutputsComposite", ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.FUNCTION_BEAN_TO_STRING));
        put("sqlInsert",                 ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.STRING));
        put("beanConverter",             ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.FUNCTION_OBJARRAY_TO_ANY));
        put("relation0",                 ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.MAP_STRING_MAP_STRING_INTARRAY));
        put("foreignTables",             ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.STRING_ARRAY));
        put("successors",                ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.MAP_STRING_LIST_STRING));
        put("enactorConverter",          ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.FUNCTION_OBJARRAY_TO_ANY));
        put("compositeEnactorConverter", ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.FUNCTION_LIST_OBJARRAY_TO_ANY));
        put("documentBuilderDispatcher", ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.PROV_FILE_BUILDER));
        put("typeAssignment",            MAP_STRING_MAP_STRING_SET_STRING);
        put("recordMaker",               ParameterizedType.get(ClassName.MAP, ClassName.STRING, ClassName.FUNCTION_OBJARRAY_TO_OBJ_ARRAY));
    }};

    public static List<String> compositeTables =List.of("compositeEnactorConverter", "csvConverter4OutputsComposite");



    public static Set<String> integratorRequired= new HashSet<>(List.of("inputs", "outputs"));
    public static Set<String> storageRequired= new HashSet<>(List.of("enactorConverter", "compositeEnactorConverter"));
    public static Set<String> sqlRelated= new HashSet<>(List.of("sqlConverter", "sqlInsert"));


    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;


    public CompilerCatalogueDispatcher(ProvFactory pFactory) {
        this.pFactory=pFactory;
        this.compilerUtil=new CompilerUtil(pFactory);
    }



    SpecificationFile generateCatalogueDispatcher(TemplatesProjectConfiguration configs,
                                                  Map<String, Map<String, Map<String, String>>> inputOutputMaps,
                                                  Locations locations,
                                                  String directory,
                                                  String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        final ParameterizedType catalogueDispatcherInterfaceType = ParameterizedType.get(CATALOGUE_DISPATCHER_INTERFACE, PROV_FILE_BUILDER);

        PastFactory pastFactory = new PastFactory();
        Class pastClass = pastFactory.CLASS(Constants.CATALOGUE_DISPATCHER)
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(catalogueDispatcherInterfaceType);


        // Add fields for each data entry
        for (String data: dataConfiguratorMap.keySet()) {
            if (!configs.integrator && (integratorRequired.contains(data) || storageRequired.contains(data))) {
                continue;
            }
            if (configs.sqlFile==null && storageRequired.contains(data)) {
                continue;
            }

            org.openprovenance.prov.template.compiler.past.type.TypeName pastTypeName = pastDataTypeMap.get(data);

            if (storageRequired.contains(data)) {
                pastClass.FIELDS(FIELD(data, pastTypeName).MODIFIERS(Modifier.PRIVATE));
            } else {
                pastClass.FIELDS(FIELD(data, pastTypeName).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL));
            }
        }

        // Add simple getters (return field) for non-storage, non-null-guard data
        for (String data: dataConfiguratorMap.keySet()) {
            if (!configs.integrator && (integratorRequired.contains(data) || storageRequired.contains(data))) {
                continue;
            }
            if (configs.sqlFile==null && storageRequired.contains(data)) {
                continue;
            }
            if (!storageRequired.contains(data)) {
                org.openprovenance.prov.template.compiler.past.type.TypeName pastTypeName = pastDataTypeMap.get(data);
                pastClass.METHOD(METHOD("get" + capitalizeFirstLetter(data))
                        .MODIFIERS(Modifier.PUBLIC)
                        .commentFileLocation()
                        .RETURNS(pastTypeName)
                        .BODY(RETURN(VARIABLE(data))));
            }
        }

        // Add static string fields with JSON initializers
        try {
            pastClass.FIELDS(FIELD("ioMap", STRING).MODIFIERS(Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL).INITIALIZER(CONSTANT(objectMapper.writeValueAsString(inputOutputMaps))));
            pastClass.FIELDS(FIELD("shortNames", STRING).MODIFIERS(Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL).INITIALIZER(CONSTANT(objectMapper.writeValueAsString(locations.getShortNames()))));
            pastClass.FIELDS(FIELD("linkers", STRING).MODIFIERS(Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL).INITIALIZER(CONSTANT(objectMapper.writeValueAsString(locations.getLinkerTableDeclarations()))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Add simple string getters for static fields
        pastClass.METHODS(
                METHOD("getIoMap")
                        .MODIFIERS(Modifier.PUBLIC)
                        .commentFileLocation()
                        .RETURNS(STRING)
                        .BODY(RETURN(VARIABLE("ioMap"))),

                METHOD("getShortNames")
                        .MODIFIERS(Modifier.PUBLIC)
                        .commentFileLocation()
                        .RETURNS(STRING)
                        .BODY(RETURN(VARIABLE("shortNames"))),

                METHOD("getLinkers")
                        .MODIFIERS(Modifier.PUBLIC)
                        .commentFileLocation()
                        .RETURNS(STRING)
                        .BODY(RETURN(VARIABLE("linkers"))));


        // Add constructor via PAST
        Constructor cspec = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(MAP_STRING_STRING, "map")
                .PARAMETER(PROV_FACTORY, "pf");
        compilerUtil.debugFileLocation(cspec);

        for (String data: dataConfiguratorMap.keySet()) {
            if (!configs.integrator && (integratorRequired.contains(data) || storageRequired.contains(data))) {
                continue;
            }
            if (configs.sqlFile==null && storageRequired.contains(data)) {
                continue;
            }

            String configurator = dataConfiguratorMap.get(data);

            if (!storageRequired.contains(data)) {
                ClassName loggerClass =
                        ClassName.get(Constants.LOGGER, locations.getFilePackage(configs.name, Constants.LOGGER));
                ClassName configuratorClass =
                        ClassName.get(configurator, locations.getFilePackage(configs.name, configurator));

                if ("foreignTables".equals(data)) {
                    // new BuilderProcessorConfigurator<>(b -> b.getForeign())
                    ParameterizedType diamondConfiguratorType = ParameterizedType.get(configuratorClass);
                    LambdaExpression lambda = LAMBDA(PARAMETER("b", ClassName.BUILDER_INTERFACE))
                            .BODY(RETURN(METHOD_CALL(VARIABLE("b"), "getForeign", List.of())));
                    cspec.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE("this"), data),
                            METHOD_CALL(loggerClass, INITIALIZE_BEAN_TABLE,
                                    List.of(CONSTRUCTOR_CALL(diamondConfiguratorType, List.of(lambda))))));
                } else if ("successors".equals(data)) {
                    // new BuilderProcessorConfigurator<>(b -> CatalogueDispatcher.process(b))
                    ParameterizedType diamondConfiguratorType = ParameterizedType.get(configuratorClass);
                    ClassName catalogueDispatcherClass =
                            ClassName.get(CATALOGUE_DISPATCHER, configs.root_package);
                    LambdaExpression lambda = LAMBDA(PARAMETER("b", ClassName.BUILDER_INTERFACE))
                            .BODY(RETURN(METHOD_CALL(catalogueDispatcherClass, "process", List.of(VARIABLE("b")))));
                    cspec.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE("this"), data),
                            METHOD_CALL(loggerClass, INITIALIZE_BEAN_TABLE,
                                    List.of(CONSTRUCTOR_CALL(diamondConfiguratorType, List.of(lambda))))));
                } else if ("documentBuilderDispatcher".equals(data)) {
                    // new TableConfiguratorWithMap(map, pf)
                    cspec.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE("this"), data),
                            METHOD_CALL(loggerClass, INITIALIZE_BEAN_TABLE,
                                    List.of(CONSTRUCTOR_CALL(configuratorClass, List.of(VARIABLE("map"), VARIABLE("pf")))))));
                } else if ("typeAssignment".equals(data)) {
                    // new TableConfiguratorForTypesWithMap(map, propertyOrder, documentBuilderDispatcher, pf)
                    cspec.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE("this"), data),
                            METHOD_CALL(loggerClass, INITIALIZE_BEAN_TABLE,
                                    List.of(CONSTRUCTOR_CALL(configuratorClass, List.of(VARIABLE("map"), VARIABLE("propertyOrder"), VARIABLE("documentBuilderDispatcher"), VARIABLE("pf")))))));
                } else if ("recordMaker".equals(data)) {
                    // new ObjectRecordMakerConfigurator(documentBuilderDispatcher)
                    cspec.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE("this"), data),
                            METHOD_CALL(loggerClass, INITIALIZE_BEAN_TABLE,
                                    List.of(CONSTRUCTOR_CALL(configuratorClass, List.of(VARIABLE("documentBuilderDispatcher")))))));
                } else {
                    if (sqlRelated.contains(data) && configs.sqlFile==null) {
                        cspec.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE("this"), data), Constant.getNull()));
                    } else {
                        // new ConfiguratorClass()
                        String initialiser=(compositeTables.contains(data))? INITIALIZE_COMPOSITE_BEAN_TABLE : INITIALIZE_BEAN_TABLE;
                        cspec.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE("this"), data),
                                METHOD_CALL(loggerClass, initialiser,
                                        List.of(CONSTRUCTOR_CALL(configuratorClass, List.of())))));
                    }
                }
            }
        }
        pastClass.CONSTRUCTOR(cspec);

        // Add null getters and null init methods for skipped entries
        for (String data: dataConfiguratorMap.keySet()) {
            if (!configs.integrator && (integratorRequired.contains(data) || storageRequired.contains(data))) {
                pastClass.METHOD(createNullGetter(data, pastDataTypeMap.get(data)));
                if (storageRequired.contains(data)) {
                    pastClass.METHOD(createNullInit(data));
                }
            } else if (configs.sqlFile==null && storageRequired.contains(data)) {
                pastClass.METHODS(
                        createNullGetter(data, pastDataTypeMap.get(data)),
                        createNullInit(data));
            }
        }

        // Add storage-required init methods and null-guard getters
        for (String data: dataConfiguratorMap.keySet()) {
            if (!configs.integrator && (integratorRequired.contains(data) || storageRequired.contains(data))) {
                continue;
            }
            if (configs.sqlFile==null && storageRequired.contains(data)) {
                continue;
            }
            if (storageRequired.contains(data)) {
                String configurator = dataConfiguratorMap.get(data);
                ClassName loggerClass =
                        ClassName.get(Constants.LOGGER, locations.getFilePackage(configs.name, Constants.LOGGER));
                ClassName configuratorClass =
                        ClassName.get(configurator, locations.getFilePackage(configs.name, configurator));
                String initMethodName = compositeTables.contains(data) ? INITIALIZE_COMPOSITE_BEAN_TABLE : INITIALIZE_BEAN_TABLE;

                pastClass.METHODS(

                        METHOD("init" + capitalizeFirstLetter(data))
                                .MODIFIERS(Modifier.PUBLIC)
                                .commentFileLocation()
                                .RETURNS(VOID)
                                .PARAMETER(FUNCTION_STRING_RESULTSET, QUERIER_VAR)
                                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, POST_PROCESSING_VAR)
                                .PARAMETER(SUPPLIER_OF_STRING, GET_PRINCIPAL_VAR)
                                .BODY(ASSIGNMENT(METHOD_CALL(VARIABLE("this"), data),
                                        METHOD_CALL(loggerClass, initMethodName,
                                                List.of(CONSTRUCTOR_CALL(configuratorClass,
                                                        List.of(VARIABLE(QUERIER_VAR), VARIABLE(POST_PROCESSING_VAR), VARIABLE(GET_PRINCIPAL_VAR))))))),

                        createGetter(configs, data, pastDataTypeMap.get(data)));
            }
        }

        // Add the process method
        pastClass.METHOD(processMethodGenerator());


        Supplier<Boolean> pythonGenerator=() -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, configs.root_package, configs, directory, stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator,pythonGenerator);
    }

    private Method createGetter(TemplatesProjectConfiguration configs, String data, org.openprovenance.prov.template.compiler.past.type.TypeName typeName) {
        Method getterSpec = METHOD("get" + capitalizeFirstLetter(data))
                .MODIFIERS(Modifier.PUBLIC)
                .commentFileLocation()
                .RETURNS(typeName);
        if (storageRequired.contains(data)) {
            getterSpec.BODY(
                    IF(BINARY_OP(VARIABLE(data), "==", Constant.getNull()))
                    .THEN(THROW(
                            CONSTRUCTOR_CALL(ILLEGAL_STATE_EXCEPTION,
                                    List.of(CONSTANT("non initialized field " + data))))));
        }
        if (configs.sqlFile==null && storageRequired.contains(data)) {
            getterSpec.BODY(RETURN(Constant.getNull()));
        } else {
            getterSpec.BODY(RETURN(VARIABLE(data)));
        }
        return getterSpec;
    }

    private Method createNullGetter(String data, org.openprovenance.prov.template.compiler.past.type.TypeName typeName) {
        return METHOD("get" + capitalizeFirstLetter(data))
                .MODIFIERS(Modifier.PUBLIC)
                .commentFileLocation()
                .RETURNS(typeName)
                .BODY(RETURN(Constant.getNull()));
    }

    private Method createNullInit(String data) {
        return METHOD("init" + capitalizeFirstLetter(data))
                .MODIFIERS(Modifier.PUBLIC)
                .commentFileLocation()
                .RETURNS(VOID)
                .PARAMETER(FUNCTION_STRING_RESULTSET, QUERIER_VAR)
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, POST_PROCESSING_VAR)
                .PARAMETER(SUPPLIER_OF_STRING, GET_PRINCIPAL_VAR)
                .COMMENT(true, "null sqlFile or false integrator flag");
    }


    private Method processMethodGenerator() {
        // Map<Integer, int[]> successors = builder.getSuccessors();
        // String[] order = builder.getPropertyOrder();
        // Map<String, List<String>> map = new HashMap<>();
        // for (Integer k : successors.keySet()) {
        //     if (successors.get(k).length != 0) {
        //         List<String> list = new ArrayList<>();
        //         for (int v : successors.get(k)) {
        //             String s = order[v];
        //             list.add(s);
        //         }
        //         if (map.put(order[k], list) != null) {
        //             throw new IllegalStateException("Duplicate key");
        //         }
        //     }
        // }
        // return map;

        return METHOD("process")
                .MODIFIERS(Modifier.STATIC, Modifier.PUBLIC)
                .commentFileLocation()
                .RETURNS(MAP_STRING_LIST_STRING)
                .PARAMETER(ClassName.BUILDER_INTERFACE, "builder")
                .BODY(
                        // Map<Integer, int[]> successors = builder.getSuccessors();
                        DEFINITION(MAP_INTEGER_INTARRAY, VARIABLE("successors"),
                                METHOD_CALL(VARIABLE("builder"), "getSuccessors", List.of())),

                        // String[] order = builder.getPropertyOrder();
                        DEFINITION(STRING_ARRAY, VARIABLE("order"),
                                METHOD_CALL(VARIABLE("builder"), "getPropertyOrder", List.of())),

                        // Map<String, List<String>> map = new HashMap<>();
                        DEFINITION(MAP_STRING_LIST_STRING, VARIABLE("map"),
                                CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of())),

                        // for (Integer k : successors.keySet()) { ... }
                        ITERATOR(PARAMETER("k", INTEGER),
                                METHOD_CALL(VARIABLE("successors"), "keySet", List.of()))
                                .BODY(
                                        // if (successors.get(k).length != 0) { ... }
                                        IF(BINARY_OP(
                                                METHOD_CALL(METHOD_CALL(VARIABLE("successors"), "get", List.of(VARIABLE("k"))), "length"),
                                                "!=",
                                                CONSTANT(0)))
                                        .THEN(
                                                // List<String> list = new ArrayList<>();
                                                DEFINITION(LIST_OF_STRING, VARIABLE("list"),
                                                        CONSTRUCTOR_CALL(ARRAY_LIST_GENERICS, List.of())),

                                                // for (int v : successors.get(k)) { ... }
                                                ITERATOR(PARAMETER("v", _int),
                                                        METHOD_CALL(VARIABLE("successors"), "get", List.of(VARIABLE("k"))))
                                                        .BODY(
                                                                // String s = order[v];
                                                                DEFINITION(STRING, VARIABLE("s"),
                                                                        ARRAY_ACCESSOR(VARIABLE("order"), VARIABLE("v"))),
                                                                // list.add(s);
                                                                METHOD_CALL(VARIABLE("list"), "add", VARIABLE("s"))
                                                        ),

                                                // if (map.put(order[k], list) != null) { throw ... }
                                                IF(BINARY_OP(
                                                        METHOD_CALL(VARIABLE("map"), "put", List.of(
                                                                ARRAY_ACCESSOR(VARIABLE("order"), VARIABLE("k")),
                                                                VARIABLE("list"))),
                                                        "!=",
                                                        Constant.getNull()))
                                                .THEN(
                                                        THROW(
                                                                CONSTRUCTOR_CALL(ILLEGAL_STATE_EXCEPTION,
                                                                        List.of(CONSTANT("Duplicate key"))))
                                                )
                                        )
                                ),

                        // return map;
                        RETURN(VARIABLE("map"))
                );
    }


    public String capitalizeFirstLetter(String s) {
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }

    static private com.squareup.javapoet.TypeName poetMapOf(com.squareup.javapoet.TypeName t) {
        return ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Map.class), com.squareup.javapoet.ClassName.get(String.class), t);
    }

}
