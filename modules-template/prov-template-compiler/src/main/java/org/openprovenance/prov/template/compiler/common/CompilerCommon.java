package org.openprovenance.prov.template.compiler.common;

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
import org.openprovenance.prov.template.compiler.past.annotations.Ignore;
import org.openprovenance.prov.template.compiler.past.annotations.StaticMethod;
import org.openprovenance.prov.template.compiler.past.type.*;
import org.openprovenance.prov.template.descriptors.*;

import javax.lang.model.element.Modifier;
import java.lang.Class;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.openprovenance.prov.model.StatementOrBundle.ALL_RELATIONS;
import static org.openprovenance.prov.template.compiler.CompilerQueryInvoker.addSpecialTypesMethods;
import static org.openprovenance.prov.template.compiler.common.Constants.INPUTS;
import static org.openprovenance.prov.template.compiler.common.Constants.OUTPUTS;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.expansion.CompilerExpansionBuilder.converterForJsonType;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.InstanceOf.INSTANCE_OF;
import static org.openprovenance.prov.template.core.InstantiateUtil.isVariable;
import static org.openprovenance.prov.template.compiler.CompilerUtil.*;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
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
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;

public class CompilerCommon {
    public static final String SB_VAR = "sb";
    public static final String SELF_VAR = "self";

    public static final String MARKER_LAMBDA = "/*#lambda#*/";
    public static final String MARKER_PARAMS = "/*#params#*/";

    public static final String MARKER_ARRAY = "/*#array#*/";

    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;

    private final CompilerSQL compilerSQL;

    public CompilerCommon(ProvFactory pFactory, CompilerSQL compilerSQL) {
        this.pFactory=pFactory;
        this.compilerSQL=compilerSQL;
        this.compilerUtil=new CompilerUtil(pFactory);
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
        Set<String> foundSpecialTypes=new HashSet<>();

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
        pastClass.METHOD(generateLoggerMethod(templateName, templateFullyQualifiedName, bindingsSchema));
        pastClass.METHOD(generateCommonCSVConverterMethod(locations, name, templateName, compilerUtil.loggerName(templateName), packageName, bindingsSchema, beanKind, BeanDirection.COMMON, consistsOf, locations.getFilePackage(configs.name,LOGGER), LOGGER));
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
            pastClass.METHOD(generateFactoryMethodToBeanWithArray(locations,"record2bean", templateName, packageName, bindingsSchema, BeanDirection.COMMON, null, null));
            pastClass.METHOD(generateArgsToRecordMethod(templateName, templateFullyQualifiedName, packageName, bindingsSchema));
            pastClass.METHOD(generateGetRelations(allVars, bindingsSchema, indexed));
            pastClass.METHOD(generateGetAllTypesMethodStatic(indexed));

            pastClass.FIELDS(generateStaticFieldSuccessor());
            pastClass.FIELDS(generateStaticFieldSuccessor2());
            pastClass.FIELDS(generateStaticFieldNodes());
            pastClass.FIELDS(generateStaticFieldRelations());
            pastClass.FIELDS(generateStaticFieldAllTypes());


            pastClass.METHOD(generateProcessorConverter(PROCESSOR_CONVERTER, templateName, packageName, bindingsSchema, BeanDirection.COMMON));
            pastClass.METHOD(generateOutputsMethod());
            pastClass.METHOD(generateInputsMethod());
            pastClass.METHOD(generateCompulsoryInputsMethod());

            pastClass.FIELDS(generateFieldOutputs(bindingsSchema));
            pastClass.FIELDS(generateFieldInputs(bindingsSchema));
            pastClass.FIELDS(generateFieldCompulsoryInputs(bindingsSchema));

            pastClass.METHOD(generateExamplarBean(templateName, packageName, bindingsSchema));

            pastClass.FIELDS(generateField4aBeanConverter2("record2bean", templateName,packageName, Constants.A_RECORD_BEAN_CONVERTER, BeanDirection.COMMON));
            pastClass.FIELDS(generateFieldRecord2CsvConverter(name,templateName,packageName));

            pastClass.METHOD(generateProcessorConverter2(templateName, packageName, bindingsSchema, foundSpecialTypes));
            pastClass.METHOD(generateFactoryMethodWithBean(templateName, packageName, bindingsSchema));
            pastClass.FIELDS(generateField4aBeanConverter(templateName, packageName, name, bindingsSchema));

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
            pastClass.METHOD(generateFactoryMethodToBeanWithArrayComposite("toBean", templateName, packageName, bindingsSchema, locations.getFilePackage(configs.name,LOGGER), LOGGER, BeanDirection.COMMON, null, null));
            pastClass.FIELDS(generateField4aBeanConverter3("toBean", templateName, packageName, A_RECORD_BEAN_CONVERTER, BeanDirection.COMMON));
        }

        if (configs.integrator) {
            pastClass.FIELDS(generateStaticFieldIntegrator(locations, templateName, templateFullyQualifiedName));
            pastClass.METHOD(generateMethodGetIntegrator(locations, templateName, templateFullyQualifiedName));
        }

        addSpecialTypesMethods(foundSpecialTypes,pastClass);


