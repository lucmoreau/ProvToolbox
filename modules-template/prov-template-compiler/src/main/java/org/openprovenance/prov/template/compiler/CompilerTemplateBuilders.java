package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

public class CompilerTemplateBuilders {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerTemplateBuilders() {
    }


    SpecificationFile generateTemplateBuilders(TemplatesCompilerConfig configs, Locations locations, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        if (configs.templateBuilders==null) throw new NullPointerException("templateBuilders is null");

        TypeSpec.Builder builder = compilerUtil.generateClassInit(configs.templateBuilders);

        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;

            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.config_common_package, templateNameClass);
            FieldSpec fspec = FieldSpec.builder(className, config.name + "Builder")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .initializer(configs.logger + "." + Constants.PREFIX_LOG_VAR + config.name)
                    .build();

            builder.addField(fspec);
        }

        TypeSpec theLogger = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, locations.logger_package, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, configs.logger_package);
    }


}