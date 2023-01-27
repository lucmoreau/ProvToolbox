package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanEnactor2 {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerBeanEnactor2() {
    }


    JavaFile generateBeanEnactor2(TemplatesCompilerConfig configs) {

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_ENACTOR2);
        builder.addModifiers(Modifier.ABSTRACT);
        builder.addTypeVariable(typeResult);


        ClassName queryInvokerClass = ClassName.get(configs.configurator_package, Constants.QUERY_INVOKER);
        ClassName beanCompleterClass = ClassName.get(configs.configurator_package+"2", Constants.BEAN_COMPLETER2);

        ClassName beanProcessorClass = ClassName.get(configs.logger_package, configs.beanProcessor);
        builder.addSuperinterface(beanProcessorClass);



        TypeSpec.Builder inface=compilerUtil.generateInterfaceInit(Constants.ENACTOR_IMPLEMENTATION);
        inface.addTypeVariable(typeResult);
        MethodSpec.Builder method1 = MethodSpec.methodBuilder("generic_enact")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeT,"bean").build())
                .addParameter(ParameterSpec.builder(consumerT,"checker").build())
                .addParameter(ParameterSpec.builder(biconsumerType,"queryInvoker").build())
                .addParameter(ParameterSpec.builder(biconsumerType2,"completeBean").build())
                .addTypeVariable(typeT)
                .returns(typeT);

        inface.addMethod(method1.build());

        MethodSpec.Builder method2 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeResult,"rs").build())
                .returns(beanCompleterClass);

        inface.addMethod(method2.build());


        builder.addType(inface.build());

        builder.addField(beanProcessorClass,"checker",Modifier.FINAL, Modifier.PRIVATE);



        // Note, this is a inner interface, and the construction of its TypeName is a bit convoluted
        final TypeName ENACTOR_IMPLEMENTATION_TYPE=ParameterizedTypeName.get(ClassName.get(configs.configurator_package+"2"+"."+ Constants.BEAN_ENACTOR2, Constants.ENACTOR_IMPLEMENTATION), typeResult);

        builder.addField(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER, Modifier.FINAL, Modifier.PRIVATE);


        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER)
                .addParameter(beanProcessorClass, "checker")
                .addStatement("this.$N = $N", Constants.REALISER, Constants.REALISER)
                .addStatement("this.$N = $N", "checker", "checker");

        builder.addMethod(cbuilder3.build());



        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            final String outputNameClass = compilerUtil.outputsNameClass(config.name);
            String packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, beanNameClass);
            final ClassName outputClassName = ClassName.get(packge+".integrator", outputNameClass);

            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(className,"bean").build())
                    .returns(className);

            mspec.addStatement("return $N.generic_enact(bean,\n" +
                    "                b -> checker.process(b),\n" +
                    "                (sb,b) -> new $T(sb).process(b),\n" +
                    "                (rs,b) -> $N.beanCompleterFactory(rs).process(new $T()))", Constants.REALISER, queryInvokerClass, Constants.REALISER, outputClassName);


            builder.addMethod(mspec.build());
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.configurator_package + "2", theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateBeanEnactor() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }



}