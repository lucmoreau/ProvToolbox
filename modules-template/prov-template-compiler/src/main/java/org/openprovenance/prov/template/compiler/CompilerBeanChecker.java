package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.typeT;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanChecker {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerBeanChecker() {
    }


    public JavaFile generateBeanChecker(TemplatesCompilerConfig configs) {


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_CHECKER);

        builder.addSuperinterface(ClassName.get(configs.logger_package,configs.beanProcessor));

        MethodSpec.Builder mspec0 = MethodSpec.methodBuilder(Constants.NOT_NULL_METHOD)
                .addModifiers(Modifier.PRIVATE,Modifier.FINAL, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(typeT,"object").build())
                .addParameter(ParameterSpec.builder(String.class,"field").build())
                .addTypeVariable(typeT)
                .returns(typeT)
                .beginControlFlow("if (object==null)")
                .addStatement("throw new NullPointerException(\"The object field \" + field + \" is null\")")
                .nextControlFlow("else")
                .addStatement("return object")
                .endControlFlow();

        builder.addMethod(mspec0.build());


        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

            final String beanNameClass = compilerUtil.beanNameClass(config.name);
            String packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, beanNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(ParameterSpec.builder(className,"bean").build())
                    .returns(className);



            for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
                if (descriptorUtils.isCompulsoryInput(key,bindingsSchema)) {
                    mspec.addStatement("$N(bean.$N,$S)", Constants.NOT_NULL_METHOD, key, key);

                }
            }
            mspec.addStatement("return bean");

            builder.addMethod(mspec.build());
        }

        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.configurator_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateBeanChecker() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }

}