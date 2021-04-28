package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.*;

import javax.lang.model.element.Modifier;
import java.util.*;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;

public class CompilerClient {
    private final CompilerUtil compilerUtil=new CompilerUtil();
    private final ProvFactory pFactory;

    private final boolean debugComment=true;
    private final CompilerSQL compilerSQL;

    public CompilerClient(ProvFactory pFactory, CompilerSQL compilerSQL) {
        this.pFactory=pFactory;
        this.compilerSQL=compilerSQL;
    }


    public TypeSpec.Builder generateClassInit(String builderName,  String builderPackage, String continuationName, String continuationPackage, String supername) {
        return TypeSpec.classBuilder(builderName)
                .addSuperinterface(ClassName.get(builderPackage,supername))
                .addSuperinterface(ClassName.get(continuationPackage,continuationName))  // implements Continuation Interface
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

        TypeSpec.Builder builder = generateClassInit(name, ConfigProcessor.CLIENT_PACKAGE, compilerUtil.continuationNameClass(templateName),packge,"Builder");




        if (bindings_schema!=null) {
            builder.addMethod(generateClientMethod(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod2(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod2PureCsv(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod3static(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod3(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod4static(allVars, allAtts, name, templateName, bindings_schema, indexed));


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

            //      builder.addMethod(generateFactoryMethodWithArray(allVars, allAtts, name, bindings_schema));

            builder.addMethod(generateFactoryMethodWithBean(allVars, allAtts, name, templateName, packge, bindings_schema));
            builder.addMethod(generateNewBean(allVars, allAtts, name, templateName, packge, bindings_schema));
            builder.addMethod(generateExamplarBean(allVars, allAtts, name, templateName, packge, bindings_schema));

            }

        compilerSQL.generateSQLstatements(builder, allVars, allAtts, name, templateName, bindings_schema);
        builder.addMethod(generateClientSQLMethod(allVars, allAtts, name, templateName, bindings_schema));

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

    public MethodSpec generateClientMethod(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        return generateClientMethod_aux(allVars,allAtts,name,template, compilerUtil.loggerName(template),bindings_schema);
    }

    public MethodSpec generateClientSQLMethod(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        return generateClientMethod_aux(allVars,allAtts,name,template, compilerUtil.sqlName(template),bindings_schema);
    }

    public MethodSpec generateClientMethod_aux(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String loggerName, JsonNode bindings_schema) {
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

        builder.addStatement("$N(" + args + ")", loggerName);
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

        String constant = (legacy? "[\"" : "\"") + template + "\"";
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
                String myEscapeStatement = "$N.append($T.escapeJavaScript($N))";
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
                builder.nextControlFlow("else")
                        .addStatement("$N.append($S)", var, "\"");

                if (doEscape) {
                    builder.addStatement(myEscapeStatement, var, ClassName.get("org.openprovenance.apache.commons.lang", "StringEscapeUtils"), newName);
                } else {
                    builder.addStatement(myStatement, var, newName);
                }
                builder.addStatement("$N.append($S)", var, "\"")
                        .endControlFlow();
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

}