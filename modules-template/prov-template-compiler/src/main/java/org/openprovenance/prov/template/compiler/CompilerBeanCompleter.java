package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.BEAN_COMPLETER;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;

public class CompilerBeanCompleter {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public class Dummy<T> {
        private final BiFunction<Class<T>, String, T> getter;

        Dummy(BiFunction<Class<T>, String, T> getter) {
            this.getter = getter;
        }



    }

    public CompilerBeanCompleter() {
    }

    static final TypeName classType=ParameterizedTypeName.get(ClassName.get(Class.class),TypeVariableName.get("T"));
    static final TypeName functionType2 = ParameterizedTypeName.get(ClassName.get(BiFunction.class), classType, TypeName.get(String.class), TypeVariableName.get("T"));
    static final TypeName functionType = ParameterizedTypeName.get(ClassName.get(Function.class), TypeName.get(String.class), TypeName.get(Object.class));




    JavaFile generateBeanCompleter(TemplatesCompilerConfig configs) {

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");




        TypeSpec.Builder builder = compilerUtil.generateClassInit(BEAN_COMPLETER);

        builder.addSuperinterface(ClassName.get(configs.logger_package,configs.beanProcessor));

        builder.addField(functionType,"getter", Modifier.FINAL);

        com.squareup.javapoet.MethodSpec.Builder cbuilder= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(functionType, "getter")
                .addStatement("this.$N = $N", "getter", "getter");
        builder.addMethod(cbuilder.build());


        //FIXME: This one does not work
        //FieldSpec.Builder fbuild=FieldSpec.builder(functionType2,"getter2");
        //builder.addField(fbuild.build());

        String packge = null;
        for (TemplateCompilerConfig config : configs.templates) {
            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);

            final String beanNameClass = compilerUtil.beanNameClass(config.name);
            packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, beanNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(ConfigProcessor.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(className,"bean").build())
                    .returns(className);


            // field= (Integer) =getter.apply("field")
            for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
                if (descriptorUtils.isOutput(key,bindingsSchema)) {
                    Class<?> cl=compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
                   // mspec.addStatement("bean.$N= getter.apply($N.class,$S)",key,cl.getName(), key);
                    mspec.addStatement("bean.$N= ($T) getter.apply($S)",key,cl, key);
                }
            }

            mspec.addStatement("return bean");

            builder.addMethod(mspec.build());
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.configurator_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateBeanCompleter() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }







  }