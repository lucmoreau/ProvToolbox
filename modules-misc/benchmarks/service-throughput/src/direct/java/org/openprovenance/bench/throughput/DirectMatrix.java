package org.openprovenance.bench.throughput;

import java.util.ArrayList;
import java.util.List;

/**
 * The plan of {@code make bench.direct.matrix}: the layers at one writer; concurrency up to 64 writers; records per
 * transaction and JDBC batching; the transport family; the relation writers. Named presets:
 *
 * <ul>
 *   <li>{@code insert}: the insert function and record_index, no key;</li>
 *   <li>{@code insert+key}: the same with an Idempotency-Key per record;</li>
 *   <li>{@code post}: + the service's post-processing, every switch at the service's default, no key;</li>
 *   <li>{@code post+key}: the HTTP baseline's work without the service (key, access-controlled read-back, hash, inline relations);</li>
 *   <li>{@code combined}: the HTTP combined's (no key, read-back without the join, no hash, async relations).</li>
 * </ul>
 */
final class DirectMatrix {

    static Settings preset(Settings base, String preset) {
        switch (preset) {
            case "insert": return base.with("LAYER", "insert", "KEY", "off");
            case "insert+key": return base.with("LAYER", "insert", "KEY", "on");
            case "post": return base.with("LAYER", "post", "KEY", "off", "READBACK", "on", "HASH", "update", "RELATIONS", "inline");
            case "post+key": return base.with("LAYER", "post", "KEY", "on", "READBACK", "on", "HASH", "update", "RELATIONS", "inline");
            case "combined": return base.with("LAYER", "post", "KEY", "off", "READBACK", "off", "HASH", "off", "RELATIONS", "async");
            default: throw new IllegalArgumentException("unknown preset " + preset);
        }
    }

    static List<Matrix.Step> plan(Settings base) {
        List<Matrix.Step> steps = new ArrayList<>();
        String name = base.get("PLAN");
        if (name.equals("smoke")) {
            for (String family : new String[]{"fs", "tr"}) {
                for (String p : new String[]{"insert", "post+key", "combined"}) {
                    steps.add(new Matrix.Step("smoke", preset(base, p).with("FAMILY", family, "N", "2", "B", "1")));
                }
            }
            steps.add(new Matrix.Step("smoke", preset(base, "insert").with("FAMILY", "fs", "N", "2", "B", "10", "JDBC_BATCH", "1")));
            steps.add(new Matrix.Step("smoke", preset(base, "post").with("FAMILY", "fs", "N", "2", "B", "10")));
            return steps;
        }
        if (!name.equals("full")) throw new IllegalArgumentException("PLAN is full or smoke, not " + name);
        // D1. the layers, and each switch of the post-processing alone, one writer, one record per transaction
        String phase = "D1 layers, fs, N=1 B=1";
        for (String p : new String[]{"insert", "insert+key", "post", "post+key", "combined"}) {
            steps.add(new Matrix.Step(phase, preset(base, p).with("FAMILY", "fs", "N", "1", "B", "1")));
        }
        steps.add(new Matrix.Step(phase, preset(base, "post").with("FAMILY", "fs", "N", "1", "B", "1", "READBACK", "off")));
        steps.add(new Matrix.Step(phase, preset(base, "post").with("FAMILY", "fs", "N", "1", "B", "1", "HASH", "header")));
        steps.add(new Matrix.Step(phase, preset(base, "post").with("FAMILY", "fs", "N", "1", "B", "1", "HASH", "off")));
        steps.add(new Matrix.Step(phase, preset(base, "post").with("FAMILY", "fs", "N", "1", "B", "1", "RELATIONS", "async")));
        // D2. writers: where it saturates, and on what
        for (String p : new String[]{"insert", "post+key", "combined"}) {
            for (int n : new int[]{2, 4, 8, 16, 32, 64}) {
                steps.add(new Matrix.Step("D2 writers, fs, B=1", preset(base, p).with("FAMILY", "fs", "N", Integer.toString(n), "B", "1")));
            }
        }
        // D3. records per transaction (what only the direct mode can do), and the inserts sent as one JDBC batch
        for (String p : new String[]{"insert", "post+key", "combined"}) {
            for (int b : new int[]{10, 100}) {
                steps.add(new Matrix.Step("D3 records per transaction, fs, N=16", preset(base, p).with("FAMILY", "fs", "N", "16", "B", Integer.toString(b))));
            }
        }
        for (int b : new int[]{10, 100}) {
            steps.add(new Matrix.Step("D3 records per transaction, fs, N=16", preset(base, "insert").with("FAMILY", "fs", "N", "16", "B", Integer.toString(b), "JDBC_BATCH", "1")));
        }
        // D4. the transport family
        for (String p : new String[]{"insert", "post+key", "combined"}) {
            for (int n : new int[]{1, 16}) {
                steps.add(new Matrix.Step("D4 transport family, B=1", preset(base, p).with("FAMILY", "tr", "N", Integer.toString(n), "B", "1")));
            }
        }
        // D5. the relation writers under the heaviest load (2 in D2)
        for (String w : new String[]{"1", "4"}) {
            steps.add(new Matrix.Step("D5 relation writers, fs, combined, N=32", preset(base, "combined").with("FAMILY", "fs", "N", "32", "B", "1", "WRITERS", w)));
        }
        return steps;
    }

