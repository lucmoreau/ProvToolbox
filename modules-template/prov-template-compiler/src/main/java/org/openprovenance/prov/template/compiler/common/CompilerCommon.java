package org.openprovenance.prov.template.compiler.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.apache.commons.lang.StringEscapeUtils;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.template.compiler.CompilerSQL;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.emitter.Poet;
import org.openprovenance.prov.template.compiler.past.type.ArrayType;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;
import org.openprovenance.prov.template.descriptors.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.Class;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.openprovenance.prov.model.StatementOrBundle.ALL_RELATIONS;
import static org.openprovenance.prov.template.compiler.past.ArrayAccessor.ARRAY_ACCESSOR;
import static org.openprovenance.prov.template.compiler.past.ArrayAllocator.ARRAY_ALLOCATOR;
import static org.openprovenance.prov.template.compiler.past.ArrayInitialiser.ARRAY_INITIALISER;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.*;
import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.Constant.*;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.ForLoop.FOR;
import static org.openprovenance.prov.template.compiler.past.IfExpression.IFEXPRESSION;
import static org.openprovenance.prov.template.compiler.past.IfExpression.IF_;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Iterator.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.CompilerBeanGenerator.newSpecificationFiles;
import static org.openprovenance.prov.template.compiler.CompilerUtil.*;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.Statement.StatementKind.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;
import static org.openprovenance.prov.template.core.InstantiateUtil.isVariable;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;

public class CompilerCommon {
    public static final String SB_VAR = "sb";
    public static final String SELF_VAR = "self";
    public static final String MARKER_LAMBDA_END = "/*#lend#*/";
    public static final String MARKER_LAMBDA_BODY = "/*#lbody#*/";
    public static final String MARKER_LAMBDA = "/*#lambda#*/";
    public static final String MARKER_PARAMS = "/*#params#*/";

    public static final String MARKER_ARRAY = "/*#array#*/";
    public static final String UNKNOWN = "unknown";
    public static final String POST_PROCESSING_VAR = "postProcessing";
    public static final String TABLE_VAR = "table";

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

    public Pair<SpecificationFile, Map<Integer, List<Integer>>> generateCommonLib(TemplatesProjectConfiguration configs, Locations locations, Document doc, String name, String templateName, String templateFullyQualifiedName, String packageName, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed, BeanKind beanKind, String fileName, String consistsOf) {


        Bundle bun=u.getBundle(doc).get(0);

        Set<QualifiedName> allVars=new HashSet<>();
        Set<QualifiedName> allAtts=new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);


