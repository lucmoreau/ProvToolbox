package org.openprovenance.bench.throughput;

import com.fasterxml.jackson.databind.JsonNode;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * One run: service down, provbench dropped and recreated, the service up with the run's configuration, the variant's
 * DDL, a warm-up, a quiet settle, the measured window, a settle, the collection, the service down.
 *
 * <p>Settling: once a phase's clients have stopped, the harness waits for the relation queue to drain (async) and then
 * {@value #QUIET_MS} ms, so that the statistics PostgreSQL flushes after a transaction (at most once a second per
 * backend) are in before the snapshot. What the measured window's submissions cause after it (the async relations
 * drained) is counted with it; the rates are per measured second.</p>
 */
final class Run {

    static final long QUIET_MS = 2500;

    private final Settings s;
    private final Store store;
    private final Service service;

    Run(Settings settings) {
        this.s = settings;
        this.store = new Store(settings);
        this.service = new Service(settings);
    }

    /** A run's figures, as the CSV holds them (column → value). */
    static final class Row {
        final Map<String, Object> values = new LinkedHashMap<>();
        final List<String> problems = new ArrayList<>();
        final List<String[]> topStatements = new ArrayList<>();

        void put(String k, Object v) {
            values.put(k, v);
        }

        boolean ok() {
            return problems.isEmpty();
        }
    }

