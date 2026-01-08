package org.openprovenance.prov.template.compiler.integration;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Triple;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.CompilerBeanGenerator;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.BeanKind;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.openprovenance.prov.template.compiler.CompilerUtil.typeT;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.BeanDirection.INPUTS;
import static org.openprovenance.prov.template.compiler.common.BeanDirection.OUTPUTS;
import static org.openprovenance.prov.template.compiler.common.CompilerCommon.functionObjArrayTo;
import static org.openprovenance.prov.template.compiler.common.Constants.*;


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
            builder.addMethod(generateProcessorConverter_old_see_common_for_new(templateName, integrator_package, bindingsSchema, OUTPUTS));
            builder.addMethod(generateFactoryMethodToBeanWithArray_old_new_version_in_Common(locations, TO_INPUTS, templateName, integrator_package, bindingsSchema, INPUTS, null, null));
            builder.addField(generateField4aBeanConverter2_old_find_new_in_common(TO_INPUTS, templateName, integrator_package, A_RECORD_INPUTS_CONVERTER, INPUTS));


        } else {
            builder.addField(compilerCommon.generateField4aBeanConverter3(TO_INPUTS, templateName,integrator_package, A_RECORD_INPUTS_CONVERTER, INPUTS));

            Map<String, Triple<String, List<String>, TemplateBindingsSchema>> variants=compilerBeanGenerator.variantTable.get(consistsOf);
            if (variants!=null) {
                variants.keySet().forEach(variant -> {
                    Triple<String, List<String>, TemplateBindingsSchema> triple=variants.get(variant);
                    String extension=triple.getLeft();
                    TemplateBindingsSchema tbs=triple.getRight();
                    List<String> shared=triple.getMiddle();
                    builder.addMethod(generateFactoryMethodToBeanWithArray_old_new_version_in_Common(locations, TO_INPUTS+extension, consistsOf, integrator_package, tbs, INPUTS, extension, shared));

                    // we assume a single variant for now
                    builder.addMethod(compilerCommon.generateFactoryMethodToBeanWithArrayComposite(TO_INPUTS, templateName, integrator_package, bindingsSchema, locations.getFilePackage(templateName,logger), logger, INPUTS, extension, shared));

                });
            } else {
                builder.addMethod(compilerCommon.generateFactoryMethodToBeanWithArrayComposite(TO_INPUTS, templateName, integrator_package, bindingsSchema, locations.getFilePackage(templateName,logger), logger, INPUTS, null, null));
            }

        }

        builder.addMethod(compilerCommon.generateNameAccessor_no_past(templateName));
        builder.addMethod(compilerCommon.generateFullyQualifiedNameAccessor_no_past(templateFullyQualifiedName));
        builder.addMethod(generateTemplateNameAccessor_no_past(templateFullyQualifiedName,locations));
        builder.addMethod(generateCBindingsAccessor_no_past(templateFullyQualifiedName,locations));



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
    public MethodSpec generateTemplateNameAccessor_no_past(String fullyQualifiedTemplateName, Locations locations) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(GET_TEMPLATE_NAME)
                .addModifiers(Modifier.PUBLIC)
                //.addAnnotation(Override.class)
                .returns(String.class);
        compilerUtil.specWithComment(builder);
        builder.addStatement("return $S", locations.getTemplateRegistrations().get(fullyQualifiedTemplateName));
        return builder.build();
    }

    public MethodSpec generateCBindingsAccessor_no_past(String fullyQualifiedTemplateName, Locations locations) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(GET_CBINDINGS)
                .addModifiers(Modifier.PUBLIC)
                //.addAnnotation(Override.class)
                .returns(String.class);
        compilerUtil.specWithComment(builder);
        builder.addStatement("return $S", locations.getCbindingsRegistrations().get(fullyQualifiedTemplateName));
        return builder.build();
    }

    // LUC: new version in common
    public MethodSpec generateFactoryMethodToBeanWithArray_old_new_version_in_Common(Locations locations, String toBean, String template, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection direction, String extension, List<String> shared) {
        if (extension!=null) {
            String shortName=locations.getShortNames().get(template);
            template=shortName;
        }
        MethodSpec.Builder builder = MethodSpec.methodBuilder(toBean)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge,compilerUtil.beanNameClass(template,direction,extension)));


        compilerUtil.specWithComment(builder);


        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        builder.addParameter(Object[].class, "record");

        ClassName className = ClassName.get(packge, compilerUtil.beanNameClass(template,direction,extension));
        builder.addStatement("$T $N=$N $T()", className, BEAN_VAR, "new", className);

        builder.addJavadoc("Converter to bean of type $T for template $N.\n", className, template);
        if (shared!=null) {
            builder.addJavadoc("Variant $N of class $T to support shared variables $N\n", extension, ClassName.get(packge,compilerUtil.beanNameClass(template,direction)), shared.toString());
        }
        builder.addJavadoc("@param record an array of objects\n");
        builder.addJavadoc("@return a bean\n");

        int count = 1;
        for (String key: variables) {
            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
            final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);

            if (direction==BeanDirection.COMMON
                    || descriptorUtils.isInput(key,bindingsSchema)
                    || (shared!=null && shared.contains(key))) {
                if (converter == null) {
                    String statement = "$N.$N=($T)$N[$L]";
                    builder.addStatement(statement, BEAN_VAR, key, declaredJavaType, "record", count);
                } else {
                    String statement = "$N.$N=($N[$L]==null)?null:((record[" + count + "] instanceof String)?$N((String)(record[" + count + "])):($T)(record[" + count + "]))";
                    builder.addStatement(statement, BEAN_VAR, key, "record", count, converter, declaredJavaType);
                }
            }
            count++;
        }
        builder.addStatement("return $N", BEAN_VAR);


        MethodSpec method = builder.build();

        return method;
    }

    public MethodSpec generateProcessorConverter_old_see_common_for_new(String template, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection beanDirection) {

        final TypeName returnClassName= beanDirection==BeanDirection.COMMON ? processorClassType(template, packge, typeT) : integratorClassType(template, packge, typeT);

        final TypeName returnClassNameNotParametrised = beanDirection==BeanDirection.COMMON ? processorClassTypeNotParametrised(template, packge): integratorClassType (template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PROCESSOR_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(returnClassName);
        compilerUtil.specWithComment(builder);


        TypeName parameterType = functionObjArrayTo(TypeVariableName.get("T"));


        String processor = compilerUtil.generateNewNameForVariable("processor");
        builder.addParameter(parameterType, processor, Modifier.FINAL);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Returns a converter from Processor taking arguments to Processor taking record\n");
        jdoc.add("@param $N a transformer for this template\n", processor);
        jdoc.add("@param <T> type variable for the result of processor\n");
        jdoc.add("@return $T&lt;$T&gt;\n", returnClassNameNotParametrised, TypeVariableName.get("T"));
        builder.addJavadoc(jdoc.build());

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        StringBuilder args = new StringBuilder();
        StringBuilder args2 = new StringBuilder();

        boolean first = true;

        for (String key : fieldNames) {
            String newKey = compilerUtil.generateNewNameForVariable(key);
            boolean isOutput=descriptorUtils.isOutput(key,bindingsSchema);
            if (beanDirection==BeanDirection.COMMON || !isOutput) {
                if (first) {
                    first = false;
                } else {
                    args.append(", ");
                }
                args.append(compilerUtil.getJavaTypeForDeclaredType(theVar, key).getName()).append(" ").append(newKey);
            }
        }
        first = true;

        for (String key : fieldNames) {
            String newKey = compilerUtil.generateNewNameForVariable(key);
            boolean isOutput=descriptorUtils.isOutput(key,bindingsSchema);
            if (first) {
                first = false;
            } else {
                args2.append(", ");
            }
            if (beanDirection!=BeanDirection.COMMON && isOutput) {
                args2.append(" null");
            } else {
                args2.append(" ").append(newKey);
            }

        }

        builder.addStatement("return ($L) -> {  return $N.apply(new Object [] { getFullyQualifiedName(), $L}); }", args, processor, args2);

        return builder.build();
    }
    private TypeName processorClassTypeNotParametrised(String template, String packge) {
        return ClassName.get(packge,compilerUtil.processorNameClass(template));
    }

    public FieldSpec generateField4aBeanConverter2_old_find_new_in_common(String toBean, String templateName, String packge, String fieldName, BeanDirection direction) {
        TypeName myType=functionObjArrayTo(ClassName.get(packge,compilerUtil.beanNameClass(templateName, direction)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, fieldName,Modifier.FINAL, Modifier.PUBLIC);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateField4aBeanConverter2()");
        fbuilder.initializer(" ($T $N) -> { return $N($N); }",Object[].class,"record",toBean,"record");
        return fbuilder.build();
    }

    private TypeName processorClassType(String template, String packge, TypeVariableName t) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),t);
        return name;
    }
    private TypeName integratorClassType(String template, String packge, TypeVariableName t) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.integratorNameClass(template)),t);
        return name;
    }
    private TypeName integratorClassType(String template, String packge) {
        return ClassName.get(packge,compilerUtil.integratorNameClass(template));
    }



}
