package org.openprovenance.prov.template.compiler.sql;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.annotations.OverrideAnnotation;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constant.getNull;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.SuperConstructorCall.SUPER_CALL;
import static org.openprovenance.prov.template.compiler.past.ThrowStatement.THROW;
import static org.openprovenance.prov.template.compiler.past.TryCatch.TRY_CATCH;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerSqlIntegration {
    private final CompilerUtil compilerUtil;


    private final PastFactory pastFactory = new PastFactory();

    public CompilerSqlIntegration(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
    }

    /** Helper to build the anonymous Getter class with try/catch wrapping rs.getObject(col,cl). */
    private Class buildGetterAnonymousClass(TemplatesProjectConfiguration configs, Locations locations) {
        return pastFactory
                .CLASS(null, Class.ClassKind.ANONYMOUS)
                .INTERFACES(ClassName.get(Constants.GETTER, locations.getFilePackage(configs.name, Constants.GETTER)))
                .METHOD(
                        METHOD("get")
                                .MODIFIERS(Modifier.PUBLIC)
                                .PARAMETER(CLASS_T, "cl")
                                .PARAMETER(STRING, "col")
                                .RETURNS(TypeVariable.T())
                                .addTypeVariables(TypeVariable.T())
                                .BODY(
                                        TRY_CATCH(SQL_EXCEPTION, "e")
                                                .TRY(
                                                        RETURN(METHOD_CALL(VARIABLE("rs"), "getObject", List.of(VARIABLE("col"), VARIABLE("cl"))))
                                                )
                                                .CATCH(
                                                        METHOD_CALL(VARIABLE("e"), "printStackTrace", List.of()),
                                                        THROW(CONSTRUCTOR_CALL(RUNTIME_EXCEPTION, List.of(VARIABLE("e"))))
                                                )
                                )
                );
    }

    /** Helper for try { return rs.next(); } catch (SQLException e) { throw new RuntimeException(e); } */
    private TryCatch buildNextTryCatch() {
        return TRY_CATCH(SQL_EXCEPTION, "e")
                .TRY(
                        RETURN(METHOD_CALL(VARIABLE("rs"), "next", List.of()))
                )
                .CATCH(
                        THROW(CONSTRUCTOR_CALL(RUNTIME_EXCEPTION, List.of(VARIABLE("e"))))
                );
    }

    /** Helper for the generic_enact try/catch body used in EnactorImplementation and IntegratorEnactorImplementation. */
    private TryCatch buildGenericEnactTryCatch(String returnVar) {
        return TRY_CATCH(SQL_EXCEPTION, "e")
                .TRY(
                        ASSIGNMENT(VARIABLE("rs"), METHOD_CALL(VARIABLE("querier"), "apply", List.of(VARIABLE("statement")))),
                        IF(BINARY_OP(METHOD_CALL(VARIABLE("rs"), "next", List.of()), "==", CONSTANT(false)))
                                .THEN(
                                        METHOD_CALL(VARIABLE("rs"), "close", List.of()),
                                        THROW(CONSTRUCTOR_CALL(SQL_EXCEPTION, List.of(CONSTANT("Single row result was expected but result set is empty "))))
                                ),
                        METHOD_CALL(VARIABLE("completeBean"), "accept", List.of(VARIABLE("rs"), VARIABLE(returnVar))),
                        IF(METHOD_CALL(VARIABLE("rs"), "next", List.of()))
                                .THEN(
                                        METHOD_CALL(VARIABLE("rs"), "close", List.of()),
                                        THROW(CONSTRUCTOR_CALL(SQL_EXCEPTION, List.of(CONSTANT("Single row result was expected for query "))))
                                )
                                .ELSE(
                                        METHOD_CALL(VARIABLE("rs"), "close", List.of())
                                )
                )
                .CATCH(
                        METHOD_CALL(VARIABLE("e"), "printStackTrace", List.of()),
                        THROW(CONSTRUCTOR_CALL(UNCHECKED_EXCEPTION, List.of(
                                BINARY_OP(CONSTANT("Issue in enactment "), "+", VARIABLE("statement")),
                                VARIABLE("e"))))
                );
    }


    public SpecificationFile generateSqlIntegration_BeanCompleter(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.BEAN_COMPLETER, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER));

        Constructor constructor = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(buildGetterAnonymousClass(configs, locations), List.of()))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_COMPLETER)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeBeanCompleter(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.SQL_BEAN_COMPLETER, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPLETER));

        Constructor constructor = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        SUPER_CALL(VARIABLE("rs")),
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "rs"), VARIABLE("rs"))
                );

        Method nextMethod = METHOD("next")
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(_bool)
                .BODY(buildNextTryCatch());

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_COMPLETER_COMPOSITE)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .FIELDS(
                        FIELD("rs", RESULT_SET).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                )
                .CONSTRUCTOR(constructor)
                .METHOD(nextMethod);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_BeanCompleter3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.BEAN_COMPLETER2, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER2));
        ClassName getterType = ClassName.get(Constants.GETTER, locations.getFilePackage(configs.name, Constants.GETTER));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        SUPER_CALL(METHOD_CALL("newGetter", List.of(VARIABLE("rs"))))
                );

        Method staticMethod = METHOD("newGetter")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(getterType)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        RETURN(CONSTRUCTOR_CALL(buildGetterAnonymousClass(configs, locations), List.of()))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_COMPLETER3)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor)
                .METHOD(staticMethod);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_BeanCompleter4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.SQL_BEAN_COMPLETER3, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPLETER3));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .BODY(
                        SUPER_CALL(VARIABLE("rs")),
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), Constants.POST_PROCESSING_VAR), VARIABLE(Constants.POST_PROCESSING_VAR))
                );

        Method postMethod = METHOD(Constants.POST_PROCESS_METHOD_NAME)
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(INTEGER, "id")
                .PARAMETER(STRING, "template")
                .RETURNS(VOID)
                .BODY(
                        IF(BINARY_OP(VARIABLE(Constants.POST_PROCESSING_VAR), "!=", getNull()))
                                .THEN(
                                        METHOD_CALL(VARIABLE(Constants.POST_PROCESSING_VAR), "apply", List.of(VARIABLE("id"), VARIABLE("template")))
                                )
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_COMPLETER4)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .FIELDS(
                        FIELD(Constants.POST_PROCESSING_VAR, BIFUNCTION_INTEGER_STRING_OBJECT).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                )
                .CONSTRUCTOR(constructor)
                .METHOD(postMethod);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_EnactorImplementation(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName enactorImpl1 = ClassName.get(ENACTOR_IMPLEMENTATION1, locations.getFilePackage(configs.name, ENACTOR_IMPLEMENTATION1));
        ParameterizedType paramEnactorImpl1 = ParameterizedType.get(enactorImpl1, RESULT_SET);

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .BODY(
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "querier"), VARIABLE("querier"))
                );

        // generic_enact method
        Method genericEnact = METHOD("generic_enact")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .addTypeVariables(TypeVariable.T())
                .PARAMETER(TypeVariable.T(), "bean")
                .PARAMETER(ParameterizedType.get(CONSUMER, TypeVariable.T()), "check")
                .PARAMETER(ParameterizedType.get(BICONSUMER, STRING_BUILDER, TypeVariable.T()), "composeQuery")
                .PARAMETER(ParameterizedType.get(BICONSUMER, RESULT_SET, TypeVariable.T()), "completeBean")
                .RETURNS(TypeVariable.T())
                .BODY(
                        METHOD_CALL(VARIABLE("check"), "accept", List.of(VARIABLE("bean"))),
                        DEFINITION(STRING_BUILDER, VARIABLE("sb"), CONSTRUCTOR_CALL(STRING_BUILDER, List.of())),
                        METHOD_CALL(VARIABLE("composeQuery"), "accept", List.of(VARIABLE("sb"), VARIABLE("bean"))),
                        DEFINITION(STRING, VARIABLE("statement"), METHOD_CALL(VARIABLE("sb"), "toString", List.of())),
                        DEFINITION(RESULT_SET, VARIABLE("rs"), getNull()),
                        buildGenericEnactTryCatch("bean"),
                        RETURN(VARIABLE("bean"))
                );

        // beanCompleterFactory method
        ClassName beanCompleterType = ClassName.get(Constants.BEAN_COMPLETER, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER));
        ClassName sqlBeanCompleterType = ClassName.get(Constants.SQL_BEAN_COMPLETER, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPLETER));

        Method beanCompleterFactory = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleterType)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        RETURN(CONSTRUCTOR_CALL(sqlBeanCompleterType, List.of(VARIABLE("rs"))))
                );

        // printResultSet static method
        Method printResultSet = METHOD("printResultSet")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .PARAMETER(RESULT_SET, "resultSet")
                .RETURNS(VOID)
                .BODY(
                        TRY_CATCH(SQL_EXCEPTION, "e")
                                .TRY(
                                        DEFINITION(RESULT_SET_META_DATA, VARIABLE("rsmd"), METHOD_CALL(VARIABLE("resultSet"), "getMetaData", List.of())),
                                        DEFINITION(_int, VARIABLE("columnsNumber"), METHOD_CALL(VARIABLE("rsmd"), "getColumnCount", List.of())),
                                        DoLoop.DO()
                                                .BODY(
                                                        new Comment("in generic_enact, we have already done next(), we are on the first record"),
                                                        METHOD_CALL(SYSTEM, "out.print", List.of(
                                                                BINARY_OP(BINARY_OP(CONSTANT("Row "), "+", METHOD_CALL(VARIABLE("resultSet"), "getRow", List.of())), "+", CONSTANT(" "))
                                                        )),
                                                        new ForLoop(
                                                                new Definition(_int, new Variable("i"), CONSTANT(1)),
                                                                BINARY_OP(VARIABLE("i"), "<=", VARIABLE("columnsNumber")),
                                                                new Assignment(VARIABLE("i"), new PostIncrement(VARIABLE("i"), 1)),
                                                                null
                                                        ).BODY(
                                                                IF(BINARY_OP(VARIABLE("i"), ">", CONSTANT(1)))
                                                                        .THEN(
                                                                                METHOD_CALL(SYSTEM, "out.print", List.of(CONSTANT(",  ")))
                                                                        ),
                                                                DEFINITION(STRING, VARIABLE("columnValue"), METHOD_CALL(VARIABLE("resultSet"), "getString", List.of(VARIABLE("i")))),
                                                                METHOD_CALL(SYSTEM, "out.print", List.of(
                                                                        BINARY_OP(BINARY_OP(VARIABLE("columnValue"), "+", CONSTANT(" ")), "+", METHOD_CALL(VARIABLE("rsmd"), "getColumnName", List.of(VARIABLE("i"))))
                                                                ))
                                                        ),
                                                        METHOD_CALL(SYSTEM, "out.println", List.of(CONSTANT("")))
                                                )
                                                .WHILE(METHOD_CALL(VARIABLE("resultSet"), "next", List.of()))
                                )
                                .CATCH(
                                        THROW(CONSTRUCTOR_CALL(RUNTIME_EXCEPTION, List.of(VARIABLE("e"))))
                                )
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_ENACTOR_IMPLEMENTATION)
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(paramEnactorImpl1)
                .FIELDS(
                        FIELD("querier", FUNCTION_STRING_RESULTSET).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                )
                .CONSTRUCTOR(constructor)
                .METHOD(genericEnact)
                .METHOD(beanCompleterFactory)
                .METHOD(printResultSet);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_IntegratorEnactorImplementation(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName enactorImpl = ClassName.get(ENACTOR_IMPLEMENTATION, locations.getFilePackage(configs.name, Constants.ENACTOR_IMPLEMENTATION));
        ParameterizedType paramEnactorImpl = ParameterizedType.get(enactorImpl, RESULT_SET);

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .BODY(
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "querier"), VARIABLE("querier"))
                );

        // generic_enact method with IN, OUT type variables
        Method genericEnact = METHOD("generic_enact")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .addTypeVariables(TYPE_IN)
                .addTypeVariables(TYPE_OUT)
                .PARAMETER(TYPE_OUT, "output")
                .PARAMETER(TYPE_IN, "bean")
                .PARAMETER(CONSUMER_OF_IN, "check")
                .PARAMETER(BICONSUMER_STRINGBUILDER_TYPEIN, "composeQuery")
                .PARAMETER(ParameterizedType.get(BICONSUMER, RESULT_SET, TYPE_OUT), "completeBean")
                .RETURNS(TYPE_OUT)
                .BODY(
                        METHOD_CALL(VARIABLE("check"), "accept", List.of(VARIABLE("bean"))),
                        DEFINITION(STRING_BUILDER, VARIABLE("sb"), CONSTRUCTOR_CALL(STRING_BUILDER, List.of())),
                        METHOD_CALL(VARIABLE("composeQuery"), "accept", List.of(VARIABLE("sb"), VARIABLE("bean"))),
                        DEFINITION(STRING, VARIABLE("statement"), METHOD_CALL(VARIABLE("sb"), "toString", List.of())),
                        DEFINITION(RESULT_SET, VARIABLE("rs"), getNull()),
                        buildGenericEnactTryCatch("output"),
                        RETURN(VARIABLE("output"))
                );

        // beanCompleterFactory methods
        ClassName beanCompleter2Type = ClassName.get(Constants.BEAN_COMPLETER2, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER2));
        ClassName sqlBeanCompleter3Type = ClassName.get(Constants.SQL_BEAN_COMPLETER3, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPLETER3));

        Method beanCompleterFactory = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleter2Type)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        RETURN(CONSTRUCTOR_CALL(sqlBeanCompleter3Type, List.of(VARIABLE("rs"))))
                );

        Method beanCompleterFactory2 = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleter2Type)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(INTEGER_ARRAY, "extra")
                .BODY(
                        RETURN(CONSTRUCTOR_CALL(sqlBeanCompleter3Type, List.of(VARIABLE("rs"))))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_ENACTOR_IMPLEMENTATION3)
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(paramEnactorImpl)
                .FIELDS(
                        FIELD("querier", FUNCTION_STRING_RESULTSET).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                )
                .CONSTRUCTOR(constructor)
                .METHOD(genericEnact)
                .METHOD(beanCompleterFactory)
                .METHOD(beanCompleterFactory2);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_IntegratorEnactorImplementation4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.SQL_ENACTOR_IMPLEMENTATION3, locations.getFilePackage(configs.name, Constants.SQL_ENACTOR_IMPLEMENTATION3));
        ClassName enactorImpl = ClassName.get(ENACTOR_IMPLEMENTATION, locations.getFilePackage(configs.name, Constants.ENACTOR_IMPLEMENTATION));
        ParameterizedType paramEnactorImpl = ParameterizedType.get(enactorImpl, RESULT_SET);
        ClassName enactorImpl4 = ClassName.get(ENACTOR_IMPLEMENTATION4, locations.getFilePackage(configs.name, Constants.ENACTOR_IMPLEMENTATION4));
        ParameterizedType paramEnactorImpl4 = ParameterizedType.get(enactorImpl4, RESULT_SET);

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .BODY(
                        SUPER_CALL(VARIABLE("querier")),
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "querier"), VARIABLE("querier"))
                );

        ClassName beanCompleter2Type = ClassName.get(Constants.BEAN_COMPLETER2, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER2));
        ClassName sqlBeanCompleter4Type = ClassName.get(Constants.SQL_BEAN_COMPLETER4, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPLETER4));

        Method beanCompleterFactory3 = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleter2Type)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(INTEGER_ARRAY, "extra")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .BODY(
                        METHOD_CALL(METHOD_CALL(SYSTEM, "out"), "println", List.of(
                                BINARY_OP(CONSTANT("LUC In beanCompleterFactory 4 with extra of length "), "+",
                                        IfExpression.IFEXPRESSION(BINARY_OP(VARIABLE("extra"), "==", getNull()), CONSTANT(0), METHOD_CALL(VARIABLE("extra"), "length")))
                        )),
                        RETURN(CONSTRUCTOR_CALL(sqlBeanCompleter4Type, List.of(VARIABLE("rs"), VARIABLE(Constants.POST_PROCESSING_VAR))))
                );

        Method beanCompleterFactory4 = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleter2Type)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .BODY(
                        METHOD_CALL(METHOD_CALL(SYSTEM, "out"), "println", List.of(CONSTANT("LUC In beanCompleterFactory 4 without extra"))),
                        RETURN(CONSTRUCTOR_CALL(sqlBeanCompleter4Type, List.of(VARIABLE("rs"), VARIABLE(Constants.POST_PROCESSING_VAR))))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_ENACTOR_IMPLEMENTATION4)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
               // .INTERFACES(paramEnactorImpl), already implemented by superclass
                .INTERFACES(paramEnactorImpl4)
                .FIELDS(
                        FIELD("querier", FUNCTION_STRING_RESULTSET).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                )
                .CONSTRUCTOR(constructor)
                .METHOD(beanCompleterFactory3)
                .METHOD(beanCompleterFactory4);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeEnactorImplementation(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.SQL_ENACTOR_IMPLEMENTATION, locations.getFilePackage(configs.name, Constants.SQL_ENACTOR_IMPLEMENTATION));

        Constructor constructor = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .BODY(
                        SUPER_CALL(VARIABLE("querier"))
                );

        ClassName beanCompleterType = ClassName.get(Constants.BEAN_COMPLETER, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER));
        ClassName sqlCompositeBeanCompleterType = ClassName.get(Constants.SQL_BEAN_COMPLETER_COMPOSITE, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPLETER_COMPOSITE));

        Method beanCompleterFactory = METHOD("beanCompleterFactory")
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleterType)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        RETURN(CONSTRUCTOR_CALL(sqlCompositeBeanCompleterType, List.of(VARIABLE("rs"))))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor)
                .METHOD(beanCompleterFactory);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeBeanEnactor(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName beanEnactorType = ClassName.get(Constants.BEAN_ENACTOR, locations.getFilePackage(configs.name, Constants.BEAN_ENACTOR));
        ParameterizedType superclass = ParameterizedType.get(beanEnactorType, RESULT_SET);

        ClassName sqlCompositeEnactorImplType = ClassName.get(Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION, locations.getFilePackage(configs.name, Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION));
        ClassName beanCheckerType = ClassName.get(Constants.BEAN_CHECKER, locations.getFilePackage(configs.name, Constants.BEAN_CHECKER));

        Constructor constructor = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(sqlCompositeEnactorImplType, List.of(VARIABLE("querier"))),
                                CONSTRUCTOR_CALL(beanCheckerType, List.of()))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_COMPOSITE_ENACTOR)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_BeanEnactor(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName beanEnactorType = ClassName.get(Constants.BEAN_ENACTOR, locations.getFilePackage(configs.name, Constants.BEAN_ENACTOR));
        ParameterizedType superclass = ParameterizedType.get(beanEnactorType, RESULT_SET);

        ClassName sqlEnactorImplType = ClassName.get(Constants.SQL_ENACTOR_IMPLEMENTATION, locations.getFilePackage(configs.name, Constants.SQL_ENACTOR_IMPLEMENTATION));
        ClassName beanCheckerType = ClassName.get(Constants.BEAN_CHECKER, locations.getFilePackage(configs.name, Constants.BEAN_CHECKER));

        Constructor constructor = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(sqlEnactorImplType, List.of(VARIABLE("querier"))),
                                CONSTRUCTOR_CALL(beanCheckerType, List.of()))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_ENACTOR)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_BeanEnactor3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName beanEnactor2Type = ClassName.get(Constants.BEAN_ENACTOR2, locations.getFilePackage(configs.name, Constants.BEAN_ENACTOR2));
        ParameterizedType superclass = ParameterizedType.get(beanEnactor2Type, RESULT_SET);

        ClassName sqlCompositeEnactorImpl3Type = ClassName.get(Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION_3, locations.getFilePackage(configs.name, Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION_3));
        ClassName beanChecker2Type = ClassName.get(Constants.BEAN_CHECKER2, locations.getFilePackage(configs.name, Constants.BEAN_CHECKER2));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .PARAMETER(SUPPLIER_OF_STRING, Constants.PRINCIPAL_MANAGER_VAR)
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(sqlCompositeEnactorImpl3Type, List.of(VARIABLE("querier"))),
                                CONSTRUCTOR_CALL(beanChecker2Type, List.of()))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_ENACTOR3)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_BeanEnactor4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName beanEnactor2WPType = ClassName.get(Constants.BEAN_ENACTOR2_WP, locations.getFilePackage(configs.name, Constants.BEAN_ENACTOR2_WP));
        ParameterizedType superclass = ParameterizedType.get(beanEnactor2WPType, RESULT_SET);

        ClassName sqlCompositeEnactorImpl4Type = ClassName.get(Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION_4, locations.getFilePackage(configs.name, Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION_4));
        ClassName beanChecker2Type = ClassName.get(Constants.BEAN_CHECKER2, locations.getFilePackage(configs.name, Constants.BEAN_CHECKER2));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .PARAMETER(SUPPLIER_OF_STRING, Constants.PRINCIPAL_MANAGER_VAR)
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(sqlCompositeEnactorImpl4Type, List.of(VARIABLE("querier"))),
                                CONSTRUCTOR_CALL(beanChecker2Type, List.of()),
                                VARIABLE(Constants.POST_PROCESSING_VAR),
                                VARIABLE(Constants.PRINCIPAL_MANAGER_VAR))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_ENACTOR4)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeEnactorImplementation3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.SQL_ENACTOR_IMPLEMENTATION3, locations.getFilePackage(configs.name, Constants.SQL_ENACTOR_IMPLEMENTATION3));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .BODY(
                        SUPER_CALL(VARIABLE("querier"))
                );

        ClassName beanCompleter2Type = ClassName.get(Constants.BEAN_COMPLETER2, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER2));
        ClassName sqlCompositeBeanCompleter3Type = ClassName.get(Constants.SQL_BEAN_COMPOSITE_COMPLETER_3, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPOSITE_COMPLETER_3));

        Method beanCompleterFactory = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleter2Type)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        RETURN(CONSTRUCTOR_CALL(sqlCompositeBeanCompleter3Type, List.of(VARIABLE("rs"))))
                );

        Method beanCompleterFactory2 = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleter2Type)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(INTEGER_ARRAY, "extra")
                .BODY(
                        RETURN(CONSTRUCTOR_CALL(sqlCompositeBeanCompleter3Type, List.of(VARIABLE("rs"), VARIABLE("extra"))))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION_3)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor)
                .METHOD(beanCompleterFactory)
                .METHOD(beanCompleterFactory2);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeEnactorImplementation4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.SQL_ENACTOR_IMPLEMENTATION4, locations.getFilePackage(configs.name, Constants.SQL_ENACTOR_IMPLEMENTATION4));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .BODY(
                        SUPER_CALL(VARIABLE("querier"))
                );

        ClassName beanCompleter2Type = ClassName.get(Constants.BEAN_COMPLETER2, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER2));
        ClassName sqlCompositeBeanCompleter4Type = ClassName.get(Constants.SQL_BEAN_COMPOSITE_COMPLETER_4, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPOSITE_COMPLETER_4));

        Method beanCompleterFactory = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleter2Type)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        RETURN(CONSTRUCTOR_CALL(sqlCompositeBeanCompleter4Type, List.of(VARIABLE("rs"))))
                );

        Method beanCompleterFactory2 = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleter2Type)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(INTEGER_ARRAY, "extra")
                .BODY(
                        METHOD_CALL(SYSTEM, "out.println", List.of(
                                BINARY_OP(CONSTANT("LUCXXX In beanCompleterFactory 4 with extra of length "), "+",
                                        IfExpression.IFEXPRESSION(BINARY_OP(VARIABLE("extra"), "==", getNull()), CONSTANT(0), METHOD_CALL(VARIABLE("extra"), "length")))
                        )),
                        RETURN(CONSTRUCTOR_CALL(sqlCompositeBeanCompleter4Type, List.of(VARIABLE("rs"), VARIABLE("extra"))))
                );

        Method beanCompleterFactory3 = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleter2Type)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(INTEGER_ARRAY, "extra")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .BODY(
                        /*
                        METHOD_CALL(SYSTEM, "out.println", List.of(
                                BINARY_OP(CONSTANT("LUC YYYY In beanCompleterFactory 4 with extra of length "), "+",
                                        IfExpression.IFEXPRESSION(BINARY_OP(VARIABLE("extra"), "==", getNull()), CONSTANT(0), METHOD_CALL(VARIABLE("extra"), "length")))
                        )),

                         */
                        RETURN(CONSTRUCTOR_CALL(sqlCompositeBeanCompleter4Type, List.of(VARIABLE("rs"), VARIABLE("extra"), VARIABLE(Constants.POST_PROCESSING_VAR))))
                );

        Method beanCompleterFactory4 = METHOD("beanCompleterFactory")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(beanCompleter2Type)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .BODY(
                       // METHOD_CALL(SYSTEM, "out.println", List.of(CONSTANT("LUC YYYY In beanCompleterFactory No extra"))),
                        RETURN(CONSTRUCTOR_CALL(sqlCompositeBeanCompleter4Type, List.of(VARIABLE("rs"), VARIABLE(Constants.POST_PROCESSING_VAR))))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION_4)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor)
                .METHOD(beanCompleterFactory)
                .METHOD(beanCompleterFactory2)
                .METHOD(beanCompleterFactory3)
                .METHOD(beanCompleterFactory4);

        String myPackage = locations.getFilePackage(configs.name, fileName);
        String directory = locations.convertToBackendDirectory(myPackage);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeBeanCompleter3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.BEAN_COMPLETER3, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER3));
        ClassName sqlBeanCompleter3Type = ClassName.get(Constants.SQL_BEAN_COMPLETER3, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPLETER3));

        Constructor constructor1 = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        METHOD_CALL("this", List.of(VARIABLE("rs"), CastExpression.CAST(INTEGER_ARRAY, getNull())))
                );

        Constructor constructor2 = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(INTEGER_ARRAY, "extra")
                .BODY(
                        SUPER_CALL(METHOD_CALL(sqlBeanCompleter3Type, "newGetter", List.of(VARIABLE("rs")))),
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "rs"), VARIABLE("rs")),
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "extra"), VARIABLE("extra"))
                );

        Method getValueFromLocation = METHOD("getValueFromLocation")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(INTEGER)
                .BODY(
                        RETURN(CastExpression.CAST(INTEGER, new ArrayAccessor(VARIABLE("extra"), CONSTANT(0))))
                );

        Method nextMethod = METHOD("next")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(OverrideAnnotation.NAME)
                .RETURNS(_bool)
                .BODY(buildNextTryCatch());

        Method setValueInLocation = METHOD("setValueInLocation")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(VOID)
                .BODY(
                        /*
                        METHOD_CALL(SYSTEM, "out.println", List.of(
                                BINARY_OP(CONSTANT("LUC WWWW In setValueInLocation with extra of length "), "+",
                                        IfExpression.IFEXPRESSION(BINARY_OP(VARIABLE("extra"), "==", getNull()), CONSTANT(0), METHOD_CALL(VARIABLE("extra"), "length")))
                        )),

                         */
                        IF(BINARY_OP(VARIABLE("extra"), "!=", getNull()))
                                .THEN(
                                        DEFINITION(_int, VARIABLE("parent"), METHOD_CALL(VARIABLE("getter"), "get", List.of(METHOD_CALL(INTEGER, "class"), VARIABLE("PARENT_COLUMN")))),
                                        /*
                                        METHOD_CALL(SYSTEM, "out.println", List.of(
                                                BINARY_OP(CONSTANT("LUC LUC Setting parent to "), "+", VARIABLE("parent"))
                                        )),

                                         */
                                        ASSIGNMENT(new ArrayAccessor(VARIABLE("extra"), CONSTANT(0)), VARIABLE("parent"))
                                )
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_COMPOSITE_COMPLETER_3)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .FIELDS(
                        FIELD("extra", INTEGER_ARRAY).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL),
                        FIELD("rs", RESULT_SET).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL),
                        FIELD("PARENT_COLUMN", STRING).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL).INITIALIZER(CONSTANT("parent"))
                )
                .CONSTRUCTOR(constructor1)
                .CONSTRUCTOR(constructor2)
                .METHOD(getValueFromLocation)
                .METHOD(nextMethod)
                .METHOD(setValueInLocation);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeBeanCompleter4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.SQL_BEAN_COMPOSITE_COMPLETER_3, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPOSITE_COMPLETER_3));

        Constructor constructor1 = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(RESULT_SET, "rs")
                .BODY(
                        METHOD_CALL("this", List.of(VARIABLE("rs"), CastExpression.CAST(INTEGER_ARRAY, getNull())))
                );

        Constructor constructor2 = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(INTEGER_ARRAY, "extra")
                .BODY(
                        SUPER_CALL(VARIABLE("rs"), VARIABLE("extra")),
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), Constants.POST_PROCESSING_VAR), getNull())
                );

        Constructor constructor3 = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(INTEGER_ARRAY, "extra")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .BODY(
                        SUPER_CALL(VARIABLE("rs"), VARIABLE("extra")),
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), Constants.POST_PROCESSING_VAR), VARIABLE(Constants.POST_PROCESSING_VAR))
                );

        Constructor constructor4 = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(RESULT_SET, "rs")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .BODY(
                        SUPER_CALL(VARIABLE("rs"), getNull()),
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), Constants.POST_PROCESSING_VAR), VARIABLE(Constants.POST_PROCESSING_VAR))
                );

        Method postMethod = METHOD(Constants.POST_PROCESS_METHOD_NAME)
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(INTEGER, "id")
                .PARAMETER(STRING, "template")
                .RETURNS(VOID)
                .BODY(
                        IF(BINARY_OP(VARIABLE(Constants.POST_PROCESSING_VAR), "!=", getNull()))
                                .THEN(
                                        METHOD_CALL(VARIABLE(Constants.POST_PROCESSING_VAR), "apply", List.of(VARIABLE("id"), VARIABLE("template")))
                                )
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_COMPOSITE_COMPLETER_4)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .FIELDS(
                        FIELD(Constants.POST_PROCESSING_VAR, BIFUNCTION_INTEGER_STRING_OBJECT).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                )
                .CONSTRUCTOR(constructor1)
                .CONSTRUCTOR(constructor2)
                .CONSTRUCTOR(constructor3)
                .CONSTRUCTOR(constructor4)
                .METHOD(postMethod);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeBeanEnactor3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName beanEnactor2CompositeType = ClassName.get(Constants.BEAN_ENACTOR2_COMPOSITE, locations.getFilePackage(configs.name, Constants.BEAN_ENACTOR2_COMPOSITE));
        ParameterizedType superclass = ParameterizedType.get(beanEnactor2CompositeType, RESULT_SET);

        ClassName sqlCompositeEnactorImpl3Type = ClassName.get(Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION_3, locations.getFilePackage(configs.name, Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION_3));
        ClassName beanChecker2Type = ClassName.get(Constants.BEAN_CHECKER2, locations.getFilePackage(configs.name, Constants.BEAN_CHECKER2));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(sqlCompositeEnactorImpl3Type, List.of(VARIABLE("querier"))),
                                CONSTRUCTOR_CALL(beanChecker2Type, List.of()))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_COMPOSITE_ENACTOR_3)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeBeanEnactor4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName beanEnactor2CompositeWPType = ClassName.get(Constants.BEAN_ENACTOR2_COMPOSITE_WP, locations.getFilePackage(configs.name, Constants.BEAN_ENACTOR2_COMPOSITE_WP));
        ParameterizedType superclass = ParameterizedType.get(beanEnactor2CompositeWPType, RESULT_SET);

        ClassName sqlCompositeEnactorImpl4Type = ClassName.get(Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION_4, locations.getFilePackage(configs.name, Constants.SQL_ENACTOR_COMPOSITE_IMPLEMENTATION_4));
        ClassName beanChecker2Type = ClassName.get(Constants.BEAN_CHECKER2, locations.getFilePackage(configs.name, Constants.BEAN_CHECKER2));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .PARAMETER(SUPPLIER_OF_STRING, Constants.PRINCIPAL_MANAGER_VAR)
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(sqlCompositeEnactorImpl4Type, List.of(VARIABLE("querier"))),
                                CONSTRUCTOR_CALL(beanChecker2Type, List.of()),
                                VARIABLE(Constants.POST_PROCESSING_VAR),
                                VARIABLE(Constants.PRINCIPAL_MANAGER_VAR))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_BEAN_COMPOSITE_ENACTOR_4)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeEnactorConfigurator3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.ENACTOR_CONFIGURATOR_COMPOSITE_2, locations.getFilePackage(configs.name, Constants.ENACTOR_CONFIGURATOR_COMPOSITE_2));
        ClassName sqlCompositeBeanEnactor3Type = ClassName.get(Constants.SQL_BEAN_COMPOSITE_ENACTOR_3, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPOSITE_ENACTOR_3));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(sqlCompositeBeanEnactor3Type, List.of(VARIABLE("querier"))))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_ENACTOR_COMPOSITE_CONFIGURATOR_3)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_CompositeEnactorConfigurator4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.ENACTOR_CONFIGURATOR_COMPOSITE_2, locations.getFilePackage(configs.name, Constants.ENACTOR_CONFIGURATOR_COMPOSITE_2));
        ClassName sqlCompositeBeanEnactor4Type = ClassName.get(Constants.SQL_BEAN_COMPOSITE_ENACTOR_4, locations.getFilePackage(configs.name, Constants.SQL_BEAN_COMPOSITE_ENACTOR_4));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .PARAMETER(SUPPLIER_OF_STRING, Constants.PRINCIPAL_MANAGER_VAR)
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(sqlCompositeBeanEnactor4Type, List.of(VARIABLE("querier"), VARIABLE(Constants.POST_PROCESSING_VAR), VARIABLE(Constants.PRINCIPAL_MANAGER_VAR))))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_ENACTOR_COMPOSITE_CONFIGURATOR_4)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_EnactorConfigurator3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.ENACTOR_CONFIGURATOR2, locations.getFilePackage(configs.name, Constants.ENACTOR_CONFIGURATOR2));
        ClassName sqlBeanEnactor3Type = ClassName.get(Constants.SQL_BEAN_ENACTOR3, locations.getFilePackage(configs.name, Constants.SQL_BEAN_ENACTOR3));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .PARAMETER(SUPPLIER_OF_STRING, Constants.PRINCIPAL_MANAGER_VAR)
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(sqlBeanEnactor3Type, List.of(VARIABLE("querier"), VARIABLE(Constants.PRINCIPAL_MANAGER_VAR))))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_ENACTOR_CONFIGURATOR3)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    public SpecificationFile generateSqlIntegration_EnactorConfigurator4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName superclass = ClassName.get(Constants.ENACTOR_CONFIGURATOR2, locations.getFilePackage(configs.name, Constants.ENACTOR_CONFIGURATOR2));
        ClassName sqlBeanEnactor4Type = ClassName.get(Constants.SQL_BEAN_ENACTOR4, locations.getFilePackage(configs.name, Constants.SQL_BEAN_ENACTOR4));

        Constructor constructor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(FUNCTION_STRING_RESULTSET, "querier")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, Constants.POST_PROCESSING_VAR)
                .PARAMETER(SUPPLIER_OF_STRING, Constants.PRINCIPAL_MANAGER_VAR)
                .BODY(
                        SUPER_CALL(CONSTRUCTOR_CALL(sqlBeanEnactor4Type, List.of(VARIABLE("querier"), VARIABLE(Constants.POST_PROCESSING_VAR), VARIABLE(Constants.PRINCIPAL_MANAGER_VAR))))
                );

        Class pastClass = pastFactory.CLASS(Constants.SQL_ENACTOR_CONFIGURATOR4)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(superclass)
                .CONSTRUCTOR(constructor);

        String directory = locations.convertToBackendDirectory(locations.getFilePackage(configs.name, fileName));
        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }


}
