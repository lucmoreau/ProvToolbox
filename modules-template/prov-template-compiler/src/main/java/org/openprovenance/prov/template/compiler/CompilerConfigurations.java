package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.openprovenance.prov.template.compiler.CompilerBeanGenerator.newSpecificationFiles;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerConfigurations {
    public static final String RECORD_2_RECORD = "Record2Record";
    public static final String PROCESS = "process";
    private final CompilerUtil compilerUtil;

    public CompilerConfigurations(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    static final TypeVariableName PARAMETRIC_T=TypeVariableName.get("T");
    public static final ParameterizedTypeName processorOfString = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"ProcessorArgsInterface"), TypeName.get(String.class));
    static final ParameterizedTypeName processorOfUnknown = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"ProcessorArgsInterface"), TypeVariableName.get("?"));
    static final TypeName stringArray = ArrayTypeName.get(String[].class);
    static final TypeName intArray = ArrayTypeName.get(int[].class);
    static final ParameterizedTypeName mapString2StringArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), stringArray);
    static final ParameterizedTypeName mapString2IntArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), intArray);
    static final ParameterizedTypeName mapString2MapString2StringArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), mapString2StringArray);
    static final ParameterizedTypeName mapString2MapString2IntArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), mapString2IntArray);

    static final ParameterizedTypeName FunctionOfString2StringArray = ParameterizedTypeName.get(ClassName.get(java.util.function.BiFunction.class), mapString2MapString2IntArray, stringArray, PARAMETRIC_T);

    static final String CONVERTER_VAR="converter";


    static final String ENACTOR_VAR = "beanEnactor";


    public SpecificationFile generateConfigurator(TemplatesProjectConfiguration configs,
                                                  Locations locations,
                                                  String theConfiguratorName,
                                                  TypeName typeName,
                                                  QuintetConsumer<String, MethodSpec.Builder, TypeName, TypeName, TypeName> generator,
                                                  String generatorMethod,
                                                  BeanDirection direction,
                                                  TypeName constructParameterType,
                                                  String constructorParameter,
                                                  TypeVariableName parametericType,
                                                  boolean defaultBehaviour,
                                                  String beanPackage,
                                                  BeanDirection outDirection,
                                                  String directory,
                                                  String fileName) {
        return generateConfigurator(configs, locations, theConfiguratorName, typeName, generator, generatorMethod, direction, constructParameterType, constructorParameter, parametericType, defaultBehaviour, beanPackage, outDirection, directory, fileName, null);
    }

    public SpecificationFile generateConfigurator(TemplatesProjectConfiguration configs,
                                                  Locations locations,
                                                  String theConfiguratorName,
                                                  TypeName typeName,
                                                  QuintetConsumer<String, MethodSpec.Builder, TypeName, TypeName, TypeName> generator,
                                                  String generatorMethod,
                                                  BeanDirection direction,
                                                  TypeName constructParameterType,
                                                  String constructorParameter,
                                                  TypeVariableName parametericType,
                                                  boolean defaultBehaviour,
                                                  String beanPackage,
                                                  BeanDirection outDirection,
                                                  String directory,
                                                  String fileName,
                                                  Consumer<TypeSpec.Builder> optionalCode) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        final ParameterizedTypeName tableConfiguratorType = ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(TABLE_CONFIGURATOR),TABLE_CONFIGURATOR), typeName);


        TypeSpec.Builder builder = compilerUtil.generateClassInit(theConfiguratorName);
        builder.addJavadoc("The table configurator $N\n", theConfiguratorName);


        if (parametericType!=null)  builder.addTypeVariable(parametericType);

        // the following in only used for the enactorConfigurator
        if (constructParameterType!=null && constructorParameter!=null) {
            builder.addField(constructParameterType, constructorParameter, Modifier.FINAL, Modifier.PRIVATE);
            MethodSpec.Builder cspec= MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(constructParameterType, constructorParameter).build());
            compilerUtil.specWithComment(cspec);

            cspec.addStatement("this.$N=$N", constructorParameter, constructorParameter);
            builder.addMethod(cspec.build());
        }


        builder.addSuperinterface(tableConfiguratorType);

        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            final String inBeanNameClass = compilerUtil.beanNameClass(config.name, direction);
            final String outBeanNameClass = compilerUtil.beanNameClass(config.name, outDirection);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), templateNameClass);
            String builderParameter = "builder";

            CodeBlock.Builder jdoc = CodeBlock.builder();
            jdoc.add("Gets configuration\n");
            jdoc.add("@param $N builder for template $N\n", builderParameter, config.name);
            jdoc.add("@return $T\n", String[].class);

            MethodSpec.Builder mspec = MethodSpec.methodBuilder(config.name)
                    .addJavadoc(jdoc.build())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(ParameterSpec.builder(className, builderParameter).build())
                    .returns(typeName);
            compilerUtil.specWithComment(mspec);

            if (config instanceof SimpleTemplateCompilerConfig || defaultBehaviour) {

                generator.accept(builderParameter,
                                 mspec,
                                className,
                                ClassName.get((direction==BeanDirection.COMMON)? locations.getFilePackage(BeanDirection.COMMON) : beanPackage, inBeanNameClass),
                                ClassName.get((direction==BeanDirection.COMMON)? locations.getFilePackage(BeanDirection.COMMON) : beanPackage, outBeanNameClass)
                        );
            } else {
                mspec.addStatement("return$Nnull", "/*null*/");
               // generateUnsupportedException(mspec);
            }
            builder.addMethod(mspec.build());

        }

        if (optionalCode!=null) {
            optionalCode.accept(builder);
        }


        TypeSpec theConfigurator = builder.build();

        String thePackage=locations.getFilePackage(theConfiguratorName);

        JavaFile myfile = compilerUtil.specWithComment(theConfigurator, configs, thePackage, stackTraceElement);

       // return new SpecificationFile(myfile, directory, fileName, configs.configurator_package);

        List<String> toGeneratePython= Stream.of(BUILDER_CONFIGURATOR, CONVERTER_CONFIGURATOR, CSV_CONFIGURATOR).map(x->x+".java").collect(Collectors.toList());

        if (locations.python_dir==null || !(toGeneratePython.contains(fileName))) {//(fileName.equals("BuilderConfigurator.java") || fileName.equals("ConverterConfigurator.java")|| fileName.equals("CsvConfigurator.java"))) {
            return new SpecificationFile(myfile, directory, fileName, thePackage);
        } else {
            return newSpecificationFiles(compilerUtil, locations, theConfigurator, configs, stackTraceElement, myfile, directory, fileName, thePackage, null);
        }

    }

    public SpecificationFile generateRelation0Configurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, mapString2MapString2IntArray, this::generateRelation0, "generateRelation0Configurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateRelationConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, PARAMETRIC_T, this::generateRelation, "generateRelationConfigurator", BeanDirection.COMMON, FunctionOfString2StringArray, CONVERTER_VAR, PARAMETRIC_T, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateSqlConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfString, this::generateMethodRecord2SqlConverter, "generateSqlConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generatePropertyOrderConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, stringArray, this::generatePropertyOrder, "generatePropertyOrderConfigurator", BeanDirection.COMMON, null, null, null, true, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateInputsConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, stringArray, this::generateInputPropertyOrder, "generateInputsConfigurator", BeanDirection.COMMON, null, null, null, true, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateOutputsConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, stringArray, this::generateOutputPropertyOrder, "generateOutputsConfigurator", BeanDirection.COMMON, null, null, null, true, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateCsvConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfString, this::generateMethodRecord2CsvConverter, "generateCsvConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateBuilderConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, ClassName.get(CLIENT_PACKAGE,"Builder"), this::generateReturnSelf, "generateBuilderConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateSqlInsertConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, ClassName.get(String.class), this::generateSqlInsert, "generateSqlInsertConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateConverterConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfUnknown, this::generateMethodRecordConverter, "generateConverterConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateRecord2RecordConfiguration(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        Consumer<TypeSpec.Builder> optionalCode=
                builder -> // add static interface declaration
                        builder.addType(TypeSpec.interfaceBuilder(RECORD_2_RECORD)
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addMethod(MethodSpec.methodBuilder(PROCESS)
                                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                        .addParameter(TypeName.get(Object[].class), "args")
                                        .returns(TypeName.get(Object[].class))
                                        .build())
                                .build());
        TypeName record2recordType=ClassName.get(locations.getFilePackage(RECORD_2_RECORD_CONFIGURATOR), RECORD_2_RECORD_CONFIGURATOR+"."+RECORD_2_RECORD);
        return  generateConfigurator(configs, locations, theConfiguratorName, record2recordType, this::generateMethodRecord2RecordConverter, "generateConverterConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName, optionalCode);
    }
    public SpecificationFile generateEnactorConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfUnknown, this::generateMethodEnactor, "generateEnactorConfigurator", BeanDirection.COMMON, compilerUtil.getClass(BEAN_PROCESSOR,locations), ENACTOR_VAR, null, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateEnactorConfigurator2(TemplatesProjectConfiguration configs, String theConfiguratorName, String integrator_package, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfUnknown, this::generateMethodEnactor2, "generateEnactorConfigurator2", BeanDirection.INPUTS, ClassName.get(locations.getFilePackage(INPUT_OUTPUT_PROCESSOR),INPUT_OUTPUT_PROCESSOR), ENACTOR_VAR, null, false, integrator_package,BeanDirection.OUTPUTS, directory, fileName);
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
    public void generateRelation0(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $T.__relations", className);
    }
    public void generateRelation(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.apply($T.__relations, $N.getPropertyOrder())", CONVERTER_VAR, className, builderParameter);
    }
    public void generateInputPropertyOrder(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.getInputs()", builderParameter);
    }
    public void generateOutputPropertyOrder(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.getOutputs()", builderParameter);
    }
    public void generateSqlInsert(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.getSQLInsert()", builderParameter);
    }
    public void generateMethodRecordConverter(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.aRecord2BeanConverter", builderParameter);
    }
    public void generateMethodRecord2RecordConverter(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return x -> builder.aRecord2BeanConverter.process(x).process(builder.aArgs2RecordConverter())");
    }
    public void generateMethodEnactor(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("$N<$T> beanConverter=$N.aRecord2BeanConverter", PROCESSOR_ARGS_INTERFACE, beanType, builderParameter);


        mspec.addStatement("$N<$T> enactor=(array) -> {\n" +
                "                    $T bean=beanConverter.process(array);\n" +
                "                    return $N.process(bean);\n" +
                "                }", PROCESSOR_ARGS_INTERFACE, beanType,beanType, ENACTOR_VAR);
        mspec.addStatement("return enactor");
    }
    public void generateMethodEnactor2(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName inputBeanType, TypeName outputBeanType) {

        mspec.addComment("Generated Automatically by ProvToolbox method $N.$N()", getClass().getName(), "generateMethodEnactor2");


        mspec.addStatement("$N<$T> beanConverter=$N.getIntegrator().aRecord2InputsConverter", PROCESSOR_ARGS_INTERFACE, inputBeanType, builderParameter);
        mspec.addStatement("$N<$T> enactor=(array) -> {\n" +
                "                    $T bean=beanConverter.process(array);\n" +
                "                    return $N.process(bean);\n" +
                "                }", PROCESSOR_ARGS_INTERFACE, outputBeanType,inputBeanType, ENACTOR_VAR);
        mspec.addStatement("return enactor");
    }

    public void generateReturnSelf(String builderParameter, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N", builderParameter);
    }
}