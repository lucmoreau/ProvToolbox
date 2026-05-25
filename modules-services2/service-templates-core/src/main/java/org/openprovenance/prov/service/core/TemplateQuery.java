package org.openprovenance.prov.service.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.model.interop.PrincipalManager;
import org.openprovenance.prov.service.core.progress.ProgressListener;
import org.openprovenance.prov.service.core.progress.VizStages;
import org.openprovenance.prov.service.core.readers.TableKey;
import org.openprovenance.prov.service.core.readers.TableKeyList;
import org.openprovenance.prov.service.core.readers.SearchConfig;
import org.openprovenance.prov.template.compiler.sql.QueryBuilder;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.postgresql.util.PGobject;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.openprovenance.prov.service.core.TemplateService.provAPI;
import static org.openprovenance.prov.template.compiler.CompilerSQL.sqlify;
import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.sql.QueryBuilder.*;
import static org.openprovenance.prov.template.compiler.sql.QueryBuilder.unquote;

public class TemplateQuery {
    // declare logger
    private static final Logger logger = LogManager.getLogger(TemplateQuery.class);
    public static final String PARAM_ID = "__param_id";
    public static final String PARAM_PROPERTY = "__param_property";
    public static final String PARAM_TEMPLATE = "__param_template";
    public static final String IN_TEMPLATE = "in_template";
    public static final String IN_PROPERTY = "in_property";
    public static final String IN_ID = "in_id";
    public static final String OUT_ID = "out_id";
    public static final String OUT_TEMPLATE = "out_template";
    public static final String OUT_PROPERTY = "out_property";

    public static final String[] COMPOSITE_LINKER_COLUMNS = new String[]{"composite","simple"};
    public static final String SHA_3_512 = "SHA3-512";
    public static final String PREFIX_REL = "__";
    private final Querier querier;
    private final ProvFactory pf = new ProvFactory();
    private final CatalogueDispatcherInterface<FileBuilder> templateDispatcher;
    private final Map<String, TemplateService.Linker> compositeLinker;
    private final ObjectMapper om;
    private final Map<String, Map<String, Map<String, String>>> ioMap;
    private final Map<String, FileBuilder> documentBuilderDispatcher;


    static TypeReference<Map<String,Map<String, Map<String, String>>>> typeRef = new TypeReference<>() {};
    static TypeReference<Map<String, String>> typeRef2 = new TypeReference<>() {};
    private final Map<String, Map<String, List<String>>> successors;
    private final RelationMapping relationMapping;
    private final Map<String, String[]> propertyOrder;
    private final Map<String, String[]> simplePropertyOrder;
    private final Map<String, String> shortNames;
    private final Map<String, String> longNames;
    private final Map<String, Map<String, List<String>>> typedSuccessors;
    private final Map<String, String> semanticType;

    public TemplateQuery(Querier querier, CatalogueDispatcherInterface<FileBuilder> templateDispatcher, PrincipalManager principalManager, Map<String, TemplateService.Linker> compositeLinker, ObjectMapper om) {
        this.querier = querier;
        this.templateDispatcher = templateDispatcher;
        this.compositeLinker = compositeLinker;
        this.om = om;
        this.documentBuilderDispatcher = templateDispatcher.getDocumentBuilderDispatcher();
        Map<String, String> shortNames = getShortNames(templateDispatcher.getShortNames());
        this.shortNames=shortNames;
        this.longNames=shortNames.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        this.ioMap = shortenNames(getIoMap(templateDispatcher.getIoMap()), shortNames);
        this.successors = templateDispatcher.getSuccessors();
        this.typedSuccessors = templateDispatcher.getTypedSuccessors();
        this.relationMapping = new RelationMapping(this,templateDispatcher,querier);

        logger.debug("**** shortNames " + shortNames);
        logger.debug("ioMap = " + ioMap);
        propertyOrder = templateDispatcher.getPropertyOrder();
        simplePropertyOrder = propertyOrder.entrySet().stream().collect(Collectors.toMap(x -> shortNames.get(x.getKey()), Map.Entry::getValue));

        initializeTypedPredecessorTable();
        this.semanticType=templateDispatcher.getSemanticType();


        generateTraversalMethods(querier, this.ioMap);
        generateTraversalMethodsWithType(querier, this.ioMap, semanticType);
        //initializePredecessorTable();
    }



