package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.emitter.Poet;
import org.openprovenance.prov.template.compiler.past.type.ArrayType;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.CompilerBeanGenerator.newSpecificationFiles;
import static org.openprovenance.prov.template.compiler.CompilerConfigurations.processorOfString;
import static org.openprovenance.prov.template.compiler.CompilerConfigurations.processorOfUnknown;
import static org.openprovenance.prov.template.compiler.CompilerUtil.builderMapType;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.objectMapper;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.typeT;
import static org.openprovenance.prov.template.compiler.common.CompilerCommon.*;
import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.STRING;

public class CompilerLogger {
    public static final String __BUILDERS_VAR = GENERATED_VAR_PREFIX + "builders";
    public static final String A_TABLE_VAR = "aTable";
    private final CompilerUtil compilerUtil;

    public CompilerLogger(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateLogger(TemplatesProjectConfiguration configs, Locations locations, String fileName, Map<String, Map<String, Map<String, String>>> inputOutputMaps) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        PastFactory pastFactory=new PastFactory();
        Class pastClass = pastFactory.CLASS(LOGGER)
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(org.openprovenance.prov.template.compiler.past.type.ClassName.get(LOGGER_INTERFACE, Constants.CLIENT_PACKAGE));

        for (TemplateCompilerConfig config : configs.templates) {
            // if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            final org.openprovenance.prov.template.compiler.past.type.ClassName className = org.openprovenance.prov.template.compiler.past.type.ClassName.get(templateNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));
            pastClass.FIELDS(Field.FIELD(Constants.GENERATED_VAR_PREFIX + config.name, className)
                    .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                    .INITIALIZER(CONSTRUCTOR_CALL(className, List.of())))
            ;

        }

        List<String> templates= Arrays.stream(configs.templates).map(x->x.name).collect(Collectors.toList());
        ArrayTypeName builderArrayType = ArrayTypeName.of(ClassName.get(Constants.CLIENT_PACKAGE, "Builder"));
        ArrayType builderArrayPastType = ArrayType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.get("Builder", CLIENT_PACKAGE));
        pastClass.FIELDS(Field.FIELD(__BUILDERS_VAR, builderArrayPastType)
                .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .INITIALIZER(new ArrayInitialiser(builderArrayPastType, makeRenamedArgsList2(null,templates))))
        ;

