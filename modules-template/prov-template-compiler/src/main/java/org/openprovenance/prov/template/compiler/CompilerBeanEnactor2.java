package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanEnactor2 {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerBeanEnactor2() {
    }


    SpecificationFile generateBeanEnactor2(TemplatesCompilerConfig configs, Locations locations, String directory, String fileName) {

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_ENACTOR2);
        builder.addModifiers(Modifier.ABSTRACT);
        builder.addTypeVariable(typeResult);


        ClassName queryInvokerClass = ClassName.get(locations.configs_integrator_package, Constants.QUERY_INVOKER3);
        ClassName beanCompleterClass = ClassName.get(locations.configurator_package2, Constants.BEAN_COMPLETER2);

        ClassName ioProcessorClass = ClassName.get(locations.configs_integrator_package, INPUT_OUTPUT_PROCESSOR);
        ClassName inputProcessorClass = ClassName.get(locations.configs_integrator_package, INPUT_PROCESSOR);
        builder.addSuperinterface(ioProcessorClass);



        TypeSpec.Builder inface=compilerUtil.generateInterfaceInit(Constants.ENACTOR_IMPLEMENTATION);
        inface.addTypeVariable(typeResult);
        MethodSpec.Builder method1 = MethodSpec.methodBuilder("generic_enact")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeOut,"output").build())
                .addParameter(ParameterSpec.builder(typeIn,"bean").build())
                .addParameter(ParameterSpec.builder(consumerIn,"checker").build())
                .addParameter(ParameterSpec.builder(biconsumerTypeIn,"queryInvoker").build())
                .addParameter(ParameterSpec.builder(biconsumerTypeOut,"completeBean").build())
                .addTypeVariable(typeIn)
                .addTypeVariable(typeOut)
                .returns(typeOut);

        inface.addMethod(method1.build());

        MethodSpec.Builder method2 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeResult,"rs").build())
                .returns(beanCompleterClass);

        inface.addMethod(method2.build());


        builder.addType(inface.build());

        builder.addField(inputProcessorClass,"checker",Modifier.FINAL, Modifier.PRIVATE);



        // Note, this is a inner interface, and the construction of its TypeName is a bit convoluted
        final TypeName ENACTOR_IMPLEMENTATION_TYPE=ParameterizedTypeName.get(ClassName.get(configs.configurator_package+"2"+"."+ Constants.BEAN_ENACTOR2, Constants.ENACTOR_IMPLEMENTATION), typeResult);

        builder.addField(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER, Modifier.FINAL, Modifier.PRIVATE);


        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER)
                .addParameter(inputProcessorClass, "checker")
                .addStatement("this.$N = $N", Constants.REALISER, Constants.REALISER)
                .addStatement("this.$N = $N", "checker", "checker");

        builder.addMethod(cbuilder3.build());



        for (TemplateCompilerConfig config : configs.templates) {
            locations.updateWithConfig(config);

            final String outputNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputNameClass = compilerUtil.inputsNameClass(config.name);
            final ClassName outputClassName = ClassName.get(locations.config_integrator_package, outputNameClass);
            final ClassName inputClassName = ClassName.get(locations.config_integrator_package, inputNameClass);

            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(inputClassName,"bean").build())
                    .returns(outputClassName);


                mspec.addStatement("return $N.generic_enact(new $T(),bean,\n" +
                        "                b -> checker.process(b),\n" +
                        "                (sb,b) -> new $T(sb).process(b),\n" +
                        "                (rs,b) -> $N.beanCompleterFactory(rs).process(b))", Constants.REALISER, outputClassName, queryInvokerClass, Constants.REALISER);

            builder.addMethod(mspec.build());
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(locations.configurator_package2, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateBeanEnactor2() for templates config $N", getClass().getName(), configs.name)
                .build();
        return new SpecificationFile(myfile, directory, fileName, locations.configurator_package2);

    }



}