package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.common.Constants.BEAN_PROCESSOR;
import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;

public class CompilerBeanProcessor {
    private final CompilerUtil compilerUtil;

    public CompilerBeanProcessor(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateBeanProcessor(TemplatesCompilerConfig configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(BEAN_PROCESSOR);

        for (TemplateCompilerConfig config : configs.templates) {
            //if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), beanNameClass);
            MethodSpec mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(ParameterSpec.builder(className,"bean").build())
                    .returns(className)
                    .build();

            builder.addMethod(mspec);
        }


        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }







  }