package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Function;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;

public class CompilerSimpleBean {
    public static final String JAVADOC_NO_DOCUMENTATION = "xsd:string";

    private final CompilerUtil compilerUtil = new CompilerUtil();


    public JavaFile generateSimpleBean(String templateName, String packge, TemplateBindingsSchema bindingsSchema, boolean outputsOnly) {

        TypeSpec.Builder builder = compilerUtil.generateClassInit(outputsOnly? compilerUtil.outputsNameClass(templateName): compilerUtil.beanNameClass(templateName));

        if (outputsOnly) {
            builder.addJavadoc("A Bean only containing the outputs of this template.");
        } else {
            builder.addJavadoc("A Bean to capture all variables of this template.");

        }

        FieldSpec.Builder b0 = FieldSpec.builder(String.class, "isA");
        b0.addModifiers(Modifier.PUBLIC);
        b0.addModifiers(Modifier.FINAL);
        b0.initializer("$S",templateName);

        builder.addField(b0.build());

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();

        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (!outputsOnly || descriptorUtils.isOutput(key, bindingsSchema)) {


                FieldSpec.Builder b = FieldSpec.builder(compilerUtil.getJavaTypeForDeclaredType(theVar, key), key);
                b.addModifiers(Modifier.PUBLIC);

                Descriptor descriptor=theVar.get(key).get(0);
                Function<NameDescriptor,Void> nf=
                        (nd) -> {
                            String documentation=nd.getDocumentation()==null? ConfigProcessor.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type=nd.getType()==null? JAVADOC_NO_DOCUMENTATION : nd.getType();
                            b.addJavadoc("$N: $L (expected type: $L)\n", key, documentation, type);
                            return null;
                        };
                Function<AttributeDescriptor,Void> af=
                        (nd) -> {
                            String documentation=nd.getDocumentation()==null? ConfigProcessor.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type=nd.getType()==null? JAVADOC_NO_DOCUMENTATION : nd.getType();
                            b.addJavadoc("$N: $L (expected type: $L)\n", key, documentation, type);
                            return null;
                        };
                descriptorUtils.getFromDescriptor(descriptor,af,nf);

                builder.addField(b.build());

            }
        }

        if (!outputsOnly) {
            MethodSpec mbuild = generateInvokeProcessor(templateName, packge, bindingsSchema);
            builder.addMethod(mbuild);
        }

        TypeSpec bean = builder.build();

        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N", getClass().getName(), templateName)
                .build();

        return myfile;

    }

    static TypeName processorClassType(CompilerUtil compilerUtil, String template, String packge) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),TypeVariableName.get("T"));
        return name;
    }
    public static TypeName processorInterfaceType(CompilerUtil compilerUtil, String template, String packge) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.templateNameClass(template)+"Interface"),TypeVariableName.get("T"));
        return name;
    }

    public MethodSpec generateInvokeProcessor(String template, String packge, TemplateBindingsSchema bindingsSchema) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("process")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeVariableName.get("T")).addTypeVariable(TypeVariableName.get("T"));

        builder.addParameter(processorClassType(compilerUtil, template,packge), "processor");


        int count = 1;
        StringBuilder args = new StringBuilder();
        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (count > 1) args.append(", ");
            args.append(key);
            count++;
        }

        builder.addStatement("return processor.process("  + args + ")");

        return builder.build();

    }


}
