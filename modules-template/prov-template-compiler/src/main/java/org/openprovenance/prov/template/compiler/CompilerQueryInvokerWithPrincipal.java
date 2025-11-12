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
import java.util.Objects;
import java.util.Set;

import static org.openprovenance.prov.template.compiler.CompilerQueryInvoker.CONVERT_TO_NON_NULLABLE_TEXT;
import static org.openprovenance.prov.template.compiler.CompilerQueryInvoker.CONVERT_TO_NULLABLE_TEXT;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerQueryInvokerWithPrincipal {
    public static final String sbVar="sb";
    public static final String linkingVar="linking";
    public static final String principalVar="principal";
    public static final String queryInvokerVar ="queryInvoker";

    private final CompilerUtil compilerUtil;


    public CompilerQueryInvokerWithPrincipal(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public SpecificationFile generateQueryInvokerWithPrincipal(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.QUERY_INVOKER2WP);


        builder.addSuperinterface(ClassName.get(locations.getFilePackage(configs.name, INPUT_PROCESSOR), INPUT_PROCESSOR));

        ClassName queryInvoke2Class=ClassName.get(locations.getFilePackage(configs.name, QUERY_INVOKER2), QUERY_INVOKER2);


        builder.addField(StringBuilder.class, sbVar, Modifier.FINAL);
        builder.addField(boolean.class, linkingVar, Modifier.FINAL);
        builder.addField(String.class, principalVar, Modifier.FINAL);
        builder.addField(queryInvoke2Class, queryInvokerVar, Modifier.FINAL);

        MethodSpec.Builder cbuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(StringBuilder.class, sbVar)
                .addParameter(String.class, principalVar)
                .addStatement("this.$N = $N", sbVar, sbVar)
                .addStatement("this.$N = false", linkingVar)
                .addStatement("this.$N = $N", principalVar, principalVar)
                .addStatement("this.$N = new $T($N,$N)", queryInvokerVar, queryInvoke2Class, sbVar, linkingVar);

        builder.addMethod(cbuilder.build());


        // add an additional constructor for QUERY_INVOKER2
        MethodSpec.Builder cbuilder2 = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(StringBuilder.class, sbVar)
                .addParameter(boolean.class, linkingVar)
                .addParameter(String.class, principalVar)
                .addStatement("this.$N = $N", sbVar, sbVar)
                .addStatement("this.$N = $N", linkingVar, linkingVar)
                .addStatement("this.$N = $N", principalVar, principalVar)
                .addStatement("this.$N = new $T($N,$N)", queryInvokerVar, queryInvoke2Class, sbVar, linkingVar) ;
        builder.addMethod(cbuilder2.build());



        Set<String> foundSpecialTypes=new HashSet<>();

        for (TemplateCompilerConfig config : configs.templates) {

            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            final String inputsNameClass = compilerUtil.inputsNameClass(config.name);
            final ClassName className = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON), beanNameClass);
            final ClassName inputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS), inputsNameClass);

            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            compilerUtil.specWithComment(mspec);
            mspec
                    .addParameter(ParameterSpec.builder(inputClassName, VARIABLE_BEAN).build())
                    .returns(inputClassName);

            if (config instanceof SimpleTemplateCompilerConfig) {
                simpleQueryInvoker(configs, config, foundSpecialTypes, sbVar, mspec, VARIABLE_BEAN);
                mspec.addStatement("return $N", VARIABLE_BEAN);
            } else {
                compositeQueryInvoker(configs, locations, config, foundSpecialTypes, sbVar, mspec, VARIABLE_BEAN, false);
                mspec.addStatement("return $N", VARIABLE_BEAN);
            }

            builder.addMethod(mspec.build());
        }



        TypeSpec theLogger = builder.build();


        String myPackage=locations.getFilePackage(configs.name, fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);

    }

    private void simpleQueryInvoker(TemplatesProjectConfiguration configs, TemplateCompilerConfig config, Set<String> foundSpecialTypes, String sbVar, MethodSpec.Builder mspec, String variableBean) {
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);
        String startCallString= Constants.INSERT_PREFIX + config.name + " (";
        compilerUtil.specWithComment(mspec);

        mspec.addStatement("$N.append($S)", sbVar, "WITH \n    insertion_result AS (select * from ");
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
        String endCallString= "))";
        mspec.addStatement("$N.append($S)", sbVar, endCallString);
        insertAccessControlSimple(config, sbVar, mspec, bindingsSchema);


        mspec.addStatement("$N.append($S)", sbVar, ";\n");


    }

    private void insertAccessControlSimple(TemplateCompilerConfig config, String sbVar, MethodSpec.Builder mspec, TemplateBindingsSchema bindingsSchema) {
        mspec.addStatement("$N.append($S)", sbVar, "\nINSERT INTO record_index(key,table_name,principal)\n");
        mspec.addStatement("$N.append($S).append($S).append($N($S))",
                sbVar,
                "VALUES ((SELECT id FROM insertion_result)",
                ",\n",
                queryInvokerVar + "." + CONVERT_TO_NON_NULLABLE_TEXT,
                config.name);
        mspec.addStatement("$N.append($S).append($N($N))",
                sbVar,
                ",\n",
                queryInvokerVar + "." + CONVERT_TO_NON_NULLABLE_TEXT,
                principalVar);
        mspec.addStatement("$N.append($S)", sbVar, ")\nRETURNING (SELECT ID FROM insertion_result) as id\n");

        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (descriptorUtils.isOutput(key, bindingsSchema)) {
                Class<?> cl = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);

                mspec.addStatement("$N.append($S)", sbVar, ",");
                mspec.addStatement("$N.append($S)", sbVar, "(SELECT " + key + " FROM insertion_result)");

            }
        }

    }

    private void insertAccessControlComposite(TemplateCompilerConfig config, String sbVar, MethodSpec.Builder mspec, SimpleTemplateCompilerConfig composee, TemplateBindingsSchema bindingsSchema) {
        mspec.addStatement("$N.append($S)", sbVar, "insertion_result2 AS (");
        mspec.addStatement("$N.append($S)", sbVar, "\n   INSERT INTO record_index(key,table_name,principal)\n");
        mspec.addStatement("$N.append($S).append($S).append($N($S))",
                sbVar,
                "   SELECT id",
                ",",
                queryInvokerVar + "." + CONVERT_TO_NON_NULLABLE_TEXT,
                composee.name);
        mspec.addStatement("$N.append($S).append($N($N))",
                sbVar,
                ",",
                queryInvokerVar + "." + CONVERT_TO_NON_NULLABLE_TEXT,
                principalVar);
        mspec.addStatement("$N.append($S)", sbVar, "\n   FROM insertion_result\n");
        mspec.addStatement("$N.append($S)", sbVar, "   returning *),\n");

        mspec.addStatement("$N.append($S)", sbVar, "insertion_result3 AS (");

        mspec.addStatement("$N.append($S)", sbVar, "\n   INSERT INTO record_index(key,table_name,principal)\n");
        mspec.addStatement("$N.append($S).append($S).append($N($S))",
                sbVar,
                "   SELECT distinct(parent) as key",
                ",",
                queryInvokerVar + "." + CONVERT_TO_NON_NULLABLE_TEXT,
                config.name);
        mspec.addStatement("$N.append($S).append($N($N))",
                sbVar,
                ",",
                queryInvokerVar + "." + CONVERT_TO_NON_NULLABLE_TEXT,
                principalVar);
        mspec.addStatement("$N.append($S)", sbVar, "\n   from insertion_result)\n");

        mspec.addStatement("$N.append($S)", sbVar, "select * from insertion_result\n");


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

        mspec.addStatement("$N.append($S)", sbVar, "---- query invoker for  " + compositeConfig.name + " (with Principal)\n\n");


        mspec.addStatement("$N.append($S)", sbVar, "WITH \n    insertion_result AS (");


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
            if (compositeConfig.consistsOf.equals(c.fullyQualifiedName)) {
                composee=(SimpleTemplateCompilerConfig) c;
            }
        }

        // Luc, this seems the same as composee above.
        String shortName=locations.getShortNames().get(compositeConfig.consistsOf);

        mspec.beginControlFlow("for ($T $N: $N.$N)",
                                  (withBean)?ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON), compilerUtil.commonNameClass(shortName))
                                            :ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS), compilerUtil.beanNameClass(shortName, BeanDirection.INPUTS, "_1")),
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

        mspec.addStatement("$N.append($S)", sbVar, "])),\n");


        insertAccessControlComposite(config, sbVar, mspec, composee, compilerUtil.getBindingsSchema(composee));

        mspec.addStatement("$N.append($S)", sbVar, ";\n");


    }


    public String converterForSpecialType(String specialType) {
        switch (specialType) {
            case Constants.SQL_DATE:
                return queryInvokerVar + "." + "convertToDate";
            case Constants.TIMESTAMPTZ:
                return queryInvokerVar + "." + "convertToTimestamptz";
            case Constants.NULLABLE_TEXT:
                return queryInvokerVar + "." + CONVERT_TO_NULLABLE_TEXT;
            case Constants.NON_NULLABLE_TEXT:
                return queryInvokerVar + "." + CONVERT_TO_NON_NULLABLE_TEXT;
            case Constants.JSON_TEXT:
                return queryInvokerVar + "." + "convertToJsonTEXT";
            default:
                return null;
            //throw new IllegalStateException("Unexpected value: " + specialType);
        }
    }

}