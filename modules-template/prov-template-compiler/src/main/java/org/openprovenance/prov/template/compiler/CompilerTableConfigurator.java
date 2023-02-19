package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.typeT;

public class CompilerTableConfigurator {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerTableConfigurator() {
    }

    Pair<String, JavaFile> generateTableConfigurator(TemplatesCompilerConfig configs, Locations locations) {
        return generateTableConfigurator(configs,false, locations);
    }
    Pair<String, JavaFile> generateCompositeTableConfigurator(TemplatesCompilerConfig configs, Locations locations) {
        return generateTableConfigurator(configs,true, locations);
    }

    Pair<String, JavaFile> generateTableConfigurator(TemplatesCompilerConfig configs, boolean compositeOnly, Locations locations) {
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
        return Pair.of(tableClassName,myfile);
    }







  }