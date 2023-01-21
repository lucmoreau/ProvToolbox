package org.openprovenance.prov.template.compiler.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.apache.commons.lang.StringEscapeUtils;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.template.compiler.CompilerSQL;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.*;

import static org.openprovenance.prov.template.compiler.CompilerUtil.*;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.expander.ExpandUtil.isVariable;

public class CompilerCommon {
    private final CompilerUtil compilerUtil=new CompilerUtil();
    private final ProvFactory pFactory;

    private final boolean debugComment=true;
    private final CompilerSQL compilerSQL;

    public CompilerCommon(ProvFactory pFactory, CompilerSQL compilerSQL) {
        this.pFactory=pFactory;
        this.compilerSQL=compilerSQL;
    }


    public TypeSpec.Builder generateClassInit(String builderName, String builderPackage, String processorName, String supername) {
        return TypeSpec.classBuilder(builderName)
                .addSuperinterface(ClassName.get(builderPackage,supername))
                .addSuperinterface(ClassName.get(builderPackage,"SQL"))
                //.addSuperinterface(ClassName.get(processorPackage,processorName))  // implements Processor Interface
                .addModifiers(Modifier.PUBLIC);
    }

    public Pair<JavaFile, Map<Integer, List<Integer>>> generateCommonLib(Document doc, String name, String templateName, String packge, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed, String logger, BeanKind beanKind) {


        Bundle bun=u.getBundle(doc).get(0);

        Set<QualifiedName> allVars=new HashSet<>();
        Set<QualifiedName> allAtts=new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);


