package org.openprovenance.prov.template.compiler.integration;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.tuple.Triple;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.CompilerBeanGenerator;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.BeanKind;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.Map;

import static org.openprovenance.prov.template.compiler.common.BeanDirection.INPUTS;
import static org.openprovenance.prov.template.compiler.common.BeanDirection.OUTPUTS;
import static org.openprovenance.prov.template.compiler.common.Constants.A_RECORD_INPUTS_CONVERTER;
import static org.openprovenance.prov.template.compiler.common.Constants.TO_INPUTS;


public class CompilerIntegrator {
    private final CompilerCommon compilerCommon;
    private final CompilerUtil compilerUtil;
    private final boolean debugComment = true;
    private final CompilerBeanGenerator compilerBeanGenerator;

    public CompilerIntegrator(ProvFactory pFactory, CompilerCommon compilerCommon, CompilerBeanGenerator compilerBeanGenerator) {
        this.compilerCommon = compilerCommon;
        this.compilerBeanGenerator=compilerBeanGenerator;
        this.compilerUtil = new CompilerUtil(pFactory);
    }

    public SpecificationFile generateIntegrator(TemplatesProjectConfiguration configs, Locations locations, String templateName, String templateFullyQualifiedName, String integrator_package, TemplateBindingsSchema bindingsSchema, String logger, BeanKind beanKind, String consistsOf, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(compilerUtil.integratorBuilderNameClass(templateName));


        if (beanKind==BeanKind.SIMPLE) {
            builder.addMethod(compilerCommon.generateProcessorConverter(templateName, integrator_package, bindingsSchema, OUTPUTS));
            builder.addMethod(compilerCommon.generateFactoryMethodToBeanWithArray(locations, TO_INPUTS, templateName, integrator_package, bindingsSchema, INPUTS, null, null));
            builder.addField(compilerCommon.generateField4aBeanConverter2(TO_INPUTS, templateName, integrator_package, A_RECORD_INPUTS_CONVERTER, INPUTS));


        } else {
            builder.addField(compilerCommon.generateField4aBeanConverter3(TO_INPUTS, templateName,integrator_package, A_RECORD_INPUTS_CONVERTER, INPUTS));

            Map<String, Triple<String, List<String>, TemplateBindingsSchema>> variants=compilerBeanGenerator.variantTable.get(consistsOf);
            if (variants!=null) {
                variants.keySet().forEach(variant -> {
                    Triple<String, List<String>, TemplateBindingsSchema> triple=variants.get(variant);
                    String extension=triple.getLeft();
                    TemplateBindingsSchema tbs=triple.getRight();
                    List<String> shared=triple.getMiddle();
                    builder.addMethod(compilerCommon.generateFactoryMethodToBeanWithArray(locations, TO_INPUTS+extension, consistsOf, integrator_package, tbs, INPUTS, extension, shared));

                    // we assume a single variant for now
                    builder.addMethod(compilerCommon.generateFactoryMethodToBeanWithArrayComposite(TO_INPUTS, templateName, integrator_package, bindingsSchema, locations.getFilePackage(templateName,logger), logger, INPUTS, extension, shared));

                });
            } else {
                builder.addMethod(compilerCommon.generateFactoryMethodToBeanWithArrayComposite(TO_INPUTS, templateName, integrator_package, bindingsSchema, locations.getFilePackage(templateName,logger), logger, INPUTS, null, null));
            }

        }

        builder.addMethod(compilerCommon.generateNameAccessor(templateName));
        builder.addMethod(compilerCommon.generateFullyQualifiedNameAccessor(templateFullyQualifiedName));
        builder.addMethod(compilerCommon.generateTemplateNameAccessor(templateFullyQualifiedName,locations));
        builder.addMethod(compilerCommon.generateCBindingsAccessor(templateFullyQualifiedName,locations));



        builder.addMethod(generateNewOutputConstructor(templateName, integrator_package, bindingsSchema, OUTPUTS));

        TypeSpec spec = builder.build();

        JavaFile myfile= compilerUtil.specWithComment(spec, templateName, integrator_package, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, integrator_package);

    }

    public MethodSpec generateNewOutputConstructor(String templateName, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection outputs) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("newOutput")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge, compilerUtil.outputsNameClass(templateName)));
        if (debugComment)
            builder.addComment("Generated by method $N", getClass().getName() + ".generateNewOutputConstructor()");
        //builder.addTypeVariable(typeOutput);
        builder.addStatement("return new $N()", compilerUtil.outputsNameClass(templateName));


        return builder.build();

    }


}
