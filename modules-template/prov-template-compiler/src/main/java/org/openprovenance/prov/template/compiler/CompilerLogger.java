package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.annotations.ClassInitialiser;
import org.openprovenance.prov.template.compiler.past.annotations.ClassMethod;
import org.openprovenance.prov.template.compiler.past.type.ArrayType;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.openprovenance.prov.model.DOMProcessing.builder;
import static org.openprovenance.prov.template.compiler.CompilerBeanGenerator.newSpecificationFiles;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.objectMapper;
import static org.openprovenance.prov.template.compiler.common.CompilerCommon.*;
import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.common.Constants.BUILDER_INTERFACE;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.Variable.VariableKind.STATIC_FIELD_VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerLogger {
    public static final String __BUILDERS_VAR = GENERATED_VAR_PREFIX + "builders";
    public static final String A_TABLE_VAR = "aTable";
    private final CompilerUtil compilerUtil;

    public CompilerLogger(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    final PastFactory pastFactory=new PastFactory();

    SpecificationFile generateLogger(TemplatesProjectConfiguration configs, Locations locations, String fileName, Map<String, Map<String, Map<String, String>>> inputOutputMaps) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        Class pastClass = pastFactory.CLASS(LOGGER)
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(org.openprovenance.prov.template.compiler.past.type.ClassName.get(LOGGER_INTERFACE, Constants.CLIENT_PACKAGE));

        for (TemplateCompilerConfig config : configs.templates) {
            // if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            Field field = generateStaticFieldForTemplate(locations, config);
            pastClass.FIELDS(field);
        }
        List<String> templates= Arrays.stream(configs.templates).map(x->x.name).collect(Collectors.toList());
        ArrayType builderArrayPastType = ArrayType.of(org.openprovenance.prov.template.compiler.past.type.ClassName.get("Builder", CLIENT_PACKAGE));
        org.openprovenance.prov.template.compiler.past.type.ClassName builderPastType = org.openprovenance.prov.template.compiler.past.type.ClassName.get("Builder", CLIENT_PACKAGE);
        pastClass.FIELDS(FIELD(__BUILDERS_VAR, builderArrayPastType)
                .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .INITIALIZER(new ArrayInitialiser(builderPastType, makeRenamedArgsList2(null,templates))))
        ;

        pastClass.METHOD(generateGetBuilderMethod(builderArrayPastType));





        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof SimpleTemplateCompilerConfig) {
                pastClass.METHOD(generateStaticLogMethod((SimpleTemplateCompilerConfig) config, locations));
            }
        }



        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof SimpleTemplateCompilerConfig) {
                pastClass.METHOD(generateStaticBeanMethod((SimpleTemplateCompilerConfig) config, locations));
            }
        }
        pastClass.METHOD(generateInitializeBeanTableMethod(configs, locations));

        org.openprovenance.prov.template.compiler.past.type.ClassName builderConfigurator= org.openprovenance.prov.template.compiler.past.type.ClassName.get(BUILDER_CONFIGURATOR, locations.getFilePackage(configs.name, BUILDER_CONFIGURATOR));

        pastClass.FIELDS(
                FIELD("simpleBuilders", MAP_STRING_BUILDER)
                .MODIFIERS(Modifier.STATIC, Modifier.PUBLIC)
                .INITIALIZER(METHOD_CALL(INITIALIZE_BEAN_TABLE,List.of(CONSTRUCTOR_CALL(builderConfigurator,List.of()))))
                .ANNOTATION(ClassInitialiser.NAME));

        pastClass.FIELDS(
                FIELD("simpleBeanConverters", ParameterizedType.get(MAP, STRING, FUNCTION_OBJARRAY_TO_ANY))
                        .MODIFIERS(Modifier.STATIC, Modifier.PUBLIC)
                        .INITIALIZER(METHOD_CALL(INITIALIZE_BEAN_TABLE,List.of(CONSTRUCTOR_CALL(org.openprovenance.prov.template.compiler.past.type.ClassName.get(CONVERTER_CONFIGURATOR,locations.getFilePackage(configs.name, CONVERTER_CONFIGURATOR)),List.of()))))
                        .ANNOTATION(ClassInitialiser.NAME));

        pastClass.FIELDS(
                FIELD("simpleCSvConverters", ParameterizedType.get(MAP, STRING, FUNCTION_OBJARRAY_TO_STRING))
                        .MODIFIERS(Modifier.STATIC, Modifier.PUBLIC)
                        .INITIALIZER(METHOD_CALL(INITIALIZE_BEAN_TABLE,List.of(CONSTRUCTOR_CALL(org.openprovenance.prov.template.compiler.past.type.ClassName.get(CSV_CONFIGURATOR,locations.getFilePackage(configs.name, CSV_CONFIGURATOR)),List.of()))))
                        .ANNOTATION(ClassInitialiser.NAME));

        try {
            pastClass.FIELDS(
                    FIELD("ioMap", STRING)
                            .MODIFIERS(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                            .INITIALIZER(CONSTANT(objectMapper.writeValueAsString(inputOutputMaps))));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        pastClass.METHOD(generateInitializeCompositeBeanTableMethod(configs, locations));

        String packageName = locations.getFilePackage(configs.name, LOGGER);

        //System.out.println("********** Generating logger class " + pastClass.name + " in package " + packageName  + " with filename " + fileName);

        String directory = locations.convertToDirectory(packageName);
        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, packageName, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packageName, configs, fileName, directory, stackTraceElement, compilerUtil);
        SpecificationFile specFile=new SpecificationFile(javaGenerator,pythonGenerator);

        return specFile;

    }

    private Field generateStaticFieldForTemplate(Locations locations, TemplateCompilerConfig config) {
        final String templateNameClass = compilerUtil.templateNameClass(config.name);
        final org.openprovenance.prov.template.compiler.past.type.ClassName className = org.openprovenance.prov.template.compiler.past.type.ClassName.get(templateNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));
        return (FIELD(Constants.GENERATED_VAR_PREFIX + config.name, className)
                .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .COMMENT("Generated by method $N", getClass().getName()+".generateStaticFieldForTemplate()")
                .INITIALIZER(CONSTRUCTOR_CALL(className, List.of())));
    }

    private Method generateInitializeBeanTableMethod(TemplatesProjectConfiguration configs, Locations locations) {
        Method builder = METHOD(INITIALIZE_BEAN_TABLE).ANNOTATIONS(ClassMethod.NAME)
                .COMMENT("Initialize a table of bean builders\n")
                .COMMENT("@param $N a table configurator \n", "configurator")
                .COMMENT("@param <T> type variable for the result associated with each template name\n")
                .COMMENT("@return $T&lt;$T,$T&gt;\n", Map.class,String.class, TypeVariableName.get("T"))
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariables(T())
                .RETURNS(MAP_STRING_T);
        compilerUtil.debugFileLocation(builder);


        builder.PARAMETER(ParameterizedType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.get(TABLE_CONFIGURATOR,locations.getFilePackage(configs.name, TABLE_CONFIGURATOR)), T()), "configurator");

        builder.BODY(ASSIGNMENT(MAP_STRING_T, VARIABLE(A_TABLE_VAR), CONSTRUCTOR_CALL(HASH_MAP_STRING_T,List.of())));
        //builder.addStatement("$T $N=$N $T()",mapType, A_TABLE_VAR,"new", mapType2);

        for (TemplateCompilerConfig config : configs.templates) {

            String thisBuilderName = Constants.GENERATED_VAR_PREFIX + config.name;
           //builder.addStatement("$N.$N($N.$N()$N,$N.$N($N))", A_TABLE_VAR, "put", thisBuilderName, "getFullyQualifiedName", MARKER_PARAMS, "configurator", config.name, thisBuilderName);

            builder.BODY(METHOD_CALL(
                    VARIABLE(A_TABLE_VAR),
                    "put",
                    List.of(METHOD_CALL(VARIABLE(thisBuilderName, STATIC_FIELD_VARIABLE), "getFullyQualifiedName", List.of()),
                            METHOD_CALL(VARIABLE("configurator"), config.name, VARIABLE(thisBuilderName,STATIC_FIELD_VARIABLE)))));
        }


        builder.BODY(RETURN(VARIABLE(A_TABLE_VAR)));

        return builder;

    }

    private Method generateInitializeCompositeBeanTableMethod(TemplatesProjectConfiguration configs, Locations locations) {
        ParameterizedType parameterType = ParameterizedType.get(get(COMPOSITE_TABLE_CONFIGURATOR,locations.getFilePackage(configs.name, COMPOSITE_TABLE_CONFIGURATOR) ), T());

        Method method = METHOD("initializeCompositeBeanTable")
                .COMMENT("Initialize a table of composite bean builders\n")
                .COMMENT("@param $N a table configurator \n", "configurator")
                .COMMENT("@param <T> type variable for the result associated with each template name\n")
                .COMMENT("@return $T&lt;$T,$T&gt;\n", Map.class,String.class, TypeVariableName.get("T"))
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariables(T())
                .RETURNS(MAP_STRING_T);
        compilerUtil.debugFileLocation(method);

        method.PARAMETER(parameterType, "configurator");
        method.BODY(ASSIGNMENT(MAP_STRING_T, VARIABLE(A_TABLE_VAR), CONSTRUCTOR_CALL(HASH_MAP_STRING_T, List.of())));

        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) {
                String thisBuilderName = Constants.GENERATED_VAR_PREFIX + config.name;
                method.BODY(METHOD_CALL(
                        VARIABLE(A_TABLE_VAR),
                        "put",
                        List.of(METHOD_CALL(VARIABLE(thisBuilderName, STATIC_FIELD_VARIABLE), "getFullyQualifiedName", List.of()),
                                METHOD_CALL(VARIABLE("configurator"), config.name, List.of(VARIABLE(thisBuilderName, STATIC_FIELD_VARIABLE))))));
            }
        }
        method.BODY(RETURN(VARIABLE(A_TABLE_VAR)));
        return method;

    }

    SpecificationFile generateBuilderInterface(TemplatesProjectConfiguration configs, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(BUILDER_INTERFACE);


        MethodSpec.Builder builder2 = MethodSpec.methodBuilder(Constants.GET_NODES_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int[].class);
        builder.addMethod(builder2.build());


        MethodSpec.Builder builder3 = MethodSpec.methodBuilder(Constants.GET_SUCCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(CompilerUtil.mapIntArrayType);
        builder.addMethod(builder3.build());


        MethodSpec.Builder builder3b = MethodSpec.methodBuilder(Constants.GET_TYPED_SUCCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(CompilerUtil.mapIntArrayType);
        builder.addMethod(builder3b.build());

        MethodSpec.Builder builder3c = MethodSpec.methodBuilder(Constants.GET_FOREIGN)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(ArrayTypeName.of(String.class));
        builder.addMethod(builder3c.build());

        MethodSpec.Builder builder4 = MethodSpec.methodBuilder(Constants.GET_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder4.build());

        //TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));

        TypeName myType=functionObjArrayTo(ClassName.get(String.class));
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.RECORD_CSV_PROCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(ArrayTypeName.of(Object.class),"record").build())
                .returns(myType);
        builder.addMethod(builder5.build());

        MethodSpec.Builder builder6 = MethodSpec.methodBuilder(Constants.PROPERTY_ORDER_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String[].class);
        builder.addMethod(builder6.build());


        MethodSpec.Builder builder7 = MethodSpec.methodBuilder(Constants.GET_TEMPLATE_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder7.build());

        MethodSpec.Builder builder8 = MethodSpec.methodBuilder(GET_FULLY_QUALIFIED_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder8.build());




        TypeSpec theInterface = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theInterface, configs, Constants.CLIENT_PACKAGE, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, Constants.CLIENT_PACKAGE);
    }
    SpecificationFile generateBuilderInterface_old(TemplatesProjectConfiguration configs, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(BUILDER_INTERFACE);


        MethodSpec.Builder builder2 = MethodSpec.methodBuilder(Constants.GET_NODES_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int[].class);
        builder.addMethod(builder2.build());


        MethodSpec.Builder builder3 = MethodSpec.methodBuilder(Constants.GET_SUCCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(CompilerUtil.mapIntArrayType);
        builder.addMethod(builder3.build());


        MethodSpec.Builder builder3b = MethodSpec.methodBuilder(Constants.GET_TYPED_SUCCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(CompilerUtil.mapIntArrayType);
        builder.addMethod(builder3b.build());

        MethodSpec.Builder builder3c = MethodSpec.methodBuilder(Constants.GET_FOREIGN)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(ArrayTypeName.of(String.class));
        builder.addMethod(builder3c.build());

        MethodSpec.Builder builder4 = MethodSpec.methodBuilder(Constants.GET_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder4.build());

        //TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));

        TypeName myType=functionObjArrayTo(ClassName.get(String.class));
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.RECORD_CSV_PROCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(ArrayTypeName.of(Object.class),"record").build())
                .returns(myType);
        builder.addMethod(builder5.build());

        MethodSpec.Builder builder6 = MethodSpec.methodBuilder(Constants.PROPERTY_ORDER_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String[].class);
        builder.addMethod(builder6.build());


        MethodSpec.Builder builder7 = MethodSpec.methodBuilder(Constants.GET_TEMPLATE_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder7.build());

        MethodSpec.Builder builder8 = MethodSpec.methodBuilder(GET_FULLY_QUALIFIED_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder8.build());




        TypeSpec theInterface = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theInterface, configs, Constants.CLIENT_PACKAGE, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, Constants.CLIENT_PACKAGE);
    }

    SpecificationFile generateLoggerInterface(TemplatesProjectConfiguration configs, String directory, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        Class pastClass = pastFactory.CLASS(LOGGER_INTERFACE, true)
                .MODIFIERS(Modifier.PUBLIC);

       // TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(Constants.LOGGER_INTERFACE);

     //   org.openprovenance.prov.template.compiler.past.type.ClassName cln = get("Builder", CLIENT_PACKAGE);
       // ArrayTypeName builderArrayType = ArrayTypeName.of(cln);

        Method builder2 = METHOD(Constants.GET_BUILDERS_METHOD)
                .COMMENT("Returns the array of builders")
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .RETURNS(BUILDERS_ARRAY);

        pastClass.METHOD(builder2);



        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, CLIENT_PACKAGE, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, CLIENT_PACKAGE, configs, fileName+ DOT_JAVA_EXTENSION, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator,pythonGenerator);

