package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Supplier;

import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.annotations.NoSerialization;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Iterator.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;

public class CompilerBeanCompleter2Composite {
    public static final String OUT_VAR = "out";
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBeanCompleter2Composite(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    SpecificationFile generateBeanCompleter2Composite(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        Class pastClass = pastFactory.CLASS(COMPOSITE_BEAN_COMPLETER2)
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATION(NoSerialization.NAME);


        // fields
        pastClass.FIELDS(
                FIELD(LL_VAR, LIST_MAP_STRING_OBJECT).MODIFIERS(Modifier.FINAL),
                FIELD(M_VAR, MAP_STRING_OBJECT).MODIFIERS(Modifier.FINAL)
        );

        Constructor constructor = CONSTRUCTOR()
                 .MODIFIERS(Modifier.PUBLIC)
                 .PARAMETER(MAP_STRING_OBJECT, M_VAR)
                 .commentFileLocation()
                 .BODY(
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), LL_VAR),
                                CAST(LIST_MAP_STRING_OBJECT, METHOD_CALL(VARIABLE(M_VAR), "get", List.of(CONSTANT(ELEMENTS))))),
                         ASSIGNMENT( METHOD_CALL(VARIABLE("this"), M_VAR), VARIABLE(M_VAR))
                 );
         pastClass.CONSTRUCTOR(constructor);

        // For each composite template create a process method
        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof SimpleTemplateCompilerConfig) continue;
            CompositeTemplateCompilerConfig config1 = (CompositeTemplateCompilerConfig) config;
            String consistsOf = config1.consistsOf;

            String shortName = locations.getShortNames().get(consistsOf);
            String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
            ClassName outClass = get(outputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));

            ClassName composeeClass = get(compilerUtil.outputsNameClass(shortName), locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));

            Method m = METHOD(PROCESS_METHOD_NAME)
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(outClass, BEAN_VAR)
                    .RETURNS(outClass)
                    .commentFileLocation()
                    .BODY(
                            ASSIGNMENT(
                                    METHOD_CALL(VARIABLE(BEAN_VAR), "ID"),
                                    CAST(INTEGER,
                                            METHOD_CALL(METHOD_CALL(VARIABLE("this"),M_VAR), "get", List.of(CONSTANT("ID")))  )   ),

                            ITERATOR(
                                    PARAMETER(ELEM_VAR, MAP_STRING_OBJECT),
                                    METHOD_CALL(VARIABLE("this"),LL_VAR))
                                    .BODY(
                                            DEFINITION(composeeClass, VARIABLE(OUT_VAR), CONSTRUCTOR_CALL(composeeClass, List.of())),
                                            METHOD_CALL(
                                                    VARIABLE(BEAN_VAR),
                                                    ADD_ELEMENTS,
                                                    List.of(
                                                            METHOD_CALL(
                                                                    CONSTRUCTOR_CALL(get(BEAN_COMPLETER2, locations.getFilePackage(configs.name, BEAN_COMPLETER2)), List.of(VARIABLE(ELEM_VAR))),
                                                                    PROCESS_METHOD_NAME,
                                                                    List.of(VARIABLE(OUT_VAR))
                                                            )
                                                    )
                                            )
                                    ),

                            RETURN(VARIABLE(BEAN_VAR))
                    );

            pastClass.METHOD(m);
        }

        String myPackage=locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, myPackage, locations, stackTraceElement);

        return new SpecificationFile(javaGenerator, pythonGenerator,  jsGenerator, rustGenerator);
    }


}