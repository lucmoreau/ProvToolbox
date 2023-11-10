package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanCompleter {
    private final CompilerUtil compilerUtil;


    public CompilerBeanCompleter(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    // FIXME: J4TS does not have java.sql.ResultSet.
    // so disabling this code for now
    final boolean sqlCode=false;

    SpecificationFile generateBeanCompleter(TemplatesCompilerConfig configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");




        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_COMPLETER);

        builder.addSuperinterface(ClassName.get(locations.getFilePackage(configs.beanProcessor),configs.beanProcessor));

        builder.addField(CompilerUtil.mapType,"m", Modifier.FINAL);


        MethodSpec.Builder callMe2=MethodSpec.methodBuilder("getMap")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CompilerUtil.classType, "cl")
                .addParameter(String.class, "key")
                .returns(CompilerUtil.typeT)
                .addTypeVariable(CompilerUtil.typeT);
        compilerUtil.specWithComment(callMe2);
        callMe2.addStatement("return ($T) m.get($N)", CompilerUtil.typeT, "key");
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
                .addParameter(CompilerUtil.mapType, "m");
        compilerUtil.specWithComment(cbuilder2);

        cbuilder2
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
                .addParameter(TypeVariableName.get(Constants.GETTER), "getter");
        compilerUtil.specWithComment(cbuilder3);
        cbuilder3
                .addStatement("this.$N = null", "m")
                .addStatement("this.getter = ($N) getter", Constants.GETTER);

        builder.addMethod(cbuilder3.build());

        for (TemplateCompilerConfig config : configs.templates) {

            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), beanNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(ParameterSpec.builder(className,BEAN_VAR).build())
                    .returns(className);
            if (config instanceof SimpleTemplateCompilerConfig) {
                compilerUtil.specWithComment(mspec);

                TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);


                for (String key : descriptorUtils.fieldNames(bindingsSchema)) {
                    if (descriptorUtils.isOutput(key, bindingsSchema)) {
                        Class<?> cl = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
                        mspec.addStatement("$N.$N= getter.get($N.class,$S)", BEAN_VAR, key, cl.getSimpleName(), key);
                    }
                }

                mspec.addStatement("return $N", BEAN_VAR);

            } else {
                compilerUtil.specWithComment(mspec);

                CompositeTemplateCompilerConfig config1=(CompositeTemplateCompilerConfig)config;
                String consistOf=config1.consistsOf;
                String composeeName=compilerUtil.commonNameClass(consistOf);
                ClassName composeeClass=ClassName.get(locations.getFilePackage(BeanDirection.COMMON),composeeName);

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

            }

            builder.addMethod(mspec.build());
        }

        builder.addMethod(MethodSpec.methodBuilder("next").addModifiers(Modifier.PUBLIC).returns(boolean.class).addStatement("return true").build());


        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);
    }



}