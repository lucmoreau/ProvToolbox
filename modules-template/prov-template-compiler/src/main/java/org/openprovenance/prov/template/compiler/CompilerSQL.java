package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.compiler.common.BeanKind;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.sql.CompilerSqlComposer;
import org.openprovenance.prov.template.descriptors.*;

import javax.lang.model.element.Modifier;
import java.io.*;
import java.util.*;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.sql.CompilerSqlComposer.getTheSqlType;

public class CompilerSQL {
    public static final String SMALL_INDENTATION = "  ";
    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;
    ObjectMapper om = new ObjectMapper();
    final public boolean withRelationId;
    private final String tableKey;

    public CompilerSQL(ProvFactory pFactory, boolean withRelationId, String tableKey) {
        this.withRelationId=withRelationId;
        this.tableKey=tableKey;
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pFactory=pFactory;
    }


    public void generateSQLEnd(String sqlFile, String root_dir) {

        new File(root_dir).mkdirs();

        final String path = root_dir + "/" + sqlFile;


        try {
            PrintStream ps=new PrintStream(new FileOutputStream(path));
            for (String k: primitiveDeclarations.keySet()) {
                ps.println(primitiveDeclarations.get(k));
                ps.println("\n\n");
            }

            for (String k: typeDeclarations.keySet()) {
                ps.println(typeDeclarations.get(k));
                ps.println("\n\n");
            }

            for (String k: tableDeclarations.keySet()) {
                ps.println(tableDeclarations.get(k));
                ps.println("\n\n");
            }

            for (String k: functionDeclarations.keySet()) {
                ps.println(functionDeclarations.get(k));
                ps.println("\n\n");
            }

            for (String k: arrayFunctionDeclarations.keySet()) {
                ps.println(arrayFunctionDeclarations.get(k));
                ps.println("\n\n");
            }


            ps.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    Map<String,String> tableDeclarations=new LinkedHashMap<>();
    Map<String,String> functionDeclarations=new LinkedHashMap<>();
    Map<String,String> primitiveDeclarations=new LinkedHashMap<>();
    Map<String,String> typeDeclarations=new LinkedHashMap<>();
    Map<String,String> arrayFunctionDeclarations=new LinkedHashMap<>();


    public void generateSQL(String jsonschema, String templateName, String root_dir, TemplateBindingsSchema templateBindingsSchema) {

        String res="";


        Map<String, List<Descriptor>> var = templateBindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(templateBindingsSchema);

        boolean first = true;

        if (debugComment) res = res + "-- Generated by method " +  getClass().getName()+".generateSQL()\n";


        res=res + "\n" + "CREATE TABLE IF NOT EXISTS " + templateName + "\n(\n";


        if (withRelationId) {

            first=false;
            res=res+ SMALL_INDENTATION + tableKey + " SERIAL" ;
        }

        String documentation=null;

        for (String key: descriptorUtils.fieldNames(templateBindingsSchema)) {

            if (first) {
                first=false;
            } else {
                res=res+ ",\n";
            }

            String sqlTypeFromJava = convertToSQLType(compilerUtil.getJavaTypeForDeclaredType(var, key).getName());

            final String sqlType = descriptorUtils.getSqlType(key, templateBindingsSchema);
            if (sqlType !=null && !sqlType.equals(Constants.NULLABLE_TEXT)) {
                sqlTypeFromJava=sqlType;
            }


                Descriptor entry=var.get(key).get(0);
            documentation = retrieveDocumentation(entry);


            if (documentation!=null)  res = res + SMALL_INDENTATION + SMALL_INDENTATION + "--  " + documentation + "\n";
            res=res + SMALL_INDENTATION + sqlify(key) + " " + sqlTypeFromJava;

        }

        res=res+"\n);\n\n";

        tableDeclarations.put(templateName,res);


        generateSqlTypeDeclaration(templateName, templateBindingsSchema, var);

    }

    private void generateSqlTypeDeclaration(String templateName, TemplateBindingsSchema templateBindingsSchema, Map<String, List<Descriptor>> var) {
        boolean first;
        StringBuilder res2= new StringBuilder();
        first=true;

        if (debugComment) res2.append("\n-- Generated by method ").append(getClass().getName()).append(".generateSqlTypeDeclaration()");

        res2.append("\nDROP TYPE IF EXISTS ").append(templateName).append("_type CASCADE;\n");
        res2.append("CREATE TYPE ").append(templateName).append("_type").append(" AS ");
        res2.append(" (\n");
        for (String key: descriptorUtils.fieldNames(templateBindingsSchema)) {
            first=sepIfNotFirst(res2,first,",\n");
            final String sqlType=getTheSqlType(compilerUtil,key,templateBindingsSchema,var);
            final String sqlType2 = convertToSQLType(compilerUtil.getJavaTypeForDeclaredType(var, key).getName());
            res2.append(SMALL_INDENTATION).append(key).append( " ").append(sqlType);
        }
        res2.append("\n );\n");

        typeDeclarations.put(templateName, res2.toString());
    }



    private String retrieveDocumentation(Descriptor entry) {
        String documentation=descriptorUtils.getFromDescriptor(entry, AttributeDescriptor::getDocumentation,NameDescriptor::getDocumentation);
        /*String documentation=null;
        switch (entry.getDescriptorType()) {
            case ATTRIBUTE:
                AttributeDescriptor ad=((AttributeDescriptorList) entry).getItems().get(0);
                documentation =ad.getDocumentation();

                break;
            case NAME:
                NameDescriptor nd=(NameDescriptor) entry;
                documentation =nd.getDocumentation();

                break;
        }
         */
        return documentation;
    }

    public void generateSQLstatements(TypeSpec.Builder builder, String templateName, TemplateBindingsSchema bindingsSchema, BeanKind beanKind) {

        StringBuffer sb=new StringBuffer();
        getInsertStringAndCount(templateName,descriptorUtils.fieldNames(bindingsSchema),sb);

        FieldSpec.Builder builder1=FieldSpec.builder(String.class,"_sqlInsert1", Modifier.PRIVATE, Modifier.STATIC);
        builder1.initializer("$S",sb.toString());

        builder.addField(builder1.build());

        builder.addMethod(generateSQLInsert(templateName,beanKind));
        builder.addMethod(generateSQLInsertStatement(templateName, bindingsSchema,beanKind));

    }
    public MethodSpec generateSQLInsert(String template, BeanKind beanKind) {
        final String loggerName = compilerUtil.loggerName(template);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getSQLInsert")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class);
        compilerUtil.specWithComment(builder);

        if (beanKind.equals(BeanKind.COMPOSITE)) {
            builder.addStatement("throw new $T()", UnsupportedOperationException.class);
        } else {
            builder.addStatement("return _sqlInsert1");
        }
        return builder.build();
    }

    public MethodSpec generateSQLInsertStatement(String template, TemplateBindingsSchema bindingsSchema, BeanKind beanKind) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getSQLInsertStatement")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class);
        compilerUtil.specWithComment(builder);


        if (beanKind.equals(BeanKind.COMPOSITE)) {
            builder.addStatement("throw new $T()", UnsupportedOperationException.class);
        } else {
            Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

            StringBuffer sb = new StringBuffer();

            int count = getInsertStringAndCount(template, variables, sb);
            boolean first;

            sb = new StringBuffer();

            sb.append(" VALUES (");

            first = true;
            for (int i = 0; i < count; i++) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append("?");
            }
            sb.append(");");

            builder.addStatement("return _sqlInsert1+$S", sb.toString());
        }

        return builder.build();
    }

