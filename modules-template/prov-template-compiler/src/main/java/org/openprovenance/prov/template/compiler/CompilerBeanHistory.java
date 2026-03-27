package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Constructor;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.annotations.NoSerialization;
import org.openprovenance.prov.template.compiler.past.annotations.OverrideAnnotation;
import org.openprovenance.prov.template.compiler.past.annotations.StatefulProcessor;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.Iterator.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.STRING;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerBeanHistory {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBeanHistory(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    SpecificationFile generateBeanHistory(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName ioProcessorClass = ClassName.get(INPUT_OUTPUT_PROCESSOR, locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR));
        ClassName mergerClass = ClassName.get(BEAN_MERGER, locations.getFilePackage(configs.name, BEAN_MERGER));
        ClassName mergerInterface = ClassName.get(BEAN_MERGER_INTERFACE, locations.getFilePackage(configs.name, BEAN_MERGER_INTERFACE));


        TypeVariable typeVariable = T().withBounds(ioProcessorClass);

        Class pastClass = pastFactory.CLASS(BEAN_HISTORY)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(get(BEAN_LOCAL_ENACTOR2, locations.getFilePackage(configs.name, BEAN_LOCAL_ENACTOR2)))
                .INTERFACES(ioProcessorClass)
                .ANNOTATION(NoSerialization.NAME)
                .FIELDS(FIELD(DELEGATOR_VAR, typeVariable).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL),
                        FIELD(MERGER_VAR, mergerInterface).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL),
                        FIELD(HISTORY_VAR, LIST_OF_OBJECTS).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL))
                .TYPE_VARIABLES(typeVariable);


        Constructor ctor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(typeVariable, DELEGATOR_VAR)
                .PARAMETER(LIST_OF_OBJECTS, HISTORY_VAR)
                .BODY(
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), DELEGATOR_VAR), VARIABLE(DELEGATOR_VAR)),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), MERGER_VAR), CONSTRUCTOR_CALL(mergerClass,List.of())),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), HISTORY_VAR), VARIABLE(HISTORY_VAR))
                );
        pastClass.CONSTRUCTOR(ctor);

        Method method1=METHOD("newIdentifier")
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(STRING,"field")
                .PARAMETER(STRING,"counter")
                //.ANNOTATIONS(OverrideAnnotation.NAME)  // not in InputOutputProcessor
                .ANNOTATIONS(StatefulProcessor.NAME)
                .RETURNS(INTEGER)
                .BODY(RETURN(CONSTANT(0)));

        Method method2=METHOD("newSIdentifier")
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(STRING,"field")
                .PARAMETER(STRING,"counter")
               // .ANNOTATIONS(OverrideAnnotation.NAME) // not in InputOutputProcessor
                .ANNOTATIONS(StatefulProcessor.NAME)
                .RETURNS(STRING)
                .BODY(RETURN(CONSTANT("xyz")));

        Method method3=METHOD("getHistory")
                .MODIFIERS(Modifier.PUBLIC)
                        .RETURNS(LIST_OF_OBJECTS)
                .BODY(RETURN(METHOD_CALL(VARIABLE("this"),HISTORY_VAR)));

        Method method4=METHOD("getDelegator")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(typeVariable)
                .BODY(RETURN(METHOD_CALL(VARIABLE("this"),DELEGATOR_VAR)));


        pastClass.METHOD(method1);
        pastClass.METHOD(method2);
        pastClass.METHOD(method3);
        pastClass.METHOD(method4);


        for (TemplateCompilerConfig config : configs.templates) {

            final String beanNameClass = compilerUtil.commonNameClass(config.name);

            final ClassName className = get(beanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));
            String beanLocalVar=BEAN_VAR+className.simpleName;

            if (config instanceof SimpleTemplateCompilerConfig) {

                TemplateBindingsSchema bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
                final String inputBeanNameClass = compilerUtil.inputsNameClass(config.name);
                final ClassName outputClassName = get(outputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));
                final ClassName inputClassName = get(inputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));

                Method mspec = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(inputClassName, INPUT_BEAN_VAR)
                        .ANNOTATIONS(OverrideAnnotation.NAME)
                        .RETURNS(outputClassName);


                mspec.BODY(

                        DEFINITION(outputClassName, VARIABLE(OUTPUT_BEAN_VAR),  METHOD_CALL(METHOD_CALL(VARIABLE("this"),DELEGATOR_VAR),PROCESS_METHOD_NAME, List.of(VARIABLE(INPUT_BEAN_VAR)))),

                        DEFINITION(className, VARIABLE(BEAN_VAR), CONSTRUCTOR_CALL(className,List.of())),

                        METHOD_CALL(METHOD_CALL(VARIABLE("this"), MERGER_VAR), PROCESS_METHOD_NAME, List.of(VARIABLE(BEAN_VAR), VARIABLE(INPUT_BEAN_VAR))),
                        METHOD_CALL(METHOD_CALL(VARIABLE("this"), MERGER_VAR), PROCESS_METHOD_NAME, List.of(VARIABLE(BEAN_VAR), VARIABLE(OUTPUT_BEAN_VAR))),

                        METHOD_CALL(METHOD_CALL(VARIABLE("this"), HISTORY_VAR), "add", List.of(VARIABLE(BEAN_VAR)))

                );


                for (String key : descriptorUtils.fieldNames(bindingsSchema)) {
                  if (descriptorUtils.isInput(key, bindingsSchema)) {
                        //mspec.BODY(DEFINITION(outputClassName, VARIABLE(OUTPUT_BEAN_VAR),  METHOD_CALL(VARIABLE(DELEGATOR_VAR),PROCESS_METHOD_NAME, List.of(VARIABLE(INPUT_BEAN_VAR)))));
                    }
                }

                mspec.BODY(RETURN(VARIABLE(OUTPUT_BEAN_VAR)));

                pastClass.METHOD(mspec);


            } else {


                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
                final String inputBeanNameClass = compilerUtil.inputsNameClass(config.name);
                final ClassName outputClassName = get(outputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));
                final ClassName inputClassName = get(inputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));

                Method mspec = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(inputClassName, INPUT_COMPOSITE_VAR)
                        .ANNOTATIONS(OverrideAnnotation.NAME)
                        .RETURNS(outputClassName);


                compilerUtil.debugFileLocation(mspec);

                CompositeTemplateCompilerConfig config1 = (CompositeTemplateCompilerConfig) config;

