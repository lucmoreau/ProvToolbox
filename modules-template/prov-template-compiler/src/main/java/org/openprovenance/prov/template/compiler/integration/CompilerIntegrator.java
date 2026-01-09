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
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.CompilerUtil.listOfArrays;
import static org.openprovenance.prov.template.compiler.CompilerUtil.typeT;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.BeanDirection.INPUTS;
import static org.openprovenance.prov.template.compiler.common.BeanDirection.OUTPUTS;
import static org.openprovenance.prov.template.compiler.common.CompilerCommon.functionListObjArrayTo;
import static org.openprovenance.prov.template.compiler.common.CompilerCommon.functionObjArrayTo;
import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;


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
        SpecificationFile specFile=new SpecificationFile(javaGenerator,pythonGenerator);

        return specFile;

    }

    public Method generateNewOutputConstructor(String templateName, String packge, TemplateBindingsSchema bindingsSchema, BeanDirection outputs) {
        org.openprovenance.prov.template.compiler.past.type.ClassName outputClassName = org.openprovenance.prov.template.compiler.past.type.ClassName.get(compilerUtil.outputsNameClass(templateName), packge);
        Method method = METHOD("newOutput")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(outputClassName)

                .COMMENT("Generated by method $N", getClass().getName() + ".generateNewOutputConstructor()")
                .BODY(RETURN(CONSTRUCTOR_CALL(outputClassName,List.of())));
        return method;

    }

        /*

    public MethodSpec generateTemplateNameAccessor_no_past(String fullyQualifiedTemplateName, Locations locations) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(GET_TEMPLATE_NAME)
                .addModifiers(Modifier.PUBLIC)
                //.addAnnotation(Override.class)
                .returns(String.class);
        compilerUtil.specWithComment(builder);
        builder.addStatement("return $S", locations.getTemplateRegistrations().get(fullyQualifiedTemplateName));
        return builder.build();
    }

     */

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

    public MethodSpec generateFactoryMethodToBeanWithArrayComposite_old_new_in_Common(String toBean, String template, String packge, TemplateBindingsSchema bindingsSchema, String loggerPackage, String logger, BeanDirection direction, String extension, List<String> sharing) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(toBean)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge,compilerUtil.beanNameClass(template,direction)));
        compilerUtil.specWithComment(builder);

        if (extension!=null) {
            builder.addComment("Refers to variant $S, sharing variables $L", extension, sharing.toString());
        }


        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        builder.addParameter(listOfArrays, "records");


        builder.addStatement("$T record=records.get(0)", Object[].class);
        ClassName className = ClassName.get(packge, compilerUtil.beanNameClass(template,direction));
        builder.addStatement("$T bean=new $T()",className,className);

        int count = 1;
        for (String key: variables) {
            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
            final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);

            if (direction==BeanDirection.COMMON || descriptorUtils.isInput(key,bindingsSchema) || (sharing!=null) && sharing.contains(key)) {
                String comment="";
                if ((sharing!=null) && sharing.contains(key)) {
                    comment="/* shared */";
                }
                if (converter == null) {
                    String statement = "bean.$N=($T) record[" + count + "] $L";
                    builder.addStatement(statement, key, declaredJavaType, comment);
                } else {
                    String statement = "bean.$N=(record[" + count + "]==null)?null:((record[" + count + "] instanceof String)?$N((String)(record[" + count + "])):($T)(record[" + count + "])) $L";
                    builder.addStatement(statement, key, converter, declaredJavaType, comment);
                }
            }
            count++;
        }

        builder.addStatement("bean.$N=new $T<>()", ELEMENTS, LinkedList.class);
        builder.beginControlFlow("for (int i=1;i<records.size(); i++) ");
        if (extension==null) {
            builder.addStatement("bean.$N($T.simpleBeanConverters.get(records.get(i)[0]).apply(records.get(i)))",
                    ADD_ELEMENTS,
                    ClassName.get(loggerPackage, logger));
        } else {
            builder.addComment("this code will only work if there is a single variant for this template");
            builder.addStatement("bean.$N(toInputs$L(records.get(i)))",
                    ADD_ELEMENTS,
                    extension);
        }
        builder.endControlFlow();



        builder.addStatement("return $N", "bean");


        MethodSpec method = builder.build();

        return method;
    }

    public FieldSpec generateField4aBeanConverter3(String toBean, String templateName, String packge, String fieldName, BeanDirection direction) {
        TypeName myType=functionListObjArrayTo(ClassName.get(packge,compilerUtil.beanNameClass(templateName, direction)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, fieldName,Modifier.FINAL, Modifier.PUBLIC);
        fbuilder.addJavadoc("Generated by method $N", getClass().getName()+".generateField4aBeanConverter3()");
        fbuilder.initializer(" ($T records) -> { return $N(records); }", listOfArrays, toBean);
        return fbuilder.build();
    }

}
