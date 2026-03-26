package org.openprovenance.prov.template.compiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Constant;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.Parameter;
import org.openprovenance.prov.template.compiler.past.annotations.OverrideAnnotation;
import org.openprovenance.prov.template.compiler.past.annotations.RegisterMethod;
import org.openprovenance.prov.template.compiler.past.annotations.SingleDispatchMethod;
import org.openprovenance.prov.template.compiler.past.annotations.StatefulProcessor;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.DescriptorUtils;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;


import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Iterator.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerBeanLocalEnactor2 {
    static Logger logger = LogManager.getLogger(CompilerBeanLocalEnactor2.class);
    private final CompilerUtil compilerUtil;

    public CompilerBeanLocalEnactor2(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    static DescriptorUtils descriptorUtils=new DescriptorUtils();

    SpecificationFile generateBeanLocalEnactor2(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        ClassName ioProcessorClass = ClassName.get(INPUT_OUTPUT_PROCESSOR, locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR));

        org.openprovenance.prov.template.compiler.past.Class pastClass=compilerUtil.getPastFactory()
                .CLASS(Constants.BEAN_LOCAL_ENACTOR2)
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .INTERFACES(ioProcessorClass);

        Method method1=METHOD("newIdentifier")
                .MODIFIERS(Modifier.PUBLIC,Modifier.ABSTRACT)
                .PARAMETER(STRING,"field")
                .PARAMETER(STRING,"counter")
                .ANNOTATIONS(StatefulProcessor.NAME)
                .RETURNS(INTEGER);

        Method method2=METHOD("newSIdentifier")
                .MODIFIERS(Modifier.PUBLIC,Modifier.ABSTRACT)
                .PARAMETER(STRING,"field")
                .PARAMETER(STRING,"counter")
                .ANNOTATIONS(StatefulProcessor.NAME)
                .RETURNS(STRING);


        pastClass.METHOD(method1);
        pastClass.METHOD(method2);

        boolean isFirst=true;


        for (TemplateCompilerConfig config : configs.templates) {
            final String outputNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputNameClass = compilerUtil.inputsNameClass(config.name);
            final ClassName outputClassName = get(outputNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));
            final ClassName inputClassName = get(inputNameClass,locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));

            Method mspec = METHOD(Constants.PROCESS_METHOD_NAME)
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(inputClassName,"bean")
                    .RETURNS(outputClassName)
                    .ANNOTATIONS(RegisterMethod.NAME, OverrideAnnotation.NAME);

            if (isFirst) {
                mspec.ANNOTATIONS(SingleDispatchMethod.NAME);
                isFirst=false;
            }

            compilerUtil.debugFileLocation(mspec);

            mspec.BODY(DEFINITION(outputClassName, VARIABLE(OUT_BEAN), CONSTRUCTOR_CALL(outputClassName, List.of())));

            if (config instanceof SimpleTemplateCompilerConfig) {
                TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);
                final Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();


                // for all output fields, call newIdentifier method, assigning the field in the output bean
                for (String field: descriptorUtils.fieldNames(bindingsSchema)) {
                    if (descriptorUtils.isOutput(field,bindingsSchema)) {
                        Optional<String> sqlTable=descriptorUtils.getSqlTable(field,bindingsSchema);

                        Class<?> theType=compilerUtil.getJavaTypeForDeclaredType(theVar, field);
                        if (theType==String.class)
                            mspec.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE(OUT_BEAN),field), METHOD_CALL(VARIABLE("this"), "newSIdentifier", List.of(CONSTANT(field), CONSTANT(sqlTable.orElse("sql/" + field))))));
                        else
                            mspec.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE(OUT_BEAN),field), METHOD_CALL(VARIABLE("this"), "newIdentifier", List.of(CONSTANT(field), CONSTANT(sqlTable.orElse("sql/" + field))))));
                    }
                }
                mspec.BODY(

                        ASSIGNMENT( METHOD_CALL(VARIABLE(OUT_BEAN),"ID"), METHOD_CALL(VARIABLE("this"), "newIdentifier", List.of(CONSTANT("template/" + config.name), CONSTANT("template/" + config.name)))),

                        RETURN(VARIABLE(OUT_BEAN))
                );


            } else {
                CompositeTemplateCompilerConfig config1 = (CompositeTemplateCompilerConfig) config;

                String extension="_1";
                logger.warn("Using fixed extension "+extension+" in CompositeTemplateCompilerConfig processing. This is a temporary measure. Template " + config1.fullyQualifiedName);

                String shortConsistOfName = locations.getShortNames().get(config1.consistsOf);
                final String inputNameClass2 = compilerUtil.inputsNameClass(shortConsistOfName, extension);
                final ClassName inputClassName2 = get(inputNameClass2, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.INPUTS));
                final String outputNameClass2 = compilerUtil.outputsNameClass(shortConsistOfName); // no extension in outputs
                final ClassName outputClassName2 = get(outputNameClass2, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.OUTPUTS));


                mspec.BODY(DEFINITION(
                        MAP_STRING_MAP_INTEGER_INTEGER,
                        VARIABLE(MAP_VAR),
                        CONSTRUCTOR_CALL(HASHMAP, List.of())
                ));
                for (String field : config1.sharing) {
                    mspec.BODY(METHOD_CALL(VARIABLE(MAP_VAR), "put", List.of(CONSTANT(field), CONSTRUCTOR_CALL(HASHMAP, List.of()))));
                }

                mspec.BODY(

                        ITERATOR(
                                Parameter.PARAMETER("in1", inputClassName2),
                                METHOD_CALL(VARIABLE("bean"), ELEMENTS))

                                .BODY(
                                        METHOD_CALL(
                                                VARIABLE(OUT_BEAN),
                                                ADD_ELEMENTS,
                                                List.of(METHOD_CALL(
                                                        VARIABLE("this"),
                                                        Constants.PROCESS_METHOD_NAME,
                                                        List.of(VARIABLE("in1"), VARIABLE(MAP_VAR)))
                                                ))),


                        ASSIGNMENT(
                                METHOD_CALL(VARIABLE(OUT_BEAN),"ID"),
                                METHOD_CALL(VARIABLE("this"), "newIdentifier", List.of(CONSTANT("template/" + config1.name), CONSTANT("template/" + config1.name)))),

                        RETURN(VARIABLE(OUT_BEAN))
                );




                Method mspec2 = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC)
                        .PARAMETER(inputClassName2, "bean")
                        .PARAMETER(MAP_STRING_MAP_INTEGER_INTEGER, MAP_VAR)
                        .RETURNS(outputClassName2)
                        .ANNOTATIONS(RegisterMethod.NAME, StatefulProcessor.NAME);



                mspec2.BODY(DEFINITION(outputClassName2, VARIABLE(OUT_BEAN), CONSTRUCTOR_CALL(outputClassName2, List.of())));


                TemplateBindingsSchema bindingsSchema=null;
                for (TemplateCompilerConfig config2: configs.templates) {
                    if (config2.fullyQualifiedName.equals(config1.consistsOf)) {
                        bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config2);
                    }
                }
                if (bindingsSchema==null) {
                    throw new RuntimeException("Cannot find bindings schema for "+config1.consistsOf);
                }

                final Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();

                for (String field: descriptorUtils.fieldNames(bindingsSchema)) {
                    if (descriptorUtils.isOutput(field,bindingsSchema)) {
                        Optional<String> sqlTable = descriptorUtils.getSqlTable(field, bindingsSchema);
                        if (config1.sharing.contains(field)) {
                            mspec2.BODY(IF(BINARY_OP(METHOD_CALL(VARIABLE(MAP_VAR), "get", List.of(CONSTANT(field))), "==", Constant.getNull()))
                                    .THEN(
                                            ASSIGNMENT(
                                                    METHOD_CALL(VARIABLE(OUT_BEAN), field),
                                                    METHOD_CALL(VARIABLE("this"), "newIdentifier", List.of(CONSTANT(field), CONSTANT(sqlTable.orElse("sql/" + field)))))
                                    )
                                    .ELSE(IF(
                                            METHOD_CALL(
                                                    METHOD_CALL(VARIABLE(MAP_VAR), "get", List.of(CONSTANT(field))),
                                                    "containsKey",
                                                    List.of(METHOD_CALL(VARIABLE(BEAN_VAR), field)) )  )
                                            .THEN(
                                                    ASSIGNMENT(
                                                            METHOD_CALL(VARIABLE(OUT_BEAN), field),
                                                            METHOD_CALL(
                                                                    METHOD_CALL(VARIABLE(MAP_VAR), "get", List.of(CONSTANT(field))),
                                                                    "get",
                                                                    List.of(METHOD_CALL(VARIABLE(BEAN_VAR), field))  ))   )
                                            .ELSE(
                                                    ASSIGNMENT(
                                                            METHOD_CALL(VARIABLE(OUT_BEAN), field),
                                                            METHOD_CALL(VARIABLE("this"), "newIdentifier", List.of(CONSTANT(field), CONSTANT(sqlTable.orElse("sql/" + field))))   ),
                                                    METHOD_CALL(
                                                            METHOD_CALL(VARIABLE(MAP_VAR), "get", List.of(CONSTANT(field))),
                                                            "put",
                                                            List.of(
                                                                    METHOD_CALL(VARIABLE("bean"), field),
                                                                    METHOD_CALL(VARIABLE(OUT_BEAN), field)  )   )    )
                                    )
                            );
                        } else {
                            Class<?> theType = compilerUtil.getJavaTypeForDeclaredType(theVar, field);
                            if (theType == String.class)
                                mspec2.BODY(ASSIGNMENT(
                                        METHOD_CALL(VARIABLE(OUT_BEAN),field),
                                        METHOD_CALL(VARIABLE("this"), "newSIdentifier", List.of(CONSTANT(field), CONSTANT(sqlTable.orElse("sql/" + field))))));
                            else
                                mspec2.BODY(ASSIGNMENT(
                                        METHOD_CALL(VARIABLE(OUT_BEAN),field),
                                        METHOD_CALL(VARIABLE("this"), "newIdentifier", List.of(CONSTANT(field), CONSTANT(sqlTable.orElse("sql/" + field))))));
                        }
                    }
                }
                mspec2.BODY(
                        ASSIGNMENT(
                                METHOD_CALL(VARIABLE(OUT_BEAN),"ID"),
                                METHOD_CALL(VARIABLE("this"), "newIdentifier", List.of(CONSTANT("template/" + shortConsistOfName), CONSTANT("template/" + shortConsistOfName)))),
                        RETURN(VARIABLE(OUT_BEAN))
                );
                pastClass.METHOD(mspec2);
            }
            pastClass.METHOD(mspec);
        }

        String myPackage= locations.getFilePackage(configs.name, fileName);

        String directory = locations.convertToDirectory(myPackage);
        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + ".java", directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, "target/generated-js", stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, myPackage, "target/generated-rust/src", stackTraceElement);
        return new SpecificationFile(javaGenerator,pythonGenerator, jsGenerator, rustGenerator);
    }





}