        return generateCommonLib_aux(allVars,allAtts,name, templateName, packge, bindings_schema, bindingsSchema, indexed, logger, beanKind);

    }


    Pair<JavaFile, Map<Integer, List<Integer>>> generateCommonLib_aux(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema, IndexedDocument indexed, String logger, BeanKind beanKind) {

        TypeSpec.Builder builder = generateClassInit(name, Constants.CLIENT_PACKAGE, compilerUtil.processorNameClass(templateName), Constants.BUILDER);


        Map<Integer, List<Integer>> successorTable=null;


        System.out.println("/// name " + name);
        System.out.println("/// templateName " + templateName);


        builder.addMethod(generateNameAccessor(templateName));
        builder.addMethod(generatePropertyOrderMethod());

        builder.addField(generateFieldPropertyOrder(bindingsSchema));
        builder.addMethod(generateCommonMethodGetNodes(beanKind));
        builder.addMethod(generateCommonMethodGetSuccessors(beanKind));
        builder.addMethod(generateCommonMethodGetTypedSuccessors(beanKind));
        builder.addMethod(generateRecordCsvProcessorMethod(beanKind));
        compilerSQL.generateSQLstatements(builder, templateName, bindingsSchema, beanKind);


        if (bindings_schema!=null) {
           // builder.addMethod(generateClientMethod(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateCommonCSVConverterMethod_aux(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindingsSchema));
            builder.addMethod(generateCommonSQLConverterMethod_aux(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindings_schema));
            builder.addMethod(generateArgsToRecordMethod(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindings_schema));
            builder.addMethod(generateProcessorConverter(templateName, packge, bindingsSchema, BeanKind.COMMON));
            builder.addMethod(generateProcessorConverter2(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindings_schema));
            builder.addMethod(generateApplyMethod(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindings_schema));


            builder.addMethod(generateCommonMethod2(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateCommonMethod2PureCsv(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateCommonMethod3static(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateCommonMethod4static(allVars, allAtts, name, templateName, bindings_schema, indexed));
            final Pair<MethodSpec, Map<Integer, List<Integer>>> methodSpecMapPair = generateCommonMethod5static(allVars, allAtts, name, templateName, bindings_schema, indexed);
            builder.addMethod(methodSpecMapPair.getLeft());
            successorTable=methodSpecMapPair.getRight();

            builder.addMethod(generateCommonMethod6static(allVars, allAtts, name, templateName, bindings_schema, indexed));

            builder.addField(generateFieldOutputs(allVars,allAtts,name,templateName,packge, bindingsSchema));
            builder.addField(generateFieldInputs(allVars,allAtts,name,templateName,packge, bindingsSchema));
            builder.addField(generateFieldCompulsoryInputs(allVars,allAtts,name,templateName,packge, bindingsSchema));

            builder.addField(generateField4aBeanConverter(allVars,allAtts,name,templateName,packge, bindings_schema));
            builder.addField(generateField4aBeanConverter2(templateName,packge));
            builder.addField(generateField4aSQLConverter2(allVars,allAtts,name,templateName,packge, bindings_schema));
            builder.addField(generateField4aArgs2CsvConverter(allVars,allAtts,name,templateName,packge, bindings_schema));
            builder.addField(generateField4aRecord2SqlConverter(allVars,allAtts,name,templateName,packge, bindings_schema));
            builder.addField(generateField4aRecord2CsvConverter(allVars,allAtts,name,templateName,packge, bindings_schema));

            builder.addField(FieldSpec.builder(mapIntArrayType, "__successors")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("__getSuccessors()")
                    .build());
            builder.addField(FieldSpec.builder(mapIntArrayType, "__successors2")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("__getTypedSuccessors()")
                    .build());
            builder.addField(FieldSpec.builder(int[].class, __NODES_FIELD)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("__getNodes()")
                    .build());


            builder.addMethod(generateOutputsMethod());
            builder.addMethod(generateCompulsoryInputsMethod());
            builder.addMethod(generateInputsMethod());

            builder.addMethod(generateFactoryMethodToBeanWithArray(templateName, packge, bindings_schema, bindingsSchema, beanKind));


            //      builder.addMethod(generateFactoryMethodWithArray(allVars, allAtts, name, bindings_schema));
            builder.addMethod(generateFactoryMethodWithBean(templateName, packge, bindings_schema, bindingsSchema));

            builder.addMethod(generateNewBean(allVars, allAtts, name, templateName, packge, bindings_schema));
            builder.addMethod(generateExamplarBean(allVars, allAtts, name, templateName, packge, bindings_schema));

            ClassName integratorClassName = ClassName.get(packge + ".integrator", compilerUtil.integratorBuilderNameClass(templateName));
            builder.addField(FieldSpec.builder(integratorClassName, "__integrator")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new $T()", integratorClassName)
                    .build());
            builder.addMethod(MethodSpec
                    .methodBuilder("getIntegrator")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addComment("Generated by method $N", getClass().getName()+".generateCommonLib_aux()")
                    .returns(integratorClassName)
                    .addStatement("return __integrator")
                    .build());


            builder.addMethod(compilerSQL.generateCommonSQLMethod2(allVars, allAtts, name, templateName, bindings_schema, bindingsSchema));

        } else {
            builder.addField(generateField4aBeanConverter3(templateName,packge));
            builder.addMethod(generateFactoryMethodToBeanWithArrayComposite(templateName, packge, bindingsSchema, logger, beanKind));

        }




        TypeSpec bean=builder.build();

        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N.generateCommonLib_aux()) for template $N",getClass().getName(), templateName)
                .build();

        return Pair.of(myfile,successorTable);
    }


    public MethodSpec generateNameAccessor(String templateName) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getName")
                .addModifiers(Modifier.PUBLIC)
                //.addAnnotation(Override.class)

                .returns(String.class);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateNameAccessor()");
        builder.addStatement("return $S", templateName);
        return builder.build();
    }

    public MethodSpec generateCommonMethod2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        return generateCommonMethod2(allVars,allAtts,name,template,bindings_schema,true);
    }
    public MethodSpec generateCommonMethod2PureCsv(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        return generateCommonMethod2(allVars,allAtts,name,template,bindings_schema,false);
    }

    public MethodSpec generateCommonCSVConverterMethod_aux(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String loggerName, String packge, TemplateBindingsSchema bindingsSchema) {
        final TypeName processorClassName = processorClassType(template, packge,ClassName.get(String.class));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.ARGS_CSV_CONVERSION_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(processorClassName);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateCommonCSVConverterMethod_aux()");

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add(loggerName + " client side logging method\n");
        jdoc.add("@return $T\n" , processorClassNameNotParametrised);
        builder.addJavadoc(jdoc.build());


        builder.addStatement("$T $N=this", ClassName.get(packge,name), "self");

        Map<String, List<Descriptor>> var = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        StringBuilder args = new StringBuilder();
        StringBuilder args2 = new StringBuilder();

        boolean first=true;

        for (String key: variables) {
            String newkey = "__" + key;
            if (first) {
                first=false;
            } else {
                args.append(", ");
                args2.append(", ");
            }
            args.append(compilerUtil.getJavaTypeForDeclaredType(var, key).getName()).append(" ").append(newkey);
            args2.append(" ").append(newkey);

        }

        builder.addStatement("return (" + args + ") -> { $T sb=new $T();$N.$N(sb," + args2 + "); return sb.toString(); }", StringBuffer.class,StringBuffer.class, "self",loggerName);

        MethodSpec method = builder.build();

        return method;
    }


    private FieldSpec generateField4aBeanConverter(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {
        TypeName myType=processorClassType(templateName,packge,ClassName.get(packge,compilerUtil.beanNameClass(templateName)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, Constants.A_ARGS_BEAN_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);

        JsonNode the_var = bindings_schema.get("var");

        String args = "";
        String args2 = "";

        boolean first=true;
        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            String newkey = "__" + key;
            if (first) {
                first=false;
            } else {
                args = args + ", ";
                args2 = args2 + ", ";
            }
            args = args +  compilerUtil.getJavaTypeForDeclaredType(the_var, key).getName() + " " + newkey;
            args2=args2+ " " + newkey;

        }
        fbuilder.initializer(" (" + args + ") -> { return $N(" + args2 + "); }", "toBean");

        return fbuilder.build();
    }

    private FieldSpec generateFieldPropertyOrder(TemplateBindingsSchema bindingsSchema) {

        FieldSpec.Builder fbuilder=FieldSpec.builder(ArrayTypeName.of(String.class), Constants.PROPERTY_ORDER, Modifier.PUBLIC, Modifier.STATIC);

        String args = "";
        String args2 = "new String[] { $S ";

        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        for (String key: variables) {

            args2 = args2 + ", ";

            args2=args2+ " " + "\""+ key+"\"";

        }
        fbuilder.initializer(args2 + "}", IS_A);

        return fbuilder.build();
    }

    private FieldSpec generateFieldOutputs(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, TemplateBindingsSchema bindingsSchema) {

        FieldSpec.Builder fbuilder=FieldSpec.builder(ArrayTypeName.of(String.class), Constants.OUTPUTS, Modifier.PUBLIC, Modifier.STATIC);

        Map<String, List<Descriptor>> var = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        String args = "new String[] { ";

        boolean first=true;

        for (String key: variables) {
            if (descriptorUtils.isOutput(key,bindingsSchema)) {
                if (first) {
                    first = false;
                } else {
                    args = args + ", ";
                }
                args = args + " " + "\"" + key + "\"";
            }
        }
        fbuilder.initializer(args + "}");

        return fbuilder.build();
    }
    private FieldSpec generateFieldInputs(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, TemplateBindingsSchema bindingsSchema) {

        FieldSpec.Builder fbuilder=FieldSpec.builder(ArrayTypeName.of(String.class), Constants.INPUTS, Modifier.PUBLIC, Modifier.STATIC);

        Map<String, List<Descriptor>> var = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        String args = "new String[] { ";

        boolean first=true;

        for (String key: variables) {
            if (descriptorUtils.isInput(key,bindingsSchema)) {
                if (first) {
                    first = false;
                } else {
                    args = args + ", ";
                }
                args = args + " " + "\"" + key + "\"";
            }
        }
        fbuilder.initializer(args + "}");

        return fbuilder.build();
    }
    private FieldSpec generateFieldCompulsoryInputs(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, TemplateBindingsSchema bindingsSchema) {

        FieldSpec.Builder fbuilder=FieldSpec.builder(ArrayTypeName.of(String.class), Constants.COMPULSORY_INPUTS, Modifier.PUBLIC, Modifier.STATIC);

        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        String args = "new String[] { ";

        boolean first=true;

        for (String key: variables) {
            if (descriptorUtils.isCompulsoryInput(key,bindingsSchema)) {
                if (first) {
                    first = false;
                } else {
                    args = args + ", ";
                }
                args = args + " " + "\"" + key + "\"";
            }
        }
        fbuilder.initializer(args + "}");

        return fbuilder.build();
    }





    private FieldSpec generateField4aBeanConverter3(String templateName, String packge) {
        TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, Constants.RECORDS_PROCESSOR_INTERFACE),ClassName.get(packge,compilerUtil.beanNameClass(templateName)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, Constants.A_RECORD_BEAN_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer(" ($T records) -> { return $N(records); }", listOfArrays, "toBean");

        return fbuilder.build();
    }


    private FieldSpec generateField4aBeanConverter2(String templateName, String packge) {
        TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE),ClassName.get(packge,compilerUtil.beanNameClass(templateName)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, Constants.A_RECORD_BEAN_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer(" (Object [] record) -> { return $N(record); }", "toBean");

        return fbuilder.build();
    }

    private FieldSpec generateField4aSQLConverter2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {

        final TypeName processorClassName = processorClassType(templateName, packge, ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(processorClassName, Constants.A_BEAN_SQL_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer("$N()", Constants.BEAN_SQL_CONVERSION_METHOD);

        return fbuilder.build();
    }


    private FieldSpec generateField4aArgs2CsvConverter(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {

        final TypeName processorClassName = processorClassType(templateName, packge, ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(processorClassName, Constants.A_ARGS_CSV_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer("$N()", Constants.ARGS_CSV_CONVERSION_METHOD);

        return fbuilder.build();
    }


    private FieldSpec generateField4aRecord2SqlConverter(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {

        TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, Constants.A_RECORD_SQL_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer(" (Object [] record) -> { return $N(record).process($N); }", "toBean", Constants.A_BEAN_SQL_CONVERTER);

        return fbuilder.build();
    }


    private FieldSpec generateField4aRecord2CsvConverter(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {

        TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, Constants.A_RECORD_CSV_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer(" (Object [] record) -> { return $N(record).process($N); }", "toBean", Constants.A_ARGS_CSV_CONVERTER);

        return fbuilder.build();
    }




    public MethodSpec generateCommonSQLConverterMethod_aux(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String loggerName, String packge, JsonNode bindings_schema) {
        final TypeName processorClassName = processorClassType(template, packge, ClassName.get(String.class));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.BEAN_SQL_CONVERSION_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(processorClassName);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateCommonSQLConverterMethod_aux()");

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add(loggerName + " client side logging method\n");
        jdoc.add("@return $T\n" , processorClassNameNotParametrised);
        builder.addJavadoc(jdoc.build());

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");
        String var = "";
        builder.addStatement("$T $N=this", ClassName.get(packge,name), "self");

        String args = "";
        String args2 = "";

        boolean first=true;
        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            String newkey = "__" + key;
            if (first) {
                first=false;
            } else {
                args = args + ", ";
                args2 = args2 + ", ";
            }
            args = args +  compilerUtil.getJavaTypeForDeclaredType(the_var, key).getName() + " " + newkey;
            args2=args2+ " " + newkey;

        }

        //ATTEMPTED
        //CodeBlock.Builder body = CodeBlock.builder();
        //body.addStatement("$N.$N(sb," + args2 + " )",  "self","sqlTuple");
        //body.addStatement("return sb.toString()");

        //CodeBlock.Builder returnLambda=CodeBlock.builder();
        //returnLambda.add("return (" + args + ") -> ").add(body.build());
        //builder.addStatement(returnLambda.build());



       builder.addStatement("return (" + args + ") -> { $T sb=new $T(); $N.$N(sb," + args2 + "); return sb.toString(); }", StringBuffer.class, StringBuffer.class, "self","sqlTuple");



        MethodSpec method = builder.build();

        return method;
    }



    public MethodSpec generateArgsToRecordMethod(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String loggerName, String packge, JsonNode bindings_schema) {
        final TypeName processorClassName = processorClassType(template, packge, ArrayTypeName.of(Object.class));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.ARGS2RECORD_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .returns(processorClassName);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateArgsToRecordMethod()");

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Returns a converter from arguments to record\n");
        jdoc.add("@return $T\n" , processorClassNameNotParametrised);
        builder.addJavadoc(jdoc.build());

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");
        String var = "";

        String args = "";
        String args2 = "";

        boolean first=true;
        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            String newkey = "__" + key;
            if (first) {
                first=false;
            } else {
                args = args + ", ";
                args2 = args2 + ", ";
            }
            args = args +  compilerUtil.getJavaTypeForDeclaredType(the_var, key).getName() + " " + newkey;
            args2=args2+ " " + newkey;

        }



        builder.addStatement("return (" + args + ") -> {  return new Object [] { getName(), " + args2 + "}; }");



        MethodSpec method = builder.build();

        return method;
    }

    public MethodSpec generateProcessorConverter(String template, String packge, TemplateBindingsSchema bindingsSchema, BeanKind beanKind) {

        final TypeName returnClassName= beanKind==BeanKind.COMMON ? processorClassType(template, packge, typeT) : integratorClassType(template, packge, typeT);

        final TypeName returnClassNameNotParametrised = beanKind==BeanKind.COMMON ? processorClassType(template, packge): integratorClassType (template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PROCESSOR_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(returnClassName);
        if (debugComment)
            builder.addComment("Generated by method $N", getClass().getName() + ".generateProcessorConverter()");

        TypeName parameterType = ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE), TypeVariableName.get("T"));


        String processor = compilerUtil.generateNewNameForVariable("processor");
        builder.addParameter(parameterType, processor, Modifier.FINAL);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Returns a converter from Processor taking arguments to Processor taking record\n");
        jdoc.add("@param $N a transformer for this template\n", processor);
        jdoc.add("@param <T> type variable for the result of processor\n");
        jdoc.add("@return $T&lt;$T&gt;\n", returnClassNameNotParametrised, TypeVariableName.get("T"));
        builder.addJavadoc(jdoc.build());

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();

        StringBuilder args = new StringBuilder();
        StringBuilder args2 = new StringBuilder();

        boolean first = true;
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);

        for (String key : fieldNames) {
            String newKey = compilerUtil.generateNewNameForVariable(key);
            boolean isOutput=descriptorUtils.isOutput(key,bindingsSchema);
            if (beanKind==BeanKind.COMMON || !isOutput) {
                if (first) {
                    first = false;
                } else {
                    args.append(", ");
                }
                args.append(compilerUtil.getJavaTypeForDeclaredType(theVar, key).getName()).append(" ").append(newKey);
            }
        }
        first = true;

        for (String key : fieldNames) {
            String newKey = compilerUtil.generateNewNameForVariable(key);
            boolean isOutput=descriptorUtils.isOutput(key,bindingsSchema);
            if (first) {
                first = false;
            } else {
                args2.append(", ");
            }
            if (beanKind!=BeanKind.COMMON && isOutput) {
                args2.append(" null");
            } else {
                args2.append(" ").append(newKey);
            }

        }

        builder.addStatement("return ($L) -> {  return $N.process(new Object [] { getName(), $L}); }", args, processor, args2);

        return builder.build();
    }

    public MethodSpec generateProcessorConverter2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String loggerName, String packge, JsonNode bindings_schema) {
        final TypeName processorClassName = processorClassType(template, packge, TypeVariableName.get("T"));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        TypeName returnType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE),TypeVariableName.get("T"));
        TypeName returnTypeNotParametrised =ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PROCESSOR_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(returnType);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateProcessorConverter2()");



        builder.addParameter(processorClassName, "processor", Modifier.FINAL);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Returns a converter from Processor taking arguments to Processor taking record\n");
        jdoc.add("@param processor a transformer for this template\n");
        jdoc.add("@param <T> type variable for the result of processor\n");
        jdoc.add("@return $T&lt;$T&gt;\n" , returnTypeNotParametrised, TypeVariableName.get("T"));
        builder.addJavadoc(jdoc.build());

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");
        String var = "";

        String args = "";
        String args2 = "";
        int count=1;

        boolean first=true;
        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            String newkey = compilerUtil.generateNewNameForVariable(key);
            if (first) {
                first=false;
            } else {
                args = args + ", ";
                args2 = args2 + ", ";
            }

            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(the_var, key);
            final String type=declaredJavaType.getName();
            final String converter = compilerUtil.getConverterForDeclaredType(declaredJavaType);
            final String converter2 = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
            String expression;
            if (converter2 == null) {
                expression = "(" + type + ") record[" + count + "]";
            } else {
                //expression = "(record[" + count + "]==null)?0:" + converter2 + "((String)(record[" + count + "]))";
                expression= "(record[" + count + "]==null)?null:((record[" + count + "] instanceof String)?" + converter2 + "((String)(record[" + count + "])):(" + type + ")(record[" + count + "]))";

            }
            args = args + type + " " + newkey;
            args2=args2+ " " + expression;
            count ++;

        }



        builder.addStatement("return (Object [] record) -> {  return processor.process(" + args2 + "); }");



        MethodSpec method = builder.build();

        return method;
    }

    public MethodSpec generateApplyMethod(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String loggerName, String packge, JsonNode bindings_schema) {
        final TypeName processorClassName = processorClassType(template, packge, TypeVariableName.get("T"));

        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("apply")
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(TypeVariableName.get("T"));
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateApplyMethod()");

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Apply method\n");
        jdoc.add("@param processor a transformer for this template\n");
        jdoc.add("@param record as an array of Objects\n");
        jdoc.add("@param <T> type variable for the result of processor\n");
        jdoc.add("@return an object of type $T\n" , TypeVariableName.get("T"));
        builder.addJavadoc(jdoc.build());

        final String var_processor = "processor";
        final String var_record = "record";
        builder.addParameter(processorClassName, var_processor);
        builder.addParameter(ArrayTypeName.of(Object.class), var_record);
        builder.addStatement("return toBean($N).process($N)", var_record, var_processor);
        MethodSpec method = builder.build();

        return method;
    }

    private TypeName processorClassType(String template, String packge, TypeVariableName t) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),t);
        return name;
    }
    private TypeName integratorClassType(String template, String packge, TypeVariableName t) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.integratorNameClass(template)),t);
        return name;
    }

    private TypeName processorClassType(String template, String packge, ParameterizedTypeName parameterizedTypeName) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),parameterizedTypeName);
        return name;
    }

    private TypeName processorClassType(String template, String packge, ArrayTypeName arrayTypeName) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),arrayTypeName);
        return name;
    }
    private TypeName processorClassType(String template, String packge, ClassName cl) {
        ParameterizedTypeName name=ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.processorNameClass(template)),cl);
        return name;
    }

    private TypeName processorClassType(String template, String packge) {
        return ClassName.get(packge,compilerUtil.processorNameClass(template));
    }
    private TypeName integratorClassType(String template, String packge) {
        return ClassName.get(packge,compilerUtil.integratorNameClass(template));
    }


    public MethodSpec generateCommonMethod2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema, boolean legacy) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(compilerUtil.loggerName(template) + (legacy ? "_impure" : ""))
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        String var = "sb";

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateCommonMethod2()");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        builder.addParameter(StringBuffer.class, var);
        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            String newkey = compilerUtil.generateNewNameForVariable(key);
            builder.addParameter(compilerUtil.getJavaTypeForDeclaredType(the_var, key), newkey);
        }


        iter = the_var.fieldNames();

        String constant = (legacy? "[" : "") + StringEscapeUtils.escapeCsv(template);
        while (iter.hasNext()) {
            String key = iter.next();
            final String newName = compilerUtil.generateNewNameForVariable(key);
            final Class<?> clazz = compilerUtil.getJavaTypeForDeclaredType(the_var, key);
            final boolean isQualifiedName = the_var.get(key).get(0).get("@id") != null;

            constant = constant + ',';
            builder.addStatement("$N.append($S)", var, constant);
            constant = "";

            if (String.class.equals(clazz)) {
                String myStatement = "$N.append($N)";
                String myEscapeStatement = "$N.append($T.escapeCsv($N))";
                boolean doEscape=false;
                if (!isQualifiedName) {
                    doEscape = the_var.get(key).get(0).get(0).get("@escape") != null;
                    if (doEscape) {
                        foundEscape=true;
                    }
                }
                builder.beginControlFlow("if ($N==null)", newName);
                if (legacy) {
                    builder.addStatement("$N.append($N)", var, newName);  // in legacy format, we insert a null
                }
                builder.nextControlFlow("else");

                if (doEscape) {
                    builder.addStatement(myEscapeStatement, var, ClassName.get("org.openprovenance.apache.commons.lang", "StringEscapeUtils"), newName);
                } else {
                    builder.addStatement(myStatement, var, newName);
                }
                builder.endControlFlow();
            } else {
                builder.beginControlFlow("if ($N==null)", newName);
                builder.nextControlFlow("else");
                builder.addStatement("$N.append($S)", var, constant);
                builder.addStatement("$N.append($N)", var, newName);
                builder.endControlFlow();
            }
        }
        constant = constant + (legacy ? ']' : "");
        builder.addStatement("$N.append($S)", var, constant);


        MethodSpec method = builder.build();

        return method;
    }

    public boolean getFoundEscape() {
        return foundEscape;
    }

    private boolean foundEscape=false;

    public MethodSpec generateCommonMethod3static(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getNodes")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(int[].class);
        String var = "sb";
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateCommonMethod3static()");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        Iterator<String> iter = the_var.fieldNames();

        int count = 0;
        List<Integer> ll = new LinkedList<Integer>();
        while (iter.hasNext()) {
            count++;
            String key = iter.next();
            if (the_var.get(key).get(0).get("@id") != null) {
                ll.add(count);
            }

        }

        String nodes = "";
        boolean first = true;
        for (int elem : ll) {
            if (first) {
                first = false;
            } else {
                nodes = nodes + ", ";
            }
            nodes = nodes + elem;
        }

        builder.addStatement("return new int[] {" + nodes + "}");


        MethodSpec method = builder.build();

        return method;
    }

    public MethodSpec generateCommonMethodGetNodes(BeanKind beanKind) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getNodes")
                .addModifiers(Modifier.PUBLIC)
                .returns(int[].class);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateCommonMethodGetNodes()");

        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(builder);
        } else {
            builder.addStatement("return $N", __NODES_FIELD);
        }


        MethodSpec method = builder.build();

        return method;
    }

    static public void generateUnsupportedException(MethodSpec.Builder builder) {
        builder.addStatement("throw new $T()", UnsupportedOperationException.class);
    }

    TypeName myType=ParameterizedTypeName.get(ClassName.get(Constants.CLIENT_PACKAGE, Constants.PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));

    public MethodSpec generatePropertyOrderMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.PROPERTY_ORDER_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);
        if (debugComment) builder5.addComment("Generated by method $N", getClass().getName()+".generatePropertyOrderMethod()");
        builder5.addStatement("return $N", Constants.PROPERTY_ORDER);
        return builder5.build();
    }
    public MethodSpec generateOutputsMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.OUTPUTS_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);
        if (debugComment) builder5.addComment("Generated by method $N", getClass().getName()+".generateOutputsMethod()");
        builder5.addStatement("return $N", Constants.OUTPUTS);
        return builder5.build();
    }
    public MethodSpec generateCompulsoryInputsMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.COMPULSORY_INPUTS_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);
        if (debugComment) builder5.addComment("Generated by method $N", getClass().getName()+".generateCompulsoryInputsMethod()");
        builder5.addStatement("return $N", Constants.COMPULSORY_INPUTS);
        return builder5.build();
    }
    public MethodSpec generateInputsMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.INPUTS_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class);
        if (debugComment) builder5.addComment("Generated by method $N", getClass().getName()+".generateInputsMethod()");
        builder5.addStatement("return $N", Constants.INPUTS);
        return builder5.build();
    }
    public MethodSpec generateRecordCsvProcessorMethod(BeanKind beanKind) {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(Constants.RECORD_CSV_PROCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ArrayTypeName.of(Object.class),"record").build())
                .returns(myType);
        if (debugComment) builder5.addComment("Generated by method $N", getClass().getName()+".generateRecordCsvProcessorMethod()");
        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(builder5);
        } else {
            builder5.addStatement("return $N", Constants.A_RECORD_CSV_CONVERTER);
        }
        return builder5.build();
    }
    public MethodSpec generateCommonMethodGetSuccessors(BeanKind beanKind) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getSuccessors")
                .addModifiers(Modifier.PUBLIC)
                .returns(mapIntArrayType);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateCommonMethodGetSuccessors()");

        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(builder);
        } else {
            builder.addStatement("return __successors");
        }
        return builder.build();
    }
    public MethodSpec generateCommonMethodGetTypedSuccessors(BeanKind beanKind) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getTypedSuccessors")
                .addModifiers(Modifier.PUBLIC)
                .returns(mapIntArrayType);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateCommonMethodGetTypedSuccessors()");

        if (beanKind.equals(BeanKind.COMPOSITE)) {
            generateUnsupportedException(builder);
        } else {
            builder.addStatement("return __successors2");
        }

        return builder.build();
    }

    public MethodSpec generateCommonMethod4static(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema, IndexedDocument indexed) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getSuccessors")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mapIntArrayType);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateCommonMethod4static()");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");


        Iterator<String> iter2 = the_var.fieldNames();
        int count2 = 0;
        HashMap<QualifiedName, Integer> index = new HashMap<>();
        while (iter2.hasNext()) {
            count2++;
            String key = iter2.next();
            for (QualifiedName qn : allVars) {
                if (key.equals(qn.getLocalPart())) {
                    index.put(qn, count2);
                }
            }
        }

        builder.addStatement("$T table = new $T()", mapIntArrayType, CompilerUtil.hashmapType);

        Iterator<String> iter = the_var.fieldNames();

        int count = 0;

        while (iter.hasNext()) {
            count++;
            String key = iter.next();
            if (the_var.get(key).get(0).get("@id") != null) {


                Set<QualifiedName> successors = new HashSet<>();
                for (QualifiedName qn : allVars) {
                    if (key.equals(qn.getLocalPart())) {
                        successors = indexed.traverseDerivations(qn);
                        break;
                    }
                }
                String initializer = "";
                boolean first = true;
                for (QualifiedName successor : successors) {
                    int i = index.get(successor);
                    if (first) {
                        first = false;
                    } else {
                        initializer = initializer + ", ";
                    }
                    initializer = initializer + i;
                }

                builder.addStatement("table.put($L,new int[] { " + initializer + "})", count);

            }

        }


        builder.addStatement("return table");


        MethodSpec method = builder.build();

        return method;
    }

    public void calculateTypedSuccessors(Set<QualifiedName> allVars, JsonNode bindings_schema, IndexedDocument indexed,
                                         Map<String, Set<Pair<QualifiedName, WasDerivedFrom>>>   successors1,
                                         Map<String, Set<Pair<QualifiedName, WasAttributedTo>>>  successors2,
                                         Map<String, Set<Pair<QualifiedName, HadMember>>>        successors3,  // pure unqualified relation
                                         Map<String, Set<Pair<QualifiedName, QualifiedHadMember>>>        successors3b, // pure qualified relation
                                         Map<String, Set<Pair<QualifiedName, SpecializationOf>>> successors4) {
        JsonNode the_var = bindings_schema.get("var");
        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            if (compilerUtil.isVariableDenotingQualifiedName(key,the_var)) {
                for (QualifiedName qn : allVars) {
                    if (key.equals(qn.getLocalPart())) {
                        final Set<Pair<QualifiedName, WasDerivedFrom>> pairs1 = indexed.traverseDerivationsWithRelations(qn);
                        if (!pairs1.isEmpty()) successors1.put(key, pairs1);
                        final Set<Pair<QualifiedName, WasAttributedTo>> pairs2 = indexed.traverseAttributionsWithRelations(qn);
                        if (!pairs2.isEmpty()) successors2.put(key, pairs2);
                        final Set<Pair<QualifiedName, HadMember>> pairs3 = indexed.traverseReverseMembershipsWithRelations(qn); // note Reverse relation
                        if (!pairs3.isEmpty()) {
                            for (Pair<QualifiedName, HadMember> pair3 : pairs3) {
                                if (pair3.getRight() instanceof QualifiedHadMember) {
                                    successors3b.computeIfAbsent(key, k -> new HashSet<>());
                                    successors3b.get(key).add(Pair.of(pair3.getLeft(),(QualifiedHadMember)pair3.getRight()));
                                } else {
                                    successors3.computeIfAbsent(key, k -> new HashSet<>());
                                    successors3.get(key).add(pair3);
                                }
                            }
                        }
                        final Set<Pair<QualifiedName, SpecializationOf>> pairs4 = indexed.traverseSpecializationsWithRelations(qn); // note Reverse relation
                        if (!pairs4.isEmpty()) successors4.put(key, pairs4);
                        break;
                    }
                }
            }
        }
    }

    public Map<String, Set<Pair<QualifiedName, WasDerivedFrom>>> getSuccessors1() {
        return successors1;
    }

    public Map<String, Set<Pair<QualifiedName, WasAttributedTo>>> getSuccessors2() {
        return successors2;
    }

    public Map<String, Set<Pair<QualifiedName, HadMember>>> getSuccessors3() {
        return successors3;
    }
    public Map<String, Set<Pair<QualifiedName, QualifiedHadMember>>> getSuccessors3b() {
        return successors3b;
    }
    public Map<String, Set<Pair<QualifiedName, SpecializationOf>>> getSuccessors4() {
        return successors4;
    }

    Map<String,Set<Pair<QualifiedName, WasDerivedFrom>>>  successors1 = new HashMap<>();
    Map<String,Set<Pair<QualifiedName, WasAttributedTo>>> successors2 = new HashMap<>();
    Map<String,Set<Pair<QualifiedName, HadMember>>>       successors3 = new HashMap<>();
    Map<String,Set<Pair<QualifiedName, QualifiedHadMember>>> successors3b = new HashMap<>();
    Map<String,Set<Pair<QualifiedName, SpecializationOf>>>successors4 = new HashMap<>();


    public Pair<MethodSpec, Map<Integer, List<Integer>>> generateCommonMethod5static(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema, IndexedDocument indexed) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getTypedSuccessors")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mapIntArrayType);

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateCommonMethod5static()");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        /* Note make sure you re initialise those, as thre is a single compile client created for all templates in a config. */

        successors1 = new HashMap<>();
        successors2 = new HashMap<>();
        successors3 = new HashMap<>();
        successors3b = new HashMap<>();
        successors4 = new HashMap<>();

        calculateTypedSuccessors(allVars,bindings_schema,indexed, successors1,successors2,successors3,successors3b,successors4);


        Iterator<String> iter2 = the_var.fieldNames();
        int count2 = 0;
        HashMap<QualifiedName, Integer> index = new HashMap<>();
        while (iter2.hasNext()) {
            count2++;
            String key = iter2.next();
            for (QualifiedName qn : allVars) {
                if (key.equals(qn.getLocalPart())) {
                    index.put(qn, count2);
                }
            }
        }

        builder.addStatement("$T table = new $T()", mapIntArrayType, CompilerUtil.hashmapType);

        Iterator<String> iter = the_var.fieldNames();

        int count = 0;

        Map<Integer,List<Integer>> tableValues=new HashMap<>();

        while (iter.hasNext()) {
            count++;
            String key = iter.next();
            if (the_var.get(key).get(0).get("@id") != null) {


                Set<Pair<QualifiedName, WasDerivedFrom>>  successors1 = new HashSet<>();
                Set<Pair<QualifiedName, WasAttributedTo>> successors2 = new HashSet<>();
                Set<Pair<QualifiedName, HadMember>>       successors3 = new HashSet<>();
                Set<Pair<QualifiedName, QualifiedHadMember>> successors3b = new HashSet<>();
                Set<Pair<QualifiedName, SpecializationOf>>successors4 = new HashSet<>();
                for (QualifiedName qn : allVars) {
                    if (key.equals(qn.getLocalPart())) {
                        successors1 = indexed.traverseDerivationsWithRelations(qn);  // TODO: make use of the successors/successor2 precalculated above.
                        successors2 = indexed.traverseAttributionsWithRelations(qn); // TODO: make use of the successors/successor2 precalculated above.
                        // note that for the client successor table, there is no need to distinguish qualitified/unqualified membership
                        successors3 = indexed.traverseReverseMembershipsWithRelations(qn);  // TODO: make use of the successors/successor2 precalculated above. // NOTE: Reverse relation
                        successors4 = indexed.traverseSpecializationsWithRelations(qn);  // TODO: make use of the successors/successor2 precalculated above. // NOTE: Reverse relation
                        break;
                    }
                }
                String initializer = "";
                List<Integer> rowValues=new LinkedList<>();
                boolean first = true;
                for (Pair<QualifiedName, WasDerivedFrom> successor : successors1) {
                    int i = index.get(successor.getLeft());
                    if (first) {
                        first = false;
                    } else {
                        initializer = initializer + ", ";
                    }
                    initializer = initializer + i + ", " + relationTypeNumber(successor.getRight()) + " /* " +  successor.getRight().getKind() + " */";
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor.getRight()));
                }
                for (Pair<QualifiedName, WasAttributedTo> successor2 : successors2) {
                    int i = index.get(successor2.getLeft());
                    if (first) {
                        first = false;
                    } else {
                        initializer = initializer + ", ";
                    }
                    initializer = initializer + i + ", " + relationTypeNumber(successor2.getRight()) + " /* " +  successor2.getRight().getKind() + " */";
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor2.getRight()));
                }
                for (Pair<QualifiedName, HadMember> successor3 : successors3) {
                    int i = index.get(successor3.getLeft());
                    if (first) {
                        first = false;
                    } else {
                        initializer = initializer + ", ";
                    }
                    initializer = initializer + i + ", " + relationTypeNumber(successor3.getRight()) + " /* " +  successor3.getRight().getKind() + " */";
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor3.getRight()));
                }
                for (Pair<QualifiedName, SpecializationOf> successor4 : successors4) {
                    int i = index.get(successor4.getLeft());
                    if (first) {
                        first = false;
                    } else {
                        initializer = initializer + ", ";
                    }
                    initializer = initializer + i + ", " + relationTypeNumber(successor4.getRight()) + " /* " +  successor4.getRight().getKind() + " */";
                    rowValues.add(i);
                    rowValues.add(relationTypeNumber(successor4.getRight()));
                }

                builder.addStatement("table.put($L,new int[] { " + initializer + "})", count);

                tableValues.put(count,rowValues);

            }

        }


        builder.addStatement("return table");


        MethodSpec method = builder.build();

        return Pair.of(method,tableValues);
    }


    public MethodSpec generateCommonMethod6static(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema, IndexedDocument indexed) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getAllTypes")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.get(String[].class));

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateCommonMethod6static()");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        Set<String> allTypes=new HashSet<>();

        for (StatementOrBundle sOrb: indexed.toDocument().getStatementOrBundle()) {
            if (sOrb instanceof HasType) {
                for (Type tp: ((HasType)sOrb).getType()) {
                    Object val=tp.getValue();
                    if (val instanceof QualifiedName) {
                        final QualifiedName qn = (QualifiedName) val;
                        if (!isVariable(qn)) allTypes.add(qn.getUri());
                    }
                }
            }
        }


        List<String> knownTypes = getCommonTypes0();

        List<String> sortedList = new ArrayList<>(allTypes);
        Collections.sort(sortedList);

        knownTypes.addAll(sortedList);



        builder.addStatement("String [] table = new String[$L]", knownTypes.size());
        int count=0;
        for (String s: knownTypes) {
            builder.addStatement("table[$L]=$S", count,s);
            count++;
        }




        builder.addStatement("return table");


        MethodSpec method = builder.build();

        return method;
    }

    public List<String> getCommonTypes0() {
        List<String> knownTypes = new ArrayList<>();
        knownTypes.add(NamespacePrefixMapper.PROV_NS + "Entity");
        knownTypes.add(NamespacePrefixMapper.PROV_NS + "Activity");
        knownTypes.add(NamespacePrefixMapper.PROV_NS + "Agent");
        return knownTypes;
    }


    public int relationTypeNumber(Relation rel) {
        return rel.getKind().ordinal();
    }


    public MethodSpec generateFactoryMethodWithBean(String template, String packge, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toBean")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(ClassName.get(packge,compilerUtil.beanNameClass(template)));

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateFactoryMethodWithBean()");


        Collection<String>variables=descriptorUtils.fieldNames(bindingsSchema);
        for (String key: variables) {
            String newkey = compilerUtil.generateNewNameForVariable(key);
            builder.addParameter(compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key), newkey);
        }

        builder.addStatement("$T bean=new $T()",ClassName.get(packge,compilerUtil.beanNameClass(template)),ClassName.get(packge,compilerUtil.beanNameClass(template)));

        String args = "";
        for (String key: variables) {
            String newkey= compilerUtil.generateNewNameForVariable(key);
            String statement = "bean.$N=$N";
            builder.addStatement(statement, key, newkey);
        }


        builder.addStatement("return bean");

        MethodSpec method = builder.build();

        return method;
    }


    public MethodSpec generateFactoryMethodToBeanWithArrayComposite(String template, String packge, TemplateBindingsSchema bindingsSchema, String logger, BeanKind beanKind) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toBean")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge,compilerUtil.beanNameClass(template)));

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateFactoryMethodToBeanWithArrayComposite()");


        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        builder.addParameter(listOfArrays, "records");

        builder.addStatement("$T record=records.get(0)", Object[].class);

        builder.addStatement("$T bean=new $T()",ClassName.get(packge,compilerUtil.beanNameClass(template)),ClassName.get(packge,compilerUtil.beanNameClass(template)));

        int count = 1;
        String args = "";
        for (String key: variables) {
            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
            final String converter = compilerUtil.getConverterForDeclaredType(declaredJavaType);
            final String converter2 = compilerUtil.getConverterForDeclaredType2(declaredJavaType);

            if (converter2 == null) {
                String statement = "bean.$N=($T) record[" + count + "]";
                builder.addStatement(statement, key, declaredJavaType);
            } else {
                String statement = "bean.$N=(record[" + count + "]==null)?null:((record[" + count + "] instanceof String)?$N((String)(record[" + count + "])):($T)(record[" + count + "]))";
                builder.addStatement(statement, key, converter2, declaredJavaType);
            }
            if (count > 1) args = args + ", ";
            args = args + key;
            count++;
        }

        builder.addStatement("bean.$N=new $T<>()", ELEMENTS, LinkedList.class);
        builder.beginControlFlow("for (int i=1;i<records.size(); i++) ")
                .addStatement("bean.$N($T.simpleBeanConverters.get(records.get(i)[0]).process(records.get(i)))",
                        ADD_ELEMENTS,
                        ClassName.get(packge,logger))

                .endControlFlow();



        builder.addStatement("return bean");


        MethodSpec method = builder.build();

        return method;
    }
    public MethodSpec generateFactoryMethodToBeanWithArray(String template, String packge, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema, BeanKind beanKind) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toBean")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge,compilerUtil.beanNameClass(template)));

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateFactoryMethodToBeanWithArray()");


        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        builder.addParameter(Object[].class, "record");

        builder.addStatement("$T bean=new $T()",ClassName.get(packge,compilerUtil.beanNameClass(template)),ClassName.get(packge,compilerUtil.beanNameClass(template)));

        int count = 1;
        String args = "";
        for (String key: variables) {
            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
            final String converter = compilerUtil.getConverterForDeclaredType(declaredJavaType);
            final String converter2 = compilerUtil.getConverterForDeclaredType2(declaredJavaType);

            if (converter2 == null) {
                String statement = "bean.$N=($T) record[" + count + "]";
                builder.addStatement(statement, key, declaredJavaType);
            } else {
                String statement = "bean.$N=(record[" + count + "]==null)?null:((record[" + count + "] instanceof String)?$N((String)(record[" + count + "])):($T)(record[" + count + "]))";
                builder.addStatement(statement, key, converter2, declaredJavaType);
            }
            if (count > 1) args = args + ", ";
            args = args + key;
            count++;
        }
        builder.addStatement("return bean");


        MethodSpec method = builder.build();

        return method;
    }


    public MethodSpec generateNewBean(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String packge, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("newBean")
                .addModifiers(Modifier.PUBLIC)
                //.addModifiers(Modifier.STATIC)
                .returns(ClassName.get(packge, compilerUtil.beanNameClass(template)));

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateNewBean()");


        builder.addStatement("$T bean=new $T()", ClassName.get(packge, compilerUtil.beanNameClass(template)), ClassName.get(packge, compilerUtil.beanNameClass(template)));


        builder.addStatement("return bean");

        MethodSpec method = builder.build();

        return method;
    }




    public MethodSpec generateExamplarBean(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String packge, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("examplar")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(ClassName.get(packge,compilerUtil.beanNameClass(template)));

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateExamplarBean()");


        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");


        builder.addStatement("$T bean=new $T()",ClassName.get(packge,compilerUtil.beanNameClass(template)),ClassName.get(packge,compilerUtil.beanNameClass(template)));

        for (QualifiedName q : allVars) {
            JsonNode jsonNode1 = the_var.get(q.getLocalPart());
            JsonNode jsonNode2=(jsonNode1==null)?null:jsonNode1.get(0);
            JsonNode jsonNode3=(jsonNode2==null)?null:jsonNode2.get("@type");
            String idType =  (jsonNode3==null)?null:jsonNode3.textValue();
            Iterator<String> iter3 = the_var.fieldNames();
            while (iter3.hasNext()) {
                String key3 = iter3.next();
                if (q.getLocalPart().equals(key3)) {
                    if (idType==null) {
                        builder.addStatement("bean.$N=$S", q.getLocalPart(), "example_" + q.getLocalPart());
                    } else {
                        String example = compilerUtil.generateExampleForType(idType, q.getLocalPart(), pFactory);
                        Class<?> declaredJavaType=compilerUtil.getJavaTypeForDeclaredType(the_var, key3);

                        final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
                        if (converter == null) {
                            builder.addStatement("bean.$N=$S",  q.getLocalPart(), example);
                        } else {
                            builder.addStatement("bean.$N=$N($S)",  q.getLocalPart(), converter, example);
                        }
                    }
                }
            }
        }


        JsonNode the_var2 = (bindings_schema == null) ? null : bindings_schema.get("var");



        for (QualifiedName q : allAtts) {


            String declaredType = null;
            Class<?> declaredJavaType = null;
            if (the_var2 != null) {
                Iterator<String> iter3 = the_var2.fieldNames();

                while (iter3.hasNext()) {
                    String key3 = iter3.next();
                    if (q.getLocalPart().equals(key3)) {
                        declaredType = compilerUtil.getDeclaredType(the_var2, key3);
                        declaredJavaType=compilerUtil.getJavaTypeForDeclaredType(the_var2, key3);
                    }
                }
            }

            String example = compilerUtil.generateExampleForType(declaredType, q.getLocalPart(), pFactory);

            final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
            if (converter == null) {
                builder.addStatement("bean.$N=$S",  q.getLocalPart(), example);
            } else {
                builder.addStatement("bean.$N=$N($S)",  q.getLocalPart(), converter, example);
            }
        }


        builder.addStatement("return bean");

        MethodSpec method = builder.build();

        return method;
    }


    public JavaFile generateSQLInterface(String packge) {

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit("SQL");


        MethodSpec.Builder builder2 = MethodSpec.methodBuilder("getSQLInsert")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder2.build());


        MethodSpec.Builder builder3 = MethodSpec.methodBuilder("getSQLInsertStatement")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder3.build());


        TypeSpec theInterface = builder.build();

        JavaFile myfile = JavaFile.builder(Constants.CLIENT_PACKAGE, theInterface)
                .addFileComment("Generated by ProvToolbox method $N", getClass().getName()+".generateSQLInterface()")
                .build();
        return myfile;
    }

    //move to expansion subpackage
    public MethodSpec commonAccessorGenerator(String templateName, String packge) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getClientBuilder")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(Constants.CLIENT_PACKAGE, Constants.BUILDER));

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".commonAccessorGenerator()");

        builder.addStatement("return new $T()", ClassName.get(packge,compilerUtil.templateNameClass(templateName)));

        return builder.build();

    }

    public MethodSpec typedRecordGenerator(String templateName, String packge) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getTypedRecord")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge,compilerUtil.templateNameClass(templateName)+"TypedRecord"));

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".typeRecordGenerator()");


        builder.addStatement("return new $T()", ClassName.get(packge,compilerUtil.templateNameClass(templateName)+"TypedRecord"));

        return builder.build();

    }
}
