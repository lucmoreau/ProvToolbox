package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerConfigurations {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerConfigurations() {
    }

    static final ParameterizedTypeName processorOfString = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"ProcessorArgsInterface"), TypeName.get(String.class));
    static final ParameterizedTypeName processorOfUnknown = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"ProcessorArgsInterface"), TypeVariableName.get("?"));
    static final TypeName stringArray = ArrayTypeName.get(String[].class);

    String enactorVar = "beanEnactor";


    public JavaFile generateConfigurator(TemplatesCompilerConfig configs,
                                         Locations locations,
                                         String theConfiguratorName,
                                         TypeName typeName,
                                         QuintetConsumer<String, MethodSpec.Builder, TypeName, TypeName, TypeName> generator,
                                         String generatorMethod,
                                         BeanDirection direction,
                                         TypeName beanProcessor,
                                         boolean defaultBehaviour,
                                         String beanPackage,
                                         BeanDirection outDirection) {
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

        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            final String inBeanNameClass = compilerUtil.beanNameClass(config.name, direction);
            final String outBeanNameClass = compilerUtil.beanNameClass(config.name, outDirection);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.config_common_package, templateNameClass);
            String builderParameter = "builder";
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(config.name)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(ParameterSpec.builder(className, builderParameter).build())
                    .returns(typeName);
            if (config instanceof SimpleTemplateCompilerConfig || defaultBehaviour) {

                generator.accept(builderParameter,
                                 mspec,
                                className,
                                ClassName.get((direction==BeanDirection.COMMON)? locations.config_common_package : beanPackage, inBeanNameClass),
                                ClassName.get((direction==BeanDirection.COMMON)? locations.config_common_package : beanPackage, outBeanNameClass)
                        );
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

    public JavaFile generateSqlConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName, Locations locations) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfString, this::generateMethodRecord2SqlConverter, "generateSqlConfigurator", BeanDirection.COMMON, null, false, null, BeanDirection.COMMON);
    }
    public JavaFile generatePropertyOrderConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName, Locations locations) {
        return  generateConfigurator(configs, locations, theConfiguratorName, stringArray, this::generatePropertyOrder, "generatePropertyOrderConfigurator", BeanDirection.COMMON, null, true, null, BeanDirection.COMMON);
    }
    public JavaFile generateCsvConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName, Locations locations) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfString, this::generateMethodRecord2CsvConverter, "generateCsvConfigurator", BeanDirection.COMMON, null, false, null, BeanDirection.COMMON);
    }
    public JavaFile generateSqlInsertConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName, Locations locations) {
        return  generateConfigurator(configs, locations, theConfiguratorName, ClassName.get(String.class), this::generateSqlInsert, "generateSqlInsertConfigurator", BeanDirection.COMMON, null, false, null, BeanDirection.COMMON);
    }
    public JavaFile generateConverterConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName, Locations locations) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfUnknown, this::generateMethodRecordConverter, "generateConverterConfigurator", BeanDirection.COMMON, null, false, null, BeanDirection.COMMON);
    }
    public JavaFile generateEnactorConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName, Locations locations) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfUnknown, this::generateMethodEnactor, "generateEnactorConfigurator", BeanDirection.COMMON, ClassName.get(configs.logger_package,configs.beanProcessor), false, null, BeanDirection.COMMON);
    }

    public JavaFile generateEnactorConfigurator2(TemplatesCompilerConfig configs, String theConfiguratorName, String integrator_package, Locations locations) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfUnknown, this::generateMethodEnactor2, "generateEnactorConfigurator2", BeanDirection.INPUTS, ClassName.get(integrator_package,INPUT_OUTPUT_PROCESSOR), false, integrator_package,BeanDirection.OUTPUTS);
    }

    public void generateMethodRecord2SqlConverter(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.aRecord2SqlConverter", builderParameter);
    }
    public void generateMethodRecord2CsvConverter(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.processorConverter($N.aArgs2CsVConverter)", builderParameter, builderParameter);
    }
    public void generatePropertyOrder(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.getPropertyOrder()", builderParameter);
    }
    public void generateSqlInsert(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.getSQLInsert()", builderParameter);
    }
    public void generateMethodRecordConverter(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.aRecord2BeanConverter", builderParameter);
    }
    public void generateMethodEnactor(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("$N<$T> beanConverter=$N.aRecord2BeanConverter", PROCESSOR_ARGS_INTERFACE, beanType, builderParameter);


        mspec.addStatement("$N<$T> enactor=(array) -> {\n" +
                "                    $T bean=beanConverter.process(array);\n" +
                "                    return $N.process(bean);\n" +
                "                }", PROCESSOR_ARGS_INTERFACE, beanType,beanType,enactorVar);
        mspec.addStatement("return enactor");
    }
    public void generateMethodEnactor2(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName inputBeanType, TypeName outputBeanType) {

        mspec.addComment("Generated Automatically by ProvToolbox method $N.$N()", getClass().getName(), "generateMethodEnactor2");


        mspec.addStatement("$N<$T> beanConverter=$N.getIntegrator().aRecord2InputsConverter", PROCESSOR_ARGS_INTERFACE, inputBeanType, builderParameter);
        mspec.addStatement("$N<$T> enactor=(array) -> {\n" +
                "                    $T bean=beanConverter.process(array);\n" +
                "                    return $N.process(bean);\n" +
                "                }", PROCESSOR_ARGS_INTERFACE, outputBeanType,inputBeanType,enactorVar);
        mspec.addStatement("return enactor");
    }
    public JavaFile generateBuilderConfigurator(TemplatesCompilerConfig configs, String theConfiguratorName, Locations locations) {
        return  generateConfigurator(configs, locations, theConfiguratorName, ClassName.get(CLIENT_PACKAGE,"Builder"), this::generateReturnSelf, "generateBuilderConfigurator", BeanDirection.COMMON, null, false, null, BeanDirection.COMMON);
    }

    public void generateReturnSelf(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N", builderParameter);
    }
}