    /** Seconds a run costs besides its warm-up and window: reset, schema and catalogue set-up (~2 s), two settles of 10.5 s, collection. */
    static final double OVERHEAD_S = 25;

    static String describe(List<Matrix.Step> steps, Settings base) {
        StringBuilder sb = new StringBuilder();
        String phase = "";
        int i = 0;
        double total = 0;
        for (Matrix.Step st : steps) {
            if (!st.phase.equals(phase)) {
                phase = st.phase;
                sb.append("\n**").append(phase).append("**\n\n");
            }
            sb.append(++i).append(". ").append(DirectRun.describe(st.settings)).append('\n');
            total += st.settings.integer("WARMUP") + st.settings.integer("DURATION") + OVERHEAD_S;
        }
        sb.append(String.format("%n%d runs x (warm-up %d s + measure %d s + reset/set-up/settle/collect ~%.0f s) = ~%.0f s = ~%.1f min%n",
                steps.size(), base.integer("WARMUP"), base.integer("DURATION"), OVERHEAD_S, total, total / 60));
        return sb.toString();
    }

    static final String[][] COLUMNS = {
            {"run", "run"}, {"family", "fam"}, {"layer", "layer"}, {"key", "key"}, {"readback", "readback"}, {"hash", "hash"}, {"relations", "relations"},
            {"n", "N"}, {"b", "B"}, {"jdbc_batch", "batch"}, {"tx_per_s", "tx/s"}, {"records_per_s", "records/s"},
            {"rows_per_record", "rows/rec"}, {"wal_bytes_per_record", "WAL B/rec"},
            {"lat_p50_ms", "p50 ms/tx"}, {"lat_p99_ms", "p99 ms/tx"}, {"lat_max_ms", "max ms/tx"},
            {"client_cpu_pct", "client CPU %"}, {"pg_cpu_pct", "PG CPU %"}, {"client_cpu_ms_per_record", "client ms/rec"}, {"pg_cpu_ms_per_record", "PG ms/rec"},
            {"busy_backends", "busy"}, {"waits_top", "top waits"}, {"lag_p95_ms", "lag p95 ms"}, {"status", "status"}};

    static final String LEGEND = "Rates are per measured second; latency is per transaction (B records); CPU % is of one core; client = the "
            + "harness JVM writing directly (no web service); rows = n_tup_ins + n_tup_upd over all tables, checked against what the catalogue "
            + "says each record writes; busy = writer backends not idle, on average; top waits = pg_stat_activity of the writers' backends, sampled "
            + "every 100 ms (CPU = active without a wait event; Client:IdleInTransaction = between a transaction's statements).";

    /** The direct matrix: every run appended to results/&lt;date&gt;-direct[-LABEL].csv, the Markdown rewritten after each. */
    static int run(Settings s) throws Exception {
        Report report = new Report(s, "direct");
        new Store(s).guard();
        List<Matrix.Step> steps = plan(s);
        String plan = describe(steps, s);
        System.out.println(plan);
        DirectMain.recordEnvironment(report);
        long t0 = System.nanoTime();
        int failed = 0;
        int i = 0;
        String stamp = Long.toString(System.currentTimeMillis() / 1000);
        for (Matrix.Step step : steps) {
            i++;
            String id = String.format("d%s-%02d", stamp, i);
            Run.Row row;
            try {
                row = new DirectRun(step.settings).execute(id);
            } catch (Guard.Refused e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                row = new Run.Row();
                row.put("run", id);
                row.put("mode", "direct");
                row.put("family", step.settings.get("FAMILY"));
                row.put("layer", step.settings.get("LAYER"));
                row.put("n", step.settings.get("N"));
                row.put("b", step.settings.get("B"));
                row.problems.add("aborted: " + e);
                row.put("status", "FAIL");
                row.put("problems", "aborted: " + e.toString().replace('\n', ' '));
            }
            if (!row.ok()) failed++;
            DirectMain.appendAligned(report, row);
            report.writeMarkdown("Service throughput, direct JDBC mode, " + report.name, plan, COLUMNS, DirectMain.INTRO, LEGEND);
            System.out.printf("  [%d/%d done, %.1f min elapsed, %d failed]%n", i, steps.size(), (System.nanoTime() - t0) / 60e9, failed);
            if (!row.ok() && "1".equals(s.get("STOP_ON_FAIL"))) break;
        }
        System.out.printf("%ndirect matrix: %d run(s) in %.1f min, %d failed; %s%n", i, (System.nanoTime() - t0) / 60e9, failed, report.markdown());
        return failed == 0 ? 0 : 1;
    }
}