        pastClass.METHOD(generateGetBuilderMethod(builderArrayPastType));





        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof SimpleTemplateCompilerConfig) {
                pastClass.METHOD(generateStaticLogMethod_new((SimpleTemplateCompilerConfig) config, locations));
            }
        }

        TypeSpec.Builder builder = new Poet().emitBuilder(pastClass);


        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof SimpleTemplateCompilerConfig) {
                builder.addMethod(generateStaticBeanMethod((SimpleTemplateCompilerConfig) config, locations));
            }
        }

        builder.addMethod(generateInitializeBeanTableMethod(configs, locations));
        builder.addMethod(generateInitializeCompositeBeanTableMethod(configs, locations));

        builder.addField(FieldSpec
                .builder(builderMapType, "simpleBuilders", Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$N($N $T())", INITIALIZE_BEAN_TABLE, "new", ClassName.get(locations.getFilePackage(configs.name, BUILDER_CONFIGURATOR), BUILDER_CONFIGURATOR))
                .build());
        builder.addField(FieldSpec
                .builder( ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), processorOfUnknown), "simpleBeanConverters", Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$N($N $T())", INITIALIZE_BEAN_TABLE, "new", ClassName.get(locations.getFilePackage(configs.name, CONVERTER_CONFIGURATOR),CONVERTER_CONFIGURATOR))
                .build());

        builder.addField(FieldSpec
                .builder( ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), processorOfString), "simpleCSvConverters", Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$N($N $T())", INITIALIZE_BEAN_TABLE, "new", ClassName.get(locations.getFilePackage(configs.name, CSV_CONFIGURATOR),CSV_CONFIGURATOR))
                // python conversion does not support javadoc .addJavadoc("generated Automatically by ProvToolbox ($N.$N())", this.getClass().getSimpleName(), "generateLogger")
                .build());

        try {
            builder.addField(FieldSpec
                    .builder(  ClassName.get(String.class), "ioMap", Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                    .initializer("$S", objectMapper.writeValueAsString(inputOutputMaps))
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        TypeSpec theLogger = builder.build();

        String myPackage = locations.getFilePackage(configs.name, fileName);
        String directory = locations.convertToDirectory(myPackage);

        try {
            new org.openprovenance.prov.template.compiler.past.emitter.Python()
                    .toWritableObject(pastClass, fileName, myPackage, stackTraceElement)
                    .writeTo(new File("target/python"));
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            try {
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(System.err, pastClass);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }


        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        if (locations.python_dir==null) {
            return new SpecificationFile(myfile, directory, fileName + DOT_JAVA_EXTENSION, myPackage);
        } else {
            return newSpecificationFiles(compilerUtil, locations, theLogger, configs, stackTraceElement, myfile, directory, fileName + DOT_JAVA_EXTENSION, myPackage, null);
        }
    }
    SpecificationFile generateLogger_OLD(TemplatesProjectConfiguration configs, Locations locations, String fileName, Map<String, Map<String, Map<String, String>>> inputOutputMaps) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(LOGGER);
        builder.addSuperinterface(ClassName.get(Constants.CLIENT_PACKAGE, LOGGER_INTERFACE));

        for (TemplateCompilerConfig config : configs.templates) {
            // if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            final ClassName className = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON), templateNameClass);
            FieldSpec fspec = FieldSpec.builder(className, Constants.GENERATED_VAR_PREFIX + config.name)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                    .initializer("$N $T()", "new", className)
                    .build();

            builder.addField(fspec);
        }

        List<String> templates= Arrays.stream(configs.templates).map(x->x.name).collect(Collectors.toList());
        ArrayTypeName builderArrayType = ArrayTypeName.of(ClassName.get(Constants.CLIENT_PACKAGE, "Builder"));
        FieldSpec fspec = FieldSpec.builder(builderArrayType, __BUILDERS_VAR)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("$N $T $N {$L}", "new", builderArrayType, MARKER_ARRAY, makeRenamedArgsList(null,templates))
                .build();

        builder.addField(fspec);

        builder.addMethod(generateGetBuilderMethod(builderArrayType));


        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof SimpleTemplateCompilerConfig) {
                builder.addMethod(generateStaticLogMethod((SimpleTemplateCompilerConfig) config, locations));
                builder.addMethod(generateStaticBeanMethod((SimpleTemplateCompilerConfig) config, locations));
            }
        }

        builder.addMethod(generateInitializeBeanTableMethod(configs, locations));
        builder.addMethod(generateInitializeCompositeBeanTableMethod(configs, locations));

        builder.addField(FieldSpec
                .builder(builderMapType, "simpleBuilders", Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$N($N $T())", INITIALIZE_BEAN_TABLE, "new", ClassName.get(locations.getFilePackage(configs.name, BUILDER_CONFIGURATOR), BUILDER_CONFIGURATOR))
                .build());
        builder.addField(FieldSpec
                .builder( ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), processorOfUnknown), "simpleBeanConverters", Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$N($N $T())", INITIALIZE_BEAN_TABLE, "new", ClassName.get(locations.getFilePackage(configs.name, CONVERTER_CONFIGURATOR),CONVERTER_CONFIGURATOR))
                .build());

        builder.addField(FieldSpec
                .builder( ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), processorOfString), "simpleCSvConverters", Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$N($N $T())", INITIALIZE_BEAN_TABLE, "new", ClassName.get(locations.getFilePackage(configs.name, CSV_CONFIGURATOR),CSV_CONFIGURATOR))
                // python conversion does not support javadoc .addJavadoc("generated Automatically by ProvToolbox ($N.$N())", this.getClass().getSimpleName(), "generateLogger")
                .build());

        try {
            builder.addField(FieldSpec
                    .builder(  ClassName.get(String.class), "ioMap", Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                    .initializer("$S", objectMapper.writeValueAsString(inputOutputMaps))
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        TypeSpec theLogger = builder.build();

        String myPackage = locations.getFilePackage(configs.name, fileName);
        String directory = locations.convertToDirectory(myPackage);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        if (locations.python_dir==null) {
            return new SpecificationFile(myfile, directory, fileName + DOT_JAVA_EXTENSION, myPackage);
        } else {
            return newSpecificationFiles(compilerUtil, locations, theLogger, configs, stackTraceElement, myfile, directory, fileName + DOT_JAVA_EXTENSION, myPackage, null);
        }
    }

    static final ParameterizedTypeName mapType = ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), TypeVariableName.get("T"));
    static final ParameterizedTypeName mapType2 = ParameterizedTypeName.get(ClassName.get(HashMap.class), TypeName.get(String.class), TypeVariableName.get("T"));

    private MethodSpec generateInitializeBeanTableMethod(TemplatesProjectConfiguration configs, Locations locations) {
        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Initialize a table of bean builders\n");
        jdoc.add("@param $N a table configurator \n", "configurator");
        jdoc.add("@param <T> type variable for the result associated with each template name\n");
        jdoc.add("@return $T&lt;$T,$T&gt;\n", Map.class,String.class, TypeVariableName.get("T"));

        MethodSpec.Builder builder = MethodSpec.methodBuilder(INITIALIZE_BEAN_TABLE)
                .addJavadoc(jdoc.build())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(typeT)
                .returns(mapType);
        compilerUtil.specWithComment(builder);


        builder.addParameter( ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.name, TABLE_CONFIGURATOR), TABLE_CONFIGURATOR), TypeVariableName.get("T")), "configurator");

        builder.addStatement("$T $N=$N $T()",mapType, A_TABLE_VAR,"new", mapType2);

        for (TemplateCompilerConfig config : configs.templates) {

            String thisBuilderName = Constants.GENERATED_VAR_PREFIX + config.name;
            builder.addStatement("$N.$N($N.$N()$N,$N.$N($N))", A_TABLE_VAR, "put", thisBuilderName, "getFullyQualifiedName", MARKER_PARAMS, "configurator", config.name, thisBuilderName);


        }


        builder.addStatement("return $N", A_TABLE_VAR);

        return builder.build();

    }

    private MethodSpec generateInitializeCompositeBeanTableMethod(TemplatesProjectConfiguration configs, Locations locations) {
        ParameterizedTypeName parameterType = ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.name, COMPOSITE_TABLE_CONFIGURATOR), COMPOSITE_TABLE_CONFIGURATOR), typeT);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Initialize a table of composite bean builders\n");
        jdoc.add("@param $N a table configurator \n", "configurator");
        jdoc.add("@param <T> type variable for the result associated with each template name\n");
        jdoc.add("@return $T&lt;$T,$T&gt;\n", Map.class,String.class, TypeVariableName.get("T"));

        MethodSpec.Builder builder = MethodSpec.methodBuilder("initializeCompositeBeanTable")
                .addJavadoc(jdoc.build())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(typeT)
                .returns(mapType);
        compilerUtil.specWithComment(builder);

        builder.addParameter(parameterType, "configurator");


        builder.addStatement("$T $N=$N $T()",mapType, A_TABLE_VAR, "new", mapType2);

        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) {
                String thisBuilderName = Constants.GENERATED_VAR_PREFIX + config.name;
                builder.addStatement("$N.$N($N.$N()$N,$N.$N($N))", A_TABLE_VAR, "put", thisBuilderName, "getFullyQualifiedName", MARKER_PARAMS, "configurator", config.name, thisBuilderName);
            }
        }


        builder.addStatement("return $N", A_TABLE_VAR);

        return builder.build();

    }

    SpecificationFile generateBuilderInterface(TemplatesProjectConfiguration configs, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(Constants.BUILDER_INTERFACE);


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

    SpecificationFile generateLoggerInterface(TemplatesProjectConfiguration configs, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(Constants.LOGGER_INTERFACE);

        ClassName cln = ClassName.get(Constants.CLIENT_PACKAGE, "Builder");
        ArrayTypeName builderArrayType = ArrayTypeName.of(cln);

        MethodSpec.Builder builder2 = MethodSpec.methodBuilder(Constants.GET_BUILDERS_METHOD)
                .addJavadoc("Returns the array of builders")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(builderArrayType)
               ;
        builder.addMethod(builder2.build());

        TypeSpec theInterface = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theInterface, configs, Constants.CLIENT_PACKAGE, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, Constants.CLIENT_PACKAGE);

    }

    public MethodSpec generateStaticLogMethod(SimpleTemplateCompilerConfig config, Locations locations) {
        final String loggerName = compilerUtil.loggerName(config.name);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(loggerName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.STATIC)
                .returns(String.class);
        compilerUtil.specWithComment(builder);

        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);
        Map<String, List<Descriptor>> theVars=bindingsSchema.getVar();

        compilerUtil.generateSpecializedParameters(builder, theVars);
        compilerUtil.generateSpecializedParametersJavadoc(builder, theVars, bindingsSchema.getDocumentation(), bindingsSchema.getReturnValue());

        CodeBlock argsList = convertToArgsList(bindingsSchema);


        builder.addStatement("return $N.$N().$N($L)", Constants.GENERATED_VAR_PREFIX + config.name, Constants.ARGS_CSV_CONVERSION_METHOD, "process",argsList);
        return builder.build();
    }
    public Method generateStaticLogMethod_new(SimpleTemplateCompilerConfig config, Locations locations) {
        final String loggerName = compilerUtil.loggerName(config.name);

        Method builder = METHOD(loggerName)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.STATIC)
                .RETURNS(STRING);
        compilerUtil.debugFileLocation(builder);

        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);
        Map<String, List<Descriptor>> theVars=bindingsSchema.getVar();

        compilerUtil.generateSpecializedParameters(builder, theVars);
        compilerUtil.generateSpecializedParametersJavadoc(builder, theVars, bindingsSchema.getDocumentation(), bindingsSchema.getReturnValue());

        List<Expression> argsList = convertToLocalVariableList(bindingsSchema);

        builder.BODY(
                RETURN(
                        METHOD_CALL(
                                METHOD_CALL(
                                        VARIABLE(GENERATED_VAR_PREFIX + config.name,true),
                                        ARGS_CSV_CONVERSION_METHOD,
                                        List.of()),
                                "process",
                                argsList)));
        return builder;
    }

    public static CodeBlock convertToArgsList(TemplateBindingsSchema bindingsSchema) {
        List<String> variables=new LinkedList<>();
        Map<String, List<Descriptor>> theVars=bindingsSchema.getVar();
        for (String key : theVars.keySet()) {
            if (theVars.containsKey(key) && theVars.get(key) != null) {
                variables.add(key);
            }
        }
        return makeArgsList(variables);
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


    public MethodSpec generateStaticBeanMethod(SimpleTemplateCompilerConfig config, Locations locations) {
        final String beanCreatorName = "bean"+compilerUtil.capitalize(config.name);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(beanCreatorName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.STATIC)
                .returns(ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON),compilerUtil.commonNameClass(config.name)));


        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);
        Map<String, List<Descriptor>> theVars=bindingsSchema.getVar();

        compilerUtil.generateSpecializedParameters(builder, theVars);
        compilerUtil.generateSpecializedParametersJavadoc(builder, theVars, bindingsSchema.getDocumentation(), bindingsSchema.getReturnValue());


        CodeBlock argsList = convertToArgsList(compilerUtil.getBindingsSchema(config));

        builder.addStatement("return $N.$N." + "$N" +  "($L)", Constants.GENERATED_VAR_PREFIX + config.name, Constants.A_ARGS_BEAN_CONVERTER, "process", argsList);

        return builder.build();

    }


    public Method generateGetBuilderMethod(ArrayType builderArrayType) {
        Method method = METHOD("getBuilders")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(builderArrayType);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(VARIABLE(__BUILDERS_VAR,true)));
        return method;

    }
    public MethodSpec generateGetBuilderMethod(ArrayTypeName builderArrayType) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getBuilders")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderArrayType);
        compilerUtil.specWithComment(builder);
        builder.addStatement("return $N", __BUILDERS_VAR);
        return builder.build();

    }

    public SpecificationFile OLDgenerateProcessorArgsInterface(TemplatesProjectConfiguration configs, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInitParameter(PROCESSOR_ARGS_INTERFACE,  CompilerUtil.typeT);

        Object [] args=new Object[0];

        MethodSpec.Builder builder2 = MethodSpec.methodBuilder(PROCESSOR_PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(ArrayTypeName.get(args.getClass()),"args").build())
                .returns(typeT);
        builder.addMethod(builder2.build());

        TypeSpec theInterface = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theInterface, configs, Constants.CLIENT_PACKAGE, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, Constants.CLIENT_PACKAGE);
    }

    public SpecificationFile generateRecordsProcessorInterface(TemplatesProjectConfiguration configs, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInitParameter(RECORDS_PROCESSOR_INTERFACE, CompilerUtil.typeT);

        Object [] args=new Object[0];

        MethodSpec.Builder builder2 = MethodSpec.methodBuilder(PROCESSOR_PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(List.class),ArrayTypeName.get(args.getClass())),"args").build())
                .returns(typeT);
        builder.addMethod(builder2.build());

        TypeSpec theInterface = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theInterface, configs, Constants.CLIENT_PACKAGE, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, Constants.CLIENT_PACKAGE);
    }
}