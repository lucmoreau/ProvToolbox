package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import java.util.function.Function;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.typeT;

public class CompilerTableConfigurator {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerTableConfigurator() {
    }

    SpecificationFile generateTableConfigurator(TemplatesCompilerConfig configs, Locations locations, String directory, Function<String,String> fileName) {
        return generateTableConfigurator(configs,false, locations, directory, fileName);
    }
    SpecificationFile generateCompositeTableConfigurator(TemplatesCompilerConfig configs, Locations locations, String directory, Function<String,String> fileName) {
        return generateTableConfigurator(configs,true, locations, directory, fileName);
    }

    SpecificationFile generateTableConfigurator(TemplatesCompilerConfig configs, boolean compositeOnly, Locations locations, String directory, Function<String,String> fileName) {
        if (configs.tableConfigurator==null) throw new NullPointerException("tableConfigurator is null");
        String tableClassName=(compositeOnly)? Constants.COMPOSITE +configs.tableConfigurator:configs.tableConfigurator;

        TypeSpec.Builder builder =  compilerUtil.generateInterfaceInitParameter(tableClassName, CompilerUtil.typeT);

        for (TemplateCompilerConfig config : configs.templates) {
            if (!compositeOnly || !(config instanceof SimpleTemplateCompilerConfig) ) {
                final String templateNameClass = compilerUtil.templateNameClass(config.name);
                locations.updateWithConfig(config);
                final ClassName className = ClassName.get(locations.config_common_package, templateNameClass);
                MethodSpec mspec = MethodSpec.methodBuilder(config.name)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(ParameterSpec.builder(className, "builder").build())
                        .returns(typeT)
                        .build();

                builder.addMethod(mspec);
            }
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.logger_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox ($N.generateTableConfigurator()) for templates config $N", getClass().getName(), configs.name)
                .build();
        return new SpecificationFile(myfile, directory, fileName.apply(tableClassName), configs.logger_package);
    }







  }