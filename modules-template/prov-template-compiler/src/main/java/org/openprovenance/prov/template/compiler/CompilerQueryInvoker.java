package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.Map;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerQueryInvoker {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerQueryInvoker() {
    }

    static final TypeVariableName typeT = TypeVariableName.get("T");
    static final TypeName classType=ParameterizedTypeName.get(ClassName.get(Class.class),typeT);
    static final TypeName mapType=ParameterizedTypeName.get(ClassName.get(Map.class),ClassName.get(String.class),ClassName.get(Object.class));


    public JavaFile generateQueryInvoker(TemplatesCompilerConfig configs) {





        TypeSpec.Builder builder = compilerUtil.generateClassInit(QUERY_COMPOSER);

        builder.addSuperinterface(ClassName.get(configs.logger_package,configs.beanProcessor));

        String sbVar="sb";
        builder.addField(StringBuilder.class, sbVar, Modifier.FINAL);


        MethodSpec.Builder cbuilder= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(StringBuilder.class, sbVar)
                .addStatement("this.$N = $N", sbVar, sbVar);
        builder.addMethod(cbuilder.build());


        boolean foundTime=false;

        for (TemplateCompilerConfig config : configs.templates) {
            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);

            final String beanNameClass = compilerUtil.beanNameClass(config.name);
            String packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, beanNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(ConfigProcessor.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(ParameterSpec.builder(className,"bean").build())
                    .returns(className);

            mspec.addStatement("$N.append($S)", sbVar, "select * from ");
            String startCallString=INSERT_PREFIX + config.name + " (";
            mspec.addStatement("$N.append($S)", sbVar, startCallString);

            boolean first=true;

            for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
                if (descriptorUtils.isInput(key,bindingsSchema)) {
                    Class<?> cl=compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
                    if (first) {
                        first=false;
                    } else {
                        mspec.addStatement("$N.append($S)", sbVar, ",");
                    }
                    final String sqlType = descriptorUtils.getSqlType(key, bindingsSchema);
                    if (sqlType !=null) {
                        addValueWithSpecialType(sqlType,mspec, sbVar, key);
                        foundTime=true;
                    } else {
                        mspec.addStatement("$N.append(bean.$N)", sbVar, key);
                    }
                }
            }
            String endCallString=");";
            mspec.addStatement("$N.append($S)", sbVar, endCallString);
            mspec.addStatement("return bean");

            builder.addMethod(mspec.build());
        }

        if (foundTime) {
            final String timeVariable = "time";
            MethodSpec.Builder cbuilder2= MethodSpec.methodBuilder("addBeanTime")
                    .addModifiers(Modifier.FINAL)
                    .addParameter(StringBuilder.class, sbVar)
                    .addParameter(String.class, timeVariable)
                    .beginControlFlow("if ($N==null)", timeVariable)
                    .addStatement("$N.append($S)", sbVar, "NULL")
                    .nextControlFlow("else")
                    .addStatement("$N.append($S)", sbVar, "'")
                    .addStatement("$N.append($N)", sbVar, timeVariable)
                    .addStatement("$N.append($S)", sbVar, "'::timestamptz")
                    .endControlFlow();

            builder.addMethod(cbuilder2.build());
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.configurator_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateQueryInvoker() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }

    public void addValueWithSpecialType(String specialType, MethodSpec.Builder mspec, String sbVar, String key) {
        switch (specialType) {
            case "timestamptz":
                mspec.addStatement("addBeanTime($N,bean.$N)", sbVar, key);
                break;
            case "nullable":
                mspec.addStatement("addBeanTime($N,bean.$N)", sbVar, key);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + specialType);
        }

    }


}