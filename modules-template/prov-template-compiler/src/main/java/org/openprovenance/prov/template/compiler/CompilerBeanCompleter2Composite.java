package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.CompilerUtil.listMapType;
import static org.openprovenance.prov.template.compiler.CompilerUtil.mapType;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerBeanCompleter2Composite {
    public static final String OUT_VAR = "out";
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerBeanCompleter2Composite() {
    }


    SpecificationFile generateBeanCompleter2Composite(TemplatesCompilerConfig configs, Locations locations, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");




        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_COMPLETER2_COMPOSITE);


        builder.addField(listMapType, LL_VAR, Modifier.FINAL);
        builder.addField(mapType, M_VAR, Modifier.FINAL);


        MethodSpec.Builder cbuilder2= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CompilerUtil.mapType, M_VAR)
                .addStatement("this.$N = ($T) $N.get($S)", LL_VAR, listMapType, M_VAR, ELEMENTS)
                .addStatement("this.$N = $N", M_VAR, M_VAR)
                ;


        builder.addMethod(cbuilder2.build());



        for (TemplateCompilerConfig config : configs.templates) {
            locations.updateWithConfig(config);
            if (config instanceof SimpleTemplateCompilerConfig) continue;
            CompositeTemplateCompilerConfig config1=(CompositeTemplateCompilerConfig) config;
            String consistsOf=config1.consistsOf;



            final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputBeanNameClass = compilerUtil.inputsNameClass(config.name);

            final ClassName outputClassName = ClassName.get(locations.config_integrator_package, outputBeanNameClass);
            MethodSpec.Builder mspec = createProcessMethod(consistsOf, locations.config_integrator_package, outputClassName, true);
            builder.addMethod(mspec.build());


        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, locations.configurator_package2, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, locations.configurator_package2);

    }

    private MethodSpec.Builder createProcessMethod(String consistsOf, String integrator_package, ClassName cutputClassName, boolean isOutput) {
        MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(cutputClassName, BEAN_VAR).build())
                .returns(cutputClassName);

        mspec.addStatement("$N.$N=($T)$N.get($S)", BEAN_VAR, "ID", Integer.class, M_VAR, "ID");

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