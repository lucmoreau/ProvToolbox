package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanCompleter {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerBeanCompleter() {
    }

    static final TypeVariableName typeT = TypeVariableName.get("T");
    static final TypeName classType=ParameterizedTypeName.get(ClassName.get(Class.class),typeT);
    static final TypeName mapType=ParameterizedTypeName.get(ClassName.get(Map.class),ClassName.get(String.class),ClassName.get(Object.class));

    // FIXME: J4TS does not have java.sql.ResultSet.
    // so disabling this code for now
    boolean sqlCode=false;

    JavaFile generateBeanCompleter(TemplatesCompilerConfig configs) {

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");




        TypeSpec.Builder builder = compilerUtil.generateClassInit(BEAN_COMPLETER);

        builder.addSuperinterface(ClassName.get(configs.logger_package,configs.beanProcessor));

        if (sqlCode) builder.addField(ResultSet.class,"rs", Modifier.PUBLIC);
        builder.addField(mapType,"m", Modifier.FINAL);

        MethodSpec.Builder callMe1=MethodSpec.methodBuilder("getResultSet")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(classType, "cl")
                .addParameter(String.class, "key")
                .returns(typeT)
                .addTypeVariable(typeT)
                .beginControlFlow("try")
                .addStatement("return ($T) rs.getObject($N, $N)", typeT, "key", "cl")  // Note that the casting to ($T) is not required, since ResultSet::getObject is generic. However, It is not known by JSWEET
                .nextControlFlow("catch ($T e)", SQLException.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow();
        if (sqlCode) {
            builder.addMethod(callMe1.build());
        } else {

        }


        MethodSpec.Builder callMe2=MethodSpec.methodBuilder("getMap")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(classType, "cl")
                .addParameter(String.class, "key")
                .returns(typeT)
                .addTypeVariable(typeT)
                .addStatement("return ($T) m.get($N)", typeT, "key");
        builder.addMethod(callMe2.build());

        TypeSpec.Builder inface=compilerUtil.generateInterfaceInit(ConfigProcessor.GETTER);
        inface.addMethod(MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .addParameter(classType,"cl")
                .addParameter(String.class,"col")
                .returns(typeT)
                .addTypeVariable(typeT)
                .build());
        builder.addType(inface.build());


        builder.addField(TypeVariableName.get(ConfigProcessor.GETTER),"getter", Modifier.FINAL);

        MethodSpec.Builder cbuilder1= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs")
                .addStatement("this.$N = $N", "rs", "rs")
                .addStatement("this.$N = null", "m")
                .addStatement("this.getter = this::getResultSet");
        if (sqlCode) builder.addMethod(cbuilder1.build());
        MethodSpec.Builder cbuilder2= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapType, "m")
                .addStatement("this.$N = $N", "m", "m")
                .addComment("The following code implements this assignment, in a way that jsweet can compile")
                .addComment("this.getter = this::getMap");
        cbuilder2.addStatement("this.getter = $L",
                TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(TypeVariableName.get(GETTER))
                        .addMethod(MethodSpec.methodBuilder("get")
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(classType,"cl")
                                .addParameter(String.class,"col")
                                .returns(typeT)
                                .addTypeVariable(typeT)
                                .addStatement("return getMap(cl, col)").build()).build());

        if (sqlCode) {
            cbuilder2.addStatement("this.$N = null", "rs");
        }

        builder.addMethod(cbuilder2.build());

        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeVariableName.get(GETTER), "getter")
                .addStatement("this.$N = null", "m")
                .addStatement("this.getter = ($N) getter", GETTER);
        if (sqlCode) {
            cbuilder3.addStatement("this.$N = null", "rs");
        }
        builder.addMethod(cbuilder3.build());

        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

            final String beanNameClass = compilerUtil.beanNameClass(config.name);
            String packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, beanNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(ConfigProcessor.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(className,"bean").build())
                    .returns(className);


            for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
                if (descriptorUtils.isOutput(key,bindingsSchema)) {
                    Class<?> cl=compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
                    mspec.addStatement("bean.$N= getter.get($N.class,$S)", key, cl.getSimpleName(), key);
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