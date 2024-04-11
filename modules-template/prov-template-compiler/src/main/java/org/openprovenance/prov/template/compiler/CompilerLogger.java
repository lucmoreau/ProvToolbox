package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.CompilerBeanGenerator.newSpecificationFiles;
import static org.openprovenance.prov.template.compiler.CompilerConfigurations.processorOfString;
import static org.openprovenance.prov.template.compiler.CompilerConfigurations.processorOfUnknown;
import static org.openprovenance.prov.template.compiler.CompilerUtil.builderMapType;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.typeT;
import static org.openprovenance.prov.template.compiler.common.CompilerCommon.*;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerLogger {
    public static final String __BUILDERS_VAR = GENERATED_VAR_PREFIX + "builders";
    public static final String A_TABLE_VAR = "aTable";
    private final CompilerUtil compilerUtil;

    public CompilerLogger(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateLogger(TemplatesCompilerConfig configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(configs.logger);
        builder.addSuperinterface(ClassName.get(Constants.CLIENT_PACKAGE, LOGGER_INTERFACE));

        for (TemplateCompilerConfig config : configs.templates) {
           // if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), templateNameClass);
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
            locations.updateWithConfig(config);
            if (config instanceof SimpleTemplateCompilerConfig) {
                builder.addMethod(generateStaticLogMethod((SimpleTemplateCompilerConfig) config, locations));
                builder.addMethod(generateStaticBeanMethod((SimpleTemplateCompilerConfig) config, locations));
            }
        }

        builder.addMethod(generateInitializeBeanTableMethod(configs, locations));
        builder.addMethod(generateInitializeCompositeBeanTableMethod(configs, locations));

        builder.addField(FieldSpec
                .builder(builderMapType, "simpleBuilders", Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$N($N $T())", INITIALIZE_BEAN_TABLE, "new", ClassName.get(configs.configurator_package, BUILDER_CONFIGURATOR))
                .build());
        builder.addField(FieldSpec
                .builder( ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), processorOfUnknown), "simpleBeanConverters", Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$N($N $T())", INITIALIZE_BEAN_TABLE, "new", ClassName.get(configs.configurator_package,CONVERTER_CONFIGURATOR))
                .build());

        builder.addField(FieldSpec
                .builder( ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), processorOfString), "simpleCSvConverters", Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$N($N $T())", INITIALIZE_BEAN_TABLE, "new", ClassName.get(configs.configurator_package,CSV_CONFIGURATOR))
                .addJavadoc("generated Automatically by ProvToolbox ($N.$N())", this.getClass().getSimpleName(), "generateLogger")
                .build());


        TypeSpec theLogger = builder.build();

        String myPackage = locations.getFilePackage(fileName);
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

    private MethodSpec generateInitializeBeanTableMethod(TemplatesCompilerConfig configs, Locations locations) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(INITIALIZE_BEAN_TABLE)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(typeT)
                .returns(mapType);
        compilerUtil.specWithComment(builder);


        builder.addParameter( ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.tableConfigurator), configs.tableConfigurator), TypeVariableName.get("T")), "configurator");

        builder.addStatement("$T $N=$N $T()",mapType, A_TABLE_VAR,"new", mapType2);

        for (TemplateCompilerConfig config : configs.templates) {

            String thisBuilderName = Constants.GENERATED_VAR_PREFIX + config.name;
            builder.addStatement("$N.$N($N.$N()$N,$N.$N($N))", A_TABLE_VAR, "put", thisBuilderName, "getName", MARKER_PARAMS, "configurator", config.name, thisBuilderName);


        }


        builder.addStatement("return $N", A_TABLE_VAR);

        return builder.build();

    }

    private MethodSpec generateInitializeCompositeBeanTableMethod(TemplatesCompilerConfig configs, Locations locations) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("initializeCompositeBeanTable")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(typeT)
                .returns(mapType);
        compilerUtil.specWithComment(builder);

        String compositeTableConfigurator = COMPOSITE + configs.tableConfigurator;
        builder.addParameter( ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(compositeTableConfigurator), compositeTableConfigurator), typeT), "configurator");


        builder.addStatement("$T $N=$N $T()",mapType, A_TABLE_VAR, "new", mapType2);

        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) {
                String thisBuilderName = Constants.GENERATED_VAR_PREFIX + config.name;
                builder.addStatement("$N.$N($N.$N()$N,$N.$N($N))", A_TABLE_VAR, "put", thisBuilderName, "getName", MARKER_PARAMS, "configurator", config.name, thisBuilderName);
            }
        }


        builder.addStatement("return $N", A_TABLE_VAR);

        return builder.build();

    }

    SpecificationFile generateBuilderInterface(TemplatesCompilerConfig configs, String directory, String fileName) {
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

        MethodSpec.Builder builder4 = MethodSpec.methodBuilder(Constants.GET_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder4.build());

        TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));

        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.RECORD_CSV_PROCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(ArrayTypeName.of(Object.class),"record").build())
                .returns(myType);
        builder.addMethod(builder5.build());

        MethodSpec.Builder builder6 = MethodSpec.methodBuilder(Constants.PROPERTY_ORDER_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String[].class);
        builder.addMethod(builder6.build());

        TypeSpec theInterface = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theInterface, configs, Constants.CLIENT_PACKAGE, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, Constants.CLIENT_PACKAGE);
    }

    SpecificationFile generateLoggerInterface(TemplatesCompilerConfig configs, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(Constants.LOGGER_INTERFACE);

        ClassName cln = ClassName.get(Constants.CLIENT_PACKAGE, "Builder");
        ArrayTypeName builderArrayType = ArrayTypeName.of(cln);

        MethodSpec.Builder builder2 = MethodSpec.methodBuilder(Constants.GET_BUILDERS_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(builderArrayType);
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

        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_documentation = bindings_schema.get("@documentation");
        JsonNode the_return = bindings_schema.get("@return");
        compilerUtil.generateSpecializedParameters(builder, the_var);
        compilerUtil.generateSpecializedParametersJavadoc(builder, the_var, the_documentation, the_return);

        CodeBlock argsList = convertToArgsList(the_var);


        builder.addStatement("return $N.$N().$N($L)", Constants.GENERATED_VAR_PREFIX + config.name, Constants.ARGS_CSV_CONVERSION_METHOD, "process",argsList);
        return builder.build();
    }

    public static CodeBlock convertToArgsList(JsonNode the_var) {
        List<String> variables=new LinkedList<>();
        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            variables.add(key);
        }
        return makeArgsList(variables);
    }

    public MethodSpec generateStaticBeanMethod(SimpleTemplateCompilerConfig config, Locations locations) {
        final String beanCreatorName = "bean"+compilerUtil.capitalize(config.name);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(beanCreatorName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.STATIC)
                .returns(ClassName.get(locations.getFilePackage(BeanDirection.COMMON),compilerUtil.commonNameClass(config.name)));

        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_documentation = bindings_schema.get("@documentation");
        JsonNode the_return = bindings_schema.get("@return");
        compilerUtil.generateSpecializedParameters(builder, the_var);
        compilerUtil.generateSpecializedParametersJavadoc(builder, the_var, the_documentation, the_return);


        CodeBlock argsList = convertToArgsList(the_var);

        builder.addStatement("return $N.$N." + "$N" +  "($L)", Constants.GENERATED_VAR_PREFIX + config.name, Constants.A_ARGS_BEAN_CONVERTER, "process", argsList);

        return builder.build();

    }



    public MethodSpec generateGetBuilderMethod(ArrayTypeName builderArrayType) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getBuilders")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderArrayType);
        compilerUtil.specWithComment(builder);
        builder.addStatement("return $N", __BUILDERS_VAR);
        return builder.build();

    }

    public SpecificationFile generateProcessorArgsInterface(TemplatesCompilerConfig configs, String directory, String fileName) {
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

    public SpecificationFile generateRecordsProcessorInterface(TemplatesCompilerConfig configs, String directory, String fileName) {
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