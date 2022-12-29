package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class CompilerTableConfigurator {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerTableConfigurator() {
    }


    JavaFile generateTableConfigurator(TemplatesCompilerConfig configs) {
        if (configs.tableConfigurator==null) throw new NullPointerException("tableConfigurator is null");

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInitParameter(configs.tableConfigurator, "T");

        String packge = null;
        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, templateNameClass);
            MethodSpec mspec = MethodSpec.methodBuilder(config.name)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(ParameterSpec.builder(className,"builder").build())
                    .returns(TypeVariableName.get("T"))
                    .build();

            builder.addMethod(mspec);
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.logger_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox ($N.generateTableConfigurator()) for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }







  }