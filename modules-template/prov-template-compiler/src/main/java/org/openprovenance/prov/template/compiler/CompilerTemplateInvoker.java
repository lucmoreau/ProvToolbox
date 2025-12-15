package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerTemplateInvoker {
    public static final String BEAN = "bean";
    private final CompilerUtil compilerUtil;

    public CompilerTemplateInvoker(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    static TypeName mapType=ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(Object.class));

    SpecificationFile generateTemplateInvoker(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();
        TypeSpec.Builder builder = compilerUtil.generateClassInit(TEMPLATE_INVOKER).addSuperinterface(ClassName.get(locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR),INPUT_OUTPUT_PROCESSOR)).addModifiers(Modifier.ABSTRACT);



        for (TemplateCompilerConfig config : configs.templates) {
            final String inputsNameClass = compilerUtil.inputsNameClass(config.name);
            final String outputsNameClass = compilerUtil.outputsNameClass(config.name);

            final ClassName inputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS), inputsNameClass);
            final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS), outputsNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(inputClassName, BEAN).build())
                    .returns(outputClassName);
            compilerUtil.specWithComment(mspec);


            //return generic_post_and_return(Defining_environmentOutputs.class, inputs0, (m, o) -> new BeanCompleter2(m).process(o));
            ClassName completerClass;

            if (config instanceof SimpleTemplateCompilerConfig) {
                completerClass = ClassName.get(locations.getFilePackage(configs.name, BEAN_COMPLETER2), BEAN_COMPLETER2);
                mspec.addStatement("return $N($T.class, $N, (m, o) -> new $T(m).process(o))", GENERIC_POST_AND_RETURN, outputClassName, BEAN, completerClass);

            } else {
                completerClass = ClassName.get(locations.getFilePackage(configs.name, COMPOSITE_BEAN_COMPLETER2), COMPOSITE_BEAN_COMPLETER2);
                mspec.addStatement("return $N($T.class, $N, (m, o) -> {o.$N=new $T<>(); return new $T(m).process(o); })", GENERIC_POST_AND_RETURN, outputClassName, BEAN, ELEMENTS, LinkedList.class, completerClass);

            }





            builder.addMethod(mspec.build());
        }

        TypeVariableName inVar = TypeVariableName.get("IN");
        TypeVariableName outVar = TypeVariableName.get("OUT");

        TypeName biFunctionType=ParameterizedTypeName.get(ClassName.get(BiFunction.class), mapType, outVar, outVar);

        MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.GENERIC_POST_AND_RETURN)
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(Class.class),outVar), "clazz").build())
                .addParameter(ParameterSpec.builder(inVar, "inputs").build())
                .addParameter(ParameterSpec.builder(biFunctionType, "completer").build())
                .addTypeVariables(List.of(inVar, outVar))
                .returns(outVar);

        builder.addMethod(mspec.build());



        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(configs.name, fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);


    }








}