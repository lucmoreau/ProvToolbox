package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.DescriptorUtils;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import static org.openprovenance.prov.template.compiler.CompilerUtil.mapStringIntInt;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanLocalEnactor2 {
    static Logger logger = LogManager.getLogger(CompilerBeanLocalEnactor2.class);
    private final CompilerUtil compilerUtil;

    public CompilerBeanLocalEnactor2(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    static DescriptorUtils descriptorUtils=new DescriptorUtils();

    SpecificationFile generateBeanLocalEnactor2(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_LOCAL_ENACTOR2);
        builder.addModifiers(Modifier.ABSTRACT);


        ClassName queryInvokerClass = ClassName.get(locations.getFilePackage(configs.name, Constants.QUERY_INVOKER2), Constants.QUERY_INVOKER2);

        ClassName ioProcessorClass = ClassName.get(locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR), INPUT_OUTPUT_PROCESSOR);
        builder.addSuperinterface(ioProcessorClass);



        MethodSpec.Builder method1 = MethodSpec.methodBuilder("newIdentifier")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(String.class,"field").build())
                .addParameter(ParameterSpec.builder(String.class,"counter").build())
                .returns(Integer.class);

        MethodSpec.Builder method2 = MethodSpec.methodBuilder("newSIdentifier")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(String.class,"field").build())
                .addParameter(ParameterSpec.builder(String.class,"counter").build())
                .returns(String.class);


        builder.addMethod(method1.build());
        builder.addMethod(method2.build());






        for (TemplateCompilerConfig config : configs.templates) {
            final String outputNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputNameClass = compilerUtil.inputsNameClass(config.name);
            final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS), outputNameClass);
            final ClassName inputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS), inputNameClass);

            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(inputClassName,"bean").build())
                    .returns(outputClassName);

            compilerUtil.specWithComment(mspec);

            mspec.addStatement("$T $N=new $T()", outputClassName, OUT_BEAN, outputClassName);

            if (config instanceof SimpleTemplateCompilerConfig) {
                TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);
                final Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();



                // for all output fields, call newIdentifier method, assigning the field in the output bean
                for (String field: descriptorUtils.fieldNames(bindingsSchema)) {
                    if (descriptorUtils.isOutput(field,bindingsSchema)) {
                        Optional<String> sqlTable=descriptorUtils.getSqlTable(field,bindingsSchema);

                        Class<?> theType=compilerUtil.getJavaTypeForDeclaredType(theVar, field);
                        if (theType==String.class)
                            mspec.addStatement("$N.$N = newSIdentifier($S,$S)", OUT_BEAN, field, field, sqlTable.orElse("sql/" + field));
                        else
                            mspec.addStatement("$N.$N = newIdentifier($S,$S)", OUT_BEAN, field, field, sqlTable.orElse("sql/" + field));
                    }
                }
                mspec.addStatement("$N.$N = newIdentifier($S,$S)", OUT_BEAN, "ID", "template/"+ config.name, "template/"+config.name);


                mspec.addStatement("return $N", OUT_BEAN);

            } else {
                CompositeTemplateCompilerConfig config1 = (CompositeTemplateCompilerConfig) config;

                String extension="_1";
                logger.warn("Using fixed extension "+extension+" in CompositeTemplateCompilerConfig processing. This is a temporary measure. Template " + config1.fullyQualifiedName);

                mspec.addStatement("$T $N = new $T<>()", mapStringIntInt, MAP_VAR, HashMap.class);
                for (String field : config1.sharing) {
                    mspec.addStatement("$N.put($S, new $T<>())", MAP_VAR, field, HashMap.class);
                }

                mspec.addStatement("$N.$N.forEach(in1 -> $N.$N($N(in1,$N)))", "bean", ELEMENTS, OUT_BEAN, ADD_ELEMENTS, Constants.PROCESS_METHOD_NAME, MAP_VAR);
                mspec.addStatement("$N.$N = newIdentifier($S,$S)", OUT_BEAN, "ID", "template/" + config1.name, "template/" + config1.name);

                mspec.addStatement("return $N",     OUT_BEAN);


                String shortConsistOfName = locations.getShortNames().get(config1.consistsOf);
                final String inputNameClass2 = compilerUtil.inputsNameClass(shortConsistOfName, extension);
                final ClassName inputClassName2 = ClassName.get(locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.INPUTS), inputNameClass2);
                final String outputNameClass2 = compilerUtil.outputsNameClass(shortConsistOfName); // no extension in outputs
                final ClassName outputClassName2 = ClassName.get(locations.getBeansPackage(config1.fullyQualifiedName, BeanDirection.OUTPUTS), outputNameClass2);

                MethodSpec.Builder mspec2 = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(inputClassName2, "bean").build())
                        .addParameter(ParameterSpec.builder(mapStringIntInt, MAP_VAR).build())
                        .returns(outputClassName2);

                compilerUtil.specWithComment(mspec2);
                mspec2.addStatement("$T $N=new $T()", outputClassName2, OUT_BEAN, outputClassName2);



                TemplateBindingsSchema bindingsSchema=null;
                for (TemplateCompilerConfig config2: configs.templates) {
                    if (config2.fullyQualifiedName.equals(config1.consistsOf)) {
                        bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config2);
                    }
                }
                if (bindingsSchema==null) {
                    throw new RuntimeException("Cannot find bindings schema for "+config1.consistsOf);
                }

                final Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();

                for (String field: descriptorUtils.fieldNames(bindingsSchema)) {
                    if (descriptorUtils.isOutput(field,bindingsSchema)) {
                        Optional<String> sqlTable = descriptorUtils.getSqlTable(field, bindingsSchema);
                        if (config1.sharing.contains(field)) {
                            mspec2.beginControlFlow("if ($N.get($S)==null)", MAP_VAR, field);
                            mspec2.addStatement("$N.$N = newIdentifier($S,$S)", OUT_BEAN, field, field, sqlTable.orElse("sql/" + field));
                            mspec2.nextControlFlow("else if ($N.get($S).containsKey($N.$N))", MAP_VAR, field, BEAN_VAR, field);
                            mspec2.addStatement("$N.$N = $N.get($S).get($N.$N)", OUT_BEAN, field, MAP_VAR, field, BEAN_VAR, field);
                            mspec2.nextControlFlow("else");
                            mspec2.addStatement("$N.$N = newIdentifier($S,$S)", OUT_BEAN, field, field, sqlTable.orElse("sql/" + field));
                            mspec2.addStatement("$N.get($S).put($N.$N, $N.$N)", MAP_VAR, field, BEAN_VAR, field, OUT_BEAN, field);
                            mspec2.endControlFlow();
                        } else {
                            Class<?> theType = compilerUtil.getJavaTypeForDeclaredType(theVar, field);
                            if (theType == String.class)
                                mspec2.addStatement("$N.$N = newSIdentifier($S,$S)", OUT_BEAN, field, field, sqlTable.orElse("sql/" + field));
                            else
                                mspec2.addStatement("$N.$N = newIdentifier($S,$S)", OUT_BEAN, field, field, sqlTable.orElse("sql/" + field));
                        }
                    }
                }
                //     out.ID = newIdentifier("template/packing","template/packing");
                mspec2.addStatement("$N.$N = newIdentifier($S,$S)", OUT_BEAN, "ID", "template/" + shortConsistOfName, "template/" + shortConsistOfName);

                mspec2.addStatement("return $N", OUT_BEAN);
                builder.addMethod(mspec2.build());
            }
            builder.addMethod(mspec.build());




        }


        TypeSpec theLogger = builder.build();

        String myPackage= locations.getFilePackage(configs.name, fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+ DOT_JAVA_EXTENSION, myPackage);

    }



}




