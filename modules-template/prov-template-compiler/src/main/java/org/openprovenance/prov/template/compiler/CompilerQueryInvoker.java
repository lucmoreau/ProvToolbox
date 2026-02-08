package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Iterator.ITERATOR;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerQueryInvoker {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;


    public CompilerQueryInvoker(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    public SpecificationFile generateQueryInvoker(TemplatesProjectConfiguration configs, Locations locations, boolean withBean, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        String className = (withBean)? QUERY_INVOKER : QUERY_INVOKER2;

        Class pastClass = pastFactory.CLASS(className)
                .MODIFIERS(Modifier.PUBLIC);

        if (withBean) {
            pastClass.INTERFACES(get(BEAN_PROCESSOR, locations.getFilePackage(configs.name, BEAN_PROCESSOR)));
        } else {
            pastClass.INTERFACES(get(INPUT_PROCESSOR, locations.getFilePackage(configs.name, INPUT_PROCESSOR)));
        }

        pastClass.FIELDS(
                FIELD(SB_VAR, STRING_BUILDER).MODIFIERS(Modifier.FINAL),
                FIELD(LINKING_VAR, _bool).MODIFIERS(Modifier.FINAL)
        );

        Constructor c1 = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(STRING_BUILDER, SB_VAR)
                .debugFileLocation()
                .BODY(
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), SB_VAR), VARIABLE(SB_VAR)),
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), LINKING_VAR), CONSTANT(false))
                );
        pastClass.CONSTRUCTOR(c1);

        if (!withBean) {
            Constructor c2 = CONSTRUCTOR()
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(STRING_BUILDER, SB_VAR)
                    .PARAMETER(_bool, LINKING_VAR)
                    .debugFileLocation()
                    .BODY(
                            ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), SB_VAR), VARIABLE(SB_VAR)),
                            ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), LINKING_VAR), VARIABLE(LINKING_VAR))
                    );
            pastClass.CONSTRUCTOR(c2);
        }

        Set<String> foundSpecialTypes = new HashSet<>();
        foundSpecialTypes.add(NON_NULLABLE_TEXT);

        // For each template generate process method
        for (TemplateCompilerConfig config : configs.templates) {
            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            final String inputsNameClass = compilerUtil.inputsNameClass(config.name);

            ClassName beanType = get(beanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));
            ClassName inputType = get(inputsNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));

            Method m = METHOD(PROCESS_METHOD_NAME)
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER((withBean)?beanType:inputType, BEAN_VAR)
                    .RETURNS((withBean)?beanType:inputType)
                    ;

            if (config instanceof SimpleTemplateCompilerConfig) {
                simpleQueryInvoker(configs, config, foundSpecialTypes, m, BEAN_VAR, null);
            } else {
                CompositeTemplateCompilerConfig comp = (CompositeTemplateCompilerConfig) config;
            }
            m.BODY(
                    METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(";\n"))),
                    RETURN(VARIABLE(BEAN_VAR))
            );
            pastClass.METHOD(m);
        }

        addSpecialTypesMethods(foundSpecialTypes, pastClass);

        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    private void addSpecialTypesMethods(Set<String> foundSpecialTypes, Class pastClass) {
        // add special type converters as methods
        if (foundSpecialTypes.contains(TIMESTAMPTZ)) {
            Method ms = METHOD("convertToTimestamptz")
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                    .PARAMETER(STRING, "time")
                    .RETURNS(STRING)
                    .BODY(
                            IF(BINARY_OP(VARIABLE("time"),"==",Constant.getNull()))
                                    .THEN(RETURN(CONSTANT("NULL"))),

                            RETURN(
                                    METHOD_CALL(STRING,"concat",
                                            List.of(
                                                    CONSTANT("'"),
                                                    VARIABLE("time"),
                                                    CONSTANT("'::timestamptz"))))


                    );
            pastClass.METHOD(ms);
        }

        if (foundSpecialTypes.contains(SQL_DATE)) {
            Method ms2 = METHOD("convertToDate")
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                    .PARAMETER(STRING, "date")
                    .RETURNS(STRING)
                    .BODY(
                            IF(BINARY_OP(VARIABLE("date"),"==",Constant.getNull()))
                                    .THEN(RETURN(Constant.getNull())),

                            RETURN(
                                    METHOD_CALL(STRING,"concat",
                                            List.of(
                                                    CONSTANT("'"),
                                                    VARIABLE("date"),
                                                    CONSTANT("'::date"))))


                    );
            pastClass.METHOD(ms2);
        }

        if (foundSpecialTypes.contains(NULLABLE_TEXT)) {
            Method ms3 = METHOD("convertToNullableTEXT")
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                    .PARAMETER(STRING, "str")
                    .RETURNS(STRING)
                    .BODY(
                            IF(BINARY_OP(VARIABLE("str"),"==",Constant.getNull()))
                                    .THEN(RETURN(CONSTANT("''::TEXT"))),

                            RETURN(METHOD_CALL(STRING,"concat",
                                    List.of(
                                            CONSTANT("'"),
                                            METHOD_CALL(VARIABLE("str"),
                                                    "replace",
                                                    List.of(
                                                            CONSTANT("'"),
                                                            CONSTANT("''")
                                                    )),
                                            CONSTANT("'::TEXT"))))
                    );
            pastClass.METHOD(ms3);
        }

        if (foundSpecialTypes.contains(NON_NULLABLE_TEXT)) {
            Method ms4 = METHOD(CONVERT_TO_NON_NULLABLE_TEXT)
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                    .PARAMETER(STRING, "str")
                    .RETURNS(STRING)
                    .BODY(
                            RETURN(METHOD_CALL(STRING,"concat",
                                    List.of(
                                            CONSTANT("'"),
                                            METHOD_CALL(VARIABLE("str"),
                                                    "replace",
                                                    List.of(
                                                            CONSTANT("'"),
                                                            CONSTANT("''")
                                                    )),
                                            CONSTANT("'::TEXT"))))
                    );
            pastClass.METHOD(ms4);
        }

        if (foundSpecialTypes.contains(JSON_TEXT)) {
            Method ms5 = METHOD("convertToJsonTEXT")
                    .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                    .PARAMETER(STRING, "str")
                    .RETURNS(STRING)
                    .BODY(
                            IF(BINARY_OP(VARIABLE("str"),"==",Constant.getNull())).THEN(METHOD_CALL("return", List.of(CONSTANT("''::json"))))
                    );
            pastClass.METHOD(ms5);
        }
    }


    public String converterForSpecialType(String specialType) {
        return switch (specialType) {
            case Constants.SQL_DATE -> "convertToDate";
            case Constants.TIMESTAMPTZ -> "convertToTimestamptz";
            case Constants.NULLABLE_TEXT -> "convertToNullableTEXT";
            case Constants.NON_NULLABLE_TEXT -> CONVERT_TO_NON_NULLABLE_TEXT;
            case Constants.JSON_TEXT -> "convertToJsonTEXT";
            default -> null;
        };
    }

    public void simpleQueryInvoker(TemplatesProjectConfiguration configs, TemplateCompilerConfig config, Set<String> foundSpecialTypes, Method m, String beanVar, String queryInvoker) {
        TemplateBindingsSchema bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

        m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("select * from "))));
        m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(INSERT_PREFIX + config.name + " ("))));

        boolean first = true;
        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (descriptorUtils.isInput(key, bindingsSchema)) {
                if (!first) {
                    m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(","))));
                }
                first = false;
                final String sqlType = descriptorUtils.getSqlType(key, bindingsSchema);
                if (sqlType != null) {
                    String fun = converterForSpecialType(sqlType);
                    if (fun != null) {
                        if (queryInvoker == null) {
                            m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(fun, List.of(METHOD_CALL(VARIABLE(beanVar), key))))));
                        } else {
                            m.BODY(METHOD_CALL(VARIABLE(SB_VAR),
                                    "append",
                                    List.of(METHOD_CALL(VARIABLE(queryInvoker),
                                            fun,
                                            List.of(METHOD_CALL(VARIABLE(beanVar), key))))));
                        }
                        foundSpecialTypes.add(sqlType);
                    } else {
                        m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(VARIABLE(beanVar), key))));
                    }
                } else {
                    m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(VARIABLE(beanVar), key))));
                }
            }
        }

        m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(")"))));
    }

    public void simpleQueryInvokerEmbedded(TemplatesProjectConfiguration configs, TemplateCompilerConfig config, Set<String> foundSpecialTypes, Iterator m, String beanVar, List<String> sharing, String queryInvoker) {
        TemplateBindingsSchema bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);
        compilerUtil.debugFileLocation(m);
        m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("( "))));

        boolean first = true;
        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            boolean doProcess = true;
            if (!doProcess) continue;
            if (!first) {
                m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(","))));
            }
            first = false;

            if (descriptorUtils.isInput(key, bindingsSchema) || (sharing != null && sharing.contains(key))) {
                String comment = (sharing != null && sharing.contains(key)) ? "/* sharing */" : "";
                final String sqlType = descriptorUtils.getSqlType(key, bindingsSchema);
                if (sqlType != null) {
                    String fun = converterForSpecialType(sqlType);
                    if (fun != null) {
                        if (!comment.isEmpty()) m.COMMENT(comment);
                        if (queryInvoker != null) {
                            m.BODY(METHOD_CALL(VARIABLE(SB_VAR),
                                    "append",
                                    List.of(METHOD_CALL(VARIABLE(queryInvoker),
                                            fun,
                                            List.of(METHOD_CALL(VARIABLE(beanVar), key))))));
                        } else {
                            m.BODY(METHOD_CALL(VARIABLE(SB_VAR),
                                    "append",
                                    List.of(METHOD_CALL(fun, List.of(METHOD_CALL(VARIABLE(beanVar), key))))));
                        }
                        foundSpecialTypes.add(sqlType);

                    } else {
                        if (!comment.isEmpty()) m.COMMENT(comment);
                        m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(VARIABLE(beanVar), key)))) ;
                    }
                } else {
                    if (!comment.isEmpty()) m.COMMENT(comment);
                    m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(METHOD_CALL(VARIABLE(beanVar), key))));
                }
            } else {
                m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("null"))));
            }
        }
        m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(") :: " + config.name + "_type"))));
    }

    public void compositeQueryInvoker(TemplatesProjectConfiguration configs, Locations locations, TemplateCompilerConfig config, Set<String> foundSpecialTypes, Method m, String beanVar, boolean withBean, String queryInvoker) {
        CompositeTemplateCompilerConfig compositeConfig = (CompositeTemplateCompilerConfig) config;

        m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("---- query invoker for  " + compositeConfig.name + "\n\n"))))
         .BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("select * from "))));

        // if (linking) append insert_composite_and_linker else insert_composite_array
        m.BODY(IF(VARIABLE(LINKING_VAR))
                .THEN(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(INSERT_PREFIX + config.name + INSERT_COMPOSITE_AND_LINKER_SUFFIX + " ("))))
                .ELSE(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(INSERT_PREFIX + config.name + INSERT_ARRAY_SUFFIX + " (")))))
        ;

        m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("ARRAY[\n"))));

        String variableBean1 = beanVar + "_1";

        // find composee template
        SimpleTemplateCompilerConfig composee = null;
        for (TemplateCompilerConfig c: configs.templates) {
            if (compositeConfig.consistsOf.equals(c.fullyQualifiedName)) {
                composee=(SimpleTemplateCompilerConfig) c;
            }
        }
        if (composee==null) throw new IllegalStateException("No composee found " + compositeConfig.consistsOf + " for composite " + compositeConfig.fullyQualifiedName);

        String shortConsistsOf = locations.getShortNames().get(compositeConfig.consistsOf);

        // determine iterator element type
        org.openprovenance.prov.template.compiler.past.type.TypeName iterType;
        if (withBean) {
            iterType = get(compilerUtil.commonNameClass(shortConsistsOf), locations.getBeansPackage(compositeConfig.fullyQualifiedName, BeanDirection.COMMON));
        } else {
            iterType = get(compilerUtil.beanNameClass(shortConsistsOf, BeanDirection.INPUTS, "_1"), locations.getBeansPackage(compositeConfig.fullyQualifiedName, BeanDirection.INPUTS));
        }

        m.BODY(DEFINITION(_bool, VARIABLE("first"), CONSTANT(true)));

        Iterator iterator = ITERATOR(PARAMETER(variableBean1, iterType), METHOD_CALL(VARIABLE(beanVar), ELEMENTS));
        iterator.BODY(
                IF(VARIABLE("first")).THEN(
                        ASSIGNMENT(null, VARIABLE("first"), CONSTANT(false))
                ).ELSE(
                        METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT(",\n     ")))
                )
        );
        simpleQueryInvokerEmbedded(configs, composee, foundSpecialTypes, iterator, variableBean1, compositeConfig.sharing, queryInvoker);

        m.BODY(iterator);



        m.BODY(METHOD_CALL(VARIABLE(SB_VAR), "append", List.of(CONSTANT("\n])"))));

    }
}
