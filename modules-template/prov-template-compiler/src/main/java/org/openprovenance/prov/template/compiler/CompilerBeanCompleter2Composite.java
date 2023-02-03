package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.CompositeTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.CompilerUtil.listMapType;
import static org.openprovenance.prov.template.compiler.CompilerUtil.mapType;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerBeanCompleter2Composite {
    public static final String OUT_VAR = "out";
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerBeanCompleter2Composite() {
    }


    JavaFile generateBeanCompleter2Composite(String integrator_package, TemplatesCompilerConfig configs) {

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");




        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_COMPLETER2_COMPOSITE);


        builder.addField(listMapType, LL_VAR, Modifier.FINAL);


        MethodSpec.Builder cbuilder2= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CompilerUtil.mapType, M_VAR)
                .addStatement("this.$N = ($T) $N.get($S)", LL_VAR, listMapType, M_VAR, ELEMENTS)
                ;


        builder.addMethod(cbuilder2.build());



        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof SimpleTemplateCompilerConfig) continue;
            CompositeTemplateCompilerConfig config1=(CompositeTemplateCompilerConfig) config;
            String consistsOf=config1.consistsOf;



            final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputBeanNameClass = compilerUtil.inputsNameClass(config.name);
            String packge = config.package_ + ".client.integrator";

            final ClassName outputClassName = ClassName.get(packge, outputBeanNameClass);
            MethodSpec.Builder mspec = createProcessMethod(consistsOf, integrator_package, outputClassName, true);
            builder.addMethod(mspec.build());


        }


        TypeSpec theLogger = builder.build();
        JavaFile myfile = JavaFile.builder(configs.configurator_package + "2", theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateBeanCompleter2Composite() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }

    private MethodSpec.Builder createProcessMethod(String consistsOf, String integrator_package, ClassName cutputClassName, boolean isOutput) {
        MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(cutputClassName, BEAN_VAR).build())
                .returns(cutputClassName);

        mspec.beginControlFlow("for ($T $N: $N)", mapType, M_VAR, LL_VAR);
        ClassName composee=ClassName.get(integrator_package,compilerUtil.outputsNameClass(consistsOf));

        mspec.addStatement("$T $N=new $T()", composee, OUT_VAR, composee);

        mspec.addStatement("$N.__addElements(new $N($N).process($N))", BEAN_VAR, BEAN_COMPLETER2, M_VAR, OUT_VAR);


        mspec.endControlFlow();

        /*

        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (isOutput && descriptorUtils.isOutput(key, bindingsSchema)) {
                Class<?> cl=compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
                mspec.addStatement("bean.$N= getter.get($N.class,$S)", key, cl.getSimpleName(), key);
            } else {
                if (!isOutput && descriptorUtils.isInput(key, bindingsSchema)) {
                    Class<?> cl=compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
                    mspec.addStatement("bean.$N= getter.get($N.class,$S)", key, cl.getSimpleName(), key);
                }
            }
        }

         */

        mspec.addStatement("return $N", BEAN_VAR);
        return mspec;
    }


}