package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.CompilerBeanEnactor2WithPrincipal.SupplierOfString;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.sql.CompilerSqlIntegration.BIFUN;

public class CompilerBeanEnactor2CompositeWithPrincipal {
    private final CompilerUtil compilerUtil;

    public CompilerBeanEnactor2CompositeWithPrincipal(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateBeanEnactor2CompositeWithPrincipal(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_ENACTOR2_COMPOSITE_WP);
        builder.addTypeVariable(typeResult);
        builder.addJavadoc("Ensures that composite beans are given an ID\n");


        ClassName queryInvokerClass = ClassName.get(locations.getFilePackage(configs.name, Constants.QUERY_INVOKER2WP), Constants.QUERY_INVOKER2WP);
        ParameterizedTypeName beanEnactor2Class = ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.name,Constants.BEAN_ENACTOR2_WP), Constants.BEAN_ENACTOR2_WP), typeResult);

        ClassName inputProcessorClass = ClassName.get(locations.getFilePackage(configs.name, INPUT_PROCESSOR), INPUT_PROCESSOR);
        builder.superclass(beanEnactor2Class);




        builder.addField(inputProcessorClass,"checker",Modifier.FINAL, Modifier.PRIVATE);
        builder.addField(BIFUN, "postProcessing", Modifier.FINAL, Modifier.PRIVATE);
        builder.addField(SupplierOfString, PRINCIPAL_MANAGER_VAR, Modifier.FINAL, Modifier.PRIVATE);



        // Note, this is a inner interface, and the construction of its TypeName is a bit convoluted
        final TypeName ENACTOR_IMPLEMENTATION_TYPE=ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.name, Constants.BEAN_ENACTOR2_WP)+"."+ Constants.BEAN_ENACTOR2_WP, Constants.ENACTOR_IMPLEMENTATION), typeResult);

        builder.addField(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER, Modifier.FINAL, Modifier.PRIVATE);


        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER)
                .addParameter(inputProcessorClass, "checker")
                .addParameter(BIFUN, "postProcessing")
                .addParameter(SupplierOfString, PRINCIPAL_MANAGER_VAR);
        compilerUtil.specWithComment(cbuilder3);

        cbuilder3
                .addStatement("super($N,$N,$N, $N)", Constants.REALISER, "checker","postProcessing", PRINCIPAL_MANAGER_VAR)
                .addStatement("this.$N = $N", Constants.REALISER, Constants.REALISER)
                .addStatement("this.$N = $N", "checker", "checker")
                .addStatement("this.$N = postProcessing", "postProcessing")
                .addStatement("this.$N = $N", PRINCIPAL_MANAGER_VAR, PRINCIPAL_MANAGER_VAR);

        builder.addMethod(cbuilder3.build());



        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof CompositeTemplateCompilerConfig) {


                final String outputNameClass = compilerUtil.outputsNameClass(config.name);
                final String inputNameClass = compilerUtil.inputsNameClass(config.name);
                final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS), outputNameClass);
                final ClassName inputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS), inputNameClass);

                MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(ParameterSpec.builder(inputClassName,"bean").build())
                        .returns(outputClassName);

                compilerUtil.specWithComment(mspec);

                mspec.addStatement("System.out.println(\"LUC Processing composite bean for: $T\" )", inputClassName);

                mspec.addStatement("return $N.generic_enact(new $T(),bean,\n" +
                        "                b -> checker.process(b),\n" +
                        "                (sb,b) -> new $T(sb,true,$N.get()).process(b),\n" +
                        "                (rs,b) -> $N.beanCompleterFactory(rs,new Object[1],postProcessing).process(b))", Constants.REALISER, outputClassName, queryInvokerClass, PRINCIPAL_MANAGER_VAR, Constants.REALISER);

                builder.addMethod(mspec.build());
            }
        }


        TypeSpec theLogger = builder.build();

        String myPackage= locations.getFilePackage(configs.name,fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName+ DOT_JAVA_EXTENSION, myPackage);

    }



}