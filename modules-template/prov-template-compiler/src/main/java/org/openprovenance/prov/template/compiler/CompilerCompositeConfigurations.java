package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerCompositeConfigurations {
    private final CompilerUtil compilerUtil;

    public CompilerCompositeConfigurations(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    String enactorVar = "beanEnactor";


    public SpecificationFile generateCompositeConfigurator(TemplatesCompilerConfig configs,
                                                           Locations locations,
                                                           TypeName typeName,
                                                           QuadConsumer<String, MethodSpec.Builder, TypeName, TypeName> generator,
                                                           String generatorMethod,
                                                           TypeName beanProcessor,
                                                           String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        final ParameterizedTypeName tableConfiguratorType = ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(COMPOSITE_TABLE_CONFIGURATOR), COMPOSITE_TABLE_CONFIGURATOR), typeName);

        TypeSpec.Builder builder = compilerUtil.generateClassInit(fileName);

        // the following in only used for the enactorConfigurator
        if (beanProcessor!=null) {
            builder.addField(beanProcessor, enactorVar, Modifier.FINAL, Modifier.PRIVATE);
            MethodSpec.Builder cspec= MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(beanProcessor, enactorVar).build());
            compilerUtil.specWithComment(cspec);
            cspec.addStatement("this.$N=$N", enactorVar, enactorVar);
            builder.addMethod(cspec.build());
        }


        builder.addSuperinterface(tableConfiguratorType);


        for (TemplateCompilerConfig config : configs.templates) {

            if (!(config instanceof SimpleTemplateCompilerConfig )) {
                final String templateNameClass = compilerUtil.templateNameClass(config.name);
                final String beanNameClass = compilerUtil.commonNameClass(config.name);
                locations.updateWithConfig(config);
                final ClassName className = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), templateNameClass);
                String builderParameter = "builder";
                MethodSpec.Builder mspec = MethodSpec.methodBuilder(config.name)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addParameter(ParameterSpec.builder(className, builderParameter).build())
                        .returns(typeName);
                compilerUtil.specWithComment(mspec);
                generator.accept(builderParameter, mspec, className, ClassName.get(locations.getFilePackage(BeanDirection.COMMON), beanNameClass));
                builder.addMethod(mspec.build());
            }

        }

        TypeSpec theConfigurator = builder.build();

        String myPackage=locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theConfigurator, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    static final ParameterizedTypeName recordsProcessorOfUnknown = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"RecordsProcessorInterface"), TypeVariableName.get("?"));

    public SpecificationFile generateCompositeEnactorConfigurator(TemplatesCompilerConfig configs, Locations locations, String fileName) {
        return  generateCompositeConfigurator(configs, locations, recordsProcessorOfUnknown, this::generateMethodEnactor, "generateCompositeConfigurator", compilerUtil.getClass(BEAN_PROCESSOR, locations), fileName);
    }


    public void generateMethodEnactor(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType) {
        mspec.addStatement("$N<$T> beanConverter=$N.aRecord2BeanConverter", RECORDS_PROCESSOR_INTERFACE, beanType, builderParameter);


        mspec.addStatement("$N<$T> enactor=(array) -> {\n" +
                "                    $T bean=beanConverter.process(array);\n" +
                "                    return $N.process(bean);\n" +
                "                }", RECORDS_PROCESSOR_INTERFACE, beanType,beanType,enactorVar);
        mspec.addStatement("return enactor");
    }

    public SpecificationFile generateCompositeConfigurator2(TemplatesCompilerConfig configs,
                                                            Locations locations,
                                                            TypeName typeName,
                                                            QuintetConsumer<String, MethodSpec.Builder, TypeName, TypeName, TypeName> generator,
                                                            String generatorMethod,
                                                            TypeName beanProcessor,
                                                            String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        final ParameterizedTypeName tableConfiguratorType = ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(COMPOSITE_TABLE_CONFIGURATOR), COMPOSITE_TABLE_CONFIGURATOR), typeName);


        TypeSpec.Builder builder = compilerUtil.generateClassInit(fileName);

        // the following in only used for the enactorConfigurator
        if (beanProcessor!=null) {
            builder.addField(beanProcessor, enactorVar, Modifier.FINAL, Modifier.PRIVATE);
            MethodSpec.Builder cspec= MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(beanProcessor, enactorVar).build());
            compilerUtil.specWithComment(cspec);
            cspec.addStatement("this.$N=$N", enactorVar, enactorVar);
            builder.addMethod(cspec.build());
        }


        builder.addSuperinterface(tableConfiguratorType);


        for (TemplateCompilerConfig config : configs.templates) {

            if (!(config instanceof SimpleTemplateCompilerConfig )) {
                final String templateNameClass = compilerUtil.templateNameClass(config.name);
                final String beanNameClass = compilerUtil.commonNameClass(config.name);
                final String inputNameClass = compilerUtil.inputsNameClass(config.name);
                final String outputNameClass = compilerUtil.outputsNameClass(config.name);
                locations.updateWithConfig(config);
                final ClassName commonClassName = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), templateNameClass);
                String builderParameter = "builder";
                MethodSpec.Builder mspec = MethodSpec.methodBuilder(config.name)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addParameter(ParameterSpec.builder(commonClassName, builderParameter).build())
                        .returns(typeName);
                compilerUtil.specWithComment(mspec);
                generator.accept(builderParameter, mspec, commonClassName, ClassName.get(locations.getFilePackage(BeanDirection.INPUTS), inputNameClass), ClassName.get(locations.getFilePackage(BeanDirection.OUTPUTS), outputNameClass));
                builder.addMethod(mspec.build());
            }

        }

        TypeSpec theConfigurator = builder.build();

        String myPackage=locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theConfigurator, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateCompositeEnactorConfigurator2(TemplatesCompilerConfig configs, Locations locations, String fileName) {
        return  generateCompositeConfigurator2(configs, locations, recordsProcessorOfUnknown, this::generateMethodEnactor2, "generateCompositeConfigurator", ClassName.get(locations.getFilePackage(BeanDirection.INPUTS),INPUT_OUTPUT_PROCESSOR), fileName);
    }

    public void generateMethodEnactor2(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName inBeanType, TypeName outBeanType) {
        mspec.addStatement("$N<$T> beanConverter=$N.getIntegrator().aRecord2InputsConverter", RECORDS_PROCESSOR_INTERFACE, inBeanType, builderParameter);


        mspec.addStatement("$N<$T> enactor=(array) -> {\n" +
                "                    $T bean=beanConverter.process(array);\n" +
                "                    return $N.process(bean);\n" +
                "                }", RECORDS_PROCESSOR_INTERFACE, outBeanType,inBeanType,enactorVar);
        mspec.addStatement("return enactor");
    }


}