package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

public class CompilerTemplateBuilders {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerTemplateBuilders() {
    }


    JavaFile generateTemplateBuilders(TemplatesCompilerConfig configs) {
        if (configs.templateBuilders==null) throw new NullPointerException("templateBuilders is null");

        TypeSpec.Builder builder = compilerUtil.generateClassInit(configs.templateBuilders);

        String packge = null;
        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, templateNameClass);
            FieldSpec fspec = FieldSpec.builder(className, config.name + "Builder")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .initializer(configs.logger + "." + ConfigProcessor.PREFIX_LOG_VAR + config.name)
                    .build();

            builder.addField(fspec);
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.logger_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox ($N.generateTemplateBuilders()) for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }







  }