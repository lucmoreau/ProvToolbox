package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.common.Constants.BEAN_VAR;
import static org.openprovenance.prov.template.compiler.common.Constants.DELEGATOR_VAR;

public class CompilerDelegator {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerDelegator() {
    }


    public JavaFile generateDelegator(TemplatesCompilerConfig configs) {


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.DELEGATOR);

        ClassName beanProcessorClass = ClassName.get(configs.logger_package, configs.beanProcessor);
        builder.addSuperinterface(beanProcessorClass);

        builder.addField(beanProcessorClass, DELEGATOR_VAR, Modifier.FINAL, Modifier.PRIVATE);

        builder.addMethod(MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(beanProcessorClass, DELEGATOR_VAR)
                .addStatement("this.$N=$N", DELEGATOR_VAR, DELEGATOR_VAR)
                .build());


        for (TemplateCompilerConfig config : configs.templates) {

            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            String packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, beanNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)  // this one is not final!
                    .addParameter(ParameterSpec.builder(className,BEAN_VAR).build())
                    .returns(className);

            mspec.addStatement("return $N.process($N)", DELEGATOR_VAR, BEAN_VAR);
            builder.addMethod(mspec.build());
        }

        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.configurator_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateDelegator() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }

}