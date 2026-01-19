package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Constant;
import org.openprovenance.prov.template.compiler.past.Constructor;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.model.DOMProcessing.builder;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Expression.ExpressionKind.CAST;
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
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerBeanCompleter {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;


    public CompilerBeanCompleter(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    // FIXME: J4TS does not have java.sql.ResultSet.
    // so disabling this code for now
    final boolean sqlCode=false;

    SpecificationFile generateBeanCompleter(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();


        org.openprovenance.prov.template.compiler.past.Class pastClass =
                pastFactory.CLASS(BEAN_COMPLETER)
                        .MODIFIERS(Modifier.PUBLIC)
                        .INTERFACES(org.openprovenance.prov.template.compiler.past.type.ClassName.get(BEAN_PROCESSOR, locations.getFilePackage(configs.name, BEAN_PROCESSOR)));


        //TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_COMPLETER);

        // builder.addSuperinterface(compilerUtil.getClass(configs.name, Constants.BEAN_PROCESSOR, locations));

        org.openprovenance.prov.template.compiler.past.type.ClassName GETTER_TYPE = get(GETTER, locations.getFilePackage("ignoreme", GETTER ));
        pastClass.FIELDS(
                FIELD("m", MAP_STRING_OBJECT).MODIFIERS(Modifier.FINAL),
                FIELD(GETTER_VAR, GETTER_TYPE).MODIFIERS(Modifier.FINAL));


        Method callMe2 = METHOD("getMap")
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(CLASS_T, "cl")
                .PARAMETER(STRING, "key")
                .RETURNS(T())
                .addTypeVariables(T());
        compilerUtil.debugFileLocation(callMe2);
        callMe2.BODY(RETURN(CAST(T(), METHOD_CALL(VARIABLE("m"), "get", List.of(VARIABLE("key"))))));
        // "("return ($T) m.get($N)", CompilerUtil.typeT, "key");
        pastClass.METHOD(callMe2);

        Constructor constructor1 = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(MAP_STRING_OBJECT, "m");
        compilerUtil.debugFileLocation(constructor1);

        constructor1
                .BODY(ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), "m"), VARIABLE("m")))
                .COMMENT("The following code implements this assignment, in a way that jsweet can compile")
                .COMMENT("this.getter = this::getMap")
                .BODY(
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), GETTER_VAR),
                                CONSTRUCTOR_CALL(
                                        pastFactory
                                                .CLASS(null, org.openprovenance.prov.template.compiler.past.Class.ClassKind.ANONYMOUS)
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
                                                                                        //VARIABLE("this"), /// note this, would refer to the anonymous class itself, not the object
                                                                                        "getMap",
                                                                                        List.of(VARIABLE("cl"), VARIABLE("col"))
                                                                                )
                                                                        )
                                                                )
                                                ),
                                        List.of())));

        /*
        cbuilder2.addStatement("this.getter = $L",
                TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(TypeVariableName.get(Constants.GETTER))
                        .addMethod(MethodSpec.methodBuilder("get")
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(CompilerUtil.classType,"cl")
                                .addParameter(String.class,"col")
                                .returns(CompilerUtil.typeT)
                                .addTypeVariable(CompilerUtil.typeT)
                                .addStatement("return getMap(cl, col)").build()).build());

         */

        pastClass.CONSTRUCTOR(constructor1);

        Constructor constructor2 = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(GETTER_TYPE, GETTER_VAR);
        compilerUtil.debugFileLocation(constructor2);
        constructor2
                .BODY(

                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), "m"), Constant.getNull()),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), GETTER_VAR), VARIABLE(GETTER_VAR)));


        pastClass.CONSTRUCTOR(constructor2);

        for (TemplateCompilerConfig config : configs.templates) {

            final String beanNameClass = compilerUtil.commonNameClass(config.name);

            final org.openprovenance.prov.template.compiler.past.type.ClassName className = get(beanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));
            Method mspec = METHOD(Constants.PROCESS_METHOD_NAME)
                    .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                    .PARAMETER(className, BEAN_VAR)
                    .RETURNS(className);
            if (config instanceof SimpleTemplateCompilerConfig) {
                compilerUtil.debugFileLocation(mspec);

                TemplateBindingsSchema bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);


                for (String key : descriptorUtils.fieldNames(bindingsSchema)) {
                    if (descriptorUtils.isOutput(key, bindingsSchema)) {
                        org.openprovenance.prov.template.compiler.past.type.ClassName cl = compilerUtil.getPastTypeForDeclaredType(bindingsSchema.getVar(), key);
                        mspec.BODY(ASSIGNMENT(null,
                                METHOD_CALL(VARIABLE(BEAN_VAR), key),
                                METHOD_CALL(
                                        VARIABLE(GETTER_VAR),
                                        "get",
                                        List.of(
                                                METHOD_CALL(cl, "class"),
                                                CONSTANT(key)
                                        )
                                )
                        ));
                        //  mspec.addStatement("$N.$N= getter.get($N.class,$S)", BEAN_VAR, key, cl.getSimpleName(), key);
                    }
                }

                mspec.BODY(RETURN(VARIABLE(BEAN_VAR)));
                //mspec.addStatement("return $N", BEAN_VAR);

            } else {
                compilerUtil.debugFileLocation(mspec);

                CompositeTemplateCompilerConfig config1 = (CompositeTemplateCompilerConfig) config;

                String shortConsistsOf = locations.getShortNames().get(config1.consistsOf);
                String composeeName = compilerUtil.commonNameClass(shortConsistsOf);
                ClassName composeeClass = get(composeeName, locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.COMMON));

                mspec.BODY(
                        ASSIGNMENT(_bool, VARIABLE("nextExists"), CONSTANT(true)),
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
                                                                null,
                                                                VARIABLE("nextExists"),
                                                                METHOD_CALL(
                                                                        VARIABLE("this"),
                                                                        "next",
                                                                        List.of())))
                                                .ELSE(

                                                        METHOD_CALL("throw",
                                                                List.of(CONSTRUCTOR_CALL(ILLEGAL_ARGUMENT_EXCEPTION,
                                                                        List.of(CONSTANT("Not enough record in the result"))))))


                                ),

                        RETURN(VARIABLE(BEAN_VAR))
                );




