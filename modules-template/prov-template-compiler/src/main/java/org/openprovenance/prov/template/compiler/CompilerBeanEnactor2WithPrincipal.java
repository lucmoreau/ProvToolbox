package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Constructor;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerBeanEnactor2WithPrincipal {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBeanEnactor2WithPrincipal(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
        this.pastFactory = compilerUtil.getPastFactory();
    }

    SpecificationFile generateBeanEnactor2WithPrincipal(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName beanEnactor2Class = get(Constants.BEAN_ENACTOR2, locations.getFilePackage(configs.name, Constants.BEAN_ENACTOR2));
        ClassName queryInvokerWpClass = get(Constants.QUERY_INVOKER2WP, locations.getFilePackage(configs.name, Constants.QUERY_INVOKER2WP));
        ClassName ioProcessorClass = get(INPUT_OUTPUT_PROCESSOR, locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR));
        ClassName inputProcessorClass = get(INPUT_PROCESSOR, locations.getFilePackage(configs.name, INPUT_PROCESSOR));

        ParameterizedType ENACTOR_IMPLEMENTATION_TYPE = ParameterizedType.get(get(Constants.ENACTOR_IMPLEMENTATION4, locations.getFilePackage(configs.name, Constants.ENACTOR_IMPLEMENTATION4)), TYPE_RESULT);

        Class pastClass = pastFactory.CLASS(Constants.BEAN_ENACTOR2_WP)
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .TYPE_VARIABLES(TYPE_RESULT)
                .SUPERCLASS(ParameterizedType.get(beanEnactor2Class,TYPE_RESULT))
                .INTERFACES(ioProcessorClass)
                .FIELDS(
                        FIELD("checker", inputProcessorClass).MODIFIERS(Modifier.FINAL, Modifier.PRIVATE),
                        FIELD("postProcessing", BIFUNCTION_INTEGER_STRING_OBJECT).MODIFIERS(Modifier.FINAL, Modifier.PRIVATE),
                        FIELD(REALISER, ENACTOR_IMPLEMENTATION_TYPE).MODIFIERS(Modifier.FINAL, Modifier.PRIVATE),
                        FIELD(PRINCIPAL_MANAGER_VAR, SUPPLIER_OF_STRING).MODIFIERS(Modifier.FINAL, Modifier.PRIVATE)
                );

        Constructor ctor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETERS(
                        PARAMETER(Constants.REALISER, ENACTOR_IMPLEMENTATION_TYPE),
                        PARAMETER("checker", inputProcessorClass),
                        PARAMETER("postProcessing", BIFUNCTION_INTEGER_STRING_OBJECT),
                        PARAMETER(PRINCIPAL_MANAGER_VAR, SUPPLIER_OF_STRING)
                )
                .BODY(
                        METHOD_CALL("super",List.of(VARIABLE(Constants.REALISER), VARIABLE("checker"))),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), Constants.REALISER), VARIABLE(Constants.REALISER)),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "checker"), VARIABLE("checker")),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "postProcessing"), VARIABLE("postProcessing")),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), PRINCIPAL_MANAGER_VAR), VARIABLE(PRINCIPAL_MANAGER_VAR))
                );
        pastClass.CONSTRUCTOR(ctor);

        for (TemplateCompilerConfig config : configs.templates) {
            final String outputNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputNameClass = compilerUtil.inputsNameClass(config.name);
            final ClassName outputClassName = get(outputNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));
            final ClassName inputClassName = get(inputNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));

            Method m = METHOD(Constants.PROCESS_METHOD_NAME)
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(inputClassName, "bean")
                    .RETURNS(outputClassName);

            m.BODY(
                    RETURN(
                            METHOD_CALL(
                                    VARIABLE(Constants.REALISER),
                                    "generic_enact",
                                    List.of(
                                            CONSTRUCTOR_CALL(outputClassName, List.of()),
                                            VARIABLE("bean"),
                                            // checker lambda: b -> checker.process(b)
                                            LAMBDA(PARAMETER("b", inputClassName))
                                                    .BODY(FUNCTIONAL_METHOD_CALL(VARIABLE("checker"), "process", List.of(VARIABLE("b")))),
                                            // queryInvoker lambda: (sb,b) -> new QueryInvoker2WP(sb, principalManager.get()).process(b)
                                            LAMBDA(PARAMETER("sb", STRING_BUILDER), PARAMETER("b", inputClassName))
                                                    .BODY(
                                                            FUNCTIONAL_METHOD_CALL(
                                                                    CONSTRUCTOR_CALL(queryInvokerWpClass, List.of(VARIABLE("sb"), METHOD_CALL(VARIABLE(PRINCIPAL_MANAGER_VAR), "get", List.of()))),
                                                                    "process",
                                                                    List.of(VARIABLE("b"))
                                                            )
                                                    ),
                                            // completeBean lambda: (rs,b) -> realiser.beanCompleterFactory(rs, postProcessing).process(b)
                                            LAMBDA(PARAMETER("rs", TYPE_RESULT), PARAMETER("b", outputClassName))
                                                    .BODY(
                                                            FUNCTIONAL_METHOD_CALL(
                                                                    METHOD_CALL(VARIABLE(Constants.REALISER), "beanCompleterFactory", List.of(VARIABLE("rs"), VARIABLE("postProcessing"))),
                                                                    "process",
                                                                    List.of(VARIABLE("b"))
                                                            )
                                                    )
                                    )
                            )
                    )
            );

            pastClass.METHOD(m);
        }

        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs,
                locations.convertToBackendDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    SpecificationFile generateEnactorImplmentation4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        ClassName beanCompleter2Class = get(Constants.BEAN_COMPLETER2, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER2));

        Class pastClass = pastFactory.INTERFACE(ENACTOR_IMPLEMENTATION4)
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(TYPE_RESULT)
                .INTERFACES(ParameterizedType.get(ClassName.get(Constants.ENACTOR_IMPLEMENTATION, locations.getFilePackage(configs.name, Constants.ENACTOR_IMPLEMENTATION)),TYPE_RESULT));


        Method mf1 = METHOD("beanCompleterFactory")
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .PARAMETER(TYPE_RESULT, "rs")
                .PARAMETER(INTEGER_ARRAY, "extra")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, "postProcessing")
                .RETURNS(beanCompleter2Class);
        pastClass.METHOD(mf1);

        Method mf2 = METHOD("beanCompleterFactory")
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .PARAMETER(TYPE_RESULT, "rs")
                .PARAMETER(BIFUNCTION_INTEGER_STRING_OBJECT, "postProcessing")
                .RETURNS(beanCompleter2Class);
        pastClass.METHOD(mf2);

        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs,
                locations.convertToBackendDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, locations, stackTraceElement);

        return new SpecificationFile(javaGenerator, pythonGenerator,  jsGenerator, emptyGenerator);

    }
}