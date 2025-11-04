package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.DescriptorUtils;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanLocalEnactor2 {
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
        ClassName inputProcessorClass = ClassName.get(locations.getBeansPackage(configs.name, BeanDirection.OUTPUTS), INPUT_PROCESSOR);
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
            final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.name, BeanDirection.OUTPUTS), outputNameClass);
            final ClassName inputClassName = ClassName.get(locations.getBeansPackage(config.name, BeanDirection.INPUTS), inputNameClass);

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
                mspec.addComment("Local enactor for complex templates not implemented yet");
                mspec.addStatement("return $N", OUT_BEAN);
            }

            builder.addMethod(mspec.build());
        }


        TypeSpec theLogger = builder.build();

        String myPackage= locations.getFilePackage(configs.name, fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+ DOT_JAVA_EXTENSION, myPackage);

    }



}