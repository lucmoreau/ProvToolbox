package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.common.Constants.INPUT_OUTPUT_PROCESSOR;

public class CompilerInputOutputProcessor {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerInputOutputProcessor() {
    }


    JavaFile generateInputOutputProcessor(String package_, TemplatesCompilerConfig configs) {

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(INPUT_OUTPUT_PROCESSOR);



        for (TemplateCompilerConfig config : configs.templates) {
            final String inputsNameClass = compilerUtil.inputsNameClass(config.name);
            final String outputsNameClass = compilerUtil.outputsNameClass(config.name);

            final ClassName inputClassName = ClassName.get(package_, inputsNameClass);
            final ClassName outputClassName = ClassName.get(package_, outputsNameClass);
            MethodSpec mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(ParameterSpec.builder(inputClassName,"bean").build())
                    .returns(outputClassName)
                    .build();

            builder.addMethod(mspec);
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(package_, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateInputOutputProcessor() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }








}