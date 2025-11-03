package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanEnactor2 {
    private final CompilerUtil compilerUtil;

    public CompilerBeanEnactor2(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateBeanEnactor2(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_ENACTOR2);
        builder.addModifiers(Modifier.ABSTRACT);
        builder.addTypeVariable(typeResult);


        ClassName queryInvokerClass = ClassName.get(locations.getFilePackage(configs.name, Constants.QUERY_INVOKER2), Constants.QUERY_INVOKER2);
        ClassName beanCompleterClass = ClassName.get(locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2);

        ClassName ioProcessorClass = ClassName.get(locations.getBeansPackage(configs.name, BeanDirection.OUTPUTS), INPUT_OUTPUT_PROCESSOR);
        ClassName inputProcessorClass = ClassName.get(locations.getBeansPackage(configs.name, BeanDirection.OUTPUTS), INPUT_PROCESSOR);
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

        MethodSpec.Builder method3 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeResult,"rs").build())
                .addParameter(ParameterSpec.builder(Object[].class,"extra").build())
                .returns(beanCompleterClass);
        inface.addMethod(method3.build());

        /*
        MethodSpec.Builder method4 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeResult,"rs").build())
                .addParameter(ParameterSpec.builder(Object[].class,"extra").build())
                .addParameter(ParameterSpec.builder(BIFUN,"postProcessing").build())
                .returns(beanCompleterClass);
        inface.addMethod(method4.build());

        MethodSpec.Builder method5 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeResult,"rs").build())
                .addParameter(ParameterSpec.builder(BIFUN,"postProcessing").build())
                .returns(beanCompleterClass);
        inface.addMethod(method5.build());

         */


        builder.addType(inface.build());

        builder.addField(inputProcessorClass,"checker",Modifier.FINAL, Modifier.PRIVATE);



        // Note, this is a inner interface, and the construction of its TypeName is a bit convoluted
        final TypeName ENACTOR_IMPLEMENTATION_TYPE=ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.name, Constants.BEAN_ENACTOR2)+"."+ Constants.BEAN_ENACTOR2, Constants.ENACTOR_IMPLEMENTATION), typeResult);

        builder.addField(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER, Modifier.FINAL, Modifier.PRIVATE);


        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER)
                .addParameter(inputProcessorClass, "checker");
        compilerUtil.specWithComment(cbuilder3);

        cbuilder3
                .addStatement("this.$N = $N", Constants.REALISER, Constants.REALISER)
                .addStatement("this.$N = $N", "checker", "checker");

        builder.addMethod(cbuilder3.build());



        for (TemplateCompilerConfig config : configs.templates) {

            final String outputNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputNameClass = compilerUtil.inputsNameClass(config.name);
            final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.name, BeanDirection.OUTPUTS), outputNameClass);
            final ClassName inputClassName = ClassName.get(locations.getBeansPackage(config.name, BeanDirection.INPUTS), inputNameClass);

            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(inputClassName,"bean").build())
                    .returns(outputClassName);

            compilerUtil.specWithComment(mspec);

                mspec.addStatement("return $N.generic_enact(new $T(),bean,\n" +
                        "                b -> checker.process(b),\n" +
                        "                (sb,b) -> new $T(sb).process(b),\n" +
                        "                (rs,b) -> $N.beanCompleterFactory(rs).process(b))", Constants.REALISER, outputClassName, queryInvokerClass, Constants.REALISER);

            builder.addMethod(mspec.build());
        }


        TypeSpec theLogger = builder.build();

        String myPackage= locations.getFilePackage(configs.name, fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+ DOT_JAVA_EXTENSION, myPackage);

    }



}