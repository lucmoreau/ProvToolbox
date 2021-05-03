package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.model.QualifiedName;

import javax.lang.model.element.Modifier;
import java.io.*;
import java.util.*;

public class CompilerSQL {
    private final CompilerUtil compilerUtil=new CompilerUtil();
    ObjectMapper om = new ObjectMapper();

    public CompilerSQL() {
    }

    public void generateSQLEnd(String sqlFile, String root_dir) {

        new File(root_dir).mkdirs();

        final String path = root_dir + "/" + sqlFile;


        try {
            PrintStream ps=new PrintStream(new FileOutputStream(path));
            for (String k: tableDeclarations.keySet()) {
                ps.println(tableDeclarations.get(k));
                ps.println("\n\n");
            }

            ps.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    Map<String,String> tableDeclarations=new HashMap<>();

    public void generateSQL(String jsonschema, String templateName, String root_dir, JsonNode bindings_schema) {

        String res="";

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");


        Iterator<String> iter = the_var.fieldNames();
        boolean first = true;
        
        res=res + "\n" + "CREATE TABLE " + templateName + "\n(\n";



        while (iter.hasNext()) {
            String key = iter.next();

            if (first) {
                first=false;
            } else {
                res=res+ ",\n";
            }

            final String sqlType = convertToSQLType(compilerUtil.getJavaTypeForDeclaredType(the_var, key).getName());

            res=res + "  " + sqlify(key) + " " + sqlType;


            final JsonNode entry = the_var.path(key);
            if (entry != null && !(entry instanceof MissingNode)) {
                JsonNode firstNode = entry.get(0);
                if (firstNode instanceof ArrayNode) {
                    firstNode = ((ArrayNode) firstNode).get(0);
                }
                final JsonNode jsonNode = firstNode.get("@documentation");
                String documentation = compilerUtil.noNode(jsonNode) ? "" : jsonNode.textValue();
                final JsonNode jsonNode2 = firstNode.get("@type");

                //TODO add documentation
                //res=res + " # " + documentation + " (" + sqlType + ")";
            }


        }

        res=res+"\n);\n\n";

        tableDeclarations.put(templateName,res);

    }

    public void generateSQLstatements(TypeSpec.Builder builder, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, JsonNode bindings_schema) {

        StringBuffer sb=new StringBuffer();
        getInsertStringAndCount(templateName,bindings_schema.get("var"),sb);

        FieldSpec.Builder builder1=FieldSpec.builder(String.class,"_sqlInsert1", Modifier.PRIVATE, Modifier.STATIC);
        builder1.initializer("$S",sb.toString());

        builder.addField(builder1.build());

        builder.addMethod(generateSQLInsert(allVars, allAtts, name, templateName, bindings_schema));
        builder.addMethod(generateSQLInsertStatement(allVars, allAtts, name, templateName, bindings_schema));

    }
    public MethodSpec generateSQLInsert(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        final String loggerName = compilerUtil.loggerName(template);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getSQLInsert")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class);
        builder.addStatement("return _sqlInsert1");

        MethodSpec method = builder.build();

        return method;
    }
    public MethodSpec generateSQLInsertStatement(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
        final String loggerName = compilerUtil.loggerName(template);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getSQLInsertStatement")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class);



        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");


        StringBuffer sb = new StringBuffer();

        int count = getInsertStringAndCount(template, the_var, sb);
        boolean first;

        sb=new StringBuffer();

        sb.append(" VALUES (");

        first=true;
        for (int i=0; i<count; i++) {
            if (first) {
                first=false;
            } else {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(");");

        builder.addStatement("return _sqlInsert1+$S", sb.toString());

        MethodSpec method = builder.build();

        return method;
    }

    public int getInsertStringAndCount(String template, JsonNode the_var, StringBuffer sb) {
        sb.append("INSERT INTO  ");
        sb.append(template);
        sb.append(" (");


        boolean first=true;
        int count=0;

        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            if (first) {
                first=false;
            } else {
                sb.append(", ");
            }
            sb.append(sqlify(key));
            count++;
        }

        sb.append(")");
        return count;
    }
    
    Map<String,String> nameMap=initNameMap();

    private Map<String, String> initNameMap() {
        Map<String,String> res=new HashMap<>();
        res.put("order", "_order");
        return res;
    }

    public String sqlify(String key) {
        return nameMap.getOrDefault(key,key);
    }


    String convertToSQLType(String name) {
        switch (name) {
            case "java.lang.String":
                return "text";
            case "java.lang.Integer":
                return "int";
            case "java.lang.Float":
                return "float";
            case "java.lang.Boolean":
                return "BOOLEAN";
        }
        throw new UnsupportedOperationException("conversion to SQL type " + name);
    }


    private final boolean debugComment=true;
    public MethodSpec generateClientSQLMethod2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema)  {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(compilerUtil.sqlName(template) )
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        String var = "sb";

        if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateClientSQLMethod2()");

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

        String constant = "(";
        boolean first=true;
        while (iter.hasNext()) {
            String key = iter.next();
            final String newName = "__" + key;
            final Class<?> clazz = compilerUtil.getJavaTypeForDeclaredType(the_var, key);
            final boolean isQualifiedName = the_var.get(key).get(0).get("@id") != null;

            if (first) {
                first=false;
            } else {
                constant = constant + ',';
            }
            builder.addStatement("$N.append($S)", var, constant);
            constant = "";

            if (String.class.equals(clazz)) {
                String myStatement = "$N.append($N)";
                String myEscapeStatement = "$N.append($T.escapeJavaScript($N))";
                boolean doEscape=false;
                if (!isQualifiedName) {
                    doEscape = the_var.get(key).get(0).get(0).get("@escape") != null;
                    if (doEscape) {
                        //foundEscape=true;
                    }
                }
                builder.beginControlFlow("if ($N==null)", newName);
                builder.addStatement("$N.append($S)", var, "''"); // is it correct, or should it be null?
                builder.nextControlFlow("else")
                        .addStatement("$N.append($S)", var, "'");

                if (doEscape) {
                    builder.addStatement(myEscapeStatement, var, ClassName.get("org.openprovenance.apache.commons.lang", "StringEscapeUtils"), newName);
                } else {
                    builder.addStatement(myStatement, var, newName);
                }
                builder.addStatement("$N.append($S)", var, "'")
                        .endControlFlow();
            } else {
                builder.beginControlFlow("if ($N==null)", newName);
                builder.addStatement("$N.append($S)", var, "''");  // is it correct, or should it be null?
                builder.nextControlFlow("else");
                builder.addStatement("$N.append($S)", var, constant);
                builder.addStatement("$N.append($N)", var, newName);
                builder.endControlFlow();
            }
        }
        builder.addStatement("$N.append($S)", var, ")");


        MethodSpec method = builder.build();

        return method;
    }

}