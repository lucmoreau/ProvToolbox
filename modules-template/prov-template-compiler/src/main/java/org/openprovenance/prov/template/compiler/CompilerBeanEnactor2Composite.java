
package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
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

/**
 * PAST-based generator for BEAN_ENACTOR2_COMPOSITE
 */
public class CompilerBeanEnactor2Composite {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBeanEnactor2Composite(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
        this.pastFactory = compilerUtil.getPastFactory();
    }

    SpecificationFile generateBeanEnactor2Composite(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        final ClassName beanEnactor2Class = get(Constants.BEAN_ENACTOR2, locations.getFilePackage(configs.name, Constants.BEAN_ENACTOR2));
        final ClassName queryInvokerClass = get(QUERY_INVOKER2, locations.getFilePackage(configs.name, Constants.QUERY_INVOKER2));
        final ClassName inputProcessorClass = get(INPUT_PROCESSOR, locations.getFilePackage(configs.name, INPUT_PROCESSOR));
        final ParameterizedType ENACTOR_IMPLEMENTATION_TYPE = ParameterizedType.get(
                get(ENACTOR_IMPLEMENTATION, locations.getFilePackage(configs.name, Constants.ENACTOR_IMPLEMENTATION)),
                TYPE_RESULT);

        Class pastClass = pastFactory.CLASS(Constants.BEAN_ENACTOR2_COMPOSITE)
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(TYPE_RESULT)
                .COMMENT("Ensures that composite beans are given an ID\n")
                .SUPERCLASS(ParameterizedType.get(beanEnactor2Class, TYPE_RESULT))
                .FIELDS(
                        FIELD("checker", inputProcessorClass).MODIFIERS(Modifier.FINAL, Modifier.PRIVATE),
                        FIELD(Constants.REALISER, ENACTOR_IMPLEMENTATION_TYPE).MODIFIERS(Modifier.FINAL, Modifier.PRIVATE)
                );

        Constructor ctor = CONSTRUCTOR()
                .debugFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETERS(
                        PARAMETER(Constants.REALISER, ENACTOR_IMPLEMENTATION_TYPE),
                        PARAMETER("checker", inputProcessorClass)
                )
                .BODY(
                        // Note: appears a method call to super
                        METHOD_CALL("super",List.of(VARIABLE(Constants.REALISER), VARIABLE("checker"))),
                        // assign fields
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), Constants.REALISER), VARIABLE(Constants.REALISER)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), "checker"), VARIABLE("checker"))
                );
        pastClass.CONSTRUCTOR(ctor);

        // for each composite template produce a process method that delegates to generic_enact
        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof CompositeTemplateCompilerConfig)) continue;

            final String outputNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputNameClass = compilerUtil.inputsNameClass(config.name);
            final ClassName outputClassName = get(outputNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));
            final ClassName inputClassName = get(inputNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));

            Method m = METHOD(Constants.PROCESS_METHOD_NAME)
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(inputClassName, "bean")
                    .RETURNS(outputClassName);


            // build the expression:
            // return realiser.generic_enact(new Output(), bean,
            //    b -> checker.process(b),
            //    (sb,b) -> new QueryInvoker2(sb,true).process(b),
            //    (rs,b) -> realiser.beanCompleterFactory(rs,new Object[1]).process(b)
            // );
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
                                                                    CONSTRUCTOR_CALL(queryInvokerClass, List.of(VARIABLE("sb"), CONSTANT(true))),
                                                                    "process",
                                                                    List.of(VARIABLE("b"))  ) ),
                                            LAMBDA(PARAMETER("rs", TYPE_RESULT), PARAMETER("b", outputClassName))
                                                    .BODY(
                                                            FUNCTIONAL_METHOD_CALL(
                                                                    METHOD_CALL(VARIABLE(REALISER), "beanCompleterFactory", List.of(VARIABLE("rs"), new ArrayAllocator(INTEGER, CONSTANT(1)))),
                                                                    "process",
                                                                    List.of(VARIABLE("b"))  )  )  )
                            )
                    )
            );

            pastClass.METHOD(m);
        }

        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }
}