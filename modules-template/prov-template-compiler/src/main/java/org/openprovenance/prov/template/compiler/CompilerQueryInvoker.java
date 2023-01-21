package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.CompositeTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Set;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerQueryInvoker {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerQueryInvoker() {
    }


    public JavaFile generateQueryInvoker(TemplatesCompilerConfig configs) {


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.QUERY_INVOKER);

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

            final String beanNameClass = compilerUtil.beanNameClass(config.name);
            String packge = config.package_ + ".client";
            final ClassName className = ClassName.get(packge, beanNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(ParameterSpec.builder(className, VARIABLE_BEAN).build())
                    .returns(className);

            if (config instanceof SimpleTemplateCompilerConfig) {
                simpleQueryInvoker(configs, config, foundSpecialTypes, sbVar, mspec, VARIABLE_BEAN);
                mspec.addStatement("return $N", VARIABLE_BEAN);
            } else {
                compositeQueryInvoker(configs, config, foundSpecialTypes, sbVar, mspec, VARIABLE_BEAN);
                mspec.addStatement("return $N", VARIABLE_BEAN);
            }

            builder.addMethod(mspec.build());
        }

        if (foundSpecialTypes.contains(Constants.TIMESTAMPTZ)) {
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
        if (foundSpecialTypes.contains(Constants.NULLABLE_TEXT)) {
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

    private void simpleQueryInvoker(TemplatesCompilerConfig configs, TemplateCompilerConfig config, Set<String> foundSpecialTypes, String sbVar, MethodSpec.Builder mspec, String variableBean) {
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);
        String startCallString= Constants.INSERT_PREFIX + config.name + " (";

        mspec.addStatement("$N.append($S)", sbVar, "select * from ");
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
                    String fun= converterForSpecialType(sqlType, mspec, sbVar, key);
                    mspec.addStatement("$N.append($N($N.$N))", sbVar, fun, variableBean, key);
                    foundSpecialTypes.add(sqlType);
                } else {
                    mspec.addStatement("$N.append($N.$N)", sbVar, variableBean, key);
                }
            }
        }
        String endCallString= ");";
        mspec.addStatement("$N.append($S)", sbVar, endCallString);

    }
    private void simpleQueryInvokerEmbedded(TemplatesCompilerConfig configs, TemplateCompilerConfig config, Set<String> foundSpecialTypes, String sbVar, MethodSpec.Builder mspec, String variableBean) {
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);
        String startCallString= Constants.INSERT_PREFIX + config.name + " (";

        mspec.addComment("Generated Automatically by ProvToolbox ($N.$N()) for template $N",getClass().getName(), "simpleQueryInvokerEmbedded", config.name);


        mspec.addStatement("$N.append($S)", sbVar, "( ");

        boolean first=true;

        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            boolean doProcess=true;
            //doProcess=!"anticipating".equals(key);  //FIXME

            if (doProcess) {
                Class<?> cl=compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
                if (first) {
                    first=false;
                } else {
                    mspec.addStatement("$N.append($S)", sbVar, ",");
                }
                final String sqlType = descriptorUtils.getSqlType(key, bindingsSchema);
                if (sqlType !=null) {
                    String fun= converterForSpecialType(sqlType, mspec, sbVar, key);
                    mspec.addStatement("$N.append($N($N.$N))", sbVar, fun, variableBean, key);
                    foundSpecialTypes.add(sqlType);
                } else {
                    mspec.addStatement("$N.append($N.$N)", sbVar, variableBean, key);
                }
            }
        }
        String endCallString= ") :: " + config.name + "_type";
        mspec.addStatement("$N.append($S)", sbVar, endCallString);

    }

    public void compositeQueryInvoker(TemplatesCompilerConfig configs, TemplateCompilerConfig config, Set<String> foundSpecialTypes, String sbVar, MethodSpec.Builder mspec, String variableBean) {
        CompositeTemplateCompilerConfig compositeConfig=(CompositeTemplateCompilerConfig ) config;
        mspec.addComment("Generated Automatically by ProvToolbox ($N.$N()) for template $N",getClass().getName(), "compositeQueryInvoker", compositeConfig.name);

        mspec.addStatement("$N.append($S)", sbVar, "---- query invoker for  " + compositeConfig.name + "\n\n");


        mspec.addStatement("$N.append($S)", sbVar, "select * from ");
        String startCallString= Constants.INSERT_PREFIX + config.name + INSERT_ARRAY_SUFFIX +" (";
        mspec.addStatement("$N.append($S)", sbVar, startCallString);
        mspec.addStatement("$N.append($S)", sbVar, "ARRAY[\n");
        String variableBean1=variableBean+"_1";
        mspec.addStatement("boolean first=true");

        SimpleTemplateCompilerConfig composee=null;
        for (TemplateCompilerConfig c: configs.templates) {
            if (compositeConfig.consistsOf.equals(c.name)) {
                composee=(SimpleTemplateCompilerConfig) c;
            }
        }
        mspec.beginControlFlow("for ($N $N: $N.$N)", compilerUtil.beanNameClass(compositeConfig.consistsOf), variableBean1, variableBean, ELEMENTS);

        mspec.beginControlFlow("if (first)")
                .addStatement("first=false")
                .nextControlFlow("else")
                .addStatement("$N.append($S)", sbVar, ",\n     ")
                .endControlFlow();


        if (composee==null) throw new IllegalStateException("No composee found " + compositeConfig.consistsOf);

        simpleQueryInvokerEmbedded(configs,composee,foundSpecialTypes,sbVar,mspec, variableBean1);

        mspec.endControlFlow();

        mspec.addStatement("$N.append($S)", sbVar, "]);\n");


        /*

select *
from insert_anticipating_impact_composite_array(
    ARRAY[  (-1, -1, 20,14,5,8, 0,  '2018-10-24T22:20:13.456Z'::timestamptz)::anticipating_impact_type,
                    (-2, -1, 21,15,7,9, 0,  '2018-11-24T22:20:13.456Z'::timestamptz)::anticipating_impact_type,
                    (-2, -1, 11,15,7,1, 1,  '2018-12-24T22:20:13.456Z'::timestamptz)::anticipating_impact_type ]);

ssue in enactment ---- query invoker for  anticipating_impact_composite

select * from insert_anticipating_impact_composite_array (ARRAY[
[ 20,14,5,8,'2018-10-24T22:20:13.456Z'::timestamptz],
     [ 21,15,7,9,'2018-11-24T22:20:13.456Z'::timestamptz],
     [ 11,15,7,1,'2018-12-24T22:20:13.456Z'::timestamptz]]);

select * from insert_anticipating_impact_composite_array (ARRAY[
[ -1,-1,20,14,5,8, 0,'2018-10-24T22:20:13.456Z'::timestamptz] :: anticipating_impact_type,
     [ -2,-1,21,15,7,9,1,'2018-11-24T22:20:13.456Z'::timestamptz] :: anticipating_impact_type,
     [ -2,-1,11,15,7,1,1,'2018-12-24T22:20:13.456Z'::timestamptz] :: anticipating_impact_type]);

*/


    }

    public String converterForSpecialType(String specialType, MethodSpec.Builder mspec, String sbVar, String key) {
        switch (specialType) {
            case Constants.TIMESTAMPTZ:
                return "convertToTimestamptz";
            case Constants.NULLABLE_TEXT:
                return "convertToNullableTEXT";
            default:
                throw new IllegalStateException("Unexpected value: " + specialType);
        }
    }


}