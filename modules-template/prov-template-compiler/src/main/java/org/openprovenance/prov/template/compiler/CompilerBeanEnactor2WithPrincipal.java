package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.sql.CompilerSqlIntegration.BIFUN;

public class CompilerBeanEnactor2WithPrincipal {
    private final CompilerUtil compilerUtil;

    public CompilerBeanEnactor2WithPrincipal(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateBeanEnactor2WithPrincipal(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_ENACTOR2_WP);
        builder.addModifiers(Modifier.ABSTRACT);
        builder.addTypeVariable(typeResult);


        ClassName queryInvokerClass = ClassName.get(locations.getFilePackage(Constants.QUERY_INVOKER2WP), Constants.QUERY_INVOKER2WP);
        ClassName beanCompleterClass = ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2);

        ClassName ioProcessorClass = ClassName.get(locations.getFilePackage(BeanDirection.OUTPUTS), INPUT_OUTPUT_PROCESSOR);
        ClassName inputProcessorClass = ClassName.get(locations.getFilePackage(BeanDirection.OUTPUTS), INPUT_PROCESSOR);
        builder.addSuperinterface(ioProcessorClass);



        /*
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


        builder.addType(inface.build());

         */

        builder.addField(inputProcessorClass,"checker",Modifier.FINAL, Modifier.PRIVATE);
        builder.addField(BIFUN,"postProcessing",Modifier.FINAL, Modifier.PRIVATE);

        // add Field   static private final ThreadLocal<String> principal= ThreadLocal.withInitial(() -> "unknown");
        final TypeName THREAD_LOCAL_STRING=ParameterizedTypeName.get(ClassName.get(ThreadLocal.class), ClassName.get(String.class));
        FieldSpec.Builder principalField=FieldSpec.builder(THREAD_LOCAL_STRING, principalVar, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).initializer("$T.withInitial(() ->  $S)", ThreadLocal.class, CompilerCommon.UNKNOWN);
        builder.addField(principalField.build());

        // add static method setPrincipal taking a principal and setting it in the ThreadLocal
        MethodSpec.Builder setPrincipal= MethodSpec.methodBuilder("setPrincipal")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String.class, "p")
                .addStatement("$N.set(p)", principalVar);
        builder.addMethod(setPrincipal.build());

        // add static method getPrincipal returning a  ThreadLocal
        MethodSpec.Builder getPrincipal= MethodSpec.methodBuilder("getPrincipal")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(String.class)
                .addStatement("return $N.get()", principalVar);
        builder.addMethod(getPrincipal.build());


        // Note, this is a inner interface, and the construction of its TypeName is a bit convoluted
        final TypeName ENACTOR_IMPLEMENTATION_TYPE=ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR2)+"."+ Constants.BEAN_ENACTOR2, Constants.ENACTOR_IMPLEMENTATION), typeResult);

        builder.addField(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER, Modifier.FINAL, Modifier.PRIVATE);


        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER)
                .addParameter(inputProcessorClass, "checker")
                .addParameter(BIFUN, "postProcessing");
        compilerUtil.specWithComment(cbuilder3);

        cbuilder3
                .addStatement("this.$N = $N", Constants.REALISER, Constants.REALISER)
                .addStatement("this.$N = $N", "checker", "checker")
                .addStatement("this.$N = $N", "postProcessing", "postProcessing");

        builder.addMethod(cbuilder3.build());



        for (TemplateCompilerConfig config : configs.templates) {
            locations.updateWithConfig(config);

            final String outputNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputNameClass = compilerUtil.inputsNameClass(config.name);
            final ClassName outputClassName = ClassName.get(locations.getFilePackage(BeanDirection.OUTPUTS), outputNameClass);
            final ClassName inputClassName = ClassName.get(locations.getFilePackage(BeanDirection.INPUTS), inputNameClass);

            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(inputClassName,"bean").build())
                    .returns(outputClassName);

            compilerUtil.specWithComment(mspec);

                mspec.addStatement("return $N.generic_enact(new $T(),bean,\n" +
                        "                b -> checker.process(b),\n" +
                        "                (sb,b) -> new $T(sb,$N.get()).process(b),\n" +
                        "                (rs,b) -> $N.beanCompleterFactory(rs,postProcessing).process(b))", Constants.REALISER, outputClassName, queryInvokerClass, principalVar, Constants.REALISER);

            builder.addMethod(mspec.build());
        }


        TypeSpec theLogger = builder.build();

        String myPackage= locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName+ DOT_JAVA_EXTENSION, myPackage);

    }



}