        String directory = locations.convertToDirectory(packageName);
        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, packageName, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packageName, configs, directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator=() -> generateJavaScript(pastClass, packageName, locations, stackTraceElement);
        SpecificationFile specFile=new SpecificationFile(javaGenerator,pythonGenerator, jsGenerator, emptyGenerator);
        return Pair.of(specFile, successorTable);
    }



    private Method generateMethodGetIntegrator(Locations locations, String templateName, String templateFullyQualifiedName) {
        org.openprovenance.prov.template.compiler.past.type.ClassName integratorClassName = get(compilerUtil.integratorBuilderNameClass(templateName), locations.getBeansPackage(templateFullyQualifiedName, BeanDirection.INPUTS));
        Method builder = METHOD("getIntegrator")
                .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                .RETURNS(integratorClassName);
        compilerUtil.debugFileLocation(builder);
        builder.BODY(RETURN(VARIABLE("__integrator", FIELD_VARIABLE)));
        return builder;
    }

    private Field generateStaticFieldIntegrator(Locations locations, String templateName, String templateFullyQualifiedName) {
        org.openprovenance.prov.template.compiler.past.type.ClassName integratorClassName = get(compilerUtil.integratorBuilderNameClass(templateName), locations.getBeansPackage(templateFullyQualifiedName, BeanDirection.INPUTS));
        return FIELD("__integrator", integratorClassName)
                .COMMENT("Generated by method $N", getClass().getName()+".generateStaticFieldIntegrator()")
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .INITIALIZER(CONSTRUCTOR_CALL(integratorClassName, List.of()));
    }

    private Field generateStaticFieldAllTypes() {
        return FIELD("allTypes", STRING_ARRAY)
                .COMMENT("Generated by method $N", getClass().getName()+".generateStaticFieldAllTypes()")
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .INITIALIZER(METHOD_CALL("__getAllTypes", List.of()).ANNOTATION(StaticMethod.NAME));
    }

    private Field generateStaticFieldRelations() {
        return FIELD("__relations", MAP_STRING_MAP_STRING_INTARRAY)
                .COMMENT("Generated by method $N", getClass().getName()+".generateStaticFieldRelations()")
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .INITIALIZER(METHOD_CALL(METHOD_GET_RELATIONS, List.of()).ANNOTATION(StaticMethod.NAME));
    }

    private Field generateStaticFieldNodes() {
        return FIELD(__NODES_FIELD, intArray)
                .COMMENT("Generated by method $N", getClass().getName()+".generateStaticFieldNodes()")
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .INITIALIZER(METHOD_CALL(METHOD_GET_NODES, List.of()).ANNOTATION(StaticMethod.NAME));
    }

    private Field generateStaticFieldSuccessor2() {
        return FIELD("__successors2", MAP_INTEGER_INTARRAY)
                .COMMENT("Generated by method $N", getClass().getName()+".generateStaticFieldSuccessor2()")
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .INITIALIZER(METHOD_CALL(METHOD_GET_TYPED_SUCCESSORS, List.of()).ANNOTATION(StaticMethod.NAME));
    }

    private Field generateStaticFieldSuccessor() {
        return FIELD("__successors", MAP_INTEGER_INTARRAY)
                .COMMENT("Generated by method $N", getClass().getName()+".generateStaticFieldSuccessor()")
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .INITIALIZER(METHOD_CALL(METHOD_GET_SUCCESSORS, List.of()).ANNOTATION(StaticMethod.NAME));
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

        Map<String, List<Descriptor>> var = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        String shortConsistsOf=locations.getShortNames().get(consistsOf);

        Collection<String> actualVariables = new LinkedList<>(variables);
        actualVariables.add(ELEMENTS1);
        String beanNameClass = compilerUtil.beanNameClass(shortConsistsOf, BeanDirection.COMMON);
        ClassName className = get(beanNameClass, packageName);
        ParameterizedType listBeanType=ParameterizedType.get(LIST, className);
        List<Parameter> paramsList= makeParamsListComposite2(actualVariables, var, compilerUtil, listBeanType);

        ParameterizedType parametericInterface=ParameterizedType.get(get(compilerUtil.processorNameClass(shortConsistsOf),packageName), OBJECT_ARRAY);

        LambdaExpression lambda=LAMBDA(paramsList)
                .BODY(
                        DEFINITION(OBJECT_ARRAY_ARRAY, VARIABLE("_result"), ARRAY_ALLOCATOR(OBJECT_ARRAY, BINARY_OP(METHOD_CALL(VARIABLE(Constants.GENERATED_VAR_PREFIX + ELEMENTS1), "size", List.of()), "+", CONSTANT(1)))),

                        DEFINITION(_int, VARIABLE("_i_"), CONSTANT(1)),

                        ASSIGNMENT(ARRAY_ACCESSOR(VARIABLE("_result"),CONSTANT(0)),
                                ARRAY_INITIALISER(OBJECT, List.of(CONSTANT("compositeThingie"),
                                        METHOD_CALL(VARIABLE(Constants.GENERATED_VAR_PREFIX + ELEMENTS1), "size", List.of()),
                                        Constant.getNull()))),

                        ITERATOR(Parameter.PARAMETER(VAR_ELEMENT,className), VARIABLE(Constants.GENERATED_VAR_PREFIX + ELEMENTS1))
                                .BODY(
                                        DEFINITION(
                                                parametericInterface,
                                                VARIABLE("processor"),
                                                METHOD_CALL(
                                                        METHOD_CALL(get(logger,loggerPackage),
                                                                Constants.GENERATED_VAR_PREFIX + shortConsistsOf),
                                                        Constants.ARGS2RECORD_CONVERTER,
                                                        List.of()
                                                )
                                        ).ANNOTATION("@import " + loggerPackage + "." + logger),

                                        DEFINITION(
                                                OBJECT_ARRAY,
                                                VARIABLE(VAR_OBJECTS),
                                                METHOD_CALL(
                                                        VARIABLE(VAR_ELEMENT),
                                                        "process",
                                                        List.of(VARIABLE("processor"))
                                                )
                                        ),

                                        ASSIGNMENT(
                                                ARRAY_ACCESSOR(VARIABLE("_result"), VARIABLE("_i_")),
                                                VARIABLE(VAR_OBJECTS)
                                        ),

                                        ASSIGNMENT(
                                                VARIABLE("_i_"),
                                                BINARY_OP(VARIABLE("_i_"), "+", CONSTANT(1))
                                        )
                                ),

                        RETURN(VARIABLE("_result"))
                );

        method.BODY(RETURN(lambda));
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
        Field field=FIELD(PROPERTY_ORDER, STRING_ARRAY)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        field.COMMENT("Generated by method $N", getClass().getName()+".generateFieldPropertyOrder()");
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        field.INITIALIZER(ARRAY_INITIALISER(STRING, makeConstantSequence(IS_A,variables)));
        return field;
    }

    public static List<Expression> makeConstantSequence(String head, Collection<String> variables) {
        List<String> variables2=new LinkedList<>();
        if (head!=null) variables2.add(head);
        variables2.addAll(variables);
        return variables2.stream().map(Constant::new).collect(Collectors.toList());

    }

    private Field generateField4aArgs2CsvConverter_new(String name, String templateName, String packge) {
        final TypeName processorClassName = processorClassType(templateName, packge, STRING);
        return FIELD(A_ARGS_CSV_CONVERTER, processorClassName)
                .MODIFIERS(Modifier.FINAL, Modifier.PUBLIC)
                .COMMENT("Generated by method $N", getClass().getName()+".generateField4aArgs2CsvConverter()")
                .INITIALIZER(METHOD_CALL(VARIABLE("this"), ARGS_CSV_CONVERSION_METHOD,List.of()));
    }


    public final ParameterizedType functionObjArrayTo (org.openprovenance.prov.template.compiler.past.type.TypeName returnType) {
        return ParameterizedType.get(FUNCTION, OBJECT_ARRAY, returnType);
    }


    public Method generateCommonCSVConverterMethod(Locations locations, String name, String template, String loggerName, String packge, TemplateBindingsSchema bindingsSchema, BeanKind beanKind, BeanDirection beanDirection, String consistsOf, String loggerPackage, String logger) {
        final org.openprovenance.prov.template.compiler.past.type.TypeName processorClassName = processorClassType(template, packge,beanDirection, STRING);

        final org.openprovenance.prov.template.compiler.past.type.TypeName processorClassNameNotParametrised = processorClassTypeNotParametrised2(template, packge);
        Method method = METHOD(ARGS_CSV_CONVERSION_METHOD)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(processorClassName);
        compilerUtil.debugFileLocation(method);

        method.COMMENT(loggerName + " client side logging method\n");
        method.COMMENT("@return $T\n" , processorClassNameNotParametrised);


        method.BODY(DEFINITION(get(name,packge), VARIABLE(SELF_VAR), VARIABLE("this")));

        Map<String, List<Descriptor>> var = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        Collection<String> actualVariables;
        List<Parameter> paramsList;

        String shortConsistsOf=null;

        if (beanKind==BeanKind.COMPOSITE) {
            shortConsistsOf=locations.getShortNames().get(consistsOf);

            actualVariables = new LinkedList<>(variables);
            actualVariables.add(ELEMENTS1);
            String beanNameClass = compilerUtil.beanNameClass(shortConsistsOf, beanDirection);
            ParameterizedType listBeanType=ParameterizedType.get(LIST, org.openprovenance.prov.template.compiler.past.type.ClassName.get(beanNameClass,packge));
            paramsList= makeParamsListComposite2(actualVariables, var, compilerUtil, listBeanType);
        } else {
            actualVariables=variables;
            paramsList= makeParamsList2(actualVariables, var, compilerUtil);
        }

        List<Expression> argsList = makeRenamedArgsLocalVariableList(SB_VAR,variables);

        LambdaExpression lambda=
                LAMBDA(paramsList)
                        .returns(STRING)
                        .BODY(
                                DEFINITION(STRING_BUILDER, VARIABLE(SB_VAR), CONSTRUCTOR_CALL(STRING_BUILDER, List.of())),

                                METHOD_CALL(VARIABLE(SELF_VAR), loggerName, argsList));


        if (consistsOf!=null) {
            String[] variableArray = variables.toArray(new String[]{});

            String beanNameClass = compilerUtil.beanNameClass(shortConsistsOf, beanDirection);
            ClassName loggerClassName = get(logger, loggerPackage);

            ParameterizedType parametericInterface = ParameterizedType.get(get(compilerUtil.processorNameClass(shortConsistsOf), packge), OBJECT_ARRAY);
            ParameterizedType processorOfString = functionObjArrayTo(STRING);


            if (beanDirection == BeanDirection.COMMON) {

                lambda.BODY(
                        DEFINITION(parametericInterface, VARIABLE("processor"),
                                METHOD_CALL(METHOD_CALL(loggerClassName, GENERATED_VAR_PREFIX + shortConsistsOf), ARGS2RECORD_CONVERTER, List.of()))
                                .ANNOTATION("@import " + loggerPackage + "." + logger),  //delayed import for python
                        FOR(
                                DEFINITION(_int, VARIABLE(_I_), CONSTANT(0)),
                                BINARY_OP(VARIABLE(_I_), BinaryOp.LT, METHOD_CALL(VARIABLE(GENERATED_VAR_PREFIX + ELEMENTS1), "size", List.of())),
                                ASSIGNMENT(VARIABLE(_I_), BINARY_OP(VARIABLE(_I_), "+", CONSTANT(1))))

                                .BODY(
                                        DEFINITION(ClassName.get(beanNameClass, packge), VARIABLE(VAR_ELEMENT),
                                                METHOD_CALL(VARIABLE(GENERATED_VAR_PREFIX + ELEMENTS1), "get", List.of(VARIABLE(_I_)))),


                                        new Comment("// the following line generates ts error: Untyped function calls may not accept type arguments."),


                                        DEFINITION(OBJECT_ARRAY, VARIABLE(VAR_OBJECTS),
                                                METHOD_CALL(VARIABLE(VAR_ELEMENT), "process", List.of(VARIABLE("processor")))),


                                        DEFINITION(processorOfString, VARIABLE(VAR_CSV_CONVERTER),
                                                METHOD_CALL(METHOD_CALL(loggerClassName, "simpleCSvConverters"),
                                                        "get", List.of(VARIABLE(GENERATED_VAR_PREFIX + variableArray[2])))),

                                        DEFINITION(STRING, VARIABLE(VAR_CSV),
                                                FUNCTIONAL_METHOD_CALL(VARIABLE(VAR_CSV_CONVERTER), "apply", List.of(VARIABLE(VAR_OBJECTS)))),

                                        METHOD_CALL(
                                                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("\n"))),
                                                "append",
                                                List.of(VARIABLE(VAR_CSV)))

                                ));
            } else {

                String integratorBuilderName= compilerUtil.integratorBuilderNameClass(shortConsistsOf);
                ClassName integratorBuilderClassName = get(integratorBuilderName, packge);
                String templateBuilderName= compilerUtil.templateNameClass(shortConsistsOf);
                ClassName templateBuilderClassName = get(templateBuilderName, locations.getBeansPackage(consistsOf,BeanDirection.COMMON));


                lambda.BODY(
                      //  DEFINITION(parametericInterface, VARIABLE("processor"),
                      //          METHOD_CALL(METHOD_CALL(loggerClassName, GENERATED_VAR_PREFIX + shortConsistsOf), ARGS2RECORD_CONVERTER, List.of()))
                      //          .ANNOTATION("@import " + loggerPackage + "." + logger),  //delayed import for python
                        FOR(
                                DEFINITION(_int, VARIABLE(_I_), CONSTANT(0)),
                                BINARY_OP(VARIABLE(_I_), BinaryOp.LT, METHOD_CALL(VARIABLE(GENERATED_VAR_PREFIX + ELEMENTS1), "size", List.of())),
                                ASSIGNMENT(VARIABLE(_I_), BINARY_OP(VARIABLE(_I_), "+", CONSTANT(1))))

                                .BODY(
                                        DEFINITION(ClassName.get(beanNameClass, packge), VARIABLE(VAR_ELEMENT),
                                                METHOD_CALL(VARIABLE(GENERATED_VAR_PREFIX + ELEMENTS1), "get", List.of(VARIABLE(_I_)))),


                                  //      DEFINITION(processorOfString, VARIABLE(VAR_CSV_CONVERTER),
                                   //             METHOD_CALL(METHOD_CALL(loggerClassName, "simpleCSvConverters"),
                                   //                     "get", List.of(VARIABLE(GENERATED_VAR_PREFIX + variableArray[2])))),


                                        //new Comment("// the following line generates ts error: Untyped function calls may not accept type arguments."),

                                        DEFINITION(integratorBuilderClassName, VARIABLE(VAR_ELEMENT_INTEGRATOR),
                                                CONSTRUCTOR_CALL(integratorBuilderClassName, List.of())),
                                        DEFINITION(templateBuilderClassName, VARIABLE(VAR_ELEMENT_BUILDER),
                                                CONSTRUCTOR_CALL(templateBuilderClassName, List.of())),


                                        DEFINITION(STRING, VARIABLE(VAR_CSV),
                                                METHOD_CALL(VARIABLE(VAR_ELEMENT),
                                                        "process",
                                                        List.of(METHOD_CALL(VARIABLE(VAR_ELEMENT_INTEGRATOR), PROCESSOR_OUTPUT_CONVERTER, List.of(METHOD_CALL(VARIABLE(VAR_ELEMENT_BUILDER), A_RECORD_CSV_CONVERTER)))))),

                                        METHOD_CALL(
                                                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("\\n"))),
                                                "append",
                                                List.of(VARIABLE(VAR_CSV)))

                                ));
            }


        }

        lambda.BODY(RETURN(METHOD_CALL(VARIABLE(SB_VAR), "toString", List.of())));
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

    public static List<Parameter> makeParamsListComposite2(Collection<String> variables, Map<String, List<Descriptor>> var, CompilerUtil compilerUtil, ParameterizedType listBeanType) {
        return variables
                .stream()
                .map(variable ->
                        PARAMETER(GENERATED_VAR_PREFIX + variable,(variable.equals(ELEMENTS1) ? listBeanType : compilerUtil.getPastTypeForDeclaredType(var, variable))))
                .collect(Collectors.toList());
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


    public static List<Expression> makeConstantStringSequence(String head, Collection<String> variables) {
        List<String> variables2=new LinkedList<>();
        if (head!=null) variables2.add(head);
        variables2.addAll(variables);
        return variables2.stream().map(Constant::CONSTANT).collect(Collectors.toList());
    }

    private Field generateField4aBeanConverter(String templateName, String packge, String classNname, TemplateBindingsSchema bindingsSchema) {
        TypeName myType=processorClassType(templateName,packge,get(compilerUtil.commonNameClass(templateName),packge));

        Map<String, List<Descriptor>> var = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<Parameter> paramsList= makeParamsList2(variables, var, compilerUtil);
        List<Expression> argsList=makeRenamedArgsList2(null,variables);

        return FIELD(A_ARGS_BEAN_CONVERTER,myType)
                .MODIFIERS(Modifier.FINAL, Modifier.PUBLIC)
                .COMMENT("Generated by method $N", getClass().getName()+".generateField4aBeanConverter()")
                .INITIALIZER(
                        LAMBDA(paramsList)
                                .BODY(RETURN(METHOD_CALL(VARIABLE("this").ANNOTATION(Ignore.NAME),ARGS_2_BEAN, argsList))));
        // issue with this METHOD_CALL
        // if operator ClassName.get(classNname,packge), python problematic, because of the generated circular import
        // if "this" is used, python OK, Java OK, but ts: member 'args2bean' is static and cannot be accessed on 'this'
        // thus, @ignore option added

    }

    private Field generateFieldOutputs(TemplateBindingsSchema bindingsSchema) {
        Field field = FIELD(OUTPUTS, STRING_ARRAY).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);
        List<String> outputs = variables.stream().filter(variable -> descriptorUtils.isOutput(variable, bindingsSchema)).collect(Collectors.toList());
        field.INITIALIZER(ARRAY_INITIALISER(STRING, makeConstantStringSequence(null, outputs)));
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

    public Field generateField4aBeanConverter3(String toBean, String templateName, String packge, String fieldName, BeanDirection direction) {
        ClassName returnType = get(compilerUtil.beanNameClass(templateName, direction), packge);
        ParameterizedType myType= FUNCTION_LIST_OBJARRAY_TO_TYPE(returnType);
        Field field=FIELD(fieldName, myType).MODIFIERS(Modifier.FINAL, Modifier.PUBLIC);
        field.COMMENT("Generated by method $N", getClass().getName()+".generateField4aBeanConverter3()");
        field.INITIALIZER(LAMBDA(PARAMETER("records", LIST_OF_OBJECT_ARRAYS_ARRAYS))
                        .returns(returnType)
                .BODY(RETURN(METHOD_CALL(toBean,List.of(VARIABLE("records"))))));
        //" ($T records) -> { return $N(records); }", listOfArrays, toBean);
        return field;
    }

    public Field generateField4aBeanConverter2(String toBean, String templateName, String packge, String fieldName, BeanDirection direction) {
        ClassName returnType = get(compilerUtil.beanNameClass(templateName, direction), packge);
        ParameterizedType myType= FUNCTION_OBJARRAY_TO_TYPE(returnType);
        Field fbuilder=FIELD(fieldName, myType).MODIFIERS(Modifier.FINAL, Modifier.PUBLIC);
        fbuilder.COMMENT("Generated by method $N", getClass().getName()+".generateField4aBeanConverter2()");
        fbuilder.INITIALIZER(LAMBDA(PARAMETER("record", OBJECT_ARRAY)).
                returns(returnType)
                .BODY(RETURN(METHOD_CALL(VARIABLE("this"), toBean,List.of(VARIABLE("record"))))));
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
                        .returns(STRING)
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
                        .returns(STRING)
                        .BODY(RETURN(
                                METHOD_CALL(
                                        METHOD_CALL(VARIABLE("this"), "record2bean", List.of(VARIABLE("record"))),
                                        "process",
                                        List.of(VARIABLE(Constants.A_ARGS_CSV_CONVERTER, FIELD_VARIABLE))
                                ))));
    }

    public Method generateBeanToSqlConversionMethod(String name, String template, String loggerName, String packge, TemplateBindingsSchema bindingsSchema) {
        final TypeName processorClassName = processorClassType(template, packge, STRING);
        final TypeName processorClassNameNotParametrised = processorClassTypeNotParametrised2(template, packge);
        Method method = METHOD(Constants.BEAN_SQL_CONVERSION_METHOD)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(processorClassName);
        compilerUtil.debugFileLocation(method);


        method.COMMENT(loggerName + " client side logging method\n");
        method.COMMENT("@return $T\n" , processorClassNameNotParametrised);

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        method.BODY(DEFINITION(get(name,packge), VARIABLE(SELF_VAR), VARIABLE("this")));

        List<Parameter> parameters=variables.stream()
                .map(key -> PARAMETER(GENERATED_VAR_PREFIX + key,compilerUtil.getPastTypeForDeclaredType(theVar, key)))
                .collect(Collectors.toList());

        method.BODY(RETURN(LAMBDA(parameters).BODY(
                DEFINITION(STRING_BUILDER, VARIABLE(SB_VAR), CONSTRUCTOR_CALL(STRING_BUILDER, List.of())),
                METHOD_CALL(VARIABLE(SELF_VAR), "sqlTuple", makeRenamedArgsList2(SB_VAR,variables)),
                RETURN(METHOD_CALL(VARIABLE(SB_VAR), "toString", List.of()))
        )));

        return method;
    }



    public Method generateArgsToRecordMethod(String template, String templateFullQualifiedName, String packge, TemplateBindingsSchema bindingsSchema) {

        final ParameterizedType processorClassName = processorClassType(template, packge, OBJECT_ARRAY);
        final TypeName processorClassNameNotParametrised = processorClassTypeNotParametrised2(template, packge);
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



    public Method generateProcessorConverter(String processorConverter, String template, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection beanDirection) {

        final ParameterizedType returnClassName= beanDirection==BeanDirection.COMMON ? processorClassType(template, packge, T()) : processorClassType(template, packge,beanDirection, T());

        final TypeName returnClassNameNotParametrised = beanDirection==BeanDirection.COMMON ? processorClassTypeNotParametrised(template, packge): integratorClassType (template, packge);
        Method method = METHOD(processorConverter)
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
        method.COMMENT("@return $T&lt;$T&gt;\n", returnClassNameNotParametrised, com.squareup.javapoet.TypeVariableName.get("T"));

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        List<Parameter> parameters = new LinkedList<>();
        List<Expression> arguments = new LinkedList<>();


        for (String key : fieldNames) {
            String newKey = compilerUtil.generateNewNameForVariable(key);
            boolean isOutput=descriptorUtils.isOutput(key,bindingsSchema);
            boolean isInput=descriptorUtils.isInput(key,bindingsSchema);
            switch (beanDirection) {
                case INPUTS -> {
                    if (isInput) {
                        parameters.add(PARAMETER(newKey,compilerUtil.getPastTypeForDeclaredType(theVar, key)));
                        arguments.add(VARIABLE(newKey));
                    } else {
                        arguments.add(Constant.getNull());
                    }
                }
                case OUTPUTS -> {
                    if (isOutput) {
                        parameters.add(PARAMETER(newKey,compilerUtil.getPastTypeForDeclaredType(theVar, key)));
                        arguments.add(VARIABLE(newKey));
                    } else {
                        arguments.add(Constant.getNull());
                    }
                }
                case COMMON -> {
                    parameters.add(PARAMETER(newKey,compilerUtil.getPastTypeForDeclaredType(theVar, key)));
                    arguments.add(VARIABLE(newKey));
                }
            }


        }
        List<Expression> values = Stream.concat(Stream.of(METHOD_CALL("getFullyQualifiedName",List.of())), arguments.stream()).collect(Collectors.toList());
        method.BODY(RETURN(LAMBDA(parameters).BODY(RETURN(FUNCTIONAL_METHOD_CALL(VARIABLE(processor), "apply", List.of(ARRAY_INITIALISER(OBJECT,values)) )))));
        return method;
    }


    public Method generateProcessorConverter2(String template, String packge, TemplateBindingsSchema bindingsSchema, Set<String> foundSpecialTypes) {
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
            final ClassName declaredJavaType2 = compilerUtil.getPastTypeForDeclaredType(theVar, key);
            //final String type=declaredJavaType.getName();
            //final String converter2 = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
            String jsonSqlConverter=converterForJsonType(key, bindingsSchema, foundSpecialTypes);
            final Function<List<Expression>, Expression> converter2 = compilerUtil.getConverterForDeclaredType3(declaredJavaType);

            Expression expression;
            //
            if (converter2 == null && jsonSqlConverter==null) {
                expression= CAST(declaredJavaType2, ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)));
            } else {
                IfExpression ifexpression=
                        IF_(
                                BINARY_OP(
                                        ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)),
                                        EQ,
                                        getNull()))
                                .THEN(getNull());
                if (converter2 != null && jsonSqlConverter==null) {
                    ifexpression
                            .ELSE(IF_(INSTANCE_OF(ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)), STRING))
                                    .THEN(converter2.apply(List.of(CAST(STRING, ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count))))))
                                    .ELSE(CAST(declaredJavaType2, ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)))));
                } else {
                    ifexpression
                            .ELSE( METHOD_CALL(jsonSqlConverter, List.of(ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)))));
                }
                expression=ifexpression;

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
                .COMMENT("@return an object of type $T\n" , com.squareup.javapoet.TypeVariableName.get("T"));

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
        return ParameterizedType.get(ClassName.get(compilerUtil.processorNameClass(template),packge),t);
    }

    private ParameterizedType processorClassType(String template, String packge, BeanDirection beanDirection, TypeVariable t) {
        return ParameterizedType.get(ClassName.get(compilerUtil.processorNameClass(template,beanDirection),packge),t);
    }
    private ParameterizedType processorClassType(String template, String packge, BeanDirection beanDirection, ClassName t) {
        return ParameterizedType.get(ClassName.get(compilerUtil.processorNameClass(template,beanDirection),packge),t);
    }

    private ParameterizedType integratorClassType(String template, String packge, BeanDirection beanDirection, TypeVariable t) {
        return ParameterizedType.get(get(compilerUtil.integratorNameClass(template, beanDirection),packge),t);
    }

    private ParameterizedType processorClassType(String template, String packge, ArrayType arrayTypeName) {
        return ParameterizedType.get(get(compilerUtil.processorNameClass(template),packge),arrayTypeName);
    }

    private TypeName processorClassType(String template, String packge, ClassName cl) {
        return ParameterizedType.get(ClassName.get(compilerUtil.processorNameClass(template),packge),cl);
    }

    private TypeName processorClassTypeNotParametrised2(String template, String packge) {
        return ClassName.get(compilerUtil.processorNameClass(template), packge);
    }
    private TypeName integratorClassType(String template, String packge) {
        return ClassName.get(packge,compilerUtil.integratorNameClass(template, BeanDirection.INPUTS));
    }

    public Method generateLoggerMethod(String template, String templateFullyQualifiedName, TemplateBindingsSchema bindingsSchema) {
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
            final ClassName clazz1 = compilerUtil.getPastTypeForDeclaredType(theVar, key);
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

    static public void generateUnsupportedException(Method builder) {
        builder.BODY(METHOD_CALL(  "throw",
                List.of(CONSTRUCTOR_CALL(UNSUPPORTED_OPERATION_EXCEPTION, List.of()))));
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

        method.BODY(DEFINITION(MAP_INTEGER_INTARRAY, VARIABLE(TABLE), CONSTRUCTOR_CALL(HASH_MAP_INTEGER_INTARRAY, List.of())));

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

        method.BODY(DEFINITION(MAP_STRING_MAP_STRING_INTARRAY, VARIABLE(Constants.TABLE_VAR), CONSTRUCTOR_CALL(HASH_MAP_STRING_MAP_STRING_INTARRAY, List.of())));
        method.BODY(DEFINITION(MAP_STRING_INTARRAY, VARIABLE("map2"), CONSTRUCTOR_CALL(HASH_MAP_STRING_INTARRAY, List.of())));

        for (Map.Entry<String, Map<String, int[]>> entry: relations.entrySet()) {
            String rel=entry.getKey();
            Map<String, int[]> map2=entry.getValue();

            method.BODY(ASSIGNMENT( VARIABLE("map2"), CONSTRUCTOR_CALL(HASH_MAP_STRING_INTARRAY, List.of())));

            for (Map.Entry<String, int[]> entry2: map2.entrySet()) {
                String key2=entry2.getKey();
                int [] values2=entry2.getValue();
                List<Expression> constants2=Arrays.stream(values2).mapToObj(Constant::new).collect(Collectors.toList());

                method.BODY(METHOD_CALL(VARIABLE("map2"), "put", List.of(CONSTANT(key2), ARRAY_INITIALISER(_int,constants2))));


            }
            method.BODY(METHOD_CALL(VARIABLE(Constants.TABLE_VAR), "put", List.of(CONSTANT(rel), VARIABLE("map2"))));
        }
        method.BODY(RETURN(VARIABLE(Constants.TABLE_VAR)));
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

        method.BODY(DEFINITION(MAP_INTEGER_INTARRAY, VARIABLE(Constants.TABLE_VAR), CONSTRUCTOR_CALL(HASH_MAP_INTEGER_INTARRAY, List.of())));



        for (Map.Entry<Integer, List<Integer>> entry: table.entrySet()) {
            Integer key=entry.getKey();
            List<Integer> values=entry.getValue();
            List<Expression> constants=values.stream().map(Constant::new).collect(Collectors.toList());
            method.BODY(METHOD_CALL(VARIABLE(Constants.TABLE_VAR), "put", List.of(CONSTANT(key), ARRAY_INITIALISER(_int,constants))));
        }
        method.BODY(RETURN(VARIABLE(Constants.TABLE_VAR)));

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
                List<Integer> rowValues=new LinkedList<>();
                boolean first = true;
                for (Pair<QualifiedName, WasDerivedFrom> successor : successors1) {
                    Integer i = index.get(successor.getLeft());
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor.getRight()));
                }
                for (Pair<QualifiedName, WasAttributedTo> successor2 : successors2) {
                    int i = index.get(successor2.getLeft());
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor2.getRight()));
                }
                for (Pair<QualifiedName, HadMember> successor3 : successors3) {
                    int i = index.get(successor3.getLeft());
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor3.getRight()));
                }
                for (Pair<QualifiedName, SpecializationOf> successor4 : successors4) {
                    int i = index.get(successor4.getLeft());
                    if (first) {
                        first = false;
                    } else {
                    }
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

        method.BODY(DEFINITION(STRING_ARRAY, VARIABLE(Constants.TABLE_VAR), ARRAY_ALLOCATOR(STRING, CONSTANT(knownTypes.size()))));
        int count=0;
        for (String s: knownTypes) {
            method.BODY(ASSIGNMENT( ARRAY_ACCESSOR(VARIABLE(Constants.TABLE_VAR), CONSTANT(count)), CONSTANT(s)));
            count++;
        }
        method.BODY(RETURN(VARIABLE(Constants.TABLE_VAR)));
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
        method.BODY(DEFINITION(get(compilerUtil.commonNameClass(template),packge), VARIABLE(BEAN_VAR), CONSTRUCTOR_CALL(get(compilerUtil.commonNameClass(template),packge), List.of())));
        for (String key: variables) {
            String newkey = compilerUtil.generateNewNameForVariable(key);
            method.PARAMETER(compilerUtil.getPastTypeForDeclaredType(bindingsSchema.getVar(), key), newkey);
            method.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE(BEAN_VAR), key), VARIABLE(newkey)));
        }
        method.BODY(RETURN(VARIABLE(BEAN_VAR)));
        return method;
    }


    public Method generateFactoryMethodToBeanWithArrayComposite(String toBean, String template, String packge, TemplateBindingsSchema bindingsSchema, String loggerPackage, String logger, BeanDirection direction, String extension, List<String> sharing) {
        ClassName className = get(compilerUtil.beanNameClass(template, direction), packge);
        Method method = METHOD(toBean)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(className);
        compilerUtil.debugFileLocation(method);

        if (extension!=null) {
            method.COMMENT("Refers to variant $S, sharing variables $L", extension, sharing.toString());
        }


        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        method.PARAMETER(LIST_OF_OBJECT_ARRAYS, "records");

        method.BODY(
                DEFINITION(OBJECT_ARRAY, VARIABLE("record"), METHOD_CALL(VARIABLE("records"), "get", List.of(CONSTANT(0)))),
                DEFINITION(className, VARIABLE("bean"), CONSTRUCTOR_CALL(className, List.of()))
        );

        int count = 1;
        for (String key: variables) {
            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
            final ClassName declaredJavaType2 = compilerUtil.getPastTypeForDeclaredType(bindingsSchema.getVar(), key);
            //final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
            final Function<List<Expression>, Expression> converter = compilerUtil.getConverterForDeclaredType3(declaredJavaType);

            if (direction==BeanDirection.COMMON || descriptorUtils.isInput(key,bindingsSchema) || (sharing!=null) && sharing.contains(key)) {
                if (converter == null) {
                    method.BODY(
                            ASSIGNMENT(  METHOD_CALL(VARIABLE("bean"), key), CAST(declaredJavaType2, ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count))))

                    );
                } else {


                    method.BODY(
                            ASSIGNMENT(
                                    METHOD_CALL(VARIABLE("bean"), key),
                                    IF_(
                                            BINARY_OP(ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)), "==", Constant.getNull()))
                                            .THEN(Constant.getNull())
                                            .ELSE(IF_(INSTANCE_OF(ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)), STRING))
                                                    .THEN(converter.apply(List.of(CAST(STRING, ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count))))))
                                                    .ELSE(CAST(declaredJavaType2, ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(count)))))));

                }
            }
            count++;
        }

        method.BODY(

                ASSIGNMENT( METHOD_CALL(VARIABLE("bean"), ELEMENTS), CONSTRUCTOR_CALL(LINKED_LIST, List.of())),

                FOR(
                        DEFINITION(_int, VARIABLE(_I_), CONSTANT(1)),
                        BINARY_OP(VARIABLE(_I_), "<", METHOD_CALL(VARIABLE("records"), "size", List.of())),
                        ASSIGNMENT( VARIABLE(_I_), BINARY_OP(VARIABLE(_I_), "+", CONSTANT(1))))

                        .BODY(
                                extension==null ?

                                        METHOD_CALL(
                                                VARIABLE("bean"),
                                                ADD_ELEMENTS,
                                                List.of(
                                                        FUNCTIONAL_METHOD_CALL(
                                                                METHOD_CALL(
                                                                        METHOD_CALL(
                                                                                get(logger, loggerPackage),
                                                                                "simpleBeanConverters"),
                                                                        "get",
                                                                        List.of(
                                                                                ARRAY_ACCESSOR(METHOD_CALL(VARIABLE("records"), "get", List.of(VARIABLE(_I_))), CONSTANT(0)))),
                                                                "apply",
                                                                List.of(METHOD_CALL(VARIABLE("records"), "get", List.of(VARIABLE(_I_))))))):

                                        //  COMMENT("this code will only work if there is a single variant for this template"),
                                        METHOD_CALL(
                                                VARIABLE("bean"),
                                                ADD_ELEMENTS,
                                                List.of(
                                                        METHOD_CALL(
                                                                "toInputs" + extension,
                                                                List.of(
                                                                        METHOD_CALL(VARIABLE("records"), "get", List.of(VARIABLE(_I_)))
                                                                )
                                                        )
                                                )
                                        )
                        )
        );

        method.BODY(RETURN(VARIABLE("bean")));
        return method;
    }

    public Method generateFactoryMethodToBeanWithArray(Locations locations, String toBean, String template, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection direction, String extension, List<String> shared) {
        if (extension!=null) {
            template= locations.getShortNames().get(template);
        }
        Method method = METHOD(toBean)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(ClassName.get(compilerUtil.beanNameClass(template,direction,extension),packge));
        compilerUtil.debugFileLocation(method);


        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        method.PARAMETER(OBJECT_ARRAY, "record");

        ClassName className = get(compilerUtil.beanNameClass(template,direction,extension),packge);
        method.addStatement(DEFINITION(className,VARIABLE(BEAN_VAR),CONSTRUCTOR_CALL(className,List.of())));

        method.COMMENT("Converter to bean of type $T for template $N.\n", className, template);
        if (shared!=null) {
            method.COMMENT("Variant $N of class $T to support shared variables $N\n", extension, ClassName.get(packge,compilerUtil.beanNameClass(template,direction)), shared.toString());
        }
        method.COMMENT("@param record an array of objects\n");
        method.COMMENT("@return a bean\n");

        int count = 1;
        for (String key: variables) {
            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
            final ClassName declaredJavaType2 = compilerUtil.getPastTypeForDeclaredType(bindingsSchema.getVar(), key);
            final Function<List<Expression>, Expression> converter = compilerUtil.getConverterForDeclaredType3(declaredJavaType);

            if (direction==BeanDirection.COMMON
                    || descriptorUtils.isInput(key,bindingsSchema)
                    || (shared!=null && shared.contains(key))) {
                if (converter == null) {
                    method.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE(BEAN_VAR),key),
                            CAST(declaredJavaType2,ARRAY_ACCESSOR(VARIABLE("record"),CONSTANT(count)))));
                } else {
                    method.BODY(
                            ASSIGNMENT( METHOD_CALL(VARIABLE(BEAN_VAR),key),
                                    IFEXPRESSION(
                                            BINARY_OP(ARRAY_ACCESSOR(VARIABLE("record"),CONSTANT(count)),EQ,getNull()),
                                            getNull(),
                                            IFEXPRESSION(
                                                    INSTANCE_OF(ARRAY_ACCESSOR(VARIABLE("record"),CONSTANT(count)),STRING),
                                                    converter.apply(List.of(CAST(STRING,ARRAY_ACCESSOR(VARIABLE("record"),CONSTANT(count))))),
                                                    CAST(declaredJavaType2,ARRAY_ACCESSOR(VARIABLE("record"),CONSTANT(count)))))));

                }
            }
            count++;
        }
        method.BODY(RETURN(VARIABLE(BEAN_VAR)));



        return method;
    }


    public Method generateNewBean(String template, String packge) {
        ClassName beanClass = get(compilerUtil.commonNameClass(template), packge);
        Method builder =METHOD("newBean")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(beanClass);
        compilerUtil.debugFileLocation(builder);
        builder.BODY(DEFINITION(beanClass, VARIABLE(BEAN_VAR), CONSTRUCTOR_CALL(beanClass, List.of())));
        builder.addStatement(RETURN(VARIABLE(BEAN_VAR)));
        return builder;
    }




    public Method generateExamplarBean(String template, String packge, TemplateBindingsSchema bindingsSchema) {
        ClassName commonName = get(compilerUtil.commonNameClass(template), packge);
        Method builder = METHOD("examplar")
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(commonName);

        compilerUtil.debugFileLocation(builder);

        builder.BODY(DEFINITION(commonName, VARIABLE(BEAN_VAR), CONSTRUCTOR_CALL(commonName, List.of())));

        Map<String, List<Descriptor>> theVars = bindingsSchema.getVar();
        Collection<String> nameVariables = descriptorUtils.getNameVariables(bindingsSchema);
        Collection<String> attrVariables = descriptorUtils.getAttributeVariables(bindingsSchema);


        for (String aVar : nameVariables) {
            List<Descriptor> descriptors = theVars.get(aVar);
            Descriptor qDescriptor = (descriptors == null) ? null : descriptors.get(0);
            String idType = (qDescriptor == null) ? null : descriptorUtils.getFromDescriptor(qDescriptor, AttributeDescriptor::getType, NameDescriptor::getType);
            Object examplar = (qDescriptor == null) ? null : descriptorUtils.getFromDescriptor(qDescriptor, AttributeDescriptor::getExamplar, NameDescriptor::getExamplar);

            if (idType == null) {
                builder.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE(BEAN_VAR),aVar), CONSTANT("example_" + aVar)));
            } else {
                String example = (examplar == null) ? compilerUtil.generateExampleForType(idType, aVar, pFactory) : examplar.toString();
                Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(theVars, aVar);

                final Function<List<Expression>, Expression> converter = compilerUtil.getConverterForDeclaredType3(declaredJavaType);
                if (converter == null) {
                    builder.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE(BEAN_VAR),aVar),CONSTANT(example)));
                } else {
                    builder.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE(BEAN_VAR),aVar),converter.apply(List.of(CONSTANT(example)))));
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

                final Function<List<Expression>, Expression> converter = compilerUtil.getConverterForDeclaredType3(declaredJavaType);
                if (converter == null) {
                    builder.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE(BEAN_VAR),aVar),CONSTANT(example)));
                } else {
                    builder.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE(BEAN_VAR),aVar),converter.apply(List.of(CONSTANT(example)))));
                }
            }
        }

        builder.BODY(RETURN(VARIABLE(BEAN_VAR)));
        return builder;

    }



}
