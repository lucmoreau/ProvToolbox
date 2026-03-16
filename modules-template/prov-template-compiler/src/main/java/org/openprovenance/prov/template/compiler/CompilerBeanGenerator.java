package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Triple;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.BeanKind;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;


import javax.lang.model.element.Modifier;

import java.lang.Class;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.common.BeanKind.SIMPLE;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.ArrayAccessor.ARRAY_ACCESSOR;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.PostIncrement.POST_INCREMENT;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static  org.openprovenance.prov.template.compiler.past.type.ClassName.STRING;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerBeanGenerator {
    public static final String JAVADOC_NO_DOCUMENTATION = "xsd:string";
    public static final String PROCESSOR_PARAMETER_NAME = Constants.GENERATED_VAR_PREFIX + "processor";
    private final CompilerUtil compilerUtil;


    public CompilerBeanGenerator(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public SpecificationFile generateBean(TemplatesProjectConfiguration configs, Locations locations, String templateName, String templateFullyQualifiedName, TemplateBindingsSchema bindingsSchema, BeanKind beanKind, BeanDirection beanDirection, String consistOf, List<String> sharing, String extension, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        String name = compilerUtil.beanNameClass(templateName, beanDirection);
        if (extension!=null) {
            name=name+extension;
        }

        org.openprovenance.prov.template.compiler.past.Class pastClass=compilerUtil.getPastFactory()
                .CLASS(name)
                .MODIFIERS(Modifier.PUBLIC)
                .COMMENT((beanKind==SIMPLE)?"A simple bean for $N": "A composite bean for $N", templateName)
                .COMMENT((beanDirection==BeanDirection.INPUTS)?" that only contains the input of this template."
                        :(beanDirection==BeanDirection.OUTPUTS)?" that only contains the outputs of this template."
                        :" that captures all variables of this template.");

        if (sharing!=null) {
            pastClass.COMMENT("\n This includes shared variables $N.", sharing.toString());
        }

        if (templateFullyQualifiedName==null) {
            System.out.println("$$$$ Warning: templateFullyQualifiedName not specified " + templateName);
            templateFullyQualifiedName=templateName;
        }


        pastClass.FIELDS(FIELD(Constants.IS_A, STRING)
                .COMMENT("The template name")
                .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                .INITIALIZER(new Constant(templateFullyQualifiedName)));


        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();

        if (beanDirection == BeanDirection.OUTPUTS) {

            pastClass.FIELDS(FIELD("ID", INTEGER)
                    .COMMENT("Allows for database key to be returned.")
                    .MODIFIERS(Modifier.PUBLIC));
        }



        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (beanDirection==BeanDirection.COMMON
                    || (beanKind==BeanKind.COMPOSITE)
                    || (beanDirection==BeanDirection.OUTPUTS && descriptorUtils.isOutput(key, bindingsSchema))
                    || (beanDirection==BeanDirection.INPUTS && (descriptorUtils.isInput(key, bindingsSchema) || sharing!=null && sharing.contains(key)))){

                Field field=FIELD(key, compilerUtil.getPastTypeForDeclaredType(theVar, key))
                        .MODIFIERS(Modifier.PUBLIC);

                Descriptor descriptor=theVar.get(key).get(0);
                Function<NameDescriptor,Void> nf=
                        (nd) -> {
                            String documentation=nd.getDocumentation()==null? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type=nd.getType()==null? JAVADOC_NO_DOCUMENTATION : nd.getType();
                            field.COMMENT("$N: $L (expected type: $L)\n", key, documentation, type);
                            if (sharing!=null && sharing.contains(key)) {
                                field.COMMENT("This is a shared variable in a template composition.\n");
                            }
                            return null;
                        };
                Function<AttributeDescriptor,Void> af=
                        (nd) -> {
                            String documentation=nd.getDocumentation()==null? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type=nd.getType()==null? JAVADOC_NO_DOCUMENTATION : nd.getType();
                            field.COMMENT("$N: $L (expected type: $L)\n", key, documentation, type);
                            if (sharing!=null && sharing.contains(key)) {
                                field.COMMENT("This is a shared variable in a template composition.\n ");
                            }
                            return null;
                        };
                descriptorUtils.getFromDescriptor(descriptor,af,nf);

                pastClass.FIELDS(field);

            }
        }



        String beanPackge=locations.getBeansPackage(templateFullyQualifiedName, beanDirection);
        String beanProcessorPackage=locations.getBeansPackage(templateFullyQualifiedName, BeanDirection.COMMON);

        if (beanKind== SIMPLE ) {
            Method method = generateInvokeProcessor(templateName, beanProcessorPackage, bindingsSchema, null, beanDirection);
            pastClass.METHOD(method);

        } else if (beanKind==BeanKind.COMPOSITE) {

            String variant=null;

            if (sharing!=null) {
                variant = newVariant(consistOf, sharing, configs);
            }
            if (beanDirection==BeanDirection.COMMON) {
                Method method = generateInvokeProcessor(templateName, beanProcessorPackage, bindingsSchema, ELEMENTS, beanDirection);
                pastClass.METHOD(method);
            }
            generateCompositeList(consistOf, beanPackge, locations, pastClass, beanDirection, variant, sharing);
            generateCompositeListExtender(consistOf, beanPackge, locations, pastClass, beanDirection, variant, sharing);
        }


        String directory = locations.convertToDirectory(beanPackge);
        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, beanPackge, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, beanPackge, configs, fileName, directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, beanPackge, "target/generated-js", stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, beanPackge, "target/generated-rust/src", stackTraceElement);
        return new SpecificationFile(javaGenerator,pythonGenerator,jsGenerator,rustGenerator);
    }

    /*
    static public SpecificationFile newSpecificationFiles(CompilerUtil compilerUtil, Locations locations, TypeSpec spec, String templateName, StackTraceElement stackTraceElement, JavaFile myfile, String directory, String fileName, String packge, Set<String> selectedExports) {
        return newSpecificationFiles(locations, spec, myfile, directory, fileName, packge, selectedExports, compilerUtil.pySpecWithComment(templateName, stackTraceElement));
    }


    static public SpecificationFile newSpecificationFiles(CompilerUtil compilerUtil, Locations locations, TypeSpec spec, TemplatesProjectConfiguration configs, StackTraceElement stackTraceElement, JavaFile myfile, String directory, String fileName, String packge, Set<String> selectedExports) {
        return newSpecificationFiles(locations, spec, myfile, directory, fileName, packge, selectedExports, compilerUtil.pySpecWithComment(configs, stackTraceElement));
    }

     */

    /*
    private static SpecificationFile newSpecificationFiles(Locations locations, TypeSpec spec, JavaFile myfile, String directory, String fileName, String packge, Set<String> selectedExports, String prelude) {
        final PoetParser poetParser = new PoetParser();
        poetParser.emitPrelude(prelude);
        int importPoint=poetParser.getSb().length();
        org.openprovenance.prov.template.compiler.past0.Class clazz = poetParser.parse(spec, selectedExports);
        Python emitter = new Python(poetParser.getSb(), 0);
        clazz.emit(emitter);
        // a bit of a trick: defined delayed fields outside the class, after the class definition, this allows the initialiser to refer to class methods.
        clazz.emitClassInitialiser(emitter,0);

        poetParser.getSb().insert(importPoint,"#end imports\n\n");
        for (String imprt: new HashSet<>(emitter.getImports()).stream().sorted().collect(Collectors.toList())) {
            poetParser.getSb().insert(importPoint,"\n");
            poetParser.getSb().insert(importPoint,imprt);
        }
        poetParser.getSb().insert(importPoint,"\n\n#start imports\n");



        String pyDirectory = locations.python_dir + "/" + packge.replace('.', '/') + "/";
        String pyFilename = myfile.typeSpec.name + ".py";
        return new SpecificationFile(myfile, directory, fileName, packge,
                pyDirectory, pyFilename, () -> poetParser.getSb().toString());
    }


     */
    public Map<String, Map<String, Triple<String, List<String>, TemplateBindingsSchema>>> variantTable=new HashMap<>();

    String newVariant(String templateFullyQualifiedName, List<String> sharing, TemplatesProjectConfiguration configs) {
        String shared= stringForSharedVariables(sharing);
        variantTable.putIfAbsent(templateFullyQualifiedName,new HashMap<>());
        Triple<String, List<String>, TemplateBindingsSchema> triple = variantTable.get(templateFullyQualifiedName).get(shared);
        if (triple ==null) {
            String extension = "_" + (variantTable.get(templateFullyQualifiedName).keySet().size() + 1);



            TemplateCompilerConfig config=Arrays.stream(configs.templates).filter(c -> Objects.equals(c.fullyQualifiedName, templateFullyQualifiedName)).findFirst().get();
            SimpleTemplateCompilerConfig sConfig=(SimpleTemplateCompilerConfig) config;
            SimpleTemplateCompilerConfig sConfig2=sConfig.cloneAsInstanceInComposition(sConfig.name+extension,templateFullyQualifiedName+extension, null);

            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(sConfig2);

            variantTable.get(templateFullyQualifiedName).put(shared, Triple.of(extension,sharing,bindingsSchema));
            return extension;

        } else {
            return triple.getLeft();
        }
    }

    private String stringForSharedVariables(List<String> sharing) {
        return sharing.stream().sorted().collect(Collectors.joining("_"));
    }

    /*
    static final ParameterizedTypeName classOfUnknown = ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("?"));


     */

    private void generateCompositeList(String templateName, String packge, Locations locations, org.openprovenance.prov.template.compiler.past.Class builder, BeanDirection beanDirection, String variant, List<String> sharing) {

        String shortName=locations.getShortNames().get(templateName);
        String name = compilerUtil.beanNameClass(shortName, beanDirection, variant);

        org.openprovenance.prov.template.compiler.past.type.ClassName consistsOfClass = org.openprovenance.prov.template.compiler.past.type.ClassName.get(name, packge);
        ParameterizedType elementList=ParameterizedType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.LIST, consistsOfClass);
        Field b1=FIELD(ELEMENTS, elementList)
                .MODIFIERS(Modifier.PUBLIC)
                .INITIALIZER(CONSTRUCTOR_CALL(
                        // Declaring LinkedList as parameterized type with no argument <> to be able to generate new $T<>()
                        ParameterizedType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.LINKED_LIST), new LinkedList<>()))
                .COMMENT("List of composed templates generated Automatically by ProvToolbox ($N.$N()) for template $N.",
                        this.getClass().getSimpleName(), "generateCompositeList", templateName);

        if(variant!=null) {
            b1.COMMENT("\nVariant $N for shared variables $N ($N).", variant, stringForSharedVariables(sharing), sharing.toString());
        }

        builder.FIELDS(b1);


        ParameterizedType classofUnknown1= ParameterizedType.get(CLASS, TypeVariable.get("?"));

        Field b2=new Field("consistsOf", STRING)
                .MODIFIERS(Modifier.PUBLIC)
                .COMMENT("Class of elements inside this composite")
                .INITIALIZER(CONSTANT(consistsOfClass.packge + "." + consistsOfClass.simpleName));
        builder.FIELDS(b2);

        if (variant!=null) {
            for (String shared : sharing) {
                generateMutatorForSharedVariables(templateName, packge, builder, beanDirection, variant, shared, name, consistsOfClass);
                generateMutatorForDistinctVariables(templateName, packge, builder, beanDirection, variant, shared, name, consistsOfClass);
            }
        }

    }


    private void generateMutatorForDistinctVariables(String templateName, String packge, org.openprovenance.prov.template.compiler.past.Class builder, BeanDirection beanDirection, String variant, String shared, String name, org.openprovenance.prov.template.compiler.past.type.ClassName consistsOfClass) {
        Method method = METHOD("distinct" + compilerUtil.capitalize(shared))
                .PARAMETER(INTEGER, "v")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(VOID);
        compilerUtil.debugFileLocation(method);

        String countName="__count";

        method.BODY(
                DEFINITION(
                        intArray,
                        VARIABLE(countName),
                        new org.openprovenance.prov.template.compiler.past.ArrayInitialiser(_int,List.of(VARIABLE("v"))))
                        .addModifier(Modifier.FINAL),
                METHOD_CALL(
                        VARIABLE(ELEMENTS, Variable.VariableKind.FIELD_VARIABLE),
                        "forEach",
                        LAMBDA(PARAMETER("b", consistsOfClass))
                                .BODY(ASSIGNMENT(
                                        METHOD_CALL(VARIABLE("b"), shared),
                                        POST_INCREMENT(ARRAY_ACCESSOR(VARIABLE(countName), CONSTANT(0)),-1)))));
        builder.METHOD(method);
    }


    private void generateMutatorForSharedVariables(String templateName, String packge, org.openprovenance.prov.template.compiler.past.Class builder, BeanDirection beanDirection, String variant, String shared, String name, org.openprovenance.prov.template.compiler.past.type.ClassName consistsOfClass) {

        Method method=METHOD("shareAll" + compilerUtil.capitalize(shared))
                .PARAMETER(INTEGER, "v")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(VOID);
        compilerUtil.debugFileLocation(method);

        method.BODY(
                METHOD_CALL(
                        VARIABLE(ELEMENTS, Variable.VariableKind.FIELD_VARIABLE),
                        "forEach",
                        LAMBDA(PARAMETER("b", consistsOfClass))
                                .BODY(ASSIGNMENT(
                                        METHOD_CALL(VARIABLE("b"), shared),
                                        VARIABLE("v")))));



        builder.METHOD(method);
    }




    private void generateCompositeListExtender(String templateName, String packge, Locations locations, org.openprovenance.prov.template.compiler.past.Class builder, BeanDirection beanDirection, String variant, List<String> sharing) {
        String shortName=locations.getShortNames().get(templateName);
        String name = compilerUtil.beanNameClass(shortName, beanDirection, variant);

        Method method=METHOD(ADD_ELEMENTS)
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(org.openprovenance.prov.template.compiler.past.type.ClassName.OBJECT, "o")
                .RETURNS(VOID);
        compilerUtil.debugFileLocation(method);

        method.BODY(METHOD_CALL(
                new Variable(ELEMENTS, Variable.VariableKind.FIELD_VARIABLE),
                "add", CAST(org.openprovenance.prov.template.compiler.past.type.ClassName.get(name, packge), VARIABLE("o"))));
        builder.METHOD(method);
    }






    private org.openprovenance.prov.template.compiler.past.type.TypeName processorClassType(String template, String packge) {
        return ParameterizedType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.get(compilerUtil.processorNameClass(template),packge), T());
    }
    public Method generateInvokeProcessor(String template, String processorPackage, TemplateBindingsSchema bindingsSchema, String elements, BeanDirection beanDirection) {

        List<String> fieldNames = (List<String>) descriptorUtils.fieldNames(bindingsSchema);
        if (fieldNames.contains(PROCESSOR_PARAMETER_NAME)) {
            throw new IllegalStateException("Template " + template + " contains variable " + PROCESSOR_PARAMETER_NAME + " " + fieldNames);
        }

        Method method=METHOD(PROCESSOR_PROCESS_METHOD_NAME)
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(T())
                .addTypeVariables(T())
                .PARAMETER(processorClassType(template,processorPackage), PROCESSOR_PARAMETER_NAME);

        List<String> actualFieldNames;
        if (elements!=null) {
            actualFieldNames=new LinkedList<>(fieldNames);
            actualFieldNames.add(elements);
        } else {
            actualFieldNames=fieldNames;
        }

        if (beanDirection==BeanDirection.COMMON) {
            method.addStatement(RETURN(
                    FUNCTIONAL_METHOD_CALL(
                            VARIABLE(PROCESSOR_PARAMETER_NAME),
                            PROCESSOR_PROCESS_METHOD_NAME,
                            actualFieldNames
                                    .stream()
                                    .map(field -> VARIABLE(field, Variable.VariableKind.FIELD_VARIABLE))
                                    .collect(Collectors.toList()))));
        } else if (beanDirection==BeanDirection.INPUTS) {
            method.addStatement(RETURN(
                    FUNCTIONAL_METHOD_CALL(
                            VARIABLE(PROCESSOR_PARAMETER_NAME),
                            PROCESSOR_PROCESS_METHOD_NAME,
                            actualFieldNames
                                    .stream()
                                    .map(field -> descriptorUtils.isInput(field,bindingsSchema)?VARIABLE(field, Variable.VariableKind.FIELD_VARIABLE):Constant.getNull())
                                    .collect(Collectors.toList()))));
        } else if (beanDirection==BeanDirection.OUTPUTS) {
            method.addStatement(RETURN(
                    FUNCTIONAL_METHOD_CALL(
                            VARIABLE(PROCESSOR_PARAMETER_NAME),
                            PROCESSOR_PROCESS_METHOD_NAME,
                            actualFieldNames
                                    .stream()
                                    .map(field -> descriptorUtils.isOutput(field,bindingsSchema)?VARIABLE(field, Variable.VariableKind.FIELD_VARIABLE):Constant.getNull())
                                    .collect(Collectors.toList()))));
        } else {
            throw new IllegalStateException("Unexpected value: " + beanDirection);
        }

        return method;
    }


    public void generateSimpleConfigsWithVariants(Locations locations, TemplatesProjectConfiguration configs) {
        variantTable.keySet().forEach(
                templateFullyQualifiedName -> {
                    Map<String, Triple<String, List<String>, TemplateBindingsSchema>> allVariants=variantTable.get(templateFullyQualifiedName);
                    allVariants.keySet().forEach(
                            shared -> {
                                Triple<String, List<String>, TemplateBindingsSchema> pair=allVariants.get(shared);
                                String extension=pair.getLeft();
                                List<String> sharing=pair.getMiddle();
                                TemplateBindingsSchema bindingsSchema = pair.getRight();

                                // find in configs the template configuration with qualifiedName templateFullyQualifiedName
                                SimpleTemplateCompilerConfig config=Arrays.stream(configs.templates)
                                        .filter(c -> c instanceof SimpleTemplateCompilerConfig)
                                        .map(c -> (SimpleTemplateCompilerConfig) c)
                                        .filter(c -> Objects.equals(c.fullyQualifiedName, templateFullyQualifiedName))
                                        .findFirst().get();

                                System.out.println("Generating variant bean for template " + templateFullyQualifiedName + " with extension " + extension + " for shared variables " + sharing + " with name " + config.name);
                                SpecificationFile spec=generateBean(configs, locations, config.name, templateFullyQualifiedName, bindingsSchema, SIMPLE, BeanDirection.INPUTS, null, sharing, extension, compilerUtil.beanNameClass(config.name,BeanDirection.INPUTS,extension)+DOT_JAVA_EXTENSION);
                                spec.save();

                                }
                    );
                }
        );


    }
}
