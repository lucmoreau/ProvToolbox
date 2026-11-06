package org.openprovenance.bench.throughput;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The cost of a backward traversal: "where did this come from?", from the end of a file-system pipeline, as the store
 * grows. No service in the measured path: the store's own {@code backwardtraversal_star_typed} over plain JDBC, the
 * statement {@code TemplateQuery.recursiveTraversal} issues, with its access-control join, derivations only (the
 * navigator's default), the bounds lifted (the navigator's 100 hops and 2,000 nodes would cut the deep pipelines).
 *
 * <p>The scenario: pipelines of file-system records, init, filtering, k transformings, splitting, training on one part,
 * validating on the other, approving. A query starts from an approval and walks back to the init. The service is started
 * once per pass, to create the schema, the traversal functions and their indexes as a deployment has them, then stopped;
 * the records are written with the generated insert functions, each with its {@code record_index} row. Not its hash, nor
 * its {@code __PROV_*} rows: the traversal reads neither. Target pipelines of each depth, then other pipelines
 * of k=3 up to each store size; a pass is one store, grown from one size to the next, built anew each pass.</p>
 *
 * <p>Checks (FAIL, loudly): every query returns exactly the connections the pipeline has ({@link #connectionsOf}), never
 * truncated; {@code record_index} holds exactly the records written.</p>
 */
final class TraversalRun {
    /** {@code StatementOrBundle.Kind.PROV_DERIVATION.ordinal()}. */
    static final int DERIVATION = 9;
    static final String PRINCIPAL = "nil";
    static final int TARGETS_PER_DEPTH = 5;
    static final int BACKGROUND_K = 3;
    static final int LOAD_THREADS = 8;

    /** Records in a pipeline of k transformings: init, filtering, k, splitting, training, validating, approving. */
    static int recordsOf(int k) {
        return k + 6;
    }

    /**
     * The derivations a walk from the approval returns: to training and to validating, each to splitting, splitting to
     * the last transforming, k among the transformings and to filtering, filtering to init.
     */
    static long connectionsOf(int k) {
        return k + 6;
    }

    /** The statement of {@code TemplateQuery.recursiveTraversal}, bounds lifted. */
    static String query(int approvingId, String principal) {
        return "SELECT DISTINCT * FROM backwardtraversal_star_typed(" + approvingId + ", 'file_approving', 'approved_pipeline', ARRAY["
                + DERIVATION + "], NULL, NULL) as template_connection"
                + "\n LEFT JOIN record_index ON record_index.key=template_connection.in_id"
                + "\n AND record_index.table_name=template_connection.in_template"
                + "\n AND record_index.principal IS NOT NULL"
                + "\n LEFT JOIN access_control\n ON access_control.record=record_index.id"
                + "\n AND access_control.authorized='" + principal + "'"
                + "\n AND (record_index.principal='" + principal + "' OR access_control.record IS NOT NULL)";
    }

    static List<Integer> ints(String csv) {
        List<Integer> r = new ArrayList<>();
        for (String v : csv.split(",")) r.add(Integer.parseInt(v.trim()));
        return r;
    }

    /** One connection's writer of pipelines. */
    private static final class Writer implements AutoCloseable {
        final Connection c;
        final PreparedStatement init, filtering, transforming, splitting, training, validating, approving, index;
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        long written;

        Writer() throws SQLException {
            c = Store.connect(Guard.DB_NAME, "provbench-traversal-load");
            c.setAutoCommit(false);
            init = c.prepareStatement("SELECT * FROM insert_file_init(?::timestamptz, ?, ?)");
            filtering = c.prepareStatement("SELECT * FROM insert_file_filtering(?, ?, 1, 1, 100, 10, ?, ?::timestamptz, ?::timestamptz, ?::timestamptz)");
            transforming = c.prepareStatement("SELECT * FROM insert_file_transforming(?, ?, 1, 1, ?, ?::timestamptz, ?::timestamptz, ?::timestamptz)");
            splitting = c.prepareStatement("SELECT * FROM insert_file_splitting(?, ?, ?, 1, 1, ?, ?, ?::timestamptz, ?::timestamptz, ?::timestamptz)");
            training = c.prepareStatement("SELECT * FROM insert_file_training(?, ?, 1, 1, ?, ?::timestamptz, ?::timestamptz, ?::timestamptz)");
            validating = c.prepareStatement("SELECT * FROM insert_file_validating(?, 1, 1, ?, 0.9, ?::timestamptz, ?::timestamptz, ?::timestamptz)");
            approving = c.prepareStatement("SELECT * FROM insert_file_approving(?, ?, ?, 1, 1, ?, ?, ?::timestamptz, ?::timestamptz, ?::timestamptz)");
            index = c.prepareStatement("INSERT INTO record_index(key, table_name, principal) VALUES (?, ?, '" + PRINCIPAL + "')");
        }

        private void times(PreparedStatement p, int from) throws SQLException {
            for (int i = 0; i < 3; i++) p.setTimestamp(from + i, now);
        }

        /** The statement's single row: the record's id, then its outputs; its record_index row batched. */
        private int[] run(PreparedStatement p, String table, int... columns) throws SQLException {
            try (ResultSet rs = p.executeQuery()) {
                if (!rs.next()) throw new SQLException("no row from insert into " + table);
                int[] out = new int[columns.length + 1];
                out[0] = rs.getInt(1);
                for (int i = 0; i < columns.length; i++) out[i + 1] = rs.getInt(columns[i]);
                index.setInt(1, out[0]);
                index.setString(2, table);
                index.addBatch();
                written++;
                return out;
            }
        }

        /** A pipeline of k transformings, committed; the approving record's id. */
        int pipeline(int k, long unique) throws SQLException {
            String n = "f" + unique;
            init.setTimestamp(1, now);
            init.setString(2, "csv");
            init.setString(3, "/in/" + n);
            int file = run(init, "file_init", 2)[1];
            filtering.setString(1, n);
            filtering.setInt(2, file);
            filtering.setString(3, "/f/" + n);
            times(filtering, 4);
            file = run(filtering, "file_filtering", 2)[1];
            for (int i = 0; i < k; i++) {
                transforming.setString(1, n);
                transforming.setInt(2, file);
                transforming.setString(3, "/t/" + n);
                times(transforming, 4);
                file = run(transforming, "file_transforming", 2)[1];
            }
            splitting.setString(1, n + "a");
            splitting.setString(2, n + "b");
            splitting.setInt(3, file);
            splitting.setString(4, "/a/" + n);
            splitting.setString(5, "/b/" + n);
            times(splitting, 6);
            int[] split = run(splitting, "file_splitting", 2, 3);
            training.setString(1, n);
            training.setInt(2, split[1]);
            training.setString(3, "/m/" + n);
            times(training, 4);
            int model = run(training, "file_training", 2)[1];
            validating.setInt(1, split[2]);
            validating.setString(2, "/v/" + n);
            times(validating, 3);
            int score = run(validating, "file_validating", 2)[1];
            approving.setString(1, n);
            approving.setInt(2, model);
            approving.setInt(3, score);
            approving.setString(4, "sig");
            approving.setString(5, "/ap/" + n);
            times(approving, 6);
            int id = run(approving, "file_approving")[0];
            index.executeBatch();
            c.commit();
            return id;
        }

        @Override
        public void close() throws SQLException {
            c.close();
        }
    }

    private final Settings s;
    private final Store store;
    private final AtomicLong unique = new AtomicLong();
    private long records;

    TraversalRun(Settings s) {
        this.s = s;
        this.store = new Store(s);
    }

    /** Other pipelines, on several connections, up to {@code size} records in all. */
    private void growTo(int size) throws Exception {
        int pipelines = Math.max(0, (int) ((size - records) / recordsOf(BACKGROUND_K)));
        ExecutorService pool = Executors.newFixedThreadPool(LOAD_THREADS);
        List<Future<Long>> done = new ArrayList<>();
        for (int t = 0; t < LOAD_THREADS; t++) {
            int share = pipelines / LOAD_THREADS + (t < pipelines % LOAD_THREADS ? 1 : 0);
            done.add(pool.submit(() -> {
                try (Writer w = new Writer()) {
                    for (int i = 0; i < share; i++) w.pipeline(BACKGROUND_K, unique.incrementAndGet());
                    return w.written;
                }
            }));
        }
        for (Future<Long> f : done) records += f.get();
        pool.shutdown();
        try (Connection c = Store.connect(Guard.DB_NAME); Statement st = c.createStatement()) {
            st.execute("CHECKPOINT");
            st.execute("ANALYZE");
        }
    }

    private static double percentile(List<Double> sorted, double p) {
        return sorted.get(Math.min(sorted.size() - 1, (int) Math.ceil(p * sorted.size()) - 1));
    }

    /** One cell: {@code readers} connections querying the targets of a depth in turn, for a warm-up then a window. */
    private Run.Row measure(String runId, int pass, int size, int depth, int readers, List<Integer> targets, String container) throws Exception {
        Run.Row row = new Run.Row();
        row.put("run", runId);
        row.put("pass", pass);
        row.put("store_records", records);
        row.put("size", size);
        row.put("k", depth);
        row.put("readers", readers);
        long warmup = s.integer("TR_WARMUP") * 1_000_000_000L;
        long window = s.integer("TR_DURATION") * 1_000_000_000L;
        long start = System.nanoTime();
        long windowStart = start + warmup;
        long end = windowStart + window;
        ExecutorService pool = Executors.newFixedThreadPool(readers);
        List<Future<Reader>> futures = new ArrayList<>();
        for (int r = 0; r < readers; r++) {
            int offset = r;
            futures.add(pool.submit(() -> new Reader().run(targets, offset, windowStart, end)));
        }
        long cpuStart = -1;
        while (System.nanoTime() < windowStart) Store.sleep(10);
        cpuStart = Store.containerCpuMicros(container);
        while (System.nanoTime() < end) Store.sleep(10);
        long cpuEnd = Store.containerCpuMicros(container);
        List<Double> ms = new ArrayList<>();
        long minRows = Long.MAX_VALUE, maxRows = -1, truncated = 0, rowsTotal = 0;
        for (Future<Reader> f : futures) {
            Reader r = f.get();
            ms.addAll(r.ms);
            minRows = Math.min(minRows, r.minRows);
            maxRows = Math.max(maxRows, r.maxRows);
            truncated += r.truncated;
            rowsTotal += r.rows;
        }
        double elapsed = (System.nanoTime() - windowStart) / 1e9;
        pool.shutdown();
        Collections.sort(ms);
        if (ms.isEmpty()) {
            row.problems.add("no query finished in the window");
            ms.add(0d);
        }
        int queries = ms.size();
        row.put("queries", queries);
        row.put("queries_per_s", String.format("%.2f", queries / elapsed));
        row.put("connections_per_query", maxRows);
        row.put("lat_p50_ms", String.format("%.3f", percentile(ms, 0.50)));
        row.put("lat_p95_ms", String.format("%.3f", percentile(ms, 0.95)));
        row.put("lat_p99_ms", String.format("%.3f", percentile(ms, 0.99)));
        row.put("lat_max_ms", String.format("%.3f", ms.get(ms.size() - 1)));
        row.put("us_per_connection", String.format("%.1f", maxRows > 0 ? 1000 * percentile(ms, 0.50) / maxRows : 0));
        row.put("pg_cpu_pct", cpuStart >= 0 && cpuEnd >= 0 ? String.format("%.0f", (cpuEnd - cpuStart) / 1e4 / elapsed) : "");
        if (minRows != maxRows) row.problems.add("connections per query range from " + minRows + " to " + maxRows);
        if (maxRows != connectionsOf(depth)) row.problems.add(maxRows + " connections per query, " + connectionsOf(depth) + " expected");
        if (truncated > 0) row.problems.add(truncated + " truncated result(s)");
        if (rowsTotal == 0) row.problems.add("no connection returned");
        row.put("status", row.ok() ? "ok" : "FAIL");
        row.put("problems", String.join("; ", row.problems));
        System.out.printf("  pass %d, %d records, k=%d, %d reader(s): %s q/s, p50 %s ms, p99 %s ms, %d connections%s%n", pass, records, depth, readers,
                row.values.get("queries_per_s"), row.values.get("lat_p50_ms"), row.values.get("lat_p99_ms"), maxRows, row.ok() ? "" : "  FAIL: " + row.problems);
        return row;
    }

    /** A reader: a connection, the service's statement, every column read as the service reads them. */
    private static final class Reader {
        final List<Double> ms = new ArrayList<>();
        long minRows = Long.MAX_VALUE, maxRows = -1, truncated, rows;

        Reader run(List<Integer> targets, int offset, long windowStart, long end) throws SQLException {
            try (Connection c = Store.connect(Guard.DB_NAME, "provbench-traversal")) {
                int i = offset;
                while (System.nanoTime() < end) {
                    String sql = query(targets.get(i++ % targets.size()), PRINCIPAL);
                    long t0 = System.nanoTime();
                    long n = 0, cut = 0;
                    try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            rs.getObject("in_id");
                            rs.getObject("in_template");
                            rs.getObject("in_property");
                            rs.getObject("in_type");
                            rs.getObject("out_id");
                            rs.getObject("out_template");
                            rs.getObject("out_property");
                            rs.getObject("out_type");
                            if (rs.getBoolean("truncated")) cut++;
                            n++;
                        }
                    }
                    long t1 = System.nanoTime();
                    if (t0 >= windowStart && t1 <= end) {
                        ms.add((t1 - t0) / 1e6);
                        minRows = Math.min(minRows, n);
                        maxRows = Math.max(maxRows, n);
                        rows += n;
                        if (cut > 0) truncated++;
                    }
                }
            }
            return this;
        }
    }

    static String describePlan(Settings s) {
        List<Integer> sizes = ints(s.get("TR_SIZES")), depths = ints(s.get("TR_DEPTHS")), readers = ints(s.get("TR_READERS"));
        int cells = sizes.size() * depths.size() * readers.size();
        int passes = s.integer("TR_PASSES");
        int seconds = s.integer("TR_WARMUP") + s.integer("TR_DURATION") + 1;
        return String.format("%d passes x %d cells (sizes %s, k %s, readers %s) x %d s = ~%.0f min, plus building a store per pass",
                passes, cells, sizes, depths, readers, seconds, passes * cells * seconds / 60.0);
    }

    /** The plan: one store per pass, grown through the sizes, every cell measured at each. */
    int matrix() throws Exception {
        Report report = new Report(s, "traversal");
        String container = store.guard();
        System.out.println(describePlan(s));
        report.recordEnvironment();
        List<Integer> sizes = ints(s.get("TR_SIZES")), depths = ints(s.get("TR_DEPTHS")), readers = ints(s.get("TR_READERS"));
        int passes = s.integer("TR_PASSES");
        Service service = new Service(s);
        int failed = 0, cell = 0;
        String stamp = Long.toString(System.currentTimeMillis() / 1000);
        for (int pass = 1; pass <= passes; pass++) {
            service.stop();
            store.reset();
            service.start();
            service.stop();
            records = 0;
            long t0 = System.nanoTime();
            Map<Integer, List<Integer>> targets = new LinkedHashMap<>();
            try (Writer w = new Writer()) {
                for (int depth : depths) {
                    List<Integer> ids = new ArrayList<>();
                    for (int i = 0; i < TARGETS_PER_DEPTH; i++) ids.add(w.pipeline(depth, unique.incrementAndGet()));
                    targets.put(depth, ids);
                }
                records += w.written;
            }
            for (int size : sizes) {
                growTo(size);
                try (Connection c = Store.connect(Guard.DB_NAME)) {
                    long counted = Long.parseLong(Store.scalar(c, "SELECT count(*) FROM record_index"));
                    if (counted != records) throw new IllegalStateException("record_index holds " + counted + " records, " + records + " were written");
                }
                System.out.printf("pass %d: store of %d records, %.0f s since the pass began%n", pass, records, (System.nanoTime() - t0) / 1e9);
                for (int depth : depths) {
                    for (int r : readers) {
                        Run.Row row = measure(String.format("t%s-%03d", stamp, ++cell), pass, size, depth, r, targets.get(depth), container);
                        if (!row.ok()) failed++;
                        report.append(row);
                    }
                }
            }
        }
        service.stop();
        System.out.printf("traversal matrix: %d cell(s), %d failed; %s%n", cell, failed, report.csv());
        return failed == 0 ? 0 : 1;
    }

    /** Whether the service's traversal statement hides a record from a principal with no access to it. */
    int accessCheck() throws Exception {
        store.guard();
        Service service = new Service(s);
        service.stop();
        store.reset();
        service.start();
        service.stop();
        int id;
        try (Writer w = new Writer()) {
            id = w.pipeline(2, unique.incrementAndGet());
        }
        try (Connection c = Store.connect(Guard.DB_NAME); Statement st = c.createStatement()) {
            for (String principal : Arrays.asList(PRINCIPAL, "somebody-else")) {
                try (ResultSet rs = st.executeQuery(query(id, principal))) {
                    int n = 0;
                    while (rs.next()) n++;
                    System.out.println("principal " + principal + ": " + n + " connection(s)");
                }
            }
        }
        return 0;
    }
}
