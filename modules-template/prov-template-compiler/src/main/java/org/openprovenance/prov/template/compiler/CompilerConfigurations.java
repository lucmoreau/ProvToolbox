package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import java.util.function.BiConsumer;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.CLIENT_PACKAGE;

public class CompilerConfigurations {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerConfigurations() {
    }

    static final ParameterizedTypeName processorOfString = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"ProcessorArgsInterface"), TypeName.get(String.class));
    static final ParameterizedTypeName processorOfUnknown = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"ProcessorArgsInterface"), TypeVariableName.get("?"));
    static final TypeName stringArray = ArrayTypeName.get(String[].class);


    public JavaFile generateConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName, TypeName typeName, BiConsumer<String, MethodSpec.Builder> generator, String generatorMethod) {
        if (configs.configurator_package==null) throw new NullPointerException("configurator_package is null");

        final ParameterizedTypeName tableConfiguratorType = ParameterizedTypeName.get(ClassName.get(configs.logger_package,configs.tableConfigurator), typeName);


        TypeSpec.Builder builder = compilerUtil.generateClassInit(theConfiguratorName);


        builder.addSuperinterface(tableConfiguratorType);

        String packge = null;
        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, templateNameClass);
            String builderParameter = "builder";
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(config.name)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(className, builderParameter).build())
                    .returns(typeName);

            generator.accept(builderParameter, mspec);

            builder.addMethod(mspec.build());
        }


        TypeSpec theConfigurator = builder.build();

        JavaFile myfile = JavaFile.builder(configs.configurator_package, theConfigurator)
                .addFileComment("Generated Automatically by ProvToolbox method $N.$N() for templates config $N", getClass().getName(), generatorMethod,configs.name)
                .build();
        return myfile;
    }

    public JavaFile generateSqlConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, processorOfString, this::generateMethodRecord2SqlConverter, "generateSqlConfigurator");
    }
    public JavaFile generatePropertyOrderConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, stringArray, this::generatePropertyOrder, "generatePropertyOrderConfigurator");
    }
    public JavaFile generateCsvConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, processorOfString, this::generateMethodRecord2CsvConverter, "generateCsvConfigurator");
    }
    public JavaFile generateSqlInsertConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, ClassName.get(String.class), this::generateSqlInsert, "generateSqlInsertConfigurator");
    }
    public JavaFile generateConverterConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, processorOfUnknown, this::generateMethodRecordConverter, "generateConverterConfigurator");
    }

    public void generateMethodRecord2SqlConverter(String builderParameter, MethodSpec.Builder mspec) {
        mspec.addStatement("return $N.aRecord2SqlConverter", builderParameter);
    }
    public void generateMethodRecord2CsvConverter(String builderParameter, MethodSpec.Builder mspec) {
        mspec.addStatement("return $N.processorConverter($N.aArgs2CsVConverter)", builderParameter,builderParameter);
    }
    public void generatePropertyOrder(String builderParameter, MethodSpec.Builder mspec) {
        mspec.addStatement("return $N.getPropertyOrder()", builderParameter);
    }
    public void generateSqlInsert(String builderParameter, MethodSpec.Builder mspec) {
        mspec.addStatement("return $N.getSQLInsert()", builderParameter);
    }
    public void generateMethodRecordConverter(String builderParameter, MethodSpec.Builder mspec) {
        mspec.addStatement("return $N.aRecord2BeanConverter", builderParameter);
    }

}