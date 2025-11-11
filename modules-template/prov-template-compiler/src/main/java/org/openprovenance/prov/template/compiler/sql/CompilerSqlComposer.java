package org.openprovenance.prov.template.compiler.sql;

import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.util.*;
import java.util.Collections;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.CompilerSQL.convertToSQLType;
import static org.openprovenance.prov.template.compiler.CompilerSQL.initNameMap;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.sql.QueryBuilder.*;

public class CompilerSqlComposer {
    public static final String[] ARRAY_OF_STRING = {};
    public static final String TOKEN_VAR_NAME = "_token";
    public static final String DUMMY = "__dummy_";
    private final CompilerUtil compilerUtil;
    final Map<String,String> functionDeclarations;
    final Map<String,String> arrayFunctionDeclarations;


    private final String tableKey;

    public CompilerSqlComposer(ProvFactory pFactory, String tableKey, Map<String, String> functionDeclarations, Map<String,String> arrayFunctionDeclarations) {
        this.tableKey=tableKey;
        this.functionDeclarations = functionDeclarations;
        this.arrayFunctionDeclarations = arrayFunctionDeclarations;
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    public static <T> Collector<T, List<T>, List<T>> toListCollector() {
        return Collector.of(
                (Supplier<List<T>>) ArrayList::new,
                (BiConsumer<List<T>, T>) List::add,
                (BinaryOperator<List<T>>) (left, right) -> { left.addAll(right); return left; }
        );
    }


    public static <T> Collector<Pair<T,T>, List<T>, List<T>> toListCollector2() {
        return Collector.of(
                (Supplier<List<T>>) ArrayList::new,
                (l, p) -> {l.add(p.getLeft()); l.add(p.getRight()); },
                (left, right) -> { left.addAll(right); return left; }
        );
    }

    public void generateSQLInsertFunction(String jsonschema, String templateName, String consistOf, Locations locations, String root_dir, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {


        templateName=locations.getShortNames().getOrDefault(templateName, templateName);
        consistOf=(consistOf==null)?null:locations.getShortNames().getOrDefault(consistOf, consistOf);


        String orig_templateName=(consistOf==null)?templateName:consistOf;

        Map<String, List<Descriptor>> var = templateBindingsSchema.getVar();

        final String insertFunctionName= Constants.INSERT_PREFIX+templateName;



        Map<String,?> funParams=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> descriptorUtils.isInput(key, templateBindingsSchema) || shared.contains(key))
                .collect(Collectors.toMap(key -> appendPossiblySharedOutput(key, shared.contains(key)),
                                          key -> unquote(getTheSqlType(compilerUtil, key, templateBindingsSchema, var)),
                                          (x, y) -> y,
                                          LinkedHashMap::new));


        Map<String,Object> functionReturns=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> descriptorUtils.isOutput(key, templateBindingsSchema) && !shared.contains(key))
                .collect(Collectors.toMap(this::sqlify,
                                          key -> unquote(getTheSqlType(compilerUtil, key, templateBindingsSchema, var)),
                                          (x, y) -> y,
                                          () -> new LinkedHashMap<String, Object>(){{
                                              put(tableKey, unquote("INT"));
                                          }}));
        if (functionReturns.keySet().size()==1) {
            // ensure that there are more than one column, otherwise postgres converts it to a scalar
            functionReturns.put(DUMMY, unquote("INT"));
        }


        Map<String,Object> insertValues2=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .collect(Collectors.toMap(  this::sqlify,
                        key -> {
                            if (descriptorUtils.isInput(key, templateBindingsSchema)) {
                                return unquote(Constants.INPUT_PREFIX + sqlify(key));
                            } else {
                                boolean isShared = shared.contains(key);
                                if (isShared) {
                                    return unquote(appendPossiblySharedOutput(key, isShared));
                                } else {
                                    String the_table = descriptorUtils.getSqlTable(key, templateBindingsSchema).orElse(key);
                                    String new_table = newTableWithId(key);
                                    String new_id = the_table + "_id";
                                    return (Function<PrettyPrinter, QueryBuilder>) (pp) -> QueryBuilder.select(new_id).apply(pp).from(new_table);
                                }
                            }
                        },
                        (x, y) -> y,
                        LinkedHashMap::new));

        Function<String,Map<String,?>> otherInputs=
                (key) -> {
                    Optional<Map<String, String>> sqlNewInputs = descriptorUtils.getSqlNewInputs(key, templateBindingsSchema);
                    if (sqlNewInputs.isPresent()) {
                        Map<String, String> theInputs = sqlNewInputs.get();
                        descriptorUtils.checkSqlInputs(theInputs, templateBindingsSchema);
                        List<String> newIns = new ArrayList<>(theInputs.keySet());
                        Map<String,Object> m=newIns.stream().collect(Collectors.toMap(  i -> i,
                                                                                        i ->  unquote(Constants.INPUT_PREFIX + sqlify(theInputs.get(i))),
                                                                                        (x, y) -> y,
                                                                                        LinkedHashMap::new));
                        return m;
                    } else {
                        Optional<Map<String, String>> sqlAlsoOutputs = descriptorUtils.getSqlAlsoOutputs(key, templateBindingsSchema);
                        if (sqlAlsoOutputs.isPresent()) {
                            Map<String, String> theOutputs = sqlAlsoOutputs.get();
                            descriptorUtils.checkSqlOutputs(theOutputs, templateBindingsSchema);
                            List<String> newOuts = new ArrayList<>(theOutputs.keySet());
                            Map<String, Object> m = newOuts.stream().collect(Collectors.toMap(i -> i,
                                   // i -> unquote(newTableWithId(theOutputs.get(i))),
                                    i -> unquote("input_policy"),
                                    (x, y) -> y,
                                    LinkedHashMap::new));
                            return m;

                        } else {

                            return Default_Values(); // DEFAULT VALUES
                        }
                    }};





        Map<String,Function<PrettyPrinter, ?>> cteValues2=
               descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> descriptorUtils.isOutput(key, templateBindingsSchema) && !shared.contains(key))
                .collect(Collectors.toMap(  this::newTableWithId,
                        key -> {
                            String the_table = descriptorUtils.getSqlTable(key, templateBindingsSchema).orElse(key);
                            Optional<AttributeDescriptor.SqlForeign> sqlForeign = descriptorUtils.getOutputSqlForeign(key, templateBindingsSchema);
                            if (sqlForeign.isPresent()) {
                                String foreignType=Constants.INPUT_PREFIX + sqlForeign.get().getType();
                                String foreignAttribute=Constants.INPUT_PREFIX + sqlForeign.get().getAttribute();
                                String foreignItem=Constants.INPUT_PREFIX + sqlForeign.get().getItem();


                                String theTable = descriptorUtils.getSqlTable(key, templateBindingsSchema).orElse(key);

                                //(SELECT f_build_and_execute_select('creating_emission_report', input_attribute, input_item) as literal),
                                return (pp) -> new QueryBuilder(pp)
                                        .selectExp("f_build_and_execute_select(" + foreignType + ", " + foreignAttribute + ", " + foreignItem + " )")
                                        .alias(key+"_id")
                                        ;
                            }
                            return (pp) -> new QueryBuilder(pp)
                                    .insertInto(the_table)
                                    .values(otherInputs.apply(key))
                                    .returning(new LinkedList<>(){{add(the_table+".ID" + " AS " + the_table+"_id");}})
                                    ;
                        },
                        (x, y) -> y,
                        LinkedHashMap::new));


