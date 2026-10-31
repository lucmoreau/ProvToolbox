package org.openprovenance.bench.throughput;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.util.Map;

/**
 * One run of the direct JDBC mode: the guard, provbench dropped and recreated, the schema and the catalogue's enactors
 * set up in-process ({@link DirectStore}), a warm-up, a settle, the measured window (N writers, wait events sampled),
 * a settle, the collection. The figures are the HTTP mode's minus the service: per transaction instead of per message,
 * the client JVM's CPU (this one) instead of the service's, record_index as the cross-check (no activity_log without
 * the service), and rows/record checked against what the catalogue says each record writes.
 */
final class DirectRun {

    /**
     * The quiet moment before a snapshot: PostgreSQL 16 keeps an idle backend's pending table statistics up to 10 s
     * (PGSTAT_IDLE_INTERVAL) when it flushed less than a second before, so only after 10 s are a phase's rows all in
     * pg_stat_user_tables — the exact rows/record check needs them all (the HTTP mode's 2.5 s leaves up to a second of
     * each backend's rows across the window's edges).
     */
    static final long QUIET_MS = 10_500;

    private final Settings s;
    private final Store store;
    private final DirectStore.Connector connector;

    DirectRun(Settings settings) {
        this(settings, new Store(settings), application -> Store.connect(Guard.DB_NAME, application));
    }

    DirectRun(Settings settings, Store store, DirectStore.Connector connector) {
        this.s = settings;
        this.store = store;
        this.connector = connector;
    }

    /** The run's settings in a line, as the plan and the console show them. */
    static String describe(Settings s) {
        String layer = DirectStore.layer(s);
        StringBuilder sb = new StringBuilder("FAMILY=" + s.get("FAMILY") + " LAYER=" + layer + " KEY=" + s.get("KEY"));
        if (layer.equals("post")) sb.append(" READBACK=").append(s.get("READBACK")).append(" HASH=").append(s.get("HASH")).append(" RELATIONS=").append(s.get("RELATIONS"));
        sb.append(" N=").append(s.get("N")).append(" B=").append(s.get("B"));
        if (!s.get("JDBC_BATCH").equals("0")) sb.append(" JDBC_BATCH=1");
        if (layer.equals("post") && s.get("RELATIONS").equals("async")) sb.append(" WRITERS=").append(s.get("WRITERS")).append(" BATCH=").append(s.get("BATCH"));
        sb.append(" WARMUP=").append(s.get("WARMUP")).append(" DURATION=").append(s.get("DURATION"));
        return sb.toString();
    }

