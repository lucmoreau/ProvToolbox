package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.annotations.MutableReceiver;
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
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Iterator.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
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

        Class pastClass = pastFactory.CLASS(BEAN_MERGER)
                .MODIFIERS(Modifier.PUBLIC);



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
                        .PARAMETER(inputClassName, INPUT_BEAN_VAR)
                        .ANNOTATIONS(MutableReceiver.NAME)
                        .RETURNS(className);
                Method mspec_out = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(outputClassName, OUTPUT_BEAN_VAR)
                        .ANNOTATIONS(MutableReceiver.NAME)
                        .RETURNS(className);



                for (String key : descriptorUtils.fieldNames(bindingsSchema)) {
                    if (descriptorUtils.isOutput(key, bindingsSchema)) {
                        mspec_out.BODY(ASSIGNMENT(METHOD_CALL(METHOD_CALL(VARIABLE("this"),beanLocalVar), key), METHOD_CALL(VARIABLE(OUTPUT_BEAN_VAR), key)));
                    } else if (descriptorUtils.isInput(key, bindingsSchema)) {
                        mspec_in.BODY(ASSIGNMENT(METHOD_CALL(METHOD_CALL(VARIABLE("this"), beanLocalVar), key), METHOD_CALL(VARIABLE(INPUT_BEAN_VAR), key)));
                    }
                }
                mspec_out.BODY(RETURN(METHOD_CALL(VARIABLE("this"), beanLocalVar)));
                mspec_in.BODY(RETURN(METHOD_CALL(VARIABLE("this"), beanLocalVar)));


                pastClass.FIELDS(FIELD(beanLocalVar,className).MODIFIERS(Modifier.PRIVATE));

                Method mspecMerge = METHOD(Constants.MERGE_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC)
                        .PARAMETER(className, BEAN_VAR)
                        .RETURNS(mergerClass)
                        .ANNOTATIONS(MutableReceiver.NAME)
                        .BODY(ASSIGNMENT(METHOD_CALL(VARIABLE("this"), beanLocalVar), VARIABLE(BEAN_VAR)),
                                RETURN(VARIABLE("this")));


                pastClass.METHOD(mspecMerge);
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
                        .PARAMETER(inputClassName, INPUT_BEAN_VAR)
                        .ANNOTATIONS(MutableReceiver.NAME)
                        .RETURNS(className);
                Method mspec_out = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(outputClassName, OUTPUT_BEAN_VAR)
                        .ANNOTATIONS(MutableReceiver.NAME)
                        .RETURNS(className);

                compilerUtil.debugFileLocation(mspec_in);

                CompositeTemplateCompilerConfig config1 = (CompositeTemplateCompilerConfig) config;

                pastClass.FIELDS(FIELD(beanLocalVar,className).MODIFIERS(Modifier.PRIVATE));

                String shortConsistsOf = locations.getShortNames().get(config1.consistsOf);
                String composeeName = compilerUtil.commonNameClass(shortConsistsOf);
                String inputComposeeName0 = compilerUtil.inputsNameClass(shortConsistsOf);  // extension hardcoded
                String inputComposeeName = compilerUtil.inputsNameClass(shortConsistsOf, "_1");  // extension hardcoded
                ClassName beanComposeeClass = get(composeeName, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.COMMON));
                ClassName inComposeeClass = get(inputComposeeName, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.INPUTS));
                String outputComposeeName = compilerUtil.outputsNameClass(shortConsistsOf);
                ClassName outComposeeClass = get(outputComposeeName, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.OUTPUTS));

                String beanLocalVar0=BEAN_VAR+composeeName;
                String beanLocalVar1=BEAN_VAR+inputComposeeName;

                Method mspec_in1 = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(inComposeeClass, INPUT_BEAN_VAR)
                        .ANNOTATIONS(MutableReceiver.NAME)
                        .RETURNS(className);

                pastClass.FIELDS(FIELD(beanLocalVar1,beanComposeeClass).MODIFIERS(Modifier.PRIVATE));


                SimpleTemplateCompilerConfig theConfig1=(SimpleTemplateCompilerConfig)Arrays.stream(configs.templates).filter(cfg -> cfg.name.equals(shortConsistsOf)).findFirst().get();
                TemplateBindingsSchema bindingsSchema1 = compilerUtil.getBindingsSchema(theConfig1);

                for (String key : descriptorUtils.fieldNames(bindingsSchema1)) {
                  if (descriptorUtils.isInput(key, bindingsSchema1)) {
                        mspec_in1.BODY(ASSIGNMENT(METHOD_CALL(METHOD_CALL(VARIABLE("this"), beanLocalVar1), key), METHOD_CALL(VARIABLE(INPUT_BEAN_VAR), key)));
                    }
                }
                mspec_in1.BODY(RETURN(METHOD_CALL(VARIABLE("this"), beanLocalVar)));

                mspec_in.BODY(
                       // DEFINITION(_bool, VARIABLE("nextExists"), CONSTANT(true)),
                        ITERATOR(
                                PARAMETER("composee", inComposeeClass),
                                METHOD_CALL(VARIABLE(INPUT_BEAN_VAR), ELEMENTS))
                                .BODY(
                                        //IF(VARIABLE("nextExists"))
                                            //    .THEN(
                                                        //        this.mergeInto(new Plead_transforming_compositeBean());
                                                        METHOD_CALL(VARIABLE("this"), MERGE_METHOD_NAME, List.of(CONSTRUCTOR_CALL(beanComposeeClass, List.of()))),
                                                        METHOD_CALL(
                                                                VARIABLE("this"),
                                                                PROCESS_METHOD_NAME,
                                                                List.of(VARIABLE("composee"))),
                                                        //        this.bean_Plead_transforming_compositeBean.addElements(bean_Plead_transformingInputs_1);
                                                        METHOD_CALL(METHOD_CALL(VARIABLE("this"), beanLocalVar), ADD_ELEMENTS, List.of(METHOD_CALL(VARIABLE("this"), beanLocalVar0)))
                                                //        ASSIGNMENT(
                                                  //              VARIABLE("nextExists"),
                                                    ///            METHOD_CALL(
                                                       //                 VARIABLE("this"),
                                                         //               "next",
                                                           //             List.of())))

                                             //   .ELSE(

                                              //          THROW(
                                              //                  CONSTRUCTOR_CALL(ILLEGAL_ARGUMENT_EXCEPTION,
                                               //                         List.of(CONSTANT("Not enough record in the result")))))
                                ),

                        RETURN(METHOD_CALL(VARIABLE("this"), beanLocalVar))
                );

                mspec_out.BODY(
                       // DEFINITION(_bool, VARIABLE("nextExists"), CONSTANT(true)),
                        DEFINITION(_int, VARIABLE("count"), CONSTANT(0)),
                        ITERATOR(
                                PARAMETER("composee", outComposeeClass),
                                METHOD_CALL(VARIABLE(OUTPUT_BEAN_VAR), ELEMENTS))
                                .BODY(
                                      //  IF(VARIABLE("nextExists"))
                                        //        .THEN(
                                                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), beanLocalVar0),METHOD_CALL(METHOD_CALL(METHOD_CALL(VARIABLE("this"), beanLocalVar), ELEMENTS), "get", List.of(VARIABLE("count")))),
                                                        METHOD_CALL(
                                                                VARIABLE("this"),
                                                                PROCESS_METHOD_NAME,
                                                                List.of(VARIABLE("composee"))),

                                      //                  ASSIGNMENT(
                                     //                           VARIABLE("nextExists"),
                                       //                         METHOD_CALL(
                                         //                               VARIABLE("this"),
                                           //                             "next",
                                             ///                           List.of())),
                                                       ASSIGNMENT(VARIABLE("count"), BINARY_OP(VARIABLE("count"), "+", CONSTANT(1))))
                                               // .ELSE(

                                                //        THROW(
                                                  ///              CONSTRUCTOR_CALL(ILLEGAL_ARGUMENT_EXCEPTION,
                                                     //                   List.of(CONSTANT("Not enough record in the result")))))
                                ,

                        RETURN(METHOD_CALL(VARIABLE("this"), beanLocalVar))
                );

                Method mspecMerge = METHOD(Constants.MERGE_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC)
                        .ANNOTATIONS(MutableReceiver.NAME)
                        .PARAMETER(className, BEAN_VAR)
                        .RETURNS(mergerClass)
                        .BODY(ASSIGNMENT(METHOD_CALL(VARIABLE("this"), beanLocalVar), VARIABLE(BEAN_VAR)),
                                RETURN(VARIABLE("this")));


                pastClass.METHOD(mspecMerge);
                pastClass.METHOD(mspec_in);
                pastClass.METHOD(mspec_in1);
                pastClass.METHOD(mspec_out);


            }

        }


        String myPackage = locations.getFilePackage(configs.name, fileName);

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, "target/generated-js", stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, myPackage, "target/generated-rust/src", stackTraceElement);

        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, rustGenerator);
    }


}