/*
        TypeSpec theInterface = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theInterface, configs, Constants.CLIENT_PACKAGE, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, Constants.CLIENT_PACKAGE);
        */

    }

    private Method generateStaticLogMethod(SimpleTemplateCompilerConfig config, Locations locations) {
        final String loggerName = compilerUtil.loggerName(config.name);
        Method builder = METHOD(loggerName)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .ANNOTATIONS(ClassMethod.NAME) // annotation aimed at python conversion
                .RETURNS(STRING);
        compilerUtil.debugFileLocation(builder);

        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);
        Map<String, List<Descriptor>> theVars=bindingsSchema.getVar();

        compilerUtil.generateSpecializedParameters(builder, theVars);
        compilerUtil.generateSpecializedParametersJavadoc(builder, theVars, bindingsSchema.getDocumentation(), bindingsSchema.getReturnValue());

        List<Expression> argsList = convertToLocalVariableList(bindingsSchema);

        builder.BODY(
                RETURN(
                        FUNCTIONAL_METHOD_CALL(
                                METHOD_CALL(
                                        VARIABLE(GENERATED_VAR_PREFIX + config.name, STATIC_FIELD_VARIABLE),
                                        ARGS_CSV_CONVERSION_METHOD,
                                        List.of()),
                                "process",
                                argsList)));
        return builder;
    }

    public static List<Expression> convertToLocalVariableList(TemplateBindingsSchema bindingsSchema) {
        List<String> variables=new LinkedList<>();
        Map<String, List<Descriptor>> theVars=bindingsSchema.getVar();
        for (String key : theVars.keySet()) {
            if (theVars.containsKey(key) && theVars.get(key) != null) {
                variables.add(key);
            }
        }
        return variables.stream().map(Variable::VARIABLE).collect(Collectors.toList());
    }

    private Method generateStaticBeanMethod(SimpleTemplateCompilerConfig config, Locations locations) {
        final String beanCreatorName = "bean"+compilerUtil.capitalize(config.name);

        Method method = METHOD(beanCreatorName)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.STATIC)
                .ANNOTATIONS(ClassMethod.NAME) // annotation aimed at python conversion
                .RETURNS(org.openprovenance.prov.template.compiler.past.type.ClassName.get(compilerUtil.commonNameClass(config.name), locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON)));
        compilerUtil.debugFileLocation(method);

        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);
        Map<String, List<Descriptor>> theVars=bindingsSchema.getVar();

        compilerUtil.generateSpecializedParameters(method, theVars);
        compilerUtil.generateSpecializedParametersJavadoc(method, theVars, bindingsSchema.getDocumentation(), bindingsSchema.getReturnValue());

        List<Expression> argsList = convertToLocalVariableList(compilerUtil.getBindingsSchema(config));

        method.BODY(
                RETURN(
                        FUNCTIONAL_METHOD_CALL(
                                METHOD_CALL(
                                        VARIABLE(GENERATED_VAR_PREFIX + config.name, STATIC_FIELD_VARIABLE),
                                        A_ARGS_BEAN_CONVERTER),
                                "process",
                                argsList)));
        return method;
    }

    private Method generateGetBuilderMethod(ArrayType builderArrayType) {
        Method method = METHOD("getBuilders")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(builderArrayType);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(VARIABLE(__BUILDERS_VAR, Variable.VariableKind.FIELD_VARIABLE)));
        return method;

    }
}