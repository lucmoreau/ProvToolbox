package org.openprovenance.prov.template.compiler.sql;

import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.CompilerSQL.convertToSQLType;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.sql.QueryBuilder.*;

public class CompilerSqlComposer {
    public static final String[] ARRAY_OF_STRING = {};
    private final CompilerUtil compilerUtil=new CompilerUtil();
    final Map<String,String> functionDeclarations;


    final public boolean withRelationId;
    private final String tableKey;

    public CompilerSqlComposer(boolean withRelationId, String tableKey, Map<String, String> functionDeclarations) {
        this.withRelationId=withRelationId;
        this.tableKey=tableKey;
        this.functionDeclarations = functionDeclarations;
    }

    public void generateSQLInsertFunction(String jsonschema, String templateName, String root_dir, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {
        String HACK_orig_templateName=templateName.replace("_shared","");


        Map<String, List<Descriptor>> var = templateBindingsSchema.getVar();

        final String insertFunctionName=INSERT_PREFIX+templateName;






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

        Map<String,Object> insertValues=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .collect(Collectors.toMap(  this::sqlify,
                                            key -> {
                                                if (descriptorUtils.isInput(key, templateBindingsSchema)) {
                                                    return unquote(ConfigProcessor.INPUT_PREFIX + sqlify(key));
                                                } else {
                                                    boolean isShared = shared.contains(key);
                                                    if (isShared) {
                                                        return unquote(appendPossiblySharedOutput(key, isShared));
                                                    } else {
                                                        String the_table = descriptorUtils.getOutputSqlTable(key, templateBindingsSchema).orElse(key);
                                                        String new_table = newTableWithId(key);
                                                        String new_id = the_table + "_id";
                                                        return select(new_id).from(new_table);
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
                                                                                        i ->  unquote(ConfigProcessor.INPUT_PREFIX + sqlify(theInputs.get(i))),
                                                                                        (x, y) -> y,
                                                                                        LinkedHashMap::new));
                        return m;
                    } else {
                        return new HashMap<>(); // DEFAULT VALUES
                    }
                };


        Map<String,Object> cteValues=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> descriptorUtils.isOutput(key, templateBindingsSchema) && !shared.contains(key))
                .collect(Collectors.toMap(  this::newTableWithId,
                                            key -> {
                                                String the_table = descriptorUtils.getOutputSqlTable(key, templateBindingsSchema).orElse(key);
                                                return new QueryBuilder("")
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
                new QueryBuilder("")
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLInsertFunctionWithSharing")
                        .next(createFunction(insertFunctionName)
                                .params(funParams)
                                .returns("table", functionReturns)
                                .bodyStart("")
                                .cte(cteValues)
                                .insertInto(HACK_orig_templateName)
                                .values(insertValues)
                                .returning(outputs)
                                .bodyEnd(""));


        functionDeclarations.put(templateName, fun.getSQL());

        //System.out.println("\n\n" + fun.getSQL() + "\n\n");



    }

    public String templateType(String templateName) {
        String HACK_orig_templateName=templateName.replace("_shared","");
        return HACK_orig_templateName+"_type";
    }


    public void generateSQLInsertArrayFunction(String jsonschema, String templateName, String root_dir, TemplateBindingsSchema templateBindingsSchema, List<String> shared) {
        String HACK_orig_templateName=templateName.replace("_shared","");


        Map<String, List<Descriptor>> var = templateBindingsSchema.getVar();

        final String insertFunctionName=INSERT_PREFIX+templateName+"_array";


        final Predicate<String> isOutput=(key) -> descriptorUtils.isOutput(key, templateBindingsSchema);
        final Predicate<String> isInput=(key) -> descriptorUtils.isInput(key, templateBindingsSchema);




        Map<String,?> funParams=new HashMap<>() {{
            put(RECORDS_VAR, arrayOf(templateType(templateName)));
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





        Map<String,Object> cteValues1=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> isOutput.test(key) && shared.contains(key))
                .collect(Collectors.toMap(  this::table_tokens,
                                            key -> select("token","(select cast(nextval('activity_id_seq') as INT)) as id")  //FIXME activity_id_seq is hard coded
                                                    .from(select("DISTINCT " + key + " AS " +  "token").from(INPUT_TABLE),
                                                            table_tokens(key)),
                                            (x, y) -> y,
                                            () -> new LinkedHashMap<String, Object>() {{
                                                put(INPUT_TABLE, select("*").from("unnest (" + RECORDS_VAR + ")"));
                                            }}));

        Map<String,Object> cteValues2=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> isOutput.test(key) && shared.contains(key))
                .collect(Collectors.toMap(  this::table_ids,
                                            key -> select("insert_into_activity(id) as id")  //FIXME insert_into_activity is hard coded
                                                    .from(table_tokens(key)),
                                            (x, y) -> y,
                                            LinkedHashMap::new));
        cteValues1.putAll(cteValues2);

        List<String> insertColumns=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> isOutput.test(key) && !shared.contains(key))
                .map(key -> "(" + ARECORD_VAR + ")." + sqlify(key))
                .collect(Collectors.toCollection(() -> new LinkedList<>(){{
                                                            if (withRelationId) {
                                                                add("(" + ARECORD_VAR + ")." + tableKey);
                                                            }}}));

        insertColumns.add("ID");


        List<String> theArguments=descriptorUtils
                .fieldNames(templateBindingsSchema)
                .stream()
                .filter(key -> isInput.test(key) || shared.contains(key))
                .map(key -> shared.contains(key)? key + "_token_id_pairs.id" : key)
                .collect(Collectors.toList());



        QueryBuilder fun=
                new QueryBuilder("")
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLInsertArrayFunction")
                        .next(createFunction(insertFunctionName)
                                .params(funParams)
                                .returns("table", functionReturns)
                                .bodyStart("")
                                .cte(cteValues1)
                                .selectExp(insertColumns.toArray(ARRAY_OF_STRING))
                                .from(select(table_token_id_pairs("anticipating")+".id",
                                             functionCall(  "insert_anticipating_impact_shared",theArguments,ARECORD_VAR).getSQL())
                                                .from(INPUT_TABLE)
                                                .join(select(table_tokens("anticipating") + ".id", table_tokens("anticipating") + ".token")
                                                        .from("anticipating_tokens"),
                                                        table_token_id_pairs("anticipating"))
                                                .on("anticipating=",table_token_id_pairs("anticipating")+".token"),
                                        "inserted_rows")

                                .bodyEnd(""));


        //functionDeclarations.put(templateName, fun.getSQL());

        System.out.println("\n\n" + fun.getSQL() + "\n\n");



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
        if (overrideSqlType!=null && !NULLABLE_TEXT.equals(overrideSqlType)) {
            theSqlType=overrideSqlType;
        } else {
            theSqlType=defaultSqlType;
        }
        return theSqlType;
    }

    private String appendPossiblySharedOutput(String key, boolean isShared) {
        return (isShared ? ConfigProcessor.SHARED_PREFIX : ConfigProcessor.INPUT_PREFIX) + sqlify(key);
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
