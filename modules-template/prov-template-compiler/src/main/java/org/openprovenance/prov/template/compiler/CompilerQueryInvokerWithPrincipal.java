package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Constructor;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.CompilerQueryInvoker.LINKING_VAR;
import static org.openprovenance.prov.template.compiler.CompilerQueryInvoker.SB_VAR;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.oldstuff.CompilerQueryInvokerOld.CONVERT_TO_NON_NULLABLE_TEXT;
import static org.openprovenance.prov.template.compiler.oldstuff.CompilerQueryInvokerOld.CONVERT_TO_NULLABLE_TEXT;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerQueryInvokerWithPrincipal {
    public static final String sbVar="sb";
    public static final String linkingVar="linking";
    public static final String PRINCIPAL_VAR ="principal";
    public static final String QUERY_INVOKER_VAR ="queryInvoker";

    private final CompilerUtil compilerUtil;
    private final CompilerQueryInvoker delegateCompiler;
    private final PastFactory pastFactory;


    public CompilerQueryInvokerWithPrincipal(ProvFactory pFactory, CompilerQueryInvoker delegateCompiler) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.delegateCompiler=delegateCompiler;
        this.pastFactory = compilerUtil.getPastFactory();

    }


    public SpecificationFile generateQueryInvokerWithPrincipal(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        org.openprovenance.prov.template.compiler.past.Class pastClass = pastFactory.CLASS(QUERY_INVOKER2WP)
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(org.openprovenance.prov.template.compiler.past.type.ClassName.get(INPUT_PROCESSOR, locations.getFilePackage(configs.name, INPUT_PROCESSOR)));



       // TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.QUERY_INVOKER2WP);


       // builder.addSuperinterface(ClassName.get(locations.getFilePackage(configs.name, INPUT_PROCESSOR), INPUT_PROCESSOR));

        ClassName queryInvoke2Class=ClassName.get(locations.getFilePackage(configs.name, QUERY_INVOKER2), QUERY_INVOKER2);
        // fields
        org.openprovenance.prov.template.compiler.past.type.ClassName QUERY_INVOKER2_CLASS = get(QUERY_INVOKER2, locations.getFilePackage(configs.name, QUERY_INVOKER2));
        pastClass.FIELDS(
                FIELD(SB_VAR, STRING_BUILDER).MODIFIERS(Modifier.FINAL),
                FIELD(LINKING_VAR, _bool).MODIFIERS(Modifier.FINAL),
                FIELD(PRINCIPAL_VAR, STRING).MODIFIERS(Modifier.FINAL),
                // queryInvoker field (delegate for bean processing/converters)
                FIELD(QUERY_INVOKER_VAR, QUERY_INVOKER2_CLASS).MODIFIERS(Modifier.FINAL)
        );

        Constructor c1 = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(STRING_BUILDER, SB_VAR)
                .PARAMETER(STRING, PRINCIPAL_VAR)
                .PARAMETER(QUERY_INVOKER2_CLASS, QUERY_INVOKER_VAR)
                .debugFileLocation()
                .BODY(
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), SB_VAR), VARIABLE(SB_VAR)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), LINKING_VAR), CONSTANT(false)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), PRINCIPAL_VAR), VARIABLE(PRINCIPAL_VAR)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), QUERY_INVOKER_VAR), VARIABLE(QUERY_INVOKER_VAR))
                );
        pastClass.CONSTRUCTOR(c1);

        Constructor c2 = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(STRING_BUILDER, SB_VAR)
                .PARAMETER(STRING, PRINCIPAL_VAR)
                .debugFileLocation()
                .BODY(
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), SB_VAR), VARIABLE(SB_VAR)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), LINKING_VAR), CONSTANT(false)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), PRINCIPAL_VAR), VARIABLE(PRINCIPAL_VAR)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), QUERY_INVOKER_VAR),
                                CONSTRUCTOR_CALL(QUERY_INVOKER2_CLASS, List.of(VARIABLE(SB_VAR), VARIABLE(LINKING_VAR))))
                );
        pastClass.CONSTRUCTOR(c2);

        Constructor c3 = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(STRING_BUILDER, SB_VAR)
                .PARAMETER(_bool, LINKING_VAR)
                .PARAMETER(STRING, PRINCIPAL_VAR)
                .debugFileLocation()
                .BODY(
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), SB_VAR), VARIABLE(SB_VAR)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), LINKING_VAR), VARIABLE(LINKING_VAR)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), PRINCIPAL_VAR), VARIABLE(PRINCIPAL_VAR)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), QUERY_INVOKER_VAR),
                                CONSTRUCTOR_CALL(QUERY_INVOKER2_CLASS, List.of(VARIABLE(SB_VAR), VARIABLE(LINKING_VAR))))
                );
        pastClass.CONSTRUCTOR(c3);




        Set<String> foundSpecialTypes=new HashSet<>();

        for (TemplateCompilerConfig config : configs.templates) {

            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            final String inputsNameClass = compilerUtil.inputsNameClass(config.name);
            final org.openprovenance.prov.template.compiler.past.type.ClassName inputClassName = get(inputsNameClass,locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));

            Method m = METHOD(PROCESS_METHOD_NAME)
                    .debugFileLocation()
                    .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                    .PARAMETER(inputClassName, BEAN_VAR)
                    .RETURNS(inputClassName)
                    ;


            if (config instanceof SimpleTemplateCompilerConfig) {
                simpleQueryInvoker(configs, config, foundSpecialTypes, m);

            } else {
                compositeQueryInvoker(configs, locations, config, foundSpecialTypes, sbVar, m, BEAN_VAR, false);
            }
            m.BODY(RETURN(VARIABLE(BEAN_VAR)));

            pastClass.METHOD(m);
        }

        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToBackendDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);

    }

    private void simpleQueryInvoker(TemplatesProjectConfiguration configs, TemplateCompilerConfig config, Set<String> foundSpecialTypes, Method method) {
        method.debugFileLocation();
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

        method.BODY(
                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( "WITH \n    insertion_result AS (  ")))
        );


        delegateCompiler.simpleQueryInvoker(configs, config, foundSpecialTypes, method, BEAN_VAR, QUERY_INVOKER_VAR);



        String endCallString= ")";
        method.BODY(
                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(endCallString)))
        );


        insertAccessControlSimple(config, method, bindingsSchema);


        method.BODY(
                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( ";\n")))
        );


    }

    private void insertAccessControlSimple(TemplateCompilerConfig config, Method method, TemplateBindingsSchema bindingsSchema) {

        method.BODY(
                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("\nINSERT INTO record_index(key,table_name,principal)\n"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( "VALUES ((SELECT id FROM insertion_result),\n"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(VARIABLE(QUERY_INVOKER_VAR), CONVERT_TO_NON_NULLABLE_TEXT, List.of(CONSTANT(config.name))))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(",\n"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(VARIABLE(QUERY_INVOKER_VAR), CONVERT_TO_NON_NULLABLE_TEXT, List.of(VARIABLE(PRINCIPAL_VAR))))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(")\nRETURNING (SELECT ID FROM insertion_result) as id\n")))

                );


        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (descriptorUtils.isOutput(key, bindingsSchema)) {
                method.BODY(
                        METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(","))),
                        METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("(SELECT " + key + " FROM insertion_result)")))
                );
            }
        }

    }

    private void insertAccessControlComposite(TemplateCompilerConfig config, String sbVar, Method method, SimpleTemplateCompilerConfig composee, TemplateBindingsSchema bindingsSchema) {


        method.BODY(
                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(  "insertion_result2 AS ("))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( "\n   INSERT INTO record_index(key,table_name,principal)\n"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( "   SELECT id,"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(VARIABLE(QUERY_INVOKER_VAR), CONVERT_TO_NON_NULLABLE_TEXT, List.of(CONSTANT(composee.name))))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( ","))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(VARIABLE(QUERY_INVOKER_VAR), CONVERT_TO_NON_NULLABLE_TEXT, List.of(VARIABLE(PRINCIPAL_VAR))))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( "\n   FROM insertion_result\n"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( "   returning *),\n"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("insertion_result3 AS ("))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("\n   INSERT INTO record_index(key,table_name,principal)\n"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("   SELECT distinct(parent) as key,"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(VARIABLE(QUERY_INVOKER_VAR), CONVERT_TO_NON_NULLABLE_TEXT, List.of(CONSTANT(config.name))))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(","))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(VARIABLE(QUERY_INVOKER_VAR), CONVERT_TO_NON_NULLABLE_TEXT, List.of(VARIABLE(PRINCIPAL_VAR))))),
                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("\n   from insertion_result)\n"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("select * from insertion_result\n")))


                );

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

    public void compositeQueryInvoker(TemplatesProjectConfiguration configs, Locations locations, TemplateCompilerConfig config, Set<String> foundSpecialTypes, final String sbVar, Method method, String variableBean, boolean withBean) {
        CompositeTemplateCompilerConfig compositeConfig=(CompositeTemplateCompilerConfig ) config;

        method.debugFileLocation();

        method.BODY(
                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(  "---- query invoker for  " + compositeConfig.name + " (with Principal)\n\n"))),

                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( "WITH \n    insertion_result AS (  ")))
        );


        delegateCompiler.compositeQueryInvoker(configs,locations, config, foundSpecialTypes, method, variableBean, false, QUERY_INVOKER_VAR);


        method.BODY( METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( " ), "))) );

        SimpleTemplateCompilerConfig composee = null;
        for (TemplateCompilerConfig c: configs.templates) {
            if (compositeConfig.consistsOf.equals(c.fullyQualifiedName)) {
                composee=(SimpleTemplateCompilerConfig) c;
            }
        }
        if (composee==null) throw new IllegalStateException("No composee found " + compositeConfig.consistsOf + " for composite " + compositeConfig.fullyQualifiedName);

        insertAccessControlComposite(config, sbVar, method, composee, compilerUtil.getBindingsSchema(composee));

        method.BODY(
                METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT( ";\n")))
        );

    }


    public String converterForSpecialType(String specialType) {
        switch (specialType) {
            case Constants.SQL_DATE:
                return QUERY_INVOKER_VAR + "." + "convertToDate";
            case Constants.TIMESTAMPTZ:
                return QUERY_INVOKER_VAR + "." + "convertToTimestamptz";
            case Constants.NULLABLE_TEXT:
                return QUERY_INVOKER_VAR + "." + CONVERT_TO_NULLABLE_TEXT;
            case Constants.NON_NULLABLE_TEXT:
                return QUERY_INVOKER_VAR + "." + CONVERT_TO_NON_NULLABLE_TEXT;
            case Constants.JSON_TEXT:
                return QUERY_INVOKER_VAR + "." + "convertToJsonTEXT";
            default:
                return null;
            //throw new IllegalStateException("Unexpected value: " + specialType);
        }
    }

}