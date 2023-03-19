package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanEnactor {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerBeanEnactor() {
    }


    SpecificationFile generateBeanEnactor(TemplatesCompilerConfig configs, Locations locations, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");




        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_ENACTOR);
        builder.addModifiers(Modifier.ABSTRACT);
        builder.addTypeVariable(typeResult);


        ClassName queryInvokerClass = ClassName.get(locations.getFilePackage(Constants.QUERY_INVOKER), Constants.QUERY_INVOKER);
        ClassName beanCompleterClass = ClassName.get(locations.getFilePackage(BEAN_COMPLETER), BEAN_COMPLETER);
        ClassName beanProcessorClass = ClassName.get(locations.getFilePackage(configs.beanProcessor), configs.beanProcessor);
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

        builder.addField(beanProcessorClass,"checker",Modifier.FINAL, Modifier.PROTECTED);



        // Note, this is a inner interface, and the construction of its TypeName is a bit convoluted
        final TypeName ENACTOR_IMPLEMENTATION_TYPE=ParameterizedTypeName.get(ClassName.get(configs.configurator_package+"."+ Constants.BEAN_ENACTOR, Constants.ENACTOR_IMPLEMENTATION), typeResult);

        builder.addField(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER, Modifier.FINAL, Modifier.PROTECTED);


        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER)
                .addParameter(beanProcessorClass, "checker")
                .addStatement("this.$N = $N", Constants.REALISER, Constants.REALISER)
                .addStatement("this.$N = $N", "checker", "checker");

        builder.addMethod(cbuilder3.build());



        for (TemplateCompilerConfig config : configs.templates) {

            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.config_common_package, beanNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(ParameterSpec.builder(className,"bean").build())
                    .returns(className);

            mspec.addStatement("return $N.generic_enact(bean,\n" +
                    "                b -> checker.process(b),\n" +
                    "                (sb,b) -> new $T(sb).process(b),\n" +
                    "                (rs,b) -> $N.beanCompleterFactory(rs).process(b))", Constants.REALISER, queryInvokerClass, Constants.REALISER);


            builder.addMethod(mspec.build());
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, locations.configurator_package, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, locations.configurator_package);

    }



}