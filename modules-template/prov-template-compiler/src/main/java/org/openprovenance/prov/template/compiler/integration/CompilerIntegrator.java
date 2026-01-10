package org.openprovenance.prov.template.compiler.integration;

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
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.common.BeanDirection.INPUTS;
import static org.openprovenance.prov.template.compiler.common.BeanDirection.OUTPUTS;
import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;


public class CompilerIntegrator {
    private final CompilerCommon compilerCommon;
    private final CompilerUtil compilerUtil;
    private final CompilerBeanGenerator compilerBeanGenerator;

    public CompilerIntegrator(ProvFactory pFactory, CompilerCommon compilerCommon, CompilerBeanGenerator compilerBeanGenerator) {
        this.compilerCommon = compilerCommon;
        this.compilerBeanGenerator=compilerBeanGenerator;
        this.compilerUtil = new CompilerUtil(pFactory);
    }

    PastFactory pastFactory=new PastFactory();

    public SpecificationFile generateIntegrator(TemplatesProjectConfiguration configs, Locations locations, String templateName, String templateFullyQualifiedName, String integrator_package, TemplateBindingsSchema bindingsSchema, String logger, BeanKind beanKind, String consistsOf, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        org.openprovenance.prov.template.compiler.past.Class pastClass=pastFactory
                .CLASS(compilerUtil.integratorBuilderNameClass(templateName))
                .MODIFIERS(Modifier.PUBLIC)
                .COMMENT("Integrator class for $N", templateName);


        if (beanKind==BeanKind.SIMPLE) {
            pastClass.METHOD(compilerCommon.generateProcessorConverter(templateName, integrator_package, bindingsSchema, OUTPUTS));
            pastClass.METHOD(compilerCommon.generateFactoryMethodToBeanWithArray(locations, TO_INPUTS, templateName, integrator_package, bindingsSchema, INPUTS, null, null));
            pastClass.FIELDS(compilerCommon.generateField4aBeanConverter2(TO_INPUTS, templateName, integrator_package, A_RECORD_INPUTS_CONVERTER, INPUTS));


        } else {
            pastClass.FIELDS(compilerCommon.generateField4aBeanConverter3(TO_INPUTS, templateName,integrator_package, A_RECORD_INPUTS_CONVERTER, INPUTS));

            Map<String, Triple<String, List<String>, TemplateBindingsSchema>> variants=compilerBeanGenerator.variantTable.get(consistsOf);
            if (variants!=null) {
                variants.keySet().forEach(variant -> {
                    Triple<String, List<String>, TemplateBindingsSchema> triple=variants.get(variant);
                    String extension=triple.getLeft();
                    TemplateBindingsSchema tbs=triple.getRight();
                    List<String> shared=triple.getMiddle();
                    pastClass.METHOD(compilerCommon.generateFactoryMethodToBeanWithArray(locations, TO_INPUTS+extension, consistsOf, integrator_package, tbs, INPUTS, extension, shared));

                    // we assume a single variant for now
                    pastClass.METHOD(compilerCommon.generateFactoryMethodToBeanWithArrayComposite(TO_INPUTS, templateName, integrator_package, bindingsSchema, locations.getFilePackage(templateName,logger), logger, INPUTS, extension, shared));

                });
            } else {
                pastClass.METHOD(compilerCommon.generateFactoryMethodToBeanWithArrayComposite(TO_INPUTS, templateName, integrator_package, bindingsSchema, locations.getFilePackage(templateName,logger), logger, INPUTS, null, null));
            }

        }

        pastClass.METHOD(compilerCommon.generateNameAccessor(templateName));
        pastClass.METHOD(compilerCommon.generateFullyQualifiedNameAccessor(templateFullyQualifiedName));
        pastClass.METHOD(compilerCommon.generateTemplateNameAccessor(templateFullyQualifiedName,locations));
        pastClass.METHOD(compilerCommon.generateCBindingsAccessor(templateFullyQualifiedName,locations));
        pastClass.METHOD(generateNewOutputConstructor(templateName, integrator_package, bindingsSchema, OUTPUTS));


        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, templateName, integrator_package, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, templateName, integrator_package, configs, fileName, directory, stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator,pythonGenerator);

    }

    public Method generateNewOutputConstructor(String templateName, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection outputs) {
        ClassName outputClassName = ClassName.get(compilerUtil.outputsNameClass(templateName), packge);
        return METHOD("newOutput")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(outputClassName)
                .COMMENT("Generated by method $N", getClass().getName() + ".generateNewOutputConstructor()")
                .BODY(RETURN(CONSTRUCTOR_CALL(outputClassName,List.of())));
    }

}
