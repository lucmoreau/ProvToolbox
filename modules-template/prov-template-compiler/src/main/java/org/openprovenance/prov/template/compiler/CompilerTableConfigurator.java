package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.typeT;
import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;

public class CompilerTableConfigurator {
    private final CompilerUtil compilerUtil;

    public CompilerTableConfigurator(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    SpecificationFile generateTableConfigurator(TemplatesCompilerConfig configs, Locations locations) {
        return generateTableConfigurator(configs,false, locations);
    }
    SpecificationFile generateCompositeTableConfigurator(TemplatesCompilerConfig configs, Locations locations) {
        return generateTableConfigurator(configs,true, locations);
    }

    SpecificationFile generateTableConfigurator(TemplatesCompilerConfig configs, boolean compositeOnly, Locations locations) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        String tableClassName=(compositeOnly)? Constants.COMPOSITE_TABLE_CONFIGURATOR:Constants.TABLE_CONFIGURATOR;

        TypeSpec.Builder builder =  compilerUtil.generateInterfaceInitParameter(tableClassName, CompilerUtil.typeT);

        for (TemplateCompilerConfig config : configs.templates) {
            if (!compositeOnly || !(config instanceof SimpleTemplateCompilerConfig) ) {
                final String templateNameClass = compilerUtil.templateNameClass(config.name);
                locations.updateWithConfig(config);
                final ClassName className = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), templateNameClass);
                MethodSpec mspec = MethodSpec.methodBuilder(config.name)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(ParameterSpec.builder(className, "builder").build())
                        .returns(typeT)
                        .build();

                builder.addMethod(mspec);
            }
        }

        TypeSpec theLogger = builder.build();

        String myPackage = locations.getFilePackage(tableClassName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), tableClassName+DOT_JAVA_EXTENSION, myPackage);
    }







  }