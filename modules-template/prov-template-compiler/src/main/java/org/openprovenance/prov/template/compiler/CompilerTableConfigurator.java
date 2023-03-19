package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import java.util.function.Function;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.typeT;
import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;

public class CompilerTableConfigurator {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerTableConfigurator() {
    }

    SpecificationFile generateTableConfigurator(TemplatesCompilerConfig configs, Locations locations) {
        return generateTableConfigurator(configs,false, locations);
    }
    SpecificationFile generateCompositeTableConfigurator(TemplatesCompilerConfig configs, Locations locations) {
        return generateTableConfigurator(configs,true, locations);
    }

    SpecificationFile generateTableConfigurator(TemplatesCompilerConfig configs, boolean compositeOnly, Locations locations) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        if (configs.tableConfigurator==null) throw new NullPointerException("tableConfigurator is null");
        String tableClassName=(compositeOnly)? Constants.COMPOSITE +configs.tableConfigurator:configs.tableConfigurator;

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