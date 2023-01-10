package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
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
import static org.openprovenance.prov.template.compiler.common.Constants.INPUTS;
import static org.openprovenance.prov.template.compiler.common.Constants.OUTPUTS;

public class CompilerSimpleBean {
    public static final String JAVADOC_NO_DOCUMENTATION = "xsd:string";
    public static final String PROCESSOR_PARAMETER_NAME = Constants.GENERATED_VAR_PREFIX + "processor";
    private final CompilerUtil compilerUtil = new CompilerUtil();


    public JavaFile generateSimpleBean(String templateName, String packge, TemplateBindingsSchema bindingsSchema, BeanKind beanKind) {

        String name;
        switch (beanKind) {
            case INPUTS:
                name= compilerUtil.inputsNameClass(templateName);
                break;
            case OUTPUTS:
                name= compilerUtil.outputsNameClass(templateName);
                break;
            case COMMON:
                name= compilerUtil.beanNameClass(templateName);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + beanKind);
        }

        TypeSpec.Builder builder = compilerUtil.generateClassInit(name);

        switch (beanKind) {
            case INPUTS:
                builder.addJavadoc("A Bean only containing the input of this template.");
                break;
            case OUTPUTS:
                builder.addJavadoc("A Bean only containing the outputs of this template.");
                break;
            case COMMON:
                builder.addJavadoc("A Bean to capture all variables of this template.");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + beanKind);
        }


        FieldSpec.Builder b0 = FieldSpec.builder(String.class, Constants.IS_A)
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .initializer("$S",templateName);

        builder.addField(b0.build());

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();

        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (beanKind==BeanKind.COMMON
                    || (beanKind==BeanKind.OUTPUTS && descriptorUtils.isOutput(key, bindingsSchema))
                    || (beanKind==BeanKind.INPUTS && descriptorUtils.isInput(key, bindingsSchema))){


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

        if (beanKind==BeanKind.COMMON) {
            MethodSpec mbuild = generateInvokeProcessor(templateName, packge, bindingsSchema);
            builder.addMethod(mbuild);
        }

        TypeSpec spec = builder.build();

        return compilerUtil.specWithComment(spec, templateName, packge, getClass().getName());

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
