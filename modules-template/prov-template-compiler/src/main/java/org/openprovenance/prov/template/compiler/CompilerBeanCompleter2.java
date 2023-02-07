package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.Constants.BEAN_VAR;

public class CompilerBeanCompleter2 {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerBeanCompleter2() {
    }


    JavaFile generateBeanCompleter2(TemplatesCompilerConfig configs) {

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");




        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_COMPLETER2);

        //builder.addSuperinterface(ClassName.get(configs.logger_package,configs.beanProcessor));

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
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

            final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputBeanNameClass = compilerUtil.inputsNameClass(config.name);
            String packge = config.package_ + ".client.integrator";

            final ClassName outputClassName = ClassName.get(packge, outputBeanNameClass);
            MethodSpec.Builder mspec = createProcessMethod(bindingsSchema, outputClassName, true);
            builder.addMethod(mspec.build());


            final ClassName inputClassName = ClassName.get(packge, inputBeanNameClass);
            MethodSpec.Builder mspec2 = createProcessMethod(bindingsSchema, inputClassName, false);
            builder.addMethod(mspec2.build());
        }


        TypeSpec theLogger = builder.build();
        JavaFile myfile = JavaFile.builder(configs.configurator_package + "2", theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateBeanCompleter() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }

    private MethodSpec.Builder createProcessMethod(TemplateBindingsSchema bindingsSchema, ClassName cutputClassName, boolean isOutput) {
        MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(cutputClassName,BEAN_VAR).build())
                .returns(cutputClassName);

        if (isOutput)
        mspec.addStatement("$N.ID= getter.get(Integer.class,$S)", BEAN_VAR, "ID");

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