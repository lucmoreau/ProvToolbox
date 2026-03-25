package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.annotations.NoSerialization;
import org.openprovenance.prov.template.compiler.past.annotations.OverrideAnnotation;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.IfExpression.IFEXPRESSION;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Iterator.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerIdentifierRegistry {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerIdentifierRegistry(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    SpecificationFile generateIdentifierRegistry(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        Class pastClass = pastFactory.CLASS(IDENTIFIER_REGISTRY)
                .MODIFIERS(Modifier.PUBLIC)
                .FIELDS(FIELD("counterMap", MAP_STRING_ATOMIC_INTEGER).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL),
                        FIELD("recordedValues", MAP_STRING_LIST_INTEGER).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL) ,
                        FIELD("counterInitialValue", INTEGER).MODIFIERS(Modifier.PRIVATE),
                        FIELD("negative", _bool).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                        )
                .ANNOTATION(NoSerialization.NAME);


        Constructor ctor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(MAP_STRING_ATOMIC_INTEGER, "counterMap")
                .PARAMETER(MAP_STRING_LIST_INTEGER, "recordedValues")
                .PARAMETER(_bool, "negative")
                .BODY(
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "counterMap"), VARIABLE("counterMap")),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "recordedValues"), VARIABLE("recordedValues")),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "negative"), VARIABLE("negative")),

                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "counterInitialValue"), BINARY_OP(METHOD_CALL(VARIABLE("this"),"sign", List.of()), "*", CONSTANT(1000)))
                );
        pastClass.CONSTRUCTOR(ctor);

        Method method1=METHOD("newIdentifier")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(STRING,"field")
                .PARAMETER(STRING,"counter")
                //.ANNOTATIONS(OverrideAnnotation.NAME)  // not in InputOutputProcessor
                .RETURNS(INTEGER);


        method1.BODY(
                IF(BINARY_OP(METHOD_CALL(METHOD_CALL(VARIABLE("this"),"counterMap"), "containsKey", List.of(VARIABLE("counter") )), "==", CONSTANT(false)))
                        .THEN(
                                ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "counterInitialValue"),
                                        BINARY_OP(METHOD_CALL(VARIABLE("this"), "counterInitialValue"), "+", BINARY_OP(METHOD_CALL(VARIABLE("this"),"sign", List.of()), "*", CONSTANT(1000)))),
                                METHOD_CALL(METHOD_CALL(VARIABLE("this"),"counterMap"), "put", List.of(VARIABLE("counter"), CONSTRUCTOR_CALL(ATOMIC_INTEGER, List.of(METHOD_CALL(VARIABLE("this"), "counterInitialValue")))))
                        ),
                DEFINITION(INTEGER, VARIABLE("newValue"),
                        IFEXPRESSION(METHOD_CALL(VARIABLE("this"),"negative"),

                                METHOD_CALL(METHOD_CALL(METHOD_CALL(VARIABLE("this"),"counterMap"), "get", List.of(VARIABLE("counter"))),"getAndDecrement", List.of()),

                                METHOD_CALL(METHOD_CALL(METHOD_CALL(VARIABLE("this"),"counterMap"), "get", List.of(VARIABLE("counter"))),"getAndIncrement", List.of()))),

                IF(BINARY_OP(METHOD_CALL(METHOD_CALL(VARIABLE("this"),"recordedValues"), "containsKey",List.of(VARIABLE("field") )),"==", CONSTANT(false)))
                        .THEN(
                                METHOD_CALL(METHOD_CALL(VARIABLE("this"),"recordedValues"), "put", List.of(VARIABLE("field"), CONSTRUCTOR_CALL(LINKED_LIST_OF_INTEGER, List.of())))
                        ),


                METHOD_CALL(METHOD_CALL(METHOD_CALL(VARIABLE("this"),"recordedValues"), "get", List.of(VARIABLE("field"))), "add", List.of(VARIABLE("newValue"))),
                RETURN(VARIABLE("newValue"))
        );

        Method method2=METHOD("newSIdentifier")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(STRING,"field")
                .PARAMETER(STRING,"counter")
               // .ANNOTATIONS(OverrideAnnotation.NAME) // not in InputOutputProcessor
                .RETURNS(STRING);
        method2.BODY(
                IF(BINARY_OP(METHOD_CALL(METHOD_CALL(VARIABLE("this"),"counterMap"), "containsKey", List.of(VARIABLE("counter") )), "==", CONSTANT(false)))
                        .THEN(
                                ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "counterInitialValue"),
                                        BINARY_OP(METHOD_CALL(VARIABLE("this"), "counterInitialValue"), "+", BINARY_OP(METHOD_CALL(VARIABLE("this"),"sign", List.of()), "*", CONSTANT(1000)))),
                                METHOD_CALL(METHOD_CALL(VARIABLE("this"),"counterMap"), "put", List.of(VARIABLE("counter"), CONSTRUCTOR_CALL(ATOMIC_INTEGER, List.of(METHOD_CALL(VARIABLE("this"), "counterInitialValue")))))
                        ),
                DEFINITION(INTEGER, VARIABLE("newValue"),
                        IFEXPRESSION(METHOD_CALL(VARIABLE("this"),"negative"),

                                METHOD_CALL(METHOD_CALL(METHOD_CALL(VARIABLE("this"),"counterMap"), "get", List.of(VARIABLE("counter"))),"getAndDecrement", List.of()),

                                METHOD_CALL(METHOD_CALL(METHOD_CALL(VARIABLE("this"),"counterMap"), "get", List.of(VARIABLE("counter"))),"getAndIncrement", List.of()))),

                IF(BINARY_OP(METHOD_CALL(METHOD_CALL(VARIABLE("this"),"recordedValues"), "containsKey",List.of(VARIABLE("field") )),"==", CONSTANT(false)))
                        .THEN(
                                METHOD_CALL(METHOD_CALL(VARIABLE("this"),"recordedValues"), "put", List.of(VARIABLE("field"), CONSTRUCTOR_CALL(LINKED_LIST_OF_INTEGER, List.of())))
                        ),


                METHOD_CALL(METHOD_CALL(METHOD_CALL(VARIABLE("this"),"recordedValues"), "get", List.of(VARIABLE("field"))), "add", List.of(VARIABLE("newValue"))),
                RETURN(METHOD_CALL(STRING,"valueOf", List.of(VARIABLE("newValue"))))
        );

        Method method3=METHOD("getCounterMap")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                        .RETURNS(MAP_STRING_ATOMIC_INTEGER)
                .BODY(RETURN(METHOD_CALL(VARIABLE("this"),"counterMap")));

        Method method4=METHOD("getRecordedValues")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(MAP_STRING_LIST_INTEGER)
                .BODY(RETURN(METHOD_CALL(VARIABLE("this"),"recordedValues")));


        Method method5=METHOD("sign")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(INTEGER)
                .BODY(RETURN(IFEXPRESSION(METHOD_CALL(VARIABLE("this"), "negative"), CONSTANT(-1), CONSTANT(1))));

        pastClass.METHOD(method1);
        pastClass.METHOD(method2);
        pastClass.METHOD(method3);
        pastClass.METHOD(method4);
        pastClass.METHOD(method5);



        String myPackage = locations.getFilePackage(configs.name, fileName);

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, "target/generated-js", stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, myPackage, "target/generated-rust/src", stackTraceElement);

        generateBeanLocalEnactor3(configs, locations, BEAN_LOCAL_ENACTOR3).save();
        generateLocalEnactor(configs, locations, LOCAL_ENACTOR).save();

        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, rustGenerator);
    }



    SpecificationFile generateBeanLocalEnactor3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName identifierRegistryClass = ClassName.get(IDENTIFIER_REGISTRY, locations.getFilePackage(configs.name, IDENTIFIER_REGISTRY));
        ClassName beanLocalEnacto2Class = ClassName.get(BEAN_LOCAL_ENACTOR2, locations.getFilePackage(configs.name, BEAN_LOCAL_ENACTOR2));


        Class pastClass = pastFactory.CLASS(BEAN_LOCAL_ENACTOR3)
                .MODIFIERS(Modifier.PUBLIC)
                .FIELDS(
                        FIELD("identifierRegistry", identifierRegistryClass).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                )
                .SUPERCLASS(beanLocalEnacto2Class)
                .ANNOTATION(NoSerialization.NAME);

        Constructor ctor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(MAP_STRING_ATOMIC_INTEGER, "counterMap")
                .PARAMETER(MAP_STRING_LIST_INTEGER, "recordedValues")
                .PARAMETER(_bool, "negative")

                .BODY(
                       // new SuperConstructorCall(List.of()),
                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "identifierRegistry"),
                                CONSTRUCTOR_CALL(identifierRegistryClass,
                                        List.of(VARIABLE("counterMap"),
                                                VARIABLE("recordedValues"),
                                                VARIABLE("negative"))))
                );

        pastClass.CONSTRUCTOR(ctor);

        Method method1 = METHOD("newIdentifier")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(STRING, "field")
                .PARAMETER(STRING, "counter")
                //.ANNOTATIONS(OverrideAnnotation.NAME)  // not in InputOutputProcessor
                .RETURNS(INTEGER)
                .BODY(
                        RETURN(METHOD_CALL(METHOD_CALL(VARIABLE("this"), "identifierRegistry"),
                                "newIdentifier",
                                List.of(VARIABLE("field"), VARIABLE("counter"))))

                );
        pastClass.METHOD(method1);


        Method method2=METHOD("newSIdentifier")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(STRING,"field")
                .PARAMETER(STRING,"counter")
                // .ANNOTATIONS(OverrideAnnotation.NAME) // not in InputOutputProcessor
                .RETURNS(STRING)
                .BODY(
                        RETURN(METHOD_CALL(METHOD_CALL(VARIABLE("this"), "identifierRegistry"),
                                "newSIdentifier",
                                List.of(VARIABLE("field"), VARIABLE("counter"))))

                );
        pastClass.METHOD(method2);

        Method method3 = METHOD("getCounterMap")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(MAP_STRING_ATOMIC_INTEGER)
                .BODY(
                        RETURN(METHOD_CALL(METHOD_CALL(VARIABLE("this"), "identifierRegistry"),
                                "getCounterMap", List.of()))
                );
        pastClass.METHOD(method3);

        Method method4 = METHOD("getRecordedValues")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(MAP_STRING_LIST_INTEGER)
                .BODY(

                        RETURN(METHOD_CALL(METHOD_CALL(VARIABLE("this"), "identifierRegistry"),
                                "getRecordedValues", List.of()))
                );
        pastClass.METHOD(method4);


        String myPackage = locations.getFilePackage(configs.name, fileName);

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, "target/generated-js", stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, myPackage, "target/generated-rust/src", stackTraceElement);

        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, rustGenerator);

    }

    /*
    class LocalEnactor extends BeanHistory {
    constructor() {
        super(new BeanLocalEnactor3(new Map(),new Map()), []);
    }

    getCounterMap() {
        return super.getDelegator().getCounterMap();
    }
    getRecordedValues() {
        return super.getDelegator().getRecordedValues();
    }
}

     */

    SpecificationFile generateLocalEnactor(TemplatesProjectConfiguration configs, Locations locations, String fileName) {

        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();


        ClassName beanHistoryClass = ClassName.get(BEAN_HISTORY, locations.getFilePackage(configs.name, BEAN_HISTORY));
        ClassName beanLocalEnactor3Class = ClassName.get(BEAN_LOCAL_ENACTOR3, locations.getFilePackage(configs.name, BEAN_LOCAL_ENACTOR3));
        ParameterizedType parametricBeanHistory=ParameterizedType.get(beanHistoryClass,beanLocalEnactor3Class);

        Class pastClass = pastFactory.CLASS(LOCAL_ENACTOR)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(parametricBeanHistory)
                .ANNOTATION(NoSerialization.NAME);

        Constructor ctor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(_bool, "negative")

                .BODY(
                        SUPER_METHOD_CALL(null,List.of(
                                CONSTRUCTOR_CALL(beanLocalEnactor3Class,
                                        List.of(CONSTRUCTOR_CALL(HASHMAP_STRING_ATOMIC_INTEGER,List.of()),
                                                CONSTRUCTOR_CALL(HASH_STRING_LIST_INTEGER,List.of()),
                                                VARIABLE("negative"))),
                                CONSTRUCTOR_CALL(LINKED_LIST_OF_OBJECTS,List.of()))
                        )
                );

        pastClass.CONSTRUCTOR(ctor);

        Method method2 = METHOD("getCounterMap")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(MAP_STRING_ATOMIC_INTEGER)
                .BODY(
                        RETURN(METHOD_CALL(SUPER_METHOD_CALL( "getDelegator", List.of()),
                                "getCounterMap", List.of()))
                );
        pastClass.METHOD(method2);

        Method method3 = METHOD("getRecordedValues")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(MAP_STRING_LIST_INTEGER)
                .BODY(

                        RETURN(METHOD_CALL(SUPER_METHOD_CALL("getDelegator", List.of()),
                                "getRecordedValues", List.of()))
                );
        pastClass.METHOD(method3);

        String myPackage = locations.getFilePackage(configs.name, fileName);

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, myPackage, "target/generated-js", stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, myPackage, "target/generated-rust/src", stackTraceElement);

        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, rustGenerator);

    }




    }