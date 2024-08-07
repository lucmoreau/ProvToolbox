package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanKind;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.sql.CompilerSqlComposer;
import org.openprovenance.prov.template.compiler.util.CompilerException;
import org.openprovenance.prov.template.descriptors.*;

import javax.lang.model.element.Modifier;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.sql.CompilerSqlComposer.findExtrasValuesForRelation;
import static org.openprovenance.prov.template.compiler.sql.CompilerSqlComposer.getTheSqlType;

public class CompilerSQL {
    public static final String SMALL_INDENTATION = "  ";
    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;
    ObjectMapper om = new ObjectMapper();
    private final String tableKey;

    public CompilerSQL(ProvFactory pFactory, String tableKey) {
        this.tableKey=tableKey;
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pFactory=pFactory;
    }


    public void generateSQLEnd(String sqlFile, String root_dir, Set<String> referencedSqlTables) {

        checkSQLtables(referencedSqlTables,declaredSqlTables);

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

    public void checkSQLtables(Set<String> referencedSqlTables, Set<String> declaredSqlTables) {
        // compare referenced and declared sql tables
        Set<String> undeclaredTables=new HashSet<>(referencedSqlTables);
        if (declaredSqlTables!=null) undeclaredTables.removeAll(declaredSqlTables);
        Set<String> unreferencedTables=(declaredSqlTables==null)?new HashSet<>():new HashSet<>(declaredSqlTables);
        unreferencedTables.removeAll(referencedSqlTables);
        if (!undeclaredTables.isEmpty()) {
            System.out.println("referenced " + referencedSqlTables);
            System.out.println("declared " + declaredSqlTables);
            throw new CompilerException("Undeclared tables: " + undeclaredTables);
        }
        if (!unreferencedTables.isEmpty()) {
            System.out.println("referenced " + referencedSqlTables);
            System.out.println("declared " + declaredSqlTables);
            throw new CompilerException("Unreferenced tables: " + unreferencedTables);
        }
    }


    private final Map<String,String> tableDeclarations         = new LinkedHashMap<>();
    private final Map<String,String> functionDeclarations      = new LinkedHashMap<>();
    private final Map<String,String> primitiveDeclarations     = new LinkedHashMap<>();
    private final Map<String,String> typeDeclarations          = new LinkedHashMap<>();
    private final Map<String,String> arrayFunctionDeclarations = new LinkedHashMap<>();


    public void generateSQL(String templateName, TemplateBindingsSchema templateBindingsSchema) {

        StringBuilder res= new StringBuilder();


        Map<String, List<Descriptor>> var = templateBindingsSchema.getVar();

        if (debugComment) res.append("-- Generated by method ").append(getClass().getName()).append(".generateSQL()\n");

        res.append("\n").append("CREATE TABLE IF NOT EXISTS ").append(templateName).append("\n(\n");

        res.append(SMALL_INDENTATION).append(tableKey).append(" SERIAL");

        String documentation=null;

        for (String key: descriptorUtils.fieldNames(templateBindingsSchema)) {

            res.append(",\n");

            String sqlTypeFromJava = convertToSQLType(compilerUtil.getJavaTypeForDeclaredType(var, key).getName());

            final String sqlType = descriptorUtils.getSqlType(key, templateBindingsSchema);
            if (sqlType !=null && !sqlType.equals(Constants.NULLABLE_TEXT) && !sqlType.equals(Constants.NON_NULLABLE_TEXT)) {
                sqlTypeFromJava=sqlType;
            }

            Descriptor entry=var.get(key).get(0);
            documentation = retrieveDocumentation(entry);

            if (documentation!=null)  res.append(SMALL_INDENTATION).append(SMALL_INDENTATION).append("--  ").append(documentation).append("\n");
            res.append(SMALL_INDENTATION).append(sqlify(key)).append(" ").append(sqlTypeFromJava);
        }

        res.append("\n);\n\n");

        tableDeclarations.put(templateName, res.toString());

        generateSqlTypeDeclaration(templateName, templateBindingsSchema, var);

    }

    private void generateSqlTypeDeclaration(String templateName, TemplateBindingsSchema templateBindingsSchema, Map<String, List<Descriptor>> var) {
        boolean first;
        StringBuilder res2= new StringBuilder();
        first=true;

        if (debugComment) res2.append("\n-- Generated by method ").append(getClass().getName()).append(".generateSqlTypeDeclaration()");

        String templateNameType=templateName+SUFFIX_TYPE;
        ensureNoSQLClashForTypes(templateNameType,templateBindingsSchema);

        res2.append("\nDROP TYPE IF EXISTS ").append(templateNameType).append(" CASCADE;\n");
        res2.append("CREATE TYPE ").append(templateNameType).append(" AS ");
        res2.append(" (\n");
        for (String key: descriptorUtils.fieldNames(templateBindingsSchema)) {
            first=sepIfNotFirst(res2,first,",\n");
            final String sqlType=getTheSqlType(compilerUtil,key,templateBindingsSchema,var);
            String sqlifiedKey = sqlify(key);
            if (!key.equals(sqlifiedKey))
                ensureNoSQLClassForColumns(sqlifiedKey,templateBindingsSchema);
            res2.append(SMALL_INDENTATION).append(sqlifiedKey).append( " ").append(sqlType);
        }
        res2.append("\n );\n");

        typeDeclarations.put(templateName, res2.toString());
    }

    public void ensureNoSQLClassForColumns(String sqlifiedKey, TemplateBindingsSchema templateBindingsSchema) {
        for (String key: descriptorUtils.fieldNames(templateBindingsSchema)) {
            if (sqlifiedKey.equals(key)) {
                throw new CompilerException("Cannot use reserved column '" + key + "' in template " + templateBindingsSchema.getTemplate());
            }
        }
    }

    public void ensureNoSQLClashForTypes(String var, TemplateBindingsSchema templateBindingsSchema) {
        if (templateBindingsSchema.getVar().containsKey(var)) {
            throw new CompilerException("Cannot use reserved word '" + var + "' as a field name in template " + templateBindingsSchema.getTemplate());
        }
    }


    private String retrieveDocumentation(Descriptor entry) {
        return descriptorUtils.getFromDescriptor(entry, AttributeDescriptor::getDocumentation,NameDescriptor::getDocumentation);
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
    
    static Map<String,String> nameMap=initNameMap();

    static public Map<String, String> initNameMap() {
        Map<String,String> res=new HashMap<>();
        res.put("order", "_order");
        res.put("end", "_end");
        res.put("start", "_start");
        return res;
    }

    static public String sqlify(String key) {
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
        MethodSpec.Builder builder
                = MethodSpec
                .methodBuilder(compilerUtil.sqlName(template))
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

    Set<String> declaredSqlTables=new HashSet<>();

    public void generateSQLPrimitiveTables(Map<String, Map<String, String>> sqlTables, TemplateBindingsSchema bindingsSchema) {
        if (sqlTables !=null) {
            declaredSqlTables.addAll( sqlTables.keySet());
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

    /* Method that generates the following table declaration for the template transforming_composite:

    CREATE TABLE IF NOT EXISTS plead_transforming_composite_linker
        (
          ID SERIAL,
            --  The file resulting from transformation
          composite INT,
          --  The file that was transformed
          simple INT,
          created_at timestamp with time zone NOT NULL DEFAULT NOW()
        );
     */

    public void generateSQLCompositeAndLinkerTable(String templateName) {
        StringBuilder res = new StringBuilder();
        boolean first = true;

        if (debugComment)
            res.append("-- Generated by method ").append(getClass().getName()).append(".generateSQLCompositeAndLinkerTable()\n");

        res.append("\nCREATE TABLE IF NOT EXISTS ").append(templateName).append("_linker");
        res.append(" (\n");

        res.append(SMALL_INDENTATION).append(tableKey).append(" SERIAL").append(",\n");

        res.append(SMALL_INDENTATION).append("--  The file resulting from transformation").append("\n");

        res.append(SMALL_INDENTATION).append("composite INT").append(",\n");

        res.append(SMALL_INDENTATION).append("--  The file that was transformed").append("\n");

        res.append(SMALL_INDENTATION).append("simple INT").append(",\n");

        res.append(SMALL_INDENTATION).append("created_at timestamp with time zone NOT NULL DEFAULT NOW()");

        res.append("\n);\n\n");

        tableDeclarations.put(templateName + "_linker", res.toString());
    }

    public void generateAccessControlTables(){
        StringBuilder res = new StringBuilder();
        if (debugComment)
            res.append("-- Generated by method ").append(getClass().getName()).append(".generateAccessControlTables()\n");

        res.append("\n" +
                "CREATE TABLE IF NOT EXISTS record_index\n" +
                "(\n" +
                "  ID SERIAL,\n" +
                "  key INT,\n" +
                "  table_name TEXT,\n" +
                "  principal TEXT,\n" +
                "  hash jsonb\n" +
                ");\n" +
                "\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS access_control\n" +
                "(\n" +
                "  ID SERIAL,\n" +
                "  record INT,\n" +
                "  authorized TEXT\n" +
                ");\n" +
                "\n" +
                "ALTER TABLE record_index \n" +
                "DROP CONSTRAINT IF EXISTS record_index_pkey CASCADE;\n" +
                "\n" +
                "\n" +
                "ALTER TABLE record_index \n" +
                "ADD CONSTRAINT record_index_pkey\n" +
                "PRIMARY KEY (ID);\n" +
                "\n" +
                "\n" +
                "ALTER TABLE access_control \n" +
                "DROP CONSTRAINT IF EXISTS fk_access_control_record CASCADE;\n" +
                "\n" +
                "\n" +
                "ALTER TABLE access_control \n" +
                "ADD CONSTRAINT fk_access_control_record \n" +
                "FOREIGN KEY (record) \n" +
                "REFERENCES record_index (ID)\n" +
                "ON DELETE CASCADE;\n" );

        tableDeclarations.put("access-control", res.toString());

    }




    /*

    private String convertToTypeDeclarationOnly(String s) {
        return s.replace(" NOT NULL DEFAULT NOW()","");  //FIXME: make this more robust (Maybe introduce initialization field in the declaration, to separate from type declaration)
    }


     */


    public void generateSQLInsertFunction(String jsonschema, String templateName, String consistOf, String root_dir, TemplateBindingsSchema templateBindingsSchema, List<String> shared, Map<String, Map<String, Map<String, String>>> inputOutputMaps, List<String> search) {
        new CompilerSqlComposer(pFactory, tableKey, functionDeclarations,arrayFunctionDeclarations).generateSQLInsertFunction(jsonschema,templateName, consistOf, root_dir,templateBindingsSchema,shared);

        if (shared!=null && !shared.isEmpty()) {
            new CompilerSqlComposer(pFactory, tableKey, functionDeclarations,arrayFunctionDeclarations).generateInsertIntoSharedRelation(templateName, templateBindingsSchema, shared);
            new CompilerSqlComposer(pFactory, tableKey, functionDeclarations,arrayFunctionDeclarations).generateSQLInsertArrayFunction(templateName, consistOf, templateBindingsSchema, shared);
            new CompilerSqlComposer(pFactory, tableKey, functionDeclarations,arrayFunctionDeclarations).generateSQLInsertCompositeAndLinkerFunction(templateName, consistOf, templateBindingsSchema, shared);

            if (consistOf!=null) {
                generateSQLCompositeAndLinkerTable(templateName);
            }
        }

        generateAccessControlTables();

        if (search!=null) {
            for (String baseRelation : search) {

                List<Pair<String, String>> templatesWithBaseRelation = getTemplatesWithBaseRelation(baseRelation, inputOutputMaps);
                // group by first element in the pair, and associated it with a list of the second element in the pair
                Map<String, List<String>> groupedTemplatesWithBaseRelation = templatesWithBaseRelation.stream().collect(Collectors.groupingBy(Pair::getLeft, Collectors.mapping(Pair::getRight, Collectors.toList())));

              //  System.out.println("######## groupedTemplatesWithBaseRelation=" + groupedTemplatesWithBaseRelation);

        /*
        List<Pair<String, String>> templates = List.of(
                Pair.of("plead_transforming", "transforming"),
                Pair.of("plead_filtering", "filtering")
        );

         */
                new CompilerSqlComposer(pFactory, tableKey, functionDeclarations, arrayFunctionDeclarations)
                        .generateSQLSearchRecordFunction(baseRelation, groupedTemplatesWithBaseRelation, templateName, consistOf, root_dir, templateBindingsSchema, shared);
                new CompilerSqlComposer(pFactory, tableKey, functionDeclarations, arrayFunctionDeclarations)
                        .generateSQLSearchRecordByIdFunction(baseRelation, groupedTemplatesWithBaseRelation, templateName, consistOf, root_dir, templateBindingsSchema, shared);

            }
        }

    }

    public List<Pair<String, String>> getTemplatesWithBaseRelation(String baseRelation, Map<String, Map<String, Map<String, String>>> inputOutputMaps) {
        List<Pair<String, String>> templates2=new LinkedList<>();
        if (inputOutputMaps !=null) {
            for (String key: inputOutputMaps.keySet()) {
                Map<String, Map<String, String>> map = inputOutputMaps.get(key);
                for (String template: map.keySet()) {
                    Map<String, String> map2 = map.get(template);
                    for (String property: map2.keySet()) {
                        if (map2.get(property).equals(baseRelation)) {
                            templates2.add(Pair.of(template,property));
                        }
                    }
                }
            }
        }
        return templates2;
    }


    /*
        private boolean sepIfNotFirst(StringBuilder res, boolean first) {
            return sepIfNotFirst(res,first,", ");
        }


     */
    private boolean sepIfNotFirst(StringBuilder res, boolean first, String sep) {
        if (first) {
            first = false;
        } else {
            res.append(sep);
        }
        return first;
    }


}