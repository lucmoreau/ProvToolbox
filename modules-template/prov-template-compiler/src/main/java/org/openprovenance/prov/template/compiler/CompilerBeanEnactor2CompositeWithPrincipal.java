
package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.annotations.OverrideAnnotation;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerBeanEnactor2CompositeWithPrincipal {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;
    private final ProvFactory pFactory;

    public CompilerBeanEnactor2CompositeWithPrincipal(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
        this.pFactory = pFactory;
        this.pastFactory = compilerUtil.getPastFactory();
    }

    SpecificationFile generateBeanEnactor2CompositeWithPrincipal(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName beanEnactor2WpClass = get(BEAN_ENACTOR2_WP, locations.getFilePackage(configs.name, BEAN_ENACTOR2_WP));
        ClassName queryInvokerWpClass = get(QUERY_INVOKER2WP, locations.getFilePackage(configs.name, QUERY_INVOKER2WP));
        ClassName inputProcessorClass = get(INPUT_PROCESSOR, locations.getFilePackage(configs.name, INPUT_PROCESSOR));

        final ParameterizedType ENACTOR_IMPLEMENTATION_TYPE = ParameterizedType.get(
                get(ENACTOR_IMPLEMENTATION4, locations.getFilePackage(configs.name, ENACTOR_IMPLEMENTATION4)),
                TYPE_RESULT
        );

        Class pastClass = pastFactory.CLASS(BEAN_ENACTOR2_COMPOSITE_WP)
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(TYPE_RESULT)
                .SUPERCLASS(ParameterizedType.get(beanEnactor2WpClass, TYPE_RESULT))
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
                        PARAMETER(REALISER, ENACTOR_IMPLEMENTATION_TYPE),
                        PARAMETER("checker", inputProcessorClass),
                        PARAMETER("postProcessing", BIFUNCTION_INTEGER_STRING_OBJECT),
                        PARAMETER(PRINCIPAL_MANAGER_VAR, SUPPLIER_OF_STRING)
                )
                .BODY(
                        METHOD_CALL("super",List.of(VARIABLE(REALISER), VARIABLE("checker"), VARIABLE("postProcessing"), VARIABLE(PRINCIPAL_MANAGER_VAR))),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), REALISER), VARIABLE(REALISER)),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "checker"), VARIABLE("checker")),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "postProcessing"), VARIABLE("postProcessing")),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), PRINCIPAL_MANAGER_VAR), VARIABLE(PRINCIPAL_MANAGER_VAR))
                );
        pastClass.CONSTRUCTOR(ctor);

        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof CompositeTemplateCompilerConfig)) continue;

            final String outputNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputNameClass = compilerUtil.inputsNameClass(config.name);
            final ClassName outputClassName = get(outputNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));
            final ClassName inputClassName = get(inputNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));

            Method m = METHOD(PROCESS_METHOD_NAME)
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(inputClassName, "bean")
                    .RETURNS(outputClassName)
                    .ANNOTATIONS(OverrideAnnotation.NAME);

            m.BODY(
                    RETURN(
                            METHOD_CALL(
                                    VARIABLE(Constants.REALISER),
                                    "generic_enact",
                                    List.of(
                                            CONSTRUCTOR_CALL(outputClassName, List.of()),
                                            VARIABLE("bean"),
                                            LAMBDA(PARAMETER("b", inputClassName))
                                                    .BODY(FUNCTIONAL_METHOD_CALL(VARIABLE("checker"), "process", List.of(VARIABLE("b")))),
                                            LAMBDA(PARAMETER("sb", STRING_BUILDER), PARAMETER("b", inputClassName))
                                                    .BODY(
                                                            FUNCTIONAL_METHOD_CALL(
                                                                    CONSTRUCTOR_CALL(queryInvokerWpClass, List.of(VARIABLE("sb"), CONSTANT(true), METHOD_CALL(VARIABLE(PRINCIPAL_MANAGER_VAR), "get", List.of()))),
                                                                    "process",
                                                                    List.of(VARIABLE("b"))
                                                            )
                                                    ),
                                            LAMBDA(PARAMETER("rs", TYPE_RESULT), PARAMETER("b", outputClassName))
                                                    .BODY(
                                                            FUNCTIONAL_METHOD_CALL(
                                                                    METHOD_CALL(VARIABLE(Constants.REALISER), "beanCompleterFactory", List.of(VARIABLE("rs"), new ArrayAllocator(INTEGER,CONSTANT(1)),VARIABLE("postProcessing"))),
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
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, locations, stackTraceElement);

        return new SpecificationFile(javaGenerator, pythonGenerator,  jsGenerator, emptyGenerator);
    }
}