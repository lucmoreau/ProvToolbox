package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.common.Constants.CLIENT_PACKAGE;
import static org.openprovenance.prov.template.compiler.common.Constants.PROCESSOR_ARGS_INTERFACE;

public class CompilerConfigurations {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerConfigurations() {
    }

    static final ParameterizedTypeName processorOfString = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"ProcessorArgsInterface"), TypeName.get(String.class));
    static final ParameterizedTypeName processorOfUnknown = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"ProcessorArgsInterface"), TypeVariableName.get("?"));
    static final TypeName stringArray = ArrayTypeName.get(String[].class);

    String enactorVar = "beanEnactor";


    public JavaFile generateConfigurator(TemplatesCompilerConfig configs,
                                         String theConfiguratorName,
                                         TypeName typeName,
                                         QuadConsumer<String, MethodSpec.Builder, TypeName, TypeName> generator,
                                         String generatorMethod,
                                         TypeName beanProcessor,
                                         boolean defaultBehaviour) {
        if (configs.configurator_package==null) throw new NullPointerException("configurator_package is null");

        final ParameterizedTypeName tableConfiguratorType = ParameterizedTypeName.get(ClassName.get(configs.logger_package,configs.tableConfigurator), typeName);


        TypeSpec.Builder builder = compilerUtil.generateClassInit(theConfiguratorName);

        // the following in only used for the enactorConfigurator
        if (beanProcessor!=null) {
            builder.addField(beanProcessor, enactorVar, Modifier.FINAL, Modifier.PRIVATE);
            MethodSpec.Builder cspec= MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(beanProcessor, enactorVar).build())
                    .addStatement("this.$N=$N", enactorVar, enactorVar);
            builder.addMethod(cspec.build());
        }


        builder.addSuperinterface(tableConfiguratorType);

        String packge = null;
        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, templateNameClass);
            String builderParameter = "builder";
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(config.name)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(ParameterSpec.builder(className, builderParameter).build())
                    .returns(typeName);
            if (config instanceof SimpleTemplateCompilerConfig || defaultBehaviour) {

                generator.accept(builderParameter, mspec, className, ClassName.get(packge, beanNameClass));
            } else {
                mspec.addStatement("return null");
               // generateUnsupportedException(mspec);
            }
            builder.addMethod(mspec.build());

        }


        TypeSpec theConfigurator = builder.build();

        JavaFile myfile = JavaFile.builder(configs.configurator_package, theConfigurator)
                .addFileComment("Generated Automatically by ProvToolbox method $N.$N() for templates config $N", getClass().getName(), generatorMethod,configs.name)
                .build();
        return myfile;
    }

    public JavaFile generateSqlConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, processorOfString, this::generateMethodRecord2SqlConverter, "generateSqlConfigurator", null, false);
    }
    public JavaFile generatePropertyOrderConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, stringArray, this::generatePropertyOrder, "generatePropertyOrderConfigurator",null, true);
    }
    public JavaFile generateCsvConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, processorOfString, this::generateMethodRecord2CsvConverter, "generateCsvConfigurator", null, false);
    }
    public JavaFile generateSqlInsertConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, ClassName.get(String.class), this::generateSqlInsert, "generateSqlInsertConfigurator", null, false);
    }
    public JavaFile generateConverterConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, processorOfUnknown, this::generateMethodRecordConverter, "generateConverterConfigurator", null, false);
    }
    public JavaFile generateEnactorConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, processorOfUnknown, this::generateMethodEnactor, "generateEnactorConfigurator", ClassName.get(configs.logger_package,configs.beanProcessor), false);
    }

    public JavaFile generateEnactorConfigurator2(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, processorOfUnknown, this::generateMethodEnactor2, "generateEnactorConfigurator2", ClassName.get(configs.logger_package,configs.beanProcessor), false);
    }

    public void generateMethodRecord2SqlConverter(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType) {
        mspec.addStatement("return $N.aRecord2SqlConverter", builderParameter);
    }
    public void generateMethodRecord2CsvConverter(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType) {
        mspec.addStatement("return $N.processorConverter($N.aArgs2CsVConverter)", builderParameter, builderParameter);
    }
    public void generatePropertyOrder(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType) {
        mspec.addStatement("return $N.getPropertyOrder()", builderParameter);
    }
    public void generateSqlInsert(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType) {
        mspec.addStatement("return $N.getSQLInsert()", builderParameter);
    }
    public void generateMethodRecordConverter(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType) {
        mspec.addStatement("return $N.aRecord2BeanConverter", builderParameter);
    }
    public void generateMethodEnactor(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType) {
        mspec.addStatement("$N<$T> beanConverter=$N.aRecord2BeanConverter", PROCESSOR_ARGS_INTERFACE, beanType, builderParameter);


        mspec.addStatement("$N<$T> enactor=(array) -> {\n" +
                "                    $T bean=beanConverter.process(array);\n" +
                "                    return $N.process(bean);\n" +
                "                }", PROCESSOR_ARGS_INTERFACE, beanType,beanType,enactorVar);
        mspec.addStatement("return enactor");
    }
    public void generateMethodEnactor2(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType) {
        //mspec.addComment("Novel stuff");
        //mspec.addStatement("$T novel=builder.getIntegrator().newOutput()", Object.class);
        //mspec.addComment("Original stuff");
        mspec.addStatement("$N<$T> beanConverter=$N.aRecord2BeanConverter", PROCESSOR_ARGS_INTERFACE, beanType, builderParameter);
        mspec.addStatement("$N<$T> enactor=(array) -> {\n" +
                "                    $T bean=beanConverter.process(array);\n" +
                "                    return $N.process(bean);\n" +
                "                }", PROCESSOR_ARGS_INTERFACE, beanType,beanType,enactorVar);
        mspec.addStatement("return enactor");
    }
    public JavaFile generateBuilderConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName) {
        return  generateConfigurator(configs, theConfiguratorName, ClassName.get(CLIENT_PACKAGE,"Builder"), this::generateReturnSelf, "generateBuilderConfigurator", null, false);
    }

    public void generateReturnSelf(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType) {
        mspec.addStatement("return $N", builderParameter);
    }
}