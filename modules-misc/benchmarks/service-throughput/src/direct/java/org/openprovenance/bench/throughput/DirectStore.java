package org.openprovenance.bench.throughput;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.model.interop.PrincipalManager;
import org.openprovenance.prov.model.interop.SubmissionKeyManager;
import org.openprovenance.prov.service.core.AsyncRelationWriter;
import org.openprovenance.prov.service.core.DirectAccess;
import org.openprovenance.prov.service.core.PooledConnections;
import org.openprovenance.prov.service.core.Querier;
import org.openprovenance.prov.service.core.Storage;
import org.openprovenance.prov.service.core.SubmissionSwitches;
import org.openprovenance.prov.service.core.TemplateLogic;
import org.openprovenance.prov.service.core.TemplateQuery;
import org.openprovenance.prov.service.core.TemplateService;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The store without the web service: the generated catalogue (provenance-templates-library's
 * {@code <catalogue.package>.CatalogueDispatcher}, loaded by name as the service loads it), its enactors composing the
 * very SQL the service sends (the generated query invokers: {@code WITH insertion_result AS (SELECT * FROM
 * insert_<t>(…)) INSERT INTO record_index … RETURNING id}), executed on JDBC connections of the harness's own, against
 * provbench; and, for {@code LAYER=post}, the service's own post-processing, in-process: {@link TemplateLogic#submitPostProcessing}
 * (read-back through {@link TemplateQuery}, hash and its {@code UPDATE}, the {@code __PROV_*} relations through
 * {@code RelationMapping}, or the {@link AsyncRelationWriter}), under the same {@link SubmissionSwitches}.
 *
 * <p>Connections: a {@link PooledConnections} of N, one per writer thread, each transaction on one
 * ({@link Querier#inTransaction}: a record's statements, or B records', commit together).</p>
 */
final class DirectStore implements AutoCloseable {

    static final String WRITER_APP = "provbench-direct-writer";
    static final String RELATION_WRITER_APP = "provbench-relation-writer";
    private static final ObjectMapper OM = new ObjectMapper();

    final Family family;
    final boolean post;
    final boolean key;
    final boolean jdbcBatch;
    final SubmissionSwitches switches;
    final double setupSeconds;

    private final Storage storage = new Storage();
    private final Connection setupConnection;
    private final PooledConnections pool;
    private final Querier querier;
    private final CatalogueDispatcherInterface<FileBuilder> dispatcher;
    private final TemplateQuery query;
    private final TemplateLogic logic;
    private final AsyncRelationWriter relationWriter;
    private final PrincipalManager principals = new PrincipalManager();
    private final SubmissionKeyManager keys = new SubmissionKeyManager();
    /** Set while a JDBC batch is being composed: the statements collected instead of run. */
    private final ThreadLocal<List<String>> capture = new ThreadLocal<>();
    private final Map<String, String[]> properties;
    private final Map<String, Function<Object[], ?>> enactors;
    private final Map<String, Function<List<Object[]>, ?>> compositeEnactors;
    /** The rows a record of each template writes (expected, from the catalogue): the run's rows/record check. */
    private final Map<String, Integer> rowsPerEntry = new HashMap<>();

    /**
     * Creates the schema in provbench as the service does at start-up (sql.initializer, then utils.sql, then what
     * {@link TemplateQuery}'s constructor installs), and the catalogue's enactors.
     *
     * @param connections the writers' connections (N)
     */
    @SuppressWarnings("unchecked")
    DirectStore(Settings s, Family family, int connections, Connector connector) throws Exception {
        long t0 = System.nanoTime();
        this.family = family;
        this.post = layer(s).equals("post");
        this.key = onOff(s, "KEY");
        this.jdbcBatch = !s.get("JDBC_BATCH").equals("0");
        if (jdbcBatch && post) throw new IllegalArgumentException("JDBC_BATCH needs LAYER=insert: the post-processing reads each record's id before its next statement");
        this.switches = switchesOf(s);

        JsonNode config = OM.readTree(new File(s.serviceTarget(), "test-classes/template-config.json"));
        String cataloguePackage = config.get("catalogue.package").asText();
        if (!cataloguePackage.equals(family.cataloguePackage)) throw new IllegalStateException(cataloguePackage + " is not the " + family.name + " catalogue");

        this.setupConnection = connector.open("provbench-direct-setup");
        storage.executeStatements(setupConnection, resource(config.get("sql.initializer").asText()));
        storage.executeStatements(setupConnection, resource("/utils.sql"));

        this.pool = new PooledConnections(() -> {
            try {
                return connector.open(WRITER_APP);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }, connections);
        this.querier = new Querier(storage, setupConnection, pool);
        Function<String, ResultSet> executor = sql -> {
            List<String> collected = capture.get();
            if (collected != null) {
                collected.add(sql);
                return placeholderRow();
            }
            try {
                return storage.executeQuery(querier.currentConnection(), sql);   // as the service's queryExecutor
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        Map<String, String> substitution = new HashMap<>();
        substitution.put("PROV_HOST", Settings.SERVICE_URL);
        substitution.put("PROV_API", Settings.SERVICE_URL + "/provapi");
        this.dispatcher = (CatalogueDispatcherInterface<FileBuilder>) Class.forName(cataloguePackage + ".CatalogueDispatcher")
                .getConstructor(Map.class, ProvFactory.class)
                .newInstance(substitution, new org.openprovenance.prov.vanilla.ProvFactory());
        BiFunction<Integer, String, Object> postProcessing = post
                ? (id, template) -> postProcess(id, template)
                : (id, template) -> null;
        dispatcher.initEnactorConverter(executor, postProcessing, principals::getPrincipal);
        dispatcher.initCompositeEnactorConverter(executor, postProcessing, principals::getPrincipal);
        Map<String, TemplateService.Linker> linkers = OM.readValue(dispatcher.getLinkers(),
                OM.getTypeFactory().constructMapType(HashMap.class, String.class, TemplateService.Linker.class));
        this.query = new TemplateQuery(querier, dispatcher, principals, linkers, OM);
        this.logic = new TemplateLogic(new org.openprovenance.prov.vanilla.ProvFactory(), query, query.getShortNames(), dispatcher,
                principals, null, OM, null);
        logic.setSwitches(switches);
        if (post && switches.relations == SubmissionSwitches.Relations.ASYNC) {
            this.relationWriter = new AsyncRelationWriter(new PooledConnections(() -> {
                try {
                    return connector.open(RELATION_WRITER_APP);
                } catch (SQLException e) {
                    throw new IllegalStateException(e);
                }
            }, switches.asyncWriters), switches.asyncWriters, switches.asyncQueue, switches.asyncBatch);
            DirectAccess.setAsyncWriter(query, relationWriter);
        } else {
            this.relationWriter = null;
        }
        this.properties = dispatcher.getPropertyOrder();
        this.enactors = dispatcher.getEnactorConverter();
        this.compositeEnactors = dispatcher.getCompositeEnactorConverter();
        for (Family.Template t : family.templates) {
            if (!properties.containsKey(t.isA)) throw new IllegalStateException("the catalogue has no template " + t.isA);
            if (t.composite() ? compositeEnactors.get(t.isA) == null : enactors.get(t.isA) == null) {
                throw new IllegalStateException("the catalogue has no enactor for " + t.isA);
            }
            rowsPerEntry.put(t.isA, expectedRows(t, family.compositeSize()));
        }
        this.setupSeconds = (System.nanoTime() - t0) / 1e9;
    }

    /** Opens a connection to provbench, its backend named so in pg_stat_activity (the guard's {@link Store#connect}). */
    interface Connector {
        Connection open(String applicationName) throws SQLException;
    }

    static String layer(Settings s) {
        String layer = s.get("LAYER");
        if (!layer.equals("insert") && !layer.equals("post")) throw new IllegalArgumentException("LAYER is insert or post, not " + layer);
        return layer;
    }

    static boolean onOff(Settings s, String key) {
        String v = s.get(key);
        if (v.equals("on")) return true;
        if (v.equals("off")) return false;
        throw new IllegalArgumentException(key + " is on or off, not " + v);
    }

    /** The service's switches for this run (they apply with LAYER=post), read as the service reads its configuration. */
    static SubmissionSwitches switchesOf(Settings s) {
        Map<String, Object> config = new HashMap<>();
        config.put(SubmissionSwitches.READBACK_ACCESS_CONTROL, s.get("READBACK"));
        config.put(SubmissionSwitches.HASH, s.get("HASH"));
        config.put(SubmissionSwitches.RELATIONS, s.get("RELATIONS"));
        config.put(SubmissionSwitches.ASYNC_WRITERS, s.get("WRITERS"));
        config.put(SubmissionSwitches.ASYNC_QUEUE, s.get("QUEUE"));
        config.put(SubmissionSwitches.ASYNC_BATCH, s.get("BATCH"));
        return SubmissionSwitches.from(config, name -> null);
    }

    private Object postProcess(Integer id, String template) {
        return logic.submitPostProcessing(id == null ? -69 : id, template);
    }

    /** The writer thread's principal: the service's without security ("nil"). */
    void bindThread() {
        principals.setPrincipal("nil");
    }

    /** What a transaction did: its records' outputs (ids, for later records to refer to) and the first simple record's id. */
    static final class Written {
        final List<Long> outputs = new ArrayList<>();
        long firstSimpleId = -1;
        String firstSimpleTemplate;
        int expectedRows;
    }

    /** B entries of one template in ONE transaction (each with its own Idempotency-Key when KEY=on). */
    Written write(Family.Template template, List<Map<String, Object>> entries) {
        return querier.inTransaction(() -> jdbcBatch ? writeBatched(template, entries) : writeEach(template, entries));
    }

    private Written writeEach(Family.Template template, List<Map<String, Object>> entries) {
        Written w = new Written();
        for (Map<String, Object> entry : entries) {
            Object result = enact(template, entry);
            w.expectedRows += rowsPerEntry.get(template.isA);
            JsonNode tree = OM.valueToTree(result);
            if (w.firstSimpleId < 0 && !template.composite()) {
                w.firstSimpleId = tree.path("ID").asLong(-1);
                w.firstSimpleTemplate = template.isA;
            }
            collect(tree, w.outputs);
        }
        return w;
    }

    /** The B statements composed by the enactors without running them, then sent in one round trip, results read in turn. */
    private Written writeBatched(Family.Template template, List<Map<String, Object>> entries) {
        Written w = new Written();
        List<String> statements = new ArrayList<>(entries.size());
        capture.set(statements);
        try {
            for (Map<String, Object> entry : entries) {
                enact(template, entry);
                w.expectedRows += rowsPerEntry.get(template.isA);
            }
        } finally {
            capture.remove();
        }
        StringBuilder sb = new StringBuilder();
        for (String sql : statements) sb.append(sql.trim()).append(sql.trim().endsWith(";") ? "\n" : ";\n");
        try (Statement st = querier.currentConnection().createStatement()) {
            boolean isResult = st.execute(sb.toString());
            int results = 0;
            while (true) {
                if (isResult) {
                    try (ResultSet rs = st.getResultSet()) {
                        int columns = rs.getMetaData().getColumnCount();
                        while (rs.next()) {
                            for (int c = 2; c <= columns; c++) {
                                Object o = rs.getObject(c);
                                if (o instanceof Integer && w.outputs.size() < 4) w.outputs.add(((Integer) o).longValue());
                            }
                        }
                    }
                    results++;
                } else if (st.getUpdateCount() == -1) {
                    break;
                }
                isResult = st.getMoreResults();
            }
            if (results != statements.size()) throw new IllegalStateException(results + " result(s) for " + statements.size() + " statement(s)");
        } catch (SQLException e) {
            throw new RuntimeException("a JDBC batch of " + statements.size() + " failed: " + e.getMessage(), e);
        }
        return w;
    }

    private Object enact(Family.Template template, Map<String, Object> entry) {
        if (key) keys.setSubmissionKey(UUID.randomUUID().toString());
        try {
            if (!template.composite()) {
                return enactors.get(template.isA).apply(record(entry, properties.get(template.isA)));
            }
            List<Object[]> records = new ArrayList<>();
            records.add(record(entry, properties.get(template.isA)));
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> elements = (List<Map<String, Object>>) entry.get("__elements");
            for (Map<String, Object> e : elements) records.add(record(e, properties.get(template.element.isA)));
            return compositeEnactors.get(template.isA).apply(records);
        } finally {
            if (key) keys.clearSubmissionKey();
        }
    }

    /**
     * A record as the generated beans are built from it: the template's values in its property order (the catalogue's
     * {@code getPropertyOrder()}), as {@code TemplateLogic.convertToArray} builds it from a posted JSON entry; numbers as
     * JSON would give them (ints as Integer).
     */
    static Object[] record(Map<String, Object> entry, String[] props) {
        Object[] array = new Object[props.length];
        for (int i = 0; i < props.length; i++) {
            Object v = entry.get(props[i]);
            if (v instanceof Long && (Long) v <= Integer.MAX_VALUE && (Long) v >= Integer.MIN_VALUE) v = ((Long) v).intValue();
            array[i] = v;
        }
        return array;
    }

    private static void collect(JsonNode tree, List<Long> out) {
        Iterator<Map.Entry<String, JsonNode>> fields = tree.fields();
        while (fields.hasNext() && out.size() < 4) {
            Map.Entry<String, JsonNode> f = fields.next();
            if (f.getValue().isInt() && !f.getKey().equals("ID") && !f.getKey().equals("count")) out.add(f.getValue().asLong());
        }
    }

    /**
     * The rows (n_tup_ins + n_tup_upd) an entry of {@code t} writes, from the catalogue: a simple record its template
     * row, one row per output variable (the entities/activities its insert function allocates), its record_index row,
     * with LAYER=post one row per relation tuple and the hash's UPDATE (submission.hash=update). A composite of k:
     * k element rows, its shared outputs once and the others per element, k + 1 record_index rows, its own row, k
     * linker rows, with LAYER=post the elements' relations and k + 1 hash UPDATEs.
     */
    int expectedRows(Family.Template t, int k) {
        boolean update = post && switches.hash == SubmissionSwitches.Hash.UPDATE;
        if (!t.composite()) {
            return 2 + outputs(t.isA) + (post ? tuples(t.isA) : 0) + (update ? 1 : 0);
        }
        String e = t.element.isA;
        int shared = t.shared.size();
        return k + shared + k * (outputs(e) - shared) + (k + 1) + 1 + k
                + (post ? k * tuples(e) : 0) + (update ? k + 1 : 0);
    }

    private int outputs(String template) {
        try {
            JsonNode out = OM.readTree(dispatcher.getIoMap()).path("output").path(template);
            return out.size();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /** The relation kinds RelationMapping writes, one __PROV_* table each (the others of a template's graph are not stored). */
    static final java.util.Set<String> STORED_RELATIONS = java.util.Set.of("PROV_DERIVATION", "PROV_GENERATION", "PROV_USAGE",
            "PROV_ASSOCIATION", "PROV_DELEGATION", "PROV_SPECIALIZATION", "PROV_MEMBERSHIP");

    private int tuples(String template) {
        Map<String, Map<String, int[]>> relations = dispatcher.getRelation0().get(template);
        if (relations == null) return 0;
        int n = 0;
        for (Map.Entry<String, Map<String, int[]>> perKind : relations.entrySet()) {
            if (STORED_RELATIONS.contains(perKind.getKey())) n += perKind.getValue().size();
        }
        return n;
    }

    /** The relation writer's status (relations.write=async), else null. */
    Map<String, Object> relationStatus() {
        return relationWriter == null ? null : relationWriter.status();
    }

    /** A resource of the classpath (the webapp's classes and libraries), as the service reads its init SQL. */
    static String resource(String path) throws IOException {
        String name = path.startsWith("/") ? path.substring(1) : path;
        return IOUtils.resourceToString(name, StandardCharsets.UTF_8, DirectStore.class.getClassLoader());
    }

    /** The one row a generated completer reads while a JDBC batch is composed: zeros and nulls (the real ids come with the batch). */
    private static ResultSet placeholderRow() {
        int[] next = {0};
        return (ResultSet) Proxy.newProxyInstance(DirectStore.class.getClassLoader(), new Class<?>[]{ResultSet.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "next": return next[0]++ == 0;
                        case "getObject":
                            if (args.length == 2 && args[1] == Integer.class) return 0;
                            return null;
                        case "getInt": case "getLong": return method.getReturnType() == int.class ? (Object) 0 : (Object) 0L;
                        case "wasNull": return false;
                        case "close": return null;
                        case "isClosed": return false;
                        case "hashCode": return System.identityHashCode(proxy);
                        case "equals": return proxy == args[0];
                        default: return null;
                    }
                });
    }

    @Override
    public void close() {
        if (relationWriter != null) relationWriter.close();
        pool.close();
        try {
            setupConnection.close();
        } catch (SQLException ignored) {
            // closing anyway
        }
    }
}