    private void initializeTypedPredecessorTable() {

        querier.do_statements(null,
                null,
                (sb, data) -> {
                    try {
                        regenerateTypedPredecessorTable(sb);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
        ;

    }

    Set<String> getRelations() {
        Set<String> set1=ioMap
                .get(INPUT)
                .values()
                .stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Set<String> set2=ioMap
                .get(INPUT)
                .values()
                .stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        set1.addAll(set2);
        return set1;
    }

    public Map<String, String> getLongNames() {
        return longNames;
    }

    private Map<String, Map<String, Map<String, String>>> shortenNames(Map<String, Map<String, Map<String, String>>> ioMap,
                                                                       Map<String, String> shortNames) {

        Map<String, Map<String, Map<String, String>>> res=new HashMap<>();
        res.put(INPUT,
                ioMap.get(INPUT).entrySet().stream().collect(Collectors.toMap(
                        e -> shortNames.get(e.getKey()), Map.Entry::getValue)));
        res.put(OUTPUT,
                ioMap.get(OUTPUT).entrySet().stream().collect(Collectors.toMap(
                        e -> shortNames.get(e.getKey()), Map.Entry::getValue)));

        return res;
    }

    RelationMapping getRelationMapping() {
        return relationMapping;
    }

/*
    String recursiveQuery="CREATE OR REPLACE FUNCTION backwardtraversal_star(\n" +
            "    __param_id integer, \n" +
            "    __param_template text, \n" +
            "    __param_property text\n" +
            ") RETURNS TABLE(\n" +
            "    in_id integer, \n" +
            "    in_template text, \n" +
            "    in_property text, \n" +
            "    out_id integer, \n" +
            "    out_template text, \n" +
            "    out_property text\n" +
            ")\n" +
            "AS $$\n" +
            "WITH RECURSIVE recurse_traverse AS (\n" +
            "    -- Initial query from backwardTraversal function with an additional column for recursion depth\n" +
            "    SELECT in_id, in_template, in_property, out_id, out_template, out_property, 1 AS depth\n" +
            "    FROM backwardTraversal(__param_id, __param_template, __param_property)\n" +
            "\n" +
            "    UNION \n" +
            "\n" +
            "\t-- Recursive step using the output of backwardTraversal called on new parameters obtained from predecessor_table\n" +
            "    SELECT bt.in_id, bt.in_template, bt.in_property, bt.out_id, bt.out_template, bt.out_property, rt.depth + 1 AS depth\n" +
            "    FROM recurse_traverse rt\n" +
            "    JOIN predecessor_table pt ON rt.out_template = pt.template AND rt.out_property = pt.output\n" +
            "    CROSS JOIN LATERAL backwardTraversal(rt.out_id, rt.out_template, pt.input) AS bt\n" +
            "    WHERE pt.input IS NOT NULL AND rt.depth < 100 -- Ensuring pt.input is not null and limiting recursion depth\n" +
            "\n" +
            ")\n" +
            "SELECT in_id, in_template, in_property, out_id, out_template, out_property\n" +
            "FROM recurse_traverse\n" +
            "$$ LANGUAGE SQL;";

 */

    /* now provides an output for the template, rather than input. */
    /*
    String recursiveQuery2="CREATE OR REPLACE FUNCTION backwardtraversal_star(\n" +
            "    __param_id integer, \n" +
            "    __param_template text, \n" +
            "    __param_property text\n" +
            ") RETURNS TABLE(\n" +
            "    in_id integer, \n" +
            "    in_template text, \n" +
            "    in_property text, \n" +
            "    out_id integer, \n" +
            "    out_template text, \n" +
            "    out_property text\n" +
            ")\n" +
            "AS $$\n" +
            "WITH RECURSIVE recurse_traverse AS (\n" +
            "    -- Initial query from backwardTraversal function with an additional column for recursion depth\n" +
            "\t\n" +
            "    SELECT \n" +
            "        bt.in_id, \n" +
            "        bt.in_template, \n" +
            "        bt.in_property, \n" +
            "        bt.out_id, \n" +
            "        bt.out_template, \n" +
            "        bt.out_property, \n" +
            "        1 AS depth\n" +
            "    FROM \n" +
            "        predecessor_table as pt \n" +
            "        CROSS JOIN LATERAL backwardTraversal(__param_id, __param_template, pt.input) AS bt\n" +
            "    WHERE \n" +
            "        pt.template = __param_template \n" +
            "        AND pt.output = __param_property\n" +
            "\t\n" +
            "    UNION \n" +
            "\n" +
            "\t-- Recursive step using the output of backwardTraversal called on new parameters obtained from predecessor_table\n" +
            "    SELECT bt.in_id, bt.in_template, bt.in_property, bt.out_id, bt.out_template, bt.out_property, rt.depth + 1 AS depth\n" +
            "    FROM recurse_traverse rt\n" +
            "    JOIN predecessor_table pt ON rt.out_template = pt.template AND rt.out_property = pt.output\n" +
            "    CROSS JOIN LATERAL backwardTraversal(rt.out_id, rt.out_template, pt.input) AS bt\n" +
            "    WHERE pt.input IS NOT NULL AND rt.depth < 100 -- Ensuring pt.input is not null and limiting recursion depth\n" +
            "\n" +
            ")\n" +
            "SELECT in_id, in_template, in_property, out_id, out_template, out_property\n" +
            "FROM recurse_traverse\n" +
            "$$ LANGUAGE SQL;";

     */

    String recursiveQuery3= """
                        CREATE OR REPLACE FUNCTION public.backwardtraversal_star(
                __param_id                  integer,
                __param_template            text,
                __param_property            text,
                __param_selected_relations  integer[]  DEFAULT NULL
            )
            RETURNS TABLE(
                in_id        integer,
                in_template  text,
                in_property  text,
                out_id       integer,
                out_template text,
                out_property text
            )
            LANGUAGE sql
            AS $function$
            WITH RECURSIVE recurse_traverse AS (
            
                -- ── Base step ─────────────────────────────────────────────────────────────
                -- Find the immediate predecessors of the starting node.
                -- The relation-type filter is applied here so that only edges of the
                -- requested types are followed from the very first hop.
                SELECT
                    bt.in_id,
                    bt.in_template,
                    bt.in_property,
                    bt.out_id,
                    bt.out_template,
                    bt.out_property,
                    1 AS depth
                FROM
                    predecessor_table AS pt
                    CROSS JOIN LATERAL backwardTraversal(
                        __param_id,
                        __param_template,
                        pt.input
                    ) AS bt
                WHERE
                    pt.template = __param_template
                    AND pt.output = __param_property
                    AND (
                        __param_selected_relations IS NULL
                        OR pt.rel = ANY(__param_selected_relations)
                    )
            
                UNION   -- UNION (not UNION ALL) provides cycle-safety via row deduplication
            
                -- ── Recursive step ────────────────────────────────────────────────────────
                -- Extend the frontier one hop at a time.  The relation-type filter is
                -- re-applied at every hop so the constraint is honoured throughout the
                -- entire traversal, not just at the starting node.
                SELECT
                    bt.in_id,
                    bt.in_template,
                    bt.in_property,
                    bt.out_id,
                    bt.out_template,
                    bt.out_property,
                    rt.depth + 1 AS depth
                FROM
                    recurse_traverse rt
                    JOIN predecessor_table pt
                        ON  rt.out_template = pt.template
                        AND rt.out_property = pt.output
                        AND (
                            __param_selected_relations IS NULL
                            OR pt.rel = ANY(__param_selected_relations)
                        )
                    CROSS JOIN LATERAL backwardTraversal(
                        rt.out_id,
                        rt.out_template,
                        pt.input
                    ) AS bt
                WHERE
                    pt.input IS NOT NULL
                    AND rt.depth < 100   -- guard against runaway cycles in malformed graphs
            
            )
            SELECT in_id, in_template, in_property, out_id, out_template, out_property
            FROM   recurse_traverse
            -- Self-loops arise from the virtual-output fix for activity-output-only templates
            -- (e.g. document_obligating_coin): backwardTraversal joins the table with itself
            -- on an input column used as a virtual output, matching the same row.
            -- Self-loops are never valid in a provenance graph, so filter them here.
            WHERE NOT (in_id = out_id AND in_template = out_template)
            $function$;

            """;

    private void generateTraversalMethods(Querier querier,  Map<String,Map<String, Map<String, String>>> ioMap) {

        // Step 1: create/truncate/repopulate backward_dispatch so the PL/pgSQL
        // backwardTraversal function can look up valid hops at query time.
        querier.do_statements(null,
                null,
                (sb, data) -> sb.append(generateClearAndPopulateBackwardDispatch(ioMap)));

        // Step 2: ensure the secondary indexes required by backwardTraversal's
        // dynamic UNION ALL exist on every referenced template table.  Without
        // these the per-call query degrades to seq scans (~25× slower).
        querier.do_statements(null,
                null,
                (sb, data) -> sb.append(generateCreateTraversalIndexes(ioMap)));

        // Step 3: install backwardTraversal (queries backward_dispatch) and
        // backwardtraversal_star (calls backwardTraversal recursively).
        querier.do_statements(null,
                null,
                (sb, data) -> {
                    sb.append(generateBackwardTemplateTraversal(ioMap));
                    sb.append(recursiveQuery3);
                });
    }

    /**
     * Generates and installs both typed traversal SQL functions into the database.
     *
     * <p>Calls {@link #generateBackwardTemplateTraversalWithType} and
     * {@link #generateBackwardTemplateTraversalStarWithType} in sequence, executing the
     * resulting {@code CREATE OR REPLACE FUNCTION} statements via the supplied
     * {@link Querier}.
     *
     * @param querier      database access helper used to execute the generated DDL
     * @param ioMap        input/output map (short SQL names) — passed through to the
     *                     single-hop generator
     * @param semanticType fully-qualified template name → semantic-type column name;
     *                     entries with {@code null} values denote templates that have
     *                     no semantic-type column
     */
    private void generateTraversalMethodsWithType(Querier querier, Map<String, Map<String, Map<String, String>>> ioMap, Map<String, String> semanticType) {
        querier.do_statements(null,
                null,
                (sb, data) -> {
                    sb.append(generateBackwardTemplateTraversalWithType(ioMap, semanticType));
                    sb.append(generateBackwardTemplateTraversalStarWithType(semanticType));
                  //  System.out.println(sb.toString());
                });
    }

    /**
     * Generates the SQL {@code CREATE OR REPLACE FUNCTION} statement for
     * {@code public.backwardtraversal_typed}.
     *
     * <p>{@code backwardtraversal_typed} is the single-hop variant of the typed
     * traversal: it wraps one call to {@code backwardtraversal} and annotates each
     * returned edge with {@code in_type} and {@code out_type} columns by looking up
     * the semantic-type value from the appropriate template table (as identified by
     * the {@code semanticType} map).
     *
     * @param ioMap        input/output property map (short SQL table names) — not used
     *                     directly in this generator but kept for API symmetry with
     *                     {@link #generateBackwardTemplateTraversal}
     * @param semanticType fully-qualified template name → semantic-type column name;
     *                     entries with {@code null} values are filtered out before SQL
     *                     generation
     * @return a {@code CREATE OR REPLACE FUNCTION} SQL string ready to execute
     */
    private String generateBackwardTemplateTraversalWithType(
            Map<String, Map<String, Map<String, String>>> ioMap,
            Map<String, String> semanticType) {
        //System.out.println("generateBackwardTemplateTraversalWithType: " + semanticType + " " + shortNames);
        return generateTypedWrapperFunction(
                "backwardtraversal_typed",
                null,
                "backwardtraversal(" + PARAM_ID + ", " + PARAM_TEMPLATE + ", " + PARAM_PROPERTY + ")",
                shortenAndFilterSemanticType(semanticType));
    }

    /**
     * Generates the SQL {@code CREATE OR REPLACE FUNCTION} statement for
     * {@code public.backwardtraversal_star_typed}.
     *
     * <p>{@code backwardtraversal_star_typed} is the full-graph (star) variant of the
     * typed traversal.  Rather than running type-lookup subqueries at every recursive
     * hop (which would multiply cost with traversal depth), it wraps the lean
     * {@code backwardtraversal_star} function and annotates the <em>final</em> result
     * set once with {@code in_type} and {@code out_type}.
     *
     * <p>The optional {@code __param_selected_relations integer[] DEFAULT NULL}
     * parameter is forwarded unchanged to the inner {@code backwardtraversal_star}
     * call, allowing callers to restrict traversal to a subset of PROV relation kinds.
     *
     * @param semanticType fully-qualified template name → semantic-type column name;
     *                     entries with {@code null} values are filtered out before SQL
     *                     generation
     * @return a {@code CREATE OR REPLACE FUNCTION} SQL string ready to execute
     */
    private String generateBackwardTemplateTraversalStarWithType(Map<String, String> semanticType) {
        return generateTypedWrapperFunction(
                "backwardtraversal_star_typed",
                "    __param_selected_relations  integer[]  DEFAULT NULL",
                "backwardtraversal_star(" + PARAM_ID + ", " + PARAM_TEMPLATE + ", " + PARAM_PROPERTY + ", __param_selected_relations)",
                shortenAndFilterSemanticType(semanticType));
    }

    /**
     * Converts a fully-qualified semantic-type map to its SQL-table-name form,
     * filtering out entries whose value is {@code null}.
     *
     * <p>The {@code semanticType} map supplied by the template dispatcher uses
     * fully-qualified template names as keys (e.g.
     * {@code "com.silubi.odoo.goods-transforming"}).  The generated SQL functions
     * use short SQL table names (e.g. {@code "goods_transforming"}).  This helper
     * performs that translation via {@link #shortNames} and drops any entry whose
     * value is {@code null} (i.e. templates that carry no semantic-type column).
     *
     * <p>Dropping null values is important because
     * {@link java.util.stream.Collectors#toMap toMap} internally calls
     * {@link java.util.HashMap#merge merge}, which rejects {@code null} values and
     * would throw a {@link NullPointerException}.
     *
     * @param semanticType raw map from the template dispatcher (may contain nulls)
     * @return a new map with short keys and no null values, safe to pass to
     *         {@link #appendAtypeCase}
     */
    private Map<String, String> shortenAndFilterSemanticType(Map<String, String> semanticType) {
        return semanticType.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .collect(Collectors.toMap(
                        e -> shortNames.getOrDefault(e.getKey(), e.getKey()),
                        Map.Entry::getValue,
                        (v1, v2) -> v1));
    }

    /**
     * Shared SQL builder for the two typed traversal wrappers.
     *
     * <p>Generates a complete {@code CREATE OR REPLACE FUNCTION public.<typedFunctionName>}
     * statement.  The function signature always includes the standard three traversal
     * parameters ({@value #PARAM_ID}, {@value #PARAM_TEMPLATE}, {@value #PARAM_PROPERTY})
     * plus an optional fourth parameter line supplied by the caller.  The return type is
     * the six standard traversal columns augmented with {@code in_type} and {@code out_type}.
     *
     * <p>Type lookup is done with a pure-SQL
     * {@code COALESCE(CASE bt.<templateCol> WHEN '<table>' THEN (SELECT <atypeCol> FROM <table>
     * WHERE id = bt.<idCol>) … ELSE 'None1' END, 'None2')} expression, avoiding any
     * PL/pgSQL dynamic SQL.  PostgreSQL short-circuits {@code CASE} branches, so only the
     * matching table's index is hit.
     *
     * <p>This method is the shared implementation for both
     * {@link #generateBackwardTemplateTraversalWithType} (single-hop) and
     * {@link #generateBackwardTemplateTraversalStarWithType} (full-graph star), which
     * differ only in {@code typedFunctionName}, {@code extraParam}, and {@code innerCall}.
     *
     * @param typedFunctionName name of the PostgreSQL function to create
     * @param extraParam        optional extra parameter declaration line to append after the
     *                          three standard parameters (e.g.
     *                          {@code "    __param_selected_relations  integer[]  DEFAULT NULL"}),
     *                          or {@code null} for none
     * @param innerCall         SQL expression used as the {@code FROM} source — typically a
     *                          call to the untyped base function with its argument list
     * @param shortSemanticType map of short SQL table name → semantic-type column name,
     *                          pre-filtered so no values are {@code null}
     *                          (see {@link #shortenAndFilterSemanticType})
     * @return a complete {@code CREATE OR REPLACE FUNCTION} SQL string
     */
    private String generateTypedWrapperFunction(
            String typedFunctionName,
            String extraParam,
            String innerCall,
            Map<String, String> shortSemanticType) {

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE OR REPLACE FUNCTION public.").append(typedFunctionName).append("(\n");
        sb.append("    ").append(PARAM_ID)       .append("       integer,\n");
        sb.append("    ").append(PARAM_TEMPLATE) .append(" text,\n");
        sb.append("    ").append(PARAM_PROPERTY) .append(" text");
        if (extraParam != null) {
            sb.append(",\n").append(extraParam).append("\n");
        } else {
            sb.append("\n");
        }
        sb.append(")\n");
        sb.append("RETURNS TABLE(\n");
        sb.append("    ").append(IN_ID)       .append("        integer,\n");
        sb.append("    ").append(IN_TEMPLATE) .append("  text,\n");
        sb.append("    ").append(IN_PROPERTY) .append("  text,\n");
        sb.append("    in_type      text,\n");
        sb.append("    ").append(OUT_ID)      .append("       integer,\n");
        sb.append("    ").append(OUT_TEMPLATE).append(" text,\n");
        sb.append("    ").append(OUT_PROPERTY).append(" text,\n");
        sb.append("    out_type     text\n");
        sb.append(")\n");
        sb.append("LANGUAGE sql\n");
        sb.append("AS $$\n");
        sb.append("    SELECT\n");
        sb.append("        bt.").append(IN_ID)       .append(",\n");
        sb.append("        bt.").append(IN_TEMPLATE) .append(",\n");
        sb.append("        bt.").append(IN_PROPERTY) .append(",\n");
        appendAtypeCase(sb, IN_TEMPLATE,  IN_ID,  "in_type",  shortSemanticType);
        sb.append(",\n");
        sb.append("        bt.").append(OUT_ID)      .append(",\n");
        sb.append("        bt.").append(OUT_TEMPLATE).append(",\n");
        sb.append("        bt.").append(OUT_PROPERTY).append(",\n");
        appendAtypeCase(sb, OUT_TEMPLATE, OUT_ID, "out_type", shortSemanticType);
        sb.append("\n");
        sb.append("    FROM ").append(innerCall).append(" bt\n");
        sb.append("$$;\n");

        return sb.toString();
    }

    /**
     * Appends a type-lookup SQL expression to {@code sb}.
     *
     * <p>The generated fragment has the form:
     * <pre>{@code
     * COALESCE(
     *     CASE bt.<templateCol>
     *         WHEN '<table1>' THEN (SELECT <atypeCol> FROM <table1> WHERE id = bt.<idCol>)
     *         WHEN '<table2>' THEN (SELECT <atypeCol> FROM <table2> WHERE id = bt.<idCol>)
     *         …
     *         ELSE 'None1'
     *     END,
     *     'None2'
     * ) AS <alias>
     * }</pre>
     *
     * <p>Sentinel meanings:
     * <ul>
     *   <li>{@code 'None1'} — the template table has no semantic-type column; no
     *       entry for that table exists in {@code semanticType}.</li>
     *   <li>{@code 'None2'} — the table has a semantic-type column but the stored
     *       value for this specific row is {@code NULL}.</li>
     * </ul>
     *
     * <p>When {@code semanticType} is empty (all entries were null and were filtered
     * out by {@link #shortenAndFilterSemanticType}), a bare {@code CASE} with only
     * an {@code ELSE} clause would be invalid SQL.  In that case this method emits
     * the literal {@code 'None1' AS <alias>} directly.
     *
     * @param sb           builder to append into
     * @param templateCol  traversal output column holding the template name
     *                     ({@value #IN_TEMPLATE} or {@value #OUT_TEMPLATE})
     * @param idCol        traversal output column holding the row id
     *                     ({@value #IN_ID} or {@value #OUT_ID})
     * @param alias        SQL column alias for the result ({@code in_type} or
     *                     {@code out_type})
     * @param semanticType pre-filtered map of SQL table name → semantic-type column
     *                     name; must contain no {@code null} values
     */
    private void appendAtypeCase(StringBuilder sb,
                                  String templateCol,
                                  String idCol,
                                  String alias,
                                  Map<String, String> semanticType) {
        if (semanticType.isEmpty()) {
            // No WHEN branches available — a bare CASE with only ELSE is invalid SQL.
            // Every table falls into the "no semantic-type column" category.
            sb.append("        'None1' AS ").append(alias);
            return;
        }
        sb.append("        COALESCE(\n");
        sb.append("            CASE bt.").append(templateCol).append("\n");
        for (Map.Entry<String, String> entry : semanticType.entrySet()) {
            sb.append("                WHEN '").append(entry.getKey())
              .append("' THEN (SELECT ").append(entry.getValue())
              .append(" FROM ").append(entry.getKey())
              .append(" WHERE id = bt.").append(idCol).append(")\n");
        }
        sb.append("                ELSE 'None1'\n");
        sb.append("            END,\n");
        sb.append("            'None2'\n");
        sb.append("        ) AS ").append(alias);
    }


    public void generateViz(Integer id, String template, String property, String style, Map<String, String> parameters, Map<String, Map<String, String>> baseTypes, String iconsFolderForGraphviz, Map<String, String> semanticType, String principal, OutputStream out, ProgressListener listener) {

        //logger.info("generateViz " + id + " " + template + " " + property);
        Set<StatementOrBundle.Kind> selectedVizKinds=processParameters(parameters);

        Map<String, Map<String, List<String>>> successors = selectSuccessors(typedSuccessors,selectedVizKinds) ;

        logger.debug("typedSuccessors: "+typedSuccessors);
        logger.debug("selectedVizKinds: "+selectedVizKinds);
        logger.debug("selected successors: "+successors);

        logger.debug("semanticType: "+semanticType);

        listener.started(VizStages.SQL);
        long sqlStart = System.nanoTime();
        List<TemplateConnection> templateConnections;
        try {
            templateConnections = recursiveTraversal(id, template, property, selectedVizKinds, principal);
            listener.done(VizStages.SQL, (System.nanoTime() - sqlStart) / 1_000_000);
        } catch (RuntimeException e) {
            listener.failed(VizStages.SQL, e);
            throw e;
        }
        listener.detail(VizStages.SQL, templateConnections.size() + " connections");
        logger.info("(id,template,property,selectedVizKinds,templateConnections): "+ id + ", " + template + ", " + selectedVizKinds + ", " + templateConnections.size());
        // reverse list
        Collections.reverse(templateConnections);

        boolean withIcons= (parameters != null) && Objects.equals(parameters.get("icons"),"true");



        logger.debug("templateConnections: " + templateConnections.stream().map(TemplateConnection::toString).collect(Collectors.joining("\n")));
        new TemplatesToDot(templateConnections, style, withIcons, iconsFolderForGraphviz, parameters, baseTypes, ioMap, templateDispatcher, successors, pf, this, principal, provAPI).convert(null, out, "template_connections", listener);
    }


    private Set<StatementOrBundle.Kind> processParameters(Map<String, String> parameters) {
        Set<StatementOrBundle.Kind> selectedVizKinds;
        if (parameters==null) {
            selectedVizKinds=new HashSet<>();
            selectedVizKinds.add(StatementOrBundle.Kind.PROV_DERIVATION); // always include derivations by default
        } else {
            String visKinds=parameters.get("successor-relations");
            if (visKinds==null) {
                selectedVizKinds=new HashSet<>();
                selectedVizKinds.add(StatementOrBundle.Kind.PROV_DERIVATION);
            } else {
                selectedVizKinds=Arrays.stream(visKinds.trim().split(",")).map(StatementOrBundle.Kind::valueOf).collect(Collectors.toSet());
            }
        }
        logger.debug("Selected Viz Kinds: " + selectedVizKinds);
        return selectedVizKinds;
    }

    final DigestUtils sha512 = new DigestUtils(DigestUtils.getSha3_512Digest());

    public Map<String, String> computeHash(String template, int id, Object[] record) {

        StringBuilder sb=new StringBuilder();
        sb.append(id).append(",");
        Function<Object[], String> fun = templateDispatcher.getCsvConverter().get((String) record[0]);
        if (fun==null) {
            logger.warn("no csv converter for template " + template);
            sb.append("no csv converter");
        } else {
            sb.append(fun.apply(record));
        }

        String csv = sb.toString();
        String hash2=sha512.digestAsHex(csv);
        Map<String,String> map=new LinkedHashMap<>();
        map.put(SHA_3_512, hash2);
        map.put("csv", csv);
        return map;
    }

    public Map<String, String> computeHash(String templateFullyQualifiedName, int id, List<Object[]> records) {
        StringBuilder sb=new StringBuilder();
        sb.append(id).append(",").append(templateFullyQualifiedName).append(",").append(records.size());
        for (Object[] record: records) {
            sb.append("\n");
            sb.append(templateDispatcher.getCsvConverter().get((String)record[0]).apply(record));
        }

        String csv = sb.toString();
        String hash2=sha512.digestAsHex(csv);
        Map<String,String> map=new LinkedHashMap<>();
        map.put(SHA_3_512, hash2);
        map.put("csv", csv);
        return map;
    }


    public void updateHash(String shortName, int id, Map<String,String> hash, String principal) {
        querier.do_statements(null,
                null,
                (sb, data) -> {
                    sb.append("UPDATE record_index");
                    sb.append(" SET hash='");
                    sb.append(makeHashRecord(hash));
                    sb.append("' WHERE key=");
                    sb.append(id);
                    sb.append(" AND principal='");
                    sb.append(principal);
                    sb.append("'");
                    sb.append(" AND table_name='");
                    sb.append(shortName);
                    sb.append("'");
                });
    }

    public Map<String, String> retrieveHash(String fullyQualifiedName, int id, String principal) {

        Map<String,String> map=new HashMap<>();

        return querier.do_query(map,
                null,
                (sb, data) -> {
                    sb.append("SELECT hash FROM record_index WHERE key=");
                    sb.append(id);
                    sb.append(" AND principal='");
                    sb.append(principal);
                    sb.append("'");
                    sb.append(" AND table_name='");
                    sb.append(shortNames.get(fullyQualifiedName));
                    sb.append("'");
                },
                (rs, data) -> {
                    while (rs.next()) {
                        String hash1 = rs.getString("hash");
                        try {
                            Map<String, String> hash2 = om.readValue(hash1, Map.class);
                            data.putAll(hash2);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }



     String makeHashRecord(Map<String,String> persistedMap) {
        Map<String, String> map = new HashMap<>();
        map.putAll(persistedMap);
        map.remove("csv");
        try {
            return om.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> makeSignatureMap(List<String> hash) {
        Map<String,String> map=new LinkedHashMap<>();
        map.put(SHA_3_512, hash.get(0));
        return map;
    }


    static public class QualifiedTemplateName {
        public String template;
        public QualifiedTemplateName (String template) {
            this.template=template;
        }
    }
    static public class TemplateConnection {
        public Integer in_id;
        public String in_template;
        public String in_property;
        public String in_type;
        public Integer out_id;
        public String out_template;
        public String out_property;
        public String out_type;

        @Override
        public String toString() {
            return "TemplateConnection{" +
                    "in_id=" + in_id +
                    ", in_template='" + in_template + '\'' +
                    ", in_property='" + in_property + '\'' +
                    ", in_type='" + in_type + '\'' +
                    ", out_id=" + out_id +
                    ", out_template='" + out_template + '\'' +
                    ", out_property='" + out_property + '\'' +
                    ", out_type='" + out_type + '\'' +
                    '}';
        }
    }

    public List<TemplateConnection> recursiveTraversal(Integer id, String template, String property, Set<StatementOrBundle.Kind> selectedVizKinds, String principal) {
        List<TemplateConnection> the_records = new LinkedList<>();

        String selectedAsASql=selectedVizKinds.stream().map(x -> ("" + x.ordinal())).collect(Collectors.joining(",","ARRAY[",  "]"));
        querier.do_query(the_records,
                null,
                (sb, data) -> {
                    sb.append("SELECT DISTINCT * FROM ");
                    sb.append("backwardtraversal_star_typed(");
                    sb.append(id);
                    sb.append(",'");
                    sb.append(template);
                    sb.append("','");
                    sb.append(property);
                    sb.append("', ");
                    sb.append(selectedAsASql);
                    sb.append(") as template_connection\n");
                    joinAccessControl("template_connection.in_template", principal, sb, "template_connection", "in_id");
                    andAccessControl(principal, sb);
                },
                (rs, data) -> {
                    while (rs.next()) {
                        TemplateConnection record = new TemplateConnection();
                        record.in_id=rs.getObject("in_id", Integer.class);
                        record.in_template=longNames.get(rs.getObject("in_template", String.class));
                        record.in_property=rs.getObject("in_property", String.class);
                        record.in_type=rs.getObject("in_type", String.class);
                        record.out_id=rs.getObject("out_id", Integer.class);
                        record.out_template=longNames.get(rs.getObject("out_template", String.class));
                        record.out_property=rs.getObject("out_property", String.class);
                        record.out_type=rs.getObject("out_type", String.class);
                        data.add(record);
                    }
                });

        return the_records;
    }

    Document constructDocument(Collection<Object[]> the_records) {
        return constructDocument(documentBuilderDispatcher, the_records);
    }


    public Document constructDocument(Map<String, FileBuilder> documentBuilderDispatcher, Collection<Object[]> the_records) {
            IndexedDocument iDoc = new IndexedDocument(pf, pf.newDocument());
            for (Object[] record : the_records) {
                FileBuilder builder = documentBuilderDispatcher.get((String)record[0]); // expected to be a long name already
                if (builder != null) {
                    Document doc = builder.make(record);
                    iDoc.merge(doc);

                } else {
                    throw new UnsupportedOperationException("unknown record " + record[0] + " " + Arrays.asList(record));
                }
            }
            return iDoc.toDocument();
        }

    public List<Object[]> query(String fullyQualifiedName, Integer id, boolean withTitles, String principal) {
        if (isComposite(fullyQualifiedName)) {
            return queryComposite(fullyQualifiedName, id, withTitles, principal);
        } else {
            return querySimple(fullyQualifiedName, id, withTitles, principal);
        }
    }

    private boolean isComposite(String template) {
        return compositeLinker.containsKey(template);
    }

    public List<Object[]> querySimple(String fullyQualifiedName, Integer id, boolean withTitles, String principal) {
        logger.debug("querySimple fullyQualifiedName=" + fullyQualifiedName + " id=" + id + " principal=" + principal);
        //System.out.println("querySimple fullyQualifiedName=" + fullyQualifiedName + " id=" + id + " principal=" + principal);
        List<Object[]> the_records = new LinkedList<>();
        if (!fullyQualifiedName.contains(".")) {
            throw new IllegalArgumentException("template table name must be a composite name including '.' " + fullyQualifiedName);

        }
        String[] propertyOrder= this.propertyOrder.get(fullyQualifiedName);
        String shortName= this.shortNames.get(fullyQualifiedName);
        if (shortName==null) {
            throw new IllegalArgumentException("no short name for template qualified name " + fullyQualifiedName);
        }
        //System.out.println("propertyOrder = " + Arrays.toString(propertyOrder));
        querier.do_query(the_records,
                null,
                (sb, data) -> {
            /* generate following query
                    SELECT template.*
                    FROM plead_transforming AS template
                    LEFT JOIN record_index
                    ON record_index.key = template.id
                    AND record_index.table_name = 'plead_transforming'
                    AND record_index.principal IS NOT NULL
                    LEFT JOIN access_control
                    ON access_control.record = record_index.id
                    AND access_control.authorized = 'joe'
                    WHERE template.id = 478
                    AND (record_index.principal = 'joe' OR access_control.record IS NOT NULL);

             */

                    sb.append("\n--- " + fullyQualifiedName + "\n");

                    sb.append("SELECT template.*\n FROM ");
                    sb.append(shortName);
                    sb.append(" as template ");
                    joinAccessControl(shortName, principal, sb);
                    sb.append("\n WHERE template.id=");
                    sb.append(id);
                    andAccessControl(principal, sb);

                },
                (rs, data) -> {
                    while (rs.next()) {
                        Object[] record = new Object[propertyOrder.length];
                        record[0]=fullyQualifiedName;
                        for (int i = 1; i < record.length; i++) {
                            // ISSUE, these are the sql names, not the property names
                            String columnLabel = sqlify(propertyOrder[i]);
                            Object o = rs.getObject(columnLabel);
                            if (o instanceof Timestamp) {
                                o = ((Timestamp)o).toInstant().toString();
                            }
                            record[i] = o;
                        }
                        data.add(record);
                    }
                });

        return the_records;
    }

    public void whereAccessControl(String principal, StringBuilder sb) {
        sb.append("\n WHERE (record_index.principal='");
        sb.append(principal);
        sb.append("' OR access_control.record IS NOT NULL)");
    }
    public void andAccessControl(String principal, StringBuilder sb) {
        sb.append("\n AND (record_index.principal='");
        sb.append(principal);
        sb.append("' OR access_control.record IS NOT NULL)");
    }

    public void joinAccessControl(String template_table_name, String principal, StringBuilder sb) {
        joinAccessControl(template_table_name, principal, sb, "template", "id");
    }

    public void joinAccessControl(String template_table_name, String principal, StringBuilder sb, String label, String id) {
        sb.append("\n LEFT JOIN record_index ON record_index.key=").append(label).append(".").append(id);
        if (template_table_name.startsWith(label)) {  // TOCHECK, this seems always false with full qualified name
            sb.append("\n AND record_index.table_name=");
            sb.append(template_table_name);
        } else {
            sb.append("\n AND record_index.table_name='");
            sb.append(template_table_name);
            sb.append("'");
        }
        sb.append("\n AND record_index.principal IS NOT NULL");
        sb.append("\n LEFT JOIN access_control\n ON access_control.record=record_index.id");
        sb.append("\n AND access_control.authorized='");
        sb.append(principal);
        sb.append("'");
    }

    public List<RecordEntry2> queryTemplatesRecords(SearchConfig config, String principal, boolean includeComposite) {
        String base_relation = config.base_relation;
        String from_date = config.from_date;
        String to_date = config.to_date;
        Integer limit = config.limit;

        if (from_date != null) {
            from_date = "'" + from_date + "'";
        }
        if (to_date != null) {
            to_date = "'" + to_date + "'";
        }


        return queryTemplatesRecords(base_relation, from_date, to_date, limit, principal, includeComposite);
    }

    private List<RecordEntry2> queryTemplatesRecords(String base_relation, String from_date, String to_date, Integer limit, String principal, boolean includeComposite) {
        List<RecordEntry2> linked_records = new LinkedList<>();
        Set<Integer> seen= new HashSet<>();

        querier.do_query(linked_records,
                null,
                (sb, data) -> {
                    StringBuffer aggregator= new StringBuffer();
                    aggregator.append("json_build_array(");
                    final int[] count0 = {0};
                    compositeLinker.forEach((k,v) -> {
                        if (includeComposite) {
                            count0[0]++;
                            if (count0[0]>1) {
                                aggregator.append(",");
                            }
                            aggregator.append("json_agg(");
                            aggregator.append("link").append(count0[0]).append(".composite)");
                            aggregator.append(",");
                            aggregator.append("'").append(k).append("'");
                        }});
                     aggregator.append(")");

                    sb.append("SELECT search_record.*,record_index.principal,json_agg(ac2.authorized) as authorized,").append(aggregator.toString()).append(" as parent, record_index.hash as hash\n FROM ");
                    sb.append("search_records_for_" + base_relation + "(").append(from_date).append(",").append(to_date).append(") as search_record ");
                    joinAccessControl("search_record.table_name", principal, sb, "search_record", "key");
                    sb.append("\n LEFT JOIN access_control as ac2  ON ac2.record=record_index.id");
                    final int[] count = {0};
                    compositeLinker.forEach((k,v) -> {
                        if (includeComposite) {
                            count[0]++;
                            sb.append("\n LEFT JOIN ");
                            sb.append(v.table);
                            sb.append(" as link").append(count[0]);
                            sb.append("\n ON search_record.key=link").append(count[0]).append(".simple");
                        }
                    });

                    //sb.append("\n LEFT JOIN plead_transforming_composite_linker as link\n");
                   // sb.append("\n ON record_index.key=simple");
                    whereAccessControl(principal, sb);
                    sb.append("\n group by search_record.id, search_record.created_at, search_record.table_name, search_record.key, record_index.principal, record_index.hash\n");
                    sb.append("\n limit ").append(limit);
                    System.out.println("sb = " + sb.toString());
                },
                (rs, data) -> {
                    while (rs.next()) {
                        PGobject parent1=rs.getObject("parent", PGobject.class);
                        Integer parent=null;
                        String parent_relation=null;
                        try {
                            List<Object> parents =(parent1==null)?null:om.readValue(parent1.getValue(), List.class);
                            if (parents!=null && !parents.isEmpty()) {
                                parent=((List<Integer>)(parents.get(0))).get(0);
                                parent_relation=(String)(parents.get(1));
                            }
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        if (parent!=null && !seen.contains(parent)) {
                            seen.add(parent);
                            RecordEntry2 parentRecord = new RecordEntry2();
                            parentRecord.key = parent;
                            parentRecord.base_relation = base_relation;
                            parentRecord.id = rs.getObject("ID", Integer.class);
                            parentRecord.created_at = rs.getObject("created_at", Timestamp.class).toInstant().toString();
                            parentRecord.table_name= parent_relation;//rs-.getObject("table_name", String.class)+"_composite"; // HACK
                            data.add(parentRecord);
                        }

                        RecordEntry2 record = new RecordEntry2();
                        record.key = rs.getObject("key", Integer.class);
                        record.created_at = rs.getObject("created_at", Timestamp.class).toInstant().toString();
                        record.base_relation = base_relation;
                        record.table_name = rs.getObject("table_name", String.class);
                        record.id = rs.getObject("ID", Integer.class);
                        record.principal = rs.getObject("principal", String.class);
                        String authorized1 = rs.getString("authorized");
                        String hash1= rs.getString("hash");
                        try {
                            List<String> authorized=(authorized1==null)?null:om.readValue(authorized1.getBytes(), List.class);
                            Map<String,String> hash=(hash1==null)?null:om.readValue(hash1, Map.class);
                            if (authorized!=null && !authorized.isEmpty() && authorized.get(0)==null) {
                                authorized=List.of();
                            }
                            record.authorized=authorized;
                            record.hash=hash;
                        } catch (IOException e) {
                            throw new RuntimeException("failed to parse authorized field: " + authorized1, e);
                        }

                        data.add(record);
                    }
                });

        return linked_records;
    }

    public List<RecordEntry2> queryTemplatesRecordsById(String base_relation, Integer id, Integer limit, String principal) {
        List<RecordEntry2> linked_records = new LinkedList<>();

        querier.do_query(linked_records,
                null,
                (sb, data) -> {
                    sb.append("SELECT * FROM ");
                    sb.append("search_records_by_id_for_" + base_relation + "(" + id + ") as search_record");
                    joinAccessControl("search_record.table_name", principal, sb, "search_record", "key");
                    whereAccessControl(principal, sb);
                    sb.append("\n limit ").append(limit);
                    //System.out.println("sb = " + sb.toString());
                },
                (rs, data) -> {
                    while (rs.next()) {
                        RecordEntry2 record = new RecordEntry2();
                        record.key = rs.getObject("key", Integer.class);
                        record.created_at = rs.getObject("created_at", Timestamp.class).toInstant().toString();
                        record.base_relation = base_relation;
                        record.table_name = rs.getObject(TABLE_NAME_COLUMN, String.class);
                        record.property = rs.getObject(PROPERTY_COLUMN, String.class);
                        record.id = rs.getObject("ID", Integer.class);

                        data.add(record);
                    }
                });

        return linked_records;
    }


    public List<Integer> queryIds4MostRecentTemplatesRecords(String base_relation, Integer count, String principal) {
        List<Integer> linked_records = new LinkedList<>();

        querier.do_query(linked_records,
                null,

                (sb, data) -> {
                    sb.append("select rel.id, idx.table_name, idx.id as overall_id\n")
                            .append("from ").append(base_relation).append(" as rel\n")
                            .append("join record_index as idx on rel.id=idx.key\n")
                            .append("where idx.table_name='").append(base_relation).append("'\n")
                            .append("and\n")
                            .append("idx.principal='").append(principal).append("'\n")
                            .append("order by rel.id DESC\n");
                    if (count != null && count > 0) {
                        sb.append("limit ")
                                .append(count)
                                .append("\n");
                    }
                },
                (rs, data) -> {
                    while (rs.next()) {
                        Integer id = rs.getObject("id", Integer.class);
                        data.add(id);
                    }
                });

        return linked_records;
    }

    public List<HashMap<String, Object>> queryMostRecentTemplatesRecords(String base_relation, Integer count, String principal) {

        //fetch out rows
        List<HashMap<String,Object>> rows = new ArrayList<>();

        List<Integer> keys=queryIds4MostRecentTemplatesRecords(base_relation, count, principal);

        // convert list into a string (k1, k2, ...)
        String postgresKeys= keys.stream().map(String::valueOf).collect(Collectors.joining(", ", "(", ")"));

        if (keys.isEmpty()) {        // ← add this guard
            return rows;             //   empty table → empty result, no second query
        }

        querier.do_query(rows,
                null,

                (sb, data) -> {
                    sb.append("select idx.table_name, idx.id as overall_id, rel.*\n")
                            .append("from ").append(base_relation).append(" as rel\n")
                            .append("join record_index as idx on rel.id=idx.key\n")
                            .append("where idx.table_name='").append(base_relation).append("'\n")
                            .append("and\n")
                            .append("rel.id IN ").append(postgresKeys).append("\n")
                            .append("and\n")
                            .append("idx.principal='").append(principal).append("'\n")
                            .append("order by rel.id DESC\n");

                },
                (rs, data) -> {
                    while (rs.next()) {

                        //get metadata
                        ResultSetMetaData meta = rs.getMetaData();

                        //get column names
                        int colCount = meta.getColumnCount();
                        ArrayList<String> cols = new ArrayList<String>();
                        for (int index=1; index<=colCount; index++)
                            cols.add(meta.getColumnName(index));


                        while (rs.next()) {
                            HashMap<String,Object> row = new HashMap<>();
                            for (String colName:cols) {
                                Object val = rs.getObject(colName);
                                row.put(colName,val);
                            }
                            data.add(row);
                        }
                    }
                });

        return rows;
    }

    static class RecordEntry {
        public String table;
        public Integer key;
        @Override
        public String toString() {
            return "RecordEntry{" +
                    "table='" + table + '\'' +
                    ", key=" + key +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RecordEntry that = (RecordEntry) o;
            return Objects.equals(table, that.table) && Objects.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(table, key);
        }
    }
    public static class RecordEntry2 {
        public Integer key;
        public String property;
        public String base_relation;
        public String created_at;
        public String table_name;
        public Integer id;
        public String principal;
        public List<String> authorized;
        public Map<String, String> hash;


        @Override
        public String toString() {
            return "RecordEntry2{" +
                    "key=" + key +
                    ", property='" + property + '\'' +
                    ", base_relation='" + base_relation + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", table_name='" + table_name + '\'' +
                    ", id=" + id +
                    ", principal='" + principal + '\'' +
                    ", authorized=" + authorized +
                    ", hash=" + hash +
                    '}';
        }


    }
    public List<Object[]> queryComposite(String templateFullyQualifiedName, Integer id, boolean withTitles, String principal) {
        List<RecordEntry> linked_records = new LinkedList<>();
        TemplateService.Linker linker = compositeLinker.get(templateFullyQualifiedName);

        //System.out.println("linker = " + linker);

        querier.do_query(linked_records,
                null,
                (sb, data) -> {
                    sb.append("SELECT * FROM (");
                    sb.append("SELECT * FROM ");
                    sb.append(shortNames.get(templateFullyQualifiedName));
                    sb.append(" AS template ");
                    joinAccessControl(shortNames.get(templateFullyQualifiedName), principal, sb);
                    sb.append("\n WHERE key=");
                    sb.append(id);
                    andAccessControl(principal, sb);
                    sb.append(") as combo ");
                    sb.append("\n LEFT JOIN ").append(linker.table).append(" AS linker ");
                    sb.append(" ON combo.key = linker.composite");
                },
                (rs, data) -> {
                    while (rs.next()) {
                        RecordEntry record = new RecordEntry();
                        record.table= linker.linked;
                        int i=1;
                        for (String colum: COMPOSITE_LINKER_COLUMNS) {
                            Integer o = rs.getObject(colum, Integer.class);
                            record.key = o;
                            i++;
                        }
                        data.add(record);
                    }
                });

        //System.out.println("linked_records = " + linked_records);

        List<Object[]> the_records = new LinkedList<>();
        for (RecordEntry linked_record : linked_records) {
            Integer simple = linked_record.key;
            List<Object[]> simple_records = querySimple(longNames.get(linked_record.table), simple, withTitles, principal);
            the_records.addAll(simple_records);
        }

        return the_records;
    }

    public List<Object[]> queryTemplates(TableKeyList tableKeyList, boolean withTitles, String principal) {
        List<Object[]> result = new LinkedList<>();
        for (TableKey tableKey : tableKeyList.key) {
            logger.info("tableKey = " + tableKey);
            List<Object[]> tmp=query(tableKey.isA, tableKey.ID, withTitles, principal);
            result.addAll(tmp);
        }
        return result;
    }


    public Map<String, Map<String, Map<String, String>>> getIoMap(String ioMapString) {
        List<String> toExclude = List.of("plead_transforming_composite");
        try {
            Map<String, Map<String, Map<String, String>>> ioMap = om.readValue(ioMapString, typeRef);
            ioMap.get(INPUT). entrySet().removeIf( entry -> toExclude.contains(entry.getKey()) || entry.getValue() == null || entry.getValue().isEmpty());
            ioMap.get(OUTPUT).entrySet().removeIf( entry -> toExclude.contains(entry.getKey()) || entry.getValue() == null || entry.getValue().isEmpty());
            return ioMap;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public  Map<String, String> getShortNames(String shortNamesString) {
        try {
            return om.readValue(shortNamesString, typeRef2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getShortNames() {
        return shortNames;
    }

    /**
     * Generates the {@code CREATE OR REPLACE FUNCTION backwardTraversal} statement
     * using PL/pgSQL dynamic dispatch.
     *
     * <p>The generated function is a drop-in replacement for the static ~1,010-branch
     * UNION formerly produced by {@link #generateBackwardTemplateTraversal_OLD}.
     * Instead of evaluating every possible source→target JOIN on every call, it:
     * <ol>
     *   <li>Fetches the entity value from the source row with one {@code EXECUTE format}
     *       query ({@code SELECT <source_property> FROM <source_template> WHERE id = $1}).</li>
     *   <li>Looks up the 2–12 relevant {@code (target_template, target_property)} pairs
     *       from {@code backward_dispatch} (populated by
     *       {@link #generateClearAndPopulateBackwardDispatch}).</li>
     *   <li>Executes one {@code RETURN QUERY EXECUTE} per target, finding predecessor
     *       rows whose property column matches the entity value.</li>
     * </ol>
     *
     * <p>This reduces the per-call work from ~1,010 JOIN evaluations to ~2–12, cutting
     * the cost of a full {@code backwardtraversal_star} from tens of seconds to
     * sub-second for typical provenance graphs.
     *
     * <p>The {@code ioMap} parameter is accepted for API compatibility with callers
     * (e.g. {@link #generateTraversalMethods}) but is not used — dispatch is
     * entirely data-driven via {@code backward_dispatch} at runtime.
     *
     * @param ioMap input/output property map (unused; kept for API symmetry)
     * @return a {@code CREATE OR REPLACE FUNCTION} SQL string in PL/pgSQL, ready to
     *         execute via {@link Querier#do_statements}
     */
    public String generateBackwardTemplateTraversal(Map<String, Map<String, Map<String, String>>> ioMap) {
        // QueryBuilder.bodyEnd() hardcodes "language SQL" and cannot emit PL/pgSQL,
        // so the DDL is produced directly as a StringBuilder.
        //
        // Performance design: each call to backwardTraversal issues exactly two EXECUTEs:
        //
        //   EXECUTE #1 — fetch the entity value from the source row
        //                (one indexed PK lookup; query text is stable per source pair)
        //
        //   EXECUTE #2 — a single UNION ALL query covering all target tables, assembled
        //                by string_agg() from backward_dispatch via a static PL/pgSQL
        //                statement (plan cached by the PL/pgSQL compiler).
        //                The assembled string is the same for every call with the same
        //                (source_template, source_property), so PostgreSQL's dynamic-SQL
        //                plan cache warms up after ~5 uses and subsequent calls pay only
        //                execution cost, not planning cost.
        //
        // This reduces from N+2 EXECUTEs (one per dispatch target) to 2 per invocation,
        // cutting the per-node overhead from ~24 ms to ~2-3 ms at warm-cache.
        StringBuilder sb = new StringBuilder();
        sb.append("-- Generated by ").append(getClass().getName()).append(".generateBackwardTemplateTraversal\n");
        sb.append("-- PL/pgSQL dynamic-dispatch variant: replaces the static ~1,010-branch UNION.\n");
        sb.append("-- Requires backward_dispatch to be populated first (generateClearAndPopulateBackwardDispatch).\n");
        sb.append("CREATE OR REPLACE FUNCTION backwardTraversal(\n");
        sb.append("    ").append(PARAM_ID)      .append("        integer,\n");
        sb.append("    ").append(PARAM_TEMPLATE) .append("  text,\n");
        sb.append("    ").append(PARAM_PROPERTY) .append("  text\n");
        sb.append(") RETURNS TABLE (\n");
        sb.append("    ").append(IN_ID)          .append("        integer,\n");
        sb.append("    ").append(IN_TEMPLATE)    .append("  text,\n");
        sb.append("    ").append(IN_PROPERTY)    .append("  text,\n");
        sb.append("    ").append(OUT_ID)         .append("       integer,\n");
        sb.append("    ").append(OUT_TEMPLATE)   .append(" text,\n");
        sb.append("    ").append(OUT_PROPERTY)   .append(" text\n");
        sb.append(") LANGUAGE plpgsql STABLE AS $$\n");
        sb.append("DECLARE\n");
        sb.append("    entity_val  bigint;\n");
        sb.append("    union_sql   text;\n");
        sb.append("BEGIN\n");
        sb.append("    -- 1. Fetch the entity value from the source row (EXECUTE #1).\n");
        sb.append("    EXECUTE format('SELECT %I FROM %I WHERE id = $1',\n");
        sb.append("                   ").append(PARAM_PROPERTY).append(", ")
                                       .append(PARAM_TEMPLATE).append(")\n");
        sb.append("        INTO entity_val\n");
        sb.append("        USING ").append(PARAM_ID).append(";\n\n");
        sb.append("    IF entity_val IS NULL THEN RETURN; END IF;\n\n");
        sb.append("    -- 2. Build a UNION ALL query for all relevant target tables.\n");
        sb.append("    --    string_agg runs as a static (plan-cached) PL/pgSQL statement.\n");
        sb.append("    --    The resulting union_sql string is identical for every call with\n");
        sb.append("    --    the same (source_template, source_property), so PostgreSQL's\n");
        sb.append("    --    dynamic-SQL plan cache eliminates re-planning after warm-up.\n");
        sb.append("    SELECT string_agg(\n");
        sb.append("        format(\n");
        sb.append("            'SELECT $1::integer, $2::text, $3::text, id, ''%s''::text, ''%s''::text\n");
        sb.append("               FROM %I WHERE %I = $4',\n");
        sb.append("            target_template, target_property,\n");
        sb.append("            target_template, target_property\n");
        sb.append("        ),\n");
        sb.append("        ' UNION ALL '\n");
        sb.append("    ) INTO union_sql\n");
        sb.append("    FROM backward_dispatch\n");
        sb.append("    WHERE source_template = ").append(PARAM_TEMPLATE).append("\n");
        sb.append("    AND   source_property  = ").append(PARAM_PROPERTY).append(";\n\n");
        sb.append("    IF union_sql IS NULL THEN RETURN; END IF;\n\n");
        sb.append("    -- 3. Execute the combined UNION ALL (EXECUTE #2).\n");
        sb.append("    RETURN QUERY EXECUTE union_sql\n");
        sb.append("        USING ").append(PARAM_ID).append(",\n");
        sb.append("              ").append(PARAM_TEMPLATE).append(",\n");
        sb.append("              ").append(PARAM_PROPERTY).append(",\n");
        sb.append("              entity_val;\n");
        sb.append("END;\n");
        sb.append("$$;\n");
        return sb.toString();
    }

    /**
     * Original static-UNION implementation of {@code backwardTraversal}, preserved
     * for reference and rollback.
     *
     * <p>Generates a {@code CREATE OR REPLACE FUNCTION} that encodes every possible
     * source→target hop as a hard-coded {@code UNION ALL} branch (~1,010 branches for
     * a typical Odoo catalogue).  All branches execute on every call, making the
     * per-call cost proportional to the total number of branches rather than the 2–12
     * that are actually relevant — the root cause of the 50+ second traversal times
     * observed in production.
     *
     * <p>Superseded by {@link #generateBackwardTemplateTraversal}, which uses PL/pgSQL
     * dynamic dispatch via {@code backward_dispatch}.
     *
     * @param ioMap input/output property map
     * @return the legacy SQL UNION function string
     */
    public String generateBackwardTemplateTraversal_OLD(Map<String,Map<String, Map<String, String>>> ioMap) {
        String backwardTraversalFunctionName="backwardTraversal";

        Map<String,?> funParams=new LinkedHashMap<>() {{
            put(PARAM_ID,       unquote("INT"));
            put(PARAM_TEMPLATE, unquote("text"));
            put(PARAM_PROPERTY, unquote("text"));

        }};
        Map<String,Object> functionReturns= new LinkedHashMap<>() {{

            put(IN_ID,           unquote("INT"));
            put(IN_TEMPLATE,     unquote("text"));
            put(IN_PROPERTY,     unquote("text"));
            put(OUT_ID,          unquote("INT"));
            put(OUT_TEMPLATE,    unquote("text"));
            put(OUT_PROPERTY,    unquote("text"));
        }};


        QueryBuilder fun=
                new QueryBuilder()
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLSearchRecordFunction")
                        .next(createFunction(backwardTraversalFunctionName))
                        .params(funParams)
                        .returns("table", functionReturns)
                        .bodyStart("");
        Set<String> allTables=ioMap.get(OUTPUT).values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toSet());
        allTables.addAll(ioMap.get(INPUT).values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toSet()));


        Map<String, Map<String, String>> input = ioMap.get(INPUT);
        Map<String, Map<String, String>> output = ioMap.get(OUTPUT);

        boolean before=false;

        for (String table: allTables) {

            //System.out.println("table = " + table);



            Map<String, Map<String, String>> input_table=
                    filterMapAccordingToTable(table, input);
            Map<String, Map<String, String>> output_table=
                    filterMapAccordingToTable(table, output);

            // ── Virtual outputs for overlay / activity-output-only templates ──
            // Two cases where backwardTraversal would otherwise miss a template:
            //
            // Case 1 — all-input (decorator/overlay) templates:
            //   Templates where ALL properties are inputs are stripped from the
            //   output map entirely by removeIf() in getIoMap(), so they never
            //   appear in output_table and the loop below can never "arrive at"
            //   them.  Condition: !output_table.containsKey(template).
            //
            // Case 2 — activity-output-only templates (e.g. document_obligating_coin):
            //   The template HAS a declared output, but it is typed as "activity"
            //   (not as an entity table like "coin" or "document").
            //   filterMapAccordingToTable() includes the template in output_table
            //   as a key, but with an EMPTY inner map for the current entity table.
            //   Condition: output_table.get(template).isEmpty().
            //
            // Fix for both: inject the template into output_table using its
            // input properties as virtual output keys so the traversal can hop
            // through it to follow predecessor_table derivation edges onward.
            for (String template : input_table.keySet()) {
                if (!output_table.containsKey(template) || output_table.get(template).isEmpty()) {
                    output_table.put(template, input_table.get(template));
                }
            }
            // ─────────────────────────────────────────────────────────────────


            for (String in_template: input_table.keySet()) {
                if (!input_table.get(in_template).keySet().isEmpty()) {
                    fun.comment(" + in_template = " + in_template);
                    for (String in_property : input_table.get(in_template).keySet()) {
                        for (String out_template : output_table.keySet()) {
                            if (!output_table.get(out_template).keySet().isEmpty()) {
                                for (String out_property : output_table.get(out_template).keySet()) {
                                    // Skip same-template same-property self-joins.
                                    // These arise when the virtual-output fix injects an input
                                    // column as a virtual output (e.g. document_triggering_goods.document0).
                                    // The resulting JOIN matches all rows that SHARE the same value
                                    // (siblings), never ancestors, so it only produces fan-out noise.
                                    // Legitimate self-joins (e.g. goodsg0→goodsg1) have DIFFERENT
                                    // in_property and out_property and are unaffected by this guard.
                                    if (in_template.equals(out_template) && in_property.equals(out_property)) continue;

                                    String in_templatex=in_template;
                                    String out_templatex=out_template;

                                    if(in_template.equals(out_template)) {
                                        in_templatex = "_" + in_template + "_in";
                                        out_templatex = "_" + out_template + "_out";
                                    }

                                    String [] args={
                                            String.format("%s as %s",    PARAM_ID, IN_ID),
                                            String.format("'%s' as %s",  in_template,   IN_TEMPLATE),
                                            String.format("'%s' as %s",  in_property,   IN_PROPERTY),
                                            String.format("%s.id as %s", out_templatex, OUT_ID),
                                            String.format("'%s' as %s",  out_template,  OUT_TEMPLATE),
                                            String.format("'%s' as %s",  out_property,  OUT_PROPERTY)
                                    };
                                    if (before) {
                                        fun.newline().union(pp -> select((Object[]) args).apply(pp));
                                    } else {
                                        fun.selectExp(args);
                                        before = true;
                                    }
                                    fun.from(in_template);
                                    if (!in_template.equals(in_templatex)) {
                                        fun.alias(in_templatex);
                                    }
                                    fun.join(out_template);
                                    if (!out_template.equals(out_templatex)) {
                                        fun.alias(out_templatex);
                                    }
                                    fun.on (out_templatex + "." +  out_property + " = " + in_templatex  + "." +  in_property )
                                            .and(           unquote(PARAM_PROPERTY) + " = '" + in_property + "'")
                                            .and(           unquote(PARAM_TEMPLATE) + " = '" + in_template + "'")  // was incorrectly in_templatex
                                            .and( in_templatex + ".id=" + PARAM_ID);
                                }
                            }
                        }
                    }
                }
            }

        }

        return fun.bodyEnd("").getSQL();

    }

    /**
     * Generates SQL that creates (if absent), truncates, and repopulates the
     * {@code backward_dispatch} table.
     *
     * <p>{@code backward_dispatch} is a data-driven alternative to the hard-coded
     * 1,000-branch UNION inside {@link #generateBackwardTemplateTraversal}.
     * Each row records one valid backward-traversal hop:
     * <pre>
     *   (source_template, source_property) → (target_template, target_property)
     * </pre>
     * meaning "if you are sitting at a row in {@code source_template} and holding
     * the entity value stored in column {@code source_property}, you can find
     * predecessor rows in {@code target_template} by matching on
     * {@code target_property}."
     *
     * <p>The iteration logic is identical to {@link #generateBackwardTemplateTraversal}
     * — same outer loop over entity tables, same virtual-output fix for
     * all-input (decorator/overlay) and activity-output-only templates, same
     * self-join guard — so the two representations encode exactly the same graph.
     * A PL/pgSQL {@code backwardTraversal} can therefore replace the static UNION
     * with a lookup into this table, executing only the 2–12 relevant queries
     * instead of all ~1,010.
     *
     * <p>Duplicate tuples (which arise because the outer loop iterates over entity
     * tables and the same template pair can appear in multiple tables) are silently
     * deduplicated.
     *
     * <p>An index on {@code (source_template, source_property)} is appended so that
     * the PL/pgSQL caller can look up relevant rows efficiently.
     *
     * @param ioMap input/output property map (short SQL table names), as returned
     *              by {@link #getIoMap}
     * @return a SQL string containing {@code CREATE TABLE IF NOT EXISTS},
     *         {@code TRUNCATE}, {@code INSERT … VALUES}, and {@code CREATE INDEX}
     *         statements, ready to execute via {@link Querier#do_statements}
     */
    public String generateClearAndPopulateBackwardDispatch(Map<String, Map<String, Map<String, String>>> ioMap) {

        List<String[]> rows = collectBackwardDispatchRows(ioMap);

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS backward_dispatch (\n");
        sb.append("    source_template  text NOT NULL,\n");
        sb.append("    source_property  text NOT NULL,\n");
        sb.append("    target_template  text NOT NULL,\n");
        sb.append("    target_property  text NOT NULL\n");
        sb.append(");\n\n");

        sb.append("TRUNCATE backward_dispatch;\n\n");

        sb.append("INSERT INTO backward_dispatch\n");
        sb.append("    (source_template, source_property, target_template, target_property)\n");
        sb.append("VALUES\n");
        boolean first = true;
        for (String[] row : rows) {
            if (!first) sb.append(",\n");
            sb.append("    ('").append(row[0]).append("', '")
              .append(row[1]).append("', '")
              .append(row[2]).append("', '")
              .append(row[3]).append("')");
            first = false;
        }
        sb.append("\n;\n\n");

        // Index used by the PL/pgSQL backwardTraversal to look up relevant hops.
        sb.append("CREATE INDEX IF NOT EXISTS backward_dispatch_src_idx\n");
        sb.append("    ON backward_dispatch (source_template, source_property);\n");

        return sb.toString();
    }

    /**
     * Iterates the {@code ioMap} the same way {@link #generateClearAndPopulateBackwardDispatch}
     * does and returns the resulting list of dispatch tuples
     * {@code (in_template, in_property, out_template, out_property)}.
     *
     * <p>Shared by {@link #generateClearAndPopulateBackwardDispatch},
     * {@link #generateCreateTraversalIndexes}, and {@link #generateDropTraversalIndexes}
     * so the three stay in agreement about which edges exist.
     */
    private List<String[]> collectBackwardDispatchRows(Map<String, Map<String, Map<String, String>>> ioMap) {

        Set<String> allTables = ioMap.get(OUTPUT).values().stream()
                .map(Map::values).flatMap(Collection::stream)
                .collect(Collectors.toSet());
        allTables.addAll(ioMap.get(INPUT).values().stream()
                .map(Map::values).flatMap(Collection::stream)
                .collect(Collectors.toSet()));

        Map<String, Map<String, String>> input  = ioMap.get(INPUT);
        Map<String, Map<String, String>> output = ioMap.get(OUTPUT);

        // Collect unique (source_template, source_property, target_template, target_property) tuples.
        // LinkedHashSet preserves insertion order, which makes downstream SQL deterministic.
        Set<String>   seen = new LinkedHashSet<>();
        List<String[]> rows = new ArrayList<>();

        for (String table : allTables) {

            Map<String, Map<String, String>> input_table  = filterMapAccordingToTable(table, input);
            Map<String, Map<String, String>> output_table = filterMapAccordingToTable(table, output);

            // ── Virtual-output fix (mirrors generateBackwardTemplateTraversal) ──
            // All-input and activity-output-only templates would otherwise be
            // unreachable as traversal targets; inject them using their input
            // properties as virtual output keys.
            for (String template : input_table.keySet()) {
                if (!output_table.containsKey(template) || output_table.get(template).isEmpty()) {
                    output_table.put(template, input_table.get(template));
                }
            }
            // ──────────────────────────────────────────────────────────────────

            for (String in_template : input_table.keySet()) {
                if (input_table.get(in_template).keySet().isEmpty()) continue;
                for (String in_property : input_table.get(in_template).keySet()) {
                    for (String out_template : output_table.keySet()) {
                        if (output_table.get(out_template).keySet().isEmpty()) continue;
                        for (String out_property : output_table.get(out_template).keySet()) {
                            // Skip same-template same-property self-joins (siblings, not ancestors).
                            if (in_template.equals(out_template) && in_property.equals(out_property)) continue;

                            String key = in_template + "|" + in_property + "|" + out_template + "|" + out_property;
                            if (seen.add(key)) {
                                rows.add(new String[]{in_template, in_property, out_template, out_property});
                            }
                        }
                    }
                }
            }
        }
        return rows;
    }

    /** Prefix shared by every index emitted by {@link #generateCreateTraversalIndexes}. */
    public static final String TRAVERSAL_INDEX_PREFIX = "ix_traversal_";

    /**
     * Builds the index name for {@code table(column)}, or {@code table(id)} when
     * {@code column} is {@code null}.  Names that would exceed PostgreSQL's
     * {@code NAMEDATALEN} (63 bytes) are shortened with an 8-char SHA-1 suffix
     * to keep them unique.
     */
    private static String traversalIndexName(String table, String column) {
        String body    = (column == null) ? table : table + "__" + column;
        String natural = TRAVERSAL_INDEX_PREFIX + body;
        if (natural.length() <= 63) return natural;
        String suffix = "_" + DigestUtils.sha1Hex(table + ":" + (column == null ? "" : column)).substring(0, 8);
        int    budget = 63 - TRAVERSAL_INDEX_PREFIX.length() - suffix.length();
        return TRAVERSAL_INDEX_PREFIX + body.substring(0, Math.min(body.length(), budget)) + suffix;
    }

    /**
     * Returns SQL that creates the secondary btree indexes required to make the
     * dynamic UNION ALL inside {@code backwardTraversal} fast.
     *
     * <p>For every distinct target table referenced in {@code backward_dispatch}
     * this emits a {@code CREATE INDEX IF NOT EXISTS} on {@code (id)} and one on
     * each referenced {@code (target_property)}.  Without these, every
     * {@code backwardTraversal} invocation seq-scans the source table once and
     * each target table once per UNION ALL branch — measured at ~3.5M buffer
     * hits and ~5 s per {@code backwardtraversal_star} call on a typical Odoo
     * catalogue.  With them, ~50K hits and ~200 ms (≥25× speedup).
     *
     * <p>All statements use {@code IF NOT EXISTS}; this method is safe to run
     * on every Chronicle startup and may be called independently of
     * {@link #generateTraversalMethods} when re-indexing in a different context.
     *
     * <p>Index names follow {@link #traversalIndexName} and share the
     * {@link #TRAVERSAL_INDEX_PREFIX} so they can be enumerated and dropped
     * uniformly by {@link #generateDropTraversalIndexes}.
     *
     * @param ioMap input/output property map (short SQL names), as returned by {@link #getIoMap}
     * @return a SQL string containing only {@code CREATE INDEX IF NOT EXISTS} statements
     */
    public String generateCreateTraversalIndexes(Map<String, Map<String, Map<String, String>>> ioMap) {
        List<String[]> rows = collectBackwardDispatchRows(ioMap);

        // Two ordered sets: tables (for PK index) and (table, column) pairs (for property indexes).
        // LinkedHashSet preserves insertion order — deterministic SQL output.
        Set<String>            tables          = new LinkedHashSet<>();
        Set<Map.Entry<String,String>> tableCols = new LinkedHashSet<>();
        for (String[] r : rows) {
            String targetTemplate = r[2];
            String targetProperty = r[3];
            tables.add(targetTemplate);
            tableCols.add(new AbstractMap.SimpleEntry<>(targetTemplate, targetProperty));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-- Generated by ").append(getClass().getName()).append(".generateCreateTraversalIndexes\n");
        sb.append("-- Indexes consumed by backwardTraversal's dynamic UNION ALL.\n\n");
        for (String t : tables) {
            sb.append("CREATE INDEX IF NOT EXISTS ").append(traversalIndexName(t, null))
              .append(" ON ").append(t).append(" (id);\n");
        }
        sb.append('\n');
        for (Map.Entry<String,String> e : tableCols) {
            String t = e.getKey(), c = e.getValue();
            sb.append("CREATE INDEX IF NOT EXISTS ").append(traversalIndexName(t, c))
              .append(" ON ").append(t).append(" (").append(c).append(");\n");
        }
        return sb.toString();
    }

    /**
     * Returns SQL that drops every index produced by
     * {@link #generateCreateTraversalIndexes}.  Each {@code DROP INDEX} uses
     * {@code IF EXISTS} so the script is idempotent and safe to run when none
     * of the indexes are present.
     *
     * @param ioMap input/output property map (short SQL names)
     * @return a SQL string containing only {@code DROP INDEX IF EXISTS} statements
     */
    public String generateDropTraversalIndexes(Map<String, Map<String, Map<String, String>>> ioMap) {
        List<String[]> rows = collectBackwardDispatchRows(ioMap);

        Set<String>            tables          = new LinkedHashSet<>();
        Set<Map.Entry<String,String>> tableCols = new LinkedHashSet<>();
        for (String[] r : rows) {
            tables.add(r[2]);
            tableCols.add(new AbstractMap.SimpleEntry<>(r[2], r[3]));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-- Generated by ").append(getClass().getName()).append(".generateDropTraversalIndexes\n\n");
        for (Map.Entry<String,String> e : tableCols) {
            sb.append("DROP INDEX IF EXISTS ").append(traversalIndexName(e.getKey(), e.getValue())).append(";\n");
        }
        sb.append('\n');
        for (String t : tables) {
            sb.append("DROP INDEX IF EXISTS ").append(traversalIndexName(t, null)).append(";\n");
        }
        return sb.toString();
    }

    private Map<String, Map<String, String>> filterMapAccordingToTable(String table, Map<String, Map<String, String>> input) {
        return input.keySet().stream().collect(Collectors.toMap(k -> k, k -> input.get(k).keySet().stream().filter(k2 -> input.get(k).get(k2).equals(table)).collect(Collectors.toMap(k2 -> k2, k2 -> input.get(k).get(k2)))));
    }

    private Map<String, Map<String, String>> trimKeys(Map<String, Map<String, String>> inputs) {
        return inputs.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey().contains(".") ? e.getKey().substring(e.getKey().lastIndexOf(".")+1) : e.getKey(),
                Map.Entry::getValue
        ));
    }


/*

    private StringBuilder regeneratePredecessorTable(StringBuilder sb) throws JsonProcessingException {
        Map<String, Map<String, List<String>>> successors0 = this.templateDispatcher.getSuccessors();
        Map<String,String> shortNames= new ObjectMapper().readValue(templateDispatcher.getShortNames(), typeRef2);
        List<List<String>> successorsList=new ArrayList<>();
        for (String key : successors0.keySet()) {
            Map<String, List<String>> value = successors0.get(key);
            if (value==null) continue;
            for (String subKey : value.keySet()) {
                for (String s : value.get(subKey)) {
                    successorsList.add(List.of(shortNames.get(key), subKey, s));
                    ;
                }
            }
        }

        sb.append("CREATE TABLE if not exists predecessor_table  (template text, output text, input text);\n" +
                "\n" +
                "truncate predecessor_table;\n" +
                "\n" +
                "insert into predecessor_table (template, output, input)\n" +
                "values\n");
        final boolean[] first = {true};
        successorsList.forEach(s->{
            if (first[0]) {
                first[0] =false;
            } else {
                sb.append(",\n");
            }
            sb.append("(");
            sb.append("'").append(s.get(0)).append("', ");
            sb.append("'").append(s.get(2)).append("', ");
            sb.append("'").append(s.get(1)).append("'");
            sb.append(")");
        });
        sb.append("\n;\n");

        System.out.println("Regenerating predecessor table with \n" + sb.toString());

        return sb;

    }

 */

    private StringBuilder regenerateTypedPredecessorTable(StringBuilder sb) throws JsonProcessingException {
        Map<String, Map<String, List<String>>> successors0 = this.templateDispatcher.getTypedSuccessors();
        Map<Integer,String> provTypes=Stream.of(StatementOrBundle.Kind.values())
                .collect(Collectors.toMap(Enum::ordinal, StatementOrBundle.Kind::name));
        Map<String,String> shortNames= new ObjectMapper().readValue(templateDispatcher.getShortNames(), typeRef2);
        List<List<String>> successorsList=new ArrayList<>();
        for (String key : successors0.keySet()) {
            Map<String, List<String>> value = successors0.get(key);
            if (value==null) continue;
            for (String subKey : value.keySet()) {
                List<String> strings = value.get(subKey);
                // strings alternate: string and type
                int count=0;
                for (String s : strings) {
                    if ((count&1)==0) {
                        successorsList.add(List.of(shortNames.get(key), subKey, s, strings.get(count + 1)));
                    }
                    count++;
                }
            }
        }


        sb.append("CREATE TABLE if not exists predecessor_table  (template text, output text, input text, rel integer, relname text);\n" +
                "\n" +
                "truncate predecessor_table;\n" +
                "\n" +
                "insert into predecessor_table (template, output, input, rel, relname)\n" +
                "values\n");
        final boolean[] first = {true};
        successorsList.forEach(s->{
            if (first[0]) {
                first[0] =false;
            } else {
                sb.append(",\n");
            }
            sb.append("(");
            sb.append("'").append(s.get(0)).append("', ");
            sb.append("'").append(s.get(2)).append("', ");
            sb.append("'").append(s.get(1)).append("', ");
            sb.append(s.get(3)).append(", ");
            sb.append("'").append(provTypes.get(Integer.valueOf(s.get(3)))).append("'");
            sb.append(")");
        });
        sb.append("\n;\n");

        //System.out.println("Regenerating predecessor table with \n" + sb.toString());

        return sb;

    }

    public List<Object[]> queryFromSql(String sql, boolean withHeader) {
        List<Object> linked_records = new LinkedList<>();

        return querier.do_query(linked_records,
                null,
                (sb, data) -> {
                    sb.append(sql);
                },
                (rs, data) -> {
                    int columnCount = rs.getMetaData().getColumnCount();
                    if (withHeader) {
                        Object[] header = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            header[i - 1] = rs.getMetaData().getColumnLabel(i);
                        }
                        data.add(header);
                    }
                    while (rs.next()) {
                        Object[] record = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            record[i-1] = rs.getObject(i);
                        }
                        data.add(record);
                    }
                }).stream().map(o -> (Object[]) o).collect(Collectors.toList());


    }

    /*
    Receives the typed successor returned by getTypedSuccessors in Builder classes, where the type of relation is encoded explicitly
    Selects the successors amon the set kinds

     */

    static public Map<String, Map<String, List<String>>> selectSuccessors(Map<String, Map<String, List<String>>> typedSuccessors,  Set<StatementOrBundle.Kind> kinds) {

        Set<Integer> kindsAsNums=kinds.stream().map(Enum::ordinal).collect(Collectors.toSet());
        Map<String, Map<String, List<String>>> result=new HashMap<>();
        for (String key : typedSuccessors.keySet()) {
            Map<String, List<String>> value = typedSuccessors.get(key);
            if (value == null) continue;
            for (String subKey : value.keySet()) {
                List<String> strings = value.get(subKey);
                // strings alternate: string and type
                int count = 0;
                List<String> alist = new ArrayList<>();
                for (String s : strings) {
                    if ((count & 1) == 0) {
                        // this is a potential successor
                        if (kindsAsNums.contains(Integer.valueOf(strings.get(count + 1)))) {
                            alist.add(s);
                        }
                    }
                    count++;
                }
                if (!alist.isEmpty()) {
                    result.computeIfAbsent(key, k -> new HashMap<>()).put(subKey, alist);
                }
            }
        }
        return result;
    }


}
