package org.openprovenance.prov.template.compiler.sql;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

//import static org.httprpc.kilo.util.Collections.mapOf;

/**
 * Provides a fluent API for programmatically constructing and executing SQL
 * queries.
 */
public class QueryBuilder {
    private StringBuilder sqlBuilder;
    protected PrettyPrinter prettyPrinter;

    private List<String> parameters = new LinkedList<>();

    private List<Map<String, Object>> results = null;
    private int updateCount = -1;
    private List<Object> generatedKeys = null;

    /**
     * Constructs a query builder from an existing SQL query.
     *
     * @param o
     * The existing SQL query.
     */
    public QueryBuilder(ByteArrayOutputStream o) {

        if (o == null) {
            throw new IllegalArgumentException();
        }

        sqlBuilder = new StringBuilder();
        prettyPrinter= newPrettyPrinter(o);


    }

    public QueryBuilder() {
        this(new ByteArrayOutputStream());
    }

    public QueryBuilder(PrettyPrinter pp) {
        if (pp == null) {
            throw new IllegalArgumentException();
        }

        sqlBuilder = new StringBuilder();
        prettyPrinter= pp;


    }

    public PrettyPrinter getPrettyPrinter() {
        return prettyPrinter;
    }

    private static PrettyPrinter newPrettyPrinter(ByteArrayOutputStream o) {
        return new PrettyPrinter(o, 160);
    }

    private QueryBuilder(StringBuilder sqlBuilder, PrettyPrinter prettyPrinter) {
        this.sqlBuilder = sqlBuilder;
        this.prettyPrinter=prettyPrinter;
    }

