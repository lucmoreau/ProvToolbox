package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.BeanKind;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Function;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.typeT;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanGenerator {
    public static final String JAVADOC_NO_DOCUMENTATION = "xsd:string";
    public static final String PROCESSOR_PARAMETER_NAME = Constants.GENERATED_VAR_PREFIX + "processor";
    private final CompilerUtil compilerUtil = new CompilerUtil();


    public JavaFile generateBean(String templateName, String packge, TemplateBindingsSchema bindingsSchema, BeanKind beanKind, BeanDirection beanDirection, String consistOf) {

        String name = compilerUtil.beanNameClass(templateName, beanDirection);

        TypeSpec.Builder builder = compilerUtil.generateClassInit(name);

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
                builder.addJavadoc("that only contains the input of this template.");
                break;
            case OUTPUTS:
                builder.addJavadoc("that only contains the outputs of this template.");
                break;
            case COMMON:
                builder.addJavadoc("that captures all variables of this template.");
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + beanDirection);
        }


        FieldSpec.Builder b0 = FieldSpec.builder(String.class, Constants.IS_A)
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .initializer("$S",templateName);

        builder.addField(b0.build());

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();

        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (beanDirection==BeanDirection.COMMON
                    || (beanKind==BeanKind.COMPOSITE)
                    || (beanDirection==BeanDirection.OUTPUTS && descriptorUtils.isOutput(key, bindingsSchema))
                    || (beanDirection==BeanDirection.INPUTS && descriptorUtils.isInput(key, bindingsSchema))){


                FieldSpec.Builder b = FieldSpec.builder(compilerUtil.getJavaTypeForDeclaredType(theVar, key), key);
                b.addModifiers(Modifier.PUBLIC);

                Descriptor descriptor=theVar.get(key).get(0);
                Function<NameDescriptor,Void> nf=
                        (nd) -> {
                            String documentation=nd.getDocumentation()==null? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type=nd.getType()==null? JAVADOC_NO_DOCUMENTATION : nd.getType();
                            b.addJavadoc("$N: $L (expected type: $L)\n", key, documentation, type);
                            return null;
                        };
                Function<AttributeDescriptor,Void> af=
                        (nd) -> {
                            String documentation=nd.getDocumentation()==null? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type=nd.getType()==null? JAVADOC_NO_DOCUMENTATION : nd.getType();
                            b.addJavadoc("$N: $L (expected type: $L)\n", key, documentation, type);
                            return null;
                        };
                descriptorUtils.getFromDescriptor(descriptor,af,nf);

                builder.addField(b.build());

            }
        }

        if (beanKind==BeanKind.SIMPLE && beanDirection==BeanDirection.COMMON) {
            MethodSpec mbuild = generateInvokeProcessor(templateName, packge, bindingsSchema);
            builder.addMethod(mbuild);

        } else if (beanKind==BeanKind.COMPOSITE) {


            generateCompositeList(consistOf, packge, builder, beanDirection);
            generateCompositeListExtender(consistOf, packge, builder, beanDirection);


        }

        TypeSpec spec = builder.build();

        return compilerUtil.specWithComment(spec, templateName, packge, getClass().getName());

    }



    private void generateCompositeList(String templateName, String packge, TypeSpec.Builder builder, BeanDirection beanDirection) {
        ParameterizedTypeName elementList=ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(packge,compilerUtil.beanNameClass(templateName,beanDirection))   );
        FieldSpec.Builder b1 = FieldSpec.builder(elementList, ELEMENTS)
                .addModifiers(Modifier.PUBLIC);



        b1.addJavadoc("List of composed templates generated Automatically by ProvToolbox ($N.$N()) for template $N.", this.getClass().getSimpleName(), "generateCompositeList", templateName);

        builder.addField(b1.build());
    }
    private void generateCompositeListExtender(String templateName, String packge, TypeSpec.Builder builder, BeanDirection beanDirection) {
        MethodSpec.Builder mbuilder=
                MethodSpec.methodBuilder(ADD_ELEMENTS)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ClassName.get(Object.class), "o")
                        .addComment("Generated by method $N", getClass().getName() + ".generateCompositeListExtender()")
                        .addStatement("$N.add(($T)o)", ELEMENTS,  ClassName.get(packge,compilerUtil.beanNameClass(templateName,beanDirection)));


        builder.addMethod(mbuilder.build());
    }

    private TypeName processorClassType(String template, String packge) {
        return ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),typeT);
    }

    public MethodSpec generateInvokeProcessor(String template, String packge, TemplateBindingsSchema bindingsSchema) {

        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);
        if (fieldNames.contains(PROCESSOR_PARAMETER_NAME)) {
            throw new IllegalStateException("Template " + template + " contains variable " + PROCESSOR_PARAMETER_NAME + " " + fieldNames);
        }

        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PROCESSOR_PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(typeT)
                .addTypeVariable(typeT);

        builder.addParameter(processorClassType(template,packge), PROCESSOR_PARAMETER_NAME);

        builder.addStatement("return $N.$N($L)", PROCESSOR_PARAMETER_NAME, Constants.PROCESSOR_PROCESS_METHOD_NAME, String.join(", ", fieldNames));

        return builder.build();

    }


}
