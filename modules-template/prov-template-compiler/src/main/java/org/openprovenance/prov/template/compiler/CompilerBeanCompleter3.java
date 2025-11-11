package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import java.util.HashSet;
import java.util.Set;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerBeanCompleter3 {
    private final CompilerUtil compilerUtil;
    private final boolean debugComment=true;


    public CompilerBeanCompleter3(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateBeanCompleter3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_COMPLETER3);

        builder.superclass(ClassName.get(locations.getFilePackage(configs.name, Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2));
        builder.addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);

        MethodSpec.Builder cbuilder2= MethodSpec.constructorBuilder();
        compilerUtil.specWithComment(cbuilder2);
        cbuilder2
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CompilerUtil.mapType, "m")
                .addStatement("super(m)");


        builder.addMethod(cbuilder2.build());

        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder();
        compilerUtil.specWithComment(cbuilder3);
        cbuilder3
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeVariableName.get(Constants.GETTER), "getter")
                .addStatement("super(getter)");

        builder.addMethod(cbuilder3.build());

        builder.addMethod(MethodSpec.methodBuilder("getValueFromLocation")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .returns(Integer.class)
                .build());
        builder.addMethod(MethodSpec.methodBuilder("setValueInLocation")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .build());

        // gather all composee templates by finding composite templates, and then finding the templates they consist of
        Set<String> composeeTemplates = new HashSet<>();
        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof CompositeTemplateCompilerConfig) {
                CompositeTemplateCompilerConfig config1=(CompositeTemplateCompilerConfig)config;
                composeeTemplates.add(config1.consistsOf);
            }
        }


        for (TemplateCompilerConfig config : configs.templates) {
            if  (config instanceof SimpleTemplateCompilerConfig) {
                if (composeeTemplates.contains(config.name)) {


                    final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);

                    final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS), outputBeanNameClass);
                    MethodSpec.Builder mspec = createSimpleProcessMethod(outputClassName,config.name);

                    builder.addMethod(mspec.build());


                }
            } else {
                CompositeTemplateCompilerConfig config1=(CompositeTemplateCompilerConfig)config;
                final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);

                final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS), outputBeanNameClass);

                MethodSpec.Builder mspec = createCompositeProcessMethod(config.fullyQualifiedName,outputClassName);
                builder.addMethod(mspec.build());

            }
        }



        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(configs.name, fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);

    }


    private MethodSpec.Builder createSimpleProcessMethod(ClassName outputClassName, String template) {
        MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(outputClassName,BEAN_VAR).build())
                .returns(outputClassName);
        compilerUtil.specWithComment(mspec);

        mspec.addStatement("super.$N($N)", Constants.PROCESS_METHOD_NAME, BEAN_VAR);
        mspec.addStatement("setValueInLocation()");
        mspec.addStatement("return $N", BEAN_VAR);
        mspec.addAnnotation(Override.class);

        return mspec;
    }

    private MethodSpec.Builder createCompositeProcessMethod(String templateFullyQualifiedName, ClassName outputClassName) {
        MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(outputClassName,BEAN_VAR).build())
                .returns(outputClassName);
        compilerUtil.specWithComment(mspec);


        mspec.addStatement("$T result=super.$N($N)", outputClassName, Constants.PROCESS_METHOD_NAME, BEAN_VAR);
        mspec.addStatement("result.ID=getValueFromLocation()");
        mspec.addStatement("$N($N.ID,$S)", POST_PROCESS_METHOD_NAME, "result", templateFullyQualifiedName);

        mspec.addStatement("return $N", "result");
        mspec.addAnnotation(Override.class);

        return mspec;
    }


}