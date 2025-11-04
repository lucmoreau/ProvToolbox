package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
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


    static public final ParameterizedTypeName functionObjArrayTo (TypeName returnType) {
        return ParameterizedTypeName.get(ClassName.get(Function.class), ArrayTypeName.of(Object.class), returnType);
    }

    static final TypeVariableName PARAMETRIC_T=TypeVariableName.get("T");
    public static final ParameterizedTypeName processorOfStringOLD = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"ProcessorArgsInterface"), TypeName.get(String.class));
    public static final ParameterizedTypeName processorOfString = functionObjArrayTo(TypeName.get(String.class));
    static final ParameterizedTypeName processorOfUnknown = functionObjArrayTo(TypeVariableName.get("?"));
    public static final TypeName stringArray = ArrayTypeName.get(String[].class);
    static final TypeName intArray = ArrayTypeName.get(int[].class);
    static final ParameterizedTypeName mapString2StringArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), stringArray);
    static final ParameterizedTypeName mapString2IntegerList = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(Integer.class)));
    static final ParameterizedTypeName mapString2StringList = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(String.class)));
    static final ParameterizedTypeName mapString2IntArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), intArray);
    static final ParameterizedTypeName mapString2MapString2StringArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), mapString2StringArray);
    static final ParameterizedTypeName mapString2MapString2IntArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), mapString2IntArray);

    static final ParameterizedTypeName BiFunctionOfString2StringArray = ParameterizedTypeName.get(ClassName.get(java.util.function.BiFunction.class), mapString2MapString2IntArray, stringArray, PARAMETRIC_T);
    static final ParameterizedTypeName FunctionOfString2StringArray = ParameterizedTypeName.get(ClassName.get(java.util.function.Function.class), ClassName.get(CLIENT_PACKAGE,BUILDER_INTERFACE), PARAMETRIC_T);

    static final ParameterizedTypeName FunctionOfObjectArray2ObjectArray = ParameterizedTypeName.get(ClassName.get(java.util.function.Function.class), ArrayTypeName.get(Object[].class), ArrayTypeName.get(Object[].class));

    static final ParameterizedTypeName MapString2FileBuilder= ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(FileBuilder.class));

    static final String CONVERTER_VAR="converter";


    static final String ENACTOR_VAR = "beanEnactor";


    public SpecificationFile generateConfigurator(TemplatesProjectConfiguration configs,
                                                  Locations locations,
                                                  String theConfiguratorName,
                                                  TypeName typeName,
                                                  SixtetConsumer<String, String, MethodSpec.Builder, TypeName, TypeName, TypeName> generator,
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
                                                  SixtetConsumer<String, String, MethodSpec.Builder, TypeName, TypeName, TypeName> generator,
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

        final ParameterizedTypeName tableConfiguratorType = ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.name, TABLE_CONFIGURATOR),TABLE_CONFIGURATOR), typeName);


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
            final ClassName className = ClassName.get(locations.getBeansPackage(config.name, BeanDirection.COMMON), templateNameClass);
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
                                 config.name,
                                 mspec,
                                className,
                                ClassName.get((direction==BeanDirection.COMMON)? locations.getBeansPackage(config.name, BeanDirection.COMMON) : beanPackage, inBeanNameClass),
                                ClassName.get((direction==BeanDirection.COMMON)? locations.getBeansPackage(config.name, BeanDirection.COMMON) : beanPackage, outBeanNameClass)
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

        String thePackage=locations.getFilePackage(configs.name, theConfiguratorName);

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
        return  generateConfigurator(configs, locations, theConfiguratorName, PARAMETRIC_T, this::generateRelation, "generateRelationConfigurator", BeanDirection.COMMON, BiFunctionOfString2StringArray, CONVERTER_VAR, PARAMETRIC_T, false, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateBuilderProcessorConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, PARAMETRIC_T, this::generateBuilderProcessor, "generateBuilderProcessorConfigurator", BeanDirection.COMMON, FunctionOfString2StringArray, PROCESSOR, PARAMETRIC_T, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateObjectRecordMakerConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, FunctionOfObjectArray2ObjectArray, new WrapperClass(locations)::generateObjectRecordMaker, "generateObjectRecordMakerConfigurator", BeanDirection.COMMON, MapString2FileBuilder, DISPATCHER_VAR, null, false, null, BeanDirection.COMMON, directory, fileName);
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
        TypeName record2recordType=ClassName.get(locations.getFilePackage(configs.name, RECORD_2_RECORD_CONFIGURATOR), RECORD_2_RECORD_CONFIGURATOR+"."+RECORD_2_RECORD);
        return  generateConfigurator(configs, locations, theConfiguratorName, record2recordType, this::generateMethodRecord2RecordConverter, "generateConverterConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName, optionalCode);
    }
    public SpecificationFile generateEnactorConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfUnknown, this::generateMethodEnactor, "generateEnactorConfigurator", BeanDirection.COMMON, compilerUtil.getClass(configs.name, BEAN_PROCESSOR,locations), ENACTOR_VAR, null, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateEnactorConfigurator2(TemplatesProjectConfiguration configs, String theConfiguratorName, String integrator_package, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, processorOfUnknown, new WrapperClass2(locations,this)::generateMethodEnactor2, "generateEnactorConfigurator2", BeanDirection.INPUTS, ClassName.get(locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR),INPUT_OUTPUT_PROCESSOR), ENACTOR_VAR, null, false, integrator_package,BeanDirection.OUTPUTS, directory, fileName);
    }

    public void generateMethodRecord2SqlConverter(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.aRecord2SqlConverter", builderParameter);
    }
    public void generateMethodRecord2CsvConverter(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.processorConverter($N.aArgs2CsVConverter)", builderParameter, builderParameter);
    }
    public void generatePropertyOrder(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.getPropertyOrder()", builderParameter);
    }
    public void generateRelation0(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $T.__relations", className);
    }
    public void generateRelation(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.apply($T.__relations, $N.getPropertyOrder())", CONVERTER_VAR, className, builderParameter);
    }
    public void generateBuilderProcessor(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.apply($N)", PROCESSOR, builderParameter);
    }

    static class WrapperClass {
        private final Locations locations;

        public void generateObjectRecordMaker(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName outType) {

            final String backendPackage = locations.getBackendPackage(name);

            System.out.println("backendPackage is "+backendPackage);

            String backendBuilder = className.toString();
            // return suffix after last dot
            final int pos = backendBuilder.lastIndexOf('.');
            backendBuilder = backendBuilder.substring(pos + 1);

            mspec.addStatement("$T $N=($T) $N.get(builder.getName())", ClassName.get(backendPackage,backendBuilder), TEMPLATE_BUILDER_VARIABLE, ClassName.get(backendPackage,backendBuilder), DISPATCHER_VAR);
            mspec.addStatement("return record -> $N.make(record, $N.getTypedRecord())", TEMPLATE_BUILDER_VARIABLE, TEMPLATE_BUILDER_VARIABLE);
        }

        WrapperClass(Locations locations) {
            this.locations = locations;
        }
    }


    static class WrapperClass2 {
        private final Locations locations;
        private final CompilerConfigurations compilerConfigurations;

        public void generateMethodEnactor2(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName _inType, TypeName _outType) {
            String inPackage=locations.getBeansPackage(name, BeanDirection.INPUTS);
            TypeName inType=ClassName.get(inPackage,_inType.toString().substring(_inType.toString().lastIndexOf('.')+1));
            String outPackage=locations.getBeansPackage(name, BeanDirection.OUTPUTS);
            TypeName outType=ClassName.get(inPackage,_outType.toString().substring(_outType.toString().lastIndexOf('.')+1));

            compilerConfigurations.generateMethodEnactor2(builderParameter, name, mspec, className, inType, outType);
        }

        WrapperClass2(Locations locations, CompilerConfigurations compilerConfigurations) {
            this.locations = locations;
            this.compilerConfigurations = compilerConfigurations;
        }
    }


    public void generateInputPropertyOrder(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.getInputs()", builderParameter);
    }
    public void generateOutputPropertyOrder(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.getOutputs()", builderParameter);
    }
    public void generateSqlInsert(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.getSQLInsert()", builderParameter);
    }
    public void generateMethodRecordConverter(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N.aRecord2BeanConverter", builderParameter);
    }
    public void generateMethodRecord2RecordConverter(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return x -> builder.aRecord2BeanConverter.apply(x).process(builder.aArgs2RecordConverter())");
    }
    public void generateMethodEnactor(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("$T beanConverter=$N.aRecord2BeanConverter", functionObjArrayTo(beanType), builderParameter);


        mspec.addStatement("$T enactor=(array) -> {\n" +
                "                    $T bean=beanConverter.apply(array);\n" +
                "                    return $N.process(bean);\n" +
                "                }", functionObjArrayTo(beanType),beanType, ENACTOR_VAR);
        mspec.addStatement("return enactor");
    }
    public void generateMethodEnactor2(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName inputBeanType, TypeName outputBeanType) {

        mspec.addComment("Generated Automatically by ProvToolbox method $N.$N()", getClass().getName(), "generateMethodEnactor2");

        mspec.addStatement("$T beanConverter=$N.getIntegrator().aRecord2InputsConverter", functionObjArrayTo(inputBeanType), builderParameter);
        mspec.addStatement("$T enactor=(array) -> {\n" +
                "                    $T bean=beanConverter.apply(array);\n" +
                "                    return $N.process(bean);\n" +
                "                }", functionObjArrayTo(outputBeanType),inputBeanType, ENACTOR_VAR);
        mspec.addStatement("return enactor");
    }

    public void generateReturnSelf(String builderParameter, String name, MethodSpec.Builder mspec, TypeName className, TypeName beanType, TypeName _out) {
        mspec.addStatement("return $N", builderParameter);
    }
}