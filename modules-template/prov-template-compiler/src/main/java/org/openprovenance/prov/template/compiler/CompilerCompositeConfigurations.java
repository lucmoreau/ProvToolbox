package org.openprovenance.prov.template.compiler;


import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Constructor;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.FUNCTIONAL_METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerCompositeConfigurations {
    private final CompilerUtil compilerUtil;

    public CompilerCompositeConfigurations(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    static final String ENACTOR_VAR = "beanEnactor";


    public SpecificationFile generateCompositeConfigurator(TemplatesProjectConfiguration configs,
                                                           Locations locations,
                                                           TypeName typeName,
                                                           QuadConsumer<String, Method, ClassName, TypeName> generator,
                                                           String generatorMethod,
                                                           TypeName beanProcessor,
                                                           String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        final ParameterizedType tableConfiguratorType = ParameterizedType.get(ClassName.get(COMPOSITE_TABLE_CONFIGURATOR, locations.getFilePackage(configs.name, COMPOSITE_TABLE_CONFIGURATOR)), typeName);

        PastFactory pastFactory=new PastFactory();
        Class pastClass = pastFactory.CLASS(fileName)
                .MODIFIERS(Modifier.PUBLIC);

        // the following in only used for the enactorConfigurator
        if (beanProcessor!=null) {
            pastClass.FIELDS(FIELD(ENACTOR_VAR, beanProcessor).MODIFIERS(Modifier.FINAL, Modifier.PRIVATE));
            Constructor cspec= CONSTRUCTOR()
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(beanProcessor, ENACTOR_VAR);
            compilerUtil.debugFileLocation(cspec);
            cspec.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE("this"), ENACTOR_VAR), VARIABLE(ENACTOR_VAR)));
            pastClass.CONSTRUCTOR(cspec);
        }

        pastClass.INTERFACES(tableConfiguratorType);


        for (TemplateCompilerConfig config : configs.templates) {

            if (!(config instanceof SimpleTemplateCompilerConfig )) {
                final String templateNameClass = compilerUtil.templateNameClass(config.name);
                final String beanNameClass = compilerUtil.commonNameClass(config.name);
                final ClassName className = ClassName.get(templateNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));
                String builderParameter = "builder";
                Method mspec = METHOD(config.name)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(className, builderParameter)
                        .RETURNS(typeName);
                generator.accept(builderParameter, mspec, className, ClassName.get(beanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON)));
                pastClass.METHOD(mspec);
            }

        }

        String myPackage=locations.getFilePackage(configs.name, fileName);

        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator,pythonGenerator);

    }

    public SpecificationFile generateCompositeEnactorConfigurator(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        return  generateCompositeConfigurator(configs, locations, FUNCTION_LIST_OBJARRAY_TO_ANY, this::generateMethodEnactor, "generateCompositeConfigurator", ClassName.get(BEAN_PROCESSOR, locations.getFilePackage(configs.name, BEAN_PROCESSOR)), fileName);
    }


    public void generateMethodEnactor(String builderParameter, Method mspec, ClassName className, TypeName beanType) {
        mspec.BODY(
                DEFINITION(FUNCTION_LIST_OBJARRAY_TO_TYPE(beanType), VARIABLE("beanConverter"),
                        METHOD_CALL(VARIABLE(builderParameter), "aRecord2BeanConverter")),

                DEFINITION(FUNCTION_LIST_OBJARRAY_TO_TYPE(beanType), VARIABLE("enactor"),
                        LAMBDA(PARAMETER("array", LIST_OF_OBJECT_ARRAYS))
                                .returns(beanType)
                                .BODY(DEFINITION(beanType, VARIABLE("bean"),
                                                FUNCTIONAL_METHOD_CALL(VARIABLE("beanConverter"), "apply", List.of(VARIABLE("array")))),
                                        RETURN(
                                                FUNCTIONAL_METHOD_CALL(
                                                        VARIABLE(ENACTOR_VAR, FIELD_VARIABLE),
                                                        "process",
                                                        List.of(VARIABLE("bean"))
                                                )
                                        ))),
                RETURN(VARIABLE("enactor"))
        );
    }

    public SpecificationFile generateCompositeConfigurator2(TemplatesProjectConfiguration configs,
                                                            Locations locations,
                                                            TypeName typeName,
                                                            QuintetConsumer<String, Method, ClassName, TypeName, TypeName> generator,
                                                            String generatorMethod,
                                                            TypeName beanProcessor,
                                                            String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        final ParameterizedType tableConfiguratorType = ParameterizedType.get(ClassName.get(COMPOSITE_TABLE_CONFIGURATOR, locations.getFilePackage(configs.name, COMPOSITE_TABLE_CONFIGURATOR)), typeName);

        PastFactory pastFactory=new PastFactory();
        Class pastClass = pastFactory.CLASS(fileName)
                .MODIFIERS(Modifier.PUBLIC);

        // the following in only used for the enactorConfigurator
        if (beanProcessor!=null) {
            pastClass.FIELDS(FIELD(ENACTOR_VAR, beanProcessor).MODIFIERS(Modifier.FINAL, Modifier.PRIVATE));
            Constructor cspec= CONSTRUCTOR()
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(beanProcessor, ENACTOR_VAR);
            compilerUtil.debugFileLocation(cspec);
            cspec.BODY(ASSIGNMENT( METHOD_CALL(VARIABLE("this"), ENACTOR_VAR), VARIABLE(ENACTOR_VAR)));
            pastClass.CONSTRUCTOR(cspec);
        }


        pastClass.INTERFACES(tableConfiguratorType);


        for (TemplateCompilerConfig config : configs.templates) {

            if (!(config instanceof SimpleTemplateCompilerConfig )) {
                final String templateNameClass = compilerUtil.templateNameClass(config.name);
                final String beanNameClass = compilerUtil.commonNameClass(config.name);
                final String inputNameClass = compilerUtil.inputsNameClass(config.name);
                final String outputNameClass = compilerUtil.outputsNameClass(config.name);
                final ClassName commonClassName = ClassName.get(templateNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));
                String builderParameter = "builder";
                Method mspec = METHOD(config.name)
                        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                        .PARAMETER(commonClassName, builderParameter)
                        .RETURNS(typeName);
                compilerUtil.debugFileLocation(mspec);
                generator.accept(builderParameter, mspec, commonClassName, ClassName.get(inputNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS)), ClassName.get(outputNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS)));
                pastClass.METHOD(mspec);
            }

        }

        String myPackage=locations.getFilePackage(configs.name, fileName);

        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator,pythonGenerator);

    }

    public SpecificationFile generateCompositeEnactorConfigurator2(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        return  generateCompositeConfigurator2(configs, locations, FUNCTION_LIST_OBJARRAY_TO_ANY, this::generateMethodEnactor2, "generateCompositeConfigurator", ClassName.get(INPUT_OUTPUT_PROCESSOR, locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR)), fileName);
    }

    public void generateMethodEnactor2(String builderParameter, Method mspec, ClassName className, TypeName inBeanType, TypeName outBeanType) {
        mspec.BODY(
                DEFINITION(FUNCTION_LIST_OBJARRAY_TO_TYPE(inBeanType), VARIABLE("beanConverter"),
                        METHOD_CALL(METHOD_CALL(VARIABLE(builderParameter), "getIntegrator", List.of()),
                                "aRecord2InputsConverter")),

                DEFINITION(FUNCTION_LIST_OBJARRAY_TO_TYPE(outBeanType), VARIABLE("enactor"),
                        LAMBDA(PARAMETER("array", LIST_OF_OBJECT_ARRAYS))
                                .returns(outBeanType)
                                .BODY(DEFINITION(inBeanType, VARIABLE("bean"),
                                                FUNCTIONAL_METHOD_CALL(VARIABLE("beanConverter"), "apply", List.of(VARIABLE("array")))),
                                        RETURN(
                                                FUNCTIONAL_METHOD_CALL(
                                                        VARIABLE(ENACTOR_VAR, FIELD_VARIABLE),
                                                        "process",
                                                        List.of(VARIABLE("bean"))
                                                )
                                        ))),
                RETURN(VARIABLE("enactor"))
        );
    }


}
