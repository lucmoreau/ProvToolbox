package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;
import org.openprovenance.apache.commons.lang.StringEscapeUtils;
import org.openprovenance.prov.model.*;

import javax.lang.model.element.Modifier;
import java.util.*;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerClient {
    private final CompilerUtil compilerUtil=new CompilerUtil();
    private final ProvFactory pFactory;

    private final boolean debugComment=true;
    private final CompilerSQL compilerSQL;

    public CompilerClient(ProvFactory pFactory, CompilerSQL compilerSQL) {
        this.pFactory=pFactory;
        this.compilerSQL=compilerSQL;
    }


    public TypeSpec.Builder generateClassInit(String builderName,  String builderPackage, String processorName, String processorPackage, String supername) {
        return TypeSpec.classBuilder(builderName)
                .addSuperinterface(ClassName.get(builderPackage,supername))
                .addSuperinterface(ClassName.get(builderPackage,"SQL"))
                //.addSuperinterface(ClassName.get(processorPackage,processorName))  // implements Processor Interface
                .addModifiers(Modifier.PUBLIC);
    }

    public JavaFile generateClientLib(Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {


        Bundle bun=u.getBundle(doc).get(0);

        Set<QualifiedName> allVars=new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts=new HashSet<QualifiedName>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);



        IndexedDocument indexed=new IndexedDocument(pFactory, pFactory.newDocument(),true);
        u.forAllStatement(bun.getStatement(), indexed);


        return generateClientLib_aux(doc, allVars,allAtts,name, templateName, packge, resource, bindings_schema, indexed);

    }


    static final ParameterizedTypeName hashmapType = ParameterizedTypeName.get(ClassName.get(HashMap.class), TypeName.get(Integer.class), TypeName.get(int[].class));


    JavaFile generateClientLib_aux(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema, IndexedDocument indexed) {

        TypeSpec.Builder builder = generateClassInit(name, ConfigProcessor.CLIENT_PACKAGE, compilerUtil.processorNameClass(templateName),packge, ConfigProcessor.BUILDER);




        if (bindings_schema!=null) {
           // builder.addMethod(generateClientMethod(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientCSVConverterMethod_aux(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindings_schema));
            builder.addMethod(generateClientSQLConverterMethod_aux(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindings_schema));
            builder.addMethod(generateArgsToRecordMethod(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindings_schema));
            builder.addMethod(generateProcessorConverter(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindings_schema));
            builder.addMethod(generateProcessorConverter2(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindings_schema));
            builder.addMethod(generateApplyMethod(allVars, allAtts, name, templateName, compilerUtil.loggerName(templateName), packge, bindings_schema));


            builder.addMethod(generateClientMethod2(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod2PureCsv(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod3static(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod3(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod4static(allVars, allAtts, name, templateName, bindings_schema, indexed));

            builder.addField(generateField4VarArray(allVars,allAtts,name,templateName,packge, bindings_schema));

            builder.addField(generateField4aBeanConverter(allVars,allAtts,name,templateName,packge, bindings_schema));
            builder.addField(generateField4aBeanConverter2(allVars,allAtts,name,templateName,packge, bindings_schema));
            builder.addField(generateField4aSQLConverter2(allVars,allAtts,name,templateName,packge, bindings_schema));
            builder.addField(generateField4aArgs2CsvConverter(allVars,allAtts,name,templateName,packge, bindings_schema));
            builder.addField(generateField4aRecord2SqlConverter(allVars,allAtts,name,templateName,packge, bindings_schema));
            builder.addField(generateField4aRecord2CsvConverter(allVars,allAtts,name,templateName,packge, bindings_schema));

            builder.addField(FieldSpec.builder(hashmapType, "__successors")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("__getSuccessors()")
                    .build());

            builder.addField(FieldSpec.builder(int[].class, "__nodes")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("__getNodes()")
                    .build());

            builder.addMethod(generateClientMethod4(allVars, allAtts, name, templateName, bindings_schema, indexed));
            builder.addMethod(nameAccessorGenerator(templateName));
            builder.addMethod(generateRecordCsvProcessorMethod());

            //      builder.addMethod(generateFactoryMethodWithArray(allVars, allAtts, name, bindings_schema));

            builder.addMethod(generateFactoryMethodWithBean(allVars, allAtts, name, templateName, packge, bindings_schema));
            builder.addMethod(generateFactoryMethodToBeanWithArray(allVars, allAtts, name, templateName, packge, bindings_schema));
            builder.addMethod(generateNewBean(allVars, allAtts, name, templateName, packge, bindings_schema));
            builder.addMethod(generateExamplarBean(allVars, allAtts, name, templateName, packge, bindings_schema));

            }

        compilerSQL.generateSQLstatements(builder, allVars, allAtts, name, templateName, bindings_schema);
        //builder.addMethod(generateClientSQLMethod(allVars, allAtts, name, templateName, bindings_schema));

        builder.addMethod(compilerSQL.generateClientSQLMethod2(allVars, allAtts, name, templateName, bindings_schema));


        // System.out.println(allVars);With group by decision aggregate record with Seq

        TypeSpec bean=builder.build();

        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N",getClass().getName(), templateName)
                .build();

        return myfile;
    }


    public MethodSpec nameAccessorGenerator(String templateName) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getName")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)

                .returns(String.class)
                .addStatement("return $S", templateName);
        return builder.build();
    }

   // public MethodSpec generateClientMethod(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
    //    return generateClientMethod_aux(allVars,allAtts,name,template, compilerUtil.loggerName(template),"process", bindings_schema);
  //  }

    //public MethodSpec generateClientSQLMethod(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
 //       return generateClientMethod_aux(allVars,allAtts,name,template, compilerUtil.sqlName(template),compilerUtil.sqlName(template), bindings_schema);
  //  }

    public MethodSpec generateClientMethod_aux(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String invoke, String loggerName, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(loggerName)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateClientMethod_aux()");

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add(loggerName + " client side logging method\n");
        JsonNode the_var1 = bindings_schema.get("var");
        Iterator<String> iter1 = the_var1.fieldNames();
        while (iter1.hasNext()) {
            String key = iter1.next();
            String newkey = "__" + key;
            jdoc.add("@param " + newkey + " " + compilerUtil.getJavaTypeForDeclaredType(the_var1, key) + "\n");
        }
        jdoc.add("@return java.lang.String\n");
        builder.addJavadoc(jdoc.build());

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");
        String var = "sb";
        builder.addStatement("$T $N=new $T()", StringBuffer.class, var, StringBuffer.class);

        String args = "" + var;

        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            String newkey = "__" + key;
            builder.addParameter(compilerUtil.getJavaTypeForDeclaredType(the_var, key), newkey);
            args = args + ", " + newkey;
        }

        builder.addStatement("$N(" + args + ")", invoke);
        builder.addStatement("return $N.toString()", var);

        MethodSpec method = builder.build();

        return method;
    }

    public MethodSpec generateClientMethod2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        return generateClientMethod2(allVars,allAtts,name,template,bindings_schema,true);
    }
    public MethodSpec generateClientMethod2PureCsv(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        return generateClientMethod2(allVars,allAtts,name,template,bindings_schema,false);
    }

    public MethodSpec generateClientCSVConverterMethod_aux(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String loggerName, String packge, JsonNode bindings_schema) {
        final TypeName processorClassName = processorClassType(template, packge,ClassName.get(String.class));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(ConfigProcessor.ARGS_CSV_CONVERSION_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(processorClassName);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateClientCSVConverterMethod_aux()");

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

        builder.addStatement("return (" + args + ") -> { $T sb=new $T();$N.$N(sb," + args2 + "); return sb.toString(); }", StringBuffer.class,StringBuffer.class, "self",loggerName);

        MethodSpec method = builder.build();

        return method;
    }


    private FieldSpec generateField4aBeanConverter(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {
        TypeName myType=processorClassType(templateName,packge,ClassName.get(packge,compilerUtil.beanNameClass(templateName)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, ConfigProcessor.A_ARGS_BEAN_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);

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

    private FieldSpec generateField4VarArray(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {

        FieldSpec.Builder fbuilder=FieldSpec.builder(ArrayTypeName.of(String.class), "propertyOrder", Modifier.PUBLIC);

        JsonNode the_var = bindings_schema.get("var");

        String args = "";
        String args2 = "new String[] { \"isA\" ";

        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();

            args2 = args2 + ", ";

            args2=args2+ " " + "\""+ key+"\"";

        }
        fbuilder.initializer(args2 + "}");

        return fbuilder.build();
    }




    private FieldSpec generateField4aBeanConverter2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {
        TypeName myType=ParameterizedTypeName.get(ClassName.get(ConfigProcessor.CLIENT_PACKAGE, PROCESSOR_ARGS_INTERFACE),ClassName.get(packge,compilerUtil.beanNameClass(templateName)));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, A_RECORD_BEAN_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer(" (Object [] record) -> { return $N(record); }", "toBean");

        return fbuilder.build();
    }

    private FieldSpec generateField4aSQLConverter2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {

        final TypeName processorClassName = processorClassType(templateName, packge, ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(processorClassName,ConfigProcessor.A_BEAN_SQL_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer("$N()",ConfigProcessor.BEAN_SQL_CONVERSION_METHOD);

        return fbuilder.build();
    }


    private FieldSpec generateField4aArgs2CsvConverter(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {

        final TypeName processorClassName = processorClassType(templateName, packge, ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(processorClassName,ConfigProcessor.A_ARGS_CSV_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer("$N()",ConfigProcessor.ARGS_CSV_CONVERSION_METHOD);

        return fbuilder.build();
    }


    private FieldSpec generateField4aRecord2SqlConverter(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {

        TypeName myType=ParameterizedTypeName.get(ClassName.get(ConfigProcessor.CLIENT_PACKAGE,ConfigProcessor.PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, ConfigProcessor.A_RECORD_SQL_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer(" (Object [] record) -> { return $N(record).process($N); }", "toBean",ConfigProcessor.A_BEAN_SQL_CONVERTER);

        return fbuilder.build();
    }


    private FieldSpec generateField4aRecord2CsvConverter(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema) {

        TypeName myType=ParameterizedTypeName.get(ClassName.get(ConfigProcessor.CLIENT_PACKAGE,ConfigProcessor.PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));
        FieldSpec.Builder fbuilder=FieldSpec.builder(myType, ConfigProcessor.A_RECORD_CSV_CONVERTER,Modifier.FINAL, Modifier.PUBLIC);


        fbuilder.initializer(" (Object [] record) -> { return $N(record).process($N); }", "toBean", ConfigProcessor.A_ARGS_CSV_CONVERTER);

        return fbuilder.build();
    }




    public MethodSpec generateClientSQLConverterMethod_aux(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String loggerName, String packge, JsonNode bindings_schema) {
        final TypeName processorClassName = processorClassType(template, packge, ClassName.get(String.class));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(ConfigProcessor.BEAN_SQL_CONVERSION_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(processorClassName);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateClientSQLConverterMethod_aux()");

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
        MethodSpec.Builder builder = MethodSpec.methodBuilder(ConfigProcessor.ARGS2RECORD_CONVERTER)
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

    public MethodSpec generateProcessorConverter(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String loggerName, String packge, JsonNode bindings_schema) {
        final TypeName processorClassName = processorClassType(template, packge, TypeVariableName.get("T"));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(ConfigProcessor.PROCESSOR_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(processorClassName);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateProcessorConverter()");

        TypeName parameterType=ParameterizedTypeName.get(ClassName.get(ConfigProcessor.CLIENT_PACKAGE,ConfigProcessor.PROCESSOR_ARGS_INTERFACE),TypeVariableName.get("T"));


        builder.addParameter(parameterType, "processor", Modifier.FINAL);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Returns a converter from Processor taking arguments to Processor taking record\n");
        jdoc.add("@param processor a transformer for this template\n");
        //jdoc.add("@param T the type of data returned by the transformer\n");  //LUC TODO: to be added when all templates are polymorphic
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



        builder.addStatement("return (" + args + ") -> {  return processor.process(new Object [] { getName(), " + args2 + "}); }");



        MethodSpec method = builder.build();

        return method;
    }

    public MethodSpec generateProcessorConverter2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String loggerName, String packge, JsonNode bindings_schema) {
        final TypeName processorClassName = processorClassType(template, packge, TypeVariableName.get("T"));
        final TypeName processorClassNameNotParametrised = processorClassType(template, packge);
        TypeName returnType=ParameterizedTypeName.get(ClassName.get(ConfigProcessor.CLIENT_PACKAGE,ConfigProcessor.PROCESSOR_ARGS_INTERFACE),TypeVariableName.get("T"));
        TypeName returnTypeNotParametrised =ClassName.get(ConfigProcessor.CLIENT_PACKAGE,ConfigProcessor.PROCESSOR_ARGS_INTERFACE);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(ConfigProcessor.PROCESSOR_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(returnType);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateProcessorConverter2()");



        builder.addParameter(processorClassName, "processor", Modifier.FINAL);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Returns a converter from Processor taking arguments to Processor taking record\n");
        jdoc.add("@param processor a transformer for this template\n");
        //jdoc.add("@param T the type of data returned by the transformer\n");  //LUC TODO: to be added when all templates are polymorphic
        jdoc.add("@return $T\n" , returnTypeNotParametrised);
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
            String newkey = "__" + key;
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
        jdoc.add("@return $T\n" , TypeVariableName.get("T"));
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

    public MethodSpec generateClientMethod2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema, boolean legacy) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(compilerUtil.loggerName(template) + (legacy ? "_impure" : ""))
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        String var = "sb";

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateClientMethod2()");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        builder.addParameter(StringBuffer.class, var);
        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            String newkey = "__" + key;
            builder.addParameter(compilerUtil.getJavaTypeForDeclaredType(the_var, key), newkey);
        }


        iter = the_var.fieldNames();

        String constant = (legacy? "[" : "") + StringEscapeUtils.escapeCsv(template);
        while (iter.hasNext()) {
            String key = iter.next();
            final String newName = "__" + key;
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

    public MethodSpec generateClientMethod3static(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getNodes")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(int[].class);
        String var = "sb";
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateClientMethod3static()");

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

    public MethodSpec generateClientMethod3(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getNodes")
                .addModifiers(Modifier.PUBLIC)
                .returns(int[].class);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateClientMethod3()");


        builder.addStatement("return __nodes");


        MethodSpec method = builder.build();

        return method;
    }

    TypeName myType=ParameterizedTypeName.get(ClassName.get(ConfigProcessor.CLIENT_PACKAGE,ConfigProcessor.PROCESSOR_ARGS_INTERFACE),ClassName.get(String.class));

    public MethodSpec generateRecordCsvProcessorMethod() {
        MethodSpec.Builder builder5 = MethodSpec.methodBuilder(ConfigProcessor.RECORD_CSV_PROCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ArrayTypeName.of(Object.class),"record").build())
                .returns(myType);
        builder5.addStatement("return $N", ConfigProcessor.A_RECORD_CSV_CONVERTER);
        return builder5.build();
    }
    public MethodSpec generateClientMethod4(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema, IndexedDocument indexed) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getSuccessors")
                .addModifiers(Modifier.PUBLIC)
                .returns(CompilerUtil.hashmapType);
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateClientMethod4()");

        builder.addStatement("return __successors");

        MethodSpec method = builder.build();

        return method;
    }

    public MethodSpec generateClientMethod4static(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema, IndexedDocument indexed) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("__getSuccessors")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(CompilerUtil.hashmapType);
        String var = "sb";
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateClientMethod4static()");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");


        Iterator<String> iter2 = the_var.fieldNames();
        int count2 = 0;
        HashMap<QualifiedName, Integer> index = new HashMap<QualifiedName, Integer>();
        while (iter2.hasNext()) {
            count2++;
            String key = iter2.next();
            for (QualifiedName qn : allVars) {
                if (key.equals(qn.getLocalPart())) {
                    index.put(qn, count2);
                }
            }
        }

        builder.addStatement("$T table = new $T()", CompilerUtil.hashmapType, CompilerUtil.hashmapType);

        Iterator<String> iter = the_var.fieldNames();

        int count = 0;

        while (iter.hasNext()) {
            count++;
            String key = iter.next();
            if (the_var.get(key).get(0).get("@id") != null) {


                Set<QualifiedName> successors = new HashSet<QualifiedName>();
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



    public MethodSpec generateFactoryMethodWithBean(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String packge, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toBean")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(ClassName.get(packge,compilerUtil.beanNameClass(template)));

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateFactoryMethodWithBean()");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");


        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            String newkey = "__" + key;
            builder.addParameter(compilerUtil.getJavaTypeForDeclaredType(the_var, key), newkey);
        }

        builder.addStatement("$T bean=new $T()",ClassName.get(packge,compilerUtil.beanNameClass(template)),ClassName.get(packge,compilerUtil.beanNameClass(template)));

        Iterator<String> iter2 = the_var.fieldNames();
        String args = "";
        while (iter2.hasNext()) {
            String key = iter2.next();
            String newkey="__"+key;
            String statement = "bean.$N=$N";
            builder.addStatement(statement, key, newkey);
        }


        builder.addStatement("return bean");

        MethodSpec method = builder.build();

        return method;
    }


    public MethodSpec generateFactoryMethodToBeanWithArray(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String packge, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toBean")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge,compilerUtil.beanNameClass(template)));
        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateFactoryMethodToBeanWithArray()");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        builder.addParameter(Object[].class, "record");

        builder.addStatement("$T bean=new $T()",ClassName.get(packge,compilerUtil.beanNameClass(template)),ClassName.get(packge,compilerUtil.beanNameClass(template)));

        int count = 1;
        Iterator<String> iter = the_var.fieldNames();
        String args = "";
        while (iter.hasNext()) {
            String key = iter.next();
            final Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(the_var, key);
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


        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");


        builder.addStatement("$T bean=new $T()",ClassName.get(packge,compilerUtil.beanNameClass(template)),ClassName.get(packge,compilerUtil.beanNameClass(template)));

        for (QualifiedName q : allVars) {
            Iterator<String> iter3 = the_var.fieldNames();
            while (iter3.hasNext()) {
                String key3 = iter3.next();
                if (q.getLocalPart().equals(key3)) {
                    builder.addStatement("bean.$N=$S", q.getLocalPart(), "example_" + q.getLocalPart());
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

        JavaFile myfile = JavaFile.builder(ConfigProcessor.CLIENT_PACKAGE, theInterface)
                .addFileComment("Generated Automatically by ProvToolbox")
                .build();
        return myfile;
    }

    public MethodSpec clientAccessorGenerator(String templateName, String packge) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getClientBuilder")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(ConfigProcessor.CLIENT_PACKAGE, BUILDER));

        builder.addStatement("return new $T()", ClassName.get(packge,compilerUtil.templateNameClass(templateName)));

        return builder.build();

    }
}
