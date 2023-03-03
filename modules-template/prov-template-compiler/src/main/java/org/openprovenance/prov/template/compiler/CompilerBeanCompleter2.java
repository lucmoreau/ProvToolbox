package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerBeanCompleter2 {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerBeanCompleter2() {
    }


    SpecificationFile generateBeanCompleter2(TemplatesCompilerConfig configs, Locations locations, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_COMPLETER2);


        builder.addField(CompilerUtil.mapType,"m", Modifier.FINAL);



        MethodSpec.Builder callMe2=MethodSpec.methodBuilder("getMap")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CompilerUtil.classType, "cl")
                .addParameter(String.class, "key")
                .returns(CompilerUtil.typeT)
                .addTypeVariable(CompilerUtil.typeT)
                .addStatement("return ($T) m.get($N)", CompilerUtil.typeT, "key");
        builder.addMethod(callMe2.build());

        TypeSpec.Builder inface=compilerUtil.generateInterfaceInit(Constants.GETTER);
        inface.addMethod(MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .addParameter(CompilerUtil.classType,"cl")
                .addParameter(String.class,"col")
                .returns(CompilerUtil.typeT)
                .addTypeVariable(CompilerUtil.typeT)
                .build());
        builder.addType(inface.build());


        builder.addField(TypeVariableName.get(Constants.GETTER),"getter", Modifier.FINAL);


        MethodSpec.Builder cbuilder2= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CompilerUtil.mapType, "m")
                .addStatement("this.$N = $N", "m", "m")
                .addComment("The following code implements this assignment, in a way that jsweet can compile")
                .addComment("this.getter = this::getMap");
        cbuilder2.addStatement("this.getter = $L",
                TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(TypeVariableName.get(Constants.GETTER))
                        .addMethod(MethodSpec.methodBuilder("get")
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(CompilerUtil.classType,"cl")
                                .addParameter(String.class,"col")
                                .returns(CompilerUtil.typeT)
                                .addTypeVariable(CompilerUtil.typeT)
                                .addStatement("return getMap(cl, col)").build()).build());


        builder.addMethod(cbuilder2.build());

        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeVariableName.get(Constants.GETTER), "getter")
                .addStatement("this.$N = null", "m")
                .addStatement("this.getter = ($N) getter", Constants.GETTER);

        builder.addMethod(cbuilder3.build());

        for (TemplateCompilerConfig config : configs.templates) {
            locations.updateWithConfig(config);
            if  (config instanceof SimpleTemplateCompilerConfig) {
                TemplateBindingsSchema bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
                final String inputBeanNameClass = compilerUtil.inputsNameClass(config.name);

                final ClassName outputClassName = ClassName.get(locations.config_integrator_package, outputBeanNameClass);
                MethodSpec.Builder mspec = createProcessMethod(bindingsSchema, outputClassName, true);
                builder.addMethod(mspec.build());


                final ClassName inputClassName = ClassName.get(locations.config_integrator_package, inputBeanNameClass);
                MethodSpec.Builder mspec2 = createProcessMethod(bindingsSchema, inputClassName, false);
                builder.addMethod(mspec2.build());
            } else {
                CompositeTemplateCompilerConfig config1=(CompositeTemplateCompilerConfig)config;
                String consistOf=config1.consistsOf;
                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);

                final ClassName outputClassName = ClassName.get(locations.config_integrator_package, outputBeanNameClass);
                String composeeName=compilerUtil.outputsNameClass(consistOf);
                ClassName composeeClass=ClassName.get(locations.config_integrator_package,composeeName);

                MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(outputClassName,BEAN_VAR).build())
                        .returns(outputClassName);


                mspec.addStatement("boolean nextExists=true");
                mspec.beginControlFlow("for ($T composee: $N.$N)", composeeClass, BEAN_VAR, ELEMENTS);
                mspec.beginControlFlow("if (nextExists) " );
                mspec.addStatement("$N(composee)", PROCESS_METHOD_NAME);
                mspec.addStatement("nextExists = next()");
                mspec.nextControlFlow("else");
                mspec.addStatement("System.out.println($S)", "Not enough record in the result");
                mspec.endControlFlow();
                mspec.endControlFlow();

                mspec.addStatement("return $N", BEAN_VAR);
                builder.addMethod(mspec.build());

            }
        }

        builder.addMethod(MethodSpec.methodBuilder("next").addModifiers(Modifier.PUBLIC).returns(boolean.class).addStatement("return true").build());


        TypeSpec theLogger = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, locations.configurator_package2, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, locations.configurator_package2);

    }

    private MethodSpec.Builder createProcessMethod(TemplateBindingsSchema bindingsSchema, ClassName outputClassName, boolean isOutput) {
        MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(outputClassName,BEAN_VAR).build())
                .returns(outputClassName);

        if (isOutput) mspec.addStatement("$N.ID= getter.get(Integer.class,$S)", BEAN_VAR, "ID");

        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (isOutput && descriptorUtils.isOutput(key, bindingsSchema)) {
                Class<?> cl=compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
                mspec.addStatement("$N.$N= getter.get($N.class,$S)", BEAN_VAR, key, cl.getSimpleName(), key);
            } else {
                if (!isOutput && descriptorUtils.isInput(key, bindingsSchema)) {
                    Class<?> cl=compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
                    mspec.addStatement("$N.$N= getter.get($N.class,$S)", BEAN_VAR, key, cl.getSimpleName(), key);
                }
            }
        }

        mspec.addStatement("return bean");
        return mspec;
    }


}