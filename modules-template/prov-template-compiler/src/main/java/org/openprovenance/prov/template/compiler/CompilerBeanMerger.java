package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.annotations.MutableFirstParam;
import org.openprovenance.prov.template.compiler.past.annotations.OverrideAnnotation;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.Iterator.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerBeanMerger {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBeanMerger(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    SpecificationFile generateBeanMerger(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName mergerClass=ClassName.get(BEAN_MERGER,locations.getFilePackage(configs.name, BEAN_MERGER));

        Class pastInterface = pastFactory.CLASS(BEAN_MERGER_INTERFACE,true)
                .MODIFIERS(Modifier.PUBLIC);


        Class pastClass = pastFactory.CLASS(BEAN_MERGER)
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(get(BEAN_MERGER_INTERFACE, locations.getFilePackage(configs.name, BEAN_MERGER_INTERFACE)));




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

                Method mspec_in = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(className, BEAN_VAR)
                        .PARAMETER(inputClassName, INPUT_BEAN_VAR)
                        .ANNOTATIONS(OverrideAnnotation.NAME, MutableFirstParam.NAME)
                        .RETURNS(className);
                Method mspec_out = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(className, BEAN_VAR)
                        .PARAMETER(outputClassName, OUTPUT_BEAN_VAR)
                        .ANNOTATIONS(OverrideAnnotation.NAME, MutableFirstParam.NAME)
                        .RETURNS(className);

                Method mspec_inI = METHOD(Constants.PROCESS_METHOD_NAME)
                        .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .PARAMETER(className, BEAN_VAR)
                        .PARAMETER(inputClassName, INPUT_BEAN_VAR)
                        .ANNOTATIONS(MutableFirstParam.NAME)
                        .RETURNS(className);
                Method mspec_outI = METHOD(Constants.PROCESS_METHOD_NAME)
                        .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .PARAMETER(className, BEAN_VAR)
                        .PARAMETER(outputClassName, OUTPUT_BEAN_VAR)
                        .ANNOTATIONS(MutableFirstParam.NAME)
                        .RETURNS(className);

                pastInterface.METHODS(mspec_inI,mspec_outI);

                for (String key : descriptorUtils.fieldNames(bindingsSchema)) {
                    if (descriptorUtils.isOutput(key, bindingsSchema)) {
                        mspec_out.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(BEAN_VAR),key), METHOD_CALL(VARIABLE(OUTPUT_BEAN_VAR), key)));
                    } else if (descriptorUtils.isInput(key, bindingsSchema)) {
                        mspec_in.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(BEAN_VAR), key), METHOD_CALL(VARIABLE(INPUT_BEAN_VAR), key)));
                    }
                }

                mspec_out.BODY(RETURN(VARIABLE(BEAN_VAR)));
                mspec_in.BODY(RETURN(VARIABLE(BEAN_VAR)));

                pastClass.METHOD(mspec_in);
                pastClass.METHOD(mspec_out);


            } else {


                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
                final String inputBeanNameClass = compilerUtil.inputsNameClass(config.name);
                final ClassName outputClassName = get(outputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));
                final ClassName inputClassName = get(inputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));

                Method mspec_in = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(className, COMPOSITE_VAR)
                        .PARAMETER(inputClassName, INPUT_COMPOSITE_VAR)
                        .ANNOTATIONS(OverrideAnnotation.NAME, MutableFirstParam.NAME)
                        .RETURNS(className);
                Method mspec_out = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(className, COMPOSITE_VAR)
                        .PARAMETER(outputClassName, OUTPUT_COMPOSITE_VAR)
                        .ANNOTATIONS(OverrideAnnotation.NAME, MutableFirstParam.NAME)
                        .RETURNS(className);

                Method mspec_inI = METHOD(Constants.PROCESS_METHOD_NAME)
                        .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .PARAMETER(className, COMPOSITE_VAR)
                        .PARAMETER(inputClassName, INPUT_BEAN_VAR)
                        .ANNOTATIONS(MutableFirstParam.NAME)
                        .RETURNS(className);
                Method mspec_outI = METHOD(Constants.PROCESS_METHOD_NAME)
                        .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .PARAMETER(className, COMPOSITE_VAR)
                        .PARAMETER(outputClassName, OUTPUT_COMPOSITE_VAR)
                        .ANNOTATIONS(MutableFirstParam.NAME)
                        .RETURNS(className);

                pastInterface.METHODS(mspec_inI,mspec_outI);


                compilerUtil.debugFileLocation(mspec_in);

                CompositeTemplateCompilerConfig config1 = (CompositeTemplateCompilerConfig) config;

                pastClass.FIELDS(FIELD(beanLocalVar,className).MODIFIERS(Modifier.PRIVATE));

                String shortConsistsOf = locations.getShortNames().get(config1.consistsOf);
                String composeeName = compilerUtil.commonNameClass(shortConsistsOf);
                String inputComposeeName = compilerUtil.inputsNameClass(shortConsistsOf, "_1");  // extension hardcoded
                ClassName beanComposeeClass = get(composeeName, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.COMMON));
                ClassName inComposeeClass = get(inputComposeeName, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.INPUTS));
                String outputComposeeName = compilerUtil.outputsNameClass(shortConsistsOf);
                ClassName outComposeeClass = get(outputComposeeName, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.OUTPUTS));


                Method mspec_in1 = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(beanComposeeClass, BEAN_VAR)
                        .PARAMETER(inComposeeClass, INPUT_BEAN_VAR)
                        .ANNOTATIONS(OverrideAnnotation.NAME, MutableFirstParam.NAME)
                        .RETURNS(beanComposeeClass);

                Method mspec_in1I = METHOD(Constants.PROCESS_METHOD_NAME)
                        .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .PARAMETER(beanComposeeClass, BEAN_VAR)
                        .PARAMETER(inComposeeClass, INPUT_BEAN_VAR)
                        .ANNOTATIONS(MutableFirstParam.NAME)
                        .RETURNS(beanComposeeClass);

                SimpleTemplateCompilerConfig theConfig1=(SimpleTemplateCompilerConfig)Arrays.stream(configs.templates).filter(cfg -> cfg.name.equals(shortConsistsOf)).findFirst().get();
                TemplateBindingsSchema bindingsSchema1 = compilerUtil.getBindingsSchema(theConfig1);

                for (String key : descriptorUtils.fieldNames(bindingsSchema1)) {
                  if (descriptorUtils.isInput(key, bindingsSchema1)) {
                        mspec_in1.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(BEAN_VAR), key), METHOD_CALL(VARIABLE(INPUT_BEAN_VAR), key)));
                    }
                }
                mspec_in1.BODY(RETURN(VARIABLE(BEAN_VAR)));

                mspec_in.BODY(
                        DEFINITION(_int, VARIABLE("count"), CONSTANT(0)),
                        ITERATOR(
                                PARAMETER("composee", inComposeeClass),
                                METHOD_CALL(VARIABLE(INPUT_COMPOSITE_VAR), ELEMENTS))
                                .BODY(
                                        DEFINITION(beanComposeeClass,VARIABLE(BEAN_VAR),
                                                METHOD_CALL(METHOD_CALL(VARIABLE(COMPOSITE_VAR), ELEMENTS), "get", List.of(VARIABLE("count")))),
                                        METHOD_CALL(
                                                VARIABLE("this"),
                                                PROCESS_METHOD_NAME,
                                                List.of(VARIABLE(BEAN_VAR), VARIABLE("composee"))),
                                        ASSIGNMENT(VARIABLE("count"), BINARY_OP(VARIABLE("count"), "+", CONSTANT(1)))),
                        RETURN(VARIABLE(COMPOSITE_VAR))
                );

                mspec_out.BODY(
                        DEFINITION(_int, VARIABLE("count"), CONSTANT(0)),
                        ITERATOR(
                                PARAMETER("composee", outComposeeClass),
                                METHOD_CALL(VARIABLE(OUTPUT_COMPOSITE_VAR), ELEMENTS))
                                .BODY(

                                        DEFINITION(beanComposeeClass,VARIABLE(BEAN_VAR),
                                                METHOD_CALL(METHOD_CALL(VARIABLE(COMPOSITE_VAR), ELEMENTS), "get", List.of(VARIABLE("count")))),
                                        METHOD_CALL(
                                                VARIABLE("this"),
                                                PROCESS_METHOD_NAME,
                                                List.of(VARIABLE(BEAN_VAR), VARIABLE("composee"))),

                                        ASSIGNMENT(VARIABLE("count"), BINARY_OP(VARIABLE("count"), "+", CONSTANT(1)))),
                        RETURN(VARIABLE(COMPOSITE_VAR))
                );


                pastInterface.METHODS(mspec_in1I);
                pastClass.METHOD(mspec_in);
                pastClass.METHOD(mspec_in1);
                pastClass.METHOD(mspec_out);


            }

        }


        String myPackage = locations.getFilePackage(configs.name, fileName);

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, myPackage, locations, stackTraceElement);

        Supplier<Boolean> pythonGenerator2 = () -> generatePython(pastInterface, myPackage, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator2 = () -> generateJava(pastInterface, myPackage, configs, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator2 = () -> generateJavaScript(pastInterface, myPackage, locations, stackTraceElement);
        Supplier<Boolean> rustGenerator2 = () -> generateRust(pastInterface, myPackage, locations, stackTraceElement);
        new SpecificationFile(javaGenerator2, pythonGenerator2, jsGenerator2, rustGenerator2).save();

        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, rustGenerator);
    }


}