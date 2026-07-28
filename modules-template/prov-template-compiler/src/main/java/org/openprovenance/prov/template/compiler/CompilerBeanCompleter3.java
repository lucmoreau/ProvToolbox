package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.annotations.NoSerialization;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.SUPER_METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.SuperConstructorCall.SUPER_CALL;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerBeanCompleter3 {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBeanCompleter3(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    SpecificationFile generateBeanCompleter3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        ClassName GETTER_TYPE = get(GETTER, locations.getFilePackage("ignoreme", GETTER ));

        Class pastClass = pastFactory.CLASS(BEAN_COMPLETER3)
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .SUPERCLASS(get(BEAN_COMPLETER2, locations.getFilePackage(configs.name, BEAN_COMPLETER2)))
                .ANNOTATION(NoSerialization.NAME);


        // constructors mirroring super(m) and super(getter)
        Constructor cons1 = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(MAP_STRING_OBJECT, "m")
                .commentFileLocation()
                .BODY(
                        // call super(m)
                        SUPER_CALL( List.of(VARIABLE("m")) )
                );
        pastClass.CONSTRUCTOR(cons1);

        Constructor cons2 = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(GETTER_TYPE, "getter")
                .BODY(
                        SUPER_CALL( List.of(VARIABLE("getter")) )
                );
        pastClass.CONSTRUCTOR(cons2);

        // abstract methods
        Method getValue = METHOD("getValueFromLocation")
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .RETURNS(INTEGER);
        pastClass.METHOD(getValue);

        Method setValue = METHOD("setValueInLocation")
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .RETURNS(VOID);
        pastClass.METHOD(setValue);

        // gather composee templates
        Set<String> composeeTemplates = new HashSet<>();
        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof CompositeTemplateCompilerConfig) {
                CompositeTemplateCompilerConfig config1 = (CompositeTemplateCompilerConfig) config;
                composeeTemplates.add(config1.consistsOf);
            }
        }

        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof SimpleTemplateCompilerConfig) {
                if (composeeTemplates.contains(config.fullyQualifiedName)) {
                    final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
                    final ClassName outputClassName = get(outputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));

                    Method mspec = METHOD(PROCESS_METHOD_NAME)
                            .MODIFIERS(Modifier.PUBLIC)
                            .PARAMETER(outputClassName, BEAN_VAR)
                            .RETURNS(outputClassName)
                            .commentFileLocation()
                            .BODY(
                                    // super.process(bean)
                                    SUPER_METHOD_CALL(PROCESS_METHOD_NAME, List.of(VARIABLE(BEAN_VAR))),
                                    METHOD_CALL("setValueInLocation",List.of()),
                                    // placeholder for calling super.process
                                    RETURN(VARIABLE(BEAN_VAR))
                            );
                    // Note: we cannot directly express a super.method call as a MethodCall object, the emitter will render it from code patterns in other places.
                    pastClass.METHOD(mspec);
                }
            } else {
                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
                final ClassName outputClassName = get(outputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));

                Method mspec = createCompositeProcessMethod(config.fullyQualifiedName,outputClassName);
                pastClass.METHOD(mspec);

            }
        }

        String myPackage=locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    private Method createCompositeProcessMethod(String templateFullyQualifiedName, ClassName outputClassName) {
        Method mspec = METHOD(Constants.PROCESS_METHOD_NAME)
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(outputClassName,BEAN_VAR)
                .RETURNS(outputClassName);

        mspec.BODY(

                DEFINITION(outputClassName, VARIABLE("result"), SUPER_METHOD_CALL(PROCESS_METHOD_NAME, List.of(VARIABLE(BEAN_VAR)))),

                ASSIGNMENT( METHOD_CALL(VARIABLE("result"), "ID"), METHOD_CALL("getValueFromLocation",List.of())),

                // PRINT STATEMENT

                METHOD_CALL(POST_PROCESS_METHOD_NAME, List.of(METHOD_CALL(VARIABLE("result"), "ID"), CONSTANT(templateFullyQualifiedName))),

                RETURN(VARIABLE("result"))         );

        return mspec;
    }

}