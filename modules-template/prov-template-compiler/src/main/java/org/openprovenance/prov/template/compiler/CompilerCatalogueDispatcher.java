package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.CompilerCompositeConfigurations.recordsProcessorOfUnknown;
import static org.openprovenance.prov.template.compiler.CompilerConfigurations.*;
import static org.openprovenance.prov.template.compiler.CompilerUtil.mapString2StringType;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.objectMapper;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerCatalogueDispatcher {
    public static final String POST_PROCESSING_VAR = "postProcessing";
    static final ParameterizedTypeName FunctionOfString2StringArray = ParameterizedTypeName.get(ClassName.get(java.util.function.Function.class), ClassName.get(CLIENT_PACKAGE,BUILDER_INTERFACE), ArrayTypeName.of(ClassName.get(String.class)));

    static final ParameterizedTypeName FunctionStringResultSet = ParameterizedTypeName.get(ClassName.get(java.util.function.Function.class), ClassName.get(String.class), ClassName.get(java.sql.ResultSet.class));
    static final ParameterizedTypeName BiFunctionIntegerStringObject=ParameterizedTypeName.get(ClassName.get(java.util.function.BiFunction.class), ClassName.get(Integer.class), ClassName.get(String.class), ClassName.get(Object.class));

    //  Function<Object[], Object[]>
    static final ParameterizedTypeName FunctionObjArray2ObjArray= ParameterizedTypeName.get(ClassName.get(java.util.function.Function.class), ArrayTypeName.of(ClassName.get(Object.class)), ArrayTypeName.of(ClassName.get(Object.class)));
   // Supplier<String>
    static final ParameterizedTypeName SupplierOfString= ParameterizedTypeName.get(ClassName.get(java.util.function.Supplier.class), ClassName.get(String.class));

    public static Map<String,String> dataConfiguratorMap=new java.util.HashMap<>() {{
        put(PROPERTY_ORDER, PROPERTY_ORDER_CONFIGURATOR);
        put("inputs", INPUTS_CONFIGURATOR);
        put("outputs", OUTPUTS_CONFIGURATOR);
        put("sqlConverter", SQL_CONFIGURATOR);
        put("csvConverter", CSV_CONFIGURATOR);
        put("sqlInsert", SQL_INSERT_CONFIGURATOR);
        put("beanConverter", CONVERTER_CONFIGURATOR);
        put("relation0", RELATION0_CONFIGURATOR);
        put("foreignTables", BUILDER_PROCESSOR_CONFIGURATOR);
        put("successors", BUILDER_PROCESSOR_CONFIGURATOR);
        put("enactorConverter", SQL_ENACTOR_CONFIGURATOR4);
        put("compositeEnactorConverter", SQL_COMPOSITE_ENACTOR_CONFIGURATOR4);
        put("documentBuilderDispatcher", TABLE_CONFIGURATOR + WITH_MAP);
        put("typeAssignment", TABLE_CONFIGURATOR + "ForTypes" + WITH_MAP);
        put("recordMaker", OBJECT_RECORD_MAKER_CONFIGURATOR);


    }};
    public static Map<String,TypeName> dataTypeMap=new java.util.HashMap<>() {{
        put(PROPERTY_ORDER, mapOf(stringArray));
        put("inputs", mapOf(stringArray));
        put("outputs", mapOf(stringArray));
        put("sqlConverter", mapOf(processorOfString));
        put("csvConverter", mapOf(processorOfString));
        put("sqlInsert", mapOf(ClassName.get(String.class)));
        put("beanConverter", mapOf(processorOfUnknown));
        put("relation0", mapOf(mapString2MapString2IntArray));
        put("foreignTables", mapOf(stringArray));
        put("successors", mapOf(mapString2StringList));
        put("enactorConverter", mapOf(processorOfUnknown));
        put("compositeEnactorConverter", mapOf(recordsProcessorOfUnknown));
        put("documentBuilderDispatcher", mapOf(ClassName.get(FileBuilder.class)));
        put("typeAssignment", mapOf(mapOf(ParameterizedTypeName.get(ClassName.get(Set.class),ClassName.get(String.class)))));
        put("recordMaker", mapOf(FunctionObjArray2ObjArray));


    }};


    public static Set<String> integratorRequired= new HashSet<>(List.of("inputs", "outputs"));
    public static Set<String> storageRequired= new HashSet<>(List.of("enactorConverter", "compositeEnactorConverter"));
    public static Set<String> sqlRelated= new HashSet<>(List.of("sqlConverter", "sqlInsert"));


    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;


    public CompilerCatalogueDispatcher(ProvFactory pFactory) {
        this.pFactory=pFactory;
        this.compilerUtil=new CompilerUtil(pFactory);
    }



    SpecificationFile generateCatalogueDispatcher(TemplatesProjectConfiguration configs, Map<String, Map<String, Map<String, String>>> inputOutputMaps, Locations locations, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil
                .generateClassInit(Constants.CATALOGUE_DISPATCHER)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(CatalogueDispatcherInterface.class), ClassName.get(FileBuilder.class)));


        MethodSpec.Builder cspec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapString2StringType, "map")
                .addParameter(ProvFactory.class, "pf");
        compilerUtil.specWithComment(cspec);

        for (String data: dataConfiguratorMap.keySet()) {
            if (!configs.integrator && (integratorRequired.contains(data) || storageRequired.contains(data))) {
                builder.addMethod(createNullGetterBuilder(data, dataTypeMap.get(data)).build());
                if (storageRequired.contains(data)) {
                    builder.addMethod(createNullInitBuilder(data).build());
                }
                continue;
            };
            if (configs.sqlFile==null && storageRequired.contains(data)) {
                builder.addMethod(createNullGetterBuilder(data, dataTypeMap.get(data)).build());
                builder.addMethod(createNullInitBuilder(data).build());
                continue;
            };

            String configurator = dataConfiguratorMap.get(data);
            TypeName typeName = dataTypeMap.get(data);

            if (storageRequired.contains(data)) {
                builder.addField(typeName, data, Modifier.PRIVATE);

                MethodSpec.Builder initSpec = MethodSpec.methodBuilder("init" + capitalizeFirstLetter(data))
                        .returns(void.class)
                        .addModifiers(Modifier.PUBLIC);
                compilerUtil.specWithComment(initSpec);
                initSpec.addParameter(FunctionStringResultSet, QUERIER_VAR);
                initSpec.addParameter(BiFunctionIntegerStringObject, POST_PROCESSING_VAR);
                initSpec.addParameter(SupplierOfString, GET_PRINCIPAL_VAR);
                initSpec.addStatement("this.$N=$T.$N(new $T($N,$N,$N))",
                        data,
                        ClassName.get(locations.getFilePackage(configs.name,Constants.LOGGER), Constants.LOGGER),
                        ("compositeEnactorConverter".equals(data))?"initializeCompositeBeanTable":"initializeBeanTable",
                        ClassName.get(locations.getFilePackage(configs.name,configurator), configurator),
                        QUERIER_VAR,POST_PROCESSING_VAR, GET_PRINCIPAL_VAR);
                builder.addMethod(initSpec.build());

            } else {


                builder.addField(typeName, data, Modifier.PRIVATE, Modifier.FINAL);


                if ("foreignTables".equals(data)) {
                    cspec.addStatement("this.$N=$T.initializeBeanTable(new $T<>($T::getForeign))",
                            data,
                            ClassName.get(locations.getFilePackage(configs.name,Constants.LOGGER), Constants.LOGGER),
                            ClassName.get(locations.getFilePackage(configs.name,configurator), configurator),
                            ClassName.get(CLIENT_PACKAGE, BUILDER_INTERFACE));

                } else if ("successors".equals(data)) {
                    cspec.addStatement("this.$N=$T.initializeBeanTable(new $T<>($T::process))",
                            data,
                            ClassName.get(locations.getFilePackage(configs.name,Constants.LOGGER), Constants.LOGGER),
                            ClassName.get(locations.getFilePackage(configs.name,configurator), configurator),
                            ClassName.get(configs.root_package, CATALOGUE_DISPATCHER));

                } else if ("documentBuilderDispatcher".equals(data)) {
                    cspec.addStatement("this.$N=$T.initializeBeanTable(new $T(map,pf))",
                            data,
                            ClassName.get(locations.getFilePackage(configs.name,Constants.LOGGER), Constants.LOGGER),
                            ClassName.get(locations.getFilePackage(configs.name,configurator), configurator));
                } else if ("typeAssignment".equals(data)) {
                    cspec.addStatement("this.$N=$T.initializeBeanTable(new $T(map,propertyOrder,documentBuilderDispatcher,pf))",
                            data,
                            ClassName.get(locations.getFilePackage(configs.name,Constants.LOGGER), Constants.LOGGER),
                            ClassName.get(locations.getFilePackage(configs.name,configurator), configurator));
                } else if ("recordMaker".equals(data)) {
                    cspec.addStatement("this.$N=$T.initializeBeanTable(new $T(documentBuilderDispatcher))",
                            data,
                            ClassName.get(locations.getFilePackage(configs.name,Constants.LOGGER), Constants.LOGGER),
                            ClassName.get(locations.getFilePackage(configs.name,configurator), configurator));
                } else {
                    if (sqlRelated.contains(data) && configs.sqlFile==null) {
                        cspec.addStatement("this.$N=null /* no sql file*/", data);
                    } else {
                        cspec.addStatement("this.$N=$T.initializeBeanTable(new $T())",
                                data,
                                ClassName.get(locations.getFilePackage(configs.name,Constants.LOGGER), Constants.LOGGER),
                                ClassName.get(locations.getFilePackage(configs.name,configurator), configurator));
                    }
                }

            }


            MethodSpec.Builder getterSpec = createGetterBuilder(configs, data, typeName);
            builder.addMethod(getterSpec.build());




        }

        builder.addMethod(cspec.build());



        try {
            builder.addField(FieldSpec
                    .builder(  ClassName.get(String.class), "ioMap", Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("$S", objectMapper.writeValueAsString(inputOutputMaps))
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        MethodSpec.Builder getterSpec = createGetterBuilder(configs, "ioMap", ClassName.get(String.class));
        builder.addMethod(getterSpec.build());




        // construct MethodSpec for
            /* static public Map<String, List<String>> process(Builder builder) {
        Map<Integer, int[]>  successors = builder.getSuccessors();
        String [] order = builder.getPropertyOrder();
        return successors
                .keySet()
                .stream()
                .filter(k -> successors.get(k).length!=0)
                .collect(Collectors.toMap(
                        k -> order[k],
                        k -> Arrays
                                .stream(successors.get(k))
                                .mapToObj(v -> order[v])
                                .collect(Collectors.toList())));
                                */

        MethodSpec.Builder processSpec = MethodSpec.methodBuilder("process")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .returns(mapString2StringList)
                .addParameter(ClassName.get(CLIENT_PACKAGE, BUILDER_INTERFACE), "builder");
        compilerUtil.specWithComment(processSpec);
        processSpec.addStatement("""
                Map<Integer, int[]>  successors = builder.getSuccessors();
                $T order = builder.getPropertyOrder();
                return successors
                        .keySet()
                        .stream()
                        .filter(k -> successors.get(k).length!=0)
                        .collect($T.toMap(
                                  k -> order[k],
                                  k -> $T
                                        .stream(successors.get(k))
                                        .mapToObj(v -> order[v])
                                        .collect($T.toList())))""",
                stringArray, Collectors.class, Arrays.class, Collectors.class);

        builder.addMethod(processSpec.build());


        TypeSpec theCatalogueDispatcher=builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theCatalogueDispatcher, configs, configs.root_package, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, configs.root_package);
    }

    private MethodSpec.Builder createGetterBuilder(TemplatesProjectConfiguration configs, String data, TypeName typeName) {
        MethodSpec.Builder getterSpec = MethodSpec.methodBuilder("get" + capitalizeFirstLetter(data))
                .returns(typeName)
                .addModifiers(Modifier.PUBLIC);
        compilerUtil.specWithComment(getterSpec);
        if (storageRequired.contains(data)) {
            getterSpec.addStatement("if ($N==null) throw new $T(\"non initialized field $N\")", data, IllegalStateException.class, data);
        }
        if (configs.sqlFile==null && storageRequired.contains(data)) {
            getterSpec.addStatement("return null", data);
        } else {
            getterSpec.addStatement("return $N", data);
        }
        return getterSpec;
    }

    private MethodSpec.Builder createNullGetterBuilder(String data, TypeName typeName) {
        MethodSpec.Builder getterSpec = MethodSpec.methodBuilder("get" + capitalizeFirstLetter(data))
                .returns(typeName)
                .addModifiers(Modifier.PUBLIC);
        compilerUtil.specWithComment(getterSpec);
        getterSpec.addStatement("return null", data);
        return getterSpec;
    }


    private MethodSpec.Builder createNullInitBuilder(String data) {
        MethodSpec.Builder initSpec = MethodSpec.methodBuilder("init" + capitalizeFirstLetter(data))
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC);
        compilerUtil.specWithComment(initSpec);
        initSpec.addComment("null sqlFile or false integrator flag");
        initSpec.addParameter(FunctionStringResultSet, QUERIER_VAR);
        initSpec.addParameter(BiFunctionIntegerStringObject, POST_PROCESSING_VAR);
        initSpec.addParameter(SupplierOfString, GET_PRINCIPAL_VAR);
        initSpec.addStatement("return");
        return initSpec;
    }


    public String capitalizeFirstLetter(String s) {
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }

    static private TypeName mapOf(TypeName t) {
        return ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), t);
    }

}