        List<String> outputs=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> descriptorUtils.isOutput(key, templateBindingsSchema) && !shared.contains(key))
                .map(key -> orig_templateName + "." + key)
                .collect(Collectors.toCollection(() -> new LinkedList<>() {{
                    add(orig_templateName + "." + tableKey);
                }}));

        // insuring that the table has more than one column, otherwise postgress converts it to a scalar
        if (outputs.size()==1) {
            outputs.add("1 AS " + DUMMY);
        }


        QueryBuilder fun=
                new QueryBuilder()
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLInsertFunction")
                        .next(QueryBuilder.createFunction(insertFunctionName))
                                .params(funParams)
                                .returns("table", functionReturns)
                                .bodyStart("")
                                .cte(cteValues2)
                                .insertInto(orig_templateName)
                                .values(insertValues2)
                                .returning(outputs)
                                .bodyEnd("");


        String sql=fun.getSQL();

        functionDeclarations.put(templateName, sql);

    }

    private String localId(String key) {
        return "_" + key + "_id";
    }


    public void generateSQLInsertArrayFunction(String templateName, String consistOf, Locations locations, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {


        templateName=locations.getShortNames().getOrDefault(templateName, templateName);
        consistOf=(consistOf==null)?null:locations.getShortNames().getOrDefault(consistOf, consistOf);

        String template_type=((consistOf==null)?templateName:consistOf)+"_type";

        Map<String, List<Descriptor>> var = templateBindingsSchema.getVar();

        final String insertFunctionName= Constants.INSERT_PREFIX+templateName+ INSERT_ARRAY_SUFFIX;


        final Predicate<String> isOutput = (key) -> descriptorUtils.isOutput(key, templateBindingsSchema);
        final Predicate<String> isInput  = (key) -> descriptorUtils.isInput (key, templateBindingsSchema);
        final Function<String,String> table  = (key) -> descriptorUtils.getSqlTable(key, templateBindingsSchema).orElse(key);


        Map<String,?> funParams=new HashMap<>() {{
            put(Constants.RECORDS_VAR, arrayOf(template_type));
        }};


        Map<String,Object> functionReturns=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(isOutput)
                .collect(Collectors.toMap(  this::sqlify,
                        key -> unquote(getTheSqlType(compilerUtil, key, templateBindingsSchema, var)),
                        (x, y) -> y,
                        () -> new LinkedHashMap<String, Object>(){{
                                put(tableKey, unquote("INT"));
                        }}));

        Map<String,Function<PrettyPrinter, ?>> cteValues1=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> isOutput.test(key) && shared.contains(key))
                .collect(Collectors.toMap(  this::table_tokens,
                        key ->  (pp0) ->
                                QueryBuilder
                                        .select(TOKEN_VAR_NAME, "id", insert_into_table(table.apply(key), additionalColumnsWithoutAlias2(templateBindingsSchema,key))) //LUC// see here how extra arg
                                        .apply(pp0)
                                        .from(
                                                (pp) ->
                                                        QueryBuilder
                                                                .select(makeArray(  TOKEN_VAR_NAME,
                                                                        nextVal(table.apply(key)),
                                                                        additionalColumnsWithAlias(templateBindingsSchema, key)))
                                                                .apply(pp)
                                                                .from((pp1) ->
                                                                                QueryBuilder
                                                                                        .select(makeArray("DISTINCT ON (" + key + ") " + key + " AS " + TOKEN_VAR_NAME,
                                                                                                additionalColumnsWithoutAlias(templateBindingsSchema,key)))
                                                                                        .apply(pp1)
                                                                                        .from(Constants.INPUT_TABLE),
                                                                        table_tokens0(key)),
                                                table_tokens(key)),
                        (x, y) -> y,
                        () -> new LinkedHashMap<String, Function<PrettyPrinter, ?>>() {{
                            put(Constants.INPUT_TABLE, pp -> QueryBuilder.select("*").apply(pp).from("unnest (" + Constants.RECORDS_VAR + ")"));
                        }}));



        List<String> insertColumns=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(isOutput)
                .map(key ->  shared.contains(key)? localId(key) : "(" + Constants.ARECORD_VAR + ")." + sqlify(key))
                .collect(Collectors.toCollection(() -> new LinkedList<>(){{
                        add("(" + Constants.ARECORD_VAR + ")." + tableKey);
                    }}));

        List<String> theArguments=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> isInput.test(key) || shared.contains(key))
                .map(key -> shared.contains(key)? table_token_id_pairs(key) + ".id" : key)
                .map(this::sqlify)
                .collect(Collectors.toList());


        String finalTemplateName = templateName;
        QueryBuilder fun =
                new QueryBuilder()
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLInsertArrayFunction")
                        .next(QueryBuilder.createFunction(insertFunctionName))
                        .params(funParams)
                        .returns("table", functionReturns)
                        .bodyStart("")
                        .cte(cteValues1)
                        .selectExp(insertColumns.toArray(ARRAY_OF_STRING))
                        .from((pp) -> shared
                                        .stream()
                                        .reduce(QueryBuilder.select(makeArray(shared.stream().map(theColumn->table_token_id_pairs(theColumn)+".id" + " AS " + localId(theColumn)).collect(Collectors.toList()),
                                                        QueryBuilder.functionCall(  "insert_" + finalTemplateName, theArguments, Constants.ARECORD_VAR))).apply(pp).from(Constants.INPUT_TABLE),
                                                (qb, s)  // (BiFunction<QueryBuilder, String, QueryBuilder>)
                                                        -> qb.join((pp1) -> QueryBuilder.select(getId(table_tokens(s)), getToken(table_tokens(s)))
                                                                .apply(pp1)
                                                                .from(table_tokens(s)), table_token_id_pairs(s))
                                                        .on(s, "=", getToken(table_token_id_pairs(s))),
                                                (qb1,qb2) -> { //(BinaryOperator<QueryBuilder>)
                                                    throw new UnsupportedOperationException();
                                                }),
                                "inserted_rows")
                        .bodyEnd("");


        String sql=fun.getSQL();



        arrayFunctionDeclarations.put(insertFunctionName, sql);




    }

    public void generateSQLInsertCompositeAndLinkerFunction(String templateName, String consistOf, Locations locations, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {


        templateName=locations.getShortNames().getOrDefault(templateName, templateName);
        consistOf=(consistOf==null)?null:locations.getShortNames().getOrDefault(consistOf, consistOf);

        String template_type=((consistOf==null)?templateName:consistOf)+"_type";

        Map<String, List<Descriptor>> var = templateBindingsSchema.getVar();

        final String insertFunctionName= Constants.INSERT_PREFIX+templateName+ INSERT_COMPOSITE_AND_LINKER_SUFFIX;
        final String insertArrayFunctionName= Constants.INSERT_PREFIX+templateName+ INSERT_ARRAY_SUFFIX;


        final Predicate<String> isOutput = (key) -> descriptorUtils.isOutput(key, templateBindingsSchema);
        final Predicate<String> isInput  = (key) -> descriptorUtils.isInput (key, templateBindingsSchema);
        final Function<String,String> table  = (key) -> descriptorUtils.getSqlTable(key, templateBindingsSchema).orElse(key);


        Map<String,?> funParams=new HashMap<>() {{
            put(Constants.RECORDS_VAR, arrayOf(template_type));
        }};


        Map<String,Object> functionReturns=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(isOutput)
                .collect(Collectors.toMap(  this::sqlify,
                        key -> unquote(getTheSqlType(compilerUtil, key, templateBindingsSchema, var)),
                        (x, y) -> y,
                        () -> new LinkedHashMap<String, Object>(){{
                                put(tableKey, unquote("INT"));
                            }}));
        functionReturns.put("parent", unquote("INT"));

        Map<String,Function<PrettyPrinter, ?>> cteValues2= new LinkedHashMap<>();
        cteValues2.put(INSERTED_CONSISTS_OF, pp -> QueryBuilder.select("*").apply(pp).from(insertArrayFunctionName + " (" + Constants.RECORDS_VAR + ")"));

        Map<String,Object> inserted_values1= new LinkedHashMap<>();
        inserted_values1.put("bean",  unquote("null"));
        inserted_values1.put("count",  ((Function<PrettyPrinter, QueryBuilder>) (pp) -> QueryBuilder.select("count(ID)").apply(pp).from(INSERTED_CONSISTS_OF)));
        inserted_values1.put("type",   "plead_transforming");

        String finalTemplateName = templateName;
        cteValues2.put(THE_RECORD, pp -> new QueryBuilder(pp).insertInto(finalTemplateName).values(inserted_values1).returning(Collections.singleton("ID")));


       // Function<PrettyPrinter, QueryBuilder> fbuild = pp -> {pp.open(); QueryBuilder qb= select("ID AS composite").apply(pp).from(THE_RECORD); pp.close(); return qb;};
        Function<PrettyPrinter, QueryBuilder> fbuild = pp -> {pp.open(); QueryBuilder qb= select("ID AS composite").apply(pp).from(THE_RECORD); pp.close(); return qb;};
        cteValues2.put(THE_PRODUCT, ((Function<PrettyPrinter, QueryBuilder>) (pp) -> QueryBuilder.select(fbuild, "ID AS simple").apply(pp).from(INSERTED_CONSISTS_OF)));

        cteValues2.put("the_linker", pp -> new QueryBuilder(pp).insertInto(finalTemplateName+ LINKER_SUFFIX + "(composite,simple) ").selectExp("*").from(THE_PRODUCT));

        List<String> insertColumns=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(isOutput)
                .map(key ->  shared.contains(key)? key : sqlify(key))  // was  "(" + Constants.ARECORD_VAR + ")." + sqlify(key)
                .collect(Collectors.toCollection(() -> new LinkedList<>(){{
                    add( "ID");
                }}));
        insertColumns.add("(select ID AS parent from " + THE_RECORD + ")");


        QueryBuilder fun =
                new QueryBuilder()
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLInsertCompositeAndLinkerFunction")
                        .next(QueryBuilder.createFunction(insertFunctionName))
                        .params(funParams)
                        .returns("table", functionReturns)
                        .bodyStart("")
                        .cte(cteValues2)
                        .selectExp(insertColumns.toArray(ARRAY_OF_STRING))
                        .from(INSERTED_CONSISTS_OF)
                        .bodyEnd("");

        String sql=fun.getSQL();




        arrayFunctionDeclarations.put(insertFunctionName, sql);




    }

    private List<String> additionalColumnsWithAlias(TemplateBindingsSchema templateBindingsSchema, String key) {
        Map<String, String> newInputs = descriptorUtils.getSqlNewInputs(key, templateBindingsSchema).orElseGet(HashMap::new);
        return newInputs.keySet().stream().map(k -> newInputs.get(k) + " AS " + k + " -- @sql.new.inputs").collect(Collectors.toList());
    }
    private List<String> additionalColumnsWithoutAlias(TemplateBindingsSchema templateBindingsSchema, String key) {
        Map<String, String> newInputs = descriptorUtils.getSqlNewInputs(key, templateBindingsSchema).orElseGet(HashMap::new);
        return newInputs.keySet().stream().map(newInputs::get).collect(Collectors.toList());
    }

    private List<String> additionalColumnsWithoutAlias2(TemplateBindingsSchema templateBindingsSchema, String key) {
        Map<String, String> newInputs = descriptorUtils.getSqlNewInputs(key, templateBindingsSchema).orElseGet(HashMap::new);
        return new ArrayList<>(newInputs.keySet());
    }



    private Object [] makeArray(List<String> strings, Function<PrettyPrinter, QueryBuilder> funarg) {
        int size = strings.size();
        Object [] result=new Object[size + 1];
        strings.toArray(result);
        result[size]=funarg;
        return result;
    }
    private Object [] makeArray(String s1, String s2, List<String> strings) {
        int size = strings.size();
        Object [] result=new Object[size + 2];
        int i=2;
        result[0]=s1;
        result[1]=s2;
        for (String s: strings) {
            result[i]=s;
            i++;
        }
        return result;
    }
    private Object [] makeArray(String s1, List<String> strings) {
        int size = strings.size();
        Object [] result=new Object[size + 1];
        int i=1;
        result[0]=s1;
        for (String s: strings) {
            result[i]=s;
            i++;
        }
        return result;
    }

    private String insert_into_table(String table) {
        return "insert_into_" + table + "(id) as id";
    }
    private String insert_into_table(String table, List<String> strings) {
        String suffix= String.join(",", strings);
        if (suffix.isBlank()) {
            return "insert_into_" + table + "(id) as _newid";
        } else {
            return "insert_into_" + table + "(id, " + suffix + ") as _newid";

        }
    }

    private String nextVal(String key) {
        return "cast(nextval('" + key + "_id_seq') as INT) as id";
    }
    static HashMap<String, Object> Default_Values() {
        return new HashMap<>();
    }
    private String newTableWithId(String key) {
        return "_" + "new_" + key + "_with_id";
    }

    private String table_tokens(String key) {
        return "_" + key + "_tokens";
    }
    private String table_tokens0(String key) {
        return "_" + key + "_tokens0";
    }
    private String getToken(String table) {
        return table+"." + TOKEN_VAR_NAME ;
    }
    private String getId(String table) {
        return table+".id";
    }
    private String table_token_id_pairs(String key) {
        return "_" + key + "_token_id_pairs";
    }
    private String table_ids(String key) {
        return "_" + key + "_ids";
    }



    static public String getTheSqlType(CompilerUtil compilerUtil, String key, TemplateBindingsSchema templateBindingsSchema, Map<String, List<Descriptor>> var) {
        final String defaultSqlType = convertToSQLType(compilerUtil.getJavaTypeForDeclaredType(var, key).getName());
        final String overrideSqlType = descriptorUtils.getSqlType(key, templateBindingsSchema);
        String theSqlType;
        if (overrideSqlType!=null && !Constants.NULLABLE_TEXT.equals(overrideSqlType) && !Constants.NON_NULLABLE_TEXT.equals(overrideSqlType)) {
            theSqlType=overrideSqlType;
        } else {
            theSqlType=defaultSqlType;
        }
        return theSqlType;
    }

    private String appendPossiblySharedOutput(String key, boolean isShared) {
        return (isShared ? Constants.SHARED_PREFIX : Constants.INPUT_PREFIX) + sqlify(key);
    }


    Map<String,String> nameMap=initNameMap();



    public String sqlify(String key) {
        return nameMap.getOrDefault(key,key);
    }


    public void generateSQLSearchRecordFunction(String baseRelation, Map<String, List<String>> templates, String templateName, Locations locations, String consistOf, String rootDir, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {

        templates=templates.entrySet().stream()
                .collect(Collectors.toMap(e -> locations.getShortNames().get(e.getKey()), Map.Entry::getValue));

        String searchFunction="search_records_for_" + baseRelation;

        Map<String,?> funParams=new HashMap<>() {{
            put("from_date", unquote("timestamptz"));
            put("to_date",   unquote("timestamptz"));
        }};

        Map<String,Object> functionReturns= new LinkedHashMap<>() {{
            put("ID",                 unquote("INT"));
            put(CREATED_AT_COLUMN,    unquote("timestamptz"));
            put(TABLE_NAME_COLUMN,    unquote("text"));
            put("key",                 unquote("INT"));

        }};

        //create string COALESCE(plead_transforming.transforming, plead_filtering.filtering) AS key,
        String coalescedKey= templates.keySet().stream().map(p -> p + ".id").collect(Collectors.joining(", ", "COALESCE (", ")"));

        Map<String, List<String>> finalTemplates = templates;
        String nameCase= templates
                .keySet()
                .stream()
                .map(p ->
                        finalTemplates
                                .get(p)
                                .stream()
                                .map(c -> p + "." + c + " IS NOT NULL ")
                                .collect(Collectors.joining( " OR ", " WHEN " , "  THEN '" + p + "'")) )
                .collect(Collectors.joining(" ", "CASE "," END"));

        QueryBuilder fun=
                new QueryBuilder()
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLSearchRecordFunction")
                        .next(QueryBuilder.createFunction(searchFunction))
                        .params(funParams)
                        .returns("table", functionReturns)
                        .bodyStart("")
                        .selectExp(baseRelation+".id as ID", CREATED_AT_COLUMN, nameCase + " AS " + TABLE_NAME_COLUMN, coalescedKey + " as " + BASETABLE_KEY_COLUMN )
                        .from(baseRelation);

        for (String template: templates.keySet()) {
            boolean first=true;
            fun.leftJoin(template);
            for (String column: templates.get(template)) {
                if (first) {
                    fun.on(baseRelation + ".id = " + template + "." + column);
                    first=false;
                } else {
                    fun.or(baseRelation + ".id = " + template + "." + column);
                }
            }
            //fun.leftJoin(template.getLeft()).on(baseRelation + ".id = " + template.getLeft() + "." + template.getRight());
        }
        fun.newline().whereOpen("from_date", "is", "null").or(CREATED_AT_COLUMN, ">", "from_date").close();
        fun.andOpen("to_date", "is", "null").or(CREATED_AT_COLUMN, "<", "to_date").close();

        boolean first= true;
        for (String template: templates.keySet()) {
            for (String column: templates.get(template)) {
                if (first) {
                    fun.begin(5).andOpen(template + "." + column, "is not", "null");
                    first = false;
                } else {
                    fun.allowBreak().or(template + "." + column, "is not", "null");
                }
            }
        };
        fun.close().end();
        fun.orderBy(CREATED_AT_COLUMN).desc();


        fun.bodyEnd("");
        String sql=fun.getSQL();

        arrayFunctionDeclarations.put(searchFunction, sql);

    }
    public void generateSQLSearchRecordByIdFunctionOLD(String baseRelation, Map<String, List<String>> templates, String templateName, String consistOf, String rootDir, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {

        String searchFunction="search_records_by_id_for_" + baseRelation;


        Map<String,?> funParams=new HashMap<>() {{
            put(INPUT_ID, unquote("INT"));
        }};
        Map<String,Object> functionReturns= new LinkedHashMap<>() {{
            put("ID",                 unquote("INT"));
            put(CREATED_AT_COLUMN,    unquote("timestamptz"));
            put(TABLE_NAME_COLUMN,    unquote("text"));
            put("key",                 unquote("INT"));

        }};

        //create string COALESCE(plead_transforming.transforming, plead_filtering.filtering) AS key,
        String coalescedKey= templates.keySet().stream().map(p -> p + ".id").collect(Collectors.joining(", ", "COALESCE (", ")"));

        String nameCase= templates
                .keySet()
                .stream()
                .map(p ->
                        templates
                                .get(p)
                                .stream()
                                .map(c ->  p + "." + c + " IS NOT NULL ")
                                .collect(Collectors.joining( " OR ", " WHEN " , "  THEN '" + p + "'")) )
                .collect(Collectors.joining(" ", "CASE "," END"));

        QueryBuilder fun=
                new QueryBuilder()
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLSearchRecordByIdFunction")
                        .next(QueryBuilder.createFunction(searchFunction))
                        .params(funParams)
                        .returns("table", functionReturns)
                        .bodyStart("")
                        .selectExp(baseRelation+".id as ID", CREATED_AT_COLUMN, nameCase + " AS " + TABLE_NAME_COLUMN, coalescedKey + " as " + BASETABLE_KEY_COLUMN )
                        .from(baseRelation);

        for (String template: templates.keySet()) {
            boolean first=true;
            fun.leftJoin(template);
            for (String column: templates.get(template)) {
                if (first) {
                    fun.on(baseRelation + ".id = " + INPUT_ID).andOpen(baseRelation + ".id = " + template + "." + column);
                    first=false;
                } else {
                    fun.or(baseRelation + ".id = " + template + "." + column);
                }
            }
            fun.close();
            //fun.leftJoin(template.getLeft()).on(baseRelation + ".id = " + template.getLeft() + "." + template.getRight());
        }


        boolean first= true;
        for (String template: templates.keySet()) {
            for (String column: templates.get(template)) {
                if (first) {
                    fun.begin(5).whereOpen(template + "." + column, "is not", "null");
                    first = false;
                } else {
                    fun.allowBreak().or(template + "." + column, "is not", "null");
                }
            }
        };
        fun.close().end();
        fun.orderBy(CREATED_AT_COLUMN).desc();


        fun.bodyEnd("");
        String sql=fun.getSQL();

        arrayFunctionDeclarations.put(searchFunction, sql);

    }

    public String sqlQuote(String s) {
        return "'" + s + "'";
    }

    public void generateSQLSearchRecordByIdFunction(String baseRelation, Map<String, List<String>> templates, String templateName, Locations locations, String consistOf, String rootDir, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {

        templates=templates.entrySet().stream()
                .collect(Collectors.toMap(e -> locations.getShortNames().get(e.getKey()), Map.Entry::getValue));

        String searchFunction="search_records_by_id_for_" + baseRelation;


        Map<String,?> funParams=new HashMap<>() {{
            put(INPUT_ID, unquote("INT"));
        }};
        Map<String,Object> functionReturns= new LinkedHashMap<>() {{
            put("ID",                 unquote("INT"));
            put(CREATED_AT_COLUMN,    unquote("timestamptz"));
            put(TABLE_NAME_COLUMN,    unquote("text"));
            put(PROPERTY_COLUMN,      unquote("text"));
            put("key",                unquote("INT"));

        }};


        QueryBuilder fun=
                new QueryBuilder()
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLSearchRecordByIdFunction")
                        .next(QueryBuilder.createFunction(searchFunction))
                        .params(funParams)
                        .returns("table", functionReturns)
                        .bodyStart("")
                ;


        boolean first=true;
        for (String template : templates.keySet()) {

            for (String column: templates.get(template)) {

                if (first) {
                    fun.selectExp(baseRelation + ".id as ID", baseRelation + "." + CREATED_AT_COLUMN, sqlQuote(template) + " AS " + TABLE_NAME_COLUMN, sqlQuote(column) + " AS " + PROPERTY_COLUMN, template + ".id as " + BASETABLE_KEY_COLUMN)
                            .from(baseRelation);
                    first = false;
                } else {
                    fun.newline().newline().union((pp) -> select(baseRelation + ".id as ID", baseRelation + "." + CREATED_AT_COLUMN, sqlQuote(template) + " AS " + TABLE_NAME_COLUMN, sqlQuote(column) + " AS " + PROPERTY_COLUMN, template + ".id as " + BASETABLE_KEY_COLUMN).apply(pp).from(baseRelation));
                }

                fun.leftJoin(template);

                fun.newline().on(baseRelation + ".id = " + INPUT_ID).and(baseRelation + ".id = " + template + "." + column);

                fun.newline().where( template + "." + column, "is not", "null");
            }
            //fun.close();
            // closing whereOpen
            //fun.end();
            // closing begin
        }




        fun.orderBy(CREATED_AT_COLUMN).desc();


        fun.bodyEnd("");
        String sql=fun.getSQL();

        arrayFunctionDeclarations.put(searchFunction, sql);

    }

    public void generateInsertIntoSharedRelation(String template, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {
        /*

CREATE OR REPLACE FUNCTION insert_into_chart (input_id BIGINT)
    returns table(id INT)
as $$
INSERT INTO chart (id)
SELECT input_id
RETURNING chart.id as id
$$ language SQL;         */

        Collection<String> sqlTables=new HashSet<>();
        for (String key: shared) {
            descriptorUtils.getSqlTable(key, templateBindingsSchema).ifPresent(sqlTables::add);
        }



        for (String relation: sqlTables) {

            // find in templateBindingsSchema all possible x, for all variable, with a field @sql.new.inputs, where x maps to relation

            List<String> extras = findExtrasValuesForRelation(templateBindingsSchema, relation);

            if (!extras.isEmpty())  System.out.println("###############################  in generateInsertIntoSharedRelation EXTRAS " + extras + " " + relation);



            String insert_into_function_name = "insert_into_" + relation;
            QueryBuilder fun =
                    new QueryBuilder()
                            .comment("Generated by method " + getClass().getName() + ".generateInsertIntoSharedRelation")
                            .next(QueryBuilder.createFunction(insert_into_function_name))
                            .params(new LinkedHashMap<>() {{
                                put(INPUT_ID, unquote("BIGINT"));
                                extras.forEach(e -> put(INPUT + "_" + e, unquote("INT")));
                            }})
                            .returns("table", new HashMap<>() {{
                                put("id", unquote("INT"));
                            }})
                            .bodyStart("")
                            .insertInto(relation).values(new LinkedHashMap<>() {{
                                put("id",  (Function<PrettyPrinter, QueryBuilder>) (pp) -> QueryBuilder.select(INPUT_ID).apply(pp));
                                extras.forEach(e -> put(e, (Function<PrettyPrinter, QueryBuilder>) (pp) -> QueryBuilder.select(INPUT + "_" + e).apply(pp)));
                            }})
                            .returning(Collections.singleton(relation + ".id as id"));


            fun.bodyEnd("");
            String sql = fun.getSQL();

            arrayFunctionDeclarations.put(insert_into_function_name, sql);

        }


    }

   static public List<String> findExtrasValuesForRelation(TemplateBindingsSchema templateBindingsSchema, String relation) {
        List<String> extras=new ArrayList<>();

        Map<String, List<Descriptor>> theVars= templateBindingsSchema.getVar();
        for (String key: theVars.keySet()) {
            Optional<Map<String, String>> sqlNewInputs = descriptorUtils.getSqlNewInputs(key, templateBindingsSchema);
            if (sqlNewInputs.isPresent()) {
                Map<String, String> theInputs = sqlNewInputs.get();
                for (String theKey: theInputs.keySet()) {
                    if (relation.equals(theInputs.get(theKey))) {
                        extras.add(theKey);
                    }
                }
            }
        }
        return extras;
    }


}
