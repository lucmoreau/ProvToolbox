package org.openprovenance.prov.template.compiler.sql;

import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.CompilerSQL.SMALL_INDENTATION;
import static org.openprovenance.prov.template.compiler.CompilerSQL.convertToSQLType;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.sql.QueryBuilder.*;

public class CompilerSqlComposer {
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
                        descriptorUtils.checkSqlInputs(theInputs, key, templateBindingsSchema);
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



    private void generateVariableTokensTable(StringBuilder res, String key) {
        res.append("-- shared output \n");
        res
                .append(SMALL_INDENTATION).append(key).append("_tokens as (\n")
                .append(SMALL_INDENTATION).append("SELECT token,  nextval('activity_id_seq') as id\n")
                .append(SMALL_INDENTATION).append(SMALL_INDENTATION).append("FROM (SELECT DISTINCT ").append(key).append("_token as token\n")
                .append(SMALL_INDENTATION).append(SMALL_INDENTATION).append(SMALL_INDENTATION).append("FROM constant_table) AS ").append(key).append("_tokens\n")
                .append(SMALL_INDENTATION).append(")\n");
    }

    private String newTableWithId(String key) {
        return "new_" + key + "_with_id";
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
