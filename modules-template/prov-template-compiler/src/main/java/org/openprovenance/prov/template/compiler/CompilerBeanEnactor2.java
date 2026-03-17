package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
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

public class CompilerBeanEnactor2 {
    private final CompilerUtil compilerUtil;

    public CompilerBeanEnactor2(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    final PastFactory pastFactory=new PastFactory();

    SpecificationFile generateBeanEnactor2(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        org.openprovenance.prov.template.compiler.past.Class pastClass=compilerUtil.getPastFactory()
                .CLASS(Constants.BEAN_ENACTOR2)
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .TYPE_VARIABLES(TYPE_RESULT);


        ClassName queryInvokerClass = ClassName.get(QUERY_INVOKER2, locations.getFilePackage(configs.name, Constants.QUERY_INVOKER2));

        ClassName ioProcessorClass = ClassName.get(INPUT_OUTPUT_PROCESSOR,locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR));
        ClassName inputProcessorClass = ClassName.get(INPUT_PROCESSOR,locations.getFilePackage(configs.name, INPUT_PROCESSOR));
        pastClass.interfaces.add(ioProcessorClass);

        pastClass.FIELDS(
                FIELD("checker", inputProcessorClass)
                        .MODIFIERS(Modifier.FINAL, Modifier.PRIVATE));


        final ParameterizedType ENACTOR_IMPLEMENTATION_TYPE= ParameterizedType.get(get(ENACTOR_IMPLEMENTATION, locations.getFilePackage(configs.name, Constants.ENACTOR_IMPLEMENTATION)), TYPE_RESULT);

        pastClass.FIELDS(
                FIELD(REALISER, ENACTOR_IMPLEMENTATION_TYPE)
                        .MODIFIERS(Modifier.FINAL, Modifier.PRIVATE));

        Constructor cbuilder3= CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETERS(
                        PARAMETER(REALISER, ENACTOR_IMPLEMENTATION_TYPE),
                        PARAMETER("checker", inputProcessorClass))
                .BODY(
                        ASSIGNMENT(
                                METHOD_CALL(VARIABLE("this"), REALISER),
                                VARIABLE(REALISER)   ),
                        ASSIGNMENT(
                                METHOD_CALL(VARIABLE("this"), "checker"),
                                VARIABLE( "checker")  )
                );
        compilerUtil.debugFileLocation(cbuilder3);

        pastClass.CONSTRUCTOR(cbuilder3);

        for (TemplateCompilerConfig config : configs.templates) {

            final String outputName = compilerUtil.outputsNameClass(config.name);
            final String inputName = compilerUtil.inputsNameClass(config.name);
            final ClassName outputClassName = ClassName.get(outputName,locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));
            final ClassName inputClassName = ClassName.get(inputName, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));

            Method mspec = METHOD(Constants.PROCESS_METHOD_NAME)
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(inputClassName,"bean")
                    .RETURNS(outputClassName);

            compilerUtil.debugFileLocation(mspec);

            mspec.BODY(

                    RETURN( METHOD_CALL(VARIABLE(REALISER),
                            "generic_enact",
                            List.of(
                                    CONSTRUCTOR_CALL(outputClassName,List.of()),
                                    VARIABLE("bean"),
                                    LAMBDA(PARAMETER("b", inputClassName))
                                            .BODY(
                                                    FUNCTIONAL_METHOD_CALL(VARIABLE("checker"), "process", List.of(VARIABLE("b")))
                                            ),
                                    LAMBDA(PARAMETER("sb", STRING_BUILDER), PARAMETER("b", inputClassName)).
                                            BODY(
                                                    FUNCTIONAL_METHOD_CALL(
                                                            CONSTRUCTOR_CALL(queryInvokerClass, List.of(VARIABLE("sb"))),
                                                            "process",
                                                            List.of(VARIABLE("b")))),
                                    LAMBDA(PARAMETER("rs", TYPE_RESULT), PARAMETER("b", outputClassName)).
                                            BODY(
                                                    FUNCTIONAL_METHOD_CALL(
                                                            METHOD_CALL(VARIABLE(REALISER), "beanCompleterFactory", List.of(VARIABLE("rs"))),
                                                            "process",
                                                            List.of(VARIABLE("b")))))

                    )));

            pastClass.METHOD(mspec);
        }

        String myPackage= locations.getFilePackage(configs.name, fileName);
        String directory = locations.convertToDirectory(myPackage);


        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + ".java", directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, "target/generated-js", stackTraceElement);

        return new SpecificationFile(javaGenerator,pythonGenerator, javaGenerator,jsGenerator);

    }

    SpecificationFile generateBeanEnactorImplementation(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        ClassName beanCompleterClass = ClassName.get(BEAN_COMPLETER2, locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER2));

        Class intfce = pastFactory.INTERFACE(ENACTOR_IMPLEMENTATION)
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(TYPE_RESULT);

        Method method1 = METHOD("generic_enact")
                .MODIFIERS(Modifier.PUBLIC,Modifier.ABSTRACT)
                .PARAMETER(TYPE_OUT,"output")
                .PARAMETER(TYPE_IN,"bean")
                .PARAMETER(CONSUMER_OF_IN,"checker")
                .PARAMETER(BICONSUMER_STRINGBUILDER_TYPEIN,"queryInvoker")
                .PARAMETER(BICONSUMER_RESULT_TYPEOUT,"completeBean")
                .addTypeVariables(TYPE_IN, TYPE_OUT)
                .RETURNS(TYPE_OUT);

        intfce.METHOD(method1);

        Method method2 = METHOD("beanCompleterFactory")
                .MODIFIERS(Modifier.PUBLIC,Modifier.ABSTRACT)
                .PARAMETER(TYPE_RESULT,"rs")
                .RETURNS(beanCompleterClass);

        intfce.METHOD(method2);

        Method method3 = METHOD("beanCompleterFactory")
                .MODIFIERS(Modifier.PUBLIC,Modifier.ABSTRACT)
                .PARAMETER(TYPE_RESULT,"rs")
                .PARAMETER(INTEGER_ARRAY,"extra")
                .RETURNS(beanCompleterClass);
        intfce.METHOD(method3);

        String myPackage= locations.getFilePackage(configs.name, fileName);
        String directory = locations.convertToDirectory(myPackage);

        Supplier<Boolean> pythonGenerator=() -> generatePython(intfce, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(intfce, myPackage, configs, fileName + ".java", directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(intfce, myPackage, "target/generated-js", stackTraceElement);

        return new SpecificationFile(javaGenerator,pythonGenerator,  jsGenerator, emptyGenerator);

    }



}