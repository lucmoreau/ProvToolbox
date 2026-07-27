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
    static TypeReference<Map<String, Set<String>>> typeRef3 = new TypeReference<>() {};
    private final Map<String, Map<String, List<String>>> successors;
    private final RelationMapping relationMapping;
    private final Map<String, String[]> propertyOrder;
    private final Map<String, String[]> simplePropertyOrder;
    private final Map<String, String> shortNames;
    private final Map<String, String> longNames;
    private final Map<String, Map<String, List<String>>> typedSuccessors;
    private final Map<String, String> semanticType;
    private final Map<String, Set<String>> uniqueMap;

    /**
     * Lazily-populated cache of the dispatch rows derived from {@link #ioMap}.
     * Populated on first call to {@link #getBackwardDispatchRows()} and reused
     * by every subsequent caller — {@link #generateClearAndPopulateBackwardDispatch},
     * {@link #generateCreateTraversalIndexes}, {@link #generateDropTraversalIndexes}
     * — so the cross-product over the ioMap is computed exactly once per instance.
     * Single-threaded init at service start: no synchronisation required.
     */
    private List<String[]> cachedBackwardDispatchRows;

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
        this.uniqueMap=shortenNames2(getUniqueMap(templateDispatcher.getUniqueMap()), shortNames);
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
        
        generateUniqueIndexes(querier,this.uniqueMap);
        //initializePredecessorTable();
    }

    /**
     * Ensure a UNIQUE INDEX exists for every {@code @unique} variable declared in the cbindings,
     * passed in as {@code table -> {unique columns}}.  Idempotent: uses
     * {@code CREATE UNIQUE INDEX IF NOT EXISTS} keyed on a deterministic index name
     * ({@code <table>_<col1>_<col2..>_unique}), so it is a no-op when the index already exists
     * (including indexes created manually before this was generated).  A partial predicate
     * ({@code WHERE <cols> IS NOT NULL}) lets NULL rows coexist while keeping non-null values unique.
     */
    private void generateUniqueIndexes(Querier querier, Map<String, Set<String>> uniqueMap) {
        if (uniqueMap == null || uniqueMap.isEmpty()) return;
        for (Map.Entry<String, Set<String>> entry : uniqueMap.entrySet()) {
            String table = entry.getKey();
            Set<String> cols = entry.getValue();
            if (table == null || table.isBlank() || cols == null || cols.isEmpty()) continue;

            String colList   = String.join(", ", cols);
            String indexName = table + "_" + String.join("_", cols) + "_unique";
            String notNull   = cols.stream()
                                   .map(c -> c + " IS NOT NULL")
                                   .collect(Collectors.joining(" AND "));

            logger.info("Ensuring unique index " + indexName + " on " + table + "(" + colList + ")");
            querier.do_statements(null, null, (sb, data) ->
                    sb.append("CREATE UNIQUE INDEX IF NOT EXISTS ").append(indexName)
                      .append(" ON ").append(table).append(" (").append(colList).append(")")
                      .append(" WHERE ").append(notNull));
        }
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

    private Map<String,Set<String>> shortenNames2(Map<String, Set<String>> map, Map<String, String> shortNames) {
        return map.entrySet().stream().collect(Collectors.toMap(e -> shortNames.get(e.getKey()), Map.Entry::getValue));
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

    // backwardtraversal_star, two-phase rewrite (odoodemo T-187).
    // recursiveQuery3 carries `depth` inside the recursive CTE's row, so UNION
    // dedupes on (edge, depth), not on the edge: an edge reachable via paths of
    // different lengths survives once per distinct path length (up to the depth
    // cap of 100) and every surviving row re-expands its producer's whole
    // subtree — on diamond-rich closures the traversal re-enumerates the same
    // region at each depth and times out.  Here the recursion carries a frontier
    // of unique (id, template, property) nodes only, so UNION memoises each node
    // once and it never re-expands; the edge 6-tuple is emitted by a second,
    // non-recursive expansion pass over the reached node set (a recursive CTE
    // cannot separate "rows in the result" from "rows fed to the next
    // iteration", hence the two phases).  Each (node, property) is expanded
    // exactly twice — once per phase — instead of once per path length.
    // The frontier key MUST include the property (the OUTPUT column through
    // which the node was reached): a dual-chain row reached through two
    // different output columns must expand the input columns of each — a factor
    // of at most the row's output-column count, not a path count.
    // Contract is unchanged (same signature, same RETURNS TABLE, same self-loop
    // filter); the DISTINCT edge set is identical (verified on the live
    // chronicle store: 460,826 edges on an unfiltered dispatch closure and a
    // rel-filtered deep closure, zero difference in either direction), but the
    // duplicate edge rows the old form emitted (same edge at several depths)
    // are gone, and there is no depth cap — memoisation over the finite node
    // set guarantees termination, so closures deeper than 100 hops are traversed
    // fully instead of being silently truncated.  When a maximal-depth bound is
    // wanted (as the old cap provided), use recursiveQuery5 below instead.
    String recursiveQuery4 = """
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
            WITH RECURSIVE reached AS (

                -- ── Phase 1: node recursion ──────────────────────────────────────────────
                -- Frontier of unique (id, template, property) triples, where property is
                -- the OUTPUT column through which the node was reached.  No depth, no edge
                -- payload: UNION memoises each triple once, so a node reachable along many
                -- paths (or at many path lengths) is expanded exactly once.  The
                -- relation-type filter is applied on every hop, starting with the
                -- expansion of the anchor itself.
                SELECT
                    __param_id       AS id,
                    __param_template AS tpl,
                    __param_property AS prop

                UNION   -- memoises: a (node, property) triple never re-expands

                SELECT
                    bt.out_id,
                    bt.out_template,
                    bt.out_property
                FROM
                    reached r
                    JOIN predecessor_table pt
                        ON  pt.template = r.tpl
                        AND pt.output   = r.prop
                        AND (
                            __param_selected_relations IS NULL
                            OR pt.rel = ANY(__param_selected_relations)
                        )
                    CROSS JOIN LATERAL backwardTraversal(
                        r.id,
                        r.tpl,
                        pt.input
                    ) AS bt
                WHERE
                    pt.input IS NOT NULL

            )
            -- ── Phase 2: edge emission ───────────────────────────────────────────────────
            -- One non-recursive expansion pass over the deduplicated node set, emitting
            -- the same 6-tuple contract as before.  The anchor sits in `reached` and
            -- contributes the base edges; nodes whose output column has no
            -- predecessor_table entry are dead ends and contribute nothing.
            SELECT DISTINCT
                bt.in_id,
                bt.in_template,
                bt.in_property,
                bt.out_id,
                bt.out_template,
                bt.out_property
            FROM
                reached r
                JOIN predecessor_table pt
                    ON  pt.template = r.tpl
                    AND pt.output   = r.prop
                    AND (
                        __param_selected_relations IS NULL
                        OR pt.rel = ANY(__param_selected_relations)
                    )
                CROSS JOIN LATERAL backwardTraversal(
                    r.id,
                    r.tpl,
                    pt.input
                ) AS bt
            WHERE
                pt.input IS NOT NULL
                -- Self-loops arise from the virtual-output fix for activity-output-only
                -- templates (e.g. document_obligating_coin): backwardTraversal joins the
                -- table with itself on an input column used as a virtual output, matching
                -- the same row.  Self-loops are never valid in a provenance graph.
                AND NOT (bt.in_id = bt.out_id AND bt.in_template = bt.out_template)
            $function$;

            """;

    // backwardtraversal_star, memoised BFS rewrite (odoodemo T-187).
    // recursiveQuery3 carries `depth` inside the recursive CTE's row, so UNION
    // dedupes on (edge, depth), not on the edge: an edge reachable via paths of
    // different lengths survives once per distinct path length (up to the depth
    // cap of 100) and every surviving row re-expands its producer's whole
    // subtree — on diamond-rich closures the traversal re-enumerates the same
    // region at each depth and times out.
    // A pure recursive CTE cannot fix this while keeping a depth limit
    // (recursiveQuery4 above is the uncapped pure-SQL form): dedup is on the
    // whole row, so a depth column defeats memoisation (the defect), and the
    // recursive term may not consult the result-so-far to test "node already
    // visited at any depth".  Hence PL/pgSQL breadth-first search: a temp table
    // memoises visited (id, template, property) triples — each expanded exactly
    // once, at the layer BFS first reaches it — and the loop counter is the
    // depth, so __param_max_depth bounds the work done, not just the rows
    // returned.  The edge 6-tuple is emitted by one expansion pass over the
    // visited set at the end; each visited triple is thus expanded exactly
    // twice instead of once per path length.
    // The frontier key MUST include the property (the OUTPUT column through
    // which the node was reached): a dual-chain row reached through two
    // different output columns must expand the input columns of each — a factor
    // of at most the row's output-column count, not a path count.
    // Contract: same RETURNS TABLE and self-loop filter; existing 3- and 4-arg
    // call sites are unchanged (__param_max_depth defaults to 100, the old
    // hard-coded cap; NULL means unbounded, mirroring __param_selected_relations).
    // The leading DROPs remove the old-signature overloads — without them a
    // CREATE OR REPLACE on a live store would leave the 4-arg function behind
    // and make 3-/4-arg calls ambiguous.
    // Verified on the live chronicle store: DISTINCT edge sets identical to
    // recursiveQuery3 (460,826 edges on an unfiltered dispatch closure, and a
    // rel-filtered deep closure, zero difference in either direction), and the
    // unfiltered deep closure the old form cannot finish even truncated at
    // depth 5 (~295k nodes) completes.  Duplicate edge rows the old form
    // emitted (same edge at several depths) are gone.
    String recursiveQuery5 = """
            DROP FUNCTION IF EXISTS public.backwardtraversal_star(integer, text, text);
            DROP FUNCTION IF EXISTS public.backwardtraversal_star(integer, text, text, integer[]);

            CREATE OR REPLACE FUNCTION public.backwardtraversal_star(
                __param_id                  integer,
                __param_template            text,
                __param_property            text,
                __param_selected_relations  integer[]  DEFAULT NULL,
                __param_max_depth           integer    DEFAULT 100
            )
            RETURNS TABLE(
                in_id        integer,
                in_template  text,
                in_property  text,
                out_id       integer,
                out_template text,
                out_property text
            )
            LANGUAGE plpgsql
            AS $function$
            DECLARE
                __layer    integer := 0;   -- last BFS layer inserted (anchor = 0)
                __inserted bigint;
            BEGIN
                -- Visited set.  The primary key memoises each (node, output-property)
                -- triple: it is inserted once, with the minimal hop count at which BFS
                -- reaches it, and expanded exactly once.  Session-scoped; TRUNCATE (not
                -- DROP/CREATE) so repeated calls in one transaction reuse the table.
                -- Not reentrant — nothing in the traversal stack calls this function.
                CREATE TEMP TABLE IF NOT EXISTS __bts_reached (
                    id    integer,
                    tpl   text,
                    prop  text,
                    depth integer,
                    PRIMARY KEY (id, tpl, prop)
                ) ON COMMIT DROP;
                CREATE INDEX IF NOT EXISTS __bts_reached_depth ON __bts_reached (depth);
                TRUNCATE __bts_reached;

                INSERT INTO __bts_reached
                VALUES (__param_id, __param_template, __param_property, 0);

                -- ── Phase 1: breadth-first node search ───────────────────────────────
                -- Each iteration expands only the nodes first reached at the previous
                -- layer; ON CONFLICT DO NOTHING drops already-visited triples, so no
                -- node re-expands however many paths (or path lengths) reach it.
                -- Layers 1 .. __param_max_depth - 1 are filled: their nodes are the
                -- consumers whose expansion in Phase 2 yields edges at depth
                -- 1 .. __param_max_depth, matching the old `rt.depth < 100` cap.
                -- The relation-type filter is applied on every hop, starting with the
                -- expansion of the anchor itself.
                WHILE __param_max_depth IS NULL OR __layer < __param_max_depth - 1 LOOP
                    INSERT INTO __bts_reached (id, tpl, prop, depth)
                    SELECT DISTINCT
                        bt.out_id, bt.out_template, bt.out_property, __layer + 1
                    FROM
                        __bts_reached r
                        JOIN predecessor_table pt
                            ON  pt.template = r.tpl
                            AND pt.output   = r.prop
                            AND (
                                __param_selected_relations IS NULL
                                OR pt.rel = ANY(__param_selected_relations)
                            )
                        CROSS JOIN LATERAL backwardTraversal(r.id, r.tpl, pt.input) AS bt
                    WHERE
                        r.depth = __layer
                        AND pt.input IS NOT NULL
                    ON CONFLICT DO NOTHING;

                    GET DIAGNOSTICS __inserted = ROW_COUNT;
                    EXIT WHEN __inserted = 0;   -- frontier exhausted before the cap
                    __layer := __layer + 1;
                END LOOP;

                -- ── Phase 2: edge emission ───────────────────────────────────────────
                -- One expansion pass over the deduplicated visited set, emitting the
                -- same 6-tuple contract as before.  The anchor contributes the base
                -- edges; nodes whose output column has no predecessor_table entry are
                -- dead ends and contribute nothing.  The depth filter only matters for
                -- __param_max_depth = 0 (Phase 1 never fills a layer past the cap).
                RETURN QUERY
                SELECT DISTINCT
                    bt.in_id,
                    bt.in_template,
                    bt.in_property,
                    bt.out_id,
                    bt.out_template,
                    bt.out_property
                FROM
                    __bts_reached r
                    JOIN predecessor_table pt
                        ON  pt.template = r.tpl
                        AND pt.output   = r.prop
                        AND (
                            __param_selected_relations IS NULL
                            OR pt.rel = ANY(__param_selected_relations)
                        )
                    CROSS JOIN LATERAL backwardTraversal(r.id, r.tpl, pt.input) AS bt
                WHERE
                    pt.input IS NOT NULL
                    AND (__param_max_depth IS NULL OR r.depth < __param_max_depth)
                    -- Self-loops arise from the virtual-output fix for
                    -- activity-output-only templates (e.g. document_obligating_coin):
                    -- backwardTraversal joins the table with itself on an input column
                    -- used as a virtual output, matching the same row.  Self-loops are
                    -- never valid in a provenance graph.
                    AND NOT (bt.in_id = bt.out_id AND bt.in_template = bt.out_template);
            END;
            $function$;

            """;

    // backwardtraversal_star, capped pure-SQL rewrite via (node, depth)
    // accumulation (odoodemo T-187).
    // Phase 1 recurses over (id, template, property, depth) rows — nodes, not
    // edges.  Compared with recursiveQuery3's (edge, depth) rows this collapses
    // the fan-in multiplicity (there, every incoming edge of a producer
    // re-expands it once per depth; here all of them dedupe into one row), so
    // the only duplication left is path-length multiplicity: a node reachable
    // at several distinct depths appears, and expands, once per depth.  Worst
    // case that factor is the depth cap; measured on the deep rel-filtered
    // closure of the live chronicle store it is 1.41× (1,213 rows for 859
    // distinct triples, deepest layer 50).  So: not the strict once-per-node
    // of recursiveQuery5's PL/pgSQL BFS, but capped AND pure SQL — depth may
    // legitimately sit in the row because the row is a node, and Phase 2
    // collapses the per-depth copies with DISTINCT before the single edge-
    // emission pass (same contract, same self-loop output filter).
    // The Phase-1 self-loop exclusion is load-bearing, not cosmetic: the store
    // contains self-loops (virtual-output fix), and a (node, depth) row on a
    // self-loop would otherwise climb depths forever when __param_max_depth is
    // NULL (unbounded).  With the exclusion, NULL terminates on acyclic stores;
    // only longer malformed cycles still need the cap, which defaults to 100
    // like the old guard.  Cap semantics match recursiveQuery5 layer for layer.
    // Verified on the live chronicle store: DISTINCT edge sets identical to the
    // deployed function (460,826 edges unfiltered from the dispatch anchor;
    // 976 rel-filtered from the deep anchor; zero difference both ways),
    // cap-5 parity with the old CTE truncated at rt.depth < 5 (6 edges), and
    // the unfiltered deep closure at cap 5 — where the old form times out —
    // completes with counts identical to recursiveQuery5 (11,540,529 edges,
    // 196,288 nodes).
    // Choice summary: recursiveQuery4 = pure SQL, uncapped, once per node;
    // recursiveQuery5 = PL/pgSQL, capped, once per node;
    // recursiveQuery6 = pure SQL, capped, once per (node, path length).
    String recursiveQuery6 = """
            DROP FUNCTION IF EXISTS public.backwardtraversal_star(integer, text, text);
            DROP FUNCTION IF EXISTS public.backwardtraversal_star(integer, text, text, integer[]);

            CREATE OR REPLACE FUNCTION public.backwardtraversal_star(
                __param_id                  integer,
                __param_template            text,
                __param_property            text,
                __param_selected_relations  integer[]  DEFAULT NULL,
                __param_max_depth           integer    DEFAULT 100
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
            WITH RECURSIVE reached AS (

                -- ── Phase 1: (node, depth) recursion ─────────────────────────────────────
                -- The row is a node plus the depth at which some path reaches it, NOT an
                -- edge: all fan-in duplicates of a node at a given depth collapse into one
                -- row.  A node reachable at several distinct depths still appears once per
                -- depth (path-length multiplicity) — Phase 2 collapses those copies.
                -- Depth in the row is what makes the cap expressible in pure SQL; layers
                -- 0 .. __param_max_depth - 1 are the consumers whose expansion in Phase 2
                -- yields edges at depth 1 .. __param_max_depth, matching the old
                -- `rt.depth < 100` cap.  The relation-type filter is applied on every
                -- hop, starting with the expansion of the anchor itself.
                SELECT
                    __param_id       AS id,
                    __param_template AS tpl,
                    __param_property AS prop,
                    0                AS depth

                UNION

                SELECT
                    bt.out_id,
                    bt.out_template,
                    bt.out_property,
                    r.depth + 1
                FROM
                    reached r
                    JOIN predecessor_table pt
                        ON  pt.template = r.tpl
                        AND pt.output   = r.prop
                        AND (
                            __param_selected_relations IS NULL
                            OR pt.rel = ANY(__param_selected_relations)
                        )
                    CROSS JOIN LATERAL backwardTraversal(
                        r.id,
                        r.tpl,
                        pt.input
                    ) AS bt
                WHERE
                    pt.input IS NOT NULL
                    -- Self-loop hops (virtual-output fix) would re-reach this very node at
                    -- depth + 1, + 2, ... — with a NULL (unbounded) cap that never
                    -- terminates, so they are excluded from the walk itself, not just
                    -- from the output.  They add no reachable node.
                    AND NOT (bt.out_id = r.id AND bt.out_template = r.tpl)
                    AND (__param_max_depth IS NULL OR r.depth < __param_max_depth - 1)

            ),
            frontier AS (
                -- Collapse the per-depth copies: each node expands once in Phase 2
                -- however many distinct depths reached it.
                SELECT DISTINCT id, tpl, prop
                FROM   reached
                WHERE  __param_max_depth IS NULL OR depth <= __param_max_depth - 1
            )
            -- ── Phase 2: edge emission ───────────────────────────────────────────────────
            -- One non-recursive expansion pass over the deduplicated node set, emitting
            -- the same 6-tuple contract as before.  The anchor sits in `frontier` and
            -- contributes the base edges; nodes whose output column has no
            -- predecessor_table entry are dead ends and contribute nothing.
            SELECT DISTINCT
                bt.in_id,
                bt.in_template,
                bt.in_property,
                bt.out_id,
                bt.out_template,
                bt.out_property
            FROM
                frontier r
                JOIN predecessor_table pt
                    ON  pt.template = r.tpl
                    AND pt.output   = r.prop
                    AND (
                        __param_selected_relations IS NULL
                        OR pt.rel = ANY(__param_selected_relations)
                    )
                CROSS JOIN LATERAL backwardTraversal(
                    r.id,
                    r.tpl,
                    pt.input
                ) AS bt
            WHERE
                pt.input IS NOT NULL
                -- Self-loops arise from the virtual-output fix for activity-output-only
                -- templates (e.g. document_obligating_coin): backwardTraversal joins the
                -- table with itself on an input column used as a virtual output, matching
                -- the same row.  Self-loops are never valid in a provenance graph.
                AND NOT (bt.in_id = bt.out_id AND bt.in_template = bt.out_template)
            $function$;

            """;

    // forward_traversal_star — the exact transpose of backwardtraversal_star.
    // Reuses the SAME backward_dispatch and predecessor_table (no new metadata):
    //   * forwardTraversal (generateForwardTemplateTraversal) reads the dispatch
    //     edge backwards — matches the TARGET (producer output) and returns the
    //     SOURCE (consumer input) rows — to find the consumers of an entity.
    //   * the recursion reads predecessor_table by `input` (not `output`) to obtain
    //     each consumer's output column (the next frontier) and the rel for filtering.
    // A forward edge B->A exists iff the backward edge A->B exists (identical three
    // conditions: predecessor_table(A) edge + backward_dispatch(A.input->B.output) +
    // matching entity value), so the filtered edge sets are exact transposes.
    String recursiveForwardQuery = """
                        CREATE OR REPLACE FUNCTION public.forward_traversal_star(
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
                -- Treat __param_property as an OUTPUT entity column of the start row (the same
                -- contract as backwardtraversal_star).  forwardTraversal finds the consumer
                -- rows whose INPUT column holds that entity; the predecessor_table join then
                -- (a) applies the relation-type filter to the consumer's derivation edge and
                -- (b) yields the consumer's OUTPUT column, which becomes the next frontier.
                SELECT
                    ft.in_id,
                    ft.in_template,
                    ft.in_property,
                    ft.out_id,
                    ft.out_template,
                    ft.out_property,
                    pt.output AS next_property,
                    1 AS depth
                FROM
                    forwardTraversal(
                        __param_id,
                        __param_template,
                        __param_property
                    ) AS ft
                    JOIN predecessor_table pt
                        ON  pt.template = ft.out_template
                        AND pt.input    = ft.out_property
                        AND (
                            __param_selected_relations IS NULL
                            OR pt.rel = ANY(__param_selected_relations)
                        )

                UNION   -- UNION (not UNION ALL) provides cycle-safety via row deduplication

                -- ── Recursive step ────────────────────────────────────────────────────────
                -- From each reached consumer advance via its own OUTPUT column (rt.next_property)
                -- to that output's consumers.  The relation-type filter is re-applied at each hop.
                SELECT
                    ft.in_id,
                    ft.in_template,
                    ft.in_property,
                    ft.out_id,
                    ft.out_template,
                    ft.out_property,
                    pt.output AS next_property,
                    rt.depth + 1 AS depth
                FROM
                    recurse_traverse rt
                    CROSS JOIN LATERAL forwardTraversal(
                        rt.out_id,
                        rt.out_template,
                        rt.next_property
                    ) AS ft
                    JOIN predecessor_table pt
                        ON  pt.template = ft.out_template
                        AND pt.input    = ft.out_property
                        AND (
                            __param_selected_relations IS NULL
                            OR pt.rel = ANY(__param_selected_relations)
                        )
                WHERE
                    rt.next_property IS NOT NULL
                    AND rt.depth < 100   -- guard against runaway cycles in malformed graphs

            )
            SELECT in_id, in_template, in_property, out_id, out_template, out_property
            FROM   recurse_traverse
            -- Self-loops mirror the backwardtraversal_star guard (virtual-output fix for
            -- activity-output-only templates); never valid in a provenance graph.
            WHERE NOT (in_id = out_id AND in_template = out_template)
            $function$;

            CREATE OR REPLACE FUNCTION public.forward_traversal_star_nodes(
                __param_id                  integer,
                __param_template            text,
                __param_property            text,
                __param_selected_relations  integer[]  DEFAULT NULL
            )
            RETURNS TABLE(
                out_id       integer,
                out_template text,
                out_property text
            )
            LANGUAGE sql
            AS $function$
            SELECT DISTINCT out_id, out_template, out_property
            FROM   forward_traversal_star(
                       __param_id,
                       __param_template,
                       __param_property,
                       __param_selected_relations
                   )
            $function$;

            """;

    // forward_traversal_star, memoised BFS rewrite — the exact transpose of
    // recursiveQuery5, kept for the record (unwired; Step 4 installs
    // forward_traversal_star6).  PL/pgSQL breadth-first search: a temp table
    // memoises visited (id, template, property) triples — prop is the OUTPUT
    // column to advance through, the old form's next_property — each expanded
    // exactly once, at the layer BFS first reaches it, and the loop counter is
    // the depth, so __param_max_depth bounds the work done (NULL = unbounded;
    // memoisation over the finite triple set still guarantees termination,
    // self-loops included, so no walk guard is needed).  Phase 2's pt join and
    // NULL-pt.output handling follow forward_traversal_star6.  Uses its own
    // temp table name (__fts_reached) so it can never collide with the
    // backward function's; like recursiveQuery5 it is not reentrant, which is
    // safe as nothing in the traversal stack calls it.
    // Verified on the live chronicle store: DISTINCT edge sets identical to
    // the deployed forward_traversal_star on the deep 61-layer closure (354
    // edges) and the dispatch anchor (7), zero difference each way; cap 5
    // returns the established 5-edge truncation.
    // If ever wired in place of forward_traversal_star6, append the
    // forward_traversal_star_nodes wrapper from that block as well — this
    // string deliberately contains only the star function.
    String forward_traversal_star5 = """
            DROP FUNCTION IF EXISTS public.forward_traversal_star(integer, text, text);
            DROP FUNCTION IF EXISTS public.forward_traversal_star(integer, text, text, integer[]);

            CREATE OR REPLACE FUNCTION public.forward_traversal_star(
                __param_id                  integer,
                __param_template            text,
                __param_property            text,
                __param_selected_relations  integer[]  DEFAULT NULL,
                __param_max_depth           integer    DEFAULT 100
            )
            RETURNS TABLE(
                in_id        integer,
                in_template  text,
                in_property  text,
                out_id       integer,
                out_template text,
                out_property text
            )
            LANGUAGE plpgsql
            AS $function$
            DECLARE
                __layer    integer := 0;   -- last BFS layer inserted (anchor = 0)
                __inserted bigint;
            BEGIN
                -- Visited set.  The primary key memoises each (node, output-property)
                -- triple: it is inserted once, with the minimal hop count at which BFS
                -- reaches it, and expanded exactly once.  Session-scoped; TRUNCATE (not
                -- DROP/CREATE) so repeated calls in one transaction reuse the table.
                CREATE TEMP TABLE IF NOT EXISTS __fts_reached (
                    id    integer,
                    tpl   text,
                    prop  text,
                    depth integer,
                    PRIMARY KEY (id, tpl, prop)
                ) ON COMMIT DROP;
                CREATE INDEX IF NOT EXISTS __fts_reached_depth ON __fts_reached (depth);
                TRUNCATE __fts_reached;

                INSERT INTO __fts_reached
                VALUES (__param_id, __param_template, __param_property, 0);

                -- ── Phase 1: breadth-first node search ───────────────────────────────
                -- Each iteration expands only the triples first reached at the previous
                -- layer; ON CONFLICT DO NOTHING drops already-visited triples, so no
                -- node re-expands however many paths (or path lengths) reach it.
                -- Layers 1 .. __param_max_depth - 1 are filled: their nodes are the
                -- producers whose expansion in Phase 2 yields edges at depth
                -- 1 .. __param_max_depth, matching the old `rt.depth < 100` cap.
                WHILE __param_max_depth IS NULL OR __layer < __param_max_depth - 1 LOOP
                    INSERT INTO __fts_reached (id, tpl, prop, depth)
                    SELECT DISTINCT
                        ft.out_id, ft.out_template, pt.output, __layer + 1
                    FROM
                        __fts_reached r
                        CROSS JOIN LATERAL forwardTraversal(r.id, r.tpl, r.prop) AS ft
                        JOIN predecessor_table pt
                            ON  pt.template = ft.out_template
                            AND pt.input   = ft.out_property
                            AND (
                                __param_selected_relations IS NULL
                                OR pt.rel = ANY(__param_selected_relations)
                            )
                    WHERE
                        r.depth = __layer
                        AND pt.output IS NOT NULL
                    ON CONFLICT DO NOTHING;

                    GET DIAGNOSTICS __inserted = ROW_COUNT;
                    EXIT WHEN __inserted = 0;   -- frontier exhausted before the cap
                    __layer := __layer + 1;
                END LOOP;

                -- ── Phase 2: edge emission ───────────────────────────────────────────
                -- One expansion pass over the deduplicated visited set, emitting the
                -- same 6-tuple contract as before.  The pt join is part of the contract
                -- (only consumer edges carrying a matching, rel-passing
                -- predecessor_table entry are returned); NULL pt.output edges ARE
                -- emitted — they are dead ends, not non-edges.  The depth filter only
                -- matters for __param_max_depth = 0.
                RETURN QUERY
                SELECT DISTINCT
                    ft.in_id,
                    ft.in_template,
                    ft.in_property,
                    ft.out_id,
                    ft.out_template,
                    ft.out_property
                FROM
                    __fts_reached r
                    CROSS JOIN LATERAL forwardTraversal(r.id, r.tpl, r.prop) AS ft
                    JOIN predecessor_table pt
                        ON  pt.template = ft.out_template
                        AND pt.input   = ft.out_property
                        AND (
                            __param_selected_relations IS NULL
                            OR pt.rel = ANY(__param_selected_relations)
                        )
                WHERE
                    (__param_max_depth IS NULL OR r.depth < __param_max_depth)
                    -- Self-loops mirror the backwardtraversal_star guard (virtual-output
                    -- fix for activity-output-only templates); never valid in a
                    -- provenance graph.
                    AND NOT (ft.in_id = ft.out_id AND ft.in_template = ft.out_template);
            END;
            $function$;

            """;

    // forward_traversal_star, capped pure-SQL rewrite via (node, depth)
    // accumulation — the exact transpose of recursiveQuery6, replacing
    // recursiveForwardQuery (which has the same (edge, depth) defect as
    // recursiveQuery3: its rows carry depth AND next_property, so UNION
    // dedupes per path length and every copy re-expands).
    // Phase 1 recurses over (id, template, property, depth) where property is
    // the OUTPUT column to expand forward through — exactly the role the old
    // form's next_property column was smuggling into the row.  Each hop calls
    // forwardTraversal(node, prop) to find the consumers of that output's
    // entity, then predecessor_table (joined on the CONSUMER's template +
    // input column, where the relation-type filter applies) yields the
    // consumer's output columns, the next frontier.  A consumer edge whose
    // input column has no predecessor_table entry is emitted (Phase 2) but is
    // a dead end (old behaviour: next_property IS NULL rows never expanded);
    // NULL pt.output rows are likewise kept out of the frontier.
    // Cap semantics, self-loop handling (excluded from the walk, filtered from
    // the output) and the NULL-parameter conventions mirror recursiveQuery6.
    // Verified on the live chronicle store: DISTINCT edge sets identical to
    // the deployed forward_traversal_star on a deep 61-layer closure (354
    // edges, where the old form returns 804 duplicated rows), a dispatch
    // anchor (7), and a rel-filtered run (354) — zero difference each way —
    // and cap-5 parity with the old CTE truncated at rt.depth < 5 (5 edges).
    // Measured phase-1 path-length duplication: 2.0× (659 rows for 330
    // distinct triples).
    // The block re-creates forward_traversal_star_nodes unchanged (it was
    // previously installed by recursiveForwardQuery); its 4-arg call into the
    // 5-arg star resolves through the max-depth default.
    String forward_traversal_star6 = """
            DROP FUNCTION IF EXISTS public.forward_traversal_star(integer, text, text);
            DROP FUNCTION IF EXISTS public.forward_traversal_star(integer, text, text, integer[]);

            CREATE OR REPLACE FUNCTION public.forward_traversal_star(
                __param_id                  integer,
                __param_template            text,
                __param_property            text,
                __param_selected_relations  integer[]  DEFAULT NULL,
                __param_max_depth           integer    DEFAULT 100
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
            WITH RECURSIVE reached AS (

                -- ── Phase 1: (node, depth) recursion ─────────────────────────────────────
                -- The row is a consumer node plus the OUTPUT column to advance through
                -- (the old next_property), NOT an edge: fan-out duplicates collapse into
                -- one row per depth; per-path-length copies remain and are collapsed by
                -- Phase 2.  Layers 0 .. __param_max_depth - 1 are the producers whose
                -- expansion in Phase 2 yields edges at depth 1 .. __param_max_depth,
                -- matching the old `rt.depth < 100` cap.  The relation-type filter
                -- applies to the reached consumer's own predecessor_table edge, as in
                -- the old form.
                SELECT
                    __param_id       AS id,
                    __param_template AS tpl,
                    __param_property AS prop,
                    0                AS depth

                UNION

                SELECT
                    ft.out_id,
                    ft.out_template,
                    pt.output,
                    r.depth + 1
                FROM
                    reached r
                    CROSS JOIN LATERAL forwardTraversal(
                        r.id,
                        r.tpl,
                        r.prop
                    ) AS ft
                    JOIN predecessor_table pt
                        ON  pt.template = ft.out_template
                        AND pt.input   = ft.out_property
                        AND (
                            __param_selected_relations IS NULL
                            OR pt.rel = ANY(__param_selected_relations)
                        )
                WHERE
                    pt.output IS NOT NULL
                    -- Self-loop hops (virtual-output fix) would re-reach this very node
                    -- at depth + 1, + 2, ... — with a NULL (unbounded) cap that never
                    -- terminates, so they are excluded from the walk itself, not just
                    -- from the output.  They add no reachable node.
                    AND NOT (ft.out_id = r.id AND ft.out_template = r.tpl)
                    AND (__param_max_depth IS NULL OR r.depth < __param_max_depth - 1)

            ),
            frontier AS (
                -- Collapse the per-depth copies: each (node, output column) expands once
                -- in Phase 2 however many distinct depths reached it.
                SELECT DISTINCT id, tpl, prop
                FROM   reached
                WHERE  __param_max_depth IS NULL OR depth <= __param_max_depth - 1
            )
            -- ── Phase 2: edge emission ───────────────────────────────────────────────────
            -- One non-recursive expansion pass over the deduplicated node set, emitting
            -- the same 6-tuple contract as before.  The pt join is part of the contract
            -- (the old form only emitted consumer edges carrying a matching, rel-passing
            -- predecessor_table entry), but NULL pt.output edges ARE emitted — they are
            -- dead ends, not non-edges.
            SELECT DISTINCT
                ft.in_id,
                ft.in_template,
                ft.in_property,
                ft.out_id,
                ft.out_template,
                ft.out_property
            FROM
                frontier r
                CROSS JOIN LATERAL forwardTraversal(
                    r.id,
                    r.tpl,
                    r.prop
                ) AS ft
                JOIN predecessor_table pt
                    ON  pt.template = ft.out_template
                    AND pt.input   = ft.out_property
                    AND (
                        __param_selected_relations IS NULL
                        OR pt.rel = ANY(__param_selected_relations)
                    )
            WHERE
                -- Self-loops mirror the backwardtraversal_star guard (virtual-output fix
                -- for activity-output-only templates); never valid in a provenance graph.
                NOT (ft.in_id = ft.out_id AND ft.in_template = ft.out_template)
            $function$;

            CREATE OR REPLACE FUNCTION public.forward_traversal_star_nodes(
                __param_id                  integer,
                __param_template            text,
                __param_property            text,
                __param_selected_relations  integer[]  DEFAULT NULL
            )
            RETURNS TABLE(
                out_id       integer,
                out_template text,
                out_property text
            )
            LANGUAGE sql
            AS $function$
            SELECT DISTINCT out_id, out_template, out_property
            FROM   forward_traversal_star(
                       __param_id,
                       __param_template,
                       __param_property,
                       __param_selected_relations
                   )
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
        // backwardtraversal_star (calls backwardTraversal recursively) — the
        // T-187 capped two-phase form (recursiveQuery6): node-memoised
        // traversal with a __param_max_depth parameter (default 100).
        querier.do_statements(null,
                null,
                (sb, data) -> {
                    sb.append(generateBackwardTemplateTraversal(ioMap));
                    sb.append(recursiveQuery6);
                });

        // Step 4: install the forward (descendant) counterpart — forwardTraversal
        // (reverse dispatch) and forward_traversal_star / _nodes, in the T-187
        // capped two-phase form (forward_traversal_star6, the transpose of
        // recursiveQuery6).  Reuses the same backward_dispatch +
        // predecessor_table populated in Steps 1-2.
        querier.do_statements(null,
                null,
                (sb, data) -> {
                    sb.append(generateForwardTemplateTraversal());
                    sb.append(forward_traversal_star6);
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
                    // Read column metadata once, up front — NOT inside the row loop.
                    // (The previous nested `while (rs.next())` advanced past the first row to
                    //  read metadata, then iterated from the second row, silently dropping the
                    //  newest record.  Harmless for multi-row tables, but a single-row table
                    //  such as currency_creating returned zero rows.)
                    ResultSetMetaData meta = rs.getMetaData();
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

    public Map<String,Set<String>> getUniqueMap(String uniqueMapString) {
        try {
            return om.readValue(uniqueMapString, typeRef3);
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
     * Generates the {@code CREATE OR REPLACE FUNCTION forwardTraversal} statement —
     * the descendant (forward) counterpart of {@link #generateBackwardTemplateTraversal}.
     *
     * <p>Where {@code backwardTraversal} hops consumer&rarr;producer (it matches the
     * {@code backward_dispatch} SOURCE side — a consumer input column — to find the
     * producer rows), {@code forwardTraversal} does the reverse: from a producer's
     * OUTPUT column it matches the dispatch TARGET side and returns the SOURCE
     * (consumer input) rows, i.e. the rows that consume the entity.  No new metadata
     * is required — it reuses the same {@code backward_dispatch} table read backwards.
     *
     * <p>Same two-{@code EXECUTE} / dynamic-SQL plan-cache design as
     * {@link #generateBackwardTemplateTraversal}.
     */
    public String generateForwardTemplateTraversal() {
        StringBuilder sb = new StringBuilder();
        sb.append("-- Generated by ").append(getClass().getName()).append(".generateForwardTemplateTraversal\n");
        sb.append("-- Descendant counterpart of backwardTraversal: reads backward_dispatch in reverse.\n");
        sb.append("-- Requires backward_dispatch to be populated first (generateClearAndPopulateBackwardDispatch).\n");
        sb.append("CREATE OR REPLACE FUNCTION forwardTraversal(\n");
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
        sb.append("    -- 1. Fetch the entity value from the (producer) source row.\n");
        sb.append("    EXECUTE format('SELECT %I FROM %I WHERE id = $1',\n");
        sb.append("                   ").append(PARAM_PROPERTY).append(", ")
                                       .append(PARAM_TEMPLATE).append(")\n");
        sb.append("        INTO entity_val\n");
        sb.append("        USING ").append(PARAM_ID).append(";\n\n");
        sb.append("    IF entity_val IS NULL THEN RETURN; END IF;\n\n");
        sb.append("    -- 2. Build a UNION ALL over every CONSUMER column that references this\n");
        sb.append("    --    entity.  Reverse of backwardTraversal: match the dispatch TARGET\n");
        sb.append("    --    (producer output) and return the SOURCE (consumer input) rows.\n");
        sb.append("    SELECT string_agg(\n");
        sb.append("        format(\n");
        sb.append("            'SELECT $1::integer, $2::text, $3::text, id, ''%s''::text, ''%s''::text\n");
        sb.append("               FROM %I WHERE %I = $4',\n");
        sb.append("            source_template, source_property,\n");
        sb.append("            source_template, source_property\n");
        sb.append("        ),\n");
        sb.append("        ' UNION ALL '\n");
        sb.append("    ) INTO union_sql\n");
        sb.append("    FROM backward_dispatch\n");
        sb.append("    WHERE target_template = ").append(PARAM_TEMPLATE).append("\n");
        sb.append("    AND   target_property  = ").append(PARAM_PROPERTY).append(";\n\n");
        sb.append("    IF union_sql IS NULL THEN RETURN; END IF;\n\n");
        sb.append("    -- 3. Execute the combined UNION ALL.\n");
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

        // Route through the instance cache when the caller passes our own ioMap (the
        // only pattern observed inside ProvToolbox); fall back to a fresh derivation
        // for any unusual external invocation with a different map.
        List<String[]> rows = (ioMap == this.ioMap)
                ? getBackwardDispatchRows()
                : collectBackwardDispatchRows(ioMap);

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

        // Index used by the PL/pgSQL forwardTraversal to look up relevant hops
        // (forward traversal queries backward_dispatch by target, not source).
        sb.append("CREATE INDEX IF NOT EXISTS backward_dispatch_tgt_idx\n");
        sb.append("    ON backward_dispatch (target_template, target_property);\n");

        return sb.toString();
    }

    /**
     * Returns the cached {@code backward_dispatch} rows for this instance.  The
     * derivation runs at most once per {@code TemplateQuery}: subsequent calls return
     * the same list.  All three internal SQL generators
     * ({@link #generateClearAndPopulateBackwardDispatch},
     * {@link #generateCreateTraversalIndexes},
     * {@link #generateDropTraversalIndexes}) go through this getter so they share
     * one computation and stay in agreement about which edges exist.
     *
     * @return the dispatch rows {@code (in_template, in_property, out_template, out_property)}
     *         derived from {@link #ioMap}; deterministic order (LinkedHashSet)
     */
    public List<String[]> getBackwardDispatchRows() {
        if (cachedBackwardDispatchRows == null) {
            cachedBackwardDispatchRows = collectBackwardDispatchRows(ioMap);
        }
        return cachedBackwardDispatchRows;
    }

    /**
     * Iterates the {@code ioMap} and returns the dispatch tuples
     * {@code (in_template, in_property, out_template, out_property)} — the producer/consumer
     * edge set used by {@code backwardTraversal} and {@code forwardTraversal} SQL functions
     * (and, externally, by step-spec validators / IT assertion libraries / build-time
     * materialisers that need the catalogue's dispatch without running SQL).
     *
     * <p>Public and {@code static} because the derivation is pure — it uses no instance
     * state — and external build-time tools (e.g. the {@code backward-dispatch.json}
     * generator in {@code odoo-templates}) need to call it without instantiating
     * {@code TemplateQuery} (which would require a live {@link Querier} / DB connection).
     * Internal callers should use {@link #getBackwardDispatchRows()} instead, which caches.
     *
     * @param ioMap input/output property map (short SQL names), as returned by {@link #getIoMap}
     * @return the dispatch rows; deterministic order via {@link LinkedHashSet}
     */
    public static List<String[]> collectBackwardDispatchRows(Map<String, Map<String, Map<String, String>>> ioMap) {

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
     * Returns SQL that creates the secondary btree indexes required to make both
     * the dynamic UNION ALL inside {@code backwardTraversal} and the one inside
     * {@code forwardTraversal} fast.
     *
     * <p>For every distinct <em>target</em> table referenced in {@code backward_dispatch}
     * this emits a {@code CREATE INDEX IF NOT EXISTS} on {@code (id)} and one on
     * each referenced {@code (target_property)} — these serve {@code backwardTraversal},
     * which queries {@code FROM target_table WHERE target_property = $entity}.
     *
     * <p>For every distinct <em>source</em> table referenced in {@code backward_dispatch}
     * this additionally emits {@code (id)} and {@code (source_property)} indexes —
     * these serve {@code forwardTraversal}, which queries
     * {@code FROM source_table WHERE source_property = $entity}.  Without these,
     * every {@code forward_traversal_star} call degrades to seq-scans at the same
     * cost as an un-indexed backward traversal (~25× slower than the indexed path).
     *
     * <p>All statements use {@code IF NOT EXISTS}; this method is idempotent and
     * safe to run on every Chronicle startup.
     *
     * <p>Index names follow {@link #traversalIndexName} and share the
     * {@link #TRAVERSAL_INDEX_PREFIX} so they can be enumerated and dropped
     * uniformly by {@link #generateDropTraversalIndexes}.
     *
     * @param ioMap input/output property map (short SQL names), as returned by {@link #getIoMap}
     * @return a SQL string containing only {@code CREATE INDEX IF NOT EXISTS} statements
     */
    public String generateCreateTraversalIndexes(Map<String, Map<String, Map<String, String>>> ioMap) {
        List<String[]> rows = (ioMap == this.ioMap)
                ? getBackwardDispatchRows()
                : collectBackwardDispatchRows(ioMap);

        // Four ordered sets — LinkedHashSet preserves insertion order for deterministic SQL.
        // Target side  (r[2], r[3]): consumed by backwardTraversal  (WHERE target_property = $v)
        // Source side  (r[0], r[1]): consumed by forwardTraversal   (WHERE source_property = $v)
        Set<String>                   tables    = new LinkedHashSet<>();
        Set<Map.Entry<String,String>> tableCols = new LinkedHashSet<>();
        for (String[] r : rows) {
            // backward traversal — target side
            tables.add(r[2]);
            tableCols.add(new AbstractMap.SimpleEntry<>(r[2], r[3]));
            // forward traversal — source side (same naming scheme; IF NOT EXISTS is idempotent)
            tables.add(r[0]);
            tableCols.add(new AbstractMap.SimpleEntry<>(r[0], r[1]));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-- Generated by ").append(getClass().getName()).append(".generateCreateTraversalIndexes\n");
        sb.append("-- Indexes consumed by backwardTraversal AND forwardTraversal dynamic UNION ALLs.\n\n");
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
        List<String[]> rows = (ioMap == this.ioMap)
                ? getBackwardDispatchRows()
                : collectBackwardDispatchRows(ioMap);

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

    private static Map<String, Map<String, String>> filterMapAccordingToTable(String table, Map<String, Map<String, String>> input) {
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
