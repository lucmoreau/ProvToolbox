package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class CompilerBeanProcessor {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerBeanProcessor() {
    }


    JavaFile generateBeanProcessor(TemplatesCompilerConfig configs) {
        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(configs.beanProcessor);

        String packge = null;
        for (TemplateCompilerConfig config : configs.templates) {
            final String beanNameClass = compilerUtil.beanNameClass(config.name);
            packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, beanNameClass);
            MethodSpec mspec = MethodSpec.methodBuilder(ConfigProcessor.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(ParameterSpec.builder(className,"bean").build())
                    .returns(className)
                    .build();

            builder.addMethod(mspec);
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.logger_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateBeanProcessor() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }







  }