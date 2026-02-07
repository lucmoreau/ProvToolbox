// java
package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

/**
 * PAST-based generator for TEMPLATE_INVOKER
 */
public class CompilerTemplateInvoker {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerTemplateInvoker(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
        this.pastFactory = compilerUtil.getPastFactory();
    }

    SpecificationFile generateTemplateInvoker(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName processorInterface = get(INPUT_OUTPUT_PROCESSOR, locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR));

        Class pastClass = pastFactory.CLASS(TEMPLATE_INVOKER)
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .INTERFACES(processorInterface);

        for (TemplateCompilerConfig config : configs.templates) {
            final String inputsClass = compilerUtil.inputsNameClass(config.name);
            final String outputsClass = compilerUtil.outputsNameClass(config.name);

            final ClassName inputClassName = get(inputsClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));
            final ClassName outputClassName = get(outputsClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));


            Method m = METHOD(PROCESS_METHOD_NAME)
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(inputClassName, BEAN_VAR)
                    .RETURNS(outputClassName);

            final ClassName completerClass;
            if (config instanceof SimpleTemplateCompilerConfig) {
                completerClass =ClassName.get(BEAN_COMPLETER2, locations.getFilePackage(configs.name, BEAN_COMPLETER2) );

                m.BODY(RETURN(
                        METHOD_CALL(
                                GENERIC_POST_AND_RETURN,
                                List.of(METHOD_CALL(outputClassName, "class"),
                                        VARIABLE(BEAN_VAR),
                                        LAMBDA(PARAMETER("m", MAP_STRING_OBJECT),
                                                PARAMETER("o", outputClassName))
                                                .BODY(RETURN(
                                                        METHOD_CALL(
                                                                CONSTRUCTOR_CALL(completerClass,List.of(VARIABLE("m"))),
                                                                "process",
                                                                List.of(VARIABLE("o")))))))));

            } else {
                completerClass =ClassName.get(COMPOSITE_BEAN_COMPLETER2, locations.getFilePackage(configs.name, COMPOSITE_BEAN_COMPLETER2) );
                m.BODY(RETURN(
                        METHOD_CALL(
                                GENERIC_POST_AND_RETURN,
                                List.of(METHOD_CALL(outputClassName, "class"),
                                        VARIABLE(BEAN_VAR),
                                        LAMBDA(PARAMETER("m", MAP_STRING_OBJECT),
                                                PARAMETER("o", outputClassName))
                                                .BODY(
                                                        ASSIGNMENT(
                                                                null,
                                                                METHOD_CALL(VARIABLE("o"), ELEMENTS),
                                                                CONSTRUCTOR_CALL(LINKED_LIST_GENERICS, List.of())),
                                                        RETURN(
                                                                METHOD_CALL(
                                                                        CONSTRUCTOR_CALL(completerClass, List.of(VARIABLE("m"))),
                                                                        "process",
                                                                        List.of(VARIABLE("o")))))))));


            }

            pastClass.METHOD(m);
        }

        Method generic = METHOD(GENERIC_POST_AND_RETURN)
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .PARAMETERS(
                        PARAMETER("clazz", CLASS_OUT),
                        PARAMETER("inputs", TYPE_IN),
                        PARAMETER( "completer", BIFUNCTION_MAP_OUT_OUT)
                )
                .addTypeVariables(TYPE_IN, TYPE_OUT)
                .RETURNS(TYPE_OUT);

        pastClass.METHOD(generic);

        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }
}