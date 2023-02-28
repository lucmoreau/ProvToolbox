package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

public class CompilerBeanProcessor {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerBeanProcessor() {
    }


    SpecificationFile generateBeanProcessor(TemplatesCompilerConfig configs, Locations locations, String directory, String fileName) {
        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(configs.beanProcessor);

        for (TemplateCompilerConfig config : configs.templates) {
            //if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.config_common_package, beanNameClass);
            MethodSpec mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
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
        return new SpecificationFile(myfile, directory, fileName, configs.logger_package);
    }







  }