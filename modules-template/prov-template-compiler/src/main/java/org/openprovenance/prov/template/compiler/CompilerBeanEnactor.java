package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanEnactor {
    public static final String REALISER = "realiser";
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerBeanEnactor() {
    }

    static final TypeVariableName typeT = TypeVariableName.get("T");
    static final TypeVariableName typeResultSet = TypeVariableName.get("ResultSet");
    static final TypeName classType=ParameterizedTypeName.get(ClassName.get(Class.class),typeT);
    static final TypeName consumerT=ParameterizedTypeName.get(ClassName.get(Consumer.class),typeT);
    static final TypeName mapType=ParameterizedTypeName.get(ClassName.get(Map.class),ClassName.get(String.class),ClassName.get(Object.class));
    static final TypeName biconsumerType=ParameterizedTypeName.get(ClassName.get(BiConsumer.class),ClassName.get(StringBuilder.class),typeT);
    static final TypeName biconsumerType2=ParameterizedTypeName.get(ClassName.get(BiConsumer.class),typeResultSet,typeT);



    JavaFile generateBeanEnactor(TemplatesCompilerConfig configs) {

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");




        TypeSpec.Builder builder = compilerUtil.generateClassInit(BEAN_ENACTOR);
        builder.addModifiers(Modifier.ABSTRACT);
        builder.addTypeVariable(typeResultSet);


        ClassName queryInvokerClass = ClassName.get(configs.configurator_package, QUERY_INVOKER);
        ClassName beanCompleterClass = ClassName.get(configs.configurator_package, BEAN_COMPLETER);

        ClassName beanProcessorClass = ClassName.get(configs.logger_package, configs.beanProcessor);
        builder.addSuperinterface(beanProcessorClass);



        TypeSpec.Builder inface=compilerUtil.generateInterfaceInit(ConfigProcessor.ENACTOR_IMPLEMENTATION);
        inface.addTypeVariable(typeResultSet);
        MethodSpec.Builder method1 = MethodSpec.methodBuilder("generic_enact")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeT,"bean").build())
                .addParameter(ParameterSpec.builder(consumerT,"checker").build())
                .addParameter(ParameterSpec.builder(biconsumerType,"queryInvoker").build())
                .addParameter(ParameterSpec.builder(biconsumerType2,"completeBean").build())
                .addTypeVariable(typeT)
                .returns(typeT);

        inface.addMethod(method1.build());

        //    abstract public BeanCompleter beanCompleterFactory(ResultSet rs);
        MethodSpec.Builder method2 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeResultSet,"rs").build())
                .returns(beanCompleterClass);

        inface.addMethod(method2.build());


        builder.addType(inface.build());

        builder.addField(beanProcessorClass,"checker",Modifier.FINAL, Modifier.PRIVATE);



        // Note, this is a inner interface, and the construction of its TypeName is a bit convoluted
        final TypeName ENACTOR_IMPLEMENTATION_TYPE=ParameterizedTypeName.get(ClassName.get(configs.configurator_package+"."+BEAN_ENACTOR,ENACTOR_IMPLEMENTATION),typeResultSet);

        builder.addField(ENACTOR_IMPLEMENTATION_TYPE, REALISER, Modifier.FINAL, Modifier.PRIVATE);


        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ENACTOR_IMPLEMENTATION_TYPE, REALISER)
                .addParameter(beanProcessorClass, "checker")
                .addStatement("this.$N = $N", REALISER, REALISER)
                .addStatement("this.$N = $N", "checker", "checker");

        builder.addMethod(cbuilder3.build());



        for (TemplateCompilerConfig config : configs.templates) {
            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);

            final String beanNameClass = compilerUtil.beanNameClass(config.name);
            String packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, beanNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(ConfigProcessor.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(className,"bean").build())
                    .returns(className);

            mspec.addStatement("return $N.generic_enact(bean,\n" +
                    "                b -> checker.process(b),\n" +
                    "                (sb,b) -> new $T(sb).process(b),\n" +
                    "                (rs,b) -> $N.beanCompleterFactory(rs).process(b))", REALISER, queryInvokerClass, REALISER);


            builder.addMethod(mspec.build());
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.configurator_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateBeanEnactor() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }



}