        return generateCommonLib_aux(configs, locations, allVars, name, templateName, templateFullyQualifiedName, packageName, bindingsSchema, indexed, beanKind, fileName, consistsOf);

    }

    PastFactory pastFactory=new PastFactory();


    Pair<SpecificationFile, Map<Integer, List<Integer>>> generateCommonLib_aux(TemplatesProjectConfiguration configs, Locations locations, Set<QualifiedName> allVars, String name, String templateName, String templateFullyQualifiedName, String packageName, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed, BeanKind beanKind, String fileName, String consistsOf) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();



        org.openprovenance.prov.template.compiler.past.Class pastClass=pastFactory
                .CLASS(name)
                .INTERFACES(
                        org.openprovenance.prov.template.compiler.past.type.ClassName.get(BUILDER, CLIENT_PACKAGE),
                        org.openprovenance.prov.template.compiler.past.type.ClassName.get("SQL", CLIENT_PACKAGE))
                .MODIFIERS(Modifier.PUBLIC)
                .COMMENT("Builder class for $N", templateName);

        Map<Integer, List<Integer>> successorTable=null;


        pastClass.METHOD(generateNameAccessor(templateName));
        pastClass.METHOD(generateFullyQualifiedNameAccessor(templateFullyQualifiedName));
        pastClass.METHOD(generateTemplateNameAccessor(templateFullyQualifiedName,locations));
        pastClass.METHOD(generateCBindingsAccessor(templateFullyQualifiedName,locations));
        pastClass.METHOD(generatePropertyOrderMethod());
        pastClass.FIELDS(generateFieldPropertyOrder(bindingsSchema));
        pastClass.METHOD(generateLoggerMethod_new(templateName, templateFullyQualifiedName, bindingsSchema));
        pastClass.METHOD(generateCommonCSVConverterMethod_aux_new(locations, name, templateName, compilerUtil.loggerName(templateName), packageName, bindingsSchema, beanKind, consistsOf, locations.getFilePackage(configs.name,LOGGER), LOGGER));
        pastClass.FIELDS(generateField4aArgs2CsvConverter_new(name,templateName,packageName));
        pastClass.METHOD(generateCommonMethodGetNodes2(beanKind));
        pastClass.METHOD(generateCommonMethodGetSuccessors(beanKind));
        pastClass.METHOD(generateCommonMethodGetTypedSuccessors(beanKind));
        pastClass.METHOD(generateNewBean(templateName, packageName));
        pastClass.METHOD(generateRecordCsvProcessorMethod(beanKind));
        pastClass.METHOD(generateIsCompositeOfMethod(consistsOf,beanKind));


        // sql
        compilerSQL.generateSQLstatements(pastClass, templateName, bindingsSchema, beanKind);
        pastClass.METHOD(generateCommonMethodGetForeign(beanKind));

        if (beanKind==BeanKind.SIMPLE) {
            pastClass.METHOD(generateCommonMethod4static(allVars, bindingsSchema, indexed));
            pastClass.FIELDS(generateFieldForeignTables(bindingsSchema));
            pastClass.METHOD(generateApplyMethod(templateName, packageName));
            pastClass.METHOD(generateGetNodeStatic(bindingsSchema));

            successorTable=getTypedSuccessors(allVars, bindingsSchema, indexed);

            pastClass.METHOD(generateGetTypedSuccessorsStatic(successorTable));
            pastClass.METHOD(generateFactoryMethodToBeanWithArray_new(locations,"record2bean", templateName, packageName, bindingsSchema, BeanDirection.COMMON, null, null));
            pastClass.METHOD(generateArgsToRecordMethod(templateName, templateFullyQualifiedName, packageName, bindingsSchema));
            pastClass.METHOD(generateGetRelations(allVars, bindingsSchema, indexed));
            pastClass.METHOD(generateGetAllTypesMethodStatic(indexed));

            pastClass.FIELDS(
                    FIELD("__successors", MAP_INTEGER_INTARRAY)
                            .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .INITIALIZER(METHOD_CALL(METHOD_GET_SUCCESSORS, List.of())),

                    FIELD("__successors2", MAP_INTEGER_INTARRAY)
                            .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .INITIALIZER(METHOD_CALL(METHOD_GET_TYPED_SUCCESSORS, List.of())),

                    FIELD(__NODES_FIELD, intArray)
                            .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .INITIALIZER(METHOD_CALL(METHOD_GET_NODES, List.of())),

                    FIELD("__relations", MAP_STRING_MAP_STRING_INTARRAY)
                            .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .INITIALIZER(METHOD_CALL(METHOD_GET_RELATIONS, List.of())),

                    FIELD("allTypes", STRING_ARRAY)
                            .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .INITIALIZER(METHOD_CALL("__getAllTypes", List.of()))
            );

            pastClass.METHOD(generateProcessorConverter(templateName, packageName, bindingsSchema, BeanDirection.COMMON));
            pastClass.METHOD(generateOutputsMethod());
            pastClass.METHOD(generateInputsMethod());
            pastClass.METHOD(generateCompulsoryInputsMethod());

            pastClass.FIELDS(generateFieldOutputs(bindingsSchema));
            pastClass.FIELDS(generateFieldInputs(bindingsSchema));
            pastClass.FIELDS(generateFieldCompulsoryInputs(bindingsSchema));

            pastClass.METHOD(generateExamplarBean(templateName, packageName, bindingsSchema));

            pastClass.FIELDS(generateField4aBeanConverter2("record2bean", templateName,packageName, Constants.A_RECORD_BEAN_CONVERTER, BeanDirection.COMMON));
            pastClass.FIELDS(generateFieldRecord2CsvConverter(name,templateName,packageName));

            pastClass.METHOD(generateProcessorConverter2(templateName, packageName, bindingsSchema));
            pastClass.METHOD(generateFactoryMethodWithBean(templateName, packageName, bindingsSchema));
            pastClass.FIELDS(generateField4aBeanConverter(templateName,packageName, bindingsSchema));

            // SQL parts
            pastClass.METHOD(generateBeanToSqlConversionMethod(name, templateName, compilerUtil.loggerName(templateName), packageName, bindingsSchema));
            pastClass.METHOD(compilerSQL.generateSqlTupleMethod(templateName, bindingsSchema));
            pastClass.FIELDS(generateFieldBeanConverter(name,templateName,packageName));
            pastClass.FIELDS(generateField4aRecord2SqlConverter(templateName));


        } else {
            pastClass.METHOD(generateNullOutputsMethod());
            pastClass.METHOD(generateNullInputsMethod());
            pastClass.FIELDS(generateField4aArgs2Records(name,templateName,packageName));
            pastClass.METHOD(generateArgsToRecordMethodComposite(locations, templateName, packageName, compilerUtil.loggerName(templateName), bindingsSchema, consistsOf, locations.getFilePackage(configs.name,LOGGER), LOGGER));

        }

        TypeSpec.Builder builder = new Poet().emitBuilder(pastClass);


        if (configs.integrator) {
            ClassName integratorClassName = ClassName.get(locations.getBeansPackage(templateFullyQualifiedName, BeanDirection.INPUTS), compilerUtil.integratorBuilderNameClass(templateName));
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


        } else {
            builder.addField(generateField4aBeanConverter3("toBean", templateName, packageName, A_RECORD_BEAN_CONVERTER, BeanDirection.COMMON));
            builder.addMethod(generateFactoryMethodToBeanWithArrayComposite("toBean", templateName, packageName, bindingsSchema, locations.getFilePackage(configs.name,LOGGER), LOGGER, BeanDirection.COMMON, null, null));
            //builder.addMethod(generateNullOutputsMethod());
            //builder.addMethod(generateNullInputsMethod());

            //builder.addMethod(generateArgsToRecordMethodComposite(locations, templateName, packageName, compilerUtil.loggerName(templateName), bindingsSchema, consistsOf, locations.getFilePackage(configs.name,LOGGER), LOGGER));
           //builder.addField(generateField4aArgs2Records(name,templateName,packageName));
            //builder.addMethod(generateNewBean(templateName, packageName));

            //builder.addField(generateField4aArgs2CsvConverter(name,templateName,packageName));
            //builder.addMethod(generateCommonMethod2PureCsv(templateName, templateFullyQualifiedName, bindingsSchema, consistsOf));
            //builder.addMethod(generateCommonCSVConverterMethod_aux(locations, name, templateName, compilerUtil.loggerName(templateName), packageName, bindingsSchema, beanKind, consistsOf, locations.getFilePackage(configs.name,LOGGER), LOGGER));

        }
        
        //builder.addMethod(generateIsCompositeOfMethod(consistsOf,beanKind));


        String directory = locations.convertToDirectory(packageName);


        TypeSpec bean=builder.build();

        try {
            new org.openprovenance.prov.template.compiler.past.emitter.Python()
                    .toWritableObject(pastClass, templateName, packageName, stackTraceElement)
                    .writeTo(new File("target/python"));
        } catch (IOException|RuntimeException e) {
            e.printStackTrace();
            try {
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(System.err, pastClass);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }


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

    private Method generateArgsToRecordMethodComposite(Locations locations,
                                                           String templateName,
                                                           String packageName,
                                                           String loggerName,
                                                           TemplateBindingsSchema bindingsSchema,
                                                           String consistsOf,
                                                           String loggerPackage,
                                                           String logger) {

        final ParameterizedType processorClassName = processorClassType(templateName, packageName,OBJECT_ARRAY_ARRAY);
        final TypeName processorClassNameNotParametrised = processorClassTypeNotParametrised(templateName, packageName);

        Method method = METHOD(ARGS_2_RECORDS)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(processorClassName);

        compilerUtil.debugFileLocation(method);

        //builder.addStatement("$T $N=$N", ClassName.get(packageName,compilerUtil.processorNameClass(templateName)), SELF_VAR, "this");
        Map<String, List<Descriptor>> var = bindingsSchema.getVar();



        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);




        String shortConsistsOf=locations.getShortNames().get(consistsOf);

        Collection<String> actualVariables = new LinkedList<>(variables);
        actualVariables.add(ELEMENTS);
        String beanNameClass = compilerUtil.beanNameClass(shortConsistsOf, BeanDirection.COMMON);
        org.openprovenance.prov.template.compiler.past.type.ClassName className = get(beanNameClass, packageName);
        ParameterizedType listBeanType=ParameterizedType.get(LIST, className);
        List<Parameter> paramsList= makeParamsListComposite2(actualVariables, var, compilerUtil, listBeanType);

        ParameterizedType parametericInterface=ParameterizedType.get(get(compilerUtil.processorNameClass(shortConsistsOf),packageName), OBJECT_ARRAY);

        LambdaExpression lambda=LAMBDA(paramsList)
                .BODY(
                        ASSIGNMENT(OBJECT_ARRAY_ARRAY, VARIABLE("_result"), ARRAY_ALLOCATOR(OBJECT_ARRAY, BINARY_OP(METHOD_CALL(VARIABLE(Constants.GENERATED_VAR_PREFIX + ELEMENTS), "size", List.of()), "+", CONSTANT(1)))),

                        ASSIGNMENT(_int, VARIABLE("_i_"), CONSTANT(1)),

                        ASSIGNMENT(null, ARRAY_ACCESSOR(VARIABLE("_result"),CONSTANT(0)),
                                ARRAY_INITIALISER(OBJECT, List.of(CONSTANT("compositeThingie"),
                                        METHOD_CALL(VARIABLE(Constants.GENERATED_VAR_PREFIX + ELEMENTS), "size", List.of()),
                                        Constant.getNull()))),

                        ITERATOR(Parameter.PARAMETER(VAR_ELEMENT,className), VARIABLE(Constants.GENERATED_VAR_PREFIX + ELEMENTS))
                                .BODY(
                                        ASSIGNMENT(
                                                parametericInterface,
                                                VARIABLE("processor"),
                                                METHOD_CALL(
                                                        METHOD_CALL(get(logger,loggerPackage),
                                                                Constants.GENERATED_VAR_PREFIX + shortConsistsOf),
                                                        Constants.ARGS2RECORD_CONVERTER,
                                                        List.of()
                                                )
                                        ),

                                        ASSIGNMENT(
                                                OBJECT_ARRAY,
                                                VARIABLE(VAR_OBJECTS),
                                                METHOD_CALL(
                                                        VARIABLE(VAR_ELEMENT),
                                                        "process",
                                                        List.of(VARIABLE("processor"))
                                                )
                                        ),

                                        ASSIGNMENT(
                                                null,
                                                ARRAY_ACCESSOR(VARIABLE("_result"), VARIABLE("_i_")),
                                                VARIABLE(VAR_OBJECTS)
                                        ),

                                        ASSIGNMENT(
                                                null,
                                                VARIABLE("_i_"),
                                                BINARY_OP(VARIABLE("_i_"), "+", CONSTANT(1))
                                        )
                                ),

                        RETURN(VARIABLE("_result"))
                );

        method.BODY(RETURN(lambda));

        /*
        //lambda.add("($L) -> $N {\n", paramsList, MARKER_LAMBDA_BODY).indent();
        ClassName loggerClassName = ClassName.get(loggerPackage, logger);


       // lambda.addStatement("$T $N=new Object[$N.size()+1][]", ArrayTypeName.of(Object[].class), "_result", Constants.GENERATED_VAR_PREFIX + ELEMENTS);

        //String[] variableArray = variables.toArray(new String[]{});

        //lambda.addStatement("int _i_=1");

        //
        // lambda.addStatement("$N[0]=new Object[] {$S, $N.size(), null}", "_result", "compositeThingie", Constants.GENERATED_VAR_PREFIX + ELEMENTS);

        lambda.beginControlFlow("for ($T $N: $N) ", ClassName.get(packageName,beanNameClass), VAR_ELEMENT, Constants.GENERATED_VAR_PREFIX + ELEMENTS);

        ParameterizedTypeName parametericInterface=ParameterizedTypeName.get(ClassName.get(packageName,compilerUtil.processorNameClass(shortConsistsOf)), TypeName.get(Object[].class));
        ParameterizedTypeName parametericInterface2=ParameterizedTypeName.get(ClassName.get(packageName,compilerUtil.processorNameClass(consistsOf)), TypeVariableName.get("?"));

        lambda.addStatement("$T $N=$T.$N.$N()",
                parametericInterface,
                "processor",
                loggerClassName,
                Constants.GENERATED_VAR_PREFIX + shortConsistsOf,
                Constants.ARGS2RECORD_CONVERTER);

        lambda.addStatement("// the following line generates ts error: Untyped function calls may not accept type arguments.");

        lambda.addStatement("$T $N=$N.$N($N)",
                Object[].class,
                VAR_OBJECTS,
                VAR_ELEMENT,
                "process",
                "processor");

        lambda.addStatement("$N[_i_]= $N", "_result", VAR_OBJECTS);
        lambda.addStatement("_i_++");
        lambda.endControlFlow();
        lambda.addStatement("return $N", "_result");
        lambda.unindent().add("}; $N", MARKER_LAMBDA_END);


        // note, poet builder does not accept nested statement codeblock, so instead of adding a statement, we add a codeblock
        builder.addCode("return $N $L", MARKER_LAMBDA, lambda.build());


         */



        return method;

    }

    private Method generateIsCompositeOfMethod(String consistsOf, BeanKind beanKind) {
        Method method = METHOD("isCompositeOf")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING);
        compilerUtil.debugFileLocation(method);

        if (beanKind==BeanKind.COMPOSITE) {
            method.BODY(RETURN(CONSTANT(consistsOf)));
        } else {
            method.BODY(RETURN(Constant.getNull()));
        }
        return method;
    }

    public Method generateNameAccessor(String templateName) {
        Method method = METHOD(GET_NAME)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(CONSTANT(templateName)));
        return method;
    }

    public Method generateFullyQualifiedNameAccessor(String fullyQualifiedTemplateName) {
        Method method = METHOD(GET_FULLY_QUALIFIED_NAME)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(CONSTANT(fullyQualifiedTemplateName)));
        return method;
    }

    public Method generateTemplateNameAccessor(String fullyQualifiedTemplateName, Locations locations) {
        Method method = METHOD(GET_TEMPLATE_NAME)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(CONSTANT(locations.getTemplateRegistrations().get(fullyQualifiedTemplateName))));
        return method;
    }
    public Method generateCBindingsAccessor(String fullyQualifiedTemplateName, Locations locations) {
        Method method = METHOD(GET_CBINDINGS)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(CONSTANT(locations.getCbindingsRegistrations().get(fullyQualifiedTemplateName))));
        return method;
    }
    public Method generatePropertyOrderMethod() {
        Method method = METHOD(PROPERTY_ORDER_METHOD)
                .COMMENT("Null method for composite\n@return the array of properties in order\n")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING_ARRAY);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(VARIABLE(PROPERTY_ORDER, FIELD_VARIABLE)));
        return method;
    }

    private Field generateFieldPropertyOrder(TemplateBindingsSchema bindingsSchema) {
        Field fbuilder=FIELD(PROPERTY_ORDER, STRING_ARRAY)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        fbuilder.COMMENT("Generated by method $N", getClass().getName()+".generateFieldPropertyOrder()");
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        fbuilder.INITIALIZER(ARRAY_INITIALISER(STRING, makeConstantSequence(IS_A,variables)));
        return fbuilder;
    }

    public static List<Expression> makeVariableSequence(String head, Collection<String> variables) {
        List<String> variables2=new LinkedList<>();
        if (head!=null) variables2.add(head);
        variables2.addAll(variables);
        return variables2.stream().map(v -> VARIABLE(v, FIELD_VARIABLE)).collect(Collectors.toList());
    }

    public static List<Expression> makeConstantSequence(String head, Collection<String> variables) {
        List<String> variables2=new LinkedList<>();
        if (head!=null) variables2.add(head);
        variables2.addAll(variables);
        return variables2.stream().map(Constant::new).collect(Collectors.toList());

    }

    private Field generateField4aArgs2CsvConverter_new(String name, String templateName, String packge) {
        final org.openprovenance.prov.template.compiler.past.type.TypeName processorClassName = processorClassType(templateName, packge, STRING);
        Field field=FIELD(A_ARGS_CSV_CONVERTER, processorClassName)
                .MODIFIERS(Modifier.FINAL, Modifier.PUBLIC)
                .COMMENT("Generated by method $N", getClass().getName()+".generateField4aArgs2CsvConverter()")
                .INITIALIZER(METHOD_CALL(VARIABLE("this"), ARGS_CSV_CONVERSION_METHOD,List.of()));
        return field;
    }




    public final ParameterizedType functionObjArrayTo (org.openprovenance.prov.template.compiler.past.type.TypeName returnType) {
        return ParameterizedType.get(FUNCTION, OBJECT_ARRAY, returnType);
    }


    public Method generateCommonCSVConverterMethod_aux_new(Locations locations, String name, String template, String loggerName, String packge, TemplateBindingsSchema bindingsSchema, BeanKind beanKind, String consistsOf, String loggerPackage, String logger) {
        final org.openprovenance.prov.template.compiler.past.type.TypeName processorClassName = processorClassType(template, packge,STRING);
        final org.openprovenance.prov.template.compiler.past.type.TypeName processorClassNameNotParametrised = processorClassTypeNotParametrised2(template, packge);
        Method method = METHOD(ARGS_CSV_CONVERSION_METHOD)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(processorClassName);
        compilerUtil.debugFileLocation(method);

        method.COMMENT(loggerName + " client side logging method\n");
        method.COMMENT("@return $T\n" , processorClassNameNotParametrised);


        method.BODY(ASSIGNMENT(get(name,packge), VARIABLE(SELF_VAR), VARIABLE("this")));

        Map<String, List<Descriptor>> var = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        Collection<String> actualVariables;
        List<Parameter> paramsList;

        String shortConsistsOf=null;

        if (beanKind==BeanKind.COMPOSITE) {
            shortConsistsOf=locations.getShortNames().get(consistsOf);

            actualVariables = new LinkedList<>(variables);
            actualVariables.add(ELEMENTS);
            String beanNameClass = compilerUtil.beanNameClass(shortConsistsOf, BeanDirection.COMMON);
            ParameterizedType listBeanType=ParameterizedType.get(LIST, org.openprovenance.prov.template.compiler.past.type.ClassName.get(beanNameClass,packge));
            paramsList= makeParamsListComposite2(actualVariables, var, compilerUtil, listBeanType);
        } else {
            actualVariables=variables;
            paramsList= makeParamsList2(actualVariables, var, compilerUtil);
        }

        LambdaExpression lambda= LAMBDA(paramsList);
        List<Expression> argsList = makeRenamedArgsLocalVariableList(SB_VAR,variables);

        lambda.BODY(ASSIGNMENT(STRING_BUILDER, VARIABLE(SB_VAR), CONSTRUCTOR_CALL(STRING_BUILDER, List.of())),

                METHOD_CALL(VARIABLE(SELF_VAR), loggerName, argsList));



        if (consistsOf!=null) {
            String[] variableArray = variables.toArray(new String[]{});


            String beanNameClass = compilerUtil.beanNameClass(shortConsistsOf, BeanDirection.COMMON);
            org.openprovenance.prov.template.compiler.past.type.ClassName loggerClassName = get(logger, loggerPackage);

            ParameterizedType parametericInterface=ParameterizedType.get(get(compilerUtil.processorNameClass(shortConsistsOf),packge), OBJECT_ARRAY);
            ParameterizedType processorOfString = functionObjArrayTo(STRING);

            // Does not convert well to JS with JSweet
            // lambda.beginControlFlow("for ($T $N: $N) ", ClassName.get(packge,beanNameClass), VAR_ELEMENT, Constants.GENERATED_VAR_PREFIX + ELEMENTS);
            // instead:

            lambda.BODY(
                    ASSIGNMENT(parametericInterface, VARIABLE("processor"),
                            METHOD_CALL(METHOD_CALL(loggerClassName, GENERATED_VAR_PREFIX + shortConsistsOf), ARGS2RECORD_CONVERTER, List.of())),
                    FOR(
                            ASSIGNMENT(_int, VARIABLE(_I_), CONSTANT(0)),
                            BINARY_OP(VARIABLE(_I_), BinaryOp.LT, METHOD_CALL(VARIABLE(GENERATED_VAR_PREFIX + ELEMENTS), "size", List.of())),
                            ASSIGNMENT(null, VARIABLE(_I_), BINARY_OP(VARIABLE(_I_), "+", CONSTANT(1))))

                            .BODY(
                                    ASSIGNMENT(org.openprovenance.prov.template.compiler.past.type.ClassName.get(beanNameClass,packge),VARIABLE(VAR_ELEMENT),
                                         METHOD_CALL(VARIABLE(GENERATED_VAR_PREFIX + ELEMENTS),"get", List.of(VARIABLE(_I_)))),



                                   new Comment("// the following line generates ts error: Untyped function calls may not accept type arguments."),


                                    ASSIGNMENT(OBJECT_ARRAY, VARIABLE(VAR_OBJECTS),
                                            METHOD_CALL(VARIABLE(VAR_ELEMENT), "process", List.of(VARIABLE("processor")))),


                                    ASSIGNMENT(processorOfString, VARIABLE(VAR_CSV_CONVERTER),
                                            METHOD_CALL(METHOD_CALL(loggerClassName, "simpleCSvConverters"),
                                                    "get", List.of(VARIABLE(GENERATED_VAR_PREFIX + variableArray[2])))),

                                    ASSIGNMENT(STRING, VARIABLE(VAR_CSV),
                                            METHOD_CALL(VARIABLE(VAR_CSV_CONVERTER), "apply", List.of(VARIABLE(VAR_OBJECTS)))),

                                    METHOD_CALL(
                                            METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("\\n"))),
                                            "append",
                                            List.of(VARIABLE(VAR_CSV)))

                            ));
        }


        lambda.BODY(RETURN(METHOD_CALL(VARIABLE(SB_VAR), "toString", List.of())));
        // note, poet method does not accept nested statement codeblock, so instead of adding a statement, we add a codeblock
        method.BODY(RETURN(lambda));


        return method;
    }

    private TypeName processorClassTypeNotParametrised(String template, String packge) {
        return ClassName.get(packge,compilerUtil.processorNameClass(template));
    }

    public static List<Parameter>  makeParamsList2(Collection<String> variables, Map<String, List<Descriptor>> theVars, CompilerUtil compilerUtil) {
        return variables
                .stream()
                .filter((v) -> theVars.containsKey(v) && theVars.get(v)!=null)
                .map(variable ->
                        PARAMETER(GENERATED_VAR_PREFIX + variable,compilerUtil.getPastTypeForDeclaredType(theVars, variable)))
                .collect(Collectors.toList());
    }
    public static CodeBlock makeParamsListComposite(Collection<String> variables, Map<String, List<Descriptor>> var, CompilerUtil compilerUtil, ParameterizedTypeName listBeanType) {
        return CodeBlock.join(variables.stream().map(variable ->
                CodeBlock.of("$T $N", (variable.equals(ELEMENTS)? listBeanType: compilerUtil.getJavaTypeForDeclaredType(var, variable)), Constants.GENERATED_VAR_PREFIX + variable)).collect(Collectors.toList()), ", ");
    }

    public static List<Parameter> makeParamsListComposite2(Collection<String> variables, Map<String, List<Descriptor>> var, CompilerUtil compilerUtil, ParameterizedType listBeanType) {
        return variables
                .stream()
                .map(variable ->
                        PARAMETER(GENERATED_VAR_PREFIX + variable,(variable.equals(ELEMENTS) ? listBeanType : compilerUtil.getPastTypeForDeclaredType(var, variable))))
                .collect(Collectors.toList());
    }


    public static CodeBlock makeRenamedArgsList(String head, Collection<String> variables) {
        List<String> variables2=new LinkedList<>();
        if (head!=null) variables2.add(head);
        variables2.addAll(variables);
        return CodeBlock.join(variables2.stream().map(variable -> CodeBlock.of("$N", (variable.equals(head)?variable: Constants.GENERATED_VAR_PREFIX + variable))).collect(Collectors.toList()), ", ");
    }

    public static List<Expression> makeRenamedArgsList2(String head, Collection<String> variables) {
        List<String> variables2=new LinkedList<>();
        if (head!=null) variables2.add(head);
        variables2.addAll(variables);
        return variables2
                .stream()
                .map(variable -> variable.equals(head)? VARIABLE(variable): VARIABLE(GENERATED_VAR_PREFIX + variable )).collect(Collectors.toList());
    }
    public static List<Expression> makeRenamedArgsLocalVariableList(String head, Collection<String> variables) {
        List<String> variables2=new LinkedList<>();
        if (head!=null) variables2.add(head);
        variables2.addAll(variables);
        return variables2
                .stream()
                .map(variable -> variable.equals(head)? VARIABLE(variable): VARIABLE(GENERATED_VAR_PREFIX + variable )).collect(Collectors.toList());
    }

    public static CodeBlock makeArgsList(Collection<String> variables) {
        return CodeBlock.join(variables.stream().map(variable -> CodeBlock.of("$N", variable)).collect(Collectors.toList()), ", ");
    }
    public static List<Expression> makeConstantStringSequence(String head, Collection<String> variables) {
        List<String> variables2=new LinkedList<>();
        if (head!=null) variables2.add(head);
        variables2.addAll(variables);
        return variables2.stream().map(Constant::CONSTANT).collect(Collectors.toList());
    }

    private Field generateField4aBeanConverter(String templateName, String packge, TemplateBindingsSchema bindingsSchema) {
        org.openprovenance.prov.template.compiler.past.type.TypeName myType=processorClassType(templateName,packge,get(compilerUtil.commonNameClass(templateName),packge));

        Map<String, List<Descriptor>> var = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<Parameter> paramsList= makeParamsList2(variables, var, compilerUtil);
        List<Expression> argsList=makeRenamedArgsList2(null,variables);

        return FIELD(A_ARGS_BEAN_CONVERTER,myType)
                .MODIFIERS(Modifier.FINAL, Modifier.PUBLIC).INITIALIZER(
                        LAMBDA(paramsList)
                                .BODY(RETURN(METHOD_CALL(VARIABLE("this"), ARGS_2_BEAN, argsList))));
    }

    private Field generateFieldOutputs(TemplateBindingsSchema bindingsSchema) {
        Field field=FIELD(OUTPUTS, STRING_ARRAY).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<String> outputs=variables.stream().filter(variable->descriptorUtils.isOutput(variable,bindingsSchema)).collect(Collectors.toList());
        field.INITIALIZER(ARRAY_INITIALISER(STRING, makeConstantStringSequence(null,outputs)));
        return field;
    }
    private Field generateFieldInputs(TemplateBindingsSchema bindingsSchema) {
        Field field=FIELD(INPUTS, STRING_ARRAY).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<String> inputs=variables.stream().filter(variable->descriptorUtils.isInput(variable,bindingsSchema)).collect(Collectors.toList());
        field.INITIALIZER(ARRAY_INITIALISER(STRING, makeConstantStringSequence(null,inputs)));
        return field;
    }

    private Field generateFieldCompulsoryInputs(TemplateBindingsSchema bindingsSchema) {
        Field field=FIELD(COMPULSORY_INPUTS, STRING_ARRAY).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<String> compulsoryInputs=variables.stream().filter(variable->descriptorUtils.isCompulsoryInput(variable,bindingsSchema)).collect(Collectors.toList());
        field.INITIALIZER(ARRAY_INITIALISER(STRING, makeConstantStringSequence(null,compulsoryInputs)));
        return field;
    }

    private Field generateFieldForeignTables(TemplateBindingsSchema bindingsSchema) {
        Field field=FIELD( Constants.FOREIGN_TABLES, STRING_ARRAY)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .COMMENT("Generated by method $N", getClass().getName()+".generateFieldForeignTables()");
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<String> foreignTables=variables.stream().map(variable->descriptorUtils.getSqlTable(variable,bindingsSchema).orElse(null)).collect(Collectors.toList());
        field.INITIALIZER(ARRAY_INITIALISER(STRING,makeConstantStringSequence(IS_A,foreignTables)));
        return field;
    }



    public FieldSpec generateField4aBeanConverter3(String toBean, String templateName, String packge, String fieldName, BeanDirection direction) {
        TypeName myType=functionListObjArrayTo(ClassName.get(packge,compilerUtil.beanNameClass(templateName, direction)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, fieldName,Modifier.FINAL, Modifier.PUBLIC);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateField4aBeanConverter3()");
        fbuilder.initializer(" ($T records) -> { return $N(records); }", listOfArrays, toBean);
        return fbuilder.build();
    }

    public Field generateField4aBeanConverter2(String toBean, String templateName, String packge, String fieldName, BeanDirection direction) {
        ParameterizedType myType= FUNCTION_OBJARRAY_TO_TYPE(get(compilerUtil.beanNameClass(templateName, direction),packge));
        Field fbuilder=FIELD(fieldName, myType).MODIFIERS(Modifier.FINAL, Modifier.PUBLIC);
        fbuilder.COMMENT("Generated by method $N", getClass().getName()+".generateField4aBeanConverter2()");
        fbuilder.INITIALIZER(LAMBDA(PARAMETER("record", OBJECT_ARRAY)).BODY(RETURN(METHOD_CALL(VARIABLE("this"), toBean,List.of(VARIABLE("record"))))));
        return fbuilder;
    }


    private Field generateFieldBeanConverter(String name, String templateName, String packge) {
        final org.openprovenance.prov.template.compiler.past.type.TypeName processorClassName = processorClassType(templateName, packge, STRING);
        return FIELD(A_BEAN_SQL_CONVERTER, processorClassName)
                .MODIFIERS(Modifier.FINAL, Modifier.PUBLIC)
                .COMMENT("Generated by method $N", getClass().getName()+".generateFieldBeanConverter()")
                .INITIALIZER(METHOD_CALL(VARIABLE("this"), BEAN_SQL_CONVERSION_METHOD, List.of()));
    }




    private Field generateField4aArgs2Records(String name, String templateName, String packge) {
        final ParameterizedType processorClassName = processorClassType(templateName, packge,OBJECT_ARRAY_ARRAY);
        return FIELD(ARGS2RECORD_CONVERTER, processorClassName)
                .MODIFIERS(Modifier.FINAL, Modifier.PUBLIC)
                .COMMENT("Generated by method $N", getClass().getName()+".generateField4aArgs2Records()")
                .INITIALIZER(METHOD_CALL(VARIABLE("this"), ARGS_2_RECORDS, List.of()));
    }


    private Field generateField4aRecord2SqlConverter(String templateName) {
        Field method=FIELD(A_RECORD_SQL_CONVERTER, functionObjArrayTo(STRING)).MODIFIERS(Modifier.FINAL, Modifier.PUBLIC);
        method.COMMENT("Generated by method $N", getClass().getName()+".generateField4aRecord2SqlConverter()");
        method.INITIALIZER(LAMBDA(PARAMETER("record", OBJECT_ARRAY))
                .BODY(RETURN(
                        METHOD_CALL(
                                METHOD_CALL(VARIABLE("this"), "record2bean", List.of(VARIABLE("record"))),
                                "process",
                                List.of(VARIABLE(Constants.A_BEAN_SQL_CONVERTER, FIELD_VARIABLE))
                        ))));

        return method;
    }


    private Field generateFieldRecord2CsvConverter(String name, String templateName, String packge) {
        return FIELD(Constants.A_RECORD_CSV_CONVERTER,functionObjArrayTo(STRING))
                .MODIFIERS(Modifier.FINAL, Modifier.PUBLIC)
                .COMMENT("Generated by method $N", getClass().getName()+".generateFieldRecord2CsvConverter()")
                .INITIALIZER(LAMBDA(PARAMETER("record", OBJECT_ARRAY))
                        .BODY(RETURN(
                                METHOD_CALL(
                                        METHOD_CALL(VARIABLE("this"), "record2bean", List.of(VARIABLE("record"))),
                                        "process",
                                        List.of(VARIABLE(Constants.A_ARGS_CSV_CONVERTER, FIELD_VARIABLE))
                                ))));
    }




    public Method generateBeanToSqlConversionMethod(String name, String template, String loggerName, String packge, TemplateBindingsSchema bindingsSchema) {
        final org.openprovenance.prov.template.compiler.past.type.TypeName processorClassName = processorClassType(template, packge, STRING);
        final org.openprovenance.prov.template.compiler.past.type.TypeName processorClassNameNotParametrised = processorClassTypeNotParametrised2(template, packge);
        Method method = METHOD(Constants.BEAN_SQL_CONVERSION_METHOD)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(processorClassName);
        compilerUtil.debugFileLocation(method);


        method.COMMENT(loggerName + " client side logging method\n");
        method.COMMENT("@return $T\n" , processorClassNameNotParametrised);

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        method.BODY(ASSIGNMENT(get(name,packge), VARIABLE(SELF_VAR), VARIABLE("this")));  //  python??
        //method.addStatement("$T $N=this", ClassName.get(packge,name), SELF_VAR);

        List<Parameter> parameters=variables.stream()
                .map(key -> PARAMETER(GENERATED_VAR_PREFIX + key,compilerUtil.getPastTypeForDeclaredType(theVar, key)))
                .collect(Collectors.toList());

        method.BODY(RETURN(LAMBDA(parameters).BODY(
                ASSIGNMENT(STRING_BUILDER, VARIABLE(SB_VAR), CONSTRUCTOR_CALL(STRING_BUILDER, List.of())),
                METHOD_CALL(VARIABLE(SELF_VAR), "sqlTuple", makeRenamedArgsList2(SB_VAR,variables)),
                RETURN(METHOD_CALL(VARIABLE(SB_VAR), "toString", List.of()))
        )));



        //method.addStatement("return (" + parameters + ") -> { $T sb=new $T(); $N.$N(sb," + args2 + "); return sb.toString(); }", StringBuffer.class, StringBuffer.class, SELF_VAR,"sqlTuple");

        return method;
    }



    public Method generateArgsToRecordMethod(String template, String templateFullQualifiedName, String packge, TemplateBindingsSchema bindingsSchema) {

        final ParameterizedType processorClassName = processorClassType(template, packge, OBJECT_ARRAY);
        final org.openprovenance.prov.template.compiler.past.type.TypeName processorClassNameNotParametrised = processorClassTypeNotParametrised2(template, packge);
        Method builder = METHOD(Constants.ARGS2RECORD_CONVERTER)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(processorClassName);
        compilerUtil.debugFileLocation(builder);

        builder.COMMENT("Returns a converter from arguments to record\n");
        builder.COMMENT("@return $T\n" , processorClassNameNotParametrised);

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);


        List<Parameter> args = new LinkedList<>();
        List<Variable> args2 = new LinkedList<>();

        for (String key: variables) {
            String newkey = "__" + key;
            args.add(PARAMETER(newkey,compilerUtil.getPastTypeForDeclaredType(theVar, key)));
            args2.add(VARIABLE(newkey));
        }

        List<Expression> values = Stream.concat(Stream.of(CONSTANT(templateFullQualifiedName)), args2.stream()).collect(Collectors.toList());

        builder.BODY(RETURN(LAMBDA(args).BODY(RETURN(ARRAY_INITIALISER(OBJECT, values)))));

        return builder;
    }



    public Method generateProcessorConverter(String template, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection beanDirection) {

        final ParameterizedType returnClassName= beanDirection==BeanDirection.COMMON ? processorClassType(template, packge, T()) : integratorClassType(template, packge, T());

        final TypeName returnClassNameNotParametrised = beanDirection==BeanDirection.COMMON ? processorClassTypeNotParametrised(template, packge): integratorClassType (template, packge);
        Method method = METHOD(Constants.PROCESSOR_CONVERTER)
                .MODIFIERS(Modifier.PUBLIC)
                .addTypeVariables(T())
                .RETURNS(returnClassName);
        compilerUtil.debugFileLocation(method);


        ParameterizedType parameterType = functionObjArrayTo(T());


        String processor = compilerUtil.generateNewNameForVariable("processor");
        method.PARAMETER(parameterType, processor).MODIFIERS(Modifier.FINAL);

        method.COMMENT("Returns a converter from Processor taking arguments to Processor taking record\n");
        method.COMMENT("@param $N a transformer for this template\n", processor);
        method.COMMENT("@param <T> type variable for the result of processor\n");
        method.COMMENT("@return $T&lt;$T&gt;\n", returnClassNameNotParametrised, TypeVariableName.get("T"));

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        List<Parameter> parameters = new LinkedList<>();
        List<Expression> arguments = new LinkedList<>();


        for (String key : fieldNames) {
            String newKey = compilerUtil.generateNewNameForVariable(key);
            boolean isOutput=descriptorUtils.isOutput(key,bindingsSchema);
            if (beanDirection==BeanDirection.COMMON || !isOutput) {
                parameters.add(PARAMETER(newKey,compilerUtil.getPastTypeForDeclaredType(theVar, key)));
            }
        }


        for (String key : fieldNames) {
            String newKey = compilerUtil.generateNewNameForVariable(key);
            boolean isOutput=descriptorUtils.isOutput(key,bindingsSchema);

            if (beanDirection!=BeanDirection.COMMON && isOutput) {
                arguments.add(CONSTANT((String)null));
            } else {
                arguments.add(VARIABLE(newKey));
            }

        }

        List<Expression> values = Stream.concat(Stream.of(METHOD_CALL("getFullyQualifiedName",List.of())), arguments.stream()).collect(Collectors.toList());

        method.BODY(RETURN(LAMBDA(parameters).BODY(RETURN(FUNCTIONAL_METHOD_CALL(VARIABLE(processor), "apply", List.of(ARRAY_INITIALISER(OBJECT,values)) )))));


        return method;
    }


    public Method generateProcessorConverter2(String template, String packge, TemplateBindingsSchema bindingsSchema) {
        final ParameterizedType processorClassName = processorClassType(template, packge, T());
        TypeName returnTypeNotParametrised =ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE);

        Method builder = METHOD(Constants.PROCESSOR_CONVERTER)
                .MODIFIERS(Modifier.PUBLIC)
                .addTypeVariables(T())
                .RETURNS(functionObjArrayTo(T()));
        compilerUtil.debugFileLocation(builder);

        builder.PARAMETER(processorClassName, "processor").MODIFIERS(Modifier.FINAL);


        builder.COMMENT("Returns a converter from Processor taking arguments to Processor taking record\n");
        builder.COMMENT("@param processor a transformer for this template\n");
        builder.COMMENT("@param <T> type variable for the result of processor\n");
        builder.COMMENT("@return $T&lt;$T&gt;\n" , returnTypeNotParametrised, T());

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        List<Expression> args2 = new LinkedList<>();
        int count=1;

        for (String key: fieldNames) {

            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(theVar, key);
            final org.openprovenance.prov.template.compiler.past.type.ClassName declaredJavaType2 = compilerUtil.getPastTypeForDeclaredType(theVar, key);
            final String type=declaredJavaType.getName();
            final String converter2 = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
            Expression expression;
            if (converter2 == null) {
                expression= CAST(declaredJavaType2, ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)));
            } else {
                expression=
                        IF_(
                                BINARY_OP(
                                        ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)),
                                        BinaryOp.EQ,
                                        Constant.getNull()))
                                .THEN(Constant.getNull())
                                .ELSE(IF_(BINARY_OP(ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)), INSTANCEOF,METHOD_CALL(STRING,"class")))
                                        .THEN(METHOD_CALL(converter2, List.of(CAST(STRING, ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count))))))
                                        .ELSE(CAST(declaredJavaType2, ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)))));

            }
            args2.add(expression);
            count ++;
        }
        builder.BODY(RETURN(LAMBDA(PARAMETER("record", OBJECT_ARRAY)).BODY(RETURN(FUNCTIONAL_METHOD_CALL(VARIABLE("processor"), "process", args2)))));
        return builder;
    }


    public Method generateApplyMethod(String template, String packge) {
        final ParameterizedType processorClassName = processorClassType(template, packge, T());

        Method method = METHOD("apply")
                .MODIFIERS(Modifier.PUBLIC)
                .addTypeVariables(T())
                .RETURNS(T());
        compilerUtil.debugFileLocation(method);

        method.COMMENT("Apply method\n")
                .COMMENT("@param processor a transformer for this template\n")
                .COMMENT("@param record as an array of Objects\n")
                .COMMENT("@param <T> type variable for the result of processor\n")
                .COMMENT("@return an object of type $T\n" , TypeVariableName.get("T"));

        final String var_processor = "processor";
        final String var_record = "record";
        method.PARAMETERS(
                PARAMETER(var_processor, processorClassName),
                PARAMETER(var_record, OBJECT_ARRAY));

        method.BODY(RETURN(FUNCTIONAL_METHOD_CALL(
                METHOD_CALL(VARIABLE("this"), "record2bean", List.of(VARIABLE(var_record))),
                "process",
                List.of(VARIABLE(var_processor)))));
        return method;
    }


    private ParameterizedType processorClassType(String template, String packge, TypeVariable t) {
        ParameterizedType name=ParameterizedType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.get(compilerUtil.processorNameClass(template),packge),t);
        return name;
    }

    private ParameterizedType integratorClassType(String template, String packge, TypeVariable t) {
        ParameterizedType name=ParameterizedType.get(get(packge,compilerUtil.integratorNameClass(template)),t);
        return name;
    }


    private TypeName processorClassType(String template, String packge, ArrayTypeName arrayTypeName) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),arrayTypeName);
        return name;
    }
    private ParameterizedType processorClassType(String template, String packge, ArrayType arrayTypeName) {
        ParameterizedType name=ParameterizedType.get(get(compilerUtil.processorNameClass(template),packge),arrayTypeName);
        return name;
    }
    private TypeName processorClassType(String template, String packge, ClassName cl) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),cl);
        return name;
    }
    private org.openprovenance.prov.template.compiler.past.type.TypeName processorClassType(String template, String packge, org.openprovenance.prov.template.compiler.past.type.ClassName cl) {
        ParameterizedType name= ParameterizedType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.get(compilerUtil.processorNameClass(template),packge),cl);
        return name;
    }



    private org.openprovenance.prov.template.compiler.past.type.TypeName processorClassTypeNotParametrised2(String template, String packge) {
        return org.openprovenance.prov.template.compiler.past.type.ClassName.get(compilerUtil.processorNameClass(template), packge);
    }
    private TypeName integratorClassType(String template, String packge) {
        return ClassName.get(packge,compilerUtil.integratorNameClass(template));
    }

    public Method generateLoggerMethod_new(String template, String templateFullyQualifiedName, TemplateBindingsSchema bindingsSchema) {
        Method method = METHOD(compilerUtil.loggerName(template))
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(VOID);
        String var = SB_VAR;

        compilerUtil.debugFileLocation(method);



        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);


        method.COMMENT("Logger method\n");
        method.COMMENT("@param $N a StringBuffer\n", var);


        method.PARAMETER(STRING_BUILDER, var);

        for (String key: fieldNames) {
            String newkey = compilerUtil.generateNewNameForVariable(key);
            Descriptor descriptor=theVar.get(key).get(0);
            String documentation=descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getDocumentation, NameDescriptor::getDocumentation);

            method.COMMENT("@param $N $N\n", newkey, documentation);
            method.PARAMETER(compilerUtil.getPastTypeForDeclaredType(theVar, key), newkey);
        }

        String separator = ",";

        String constant = StringEscapeUtils.escapeCsv(templateFullyQualifiedName);
        method.BODY(METHOD_CALL(VARIABLE(var), "append", CONSTANT(constant)));

        for (String key: fieldNames) {

            final String newName = compilerUtil.generateNewNameForVariable(key);
            final org.openprovenance.prov.template.compiler.past.type.ClassName clazz1 = compilerUtil.getPastTypeForDeclaredType(theVar, key);
            final boolean isQualifiedName = theVar.get(key).get(0) instanceof NameDescriptor; //the_var.get(key).get(0).get("@id") != null;

            method.BODY(METHOD_CALL(VARIABLE(var), "append", CONSTANT(separator)));


            if (STRING.packge.equals(clazz1.packge) && STRING.simpleName.equals(clazz1.simpleName)) {
                boolean doEscape=false;
                if (!isQualifiedName) {
                    doEscape = ((AttributeDescriptorList)theVar.get(key).get(0)).getItems().get(0).getEscape() !=null; //.the_var.get(key).get(0).get(0).get("@escape") != null;
                    if (doEscape) {
                        foundEscape=true;
                    }
                }
                method.BODY(IF(BINARY_OP(VARIABLE(newName), EQ, Constant.getNull()))
                                .ELSE(
                                        (doEscape)?
                                                METHOD_CALL(VARIABLE(var), "append",
                                                        METHOD_CALL(get("StringEscapeUtils", "org.openprovenance.apache.commons.lang"), "escapeCsv", VARIABLE(newName))
                                                )
                                                :
                                                METHOD_CALL(VARIABLE(var), "append", VARIABLE(newName)
                                                )
                                ));


            } else {
                method.BODY(IF(BINARY_OP(VARIABLE(newName), EQ, Constant.getNull()))
                        .ELSE(
                                METHOD_CALL(VARIABLE(var), "append", VARIABLE(newName))
                        ));
            }
        };


        return method;
    }


    public boolean getFoundEscape() {
        return foundEscape;
    }

    private boolean foundEscape=false;

    public Method generateGetNodeStatic(TemplateBindingsSchema bindingsSchema) {
        Method method = METHOD(METHOD_GET_NODES)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(intArray);
        compilerUtil.debugFileLocation(method);

        Map<String, List<Descriptor>> theVar= bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);
        int count = 0;
        List<Integer> integers = new LinkedList<>();
        for (String key: fieldNames) {
            count++;
            if (theVar.get(key).get(0) instanceof NameDescriptor) {
                integers.add(count);
            }
        }
        List<Expression> constants=integers.stream().map(Constant::new).collect(Collectors.toList());
        method.BODY(RETURN(ARRAY_INITIALISER(_int,constants)));
        return method;
    }

    public Method generateCommonMethodGetNodes2(BeanKind beanKind) {
        Method method = METHOD("getNodes")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(intArray);
        compilerUtil.debugFileLocation(method);
        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(method);
        } else {
            method.addStatement(RETURN(VARIABLE(__NODES_FIELD,FIELD_VARIABLE)));
        }
        return method;
    }

    static public void generateUnsupportedException(MethodSpec.Builder builder) {
        builder.addStatement("throw new $T()", UnsupportedOperationException.class);
    }
    static public void generateUnsupportedException(Method builder) {
        builder.BODY(METHOD_CALL(  "throw",
                List.of(CONSTRUCTOR_CALL(UNSUPPORTED_OPERATION_EXCEPTION, List.of()))));
    }
    static public final ParameterizedTypeName functionObjArrayTo (TypeName returnType) {
        return ParameterizedTypeName.get(ClassName.get(Function.class), ArrayTypeName.of(Object.class), returnType);
    }
    static public final ParameterizedTypeName functionListObjArrayTo (TypeName returnType) {
        return ParameterizedTypeName.get(ClassName.get(Function.class), ParameterizedTypeName.get(ClassName.get(List.class),ArrayTypeName.of(Object.class)), returnType);
    }


    public Method generateOutputsMethod() {
        Method method = METHOD(Constants.OUTPUTS_METHOD)
                .COMMENT("Null method for composite\n@return null")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING_ARRAY);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(VARIABLE(OUTPUTS, FIELD_VARIABLE)));
        return method;
    }
    public Method generateCompulsoryInputsMethod() {
        Method method = METHOD(COMPULSORY_INPUTS_METHOD)
                .COMMENT("Null method for composite\n@return null")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING_ARRAY);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(VARIABLE(COMPULSORY_INPUTS, FIELD_VARIABLE)));
        return method;
    }

    public Method generateInputsMethod() {
        Method method = METHOD(Constants.INPUTS_METHOD)
                .COMMENT("Null method for composite\n@return null")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING_ARRAY);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(VARIABLE(INPUTS, FIELD_VARIABLE)));
        return method;
    }
    public Method generateNullInputsMethod() {
        Method method = METHOD(Constants.INPUTS_METHOD)
                .COMMENT("Null method for composite\n@return null")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING_ARRAY);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(Constant.getNull()));
        return method;
    }

    public Method generateNullOutputsMethod() {
        Method method = METHOD(Constants.OUTPUTS_METHOD)
                .COMMENT("Null method for composite\n@return null")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING_ARRAY);
        compilerUtil.debugFileLocation(method);
        method.BODY(RETURN(Constant.getNull()));
        return method;
    }

    public Method generateRecordCsvProcessorMethod(BeanKind beanKind) {
        Method method = METHOD(RECORD_CSV_PROCESSOR_METHOD)
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER( OBJECT_ARRAY, "record")
                .RETURNS(functionObjArrayTo(STRING));
        compilerUtil.debugFileLocation(method);
        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(method);
        } else {
            method.BODY(RETURN(VARIABLE(A_RECORD_CSV_CONVERTER)));
        }
        return method;
    }
    public Method generateCommonMethodGetSuccessors(BeanKind beanKind) {
        Method method = METHOD("getSuccessors")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(MAP_INTEGER_INTARRAY);
        compilerUtil.debugFileLocation(method);

        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(method);
        } else {
            method.addStatement(RETURN(VARIABLE("__successors", FIELD_VARIABLE)));
        }
        return method;
    }

    public Method generateCommonMethodGetForeign(BeanKind beanKind) {
        Method method = METHOD(GET_FOREIGN)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING_ARRAY);
        compilerUtil.debugFileLocation(method);
        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(method);
        } else {
            method.BODY(RETURN(VARIABLE(FOREIGN_TABLES)));
        }
        return method;
    }

    public Method generateCommonMethodGetTypedSuccessors(BeanKind beanKind) {
        Method method = METHOD("getTypedSuccessors")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(MAP_INTEGER_INTARRAY);
        compilerUtil.debugFileLocation(method);

        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(method);
        } else {
            method.addStatement(RETURN(VARIABLE("__successors2",FIELD_VARIABLE)));
        }

        return method;
    }


    public Method generateCommonMethod4static(Set<QualifiedName> allVars, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed) {
        Method method =METHOD(METHOD_GET_SUCCESSORS)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(MAP_INTEGER_INTARRAY);
        compilerUtil.debugFileLocation(method);

        method.BODY(ASSIGNMENT(MAP_INTEGER_INTARRAY, VARIABLE(TABLE), CONSTRUCTOR_CALL(HASH_MAP_INTEGER_INTARRAY, List.of())));

        Map<Integer, int[]> map=generateSucccessorMap(allVars, bindingsSchema, indexed);

        for (Map.Entry<Integer, int[]> entry: map.entrySet()) {
            Integer key=entry.getKey();
            int [] values=entry.getValue();
            List<Expression> constants=Arrays.stream(values).mapToObj(Constant::new).collect(Collectors.toList());
            method.BODY(METHOD_CALL(VARIABLE(TABLE), "put", List.of(CONSTANT(key), ARRAY_INITIALISER(_int,constants))));
        }

        method.BODY(RETURN(VARIABLE(TABLE)));
        return method;
    }


    public Map<Integer, int[]> generateSucccessorMap(Set<QualifiedName> allVars, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed) {

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


        Map<Integer, int[]> table = new HashMap<>();

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
                List<Integer> initializer = new ArrayList<>();
                for (QualifiedName successor : successors) {
                    Integer i = index.get(successor);
                    if (i!=null) {
                        initializer.add(i);
                    }
                }
                table.put(count, initializer.stream().mapToInt(i->i).toArray());
            }

        }
        return table;
    }

    public Map<String, Map<String, int[]>> getRelations(Set<QualifiedName> allVars, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed) {

        Map<String, Map<String, int[]>> relations = new HashMap<>();

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


        for (StatementOrBundle.Kind rel: ALL_RELATIONS) {
            AtomicInteger count;
            switch (rel) {
                case PROV_DERIVATION:
                    count = new AtomicInteger();
                    Collection<WasDerivedFrom> anonWasDerivedFrom = indexed.getWasDerivedFrom();
                    Collection<WasDerivedFrom> namedWasDerivedFrom = indexed.getNamedWasDerivedFrom().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    processWasDerivedFrom(anonWasDerivedFrom, varCount, relations, count, true);
                    processWasDerivedFrom(namedWasDerivedFrom, varCount, relations, count, false);
                    break;
                case PROV_ATTRIBUTION:
                    count = new AtomicInteger();
                    Collection<WasAttributedTo> anonWasAttributedTo = indexed.getWasAttributedTo();
                    Collection<WasAttributedTo> namedWasAttributedTo = indexed.getNamedWasAttributedTo().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    processWasAttributedTo(anonWasAttributedTo, varCount, relations, count, true);
                    processWasAttributedTo(namedWasAttributedTo, varCount, relations, count, false);
                    break;
                case PROV_ASSOCIATION:
                    count = new AtomicInteger();
                    Collection<WasAssociatedWith> anonWasAssociatedWith = indexed.getWasAssociatedWith();
                    Collection<WasAssociatedWith> namedWasAssociatedWith = indexed.getNamedWasAssociatedWith().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    processWasAssociatedWith(anonWasAssociatedWith, varCount, relations, count, true);
                    processWasAssociatedWith(namedWasAssociatedWith, varCount, relations, count, false);
                    break;
                case PROV_GENERATION:
                    count = new AtomicInteger();
                    Collection<WasGeneratedBy> anonWasGeneratedBy = indexed.getWasGeneratedBy();
                    Collection<WasGeneratedBy> namedWasGeneratedBy = indexed.getNamedWasGeneratedBy().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    processWasGeneratedBy(anonWasGeneratedBy, varCount,  relations, count, true);
                    processWasGeneratedBy(namedWasGeneratedBy, varCount, relations, count, false);
                    break;
                case PROV_USAGE:
                    count = new AtomicInteger();
                    Collection<Used> anonUsed = indexed.getUsed();
                    Collection<Used> namedUsed = indexed.getNamedUsed().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    processUsed(anonUsed, varCount, relations, count,true);
                    processUsed(namedUsed, varCount, relations, count,false);
                    break;
                case PROV_DELEGATION:
                    count = new AtomicInteger();
                    Collection<ActedOnBehalfOf> anonActedOnBehalfOf = indexed.getActedOnBehalfOf();
                    Collection<ActedOnBehalfOf> namedActedOnBehalfOf = indexed.getNamedActedOnBehalfOf().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    processActedOnBehalfOf(anonActedOnBehalfOf, varCount, relations, count, true);
                    processActedOnBehalfOf(namedActedOnBehalfOf, varCount, relations, count, false);
                    break;

                case PROV_SPECIALIZATION:
                    count = new AtomicInteger();
                    Collection<SpecializationOf> anonSpecializationOf = indexed.getSpecializationOf();
                    Collection<SpecializationOf> namedSpecializationOf = indexed.getNamedSpecializationOf().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    processSpecializationOf(anonSpecializationOf, varCount, relations, count, true);
                    processSpecializationOf(namedSpecializationOf, varCount, relations, count, false);
                    break;

                case PROV_MEMBERSHIP:
                    count = new AtomicInteger();
                    Collection<HadMember> anonHadMember = indexed.getHadMember();
                    Collection<HadMember> namedHadMember = indexed.getNamedHadMember().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    processHadMember(anonHadMember, varCount, relations, count, true);
                    processHadMember(namedHadMember, varCount, relations, count, false);
                    break;
            }
        }
        return relations;
    }
    public Method generateGetRelations(Set<QualifiedName> allVars, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed) {

        Map<String, Map<String, int[]>> relations= getRelations(allVars, bindingsSchema, indexed);

        Method method = METHOD(METHOD_GET_RELATIONS)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(MAP_STRING_MAP_STRING_INTARRAY);
        compilerUtil.debugFileLocation(method);


        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        method.BODY(ASSIGNMENT(MAP_STRING_MAP_STRING_INTARRAY, VARIABLE(TABLE_VAR), CONSTRUCTOR_CALL(HASH_MAP_STRING_MAP_STRING_INTARRAY, List.of())));
        method.BODY(ASSIGNMENT(MAP_STRING_INTARRAY, VARIABLE("map2"), CONSTRUCTOR_CALL(HASH_MAP_STRING_INTARRAY, List.of())));

        for (Map.Entry<String, Map<String, int[]>> entry: relations.entrySet()) {
            String rel=entry.getKey();
            Map<String, int[]> map2=entry.getValue();

            method.BODY(ASSIGNMENT(null, VARIABLE("map2"), CONSTRUCTOR_CALL(HASH_MAP_STRING_INTARRAY, List.of())));

            for (Map.Entry<String, int[]> entry2: map2.entrySet()) {
                String key2=entry2.getKey();
                int [] values2=entry2.getValue();
                List<Expression> constants2=Arrays.stream(values2).mapToObj(Constant::new).collect(Collectors.toList());

                method.BODY(METHOD_CALL(VARIABLE("map2"), "put", List.of(CONSTANT(key2), ARRAY_INITIALISER(_int,constants2))));


            }
            method.BODY(METHOD_CALL(VARIABLE(TABLE_VAR), "put", List.of(CONSTANT(rel), VARIABLE("map2"))));
        }
        method.BODY(RETURN(VARIABLE(TABLE_VAR)));
        return method;
    }


    private void processWasDerivedFrom(Collection<WasDerivedFrom> wdfCollection, HashMap<QualifiedName, Integer> varCount, Map<String, Map<String, int[]>> relations, AtomicInteger count, boolean anon) {
        for (WasDerivedFrom rel : wdfCollection) {
            Integer gen = countIsNull(varCount.get(rel.getGeneratedEntity()));
            Integer usd = countIsNull(varCount.get(rel.getUsedEntity()));
            Integer act = countIsNull(varCount.get(rel.getActivity()));
            if (gen >= 0) {
                String label = getLabel(count.get(), anon, rel.getId());
                String name = rel.getKind().name();
                relations.computeIfAbsent(name, k -> new HashMap<>());
                relations.get(name).put(label, new int[] { gen, usd, act });
            }
            count.getAndIncrement();
        }
    }
    private void processWasAttributedTo(Collection<WasAttributedTo> watCollection, HashMap<QualifiedName, Integer> varCount, Map<String, Map<String, int[]>> relations, AtomicInteger count, boolean anon) {
        for (WasAttributedTo rel : watCollection) {
            Integer ent = countIsNull(varCount.get(rel.getEntity()));
            Integer ag = countIsNull(varCount.get(rel.getAgent()));
            if (ent >= 0 && ag >= 0) {
                String label = getLabel(count.get(), anon, rel.getId());
                relations.computeIfAbsent(rel.getKind().name(), k -> new HashMap<>());
                relations.get(rel.getKind().name()).put(label, new int[] { ent, ag });
            }
            count.getAndIncrement();
        }
    }
    private void processWasAssociatedWith(Collection<WasAssociatedWith> wawCollection, HashMap<QualifiedName, Integer> varCount, Map<String, Map<String, int[]>> relations, AtomicInteger count, boolean anon) {
        for (WasAssociatedWith rel : wawCollection) {
            Integer act = countIsNull(varCount.get(rel.getActivity()));
            Integer ag = countIsNull(varCount.get(rel.getAgent()));
            Integer pl = countIsNull(varCount.get(rel.getPlan()));
            if (act >= 0 && ag >= 0) {
                String label = getLabel(count.get(), anon, rel.getId());
                relations.computeIfAbsent(rel.getKind().name(), k -> new HashMap<>());
                relations.get(rel.getKind().name()).put(label, new int[] { act, ag, pl });
            }
            count.getAndIncrement();
        }
    }

    private void processWasGeneratedBy(Collection<WasGeneratedBy> wgbCollection, HashMap<QualifiedName, Integer> varCount, Map<String, Map<String, int[]>> relations, AtomicInteger count, boolean anon) {
        for (WasGeneratedBy rel : wgbCollection) {
            Integer ent = countIsNull(varCount.get(rel.getEntity()));
            Integer act = countIsNull(varCount.get(rel.getActivity()));
            if (ent >= 0 && act >= 0) {
                String label = getLabel(count.get(), anon, rel.getId());
                relations.computeIfAbsent(rel.getKind().name(), k -> new HashMap<>());
                relations.get(rel.getKind().name()).put(label, new int[] { ent, act });
            }
            count.getAndIncrement();
        }
    }

    private void processUsed(Collection<Used> usedCollection, HashMap<QualifiedName, Integer> varCount, Map<String, Map<String, int[]>> relations, AtomicInteger count, boolean anon) {
        for (Used rel : usedCollection) {
            Integer act = countIsNull(varCount.get(rel.getActivity()));
            Integer ent = countIsNull(varCount.get(rel.getEntity()));
            if (act >= 0 && ent >= 0) {
                String label = getLabel(count.get(), anon, rel.getId());
                relations.computeIfAbsent(rel.getKind().name(), k -> new HashMap<>());
                relations.get(rel.getKind().name()).put(label, new int[] { act, ent });
            }
            count.getAndIncrement();
        }
    }

    private void processActedOnBehalfOf(Collection<ActedOnBehalfOf> aoboCollection, HashMap<QualifiedName, Integer> varCount, Map<String, Map<String, int[]>> relations, AtomicInteger count, boolean anon) {
        for (ActedOnBehalfOf rel : aoboCollection) {
            Integer del = countIsNull(varCount.get(rel.getDelegate()));
            Integer ag = countIsNull(varCount.get(rel.getResponsible()));
            Integer act = countIsNull(varCount.get(rel.getActivity()));
            if (del >= 0 && ag >= 0) {
                String label = getLabel(count.get(), anon, rel.getId());
                relations.computeIfAbsent(rel.getKind().name(), k -> new HashMap<>());
                relations.get(rel.getKind().name()).put(label, new int[] { del, ag, act });
            }
            count.getAndIncrement();
        }
    }

    private void processSpecializationOf(Collection<SpecializationOf> soCollection, HashMap<QualifiedName, Integer> varCount, Map<String, Map<String, int[]>> relations, AtomicInteger count, boolean anon) {
        for (SpecializationOf rel : soCollection) {
            Integer spec = countIsNull(varCount.get(rel.getSpecificEntity()));
            Integer gen = countIsNull(varCount.get(rel.getGeneralEntity()));
            if (spec >= 0 && gen >= 0) {
                String label = getLabel(count.get(), anon, null);
                relations.computeIfAbsent(rel.getKind().name(), k -> new HashMap<>());
                relations.get(rel.getKind().name()).put(label, new int[] { spec, gen });
            }
            count.getAndIncrement();
        }
    }

    private void processHadMember(Collection<HadMember> hmCollection, HashMap<QualifiedName, Integer> varCount, Map<String, Map<String, int[]>> relations, AtomicInteger count, boolean anon) {
        for (HadMember rel : hmCollection) {
            Integer coll = countIsNull(varCount.get(rel.getCollection()));
            List<QualifiedName> entities = rel.getEntity();
            for (QualifiedName entity: entities) {
                Integer mem = countIsNull(varCount.get(entity));
                if (coll >= 0 && mem >= 0) {
                    String label = getLabel(count.get(), anon, null); // note labelling is for each member
                    relations.computeIfAbsent(rel.getKind().name(), k -> new HashMap<>());
                    relations.get(rel.getKind().name()).put(label, new int[] { coll, mem });
                }
                count.getAndIncrement();
            }
        }
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


    public Method generateGetTypedSuccessorsStatic(Map<Integer, List<Integer>> table) {
        Method method = METHOD(METHOD_GET_TYPED_SUCCESSORS)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(MAP_INTEGER_INTARRAY);
        compilerUtil.debugFileLocation(method);

        method.BODY(ASSIGNMENT(MAP_INTEGER_INTARRAY, VARIABLE(TABLE_VAR), CONSTRUCTOR_CALL(HASH_MAP_INTEGER_INTARRAY, List.of())));



        for (Map.Entry<Integer, List<Integer>> entry: table.entrySet()) {
            Integer key=entry.getKey();
            List<Integer> values=entry.getValue();
            List<Expression> constants=values.stream().map(Constant::new).collect(Collectors.toList());
            method.BODY(METHOD_CALL(VARIABLE(TABLE_VAR), "put", List.of(CONSTANT(key), ARRAY_INITIALISER(_int,constants))));
        }
        method.BODY(RETURN(VARIABLE(TABLE_VAR)));

        return method;
    }
    public Map<Integer, List<Integer>> getTypedSuccessors(Set<QualifiedName> allVars, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed) {

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


                tableValues.put(count,rowValues);

            }

        }

        return tableValues;
    }



    public Method generateGetAllTypesMethodStatic(IndexedDocument indexed) {
        Method method = METHOD("__getAllTypes")
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(STRING_ARRAY);

        compilerUtil.debugFileLocation(method);

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

        method.BODY(ASSIGNMENT(STRING_ARRAY, VARIABLE(TABLE_VAR), ARRAY_ALLOCATOR(STRING, CONSTANT(knownTypes.size()))));
        int count=0;
        for (String s: knownTypes) {
            method.BODY(ASSIGNMENT(null, ARRAY_ACCESSOR(VARIABLE(TABLE_VAR), CONSTANT(count)), CONSTANT(s)));
            count++;
        }
        method.BODY(RETURN(VARIABLE(TABLE_VAR)));
        return method;
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


    public Method generateFactoryMethodWithBean(String template, String packge, TemplateBindingsSchema bindingsSchema) {
        Method method = METHOD(ARGS_2_BEAN)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(get(compilerUtil.commonNameClass(template), packge));

        compilerUtil.debugFileLocation(method);
        Collection<String>variables=descriptorUtils.fieldNames(bindingsSchema);
        method.BODY(ASSIGNMENT(get(compilerUtil.commonNameClass(template),packge), VARIABLE(BEAN_VAR), CONSTRUCTOR_CALL(get(compilerUtil.commonNameClass(template),packge), List.of())));
        for (String key: variables) {
            String newkey = compilerUtil.generateNewNameForVariable(key);
            method.PARAMETER(compilerUtil.getPastTypeForDeclaredType(bindingsSchema.getVar(), key), newkey);
            method.BODY(ASSIGNMENT(null, METHOD_CALL(VARIABLE(BEAN_VAR), key), VARIABLE(newkey)));
        }
        method.BODY(RETURN(VARIABLE(BEAN_VAR)));
        return method;
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

    public Method generateFactoryMethodToBeanWithArray_new(Locations locations, String toBean, String template, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection direction, String extension, List<String> shared) {
        if (extension!=null) {
            template= locations.getShortNames().get(template);
        }
        Method method = METHOD(toBean)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(org.openprovenance.prov.template.compiler.past.type.ClassName.get(compilerUtil.beanNameClass(template,direction,extension),packge));
        compilerUtil.debugFileLocation(method);


        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        method.PARAMETER(OBJECT_ARRAY, "record");

        org.openprovenance.prov.template.compiler.past.type.ClassName className = get(compilerUtil.beanNameClass(template,direction,extension),packge);
        method.addStatement(ASSIGNMENT(className,VARIABLE(BEAN_VAR),CONSTRUCTOR_CALL(className,List.of())));

        method.COMMENT("Converter to bean of type $T for template $N.\n", className, template);
        if (shared!=null) {
            method.COMMENT("Variant $N of class $T to support shared variables $N\n", extension, ClassName.get(packge,compilerUtil.beanNameClass(template,direction)), shared.toString());
        }
        method.COMMENT("@param record an array of objects\n");
        method.COMMENT("@return a bean\n");

        int count = 1;
        for (String key: variables) {
            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
            final org.openprovenance.prov.template.compiler.past.type.ClassName declaredJavaType2 = compilerUtil.getPastTypeForDeclaredType(bindingsSchema.getVar(), key);
            final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);

            if (direction==BeanDirection.COMMON
                    || descriptorUtils.isInput(key,bindingsSchema)
                    || (shared!=null && shared.contains(key))) {
                if (converter == null) {
                    method.BODY(ASSIGNMENT(null, METHOD_CALL(VARIABLE(BEAN_VAR),key),
                            CAST(declaredJavaType2,ARRAY_ACCESSOR(VARIABLE("record"),CONSTANT(count)))));
                } else {
                    method.BODY(
                            ASSIGNMENT(null, METHOD_CALL(VARIABLE(BEAN_VAR),key),
                                    IFEXPRESSION(
                                            BINARY_OP(ARRAY_ACCESSOR(VARIABLE("record"),CONSTANT(count)),EQ,getNull()),
                                            getNull(),
                                            IFEXPRESSION(
                                                    BINARY_OP(ARRAY_ACCESSOR(VARIABLE("record"),CONSTANT(count)),INSTANCEOF,METHOD_CALL(STRING,"class")),
                                                    METHOD_CALL(converter,List.of(CAST(STRING,ARRAY_ACCESSOR(VARIABLE("record"),CONSTANT(count))))),
                                                    CAST(declaredJavaType2,ARRAY_ACCESSOR(VARIABLE("record"),CONSTANT(count)))))));

                }
            }
            count++;
        }
        method.BODY(RETURN(VARIABLE(BEAN_VAR)));



        return method;
    }


    public Method generateNewBean(String template, String packge) {
        org.openprovenance.prov.template.compiler.past.type.ClassName beanClass = get(compilerUtil.commonNameClass(template), packge);
        Method builder =METHOD("newBean")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(beanClass);
        compilerUtil.debugFileLocation(builder);
        builder.BODY(ASSIGNMENT(beanClass, VARIABLE(BEAN_VAR), CONSTRUCTOR_CALL(beanClass, List.of())));
        builder.addStatement(RETURN(VARIABLE(BEAN_VAR)));
        return builder;
    }




    public Method generateExamplarBean(String template, String packge, TemplateBindingsSchema bindingsSchema) {
        org.openprovenance.prov.template.compiler.past.type.ClassName commonName = get(compilerUtil.commonNameClass(template), packge);
        Method builder = METHOD("examplar")
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(commonName);

        compilerUtil.debugFileLocation(builder);

        builder.BODY(ASSIGNMENT(commonName, VARIABLE(BEAN_VAR), CONSTRUCTOR_CALL(commonName, List.of())));

        Map<String, List<Descriptor>> theVars = bindingsSchema.getVar();
        Collection<String> nameVariables = descriptorUtils.getNameVariables(bindingsSchema);
        Collection<String> attrVariables = descriptorUtils.getAttributeVariables(bindingsSchema);


        for (String aVar : nameVariables) {
            List<Descriptor> descriptors = theVars.get(aVar);
            Descriptor qDescriptor = (descriptors == null) ? null : descriptors.get(0);
            String idType = (qDescriptor == null) ? null : descriptorUtils.getFromDescriptor(qDescriptor, AttributeDescriptor::getType, NameDescriptor::getType);
            Object examplar = (qDescriptor == null) ? null : descriptorUtils.getFromDescriptor(qDescriptor, AttributeDescriptor::getExamplar, NameDescriptor::getExamplar);

            if (idType == null) {
                builder.BODY(ASSIGNMENT(null, METHOD_CALL(VARIABLE(BEAN_VAR),aVar), CONSTANT("example_" + aVar)));
            } else {
                String example = (examplar == null) ? compilerUtil.generateExampleForType(idType, aVar, pFactory) : examplar.toString();
                Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(theVars, aVar);

                final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
                if (converter == null) {
                    builder.BODY(ASSIGNMENT(null, METHOD_CALL(VARIABLE(BEAN_VAR),aVar),CONSTANT(example)));
                } else {
                    builder.BODY(ASSIGNMENT(null, METHOD_CALL(VARIABLE(BEAN_VAR),aVar),METHOD_CALL(converter,List.of(CONSTANT(example)))));
                }
            }
        }


        for (String aVar : attrVariables) {

            String declaredType = compilerUtil.getDeclaredType(theVars, aVar);
            Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(theVars, aVar);
            List<Descriptor> descriptors = theVars.get(aVar);
            Descriptor qDescriptor = (descriptors == null) ? null : descriptors.get(0);
            Object examplar = (qDescriptor == null) ? null : descriptorUtils.getFromDescriptor(qDescriptor, AttributeDescriptor::getExamplar, NameDescriptor::getExamplar);


            if (qDescriptor != null) { // only generate code if there is a descriptor!

                String example = (examplar != null) ? examplar.toString() : compilerUtil.generateExampleForType(declaredType, aVar, pFactory);

                final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
                if (converter == null) {
                    builder.BODY(ASSIGNMENT(null, METHOD_CALL(VARIABLE(BEAN_VAR),aVar),CONSTANT(example)));
                } else {
                    builder.BODY(ASSIGNMENT(null, METHOD_CALL(VARIABLE(BEAN_VAR),aVar),METHOD_CALL(converter,List.of(CONSTANT(example)))));
                }
            }
        }

        builder.BODY(RETURN(VARIABLE(BEAN_VAR)));
        return builder;

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

        String myPackage=locations.getFilePackage(configs.name,fileName);

        JavaFile myfile = compilerUtil.specWithComment(theInterface, configs, myPackage, stackTraceElement);
        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);
    }




    // USED ELSEWHERE


    public MethodSpec generateNameAccessor_no_past(String templateName) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(GET_NAME)
                .addModifiers(Modifier.PUBLIC)
                // .addAnnotation(Override.class)
                .returns(String.class);
        compilerUtil.specWithComment(builder);

        builder.addStatement("return $S", templateName);
        return builder.build();
    }

    public MethodSpec generateFullyQualifiedNameAccessor_no_past(String fullyQualifiedTemplateName) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(GET_FULLY_QUALIFIED_NAME)
                .addModifiers(Modifier.PUBLIC)
                //.addAnnotation(Override.class)
                .returns(String.class);
        compilerUtil.specWithComment(builder);

        builder.addStatement("return $S", fullyQualifiedTemplateName);
        return builder.build();
    }



}
