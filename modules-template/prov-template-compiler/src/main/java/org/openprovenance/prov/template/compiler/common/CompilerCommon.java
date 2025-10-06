package org.openprovenance.prov.template.compiler.common;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.apache.commons.lang.StringEscapeUtils;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.template.compiler.CompilerSQL;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.descriptors.*;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openprovenance.prov.model.StatementOrBundle.ALL_RELATIONS;
import static org.openprovenance.prov.template.compiler.CompilerBeanGenerator.newSpecificationFiles;
import static org.openprovenance.prov.template.compiler.CompilerConfigurations.processorOfString;
import static org.openprovenance.prov.template.compiler.CompilerUtil.*;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.expander.ExpandUtil.isVariable;

public class CompilerCommon {
    public static final String SB_VAR = "sb";
    public static final String SELF_VAR = "self";
    public static final String MARKER_LAMBDA_END = "/*#lend#*/";
    public static final String MARKER_LAMBDA_BODY = "/*#lbody#*/";
    public static final String MARKER_LAMBDA = "/*#lambda#*/";
    public static final String MARKER_PARAMS = "/*#params#*/";
    public static final String MARKER_PARAMS_END = "/*#paramsend#*/";
    public static final String MARKER_ENDIF = "/*#endif#*/";
    public static final String MARKER_ELSE = "/*#else#*/";
    public static final String MARKER_THEN = "/*#then#*/";
    public static final String MARKER_ARRAY = "/*#array#*/";
    public static final String UNKNOWN = "unknown";
    public static final String POST_PROCESSING_VAR = "postProcessing";
    public static final String[] ALL_RELATIONS_TODELETE = {
            "wasDerivedFrom",
            "wasAttributedTo",
            "wasAssociatedWith",
            "wasGeneratedBy",
            "used",
            "actedOnBehalfOf",
            "hadMember",
            "specializationOf"
    };
    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;

    private final CompilerSQL compilerSQL;

    public CompilerCommon(ProvFactory pFactory, CompilerSQL compilerSQL) {
        this.pFactory=pFactory;
        this.compilerSQL=compilerSQL;
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public TypeSpec.Builder generateClassInit(String builderName, String builderPackage, String processorName, String supername, String templateName) {
        return TypeSpec.classBuilder(builderName)
                .addSuperinterface(ClassName.get(builderPackage,supername))
                .addSuperinterface(ClassName.get(builderPackage,"SQL"))
                .addJavadoc("Builder class for $N", templateName)
                //.addSuperinterface(ClassName.get(processorPackage,processorName))  // implements Processor Interface
                .addModifiers(Modifier.PUBLIC);
    }

    public Pair<SpecificationFile, Map<Integer, List<Integer>>> generateCommonLib(TemplatesProjectConfiguration configs, Locations locations, Document doc, String name, String templateName, String packageName, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed, BeanKind beanKind, String fileName, String consistsOf) {


        Bundle bun=u.getBundle(doc).get(0);

        Set<QualifiedName> allVars=new HashSet<>();
        Set<QualifiedName> allAtts=new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);


        return generateCommonLib_aux(configs, locations, allVars, name, templateName, packageName, bindingsSchema, indexed, beanKind, fileName, consistsOf);

    }


    Pair<SpecificationFile, Map<Integer, List<Integer>>> generateCommonLib_aux(TemplatesProjectConfiguration configs, Locations locations, Set<QualifiedName> allVars, String name, String templateName, String packageName, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed, BeanKind beanKind, String fileName, String consistsOf) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = generateClassInit(name, Constants.CLIENT_PACKAGE, compilerUtil.processorNameClass(templateName), Constants.BUILDER, templateName);


        Map<Integer, List<Integer>> successorTable=null;

        //add default constructor
        builder.addMethod(MethodSpec.constructorBuilder()
                .addJavadoc("Default constructor for builder for template $N", templateName)
                .addModifiers(Modifier.PUBLIC)
                .build());



        builder.addMethod(generateNameAccessor(templateName));
        builder.addMethod(generatePropertyOrderMethod());

        builder.addField(generateFieldPropertyOrder(bindingsSchema));
        builder.addMethod(generateCommonMethodGetNodes(beanKind));
        builder.addMethod(generateCommonMethodGetSuccessors(beanKind));
        builder.addMethod(generateCommonMethodGetForeign(beanKind));
        builder.addMethod(generateCommonMethodGetTypedSuccessors(beanKind));
        builder.addMethod(generateRecordCsvProcessorMethod(beanKind));
        compilerSQL.generateSQLstatements(builder, templateName, bindingsSchema, beanKind);

        if (configs.integrator) {
            ClassName integratorClassName = ClassName.get(locations.getFilePackage(BeanDirection.INPUTS), compilerUtil.integratorBuilderNameClass(templateName));
            builder.addField(FieldSpec.builder(integratorClassName, "__integrator")
                    .addJavadoc("Generated by method $N", getClass().getName()+".generateCommonLib_aux()")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new $T()", integratorClassName)
                    .build());

            MethodSpec.Builder builder1 = MethodSpec
                    .methodBuilder("getIntegrator")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            compilerUtil.specWithJavaDoc(builder1);
            builder1.returns(integratorClassName)
                    .addStatement("return __integrator");
            builder.addMethod(builder1.build());
        }

        if (beanKind==BeanKind.SIMPLE) {
            builder.addMethod(generateCommonCSVConverterMethod_aux(name, templateName, compilerUtil.loggerName(templateName), packageName, bindingsSchema, beanKind, consistsOf, locations.getFilePackage(LOGGER), LOGGER));
            builder.addMethod(generateCommonSQLConverterMethod_aux(name, templateName, compilerUtil.loggerName(templateName), packageName, bindingsSchema));
            builder.addMethod(generateArgsToRecordMethod(templateName, packageName, bindingsSchema));
            builder.addMethod(generateProcessorConverter(templateName, packageName, bindingsSchema, BeanDirection.COMMON));
            builder.addMethod(generateProcessorConverter2(templateName, packageName, bindingsSchema));
            builder.addMethod(generateApplyMethod(templateName, packageName));


            builder.addMethod(generateLoggerMethod(templateName, bindingsSchema));
            builder.addMethod(generateCommonMethod2PureCsv(templateName, bindingsSchema, consistsOf));
            builder.addMethod(generateCommonMethod3static(bindingsSchema));
            builder.addMethod(generateCommonMethod4static(allVars, bindingsSchema, indexed));
            builder.addMethod(generateGetRelations(allVars, bindingsSchema, indexed));
            final Pair<MethodSpec, Map<Integer, List<Integer>>> methodSpecMapPair = generateCommonMethod5static(allVars, bindingsSchema, indexed);
            builder.addMethod(methodSpecMapPair.getLeft());
            successorTable=methodSpecMapPair.getRight();

            builder.addMethod(generateCommonMethod6static(indexed));

            builder.addField(generateFieldOutputs(bindingsSchema));
            builder.addField(generateFieldInputs(bindingsSchema));
            builder.addField(generateFieldCompulsoryInputs(bindingsSchema));
            builder.addField(generateFieldForeignTables(bindingsSchema));

            builder.addField(generateField4aBeanConverter(templateName,packageName, bindingsSchema));
            builder.addField(generateField4aBeanConverter2("toBean", templateName,packageName, Constants.A_RECORD_BEAN_CONVERTER, BeanDirection.COMMON));
            builder.addField(generateField4aSQLConverter2(name,templateName,packageName));
            builder.addField(generateField4aArgs2CsvConverter(name,templateName,packageName));
            builder.addField(generateField4aRecord2SqlConverter(templateName));
            builder.addField(generateField4aRecord2CsvConverter(name,templateName,packageName));

            builder.addField(FieldSpec.builder(mapIntArrayType, "__successors")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("__getSuccessors()")
                    .build());
            builder.addField(FieldSpec.builder(mapIntArrayType, "__successors2")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("__getTypedSuccessors()")
                    .build());
            builder.addField(FieldSpec.builder(int[].class, __NODES_FIELD)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("__getNodes()")
                    .build());
            builder.addField(FieldSpec.builder(mapStringMapStringArrayType, "__relations")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("__getRelations()")
                    .build());


            builder.addMethod(generateOutputsMethod());
            builder.addMethod(generateCompulsoryInputsMethod());
            builder.addMethod(generateInputsMethod());

            builder.addMethod(generateFactoryMethodToBeanWithArray("toBean", templateName, packageName, bindingsSchema, BeanDirection.COMMON, null, null));


            builder.addMethod(generateFactoryMethodWithBean(templateName, packageName, bindingsSchema));

            builder.addMethod(generateNewBean(templateName, packageName));
            builder.addMethod(generateExamplarBean(templateName, packageName, bindingsSchema));



            builder.addMethod(compilerSQL.generateCommonSQLMethod2(templateName, bindingsSchema));

        } else {
            builder.addField(generateField4aBeanConverter3("toBean", templateName, packageName, A_RECORD_BEAN_CONVERTER, BeanDirection.COMMON));
            builder.addMethod(generateFactoryMethodToBeanWithArrayComposite("toBean", templateName, packageName, bindingsSchema, locations.getFilePackage(LOGGER), LOGGER, BeanDirection.COMMON, null, null));
            builder.addMethod(generateNewBean(templateName, packageName));

            builder.addField(generateField4aArgs2CsvConverter(name,templateName,packageName));
            builder.addMethod(generateCommonMethod2PureCsv(templateName, bindingsSchema, consistsOf));
            builder.addMethod(generateCommonCSVConverterMethod_aux(name, templateName, compilerUtil.loggerName(templateName), packageName, bindingsSchema, beanKind, consistsOf, locations.getFilePackage(LOGGER), LOGGER));
            builder.addMethod(generateNullOutputsMethod());
            builder.addMethod(generateNullInputsMethod());

        }