    Row execute(String runId) throws Exception {
        Row row = new Row();
        Variant v = s.variant();
        int n = s.integer("N");
        int b = s.integer("B");
        boolean key = v.idempotencyKey && b == 1;
        row.put("run", runId);
        row.put("family", s.family());
        row.put("variant", v.name);
        row.put("n", n);
        row.put("b", b);
        row.put("pool", s.integer("POOL"));
        row.put("writers", v.async ? s.integer("WRITERS") : 0);
        row.put("batch", v.async ? s.integer("BATCH") : 0);
        row.put("key", key ? "yes" : "no");
        row.put("warmup_s", s.integer("WARMUP"));
        row.put("duration_s", s.integer("DURATION"));
        System.out.println("\n=== run " + runId + ": " + s + "  (" + v.description + ")");

        String container = store.guard();
        long tRestart = System.nanoTime();
        service.stop();
        store.reset();
        store.apply(v.preStart);
        double startup = service.start();
        row.put("startup_s", round(startup, 1));
        store.apply(v.ddl);
        System.out.printf("  service up in %.1f s; %s%n", startup, service.writePathLine());

        Family family = Family.named(s.family(), s.integer("COMPOSITE_SIZE"));
        Load load = new Load(family, service.base(), n, b, key);
        try (Connection c = Store.connect(Guard.DB_NAME); Connection probe = Store.connect(Guard.DB_NAME)) {
            System.out.printf("  warm-up %d s ...%n", s.integer("WARMUP"));
            Load.Outcome warm = load.run(s.integer("WARMUP"), null);
            if (warm.failures > 0) row.problems.add("warm-up: " + warm.failures + " failed message(s): " + warm.failureSamples);
            settle(v);
            Store.Snapshot before = store.snapshot(c, container);
            long cpuBefore = service.cpuNanos();
            JsonNode statusBefore = service.status();
            Store.resetStatements(c);
            row.put("restart_to_measure_s", round((System.nanoTime() - tRestart) / 1e9, 1));

            System.out.printf("  measure %d s, N=%d B=%d ...%n", s.integer("DURATION"), n, b);
            Load.Outcome m = load.run(s.integer("DURATION"), probe);
            double drain = settle(v);
            long cpuAfter = service.cpuNanos();
            Store.Snapshot after = store.snapshot(c, container);
            JsonNode statusAfter = service.status();
            row.topStatements.addAll(Store.topStatements(c, 8));
            double d = m.seconds;

            row.put("messages", m.messages);
            row.put("entries", m.entries);
            row.put("records", m.records);
            row.put("msg_per_s", round(m.messages / d, 1));
            row.put("records_per_s", round(m.records / d, 1));
            long rows = after.rowsWritten - before.rowsWritten;
            long wal = after.walBytes - before.walBytes;
            row.put("rows_per_s", round(rows / d, 0));
            row.put("rows_per_record", m.records == 0 ? "" : round((double) rows / m.records, 2));
            row.put("wal_kb_per_s", round(wal / 1024.0 / d, 0));
            row.put("wal_bytes_per_record", m.records == 0 ? "" : round((double) wal / m.records, 0));
            row.put("lat_p50_ms", round(m.percentileMillis(50), 2));
            row.put("lat_p95_ms", round(m.percentileMillis(95), 2));
            row.put("lat_p99_ms", round(m.percentileMillis(99), 2));
            row.put("lat_max_ms", round(m.percentileMillis(100), 2));
            double svcCpu = cpuBefore < 0 || cpuAfter < 0 ? Double.NaN : (cpuAfter - cpuBefore) / 1e9;
            double pgCpu = before.cpuMicros < 0 || after.cpuMicros < 0 ? Double.NaN : (after.cpuMicros - before.cpuMicros) / 1e6;
            row.put("svc_cpu_pct", round(100 * svcCpu / d, 0));
            row.put("pg_cpu_pct", round(100 * pgCpu / d, 0));
            row.put("svc_cpu_ms_per_record", m.records == 0 ? "" : round(1000 * svcCpu / m.records, 3));
            row.put("pg_cpu_ms_per_record", m.records == 0 ? "" : round(1000 * pgCpu / m.records, 3));
            row.put("lag_p50_ms", round(m.lagPercentileMillis(50), 1));
            row.put("lag_p95_ms", round(m.lagPercentileMillis(95), 1));
            row.put("lag_max_ms", round(m.lagPercentileMillis(100), 1));
            row.put("lag_samples", m.lagNanos.size());
            JsonNode rel = statusAfter.path("relations");
            row.put("svc_lag_mean_ms", rel.isMissingNode() || rel.isNull() ? "" : round(rel.path("lag_ms_mean").asDouble(), 1));
            row.put("svc_lag_max_ms", rel.isMissingNode() || rel.isNull() ? "" : round(rel.path("lag_ms_max").asDouble(), 1));
            row.put("queue_blocked", rel.isMissingNode() || rel.isNull() ? "" : rel.path("blocked").asLong() - statusBefore.path("relations").path("blocked").asLong());
            row.put("drain_s", round(drain, 2));
            row.put("failures", m.failures);

            // ---- checks: a run whose counts do not add up is not a result
            if (m.failures > 0) row.problems.add(m.failures + " failed message(s): " + m.failureSamples);
            if (m.lagTimeouts > 0) row.problems.add(m.lagTimeouts + " probed record(s) whose relations never appeared");
            long dStatements = delta(after, before, "ps.statements");
            long dRecords = delta(after, before, "ps.records");
            long dFailed = delta(after, before, "ps.statements.failed");
            if (dStatements != m.messages) row.problems.add("activity_log ps.statements +" + dStatements + ", the harness counted " + m.messages + " messages");
            if (dRecords != m.entries) row.problems.add("activity_log ps.records +" + dRecords + ", the harness counted " + m.entries + " entries");
            if (dFailed != 0) row.problems.add("activity_log ps.statements.failed +" + dFailed);
            long dIndex = after.recordIndex - before.recordIndex;
            if (dIndex != m.records) row.problems.add("record_index +" + dIndex + " rows, the harness expected " + m.records + " records");
            long expectedHeaders = v.hashHeader() ? m.messages : 0;
            if (m.hashHeaders != expectedHeaders) row.problems.add(m.hashHeaders + " PROV-Hash header(s) for " + m.messages + " message(s), expected " + expectedHeaders);
            if (v.async) {
                long failed = rel.path("failed").asLong();
                if (failed > 0) row.problems.add(failed + " record(s)' relations failed to be written by the relation writers");
                if (rel.path("depth").asLong() != 0) row.problems.add("the relation queue was not drained: " + rel.path("depth").asLong());
            }
            row.put("activity_statements", dStatements);
            row.put("activity_records", dRecords);
            row.put("record_index_rows", dIndex);
            System.out.printf("  %.1f msg/s, %.1f records/s, p50 %.1f ms p99 %.1f ms, service CPU %.0f%%, PostgreSQL CPU %.0f%%, %.0f rows/s%s%n",
                    m.messages / d, m.records / d, m.percentileMillis(50), m.percentileMillis(99), 100 * svcCpu / d, 100 * pgCpu / d, rows / d,
                    m.lagNanos.isEmpty() ? "" : String.format(", relation lag p50 %.1f ms p95 %.1f ms", m.lagPercentileMillis(50), m.lagPercentileMillis(95)));
        } finally {
            double stop = service.stop();
            row.put("stop_s", round(stop, 1));
        }
        row.put("status", row.ok() ? "ok" : "FAIL");
        row.put("problems", String.join("; ", row.problems));
        if (!row.ok()) {
            System.out.println("  ************************************************************");
            System.out.println("  RUN " + runId + " FAILED:");
            for (String p : row.problems) System.out.println("   - " + p);
            System.out.println("  ************************************************************");
        }
        return row;
    }

    /** Waits for the relation queue to drain (async) and then a quiet moment; the drain's duration, in s. */
    private double settle(Variant v) throws InterruptedException {
        long t0 = System.nanoTime();
        if (v.async) {
            long deadline = System.currentTimeMillis() + 600_000;
            while (System.currentTimeMillis() < deadline) {
                JsonNode rel = service.status().path("relations");
                if (rel.path("depth").asLong() == 0 && rel.path("queued").asLong() == rel.path("written").asLong() + rel.path("failed").asLong()) break;
                Thread.sleep(20);
            }
        }
        double drain = (System.nanoTime() - t0) / 1e9;
        Thread.sleep(QUIET_MS);
        try {
            service.flushActivity();
        } catch (java.io.IOException e) {
            throw new IllegalStateException("cannot flush the service's activity counters: " + e.getMessage(), e);
        }
        return drain;
    }

    private static long delta(Store.Snapshot after, Store.Snapshot before, String kind) {
        return after.activity.getOrDefault(kind, 0L) - before.activity.getOrDefault(kind, 0L);
    }

    static Object round(double x, int digits) {
        if (Double.isNaN(x) || Double.isInfinite(x)) return "";
        if (digits == 0) return Math.round(x);
        double f = Math.pow(10, digits);
        return Math.round(x * f) / f;
    }
}
