package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.Runner;

import javax.lang.model.element.Modifier;
import java.util.Iterator;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

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


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.logger_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }

    JavaFile generateBuilderInterface(TemplatesCompilerConfig configs) {

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(ConfigProcessor.BUILDER_INTERFACE);


        MethodSpec.Builder builder2 = MethodSpec.methodBuilder(ConfigProcessor.GET_NODES_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int[].class);
        builder.addMethod(builder2.build());


        MethodSpec.Builder builder3 = MethodSpec.methodBuilder(ConfigProcessor.GET_SUCCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(CompilerUtil.hashmapType);
        builder.addMethod(builder3.build());


        MethodSpec.Builder builder4 = MethodSpec.methodBuilder(ConfigProcessor.GET_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder4.build());


        TypeSpec theInterface = builder.build();

        JavaFile myfile = JavaFile.builder(ConfigProcessor.CLIENT_PACKAGE, theInterface)
                .addFileComment("Generated Automatically by ProvToolbox for templates config $S", configs.name)
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
                .addFileComment("Generated Automatically by ProvToolbox ($N) for templates config $S", getClass().getName(), configs.name)
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
        compilerUtil.generateSpecializedParameters(builder, the_var);
        compilerUtil.generateSpecializedParametersJavadoc(builder, the_var, the_documentation);


        int count = 1;
        Iterator<String> iter = the_var.fieldNames();
        String args = "";
        while (iter.hasNext()) {
            String key = iter.next();
            if (count > 1) args = args + ", ";
            args = args + key;
            count++;
        }
        builder.addStatement("return $N." + loggerName + "(" + args + ")", ConfigProcessor.PREFIX_LOG_VAR + config.name);


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
        compilerUtil.generateSpecializedParameters(builder, the_var);
        compilerUtil.generateSpecializedParametersJavadoc(builder, the_var, the_documentation);


        int count = 1;
        Iterator<String> iter = the_var.fieldNames();
        String args = "";
        while (iter.hasNext()) {
            String key = iter.next();
            if (count > 1) args = args + ", ";
            args = args + key;
            count++;
        }
        builder.addStatement("return " + compilerUtil.templateNameClass(config.name) + ".toBean(" + args + ")");


        return builder.build();

    }



    public MethodSpec generateGetBuilderMethod(ArrayTypeName builderArrayType) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getBuilders")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderArrayType);


        builder.addStatement("return __builders");


        return builder.build();

    }

}