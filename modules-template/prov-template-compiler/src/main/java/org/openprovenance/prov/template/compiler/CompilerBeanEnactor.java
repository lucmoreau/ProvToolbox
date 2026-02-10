package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
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
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerBeanEnactor {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;


    public CompilerBeanEnactor(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }


    SpecificationFile generateBeanEnactor(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        ClassName queryInvokerClass  = ClassName.get(QUERY_INVOKER,locations.getFilePackage(configs.name,QUERY_INVOKER));
        ClassName beanProcessorClass = ClassName.get(BEAN_PROCESSOR,locations.getFilePackage(configs.name,BEAN_PROCESSOR));

        final ParameterizedType ENACTOR_IMPLEMENTATION_TYPE= ParameterizedType.get(ClassName.get( ENACTOR_IMPLEMENTATION1,locations.getFilePackage(configs.name,ENACTOR_IMPLEMENTATION1)), TYPE_RESULT);


        Class pastClass= pastFactory
                .CLASS(Constants.BEAN_ENACTOR)
                .MODIFIERS(Modifier.ABSTRACT, Modifier.PUBLIC)
                .TYPE_VARIABLES(TYPE_RESULT)
                .INTERFACES(beanProcessorClass)
                .FIELDS(FIELD("checker", beanProcessorClass).MODIFIERS(Modifier.FINAL, Modifier.PROTECTED),
                        FIELD(REALISER,ENACTOR_IMPLEMENTATION_TYPE).MODIFIERS(Modifier.FINAL, Modifier.PROTECTED))
                ;

        pastClass.CONSTRUCTOR(CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(ENACTOR_IMPLEMENTATION_TYPE, REALISER)
                .PARAMETER(beanProcessorClass, "checker")
                .BODY(

                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"),"checker"), VARIABLE("checker")),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"),REALISER), VARIABLE(REALISER))            ))      ;


        for (TemplateCompilerConfig config : configs.templates) {

            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            final ClassName className = ClassName.get(beanNameClass,locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));
            Method mspec = METHOD(Constants.PROCESS_METHOD_NAME)
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                    .PARAMETER(className,"bean")
                    .RETURNS(className)
                    .BODY(
                            RETURN(
                                    METHOD_CALL(
                                            VARIABLE(REALISER),
                                            "generic_enact",
                                            List.of(
                                                    VARIABLE("bean"),
                                                    LAMBDA(PARAMETER("b",className))
                                                            .BODY(
                                                                    FUNCTIONAL_METHOD_CALL(
                                                                            VARIABLE("checker"),
                                                                            "process",
                                                                            VARIABLE("b")  )  ),
                                                    LAMBDA(PARAMETER("sb",STRING_BUILDER),PARAMETER("b",className))
                                                            .BODY(METHOD_CALL(
                                                                    CONSTRUCTOR_CALL(queryInvokerClass, List.of(VARIABLE("sb"))),
                                                                    "process",
                                                                    List.of(VARIABLE("b")))),

                                                    LAMBDA(PARAMETER("rs",TYPE_RESULT), PARAMETER("b",className))
                                                            .BODY( METHOD_CALL(
                                                                            METHOD_CALL(
                                                                                    VARIABLE(REALISER),
                                                                                    "beanCompleterFactory",
                                                                                    List.of(VARIABLE("rs"))),
                                                                            "process",
                                                                            List.of(VARIABLE("b"))   )   )   ) ) ))  ;
            pastClass.METHOD(mspec);
        }

        String myPackage = locations.getFilePackage(configs.name, BEAN_ENACTOR);

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);

    }


    public SpecificationFile generateEnactorImplementationInterface(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        ClassName beanCompleterClass = ClassName.get(BEAN_COMPLETER,locations.getFilePackage(configs.name,BEAN_COMPLETER));

        Class pastClass=pastFactory.INTERFACE(ENACTOR_IMPLEMENTATION1)
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(TYPE_RESULT)
                .METHOD(
                        METHOD("generic_enact")
                                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .PARAMETER(T(), "bean")
                                .PARAMETER(CONSUMER_OF_T, "checker")
                                .PARAMETER(BICONSUMER_STRINGBUILDER_T, "queryInvoker")
                                .PARAMETER(BICONSUMER_RESULT_T, "completeBean")
                                .RETURNS(T())
                                .addTypeVariables(T()))
                .METHOD(
                        METHOD("beanCompleterFactory")
                                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .PARAMETER(TYPE_RESULT, "rs")
                                .RETURNS(beanCompleterClass));

            String myPackage = locations.getFilePackage(configs.name, fileName);

            Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
            Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

            return new SpecificationFile(javaGenerator, pythonGenerator);
        }


}