/*

                mspec.addStatement("boolean nextExists=true");
                mspec.beginControlFlow("for ($T composee: $N.$N)", composeeClass, BEAN_VAR, ELEMENTS);
                mspec.beginControlFlow("if (nextExists) " );
                mspec.addStatement("$N(composee)", PROCESS_METHOD_NAME);
                mspec.addStatement("nextExists = next()");
                mspec.nextControlFlow("else");
                mspec.addStatement("System.out.println($S)", "Not enough record in the result");
                mspec.endControlFlow();
                mspec.endControlFlow();

                mspec.addStatement("return $N", BEAN_VAR);

 */

            }

            pastClass.METHOD(mspec);
        }

        Method nMethod = METHOD("next")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(_bool);
        compilerUtil.debugFileLocation(nMethod);
        nMethod.BODY(RETURN(CONSTANT(true)));
        pastClass.METHOD(nMethod);


        String myPackage = locations.getFilePackage(configs.name, fileName);


        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);


        // builder.addMethod(MethodSpec.methodBuilder("next").addModifiers(Modifier.PUBLIC).returns(boolean.class).addStatement("return true").build());
/*

        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(configs.name, fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);


 */
    }

    SpecificationFile generateGetterInterface(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        Class pastClass=pastFactory.INTERFACE(GETTER)
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .METHOD(
                        METHOD("get")
                                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .PARAMETER(CLASS_T, "cl")
                                .PARAMETER(STRING, "col")
                                .RETURNS(T())
                                .addTypeVariables(T())
                );


        String myPackage = locations.getFilePackage(configs.name, fileName);


        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);

        /*
        TypeSpec.Builder inface = compilerUtil.generateInterfaceInit(Constants.GETTER);
        inface.addMethod(MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                .addParameter(CompilerUtil.classType, "cl")
                .addParameter(String.class, "col")
                .returns(CompilerUtil.typeT)
                .addTypeVariable(CompilerUtil.typeT)
                .build());
        builder.addType(inface.build());
*/


    }


    }