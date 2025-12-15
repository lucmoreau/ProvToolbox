package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerBeanCompleter2 {
    private final CompilerUtil compilerUtil;
    private final boolean debugComment=true;


    public CompilerBeanCompleter2(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateBeanCompleter2(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_COMPLETER2);


        builder.addField(CompilerUtil.mapType,"m", Modifier.FINAL);



        MethodSpec.Builder callMe2=MethodSpec.methodBuilder("getMap");
        compilerUtil.specWithComment(callMe2);
        callMe2 .addModifiers(Modifier.PUBLIC)
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


        builder.addField(TypeVariableName.get(Constants.GETTER),"getter", Modifier.FINAL, Modifier.PROTECTED);


        MethodSpec.Builder cbuilder2= MethodSpec.constructorBuilder();
        compilerUtil.specWithComment(cbuilder2);
        cbuilder2
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

        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder();
        compilerUtil.specWithComment(cbuilder3);
        cbuilder3
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeVariableName.get(Constants.GETTER), "getter")
                .addStatement("this.$N = null", "m")
                .addStatement("this.getter = ($N) getter", Constants.GETTER);

        builder.addMethod(cbuilder3.build());

        for (TemplateCompilerConfig config : configs.templates) {
            if  (config instanceof SimpleTemplateCompilerConfig) {
                TemplateBindingsSchema bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
                final String inputBeanNameClass = compilerUtil.inputsNameClass(config.name);

                final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS), outputBeanNameClass);
                MethodSpec.Builder mspec = createProcessMethod(config.name, config.fullyQualifiedName, bindingsSchema, outputClassName, true);

                builder.addMethod(mspec.build());


                final ClassName inputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS), inputBeanNameClass);
                MethodSpec.Builder mspec2 = createProcessMethod(config.name, config.fullyQualifiedName, bindingsSchema, inputClassName, false);
                builder.addMethod(mspec2.build());
            } else {
                CompositeTemplateCompilerConfig config1=(CompositeTemplateCompilerConfig)config;

                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);
                final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS), outputBeanNameClass);

                String shortConsistsOf=locations.getShortNames().get(config1.consistsOf);
                String composeeName=compilerUtil.outputsNameClass(shortConsistsOf);
                ClassName composeeClass=ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS),composeeName);

                MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(outputClassName,BEAN_VAR).build())
                        .returns(outputClassName);

                compilerUtil.specWithComment(mspec);

                mspec.beginControlFlow("do ");
                mspec.addStatement("$T composee=new $T()", composeeClass,composeeClass);
                mspec.addStatement("$N.$N(composee)", BEAN_VAR,ADD_ELEMENTS);
                mspec.addStatement("$N(composee)", PROCESS_METHOD_NAME);
                mspec.endControlFlow("while (next()) ");


                mspec.addStatement("return $N", BEAN_VAR);
                builder.addMethod(mspec.build());

            }
        }

        builder.addMethod(MethodSpec.methodBuilder(POST_PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Integer.class, "id")
                .addParameter(String.class, "template")
                .returns(void.class).build());

        builder.addMethod(MethodSpec.methodBuilder("next").addModifiers(Modifier.PUBLIC).returns(boolean.class).addStatement("return true").build());


        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(configs.name, fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);

    }


    private MethodSpec.Builder createProcessMethod(String template, String fullyQualifiedName, TemplateBindingsSchema bindingsSchema, ClassName outputClassName, boolean isOutput) {
        MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(outputClassName,BEAN_VAR).build())
                .returns(outputClassName);
        compilerUtil.specWithComment(mspec);


        if (isOutput) {
            mspec.addStatement("$N.ID= getter.get(Integer.class,$S)", BEAN_VAR, "ID");
            //mspec.addStatement("$N($N.ID,$S)", POST_PROCESS_METHOD_NAME, BEAN_VAR, template);
            mspec.addStatement("$N($N.ID,$S)", POST_PROCESS_METHOD_NAME, BEAN_VAR, fullyQualifiedName);
        }

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