    public int getInsertStringAndCount(String template, Collection<String> variables, StringBuffer sb) {
        sb.append("INSERT INTO  ");
        sb.append(template);
        sb.append(" (");


        boolean first=true;
        int count=0;

        for (String key: variables) {
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


    static public String convertToSQLType(String name) {
        switch (name) {
            case "java.lang.String":
                return "TEXT";
            case "java.lang.Integer":
                return "INT";
            case "java.lang.Float":
                return "FLOAT";
            case "java.lang.Double":
                return "double precision";
            case "java.lang.Boolean":
                return "BOOLEAN";
        }
        throw new UnsupportedOperationException("conversion to SQL type " + name);
    }


    private final boolean debugComment=true;


    public MethodSpec generateCommonSQLMethod2(String template, TemplateBindingsSchema bindingsSchema)  {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(compilerUtil.sqlName(template) )
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        String var = "sb";
        compilerUtil.specWithComment(builder);


        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();
        Collection<String> fieldNames = descriptorUtils.fieldNames(bindingsSchema);


        builder.addParameter(StringBuffer.class, var);
        for (String key: fieldNames) {
            String newkey = "__" + key;
            builder.addParameter(compilerUtil.getJavaTypeForDeclaredType(theVar, key), newkey);
        }




        String constant = "(";
        boolean first=true;
        for (String key: fieldNames) {
            final String newName = "__" + key;
            final Class<?> clazz = compilerUtil.getJavaTypeForDeclaredType(theVar, key);
            final boolean isQualifiedName = compilerUtil.isVariableDenotingQualifiedName(key,theVar);

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
                    Descriptor descriptor = theVar.get(key).get(0);
                    doEscape = ((AttributeDescriptorList)descriptor).getItems().get(0).getEscape() != null;
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

    public void generateSQLPrimitiveTables(Map<String, Map<String, String>> sqlTables) {
        if (sqlTables !=null) {
            for (String table: sqlTables.keySet()) {
                StringBuilder res= new StringBuilder();
                boolean first=true;

                if (debugComment) res.append("-- Generated by method ").append(getClass().getName()).append(".generateSQLPrimitiveTables()\n");

                res.append("\nCREATE TABLE IF NOT EXISTS ").append(table);
                res.append(" (\n");
                Map<String,String> sqlTable= sqlTables.get(table);
                for (String key: sqlTable.keySet()) {
                    first=sepIfNotFirst(res,first,",\n");
                    res.append(SMALL_INDENTATION).append(key).append( " ").append(sqlTable.get(key));
                }
                res.append("\n );\n");

                primitiveDeclarations.put(table, res.toString());

            }

        }

    }



    private String convertToTypeDeclarationOnly(String s) {
        return s.replace(" NOT NULL DEFAULT NOW()","");  //FIXME: make this more robust (Maybe introduce initialization field in the declaration, to separate from type declaration)
    }


    public void generateSQLInsertFunction(String jsonschema, String templateName, String consistOf, String root_dir, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {
        new CompilerSqlComposer(pFactory, withRelationId, tableKey, functionDeclarations,arrayFunctionDeclarations).generateSQLInsertFunction(jsonschema,templateName, consistOf, root_dir,templateBindingsSchema,shared);

        if (shared!=null && shared.size()>0) {
            new CompilerSqlComposer(pFactory, withRelationId, tableKey, functionDeclarations,arrayFunctionDeclarations).generateSQLInsertArrayFunction(templateName, consistOf, templateBindingsSchema, shared);
        }


    }




    private boolean sepIfNotFirst(StringBuilder res, boolean first) {
        return sepIfNotFirst(res,first,", ");
    }

    private boolean sepIfNotFirst(StringBuilder res, boolean first, String sep) {
        if (first) {
            first = false;
        } else {
            res.append(sep);
        }
        return first;
    }


}