//                /pastClass.FIELDS(FIELD(beanLocalVar,className).MODIFIERS(Modifier.PRIVATE));

                String shortConsistsOf = locations.getShortNames().get(config1.consistsOf);
                String composeeName = compilerUtil.commonNameClass(shortConsistsOf);
                String inputComposeeName = compilerUtil.inputsNameClass(shortConsistsOf, "_1");  // extension hardcoded
                ClassName beanComposeeClass = get(composeeName, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.COMMON));
                ClassName inComposeeClass = get(inputComposeeName, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.INPUTS));
                String outputComposeeName = compilerUtil.outputsNameClass(shortConsistsOf);
                ClassName outComposeeClass = get(outputComposeeName, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.OUTPUTS));



                SimpleTemplateCompilerConfig theConfig1=(SimpleTemplateCompilerConfig)Arrays.stream(configs.templates).filter(cfg -> cfg.name.equals(shortConsistsOf)).findFirst().get();
                TemplateBindingsSchema bindingsSchema1 = compilerUtil.getBindingsSchema(theConfig1);

                for (String key : descriptorUtils.fieldNames(bindingsSchema1)) {
                  if (descriptorUtils.isInput(key, bindingsSchema1)) {
                    }
                }



                mspec.BODY(

                        DEFINITION(outputClassName, VARIABLE(OUTPUT_COMPOSITE_VAR),  METHOD_CALL(METHOD_CALL(VARIABLE("this"),DELEGATOR_VAR),PROCESS_METHOD_NAME, List.of(VARIABLE(INPUT_COMPOSITE_VAR)))),

                        DEFINITION(className, VARIABLE(BEAN_VAR), CONSTRUCTOR_CALL(className,List.of())),

                        ITERATOR(
                                PARAMETER("composee", outComposeeClass),
                                METHOD_CALL(VARIABLE(OUTPUT_COMPOSITE_VAR), ELEMENTS))
                                .BODY(
                                        METHOD_CALL(VARIABLE(BEAN_VAR), ADD_ELEMENTS, List.of(CONSTRUCTOR_CALL(beanComposeeClass, List.of())))),

                        METHOD_CALL(METHOD_CALL(VARIABLE("this"), MERGER_VAR), PROCESS_METHOD_NAME, List.of(VARIABLE(BEAN_VAR), VARIABLE(INPUT_COMPOSITE_VAR))),
                        METHOD_CALL(METHOD_CALL(VARIABLE("this"), MERGER_VAR), PROCESS_METHOD_NAME, List.of(VARIABLE(BEAN_VAR), VARIABLE(OUTPUT_COMPOSITE_VAR))),

                        METHOD_CALL(METHOD_CALL(VARIABLE("this"), HISTORY_VAR), "add", List.of(VARIABLE(BEAN_VAR))),

                        RETURN(VARIABLE(OUTPUT_COMPOSITE_VAR))


                );



                pastClass.METHOD(mspec);


            }

        }


        String myPackage = locations.getFilePackage(configs.name, fileName);

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, myPackage, locations, stackTraceElement);

        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, rustGenerator);
    }


}