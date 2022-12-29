package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CompilerLogger {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerLogger() {
    }


    JavaFile generateLogger(TemplatesCompilerConfig configs) {

        TypeSpec.Builder builder = compilerUtil.generateClassInit(configs.logger);
        builder.addSuperinterface(ClassName.get(ConfigProcessor.CLIENT_PACKAGE, "LoggerInterface"));

        String packge = null;
        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, templateNameClass);
            FieldSpec fspec = FieldSpec.builder(className, ConfigProcessor.PREFIX_LOG_VAR + config.name)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                    .initializer("new $T()", className)
                    .build();

            builder.addField(fspec);
        }

        String names = "";
        boolean first = true;
        for (TemplateCompilerConfig config : configs.templates) {
            if (first) {
                first = false;
            } else {
                names = names + ", ";
            }
            names = names + ConfigProcessor.PREFIX_LOG_VAR + config.name;
        }
        ClassName cln = ClassName.get(ConfigProcessor.CLIENT_PACKAGE, "Builder");
        ArrayTypeName builderArrayType = ArrayTypeName.of(cln);
        FieldSpec fspec = FieldSpec.builder(builderArrayType, "__builders")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("new $T[] {" + names + "}", cln)
                .build();

        builder.addField(fspec);

        builder.addMethod(generateGetBuilderMethod(builderArrayType));


        for (TemplateCompilerConfig config : configs.templates) {
            builder.addMethod(generateStaticLogMethod(config));
            builder.addMethod(generateStaticBeanMethod(config));
        }
        
        builder.addMethod(generateInitializeBeanTableMethod(configs));


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.logger_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox ($N) method $N for templates config $N", getClass().getName(), "generateLogger()", configs.name)
                .build();
        return myfile;
    }

    static final ParameterizedTypeName mapType = ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), TypeVariableName.get("T"));
    static final ParameterizedTypeName mapType2 = ParameterizedTypeName.get(ClassName.get(HashMap.class), TypeName.get(String.class), TypeVariableName.get("T"));

    private MethodSpec generateInitializeBeanTableMethod(TemplatesCompilerConfig configs) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("initializeBeanTable")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(mapType);

        builder.addComment("Generated Automatically by ProvToolbox ($N) method $N for templates config $N", getClass().getName(), "generateInitializeBeanTableMethod()", configs.name);

        builder.addParameter( ParameterizedTypeName.get(ClassName.get(configs.logger_package,configs.tableConfigurator), TypeVariableName.get("T")), "configurator");

        String  packge = configs.logger_package;

        builder.addStatement("$T aTable=new $T()",mapType,mapType2);

        for (TemplateCompilerConfig config : configs.templates) {

            //        aTable.put(allBuilders.anticipating_impactBuilder  .getName(), configurator.anticipating_impact     (allBuilders.anticipating_impactBuilder));
            String thisBuilderName = ConfigProcessor.PREFIX_LOG_VAR + config.name;
            builder.addStatement("aTable.put($N.getName(),configurator.$N($N))", thisBuilderName,  config.name, thisBuilderName);

        }


        builder.addStatement("return aTable");

        return builder.build();

    }

    JavaFile generateBuilderInterface(TemplatesCompilerConfig configs) {

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(ConfigProcessor.BUILDER_INTERFACE);


        MethodSpec.Builder builder2 = MethodSpec.methodBuilder(ConfigProcessor.GET_NODES_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int[].class);
        builder.addMethod(builder2.build());


        MethodSpec.Builder builder3 = MethodSpec.methodBuilder(ConfigProcessor.GET_SUCCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(CompilerUtil.mapType);
        builder.addMethod(builder3.build());


        MethodSpec.Builder builder3b = MethodSpec.methodBuilder(ConfigProcessor.GET_TYPED_SUCCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(CompilerUtil.mapType);
        builder.addMethod(builder3b.build());

        MethodSpec.Builder builder4 = MethodSpec.methodBuilder(ConfigProcessor.GET_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder4.build());

        TypeName myType=ParameterizedTypeName.get(ClassName.get(ConfigProcessor.CLIENT_PACKAGE,ConfigProcessor.PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));

        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(ConfigProcessor.RECORD_CSV_PROCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(ArrayTypeName.of(Object.class),"record").build())
                .returns(myType);
        builder.addMethod(builder5.build());


        TypeSpec theInterface = builder.build();

        JavaFile myfile = JavaFile.builder(ConfigProcessor.CLIENT_PACKAGE, theInterface)
                .addFileComment("Generated Automatically by ProvToolbox ($N.generateBuilderInterface()) for templates config $S", getClass().getName(),  configs.name)
                .build();
        return myfile;
    }

    JavaFile generateLoggerInterface(TemplatesCompilerConfig configs) {

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(ConfigProcessor.LOGGER_INTERFACE);

        ClassName cln = ClassName.get(ConfigProcessor.CLIENT_PACKAGE, "Builder");
        ArrayTypeName builderArrayType = ArrayTypeName.of(cln);

        MethodSpec.Builder builder2 = MethodSpec.methodBuilder(ConfigProcessor.GET_BUILDERS_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(builderArrayType);
        builder.addMethod(builder2.build());


        TypeSpec theInterface = builder.build();

        JavaFile myfile = JavaFile.builder(ConfigProcessor.CLIENT_PACKAGE, theInterface)
                .addFileComment("Generated Automatically by ProvToolbox ($N.generateLoggerInterface()) for templates config $S", getClass().getName(), configs.name)
                .build();
        return myfile;
    }

    public MethodSpec generateStaticLogMethod(TemplateCompilerConfig config) {
        final String loggerName = compilerUtil.loggerName(config.name);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(loggerName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.STATIC)
                .returns(String.class);

        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");
        JsonNode the_documentation = bindings_schema.get("@documentation");
        JsonNode the_return = bindings_schema.get("@return");
        compilerUtil.generateSpecializedParameters(builder, the_var);
        compilerUtil.generateSpecializedParametersJavadoc(builder, the_var, the_documentation, the_return);

        int count = 1;
        Iterator<String> iter = the_var.fieldNames();
        String args = "";
        while (iter.hasNext()) {
            String key = iter.next();
            if (count > 1) args = args + ", ";
            args = args + key;
            count++;
        }
        builder.addStatement("return $N.$N()." + "process" + "(" + args + ")", ConfigProcessor.PREFIX_LOG_VAR + config.name,ConfigProcessor.ARGS_CSV_CONVERSION_METHOD);
        return builder.build();
    }

    public MethodSpec generateStaticBeanMethod(TemplateCompilerConfig config) {
        final String beanCreatorName = "bean"+compilerUtil.capitalize(config.name);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(beanCreatorName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.STATIC)
                .returns(ClassName.get(config.package_ + ".client",compilerUtil.beanNameClass(config.name)));

        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");
        JsonNode the_documentation = bindings_schema.get("@documentation");
        JsonNode the_return = bindings_schema.get("@return");
        compilerUtil.generateSpecializedParameters(builder, the_var);
        compilerUtil.generateSpecializedParametersJavadoc(builder, the_var, the_documentation, the_return);


        int count = 1;
        Iterator<String> iter = the_var.fieldNames();
        String args = "";
        while (iter.hasNext()) {
            String key = iter.next();
            if (count > 1) args = args + ", ";
            args = args + key;
            count++;
        }
        //builder.addStatement("return " + compilerUtil.templateNameClass(config.name) + ".toBean(" + args + ")");
        builder.addStatement("return $N.$N." + "process" +  "(" + args + ")", ConfigProcessor.PREFIX_LOG_VAR + config.name, ConfigProcessor.A_ARGS_BEAN_CONVERTER);

        return builder.build();

    }



    public MethodSpec generateGetBuilderMethod(ArrayTypeName builderArrayType) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getBuilders")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderArrayType);


        builder.addStatement("return __builders");


        return builder.build();

    }

    public JavaFile generateProcessorArgsInterface(TemplatesCompilerConfig configs) {

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInitParameter("ProcessorArgsInterface", "T");

        Object [] args=new Object[0];

        MethodSpec.Builder builder2 = MethodSpec.methodBuilder("process")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(ArrayTypeName.get(args.getClass()),"args").build())
                .returns(TypeVariableName.get("T"));
        builder.addMethod(builder2.build());

        TypeSpec theInterface = builder.build();

        JavaFile myfile = JavaFile.builder(ConfigProcessor.CLIENT_PACKAGE, theInterface)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for templates config $S by method generateProcessorArgsInterface()", getClass().getName(), configs.name)
                .build();
        return myfile;
    }
}