package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.annotations.NoSerialization;
import org.openprovenance.prov.template.compiler.past.annotations.OverloadedMethodPython;
import org.openprovenance.prov.template.compiler.past.annotations.OverloadedMethodJavascript;
import org.openprovenance.prov.template.compiler.past.annotations.OverloadedMethodRust;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Supplier;

import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.Class.ClassKind.ANONYMOUS;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.DoLoop.DO;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
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

public class CompilerBeanCompleter2 {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;
    private final boolean debugComment=true;


    public CompilerBeanCompleter2(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    SpecificationFile generateBeanCompleter2(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();
        ClassName GETTER_TYPE = get(GETTER, locations.getFilePackage("ignoreme", GETTER ));

        Class pastClass = pastFactory.CLASS(BEAN_COMPLETER2)
                .MODIFIERS(Modifier.PUBLIC)
                .FIELDS(
                        FIELD("m", MAP_STRING_OBJECT).MODIFIERS(Modifier.FINAL),
                        FIELD(GETTER_VAR, GETTER_TYPE).MODIFIERS(Modifier.FINAL, Modifier.PROTECTED)
                )
                .ANNOTATION(NoSerialization.NAME);

        Method callMe2 = METHOD("getMap")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(CLASS_T, "cl")
                .PARAMETER(STRING, "key")
                .RETURNS(T())
                .addTypeVariables(T())
                .BODY(RETURN(
                        CAST(T(),
                                METHOD_CALL(METHOD_CALL(VARIABLE("this"),"m"), "get", List.of(VARIABLE("key"))))));
        pastClass.METHOD(callMe2);

        Constructor constructor1 = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(MAP_STRING_OBJECT, "m")
                .commentFileLocation()
                .BODY(ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "m"), VARIABLE("m")))
                .COMMENT("The following code implements this assignment, in a way that jsweet can compile")
                .COMMENT("this.getter = this::getMap")
                .BODY(
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), GETTER_VAR),
                                CONSTRUCTOR_CALL(
                                        pastFactory
                                                .CLASS(null, ANONYMOUS)
                                                .INTERFACES(GETTER_TYPE)
                                                .METHOD(
                                                        METHOD("get")
                                                                .MODIFIERS(Modifier.PUBLIC)
                                                                .PARAMETER(CLASS_T, "cl")
                                                                .PARAMETER(STRING, "col")
                                                                .RETURNS(T())
                                                                .addTypeVariables(T())
                                                                .BODY(
                                                                        RETURN(
                                                                                METHOD_CALL(
                                                                                        //VARIABLE("this"), /// note this, would refer to the anonymous class itself, not the object.
                                                                                        // See hack in rust and python emitters, inserting self.outer
                                                                                        "getMap",
                                                                                        List.of(VARIABLE("cl"), VARIABLE("col"))
                                                                                )
                                                                        )
                                                                )
                                                ),
                                        List.of())));

        pastClass.CONSTRUCTOR(constructor1);

        Constructor constructor2 = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(GETTER_TYPE, GETTER_VAR)
                .BODY(
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "m"), Constant.getNull()),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), GETTER_VAR), VARIABLE(GETTER_VAR)));

        constructor2.annotation.add(new OverloadedMethodPython("____init2__"));
        constructor2.annotation.add(new OverloadedMethodRust("make" + BEAN_COMPLETER2));
        constructor2.annotation.add(new OverloadedMethodJavascript("make" + BEAN_COMPLETER2));

        pastClass.CONSTRUCTOR(constructor2);

        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof SimpleTemplateCompilerConfig) {

                TemplateBindingsSchema bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
                final String inputBeanNameClass = compilerUtil.inputsNameClass(config.name);

                final ClassName outputClassName = get(outputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));
                Method mspecOut = METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC)
                        .PARAMETER(outputClassName, BEAN_VAR)
                        .RETURNS(outputClassName)
                        .BODY(
                                // if output, set ID and call post processing

                                ASSIGNMENT(
                                        METHOD_CALL(VARIABLE(BEAN_VAR), "ID"),
                                        METHOD_CALL(METHOD_CALL(VARIABLE("this"),GETTER_VAR), "get",
                                                List.of(METHOD_CALL(INTEGER, "class"), CONSTANT("ID")))
                                ),

                                // call postEnactmentProcessing(bean.ID, fullyQualifiedName)
                                // CAST(INTEGER, ...) triggers unwrap_or_default() in the Rust emitter,
                                // since struct fields are Option<i32> in Rust.

                                METHOD_CALL(VARIABLE("this"),POST_PROCESS_METHOD_NAME, List.of(CAST(INTEGER, METHOD_CALL(VARIABLE(BEAN_VAR), "ID")), CONSTANT(config.fullyQualifiedName)))
                        );

                for (String key : descriptorUtils.fieldNames(bindingsSchema)) {
                    if (descriptorUtils.isOutput(key, bindingsSchema)) {
                        ClassName cl = compilerUtil.getPastTypeForDeclaredType(bindingsSchema.getVar(), key);
                        mspecOut.BODY(ASSIGNMENT(
                                METHOD_CALL(VARIABLE(BEAN_VAR), key),
                                METHOD_CALL(
                                        METHOD_CALL(VARIABLE("this"),GETTER_VAR),
                                        "get",
                                        List.of(
                                                METHOD_CALL(cl, "class"),
                                                CONSTANT(key)
                                        )
                                )
                        ));
                    }
                }

                mspecOut.BODY(RETURN(VARIABLE(BEAN_VAR)));
                pastClass.METHOD(mspecOut);

                // input variant
                final ClassName inputClassName = get(inputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));
                Method mspecIn = METHOD(Constants.PROCESS_METHOD_NAME)
                        .MODIFIERS(Modifier.PUBLIC)
                        .PARAMETER(inputClassName, BEAN_VAR)
                        .RETURNS(inputClassName);
                compilerUtil.debugFileLocation(mspecIn);

                for (String key : descriptorUtils.fieldNames(bindingsSchema)) {
                    if (descriptorUtils.isInput(key, bindingsSchema)) {
                        ClassName cl = compilerUtil.getPastTypeForDeclaredType(bindingsSchema.getVar(), key);
                        mspecIn.BODY(ASSIGNMENT(
                                METHOD_CALL(VARIABLE(BEAN_VAR), key),
                                METHOD_CALL(
                                        METHOD_CALL(VARIABLE("this"),GETTER_VAR),
                                        "get",
                                        List.of(
                                                METHOD_CALL(cl, "class"),
                                                CONSTANT(key)
                                        )
                                )
                        ));
                    }
                }

                mspecIn.BODY(RETURN(VARIABLE(BEAN_VAR)));
                pastClass.METHOD(mspecIn);

            } else {

                CompositeTemplateCompilerConfig config1 = (CompositeTemplateCompilerConfig) config;

                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
                final ClassName outputClassName = get(outputBeanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));

                String shortConsistsOf = locations.getShortNames().get(config1.consistsOf);
                String composeeName = compilerUtil.outputsNameClass(shortConsistsOf);
                ClassName composeeClass = get(composeeName, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS));



                Method mspec0=METHOD(Constants.PROCESS_METHOD_NAME)
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC)
                        .PARAMETER(outputClassName, BEAN_VAR)
                        .RETURNS(outputClassName)
                        .BODY(
                                ASSIGNMENT(METHOD_CALL(VARIABLE(BEAN_VAR),"count"),  CONSTANT(0)),
                                DO()
                                        .BODY(
                                                DEFINITION(composeeClass, VARIABLE("composee"), CONSTRUCTOR_CALL(composeeClass, List.of())),
                                                ASSIGNMENT(VARIABLE("composee"), METHOD_CALL(
                                                        VARIABLE("this"),
                                                        PROCESS_METHOD_NAME,
                                                        List.of(VARIABLE("composee")))),
                                                METHOD_CALL(
                                                        VARIABLE(BEAN_VAR),
                                                        ADD_ELEMENTS,
                                                        List.of(VARIABLE("composee"))),
                                                ASSIGNMENT(METHOD_CALL(VARIABLE(BEAN_VAR),"count"), BINARY_OP(METHOD_CALL(VARIABLE(BEAN_VAR),"count"), "+", CONSTANT(1))),
                                                ASSIGNMENT(METHOD_CALL(VARIABLE(BEAN_VAR),"type"), METHOD_CALL(VARIABLE("composee"),"isA"))
                                        )
                                        .WHILE(METHOD_CALL(VARIABLE("this"), "next", List.of())),
                                RETURN(VARIABLE(BEAN_VAR)));
                pastClass.METHOD(mspec0);

                /*
                Method mspec_incorrect = METHOD(Constants.PROCESS_METHOD_NAME+"Incorrect")
                        .commentFileLocation()
                        .MODIFIERS(Modifier.PUBLIC)
                        .PARAMETER(outputClassName, BEAN_VAR)
                        .RETURNS(outputClassName)
                        .BODY(
                                DEFINITION(_bool, VARIABLE("nextExists"), CONSTANT(true)),
                                ITERATOR(
                                        PARAMETER("composee", composeeClass),
                                        METHOD_CALL(VARIABLE(BEAN_VAR), ELEMENTS))
                                        .BODY(
                                                IF(VARIABLE("nextExists"))
                                                        .THEN(
                                                                METHOD_CALL(
                                                                        VARIABLE("this"),
                                                                        PROCESS_METHOD_NAME,
                                                                        List.of(VARIABLE("composee"))),
                                                                ASSIGNMENT(
                                                                        VARIABLE("nextExists"),
                                                                        METHOD_CALL(
                                                                                VARIABLE("this"),
                                                                                "next",
                                                                                List.of())))
                                                        .ELSE(
                                                                THROW(
                                                                        CONSTRUCTOR_CALL(ILLEGAL_ARGUMENT_EXCEPTION,
                                                                                List.of(CONSTANT("Not enough record in the result")))))),


                                RETURN(VARIABLE(BEAN_VAR)));

                pastClass.METHOD(mspec_incorrect);

                 */

            }
        }

        Method nMethod = METHOD("next")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(_bool)
                .BODY(RETURN(CONSTANT(true)));
        pastClass.METHOD(nMethod);

        Method pMethod = METHOD(POST_PROCESS_METHOD_NAME)
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(INTEGER, "id")
                .PARAMETER(STRING, "template")
                .RETURNS(VOID)
                .BODY(); // empty body
        pastClass.METHOD(pMethod);

        // only for Rust
        generateGetterInterface(configs, locations, GETTER).save();

        String myPackage=locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, myPackage, locations, stackTraceElement);


        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, rustGenerator);

    }

    public  SpecificationFile  generateGetterInterface(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        /*

        public abstract interface Getter {
          <T> T get(Class<T> cl, String col);
        }

         */

        Class pastClass = pastFactory.INTERFACE(fileName)
                .MODIFIERS(Modifier.PUBLIC)
                .METHOD(
                        METHOD("get")
                                .MODIFIERS(Modifier.PUBLIC)
                                .PARAMETER(CLASS_T, "cl")
                                .PARAMETER(STRING, "col")
                                .RETURNS(T())
                                .addTypeVariables(T())
                );

        String myPackage=locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, myPackage, locations, stackTraceElement);

        return new SpecificationFile(emptyGenerator, emptyGenerator, emptyGenerator, rustGenerator);


    }


}