        String directory = locations.convertToDirectory(packageName);


        TypeSpec bean=builder.build();


        JavaFile myfile = compilerUtil.specWithComment(bean, configs, packageName, stackTraceElement);
        SpecificationFile specFile;
        if (locations.python_dir==null) {
            specFile =  new SpecificationFile(myfile, directory, fileName, packageName);
        } else {
            Set<String> selectedExports = Set.of("args2csv", "toBean", "getName", "aArgs2BeanConverter", "log" + myfile.typeSpec.name.replace("Builder", ""));

            specFile =  newSpecificationFiles(compilerUtil, locations, bean, templateName, stackTraceElement, myfile, directory, fileName, packageName, selectedExports);

            /*
            final PoetParser poetParser = new PoetParser();
            poetParser.emitPrelude(compilerUtil.pySpecWithComment(bean, templateName, packageName, stackTraceElement));
            Set<String> selectedExports = Set.of("args2csv", "log" + myfile.typeSpec.name.replace("Builder", ""));
            org.openprovenance.prov.template.emitter.minilanguage.Class clazz = poetParser.parse(bean, selectedExports);
            clazz.emit(new Python(poetParser.getSb(), 0));

            String pyDirectory = locations.python_dir + "/" + packageName.replace('.', '/') + "/";

            specFile = new SpecificationFile(myfile, locations.convertToDirectory(packageName), fileName, packageName,
                    pyDirectory, myfile.typeSpec.name + ".py", () -> poetParser.getSb().toString());

             */
        }
        return Pair.of(specFile, successorTable);
    }


    public MethodSpec generateNameAccessor(String templateName) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getName")
                .addModifiers(Modifier.PUBLIC)
                //.addAnnotation(Override.class)
                .returns(String.class);
        compilerUtil.specWithComment(builder);

        builder.addStatement("return $S", templateName);
        return builder.build();
    }

    public MethodSpec generateLoggerMethod(String template, TemplateBindingsSchema bindingsSchema) {
        return generateLoggerMethod(template, bindingsSchema, true, null);
    }
    public MethodSpec generateCommonMethod2PureCsv(String template, TemplateBindingsSchema bindingsSchema, String consistsOf) {
        return generateLoggerMethod(template, bindingsSchema, false, consistsOf);
    }

    public MethodSpec generateCommonCSVConverterMethod_aux(String name, String template, String loggerName, String packge, TemplateBindingsSchema bindingsSchema, BeanKind beanKind, String consistsOf, String loggerPackage, String logger) {
        final TypeName processorClassName = processorClassType(template, packge,ClassName.get(String.class));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.ARGS_CSV_CONVERSION_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(processorClassName);
        compilerUtil.specWithComment(builder);


        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add(loggerName + " client side logging method\n");
        jdoc.add("@return $T\n" , processorClassNameNotParametrised);
        builder.addJavadoc(jdoc.build());


        builder.addStatement("$T $N=$N", ClassName.get(packge,name), SELF_VAR, "this");

        Map<String, List<Descriptor>> var = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        CodeBlock.Builder lambda=CodeBlock.builder();
        Collection<String> actualVariables;
        CodeBlock paramsList;
        if (beanKind==BeanKind.COMPOSITE) {
            actualVariables = new LinkedList<>(variables);
            actualVariables.add(ELEMENTS);
            String beanNameClass = compilerUtil.beanNameClass(consistsOf, BeanDirection.COMMON);
            ParameterizedTypeName listBeanType=ParameterizedTypeName.get(ClassName.get(List.class),ClassName.get(packge,beanNameClass));
            paramsList= makeParamsListComposite(actualVariables, var, compilerUtil, listBeanType);
        } else {
            actualVariables=variables;
            paramsList= makeParamsList(actualVariables, var, compilerUtil);
        }

        lambda.add("($L) -> $N {\n", paramsList, MARKER_LAMBDA_BODY).indent();
        lambda.addStatement("$T $N=$N $T()", StringBuffer.class, SB_VAR, "new", StringBuffer.class);

        CodeBlock argsList = makeRenamedArgsList(SB_VAR,variables);
        lambda.addStatement("$N.$N($L)", SELF_VAR, loggerName, argsList);

        if (consistsOf!=null) {
            String[] variableArray = variables.toArray(new String[]{});
            String beanNameClass = compilerUtil.beanNameClass(consistsOf, BeanDirection.COMMON);
            lambda.beginControlFlow("for ($T $N: $N) ", ClassName.get(packge,beanNameClass), VAR_ELEMENT, Constants.GENERATED_VAR_PREFIX + ELEMENTS);
            ClassName loggerClassName = ClassName.get(loggerPackage, logger);

            ParameterizedTypeName parametericInterface=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(consistsOf)), TypeName.get(Object[].class));
            ParameterizedTypeName parametericInterface2=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(consistsOf)), TypeVariableName.get("?"));

            lambda.addStatement("$T $N=$T.$N.$N()",
                    parametericInterface,
                    "processor",
                    loggerClassName,
                    Constants.GENERATED_VAR_PREFIX + consistsOf,
                    Constants.ARGS2RECORD_CONVERTER);

            lambda.addStatement("// the following line generates ts error: Untyped function calls may not accept type arguments.");

            lambda.addStatement("$T $N=$N.$N($N)",
                    Object[].class,
                    VAR_OBJECTS,
                    VAR_ELEMENT,
                    "process",
                    "processor");

            lambda.addStatement("$T $N=$T.$N.$N($N)",
                    processorOfString,
                    VAR_CSV_CONVERTER,
                    loggerClassName,
                    "simpleCSvConverters",
                    "get",
                    Constants.GENERATED_VAR_PREFIX + variableArray[2]);

            lambda.addStatement("$T $N=$N.$N($N)",
                    String.class,
                    VAR_CSV,
                    VAR_CSV_CONVERTER,
                    "apply",
                    VAR_OBJECTS);



