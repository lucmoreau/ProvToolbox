package org.openprovenance.prov.template.compiler.sql;

import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.CompilerSQL.convertToSQLType;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.sql.QueryBuilder.arrayOf;
import static org.openprovenance.prov.template.compiler.sql.QueryBuilder.unquote;

public class CompilerSqlComposer {
    public static final String[] ARRAY_OF_STRING = {};
    public static final String COMPOSITE = "_composite";
    private final CompilerUtil compilerUtil=new CompilerUtil();
    final Map<String,String> functionDeclarations;
    final Map<String,String> arrayFunctionDeclarations;


    final public boolean withRelationId;
    private final String tableKey;

    public CompilerSqlComposer(boolean withRelationId, String tableKey, Map<String, String> functionDeclarations,  Map<String,String> arrayFunctionDeclarations) {
        this.withRelationId=withRelationId;
        this.tableKey=tableKey;
        this.functionDeclarations = functionDeclarations;
        this.arrayFunctionDeclarations = arrayFunctionDeclarations;
    }

    public void generateSQLInsertFunction(String jsonschema, String templateName, String root_dir, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {
        String HACK_orig_templateName=templateName.replace(COMPOSITE,"");


        Map<String, List<Descriptor>> var = templateBindingsSchema.getVar();

        final String insertFunctionName= Constants.INSERT_PREFIX+templateName;



        Map<String,?> funParams=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> descriptorUtils.isInput(key, templateBindingsSchema) || shared.contains(key))
                .collect(Collectors.toMap(key -> appendPossiblySharedOutput(key, shared.contains(key)),
                                          key -> unquote(getTheSqlType(key, templateBindingsSchema, var)),
                                          (x, y) -> y,
                                          LinkedHashMap::new));


        Map<String,Object> functionReturns=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> descriptorUtils.isOutput(key, templateBindingsSchema) && !shared.contains(key))
                .collect(Collectors.toMap(this::sqlify,
                                          key -> unquote(getTheSqlType(key, templateBindingsSchema, var)),
                                          (x, y) -> y,
                                          () -> new LinkedHashMap<String, Object>(){{
                                                            if (withRelationId) {
                                                                put(tableKey, unquote("INT"));
                                                 }}}));


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
                                    String the_table = descriptorUtils.getOutputSqlTable(key, templateBindingsSchema).orElse(key);
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
                        return Default_Values(); // DEFAULT VALUES
                    }
                };


        Map<String,Function<PrettyPrinter, ?>> cteValues2=
               descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> descriptorUtils.isOutput(key, templateBindingsSchema) && !shared.contains(key))
                .collect(Collectors.toMap(  this::newTableWithId,
                        key -> {
                            String the_table = descriptorUtils.getOutputSqlTable(key, templateBindingsSchema).orElse(key);
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
                .map(key -> HACK_orig_templateName + "." + key)
                .collect(Collectors.toCollection(() -> new LinkedList<>() {{
                    if (withRelationId) {
                        add(HACK_orig_templateName + "." + tableKey);
                    }
                }}));





        QueryBuilder fun=
                new QueryBuilder()
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLInsertFunctionWithSharing")
                        .next(QueryBuilder.createFunction(insertFunctionName))
                                .params(funParams)
                                .returns("table", functionReturns)
                                .bodyStart("")
                                .cte(cteValues2)
                                .insertInto(HACK_orig_templateName)
                                .values(insertValues2)
                                .returning(outputs)
                                .bodyEnd("");


        String sql=fun.getSQL();

        functionDeclarations.put(templateName, sql);

        System.out.println("=== PRETTY ==>\n " + sql + "\n===========");


    }

    public String templateType(String templateName) {
        String HACK_orig_templateName=templateName.replace(COMPOSITE,"");
        return HACK_orig_templateName+"_type";
    }


    public void generateSQLInsertArrayFunction(String jsonschema, String templateName, String root_dir, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {


        Map<String, List<Descriptor>> var = templateBindingsSchema.getVar();

        final String insertFunctionName= Constants.INSERT_PREFIX+templateName+"_array";


        final Predicate<String> isOutput=(key) -> descriptorUtils.isOutput(key, templateBindingsSchema);
        final Predicate<String> isInput=(key) -> descriptorUtils.isInput(key, templateBindingsSchema);




        Map<String,?> funParams=new HashMap<>() {{
            put(Constants.RECORDS_VAR, arrayOf(templateType(templateName)));
        }};


        Map<String,Object> functionReturns=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(isOutput)
                .collect(Collectors.toMap(  this::sqlify,
                                            key -> unquote(getTheSqlType(key, templateBindingsSchema, var)),
                                            (x, y) -> y,
                                            () -> new LinkedHashMap<String, Object>(){{
                                                if (withRelationId) {
                                                    put(tableKey, unquote("INT"));
                                                }}}));





        Map<String,Function<PrettyPrinter, ?>> cteValues1=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> isOutput.test(key) && shared.contains(key))
                .collect(Collectors.toMap(  this::table_tokens,
                                            key ->  (pp) -> QueryBuilder.select("token","(select cast(nextval('activity_id_seq') as INT)) as id").apply(pp)  //FIXME activity_id_seq is hard coded
                                                    .from((pp1) -> QueryBuilder.select("DISTINCT " + key + " AS " +  "token").apply(pp1).from(Constants.INPUT_TABLE),
                                                            table_tokens(key)),
                                            (x, y) -> y,
                                            () -> new LinkedHashMap<String, Function<PrettyPrinter, ?>>() {{
                                                put(Constants.INPUT_TABLE, pp -> QueryBuilder.select("*").apply(pp).from("unnest (" + Constants.RECORDS_VAR + ")"));
                                            }}));

        Map<String,Function<PrettyPrinter, Object>> cteValues2=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> isOutput.test(key) && shared.contains(key))
                .collect(Collectors.toMap(  this::table_ids,
                                            key -> (pp) -> QueryBuilder.select("insert_into_activity(id) as id").apply(pp) //FIXME insert_into_activity is hard coded
                                                    .from(table_tokens(key)),
                                            (x, y) -> y,
                                            LinkedHashMap::new));
        cteValues1.putAll(cteValues2);

        List<String> insertColumns=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> isOutput.test(key) && !shared.contains(key))
                .map(key -> "(" + Constants.ARECORD_VAR + ")." + sqlify(key))
                .collect(Collectors.toCollection(() -> new LinkedList<>(){{
                                                            if (withRelationId) {
                                                                add("(" + Constants.ARECORD_VAR + ")." + tableKey);
                                                            }}}));

        insertColumns.add("ID");


        List<String> theArguments=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> isInput.test(key) || shared.contains(key))
                .map(key -> shared.contains(key)? key + "_token_id_pairs.id" : key)
                .collect(Collectors.toList());


        String PRELUDE_TO_AUTO_GENERATE="\n\n--- PRELUDE_TO_AUTO_GENERATE\n\n " +
                "CREATE OR REPLACE FUNCTION insert_into_activity (input_id BIGINT)\n" +
                "    returns table(id INT)\n" +
                "as $$\n" +
                "INSERT INTO activity (id)\n" +
                "SELECT input_id\n" +
                "RETURNING activity.id as id\n" +
                "$$ language SQL;\n" +
                "\n" +
                "\n" +
                "DROP TYPE IF EXISTS anticipating_impact_composite_type CASCADE;\n" +
                "CREATE TYPE anticipating_impact_composite_type AS\n" +
                "(\n" +
                "    impact INT,\n" +
                "    aspect1 INT,\n" +
                "    aspect0 INT,\n" +
                "    aspect INT,\n" +
                "    organization INT,\n" +
                "    management INT,\n" +
                "    anticipating INT,\n" +
                "    time timestamptz\n" +
                ");\n\n";






        QueryBuilder fun=
                new QueryBuilder().comment(PRELUDE_TO_AUTO_GENERATE)
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLInsertArrayFunction")
                        .next(QueryBuilder.createFunction(insertFunctionName))
                                .params(funParams)
                                .returns("table", functionReturns)
                                .bodyStart("")
                                .cte(cteValues1)
                                .selectExp(insertColumns.toArray(ARRAY_OF_STRING))
                                .from((pp)-> QueryBuilder.select(table_token_id_pairs("anticipating")+".id",
                                                QueryBuilder.functionCall(  "insert_anticipating_impact_composite",theArguments, Constants.ARECORD_VAR)).apply(pp)
                                                .from(Constants.INPUT_TABLE)
                                                .join((pp1) -> QueryBuilder.select(table_tokens("anticipating") + ".id", table_tokens("anticipating") + ".token").apply(pp1)
                                                                .from("anticipating_tokens"),
                                                        table_token_id_pairs("anticipating"))
                                                .on("anticipating=",table_token_id_pairs("anticipating")+".token"),
                                        "inserted_rows")

                                .bodyEnd("");


        String sql=fun.getSQL();




        arrayFunctionDeclarations.put(insertFunctionName, sql);

        System.out.println("=== PRETTY2 ==>\n " + sql + "\n===========");



    }

    static HashMap<String, Object> Default_Values() {
        return new HashMap<>();
    }


    private String newTableWithId(String key) {
        return "new_" + key + "_with_id";
    }

    private String table_tokens(String key) {
        return key + "_tokens";
    }
    private String table_token_id_pairs(String key) {
        return key + "_token_id_pairs";
    }
    private String table_ids(String key) {
        return key + "_ids";
    }



    public String getTheSqlType(String key, TemplateBindingsSchema templateBindingsSchema, Map<String, List<Descriptor>> var) {
        final String defaultSqlType = convertToSQLType(compilerUtil.getJavaTypeForDeclaredType(var, key).getName());
        final String overrideSqlType = descriptorUtils.getSqlType(key, templateBindingsSchema);
        String theSqlType;
        if (overrideSqlType!=null && !Constants.NULLABLE_TEXT.equals(overrideSqlType)) {
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

    private Map<String, String> initNameMap() {
        Map<String,String> res=new HashMap<>();
        res.put("order", "_order");
        return res;
    }

    public String sqlify(String key) {
        return nameMap.getOrDefault(key,key);
    }


}
