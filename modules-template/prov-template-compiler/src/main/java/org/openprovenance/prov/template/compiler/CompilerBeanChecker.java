package org.openprovenance.prov.template.compiler;

import org.apache.commons.lang3.tuple.Triple;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.common.BeanDirection.COMMON;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Iterator.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.ThrowStatement.THROW;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerBeanChecker {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBeanChecker(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }



    public SpecificationFile generateBeanChecker(TemplatesProjectConfiguration configs, Locations locations, BeanDirection direction, Map<String, Map<String, Triple<String, List<String>, TemplateBindingsSchema>>> variantTable, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        Class pastClass= pastFactory
                .CLASS(fileName)
                .MODIFIERS(Modifier.PUBLIC);

        String packageForBeanProcessor;
        if (direction== COMMON) {
            packageForBeanProcessor=locations.getFilePackage(configs.name, Constants.BEAN_PROCESSOR);
            pastClass.INTERFACES(ClassName.get(BEAN_PROCESSOR, packageForBeanProcessor));
        } else {
            packageForBeanProcessor=locations.getFilePackage(configs.name, INPUT_PROCESSOR);
            pastClass.INTERFACES(ClassName.get(INPUT_PROCESSOR, packageForBeanProcessor));
        }

        Method mspec0 = METHOD(Constants.NOT_NULL_METHOD)
                .commentFileLocation()
                .MODIFIERS(Modifier.PRIVATE,Modifier.FINAL, Modifier.STATIC)
                .PARAMETER(T(),"object")
                .PARAMETER(STRING,"field")
                .PARAMETER(STRING,"template")
                .addTypeVariables(T())
                .RETURNS(T()).BODY(
                        IF(BINARY_OP(VARIABLE("object"), "==", Constant.getNull()))
                                .THEN(THROW(
                                        CONSTRUCTOR_CALL(ILLEGAL_ARGUMENT_EXCEPTION,
                                                List.of(CONSTANT("The object field is null in template")
                                                        //   VARIABLE("field"),
                                                        //   CONSTANT(" is null in template "),

                                                        //  VARIABLE("template")
                                                )))  ),
                        RETURN(VARIABLE("object")));

        pastClass.METHOD(mspec0);


        for (TemplateCompilerConfig config : configs.templates) {

            String packageForBeans2=locations.getBeansPackage(config.fullyQualifiedName, direction);
            pastClass.METHOD(generateCheckerMethod(config.name, null, config, direction, packageForBeans2, null, locations));
        }

        if (variantTable!=null) {
            variantTable.keySet().forEach(
                    templateFullyQualifiedName -> {
                        Map<String, Triple<String, List<String>, TemplateBindingsSchema>> allVariants = variantTable.get(templateFullyQualifiedName);
                        allVariants.keySet().forEach(
                                shared -> {
                                    Triple<String, List<String>, TemplateBindingsSchema> triple = allVariants.get(shared);
                                    String extension = triple.getLeft();
                                    List<String> sharing = triple.getMiddle();
                                    TemplateBindingsSchema tbs=triple.getRight();

                                    TemplateCompilerConfig config = Arrays.stream(configs.templates).filter(c -> Objects.equals(c.fullyQualifiedName, templateFullyQualifiedName)).findFirst().get();
                                    SimpleTemplateCompilerConfig sConfig = (SimpleTemplateCompilerConfig) config;
                                    SimpleTemplateCompilerConfig sConfig2 = sConfig.cloneAsInstanceInComposition(sConfig.name + extension, templateFullyQualifiedName + extension, null);
                                    String packageForBeans2=locations.getBeansPackage(config.fullyQualifiedName, direction);

                                    pastClass.METHOD(generateCheckerMethod(sConfig.name , extension, sConfig2, direction, packageForBeans2, sharing, locations));

                                }
                        );
                    }
            );
        }

        String myPackage=locations.getFilePackage(configs.name,fileName);


        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, locations, stackTraceElement);

        return new SpecificationFile(javaGenerator,pythonGenerator, jsGenerator, emptyGenerator);



    }

    public Method generateCheckerMethod(String templateName, String extension, TemplateCompilerConfig config, BeanDirection direction, String packageForBeans, List<String> sharing, Locations locations) {
        final String beanNameClass = compilerUtil.beanNameClass(templateName, direction, extension);

        final ClassName className = get(beanNameClass, packageForBeans);
        Method mspec = METHOD(PROCESS_METHOD_NAME)
                .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                .PARAMETER(className,"bean")
                .RETURNS(className);
        compilerUtil.debugFileLocation(mspec);


        if (config instanceof SimpleTemplateCompilerConfig) {
            TemplateBindingsSchema bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

            for (String key : descriptorUtils.fieldNames(bindingsSchema)) {
                if (descriptorUtils.isCompulsoryInput(key, bindingsSchema)) {
                    mspec.BODY(METHOD_CALL(NOT_NULL_METHOD, List.of(METHOD_CALL(VARIABLE("bean"), key),CONSTANT(key), CONSTANT(templateName))));

                }
            }


            if (sharing != null) {
                sharing.forEach(shared -> {
                    mspec.BODY(METHOD_CALL(NOT_NULL_METHOD, List.of(METHOD_CALL(VARIABLE("bean"), shared),CONSTANT("shared"), CONSTANT(templateName))));

                });
            }
        } else {

            CompositeTemplateCompilerConfig config1 = (CompositeTemplateCompilerConfig) config;

            String shortConsistOfName = locations.getShortNames().get(config1.consistsOf);
            final String innerNameClass2 = compilerUtil.beanNameClass(shortConsistOfName, direction, direction.equals(COMMON)?extension:"_1"); // LUC: TODO FIXME: extension is hard coded
            final ClassName innerClassName2 = get(innerNameClass2, locations.getBeansPackage(config1.fullyQualifiedName, direction));

            mspec.BODY(
                    ITERATOR(
                            PARAMETER("el", innerClassName2),
                            METHOD_CALL(VARIABLE("bean"), ELEMENTS))
                            .BODY(
                                    METHOD_CALL(PROCESS_METHOD_NAME, List.of(VARIABLE("el")))));
        }
        mspec.BODY(RETURN(VARIABLE("bean")));

        return mspec;
    }

}