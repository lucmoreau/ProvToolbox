package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerQueryInvoker {
    public static final String CONVERT_TO_NULLABLE_TEXT = "convertToNullableTEXT";
    public static final String CONVERT_TO_NON_NULLABLE_TEXT = "convertToNonNullableTEXT";
    public static final String sbVar="sb";
    public static final String linkingVar="linking";
    private final CompilerUtil compilerUtil;


    public CompilerQueryInvoker(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public SpecificationFile generateQueryInvoker(TemplatesProjectConfiguration configs, Locations locations, boolean withBean, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil.generateClassInit((withBean)?Constants.QUERY_INVOKER:Constants.QUERY_INVOKER2);

        if (withBean) {
            builder.addSuperinterface(compilerUtil.getClass(BEAN_PROCESSOR,locations));
        } else {
            builder.addSuperinterface(ClassName.get(locations.getFilePackage(INPUT_PROCESSOR), INPUT_PROCESSOR));
        }


        builder.addField(StringBuilder.class, sbVar, Modifier.FINAL);
        builder.addField(boolean.class, linkingVar, Modifier.FINAL);

        MethodSpec.Builder cbuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(StringBuilder.class, sbVar)
                .addStatement("this.$N = $N", sbVar, sbVar)
                .addStatement("this.$N = false", linkingVar);
        builder.addMethod(cbuilder.build());

        if (!withBean) {
            // add an additional constructor for QUERY_INVOKER2
            MethodSpec.Builder cbuilder2 = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(StringBuilder.class, sbVar)
                    .addParameter(boolean.class, linkingVar)
                    .addStatement("this.$N = $N", sbVar, sbVar)
                    .addStatement("this.$N = $N", linkingVar, linkingVar);
            builder.addMethod(cbuilder2.build());
        }


        Set<String> foundSpecialTypes=new HashSet<>();
        foundSpecialTypes.add(NON_NULLABLE_TEXT); // always add this one, as it is required in QueryInvoker2WP

        for (TemplateCompilerConfig config : configs.templates) {

            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            final String inputsNameClass = compilerUtil.inputsNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), beanNameClass);
            final ClassName inputClassName = ClassName.get(locations.getFilePackage(BeanDirection.INPUTS), inputsNameClass);

            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            compilerUtil.specWithComment(mspec);
            mspec
                    .addParameter(ParameterSpec.builder((withBean)?className:inputClassName, VARIABLE_BEAN).build())
                    .returns((withBean)?className:inputClassName);

            if (config instanceof SimpleTemplateCompilerConfig) {
                simpleQueryInvoker(configs, config, foundSpecialTypes, sbVar, mspec, VARIABLE_BEAN);
                mspec.addStatement("return $N", VARIABLE_BEAN);
            } else {
                compositeQueryInvoker(configs, locations, config, foundSpecialTypes, sbVar, mspec, VARIABLE_BEAN, withBean);
                mspec.addStatement("return $N", VARIABLE_BEAN);
            }

            builder.addMethod(mspec.build());
        }

        if (foundSpecialTypes.contains(Constants.TIMESTAMPTZ)) {
            final String timeVariable = "time";
            MethodSpec.Builder mbuilder2= MethodSpec.methodBuilder("convertToTimestamptz");
            compilerUtil.specWithComment(mbuilder2);
            mbuilder2
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                    .addParameter(String.class, timeVariable)
                    .returns(String.class)
                    .beginControlFlow("if ($N==null)", timeVariable)
                    .addStatement("return $S",  "NULL")
                    .nextControlFlow("else")
                    .addStatement("return $S+$N+$S", "'", timeVariable, "'::timestamptz")
                    .endControlFlow();

            builder.addMethod(mbuilder2.build());
        }
        if (foundSpecialTypes.contains(Constants.SQL_DATE)) {
            final String dateVariable = "date";
            MethodSpec.Builder mbuilder2= MethodSpec.methodBuilder("convertToDate");
            compilerUtil.specWithComment(mbuilder2);
            mbuilder2
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                    .addParameter(String.class, dateVariable)
                    .returns(String.class)
                    .beginControlFlow("if ($N==null)", dateVariable)
                    .addStatement("return $S",  "NULL")
                    .nextControlFlow("else")
                    .addStatement("return $S+$N+$S", "'", dateVariable, "'::date")
                    .endControlFlow();

            builder.addMethod(mbuilder2.build());
        }
        if (foundSpecialTypes.contains(Constants.NULLABLE_TEXT)) {
            final String strVariable = "str";
            MethodSpec.Builder mbuilder3= MethodSpec.methodBuilder(CONVERT_TO_NULLABLE_TEXT);
            compilerUtil.specWithComment(mbuilder3);
            mbuilder3
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                    .addParameter(String.class, strVariable)
                    .returns(String.class)
                    .beginControlFlow("if ($N==null)", strVariable)
                    .addStatement("return $S",  "''::TEXT")
                    .nextControlFlow("else")
                    .addStatement("return $S+$N.replace($S,$S)+$S", "'", strVariable, "'", "''", "'::TEXT")
                    .endControlFlow();

            builder.addMethod(mbuilder3.build());
        }
        if (foundSpecialTypes.contains(Constants.NON_NULLABLE_TEXT)) {
            final String strVariable = "str";
            MethodSpec.Builder mbuilder3= MethodSpec.methodBuilder(CONVERT_TO_NON_NULLABLE_TEXT);
            compilerUtil.specWithComment(mbuilder3);
            mbuilder3
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                    .addParameter(String.class, strVariable)
                    .returns(String.class)
                    .addStatement("return $S+$N.replace($S,$S)+$S", "'", strVariable, "'", "''", "'::TEXT");

            builder.addMethod(mbuilder3.build());
        }

        if (foundSpecialTypes.contains(Constants.JSON_TEXT)) {
            final String strVariable = "str";
            MethodSpec.Builder mbuilder3= MethodSpec.methodBuilder("convertToJsonTEXT");
            compilerUtil.specWithComment(mbuilder3);
            mbuilder3
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                    .addParameter(String.class, strVariable)
                    .returns(String.class)
                    .beginControlFlow("if ($N==null)", strVariable)
                    .addStatement("return $S",  "''::json")
                    .nextControlFlow("else")
                    .addStatement("return $S+$N.replace($S,$S)+$S", "'", strVariable, "'", "''", "'::json")
                    .endControlFlow();

            builder.addMethod(mbuilder3.build());
        }



        TypeSpec theLogger = builder.build();


        String myPackage=locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);

    }

    private void simpleQueryInvoker(TemplatesProjectConfiguration configs, TemplateCompilerConfig config, Set<String> foundSpecialTypes, String sbVar, MethodSpec.Builder mspec, String variableBean) {
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);
        String startCallString= Constants.INSERT_PREFIX + config.name + " (";
        compilerUtil.specWithComment(mspec);

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
                    String fun= converterForSpecialType(sqlType);
                    if (fun!=null) {
                        mspec.addStatement("$N.append($N($N.$N))", sbVar, fun, variableBean, key);
                        foundSpecialTypes.add(sqlType);
                    } else {
                        mspec.addStatement("$N.append($N.$N)", sbVar, variableBean, key);
                    }
                } else {
                    mspec.addStatement("$N.append($N.$N)", sbVar, variableBean, key);
                }
            }
        }
        String endCallString= ");";
        mspec.addStatement("$N.append($S)", sbVar, endCallString);

    }
    private void simpleQueryInvokerEmbedded(TemplatesProjectConfiguration configs, TemplateCompilerConfig config, Set<String> foundSpecialTypes, String sbVar, MethodSpec.Builder mspec, String variableBean, List<String> sharing) {
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);
        String startCallString= Constants.INSERT_PREFIX + config.name + " (";
        compilerUtil.specWithComment(mspec);



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
                if (descriptorUtils.isInput(key,bindingsSchema) || (sharing!=null && sharing.contains(key))) {
                    String comment="";
                    if (sharing!=null && sharing.contains(key)) {
                        comment="/* sharing */";
                    }
                    final String sqlType = descriptorUtils.getSqlType(key, bindingsSchema);
                    if (sqlType != null) {
                        String fun = converterForSpecialType(sqlType);
                        if (fun!=null) {
                            mspec.addStatement("$N.append($N($N.$N)) $L", sbVar, fun, variableBean, key, comment);
                            foundSpecialTypes.add(sqlType);
                        } else {
                            mspec.addStatement("$N.append($N.$N) $L", sbVar, variableBean, key, comment);
                        }
                    } else {
                        mspec.addStatement("$N.append($N.$N) $L", sbVar, variableBean, key, comment);
                    }
                } else {
                    mspec.addStatement("$N.append($S) /* output */", sbVar, "null");
                }
            }
        }
        String endCallString= ") :: " + config.name + "_type";
        mspec.addStatement("$N.append($S)", sbVar, endCallString);

    }

    public void compositeQueryInvoker(TemplatesProjectConfiguration configs, Locations locations, TemplateCompilerConfig config, Set<String> foundSpecialTypes, String sbVar, MethodSpec.Builder mspec, String variableBean, boolean withBean) {
        CompositeTemplateCompilerConfig compositeConfig=(CompositeTemplateCompilerConfig ) config;
        compilerUtil.specWithComment(mspec);

        mspec.addStatement("$N.append($S)", sbVar, "---- query invoker for  " + compositeConfig.name + "\n\n");


        mspec.addStatement("$N.append($S)", sbVar, "select * from ");

        mspec.beginControlFlow("if ($N)", linkingVar);
        String startCallString= Constants.INSERT_PREFIX + config.name + INSERT_COMPOSITE_AND_LINKER_SUFFIX +" (";
        mspec.addStatement("$N.append($S)", sbVar, startCallString);
        mspec.nextControlFlow("else");
        String startCallString2= Constants.INSERT_PREFIX + config.name + INSERT_ARRAY_SUFFIX   +" (";
        mspec.addStatement("$N.append($S)", sbVar, startCallString2);
        mspec.endControlFlow();

        mspec.addStatement("$N.append($S)", sbVar, "ARRAY[\n");
        String variableBean1=variableBean+"_1";
        mspec.addStatement("boolean first=true");

        SimpleTemplateCompilerConfig composee=null;
        for (TemplateCompilerConfig c: configs.templates) {
            if (compositeConfig.consistsOf.equals(c.name)) {
                composee=(SimpleTemplateCompilerConfig) c;
            }
        }
        mspec.beginControlFlow("for ($T $N: $N.$N)",
                                  (withBean)?ClassName.get(locations.getFilePackage(BeanDirection.COMMON), compilerUtil.commonNameClass(compositeConfig.consistsOf))
                                            :ClassName.get(locations.getFilePackage(BeanDirection.INPUTS), compilerUtil.beanNameClass(compositeConfig.consistsOf, BeanDirection.INPUTS, "_1")),
                                  variableBean1,
                                  variableBean,
                                  ELEMENTS);

        mspec.beginControlFlow("if (first)")
                .addStatement("first=false")
                .nextControlFlow("else")
                .addStatement("$N.append($S)", sbVar, ",\n     ")
                .endControlFlow();


        if (composee==null) throw new IllegalStateException("No composee found " + compositeConfig.consistsOf);

        simpleQueryInvokerEmbedded(configs,composee,foundSpecialTypes,sbVar,mspec, variableBean1,compositeConfig.sharing);

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

    public String converterForSpecialType(String specialType) {
        switch (specialType) {
            case Constants.SQL_DATE:
                return "convertToDate";
            case Constants.TIMESTAMPTZ:
                return "convertToTimestamptz";
            case Constants.NULLABLE_TEXT:
                return CONVERT_TO_NULLABLE_TEXT;
            case Constants.NON_NULLABLE_TEXT:
                return CONVERT_TO_NON_NULLABLE_TEXT;
            case Constants.JSON_TEXT:
                return "convertToJsonTEXT";
            default:
                return null;
                //throw new IllegalStateException("Unexpected value: " + specialType);
        }
    }


}