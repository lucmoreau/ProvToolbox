package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerQueryInvoker {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerQueryInvoker() {
    }

    static final TypeVariableName typeT = TypeVariableName.get("T");
    static final TypeName classType=ParameterizedTypeName.get(ClassName.get(Class.class),typeT);
    static final TypeName mapType=ParameterizedTypeName.get(ClassName.get(Map.class),ClassName.get(String.class),ClassName.get(Object.class));


    public JavaFile generateQueryInvoker(TemplatesCompilerConfig configs) {





        TypeSpec.Builder builder = compilerUtil.generateClassInit(QUERY_INVOKER);

        builder.addSuperinterface(ClassName.get(configs.logger_package,configs.beanProcessor));

        String sbVar="sb";
        builder.addField(StringBuilder.class, sbVar, Modifier.FINAL);


        MethodSpec.Builder cbuilder= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(StringBuilder.class, sbVar)
                .addStatement("this.$N = $N", sbVar, sbVar);
        builder.addMethod(cbuilder.build());


        Set<String> foundSpecialTypes=new HashSet<>();

        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;

            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

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
                        String fun= converterForSpecialType(sqlType,mspec, sbVar, key);
                        mspec.addStatement("$N.append($N(bean.$N))", sbVar, fun, key);
                        foundSpecialTypes.add(sqlType);
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

        if (foundSpecialTypes.contains(TIMESTAMPTZ)) {
            final String timeVariable = "time";
            MethodSpec.Builder mbuilder2= MethodSpec.methodBuilder("convertToTimestamptz")
                    .addModifiers(Modifier.FINAL)
                    .addParameter(String.class, timeVariable)
                    .returns(String.class)
                    .beginControlFlow("if ($N==null)", timeVariable)
                    .addStatement("return $S",  "NULL")
                    .nextControlFlow("else")
                    .addStatement("return $S+$N+$S", "'", timeVariable, "'::timestamptz")
                    .endControlFlow();

            builder.addMethod(mbuilder2.build());
        }
        if (foundSpecialTypes.contains(NULLABLE_TEXT)) {
            final String strVariable = "str";
            MethodSpec.Builder mbuilder3= MethodSpec.methodBuilder("convertToNullableTEXT")
                    .addModifiers(Modifier.FINAL)
                    .addParameter(String.class, strVariable)
                    .returns(String.class)
                    .beginControlFlow("if ($N==null)", strVariable)
                    .addStatement("return $S",  "''::TEXT")
                    .nextControlFlow("else")
                    .addStatement("return $S+$N.replace($S,$S)+$S", "'", strVariable, "'", "''", "'::TEXT")
                    .endControlFlow();

            builder.addMethod(mbuilder3.build());
        }



        TypeSpec theLogger = builder.build();

        JavaFile myfile = JavaFile.builder(configs.configurator_package, theLogger)
                .addFileComment("Generated Automatically by ProvToolbox method $N.generateQueryInvoker() for templates config $N", getClass().getName(), configs.name)
                .build();
        return myfile;
    }

    public String converterForSpecialType(String specialType, MethodSpec.Builder mspec, String sbVar, String key) {
        switch (specialType) {
            case TIMESTAMPTZ:
                return "convertToTimestamptz";
            case NULLABLE_TEXT:
                return "convertToNullableTEXT";
            default:
                throw new IllegalStateException("Unexpected value: " + specialType);
        }
    }


}