    Run.Row execute(String runId) throws Exception {
        String container = store.guard();   // before anything is opened, reset or written
        if (Store.portInUse(Guard.SERVICE_PORT)) {
            throw new Guard.Refused("the HTTP service is up on " + Guard.SERVICE_PORT + " (make bench.service.down): it would write to provbench too");
        }
        int n = s.integer("N");
        int b = s.integer("B");
        String layer = DirectStore.layer(s);
        boolean post = layer.equals("post");
        boolean key = DirectStore.onOff(s, "KEY");
        DirectStore.onOff(s, "READBACK");
        Run.Row row = new Run.Row();
        row.put("run", runId);
        row.put("mode", "direct");
        row.put("family", s.family());
        row.put("layer", layer);
        row.put("key", key ? "on" : "off");
        row.put("readback", post ? s.get("READBACK") : "");
        row.put("hash", post ? s.get("HASH") : "");
        row.put("relations", post ? s.get("RELATIONS") : "");
        row.put("n", n);
        row.put("b", b);
        row.put("jdbc_batch", s.get("JDBC_BATCH"));
        boolean async = post && s.get("RELATIONS").equals("async");
        row.put("writers", async ? s.integer("WRITERS") : 0);
        row.put("batch", async ? s.integer("BATCH") : 0);
        row.put("warmup_s", s.integer("WARMUP"));
        row.put("duration_s", s.integer("DURATION"));
        System.out.println("\n=== direct run " + runId + ": " + describe(s));

        store.reset();
        Family family = Family.named(s.family(), s.integer("COMPOSITE_SIZE"));
        try (DirectStore ds = new DirectStore(s, family, n, connector);
             Connection c = Store.connect(Guard.DB_NAME); Connection probe = Store.connect(Guard.DB_NAME);
             Connection sampling = Store.connect(Guard.DB_NAME)) {
            row.put("setup_s", Run.round(ds.setupSeconds, 1));
            System.out.printf("  schema and catalogue set up in %.1f s; %s%n", ds.setupSeconds, ds.switches);
            DirectLoad load = new DirectLoad(ds, family, n, b);
            System.out.printf("  warm-up %d s ...%n", s.integer("WARMUP"));
            DirectLoad.Outcome warm = load.run(s.integer("WARMUP"), null);
            if (warm.o.failures > 0) row.problems.add("warm-up: " + warm.o.failures + " failed transaction(s): " + warm.o.failureSamples);
            settle(ds);
            Store.Snapshot before = store.snapshot(c, container);
            long cpuBefore = processCpuNanos();
            Map<String, Object> relBefore = ds.relationStatus();
            Store.resetStatements(c);

            System.out.printf("  measure %d s, N=%d B=%d ...%n", s.integer("DURATION"), n, b);
            DirectLoad.Outcome out;
            WaitSampler waits = new WaitSampler(sampling);
            try {
                out = load.run(s.integer("DURATION"), async ? probe : null);
            } finally {
                waits.close();
            }
            Load.Outcome m = out.o;
            double drain = settle(ds);
            long cpuAfter = processCpuNanos();
            Store.Snapshot after = store.snapshot(c, container);
            Map<String, Object> rel = ds.relationStatus();
            row.topStatements.addAll(Store.topStatements(c, 8));
            double d = m.seconds;

            long rows = after.rowsWritten - before.rowsWritten;
            long wal = after.walBytes - before.walBytes;
            double clientCpu = (cpuAfter - cpuBefore) / 1e9;
            double pgCpu = before.cpuMicros < 0 || after.cpuMicros < 0 ? Double.NaN : (after.cpuMicros - before.cpuMicros) / 1e6;
            row.put("transactions", m.messages);
            row.put("records", m.records);
            row.put("tx_per_s", Run.round(m.messages / d, 1));
            row.put("records_per_s", Run.round(m.records / d, 1));
            row.put("rows_per_s", Run.round(rows / d, 0));
            row.put("rows_per_record", m.records == 0 ? "" : Run.round((double) rows / m.records, 2));
            row.put("rows_expected_per_record", m.records == 0 ? "" : Run.round((double) out.expectedRows / m.records, 2));
            row.put("wal_kb_per_s", Run.round(wal / 1024.0 / d, 0));
            row.put("wal_bytes_per_record", m.records == 0 ? "" : Run.round((double) wal / m.records, 0));
            row.put("lat_p50_ms", Run.round(m.percentileMillis(50), 2));
            row.put("lat_p95_ms", Run.round(m.percentileMillis(95), 2));
            row.put("lat_p99_ms", Run.round(m.percentileMillis(99), 2));
            row.put("lat_max_ms", Run.round(m.percentileMillis(100), 2));
            row.put("client_cpu_pct", Run.round(100 * clientCpu / d, 0));
            row.put("pg_cpu_pct", Run.round(100 * pgCpu / d, 0));
            row.put("client_cpu_ms_per_record", m.records == 0 ? "" : Run.round(1000 * clientCpu / m.records, 3));
            row.put("pg_cpu_ms_per_record", m.records == 0 ? "" : Run.round(1000 * pgCpu / m.records, 3));
            row.put("busy_backends", Run.round(waits.meanBusy(), 1));
            row.put("waits_top", waits.top(5));
            row.put("lag_p50_ms", Run.round(m.lagPercentileMillis(50), 1));
            row.put("lag_p95_ms", Run.round(m.lagPercentileMillis(95), 1));
            row.put("lag_max_ms", Run.round(m.lagPercentileMillis(100), 1));
            row.put("lag_samples", m.lagNanos.size());
            row.put("writer_lag_mean_ms", rel == null ? "" : Run.round(((Number) rel.get("lag_ms_mean")).doubleValue(), 1));
            row.put("writer_lag_max_ms", rel == null ? "" : Run.round(((Number) rel.get("lag_ms_max")).doubleValue(), 1));
            row.put("queue_blocked", rel == null ? "" : ((Number) rel.get("blocked")).longValue() - ((Number) relBefore.get("blocked")).longValue());
            row.put("drain_s", Run.round(drain, 2));
            row.put("failures", m.failures);

            // ---- checks
            if (m.failures > 0) row.problems.add(m.failures + " failed transaction(s): " + m.failureSamples);
            if (m.lagTimeouts > 0) row.problems.add(m.lagTimeouts + " probed record(s) whose relations never appeared");
            long dIndex = after.recordIndex - before.recordIndex;
            if (dIndex != m.records) row.problems.add("record_index +" + dIndex + " rows, the harness wrote " + m.records + " records");
            if (rows != out.expectedRows) {
                row.problems.add("rows written +" + rows + " (n_tup_ins + n_tup_upd), the catalogue expects " + out.expectedRows
                        + " for these records (" + String.format("%.2f", (double) rows / Math.max(1, m.records)) + " against "
                        + String.format("%.2f", (double) out.expectedRows / Math.max(1, m.records)) + " per record)");
            }
            if (rel != null) {
                if (((Number) rel.get("failed")).longValue() > 0) row.problems.add(rel.get("failed") + " record(s)' relations failed to be written");
                if (((Number) rel.get("depth")).longValue() != 0) row.problems.add("the relation queue was not drained: " + rel.get("depth"));
            }
            if (waits.error() != null) row.problems.add("wait sampler: " + waits.error());
            row.put("record_index_rows", dIndex);
            System.out.printf("  %.1f tx/s, %.1f records/s, p50 %.2f ms p99 %.2f ms per transaction, client CPU %.0f%%, PostgreSQL CPU %.0f%%, %.0f rows/s (%.2f/record), busy backends %.1f; waits %s%s%n",
                    m.messages / d, m.records / d, m.percentileMillis(50), m.percentileMillis(99), 100 * clientCpu / d, 100 * pgCpu / d, rows / d,
                    (double) rows / Math.max(1, m.records), waits.meanBusy(), waits.top(3),
                    m.lagNanos.isEmpty() ? "" : String.format(", relation lag p50 %.1f ms p95 %.1f ms", m.lagPercentileMillis(50), m.lagPercentileMillis(95)));
        }
        row.put("status", row.ok() ? "ok" : "FAIL");
        row.put("problems", String.join("; ", row.problems));
        if (!row.ok()) {
            System.out.println("  ************************************************************");
            System.out.println("  DIRECT RUN " + runId + " FAILED:");
            for (String p : row.problems) System.out.println("   - " + p);
            System.out.println("  ************************************************************");
        }
        return row;
    }

    /** Waits for the relation queue to drain (async) and then a quiet moment, for PostgreSQL's statistics; the drain in s. */
    private static double settle(DirectStore ds) throws InterruptedException {
        long t0 = System.nanoTime();
        long deadline = System.currentTimeMillis() + 600_000;
        while (ds.relationStatus() != null && System.currentTimeMillis() < deadline) {
            Map<String, Object> rel = ds.relationStatus();
            long queued = ((Number) rel.get("queued")).longValue();
            long settled = ((Number) rel.get("written")).longValue() + ((Number) rel.get("failed")).longValue();
            if (((Number) rel.get("depth")).longValue() == 0 && queued == settled) break;
            Thread.sleep(20);
        }
        double drain = (System.nanoTime() - t0) / 1e9;
        Thread.sleep(QUIET_MS);
        return drain;
    }

    /** This JVM's CPU time (the writers, the probe and the sampler): the client's cost. */
    static long processCpuNanos() {
        return ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuTime();
    }
}