    /**
     * Creates a "select" query.
     *
     * @param columns
     * The column names.
     *
     * @return
     * The new {@link QueryBuilder} instance.
     */
    public static Function<PrettyPrinter, QueryBuilder> select(Object... columns) {
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException();
        } else {

            return (pp) -> {

                StringBuilder sqlBuilder = new StringBuilder();


                pp.write("SELECT ");
                pp.begin(0);

                QueryBuilder queryBuilder = new QueryBuilder(sqlBuilder, pp);

                for (int i = 0; i < columns.length; i++) {
                    if (i > 0) {
                        pp.comma();
                    }

                    if (columns[i] instanceof String) {
                        pp.write((String)columns[i]);
                    } else {
                        pp.begin(0);
                        ((Function<PrettyPrinter,QueryBuilder>)columns[i]).apply(pp);
                        pp.end();
                    }
                }

                pp.end();
                pp.write(" "); pp.allowBreak(0);

                return queryBuilder;
            };
        }
    }

    public static Function<PrettyPrinter,QueryBuilder> functionCall(String function, List<?> values, String alias) {
        if (function == null) {
            throw new IllegalArgumentException();
        }

        return (prettyPrinter) -> {

            StringBuilder sqlBuilder = new StringBuilder();


            sqlBuilder.append(function);
            prettyPrinter.write(function);


            QueryBuilder queryBuilder = new QueryBuilder(sqlBuilder, prettyPrinter);
            queryBuilder.args(values);
            prettyPrinter.write(" ");
            queryBuilder.alias(alias);

            return queryBuilder;
        };
    }

    public static Function<PrettyPrinter,QueryBuilder> ARGS(List<?> values) {

        return (prettyPrinter) -> {

            StringBuilder sqlBuilder = new StringBuilder();


            //sqlBuilder.append(function);
            //prettyPrinter.open();


            QueryBuilder queryBuilder = new QueryBuilder(sqlBuilder, prettyPrinter);
            queryBuilder.args(values);
            return queryBuilder;
        };
    }

    public QueryBuilder selectExp(String... columns) {
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException();
        }


        prettyPrinter.write("SELECT ");
        prettyPrinter.begin(0);


        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                prettyPrinter.comma();
            }

            prettyPrinter.write(columns[i]);
        }
        prettyPrinter.end();

        prettyPrinter.newline(0);
        return this;
    }

    /**
     * Appends a "from" clause to a query.
     *
     * @param tables
     * The table names.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder from(String... tables) {
        if (tables == null || tables.length == 0) {
            throw new IllegalArgumentException();
        }



        prettyPrinter.write("FROM ");
        boolean first=true;
        for (String table: tables) {
            if (first) {
                first=false;
            } else {
                prettyPrinter.comma();
            }
            prettyPrinter.write(table);
        }

        return this;
    }

    /**
     * Appends a "from" clause to a query.
     * @param queryBuilderFun a builder for a subquery
     * @param alias the alias for the subquery
     * @return the {@link QueryBuilder} instance.

    public QueryBuilder from(QueryBuilder queryBuilder, String alias) {
        if (queryBuilder == null || alias == null) {
            throw new IllegalArgumentException();
        }

        sqlBuilder.append(" FROM (");
        sqlBuilder.append(queryBuilder.getSQL());
        sqlBuilder.append(") AS ");
        sqlBuilder.append(alias);
        sqlBuilder.append(" ");

        parameters.addAll(queryBuilder.parameters);

        return this;
    }

     */
    public QueryBuilder from(Function<PrettyPrinter,QueryBuilder> queryBuilderFun, String alias) {
        if (queryBuilderFun == null || alias == null) {
            throw new IllegalArgumentException();
        }

        prettyPrinter.write("FROM ");
        prettyPrinter.open();
        QueryBuilder queryBuilder=queryBuilderFun.apply(prettyPrinter);
        prettyPrinter.close();
        prettyPrinter.write(" ");
        prettyPrinter.write(alias);


        parameters.addAll(queryBuilder.parameters);



        return this;
    }

    /**
     * Appends a "join" clause to a query.
     *
     * @param table
     * The table name.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder join(String table) {
        if (table == null) {
            throw new IllegalArgumentException();
        }

        prettyPrinter.write(" join ");
        prettyPrinter.write(table);

        return this;
    }

    /**
     * Appends a "join" clause to a query.
     *
     * @param queryBuilderFun
     * A "select" subquery.
     *
     * @param alias
     * The subquery's alias.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder join(Function<PrettyPrinter,QueryBuilder> queryBuilderFun, String alias) {
        if (queryBuilderFun == null || alias == null) {
            throw new IllegalArgumentException();
        }


        prettyPrinter.newline(0);
        prettyPrinter.write("JOIN ");
        prettyPrinter.open();
        QueryBuilder queryBuilder=queryBuilderFun.apply(prettyPrinter);
        prettyPrinter.close();
        prettyPrinter.write(" ");
        prettyPrinter.write(alias);
        prettyPrinter.newline(0);

        parameters.addAll(queryBuilder.parameters);

        return this;
    }

    /**
     * Appends a "left join" clause to a query.
     *
     * @param table
     * The table name.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder leftJoin(String table) {
        if (table == null) {
            throw new IllegalArgumentException();
        }

        //sqlBuilder.append(" left join ");
        //sqlBuilder.append(table);

        prettyPrinter.newline(0);

        prettyPrinter.write(" LEFT JOIN ");
        prettyPrinter.write(table);

        return this;
    }

    /**
     * Appends a "right join" clause to a query.
     *
     * @param table
     * The table name.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder rightJoin(String table) {
        if (table == null) {
            throw new IllegalArgumentException();
        }

        prettyPrinter.write(" right join ");
        prettyPrinter.write(table);


        return this;
    }

    /**
     * Appends an "on" clause to a query.
     *
     * @param predicates
     * The clause predicates.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder on(String... predicates) {
        return filterOpen(false, "ON", predicates);
    }

    /**
     * Appends a "where" clause to a query.
     *
     * @param predicates
     * The clause predicates.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder where(String... predicates) {
        return filterOpen(false, " WHERE", predicates);
    }
    public QueryBuilder whereOpen(String... predicates) {
        return filterOpen(true," WHERE", predicates);
    }
    private QueryBuilder filterOpen(boolean bracket, String clause, String... predicates) {
        if (predicates == null) {
            throw new IllegalArgumentException();
        }

        prettyPrinter.write(" ");
        prettyPrinter.write(clause);
        prettyPrinter.write(" ");

        if (bracket) {
            prettyPrinter.write("(");
        }

        for (int i = 0; i < predicates.length; i++) {
            if (i > 0) {
                prettyPrinter.write(" ");
            }
            append(predicates[i]);

        }

        return this;
    }

    /**
     * Creates an "and" conditional.
     *
     * @param predicates
     * The conditional's predicates.
     *
     * @return
     * The conditional text.
     */
    public QueryBuilder and(String... predicates) {
        return conditional(" AND", predicates);
    }
    public QueryBuilder andOpen(String... predicates) {
        return conditional(true," AND", predicates);
    }


    /**
     * Creates an "or" conditional.
     *
     * @param predicates
     * The conditional's predicates.
     *
     * @return
     * The conditional text.
     */
    public QueryBuilder or(String... predicates) {
        return conditional(" OR", predicates);
    }
    private QueryBuilder conditional(String operator, String... predicates) {
        return conditional(false, operator, predicates);
    }

    private QueryBuilder conditional(boolean bracket, String operator, String... predicates) {
        if (predicates == null || predicates.length == 0) {
            throw new IllegalArgumentException();
        }

        prettyPrinter.write(operator);
        prettyPrinter.write(" ");


        if (bracket) {
            prettyPrinter.write("(");
        }


        if (predicates.length > 1)  {
            prettyPrinter.open();
        }

        for (int i = 0; i < predicates.length; i++) {
            if (i > 0) {
                prettyPrinter.write(" ");
            }
            prettyPrinter.write(predicates[i]);
        }

        if (predicates.length > 1) {
            prettyPrinter.close();
        }

        return this;

    }

    /*
    public QueryBuilder and() {
        return conditionalStart(" AND");
    }

     */

    public QueryBuilder close() {
        prettyPrinter.write(")");
        return this;

    }
    public QueryBuilder newline() {
        prettyPrinter.newline(0);
        return this;

    }
    public QueryBuilder allowBreak() {
        prettyPrinter.allowBreak(0);
        return this;
    }
    public QueryBuilder begin(int n) {
        prettyPrinter.begin(n);
        return this;
    }



    private QueryBuilder conditionalStart(String operator) {
        prettyPrinter.write(operator);
        prettyPrinter.write(" ");
        return this;

    }

    /**
     * Creates an "and" conditional group.
     *
     * @param predicates
     * The group's predicates.
     *
     * @return
     * The conditional text.
     */
    public static String allOf(String... predicates) {
        if (predicates == null || predicates.length == 0) {
            throw new IllegalArgumentException();
        }

        return conditionalGroup("and", predicates);
    }

    /**
     * Creates an "or" conditional group.
     *
     * @param predicates
     * The group's predicates.
     *
     * @return
     * The conditional text.
     */
    public static String anyOf(String... predicates) {
        if (predicates == null || predicates.length == 0) {
            throw new IllegalArgumentException();
        }

        return conditionalGroup("or", predicates);
    }

    private static String conditionalGroup(String operator, String... predicates) {
        if (predicates == null || predicates.length == 0) {
            throw new IllegalArgumentException();
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(");

        for (int i = 0; i < predicates.length; i++) {
            if (i > 0) {
                stringBuilder.append(" ");
                stringBuilder.append(operator);
                stringBuilder.append(" ");
            }

            stringBuilder.append(predicates[i]);
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    /**
     * Creates an "equal to" conditional.
     *
     * @param queryBuilder
     * The conditional's subquery.
     *
     * @return
     * The conditional text.
     */
    public static String equalTo(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException();
        }

        return String.format("= (%s)", queryBuilder);
    }

    /**
     * Creates a "not equal to" conditional.
     *
     * @param queryBuilder
     * The conditional's subquery.
     *
     * @return
     * The conditional text.
     */
    public static String notEqualTo(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException();
        }

        return String.format("!= (%s)", queryBuilder);
    }

    /**
     * Creates an "in" conditional.
     *
     * @param queryBuilder
     * The conditional's subquery.
     *
     * @return
     * The conditional text.
     */
    public static String in(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException();
        }

        return String.format("in (%s)", queryBuilder);
    }

    /**
     * Creates a "not in" conditional.
     *
     * @param queryBuilder
     * The conditional's subquery.
     *
     * @return
     * The conditional text.
     */
    public static String notIn(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException();
        }

        return String.format("not in (%s)", queryBuilder);
    }

    /**
     * Creates an "exists" conditional.
     *
     * @param queryBuilder
     * The conditional's subquery.
     *
     * @return
     * The conditional text.
     */
    public static String exists(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException();
        }

        return String.format("exists (%s)", queryBuilder);
    }

    /**
     * Creates a "not exists" conditional.
     *
     * @param queryBuilder
     * The conditional's subquery.
     *
     * @return
     * The conditional text.
     */
    public static String notExists(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException();
        }

        return String.format("not exists (%s)", queryBuilder);
    }

    /**
     * Appends an "order by" clause to a query.
     *
     * @param columns
     * The column names.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder orderBy(String... columns) {
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException();
        }

        prettyPrinter.newline(0);
        prettyPrinter.write(" ORDER BY ");
        prettyPrinter.write(String.join(", ", columns));

        return this;
    }

    /**
     * Appends outputs
     *
     * @param outputs
     * The column names.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder returning(Collection<String> outputs) {
        return returning(false,outputs);
    }

    public QueryBuilder returning(boolean brackets, Collection<String> outputs) {
        if (outputs == null || outputs.size() == 0) {
            throw new IllegalArgumentException();
        }

        prettyPrinter.newline(0);
        prettyPrinter.write("RETURNING ");

        if (brackets) {
            prettyPrinter.open();
        } else {
            prettyPrinter.begin(0);
        }
        boolean first=true;
        for (String output:outputs) {
            if (first) {
                first=false;
            } else {
                prettyPrinter.comma();
            }
            prettyPrinter.write(output);
        }
        if (brackets) {
            prettyPrinter.close();
        } else {
            prettyPrinter.end();
        }

        return this;
    }
    /**
     * Appends alias
     *
     * @param alias the alias for the subquery
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder alias(String alias) {


        prettyPrinter.write(" AS ");
        prettyPrinter.write(alias);
        prettyPrinter.write(" ");

        return this;
    }

    /**
     * Appends comment
     * @param comment the comment
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder comment(String comment) {

        prettyPrinter.newline(0);
        prettyPrinter.write("\n-- ");
        prettyPrinter.write(comment);
        prettyPrinter.newline(0);
        return this;
    }


    /**
     * Appends DEFAULT VALUES
     *
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder defaultValues() {

        prettyPrinter.write(" DEFAULT VALUES ");

        return this;
    }
    /**
     * Appends a "limit" clause to a query.
     *
     * @param count
     * The limit count.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder limit(int count) {
        if (count < 0) {
            throw new IllegalArgumentException();
        }

        sqlBuilder.append(" limit ");
        sqlBuilder.append(count);

        return this;
    }

    public QueryBuilder desc() {
        prettyPrinter.write(" DESC ");
        return this;
    }


    /**
     * Appends a "for update" clause to a query.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder forUpdate() {
        sqlBuilder.append(" for update");

        return this;
    }

    /**
     * Appends a "union" clause to a query.
     *
     * @param queryBuilderFun
     * The query builder to append.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder union(Function<PrettyPrinter,QueryBuilder> queryBuilderFun) {
        if (queryBuilderFun == null) {
            throw new IllegalArgumentException();
        }

        prettyPrinter.write(" UNION ");
        prettyPrinter.allowBreak(0);
        QueryBuilder queryBuilder=queryBuilderFun.apply(prettyPrinter);

        //sqlBuilder.append(" union ");
        //sqlBuilder.append(queryBuilder.getSQL());

        parameters.addAll(queryBuilder.parameters);

        return queryBuilder;
    }

    /**
     * Creates an "insert into" query.
     *
     * @param table
     * The table name.
     *
     * @return
     * The new {@link QueryBuilder} instance.
     */
    public QueryBuilder insertInto(String table) {
        if (table == null) {
            throw new IllegalArgumentException();
        }



        prettyPrinter.begin(1);
        prettyPrinter.write("INSERT INTO ");
        prettyPrinter.write(table);
        prettyPrinter.end();

        return this;
    }

    /**
     * Appends column values to an "insert into" query.
     *
     * @param values
     * The values to insert.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder values(Map<String, ?> values) {
        if (values == null) {
            throw new IllegalArgumentException();
        }

        if (values.isEmpty()) {
            prettyPrinter.write(" DEFAULT VALUES ");
            return this;
        }

        prettyPrinter.open();

        List<String> columns = new ArrayList<>(values.keySet());

        int n = columns.size();

        for (int i = 0; i < n; i++) {
            if (i > 0) {
                prettyPrinter.comma();
            }

            prettyPrinter.write(columns.get(i));
        }

        prettyPrinter.close();

        prettyPrinter.newline(0);
        prettyPrinter.write("VALUES ");
        prettyPrinter.open();

        for (int i = 0; i < n; i++) {
            if (i > 0) {
                prettyPrinter.comma();
            }
            encode(values.get(columns.get(i)));
        }

        prettyPrinter.close();

        return this;
    }

    /**
     * Appends arguments to a function call
     *
     * @param values
     * The values to insert.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder args(List<?> values) {
        if (values == null) {
            throw new IllegalArgumentException();
        }

        if (values.isEmpty()) {
            prettyPrinter.write("()");
            return this;
        }


        prettyPrinter.open();


        int n = values.size();

        for (int i = 0; i < n; i++) {
            if (i > 0) {
                prettyPrinter.comma();
            }

            if (values.get(i) instanceof Function) {
                prettyPrinter.write("(");
                ((Function<PrettyPrinter, QueryBuilder>) values.get(i)).apply(prettyPrinter);
                prettyPrinter.write(")");
            } else {
                prettyPrinter.write(String.valueOf(values.get(i)));
            }


        }

        prettyPrinter.close();

        return this;
    }

    /**
     * Appends commont table expression CTE
     *
     * @param values
     * The values to insert.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder cte(Map<String, Function<PrettyPrinter,?>> values) {
        if (values == null) {
            throw new IllegalArgumentException();
        }

        if (values.isEmpty()) {
            return this;
        }

        prettyPrinter.write("WITH");
        prettyPrinter.begin(2);
        prettyPrinter.newline(0);

        List<String> columns = new ArrayList<>(values.keySet());

        int n = columns.size();

        for (int i = 0; i < n; i++) {
            if (i > 0) {
                prettyPrinter.comma();
            }

            prettyPrinter.write(columns.get(i));
            prettyPrinter.write(" AS ");
            encode(values.get(columns.get(i)));
        }

        prettyPrinter.end();
        prettyPrinter.newline(0);

        return this;
    }

    /**
     * Creates an "update" query.
     *
     * @param table
     * The table name.
     *
     * @return
     * The new {@link QueryBuilder} instance.
     */
    public static QueryBuilder update(String table) {
        if (table == null) {
            throw new IllegalArgumentException();
        }

        StringBuilder sqlBuilder = new StringBuilder();
        PrettyPrinter prettyPrinter = newPrettyPrinter(null);

        sqlBuilder.append("update ");
        sqlBuilder.append(table);

        prettyPrinter.write("update ");
        prettyPrinter.write(table);

        return new QueryBuilder(sqlBuilder, prettyPrinter);
    }

    /**
     * Appends column values to an "update" query.
     *
     * @param values
     * The values to update.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder set(Map<String, ?> values) {
        if (values == null) {
            throw new IllegalArgumentException();
        }

        sqlBuilder.append(" set ");

        int i = 0;

        for (Map.Entry<String, ?> entry : values.entrySet()) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }

            sqlBuilder.append(entry.getKey());
            sqlBuilder.append(" = ");

            encode(entry.getValue());

            i++;
        }

        return this;
    }

    /**
     * Appends body end
     *
     * @param key
     * The params of the function.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder bodyEnd (String key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }


        prettyPrinter.end();
        prettyPrinter.newline(0);
        prettyPrinter.write("$");
        prettyPrinter.write(key);
        prettyPrinter.write("$ language SQL;\n");
        prettyPrinter.end();

        return this;
    }

    /**
     * Appends body start
     *
     * @param key
     * The params of the function.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder bodyStart(String key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }


        prettyPrinter.newline(0);
        prettyPrinter.begin(1);
        prettyPrinter.write("AS $");
        prettyPrinter.write(key);
        prettyPrinter.write("$");
        prettyPrinter.newline(0);


        return this;
    }

    public <T extends QueryBuilder> T next(Function<PrettyPrinter,T>  qb) {
        if (qb == null) {
            throw new IllegalArgumentException();
        }

       //sqlBuilder.append("\n").append(qb.apply(prettyPrinter).getSQL());

        prettyPrinter.newline(0);
        prettyPrinter.begin(2);


        return qb.apply(prettyPrinter);
    }

    List<Supplier<Void>> suppliers =new LinkedList<>();

    public QueryBuilder andThen (Supplier<Void> supplier) {
        suppliers.add(supplier);
        return this;
    }

    /**
     * Appends return clause to an "create function" statement.
     *
     * @param relation the relation name
     * @param params
     * The params of the function.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder returns(String relation, Map<String, ?> params) {
        if (params == null || relation==null) {
            throw new IllegalArgumentException();
        }


        prettyPrinter.newline(0);
        prettyPrinter.write("RETURNS ");
        prettyPrinter.write(relation);
        prettyPrinter.open();

        int i = 0;

        for (Map.Entry<String, ?> entry : params.entrySet()) {
            if (i > 0) {
                prettyPrinter.comma();
            }

            prettyPrinter.write(entry.getKey());
            prettyPrinter.write(" ");

            encode(entry.getValue());

            i++;
        }
        prettyPrinter.close();
        return this;
    }


    /**
     * Appends column params to an "create function" statement.
     *
     * @param params
     * The params of the function.
     *
     * @return
     * The {@link QueryBuilder} instance.
     */
    public QueryBuilder params(Map<String, ?> params) {
        if (params == null) {
            throw new IllegalArgumentException();
        }

        prettyPrinter.open();

        int i = 0;

        for (Map.Entry<String, ?> entry : params.entrySet()) {
            if (i > 0) {
                prettyPrinter.comma();
            }

            prettyPrinter.write(entry.getKey());
            prettyPrinter.write(" ");

            encode(entry.getValue());

            i++;
        }
        prettyPrinter.close();

        return this;
    }

    public QueryBuilder end() {
        for (Supplier<Void> supplier: suppliers) {
            supplier.get();
        }
        prettyPrinter.end();
        return null;
    };

    public void flush() {
        prettyPrinter.flush();
    }

    static public class FunctionQueryBuilder extends QueryBuilder {

        public FunctionQueryBuilder(StringBuilder sqlBuilder, PrettyPrinter prettyPrinter)  {
            super(sqlBuilder, prettyPrinter);
        }

        public QueryBuilder end() {
            prettyPrinter.end();
            return null;
        };
    }

    /**
     * Creates a "create function" statement.
     *
     * @param fun
     * The function name.
     *
     * @return
     * The new {@link QueryBuilder} instance.
     */
    public static Function<PrettyPrinter,FunctionQueryBuilder> createFunction(String fun) {
       return (pp) -> {
           if (fun == null) {
               throw new IllegalArgumentException();
           }

           StringBuilder sqlBuilder = new StringBuilder();

           pp.begin(1);
           pp.write("CREATE OR REPLACE FUNCTION ");
           pp.write(fun);

           return new FunctionQueryBuilder(sqlBuilder, pp);
       };
    }

    /**
     * Creates a "delete from" query.
     *
     * @param table
     * The table name.
     *
     * @return
     * The new {@link QueryBuilder} instance.
     */
    public static QueryBuilder deleteFrom(String table) {
        if (table == null) {
            throw new IllegalArgumentException();
        }

        StringBuilder sqlBuilder = new StringBuilder();
        PrettyPrinter prettyPrinter = newPrettyPrinter(null);

        sqlBuilder.append("delete from ");
        sqlBuilder.append(table);

        return new QueryBuilder(sqlBuilder, prettyPrinter);
    }

    /**
     * Executes a query.
     *
     * @param connection
     * The connection on which the query will be executed.
     *
     * @return
     * The {@link QueryBuilder} instance.
     *
     * @throws SQLException
     * If an error occurs while executing the query.
     */
    public QueryBuilder execute(Connection connection) throws SQLException {
        return execute(connection, new HashMap<>());
    }

    /**
     * Executes a query.
     *
     * @param connection
     * The connection on which the query will be executed.
     *
     * @param arguments
     * The query arguments.
     *
     * @return
     * The {@link QueryBuilder} instance.
     *
     * @throws SQLException
     * If an error occurs while executing the query.
     */
    public QueryBuilder execute(Connection connection, Map<String, ?> arguments) throws SQLException {
        if (connection == null || arguments == null) {
            throw new IllegalArgumentException();
        }

        try (PreparedStatement statement = prepare(connection)) {
            apply(statement, arguments);

            if (statement.execute()) {
                try (ResultSetAdapter resultSetAdapter = new ResultSetAdapter(statement.getResultSet())) {
                    results = resultSetAdapter.stream().collect(Collectors.toList());
                }
            } else {
                updateCount = statement.getUpdateCount();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ResultSetMetaData generatedKeysMetaData = generatedKeys.getMetaData();

                        int n = generatedKeysMetaData.getColumnCount();

                        this.generatedKeys = new ArrayList<>(n);

                        for (int i = 0; i < n; i++) {
                            this.generatedKeys.add(generatedKeys.getObject(i + 1));
                        }
                    } else {
                        this.generatedKeys = null;
                    }
                }
            }
        }

        return this;
    }

    /**
     * Returns the result of executing a query that is expected to return at
     * most a single row.
     *
     * @return
     * The query result, or {@code null} if the query either did not produce a
     * result set or did not return any rows.
     */
    public Map<String, Object> getResult() {
        if (results == null) {
            return null;
        }

        switch (results.size()) {
            case 0: {
                return null;
            }

            case 1: {
                return results.get(0);
            }

            default: {
                throw new IllegalStateException("Unexpected result count.");
            }
        }
    }

    /**
     * Returns the results of executing a query.
     *
     * @return
     * The query results, or {@code null} if the query did not produce a result
     * set.
     */
    public List<Map<String, Object>> getResults() {
        return results;
    }

    /**
     * Returns the number of rows that were affected by the query.
     *
     * @return
     * The number of rows that were affected by the query, or -1 if the query
     * did not produce an update count.
     */
    public int getUpdateCount() {
        return updateCount;
    }

    /**
     * Returns the keys that were generated by the query.
     *
     * @return
     * The keys that were generated by the query, or {@code null} if the query
     * did not produce any generated keys.
     */
    public List<Object> getGeneratedKeys() {
        return generatedKeys;
    }

    /**
     * Prepares a query for execution.
     *
     * @param connection
     * The connection on which the query will be executed.
     *
     * @return
     * A prepared statement that can be used to execute the query.
     *
     * @throws SQLException
     * If an error occurs while preparing the query.
     */
    public PreparedStatement prepare(Connection connection) throws SQLException {
        if (connection == null) {
            throw new IllegalArgumentException();
        }

        return connection.prepareStatement(getSQL(), Statement.RETURN_GENERATED_KEYS);
    }

    /**
     * Executes a query.
     *
     * @param statement
     * The statement that will be used to execute the query.
     *
     * @param arguments
     * The query arguments.
     *
     * @return
     * The query results.
     *
     * @throws SQLException
     * If an error occurs while executing the query.
     */
    public ResultSet executeQuery(PreparedStatement statement, Map<String, ?> arguments) throws SQLException {
        if (statement == null || arguments == null) {
            throw new IllegalArgumentException();
        }

        apply(statement, arguments);

        return statement.executeQuery();
    }

    /**
     * Executes a query.
     *
     * @param statement
     * The statement that will be used to execute the query.
     *
     * @param arguments
     * The query arguments.
     *
     * @return
     * The number of rows that were affected by the query.
     *
     * @throws SQLException
     * If an error occurs while executing the query.
     */
    public int executeUpdate(PreparedStatement statement, Map<String, ?> arguments) throws SQLException {
        if (statement == null || arguments == null) {
            throw new IllegalArgumentException();
        }

        apply(statement, arguments);

        return statement.executeUpdate();
    }

    private void apply(PreparedStatement statement, Map<String, ?> arguments) throws SQLException {
        int i = 1;

        for (String parameter : parameters) {
            if (parameter == null) {
                continue;
            }

            statement.setObject(i++, arguments.get(parameter));
        }
    }

    /**
     * Returns the parameters parsed by the query builder.
     *
     * @return
     * The parameters parsed by the query builder.
     */
    public Collection<String> getParameters() {
        return java.util.Collections.unmodifiableList(parameters);
    }

    /**
     * Returns the generated SQL.
     *
     * @return
     * The generated SQL.
     */
    public String getSQL() {
        flush();
        return prettyPrinter.getOutputStream().toString();
    }

    private void append(String sql) {
        boolean quoted = false;

        int n = sql.length();
        int i = 0;

        while (i < n) {
            char c = sql.charAt(i++);

            if (c == ':' && !quoted) {
                StringBuilder parameterBuilder = new StringBuilder();

                while (i < n) {
                    c = sql.charAt(i);

                    if (!Character.isJavaIdentifierPart(c)) {
                        break;
                    }

                    parameterBuilder.append(c);

                    i++;
                }

                if (parameterBuilder.length() == 0) {
                    //throw new IllegalArgumentException("Missing parameter name.");
                }

                parameters.add(parameterBuilder.toString());

                prettyPrinter.write("?");

            } else if (c == '?' && !quoted) {
                parameters.add(null);

                prettyPrinter.write(String.valueOf(c));
            } else {
                if (c == '\'') {
                    quoted = !quoted;
                }

                prettyPrinter.write(String.valueOf(c));

            }
        }
    }

    static class Unquote {
        String val;

        public Unquote(String val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return val;
        }
    }



    public static Unquote unquote(String val) {
        return new Unquote(val);
    }
    static Unquote arrayOf(String val) {
        return new Unquote(val + " []");
    }

    private void encode(Object value) {
        if (value instanceof String) {
            String string = (String)value;

            if (string.startsWith(":") || string.equals("?")) {
                append(string);
            } else {
                prettyPrinter.write("'");

                for (int i = 0, n = string.length(); i < n; i++) {
                    char c = string.charAt(i);

                    if (c == '\'') {
                        prettyPrinter.write(String.valueOf(c));
                    }

                    prettyPrinter.write(String.valueOf(c));

                }

                prettyPrinter.write("'");

            }
        } else if (value instanceof  Function) {
            Function<PrettyPrinter, QueryBuilder> queryBuilderMaker = (Function<PrettyPrinter, QueryBuilder>)value;

            //sqlBuilder.append("(");
           // sqlBuilder.append(queryBuilder.getSQL());
            //sqlBuilder.append(")");

            prettyPrinter.open();
            prettyPrinter.begin(0);
            QueryBuilder queryBuilder=queryBuilderMaker.apply(prettyPrinter);

            prettyPrinter.end();
            prettyPrinter.close();

            //prettyPrinter.write(queryBuilder.getSQL());


            parameters.addAll(queryBuilder.parameters);
        } else {
            prettyPrinter.write(value.toString());
        }
    }

    /**
     * Returns the query as a string.
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        Iterator<String> parameterIterator = parameters.iterator();

        for (int i = 0, n = sqlBuilder.length(); i < n; i++) {
            char c = sqlBuilder.charAt(i);

            if (c == '?') {
                String parameter = parameterIterator.next();

                if (parameter == null) {
                    stringBuilder.append(c);
                } else {
                    stringBuilder.append(':');
                    stringBuilder.append(parameter);
                }
            } else {
                stringBuilder.append(c);
            }
        }

        return stringBuilder.toString();
    }
}
