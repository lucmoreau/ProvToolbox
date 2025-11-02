package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Triple;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.BeanKind;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

//import org.openprovenance.prov.template.emitter.PoetParser;
import org.openprovenance.prov.template.emitter.PoetParser;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.typeT;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanGenerator {
    public static final String JAVADOC_NO_DOCUMENTATION = "xsd:string";
    public static final String PROCESSOR_PARAMETER_NAME = Constants.GENERATED_VAR_PREFIX + "processor";
    private final CompilerUtil compilerUtil;


    public CompilerBeanGenerator(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    public SpecificationFile generateBean(TemplatesProjectConfiguration configs, Locations locations, String templateName, TemplateBindingsSchema bindingsSchema, BeanKind beanKind, BeanDirection beanDirection, String consistOf, List<String> sharing, String extension, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        String name = compilerUtil.beanNameClass(templateName, beanDirection);
        if (extension!=null) {
            name=name+extension;
        }

        TypeSpec.Builder builder = compilerUtil.generateClassInit(name);

        /* does not work with transpiler
        builder.addAnnotation(AnnotationSpec.builder(JsonInclude.class)
                .addMember("value", "$T.Include.NON_NULL", JsonInclude.class)
                .build());

         */

        switch (beanKind) {
            case SIMPLE:
                builder.addJavadoc("A Simple Bean");
                break;
            case COMPOSITE:
                builder.addJavadoc("A composite Bean");
                break;
        }


        switch (beanDirection) {
            case INPUTS:
                builder.addJavadoc(" that only contains the input of this template.");
                break;
            case OUTPUTS:
                builder.addJavadoc(" that only contains the outputs of this template.");
                break;
            case COMMON:
                builder.addJavadoc(" that captures all variables of this template.");
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + beanDirection);
        }

        if (sharing!=null) {
            builder.addJavadoc("\n This includes shared variables $N.", sharing.toString());
        }


        FieldSpec.Builder b0 = FieldSpec.builder(String.class, Constants.IS_A)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc("The template name")
                .initializer("$S", templateName);

        builder.addField(b0.build());

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();

        if (beanDirection == BeanDirection.OUTPUTS) {
            FieldSpec.Builder b = FieldSpec.builder(Integer.class, "ID");
            b.addModifiers(Modifier.PUBLIC);
            b.addJavadoc("Allows for database key to be returned.");
            builder.addField(b.build());
        }



        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (beanDirection==BeanDirection.COMMON
                    || (beanKind==BeanKind.COMPOSITE)
                    || (beanDirection==BeanDirection.OUTPUTS && descriptorUtils.isOutput(key, bindingsSchema))
                    || (beanDirection==BeanDirection.INPUTS && (descriptorUtils.isInput(key, bindingsSchema) || sharing!=null && sharing.contains(key)))){


                FieldSpec.Builder b = FieldSpec.builder(compilerUtil.getJavaTypeForDeclaredType(theVar, key), key);
                b.addModifiers(Modifier.PUBLIC);

                Descriptor descriptor=theVar.get(key).get(0);
                Function<NameDescriptor,Void> nf=
                        (nd) -> {
                            String documentation=nd.getDocumentation()==null? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type=nd.getType()==null? JAVADOC_NO_DOCUMENTATION : nd.getType();
                            b.addJavadoc("$N: $L (expected type: $L)\n", key, documentation, type);
                            if (sharing!=null && sharing.contains(key))
                                b.addJavadoc("This is a shared variable in a template composition.\n");
                            return null;
                        };
                Function<AttributeDescriptor,Void> af=
                        (nd) -> {
                            String documentation=nd.getDocumentation()==null? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type=nd.getType()==null? JAVADOC_NO_DOCUMENTATION : nd.getType();
                            b.addJavadoc("$N: $L (expected type: $L)\n", key, documentation, type);
                            if (sharing!=null && sharing.contains(key))
                                b.addJavadoc("This is a shared variable in a template composition.\n ");
                            return null;
                        };
                descriptorUtils.getFromDescriptor(descriptor,af,nf);

                builder.addField(b.build());

            }
        }

        String packge=locations.getFilePackage(beanDirection);
        String processorPackage=locations.getFilePackage(BEAN_PROCESSOR);

        if (beanKind==BeanKind.SIMPLE ) {
            MethodSpec mbuild = generateInvokeProcessor(templateName, processorPackage, bindingsSchema, null, beanDirection);
            builder.addMethod(mbuild);

        } else if (beanKind==BeanKind.COMPOSITE) {

            String variant=null;

            if (sharing!=null) {
                variant = newVariant(consistOf, sharing, configs);
            }

            if (beanDirection==BeanDirection.COMMON) {
                MethodSpec mbuild = generateInvokeProcessor(templateName, processorPackage, bindingsSchema, ELEMENTS, beanDirection);
                builder.addMethod(mbuild);
            }

            generateCompositeList(consistOf, packge, builder, beanDirection, variant, sharing);
            generateCompositeListExtender(consistOf, packge, builder, beanDirection, variant, sharing);


        }


        TypeSpec spec = builder.build();


        String directory = locations.convertToDirectory(packge);

        JavaFile myfile = compilerUtil.specWithComment(spec, templateName, packge, stackTraceElement);

        if (locations.python_dir==null) {
            return new SpecificationFile(myfile, directory, fileName, packge);
        } else {
            return newSpecificationFiles(compilerUtil, locations, spec, templateName, stackTraceElement, myfile, directory, fileName, packge, null);
        }
    }

    static public SpecificationFile newSpecificationFiles(CompilerUtil compilerUtil, Locations locations, TypeSpec spec, String templateName, StackTraceElement stackTraceElement, JavaFile myfile, String directory, String fileName, String packge, Set<String> selectedExports) {
        return newSpecificationFiles(locations, spec, myfile, directory, fileName, packge, selectedExports, compilerUtil.pySpecWithComment(templateName, stackTraceElement));
    }


    static public SpecificationFile newSpecificationFiles(CompilerUtil compilerUtil, Locations locations, TypeSpec spec, TemplatesProjectConfiguration configs, StackTraceElement stackTraceElement, JavaFile myfile, String directory, String fileName, String packge, Set<String> selectedExports) {
        return newSpecificationFiles(locations, spec, myfile, directory, fileName, packge, selectedExports, compilerUtil.pySpecWithComment(configs, stackTraceElement));
    }

    private static SpecificationFile newSpecificationFiles(Locations locations, TypeSpec spec, JavaFile myfile, String directory, String fileName, String packge, Set<String> selectedExports, String prelude) {
        final PoetParser poetParser = new PoetParser();
        poetParser.emitPrelude(prelude);
        int importPoint=poetParser.getSb().length();
        org.openprovenance.prov.template.emitter.minilanguage.Class clazz = poetParser.parse(spec, selectedExports);
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

    public Map<String, Map<String, Triple<String, List<String>, TemplateBindingsSchema>>> variantTable=new HashMap<>();

    String newVariant(String templateName, List<String> sharing, TemplatesProjectConfiguration configs) {
        String shared= stringForSharedVariables(sharing);
        variantTable.putIfAbsent(templateName,new HashMap<>());
        Triple<String, List<String>, TemplateBindingsSchema> triple = variantTable.get(templateName).get(shared);
        if (triple ==null) {
            String extension = "_" + (variantTable.get(templateName).keySet().size() + 1);



            TemplateCompilerConfig config=Arrays.stream(configs.templates).filter(c -> Objects.equals(c.name, templateName)).findFirst().get();
            SimpleTemplateCompilerConfig sConfig=(SimpleTemplateCompilerConfig) config;
            SimpleTemplateCompilerConfig sConfig2=sConfig.cloneAsInstanceInComposition(templateName+extension, null);

            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(sConfig2);

            variantTable.get(templateName).put(shared, Triple.of(extension,sharing,bindingsSchema));
            return extension;

        } else {
            return triple.getLeft();
        }
    }

    private String stringForSharedVariables(List<String> sharing) {
        return sharing.stream().sorted().collect(Collectors.joining("_"));
    }

    static final ParameterizedTypeName classOfUnknown = ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("?"));

    private void generateCompositeList(String templateName, String packge, TypeSpec.Builder builder, BeanDirection beanDirection, String variant, List<String> sharing) {
        String name = compilerUtil.beanNameClass(templateName, beanDirection, variant);

        ClassName consistsOfClass = ClassName.get(packge, name);
        ParameterizedTypeName elementList=ParameterizedTypeName.get(ClassName.get(List.class), consistsOfClass);
        FieldSpec.Builder b1 = FieldSpec.builder(elementList, ELEMENTS)
                .addModifiers(Modifier.PUBLIC)
                .initializer("new $T<>()", LinkedList.class);

        b1.addJavadoc("List of composed templates generated Automatically by ProvToolbox ($N.$N()) for template $N.", this.getClass().getSimpleName(), "generateCompositeList", templateName);

        if(variant!=null) {
            b1.addJavadoc("\nVariant $N for shared variables $N ($N).", variant, stringForSharedVariables(sharing), sharing.toString());
        }

        builder.addField(b1.build());

        FieldSpec.Builder b2 = FieldSpec.builder(classOfUnknown, "consistsOf")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Class of elements inside this composite")
                .initializer("$T.class", consistsOfClass);
        builder.addField(b2.build());

        if (variant!=null) {
            for (String shared : sharing) {
                generateMutatorForSharedVariables(templateName, packge, builder, beanDirection, variant, shared, name);
                generateMutatorForDistinctVariables(templateName, packge, builder, beanDirection, variant, shared, name);
            }
        }

    }

    private void generateMutatorForDistinctVariables(String templateName, String packge, TypeSpec.Builder builder, BeanDirection beanDirection, String variant, String shared, String name) {
        MethodSpec.Builder mbuild = MethodSpec.methodBuilder("distinct" + compilerUtil.capitalize(shared))
                .addParameter(ClassName.get(Integer.class), "v")
                .addModifiers(Modifier.PUBLIC);
        compilerUtil.specWithComment(mbuild);

        String countName="__count";
        mbuild.addStatement("final int [] $N= { $N }", countName,  "v");
        mbuild.addStatement("$N.forEach(b -> { b.$N=$N[0]--; })", ELEMENTS, shared, countName);
        builder.addMethod(mbuild.build());
    }

    private void generateMutatorForSharedVariables(String templateName, String packge, TypeSpec.Builder builder, BeanDirection beanDirection, String variant, String shared, String name) {
        MethodSpec.Builder mbuild = MethodSpec.methodBuilder("shareAll" + compilerUtil.capitalize(shared))
                .addParameter(ClassName.get(Integer.class), "v")
                .addModifiers(Modifier.PUBLIC);
        compilerUtil.specWithComment(mbuild);

        mbuild.addStatement("$N.forEach(b -> { b.$N=$N; })", ELEMENTS, shared, "v");
        builder.addMethod(mbuild.build());
    }





    private void generateCompositeListExtender(String templateName, String packge, TypeSpec.Builder builder, BeanDirection beanDirection, String variant, List<String> sharing) {
        String name = compilerUtil.beanNameClass(templateName, beanDirection,variant);
        MethodSpec.Builder mbuilder=
                MethodSpec.methodBuilder(ADD_ELEMENTS)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ClassName.get(Object.class), "o");
        compilerUtil.specWithComment(mbuilder);
        mbuilder.addStatement("$N.add(($T)o)", ELEMENTS,  ClassName.get(packge, name));
        builder.addMethod(mbuilder.build());
    }

    private TypeName processorClassType(String template, String packge) {
        return ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),typeT);
    }

    public MethodSpec generateInvokeProcessor(String template, String processorPackage, TemplateBindingsSchema bindingsSchema, String elements, BeanDirection beanDirection) {

        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);
        if (fieldNames.contains(PROCESSOR_PARAMETER_NAME)) {
            throw new IllegalStateException("Template " + template + " contains variable " + PROCESSOR_PARAMETER_NAME + " " + fieldNames);
        }

        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PROCESSOR_PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(typeT)
                .addTypeVariable(typeT);

        builder.addParameter(processorClassType(template,processorPackage), PROCESSOR_PARAMETER_NAME);

        Collection<String> actualFieldNames;
        if (elements!=null) {
            actualFieldNames=new LinkedList<>(fieldNames);
            actualFieldNames.add(elements);
        } else {
            actualFieldNames=fieldNames;
        }

        if (beanDirection==BeanDirection.COMMON) {
            builder.addStatement("return $N.$N($L)", PROCESSOR_PARAMETER_NAME, Constants.PROCESSOR_PROCESS_METHOD_NAME,
                    CodeBlock.join(actualFieldNames.stream().map(field ->
                            CodeBlock.of("$N", field)).collect(Collectors.toList()), ","));
        } else if (beanDirection==BeanDirection.INPUTS) {
            builder.addStatement("return $N.$N($L)", PROCESSOR_PARAMETER_NAME, Constants.PROCESSOR_PROCESS_METHOD_NAME,
                    CodeBlock.join(actualFieldNames.stream().map(field ->
                            descriptorUtils.isInput(field,bindingsSchema)?CodeBlock.of("$N", field):CodeBlock.of("null")).collect(Collectors.toList()), ","));
        } else if (beanDirection==BeanDirection.OUTPUTS) {
            builder.addStatement("return $N.$N($L)", PROCESSOR_PARAMETER_NAME, Constants.PROCESSOR_PROCESS_METHOD_NAME,
                    CodeBlock.join(actualFieldNames.stream().map(field ->
                            descriptorUtils.isOutput(field,bindingsSchema)?CodeBlock.of("$N", field):CodeBlock.of("null")).collect(Collectors.toList()), ","));
        } else {
            throw new IllegalStateException("Unexpected value: " + beanDirection);
        }

        return builder.build();

    }

    public MethodSpec generateInputInvokeProcessor(String template, String packge, TemplateBindingsSchema bindingsSchema, String elements) {

        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);
        if (fieldNames.contains(PROCESSOR_PARAMETER_NAME)) {
            throw new IllegalStateException("Template " + template + " contains variable " + PROCESSOR_PARAMETER_NAME + " " + fieldNames);
        }

        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PROCESSOR_PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(typeT)
                .addTypeVariable(typeT);

        builder.addParameter(processorClassType(template,packge), PROCESSOR_PARAMETER_NAME);

        Collection<String> actualFieldNames;
        if (elements!=null) {
            actualFieldNames=new LinkedList<>(fieldNames);
            actualFieldNames.add(elements);
        } else {
            actualFieldNames=fieldNames;
        }

        builder.addStatement("return $N.$N($L)", PROCESSOR_PARAMETER_NAME, Constants.PROCESSOR_PROCESS_METHOD_NAME,
                CodeBlock.join(actualFieldNames.stream().map(field ->
                        descriptorUtils.isInput(field,bindingsSchema)?CodeBlock.of("$N", field):CodeBlock.of("null")).collect(Collectors.toList()), ","));

        return builder.build();

    }




    public void generateSimpleConfigsWithVariants(Locations locations, TemplatesProjectConfiguration configs) {
        variantTable.keySet().forEach(
                templateName -> {
                    Map<String, Triple<String, List<String>, TemplateBindingsSchema>> allVariants=variantTable.get(templateName);
                    allVariants.keySet().forEach(
                            shared -> {
                                Triple<String, List<String>, TemplateBindingsSchema> pair=allVariants.get(shared);
                                String extension=pair.getLeft();
                                List<String> sharing=pair.getMiddle();
                                TemplateBindingsSchema bindingsSchema = pair.getRight();


                                /*
                                TemplateCompilerConfig config=Arrays.stream(configs.templates).filter(c -> Objects.equals(c.name, templateName)).findFirst().get();
                                SimpleTemplateCompilerConfig sConfig=(SimpleTemplateCompilerConfig) config;
                                SimpleTemplateCompilerConfig sConfig2=sConfig.cloneAsInstanceInComposition(templateName+extension);

                                TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(sConfig2);

                                allVariantsUpdates.putIfAbsent(templateName,new HashMap<>());
                                allVariantsUpdates.get(templateName).put(shared,Triple.of(extension, sharing,bindingsSchema));


                                 */

                                SpecificationFile spec=generateBean(configs, locations, templateName, bindingsSchema, BeanKind.SIMPLE, BeanDirection.INPUTS, null, sharing, extension, compilerUtil.beanNameClass(templateName,BeanDirection.INPUTS,extension)+DOT_JAVA_EXTENSION);
                                spec.save();

                                }
                    );
                }
        );

        /*
        System.out.println("variants Updating with bindings schema ");

        allVariantsUpdates.keySet().forEach(
                templateName -> allVariantsUpdates.get(templateName).keySet().forEach(
                        shared -> variantTable.get(templateName).put(shared,allVariantsUpdates.get(templateName).get(shared))
                )
        );

         */


    }
}