/*
            lambda.addStatement("$T $N=$T.simpleCSvConverters.get($N).process($N.get(i).process($T.$N.$N()))",
                    String.class,
                    "s",
                    loggerClassName,
                    Constants.GENERATED_VAR_PREFIX + variableArray[2],
                    Constants.GENERATED_VAR_PREFIX + ELEMENTS,
                    loggerClassName,
                    Constants.GENERATED_VAR_PREFIX + consistsOf,
                    Constants.ARGS2RECORD_CONVERTER);


 */
          //  lambda.addStatement("$T $N=$S", String.class, VAR_CSV, "hello");
            lambda.addStatement("$N.append($S).append($N)", SB_VAR, "\n", VAR_CSV);
            lambda.endControlFlow();
        }


        lambda.addStatement("return $N.$N()", SB_VAR,"toString");
        lambda.unindent().add("}; $N", MARKER_LAMBDA_END);
        // note, poet builder does not accept nested statement codeblock, so instead of adding a statement, we add a codeblock
        builder.addCode("return $N $L", MARKER_LAMBDA, lambda.build());

        MethodSpec method = builder.build();

        return method;
    }

    public static CodeBlock makeParamsList(Collection<String> variables, Map<String, List<Descriptor>> theVars, CompilerUtil compilerUtil) {
        return CodeBlock.join(variables.stream().filter((v) -> theVars.containsKey(v) && theVars.get(v)!=null).map(variable ->
                    CodeBlock.of("$T $N", compilerUtil.getJavaTypeForDeclaredType(theVars, variable), Constants.GENERATED_VAR_PREFIX + variable)).collect(Collectors.toList()), ", ");
    }
    public static CodeBlock makeParamsListComposite(Collection<String> variables, Map<String, List<Descriptor>> var, CompilerUtil compilerUtil, ParameterizedTypeName listBeanType) {
        return CodeBlock.join(variables.stream().map(variable ->
                CodeBlock.of("$T $N", (variable.equals(ELEMENTS)? listBeanType: compilerUtil.getJavaTypeForDeclaredType(var, variable)), Constants.GENERATED_VAR_PREFIX + variable)).collect(Collectors.toList()), ", ");
    }

    public static CodeBlock makeRenamedArgsList(String head, Collection<String> variables) {
        List<String> variables2=new LinkedList<>();
        if (head!=null) variables2.add(head);
        variables2.addAll(variables);
        return CodeBlock.join(variables2.stream().map(variable -> CodeBlock.of("$N", (variable.equals(head)?variable: Constants.GENERATED_VAR_PREFIX + variable))).collect(Collectors.toList()), ", ");
    }

    public static CodeBlock makeArgsList(Collection<String> variables) {
        return CodeBlock.join(variables.stream().map(variable -> CodeBlock.of("$N", variable)).collect(Collectors.toList()), ", ");
    }
    public static CodeBlock makeStringSequence(String head, Collection<String> variables) {
        List<String> variables2=new LinkedList<>();
        if (head!=null) variables2.add(head);
        variables2.addAll(variables);
        return CodeBlock.join(variables2.stream().map(variable -> CodeBlock.of("$S",variable) ).collect(Collectors.toList()), ", ");
    }


    private FieldSpec generateField4aBeanConverter(String templateName, String packge, TemplateBindingsSchema bindingsSchema) {
        TypeName myType=processorClassType(templateName,packge,ClassName.get(packge,compilerUtil.commonNameClass(templateName)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, Constants.A_ARGS_BEAN_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);
        Map<String, List<Descriptor>> var = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        CodeBlock paramsList= makeParamsList(variables, var, compilerUtil);
        CodeBlock argsList=makeRenamedArgsList(null,variables);

        //fbuilder.initializer(" $N ($L) -> { return $N($L); }", MARKER_LAMBDA, paramsList, "toBean", argsList);

        CodeBlock.Builder lambda=CodeBlock.builder();
        lambda.add("($L) -> $N {", paramsList, MARKER_LAMBDA_BODY).indent();
        lambda.addStatement("return $N($L)", "toBean", argsList);
        lambda.add("}; $N", MARKER_LAMBDA_END);
        fbuilder.initializer(" $N $L", MARKER_LAMBDA, lambda.build());


        return fbuilder.build();
    }

    private FieldSpec generateFieldPropertyOrder(TemplateBindingsSchema bindingsSchema) {
        FieldSpec.Builder fbuilder=FieldSpec.builder(ArrayTypeName.of(String.class), Constants.PROPERTY_ORDER, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateFieldPropertyOrder()");
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        fbuilder.initializer("new $T {$L}", String[].class, makeStringSequence(IS_A,variables));
        return fbuilder.build();
    }

    private FieldSpec generateFieldOutputs(TemplateBindingsSchema bindingsSchema) {
        FieldSpec.Builder fbuilder=FieldSpec.builder(ArrayTypeName.of(String.class), Constants.OUTPUTS, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<String> outputs=variables.stream().filter(variable->descriptorUtils.isOutput(variable,bindingsSchema)).collect(Collectors.toList());
        fbuilder.initializer("new $T {$L}", String[].class, makeStringSequence(null,outputs));
        return fbuilder.build();
    }
    private FieldSpec generateFieldInputs(TemplateBindingsSchema bindingsSchema) {
        FieldSpec.Builder fbuilder=FieldSpec.builder(ArrayTypeName.of(String.class), Constants.INPUTS, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<String> inputs=variables.stream().filter(variable->descriptorUtils.isInput(variable,bindingsSchema)).collect(Collectors.toList());
        fbuilder.initializer("new $T {$L}", String[].class, makeStringSequence(null,inputs));
        return fbuilder.build();
    }

    private FieldSpec generateFieldCompulsoryInputs(TemplateBindingsSchema bindingsSchema) {
        FieldSpec.Builder fbuilder=FieldSpec.builder(ArrayTypeName.of(String.class), Constants.COMPULSORY_INPUTS, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateFieldCompulsoryInputs()");
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<String> compulsoryInputs=variables.stream().filter(variable->descriptorUtils.isCompulsoryInput(variable,bindingsSchema)).collect(Collectors.toList());
        fbuilder.initializer("new $T {$L}", String[].class, makeStringSequence(null,compulsoryInputs));
        return fbuilder.build();
    }

    private FieldSpec generateFieldForeignTables(TemplateBindingsSchema bindingsSchema) {
        FieldSpec.Builder fbuilder=FieldSpec.builder(ArrayTypeName.of(String.class), Constants.FOREIGN_TABLES, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateFieldForeignTables()");
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<String> foreignTables=variables.stream().map(variable->descriptorUtils.getSqlTable(variable,bindingsSchema).orElse(null)).collect(Collectors.toList());
        fbuilder.initializer("new $T {$L}", String[].class, makeStringSequence(IS_A,foreignTables));
        return fbuilder.build();
    }



    public FieldSpec generateField4aBeanConverter3(String toBean, String templateName, String packge, String fieldName, BeanDirection direction) {
        TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, ConfigProcessor.RECORDS_PROCESSOR_INTERFACE),ClassName.get(packge,compilerUtil.beanNameClass(templateName, direction)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, fieldName,Modifier.FINAL, Modifier.PUBLIC);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateField4aBeanConverter3()");
        fbuilder.initializer(" ($T records) -> { return $N(records); }", listOfArrays, toBean);
        return fbuilder.build();
    }


    public FieldSpec generateField4aBeanConverter2(String toBean, String templateName, String packge, String fieldName, BeanDirection direction) {
        TypeName myType=functionObjArrayTo(ClassName.get(packge,compilerUtil.beanNameClass(templateName, direction)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, fieldName,Modifier.FINAL, Modifier.PUBLIC);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateField4aBeanConverter2()");
        fbuilder.initializer(" ($T $N) -> { return $N($N); }",Object[].class,"record",toBean,"record");
        return fbuilder.build();
    }

    private FieldSpec generateField4aSQLConverter2(String name, String templateName, String packge) {
        final TypeName processorClassName = processorClassType(templateName, packge, ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(processorClassName, Constants.A_BEAN_SQL_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateField4aSQLConverter2()");
        fbuilder.initializer("$N()", Constants.BEAN_SQL_CONVERSION_METHOD);
        return fbuilder.build();
    }


    private FieldSpec generateField4aArgs2CsvConverter(String name, String templateName, String packge) {
        final TypeName processorClassName = processorClassType(templateName, packge, ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(processorClassName, Constants.A_ARGS_CSV_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateField4aArgs2CsvConverter()");
        fbuilder.initializer("$N()", Constants.ARGS_CSV_CONVERSION_METHOD);
        return fbuilder.build();
    }


    private FieldSpec generateField4aRecord2SqlConverter(String templateName) {
        //TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, Constants.A_RECORD_SQL_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer(" ($T $N) -> { return $N($N).$N($N); }", Object[].class, "record", "toBean", "record", "process", Constants.A_BEAN_SQL_CONVERTER);
        return fbuilder.build();
    }


    private FieldSpec generateField4aRecord2CsvConverter(String name, String templateName, String packge) {
        //TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, Constants.A_RECORD_CSV_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateField4aRecord2CsvConverter()");
        fbuilder.initializer(" ($T $N) -> { return $N($N).$N($N); }", Object[].class, "record", "toBean", "record", "process", Constants.A_ARGS_CSV_CONVERTER);
        return fbuilder.build();
    }




    public MethodSpec generateCommonSQLConverterMethod_aux(String name, String template, String loggerName, String packge, TemplateBindingsSchema bindingsSchema) {
        final TypeName processorClassName = processorClassType(template, packge, ClassName.get(String.class));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.BEAN_SQL_CONVERSION_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(processorClassName);
        compilerUtil.specWithComment(builder);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add(loggerName + " client side logging method\n");
        jdoc.add("@return $T\n" , processorClassNameNotParametrised);
        builder.addJavadoc(jdoc.build());

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        builder.addStatement("$T $N=this", ClassName.get(packge,name), SELF_VAR);

        String args = "";
        String args2 = "";

        boolean first=true;
        for (String key:variables) {
            String newkey = "__" + key;
            if (first) {
                first=false;
            } else {
                args = args + ", ";
                args2 = args2 + ", ";
            }
            args = args +  compilerUtil.getJavaTypeForDeclaredType(theVar, key).getName() + " " + newkey;
            args2=args2+ " " + newkey;

        }

        //ATTEMPTED
        //CodeBlock.Builder body = CodeBlock.builder();
        //body.addStatement("$N.$N(sb," + args2 + " )",  "self","sqlTuple");
        //body.addStatement("return sb.toString()");

        //CodeBlock.Builder returnLambda=CodeBlock.builder();
        //returnLambda.add("return (" + args + ") -> ").add(body.build());
        //builder.addStatement(returnLambda.build());



        builder.addStatement("return (" + args + ") -> { $T sb=new $T(); $N.$N(sb," + args2 + "); return sb.toString(); }", StringBuffer.class, StringBuffer.class, SELF_VAR,"sqlTuple");

        return builder.build();
    }



    public MethodSpec generateArgsToRecordMethod(String template, String packge, TemplateBindingsSchema bindingsSchema) {
        final TypeName processorClassName = processorClassType(template, packge, ArrayTypeName.of(Object.class));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.ARGS2RECORD_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .returns(processorClassName);
        compilerUtil.specWithComment(builder);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Returns a converter from arguments to record\n");
        jdoc.add("@return $T\n" , processorClassNameNotParametrised);
        builder.addJavadoc(jdoc.build());

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);


        String args = "";
        String args2 = "";

        boolean first=true;
        for (String key: variables) {
            String newkey = "__" + key;
            if (first) {
                first=false;
            } else {
                args = args + ", ";
                args2 = args2 + ", ";
            }
            args = args +  compilerUtil.getJavaTypeForDeclaredType(theVar, key).getName() + " " + newkey;
            args2=args2+ " " + newkey;

        }

        builder.addStatement("return (" + args + ") -> {  return new Object [] { getName(), " + args2 + "}; }");

        return builder.build();
    }

    public MethodSpec generateProcessorConverter(String template, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection beanDirection) {

        final TypeName returnClassName= beanDirection==BeanDirection.COMMON ? processorClassType(template, packge, typeT) : integratorClassType(template, packge, typeT);

        final TypeName returnClassNameNotParametrised = beanDirection==BeanDirection.COMMON ? processorClassType(template, packge): integratorClassType (template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PROCESSOR_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(returnClassName);
        compilerUtil.specWithComment(builder);


        TypeName parameterType = functionObjArrayTo(TypeVariableName.get("T"));


        String processor = compilerUtil.generateNewNameForVariable("processor");
        builder.addParameter(parameterType, processor, Modifier.FINAL);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Returns a converter from Processor taking arguments to Processor taking record\n");
        jdoc.add("@param $N a transformer for this template\n", processor);
        jdoc.add("@param <T> type variable for the result of processor\n");
        jdoc.add("@return $T&lt;$T&gt;\n", returnClassNameNotParametrised, TypeVariableName.get("T"));
        builder.addJavadoc(jdoc.build());

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        StringBuilder args = new StringBuilder();
        StringBuilder args2 = new StringBuilder();

        boolean first = true;

        for (String key : fieldNames) {
            String newKey = compilerUtil.generateNewNameForVariable(key);
            boolean isOutput=descriptorUtils.isOutput(key,bindingsSchema);
            if (beanDirection==BeanDirection.COMMON || !isOutput) {
                if (first) {
                    first = false;
                } else {
                    args.append(", ");
                }
                args.append(compilerUtil.getJavaTypeForDeclaredType(theVar, key).getName()).append(" ").append(newKey);
            }
        }
        first = true;

        for (String key : fieldNames) {
            String newKey = compilerUtil.generateNewNameForVariable(key);
            boolean isOutput=descriptorUtils.isOutput(key,bindingsSchema);
            if (first) {
                first = false;
            } else {
                args2.append(", ");
            }
            if (beanDirection!=BeanDirection.COMMON && isOutput) {
                args2.append(" null");
            } else {
                args2.append(" ").append(newKey);
            }

        }

        builder.addStatement("return ($L) -> {  return $N.apply(new Object [] { getName(), $L}); }", args, processor, args2);

        return builder.build();
    }

    public MethodSpec generateProcessorConverter2(String template, String packge, TemplateBindingsSchema bindingsSchema) {
        final TypeName processorClassName = processorClassType(template, packge, typeT);
        TypeName returnType=functionObjArrayTo(typeT);
        TypeName returnTypeNotParametrised =ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PROCESSOR_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(typeT)
                .returns(returnType);
        compilerUtil.specWithComment(builder);




        builder.addParameter(processorClassName, "processor", Modifier.FINAL);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Returns a converter from Processor taking arguments to Processor taking record\n");
        jdoc.add("@param processor a transformer for this template\n");
        jdoc.add("@param <T> type variable for the result of processor\n");
        jdoc.add("@return $T&lt;$T&gt;\n" , returnTypeNotParametrised, typeT);
        builder.addJavadoc(jdoc.build());

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        String args = "";
        String args2 = "";
        int count=1;

        boolean first=true;
        for (String key: fieldNames) {
            String newkey = compilerUtil.generateNewNameForVariable(key);
            if (first) {
                first=false;
            } else {
                args = args + ", ";
                args2 = args2 + ", ";
            }

            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(theVar, key);
            final String type=declaredJavaType.getName();
            final String converter = compilerUtil.getConverterForDeclaredType(declaredJavaType);
            final String converter2 = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
            String expression;
            if (converter2 == null) {
                expression = "(" + type + ") record[" + count + "]";
            } else {
                //expression = "(record[" + count + "]==null)?0:" + converter2 + "((String)(record[" + count + "]))";
                expression= "(record[" + count + "]==null)?null:((record[" + count + "] instanceof String)?" + converter2 + "((String)(record[" + count + "])):(" + type + ")(record[" + count + "]))";

            }
            args = args + type + " " + newkey;
            args2=args2+ " " + expression;
            count ++;

        }



        builder.addStatement("return (Object [] record) -> {  return processor.process(" + args2 + "); }");

        return builder.build();
    }

    public MethodSpec generateApplyMethod(String template, String packge) {
        final TypeName processorClassName = processorClassType(template, packge, TypeVariableName.get("T"));

        MethodSpec.Builder builder = MethodSpec.methodBuilder("apply")
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(TypeVariableName.get("T"));
        compilerUtil.specWithComment(builder);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Apply method\n");
        jdoc.add("@param processor a transformer for this template\n");
        jdoc.add("@param record as an array of Objects\n");
        jdoc.add("@param <T> type variable for the result of processor\n");
        jdoc.add("@return an object of type $T\n" , TypeVariableName.get("T"));
        builder.addJavadoc(jdoc.build());

        final String var_processor = "processor";
        final String var_record = "record";
        builder.addParameter(processorClassName, var_processor);
        builder.addParameter(ArrayTypeName.of(Object.class), var_record);
        builder.addStatement("return toBean($N).process($N)", var_record, var_processor);
        MethodSpec method = builder.build();

        return method;
    }

    private TypeName processorClassType(String template, String packge, TypeVariableName t) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),t);
        return name;
    }
    private TypeName integratorClassType(String template, String packge, TypeVariableName t) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.integratorNameClass(template)),t);
        return name;
    }

    private TypeName processorClassType(String template, String packge, ParameterizedTypeName parameterizedTypeName) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),parameterizedTypeName);
        return name;
    }

    private TypeName processorClassType(String template, String packge, ArrayTypeName arrayTypeName) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),arrayTypeName);
        return name;
    }
    private TypeName processorClassType(String template, String packge, ClassName cl) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),cl);
        return name;
    }

    private TypeName processorClassType(String template, String packge) {
        return ClassName.get(packge,compilerUtil.processorNameClass(template));
    }
    private TypeName integratorClassType(String template, String packge) {
        return ClassName.get(packge,compilerUtil.integratorNameClass(template));
    }


    public MethodSpec generateLoggerMethod(String template, TemplateBindingsSchema bindingsSchema, boolean legacy, String consistOf) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(compilerUtil.loggerName(template) + (legacy ? "_impure" : ""))
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        String var = SB_VAR;

        compilerUtil.specWithComment(builder);



        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Logger method\n");
        jdoc.add("@param $N a StringBuffer\n", var);


        builder.addParameter(StringBuffer.class, var);

        for (String key: fieldNames) {
            String newkey = compilerUtil.generateNewNameForVariable(key);
            Descriptor descriptor=theVar.get(key).get(0);
            String documentation=descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getDocumentation, NameDescriptor::getDocumentation);

            jdoc.add("@param $N $N\n", newkey, documentation);
            builder.addParameter(compilerUtil.getJavaTypeForDeclaredType(theVar, key), newkey);
        }

        builder.addJavadoc(jdoc.build());


        String constant = (legacy? "[" : "") + StringEscapeUtils.escapeCsv(template);
        for (String key: fieldNames) {

            final String newName = compilerUtil.generateNewNameForVariable(key);
            final Class<?> clazz = compilerUtil.getJavaTypeForDeclaredType(theVar, key);
            final boolean isQualifiedName = theVar.get(key).get(0) instanceof NameDescriptor; //the_var.get(key).get(0).get("@id") != null;

            constant = constant + ',';
            builder.addStatement("$N.$N($S)", var, "append", constant);
            constant = "";

            if (String.class.equals(clazz)) {
                String myStatement = "$N.$N($N)";
                String myEscapeStatement = "$N.$N($T.$N($N))";
                boolean doEscape=false;
                if (!isQualifiedName) {
                    doEscape = ((AttributeDescriptorList)theVar.get(key).get(0)).getItems().get(0).getEscape() !=null; //.the_var.get(key).get(0).get(0).get("@escape") != null;
                    if (doEscape) {
                        foundEscape=true;
                    }
                }
                builder.beginControlFlow("if ($N==$L) $N", newName,null, MARKER_THEN);
                if (legacy) {
                    builder.addStatement("$N.$N($N)", var, "append", newName);  // in legacy format, we insert a null
                }
                builder.nextControlFlow("else $N", MARKER_ELSE);

                if (doEscape) {
                    builder.addStatement(myEscapeStatement, var, "append", ClassName.get("org.openprovenance.apache.commons.lang", "StringEscapeUtils"), "escapeCsv", newName);
                } else {
                    builder.addStatement(myStatement, var, "append", newName);
                }
                builder.endControlFlow("$N", MARKER_ENDIF);
            } else {
                builder.beginControlFlow("if ($N==$L) $N", newName, null, MARKER_THEN);
                builder.nextControlFlow("else $N", MARKER_ELSE);
                builder.addStatement("$N.$N($S)", var, "append", constant);
                builder.addStatement("$N.$N($N)", var, "append", newName);
                builder.endControlFlow("$N", MARKER_ENDIF);
            }
        }
        constant = constant + (legacy ? ']' : "");
        builder.addStatement("$N.$N($S)", var, "append", constant);


        return builder.build();
    }

    public boolean getFoundEscape() {
        return foundEscape;
    }

    private boolean foundEscape=false;

    public MethodSpec generateCommonMethod3static(TemplateBindingsSchema bindingsSchema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getNodes")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(int[].class);
        compilerUtil.specWithComment(builder);



        Map<String, List<Descriptor>> theVar= bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);


        int count = 0;
        List<Integer> ll = new LinkedList<>();
        for (String key: fieldNames) {
            count++;
            if (theVar.get(key).get(0) instanceof NameDescriptor) {
                ll.add(count);
            }
        }

        String nodes = "";
        boolean first = true;
        for (int elem : ll) {
            if (first) {
                first = false;
            } else {
                nodes = nodes + ", ";
            }
            nodes = nodes + elem;
        }

        builder.addStatement("return new int[] {" + nodes + "}");

        return builder.build();
    }

    public MethodSpec generateCommonMethodGetNodes(BeanKind beanKind) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getNodes")
                .addModifiers(Modifier.PUBLIC)
                .returns(int[].class);
        compilerUtil.specWithComment(builder);

        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(builder);
        } else {
            builder.addStatement("return $N", __NODES_FIELD);
        }


        MethodSpec method = builder.build();

        return method;
    }

    static public void generateUnsupportedException(MethodSpec.Builder builder) {
        builder.addStatement("throw new $T()", UnsupportedOperationException.class);
    }
    static public final ParameterizedTypeName functionObjArrayTo (TypeName returnType) {
        return ParameterizedTypeName.get(ClassName.get(Function.class), ArrayTypeName.of(Object.class), returnType);
    }

    TypeName OLDmyType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));
    TypeName myType=functionObjArrayTo(ClassName.get(String.class));

    public MethodSpec generatePropertyOrderMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.PROPERTY_ORDER_METHOD)
                .addJavadoc("Null method for composite\n@return the array of properties in order\n")
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);
        compilerUtil.specWithComment(builder5);
        builder5.addStatement("return $N", Constants.PROPERTY_ORDER);
        return builder5.build();
    }
    public MethodSpec generateOutputsMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.OUTPUTS_METHOD)
                .addJavadoc("Null method for composite\n@return null")
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);
        compilerUtil.specWithComment(builder5);
        builder5.addStatement("return $N", Constants.OUTPUTS);
        return builder5.build();
    }
    public MethodSpec generateCompulsoryInputsMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.COMPULSORY_INPUTS_METHOD)
                .addJavadoc("Null method for composite\n@return null")
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);
        compilerUtil.specWithComment(builder5);
        builder5.addStatement("return $N", Constants.COMPULSORY_INPUTS);
        return builder5.build();
    }
    public MethodSpec generateInputsMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.INPUTS_METHOD)
                .addJavadoc("Null method for composite\n@return null")
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);
        compilerUtil.specWithComment(builder5);
        builder5.addStatement("return $N", Constants.INPUTS);
        return builder5.build();
    }
    public MethodSpec generateNullInputsMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.INPUTS_METHOD)
                .addJavadoc("Null method for composite\n@return null")
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);
        compilerUtil.specWithComment(builder5);
        builder5.addStatement("return $N", "null");
        return builder5.build();
    }
    public MethodSpec generateNullOutputsMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.OUTPUTS_METHOD)
                .addJavadoc("Null method for composite\n@return null")
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);
        compilerUtil.specWithComment(builder5);
        builder5.addStatement("return $N", "null");
        return builder5.build();
    }
    public MethodSpec generateRecordCsvProcessorMethod(BeanKind beanKind) {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.RECORD_CSV_PROCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ArrayTypeName.of(Object.class),"record").build())
                .returns(myType);
        compilerUtil.specWithComment(builder5);
        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(builder5);
        } else {
            builder5.addStatement("return $N", Constants.A_RECORD_CSV_CONVERTER);
        }
        return builder5.build();
    }
    public MethodSpec generateCommonMethodGetSuccessors(BeanKind beanKind) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getSuccessors")
                .addModifiers(Modifier.PUBLIC)
                .returns(mapIntArrayType);
        compilerUtil.specWithComment(builder);

        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(builder);
        } else {
            builder.addStatement("return __successors");
        }
        return builder.build();
    }


    public MethodSpec generateCommonMethodGetForeign(BeanKind beanKind) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(GET_FOREIGN)
                .addModifiers(Modifier.PUBLIC)
                .returns(ArrayTypeName.of(String.class));
        compilerUtil.specWithComment(builder);

        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(builder);
        } else {
            builder.addStatement("return $N",Constants.FOREIGN_TABLES);
        }
        return builder.build();
    }
    public MethodSpec generateCommonMethodGetTypedSuccessors(BeanKind beanKind) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getTypedSuccessors")
                .addModifiers(Modifier.PUBLIC)
                .returns(mapIntArrayType);
        compilerUtil.specWithComment(builder);

        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(builder);
        } else {
            builder.addStatement("return __successors2");
        }

        return builder.build();
    }

    public MethodSpec generateCommonMethod4static(Set<QualifiedName> allVars, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getSuccessors")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mapIntArrayType);
        compilerUtil.specWithComment(builder);


        Map<String, List<Descriptor>> theVar= bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        int count2 = 0;
        HashMap<QualifiedName, Integer> index = new HashMap<>();
        for (String key: fieldNames) {
            count2++;
            for (QualifiedName qn : allVars) {
                if (key.equals(qn.getLocalPart())) {
                    index.put(qn, count2);
                }
            }
        }

        builder.addStatement("$T table = new $T()", mapIntArrayType, CompilerUtil.hashmapType);


        int count = 0;

        for (String key: fieldNames) {
            count++;
            if (theVar.get(key).get(0) instanceof NameDescriptor) {

                Set<QualifiedName> successors = new HashSet<>();
                for (QualifiedName qn : allVars) {
                    if (key.equals(qn.getLocalPart())) {
                        successors = indexed.traverseDerivations(qn);
                        break;
                    }
                }
                String initializer = "";
                boolean first = true;
                for (QualifiedName successor : successors) {
                    Integer i = index.get(successor);
                    if (i!=null) {
                        if (first) {
                            first = false;
                        } else {
                            initializer = initializer + ", ";
                        }
                        initializer = initializer + i;
                    }
                }

                builder.addStatement("table.put($L,new int[] { " + initializer + "})", count);

            }

        }


        builder.addStatement("return table");


        return builder.build();
    }
    public MethodSpec generateGetRelations(Set<QualifiedName> allVars, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getRelations")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mapStringMapStringArrayType);
        compilerUtil.specWithComment(builder);


        Map<String, List<Descriptor>> theVar= bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        int count2 = 0;
        HashMap<QualifiedName, Integer> varCount = new HashMap<>();
        for (String key: fieldNames) {
            count2++;
            for (QualifiedName qn : allVars) {
                if (key.equals(qn.getLocalPart())) {
                    varCount.put(qn, count2);
                }
            }
        }

        builder.addStatement("$T table = new $T<>()", mapStringMapStringArrayType, HashMap.class);
        builder.addStatement("$T map2", mapStringArrayType);
        for (StatementOrBundle.Kind rel: ALL_RELATIONS) {
            AtomicInteger count;
            boolean found;
            switch (rel) {
                case PROV_DERIVATION:
                    count = new AtomicInteger();
                    found=false;
                    Collection<WasDerivedFrom> anonWasDerivedFrom = indexed.getWasDerivedFrom();
                    Collection<WasDerivedFrom> namedWasDerivedFrom = indexed.getNamedWasDerivedFrom().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    found = processWasDerivedFrom(anonWasDerivedFrom, varCount, found, builder, count, true);
                    found = processWasDerivedFrom(namedWasDerivedFrom, varCount, found, builder, count, false);
                    if (found) builder.addStatement("table.put($S,map2)", rel);
                    break;
                case PROV_ATTRIBUTION:
                    count = new AtomicInteger();
                    found=false;
                    Collection<WasAttributedTo> anonWasAttributedTo = indexed.getWasAttributedTo();
                    Collection<WasAttributedTo> namedWasAttributedTo = indexed.getNamedWasAttributedTo().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    found = processWasAttributedTo(anonWasAttributedTo, varCount, found, builder, count, true);
                    found = processWasAttributedTo(namedWasAttributedTo, varCount, found, builder, count, false);
                    if (found) builder.addStatement("table.put($S,map2)", rel);
                    break;
                case PROV_ASSOCIATION:
                    count = new AtomicInteger();
                    found=false;
                    Collection<WasAssociatedWith> anonWasAssociatedWith = indexed.getWasAssociatedWith();
                    Collection<WasAssociatedWith> namedWasAssociatedWith = indexed.getNamedWasAssociatedWith().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    found = processWasAssociatedWith(anonWasAssociatedWith, varCount, found, builder, count, true);
                    found = processWasAssociatedWith(namedWasAssociatedWith, varCount, found, builder, count, false);
                    if (found) builder.addStatement("table.put($S,map2)", rel);
                    break;
                case PROV_GENERATION:
                    count = new AtomicInteger();
                    found=false;
                    Collection<WasGeneratedBy> anonWasGeneratedBy = indexed.getWasGeneratedBy();
                    Collection<WasGeneratedBy> namedWasGeneratedBy = indexed.getNamedWasGeneratedBy().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    found = processWasGeneratedBy(anonWasGeneratedBy, varCount, found, builder, count, true);
                    found = processWasGeneratedBy(namedWasGeneratedBy, varCount, found, builder, count, false);
                    if (found) builder.addStatement("table.put($S,map2)", rel);
                    break;
                case PROV_USAGE:
                    count = new AtomicInteger();
                    found=false;
                    Collection<Used> anonUsed = indexed.getUsed();
                    Collection<Used> namedUsed = indexed.getNamedUsed().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    found = processUsed(anonUsed, varCount, found, builder, count, true);
                    found = processUsed(namedUsed, varCount, found, builder, count, false);
                    if (found) builder.addStatement("table.put($S,map2)", rel);
                    break;
                case PROV_DELEGATION:
                    count = new AtomicInteger();
                    found=false;
                    Collection<ActedOnBehalfOf> anonActedOnBehalfOf = indexed.getActedOnBehalfOf();
                    Collection<ActedOnBehalfOf> namedActedOnBehalfOf = indexed.getNamedActedOnBehalfOf().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    found = processActedOnBehalfOf(anonActedOnBehalfOf, varCount, found, builder, count, true);
                    found = processActedOnBehalfOf(namedActedOnBehalfOf, varCount, found, builder, count, false);
                    if (found) builder.addStatement("table.put($S,map2)", rel);
                    break;

                case PROV_SPECIALIZATION:
                    count = new AtomicInteger();
                    found=false;
                    Collection<SpecializationOf> anonSpecializationOf = indexed.getSpecializationOf();
                    Collection<SpecializationOf> namedSpecializationOf = indexed.getNamedSpecializationOf().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    found = processSpecializationOf(anonSpecializationOf, varCount, found, builder, count, true);
                    found = processSpecializationOf(namedSpecializationOf, varCount, found, builder, count, false);
                    if (found) builder.addStatement("table.put($S,map2)", rel);
                    break;

                case PROV_MEMBERSHIP:
                    count = new AtomicInteger();
                    found=false;
                    Collection<HadMember> anonHadMember = indexed.getHadMember();
                    Collection<HadMember> namedHadMember = indexed.getNamedHadMember().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    found = processHadMember(anonHadMember, varCount, found, builder, count, true);
                    found = processHadMember(namedHadMember, varCount, found, builder, count, false);
                    if (found) builder.addStatement("table.put($S,map2)", rel);
                    break;
            }
        }



        builder.addStatement("return table");


        return builder.build();
    }

    private boolean processWasDerivedFrom(Collection<WasDerivedFrom> wdfCollection, HashMap<QualifiedName, Integer> varCount, boolean found, MethodSpec.Builder builder, AtomicInteger count, boolean anon) {
        for (WasDerivedFrom rel : wdfCollection) {
            Integer gen = countIsNull(varCount.get(rel.getGeneratedEntity()));
            Integer usd = countIsNull(varCount.get(rel.getUsedEntity()));
            Integer act = countIsNull(varCount.get(rel.getActivity()));
            if (gen >= 0) {
                if (!found) {
                    builder.addStatement("map2 = new $T<>()", HashMap.class);
                    found=true;
                }
                String label = getLabel(count.get(), anon, rel.getId());
                builder.addStatement("map2.put($S, (new int[] { $L, $L, $L }))", label, gen, usd, act);
            }
            count.getAndIncrement();
        }
        return found;
    }
    private boolean processWasAttributedTo(Collection<WasAttributedTo> watCollection, HashMap<QualifiedName, Integer> varCount, boolean found, MethodSpec.Builder builder, AtomicInteger count, boolean anon) {
        for (WasAttributedTo rel : watCollection) {
            Integer ent = countIsNull(varCount.get(rel.getEntity()));
            Integer ag = countIsNull(varCount.get(rel.getAgent()));
            if (ent >= 0 && ag >= 0) {
                if (!found) {
                    builder.addStatement("map2 = new $T<>()", HashMap.class);
                    found=true;
                }
                String label = getLabel(count.get(), anon, rel.getId());
                builder.addStatement("map2.put($S, (new int[] { $L, $L}))", label, ent, ag);
            }
            count.getAndIncrement();
        }
        return found;
    }
    private boolean processWasAssociatedWith(Collection<WasAssociatedWith> wawCollection, HashMap<QualifiedName, Integer> varCount, boolean found, MethodSpec.Builder builder, AtomicInteger count, boolean anon) {
        for (WasAssociatedWith rel : wawCollection) {
            Integer act = countIsNull(varCount.get(rel.getActivity()));
            Integer ag = countIsNull(varCount.get(rel.getAgent()));
            Integer pl = countIsNull(varCount.get(rel.getPlan()));
            if (act >= 0 && ag >= 0) {
                if (!found) {
                    builder.addStatement("map2 = new $T<>()", HashMap.class);
                    found=true;
                }
                String label = getLabel(count.get(), anon, rel.getId());
                builder.addStatement("map2.put($S, (new int[] { $L, $L, $L}))", label, act, ag, pl);
            }
            count.getAndIncrement();
        }
        return found;
    }

    private boolean processWasGeneratedBy(Collection<WasGeneratedBy> wgbCollection, HashMap<QualifiedName, Integer> varCount, boolean found, MethodSpec.Builder builder, AtomicInteger count, boolean anon) {
        for (WasGeneratedBy rel : wgbCollection) {
            Integer ent = countIsNull(varCount.get(rel.getEntity()));
            Integer act = countIsNull(varCount.get(rel.getActivity()));
            if (ent >= 0 && act >= 0) {
                if (!found) {
                    builder.addStatement("map2 = new $T<>()", HashMap.class);
                    found=true;
                }
                String label = getLabel(count.get(), anon, rel.getId());
                builder.addStatement("map2.put($S, (new int[] { $L, $L}))", label, ent, act);
            }
            count.getAndIncrement();
        }
        return found;
    }

    private boolean processUsed(Collection<Used> usedCollection, HashMap<QualifiedName, Integer> varCount, boolean found, MethodSpec.Builder builder, AtomicInteger count, boolean anon) {
        for (Used rel : usedCollection) {
            Integer act = countIsNull(varCount.get(rel.getActivity()));
            Integer ent = countIsNull(varCount.get(rel.getEntity()));
            if (act >= 0 && ent >= 0) {
                if (!found) {
                    builder.addStatement("map2 = new $T<>()", HashMap.class);
                    found=true;
                }
                String label = getLabel(count.get(), anon, rel.getId());
                builder.addStatement("map2.put($S, (new int[] { $L, $L}))", label, act, ent);
            }
            count.getAndIncrement();
        }
        return found;
    }

    private boolean processActedOnBehalfOf(Collection<ActedOnBehalfOf> aoboCollection, HashMap<QualifiedName, Integer> varCount, boolean found, MethodSpec.Builder builder, AtomicInteger count, boolean anon) {
        for (ActedOnBehalfOf rel : aoboCollection) {
            Integer del = countIsNull(varCount.get(rel.getDelegate()));
            Integer ag = countIsNull(varCount.get(rel.getResponsible()));
            Integer act = countIsNull(varCount.get(rel.getActivity()));
            if (del >= 0 && ag >= 0) {
                if (!found) {
                    builder.addStatement("map2 = new $T<>()", HashMap.class);
                    found=true;
                }
                String label = getLabel(count.get(), anon, rel.getId());
                builder.addStatement("map2.put($S, (new int[] { $L, $L, $L}))", label, del, ag, act);
            }
            count.getAndIncrement();
        }
        return found;
    }

    private boolean processSpecializationOf(Collection<SpecializationOf> soCollection, HashMap<QualifiedName, Integer> varCount, boolean found, MethodSpec.Builder builder, AtomicInteger count, boolean anon) {
        for (SpecializationOf rel : soCollection) {
            Integer spec = countIsNull(varCount.get(rel.getSpecificEntity()));
            Integer gen = countIsNull(varCount.get(rel.getGeneralEntity()));
            if (spec >= 0 && gen >= 0) {
                if (!found) {
                    builder.addStatement("map2 = new $T<>()", HashMap.class);
                    found=true;
                }
                String label = getLabel(count.get(), anon, null);
                builder.addStatement("map2.put($S, (new int[] { $L, $L}))", label, spec, gen);
            }
            count.getAndIncrement();
        }
        return found;
    }

    private boolean processHadMember(Collection<HadMember> hmCollection, HashMap<QualifiedName, Integer> varCount, boolean found, MethodSpec.Builder builder, AtomicInteger count, boolean anon) {
        for (HadMember rel : hmCollection) {
            Integer coll = countIsNull(varCount.get(rel.getCollection()));
            List<QualifiedName> entities = rel.getEntity();
            for (QualifiedName entity: entities) {
                Integer mem = countIsNull(varCount.get(entity));
                if (coll >= 0 && mem >= 0) {
                    if (!found) {
                        builder.addStatement("map2 = new $T<>()", HashMap.class);
                        found = true;
                    }
                    String label = getLabel(count.get(), anon, null); // note labelling is for each member
                    builder.addStatement("map2.put($S, (new int[] { $L, $L}))", label, coll, mem);
                }
                count.getAndIncrement();
            }
        }
        return found;
    }

    private String getLabel(int count, boolean anon, QualifiedName id) {
        String label;
        if (!anon && id!=null) {
            label= id.getLocalPart();
        } else  if (anon && id!=null) {
            label="--"+ id.getLocalPart()+"--";
        } else {
            label="--"+ count +"--";
        }
        return label;
    }

    private Integer countIsNull(Integer integer) {
        if (integer==null) return -1;
        return integer;

    }

    public void calculateTypedSuccessors(Set<QualifiedName> allVars,
                                         Map<String, List<Descriptor>> theVar,
                                         Collection<String> fieldNames,
                                         IndexedDocument indexed,
                                         Map<String, Set<Pair<QualifiedName, WasDerivedFrom>>>   successors1,
                                         Map<String, Set<Pair<QualifiedName, WasAttributedTo>>>  successors2,
                                         Map<String, Set<Pair<QualifiedName, HadMember>>>        successors3,  // pure unqualified relation
                                         Map<String, Set<Pair<QualifiedName, QualifiedHadMember>>>        successors3b, // pure qualified relation
                                         Map<String, Set<Pair<QualifiedName, SpecializationOf>>> successors4) {
        for (String key: fieldNames) {
            if (compilerUtil.isVariableDenotingQualifiedName(key,theVar)) {
                for (QualifiedName qn : allVars) {
                    if (key.equals(qn.getLocalPart())) {
                        final Set<Pair<QualifiedName, WasDerivedFrom>> pairs1 = indexed.traverseDerivationsWithRelations(qn);
                        if (!pairs1.isEmpty()) successors1.put(key, pairs1);
                        final Set<Pair<QualifiedName, WasAttributedTo>> pairs2 = indexed.traverseAttributionsWithRelations(qn);
                        if (!pairs2.isEmpty()) successors2.put(key, pairs2);
                        final Set<Pair<QualifiedName, HadMember>> pairs3 = indexed.traverseReverseMembershipsWithRelations(qn); // note Reverse relation
                        if (!pairs3.isEmpty()) {
                            for (Pair<QualifiedName, HadMember> pair3 : pairs3) {
                                if (pair3.getRight() instanceof QualifiedHadMember) {
                                    successors3b.computeIfAbsent(key, k -> new HashSet<>());
                                    successors3b.get(key).add(Pair.of(pair3.getLeft(),(QualifiedHadMember)pair3.getRight()));
                                } else {
                                    successors3.computeIfAbsent(key, k -> new HashSet<>());
                                    successors3.get(key).add(pair3);
                                }
                            }
                        }
                        final Set<Pair<QualifiedName, SpecializationOf>> pairs4 = indexed.traverseSpecializationsWithRelations(qn); // note Reverse relation
                        if (!pairs4.isEmpty()) successors4.put(key, pairs4);
                        break;
                    }
                }
            }
        }
    }

    public Map<String, Set<Pair<QualifiedName, WasDerivedFrom>>> getSuccessors1() {
        return successors1;
    }

    public Map<String, Set<Pair<QualifiedName, WasAttributedTo>>> getSuccessors2() {
        return successors2;
    }

    public Map<String, Set<Pair<QualifiedName, HadMember>>> getSuccessors3() {
        return successors3;
    }
    public Map<String, Set<Pair<QualifiedName, QualifiedHadMember>>> getSuccessors3b() {
        return successors3b;
    }
    public Map<String, Set<Pair<QualifiedName, SpecializationOf>>> getSuccessors4() {
        return successors4;
    }

    Map<String,Set<Pair<QualifiedName, WasDerivedFrom>>>  successors1 = new HashMap<>();
    Map<String,Set<Pair<QualifiedName, WasAttributedTo>>> successors2 = new HashMap<>();
    Map<String,Set<Pair<QualifiedName, HadMember>>>       successors3 = new HashMap<>();
    Map<String,Set<Pair<QualifiedName, QualifiedHadMember>>> successors3b = new HashMap<>();
    Map<String,Set<Pair<QualifiedName, SpecializationOf>>>successors4 = new HashMap<>();


    public Pair<MethodSpec, Map<Integer, List<Integer>>> generateCommonMethod5static(Set<QualifiedName> allVars, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getTypedSuccessors")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mapIntArrayType);
        compilerUtil.specWithComment(builder);



        Map<String, List<Descriptor>> theVar= bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);


        /* Note make sure you re initialise those, as thre is a single compile client created for all templates in a config. */

        successors1 = new HashMap<>();
        successors2 = new HashMap<>();
        successors3 = new HashMap<>();
        successors3b = new HashMap<>();
        successors4 = new HashMap<>();

        calculateTypedSuccessors(allVars, theVar, fieldNames, indexed, successors1,successors2,successors3,successors3b,successors4);



        int count2 = 0;
        HashMap<QualifiedName, Integer> index = new HashMap<>();
        for (String key: fieldNames) {
            count2++;
            for (QualifiedName qn : allVars) {
                if (key.equals(qn.getLocalPart())) {
                    index.put(qn, count2);
                }
            }
        }

        builder.addStatement("$T table = new $T()", mapIntArrayType, CompilerUtil.hashmapType);


        int count = 0;

        Map<Integer,List<Integer>> tableValues=new HashMap<>();

        for (String key: fieldNames) {

            count++;

            if (theVar.get(key).get(0) instanceof NameDescriptor) {


                Set<Pair<QualifiedName, WasDerivedFrom>>  successors1 = new HashSet<>();
                Set<Pair<QualifiedName, WasAttributedTo>> successors2 = new HashSet<>();
                Set<Pair<QualifiedName, HadMember>>       successors3 = new HashSet<>();
                Set<Pair<QualifiedName, QualifiedHadMember>> successors3b = new HashSet<>();
                Set<Pair<QualifiedName, SpecializationOf>>successors4 = new HashSet<>();
                for (QualifiedName qn : allVars) {
                    if (key.equals(qn.getLocalPart())) {
                        successors1 = indexed.traverseDerivationsWithRelations(qn);  // TODO: make use of the successors/successor2 precalculated above.
                        successors2 = indexed.traverseAttributionsWithRelations(qn); // TODO: make use of the successors/successor2 precalculated above.
                        // note that for the client successor table, there is no need to distinguish qualitified/unqualified membership
                        successors3 = indexed.traverseReverseMembershipsWithRelations(qn);  // TODO: make use of the successors/successor2 precalculated above. // NOTE: Reverse relation
                        successors4 = indexed.traverseSpecializationsWithRelations(qn);  // TODO: make use of the successors/successor2 precalculated above. // NOTE: Reverse relation
                        break;
                    }
                }
                String initializer = "";
                List<Integer> rowValues=new LinkedList<>();
                boolean first = true;
                for (Pair<QualifiedName, WasDerivedFrom> successor : successors1) {
                    Integer i = index.get(successor.getLeft());
                    if (first) {
                        first = false;
                    } else {
                        initializer = initializer + ", ";
                    }
                    initializer = initializer + i + ", " + relationTypeNumber(successor.getRight()) + " /* " +  successor.getRight().getKind() + " */";
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor.getRight()));
                }
                for (Pair<QualifiedName, WasAttributedTo> successor2 : successors2) {
                    int i = index.get(successor2.getLeft());
                    if (first) {
                        first = false;
                    } else {
                        initializer = initializer + ", ";
                    }
                    initializer = initializer + i + ", " + relationTypeNumber(successor2.getRight()) + " /* " +  successor2.getRight().getKind() + " */";
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor2.getRight()));
                }
                for (Pair<QualifiedName, HadMember> successor3 : successors3) {
                    int i = index.get(successor3.getLeft());
                    if (first) {
                        first = false;
                    } else {
                        initializer = initializer + ", ";
                    }
                    initializer = initializer + i + ", " + relationTypeNumber(successor3.getRight()) + " /* " +  successor3.getRight().getKind() + " */";
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor3.getRight()));
                }
                for (Pair<QualifiedName, SpecializationOf> successor4 : successors4) {
                    int i = index.get(successor4.getLeft());
                    if (first) {
                        first = false;
                    } else {
                        initializer = initializer + ", ";
                    }
                    initializer = initializer + i + ", " + relationTypeNumber(successor4.getRight()) + " /* " +  successor4.getRight().getKind() + " */";
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor4.getRight()));
                }

                builder.addStatement("table.put($L,new int[] { " + initializer + "})", count);

                tableValues.put(count,rowValues);

            }

        }


        builder.addStatement("return table");


        MethodSpec method = builder.build();

        return Pair.of(method,tableValues);
    }


    public MethodSpec generateCommonMethod6static(IndexedDocument indexed) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getAllTypes")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.get(String[].class));

        compilerUtil.specWithComment(builder);

        Set<String> allTypes=new HashSet<>();

        for (StatementOrBundle sOrb: indexed.toDocument().getStatementOrBundle()) {
            if (sOrb instanceof HasType) {
                for (Type tp: ((HasType)sOrb).getType()) {
                    Object val=tp.getValue();
                    if (val instanceof QualifiedName) {
                        final QualifiedName qn = (QualifiedName) val;
                        if (!isVariable(qn)) allTypes.add(qn.getUri());
                    }
                }
            }
        }


        List<String> knownTypes = getCommonTypes0();

        List<String> sortedList = new ArrayList<>(allTypes);
        Collections.sort(sortedList);

        knownTypes.addAll(sortedList);

        builder.addStatement("String [] table = new String[$L]", knownTypes.size());
        int count=0;
        for (String s: knownTypes) {
            builder.addStatement("table[$L]=$S", count,s);
            count++;
        }


        builder.addStatement("return table");
        return builder.build();
    }

    public List<String> getCommonTypes0() {
        List<String> knownTypes = new ArrayList<>();
        knownTypes.add(NamespacePrefixMapper.PROV_NS + "Entity");
        knownTypes.add(NamespacePrefixMapper.PROV_NS + "Activity");
        knownTypes.add(NamespacePrefixMapper.PROV_NS + "Agent");
        return knownTypes;
    }


    public int relationTypeNumber(Relation rel) {
        return rel.getKind().ordinal();
    }


    public MethodSpec generateFactoryMethodWithBean(String template, String packge, TemplateBindingsSchema bindingsSchema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toBean")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(ClassName.get(packge,compilerUtil.commonNameClass(template)));

        compilerUtil.specWithComment(builder);


        Collection<String>variables=descriptorUtils.fieldNames(bindingsSchema);
        for (String key: variables) {
            String newkey = compilerUtil.generateNewNameForVariable(key);
            builder.addParameter(compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key), newkey);
        }

        builder.addStatement("$T $N=$N $T()",ClassName.get(packge,compilerUtil.commonNameClass(template)),BEAN_VAR, "new", ClassName.get(packge,compilerUtil.commonNameClass(template)));

        for (String key: variables) {
            String newkey= compilerUtil.generateNewNameForVariable(key);
            String statement = "$N.$N=$N";
            builder.addStatement(statement, BEAN_VAR, key, newkey);
        }


        builder.addStatement("return $N", BEAN_VAR);

        return builder.build();
    }


    public MethodSpec generateFactoryMethodToBeanWithArrayComposite(String toBean, String template, String packge, TemplateBindingsSchema bindingsSchema, String loggerPackage, String logger, BeanDirection direction, String extension, List<String> sharing) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(toBean)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge,compilerUtil.beanNameClass(template,direction)));
        compilerUtil.specWithComment(builder);

        if (extension!=null) {
            builder.addComment("Refers to variant $S, sharing variables $L", extension, sharing.toString());
        }


        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        builder.addParameter(listOfArrays, "records");


        builder.addStatement("$T record=records.get(0)", Object[].class);
        ClassName className = ClassName.get(packge, compilerUtil.beanNameClass(template,direction));
        builder.addStatement("$T bean=new $T()",className,className);

        int count = 1;
        for (String key: variables) {
            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
            final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);

            if (direction==BeanDirection.COMMON || descriptorUtils.isInput(key,bindingsSchema) || (sharing!=null) && sharing.contains(key)) {
                String comment="";
                if ((sharing!=null) && sharing.contains(key)) {
                    comment="/* shared */";
                }
                if (converter == null) {
                    String statement = "bean.$N=($T) record[" + count + "] $L";
                    builder.addStatement(statement, key, declaredJavaType, comment);
                } else {
                    String statement = "bean.$N=(record[" + count + "]==null)?null:((record[" + count + "] instanceof String)?$N((String)(record[" + count + "])):($T)(record[" + count + "])) $L";
                    builder.addStatement(statement, key, converter, declaredJavaType, comment);
                }
            }
            count++;
        }

        builder.addStatement("bean.$N=new $T<>()", ELEMENTS, LinkedList.class);
        builder.beginControlFlow("for (int i=1;i<records.size(); i++) ");
        if (extension==null) {
            builder.addStatement("bean.$N($T.simpleBeanConverters.get(records.get(i)[0]).apply(records.get(i)))",
                    ADD_ELEMENTS,
                    ClassName.get(loggerPackage, logger));
        } else {
            builder.addComment("this code will only work if there is a single variant for this template");
            builder.addStatement("bean.$N(toInputs$L(records.get(i)))",
                    ADD_ELEMENTS,
                    extension);
        }
        builder.endControlFlow();



        builder.addStatement("return $N", "bean");


        MethodSpec method = builder.build();

        return method;
    }
    public MethodSpec generateFactoryMethodToBeanWithArray(String toBean, String template, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection direction, String extension, List<String> shared) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(toBean)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge,compilerUtil.beanNameClass(template,direction,extension)));


        compilerUtil.specWithComment(builder);


        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        builder.addParameter(Object[].class, "record");

        ClassName className = ClassName.get(packge, compilerUtil.beanNameClass(template,direction,extension));
        builder.addStatement("$T $N=$N $T()", className, BEAN_VAR, "new", className);

        builder.addJavadoc("Converter to bean of type $T for template $N.\n", className, template);
        if (shared!=null) {
            builder.addJavadoc("Variant $N of class $T to support shared variables $N\n", extension, ClassName.get(packge,compilerUtil.beanNameClass(template,direction)), shared.toString());
        }
        builder.addJavadoc("@param record an array of objects\n");
        builder.addJavadoc("@return a bean\n");

        int count = 1;
        for (String key: variables) {
            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
            final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);

            if (direction==BeanDirection.COMMON
                    || descriptorUtils.isInput(key,bindingsSchema)
                    || (shared!=null && shared.contains(key))) {
                if (converter == null) {
                    String statement = "$N.$N=($T)$N[$L]";
                    builder.addStatement(statement, BEAN_VAR, key, declaredJavaType, "record", count);
                } else {
                    String statement = "$N.$N=($N[$L]==null)?null:((record[" + count + "] instanceof String)?$N((String)(record[" + count + "])):($T)(record[" + count + "]))";
                    builder.addStatement(statement, BEAN_VAR, key, "record", count, converter, declaredJavaType);
                }
            }
            count++;
        }
        builder.addStatement("return $N", BEAN_VAR);


        MethodSpec method = builder.build();

        return method;
    }


    public MethodSpec generateNewBean(String template, String packge) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("newBean")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge, compilerUtil.commonNameClass(template)));

        compilerUtil.specWithComment(builder);

        builder.addStatement("$T bean=new $T()", ClassName.get(packge, compilerUtil.commonNameClass(template)), ClassName.get(packge, compilerUtil.commonNameClass(template)));


        builder.addStatement("return bean");

        MethodSpec method = builder.build();

        return method;
    }




    public MethodSpec generateExamplarBean(String template, String packge, TemplateBindingsSchema bindingsSchema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("examplar")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(ClassName.get(packge, compilerUtil.commonNameClass(template)));

        compilerUtil.specWithComment(builder);

        builder.addStatement("$T bean=new $T()", ClassName.get(packge, compilerUtil.commonNameClass(template)), ClassName.get(packge, compilerUtil.commonNameClass(template)));

        Map<String, List<Descriptor>> theVars = bindingsSchema.getVar();
        //Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        Collection<String> nameVariables = descriptorUtils.getNameVariables(bindingsSchema);
        Collection<String> attrVariables = descriptorUtils.getAttributeVariables(bindingsSchema);


        for (String aVar : nameVariables) {
            List<Descriptor> descriptors = theVars.get(aVar);
            Descriptor qDescriptor = (descriptors == null) ? null : descriptors.get(0);
            String idType = (qDescriptor == null) ? null : descriptorUtils.getFromDescriptor(qDescriptor, AttributeDescriptor::getType, NameDescriptor::getType);
            Object examplar = (qDescriptor == null) ? null : descriptorUtils.getFromDescriptor(qDescriptor, AttributeDescriptor::getExamplar, NameDescriptor::getExamplar);


            if (idType == null) {
                builder.addStatement("bean.$N=$S", aVar, "example_" + aVar);
            } else {
                String example = (examplar == null) ? compilerUtil.generateExampleForType(idType, aVar, pFactory) : examplar.toString();
                Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(theVars, aVar);

                final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
                if (converter == null) {
                    builder.addStatement("bean.$N=$S", aVar, example);
                } else {
                    builder.addStatement("bean.$N=$N($S)", aVar, converter, example);
                }
            }


        }


        for (String aVar : attrVariables) {

            String declaredType = null;
            Class<?> declaredJavaType = null;
            Object examplar = null;

            Descriptor qDescriptor = null;

            declaredType = compilerUtil.getDeclaredType(theVars, aVar);
            declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(theVars, aVar);
            List<Descriptor> descriptors = theVars.get(aVar);
            qDescriptor = (descriptors == null) ? null : descriptors.get(0);
            examplar = (qDescriptor == null) ? null : descriptorUtils.getFromDescriptor(qDescriptor, AttributeDescriptor::getExamplar, NameDescriptor::getExamplar);


            if (qDescriptor != null) { // only generate code if there is a descriptor!

                String example = (examplar != null) ? examplar.toString() : compilerUtil.generateExampleForType(declaredType, aVar, pFactory);

                final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
                if (converter == null) {
                    builder.addStatement("bean.$N=$S", aVar, example);
                } else {
                    builder.addStatement("bean.$N=$N($S)", aVar, converter, example);
                }
            }
        }


        builder.addStatement("return bean");

        return builder.build();

    }


    public SpecificationFile generateSQLInterface(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit("SQL");


        MethodSpec.Builder builder2 = MethodSpec.methodBuilder("getSQLInsert")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder2.build());


        MethodSpec.Builder builder3 = MethodSpec.methodBuilder("getSQLInsertStatement")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder3.build());


        TypeSpec theInterface = builder.build();

        String myPackage=locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theInterface, configs, myPackage, stackTraceElement);
        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);
    }

    //move to expansion subpackage
    public MethodSpec commonAccessorGenerator(String templateName, String packge) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getClientBuilder")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(Constants.CLIENT_PACKAGE, Constants.BUILDER));

        compilerUtil.specWithComment(builder);

        builder.addStatement("return new $T()", ClassName.get(packge,compilerUtil.templateNameClass(templateName)));

        return builder.build();

    }

    public MethodSpec typedRecordGenerator(String templateName, String packge) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getTypedRecord")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge,compilerUtil.templateNameClass(templateName)+"TypedRecord"));

        compilerUtil.specWithComment(builder);


        builder.addStatement("return new $T()", ClassName.get(packge,compilerUtil.templateNameClass(templateName)+"TypedRecord"));

        return builder.build();

    }
}
